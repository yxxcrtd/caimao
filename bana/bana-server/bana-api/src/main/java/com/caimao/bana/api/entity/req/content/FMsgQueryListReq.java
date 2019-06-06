package com.caimao.bana.api.entity.req.content;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.content.TpzPushMsgEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 查询用户消息的请求对象
 * Created by Administrator on 2015/7/24.
 */
public class FMsgQueryListReq extends QueryBase<TpzPushMsgEntity> {
    @NotNull(message = "用户ID不能为空")
    private String pushUserId;
    private String createDatetimeBegin;
    private String createDatetimeEnd;
    private String isRead;
    private String pushType;
    private String pushModel;
    private List<String> pushTypes;

    public String getPushModel() {
        return pushModel;
    }

    public void setPushModel(String pushModel) {
        this.pushModel = pushModel;
    }

    public String getPushUserId() {
        return pushUserId;
    }

    public void setPushUserId(String pushUserId) {
        this.pushUserId = pushUserId;
    }

    public String getCreateDatetimeBegin() {
        return createDatetimeBegin;
    }

    public void setCreateDatetimeBegin(String createDatetimeBegin) {
        this.createDatetimeBegin = createDatetimeBegin;
    }

    public String getCreateDatetimeEnd() {
        return createDatetimeEnd;
    }

    public void setCreateDatetimeEnd(String createDatetimeEnd) {
        this.createDatetimeEnd = createDatetimeEnd;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public List<String> getPushTypes() {
        return pushTypes;
    }

    public void setPushTypes(List<String> pushTypes) {
        this.pushTypes = pushTypes;
    }
}
