/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao;

import java.math.BigInteger;
import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.GradeDicountEntity;
import com.caimao.bana.api.entity.req.F830104Req;
import com.caimao.bana.api.entity.res.F830101Res;
import com.caimao.bana.api.entity.res.F831938Res;

/**
 * 
 * @author yanjg
 * 
 */
@Repository("gradeDicountDAO")
public class GradeDicountDAO extends SqlSessionDaoSupport{

    /**
     * @param userGrade
     * @return
     */
    public GradeDicountEntity getById(Short userGrade) {
        return getSqlSession().selectOne("GradeDicount.getById",userGrade);
    }

    /**
     * @param gd
     */
    public void save(GradeDicountEntity gd) {
        getSqlSession().insert("GradeDicount.save", gd);
    }

    /**
     * @param gd
     */
    public void update(GradeDicountEntity gd) {
        getSqlSession().update("GradeDicount.update", gd);
    }

    /**
     * @param userGrade
     */
    public void deleteById(Short userGrade) {
        getSqlSession().update("GradeDicount.deleteById", userGrade);
    }

    /**
     * @return
     */
    public List<F831938Res> queryF831938Res() {
        return getSqlSession().selectList("GradeDicount.queryF831938Res");
    }

    /**
     * @param userGrade
     * @return
     */
    public GradeDicountEntity getById(BigInteger userGrade) {
        return getSqlSession().selectOne("GradeDicount.getById",userGrade);
    }
}
