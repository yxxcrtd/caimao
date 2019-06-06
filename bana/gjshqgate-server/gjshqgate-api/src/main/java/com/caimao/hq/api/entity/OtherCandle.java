package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * 伦敦国际金融期货期权交易所
 * Created by yzc on 2015/12/4.
 */
public class OtherCandle extends Candle{

    private int candleId;
    private static final long serialVersionUID = 522889572772314584L;
    private String optDate;//添加时间
    private String status;//数据状态

    private  String  apiName;
    private int beginNumber;//分页查询
    private int endNumber;
    private  String  direction;//查询方向

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getCandleId() {
        return candleId;
    }

    public void setCandleId(int candleId) {
        this.candleId = candleId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOptDate() {
        return optDate;
    }

    public void setOptDate(String optDate) {
        this.optDate = optDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public int getBeginNumber() {
        return beginNumber;
    }

    public void setBeginNumber(int beginNumber) {
        this.beginNumber = beginNumber;
    }

    public int getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(int endNumber) {
        this.endNumber = endNumber;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
