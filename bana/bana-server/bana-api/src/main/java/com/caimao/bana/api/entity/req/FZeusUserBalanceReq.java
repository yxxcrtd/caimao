package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.zeus.BanaHomsFinanceHistoryEntity;

import java.io.Serializable;

/**
 * 后台查询用户资产列表
 */
public class FZeusUserBalanceReq extends QueryBase<TpzAccountEntity> implements Serializable {
    private Long userId;
    private String userRealName;
    private String mobile;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
