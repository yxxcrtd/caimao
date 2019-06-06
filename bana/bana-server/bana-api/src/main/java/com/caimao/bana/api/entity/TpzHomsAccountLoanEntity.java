package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * homs 融资合约分配账户表
 * Created by WangXu on 2015/6/12.
 */
public class TpzHomsAccountLoanEntity implements Serializable {
    private static final long serialVersionUID = -1994420106576740141L;
    private Long id;
    private Long userId;
    private Long pzAccountId;
    private Long contractNo;
    private String homsFundAccount;
    private String homsCombineId;
    private String homsManageId;
    private Long beginAmount;
    private Date createDatetime;
    private Date updateDatetime;
    private String operatorNo;
    private String tradeLimit;
    private Double enableRatio;
    private Double exposureRatio;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public Long getContractNo() {
        return contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public String getHomsFundAccount() {
        return homsFundAccount;
    }

    public void setHomsFundAccount(String homsFundAccount) {
        this.homsFundAccount = homsFundAccount;
    }

    public String getHomsCombineId() {
        return homsCombineId;
    }

    public void setHomsCombineId(String homsCombineId) {
        this.homsCombineId = homsCombineId;
    }

    public String getHomsManageId() {
        return homsManageId;
    }

    public void setHomsManageId(String homsManageId) {
        this.homsManageId = homsManageId;
    }

    public Long getBeginAmount() {
        return beginAmount;
    }

    public void setBeginAmount(Long beginAmount) {
        this.beginAmount = beginAmount;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public String getTradeLimit() {
        return tradeLimit;
    }

    public void setTradeLimit(String tradeLimit) {
        this.tradeLimit = tradeLimit;
    }

    public Double getEnableRatio() {
        return enableRatio;
    }

    public void setEnableRatio(Double enableRatio) {
        this.enableRatio = enableRatio;
    }

    public Double getExposureRatio() {
        return exposureRatio;
    }

    public void setExposureRatio(Double exposureRatio) {
        this.exposureRatio = exposureRatio;
    }
}
