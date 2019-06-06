package com.caimao.bana.api.entity.res;

import java.io.Serializable;
import java.util.Date;

/**
 * SMS短消息查询Res
 */
public class FSmsListRes implements Serializable {

    private Long id;

    private String mobile;

    private Date createDatetime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}