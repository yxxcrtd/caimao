package com.caimao.bana.api.entity.activity;

import java.io.Serializable;
import java.util.Date;

public class BanaLoseRPRecordEntity implements Serializable {
    private Long id;
    private Long userId;
    private Long money;
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

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}