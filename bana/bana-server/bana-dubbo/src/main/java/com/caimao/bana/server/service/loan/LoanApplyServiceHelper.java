/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.service.loan;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.caimao.bana.server.utils.BigMath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.caimao.bana.api.entity.TpzLoanApplyEntity;
import com.caimao.bana.api.entity.TpzProdEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.p2p.P2PLoanRecordEntity;
import com.caimao.bana.api.enums.ELoanApplyAction;
import com.caimao.bana.api.enums.EP2PLoanStatus;
import com.caimao.bana.api.enums.EProdBillType;
import com.caimao.bana.api.enums.LoanEVerifyStatus;
import com.caimao.bana.server.dao.LoanApplyDao;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.p2p.P2PLoanRecordDao;
import com.caimao.bana.server.dao.prod.TpzProdDao;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.utils.BigMath;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import com.huobi.commons.utils.HsDateUtil;

/**
 * 提交融资申请，并回写contract_no至p2p_loan_record表
 * @author yanjg
 * 2015年6月8日
 */
@Component("loanApplyServiceHelper")
public class LoanApplyServiceHelper {
    @Autowired
    private TpzProdDao tpzProdDao;
    @Autowired
    private TpzAccountDao tpzAccountDao;
    @Autowired
    private TpzUserDao tpzUserDao;
    @Autowired
    private LoanApplyDao loanApplyDao;
    @Autowired
    private P2PLoanRecordDao p2pLoanRecordDao;
    @Resource
    private MemoryDbidGenerator dbidGenerator;
    /**
     * 根据P2P借贷记录，生成融资借贷申请
     * @param p2pLoanRecord
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void commitLoanApply(P2PLoanRecordEntity p2pLoanRecord) {
//        1.提交申请记录
        Long contractNo=saveLoanApply(p2pLoanRecord);
//        2.回写contract_no
        p2pLoanRecord.setContractId(contractNo);
        Map<String,Object> paraMap=new HashMap<String,Object>();
        paraMap.put("contractId",contractNo);
        paraMap.put("targetStatus",EP2PLoanStatus.COMMIT_LOAN_APPLY.getCode());
        paraMap.put("lastUpdate",new Date());
        paraMap.put("targetId", p2pLoanRecord.getTargetId());
        p2pLoanRecordDao.update(paraMap);
    }
    
    private  Long saveLoanApply(P2PLoanRecordEntity p2pLoanRecord){
        TpzProdEntity prod=tpzProdDao.getProd(p2pLoanRecord.getTargetProdId());
        TpzLoanApplyEntity loanApply = new TpzLoanApplyEntity();
        loanApply.setProdTerm(p2pLoanRecord.getLiftTime());
        loanApply.setInterestRate(new BigDecimal(0));
        loanApply.setFee(new Long(0L));
        //保证金
        loanApply.setCashAmount(p2pLoanRecord.getPayMargin());
        //冻结金额,需要计算。(保证金 + 借贷利息 + 管理费)
        loanApply.setFreezeAmount(BigMath.add(BigMath.add(p2pLoanRecord.getPayMargin(), p2pLoanRecord.getPayTargetInterest()), p2pLoanRecord.getPayManageFee()).longValue());
        //融资金额，标的总金额
        loanApply.setOrderAmount(p2pLoanRecord.getTargetAmount());
        int loanRatio = p2pLoanRecord.getTargetProdLever();
        loanApply.setOrderNo(dbidGenerator.getNextId());
        loanApply.setInterestRate(BigMath.divide(p2pLoanRecord.getYearRate(), 12));
        loanApply.setFee(0L);
        loanApply.setProdBillType(prod.getProdBillType());
        loanApply.setInterestAccrualMode(prod.getInterestAccrualMode());
        loanApply.setInterestSettleMode(prod.getInterestSettleMode());
        loanApply.setInterestSettleDays(prod.getInterestSettleDays());
        loanApply.setLoanRatio(loanRatio);
        loanApply.setUserId(p2pLoanRecord.getTargetUserId());
        Long pzAccountId = tpzAccountDao.getByUserId(loanApply.getUserId()).getPzAccountId();
        loanApply.setPzAccountId(pzAccountId);
        TpzUserEntity user=tpzUserDao.getById(loanApply.getUserId());
        loanApply.setUserRealName(user.getUserRealName());
        Date today = new Date();
        loanApply.setApplyDatetime(today);
        loanApply.setLoanApplyAction(ELoanApplyAction.LOAN.getCode());
        loanApply.setProdId(prod.getProdId());
        loanApply.setContractBeginDate(HsDateUtil.convertDateToString("yyyyMMdd", today));
        loanApply.setContractEndDate(HsDateUtil.convertDateToString("yyyyMMdd", HsDateUtil.addDays(today, loanApply.getProdTerm() - 1)));
        if(!EProdBillType.FREE.getCode().equals(prod.getProdBillType()))
            loanApply.setBeginInterestDate(loanApply.getContractBeginDate());
        loanApply.setVerifyStatus(LoanEVerifyStatus.APPLYING.getCode());
        loanApply.setOrderAbstract("P2P借贷申请");
        loanApply.setApplyType((short)1);
        loanApplyDao.save(loanApply);
        return loanApply.getOrderNo();
    }


}
