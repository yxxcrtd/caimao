/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity;

import java.io.Serializable;

/**
 * @author yanjg
 *         2015年5月22日
 */
public class TpzPushMsgContentEntity implements Serializable {

    private static final long serialVersionUID = -9052239233366463840L;

    public TpzPushMsgContentEntity() {
    }

    public Long getPushMsgId() {
        return pushMsgId;
    }

    public void setPushMsgId(Long pushMsgId) {
        this.pushMsgId = pushMsgId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    private Long pushMsgId;
    private String msgContent;
}
