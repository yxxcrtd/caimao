package com.caimao.gjs.api.entity.history;

//import lombok.Data;

import java.io.Serializable;

/**
 * NJS历史委托
 *
 * Created by yangxinxin@huobi.com on 2015/10/12.
 */
// @Setter：为属性提供一个set方法
// @Getter：为属性提供一个get方法
// @Log4j ：注解在类上；为类提供一个 属性名为log 的 log4j 日志对象7
// @AllArgsConstructor // 为类提供一个全参的构造方法
// @EqualsAndHashCode(callSuper = false)
// @Data // 提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
// @NoArgsConstructor // 为类提供一个无参的构造方法
public class GjsNJSHistoryEntrustEntity implements Serializable {
    private Long id;
    private String date; // 日期，8位字符串
    private String traderId; // 交易员编号，16位字符串
    private String serialNo; // 委托编号，8位字符串
    private String wareId; // 品种编码，16位字符串
    private String buyOrSal; // 买卖标记，1位字符串，B买入;S卖出
    private String price; // 价格，货币型（12,2）
    private String num; // 委托数量
    private String contNum; // 成交数量
    private String time; // 委托时间，6位字符串
    private String cStatus; // 交易单状态，1位字符串，A未成交；B部分成交；C全部成交；D已撤单。
    private String sCancleTime; // 撤单时间，6位字符串
    private String soIpAddress; // 委托IP地址，30位字符串
    private String cGenerateFlag; // 下单类型，1位字符串，A，正常的委托单；B，止盈止损单触发的委托单；C，条件单触发的委托单。
    private String orderSty; //委托标志（101市价单；111正常单；151强制单）

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

    public String getTraderId() {
        return traderId;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getContNum() {
        return contNum;
    }

    public void setContNum(String contNum) {
        this.contNum = contNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getcStatus() {
        return cStatus;
    }

    public void setcStatus(String cStatus) {
        this.cStatus = cStatus;
    }

    public String getsCancleTime() {
        return sCancleTime;
    }

    public void setsCancleTime(String sCancleTime) {
        this.sCancleTime = sCancleTime;
    }

    public String getSoIpAddress() {
        return soIpAddress;
    }

    public void setSoIpAddress(String soIpAddress) {
        this.soIpAddress = soIpAddress;
    }

    public String getcGenerateFlag() {
        return cGenerateFlag;
    }

    public void setcGenerateFlag(String cGenerateFlag) {
        this.cGenerateFlag = cGenerateFlag;
    }

    public String getOrderSty() {
        return orderSty;
    }

    public void setOrderSty(String orderSty) {
        this.orderSty = orderSty;
    }
}
