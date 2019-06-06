package com.caimao.gjs.api.entity.res;

import java.io.Serializable;

/**
 * 查询商品下单数量
 */
public class FQueryProdSingleRes implements Serializable {
    /**最大可买数量*/
    private String buyMax;
    /**最大可卖数量*/
    private String sellMax;

    public String getBuyMax() {
        return buyMax;
    }

    public void setBuyMax(String buyMax) {
        this.buyMax = buyMax;
    }

    public String getSellMax() {
        return sellMax;
    }

    public void setSellMax(String sellMax) {
        this.sellMax = sellMax;
    }
}