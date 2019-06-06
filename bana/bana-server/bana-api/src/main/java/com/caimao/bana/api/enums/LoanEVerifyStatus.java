package com.caimao.bana.api.enums;

/**
 * Created by WangXu on 2015/4/28.
 */
public enum LoanEVerifyStatus {
      APPLYING("0", "申请中"), CHECK_SUCCESS("1", "申请通过"), CHECK_FAIL("2", "申请失败");

      private String code;
      private String value;

      private LoanEVerifyStatus(String code, String value) { this.code = code;
        this.value = value;
      }

      public String getCode()
      {
        return this.code;
      }

      public String getValue() {
        return this.value;
      }
}