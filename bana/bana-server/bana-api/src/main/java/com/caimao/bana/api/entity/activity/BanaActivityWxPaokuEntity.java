package com.caimao.bana.api.entity.activity;

import java.util.Date;
/**
 * 微信活动跑酷数据表
 * Created by WangXu on 2015/5/22.
 */
public class BanaActivityWxPaokuEntity {

    private int id;
    private String phone;
    private String pzValue;
    private String ip;
    private Date created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPzValue() {
        return pzValue;
    }

    public void setPzValue(String pzValue) {
        this.pzValue = pzValue;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
