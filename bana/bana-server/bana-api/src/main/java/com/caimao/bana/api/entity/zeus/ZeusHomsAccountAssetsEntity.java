package com.caimao.bana.api.entity.zeus;

import java.io.Serializable;

/**
 * HOMS操盘账户净值资产信息
 * Created by xavier on 15/7/8.
 */
public class ZeusHomsAccountAssetsEntity implements Serializable {
    private Integer id;
    private Long userId;
    private String userName;
    private String mobile;
    private Long contractNo;
    private String homsCombineId;
    private String homsFundAccount;
    private Long totalAsset;
    private Long totalMarketValue;
    private Long totalProfit;
    private Long enableWithdraw;
    private Long curAmount;
    private Long currentCash;
    private Long beginAmount;
    private Long totalNetAssets;
    private Double enableRatio;
    private Double exposureRatio;
    private Long loanAmount;
    private String updateDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getContractNo() {
        return contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public String getHomsCombineId() {
        return homsCombineId;
    }

    public void setHomsCombineId(String homsCombineId) {
        this.homsCombineId = homsCombineId;
    }

    public String getHomsFundAccount() {
        return homsFundAccount;
    }

    public void setHomsFundAccount(String homsFundAccount) {
        this.homsFundAccount = homsFundAccount;
    }

    public Long getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(Long totalAsset) {
        this.totalAsset = totalAsset;
    }

    public Long getTotalMarketValue() {
        return totalMarketValue;
    }

    public void setTotalMarketValue(Long totalMarketValue) {
        this.totalMarketValue = totalMarketValue;
    }

    public Long getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(Long totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Long getEnableWithdraw() {
        return enableWithdraw;
    }

    public void setEnableWithdraw(Long enableWithdraw) {
        this.enableWithdraw = enableWithdraw;
    }

    public Long getCurAmount() {
        return curAmount;
    }

    public void setCurAmount(Long curAmount) {
        this.curAmount = curAmount;
    }

    public Long getCurrentCash() {
        return currentCash;
    }

    public void setCurrentCash(Long currentCash) {
        this.currentCash = currentCash;
    }

    public Long getBeginAmount() {
        return beginAmount;
    }

    public void setBeginAmount(Long beginAmount) {
        this.beginAmount = beginAmount;
    }

    public Long getTotalNetAssets() {
        return totalNetAssets;
    }

    public void setTotalNetAssets(Long totalNetAssets) {
        this.totalNetAssets = totalNetAssets;
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

    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
