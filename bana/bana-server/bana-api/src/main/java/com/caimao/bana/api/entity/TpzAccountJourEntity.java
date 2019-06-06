package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户资产记账表
 * Created by WangXu on 2015/4/23.
 */
public class TpzAccountJourEntity implements Serializable {
    private static final long serialVersionUID = 7054912524104406073L;
    private Long id;
    private Long pzAccountId;
    private Long userId;
    private Date transDatetime;
    private String accountBizType;
    private Long transAmount;
    private Long preAmount;
    private Long postAmount;
    private String seqFlag;
    private String refSerialNo;
    private String remark;
    private String workDate;
    private Long version;
    private String userName;
    private String mobile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
