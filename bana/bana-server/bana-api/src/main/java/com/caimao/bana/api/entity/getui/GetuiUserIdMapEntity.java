package com.caimao.bana.api.entity.getui;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
* GetuiUserIdMapEntity 实例对象
*
* Created by Xavier on 2015-11-04 16:11:36 星期三
*/
public class GetuiUserIdMapEntity implements Serializable {

    /** 自增主键 */
    private Long id;
    /** 用户ID */
    @NotNull
    private Long userId;
    /** 个推的推送设备cid */
    @NotNull
    private String cid;
    /** ios的deviceToken */
    private String deviceToken;
    /** 设备类型 */
    private String deviceType;
    /** 更新时间 */
    private Date timeUpdate;
    /** 创建时间 */
    private Date timeCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Date getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(Date timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    public Date getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Date timeCreate) {
        this.timeCreate = timeCreate;
    }
}