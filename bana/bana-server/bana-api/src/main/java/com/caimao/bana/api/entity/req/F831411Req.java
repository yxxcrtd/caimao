/*
*F831411Req.java
*Created on 2015/5/16 11:20
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.api.entity.req;

import java.io.Serializable;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class F831411Req implements Serializable {

    private static final long serialVersionUID = -8778479422821860597L;

    private Long orderNo;

    private Long userId;

    private Long pzAccountId;

    private String createDatetimeBegin;

    private String createDatetimeEnd;

    private String orderStatus;

    private String verifyUser;

    private String verifyStatus;

    private String mobile;

    private String userRealName;

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public String getCreateDatetimeBegin() {
        return createDatetimeBegin;
    }

    public void setCreateDatetimeBegin(String createDatetimeBegin) {
        this.createDatetimeBegin = createDatetimeBegin;
    }

    public String getCreateDatetimeEnd() {
        return createDatetimeEnd;
    }

    public void setCreateDatetimeEnd(String createDatetimeEnd) {
        this.createDatetimeEnd = createDatetimeEnd;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(String verifyUser) {
        this.verifyUser = verifyUser;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }
}
