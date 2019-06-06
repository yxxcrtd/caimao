package com.caimao.bana.api.entity.res;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户资金流水记录
 * Created by WangXu on 2015/6/1.
 */
public class FAccountQueryAccountJourRes implements Serializable {
    private Long id;
    private Date transDatetime;
    private String accountBizType;
    private Long transAmount;
    private Long preAmount;
    private Long postAmount;
    private String seqFlag;
    private String refSerialNo;
    private String remark;
    private String pzAccountId;
    private String userId;
    private String userRealName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTransDatetime() {
        return transDatetime;
    }

    public void setTransDatetime(Date transDatetime) {
        this.transDatetime = transDatetime;
    }

    public String getAccountBizType() {
        return accountBizType;
    }

    public void setAccountBizType(String accountBizType) {
        this.accountBizType = accountBizType;
    }

    public Long getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Long transAmount) {
        this.transAmount = transAmount;
    }

    public Long getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(Long preAmount) {
        this.preAmount = preAmount;
    }

    public Long getPostAmount() {
        return postAmount;
    }

    public void setPostAmount(Long postAmount) {
        this.postAmount = postAmount;
    }

    public String getSeqFlag() {
        return seqFlag;
    }

    public void setSeqFlag(String seqFlag) {
        this.seqFlag = seqFlag;
    }

    public String getRefSerialNo() {
        return refSerialNo;
    }

    public void setRefSerialNo(String refSerialNo) {
        this.refSerialNo = refSerialNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(String pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }
}
