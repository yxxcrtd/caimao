package com.caimao.account.api.entity.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表数据实例
 * Created by WangXu on 2015/4/22.
 */
public class TpzUserEntity implements Serializable {
    private static final long serialVersionUID = 4536428988793186638L;
    private Long userId;
    private String userName;
    private String userRealName;
    private String userNickName;
    private Short userGrade;
    private Integer userScore;
    private String userPwd;
    private String userPwdStrength;
    private String email;
    private String mobile;
    private String idcardKind;
    private String idcard;
    private String userAddress;
    private Date registerDatetime;
    private String registerIp;
    private Date lastLoginDatetime;
    private String lastLoginIp;
    private Integer loginCount;
    private String userStatus;
    private Integer errorCount;
    private String isTrust;
    private String userInit;
    private String userKind;
    private Long refUserId;
    private Date registerDatetimeBegin;
    private Date registerDatetimeEnd;
    private Long caimaoId;

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

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public Short getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(Short userGrade) {
        this.userGrade = userGrade;
    }

    public Integer getUserScore() {
        return userScore;
    }

    public void setUserScore(Integer userScore) {
        this.userScore = userScore;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserPwdStrength() {
        return userPwdStrength;
    }

    public void setUserPwdStrength(String userPwdStrength) {
        this.userPwdStrength = userPwdStrength;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Date getRegisterDatetime() {
        return registerDatetime;
    }

    public void setRegisterDatetime(Date registerDatetime) {
        this.registerDatetime = registerDatetime;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public Date getLastLoginDatetime() {
        return lastLoginDatetime;
    }

    public void setLastLoginDatetime(Date lastLoginDatetime) {
        this.lastLoginDatetime = lastLoginDatetime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getIsTrust() {
        return isTrust;
    }

    public void setIsTrust(String isTrust) {
        this.isTrust = isTrust;
    }

    public String getUserInit() {
        return userInit;
    }

    public void setUserInit(String userInit) {
        this.userInit = userInit;
    }

    public String getUserKind() {
        return userKind;
    }

    public void setUserKind(String userKind) {
        this.userKind = userKind;
    }

    public Long getRefUserId() {
        return refUserId;
    }

    public void setRefUserId(Long refUserId) {
        this.refUserId = refUserId;
    }

    public Date getRegisterDatetimeBegin() {
        return registerDatetimeBegin;
    }

    public void setRegisterDatetimeBegin(Date registerDatetimeBegin) {
        this.registerDatetimeBegin = registerDatetimeBegin;
    }

    public Date getRegisterDatetimeEnd() {
        return registerDatetimeEnd;
    }

    public void setRegisterDatetimeEnd(Date registerDatetimeEnd) {
        this.registerDatetimeEnd = registerDatetimeEnd;
    }

    public Long getCaimaoId() {
        return caimaoId;
    }

    public void setCaimaoId(Long caimaoId) {
        this.caimaoId = caimaoId;
    }
}
