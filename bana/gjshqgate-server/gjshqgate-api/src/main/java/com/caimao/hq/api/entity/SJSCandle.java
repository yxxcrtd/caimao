package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by Administrator on 2015/10/14.
 */
public class SJSCandle extends Candle {




    private int candleId;
    private static final long serialVersionUID = 522889572772314584L;
    private String optDate;//添加时间
    private String status;//数据状态
    private String beginDate ;//按日期查询
    private String endDate ;
    private int beginNumber;//分页查询
    private int endNumber;
    private  String  direction;//查询方向

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

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
