package com.caimao.bana.api.entity;

import java.io.Serializable;

public class BrokerInfoEntity implements Serializable {
    private static final long serialVersionUID = 5636034964527613196L;
    private String brokerId;
    private String brokerType;
    private String brokerName;
    private String brokerStatus;
    private String brokerDescription;

    public String getBrokerId() {
        return this.brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public String getBrokerType() {
        return this.brokerType;
    }

    public void setBrokerType(String brokerType) {
        this.brokerType = brokerType;
    }

    public String getBrokerName() {
        return this.brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getBrokerStatus() {
        return this.brokerStatus;
    }

    public void setBrokerStatus(String brokerStatus) {
        this.brokerStatus = brokerStatus;
    }

    public String getBrokerDescription() {
        return this.brokerDescription;
    }

    public void setBrokerDescription(String brokerDescription) {
        this.brokerDescription = brokerDescription;
    }
}