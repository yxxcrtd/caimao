/*
*TpzAccruedInterestBillDao.java
*Created on 2015/5/23 11:58
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server.dao.accruedInterest;

import com.caimao.bana.api.entity.TpzAccruedInterestBillEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0.1
 */
@Repository
public class TpzAccruedInterestBillDao extends SqlSessionDaoSupport {

    public List<TpzAccruedInterestBillEntity> queryAccruedInterestBillList(TpzAccruedInterestBillEntity tpzAccruedInterestBillEntity) {
        return getSqlSession().selectList("TpzAccruedInterestBill.queryAccruedInterestBillList", tpzAccruedInterestBillEntity);
    }

    public List<TpzAccruedInterestBillEntity> queryAccruedInterestBillListByWorkDate(String workDate) {
        return getSqlSession().selectList("TpzAccruedInterestBill.queryAccruedInterestBillListByWorkDate", workDate);
    }

    public Integer queryBillReceivedCnt(Long contractNo) throws Exception{
        return getSqlSession().selectOne("TpzAccruedInterestBill.queryBillReceivedCnt", contractNo);
    }

    public Integer queryBillAmountByContractNo(Long contractNo) throws Exception{
        return getSqlSession().selectOne("TpzAccruedInterestBill.queryBillAmountByContractNo", contractNo);
    }
}
