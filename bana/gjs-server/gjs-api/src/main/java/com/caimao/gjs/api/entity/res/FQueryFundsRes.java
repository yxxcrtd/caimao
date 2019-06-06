package com.caimao.gjs.api.entity.res;

import java.io.Serializable;
import java.util.List;

/**
 * 查询资产
 */
public class FQueryFundsRes implements Serializable {
    /**净资产*/
    private String netValue;
    /**可用资金*/
    private String enableMoney;
    /**冻结资金*/
    private String freezeMoney;
    /**可提资金*/
    private String enableOutMoney;
    /**资金余额*/
    private String balanceMoney;
    /**安全率*/
    private String safeRate;
    /**总盈利*/
    private String totalGain;
    /**持仓成本*/
    private String occupancyMoney;
    /**持仓市值*/
    private String holdMarket;
    /**持仓列表*/
    private List<FQueryHoldRes> holdList;
    /**入金广告*/
    private String transferAD;

    public String getNetValue() {
        return netValue;
    }

    public void setNetValue(String netValue) {
        this.netValue = netValue;
    }

    public String getEnableMoney() {
        return enableMoney;
    }

    public void setEnableMoney(String enableMoney) {
        this.enableMoney = enableMoney;
    }

    public String getFreezeMoney() {
        return freezeMoney;
    }

    public void setFreezeMoney(String freezeMoney) {
        this.freezeMoney = freezeMoney;
    }

    public String getEnableOutMoney() {
        return enableOutMoney;
    }

    public void setEnableOutMoney(String enableOutMoney) {
        this.enableOutMoney = enableOutMoney;
    }

    public String getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney(String balanceMoney) {
        this.balanceMoney = balanceMoney;
    }

    public String getSafeRate() {
        return safeRate;
    }

    public void setSafeRate(String safeRate) {
        this.safeRate = safeRate;
    }

    public String getTotalGain() {
        return totalGain;
    }

    public void setTotalGain(String totalGain) {
        this.totalGain = totalGain;
    }

    public String getOccupancyMoney() {
        return occupancyMoney;
    }

    public void setOccupancyMoney(String occupancyMoney) {
        this.occupancyMoney = occupancyMoney;
    }

    public String getHoldMarket() {
        return holdMarket;
    }

    public void setHoldMarket(String holdMarket) {
        this.holdMarket = holdMarket;
    }

    public List<FQueryHoldRes> getHoldList() {
        return holdList;
    }

    public void setHoldList(List<FQueryHoldRes> holdList) {
        this.holdList = holdList;
    }

    public String getTransferAD() {
        return transferAD;
    }

    public void setTransferAD(String transferAD) {
        this.transferAD = transferAD;
    }
}