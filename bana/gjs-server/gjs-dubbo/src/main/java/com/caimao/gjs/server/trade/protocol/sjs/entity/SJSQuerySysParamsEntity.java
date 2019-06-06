package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 查询系统参数
 */
public class SJSQuerySysParamsEntity implements Serializable {
    private String para_id; //参数编号
    private String para_value; //参数值

    public String getPara_id() {
        return para_id;
    }

    public void setPara_id(String para_id) {
        this.para_id = para_id;
    }

    public String getPara_value() {
        return para_value;
    }

    public void setPara_value(String para_value) {
        this.para_value = para_value;
    }
}