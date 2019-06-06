/*    */ package com.caimao.bana.api.enums;
/*    */ 
/*    */ public enum EProdBillType
/*    */ {
/* 10 */   FREE("0", "不收费"), INTEREST("1", "利率"), FEE("2", "管理费");
/*    */ 
/*    */   private String code;
/*    */   private String value;
/*    */ 
/* 13 */   private EProdBillType(String code, String value) { this.code = code;
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
 * Qualified Name:     com.hsnet.pz.biz.base.enums.EProdBillType
 * JD-Core Version:    0.6.2
 */