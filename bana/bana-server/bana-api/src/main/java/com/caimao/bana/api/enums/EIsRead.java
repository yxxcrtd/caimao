/*    */ package com.caimao.bana.api.enums;
/*    */ 
/*    */ public enum EIsRead
/*    */ {
/*  4 */   UNREAD("0", "未读"), READ("1", "已读");
/*    */ 
/*    */   private String code;
/*    */   private String value;
/*    */ 
/*  7 */   private EIsRead(String code, String value) { this.code = code;
/*  8 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public String getCode()
/*    */   {
/* 16 */     return this.code;
/*    */   }
/*    */ 
/*    */   public String getValue() {
/* 20 */     return this.value;
/*    */   }
/*    */ }

/* Location:           E:\nexus_repo\com\hsnet\pz\biz\pzbiz-base-service\2.0.1\pzbiz-base-service-2.0.1.jar
 * Qualified Name:     com.hsnet.pz.biz.base.enums.EIsRead
 * JD-Core Version:    0.6.2
 */