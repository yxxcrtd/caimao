package com.caimao.bana.api.entity.zeus;

import java.io.Serializable;
import java.util.Date;

/**
 * 有持仓可以进行还款的排除列表
 * Created by WangXu on 2015/7/16.
 */
public class ZeusHomsRepaymentExcludeEntity implements Serializable {
    private Long userId;
    private String userRealName;
    private String mobile;
    private String homsFundAccount;
    private String homsCombineId;
    private Date created;


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

    public String getHomsFundAccount() {
        return homsFundAccount;
    }

    public void setHomsFundAccount(String homsFundAccount) {
        this.homsFundAccount = homsFundAccount;
    }

    public String getHomsCombineId() {
        return homsCombineId;
    }

    public void setHomsCombineId(String homsCombineId) {
        this.homsCombineId = homsCombineId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
