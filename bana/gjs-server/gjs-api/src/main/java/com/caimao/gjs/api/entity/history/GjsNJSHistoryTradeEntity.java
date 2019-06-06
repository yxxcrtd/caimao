package com.caimao.gjs.api.entity.history;

import java.io.Serializable;

/**
 * NJS历史交易
 *
 * Created by yangxinxin@huobi.com on 2015/10/19.
 */
public class GjsNJSHistoryTradeEntity implements Serializable {
    private Long id;
    private String date; // 成交日期
    private String firmId; // 交易商编号
    private String serialNo; // 委托编号
    private String contNo; // 成交编号
    private String wareId; // 商品代码
    private String buyOrSal; // 买卖标记，1位字符串，B买入;S卖出
    private String conPrice; // 成交价格，货币型（12,2）
    private String contNum; // 成交数量
    private String fTime; // 成交时间
    private String tmpMoney; // 手续费
    private String orderSty; // 委托标志（101市价单；111正常单；151强制单）
    private String contQty; // 成交额
    private String cGenerateFlag; // 下单类型，A，正常的委托单；B，止盈止损单触发的委托单；C，条件单触发的委托单。

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getContNo() {
        return contNo;
    }

    public void setContNo(String contNo) {
        this.contNo = contNo;
    }

    public String getWareId() {
        return wareId;
    }

    public void setWareId(String wareId) {
        this.wareId = wareId;
    }

    public String getBuyOrSal() {
        return buyOrSal;
    }

    public void setBuyOrSal(String buyOrSal) {
        this.buyOrSal = buyOrSal;
    }

    public String getConPrice() {
        return conPrice;
    }

    public void setConPrice(String conPrice) {
        this.conPrice = conPrice;
    }

    public String getContNum() {
        return contNum;
    }

    public void setContNum(String contNum) {
        this.contNum = contNum;
    }

    public String getfTime() {
        return fTime;
    }

    public void setfTime(String fTime) {
        this.fTime = fTime;
    }

    public String getTmpMoney() {
        return tmpMoney;
    }

    public void setTmpMoney(String tmpMoney) {
        this.tmpMoney = tmpMoney;
    }

    public String getOrderSty() {
        return orderSty;
    }

    public void setOrderSty(String orderSty) {
        this.orderSty = orderSty;
    }

    public String getContQty() {
        return contQty;
    }

    public void setContQty(String contQty) {
        this.contQty = contQty;
    }

    public String getcGenerateFlag() {
        return cGenerateFlag;
    }

    public void setcGenerateFlag(String cGenerateFlag) {
        this.cGenerateFlag = cGenerateFlag;
    }
}
