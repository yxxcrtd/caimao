/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao.p2p;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.caimao.bana.api.entity.req.*;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.p2p.P2PInvestRecordEntity;
import com.caimao.bana.api.entity.p2p.P2PLoanRecordEntity;
import com.caimao.bana.api.entity.res.FP2PQueryLoanPageInvestRes;
import com.caimao.bana.api.entity.res.FP2PQueryLoanPageInvestWithUserRes;
import com.caimao.bana.api.entity.res.FP2PQueryStatisticsByUserRes;
import com.caimao.bana.api.entity.res.FP2PQueryUserPageInvestRes;
import com.caimao.bana.api.enums.EP2PInvestStatus;

@Repository("P2PInvestRecordDao")
public class P2PInvestRecordDao extends SqlSessionDaoSupport{
    //累计投资
    public Long queryUserTotalInvestment(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("P2PInvestRecord.queryUserTotalInvestment", paramMap);
    }

    //累计收益
    public Long queryUserTotalIncome(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("P2PInvestRecord.queryUserTotalIncome", paramMap);
    }

    //已收本金
    public Long queryUserTotalMarginReceived(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("P2PInvestRecord.queryUserTotalMarginReceived", paramMap);
    }

    //待收本金
    public Long queryUserTotalMarginClosed(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("P2PInvestRecord.queryUserTotalMarginClosed", paramMap);
    }

    //已收利息
    public Long queryUserTotalInterestReceived(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("P2PInvestRecord.queryUserTotalInterestReceived", paramMap);
    }

    //待收利息
    public Long queryUserTotalInterestClosed(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("P2PInvestRecord.queryUserTotalInterestClosed", paramMap);
    }

    //获取我的投资列表分页
    public List<FP2PQueryUserPageInvestRes> queryFP2PQueryUserPageInvestResWithPage(FP2PQueryUserPageInvestReq req) throws Exception{
        return getSqlSession().selectList("P2PInvestRecord.queryFP2PQueryUserPageInvestResWithPage", req);
    }

    //获取投资详情
    public P2PInvestRecordEntity queryUserInvestRecord(Long userId, Long investId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("investId", investId);
        return getSqlSession().selectOne("P2PInvestRecord.queryUserInvestRecord", paramMap);
    }

    //获取借贷的投标列表分页
    public List<FP2PQueryLoanPageInvestRes> queryFP2PQueryLoanPageInvestResWithPage(FP2PQueryLoanPageInvestReq req) throws Exception{
        return getSqlSession().selectList("P2PInvestRecord.queryFP2PQueryLoanPageInvestResWithPage", req);
    }
    
    //获取借贷的投标列表分页(关联用户)
    public List<FP2PQueryLoanPageInvestWithUserRes> queryFP2PQueryLoanAndUserPageInvestResWithPage(FP2PQueryLoanPageInvestWithUserReq req) throws Exception{
        return getSqlSession().selectList("P2PInvestRecord.queryFP2PQueryLoanAndUserPageInvestResWithPage", req);
    }
    
    //统计用户总投资次数与金额
    public List<FP2PQueryStatisticsByUserRes> queryStatisticsByUserWithPage(FP2PQueryStatisticsByUserReq req) throws Exception{
        return getSqlSession().selectList("P2PInvestRecord.queryStatisticsByUserWithPage", req);
    }

    //后台  查询投资人的投资列表
    public List<FP2PQueryLoanPageInvestWithUserRes> queryInvestPageWithPage(FP2PQueryPageInvestListReq req) throws Exception{
        return getSqlSession().selectList("P2PInvestRecord.queryInvestPageWithPage", req);
    }
    
    /**
     * @param paraMap
     * @return
     */
    public List<P2PInvestRecordEntity> getInvestRecordList(Map<String, Object> paraMap) {
        return getSqlSession().selectList("P2PInvestRecord.getInvestRecordList", 
                paraMap);
    }

    /**
     * @param p2pInvestRecord
     */
    public void update(P2PInvestRecordEntity p2pInvestRecord) {
        getSqlSession().update("P2PInvestRecord.update", p2pInvestRecord);
        
    }

    /**
     *
     * @param investId
     * @param investStatusEnum
     */
    public void updateStatusByInvestId(Long investId, EP2PInvestStatus investStatusEnum) {
        Map<String,Object> paraMap=new HashMap<String,Object>();
        paraMap.put("investId", investId);
        paraMap.put("investStatus", investStatusEnum.getCode());
        getSqlSession().update("P2PInvestRecord.updateStatus",paraMap);
    }

    /**
     * 查找一个标的投资用户列表,未满标
     * @param p2pLoanRecord
     * @param investStatus 
     * @return
     */
    public List<P2PInvestRecordEntity> getInvestUsers(P2PLoanRecordEntity p2pLoanRecord, EP2PInvestStatus investStatus) {
        Map<String,Object> paraMap=new HashMap<String,Object>();
        paraMap.put("targetId", p2pLoanRecord.getTargetId());
        paraMap.put("investStatus", investStatus.getCode());
        return getSqlSession().selectList("P2PInvestRecord.getFailedTargetUsers",paraMap);
    }
    public void save(P2PInvestRecordEntity p2pInvestRecord) {
        getSqlSession().insert("P2PInvestRecord.insert", p2pInvestRecord);
        
    }
    public void updateStatusByTargetId(Long targetId,byte status) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("targetId", targetId);
        map.put("status", status);
        map.put("lastUpdate", new Date(System.currentTimeMillis()));
        if(EP2PInvestStatus.FULL.getCode().byteValue()==status){
            map.put("fullDate", new Date(System.currentTimeMillis()));
        }
        getSqlSession().update("P2PInvestRecord.updateTargetStatus", map);
    }

    /**
     * @param paraMap
     */
    public void setInterestTime(Map<String, Object> paraMap) {
        getSqlSession().update("P2PInvestRecord.setInterestTime",paraMap);
    }

    public void updateExtByTargetId(Long targetId, Map<String, Object> updateInfo) throws Exception {
        Map<String, Object> paramMap = new HashMap<>(updateInfo);
        paramMap.put("targetId", targetId);
        getSqlSession().update("P2PInvestRecord.updateExtByTargetId", paramMap);
    }

    public List<P2PInvestRecordEntity> queryPrepaymentList() throws Exception{
        return getSqlSession().selectList("P2PInvestRecord.queryPrepaymentList");
    }
}