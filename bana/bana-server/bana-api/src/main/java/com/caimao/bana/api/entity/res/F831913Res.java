 package com.caimao.bana.api.entity.res;
 
 
 public class F831913Res
 {
   private Long id;
   private Long prodId;
   private Double loanRatioFrom;
   private Double loanRatioTo;
   private Long loanAmountFrom;
   private Long loanAmountTo;
   private Integer loanTermFrom;
   private Integer loanTermTo;
   private Long fee;
   private Double interestRate;
   private Double exposureRatio;
   private Double enableRatio;
   private Long stockPoolNo;
   private Double stockRatioLimit;
   private String createDatetime;
   private String updateDatetime;
 
   public Long getId()
   {
     return this.id;
   }
 
   public void setId(Long id) {
     this.id = id;
   }
 
   public Long getProdId() {
     return this.prodId;
   }
 
   public void setProdId(Long prodId) {
     this.prodId = prodId;
   }
 
   public Double getLoanRatioFrom() {
     return this.loanRatioFrom;
   }
 
   public void setLoanRatioFrom(Double loanRatioFrom) {
     this.loanRatioFrom = loanRatioFrom;
   }
 
   public Double getLoanRatioTo() {
     return this.loanRatioTo;
   }
 
   public void setLoanRatioTo(Double loanRatioTo) {
     this.loanRatioTo = loanRatioTo;
   }
 
   public Long getLoanAmountFrom() {
     return this.loanAmountFrom;
   }
 
   public void setLoanAmountFrom(Long loanAmountFrom) {
     this.loanAmountFrom = loanAmountFrom;
   }
 
   public Long getLoanAmountTo() {
     return this.loanAmountTo;
   }
 
   public void setLoanAmountTo(Long loanAmountTo) {
     this.loanAmountTo = loanAmountTo;
   }
 
   public Integer getLoanTermFrom() {
     return this.loanTermFrom;
   }
 
   public void setLoanTermFrom(Integer loanTermFrom) {
     this.loanTermFrom = loanTermFrom;
   }
 
   public Integer getLoanTermTo() {
     return this.loanTermTo;
   }
 
   public void setLoanTermTo(Integer loanTermTo) {
     this.loanTermTo = loanTermTo;
   }
 
   public Long getFee() {
     return this.fee;
   }
 
   public void setFee(Long fee) {
     this.fee = fee;
   }
 
   public Double getInterestRate() {
     return this.interestRate;
   }
 
   public void setInterestRate(Double interestRate) {
     this.interestRate = interestRate;
   }
 
   public Double getExposureRatio() {
     return this.exposureRatio;
   }
 
   public void setExposureRatio(Double exposureRatio) {
     this.exposureRatio = exposureRatio;
   }
 
   public Double getEnableRatio() {
     return this.enableRatio;
   }
 
   public void setEnableRatio(Double enableRatio) {
     this.enableRatio = enableRatio;
   }
 
   public Long getStockPoolNo() {
     return this.stockPoolNo;
   }
 
   public void setStockPoolNo(Long stockPoolNo) {
     this.stockPoolNo = stockPoolNo;
   }
 
   public Double getStockRatioLimit() {
     return this.stockRatioLimit;
   }
 
   public void setStockRatioLimit(Double stockRatioLimit) {
     this.stockRatioLimit = stockRatioLimit;
   }
 
   public String getCreateDatetime() {
     return this.createDatetime;
   }
 
   public void setCreateDatetime(String createDatetime) {
       ///* 155 */     this.createDatetime = DateUtil.convertDateNumToStr(createDatetime);
       this.createDatetime = createDatetime;
   }
 
   public String getUpdateDatetime() {
     return this.updateDatetime;
   }
 
   public void setUpdateDatetime(String updateDatetime) {
       ///* 163 */     this.updateDatetime = DateUtil.convertDateNumToStr(updateDatetime);
       this.updateDatetime = updateDatetime;
   }
 }