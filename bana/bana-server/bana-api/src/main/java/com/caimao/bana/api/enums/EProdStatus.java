/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.enums;

/**
 * @author yanjg
 * 2015年5月13日
 */
public enum EProdStatus {
      UN_OPEN("0", "未开放"), OPENING("1", "开放"), STOP("2", "已终止");

      private String code;
      private String value;

      private EProdStatus(String code, String value) { this.code = code;
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
