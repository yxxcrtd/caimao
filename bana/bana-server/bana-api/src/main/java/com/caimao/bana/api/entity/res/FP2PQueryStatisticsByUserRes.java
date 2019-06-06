/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.api.entity.res;

import java.io.Serializable;

/**
 * @author zxd $Id$
 * 
 */
public class FP2PQueryStatisticsByUserRes implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -7361695058500224408L;

    private String userName;

    private String mobile;

    private Long total;

    private Integer num;

    private Long investUserId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getInvestUserId() {
        return investUserId;
    }

    public void setInvestUserId(Long investUserId) {
        this.investUserId = investUserId;
    }
    
    
}
