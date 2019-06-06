package com.caimao.hq.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* GjsPriceAlertEntity 实例对象
*
* Created by wangxu@huobi.com on 2015-11-23 11:10:10 星期一
*/
public class GjsPriceAlertEntity implements Serializable {

    /**  */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 交易所代码 */
    private String exchange;

    /** 商品代码 */
    private String goodCode;

    /** 商品名称 */
    private String goodName;

    /** 条件 1 大于 2 小于 3 涨幅 */
    private String condition;

    /** 开户 1 开 0 关 */
    private String on;

    /** 价格条件（涨幅） */
    private BigDecimal price;

    /** 最后发送的时间 */
    private Date lastSendTime;

    /** 最后一次开启后发送次数 */
    private Integer sendNum;

    /** 最后一次触发的价格 **/
    private BigDecimal triggerPrice;

    /** 创建时间 */
    private Date created;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getGoodCode() {
        return goodCode;
    }

    public void setGoodCode(String goodCode) {
        this.goodCode = goodCode;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getLastSendTime() {
        return lastSendTime;
    }

    public void setLastSendTime(Date lastSendTime) {
        this.lastSendTime = lastSendTime;
    }

    public Integer getSendNum() {
        return sendNum;
    }

    public void setSendNum(Integer sendNum) {
        this.sendNum = sendNum;
    }

    public BigDecimal getTriggerPrice() {
        return triggerPrice;
    }

    public void setTriggerPrice(BigDecimal triggerPrice) {
        this.triggerPrice = triggerPrice;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}