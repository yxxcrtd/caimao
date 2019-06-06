package com.caimao.bana.api.entity.req;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户绑定&变更邮箱
 * Created by WangXu on 2015/5/27.
 */
public class FUserBindOrChangeEmailReq implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotEmpty(message = "请填写邮箱")
    @Email(message = "邮箱格式错误")
    private String email;

    @NotEmpty(message = "请填写资金密码")
    private String tradePwd;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }
}
