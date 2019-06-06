/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.api.enums;

/**
 * @author Administrator $Id$
 * 
 */
public enum EP2PConfigKey {
    理财人最大投资("invest_max_value"),
    理财人最小投资("invest_min_value"),
    月费率最大值("month_rate_max"),
    借贷有效周期天数("period_day"),
    财猫是否跟投("caimao_with_invest"),
    剩余自动展期天数("auto_ext_day");
    private String key;
    private EP2PConfigKey(String key) {
        this.key = key;
    }
    public String getKey() {
        return this.key;
    }
}
