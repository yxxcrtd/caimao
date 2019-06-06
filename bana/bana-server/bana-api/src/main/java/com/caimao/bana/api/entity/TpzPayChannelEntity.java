package com.caimao.bana.api.entity;

import java.io.Serializable;

/**
 * 充值渠道表
 * Created by WangXu on 2015/4/22.
 */
public class TpzPayChannelEntity implements Serializable {
    private static final long serialVersionUID = 4077432440505623227L;
    private Long channelId;
    private String channelName;
    private String channelStatus;
    private String merchantId;
    private String channelVersion;
    private String signType;
    private String poundageType;
    private String businessWebgateway;
    private String businessWapgateway;
    private String signKey;
    private String cerPath;
    private String remark;
    private String businessFilegateway;

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(String channelStatus) {
        this.channelStatus = channelStatus;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getChannelVersion() {
        return channelVersion;
    }

    public void setChannelVersion(String channelVersion) {
        this.channelVersion = channelVersion;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPoundageType() {
        return poundageType;
    }

    public void setPoundageType(String poundageType) {
        this.poundageType = poundageType;
    }

    public String getBusinessWebgateway() {
        return businessWebgateway;
    }

    public void setBusinessWebgateway(String businessWebgateway) {
        this.businessWebgateway = businessWebgateway;
    }

    public String getBusinessWapgateway() {
        return businessWapgateway;
    }

    public void setBusinessWapgateway(String businessWapgateway) {
        this.businessWapgateway = businessWapgateway;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public String getCerPath() {
        return cerPath;
    }

    public void setCerPath(String cerPath) {
        this.cerPath = cerPath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBusinessFilegateway() {
        return businessFilegateway;
    }

    public void setBusinessFilegateway(String businessFilegateway) {
        this.businessFilegateway = businessFilegateway;
    }
}
