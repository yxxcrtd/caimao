package com.caimao.bana.api.entity.res;

import java.util.Date;

public class F830917Res {
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

    public String toString() {
        return "F830917Res [id=" + this.id + ", infoChannel=" + this.infoChannel + ", infoTitle=" + this.infoTitle
                + ", createDatetime=" + this.createDatetime + ", infoContent=" + this.infoContent + "]";
    }
}
