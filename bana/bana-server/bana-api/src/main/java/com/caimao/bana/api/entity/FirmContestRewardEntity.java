package com.caimao.bana.api.entity;

import java.io.Serializable;

public class FirmContestRewardEntity implements Serializable {
    private static final long serialVersionUID = 279153015032095804L;
    private Long contestId;
    private Integer rank;
    private Long rewardAmount;

    public Long getContestId() {
        return this.contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }

    public Integer getRank() {
        return this.rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Long getRewardAmount() {
        return this.rewardAmount;
    }

    public void setRewardAmount(Long rewardAmount) {
        this.rewardAmount = rewardAmount;
    }
}