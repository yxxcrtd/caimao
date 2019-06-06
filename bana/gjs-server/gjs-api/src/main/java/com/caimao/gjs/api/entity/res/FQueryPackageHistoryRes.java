package com.caimao.gjs.api.entity.res;

import java.io.Serializable;

/**
 * 查询套餐历史记录
 */
public class FQueryPackageHistoryRes implements Serializable {
    /**申请时间*/
    private String datetime;
    /**流水号*/
    private String orderId;
    /**套餐编号*/
    private String packageId;
    /**套餐名称*/
    private String packageName;
    /**套餐总量*/
    private String contMoney;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getContMoney() {
        return contMoney;
    }

    public void setContMoney(String contMoney) {
        this.contMoney = contMoney;
    }
}