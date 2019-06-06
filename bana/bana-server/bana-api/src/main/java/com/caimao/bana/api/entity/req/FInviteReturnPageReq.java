package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.InviteReturnHistoryEntity;
import com.caimao.bana.api.entity.QueryBase;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户返佣记录
 */
public class FInviteReturnPageReq extends QueryBase<InviteReturnHistoryEntity> implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
