/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.req;

import java.io.Serializable;

/**
 * @author yanjg
 * 2015年5月6日
 */
public class F830011Req extends TxBaseReq implements Serializable{
     public F830011Req()
     {
     }

     public Long getUserId()
     {
         return userId;
     }

     public void setUserId(Long userId)
     {
         this.userId = userId;
     }

     public String getMobile()
     {
         return mobile;
     }

     public void setMobile(String mobile)
     {
         this.mobile = mobile;
     }

     public String getCheckCode()
     {
         return checkCode;
     }

     public void setCheckCode(String checkCode)
     {
         this.checkCode = checkCode;
     }

     public String getQuestionId()
     {
         return questionId;
     }

     public void setQuestionId(String questionId)
     {
         this.questionId = questionId;
     }

     public String getAnswerResult()
     {
         return answerResult;
     }

     public void setAnswerResult(String answerResult)
     {
         this.answerResult = answerResult;
     }

     public String getUserTradePwd()
     {
         return userTradePwd;
     }

     public void setUserTradePwd(String userTradePwd)
     {
         this.userTradePwd = userTradePwd;
     }

     private Long userId;
     private String mobile;
     private String checkCode;
     private String questionId;
     private String answerResult;
     private String userTradePwd;
 }