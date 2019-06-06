/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao.inviteInfo;

import com.caimao.bana.api.entity.InviteInfoEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("inviteInfoDao")
public class InviteInfoDao extends SqlSessionDaoSupport{
    public List<Map<String,Object>> getRegCount() throws Exception{
        return getSqlSession().selectList("InviteInfo.getRegCount");
    }

    public Integer getPZCount(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        List<Object> userList= getSqlSession().selectList("InviteInfo.getPZCount", paramMap);
        return userList.size();
    }

    public Long getPZAmountDay(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("InviteInfo.getPZAmountDay", paramMap);
    }

    public Long getPZAmountDayHis(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("InviteInfo.getPZAmountDayHis", paramMap);
    }

    public Long getPZAmountMonth(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("InviteInfo.getPZAmountMonth", paramMap);
    }

    public Long getPZAmountMonthHis(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("InviteInfo.getPZAmountMonthHis", paramMap);
    }

    public List<Map<String,Object>> getUserListRef() throws Exception{
        return getSqlSession().selectList("InviteInfo.getUserListRef");
    }

    public Long getPZAmountDayByUserId(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("InviteInfo.getPZAmountDayByUserId", paramMap);
    }

    public Long getPZAmountDayHisByUserId(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("InviteInfo.getPZAmountDayHisByUserId", paramMap);
    }

    public Long getPZAmountMonthByUserId(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("InviteInfo.getPZAmountMonthByUserId", paramMap);
    }

    public Long getPZAmountMonthHisByUserId(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("InviteInfo.getPZAmountMonthHisByUserId", paramMap);
    }

    public Long getInterest(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("InviteInfo.getInterest", paramMap);
    }
    
    public Integer updateInviteInfo(Long userId, Map<String, Object> updateInfo) throws Exception{
        Map<String, Object> paramMap = new HashMap<>(updateInfo);
        paramMap.put("userId", userId);
        return getSqlSession().update("InviteInfo.updateInviteInfo", paramMap);
    }

    public InviteInfoEntity getInviteInfoForUpdate(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("InviteInfo.getInviteInfoForUpdate", paramMap);
    }

    public InviteInfoEntity getInviteInfo(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("InviteInfo.getInviteInfo", paramMap);
    }

    public Integer insertInviteInfo(Map<String, Object> createInfo) throws Exception{
        return getSqlSession().insert("InviteInfo.insertInviteInfo", createInfo);
    }

    public List<Map<String, Object>> getBrokerageRankCnt() throws Exception{
        return getSqlSession().selectList("InviteInfo.getBrokerageRankCnt");
    }

    public List<Map<String, Object>> getBrokerageRankMoney() throws Exception{
        return getSqlSession().selectList("InviteInfo.getBrokerageRankMoney");
    }
}
