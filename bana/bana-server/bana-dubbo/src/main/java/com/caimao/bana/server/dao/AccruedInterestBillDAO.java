/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.TpzAccruedInterestBillEntity;
import com.caimao.bana.api.entity.req.F830202Req;
import com.caimao.bana.api.entity.req.F831209Req;
import com.caimao.bana.api.entity.res.F830202Res;
import com.caimao.bana.api.entity.res.F831209Res;

/**
 * 
 * @author yanjg
 * 
 */
@Repository("accruedInterestBillDAO")
public class AccruedInterestBillDAO extends SqlSessionDaoSupport{
    /**
     * @param req
     * @return
     */
    public List<F830202Res> queryF830202ResWithPage(F830202Req req) {
        return getSqlSession().selectList("TpzAccruedInterestBill.queryF830202ResWithPage", req);
    }

    public int getByDate(Map<String, Object> paramMap){
        return getSqlSession().selectOne("TpzAccruedInterestBill.getByDate",paramMap);
    }

    public List<TpzAccruedInterestBillEntity> queryByDate(String paramString){
        return getSqlSession().selectList("TpzAccruedInterestBill.queryByDate",paramString);
    }

    public List<F831209Res> queryBillByRefUserWithPage(F831209Req paramF831209Req){
        return getSqlSession().selectList("TpzAccruedInterestBill.queryBillByRefUserWithPage", paramF831209Req);
    }
    
    
}
