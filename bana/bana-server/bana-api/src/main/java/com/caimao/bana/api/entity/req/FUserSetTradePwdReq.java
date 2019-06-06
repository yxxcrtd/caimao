package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.base.validated.Phone;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 安全密码设置请求
 * Created by WangXu on 2015/5/27.
 */
public class FUserSetTradePwdReq implements Serializable {

    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotBlank(message = "手机号不能为空")
    @Phone(message = "手机号格式错误")
    private String mobile;
    @NotBlank(message = "验证码不能为空")
    private String code;
    @NotBlank(message = "资金密码不能为空")
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
