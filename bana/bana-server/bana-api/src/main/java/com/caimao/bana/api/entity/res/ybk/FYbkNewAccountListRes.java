package com.caimao.bana.api.entity.res.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * 前端API调用简单的最新的开户列表
 * Created by Administrator on 2015/9/16.
 */
public class FYbkNewAccountListRes implements Serializable {
    private String userName;
    private String phoneNo;
    private String exchangeName;
    private String exchangeShortName;
    private String status;
    private Date createDate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeShortName() {
        return exchangeShortName;
    }

    public void setExchangeShortName(String exchangeShortName) {
        this.exchangeShortName = exchangeShortName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
