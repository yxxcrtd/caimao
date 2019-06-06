 package com.caimao.bana.api.entity.res;
 
 public class F830908Res
 {
   private String bankNo;
   private String bankName;
   private String isQuickPay;
   private Double rates;
   private Long channelId;
 
   public String getBankNo()
   {
     return this.bankNo;
   }
 
   public void setBankNo(String bankNo) {
     this.bankNo = bankNo;
   }
 
   public String getBankName() {
     return this.bankName;
   }
 
   public void setBankName(String bankName) {
     this.bankName = bankName;
   }
 
   public String getIsQuickPay() {
     return this.isQuickPay;
   }
 
   public void setIsQuickPay(String isQuickPay) {
     this.isQuickPay = isQuickPay;
   }
 
   public Double getRates() {
     return this.rates;
   }
 
   public void setRates(Double rates) {
     this.rates = rates;
   }
 
   public Long getChannelId() {
     return this.channelId;
   }
 
   public void setChannelId(Long channelId) {
     this.channelId = channelId;
   }
 }