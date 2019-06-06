package com.caimao.bana.api.entity.req;

import com.caimao.bana.common.api.base.validated.CheckPwd;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 重置用户登陆密码
 * Created by WangXu on 2015/5/27.
 */
public class FUserResetLoginPwdReq implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotBlank(message = "旧密码不能为空")
    private String oldPwd;
    @NotBlank(message = "新密码不能为空")
    @CheckPwd(message = "密码格式不正确，请输入8-20位字母、数字或特殊字符，不能为纯数字。")
    private String newPwd;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
