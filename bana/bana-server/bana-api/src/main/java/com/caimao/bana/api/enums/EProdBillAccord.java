/*    */ package com.caimao.bana.api.enums;
/*    */ 
/*    */ public enum EProdBillAccord
/*    */ {
/* 10 */   LOAN_RATIO("0", "融资比例"), LOAN_AMOUNT("1", "借款金额"), LOAN_TERM("2", "借款期限");
/*    */ 
/*    */   private String code;
/*    */   private String value;
/*    */ 
/* 13 */   private EProdBillAccord(String code, String value) { this.code = code;
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
 * Qualified Name:     com.hsnet.pz.biz.base.enums.EProdBillAccord
 * JD-Core Version:    0.6.2
 */