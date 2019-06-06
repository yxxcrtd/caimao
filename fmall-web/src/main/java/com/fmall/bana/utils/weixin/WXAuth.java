package com.fmall.bana.utils.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.enums.guji.EGujiAuthStatus;
import com.caimao.bana.api.service.guji.IGujiService;
import com.fmall.bana.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 用户认证功能
 * Created by Administrator on 2016/1/12.
 */
@Component
public class WXAuth {

    private static final Logger logger = LoggerFactory.getLogger(WXAuth.class);

    @Resource
    private WXBaseService wxBaseService;
    @Resource
    private IGujiService gujiService;
    @Resource
    private RedisUtils redisUtils;

    private void clearAllRedisKey(String openId) {
        this.redisUtils.del(WXBaseService.redisCurrentProcKey+openId);
        this.redisUtils.del(openId + "_pic");
        this.redisUtils.del(openId + "_title");
    }
    /**
     * 为了解决Redis中文的问题，单独写的方法
     * @param key
     * @param val
     */
    private void setRedis(String key, String val) {
        this.redisUtils.set(key, JSON.toJSONString(val, SerializerFeature.BrowserCompatible), WXBaseService.redisCacheUserInfoExpires);
    }

    private String getRedis(String key) {
        Object val = this.redisUtils.get(key);
        if (val == null) return null;
        return JSON.parseObject(val.toString(), String.class);
    }


    /**
     * 用户点击认证的功能
     * @param reqMap
     * @return
     */
    public String clickProcess(Map<String, String> reqMap) throws Exception {
        String formOpenId = reqMap.get("FromUserName");
        String key = WXBaseService.redisCurrentProcKey + formOpenId;
        String val = "auth";

        GujiUserEntity userEntity = this.gujiService.getUserByOpenId(formOpenId);
        if (userEntity.getAuthStatus().equals(EGujiAuthStatus.OK.getCode())) {
            return "您已通过认证，title“"+userEntity.getCertificationAuth()+"”";
        } else if (userEntity.getAuthStatus().equals(EGujiAuthStatus.AUDIT.getCode())) {
            return "已收到您的申请，稍后会通知您认证结果";
        }

        this.redisUtils.set(key, val, WXBaseService.redisCurrentExpires);

        return "股计目前开放券商、基金等机构投资人身份认证，认证请回复名片照片";
    }

    /**
     * 处理用户发送title的请求
     * @param reqMap
     * @return
     * @throws Exception
     */
    public String process(Map<String, String> reqMap, String currentProc) throws Exception {
        String formOpenId = reqMap.get("FromUserName");
        String key = WXBaseService.redisCurrentProcKey + formOpenId;

        if (reqMap.get("MsgType").equals("text") && reqMap.get("Content").equalsIgnoreCase("c")) {
            this.clearAllRedisKey(formOpenId);
            return "您取消了认证";
        }

        switch (currentProc) {
            case "auth" :
                if ( ! reqMap.get("MsgType").equals("image")) {
                    return "认证请回复名片照片，取消认证请回复c";
                }
                // 获取mediaId，根据这个将图片下载到本地
                String picUrl = null;
                try {
                    picUrl = this.wxBaseService.saveMediaPic(reqMap.get("MediaId"), formOpenId+".jpg");
                } catch (Exception e) {
                    logger.error("下载微信图片异常 ：{}", e);
                }
                if (picUrl == null) {
                    this.clearAllRedisKey(formOpenId);
                    return "获取图片错误，请稍后再试";
                }
                this.setRedis(formOpenId+"_pic", picUrl);
                this.redisUtils.set(key, "auth-1", WXBaseService.redisCurrentExpires);

                return "请输入您的title，不超过15字。\n" +
                        "例如： 苹果证券农林牧渔首席分析师；\n" +
                        "水果基金研究员";
            case "auth-1":
                if (!reqMap.get("MsgType").equals("text")) {
                    return "请输入您的title，不超过15字。\n" +
                            "例如： 苹果证券农林牧渔首席分析师；\n" +
                            "水果基金研究员";
                }
                String title = reqMap.get("Content");
                if (title.length() > 15) {
                    return "请输入您的title，不超过15字。\n" +
                            "例如： 苹果证券农林牧渔首席分析师；\n" +
                            "水果基金研究员";
                }
                String pic = this.getRedis(formOpenId+"_pic");
                if (pic == null) {
                    this.clearAllRedisKey(formOpenId);
                    return "出问题了，请大侠重新来过！-_#";
                }
                GujiUserEntity userEntity = this.gujiService.getUserByOpenId(formOpenId);
                userEntity.setCertificationAuth(title);
                userEntity.setCardPic(pic);
                userEntity.setAuthStatus(EGujiAuthStatus.AUDIT.getCode());
                this.gujiService.addOrUpdateUserInfo(userEntity);

                this.clearAllRedisKey(formOpenId);
                return "已收到您的申请，稍后会通知您认证结果";
            default:
                this.clearAllRedisKey(formOpenId);
                return "success";
        }
    }


}
