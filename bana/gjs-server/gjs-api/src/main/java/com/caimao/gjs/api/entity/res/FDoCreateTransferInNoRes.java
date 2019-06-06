package com.caimao.gjs.api.entity.res;

import java.io.Serializable;

/**
 * 获取入金编号
 */
public class FDoCreateTransferInNoRes implements Serializable {
    /**商户编号*/
    private String MERID;
    /**订单编号*/
    private String ORDERNO;

    public String getMERID() {
        return MERID;
    }

    public void setMERID(String MERID) {
        this.MERID = MERID;
    }

    public String getORDERNO() {
        return ORDERNO;
    }

    public void setORDERNO(String ORDERNO) {
        this.ORDERNO = ORDERNO;
    }
}