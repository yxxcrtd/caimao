package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.zeus.BanaHomsFinanceHistoryEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountAssetsEntity;

import java.io.Serializable;

/**
 * 后台查询homs系统流水
 */
public class FZeusHomsJourReq extends QueryBase<BanaHomsFinanceHistoryEntity> implements Serializable {
    private String dateStart;
    private String dateEnd;
    private Long transNo;
    private String transBizType;
    private String stockCode;
    private String stockName;
    private String account;
    private Long accountUnitNo;
    private String accountUnitName;
    private String transAmount;
    private Integer accountType;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Long getTransNo() {
        return transNo;
    }

    public void setTransNo(Long transNo) {
        this.transNo = transNo;
    }

    public String getTransBizType() {
        return transBizType;
    }

    public void setTransBizType(String transBizType) {
        this.transBizType = transBizType;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getAccountUnitNo() {
        return accountUnitNo;
    }

    public void setAccountUnitNo(Long accountUnitNo) {
        this.accountUnitNo = accountUnitNo;
    }

    public String getAccountUnitName() {
        return accountUnitName;
    }

    public void setAccountUnitName(String accountUnitName) {
        this.accountUnitName = accountUnitName;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}
