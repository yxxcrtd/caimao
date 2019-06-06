package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.base.validated.Phone;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 重置手机号的请求
 * Created by WangXu on 2015/5/27.
 */
public class FUserResetMobileReq implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotEmpty(message = "手机号不能为空")
    @Phone(message = "手机号格式错误")
    private String mobile;
    @NotEmpty(message = "验证码不能为空")
    private String checkCode;
    @NotEmpty(message = "资金密码错误")
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

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }
}
