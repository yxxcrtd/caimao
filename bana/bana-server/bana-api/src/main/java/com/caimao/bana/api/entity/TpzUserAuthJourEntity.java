package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实名认证记录
 * Created by WangXu on 2015/5/18.
 */
public class TpzUserAuthJourEntity implements Serializable {
    private static final long serialVersionUID = 8753516944487757604L;
    private Long id;
    private Long userId;
    private String userRealName;
    private String idcardKind;
    private String idcard;
    private String authStatus;
    private String authFailReason;
    private String regDate;
    private String payAccountId;
    private String payUserId;
    private Date createDatetime;
    private Date updateDatetime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getIdcardKind() {
        return idcardKind;
    }

    public void setIdcardKind(String idcardKind) {
        this.idcardKind = idcardKind;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getAuthFailReason() {
        return authFailReason;
    }

    public void setAuthFailReason(String authFailReason) {
        this.authFailReason = authFailReason;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getPayAccountId() {
        return payAccountId;
    }

    public void setPayAccountId(String payAccountId) {
        this.payAccountId = payAccountId;
    }

    public String getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(String payUserId) {
        this.payUserId = payUserId;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
