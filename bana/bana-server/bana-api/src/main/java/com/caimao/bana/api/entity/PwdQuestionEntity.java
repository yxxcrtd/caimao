/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity;

import java.io.Serializable;

/**
 * @author yanjg
 *         2015年5月8日
 */
public class PwdQuestionEntity implements Serializable {
    private static final long serialVersionUID = -3869998763787031154L;

    public PwdQuestionEntity() {
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    private String questionId;
    private String questionTitle;
}
