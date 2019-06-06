package com.caimao.bana.server.service.user;

import com.alibaba.fastjson.JSONObject;
import com.caimao.bana.api.exception.CustomerException;
import com.caimao.bana.server.utils.Constants;
import com.caimao.bana.server.utils.http.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 火币账户登陆的http请求的接口东东
 * Created by Administrator on 2015/12/15.
 */
@Component
public class HuobiUserLoginHelper {

    private final Logger logger = LoggerFactory.getLogger(HuobiUserLoginHelper.class);

    /**
     * 根据UID获取用户的手机号
     * @param uid       UID
     * @param loginIp   登陆IP
     * @return  用户手机号
     */
    public String getUserByUid(Long uid, String loginIp) throws Exception {
        try {
            String url = String.format(Constants.getMessage("HUOBILOGINURL") + "/getUserByUid.json?uid=%s&loginip=%s", uid, loginIp);
            String json = HttpHelper.doGet(url);
            logger.info("Huobi getUserByUid res json : " + json);

            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject.get("status").toString().equals("0")) {
                return jsonObject.get("data").toString();
            }
            return null;
        } catch (Exception e) {
            logger.error("Huobi getUserByUid exception : {}", e);
            throw new CustomerException("HB获取用户信息失败", 888888);
        }
    }

    /**
     * 根据手机号，获取用户的UID
     * @param phone     手机号
     * @param loginIp   登陆IP
     * @return  用户UID
     */
    public Long getUidByPhone(String phone, String loginIp) throws Exception {
        try {
            String url = String.format(Constants.getMessage("HUOBILOGINURL") + "/getUidByPhoneNo.json?phone=%s&loginip=%s", phone, loginIp);
            String json = HttpHelper.doGet(url);
            logger.info("Huobi getUidByPhone res json : " + json);

            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject.get("status").toString().equals("0")) {
                return Long.valueOf(jsonObject.get("data").toString());
            }
            return null;
        } catch (Exception e) {
            logger.error("Huobi getUidByPhone exception : {}", e);
            throw new CustomerException("HB获取用户信息失败", 888888);
        }
    }

    /**
     * 修改用户的密码
     * @param uid   uid
     * @param oldPwd    旧密码
     * @param newPwd    新密码
     * @param loginIp   登陆IP
     * @return  成功返回true，失败返回false
     */
    public boolean updateUserPwd(Long uid, String oldPwd, String newPwd, String loginIp) throws Exception {
        try {
            String baseAPI = Constants.getMessage("HUOBILOGINURL");
            String url = baseAPI + "/updateUserPasswd.json";
            Map<String, Object> params = new HashMap<>();
            params.put("uid", uid);
            params.put("passwordOld", oldPwd);
            params.put("passwordNew", newPwd);
            params.put("loginip", loginIp);

            String json = "";
            if (baseAPI.contains("https")) {
                json = HttpHelper.doPostSSL(url, params);
            } else {
                json = HttpHelper.doPost(url, params);
            }
            logger.info("Huobi updateUserPwd res json : " + json);

            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject.get("status").toString().equals("0")) {
                return Boolean.valueOf(jsonObject.get("data").toString());
            }
            return false;
        } catch (Exception e) {
            logger.error("Huobi updateUserPwd exception : {}", e);
            throw new CustomerException("HB更新密码失败", 888888);
        }
    }

    /**
     * 验证用户的密码
     * @param phone     手机号
     * @param pwd   密码
     * @param loginIp   登陆密码
     * @return  成功返回true，失败返回false
     */
    public Boolean validUserPwd(String phone, String pwd, String loginIp) throws Exception {
        try {
            String baseAPI = Constants.getMessage("HUOBILOGINURL");
            String url = baseAPI + "/validByPhoneNoAndPasswd.json";
            Map<String, Object> params = new HashMap<>();
            params.put("phone", phone);
            params.put("passwordNew", pwd);
            params.put("loginip", loginIp);

            String json = "";
            if (baseAPI.contains("https")) {
                json = HttpHelper.doPostSSL(url, params);
            } else {
                json = HttpHelper.doPost(url, params);
            }
            logger.info("Huobi validUserPwd res json : " + json);

            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject.get("status").toString().equals("0")) {
                return Boolean.valueOf(jsonObject.get("data").toString());
            }
            return false;
        } catch (Exception e) {
            logger.error("Huobi validUserPwd exception : {}", e);
            throw new CustomerException("HB账户或密码失败", 888888);
        }
    }

}
