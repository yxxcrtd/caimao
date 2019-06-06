package com.caimao.bana.api.enums;

public enum EProdType
{
  FREE("0", "免费体验"), DAY("1", "日配"), MONTH("2", "月配"), FIRM_CONTEST("3", 
    "实盘大赛"),  PREFERENTIAL("4", "优惠");

  private String code;
  private String value;

  private EProdType(String code, String value) { this.code = code;
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