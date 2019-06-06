package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 下限价单
 */
public class SJSDoEntrustLimitEntity implements Serializable {
    private String local_order_no; //委托编号

    public String getLocal_order_no() {
        return local_order_no;
    }

    public void setLocal_order_no(String local_order_no) {
        this.local_order_no = local_order_no;
    }
}