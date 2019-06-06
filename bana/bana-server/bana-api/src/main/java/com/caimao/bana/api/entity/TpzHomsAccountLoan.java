package com.caimao.bana.api.entity;

import java.util.Date;


public class TpzHomsAccountLoan extends QueryBase<TpzHomsAccountLoan> {
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
    private String operatorPass;
    private String tradeLimit;
    private Double enableRatio;
    private Double exposureRatio;
    private TpzHomsAccountChild accountChild;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPzAccountId() {
        return this.pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public Long getContractNo() {
        return this.contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public String getHomsFundAccount() {
        return this.homsFundAccount;
    }

    public void setHomsFundAccount(String homsFundAccount) {
        this.homsFundAccount = homsFundAccount;
    }

    public String getHomsCombineId() {
        return this.homsCombineId;
    }

    public void setHomsCombineId(String homsCombineId) {
        this.homsCombineId = homsCombineId;
    }

    public String getHomsManageId() {
        return this.homsManageId;
    }

    public void setHomsManageId(String homsManageId) {
        this.homsManageId = homsManageId;
    }

    public Long getBeginAmount() {
        return this.beginAmount;
    }

    public void setBeginAmount(Long beginAmount) {
        this.beginAmount = beginAmount;
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

    public String getOperatorNo() {
        return this.operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public TpzHomsAccountChild getAccountChild() {
        return this.accountChild;
    }

    public void setAccountChild(TpzHomsAccountChild accountChild) {
        this.accountChild = accountChild;
    }

    public String getTradeLimit() {
        return this.tradeLimit;
    }

    public void setTradeLimit(String tradeLimit) {
        this.tradeLimit = tradeLimit;
    }

    public Double getEnableRatio() {
        return this.enableRatio;
    }

    public void setEnableRatio(Double enableRatio) {
        this.enableRatio = enableRatio;
    }

    public Double getExposureRatio() {
        return this.exposureRatio;
    }

    public void setExposureRatio(Double exposureRatio) {
        this.exposureRatio = exposureRatio;
    }

    public String getOperatorPass() {
        return this.operatorPass;
    }

    public void setOperatorPass(String operatorPass) {
        this.operatorPass = operatorPass;
    }
}
