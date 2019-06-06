/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao.zeusStatistics;

import com.caimao.bana.api.entity.*;
import com.caimao.bana.api.entity.req.FTPZQueryHomsAccountJourReq;
import com.caimao.bana.api.entity.req.FTPZQueryUserAccountJourReq;
import com.caimao.bana.api.entity.req.FZeusHomsJourReq;
import com.caimao.bana.api.entity.req.FZeusUserBalanceDailyReq;
import com.caimao.bana.api.entity.zeus.BanaHomsFinanceHistoryEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountLoanLogEntity;
import com.caimao.bana.api.entity.zeus.ZeusUserBalanceDailyEntity;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository("zeusStatisticsDao")
public class ZeusStatisticsDao extends SqlSessionDaoSupport{
    @Autowired
    private MemoryDbidGenerator memoryDbidGenerator;

    public Long queryUserDepositTotal(Long userId) throws Exception {
        return getSqlSession().selectOne("ZeusStatistics.queryUserDepositTotal", userId);
    }

    public Long queryUserWithdrawTotal(Long userId) throws Exception {
        return getSqlSession().selectOne("ZeusStatistics.queryUserWithdrawTotal", userId);
    }

    public Date queryLastWithdrawSuccess(Long userId) throws Exception {
        return getSqlSession().selectOne("ZeusStatistics.queryLastWithdrawSuccess", userId);
    }

    public Long queryIsHasBadJour(Long userId, Date dateStart, Date dateEnd) throws Exception {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("dateStart", dateStart);
        paramsMap.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("ZeusStatistics.queryIsHasBadJour", paramsMap);
    }

    public List<TpzHomsAccountJourEntity> queryHomsAccountJourListWithPage(FTPZQueryHomsAccountJourReq req) throws Exception{
        return getSqlSession().selectList("ZeusStatistics.queryHomsAccountJourListWithPage", req);
    }

    public List<TpzAccountJourEntity> queryUserAccountJourListWithPage(FTPZQueryUserAccountJourReq req) throws Exception{
        return getSqlSession().selectList("ZeusStatistics.queryUserAccountJourListWithPage", req);
    }

    public Integer saveHomsFinanceHistory(HashMap<String, Object> record) throws Exception{
        return getSqlSession().insert("ZeusStatistics.saveHomsFinanceHistory", record);
    }

    public void saveHomsFinanceHistoryBatch(List<HashMap<String, Object>> recordList) throws Exception{
        getSqlSession().insert("ZeusStatistics.saveHomsFinanceHistoryBatch", recordList);
    }

    public List<BanaHomsFinanceHistoryEntity> queryHomsJourListWithPage(FZeusHomsJourReq req) throws Exception{
        return getSqlSession().selectList("ZeusStatistics.queryHomsJourListWithPage", req);
    }

    public List<ZeusUserBalanceDailyEntity> queryUserBalanceDailyListWithPage(FZeusUserBalanceDailyReq req) throws Exception{
        return getSqlSession().selectList("ZeusStatistics.queryUserBalanceDailyListWithPage", req);
    }

    public HashMap<String, Object> userBalanceDailyBalance() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyBalance");
    }

    public Long userBalanceDailyLoanInterest() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyLoanInterest");
    }

    public Long userBalanceDailyLoanCurrent() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyLoanCurrent");
    }

    public Long userBalanceDailyLoanHis() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyLoanHis");
    }

    public Long userBalanceDailyLoanRepay() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyLoanRepay");
    }

    public Long userBalanceDailyP2PInterest() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyP2PInterest");
    }

    public Long userBalanceDailyP2PInterestPay() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyP2PInterestPay");
    }

    public Long userBalanceDailyP2PInvest() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyP2PInvest");
    }

    public Long userBalanceDailyP2PInvestFail() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyP2PInvestFail");
    }

    public Long userBalanceDailyP2PInvestSuccess() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyP2PInvestSuccess");
    }

    public Long userBalanceDailyP2PInvestRepay() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyP2PInvestRepay");
    }

    public Long userBalanceDailyDepositTotal() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyDepositTotal");
    }

    public Long userBalanceDailyWithdrawTotal() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyWithdrawTotal");
    }

    public Long userBalanceDailyLoanBalance() throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.userBalanceDailyLoanBalance");
    }

    public Integer userBalanceDailySave(ZeusUserBalanceDailyEntity zeusUserBalanceDailyEntity) throws Exception{
        return getSqlSession().insert("ZeusStatistics.userBalanceDailySave", zeusUserBalanceDailyEntity);
    }

    public Long getTotalByBizType(Long userId, String bizType, String seqFlag, String dateStart, String dateEnd) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("accountBizType", bizType);
        params.put("seqFlag", seqFlag);
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("ZeusStatistics.getTotalByBizType", params);
    }

    public Long getAccountTotal(Long userId, String dateStart, String dateEnd) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("ZeusStatistics.getAccountTotal", params);
    }

    public List<ZeusHomsAccountLoanLogEntity> queryHomsAccountLoanLog(ZeusHomsAccountLoanLogEntity zeusHomsAccountLoanLogEntity) throws Exception{
        return getSqlSession().selectList("ZeusStatistics.queryHomsAccountLoanLog", zeusHomsAccountLoanLogEntity);
    }

    public void saveHomsAccountLoanLog(ZeusHomsAccountLoanLogEntity zeusHomsAccountLoanLogEntity) throws Exception{
        getSqlSession().selectList("ZeusStatistics.saveHomsAccountLoanLog", zeusHomsAccountLoanLogEntity);
    }

    public BanaHomsFinanceHistoryEntity queryHomsFinanceHistoryStart(String transDate, Long accountUnitNo, Long transAmount, String remark) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("transDate", transDate);
        params.put("accountUnitNo", accountUnitNo);
        params.put("transAmount", transAmount);
        params.put("remark", remark);
        return getSqlSession().selectOne("ZeusStatistics.queryHomsFinanceHistoryStart", params);
    }

    public BanaHomsFinanceHistoryEntity queryHomsFinanceHistoryEnd(Long transNo, Long accountUnitNo, String remark) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("transNo", transNo);
        params.put("accountUnitNo", accountUnitNo);
        params.put("remark", remark);
        return getSqlSession().selectOne("ZeusStatistics.queryHomsFinanceHistoryEnd", params);
    }

    public BanaHomsFinanceHistoryEntity queryHomsFinanceHistoryEnd(Long transNo, Long accountUnitNo, String remark, String remark2) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("transNo", transNo);
        params.put("accountUnitNo", accountUnitNo);
        params.put("remark", remark);
        params.put("remark2", remark);
        return getSqlSession().selectOne("ZeusStatistics.queryHomsFinanceHistoryEnd2", params);
    }

    public Long queryHomsFinanceHistoryTotalByBizType(String transBizType, HashMap<String, Object> params) throws Exception{
        HashMap<String, Object> paramsAll = new HashMap<>(params);
        paramsAll.put("transBizType", transBizType);
        return getSqlSession().selectOne("ZeusStatistics.queryHomsFinanceHistoryTotalByBizType", paramsAll);
    }

    public Long queryHomsFinanceHistoryTotalByRemark(String remark, HashMap<String, Object> params) throws Exception{
        HashMap<String, Object> paramsAll = new HashMap<>(params);
        paramsAll.put("remark", remark);
        return getSqlSession().selectOne("ZeusStatistics.queryHomsFinanceHistoryTotalByRemark", paramsAll);
    }

    public Long queryHomsFinanceHistoryTotalByRemark(String remark, String remark2, HashMap<String, Object> params) throws Exception{
        HashMap<String, Object> paramsAll = new HashMap<>(params);
        paramsAll.put("remark", remark);
        paramsAll.put("remark2", remark2);
        return getSqlSession().selectOne("ZeusStatistics.queryHomsFinanceHistoryTotalByRemark2", paramsAll);
    }

    public Long queryOperationRegNew(Long refUserId, String dateStart, String dateEnd) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("refUserId", refUserId);
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("ZeusStatistics.queryOperationRegNew", params);
    }

    public Long queryOperationRegAll(Long refUserId) throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.queryOperationRegAll", refUserId);
    }

    public Long queryOperationRealNameNew(Long refUserId, String dateStart, String dateEnd) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("refUserId", refUserId);
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("ZeusStatistics.queryOperationRealNameNew", params);
    }

    public Long queryOperationRealNameAll(Long refUserId) throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.queryOperationRealNameAll", refUserId);
    }

    // 邮币卡当日注册数
    public Long queryYBKRegiestNew(Long refUserId, String dateStart, String dateEnd) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("refUserId", refUserId);
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("ZeusStatistics.queryYBKRegiestNew", params);
    }
    // 邮币卡总的注册数
    public Long queryYBKRegiestAll(Long refUserId) throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.queryYBKRegiestAll", refUserId);
    }

    public Long queryOperationLoanUserNew(Long refUserId, String dateStart, String dateEnd) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("refUserId", refUserId);
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("ZeusStatistics.queryOperationLoanUserNew", params);
    }

    public Long queryOperationLoanUserAll(Long refUserId) throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.queryOperationLoanUserAll", refUserId);
    }

    public Long queryOperationLoanAmountNew(Long refUserId, String dateStart, String dateEnd) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("refUserId", refUserId);
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("ZeusStatistics.queryOperationLoanAmountNew", params);
    }

    public Long queryOperationLoanAmountAll(Long refUserId) throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.queryOperationLoanAmountAll", refUserId);
    }

    public Long queryOperationLoanInterestNew(Long refUserId, String dateStart, String dateEnd) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("refUserId", refUserId);
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        return getSqlSession().selectOne("ZeusStatistics.queryOperationLoanInterestNew", params);
    }

    public Long queryOperationLoanInterestAll(Long refUserId) throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.queryOperationLoanInterestAll", refUserId);
    }

    public List<TpzUserEntity> queryDateRealUserList(String refUserId, String dateStart, String dateEnd) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("refUserId", refUserId);
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        return getSqlSession().selectList("ZeusStatistics.queryDateRealUserList", params);
    }

    public TpzLoanContractEntity queryContractByNo(Long contractNo) throws Exception{
        return getSqlSession().selectOne("ZeusStatistics.queryContractByNo", contractNo);
    }

    public void insertContractHistory(TpzLoanContractEntity entity) throws Exception{
        getSqlSession().insert("ZeusStatistics.insertContractHistory", entity);
    }

    public void insertContractRepay(TpzRepayOrderEntity entity) throws Exception{
        entity.setOrderNo(this.memoryDbidGenerator.getNextId());
        getSqlSession().insert("ZeusStatistics.insertContractRepay", entity);
    }

    public void deleteContractByNo(Long contractNo) throws Exception{
        getSqlSession().delete("ZeusStatistics.deleteContractByNo", contractNo);
    }
}
