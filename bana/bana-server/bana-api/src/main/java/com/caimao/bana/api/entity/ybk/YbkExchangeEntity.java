package com.caimao.bana.api.entity.ybk;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 邮币卡交易所数据表实体类
 */
public class YbkExchangeEntity implements Serializable {
    private Integer id;

    private String shortName;

    private String number;

    private String name;

    private Integer tradeDayType;

    private String amBeginTime;

    private String amEndTime;

    private String pmBeginTime;

    private String pmEndTime;

    private String apiMarketUrl;

    private String apiTickerUrl;

    private String articleUrl;

    private Integer status;

    private Date addDatetime;

    private String openProcessTimes;

    private String comeInTimes;

    private String bankBindTimes;

    private String tradeTimes;

    private String tradeFee;

    private Integer sortNo;

    private String province;

    private String city;

    private String supportBank;

    private String addUser;

    private Boolean openOrClose;

    private Integer shenGouNum;

    private String officialUrl;

    List<YBKActivityEntity> activityList;

    public List<YBKActivityEntity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<YBKActivityEntity> activityList) {
        this.activityList = activityList;
    }

    public String getOpenProcessTimes() {
        return openProcessTimes;
    }

    public void setOpenProcessTimes(String openProcessTimes) {
        this.openProcessTimes = openProcessTimes;
    }

    public String getComeInTimes() {
        return comeInTimes;
    }

    public void setComeInTimes(String comeInTimes) {
        this.comeInTimes = comeInTimes;
    }

    public String getBankBindTimes() {
        return bankBindTimes;
    }

    public void setBankBindTimes(String bankBindTimes) {
        this.bankBindTimes = bankBindTimes;
    }

    public String getTradeTimes() {
        return tradeTimes;
    }

    public void setTradeTimes(String tradeTimes) {
        this.tradeTimes = tradeTimes;
    }

    public String getTradeFee() {
        return tradeFee;
    }

    public void setTradeFee(String tradeFee) {
        this.tradeFee = tradeFee;
    }

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }

    public Integer getShenGouNum() {
        return shenGouNum;
    }

    public void setShenGouNum(Integer shenGouNum) {
        this.shenGouNum = shenGouNum;
    }

    public Boolean getOpenOrClose() {
        return openOrClose;
    }

    public void setOpenOrClose(Boolean openOrClose) {
        this.openOrClose = openOrClose;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getApiTickerUrl() {
        return apiTickerUrl;
    }

    public void setApiTickerUrl(String apiTickerUrl) {
        this.apiTickerUrl = apiTickerUrl;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getTradeDayType() {
        return tradeDayType;
    }

    public void setTradeDayType(Integer tradeDayType) {
        this.tradeDayType = tradeDayType;
    }

    public String getAmBeginTime() {
        return amBeginTime;
    }

    public void setAmBeginTime(String amBeginTime) {
        this.amBeginTime = amBeginTime == null ? null : amBeginTime.trim();
    }

    public String getAmEndTime() {
        return amEndTime;
    }

    public void setAmEndTime(String amEndTime) {
        this.amEndTime = amEndTime == null ? null : amEndTime.trim();
    }

    public String getPmBeginTime() {
        return pmBeginTime;
    }

    public void setPmBeginTime(String pmBeginTime) {
        this.pmBeginTime = pmBeginTime == null ? null : pmBeginTime.trim();
    }

    public String getPmEndTime() {
        return pmEndTime;
    }

    public void setPmEndTime(String pmEndTime) {
        this.pmEndTime = pmEndTime == null ? null : pmEndTime.trim();
    }

    public String getApiMarketUrl() {
        return apiMarketUrl;
    }

    public void setApiMarketUrl(String apiMarketUrl) {
        this.apiMarketUrl = apiMarketUrl == null ? null : apiMarketUrl.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAddDatetime() {
        return addDatetime;
    }

    public void setAddDatetime(Date addDatetime) {
        this.addDatetime = addDatetime;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getSupportBank() {
        return supportBank;
    }

    public void setSupportBank(String supportBank) {
        this.supportBank = supportBank == null ? null : supportBank.trim();
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
}