package com.caimao.bana.api.entity;

import java.io.Serializable;

/**
 * Created by WangXu on 2015/4/24.
 */
public class HeepayNoticeEntity implements Serializable {

    private static final long serialVersionUID = -5480352140694417544L;
    private String result;
    private String payMessage;
    private String agentId;
    private String jnetBillNo;
    private String agentBillNo;
    private String payType;
    private Long payAmt;
    private String remark;
    private String sign;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPayMessage() {
        return payMessage;
    }

    public void setPayMessage(String payMessage) {
        this.payMessage = payMessage;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getJnetBillNo() {
        return jnetBillNo;
    }

    public void setJnetBillNo(String jnetBillNo) {
        this.jnetBillNo = jnetBillNo;
    }

    public String getAgentBillNo() {
        return agentBillNo;
    }

    public void setAgentBillNo(String agentBillNo) {
        this.agentBillNo = agentBillNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Long getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(Long payAmt) {
        this.payAmt = payAmt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
