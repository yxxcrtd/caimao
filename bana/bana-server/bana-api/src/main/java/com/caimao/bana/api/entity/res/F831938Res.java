 package com.caimao.bana.api.entity.res;
 
 import java.math.BigDecimal;
 
 public class F831938Res
 {
   private Short userGrade;
   private BigDecimal discountRate;
 
   public Short getUserGrade()
   {
     return this.userGrade;
   }
 
   public void setUserGrade(Short userGrade) {
     this.userGrade = userGrade;
   }
 
   public BigDecimal getDiscountRate() {
     return this.discountRate;
   }
 
   public void setDiscountRate(BigDecimal discountRate) {
     this.discountRate = discountRate;
   }
 }