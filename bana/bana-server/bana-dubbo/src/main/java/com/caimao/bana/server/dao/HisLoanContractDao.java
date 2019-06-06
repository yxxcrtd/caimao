/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao;

import java.util.List;
import java.util.Map;

import com.caimao.bana.api.entity.HisLoanContractEntity;
import com.caimao.bana.api.entity.TpzLoanApplyEntity;
import com.caimao.bana.api.entity.TpzLoanContractEntity;
import com.caimao.bana.api.entity.req.FTpzQueryLoanContractPageReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.caimao.bana.api.entity.req.loan.FHisContractReq;
import com.caimao.bana.api.entity.res.loan.FHisContractRes;

/**
 * 用户邀请记录表
 * @author yanjg
 * 
 */
@Repository("hisLoanContractDao")
public class HisLoanContractDao extends SqlSessionDaoSupport{

    /**
     * @param param
     * @return
     */
    public int getByUserProdCount(Map param) {
        return getSqlSession().selectOne("HisLoanContract.getByUserProdCount",param);
    }

    public List<FHisContractRes> queryHisLoanContractPage(FHisContractReq req) {
        return getSqlSession().selectList("HisLoanContract.queryFHisContractResWithPage", req);
    }

    public HisLoanContractEntity getHisContractByApplyRecord(TpzLoanApplyEntity tpzLoanApplyEntity){
        return getSqlSession().selectOne("HisLoanContract.getHisContractByApplyRecord", tpzLoanApplyEntity);
    }

    //查询合约列表
    public List<TpzLoanContractEntity> queryLoanHisContractWithPage(FTpzQueryLoanContractPageReq req) throws Exception{
        return getSqlSession().selectList("HisLoanContract.queryLoanHisContractWithPage", req);
    }

    //获取所有历史合约记录
    public List<HisLoanContractEntity> queryAllHisLoanContract() throws Exception{
        return getSqlSession().selectList("HisLoanContract.queryAllHisLoanContract");
    }
}
