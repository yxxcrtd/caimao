package com.caimao.gjs.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class FDoResetTradePwdReq implements Serializable{
    /**用户编号*/
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    /**交易所代码*/
    @NotEmpty(message = "交易所代码不能为空")
    private String exchange;
    /**短信验证码*/
    @NotEmpty(message = "短信验证码不能为空")
    private String smsCode;
    /**新密码*/
    @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$", message = "交易密码格式错误，请输入6-10位字母或数字")
    private String newPwd;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
