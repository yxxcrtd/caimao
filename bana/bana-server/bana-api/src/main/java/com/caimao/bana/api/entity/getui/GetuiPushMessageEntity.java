package com.caimao.bana.api.entity.getui;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
* GetuiPushMessageEntity 实例对象
*
* Created by Xavier on 2015-11-04 16:14:49 星期三
*/
public class GetuiPushMessageEntity implements Serializable {

    /** 自增主键 */
    private Long id;
    /** 接收的设备cid */
    private String recvId;
    /** ios的deviceToken */
    private String deviceToken;
    /** 用户ID */
    @NotNull
    private Long userId;
    /** 接收id的类：1为cid，2为app，3为ios deviceToken */
    @NotNull
    private Integer recvIdType;
    /** 并发处理序号，每个服务器实例一个id，不能重复 */
    private Integer taskIndex;
    /** 设备类型 */
    private String deviceType;
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
    /** 消息状态：1初始化，2，正在发送，3，发送成功，4，发送失败 */
    private String status;
    /** 重试次数 */
    private Integer tryTimes;
    /** 推送成功的时间 */
    private Date timePush;
    /** 创建时间 */
    private Date timeCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecvId() {
        return recvId;
    }

    public void setRecvId(String recvId) {
        this.recvId = recvId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRecvIdType() {
        return recvIdType;
    }

    public void setRecvIdType(Integer recvIdType) {
        this.recvIdType = recvIdType;
    }

    public Integer getTaskIndex() {
        return taskIndex;
    }

    public void setTaskIndex(Integer taskIndex) {
        this.taskIndex = taskIndex;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(Integer tryTimes) {
        this.tryTimes = tryTimes;
    }

    public Date getTimePush() {
        return timePush;
    }

    public void setTimePush(Date timePush) {
        this.timePush = timePush;
    }

    public Date getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Date timeCreate) {
        this.timeCreate = timeCreate;
    }
}