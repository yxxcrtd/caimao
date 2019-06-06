package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSDoEntrustLimitReq implements Serializable{
    private String prod_code; //合约代码
    private String exch_type; //交易类型
    private String entr_price; //委托价格
    private String entr_amount; //委托数量
    private String client_serial_no; //客户端流水号

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getExch_type() {
        return exch_type;
    }

    public void setExch_type(String exch_type) {
        this.exch_type = exch_type;
    }

    public String getEntr_price() {
        return entr_price;
    }

    public void setEntr_price(String entr_price) {
        this.entr_price = entr_price;
    }

    public String getEntr_amount() {
        return entr_amount;
    }

    public void setEntr_amount(String entr_amount) {
        this.entr_amount = entr_amount;
    }

    public String getClient_serial_no() {
        return client_serial_no;
    }

    public void setClient_serial_no(String client_serial_no) {
        this.client_serial_no = client_serial_no;
    }
}
