package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.TpzAccountJourEntity;
import com.caimao.bana.api.entity.TpzHomsAccountJourEntity;

import java.io.Serializable;

public class FTPZQueryUserAccountJourReq extends QueryBase<TpzAccountJourEntity> implements Serializable {
    private Long userId;
    private String userName;
    private String mobile;
    private String accountBizType;
    private String dateStart;
    private String dateEnd;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getAccountBizType() {
        return accountBizType;
    }

    public void setAccountBizType(String accountBizType) {
        this.accountBizType = accountBizType;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
}