package com.caimao.bana.api.entity.res;

/*     */
public class F830103Res {
    private Long id;
    private String transDatetime;
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

    public String getUserRealName() {
        return this.userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getPzAccountId() {
        return this.pzAccountId;
    }

    public void setPzAccountId(String pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransDatetime() {
        return this.transDatetime;
    }

    public void setTransDatetime(String transDatetime) {
        this.transDatetime = transDatetime;
    }

    public String getAccountBizType() {
        return this.accountBizType;
    }

    public void setAccountBizType(String accountBizType) {
        this.accountBizType = accountBizType;
    }

    public Long getTransAmount() {
        return this.transAmount;
    }

    public void setTransAmount(Long transAmount) {
        this.transAmount = transAmount;
    }

    public Long getPreAmount() {
        return this.preAmount;
    }

    public void setPreAmount(Long preAmount) {
        this.preAmount = preAmount;
    }

    public Long getPostAmount() {
        return this.postAmount;
    }

    public void setPostAmount(Long postAmount) {
        this.postAmount = postAmount;
    }

    public String getSeqFlag() {
        return this.seqFlag;
    }

    public void setSeqFlag(String seqFlag) {
        this.seqFlag = seqFlag;
    }

    public String getRefSerialNo() {
        return this.refSerialNo;
    }

    public void setRefSerialNo(String refSerialNo) {
        this.refSerialNo = refSerialNo;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
