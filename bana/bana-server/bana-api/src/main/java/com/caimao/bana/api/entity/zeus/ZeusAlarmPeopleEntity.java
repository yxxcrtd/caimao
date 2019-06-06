package com.caimao.bana.api.entity.zeus;

import java.io.Serializable;

/**
 * 报警通知人数据对象
 * Created by Administrator on 2015/8/19.
 */
public class ZeusAlarmPeopleEntity implements Serializable {

    private String key;
    private String name;
    private String emails;
    private String smss;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getSmss() {
        return smss;
    }

    public void setSmss(String smss) {
        this.smss = smss;
    }
}
