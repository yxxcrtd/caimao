/*    */ package com.caimao.bana.api.enums;
/*    */ 
/*    */ public enum EContestStatus
/*    */ {
/* 10 */   CUR("0", "当前"), NEXT("1", "下期"), END("2", "已结束");
/*    */ 
/*    */   private String code;
/*    */   private String value;
/*    */ 
/* 13 */   private EContestStatus(String code, String value) { this.code = code;
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
 * Qualified Name:     com.hsnet.pz.biz.base.enums.EContestStatus
 * JD-Core Version:    0.6.2
 */