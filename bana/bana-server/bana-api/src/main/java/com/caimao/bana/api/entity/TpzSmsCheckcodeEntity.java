package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

public class TpzSmsCheckcodeEntity implements Serializable {
    private static final long serialVersionUID = -7000469445990740501L;
    private Long id;

    private String mobile;

    private String smsBizType;

    private String checkCode;

    private Date checkcodeSetDatetime;

    private Date lastCheckDatetime;

    private Date invalidDatetime;

    private Integer checkTimes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsBizType() {
        return smsBizType;
    }

    public void setSmsBizType(String smsBizType) {
        this.smsBizType = smsBizType;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public Date getCheckcodeSetDatetime() {
        return checkcodeSetDatetime;
    }

    public void setCheckcodeSetDatetime(Date checkcodeSetDatetime) {
        this.checkcodeSetDatetime = checkcodeSetDatetime;
    }

    public Date getLastCheckDatetime() {
        return lastCheckDatetime;
    }

    public void setLastCheckDatetime(Date lastCheckDatetime) {
        this.lastCheckDatetime = lastCheckDatetime;
    }

    public Date getInvalidDatetime() {
        return invalidDatetime;
    }

    public void setInvalidDatetime(Date invalidDatetime) {
        this.invalidDatetime = invalidDatetime;
    }

    public Integer getCheckTimes() {
        return checkTimes;
    }

    public void setCheckTimes(Integer checkTimes) {
        this.checkTimes = checkTimes;
    }
}