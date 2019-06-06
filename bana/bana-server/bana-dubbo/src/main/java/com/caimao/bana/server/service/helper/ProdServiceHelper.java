/*
 * Timeloit.com Inc.
 * Copyright (c) 2012 时代凌宇物联网数据平台. All Rights Reserved
 */
package com.caimao.bana.server.service.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caimao.bana.api.entity.TpzProdDetailEntity;
import com.caimao.bana.api.entity.TpzProdEntity;
import com.caimao.bana.api.enums.EInterestAccrualMode;
import com.caimao.bana.api.enums.EProdBillAccord;
import com.caimao.bana.api.enums.EProdBillType;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.dao.prod.TpzProdDao;
import com.caimao.bana.server.dao.prod.TpzProdDetailDao;
import com.hsnet.pz.core.util.ArithUtil;

/**
 * @author yanjg 2015年3月24日
 */
@Service("prodServiceHelper")
public class ProdServiceHelper {
    @Autowired
    private TpzProdDao tpzProdDao;
    @Autowired
    private TpzProdDetailDao tpzProdDetailDao;

    /**
     * @param prodId
     * @return
     */
    public TpzProdEntity getProd(Long prodId) {
        return tpzProdDao.getProd(prodId);
    }

    /**
     * @param prod
     * @param valueOf
     * @param valueOf2
     * @param valueOf3
     * @return
     * @throws CustomerException 
     */
    public TpzProdDetailEntity getProdDetail(TpzProdEntity prod, Integer loanRatio, Long loanAmount, Integer loanTerm) throws CustomerException {
        TpzProdDetailEntity pd = null;
        if (!EProdBillType.FREE.getCode().equals(prod.getProdBillType())) {
          String[] accords = prod.getProdBillAccords().split(",");
          Double loanRatioCond = null;
          Long loanA3mountCond = null;
          Integer prodTermCond = null;
          for (String accord : accords) {
            if (EProdBillAccord.LOAN_RATIO.getCode().equals(accord))
              loanRatioCond = new Double(loanRatio.intValue());
            else if (EProdBillAccord.LOAN_AMOUNT.getCode().equals(accord))
              loanA3mountCond = loanAmount;
            else if (EProdBillAccord.LOAN_TERM.getCode().equals(accord)) {
              prodTermCond = loanTerm;
            }
          }
          Map param = new HashMap();
          param.put("prodId", prod.getProdId());
          param.put("loanRatio", loanRatioCond);
          param.put("loanAmount", loanA3mountCond);
          param.put("loanTerm", prodTermCond);
          pd = this.tpzProdDetailDao.getProdDetail(param);
          if (pd == null) {
              throw new CustomerException("没有符合您的借款条件，请联系客服", 83021701, "BizServiceException");
          }
        }
        return pd;
    }

    /**
     * @param valueOf
     * @param prod
     * @param pd
     * @return
     */
    public long getApplyFee(Long loanAmount, TpzProdEntity prod, TpzProdDetailEntity pd) {
        if (EProdBillType.FEE.getCode().equals(prod.getProdBillType())) {
            if ((prod.getInterestAccrualMode().equals(EInterestAccrualMode.DAY.getCode())) || (prod.getInterestAccrualMode().equals(EInterestAccrualMode.TRADE_DAY.getCode())))
            {
              return prod.getInterestSettleDays().longValue() * pd.getFee().longValue();
            }
            if (prod.getInterestAccrualMode().equals(EInterestAccrualMode.MONTH.getCode()))
            {
              return prod.getInterestSettleDays().longValue() / 30L * pd.getFee().longValue();
            }
          }
          else if (EProdBillType.INTEREST.getCode().equals(prod.getProdBillType()))
          {
            double perAmount = ArithUtil.mul(loanAmount.doubleValue(), pd.getInterestRate().doubleValue());

            if ((prod.getInterestAccrualMode().equals(EInterestAccrualMode.DAY.getCode())) || (prod.getInterestAccrualMode().equals(EInterestAccrualMode.TRADE_DAY.getCode())))
            {
              return Math.round(perAmount * prod.getInterestSettleDays().intValue());
            }
            if (prod.getInterestAccrualMode().equals(EInterestAccrualMode.MONTH.getCode()))
            {
              return Math.round(perAmount * (prod.getInterestSettleDays().intValue() / 30));
            }

          }

          return 0L;
    }
    
}
