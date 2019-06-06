package com.fmall.bana.controller;

import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.entity.session.SessionUser;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.service.guji.IGujiService;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.bana.common.api.exception.CustomerException;
import com.fmall.bana.utils.CommonStringUtils;
import com.fmall.bana.utils.RedisUtils;
import com.fmall.bana.utils.Session.ControllerContext;
import com.fmall.bana.utils.Session.SessionProvider;
import com.fmall.bana.utils.captcha.CustomGenericManageableCaptchaService;
import com.fmall.bana.utils.weixin.WXBaseService;
import com.fmall.bana.utils.weixin.hq.StockHQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 基础的控制器
 */
@Controller
public class BaseController {

    /** 日志 */
    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    private static final String SESSION_KEY_USER = "user";

    @Autowired
    protected SessionProvider sessionProvider;
    @Resource
    private IUserService userService;
    @Resource
    private RedisUtils redisUtils;

    @Resource(name = "imageCaptchaService")
    protected CustomGenericManageableCaptchaService imageCaptchaService;

    @Resource
    protected WXBaseService wxBaseService;

    @Resource
    protected IGujiService gujiService;

    @Resource
    protected StockHQService stockHQService;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    /**
     * 获取股计用户实体对象
     *
     * @param mav
     * @param wxId
     * @return
     * @throws Exception
     */
    protected GujiUserEntity getGujiUserEntityByAccessToken(String code, ModelAndView mav, Long wxId) throws Exception {
        if (null != code) {
            this.saveAccessTokenAndOpenIdByCode(code, WXBaseService.scopeBase);
        }

        Map<String, String> authInfo = this.verificationAccessToken(WXBaseService.scopeBase);
        mav.addObject("authInfo", authInfo);

        // 获取null，则返回null进行跳转授权页面
        if (null == authInfo) {
            return null;
        }

        // 获取分析师的个人信息
        GujiUserEntity gujiUserEntity = null;
        if (null == wxId) {
            gujiUserEntity = this.gujiService.getUserByOpenId(authInfo.get("openId"));
        } else {
            gujiUserEntity = this.gujiService.getUserById(wxId);
        }

        return gujiUserEntity;
    }


//    protected void baseAuthInfo(String code, ModelAndView mav) throws Exception {
//        if (null != code) {
//            this.saveAccessTokenAndOpenIdByCode(code, WXBaseService.scopeBase);
//        }
//
//        Map<String, String> authInfo = this.verificationAccessToken(WXBaseService.scopeBase);
//        mav.addObject("authInfo", authInfo);
//
//        if (authInfo == null) return null;  // 获取null，则返回null进行跳转授权页面
//
//        openId = authInfo.get("openId");
//
//        // 查询看的这个人是否关注了股计服务号
//        Integer isFollowGuji = 0;
//        GujiUserEntity checkGujiUserEntity = this.gujiService.getUserByOpenId(authInfo.get("openId"));
//        if (checkGujiUserEntity != null && checkGujiUserEntity.getSubscribe().equals(1)) {
//            isFollowGuji = 1;
//        }
//        mav.addObject("isFollowGuji", isFollowGuji);
//    }



    /**
     * 根据授权返回的code，获取用户的accessToken与openId
     * 并且保存到session中，进行记录
     * @param code
     */
    protected void saveAccessTokenAndOpenIdByCode(String code, String scope) throws Exception {
        Map<String, String> resMap = this.wxBaseService.getWebAccessToken(code);
        if (resMap != null && resMap.get("access_token") != null) {
            this.sessionProvider.setAttribute("accessToken"+scope, resMap.get("access_token"));
            this.sessionProvider.setAttribute("openId"+scope, resMap.get("openid"));
        }
    }

    /**
     * 如果当前页面需要获取用户的基本信息，那就需要调用这个方法获取网页的accessToken
     * 从session中获取当前访问用户的信息
     * @return
     */
    protected Map<String, String> verificationAccessToken(String scope) throws Exception {
        Object accessToken = this.sessionProvider.getAttribute("accessToken"+scope);
        Object openId = this.sessionProvider.getAttribute("openId"+scope);
        if (accessToken == null || openId == null) {
            // 没有获取授权，需要跳转到专门的页面进行跳转
            String authUrl = this.wxBaseService.createdAuthUrl(this.getNowUrl(), scope, "");
            response.sendRedirect(authUrl);
            return null;
        }
        Map<String, String> resMap = new TreeMap<>();
        resMap.put("accessToken", accessToken.toString());
        resMap.put("openId", openId.toString());
        return resMap;
    }

    /**
     * 获取用户当前访问网址链接
     * @return
     */
    protected String getNowUrl() {
        String url = this.request.getRequestURL().toString();
        if (this.request.getQueryString() != null && this.request.getQueryString() != "") {
            url += "?" + this.request.getQueryString();
        }
        return url;
    }

    /**
     * 根据传递的token，获取用户的userId
     *
     * @param token 登陆后返回的token
     * @return 用户编号
     * @throws Exception
     */
    protected Long getUserIdByToken(String token) throws Exception {
        Object redisUserId = this.redisUtils.get(token);
        if (redisUserId == null) {
            throw new CustomerException(EErrorCode.ERROR_CODE_100001.getMessage(), EErrorCode.ERROR_CODE_100001.getCode());
        }
        return Long.valueOf(redisUserId.toString());
    }

    /**
     * 返回登陆用户的基本信息，前端返回的
     * @return
     * @throws Exception
     */
    public Map<String, Object> getUserInfoMap() throws Exception {
        try{
            Long userId = this.getSessionUser().getUser_id();
            TpzUserEntity userEntity = this.userService.getById(userId);
            Map<String, Object> res = new HashMap<>();
            res.put("name", userEntity.getUserRealName());
            res.put("mobile", userEntity.getMobile().substring(0, 3) + "****" + userEntity.getMobile().substring(8, 11));
            return res;
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 获取session user
     *
     * @return
     */
    protected SessionUser getSessionUser() {
        //SessionUser user = (SessionUser) UserDetailHolder.getUserDetail();
        SessionUser user = (SessionUser) sessionProvider.getAttribute(SESSION_KEY_USER);
        if (user != null && user.getUser_id() != null && user.getUser_id() != 0) {
            Object tokenObj = this.redisUtils.get("token" + user.getUser_id().toString());
            if (tokenObj == null || tokenObj.toString().equals("")) {
                // 生成一个随机的token
                String token = CommonStringUtils.getRandomString(20);
                // 存Redis
                this.redisUtils.set("token" + user.getUser_id().toString(), token, 7200L);
                this.redisUtils.set(token, user.getUser_id().toString(), 7200L);
            }
        }
        return user;
    }

    /**
     * 获取用户
     *
     * @return
     * @throws Exception
     */
    protected TpzUserEntity getCaimaoUser() throws Exception {
        return userService.getById(getSessionUser().getUser_id());
    }

    /**
     * 获取用户token
     *
     * @return
     * @throws Exception
     */
    protected String getUserToken() throws Exception {
        Object tokenObj = null;
        SessionUser user = getSessionUser();
        if (user != null && user.getUser_id() != null && user.getUser_id() != 0) {
            tokenObj = this.redisUtils.get("token" + user.getUser_id().toString());
        }
        return tokenObj == null ? "" : tokenObj.toString();
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    protected String getRemoteHost() {
        String ipAddress = ControllerContext.getRequest().getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ControllerContext.getRequest().getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ControllerContext.getRequest().getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ControllerContext.getRequest().getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

}
