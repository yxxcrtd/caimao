/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.req.F830201Req;
import com.caimao.bana.api.entity.res.F830201Res;

/**
 * 
 * @author yanjg
 * 
 */
@Repository("interestBillDAO")
public class InterestBillDAO extends SqlSessionDaoSupport{
    public List<F830201Res> queryF830201ResWithPage(F830201Req paramF830201Req){
        return getSqlSession().selectList("TpzInterestBill.queryF830201ResWithPage", paramF830201Req);
    }
}
