package com.caimao.gjs.api.entity.res;

import java.io.Serializable;

/**
 * 查询套餐信息
 */
public class FQueryPackageInfoRes implements Serializable {
    /**套餐有效日期*/
    private String validDate;
    /**套餐额度*/
    private String quota;
    /**已用额度*/
    private String useQuota;

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public String getUseQuota() {
        return useQuota;
    }

    public void setUseQuota(String useQuota) {
        this.useQuota = useQuota;
    }
}