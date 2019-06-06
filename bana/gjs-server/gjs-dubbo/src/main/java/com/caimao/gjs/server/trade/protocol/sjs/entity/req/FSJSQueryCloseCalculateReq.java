package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSQueryCloseCalculateReq implements Serializable{
    private String user_id; //客户编号
    private String prod_code; //合约代码
    private String cov_bs; //平仓方向
    private String cov_price; //平仓价格
    private String cov_amount; //平仓数量

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getCov_bs() {
        return cov_bs;
    }

    public void setCov_bs(String cov_bs) {
        this.cov_bs = cov_bs;
    }

    public String getCov_price() {
        return cov_price;
    }

    public void setCov_price(String cov_price) {
        this.cov_price = cov_price;
    }

    public String getCov_amount() {
        return cov_amount;
    }

    public void setCov_amount(String cov_amount) {
        this.cov_amount = cov_amount;
    }
}
