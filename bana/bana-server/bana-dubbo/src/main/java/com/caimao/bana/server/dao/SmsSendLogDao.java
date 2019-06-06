/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.SmsSendLogEntity;

/**
 * 短信记录表
 * @author yanjg
 * 
 */
@Repository("smsSendLogDao")
public class SmsSendLogDao extends SqlSessionDaoSupport{
    /**
     * 邀请用户绑定关系
     * @param userId
     * @param phone
     */
    public void createSmsSendLog(Long userId, String countryCode,
            String phone,String content,Byte sendStatus) {
        SmsSendLogEntity smsSendLog=new SmsSendLogEntity();
        smsSendLog.setUserId(userId);
        smsSendLog.setCountryCode(countryCode);
        smsSendLog.setPhone(phone);
        smsSendLog.setContent(content);
        smsSendLog.setSendStatus(sendStatus);
        
        getSqlSession().insert("sms_send_log.createSmsSendLog",smsSendLog);
    }
}
