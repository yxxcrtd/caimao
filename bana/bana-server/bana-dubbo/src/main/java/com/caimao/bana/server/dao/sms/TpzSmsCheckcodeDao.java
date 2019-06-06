/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.dao.sms;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.TpzSmsCheckcodeEntity;
import com.caimao.bana.server.utils.MemoryDbidGenerator;

/**
 * @author yanjg
 * 2015年4月28日
 */
@Repository
public class TpzSmsCheckcodeDao extends SqlSessionDaoSupport {
    @Autowired
    private MemoryDbidGenerator dbidGenerator;

    /**
     * @param cond
     * @return
     */
    public TpzSmsCheckcodeEntity getByMobileAndBiz(TpzSmsCheckcodeEntity cond) {
        return getSqlSession().selectOne("TpzSmsCheckcode.getByMobileAndBiz", cond);
    }

    /**
     * @param smsCheckcode
     */
    public void update(TpzSmsCheckcodeEntity smsCheckcode) {
        System.out.println("the :"+smsCheckcode.getCheckTimes());
        getSqlSession().selectOne("TpzSmsCheckcode.update", smsCheckcode);
    }

    /**
     * @param smsCheckcode
     */
    public void save(TpzSmsCheckcodeEntity smsCheckcode) {
        smsCheckcode.setId(dbidGenerator.getNextId());
        getSqlSession().insert("TpzSmsCheckcode.save", smsCheckcode);
    }

}
