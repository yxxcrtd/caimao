package com.caimao.bana.api.entity;

import java.io.Serializable;

public class TpzUserPwdAnswerEntity implements Serializable {
    private static final long serialVersionUID = 3039635325650052206L;
    private String answerResult;
    private String questionId;
    private Long userId;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAnswerResult() {
        return answerResult;
    }

    public void setAnswerResult(String answerResult) {
        this.answerResult = answerResult;
    }
}