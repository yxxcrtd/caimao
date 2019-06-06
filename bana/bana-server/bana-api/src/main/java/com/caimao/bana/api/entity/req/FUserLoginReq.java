package com.caimao.bana.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户登陆请求
 * Created by WangXu on 2015/5/27.
 */
public class FUserLoginReq implements Serializable {

    @NotEmpty(message = "用户名不能为空")
    private String loginName;

    @NotNull(message = "登陆密码不能为空")
    private String loginPwd;

    private String source;

    @NotNull(message = "登陆IP不能为空")
    private String loginIP;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }
}
