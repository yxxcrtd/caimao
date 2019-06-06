package com.caimao.gjs.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class FDoBindAccountSJSReq implements Serializable{
    /**用户编号*/
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    /**交易员编号*/
    @NotEmpty(message = "交易员编号不能为空")
    private String traderId;
    /**交易员密码*/
    @NotEmpty(message = "交易员编号不能为空")
    private String traderPwd;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTraderId() {
        return traderId;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public String getTraderPwd() {
        return traderPwd;
    }

    public void setTraderPwd(String traderPwd) {
        this.traderPwd = traderPwd;
    }
}
