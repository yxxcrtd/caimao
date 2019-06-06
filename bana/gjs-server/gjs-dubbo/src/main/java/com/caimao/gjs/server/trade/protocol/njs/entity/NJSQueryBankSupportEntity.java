package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询银行是否支持发起客户端签约
 */
public class NJSQueryBankSupportEntity implements Serializable {
    private String SUPPORT; //是否支持 Y支持 N不支持

    public String getSUPPORT() {
        return SUPPORT;
    }

    public void setSUPPORT(String SUPPORT) {
        this.SUPPORT = SUPPORT;
    }
}