package com.caimao.gjs.api.entity.history;

import java.io.Serializable;

/**
 * NJS历史出入金
 *
 * Created by yangxinxin@huobi.com on 2015/10/22
 */
public class GjsNJSHistoryTransferEntity implements Serializable {
    private Long id;
    private String firm_id; // 交易商号
    private String change_type; // 变动类型：A入金；B出金；C质押；D解除质押；E银行入金；F银行出金
    private String money_type; // 币种：0人民币；1美元；2港币
    private String change_money; // 变动金额（12,2）
    private String deal_date; // 处理日期 YYYYMMDD
    private String deal_time; // 处理时间，HHMMSS
    private String flag; // 出入金标识，A结算出入金；B在线出入金；C银行出入金
    private String style; // 资金状态：Y该笔记录正常参与计算；N该笔记录不正常不能参与计算
    private String user_id; // 管理员代码
    private String bank_water_id; // 出入金流水号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirm_id() {
        return firm_id;
    }

    public void setFirm_id(String firm_id) {
        this.firm_id = firm_id;
    }

    public String getChange_type() {
        return change_type;
    }

    public void setChange_type(String change_type) {
        this.change_type = change_type;
    }

    public String getMoney_type() {
        return money_type;
    }

    public void setMoney_type(String money_type) {
        this.money_type = money_type;
    }

    public String getChange_money() {
        return change_money;
    }

    public void setChange_money(String change_money) {
        this.change_money = change_money;
    }

    public String getDeal_date() {
        return deal_date;
    }

    public void setDeal_date(String deal_date) {
        this.deal_date = deal_date;
    }

    public String getDeal_time() {
        return deal_time;
    }

    public void setDeal_time(String deal_time) {
        this.deal_time = deal_time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBank_water_id() {
        return bank_water_id;
    }

    public void setBank_water_id(String bank_water_id) {
        this.bank_water_id = bank_water_id;
    }
}
