package com.caimao.bana.api.entity.activity;

import java.io.Serializable;
import java.util.Date;

public class BanaLoseRPEntity implements Serializable {
    private Long rpId;
    private String rpData;
    private Date updated;

    public Long getRpId() {
        return rpId;
    }

    public void setRpId(Long rpId) {
        this.rpId = rpId;
    }

    public String getRpData() {
        return rpData;
    }

    public void setRpData(String rpData) {
        this.rpData = rpData;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}