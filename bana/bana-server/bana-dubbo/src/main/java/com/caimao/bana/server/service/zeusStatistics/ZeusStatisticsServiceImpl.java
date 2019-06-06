/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.server.service.zeusStatistics;

import com.caimao.bana.api.entity.*;
import com.caimao.bana.api.entity.req.*;
import com.caimao.bana.api.entity.zeus.BanaHomsFinanceHistoryEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountLoanLogEntity;
import com.caimao.bana.api.entity.zeus.ZeusUserBalanceDailyEntity;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.service.IZeusStatisticsService;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.accountDao.TpzAccountJourDao;
import com.caimao.bana.server.dao.homs.TpzHomsAccountChildDao;
import com.caimao.bana.server.dao.homs.TpzHomsAccountLoanDao;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.dao.zeusStatistics.ZeusStatisticsDao;
import com.caimao.bana.server.utils.UserFinanceUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 后台数据统计查询
 */
@Service("zeusStatisticsService")
public class ZeusStatisticsServiceImpl implements IZeusStatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(ZeusStatisticsServiceImpl.class);

    @Resource
    private ZeusStatisticsDao zeusStatisticsDao;
    @Resource
    private TpzAccountDao tpzAccountDao;
    @Resource
    private TpzUserDao tpzUserDao;
    @Resource
    private TpzHomsAccountLoanDao tpzHomsAccountLoanDao;
    @Resource
    private TpzHomsAccountChildDao tpzHomsAccountChildDao;
    @Resource
    private TpzAccountJourDao tpzAccountJourDao;

    //获取用户充值总额
    @Override
    public Long queryUserDepositTotal(Long userId) throws Exception {
        return zeusStatisticsDao.queryUserDepositTotal(userId);
    }

    //获取用户提现总额
    @Override
    public Long queryUserWithdrawTotal(Long userId) throws Exception {
        return zeusStatisticsDao.queryUserWithdrawTotal(userId);
    }

    //获取用户最后一笔成功的提现时间
    @Override
    public Date queryLastWithdrawSuccess(Long userId) throws Exception {
        return zeusStatisticsDao.queryLastWithdrawSuccess(userId);
    }

    //获取用户坏的财务流水
    @Override
    public Long queryIsHasBadJour(Long userId, Date dateStart, Date dateEnd) throws Exception {
        return zeusStatisticsDao.queryIsHasBadJour(userId, dateStart, dateEnd);
    }

    //获取用户财务流水列表
    @Override
    public FTPZQueryHomsAccountJourReq queryHomsAccountJourListWithPage(FTPZQueryHomsAccountJourReq req) throws Exception {
        req.setItems(zeusStatisticsDao.queryHomsAccountJourListWithPage(req));
        return req;
    }

    //获取用户财务流水列表
    @Override
    public FTPZQueryUserAccountJourReq queryUserAccountJourListWithPage(FTPZQueryUserAccountJourReq req) throws Exception {
        req.setItems(zeusStatisticsDao.queryUserAccountJourListWithPage(req));
        return req;
    }

    //保存homs系统流水
    @Override
    public Integer saveHomsFinanceHistory(HashMap<String, Object> record) throws Exception {
        return zeusStatisticsDao.saveHomsFinanceHistory(record);
    }

    @Override
    public void saveHomsFinanceHistoryBatch(List<HashMap<String, Object>> recordList) throws Exception {
        zeusStatisticsDao.saveHomsFinanceHistoryBatch(recordList);
    }

    //查询homs系统流水列表
    @Override
    public FZeusHomsJourReq queryHomsJourListWithPage(FZeusHomsJourReq req) throws Exception {
        req.setItems(zeusStatisticsDao.queryHomsJourListWithPage(req));
        return req;
    }

    //查询用户资产汇总日报列表
    @Override
    public FZeusUserBalanceDailyReq queryUserBalanceDailyList(FZeusUserBalanceDailyReq req) throws Exception {
        req.setItems(zeusStatisticsDao.queryUserBalanceDailyListWithPage(req));
        return req;
    }

    //查询用户资产汇总信息
    @Override
    public void userBalanceDailySave() throws Exception {
        ZeusUserBalanceDailyEntity zeusUserBalanceDailyEntity = new ZeusUserBalanceDailyEntity();
        zeusUserBalanceDailyEntity.setCreated(new Date());
        zeusUserBalanceDailyEntity.setCreateDatetime(new Date());
        //查询总可用、总冻结
        HashMap<String, Object> dataBalance = zeusStatisticsDao.userBalanceDailyBalance();
        zeusUserBalanceDailyEntity.setAvailableAmount(Long.valueOf(dataBalance.get("availableAmount").toString()));
        zeusUserBalanceDailyEntity.setFreezeAmount(Long.valueOf(dataBalance.get("freezeAmount").toString()));
        zeusUserBalanceDailyEntity.setUserCount(Long.valueOf(dataBalance.get("userCount").toString()));
        //查询总利息
        zeusUserBalanceDailyEntity.setLoanInterestTotal(zeusStatisticsDao.userBalanceDailyLoanInterest());
        //查询借贷总额
        Long LoanCur = zeusStatisticsDao.userBalanceDailyLoanCurrent();
        Long LoanHis = zeusStatisticsDao.userBalanceDailyLoanHis();
        zeusUserBalanceDailyEntity.setLoanTotal(LoanCur + LoanHis);
        //查询借贷已还
        zeusUserBalanceDailyEntity.setLoanTotalRepay(zeusStatisticsDao.userBalanceDailyLoanRepay());
        //查询p2p总计利息
        zeusUserBalanceDailyEntity.setP2pInterestTotal(zeusStatisticsDao.userBalanceDailyP2PInterest());
        //查询p2p总发利息
        zeusUserBalanceDailyEntity.setP2pInterestTotalPay(zeusStatisticsDao.userBalanceDailyP2PInterestPay());
        //查询投资总额
        zeusUserBalanceDailyEntity.setP2pInvestTotal(zeusStatisticsDao.userBalanceDailyP2PInvest());
        //查询投资流标
        zeusUserBalanceDailyEntity.setP2pInvestTotalFail(zeusStatisticsDao.userBalanceDailyP2PInvestFail());
        //查询投资计息
        zeusUserBalanceDailyEntity.setP2pInvestTotalSuccess(zeusStatisticsDao.userBalanceDailyP2PInvestSuccess());
        //查询投资返还
        zeusUserBalanceDailyEntity.setP2pInvestTotalRepay(zeusStatisticsDao.userBalanceDailyP2PInvestRepay());
        //查询充值
        zeusUserBalanceDailyEntity.setDepositTotal(zeusStatisticsDao.userBalanceDailyDepositTotal());
        //查询提现
        zeusUserBalanceDailyEntity.setWithdrawTotal(zeusStatisticsDao.userBalanceDailyWithdrawTotal());
        //查询当前借款总额
        zeusUserBalanceDailyEntity.setLoanBalance(zeusStatisticsDao.userBalanceDailyLoanBalance());

        zeusStatisticsDao.userBalanceDailySave(zeusUserBalanceDailyEntity);
    }

    @Override
    public HashMap<String, Object> queryUserFinanceStream(FTPZQueryAccountJourStreamReq req) throws Exception {
        HashMap<String, Object> userFinanceStream = new HashMap<>();
        //查询资产账户
        TpzAccountEntity tpzAccountEntity = tpzAccountDao.getAccountByUserId(req.getUserId());
        if(tpzAccountEntity == null) return null;
        userFinanceStream.put("pzAccountId", tpzAccountEntity.getPzAccountId());
        //查询用户基本信息
        TpzUserEntity tpzUserEntity = tpzUserDao.getById(req.getUserId());
        userFinanceStream.put("userRealName", tpzUserEntity.getUserRealName());
        userFinanceStream.put("mobile", tpzUserEntity.getMobile());
        //查询现金流量
        HashMap<String, List<HashMap<String, String>>> accountBizTypeMap = UserFinanceUtils.getAccountBizTypeMap();
        //入账
        for(HashMap<String, String> accountIn:accountBizTypeMap.get("accountIn")){
            userFinanceStream.put("accountIn" + accountIn.get("typeCode"), zeusStatisticsDao.getTotalByBizType(req.getUserId(), accountIn.get("typeCode"), "2", req.getDateStart().replace("-", ""), req.getDateEnd().replace("-", "")));
        }
        //出账
        for(HashMap<String, String> accountIn:accountBizTypeMap.get("accountOut")){
            userFinanceStream.put("accountOut" + accountIn.get("typeCode"), zeusStatisticsDao.getTotalByBizType(req.getUserId(), accountIn.get("typeCode"), "1", req.getDateStart().replace("-", ""), req.getDateEnd().replace("-", "")));
        }
        //总计
        userFinanceStream.put("accountTotal", zeusStatisticsDao.getAccountTotal(req.getUserId(), req.getDateStart().replace("-", ""), req.getDateEnd().replace("-", "")));
        return userFinanceStream;
    }

    @Override
    public void updateHomsAccountLoanLog() throws Exception {
        //查询所有
        List<TpzHomsAccountLoanEntity> homsAccountLoanList = this.tpzHomsAccountLoanDao.getHomsAccount(new TpzHomsAccountLoanEntity());
        if(homsAccountLoanList != null){
            for (TpzHomsAccountLoanEntity homsAccountLoan:homsAccountLoanList){
                try{
                    //查询是否存在
                    ZeusHomsAccountLoanLogEntity zeusHomsAccountLoanLogEntity = new ZeusHomsAccountLoanLogEntity();
                    zeusHomsAccountLoanLogEntity.setUserId(homsAccountLoan.getUserId());
                    zeusHomsAccountLoanLogEntity.setHomsCombineId(homsAccountLoan.getHomsCombineId());
                    zeusHomsAccountLoanLogEntity.setCreateDatetime(homsAccountLoan.getCreateDatetime());
                    //查询资产id
                    TpzHomsAccountChildEntity tpzHomsAccountChildEntity = tpzHomsAccountChildDao.queryAccountChildByAccount(homsAccountLoan.getHomsFundAccount(), homsAccountLoan.getHomsCombineId());
                    if(tpzHomsAccountChildEntity == null) continue;
                    zeusHomsAccountLoanLogEntity.setAssetId(tpzHomsAccountChildEntity.getAssetId());

                    List<ZeusHomsAccountLoanLogEntity> homsAccountLoanLogExit = zeusStatisticsDao.queryHomsAccountLoanLog(zeusHomsAccountLoanLogEntity);
                    if(homsAccountLoanLogExit.size() == 0){
                        zeusHomsAccountLoanLogEntity.setPzAccountId(homsAccountLoan.getPzAccountId());
                        zeusHomsAccountLoanLogEntity.setContractNo(homsAccountLoan.getContractNo());
                        zeusHomsAccountLoanLogEntity.setHomsFundAccount(homsAccountLoan.getHomsFundAccount());
                        zeusHomsAccountLoanLogEntity.setHomsManageId(homsAccountLoan.getHomsManageId());
                        zeusHomsAccountLoanLogEntity.setAssetId(tpzHomsAccountChildEntity.getAssetId());
                        zeusHomsAccountLoanLogEntity.setBeginAmount(homsAccountLoan.getBeginAmount());
                        zeusHomsAccountLoanLogEntity.setOperatorNo(homsAccountLoan.getOperatorNo());
                        zeusStatisticsDao.saveHomsAccountLoanLog(zeusHomsAccountLoanLogEntity);
                    }
                }catch(Exception e){
                    System.out.println("更新homs账户借款log" + homsAccountLoan.getId() + "更新错误");
                }
            }
        }
    }

    @Override
    public List<HashMap<String, Object>> queryUserHomsStatistics(Long userId) throws Exception {
        //查询用户是否存在
        TpzUserEntity tpzUserEntity = tpzUserDao.getById(userId);
        if(tpzUserEntity == null) return null;

        List<HashMap<String, Object>> userHomsStatistics = new ArrayList<>();
        //查询用户所有homs历史
        ZeusHomsAccountLoanLogEntity zeusHomsAccountLoanLogEntity = new ZeusHomsAccountLoanLogEntity();
        zeusHomsAccountLoanLogEntity.setUserId(userId);
        List<ZeusHomsAccountLoanLogEntity> homsAccountLoanLogList = zeusStatisticsDao.queryHomsAccountLoanLog(zeusHomsAccountLoanLogEntity);
        if(homsAccountLoanLogList.size() == 0) return null;
        //循环查询每个账户的流水记录统计信息
        for(ZeusHomsAccountLoanLogEntity homsAccountLoanLog:homsAccountLoanLogList){
            HashMap<String, Object> homsStatistics = new HashMap<>();
            //姓名
            homsStatistics.put("userRealName", tpzUserEntity.getUserRealName());
            //资产账户
            homsStatistics.put("assetId", homsAccountLoanLog.getAssetId());
            //启用日期
            homsStatistics.put("createDatetime", homsAccountLoanLog.getCreateDatetime());
            //合约编号
            homsStatistics.put("contractNo", homsAccountLoanLog.getContractNo());
            //开始金额
            homsStatistics.put("beginAmount", homsAccountLoanLog.getBeginAmount());
            //********************************开始统计homs流水**********************************//
            //查找开始记录ID
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            BanaHomsFinanceHistoryEntity HomsFinanceStart = zeusStatisticsDao.queryHomsFinanceHistoryStart(sdf.format(homsAccountLoanLog.getCreateDatetime()), Long.parseLong(homsAccountLoanLog.getAssetId()), homsAccountLoanLog.getBeginAmount(), "借款成功");
            if(HomsFinanceStart != null){
                Long transNoStart = HomsFinanceStart.getTransNo();
                homsStatistics.put("transNoStart", transNoStart);
                Long transNoEnd = null;
                //查找结束记录ID
                //BanaHomsFinanceHistoryEntity HomsFinanceEnd = zeusStatisticsDao.queryHomsFinanceHistoryEnd(HomsFinanceStart.getTransNo(), Long.parseLong(homsAccountLoanLog.getAssetId()), "HOMS账户回收", "将单元所有资金划回管理单元");
                BanaHomsFinanceHistoryEntity HomsFinanceEnd = zeusStatisticsDao.queryHomsFinanceHistoryEnd(HomsFinanceStart.getTransNo(), Long.parseLong(homsAccountLoanLog.getAssetId()), "借款成功");
                if(HomsFinanceEnd != null){
                    transNoEnd = HomsFinanceEnd.getTransNo() - 1;
                }
                homsStatistics.put("transNoEnd", transNoEnd);
                HashMap<String, Object> params = new HashMap<>();
                params.put("accountUnitNo", Long.parseLong(homsAccountLoanLog.getAssetId()));
                params.put("transNoStart", transNoStart);
                params.put("transNoEnd", transNoEnd);

                //借款成功
                homsStatistics.put("LoanSuccess", zeusStatisticsDao.queryHomsFinanceHistoryTotalByRemark("借款成功", params));
                //证券买入
                homsStatistics.put("stockBuy", zeusStatisticsDao.queryHomsFinanceHistoryTotalByBizType("证券买入", params));
                //证券卖出
                homsStatistics.put("stockSell", zeusStatisticsDao.queryHomsFinanceHistoryTotalByBizType("证券卖出", params));
                //债券买入
                homsStatistics.put("bondBuy", zeusStatisticsDao.queryHomsFinanceHistoryTotalByBizType("债券买入", params));
                //债券卖出
                homsStatistics.put("bondSell", zeusStatisticsDao.queryHomsFinanceHistoryTotalByBizType("债券卖出", params));
                //还款
                homsStatistics.put("repay", zeusStatisticsDao.queryHomsFinanceHistoryTotalByRemark("还款", params));
                //提盈
                homsStatistics.put("mentionSurplus", zeusStatisticsDao.queryHomsFinanceHistoryTotalByRemark("提盈", params));
                //追保
                homsStatistics.put("paulChase", zeusStatisticsDao.queryHomsFinanceHistoryTotalByRemark("追保", "增加保证金", params));
                //调整佣金差
                homsStatistics.put("poorAdjustmentCommission", zeusStatisticsDao.queryHomsFinanceHistoryTotalByBizType("调整佣金差", params));
                //红利到帐
                homsStatistics.put("bonusArrive", zeusStatisticsDao.queryHomsFinanceHistoryTotalByBizType("红利到帐", params));
                //结息
                homsStatistics.put("bearInterest", zeusStatisticsDao.queryHomsFinanceHistoryTotalByRemark("结息", params));
                //资金划转
                homsStatistics.put("transferOfFunds", zeusStatisticsDao.queryHomsFinanceHistoryTotalByRemark("资金划转(手工)", "资金增加(手工)", params));
                //账户回收
                homsStatistics.put("accountRecovery", zeusStatisticsDao.queryHomsFinanceHistoryTotalByRemark("HOMS账户回收", "将单元所有资金划回管理单元", params));
                //佣金清算补差
                homsStatistics.put("liquidationCommissionMakeup", zeusStatisticsDao.queryHomsFinanceHistoryTotalByRemark("佣金清算补差", params));
                //现货资金自动核对
                homsStatistics.put("stockFundsCheck", zeusStatisticsDao.queryHomsFinanceHistoryTotalByRemark("现货核对", "现货资金自动核对", params));
                //融券回购
                homsStatistics.put("rongquanhuigou", zeusStatisticsDao.queryHomsFinanceHistoryTotalByBizType("融券回购", params));
                //融券购回
                homsStatistics.put("rongquangouhui", zeusStatisticsDao.queryHomsFinanceHistoryTotalByBizType("融券购回", params));
                //账户余额
                Long balanceTotal = 0L;
                List StringFilter = Arrays.asList("LoanSuccess","stockBuy","stockSell","bondBuy","bondSell","repay","mentionSurplus","paulChase","poorAdjustmentCommission","bonusArrive","bearInterest","transferOfFunds","accountRecovery","liquidationCommissionMakeup","stockFundsCheck","rongquanhuigou","rongquangouhui");
                for (Map.Entry entry : homsStatistics.entrySet()) {
                    if(StringFilter.contains(entry.getKey().toString())){
                        if(entry.getValue() != null) balanceTotal += Long.parseLong(entry.getValue().toString());
                    }
                }
                homsStatistics.put("accountBalance", balanceTotal);
            }
            userHomsStatistics.add(homsStatistics);
        }
        return userHomsStatistics;
    }

    @Override
    public void changeUserAccountJourBizType(Long id, String bizType) throws Exception {
        if(bizType == null || bizType.equals("") || bizType.equals("00")) throw new Exception("变更的类型不正确");
        //检测变更的类型是否合法
        if(EAccountBizType.findByCode(bizType) == null) throw new Exception("变更的类型不存在");
        //检测记录是否存在
        TpzAccountJourEntity tpzAccountJourEntity = tpzAccountJourDao.queryById(id);
        if(tpzAccountJourEntity == null || !tpzAccountJourEntity.getAccountBizType().equals("00")) throw new Exception("记录不存在,或业务类型不为其他");
        //变更记录
        tpzAccountJourDao.updateBizType(id, bizType);
    }

    //查询每日新增数据
    @Override
    public List<HashMap> queryDailyNewUser(String dateStart, String dateEnd, Integer selectId, String customId) throws Exception {
        if(dateStart == null || dateStart.equals("") || dateEnd == null || dateEnd.equals("")) return null;
        dateStart = dateStart + " 00:00:00";
        dateEnd = dateEnd + " 23:59:59";
        List<HashMap> dataList = new ArrayList<>();
        Integer startNum = 0;
        Integer endNum = 0;
        Integer isCustom = 0;
        if(selectId == 999){
            isCustom = 1;
        }else{
            startNum = (selectId - 1) * 50 + 1;
            endNum = selectId * 50;
        }

        if(isCustom == 1){
            if(customId.equals("")) return null;
            String[] userIds = customId.split(",");
            for (String userId:userIds){
                dataList.add(this.queryDailyNewUserSingle(Long.parseLong(userId.trim()), dateStart, dateEnd));
            }
        }else{
            for(long i = startNum;i <= endNum;i++){
                dataList.add(this.queryDailyNewUserSingle(i, dateStart, dateEnd));
            }
        }
        return dataList;
    }

    private HashMap queryDailyNewUserSingle(Long userId, String dateStart, String dateEnd) throws Exception{
        HashMap<String, Object> dailyData = new HashMap<>();
        TpzUserEntity tpzUserEntity = tpzUserDao.getById(userId);
        if(tpzUserEntity == null){
            return dailyData;
        }else{
            dailyData.put("userId", tpzUserEntity.getUserId());
            dailyData.put("userName", tpzUserEntity.getUserNickName() == null ? tpzUserEntity.getUserName() : tpzUserEntity.getUserNickName());
            dailyData.put("caimaoId", tpzUserEntity.getCaimaoId());
        }
        //当日注册数
        dailyData.put("regNew", zeusStatisticsDao.queryOperationRegNew(userId, dateStart, dateEnd));
        //当日总注册数
        dailyData.put("regTmp", zeusStatisticsDao.queryOperationRegNew(userId, "2015-01-01", dateEnd));
        //总注册数
        dailyData.put("regAll", zeusStatisticsDao.queryOperationRegAll(userId));
        //当日实名数
        dailyData.put("realNameNew", zeusStatisticsDao.queryOperationRealNameNew(userId, dateStart, dateEnd));
        //当日总实名数
        dailyData.put("realNameTmp", zeusStatisticsDao.queryOperationRealNameNew(userId, "2015-01-01", dateEnd));
        //总实名数
        dailyData.put("realNameAll", zeusStatisticsDao.queryOperationRealNameAll(userId));
        //当日邮币卡注册数
        dailyData.put("ybkRegNew", zeusStatisticsDao.queryYBKRegiestNew(userId, dateStart, dateEnd));
        //当日总邮币卡注册数
        dailyData.put("ybkRegTmp", zeusStatisticsDao.queryYBKRegiestAll(userId));
        //当日配资人数
        dailyData.put("loanUserNew", zeusStatisticsDao.queryOperationLoanUserNew(userId, dateStart, dateEnd));
        //当日总配资人数
        dailyData.put("loanUserTmp", zeusStatisticsDao.queryOperationLoanUserNew(userId, "2015-01-01", dateEnd));
        //总配资人数
        dailyData.put("loanUserAll", zeusStatisticsDao.queryOperationLoanUserAll(userId));
        //当日配资金额
        dailyData.put("loanAmountNew", zeusStatisticsDao.queryOperationLoanAmountNew(userId, dateStart, dateEnd));
        //当日总配资金额
        dailyData.put("loanAmountTmp", zeusStatisticsDao.queryOperationLoanAmountNew(userId, "2015-01-01", dateEnd));
        //总配资金额
        dailyData.put("loanAmountAll", zeusStatisticsDao.queryOperationLoanAmountAll(userId));
        //当日配资利息
        dailyData.put("loanInterestNew", zeusStatisticsDao.queryOperationLoanInterestNew(userId, dateStart, dateEnd));
        //当日总配资利息
        dailyData.put("loanInterestTmp", zeusStatisticsDao.queryOperationLoanInterestNew(userId, "2015-01-01", dateEnd));
        //总配资利息
        dailyData.put("loanInterestAll", zeusStatisticsDao.queryOperationLoanInterestAll(userId));
        return dailyData;
    }

    @Override
    public List<TpzUserEntity> queryDateRealUserList(String refUserId, String dateStart, String dateEnd) throws Exception {
        if(dateStart == null || dateStart.equals("") || dateEnd == null || dateEnd.equals("") || refUserId == null || refUserId.equals("")) return null;
        dateStart = dateStart + " 00:00:00";
        dateEnd = dateEnd + " 23:59:59";
        return zeusStatisticsDao.queryDateRealUserList(refUserId, dateStart, dateEnd);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void removeContract(Long contractNo) throws Exception {
        //检测合约有效性
        if(contractNo == null || contractNo == 0) throw new Exception("合约不存在");
        TpzLoanContractEntity entity = zeusStatisticsDao.queryContractByNo(contractNo);
        if(entity == null || entity.getContractNo() == null || entity.getContractNo() == 0) throw new Exception("合约不存在");
        //插入历史合约
        zeusStatisticsDao.insertContractHistory(entity);
        //插入还款记录
        TpzRepayOrderEntity tpzRepayOrderEntity = new TpzRepayOrderEntity();
        tpzRepayOrderEntity.setContractNo(entity.getContractNo());
        tpzRepayOrderEntity.setPzAccountId(entity.getPzAccountId());
        tpzRepayOrderEntity.setUserId(entity.getUserId());
        tpzRepayOrderEntity.setOrderAmount(entity.getLoanAmount());
        tpzRepayOrderEntity.setAccruedInterest(0L);
        tpzRepayOrderEntity.setOrderAbstract("还款");
        tpzRepayOrderEntity.setRemark("");
        tpzRepayOrderEntity.setRepayDatetime(new Date());
        zeusStatisticsDao.insertContractRepay(tpzRepayOrderEntity);
        //删除合约
        zeusStatisticsDao.deleteContractByNo(entity.getContractNo());
    }
}
