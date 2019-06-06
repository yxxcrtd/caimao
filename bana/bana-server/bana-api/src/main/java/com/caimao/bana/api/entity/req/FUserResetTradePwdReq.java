package com.caimao.bana.api.entity.req;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户重置资金密码的请求
 * Created by WangXu on 2015/5/27.
 */
public class FUserResetTradePwdReq implements Serializable {

    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotBlank(message = "旧资金密码不能为空")
    private String oldTradePwd;
    @NotBlank(message = "新资金密码不能为空")
    private String newTradePwd;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOldTradePwd() {
        return oldTradePwd;
    }

    public void setOldTradePwd(String oldTradePwd) {
        this.oldTradePwd = oldTradePwd;
    }

    public String getNewTradePwd() {
        return newTradePwd;
    }

    public void setNewTradePwd(String newTradePwd) {
        this.newTradePwd = newTradePwd;
    }
}
