/*    */ package com.caimao.bana.api.enums;
/*    */ 
/*    */ public enum EProdRepayMode
/*    */ {
/* 10 */   PART("0", "部分还款"), ALL("1", "一次性还款");
/*    */ 
/*    */   private String code;
/*    */   private String value;
/*    */ 
/* 13 */   private EProdRepayMode(String code, String value) { this.code = code;
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
 * Qualified Name:     com.hsnet.pz.biz.base.enums.EProdRepayMode
 * JD-Core Version:    0.6.2
 */