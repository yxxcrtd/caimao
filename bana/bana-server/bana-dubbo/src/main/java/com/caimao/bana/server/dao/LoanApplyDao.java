/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao;

import com.caimao.bana.api.entity.TpzLoanApplyEntity;
import com.caimao.bana.api.entity.req.loan.FLoanApplyReq;
import com.caimao.bana.api.entity.req.loan.FLoanP2PApplyReq;
import com.caimao.bana.api.entity.res.loan.FLoanApplyRes;
import com.caimao.bana.api.entity.res.loan.FLoanP2PApplyRes;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户借贷申请
 * @author yanjg
 * 
 */
@Repository("loanApplyDao")
public class LoanApplyDao extends SqlSessionDaoSupport{

    /**
     * @param param
     * @return
     */
    public int getByUserProdCount(Map param) {
        return getSqlSession().selectOne("LoanApply.getByUserProdCount",param);
    }

    /**
     * @param loanApply
     */
    public void save(TpzLoanApplyEntity loanApply) {
        getSqlSession().insert("LoanApply.save", loanApply);
    }

    public List<FLoanApplyRes> queryLoanContractApplyWithPage(FLoanApplyReq req) {
        return getSqlSession().selectList("LoanApply.queryFLoanApplyWithPage", req);
    }

    public TpzLoanApplyEntity getUserHasLoan(Long userId, String verifyDatetime){
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("verifyDatetimeEnd", verifyDatetime);
        return getSqlSession().selectOne("LoanApply.getUserHasLoan", params);
    }

    public List<TpzLoanApplyEntity> getP2PContractNoNullList(){
        return getSqlSession().selectList("LoanApply.getP2PContractNoNullList");
    }

    public Integer updateApplyContractNo(Long orderNo, Long p2pContractNo){
        HashMap<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("orderNo", orderNo);
        updateInfo.put("p2pContractNo", p2pContractNo);
        return getSqlSession().update("LoanApply.updateP2PContractNo", updateInfo);
    }


    /**
     * 查询指定用户的申请合约列表（配资P2P合并）
     * @param req
     * @return
     */
    public List<FLoanP2PApplyRes> queryLoanP2PLiseWithPage(FLoanP2PApplyReq req) {
        return getSqlSession().selectList("LoanApply.queryLoanP2PLiseWithPage", req);
    }
}
