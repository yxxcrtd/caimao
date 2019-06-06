/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.req;

import java.io.Serializable;

/**
 * @author yanjg
 * 2015年5月8日
 */
public class F830013Req implements Serializable{

        /**
     * 
     */
    private static final long serialVersionUID = 1L;
        public F830013Req()
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

        public String getUserTradePwd()
        {
            return userTradePwd;
        }

        public void setUserTradePwd(String userTradePwd)
        {
            this.userTradePwd = userTradePwd;
        }

        public String getQuestion1Id()
        {
            return question1Id;
        }

        public void setQuestion1Id(String question1Id)
        {
            this.question1Id = question1Id;
        }

        public String getQuestion1Answer()
        {
            return question1Answer;
        }

        public void setQuestion1Answer(String question1Answer)
        {
            this.question1Answer = question1Answer;
        }

        public String getQuestion2Id()
        {
            return question2Id;
        }

        public void setQuestion2Id(String question2Id)
        {
            this.question2Id = question2Id;
        }

        public String getQuestion2Answer()
        {
            return question2Answer;
        }

        public void setQuestion2Answer(String question2Answer)
        {
            this.question2Answer = question2Answer;
        }

        public String getQuestion3Id()
        {
            return question3Id;
        }

        public void setQuestion3Id(String question3Id)
        {
            this.question3Id = question3Id;
        }

        public String getQuestion3Answer()
        {
            return question3Answer;
        }

        public void setQuestion3Answer(String question3Answer)
        {
            this.question3Answer = question3Answer;
        }

        private Long userId;
        private String mobile;
        private String checkCode;
        private String userTradePwd;
        private String question1Id;
        private String question1Answer;
        private String question2Id;
        private String question2Answer;
        private String question3Id;
        private String question3Answer;
}