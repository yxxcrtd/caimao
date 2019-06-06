/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao.p2p;

import com.caimao.bana.api.entity.p2p.P2PInterestRecordEntity;
import com.caimao.bana.api.entity.req.FP2PQueryUserPageInterestReq;
import com.caimao.bana.api.entity.res.FP2PQueryUserPageInterestRes;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("P2PInterestRecordDao")
public class P2PInterestRecordDao extends SqlSessionDaoSupport{
    //获取利息列表分页
    public List<FP2PQueryUserPageInterestRes> queryFP2PQueryUserPageInterestResWithPage(FP2PQueryUserPageInterestReq req) throws Exception{
        return getSqlSession().selectList("P2PInterestRecord.queryFP2PQueryUserPageInterestResWithPage", req);
    }

    /**
     * 插入结息记录
     * @param p2pInterestRecord
     */
    public void save(P2PInterestRecordEntity p2pInterestRecord) {
        getSqlSession().insert("P2PInterestRecord.save", p2pInterestRecord);
    }

    /**
     * 查询指定的投资id是否已经在第几次发息过了
     * @param investId
     * @param interestTimes
     * @return
     */
    public Integer queryInvestInterestExist(Long investId, Integer interestTimes) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("investId", investId);
        paramsMap.put("interestTimes", interestTimes);
        return getSqlSession().selectOne("P2PInterestRecord.queryInvestInterestExist", paramsMap);
    }
}
