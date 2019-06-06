package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 查询资金
 */
public class SJSQueryFundsEntity implements Serializable {
    private String user_id; //客户编号
    private String currency_id; //币种
    private String curr_bal; //余额
    private String curr_can_use; //可用资金
    private String curr_can_get; //可提资金
    private String in_bal; //当日入金
    private String out_bal; //当日出金
    private String real_buy; //买入金额
    private String real_sell; //卖出金额
    private String exch_froz_bal; //交易冻结资金
    private String posi_margin; //持仓保证金
    private String base_margin; //开户保证金
    private String take_margin; //提货保证金
    private String stor_margin; //仓储费保证金
    private String pt_reserve; //铂金冻结资金
    private String ag_margin; //白银货款冻结
    private String forward_froz; //远期浮亏冻结
    private String exch_fare; //交易手续费
    private String float_surplus; //浮动盈亏

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getCurr_bal() {
        return curr_bal;
    }

    public void setCurr_bal(String curr_bal) {
        this.curr_bal = curr_bal;
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

    public String getIn_bal() {
        return in_bal;
    }

    public void setIn_bal(String in_bal) {
        this.in_bal = in_bal;
    }

    public String getOut_bal() {
        return out_bal;
    }

    public void setOut_bal(String out_bal) {
        this.out_bal = out_bal;
    }

    public String getReal_buy() {
        return real_buy;
    }

    public void setReal_buy(String real_buy) {
        this.real_buy = real_buy;
    }

    public String getReal_sell() {
        return real_sell;
    }

    public void setReal_sell(String real_sell) {
        this.real_sell = real_sell;
    }

    public String getExch_froz_bal() {
        return exch_froz_bal;
    }

    public void setExch_froz_bal(String exch_froz_bal) {
        this.exch_froz_bal = exch_froz_bal;
    }

    public String getPosi_margin() {
        return posi_margin;
    }

    public void setPosi_margin(String posi_margin) {
        this.posi_margin = posi_margin;
    }

    public String getBase_margin() {
        return base_margin;
    }

    public void setBase_margin(String base_margin) {
        this.base_margin = base_margin;
    }

    public String getTake_margin() {
        return take_margin;
    }

    public void setTake_margin(String take_margin) {
        this.take_margin = take_margin;
    }

    public String getStor_margin() {
        return stor_margin;
    }

    public void setStor_margin(String stor_margin) {
        this.stor_margin = stor_margin;
    }

    public String getPt_reserve() {
        return pt_reserve;
    }

    public void setPt_reserve(String pt_reserve) {
        this.pt_reserve = pt_reserve;
    }

    public String getAg_margin() {
        return ag_margin;
    }

    public void setAg_margin(String ag_margin) {
        this.ag_margin = ag_margin;
    }

    public String getForward_froz() {
        return forward_froz;
    }

    public void setForward_froz(String forward_froz) {
        this.forward_froz = forward_froz;
    }

    public String getExch_fare() {
        return exch_fare;
    }

    public void setExch_fare(String exch_fare) {
        this.exch_fare = exch_fare;
    }

    public String getFloat_surplus() {
        return float_surplus;
    }

    public void setFloat_surplus(String float_surplus) {
        this.float_surplus = float_surplus;
    }
}