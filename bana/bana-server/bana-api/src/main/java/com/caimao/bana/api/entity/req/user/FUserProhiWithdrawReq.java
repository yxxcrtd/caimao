package com.caimao.bana.api.entity.req.user;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 禁止用户提现的操作请求
 * Created by Administrator on 2015/8/14.
 */
public class FUserProhiWithdrawReq implements Serializable {
    @NotNull(message = "请设置用户ID")
    private Long userId;
    @NotBlank(message = "变动类型不能为空")
    private String type;
    @NotBlank(message = "变动值不能为空")
    private String status;
    @NotBlank(message = "变动描述不能为空")
    private String memo;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
