package com.caimao.bana.api.entity.req;

import java.io.Serializable;

/**
 * 找回密保问题的验证
 * Created by Administrator on 2015/7/28.
 */
public class FUserFindQuestionCheckReq implements Serializable {
    private Long userId;
    private String mobile;
    private String code;
    private String tradePwd;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }
}
