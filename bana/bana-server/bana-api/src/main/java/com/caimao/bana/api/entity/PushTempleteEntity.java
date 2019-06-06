package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

public class PushTempleteEntity implements Serializable {
    private static final long serialVersionUID = -7409896635627499119L;
    private Long templeteId;
    private String templeteType;
    private String templeteContent;
    private Date createDatetime;
    private Date updateDatetime;
    private String resultType;

    public Long getTempleteId() {
        return this.templeteId;
    }

    public void setTempleteId(Long templeteId) {
        this.templeteId = templeteId;
    }

    public String getTempleteType() {
        return this.templeteType;
    }

    public void setTempleteType(String templeteType) {
        this.templeteType = templeteType;
    }

    public String getTempleteContent() {
        return this.templeteContent;
    }

    public void setTempleteContent(String templeteContent) {
        this.templeteContent = templeteContent;
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

    public String getResultType() {
        return this.resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}