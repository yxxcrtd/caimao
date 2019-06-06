package com.caimao.gjs.server.trade.protocol.sjs.entity.res;

import java.io.Serializable;
import java.util.List;

public class FSJSBaseRes<T> implements Serializable {
    private String h_exch_code; //交易代码
    private String h_rsp_code; //响应代码
    private String h_rsp_msg; //响应消息
    private List<T> record; //数据体

    public String getH_exch_code() {
        return h_exch_code;
    }

    public void setH_exch_code(String h_exch_code) {
        this.h_exch_code = h_exch_code;
    }

    public String getH_rsp_code() {
        return h_rsp_code;
    }

    public void setH_rsp_code(String h_rsp_code) {
        this.h_rsp_code = h_rsp_code;
    }

    public String getH_rsp_msg() {
        return h_rsp_msg;
    }

    public void setH_rsp_msg(String h_rsp_msg) {
        this.h_rsp_msg = h_rsp_msg;
    }

    public List<T> getRecord() {
        return record;
    }

    public void setRecord(List<T> record) {
        this.record = record;
    }
}
