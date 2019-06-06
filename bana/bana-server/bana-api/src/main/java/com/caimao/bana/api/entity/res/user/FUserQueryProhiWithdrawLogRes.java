package com.caimao.bana.api.entity.res.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询用户禁止提现变更历史的日志
 * Created by Administrator on 2015/8/14.
 */
public class FUserQueryProhiWithdrawLogRes implements Serializable {
    private Long id;
    private Long userId;
    private String userName;
    private String mobile;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
