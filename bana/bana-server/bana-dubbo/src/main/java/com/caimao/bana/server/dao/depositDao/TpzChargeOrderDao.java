package com.caimao.bana.server.dao.depositDao;

import com.caimao.bana.api.entity.TpzChargeOrderEntity;
import com.caimao.bana.api.entity.req.FAccountQueryChargeOrderReq;
import com.caimao.bana.api.entity.res.FAccountQueryChargeOrderRes;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 充值订单记录管理
 * Created by WangXu on 2015/4/23.
 */
@Repository
public class TpzChargeOrderDao extends SqlSessionDaoSupport {

    @Resource
    private MemoryDbidGenerator dbidGenerator;

    /**
     * 保存充值订单信息记录
     *
     * @param order
     */
    public void save(TpzChargeOrderEntity order) {
        order.setOrderNo(this.dbidGenerator.getNextId());
        getSqlSession().insert("TpzChargeOrder.save", order);
    }

    /**
     * 根据订单号码获取订单详情 (FOR UPDATE)
     *
     * @param orderNo
     * @return
     */
    public TpzChargeOrderEntity getChargeOrderByOrderNo(Long orderNo) {
        return getSqlSession().selectOne("TpzChargeOrder.getChargeOrderByOrderNo", orderNo);
    }

    public List<TpzChargeOrderEntity> getChargeOrdersByUserId(Long userId) {
        return getSqlSession().selectList("TpzChargeOrder.getChargeOrdersByUserId", userId);
    }

    /**
     * 更新充值订单信息，根据订单号码
     *
     * @param order
     */
    public void update(TpzChargeOrderEntity order) {
        getSqlSession().update("TpzChargeOrder.update", order);
    }

    /**
     * 更新支付宝
     * @param order
     */
    public void updateAlipay(TpzChargeOrderEntity order) {
        getSqlSession().update("TpzChargeOrder.updateAlipay", order);
    }


    /**
     * 删除充值记录信息
     *
     * @param orderNo
     */
    public void delete(Long orderNo) {
        getSqlSession().delete("TpzChargeOrder.delete", orderNo);
    }

    /**
     * 获取充值记录分页
     * @param chargeOrderEntity
     * @return
     */
    public List<TpzChargeOrderEntity> queryChargeOrderWithPage(TpzChargeOrderEntity chargeOrderEntity) {
        return getSqlSession().selectList("TpzChargeOrder.queryChargeOrderWithPage", chargeOrderEntity);
    }

    /**
     * 获取支付宝未成功的记录
     * @param dateStart
     * @return
     * @throws Exception
     */
    public List<TpzChargeOrderEntity> queryAlipayNoSuccess(Date dateStart) throws Exception{
        return getSqlSession().selectList("TpzChargeOrder.queryAlipayNoSuccess", dateStart);
    }
}
