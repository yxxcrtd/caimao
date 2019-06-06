/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.content;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户推送消息信息
 */
public class TpzPushMsgEntity implements Serializable {
    private static final long serialVersionUID = 8009784627830474585L;
    private Long pushMsgId;
    private String pushModel;
    private String pushType;
    private String pushMsgKind;
    private String pushMsgTitle;
    private String pushMsgDigest;
    private String pushExtend;
    private String pushUserId;
    private Date createDatetime;
    private String isRead;
    private String typeStr;

    public Long getPushMsgId() {
        return this.pushMsgId;
    }

    public void setPushMsgId(Long pushMsgId) {
        this.pushMsgId = pushMsgId;
    }

    public String getPushModel() {
        return this.pushModel;
    }

    public void setPushModel(String pushModel) {
        this.pushModel = pushModel;
    }

    public String getPushType() {
        return this.pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getPushMsgKind() {
        return this.pushMsgKind;
    }

    public void setPushMsgKind(String pushMsgKind) {
        this.pushMsgKind = pushMsgKind;
    }

    public String getPushMsgTitle() {
        return this.pushMsgTitle;
    }

    public void setPushMsgTitle(String pushMsgTitle) {
        this.pushMsgTitle = pushMsgTitle;
    }

    public String getPushMsgDigest() {
        return this.pushMsgDigest;
    }

    public void setPushMsgDigest(String pushMsgDigest) {
        this.pushMsgDigest = pushMsgDigest;
    }

    public String getPushExtend() {
        return this.pushExtend;
    }

    public void setPushExtend(String pushExtend) {
        this.pushExtend = pushExtend;
    }

    public String getPushUserId() {
        return this.pushUserId;
    }

    public void setPushUserId(String pushUserId) {
        this.pushUserId = pushUserId;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getIsRead() {
        return this.isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }
}
