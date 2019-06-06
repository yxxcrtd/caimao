 package com.caimao.bana.api.entity.res;
 
 public class F831917Res
 {
   private Long id;
   private String bankNo;
   private String bankName;
   private String isEnable;
   private String bankAddrNo;
   private String isQuickPay;
   private Double rates;
   private Long channelId;
 
   public Long getId()
   {
     return this.id;
   }
 
   public void setId(Long id) {
     this.id = id;
   }
 
   public String getBankNo() {
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