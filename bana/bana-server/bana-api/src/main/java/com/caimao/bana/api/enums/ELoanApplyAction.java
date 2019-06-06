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
public enum ELoanApplyAction
{
  LOAN("0", "借款"), ADD("1", "追加借款"), DEFERED("2", "借款展期");

  private String code;
  private String value;

  private ELoanApplyAction(String code, String value) { this.code = code;
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