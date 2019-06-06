package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.base.validated.Phone;
import com.caimao.bana.common.api.base.validated.CheckPwd;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户注册请求参数对象
 * Created by WangXu on 2015/5/27.
 */
public class FUserRegisterReq implements Serializable {
    private String userName;
    @NotEmpty(message = "手机号不能为空")
    @Phone(message = "手机号格式错误")
    private String mobile;

    @NotEmpty(message = "密码不能为空")
    @CheckPwd(message = "密码格式不正确，请输入8-20位字母、数字或特殊字符，不能为纯数字。")
    private String userPwd;

    @NotBlank(message = "注册ip不能为空")
    private String registerIp;

    @NotEmpty(message = "用户来源不能为空")
    private String userInit;

    private Long refUserId;
    @NotBlank(message = "短信校验码不能为空")
    @Size(min = 6, max = 6, message = "校验码格式错误")
    private String checkCode;

    private String qq;

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public String getUserInit() {
        return userInit;
    }

    public void setUserInit(String userInit) {
        this.userInit = userInit;
    }

    public Long getRefUserId() {
        return refUserId;
    }

    public void setRefUserId(Long refUserId) {
        this.refUserId = refUserId;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
}
