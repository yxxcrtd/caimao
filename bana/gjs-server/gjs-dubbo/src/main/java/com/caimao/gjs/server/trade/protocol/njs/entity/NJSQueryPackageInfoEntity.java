package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 套餐信息查询
 */
public class NJSQueryPackageInfoEntity implements Serializable {
    private String VALID_DATE; //套餐有效日期
    private String QUOTA; //套餐额度
    private String USE_QUOTA; //已用额度

    public String getVALID_DATE() {
        return VALID_DATE;
    }

    public void setVALID_DATE(String VALID_DATE) {
        this.VALID_DATE = VALID_DATE;
    }

    public String getQUOTA() {
        return QUOTA;
    }

    public void setQUOTA(String QUOTA) {
        this.QUOTA = QUOTA;
    }

    public String getUSE_QUOTA() {
        return USE_QUOTA;
    }

    public void setUSE_QUOTA(String USE_QUOTA) {
        this.USE_QUOTA = USE_QUOTA;
    }
}