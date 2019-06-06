/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.dao.message;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.TpzPushMsgContentEntity;

/**
 * @author yanjg
 * 2015年5月22日
 */
@Repository
public class TpzPushMsgContentDao extends SqlSessionDaoSupport{

    /**
     * @param pushMsgId
     * @return
     */
    public TpzPushMsgContentEntity getById(Long pushMsgId) {
        return getSqlSession().selectOne("TpzPushMsgContent.getById", pushMsgId);
    }

    public void save(TpzPushMsgContentEntity entity) {
        this.getSqlSession().insert("TpzPushMsgContent.save", entity);
    }
    
}
