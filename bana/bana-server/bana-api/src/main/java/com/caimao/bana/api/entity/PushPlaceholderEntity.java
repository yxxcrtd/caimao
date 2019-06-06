package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

public class PushPlaceholderEntity implements Serializable {
    private static final long serialVersionUID = -6022998867200235474L;
    private Long placeholderId;
    private String placeholderKey;
    private String placeholderType;
    private String placeholderValue;
    private Date createDatetime;
    private Date updateDatetime;
    private String remark;

    public Long getPlaceholderId() {
        return this.placeholderId;
    }

    public void setPlaceholderId(Long placeholderId) {
        this.placeholderId = placeholderId;
    }

    public String getPlaceholderKey() {
        return this.placeholderKey;
    }

    public void setPlaceholderKey(String placeholderKey) {
        this.placeholderKey = placeholderKey;
    }

    public String getPlaceholderType() {
        return this.placeholderType;
    }

    public void setPlaceholderType(String placeholderType) {
        this.placeholderType = placeholderType;
    }

    public String getPlaceholderValue() {
        return this.placeholderValue;
    }

    public void setPlaceholderValue(String placeholderValue) {
        this.placeholderValue = placeholderValue;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}