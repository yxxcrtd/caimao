package com.fmall.bana.controller.api.user;

import com.caimao.bana.api.entity.TpzUserBankCardEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.TpzUserExtEntity;
import com.caimao.bana.api.entity.req.FUserFindLoginPwdReq;
import com.caimao.bana.api.entity.req.FUserLoginReq;
import com.caimao.bana.api.entity.req.FUserRegisterReq;
import com.caimao.bana.api.entity.req.FUserResetLoginPwdReq;
import com.caimao.bana.api.entity.session.SessionUser;
import com.caimao.bana.api.enums.EIdcardKind;
import com.caimao.bana.api.enums.ESmsBizType;
import com.caimao.bana.api.enums.EUserInit;
import com.caimao.bana.api.service.IUserBankCardService;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.service.IUserTrustService;
import com.caimao.bana.api.service.SmsService;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.bana.common.api.exception.CustomerException;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.controller.api.user.helper.UserApiHelper;
import com.fmall.bana.utils.RedisUtils;
import com.fmall.bana.utils.Session.SessionHelper;
import com.fmall.bana.utils.crypto.RSA;
import com.fmall.bana.utils.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户相关的API接口
 * Created by Administrator on 2015/9/15.
 */
@Controller
@RequestMapping(value = "/api/user/")
public class UserApiController extends BaseController {

    @Resource
    private IUserService userService;
    @Resource
    private IUserTrustService userTrustService;
    @Resource
    private SmsService smsService;
    @Resource
    private IUserBankCardService userBankCardService;

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private UserApiHelper userApiHelper;

    @Value("${ybk_url}")
    private String fmallUrl;

    // token的有效时间 7 天
    public final static Long TOKEN_VALID_TIME = 604800L;

    /**
     * <p>查询手机号是否已经是财猫用户</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/mobile_exists?mobile=18612839215</p>
     * <p>请求方式：GET</p>
     *
     * @param mobile 手机号
     * @return 存在返回 true，不存在返回 false
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/mobile_exists", method = RequestMethod.GET)
    public boolean mobileExists(@RequestParam(value = "mobile") String mobile) throws Exception {
        try {
            Long userId = this.userService.getUserIdByPhone(mobile);
            return userId != null;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>用户登陆财猫接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/login</p>
     * <p>请求方式：GET</p>
     *
     * @param mobile   登陆名：手机号
     * @param loginPwd 登陆密码
     * @param source   登陆来源
     * @return String    登陆后的token
     * @throws Exception
     * @see com.caimao.bana.common.api.enums.user.EUserLoginSource
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(
            @RequestParam("mobile") String mobile,
            @RequestParam("login_pwd") String loginPwd,
            @RequestParam(value = "source", required = false, defaultValue = "1") String source
    ) throws Exception {
        try {
            FUserLoginReq userLoginReq = new FUserLoginReq();
            userLoginReq.setLoginName(mobile);
            userLoginReq.setLoginPwd(RSA.decodeByPwd(loginPwd));
            userLoginReq.setLoginIP(getRemoteHost());
            userLoginReq.setSource(source);
            Long userId = this.userService.doLogin(userLoginReq);

            TpzUserEntity userEntity = this.userService.getById(userId);
            if (userEntity.getUserKind().trim().equals("10")
                    || userEntity.getUserKind().trim().equals("11")) {
                throw new CustomerException(EErrorCode.ERROR_CODE_100003.getMessage(), EErrorCode.ERROR_CODE_100003.getCode());
            }

            // 生成一个随机的token
            String token = UserApiController.getRandomString(20);

            // 存Redis
            this.redisUtils.set(token, userId.toString(), TOKEN_VALID_TIME);
            //延时等待
            int count = 0;
            while (this.redisUtils.get(token) == null && count < 60) {
                Thread.sleep(10);
                this.redisUtils.set(token, userId.toString(), TOKEN_VALID_TIME);
                count++;
            }

            // TODO 暂时使用这个进行下网站的登陆操作
            try {
                // 存session
                SessionUser sessionUser = SessionHelper.setIdentity(userId);
                sessionProvider.setUserDetail(sessionUser);
            } catch (Exception e) {
                throw e;
            }

            return token;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>用户登陆财猫接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/loginSalt</p>
     * <p>请求方式：GET</p>
     *
     * @param mobile   登陆名：手机号
     * @param loginPwd 登陆密码
     * @param source   登陆来源
     * @return String    登陆后的token
     * @throws Exception
     * @see com.caimao.bana.common.api.enums.user.EUserLoginSource
     */
    @ResponseBody
    @RequestMapping(value = "/loginSalt", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, String> loginSalt(
            @RequestParam("mobile") String mobile,
            @RequestParam("login_pwd") String loginPwd,
            @RequestParam(value = "source", required = false, defaultValue = "1") String source
    ) throws Exception {
        try {
            FUserLoginReq userLoginReq = new FUserLoginReq();
            userLoginReq.setLoginName(mobile);
            userLoginReq.setLoginPwd(RSA.decodeByPwd(loginPwd));
            userLoginReq.setLoginIP(getRemoteHost());
            userLoginReq.setSource(source);
            Long userId = this.userService.doLogin(userLoginReq);

            TpzUserEntity userEntity = this.userService.getById(userId);
            if (userEntity.getUserKind().trim().equals("10")
                    || userEntity.getUserKind().trim().equals("11")) {
                throw new CustomerException(EErrorCode.ERROR_CODE_100003.getMessage(), EErrorCode.ERROR_CODE_100003.getCode());
            }

            // 生成一个随机的token
            String token = UserApiController.getRandomString(20);

            // 存Redis
            this.redisUtils.set(token, userId.toString(), TOKEN_VALID_TIME);

            // TODO 暂时使用这个进行下网站的登陆操作
            try {
                // 存session
                SessionUser sessionUser = SessionHelper.setIdentity(userId);
                sessionProvider.setUserDetail(sessionUser);
            } catch (Exception e) {
                throw e;
            }

            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("token", token);
            resultMap.put("saltKey", userEntity.getPwdSaltKey());
            return resultMap;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>用户退出登陆财猫接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/loginout</p>
     * <p>请求方式：GET</p>
     *
     * @param token 登陆后的token
     * @return Boolean  成功返回true
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/loginout", method = RequestMethod.GET)
    public Boolean loginout(
            @RequestParam(value = "token") String token
    ) throws Exception {
        try {
            this.redisUtils.del(token);
            try {
                // 存session
                sessionProvider.setUserDetail(null);
            } catch (Exception e) {
                throw e;
            }
            return true;
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>财猫用户注册接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/register</p>
     * <p>请求方式：POST</p>
     *
     * @param mobile      手机号
     * @param loginPwd    登陆密码
     * @param smsCode     手机验证码
     * @param source      用户注册来源
     * @param refCaimaoId 注册推荐的财猫ID
     * @return Boolean  注册成功返回 true，失败返回false
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Boolean registerNew(@RequestParam("mobile") String mobile,
                               @RequestParam("login_pwd") String loginPwd,
                               @RequestParam("sms_code") String smsCode,
                               @RequestParam(value = "source", required = false, defaultValue = "4") String source,
                               @RequestParam(value = "ref_caimao_id", required = false) Long refCaimaoId
    ) throws Exception {
        try {
            if (source == null) source = EUserInit.YBK.getCode();
            FUserRegisterReq userRegisterReq = new FUserRegisterReq();
            userRegisterReq.setMobile(mobile);
            userRegisterReq.setUserPwd(RSA.decodeByPwd(loginPwd));
            userRegisterReq.setCheckCode(smsCode);
            userRegisterReq.setRegisterIp(getRemoteHost());
            userRegisterReq.setUserInit(source);
            try {
                if (refCaimaoId != null) {
                    TpzUserEntity refUser = this.userService.getByCaimaoId(refCaimaoId);
                    if (refUser != null) userRegisterReq.setRefUserId(refUser.getUserId());
                }
            } catch (Exception ignored) {
            }

            this.userService.doRegister(userRegisterReq);
            return true;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>注册、登陆、实名认证 3合一接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/registerLoginAuth</p>
     * <p>请求方式：POST</p>
     *
     * @param mobile      手机号
     * @param loginPwd    登陆密码
     * @param smsCode     注册验证码
     * @param source      注册来源
     * @param refCaimaoId 邀请ID
     * @param realName    真实姓名
     * @param idcard_no   身份证号码
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/registerLoginAuth", method = RequestMethod.POST)
    public String registerLoginAuth(
            @RequestParam("mobile") String mobile,
            @RequestParam("login_pwd") String loginPwd,
            @RequestParam("sms_code") String smsCode,
            @RequestParam(value = "source", required = false) String source,
            @RequestParam(value = "ref_caimao_id", required = false) Long refCaimaoId,
            @RequestParam(value = "real_name") String realName,
            @RequestParam(value = "idcard_no") String idcard_no
    ) throws Exception {
        try {
            // 注册
            if (source == null) source = EUserInit.YBK.getCode();
            FUserRegisterReq userRegisterReq = new FUserRegisterReq();
            userRegisterReq.setMobile(mobile);
            userRegisterReq.setUserPwd(RSA.decodeByPwd(loginPwd));
            userRegisterReq.setCheckCode(smsCode);
            userRegisterReq.setRegisterIp(getRemoteHost());
            userRegisterReq.setUserInit(source);
            try {
                if (refCaimaoId != null) {
                    TpzUserEntity refUser = this.userService.getByCaimaoId(refCaimaoId);
                    if (refUser != null) userRegisterReq.setRefUserId(refUser.getUserId());
                }
            } catch (Exception ignored) {
            }

            this.userService.doRegister(userRegisterReq);

            // 登陆
            FUserLoginReq userLoginReq = new FUserLoginReq();
            userLoginReq.setLoginName(mobile);
            userLoginReq.setLoginPwd(RSA.decodeByPwd(loginPwd));
            userLoginReq.setLoginIP(getRemoteHost());
            Long userId = this.userService.doLogin(userLoginReq);

            TpzUserEntity userEntity = this.userService.getById(userId);
            if (userEntity.getUserKind().trim().equals("10")
                    || userEntity.getUserKind().trim().equals("11")) {
                throw new CustomerException("你的角色为理财人，不允许登录!", 888888);
            }

            // 生成一个随机的token
            String token = UserApiController.getRandomString(20);

            // 存Redis
            this.redisUtils.set(token, userId.toString(), 7200L);
            //延时等待
            int count = 0;
            while (this.redisUtils.get(token) == null && count < 60) {
                Thread.sleep(10);
                this.redisUtils.set(token, userId.toString(), 7200L);
                count++;
            }
            // TODO 暂时使用这个进行下网站的登陆操作
            try {
                // 存session
                SessionUser sessionUser = SessionHelper.setIdentity(userId);
                sessionProvider.setUserDetail(sessionUser);
            } catch (Exception e) {
                throw e;
            }

            // 实名认证
            this.userTrustService.doVerifyIdentity(userId, realName, EIdcardKind.IDCARD.getId(), idcard_no);

            return token;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), 888888);
        } catch (Exception e) {
            throw new ApiException("服务器异常", 888888);
        }
    }


    /**
     * <p>财猫用户找回密码重置接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/find_pwd</p>
     * <p>请求方式：POST</p>
     *
     * @param mobile    手机号
     * @param checkCode 验证码
     * @param userPwd   新密码
     * @return 重置成功返回 true
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/find_pwd", method = RequestMethod.POST)
    public boolean findLoginPwdNew(
            @RequestParam("mobile") String mobile,
            @RequestParam("check_code") String checkCode,
            @RequestParam("user_pwd") String userPwd
    ) throws Exception {
        try {
            FUserFindLoginPwdReq userFindLoginPwdReq = new FUserFindLoginPwdReq();
            userFindLoginPwdReq.setMobile(mobile);
            userFindLoginPwdReq.setCheckCode(checkCode);
            userFindLoginPwdReq.setUserLoginPwd(RSA.decodeByPwd(userPwd));
            userService.doFindLoginPwd(userFindLoginPwdReq);
            return true;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>财猫用户修改登陆密码接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/modify_pwd</p>
     * <p>请求方式：POST</p>
     *
     * @param token  登陆后返回的token
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return 成功返回true
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/modify_pwd", method = RequestMethod.POST)
    public boolean resetLoginpwd(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "old_pwd") String oldPwd,
            @RequestParam(value = "new_pwd") String newPwd
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FUserResetLoginPwdReq userResetLoginPwdReq = new FUserResetLoginPwdReq();
            userResetLoginPwdReq.setUserId(userId);
            userResetLoginPwdReq.setNewPwd(RSA.decodeByPwd(newPwd));
            userResetLoginPwdReq.setOldPwd(RSA.decodeByPwd(oldPwd));
            this.userService.resetLoginpwd(userResetLoginPwdReq);
            return true;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>用户实名认证的接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/user_auth</p>
     * <p>请求方式：POST</p>
     *
     * @param token     登陆后返回的token
     * @param realName  用户真实姓名
     * @param idcard_no 用户身份证号码
     * @return 成功返回true
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/user_auth", method = RequestMethod.POST)
    public boolean userAuth(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "real_name") String realName,
            @RequestParam(value = "idcard_no") String idcard_no
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            this.userTrustService.doVerifyIdentity(userId, realName, EIdcardKind.IDCARD.getId(), idcard_no);
            return true;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>发送注册时的手机验证码</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/registercode?mobile=18612839215</p>
     * <p>请求方式：GET</p>
     *
     * @param mobile 手机号
     * @return 发送成功返回 true
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/registercode", method = RequestMethod.GET)
    public boolean sendRegisterCode(@RequestParam("mobile") String mobile) throws Exception {
        try {
            this.sendPhoneCode(ESmsBizType.REGISTER.getCode(), mobile);
            return true;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>发送找回密码时的手机验证码</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/findpwdcode?mobile=18612839215</p>
     * <p>请求方式：GET</p>
     *
     * @param mobile 手机号
     * @return 发送成功返回 true
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/findpwdcode", method = RequestMethod.POST)
    public boolean sendFindpwdCode(@RequestParam("mobile") String mobile) throws Exception {
        try {
            this.sendPhoneCode(ESmsBizType.FINDLONGINPWD.getCode(), mobile);
            return true;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>发送重置贵金属密码</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/resetgjscode</p>
     * <p>请求方式：GET</p>
     *
     * @param token   登陆后返回的token
     * @param smsType 短信类型 10资金密码 11交易密码
     * @return 发送成功返回 true
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/resetgjscode", method = RequestMethod.GET)
    public boolean sendResetGjsCode(
            @RequestParam("token") String token,
            @RequestParam("smsType") String smsType
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            TpzUserEntity userEntity = this.userService.getById(userId);
            if (smsType.equals(ESmsBizType.GJSRESETFUNDS.getCode())) {
                this.sendPhoneCode(ESmsBizType.GJSRESETFUNDS.getCode(), userEntity.getMobile());
            } else if (smsType.equals(ESmsBizType.GJSRESETTRADE.getCode())) {
                this.sendPhoneCode(ESmsBizType.GJSRESETTRADE.getCode(), userEntity.getMobile());
            } else {
                throw new CustomerException(EErrorCode.ERROR_CODE_100005.getMessage(), EErrorCode.ERROR_CODE_100005.getCode());
            }
            return true;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>获取用户的信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/get_user_info?token=*********</p>
     * <p>请求方式：GET</p>
     * <p/>
     * <p>
     * 返回值 <br />
     * userId	用户编号 <br />
     * userName	实名认证姓名 <br />
     * mobile	用户手机号 <br />
     * idcard	身份证号码 <br />
     * isTrust	是否实名认证（0 未认证 1 已认证） <br />
     * avatarPic	头像地址 <br />
     * </p>
     *
     * @param token 登陆后返回的token
     * @return Map 返回用户的注册信息
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/get_user_info", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getUserInfo(@RequestParam(value = "token") String token) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            TpzUserEntity userEntity = this.userService.getById(userId);
            TpzUserExtEntity userExtEntity = this.userService.getUserExtById(userId);
            String avatarPic = userExtEntity.getUserPhoto();
            if (avatarPic != null && !Objects.equals(avatarPic, "")) {
                if (!avatarPic.contains("upload")) {
                    avatarPic = this.fmallUrl + "/avatars/" + avatarPic;
                } else {
                    avatarPic = this.fmallUrl + avatarPic;
                }
            }

            // 自动续期token有效期
            this.redisUtils.set(token, userId.toString(), TOKEN_VALID_TIME);

            Map<String, Object> res = new HashMap<>();
            res.put("userId", userEntity.getUserId());
            res.put("userName", userEntity.getUserRealName());
            res.put("pwdSaltKey", userEntity.getPwdSaltKey());
            res.put("mobile", userEntity.getMobile());
            res.put("idcard", userEntity.getIdcard());
            res.put("isTrust", userEntity.getIsTrust());
            res.put("avatarPic", avatarPic);
            return res;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>获得上传证件照片的路径</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/get_card_path?token=*********</p>
     * <p>请求方式：GET</p>
     *
     * @param token 登陆后返回的token
     * @return Map 返回证件照片路径
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/get_card_path", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getCardPath(@RequestParam(value = "token") String token) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            TpzUserExtEntity userExtEntity = this.userService.getUserExtById(userId);
            Map<String, Object> res = new HashMap<>();
            res.put("userId", userExtEntity.getUserId());
            res.put("cardPositivePath", userExtEntity.getIdcardPositivePath());
            res.put("cardOppositePath", userExtEntity.getIdcardOppositePath());
            return res;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>用户头像设置</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/set_avatar</p>
     * <p>请求方式：GET</p>
     * <p/>
     * <p>返回值</p>
     * <p/>
     * <p>
     * 成功返回true，失败返回false
     * </p>
     *
     * @param token     登陆返回token
     * @param avatarPic 图片地址
     * @return 成功返回 true 失败返回 false
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/set_avatar", method = {RequestMethod.GET, RequestMethod.POST})
    public Boolean setAvatar(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "avatar_pic") String avatarPic
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            this.userService.updatePhotoByUserId(userId, avatarPic);
            return true;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>获取用户绑定的银行卡列表信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/user/get_user_bind_bank?token=*********</p>
     * <p>请求方式：GET</p>
     *
     * @param token 登陆后返回的token
     * @return 用户绑定的银行卡列表
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/get_user_bind_bank", method = {RequestMethod.GET, RequestMethod.POST})
    public List<TpzUserBankCardEntity> getUserBindBank(@RequestParam(value = "token") String token) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            return this.userBankCardService.queryUserBankList(userId, "1");
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * 发送手机验证码
     *
     * @param bizType
     * @param mobile
     * @throws Exception
     */
    private void sendPhoneCode(String bizType, String mobile) throws Exception {
        // 检查手机号格式是否正确
        this.isMobile(mobile);
        smsService.doSendSmsCheckCode(mobile, bizType);
    }


    /**
     * 生成随机的字符串
     *
     * @param length 生成的长度
     * @return 随机的字符串
     */
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 检查是否是手机号格式
     *
     * @param phone 手机号码
     * @throws CustomerException
     */
    private void isMobile(String phone) throws CustomerException {
        Pattern p = Pattern.compile("^[1][0-9]{10}$"); // 验证手机号
        Matcher m = p.matcher(phone);
        boolean b = m.matches();
        if (b == false) {
            throw new CustomerException("手机号码格式错误", 888888);
        }
    }

}
