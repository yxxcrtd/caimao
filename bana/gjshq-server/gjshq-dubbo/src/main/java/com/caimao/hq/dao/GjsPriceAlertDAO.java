package com.caimao.hq.dao;


import com.caimao.hq.api.entity.GjsPriceAlertEntity;
import com.caimao.hq.api.entity.req.FQueryGjsPriceAlertReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* GjsPriceAlertEntity DAO实现
*
* Created by wangxu@huobi.com on 2015-11-23 11:10:10 星期一
*/
@Repository
public class GjsPriceAlertDAO extends SqlSessionDaoSupport {

    /**
     * 查询符合设置价格的提醒设置
     * @param req
     * @return
     */
    public List<GjsPriceAlertEntity> queryPriceAlertList(FQueryGjsPriceAlertReq req) {
        return this.getSqlSession().selectList("GjsPriceAlert.queryPriceAlertList", req);
    }

    public GjsPriceAlertEntity getPriceAlertInfo(GjsPriceAlertEntity entity) {
        return this.getSqlSession().selectOne("GjsPriceAlert.getPriceAlertInfo", entity);
    }

    /**
     * 根据参数重置用户的提醒价格
     * @param condition
     * @param nowPrice
     */
    public void resetTriggerPrice(String condition, String nowPrice) {
        Map<String, String> params = new HashMap<>();
        params.put("condition", condition);
        params.put("nowPrice", nowPrice);
        this.getSqlSession().update("GjsPriceAlert.resetTriggerPrice", params);
    }

   /**
    * 查询指定GjsPriceAlert内容   FOR UPDATE
    *
    * @param id
    * @return
    */
    public GjsPriceAlertEntity selectById(Long id) {
        return this.getSqlSession().selectOne("GjsPriceAlert.selectById", id);
    }

    /**
     * 根据用户id，查询列表
     * @param userId
     * @return
     */
    public List<GjsPriceAlertEntity> selectByUserId(Long userId) {
        return this.getSqlSession().selectList("GjsPriceAlert.selectByUserId", userId);
    }

   /**
    * 添加GjsPriceAlert的接口
    *
    * @param entity
    */
    public Integer insert(GjsPriceAlertEntity entity) {
        return this.getSqlSession().insert("GjsPriceAlert.insert", entity);
    }

   /**
    * 删除GjsPriceAlert的接口
    *
    * @param id
    */
    public Integer deleteById(Long id) {
        return this.getSqlSession().delete("GjsPriceAlert.deleteById", id);
    }

   /**
    * 更新GjsPriceAlert的接口
    *
    * @param entity
    */
    public Integer update(GjsPriceAlertEntity entity) {
        return this.getSqlSession().update("GjsPriceAlert.update", entity);
    }

}