package com.caimao.bana.api.entity.res;

import java.math.BigDecimal;

public class F830102Res {
    private Long totalAsset;
    private Long netAsset;
    private Long totalMarketValue;
    private BigDecimal enableRatio;
    private BigDecimal exposureRatio;
    private BigDecimal riskRatio;

    public Long getTotalAsset() {
        return this.totalAsset;
    }

    public void setTotalAsset(Long totalAsset) {
        this.totalAsset = totalAsset;
    }

    public Long getNetAsset() {
        return this.netAsset;
    }

    public void setNetAsset(Long netAsset) {
        this.netAsset = netAsset;
    }

    public Long getTotalMarketValue() {
        return this.totalMarketValue;
    }

    public void setTotalMarketValue(Long totalMarketValue) {
        this.totalMarketValue = totalMarketValue;
    }

    public BigDecimal getEnableRatio() {
        return this.enableRatio;
    }

    public void setEnableRatio(BigDecimal enableRatio) {
        this.enableRatio = enableRatio;
    }

    public BigDecimal getExposureRatio() {
        return this.exposureRatio;
    }

    public void setExposureRatio(BigDecimal exposureRatio) {
        this.exposureRatio = exposureRatio;
    }

    public BigDecimal getRiskRatio() {
        return this.riskRatio;
    }

    public void setRiskRatio(BigDecimal riskRatio) {
        this.riskRatio = riskRatio;
    }
}
