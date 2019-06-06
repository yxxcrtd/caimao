package com.caimao.hq.api.entity;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/26.
 */
public class TradeAmountReq implements Serializable,Cloneable {

    public String prodCode;
    public String beginDate;
    public String endDate;
    public int number;

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String exchange;
    public int type;// 查询周期，  5表示  5日分时
    public String cycle;
    public String retParaKeys;
    public String objParaKeys;

    public String getObjParaKeys() {
        return objParaKeys;
    }

    public void setObjParaKeys(String objParaKeys) {
        this.objParaKeys = objParaKeys;
    }

    public String getRetParaKeys() {
        return retParaKeys;
    }

    public void setRetParaKeys(String retParaKeys) {
        this.retParaKeys = retParaKeys;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    public Object clone() throws CloneNotSupportedException {
        return SerializationUtils.clone(this);
    }
}
