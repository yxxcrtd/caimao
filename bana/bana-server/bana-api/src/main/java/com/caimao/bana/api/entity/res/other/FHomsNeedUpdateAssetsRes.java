package com.caimao.bana.api.entity.res.other;

import java.io.Serializable;

/**
 * 需要更新HOMS资产的记录
 * Created by xavier on 15/7/8.
 */
public class FHomsNeedUpdateAssetsRes implements Serializable {
    private Long userId;
    private String homsFundAccount;
    private String homsCombineId;
    private Long contractNo;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getContractNo() {
        return contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }
}
