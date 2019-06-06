package com.caimao.bana.api.entity.ybk;

import java.util.Date;

/**
 * 邮币卡活动(ybk_activity)
 */
public class YBKActivityEntity implements java.io.Serializable {
    /** 自增主键 */
    private Long id;
    /** 交易所编号 */
    private Integer exchangeId;
    /** 交易所名称 */
    private String exchangeName;
    /** 活动名称 */
    private String activityName;
    /** 截止时间 */
    private Date endDatetime;
    /** 要求 */
    private String ask;
    /** 奖励 */
    private String reward;
    /** 奖励价值 */
    private String rewardValue;
    /** 0显示 1不显示 */
    private Integer isShow;
    /** 活动地址 */
    private String activityUrl;
    /** 活动banner图片 **/
    private String activityBanner;
    /** 排序*/
    private Integer sort;
    /** 创建时间 */
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(String rewardValue) {
        this.rewardValue = rewardValue;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }


    public String getActivityBanner() {
        return activityBanner;
    }

    public void setActivityBanner(String activityBanner) {
        this.activityBanner = activityBanner;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}