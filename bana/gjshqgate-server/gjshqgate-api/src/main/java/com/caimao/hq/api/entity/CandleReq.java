package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/20.
 */
public class CandleReq implements Serializable {


    public String prodCode;
    public String beginDate;
    public String exchange;
    public String endDate;
    public String cycle;
    public int number;
    public String callback;
    public String retParaKeys;
    public String objParaKeys;
    public String searchDirection;//1 表示向前查找（默认值） ，2 表示向后查找。

    public String getSearchDirection() {
        return searchDirection;
    }

    public void setSearchDirection(String searchDirection) {
        this.searchDirection = searchDirection;
    }

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

    public String getProdCode() {
        return prodCode;
    }

    public String getCallback() {
        return callback;
    }


    public void setCallback(String callback) {
        this.callback = callback;
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


    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
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
}
