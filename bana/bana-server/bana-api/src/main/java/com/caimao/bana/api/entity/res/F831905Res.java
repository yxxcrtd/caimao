package com.caimao.bana.api.entity.res;

public class F831905Res {
    private Long id;
    private String mobile;
    private String smsContent;
    private String smsStatus;
    private String smsType;
    private String sendDatetime;
    private String createDatetime;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsContent() {
        return this.smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getSmsStatus() {
        return this.smsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        this.smsStatus = smsStatus;
    }

    public String getSmsType() {
        return this.smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getSendDatetime() {
        return this.sendDatetime;
    }

    public void setSendDatetime(String sendDatetime) {
        // /* 65 */ this.sendDatetime = DateUtil.convertDateNumToStr(sendDatetime);
        this.sendDatetime = sendDatetime;
    }

    public String getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        // /* 73 */ this.createDatetime = DateUtil.convertDateNumToStr(createDatetime);
        this.createDatetime = createDatetime;
    }
}
