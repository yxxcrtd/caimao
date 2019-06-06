/*    */ package com.caimao.bana.api.enums;
/*    */ 
/*    */ public enum EInterestSettleMode
/*    */ {
/* 10 */   BEFORE("0", "先结"), AFTER("1", "后结");
/*    */ 
/*    */   private String code;
/*    */   private String value;
/*    */ 
/* 13 */   private EInterestSettleMode(String code, String value) { this.code = code;
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
 * Qualified Name:     com.hsnet.pz.biz.base.enums.EInterestSettleMode
 * JD-Core Version:    0.6.2
 */