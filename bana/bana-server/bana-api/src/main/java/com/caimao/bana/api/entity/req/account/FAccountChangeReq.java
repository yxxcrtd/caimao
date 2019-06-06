package com.caimao.bana.api.entity.req.account;

import java.io.Serializable;

/**
 * 融资资金账户的操作请求
 * Created by WangXu on 2015/7/13.
 */
public class FAccountChangeReq implements Serializable {
    private String refSerialNo;
    private Long pzAccountId;
    private Long amount;
    private String seqFlag;
    private String accountBizType;

    public String getRefSerialNo() {
        return refSerialNo;
    }

    public void setRefSerialNo(String refSerialNo) {
        this.refSerialNo = refSerialNo;
    }

    public Long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getSeqFlag() {
        return seqFlag;
    }

    public void setSeqFlag(String seqFlag) {
        this.seqFlag = seqFlag;
    }

    public String getAccountBizType() {
        return accountBizType;
    }

    public void setAccountBizType(String accountBizType) {
        this.accountBizType = accountBizType;
    }
}
