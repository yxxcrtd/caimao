/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.dao.email;

import com.caimao.bana.api.entity.EmailSendEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public class BanaEmailSendDao extends SqlSessionDaoSupport {
    public List<EmailSendEntity> queryEmailSendList() throws Exception{
        return getSqlSession().selectList("BanaEmailSend.queryEmailSendList");
    }

    public Integer insertEmailSend(EmailSendEntity emailSendEntity) throws Exception{
        return getSqlSession().insert("BanaEmailSend.insertEmailSend", emailSendEntity);
    }

    public Integer sendSuccess(Long id, Integer sendStatus) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("sendStatus", sendStatus);
        params.put("sendTime", new Date());
        return getSqlSession().insert("BanaEmailSend.sendSuccess", params);
    }
}
