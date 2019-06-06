package com.fmall.bana.utils.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.enums.guji.EGujiAuthStatus;
import com.caimao.bana.api.exception.CustomerException;
import com.caimao.bana.api.service.guji.IGujiService;
import com.fmall.bana.utils.RedisUtils;
import com.fmall.bana.utils.weixin.aes.SHA1;
import com.fmall.bana.utils.weixin.utils.HttpHelper;
import com.fmall.bana.utils.weixin.utils.WXMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信的基础服务类
 * Created by Administrator on 2016/1/5.
 */
@Component
public class WXBaseService {

    private static final Logger logger = LoggerFactory.getLogger(WXBaseService.class);

    @Value("${wx.token}")
    private String token;
    @Value("${wx.encodingAESKey}")
    private String encodingAESKey;
    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.appSecret}")
    private String appSecret;
    @Value("${wx.domain}")
    private String domain;
    @Value("${picturePath}")
    private String savePicPath;


    // 一些基本的设置
    /** 记录用户当前对话框中进行的过程 */
    public static String redisCurrentProcKey = "wx_current_process_";
    /** 用户当前过程的超时时间 */
    public static Long redisCurrentExpires = 1800L;
    /** Redis缓存 openId 用户信息的时间 */
    public static Long redisCacheUserInfoExpires = 1800L;
    /** 更新数据库中微信用户的基本信息时间（单位：小时） */
    public static Long updateDbWeixinUserInfoHours = 24L;

    /** web授权作用域 */
    public static String scopeBase = "snsapi_base";
    public static String scopeUser = "snsapi_userinfo";

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private IGujiService gujiService;
    @Resource
    private HttpServletRequest request;

    public String getToken() {
        return token;
    }

    public String getDomain() {
        return domain;
    }


    /**
     * 创建授权的跳转链接
     * @param redirectUrl
     * @param scope
     * @param state
     * @return
     */
    public String createdAuthUrl(String redirectUrl, String scope, String state) throws Exception {
        return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect",
                this.appId,
                URLEncoder.encode(redirectUrl, "UTF-8"),
                scope,
                state);
    }

    /**
     * 获取accessToken
     * @return
     * @throws Exception
     */
    public String getAccessToken() throws Exception {
        Object accessTokenObj = this.redisUtils.get("wx_access_token");
        if (accessTokenObj != null) {
            logger.info("获取accessToken，redis直接返回 ：{}", accessTokenObj.toString());
            return accessTokenObj.toString();
        }
        String accessToken = null;
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", this.appId, this.appSecret);
        String httpRes = HttpHelper.doGet(url);
        logger.info("获取accessToken，http返回json：{}", httpRes);
        Map<String, String> resMap = WXMessageUtil.parseJson(httpRes);
        logger.info("json结息返回的map值：{}", resMap);
        if (resMap == null) {
            throw new Exception("获取accessToken错误");
        }
        accessToken = resMap.get("access_token");
        Long expires = Long.valueOf(resMap.get("expires_in"));
        this.redisUtils.set("wx_access_token", accessToken, expires - 200);
        return accessToken;
    }

    /**
     * 获取JSAPI TICKET 那些信息
     * @return
     * @throws Exception
     */
    public Map<String, Object> getJsApiTicket(String reqUrl) throws Exception {
        Object jsApiTicket = this.redisUtils.get("wx_js_api_ticket");
        if (jsApiTicket == null) {
            // 重新获取ticket
            String apiUrl = String.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi", this.getAccessToken());
            String httpRes = HttpHelper.doGet(apiUrl);
            logger.info("获取JSAPI Ticket http返回信息：{}", httpRes);
            Map<String, String> resMap = WXMessageUtil.parseJson(httpRes);
            if (resMap == null) {
                throw new CustomerException("获取jsApiTicket失败", 888888);
            }
            jsApiTicket = resMap.get("ticket");
            Long expires = Long.valueOf(resMap.get("expires_in"));
            this.redisUtils.set("wx_js_api_ticket", jsApiTicket.toString(), expires - 200);
        }
        String noncestr = UUID.randomUUID().toString();;
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        //String reqUrl = this.request.getRequestURL().toString();
        if (reqUrl.contains("#")) {
            reqUrl = reqUrl.substring(0, reqUrl.indexOf("#"));
        }
        String sha1Str = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", jsApiTicket.toString(), noncestr, timestamp, reqUrl);
        logger.info("JSSDK signature STR : {}", sha1Str);
        String signature = SHA1.getSHA1(sha1Str);

        Map<String, Object> ticketInfo = new TreeMap<>();
        ticketInfo.put("appId", this.appId);
        ticketInfo.put("timestamp", timestamp);
        ticketInfo.put("nonceStr", noncestr);
        ticketInfo.put("signature", signature);

        logger.info("JSTicket 返回的信息 ：{}", ticketInfo);

        return ticketInfo;
    }

    /**
     * 网页授权的accessToken获取
     * @param code
     * @return
     * @throws Exception
     */
    public Map<String, String> getWebAccessToken(String code) throws Exception {
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", this.appId, this.appSecret, code);
        String httpRes = HttpHelper.doGet(url);
        logger.info("根据code获取accessToken返回值：{}", httpRes);
        return WXMessageUtil.parseJson(httpRes);
    }

    /**
     * 获取用户信息
     * @param openId
     * @return
     */
    public Map<String, String> getUserInfoByOpenId(String openId) throws Exception {
        // redis进行缓存
        Object httpRes = this.redisUtils.get(openId);
        if (httpRes == null) {
            String url = String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN", this.getAccessToken(), openId);
            httpRes = HttpHelper.doGet(url);
            try {
                // 验证返回值是否正确，错误的话会抛出异常，正确的话，加到redis cache中
                WXMessageUtil.verifiedResults(httpRes.toString());
                this.redisUtils.set(openId, JSON.toJSONString(httpRes.toString(), SerializerFeature.BrowserCompatible), WXBaseService.redisCacheUserInfoExpires);
            } catch (Exception ignored) {}
        } else {
            httpRes = JSON.parseObject(httpRes.toString(), String.class);
        }
        logger.info("根据openId获取userInfo返回值：{}", httpRes.toString());
        return WXMessageUtil.parseJson(httpRes.toString());
    }

    /**
     * 获取web用户信息
     * @param accessToken
     * @param openId
     * @return
     */
    public Map<String, String> getWebUserInfoByOpenId(String accessToken, String openId) throws Exception {
        // redis进行缓存
        Object httpRes = this.redisUtils.get(openId);
        if (httpRes == null) {
            String url = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN", accessToken, openId);
            httpRes = HttpHelper.doGet(url);
            try {
                // 验证返回值是否正确，错误的话会抛出异常，正确的话，加到redis cache中
                WXMessageUtil.verifiedResults(httpRes.toString());
                this.redisUtils.set(openId, JSON.toJSONString(httpRes.toString(), SerializerFeature.BrowserCompatible), WXBaseService.redisCacheUserInfoExpires);
            } catch (Exception ignored) {}
        } else {
            httpRes = JSON.parseObject(httpRes.toString(), String.class);
        }

        logger.info("根据web openId获取userInfo返回值：{}", httpRes.toString());
        return WXMessageUtil.parseJson(httpRes.toString());
    }

    /**
     * 发送模板消息
     * @param toOpenId      给谁发
     * @param type          模板类型
     * @param jumpUrl       点击后的地址
     * @param params        参数
     * @return      成功返回 true，失败返回false
     */
    public boolean sendTemplateMsg(String toOpenId, String type, String jumpUrl, Map<String, Object> params) throws Exception {
        // 定义类型与实际的模板id的关连关系
        Map<String, String> templatesMap = new HashMap<>();
        templatesMap.put("", "");
        if (templatesMap.get(type) == null) {
            return false;
        }
        String accessToken = this.getAccessToken();
        String apiUrl = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", accessToken);
        Map<String, Object> reqMap = new TreeMap<>();
        reqMap.put("touser", toOpenId);
        reqMap.put("template_id", templatesMap.get(type));
        reqMap.put("url", jumpUrl);
        reqMap.put("data", params);
        String reqJson = WXMessageUtil.mapToJson(reqMap);
        String httpRes = HttpHelper.doPostSSL(apiUrl, reqJson);
        return WXMessageUtil.verifiedResults(httpRes);
    }

    /**
     * 根据网页获取的webUserInfo 信息，查找数据库中用户，没有则注册
     * @param wxUserInfo
     */
    public void registerGujiUser(Map<String, String> wxUserInfo) throws Exception {
        String openId = wxUserInfo.get("openid");
        if (openId == null)  return;
        GujiUserEntity gujiUserEntity = this.gujiService.getUserByOpenId(openId);
        if (gujiUserEntity == null) {
            // 将关注用户添加到自己的用户表中
            GujiUserEntity userEntity = new GujiUserEntity();
            userEntity.setOpenId(wxUserInfo.get("openid"));
            userEntity.setNickname(wxUserInfo.get("nickname"));
            userEntity.setHeadimgurl(wxUserInfo.get("headimgurl"));
            userEntity.setSubscribe(0);
            userEntity.setPublicRecom(1);
            userEntity.setCertificationAuth("");
            userEntity.setCardPic("");
            userEntity.setAuthStatus(EGujiAuthStatus.NO.getCode());
            this.gujiService.addOrUpdateUserInfo(userEntity);
        } else {
            this.updateWeixinUserInfo(gujiUserEntity);
        }
    }

    /**
     * 更新数据库中用户信息（昵称、头像），如果最后更新时间大于24小时的，更新一下
     * @param userEntity
     */
    public void updateWeixinUserInfo(GujiUserEntity userEntity) {
        try {
            if (userEntity == null) return;
            if (new Date().getTime() - userEntity.getUpdateTime().getTime() >= (WXBaseService.updateDbWeixinUserInfoHours * 60 * 60 * 1000)) {
                Map<String, String> wxMap = this.getUserInfoByOpenId(userEntity.getOpenId());
                if (wxMap == null) {
                    return;
                }
                GujiUserEntity updateUserEntity = new GujiUserEntity();
                updateUserEntity.setWxId(userEntity.getWxId());
                updateUserEntity.setOpenId(userEntity.getOpenId());
                updateUserEntity.setNickname(wxMap.get("nickname"));
                updateUserEntity.setHeadimgurl(wxMap.get("headimgurl"));
                updateUserEntity.setSubscribe(Integer.getInteger(wxMap.get("subscribe")));
                if (wxMap.get("unionid") != null) updateUserEntity.setUnionId(wxMap.get("unionid"));
                updateUserEntity.setUpdateTime(new Date());
                this.gujiService.addOrUpdateUserInfo(updateUserEntity);
            }
        } catch (Exception e) {
            logger.error(" 更新数据库中微信用户信息异常 {}", e);
        }
    }

    /**
     * 下载指定的mediaId内容到本地（只测试过图片啊）
     * @param mediaId   媒体文件
     * @param fileName  保存的文件名
     * @return  保存的路径名称
     */
    public String saveMediaPic(String mediaId, String fileName) throws Exception {
        String filePathName = this.savePicPath+"/wx/"+fileName;
        InputStream inputStream = null;
        String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token="+this.getAccessToken()+"&media_id="+mediaId;
        URL urlGet = new URL(url);
        HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
        http.setRequestMethod("GET"); // 必须是get方式请求
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        http.setDoOutput(true);
        http.setDoInput(true);
        http.connect();
        // 获取文件转化为byte流
        inputStream = http.getInputStream();

        if (inputStream == null) return null;

        byte[] data = new byte[1024];
        int len = 0;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePathName);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePathName.substring(filePathName.indexOf("upload")-1, filePathName.length());
    }


    /**
     * \n 转换为 <br/>
     * @param str
     * @return
     */
    public static String nl2br(String str) {
        Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
        Matcher m = CRLF.matcher(str);
        if (m.find()) {
            str = m.replaceAll("<br/>");
        }
        return str;
    }

}
