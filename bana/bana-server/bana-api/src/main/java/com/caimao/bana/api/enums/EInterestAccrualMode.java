/*    */ package com.caimao.bana.api.enums;
/*    */ 
/*    */ public enum EInterestAccrualMode
/*    */ {
/* 10 */   DAY("0", "自然日"), TRADE_DAY("1", "交易日"), MONTH("2", "30自然日");
/*    */ 
/*    */   private String code;
/*    */   private String value;
/*    */ 
/* 13 */   private EInterestAccrualMode(String code, String value) { this.code = code;
/* 14 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public String getCode()
/*    */   {
/* 22 */     return this.code;
/*    */   }
/*    */ 
/*    */   public String getValue() {
/* 26 */     return this.value;
/*    */   }
/*    */ }

/* Location:           E:\nexus_repo\com\hsnet\pz\biz\pzbiz-base-service\2.0.1\pzbiz-base-service-2.0.1.jar
 * Qualified Name:     com.hsnet.pz.biz.base.enums.EInterestAccrualMode
 * JD-Core Version:    0.6.2
 */