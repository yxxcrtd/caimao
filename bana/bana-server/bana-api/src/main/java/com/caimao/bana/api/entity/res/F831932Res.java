 package com.caimao.bana.api.entity.res;
 
 public class F831932Res
 {
   private String bankNo;
   private String bankName;
   private String isEnable;
   private String bankAddrNo;
   private String isQuickPay;
   private Long rates;
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
 
   public Long getRates() {
     return this.rates;
   }
 
   public void setRates(Long rates) {
     this.rates = rates;
   }
 
   public Long getChannelId() {
     return this.channelId;
   }
 
   public void setChannelId(Long channelId) {
     this.channelId = channelId;
   }
 
   public String getIsEnable() {
     return this.isEnable;
   }
 
   public void setIsEnable(String isEnable) {
     this.isEnable = isEnable;
   }
 
   public String getBankAddrNo() {
     return this.bankAddrNo;
   }
 
   public void setBankAddrNo(String bankAddrNo) {
     this.bankAddrNo = bankAddrNo;
   }
 }