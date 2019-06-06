package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

public class StockPoolEntity implements Serializable {
    private static final long serialVersionUID = -4151530603588381374L;
    private Long stockPoolNo;
    private String stockPoolName;
    private String stockBlackFlag;
    private Date createDatetime;
    private Date updateDatetime;

    public Long getStockPoolNo() {
        return this.stockPoolNo;
    }

    public void setStockPoolNo(Long stockPoolNo) {
        this.stockPoolNo = stockPoolNo;
    }

    public String getStockPoolName() {
        return this.stockPoolName;
    }

    public void setStockPoolName(String stockPoolName) {
        this.stockPoolName = stockPoolName;
    }

    public String getStockBlackFlag() {
        return this.stockBlackFlag;
    }

    public void setStockBlackFlag(String stockBlackFlag) {
        this.stockBlackFlag = stockBlackFlag;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}