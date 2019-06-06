/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao;

import java.util.List;
import java.util.Map;

import com.caimao.bana.api.entity.TpzLoanApplyEntity;
import com.caimao.bana.api.entity.TpzLoanContractEntity;
import com.caimao.bana.api.entity.req.FTpzQueryLoanContractAllPageReq;
import com.caimao.bana.api.entity.req.FTpzQueryLoanContractPageReq;
import com.caimao.bana.api.entity.res.FTpzQueryLoanContractAllPageRes;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.req.loan.FLoanContractReq;
import com.caimao.bana.api.entity.res.F830216Res;
import com.caimao.bana.api.entity.res.loan.FLoanContractRes;

/**
 * 用户当前合约表
 * @author yanjg
 * 
 */
@Repository("loanContractDao")
public class LoanContractDao extends SqlSessionDaoSupport{


    public TpzLoanContractEntity getById(long id){
        return getSqlSession().selectOne("LoanContract.getById",id);
    }
    /**
     * @param param
     * @return
     */
    public int getByUserProdCount(Map param) {
        return getSqlSession().selectOne("LoanContract.getByUserProdCount",param);
    }
    
    public long getHasLoanAmount(Map<String, Object> paramMap){
        return getSqlSession().selectOne("LoanContract.getHasLoanAmount",paramMap);
    }

    public List<FLoanContractRes> queryLoanContractWithPage(FLoanContractReq req){
        return getSqlSession().selectList("LoanContract.queryLoanContrac2tWithPage", req);
    }

    public Long getTotalLoanAmount(Long paramLong){
        return getSqlSession().selectOne("LoanContract.getTotalLoanAmount", paramLong);
    }
    
    public F830216Res  getTotalLoanAndBill(Long userId){
        return getSqlSession().selectOne("LoanContract.getTotalLoanAndBill",userId);
    }

    //根据apply条件获取合约
    public TpzLoanContractEntity getContractByApplyRecord(TpzLoanApplyEntity tpzLoanApplyEntity){
        return getSqlSession().selectOne("LoanContract.getContractByApplyRecord", tpzLoanApplyEntity);
    }

    //清除合约下一次结息日
    public void cleanNextSettleInterestDate(Long prodId) throws Exception{
        getSqlSession().update("LoanContract.cleanNextSettleInterestDate", prodId);
    }

    //查询合约列表
    public List<TpzLoanContractEntity> queryLoanContractWithPage(FTpzQueryLoanContractPageReq req) throws Exception{
        return getSqlSession().selectList("LoanContract.queryLoanContractWithPage", req);
    }

    //查询合约列表所有
    public List<FTpzQueryLoanContractAllPageRes> queryLoanContractAllWithPage(FTpzQueryLoanContractAllPageReq req) throws Exception{
        return getSqlSession().selectList("LoanContract.queryLoanContractAllWithPage", req);
    }
}
