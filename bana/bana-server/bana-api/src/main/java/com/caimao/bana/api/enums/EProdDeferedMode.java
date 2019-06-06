/*    */ package com.caimao.bana.api.enums;
/*    */ 
/*    */ public enum EProdDeferedMode
/*    */ {
/* 10 */   NO("0", "不可展期"), YES("1", "可展期"), AUTO("2", "自动展期");
/*    */ 
/*    */   private String code;
/*    */   private String value;
/*    */ 
/* 13 */   private EProdDeferedMode(String code, String value) { this.code = code;
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
 * Qualified Name:     com.hsnet.pz.biz.base.enums.EProdDeferedMode
 * JD-Core Version:    0.6.2
 */