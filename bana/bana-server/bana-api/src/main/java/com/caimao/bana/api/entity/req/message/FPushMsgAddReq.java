package com.caimao.bana.api.entity.req.message;

import java.io.Serializable;
import java.util.Date;

/**
 * 站内信的添加请求
 * Created by Administrator on 2015/11/25.
 */
public class FPushMsgAddReq implements Serializable {

    private String pushModel;
    private String pushType;
    private String pushMsgKind;
    private String pushMsgTitle;
    private String pushMsgContent;
    private String pushMsgDigest;
    private String pushExtend;
    private String pushUserId;
    private Date createDatetime;
    private String isRead;

    public String getPushModel() {
        return pushModel;
    }

    public void setPushModel(String pushModel) {
        this.pushModel = pushModel;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getPushMsgKind() {
        return pushMsgKind;
    }

    public void setPushMsgKind(String pushMsgKind) {
        this.pushMsgKind = pushMsgKind;
    }

    public String getPushMsgTitle() {
        return pushMsgTitle;
    }

    public void setPushMsgTitle(String pushMsgTitle) {
        this.pushMsgTitle = pushMsgTitle;
    }

    public String getPushMsgContent() {
        return pushMsgContent;
    }

    public void setPushMsgContent(String pushMsgContent) {
        this.pushMsgContent = pushMsgContent;
    }

    public String getPushMsgDigest() {
        return pushMsgDigest;
    }

    public void setPushMsgDigest(String pushMsgDigest) {
        this.pushMsgDigest = pushMsgDigest;
    }

    public String getPushExtend() {
        return pushExtend;
    }

    public void setPushExtend(String pushExtend) {
        this.pushExtend = pushExtend;
    }

    public String getPushUserId() {
        return pushUserId;
    }

    public void setPushUserId(String pushUserId) {
        this.pushUserId = pushUserId;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
