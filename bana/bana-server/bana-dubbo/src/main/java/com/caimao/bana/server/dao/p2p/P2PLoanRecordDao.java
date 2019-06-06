/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao.p2p;

import com.caimao.bana.api.entity.p2p.P2PLoanRecordEntity;
import com.caimao.bana.api.entity.req.FP2PQueryPageLoanAndUserReq;
import com.caimao.bana.api.entity.req.FP2PQueryPageLoanReq;
import com.caimao.bana.api.entity.req.FP2PQueryUserPageLoanReq;
import com.caimao.bana.api.entity.res.FP2PQueryPageLoanAndUserRes;
import com.caimao.bana.api.entity.res.FP2PQueryPageLoanRes;
import com.caimao.bana.api.entity.res.FP2PQueryUserPageLoanRes;
import com.caimao.bana.api.enums.EP2PLoanStatus;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("P2PLoanRecordDao")
public class P2PLoanRecordDao extends SqlSessionDaoSupport{

    /**
     * 保存借贷融资记录
     * @param recordEntity 借贷融资信息
     * @return  int
     */
    public Integer save(P2PLoanRecordEntity recordEntity) {
        return getSqlSession().insert("P2PLoanRecord.save", recordEntity);
    }

    //获取我的借贷列表分页
    public List<FP2PQueryUserPageLoanRes> queryFP2PQueryUserPageLoanResWithPage(FP2PQueryUserPageLoanReq req) throws Exception{
        return getSqlSession().selectList("P2PLoanRecord.queryFP2PQueryUserPageLoanResWithPage", req);
    }

    //获取借贷详情
    public P2PLoanRecordEntity queryUserLoanRecord(Long userId, Long loanId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("loanId", loanId);
        return getSqlSession().selectOne("P2PLoanRecord.queryUserLoanRecord", paramMap);
    }

    //获取借贷列表分页
    public List<FP2PQueryPageLoanRes> queryFP2PQueryPageLoanResWithPage(FP2PQueryPageLoanReq req) throws Exception{
        return getSqlSession().selectList("P2PLoanRecord.queryFP2PQueryPageLoanResWithPage", req);
    }

    //获取借贷满标列表分页
    public List<FP2PQueryPageLoanRes> queryFP2PQueryPageLoanFullResWithPage(FP2PQueryPageLoanReq req) throws Exception{
        return getSqlSession().selectList("P2PLoanRecord.queryFP2PQueryPageLoanFullResWithPage", req);
    }

    //获取借贷详情
    public P2PLoanRecordEntity queryLoanRecord(Long loanId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("loanId", loanId);
        return getSqlSession().selectOne("P2PLoanRecord.queryLoanRecord", paramMap);
    }
    
    //获取借贷详情，并加锁
    public P2PLoanRecordEntity getLoanRecordAndLock(Long loanId) throws Exception {
        return getSqlSession().selectOne("P2PLoanRecord.queryLoanRecordAndLock", loanId);
    }
    
    public List<P2PLoanRecordEntity> getLoanRecordList(Map<String,Object> paraMap){
        return getSqlSession().selectList("P2PLoanRecord.getLoanRecordList", paraMap);
    }
    
    /**回写记录
     * @param paraMap
     */
    public void update(Map<String, Object> paraMap) {
        getSqlSession().update("P2PLoanRecord.update", paraMap);
    }

    public void updateExt(Map<String, Object> paraMap){
        getSqlSession().update("P2PLoanRecord.updateExt", paraMap);
    }
    
    public void updateActualAndUserNum(P2PLoanRecordEntity p2pLoanRecord) {
        getSqlSession().update("P2PLoanRecord.updateActualAndUserNum", p2pLoanRecord);
    }
    //获取借贷列表分页
    public List<FP2PQueryPageLoanAndUserRes> queryFP2PQueryPageLoanAndUserResWithPage(FP2PQueryPageLoanAndUserReq req) throws Exception{
        return getSqlSession().selectList("P2PLoanRecord.queryFP2PQueryPageLoanAndUserResWithPage", req);
    }

    /**
     * @param liftDays 
     * @return
     */
    public List<P2PLoanRecordEntity> getFailedLoanRecordList(String liftDays) {
        return getSqlSession().selectList("P2PLoanRecord.getFailedLoanRecordList",Integer.parseInt(liftDays));
    }

    /**
     * 改变标的状态
     * @param targetId
     * @param fail
     */
    public void updateStatus(Long targetId, EP2PLoanStatus fail) {
        Map<String,Object> paraMap=new HashMap<String,Object>();
        paraMap.put("targetId", targetId);
        paraMap.put("targetStatus", fail.getCode());
        getSqlSession().update("P2PLoanRecord.updateStatus", paraMap);
    }

    /**
     * 更新财猫的出资数量
     * @param entity
     * @return
     */
    public int updateCaimaoValue(P2PLoanRecordEntity entity) {
        return getSqlSession().update("P2PLoanRecord.updateCaimaoValue", entity);
    }

    /**
     * 获取已发送配置申请并审核通过的p2p借贷列表
     * @return
     */
    public List<P2PLoanRecordEntity> getFullCheckedLoanRecordList() {
        return getSqlSession().selectList("P2PLoanRecord.getFullCheckedLoanRecordList");
    }

    //查询借贷总条数
    public Integer queryLoanCount(EP2PLoanStatus p2pLoanStatus) throws Exception {
        return getSqlSession().selectOne("P2PLoanRecord.queryLoanCount", p2pLoanStatus.getCode());
    }

    //查询借贷满标总条数
    public Integer queryLoanFullCount() throws Exception{
        return getSqlSession().selectOne("P2PLoanRecord.queryLoanFullCount");
    }

    //查询需要展期的p2p
    public List<P2PLoanRecordEntity> getNeedExtTargetList() throws Exception{
        return getSqlSession().selectList("P2PLoanRecord.getNeedExtTargetList");
    }

    //查询所有满标的展期标的
    public List<P2PLoanRecordEntity> getExtTargetFullList() throws Exception{
        return getSqlSession().selectList("P2PLoanRecord.getExtTargetFullList");
    }

    //更新标的表合约编号
    public Integer updateP2PLoanContractNo(Long orderNo, Long p2pContractNo){
        HashMap<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("orderNo", orderNo);
        updateInfo.put("contractNo", p2pContractNo);
        return getSqlSession().update("P2PLoanRecord.updateP2PLoanContractNo", updateInfo);
    }

    //获取所有已经还款的记录
    public List<P2PLoanRecordEntity> getRepayContractList() throws Exception{
        return getSqlSession().selectList("P2PLoanRecord.getRepayContractList");
    }

    //更新是否已经还款
    public void updateIsRepayment(Long targetId) throws Exception{
        getSqlSession().update("P2PLoanRecord.updateIsRepayment", targetId);
    }

    //查询展期次数
    public Integer queryExtCnt(Long targetId) throws Exception{
        return getSqlSession().selectOne("P2PLoanRecord.queryExtCnt", targetId);
    }

    // 获取P2P借贷记录，根据合约号码来查
    public P2PLoanRecordEntity getByContractNo(Long contractNo) throws Exception {
        return getSqlSession().selectOne("P2PLoanRecord.getByContractNo", contractNo);
    }
}
