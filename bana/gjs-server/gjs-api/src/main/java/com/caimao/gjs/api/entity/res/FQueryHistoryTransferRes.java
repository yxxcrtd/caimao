package com.caimao.gjs.api.entity.res;

import java.io.Serializable;

/**
 * 查询历史出入金
 */
public class FQueryHistoryTransferRes implements Serializable {

    /**转账流水*/
    private String serialNo;
    /**资金操作方向 1存入 2取出*/
    private String accessWay;
    /**转账数量*/
    private String amount;
    /**转账状态*/
    private String state;
    /**审核状态*/
    private String checkState;
    /**转账时间*/
    private String createTime;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getAccessWay() {
        return accessWay;
    }

    public void setAccessWay(String accessWay) {
        this.accessWay = accessWay;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}