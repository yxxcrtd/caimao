/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.service.p2p;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.caimao.bana.api.entity.p2p.P2PInterestRecordEntity;
import com.caimao.bana.api.entity.p2p.P2PInvestRecordEntity;
import com.caimao.bana.api.entity.p2p.P2PLoanRecordEntity;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.enums.EP2PInvestStatus;
import com.caimao.bana.api.enums.EP2PLoanStatus;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.p2p.P2PInterestRecordDao;
import com.caimao.bana.server.dao.p2p.P2PInvestRecordDao;
import com.caimao.bana.server.dao.p2p.P2PLoanRecordDao;
import com.caimao.bana.server.utils.AccountManager;
import com.huobi.commons.utils.DateUtil;

/**
 * 
 * @author yanjg
 * 2015年6月8日
 */
@Component("p2pInvestRecordService")
public class P2PInvestRecordServiceHelper {
    @Autowired
    private P2PInvestRecordDao p2pInvestRecordDao;
    @Autowired
    private TpzAccountDao accountDao;
    @Autowired
    private P2PInterestRecordDao p2pInterestRecordDao;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private P2PLoanRecordDao p2pLoanRecordDao;

    /**
     * 获取满标的投资列表,方便发息
     * @param start
     * @param listSize
     * @param investStatus
     * @return
     */
    public List<P2PInvestRecordEntity> getInvestRecordList(int start,int listSize,int investStatus) {
        Map<String,Object> paraMap=new HashMap<String,Object>();
        paraMap.put("start", start);
        paraMap.put("length", listSize);
        paraMap.put("investStatus",investStatus);
        //在上一天有效       
        //Date startDate = DateUtil.addDays(new Date(), -1);
        //startDate.setHours(0);startDate.setMinutes(0);startDate.setSeconds(0);
        // 截止日期小于今天日期的  TODO 如果这样查，接口超过一个月的会在中途不进行发息的，先获取所有状态是3的，进行执行，之后再进行优化吧
        //String startDate = DateUtil.toString(new Date(), DateUtil.DEFAULT_SHORT_FORMAT);
        //paraMap.put("expirationDatetimeStart", startDate);
        return p2pInvestRecordDao.getInvestRecordList(paraMap);
    }

    /**
     * 真正的发息操作，
     * 1.修改本条记录的利息
     * 2.给添加资产  
     * 3.在p2p_interest_record中加一条记录
     * @param p2pInvestRecord
     * @throws Exception 
     */
    @Transactional
    public void commitDistributeInterest(P2PInvestRecordEntity p2pInvestRecord) throws Exception {
        //只有开始计息时间+支付利息周期*interestTimes=今天，才进行结息操作
        Date interestTime = DateUtil.addDays(p2pInvestRecord.getInterestDatetime(), p2pInvestRecord.getInterestPeriod() * (p2pInvestRecord.getInterestTimes()+1));
        if(DateUtil.getDayOfYear(DateUtil.addDays(interestTime, 1)) <= DateUtil.getDayOfYear(new Date())){
            // 检查是否已经进行过发息操作
            Integer interestExist = this.p2pInterestRecordDao.queryInvestInterestExist(p2pInvestRecord.getInvestId(), p2pInvestRecord.getInterestTimes() + 1);
            if (interestExist <= 0) {
                p2pInvestRecord.setPayInterest(p2pInvestRecord.getPayInterest()+p2pInvestRecord.getYearValue()/12);
                p2pInvestRecord.setLastUpdate(new Date());
                p2pInvestRecord.setInterestTimes(p2pInvestRecord.getInterestTimes()+1);
                p2pInvestRecordDao.update(p2pInvestRecord);
                //给用户添加资产
                accountManager.doUpdateAvaiAmount("DI_"+System.currentTimeMillis(),
                        accountDao.getAccountByUserId(p2pInvestRecord.getInvestUserId()).getPzAccountId(),
                        p2pInvestRecord.getYearValue()/12, EAccountBizType.P2PSETTLE.getCode(), ESeqFlag.COME.getCode());
                //在添加结息记录
                P2PInterestRecordEntity p2pInterestRecord =new P2PInterestRecordEntity();
                p2pInterestRecord.setInvestId(p2pInvestRecord.getInvestId());
                p2pInterestRecord.setInvestUserId(p2pInvestRecord.getInvestUserId());
                p2pInterestRecord.setTargetId(p2pInvestRecord.getTargetId());
                p2pInterestRecord.setTargetUserId(p2pInvestRecord.getTargetUserId());
                p2pInterestRecord.setInterestDate(interestTime);
                p2pInterestRecord.setInterestValue(p2pInvestRecord.getYearValue()/12);
                p2pInterestRecord.setCreateTime(new Date());
                p2pInterestRecord.setInterestTimes(p2pInvestRecord.getInterestTimes());
                p2pInterestRecordDao.save(p2pInterestRecord);
            }
        }
        //如果今天为到期时间，修改为已到期
        if(DateUtil.getDayOfYear(DateUtil.addDays(p2pInvestRecord.getExpirationDatetime(), 1)) <= DateUtil.getDayOfYear(new Date())){
            //给投资人返本金
            accountManager.doUpdateAvaiAmount("DI_"+System.currentTimeMillis(),
                    accountDao.getAccountByUserId(p2pInvestRecord.getInvestUserId()).getPzAccountId(),
                    p2pInvestRecord.getInvestValue(), EAccountBizType.P2P_PRINCIPAL.getCode(), ESeqFlag.COME.getCode());
            p2pInvestRecordDao.updateStatusByInvestId(p2pInvestRecord.getInvestId(), EP2PInvestStatus.END);
            //设置p2p_loan_apply的状态为已还款
            p2pLoanRecordDao.updateStatus(p2pInvestRecord.getTargetId(), EP2PLoanStatus.END);
        }
    }

    /**
     * 更新p2p_invest_record表的计息日期和到期时间（开始计息日期+lift_time）
     * @param p2pLoanRecord
     */
    public void setInterestTime(P2PLoanRecordEntity p2pLoanRecord) {
        Map<String,Object> paraMap=new HashMap<String,Object>();
        //p2pLoanRecord.getLastUpdate(),这个时间被设置为审核通过时间
        if(p2pLoanRecord.getLastUpdate()==null){
            p2pLoanRecord.setLastUpdate(new Date());
        }
        paraMap.put("expirationDatetime",DateUtil.addDays(p2pLoanRecord.getLastUpdate(), p2pLoanRecord.getLiftTime()));
        paraMap.put("targetId",p2pLoanRecord.getTargetId());
        paraMap.put("interestDatetime",p2pLoanRecord.getLastUpdate());

        p2pInvestRecordDao.setInterestTime(paraMap);
    }

}
