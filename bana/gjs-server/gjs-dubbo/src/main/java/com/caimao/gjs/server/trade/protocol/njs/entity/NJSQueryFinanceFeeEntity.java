package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询融资费用
 */
public class NJSQueryFinanceFeeEntity implements Serializable {
    private String TRADEDATE; //交易日期
    private String FINANMONEY; //融资金额
    private String FINANCOMM; //融资手续费

    public String getTRADEDATE() {
        return TRADEDATE;
    }

    public void setTRADEDATE(String TRADEDATE) {
        this.TRADEDATE = TRADEDATE;
    }

    public String getFINANMONEY() {
        return FINANMONEY;
    }

    public void setFINANMONEY(String FINANMONEY) {
        this.FINANMONEY = FINANMONEY;
    }

    public String getFINANCOMM() {
        return FINANCOMM;
    }

    public void setFINANCOMM(String FINANCOMM) {
        this.FINANCOMM = FINANCOMM;
    }
}