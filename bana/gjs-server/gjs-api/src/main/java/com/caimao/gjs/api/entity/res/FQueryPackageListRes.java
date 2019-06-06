package com.caimao.gjs.api.entity.res;

import java.io.Serializable;
import java.util.List;

/**
 * 查询套餐列表
 */
public class FQueryPackageListRes implements Serializable {
    /**套餐编号*/
    private String packageId;
    /**套餐名称*/
    private String packageName;
    /**成交额*/
    private String contMoney;
    /**套餐费用*/
    private String comm;
    /**有效期限*/
    private String timeLimit;
    /**详细说明*/
    private String memo;

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

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}