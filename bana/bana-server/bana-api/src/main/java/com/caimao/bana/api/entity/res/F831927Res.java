package com.caimao.bana.api.entity.res;

public class F831927Res
{
  private Long contestId;
  private Long prodId;
  private String contestName;
  private String contestBeginDate;
  private String contestEndDate;
  private String contestStatus;
  private String rankingDate;
  private Integer contestUserNum;
  private String prodName;

  public String getProdName()
  {
    return this.prodName;
  }

  public void setProdName(String prodName) {
    this.prodName = prodName;
  }

  public Long getContestId() {
    return this.contestId;
  }

  public void setContestId(Long contestId) {
    this.contestId = contestId;
  }

  public Long getProdId() {
    return this.prodId;
  }

  public void setProdId(Long prodId) {
    this.prodId = prodId;
  }

  public String getContestName() {
    return this.contestName;
  }

  public void setContestName(String contestName) {
    this.contestName = contestName;
  }

  public String getContestBeginDate() {
    return this.contestBeginDate;
  }

  public void setContestBeginDate(String contestBeginDate) {
    this.contestBeginDate = contestBeginDate;
  }

  public String getContestEndDate() {
    return this.contestEndDate;
  }

  public void setContestEndDate(String contestEndDate) {
    this.contestEndDate = contestEndDate;
  }

  public String getContestStatus() {
    return this.contestStatus;
  }

  public void setContestStatus(String contestStatus) {
    this.contestStatus = contestStatus;
  }

  public String getRankingDate() {
    return this.rankingDate;
  }

  public void setRankingDate(String rankingDate) {
    this.rankingDate = rankingDate;
  }

  public Integer getContestUserNum() {
    return this.contestUserNum;
  }

  public void setContestUserNum(Integer contestUserNum) {
    this.contestUserNum = contestUserNum;
  }
}