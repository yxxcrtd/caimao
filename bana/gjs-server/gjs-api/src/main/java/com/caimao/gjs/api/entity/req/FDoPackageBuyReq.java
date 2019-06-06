package com.caimao.gjs.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**变更银行卡*/
public class FDoPackageBuyReq implements Serializable{
    /**用户编号*/
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    /**交易所代码*/
    @NotEmpty(message = "交易所代码不能为空")
    private String exchange;
    /**套餐编号*/
    @NotEmpty(message = "套餐编号不能为空")
    private String packageId;

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

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
}
