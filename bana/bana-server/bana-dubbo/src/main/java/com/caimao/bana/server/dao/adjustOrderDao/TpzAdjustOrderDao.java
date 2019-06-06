package com.caimao.bana.server.dao.adjustOrderDao;

import com.caimao.bana.api.entity.TpzAdjustOrderEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 红冲、蓝补等那些其他充值方式的订单
 * Created by WangXu on 2015/4/28.
 */
@Repository
public class TpzAdjustOrderDao extends SqlSessionDaoSupport {

    /**
     * 保存订单信息
     * @param tpzAdjustOrderEntity
     */
    public void save(TpzAdjustOrderEntity tpzAdjustOrderEntity) {
        getSqlSession().insert("TpzAdjustOrder.save", tpzAdjustOrderEntity);
    }

    /**
     * 更新订单的信息
     * @param tpzAdjustOrderEntity
     */
    public void update(TpzAdjustOrderEntity tpzAdjustOrderEntity) {
        getSqlSession().update("TpzAdjustOrder.update", tpzAdjustOrderEntity);
    }
}
