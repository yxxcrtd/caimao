package com.caimao.bana.api.entity;

public class TpzHomsAccountChild extends QueryBase<TpzHomsAccountChild>{
    private String userRealName;
    private Long id;
    private String homsFundAccount;
    private String homsCombineId;
    private String homsCombineName;
    private String homsCombineStatus;
    private String combineNo;
    private String assetId;
    private String clientName;
    private String assetunitName;
    private Double counterFee;
    private String operatorNo;
    private String operatorPwd;
    private String tradeLimit;
    private String remark;

    public String getUserRealName() {
        return this.userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getHomsCombineName() {
        return this.homsCombineName;
    }

    public void setHomsCombineName(String homsCombineName) {
        this.homsCombineName = homsCombineName;
    }

    public String getHomsCombineStatus() {
        return this.homsCombineStatus;
    }

    public void setHomsCombineStatus(String homsCombineStatus) {
        this.homsCombineStatus = homsCombineStatus;
    }

    public String getCombineNo() {
        return this.combineNo;
    }

    public void setCombineNo(String combineNo) {
        this.combineNo = combineNo;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAssetunitName() {
        return this.assetunitName;
    }

    public void setAssetunitName(String assetunitName) {
        this.assetunitName = assetunitName;
    }

    public Double getCounterFee() {
        return this.counterFee;
    }

    public void setCounterFee(Double counterFee) {
        this.counterFee = counterFee;
    }

    public String getOperatorNo() {
        return this.operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public String getOperatorPwd() {
        return this.operatorPwd;
    }

    public void setOperatorPwd(String operatorPwd) {
        this.operatorPwd = operatorPwd;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTradeLimit() {
        return this.tradeLimit;
    }

    public void setTradeLimit(String tradeLimit) {
        this.tradeLimit = tradeLimit;
    }
}
