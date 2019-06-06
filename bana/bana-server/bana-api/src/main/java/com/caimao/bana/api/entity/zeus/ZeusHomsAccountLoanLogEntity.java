package com.caimao.bana.api.entity.zeus;

import java.io.Serializable;
import java.util.Date;

/**
 * homs借款账户log
 */
public class ZeusHomsAccountLoanLogEntity implements Serializable {
private Long userId;
    private Long pzAccountId;
    private Long contractNo;
    private String homsFundAccount;
    private String homsCombineId;
    private String homsManageId;
    private String assetId;
    private Long beginAmount;
    private String operatorNo;
    private Date createDatetime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public Long getContractNo() {
        return contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public String getHomsFundAccount() {
        return homsFundAccount;
    }

    public void setHomsFundAccount(String homsFundAccount) {
        this.homsFundAccount = homsFundAccount;
    }

    public String getHomsCombineId() {
        return homsCombineId;
    }

    public void setHomsCombineId(String homsCombineId) {
        this.homsCombineId = homsCombineId;
    }

    public String getHomsManageId() {
        return homsManageId;
    }

    public void setHomsManageId(String homsManageId) {
        this.homsManageId = homsManageId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public Long getBeginAmount() {
        return beginAmount;
    }

    public void setBeginAmount(Long beginAmount) {
        this.beginAmount = beginAmount;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}