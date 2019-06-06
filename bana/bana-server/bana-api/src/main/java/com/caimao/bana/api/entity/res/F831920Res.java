package com.caimao.bana.api.entity.res;

public class F831920Res
{
  private Long id;
  private String bankNo;
  private String serviceNote;
  private String serviceClass;

  public Long getId()
  {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBankNo() {
    return this.bankNo;
  }

  public void setBankNo(String bankNo) {
    this.bankNo = bankNo;
  }

  public String getServiceNote() {
    return this.serviceNote;
  }

  public void setServiceNote(String serviceNote) {
    this.serviceNote = serviceNote;
  }

  public String getServiceClass() {
    return this.serviceClass;
  }

  public void setServiceClass(String serviceClass) {
    this.serviceClass = serviceClass;
  }
}