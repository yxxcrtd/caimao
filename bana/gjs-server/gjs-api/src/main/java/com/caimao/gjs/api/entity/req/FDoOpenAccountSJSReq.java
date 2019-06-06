package com.caimao.gjs.api.entity.req;

import com.caimao.bana.common.api.base.validated.IDCard;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class FDoOpenAccountSJSReq implements Serializable{
    /**用户编号*/
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    /**用户姓名*/
    @NotEmpty(message = "用户姓名不能为空")
    private String realName;
    /**身份证号码*/
    @NotEmpty(message = "身份证号码不能为空")
    @IDCard()
    private String idCard;
    /**银行编码*/
    @NotEmpty(message = "银行编码不能为空")
    private String bankNo;
    /**银行卡号*/
    @Pattern(regexp = "[0-9]+", message = "银行卡格式错误，银行卡号码不正确")
    private String bankCard;
    /**交易密码*/
    @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$", message = "交易密码格式错误，请输入6-10位字母或数字")
    private String tradePwd;
    /**资金密码*/
    @Pattern(regexp = "^[0-9]{6}$", message = "资金密码格式错误，请输入6位数字")
    private String fundsPwd;
    /**风险评估*/
    @NotEmpty(message = "风险评估不能为空")
    private String risk;
    /**短信验证码*/
    private String smsCode;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }

    public String getFundsPwd() {
        return fundsPwd;
    }

    public void setFundsPwd(String fundsPwd) {
        this.fundsPwd = fundsPwd;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
