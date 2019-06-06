package com.caimao.bana.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加P2P借贷的申请记录请求对象
 * Created by WangXu on 2015/6/8.
 */
public class FP2PAddLoanReq implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;    // 用户ID
    private Long prodId;    // 融资的产品ID
    private Long margin;    // 支付保证金
    private Integer lever;  // 杠杆倍数
    private Long loanValue; // 借贷金额
    private Long caimaoValue;   // 财猫出资金额
    private Integer peroid; // 借贷周期
    private double customRate;    // 自定义费率
    @NotEmpty(message = "安全密码不能为空")
    private String tradePwd;    // 安全密码

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Long getMargin() {
        return margin;
    }

    public void setMargin(Long margin) {
        this.margin = margin;
    }

    public Integer getLever() {
        return lever;
    }

    public void setLever(Integer lever) {
        this.lever = lever;
    }

    public Long getLoanValue() {
        return loanValue;
    }

    public void setLoanValue(Long loanValue) {
        this.loanValue = loanValue;
    }

    public Long getCaimaoValue() {
        return caimaoValue;
    }

    public void setCaimaoValue(Long caimaoValue) {
        this.caimaoValue = caimaoValue;
    }

    public Integer getPeroid() {
        return peroid;
    }

    public void setPeroid(Integer peroid) {
        this.peroid = peroid;
    }

    public double getCustomRate() {
        return customRate;
    }

    public void setCustomRate(double customRate) {
        this.customRate = customRate;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }
}
