/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.ProdDicountEntity;

/**
 * @author yanjg
 * 2015年5月14日
 */
@Repository("prodDicountDAO")
public class ProdDicountDAO extends SqlSessionDaoSupport{

    /**
     * @param prodId
     * @return
     */
    public ProdDicountEntity getByProd(Long prodId) {
        return getSqlSession().selectOne("ProdDicount.getByProd",prodId);
    }

    /**
     * @param pd
     */
    public void updateUse(ProdDicountEntity pd) {
        getSqlSession().update("ProdDicount.updateUse",pd);
    }

}
