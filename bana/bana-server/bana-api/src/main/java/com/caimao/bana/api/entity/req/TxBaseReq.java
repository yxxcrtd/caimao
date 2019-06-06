/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.req;

/**
 * @author yanjg
 * 2015年6月2日
 */
public class TxBaseReq {
    private String txId;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
//        this.txId = txId+"_"+System.currentTimeMillis();
        this.txId=txId;
    }
    
}
