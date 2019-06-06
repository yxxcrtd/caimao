package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

public class LoginHistoryEntity implements Serializable {

    private static final long serialVersionUID = -1647755585938356259L;
    private Long id;

    private Date loginTime;

    private String userIp;

    private String userAgent;

    private String loginReturncode;

    private String loginReturnmessage;

    private Long userId;

    private String country;

    private String province;

    private String city;

    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getLoginReturncode() {
        return loginReturncode;
    }

    public void setLoginReturncode(String loginReturncode) {
        this.loginReturncode = loginReturncode;
    }

    public String getLoginReturnmessage() {
        return loginReturnmessage;
    }

    public void setLoginReturnmessage(String loginReturnmessage) {
        this.loginReturnmessage = loginReturnmessage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}