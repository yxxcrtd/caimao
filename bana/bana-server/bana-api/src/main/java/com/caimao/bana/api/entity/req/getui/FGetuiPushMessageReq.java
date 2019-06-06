package com.caimao.bana.api.entity.req.getui;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 个推发送请求的对象
 * Created by Administrator on 2015/11/4.
 */
public class FGetuiPushMessageReq implements Serializable {
    /** 用户ID */
    @NotNull
    private Long userId;
    /** 动作类型：1打开应用，2打开url，3透传消息 */
    private String actionType;
    /** 推送的消息来源，只做标记使用 */
    private String source;
    /** 推送的消息标题 */
    @NotNull
    private String title;
    /** 推送的消息内容 */
    @NotNull
    private String content;
    /** 打开的url */
    private String url;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
