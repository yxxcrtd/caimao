package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 查询风险
 */
public class SJSQueryRiskEntity implements Serializable {
    private String risk_degree; //风险度
    private String risk_grade; //风险等级
    private String margin_mem; //会员保证金
    private String margin_exch; //交易所保证金
    private String debt_call; //追保金额
    private String surplus; //浮动盈亏
    private String risk_curr_can_use; //可用资金
    private String f_in_out_bal; //今日出入金
    private String risk_deg_yda; //上日风险等级
    private String debt_call_yda; //上日追保金额
    private String risk_days; //连续风险天数

    public String getRisk_degree() {
        return risk_degree;
    }

    public void setRisk_degree(String risk_degree) {
        this.risk_degree = risk_degree;
    }

    public String getRisk_grade() {
        return risk_grade;
    }

    public void setRisk_grade(String risk_grade) {
        this.risk_grade = risk_grade;
    }

    public String getMargin_mem() {
        return margin_mem;
    }

    public void setMargin_mem(String margin_mem) {
        this.margin_mem = margin_mem;
    }

    public String getMargin_exch() {
        return margin_exch;
    }

    public void setMargin_exch(String margin_exch) {
        this.margin_exch = margin_exch;
    }

    public String getDebt_call() {
        return debt_call;
    }

    public void setDebt_call(String debt_call) {
        this.debt_call = debt_call;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getRisk_curr_can_use() {
        return risk_curr_can_use;
    }

    public void setRisk_curr_can_use(String risk_curr_can_use) {
        this.risk_curr_can_use = risk_curr_can_use;
    }

    public String getF_in_out_bal() {
        return f_in_out_bal;
    }

    public void setF_in_out_bal(String f_in_out_bal) {
        this.f_in_out_bal = f_in_out_bal;
    }

    public String getRisk_deg_yda() {
        return risk_deg_yda;
    }

    public void setRisk_deg_yda(String risk_deg_yda) {
        this.risk_deg_yda = risk_deg_yda;
    }

    public String getDebt_call_yda() {
        return debt_call_yda;
    }

    public void setDebt_call_yda(String debt_call_yda) {
        this.debt_call_yda = debt_call_yda;
    }

    public String getRisk_days() {
        return risk_days;
    }

    public void setRisk_days(String risk_days) {
        this.risk_days = risk_days;
    }
}