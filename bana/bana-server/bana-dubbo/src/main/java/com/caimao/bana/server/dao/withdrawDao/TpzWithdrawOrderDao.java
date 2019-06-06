/*
*WithdrawDao.java
*Created on 2015/5/13 10:12
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server.dao.withdrawDao;

import com.caimao.bana.api.entity.TpzWithdrawOrderEntity;
import com.caimao.bana.api.entity.req.F831411Req;
import com.caimao.bana.api.entity.req.FAccountQueryWithdrawOrderReq;
import com.caimao.bana.api.entity.res.F831411Res;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0.1
 */
@Repository
public class TpzWithdrawOrderDao extends SqlSessionDaoSupport {

    /**
     * 获取提现订单记录  UPDATE
     *
     * @param orderNo 提现订单ID
     * @return 提现订单信息
     */
    public TpzWithdrawOrderEntity getWithdrawOrderByOrderNo(Long orderNo) {
        return getSqlSession().selectOne("TpzWithdrawOrder.getById", orderNo);
    }

    /**
     * 更新提现订单记录
     * @param withdrawOrderEntity 提现订单信息
     * @return  更新的ID
     */
    public int update(TpzWithdrawOrderEntity withdrawOrderEntity) {
        return getSqlSession().update("TpzWithdrawOrder.update", withdrawOrderEntity);
    }

    /**
     * 保存提现订单信息
     * @param withdrawOrderEntity   提现信息
     * @return  返回的自增id
     */
    public int save(TpzWithdrawOrderEntity withdrawOrderEntity) {
        return getSqlSession().insert("TpzWithdrawOrder.save", withdrawOrderEntity);
    }

    /**
     * 查询提现列表
     * @param accountQueryWithdrawOrderReq 查询条件
     * @return  提现列表
     */
    public List<TpzWithdrawOrderEntity> queryWithdrawOrdersWithPage(FAccountQueryWithdrawOrderReq accountQueryWithdrawOrderReq) {
        return getSqlSession().selectList("TpzWithdrawOrder.queryWithdrawOrdersWithPage", accountQueryWithdrawOrderReq);
    }

    /**
     * 查询提现列表
     *
     * @param f831411Req
     * @return
     */
    public List<F831411Res> queryWithdraws(F831411Req f831411Req) {
        return getSqlSession().selectList("TpzWithdrawOrder.queryWithdrawOrders", f831411Req);
    }


}
