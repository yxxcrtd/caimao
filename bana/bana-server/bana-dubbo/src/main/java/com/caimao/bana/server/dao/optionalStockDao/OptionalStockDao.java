/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.dao.optionalStockDao;

import com.caimao.bana.api.entity.OptionalStockEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 自选股
 */
@Repository
public class OptionalStockDao extends SqlSessionDaoSupport{
    public Integer insertStock(OptionalStockEntity optionalStockEntity) throws Exception {
        return getSqlSession().insert("OptionalStock.insertStock", optionalStockEntity);
    }

    public Integer deleteStock(Long userId, Long id) throws Exception {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("id", id);
        return getSqlSession().delete("OptionalStock.deleteStock", paramMap);
    }

    public Integer sortStock(Long userId, Long id, Long sort) throws Exception {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("id", id);
        paramMap.put("sort", sort);
        return getSqlSession().update("OptionalStock.sortStock", paramMap);
    }

    public List<OptionalStockEntity> queryStockByUserId(Long userId) throws Exception {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        return getSqlSession().selectList("OptionalStock.queryStockByUserId", paramMap);
    }

    public Integer queryUserStockNum(Long userId) throws Exception{
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("OptionalStock.queryUserStockNum", paramMap);
    }

    public Integer deleteStockByCode(Long userId, String stockCode) throws Exception{
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("stockCode", stockCode);
        return getSqlSession().delete("OptionalStock.deleteStockByCode", paramMap);
    }
}
