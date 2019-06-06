package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登陆历史记录表
 * Created by WangXu on 2015/5/18.
 */
public class TpzUserLoginLogEntity implements Serializable {
    private static final long serialVersionUID = -5742080949228948545L;
    private Long id;
    private Long userId;
    private Date loginDatetime;
    private String loginIp;
    private String isSuccess;
    private Integer source;

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

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

    public Date getLoginDatetime() {
        return loginDatetime;
    }

    public void setLoginDatetime(Date loginDatetime) {
        this.loginDatetime = loginDatetime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }
}
