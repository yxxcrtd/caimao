package com.caimao.bana.api.entity.ybk;

import java.io.Serializable;
import java.util.Date;

/**
* YbkDaxinEntity 实例对象
*
* Created by yangxinxin@huobi.com on 2015-11-17 17:47:39 星期二
*/
public class YbkDaxinEntity implements Serializable {

    private Long id;

    private Integer exchangeId;

    private String exchangeName;

    private String daxinName;

    private Date endDatetime;

    private String isShow;

    private String daxinUrl;

    private Integer sort;

    private Date created;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getDaxinName() {
        return daxinName;
    }

    public void setDaxinName(String daxinName) {
        this.daxinName = daxinName;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getDaxinUrl() {
        return daxinUrl;
    }

    public void setDaxinUrl(String daxinUrl) {
        this.daxinUrl = daxinUrl;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}