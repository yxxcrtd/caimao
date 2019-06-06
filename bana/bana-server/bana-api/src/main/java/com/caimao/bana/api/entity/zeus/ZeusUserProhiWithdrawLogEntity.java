package com.caimao.bana.api.entity.zeus;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户禁止提现状态历史表
 * Created by Administrator on 2015/8/14.
 */
public class ZeusUserProhiWithdrawLogEntity implements Serializable {
    private Long id;
    private Long userId;
    private String type;
    private String status;
    private String memo;
    private Date created;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
