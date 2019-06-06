package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 查询库存
 */
public class SJSQueryProdEntity implements Serializable {
    private String variety_id; //品种代码
    private String curr_amt; //当前库存
    private String curr_can_use; //可用库存
    private String curr_can_get; //可提库存
    private String day_deposit; //当日入库
    private String day_draw; //当日出库
    private String buy_amt; //成交买入
    private String sell_amt; //成交卖出
    private String entr_amt; //报单冻结
    private String app_froz_amt; //提货冻结
    private String impawn_in; //质权数量
    private String impawn_out; //出质数量
    private String storage_price; //库存均价

    public String getVariety_id() {
        return variety_id;
    }

    public void setVariety_id(String variety_id) {
        this.variety_id = variety_id;
    }

    public String getCurr_amt() {
        return curr_amt;
    }

    public void setCurr_amt(String curr_amt) {
        this.curr_amt = curr_amt;
    }

    public String getCurr_can_use() {
        return curr_can_use;
    }

    public void setCurr_can_use(String curr_can_use) {
        this.curr_can_use = curr_can_use;
    }

    public String getCurr_can_get() {
        return curr_can_get;
    }

    public void setCurr_can_get(String curr_can_get) {
        this.curr_can_get = curr_can_get;
    }

    public String getDay_deposit() {
        return day_deposit;
    }

    public void setDay_deposit(String day_deposit) {
        this.day_deposit = day_deposit;
    }

    public String getDay_draw() {
        return day_draw;
    }

    public void setDay_draw(String day_draw) {
        this.day_draw = day_draw;
    }

    public String getBuy_amt() {
        return buy_amt;
    }

    public void setBuy_amt(String buy_amt) {
        this.buy_amt = buy_amt;
    }

    public String getSell_amt() {
        return sell_amt;
    }

    public void setSell_amt(String sell_amt) {
        this.sell_amt = sell_amt;
    }

    public String getEntr_amt() {
        return entr_amt;
    }

    public void setEntr_amt(String entr_amt) {
        this.entr_amt = entr_amt;
    }

    public String getApp_froz_amt() {
        return app_froz_amt;
    }

    public void setApp_froz_amt(String app_froz_amt) {
        this.app_froz_amt = app_froz_amt;
    }

    public String getImpawn_in() {
        return impawn_in;
    }

    public void setImpawn_in(String impawn_in) {
        this.impawn_in = impawn_in;
    }

    public String getImpawn_out() {
        return impawn_out;
    }

    public void setImpawn_out(String impawn_out) {
        this.impawn_out = impawn_out;
    }

    public String getStorage_price() {
        return storage_price;
    }

    public void setStorage_price(String storage_price) {
        this.storage_price = storage_price;
    }
}