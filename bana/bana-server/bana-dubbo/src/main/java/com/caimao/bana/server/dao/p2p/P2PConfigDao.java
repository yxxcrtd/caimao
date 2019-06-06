/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao.p2p;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.p2p.P2PConfigEntity;

@Repository("P2PConfigDao")
public class P2PConfigDao extends SqlSessionDaoSupport {

    public P2PConfigEntity getProdSetting(Long prodId, Integer prodLever) {
        P2PConfigEntity pc = new P2PConfigEntity();
        pc.setProdId(prodId);
        pc.setProdLever(prodLever);
        return getSqlSession().selectOne("p2pConfig.selectByPrimaryKey", pc);
    }
    
    public List<P2PConfigEntity> getProdSetting(Long prodId) {
        return getSqlSession().selectList("p2pConfig.selectByProdId", prodId);
    }

    public void save(P2PConfigEntity pc) {
        getSqlSession().selectOne("p2pConfig.insert", pc);
    }

    public void update(P2PConfigEntity pc) {
        getSqlSession().selectOne("p2pConfig.updateByPrimaryKey", pc);
    }
}
