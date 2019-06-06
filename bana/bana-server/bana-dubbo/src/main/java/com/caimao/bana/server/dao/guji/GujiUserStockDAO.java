package com.caimao.bana.server.dao.guji;

import com.caimao.bana.api.entity.guji.GujiUserStockEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* GujiUserStockEntity DAO实现
*
* Created by wangxu@huobi.com on 2016-01-07 17:44:32 星期四
*/
@Repository
public class GujiUserStockDAO extends SqlSessionDaoSupport {

    /**
     * 根据Id查询对象
     *
     * @param id
     * @return
     */
    public GujiUserStockEntity findById(Long id) {
        return this.getSqlSession().selectOne("GujiUserStock.findById", id);
    }

   /**
    * 查询指定GujiUserStock内容
    *
    * @param wxId
    * @return
    */
    public List<GujiUserStockEntity> selectByWxId(Long wxId) {
        return this.getSqlSession().selectList("GujiUserStock.selectByWxId", wxId);
    }

    /**
     * 根据用户与股票，查询用户股票持仓信息
     * @param wxId
     * @param stockCode
     * @return
     */
    public GujiUserStockEntity selectByWxIdAndCode(Long wxId, String stockCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("wxId", wxId);
        params.put("stockCode", stockCode);
        return this.getSqlSession().selectOne("GujiUserStock.selectByWxIdAndCode", params);
    }

   /**
    * 添加GujiUserStock的接口
    *
    * @param entity
    */
    public Integer insert(GujiUserStockEntity entity) {
        return this.getSqlSession().insert("GujiUserStock.insert", entity);
    }

   /**
    * 更新GujiUserStock的接口
    *
    * @param entity
    */
    public Integer update(GujiUserStockEntity entity) {
        return this.getSqlSession().update("GujiUserStock.update", entity);
    }

    /**
     * 删除用户的股票持仓信息
     */
    public void delete(Long id) {
        this.getSqlSession().delete("GujiUserStock.delete", id);
    }

}