/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * 计算K线基本
 */
public class KLineEntity implements Serializable {
    private Long open;
    private Long high;
    private Long close;
    private Long low;

    public Long getOpen() {
        return open;
    }

    public void setOpen(Long open) {
        this.open = open;
    }

    public Long getHigh() {
        return high;
    }

    public void setHigh(Long high) {
        this.high = high;
    }

    public Long getClose() {
        return close;
    }

    public void setClose(Long close) {
        this.close = close;
    }

    public Long getLow() {
        return low;
    }

    public void setLow(Long low) {
        this.low = low;
    }
}