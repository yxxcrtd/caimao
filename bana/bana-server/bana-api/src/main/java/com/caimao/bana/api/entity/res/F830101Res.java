 package com.caimao.bana.api.entity.res;
 
 public class F830101Res
 {
   private Long pzAccountId;
   private String currencyType;
   private Long avalaibleAmount;
   private Long freezeAmount;
 
   public Long getPzAccountId()
   {
     return this.pzAccountId;
   }
 
   public void setPzAccountId(Long pzAccountId) {
     this.pzAccountId = pzAccountId;
   }
 
   public String getCurrencyType() {
     return this.currencyType;
   }
 
   public void setCurrencyType(String currencyType) {
     this.currencyType = currencyType;
   }
 
   public Long getAvalaibleAmount() {
     return this.avalaibleAmount;
   }
 
   public void setAvalaibleAmount(Long avalaibleAmount) {
     this.avalaibleAmount = avalaibleAmount;
   }
 
   public Long getFreezeAmount() {
     return this.freezeAmount;
   }
 
   public void setFreezeAmount(Long freezeAmount) {
     this.freezeAmount = freezeAmount;
   }
 }