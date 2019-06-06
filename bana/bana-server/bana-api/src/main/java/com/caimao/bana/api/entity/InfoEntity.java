package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

public class InfoEntity implements Serializable {
    private static final long serialVersionUID = 5222238807927444354L;
    private Long id;
    private String infoChannel;
    private String infoTitle;
    private Date createDatetime;
    private String infoContent;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfoChannel() {
        return this.infoChannel;
    }

    public void setInfoChannel(String infoChannel) {
        this.infoChannel = infoChannel;
    }

    public String getInfoTitle() {
        return this.infoTitle;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getInfoContent() {
        return this.infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }
}