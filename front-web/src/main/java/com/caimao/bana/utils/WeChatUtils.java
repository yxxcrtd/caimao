package com.caimao.bana.utils;

import com.caimao.bana.api.entity.WeChatForecastEntity;
import com.caimao.bana.api.entity.WeChatUserEntity;
import com.caimao.bana.api.service.WeChatUserService;
import com.huobi.commons.utils.HttpUtil;
import com.huobi.commons.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class WeChatUtils {

    @Resource
    private WeChatUserService weChatUserService;

    private static final Map<String, Object> cacheHashMap = new HashMap<>();
    private static final String APPID = "wxad6843828090a34d";
    private static final String SECRET = "10cf8218faa442d6a8b5e5c694a4cd11";

    //获取code
    private String checkCode(HttpServletRequest request, HttpServletResponse response, String redirectUrl) throws Exception{
        String code = request.getParameter("code");
        if(code == null || "".equals(code)){
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ APPID +"&redirect_uri="+ java.net.URLEncoder.encode(redirectUrl, "UTF-8") +"&scope=snsapi_userinfo&response_type=code&state=caimao#wechat_redirect";
            response.sendRedirect(url);
            throw new Exception("获取终止");
        }
        return code;
    }


    //获取token
    private Map<String, String> getToken(String code) throws Exception{
        String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ APPID +"&secret="+ SECRET +"&code="+ code +"&grant_type=authorization_code";
        String tokenJson = HttpUtil.doGet(tokenUrl);
        if(tokenJson == null) throw new Exception("获取微信token无返回数据");
        if(!StringUtils.startsWith(tokenJson, "{")) throw new Exception("token不是json格式");

        Map<String, String> tokenInfo = JsonUtil.toObject(tokenJson, Map.class);

        if(tokenInfo == null || "".equals(tokenInfo.get("openid")) || "".equals(tokenInfo.get("access_token"))){
            tokenJson = HttpUtil.doGet(tokenUrl);
            if(tokenJson == null) throw new Exception("获取微信token无返回数据");
            if(!StringUtils.startsWith(tokenJson, "{")) throw new Exception("token不是json格式");
            tokenInfo = JsonUtil.toObject(tokenJson, Map.class);
        }
        return tokenInfo;
    }

    //获取access_token
    private Map<String, String> getAccessToken() throws Exception{
        Object tokenTime = cacheHashMap.get("weChatAccessTokenTime");
        if(tokenTime == null || System.currentTimeMillis() - Long.parseLong(tokenTime.toString()) > 1){
            cacheHashMap.put("weChatAccessTokenTime", System.currentTimeMillis());
            String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ APPID +"&secret="+ SECRET;
            String tokenJson = HttpUtil.doGet(tokenUrl);
            if(tokenJson == null) throw new Exception("获取微信access_token无返回数据");
            if(!StringUtils.startsWith(tokenJson, "{")) throw new Exception("获取微信access_token无返回数据不是json格式");
            Map<String, String> tokenInfo = JsonUtil.toObject(tokenJson, Map.class);
            if(tokenInfo == null || "".equals(tokenInfo.get("access_token"))){
                tokenJson = HttpUtil.doGet(tokenUrl);
                if(tokenJson == null) throw new Exception("获取微信access_token无返回数据");
                if(!StringUtils.startsWith(tokenJson, "{")) throw new Exception("access_token不是json格式");
                tokenInfo = JsonUtil.toObject(tokenJson, Map.class);
            }
            cacheHashMap.put("weChatAccessTokenInfo", tokenInfo);
            return tokenInfo;
        }else{
            Object tokenInfoObject = cacheHashMap.get("weChatAccessTokenInfo");
            Map<String, String> tokenInfo = (Map<String, String>)tokenInfoObject;
            return tokenInfo;
        }
    }

    //获取jsapi_ticket
    private Map<String, Object> getTicket(String accessToken) throws Exception{
        Object tokenTime = cacheHashMap.get("weChatTicketTime");
        if(tokenTime == null || System.currentTimeMillis() - Long.parseLong(tokenTime.toString()) > 1){
            cacheHashMap.put("weChatTicketTime", System.currentTimeMillis());
            String ticketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ accessToken +"&type=jsapi";
            String ticketJson = HttpUtil.doGet(ticketUrl);
            if(ticketJson == null) throw new Exception("获取微信ticket无返回数据");
            if(!StringUtils.startsWith(ticketJson, "{")) throw new Exception("ticket不是json格式");
            Map<String, Object> ticketInfo = JsonUtil.toObject(ticketJson, Map.class);
            if(ticketInfo == null || "".equals(ticketInfo.get("ticket"))){
                ticketJson = HttpUtil.doGet(ticketUrl);
                if(ticketJson == null) throw new Exception("获取微信ticket无返回数据");
                if(!StringUtils.startsWith(ticketJson, "{")) throw new Exception("ticket不是json格式");
                ticketInfo = JsonUtil.toObject(ticketJson, Map.class);
            }
            cacheHashMap.put("weChatTicketInfo", ticketInfo);
            return ticketInfo;
        }else{
            Object ticketInfoObject = cacheHashMap.get("weChatTicketInfo");
            Map<String, Object> ticketInfo = (Map<String, Object>)ticketInfoObject;
            return ticketInfo;
        }
    }

    //获取用户信息
    private Map<String, Object> getUserInfo(String token, String openid) throws Exception{
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+ token +"&openid="+ openid +"&lang=zh_CN";
        String userInfoJson = HttpUtil.doGet(userInfoUrl);
        if(userInfoJson == null) throw new Exception("获取微信用户信息无返回数据");
        if(!StringUtils.startsWith(userInfoJson, "{")) throw new Exception("返回信息不是json格式");

        Map<String, Object> userInfo = JsonUtil.toObject(userInfoJson, Map.class);
        if(userInfo == null || "".equals(userInfo.get("nickname"))){
            userInfoJson = HttpUtil.doGet(userInfoUrl);
            if(userInfoJson == null) throw new Exception("获取微信用户信息无返回数据");
            if(!StringUtils.startsWith(userInfoJson, "{")) throw new Exception("返回信息不是json格式");
            userInfo = JsonUtil.toObject(userInfoJson, Map.class);
        }
        return userInfo;
    }

    public WeChatUserEntity getWeChatUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //获取授权code
        String code = checkCode(request, response, "http://172.32.3.227:8080/activity/stockMarketForecast");
        //获取token
        Map<String, String> tokenInfo = getToken(code);
        //获取access_token
        Map<String, String> accessTokenInfo = getAccessToken();
        //获取ticket
        getTicket(accessTokenInfo.get("access_token"));
        //获取用户信息
        Map<String, Object> userInfo = getUserInfo(tokenInfo.get("access_token"), tokenInfo.get("openid"));
        //更新微信用户信息
        WeChatUserEntity weChatUserEntity = this.weChatUserService.getWeChatUserByOpenid(tokenInfo.get("openid"));
        if(weChatUserEntity == null){
            //创建用户信息
            WeChatUserEntity createUserInfo = new WeChatUserEntity();
            createUserInfo.setOpenid(userInfo.get("openid").toString());
            createUserInfo.setNickname(userInfo.get("nickname").toString());
            if(!"".equals(userInfo.get("sex"))) createUserInfo.setSex(new Byte(userInfo.get("sex").toString()));
            if(!"".equals(userInfo.get("city"))) createUserInfo.setCity(userInfo.get("city").toString());
            if(!"".equals(userInfo.get("province"))) createUserInfo.setProvince(userInfo.get("province").toString());
            if(!"".equals(userInfo.get("country"))) createUserInfo.setCountry(userInfo.get("country").toString());
            if(!"".equals(userInfo.get("headimgurl"))) createUserInfo.setHeadImgUrl(userInfo.get("headimgurl").toString());
            createUserInfo.setCreated(System.currentTimeMillis() / 1000);
            this.weChatUserService.createWeChatUser(createUserInfo);
        }else{
            //更新用户信息
            Map<String, Object> updateParamMap = new HashMap<>();
            if(!"".equals(userInfo.get("nickname"))) updateParamMap.put("nickname", userInfo.get("nickname").toString());
            if(!"".equals(userInfo.get("sex"))) updateParamMap.put("sex", new Byte(userInfo.get("sex").toString()));
            if(!"".equals(userInfo.get("city"))) updateParamMap.put("city", userInfo.get("city").toString());
            if(!"".equals(userInfo.get("province"))) updateParamMap.put("province", userInfo.get("province").toString());
            if(!"".equals(userInfo.get("country"))) updateParamMap.put("country", userInfo.get("country").toString());
            if (!"".equals(userInfo.get("headimgurl"))) updateParamMap.put("headimgurl", userInfo.get("headimgurl").toString());
            this.weChatUserService.updateWeChatUser(userInfo.get("openid").toString(), updateParamMap);
        }

        return this.weChatUserService.getWeChatUserByOpenid(userInfo.get("openid").toString());
    }

    public void createMarketForecast(Long weChatUserId, Byte forecastType) throws Exception{
        WeChatForecastEntity weChatForecastEntity = new WeChatForecastEntity();
        weChatForecastEntity.setForecastDate(getTimesToday() + 86400);
        weChatForecastEntity.setForecastType(forecastType);
        weChatForecastEntity.setWeChatUserId(weChatUserId);
        this.weChatUserService.createMarketForecast(weChatForecastEntity);
    }

    public static long getTimesToday(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis()/1000;
    }

    public Map<String, String> getSign(String Url) throws Exception{
        Object ticketInfoObject = cacheHashMap.get("weChatTicketInfo");
        Map<String, Object> ticketInfo = (Map<String, Object>)ticketInfoObject;
        return WeChatJsSDKUtils.sign(ticketInfo.get("ticket").toString(), Url);
    }
}
