/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.service.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caimao.bana.api.entity.TpzProdEntity;
import com.caimao.bana.api.entity.res.F830101Res;
import com.caimao.bana.api.enums.ELoanApplyAction;
import com.caimao.bana.api.enums.EProdStatus;
import com.caimao.bana.api.enums.LoanEVerifyStatus;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.dao.HisLoanContractDao;
import com.caimao.bana.server.dao.LoanApplyDao;
import com.caimao.bana.server.dao.LoanContractDao;
import com.caimao.bana.server.service.account.AccountServiceHelper;

/**
 * @author yanjg 2015年5月13日
 */
@Service("loanRuleHelper")
public class LoanRuleHelper {
    @Autowired
    private LoanApplyDao loanApplyDAO;
    @Autowired
    private LoanContractDao loanContractDAO;
    @Autowired
    private HisLoanContractDao hisLoanContractDAO;
    @Autowired
    private AccountServiceHelper accountService;

    /**
     * @param prod
     * @throws CustomerException
     */
    public void checkProdStatus(TpzProdEntity prod) throws CustomerException {
        if (prod == null) {
            throw new CustomerException("该借款产品不存在", 83020000, "BizServiceException");
        }

        if (!EProdStatus.OPENING.getCode().equals(prod.getProdStatus()))
            throw new CustomerException("该借款产品未开放或已终止", 83020000, "BizServiceException");
        else
            return;
    }

    /**
     * @param userId
     * @param prodId
     * @throws CustomerException
     */
    public void checkExistLoanApply(Long userId, Long prodId) throws CustomerException {
        Map param = new HashMap();
        param.put("userId", userId);
        param.put("prodId", prodId);
        param.put("verifyStatus", LoanEVerifyStatus.APPLYING.getCode());
        param.put("loanApplyAction", ELoanApplyAction.LOAN.getCode());
        int count = loanApplyDAO.getByUserProdCount(param);
        if (count > 0)
            throw new CustomerException("已有借款申请，请耐心等待工作人员审核", 83020001, "BizServiceException");
        else
            return;
    }

    /**
     * @param userId
     * @param prod
     * @throws CustomerException 
     */
    public void checkLoanMaxCount(Long userId, TpzProdEntity prod) throws CustomerException {
        if (prod.getProdLoanLimit().intValue() > 0 || prod.getProdLoanCurLimit().intValue() > 0) {
            Map param = new HashMap();
            param.put("userId", userId);
            param.put("prodId", prod.getProdId());
            int count = loanContractDAO.getByUserProdCount(param);
            int prodLoanCurLimit = prod.getProdLoanCurLimit().intValue();
            if (prodLoanCurLimit != 0 && count >= prodLoanCurLimit)
                throw new CustomerException("您当前已经超过该借款产品可用次数，无法再申请", 83020002, "BizServiceException");
            int prodLoanLimit = prod.getProdLoanLimit().intValue();
            if (prodLoanLimit != 0 && count < prodLoanLimit)
                count += hisLoanContractDAO.getByUserProdCount(param);
            if (count >= prodLoanLimit)
                throw new CustomerException("您当前已经超过该借款产品可用次数，无法再申请", 83020002, "BizServiceException");
        }
    }

    /**
     * @param userId
     * @param prod
     * @param cashAmount
     * @param loanAmount
     * @throws CustomerException
     */
    public void checkCashAmount(Long userId, TpzProdEntity prod, long cashAmount, long loanAmount)
            throws CustomerException {
        F830101Res f830101Res = accountService.getPZAccount(userId);
        long amount = f830101Res.getAvalaibleAmount().longValue() - f830101Res.getFreezeAmount().longValue();
        if (amount * (long) prod.getProdLoanRatioMax().intValue() < prod.getProdAmountMin().longValue())
            throw new CustomerException("您的账户可用余额不足最低保证金", 83020003, "CustomerException");
        else
            return;
    }

    /**
     * @param prodTerms
     * @param prodTerm
     * @throws CustomerException
     */
    public void checkLoanTerm(String prodTerms, int prodTerm) throws CustomerException {
        if (StringUtils.isNotEmpty(prodTerms)) {
            String[] terms = prodTerms.split(",");
            for (String term : terms) {
                if (Integer.parseInt(term) == prodTerm) {
                    return;
                }
            }
            throw new CustomerException("借款期限不符", 83020004, "BizServiceException");
        }

    }

    /**
     * 
     * @param userId
     * @param prod
     */
    public void checkCashAmount(Long userId, TpzProdEntity prod) throws CustomerException {
        F830101Res f830101Res = accountService.getPZAccount(userId);
        long amount = f830101Res.getAvalaibleAmount().longValue() - f830101Res.getFreezeAmount().longValue();

        if (amount * prod.getProdLoanRatioMax().intValue() >= prod.getProdAmountMin().longValue())
            return;
        throw new CustomerException("83020003", "您的账户可用余额不足最低保证金", "BizServiceException");
    }

}
