package com.caimao.gjs.api.entity.res;

import java.io.Serializable;
import java.util.List;

/**
 * 查询资产
 */
public class FQueryFundsSimpleRes implements Serializable {
    /**可用资金*/
    private String enableMoney;
    /**可提资金*/
    private String enableOutMoney;
    /**资金余额*/
    private String balanceMoney;

    public String getEnableMoney() {
        return enableMoney;
    }

    public void setEnableMoney(String enableMoney) {
        this.enableMoney = enableMoney;
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
}