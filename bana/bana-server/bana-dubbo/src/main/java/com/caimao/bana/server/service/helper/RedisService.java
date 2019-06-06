package com.caimao.bana.server.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caimao.bana.server.service.user.UserServiceHelper;
import com.caimao.bana.server.utils.RedisUtils;

@Service("redisService")
public class RedisService {
    private Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserServiceHelper userServiceHelper;

//    /**
//     * 保存【获取注册登录所用的sessionid、code】
//     */
//    public void saveSessionidAndCode(String sessionid, String code, Long ttl) {
//        if (ttl > Constants.UC_CAPTCHA_SESSION_TTL_MAX) {
//            ttl = Constants.UC_CAPTCHA_SESSION_TTL_MAX;
//        } else if (ttl <= 0) {
//            ttl = Constants.UC_CAPTCHA_SESSION_TTL_DEFAULT;
//        }
//        String key = Constants.UC_CAPTCHA_SESSION_PREFIX + sessionid;
//        String value = code;
//        redisUtils.set(Constants.USER_REDIS_INDEX, key, value, ttl);
//    }
//
//    public void saveSessionidAndCode(String sessionid, String code) {
//        saveSessionidAndCode(sessionid, code, Constants.UC_CAPTCHA_SESSION_TTL_DEFAULT);
//    }
//
//    /**
//     * 验证【注册登录所用的sessionid、code】
//     * 
//     * @param sessionid
//     * @param code
//     * @return true 验证通过，false 验证不通过
//     */
//    public boolean verifySessionidAndCode(String sessionid, String code) {
//        if (sessionid == null || code == null) {
//            return false;
//        }
//        sessionid = sessionid.trim();
//        code = code.trim();
//        String key = Constants.UC_CAPTCHA_SESSION_PREFIX + sessionid;
//        String value = code;
//        String savedValue = redisUtils.get(Constants.USER_REDIS_INDEX, key).toString();
//        logger.debug(key + "  " + savedValue + "  " + code);
//        boolean ret = value.equalsIgnoreCase(savedValue);
//        redisUtils.del(Constants.USER_REDIS_INDEX, key);
//        return ret;
//    }
//
//    /**
//     * 用户申请重置密码时需要首先申请一个token 这个方法就是保存申请的token与email的对于关系
//     */
//    public void saveApplyResetPasswordToken(String email, String token) {
//        saveApplyResetPasswordToken(email, token, Constants.UC_RESTPASSWORDTOKEN_TTL_DEFAULT);
//    }
//
//    public void saveApplyResetPasswordToken(String email, String token, Long ttl) {
//        if (ttl > Constants.UC_RESTPASSWORDTOKEN_TTL_MAX) {
//            ttl = Constants.UC_RESTPASSWORDTOKEN_TTL_MAX;
//        } else if (ttl <= 0) {
//            ttl = Constants.UC_RESTPASSWORDTOKEN_TTL_DEFAULT;
//        }
//        String key = Constants.UC_RESTPASSWORDTOKEN_PREFIX + email;
//        String value = token;
//        redisUtils.del(Constants.USER_REDIS_INDEX, key);
//        redisUtils.set(Constants.USER_REDIS_INDEX, key, value, ttl);
//    }
//
//    public boolean verifResetPasswordToken(String email, String token) {
//        String key = Constants.UC_RESTPASSWORDTOKEN_PREFIX + email;
//        String value = token;
//        String savedValue = redisUtils.get(Constants.USER_REDIS_INDEX, key).toString();
//        redisUtils.del(Constants.USER_REDIS_INDEX, key);
//        return value.equals(savedValue);
//    }
//
//    public void saveAccessTokenAndUserLoginInfo(String accessToken, User ucUserLogin) {
//        saveAccessTokenAndUserLoginInfo(accessToken, ucUserLogin, Constants.UC_ACCESSTOKEN_TTL_DEFAULT);
//    }
//
//    public void saveAccessTokenAndUserLoginInfo(String accessToken, User user, Long ttl) {
//        // 检查用户大类
//        String user_agentType = user.getUserAgent();
//        String email = user.getEmail();
//        String user_agentKey = Constants.UC_ACCESSTOKEN_USERAGENT_PREFIX + email + user_agentType;
//        logger.debug("this accessToken is {} user_agentKey is {}", accessToken, user_agentKey);
//        String savedTokenKey = redisUtils.get(0, user_agentKey).toString();
//
//        if (savedTokenKey != null) {// 本user_agent已经登录过
//            redisUtils.del(0, savedTokenKey);
//        }
//
//        String key = Constants.UC_ACCESSTOKEN_PREFIX + accessToken;
//        String value = JsonUtil.toString(user);
//        redisUtils.set(Constants.USER_REDIS_INDEX, key, value, ttl);
//        redisUtils.set(Constants.USER_REDIS_INDEX, user_agentKey, key, ttl);
//    }
//
//    public User getUserLoginInfoByAccesstoken(String accessToken, FromTypeEnum fromTypeEnum) {
//        String userTokenKey = userServiceHelper.getUserTokenKey(accessToken);
//        String userValue = redisUtils.get(Constants.USER_REDIS_INDEX, userTokenKey).toString();
//        if (userValue == null) {
//            return null;
//        }
//        User user = JsonUtil.toObject(userValue, User.class);
//        String email = user.getEmail();
//        String userEmailKey = userServiceHelper.getUserEmailKey(fromTypeEnum, email);
//        long tokenTtlSec = Constants.UC_ACCESSTOKEN_TTL_DEFAULT;
//        if (fromTypeEnum.getValue() == fromTypeEnum.MOBILE.getValue()) {
//            tokenTtlSec = Constants.UC_ACCESSTOKEN_TTL_MOBILE;
//        }
//        redisUtils.expired(Constants.USER_REDIS_INDEX, userTokenKey, tokenTtlSec);
//        redisUtils.expired(Constants.USER_REDIS_INDEX, userEmailKey, tokenTtlSec);
//        return user;
//    }
//
//    public void removeAccessToken(String accessToken) {
//        String key = userServiceHelper.getUserTokenKey(accessToken);
//        redisUtils.del(Constants.USER_REDIS_INDEX, key);
//    }
//
//    public void removeAllAccessTokenByEmail(String email) {
//        for (FromTypeEnum fromTypeEnum : FromTypeEnum.values()) {
//            String userEmailKey = userServiceHelper.getUserEmailKey(fromTypeEnum, email);
//            String tokenKey = redisUtils.get(Constants.USER_REDIS_INDEX, userEmailKey).toString();
//            if (tokenKey != null) {
//                tokenKey = userServiceHelper.getUserTokenKey(tokenKey);
//                redisUtils.del(Constants.USER_REDIS_INDEX, tokenKey);
//            }
//        }
//    }
//
//    /**
//     * 记录email试错次数。 每调用一次，将会+1.
//     * 
//     * @param email
//     * @return 一共试错了多少次
//     */
//    public int increaseWrongEmailAndPasswordNumber(String email) {
//        String key = Constants.UC_CAPTCHA_WRONGEMAILANDPASSWORD_PREFIX + email;
//        return redisUtils.incr(Constants.USER_REDIS_INDEX, key).intValue();
//    }
//
//    public int getWrongEmailAndPasswordNumber(String email) {
//        String key = Constants.UC_CAPTCHA_WRONGEMAILANDPASSWORD_PREFIX + email;
//        String value = redisUtils.get(Constants.USER_REDIS_INDEX, key).toString();
//        if (value == null) {
//            return 0;
//        }
//        return Integer.parseInt(value);
//    }
//
//    /**
//     * email 试错次数清零
//     * 
//     * @param email
//     */
//    public void cleanWrongEmailAndPasswordNumber(String email) {
//        String key = Constants.UC_CAPTCHA_WRONGEMAILANDPASSWORD_PREFIX + email;
//        redisUtils.del(Constants.USER_REDIS_INDEX, key);
//    }
//
//    /**
//     * 
//     * 如果email的密码重试次数超过锁定阀值，会锁定30分钟 这个方法返回如果账户锁定了，还有多少时间才可用
//     * 
//     * @return redisUtils.ttl,单位秒 -1 表示已经过了锁定时间 正数 表示还有多少时间才能解锁,单位秒
//     */
//    public Long getWrongEmailLockTimeRest(String email) {
//        String key = Constants.UC_CAPTCHA_WRONGEMAILANDPASSWORD_LOCKPREFIX + email;
//        return redisUtils.ttl(Constants.USER_REDIS_INDEX, key);
//    }
//
//    /**
//     * 记录密码重试次数超过锁定阀值的时间
//     * 
//     * @return 保存时的 redisUtils.ttl,单位秒
//     */
//    public void saveWrongEmailLockTime(String email) {
//        String key = Constants.UC_CAPTCHA_WRONGEMAILANDPASSWORD_LOCKPREFIX + email;
//        String value = String.valueOf(System.currentTimeMillis());
//        redisUtils.set(Constants.USER_REDIS_INDEX, key, value, Constants.UC_CAPTCHA_WRONGEMAILANDPASSWORD_LOCKTIME);
//    }

}
