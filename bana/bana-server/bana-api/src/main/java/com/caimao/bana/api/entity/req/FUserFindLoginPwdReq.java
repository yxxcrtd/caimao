package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.base.validated.Phone;
import com.caimao.bana.common.api.base.validated.CheckPwd;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 找回密码的请求对象
 * Created by WangXu on 2015/5/27.
 */
public class FUserFindLoginPwdReq implements Serializable {
    @Phone(message = "手机号格式错误")
    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    @NotEmpty(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码格式错误")
    private String checkCode;

    @NotEmpty(message = "密码不能为空")
    @CheckPwd(message = "密码格式不正确，请输入8-20位字母、数字或特殊字符，不能为纯数字。")
    private String userLoginPwd;

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

    public String getUserLoginPwd() {
        return userLoginPwd;
    }

    public void setUserLoginPwd(String userLoginPwd) {
        this.userLoginPwd = userLoginPwd;
    }
}
