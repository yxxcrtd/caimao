 package com.caimao.bana.api.entity.res;
 
 import java.math.BigDecimal;
 
 public class F830907Res
 {
   private Integer loanRatio;
   private String loanRatioName;
   private BigDecimal interestRate;
   private BigDecimal interestMonthRate;
   private Long stockPoolNo;
   private BigDecimal stockRatioLimit;
   private BigDecimal exposureRatio;
   private BigDecimal enableRatio;
 
   public Integer getLoanRatio()
   {
     return this.loanRatio;
   }
 
   public void setLoanRatio(Integer loanRatio) {
     this.loanRatio = loanRatio;
   }
 
   public String getLoanRatioName() {
     return this.loanRatioName;
   }
 
   public void setLoanRatioName(String loanRatioName) {
     this.loanRatioName = loanRatioName;
   }
 
   public BigDecimal getInterestRate() {
     return this.interestRate;
   }
 
   public void setInterestRate(BigDecimal interestRate) {
     this.interestRate = interestRate;
   }
 
   public BigDecimal getInterestMonthRate() {
     return this.interestMonthRate;
   }
 
   public void setInterestMonthRate(BigDecimal interestMonthRate) {
     this.interestMonthRate = interestMonthRate;
   }
 
   public Long getStockPoolNo() {
     return this.stockPoolNo;
   }
 
   public void setStockPoolNo(Long stockPoolNo) {
     this.stockPoolNo = stockPoolNo;
   }
 
   public BigDecimal getStockRatioLimit() {
     return this.stockRatioLimit;
   }
 
   public void setStockRatioLimit(BigDecimal stockRatioLimit) {
     this.stockRatioLimit = stockRatioLimit;
   }
 
   public BigDecimal getExposureRatio() {
     return this.exposureRatio;
   }
 
   public void setExposureRatio(BigDecimal exposureRatio) {
     this.exposureRatio = exposureRatio;
   }
 
   public BigDecimal getEnableRatio() {
     return this.enableRatio;
   }
 
   public void setEnableRatio(BigDecimal enableRatio) {
     this.enableRatio = enableRatio;
   }
 }