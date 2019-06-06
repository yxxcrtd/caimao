/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao.risk;

import com.caimao.bana.api.entity.RiskRecordEntity;
import com.caimao.bana.api.enums.RiskMsgSendType;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository("riskRecordDao")
public class RiskRecordDao extends SqlSessionDaoSupport {
    public RiskRecordEntity getUserLastSendMsg(Long userId, RiskMsgSendType riskMsgSendType) throws Exception{
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("sendType", riskMsgSendType.getCode());
        return getSqlSession().selectOne("RiskRecord.getUserLastSendMsg", paramsMap);
    }

    public Integer insertRiskRecord(RiskRecordEntity riskRecordEntity) throws Exception{
        return getSqlSession().insert("RiskRecord.doSave", riskRecordEntity);
    }
}
