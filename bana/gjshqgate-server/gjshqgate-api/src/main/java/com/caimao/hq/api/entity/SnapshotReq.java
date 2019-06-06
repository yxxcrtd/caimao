package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/20.
 */
public class SnapshotReq implements Serializable {

    public String prodCode;
    public String callback;
    public  String exchange;
    public String retParaKeys;
    public String objParaKeys;

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getProdCode() {
        return prodCode;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRetParaKeys() {
        return retParaKeys;
    }

    public void setRetParaKeys(String retParaKeys) {
        this.retParaKeys = retParaKeys;
    }

    public String getObjParaKeys() {
        return objParaKeys;
    }

    public void setObjParaKeys(String objParaKeys) {
        this.objParaKeys = objParaKeys;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
