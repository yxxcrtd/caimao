package com.caimao.bana.api.entity.res;

public class F830312Res {
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

    public Long getTotalAsset() {
        return this.totalAsset;
    }

    public void setTotalAsset(Long totalAsset) {
        this.totalAsset = totalAsset;
    }

    public Long getTotalMarketValue() {
        return this.totalMarketValue;
    }

    public void setTotalMarketValue(Long totalMarketValue) {
        this.totalMarketValue = totalMarketValue;
    }

    public Long getTotalProfit() {
        return this.totalProfit;
    }

    public void setTotalProfit(Long totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Long getEnableWithdraw() {
        return this.enableWithdraw;
    }

    public void setEnableWithdraw(Long enableWithdraw) {
        this.enableWithdraw = enableWithdraw;
    }

    public Long getCurAmount() {
        return this.curAmount;
    }

    public void setCurAmount(Long curAmount) {
        this.curAmount = curAmount;
    }

    public Long getBeginAmount() {
        return this.beginAmount;
    }

    public void setBeginAmount(Long beginAmount) {
        this.beginAmount = beginAmount;
    }

    public Long getTotalNetAssets() {
        return this.totalNetAssets;
    }

    public void setTotalNetAssets(Long totalNetAssets) {
        this.totalNetAssets = totalNetAssets;
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

    public Long getLoanAmount() {
        return this.loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Long getCurrentCash() {
        return this.currentCash;
    }

    public void setCurrentCash(Long currentCash) {
        this.currentCash = currentCash;
    }
}
