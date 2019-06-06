package com.caimao.gjs.server.dao;

import com.caimao.gjs.api.entity.GJSProductEntity;
import com.caimao.gjs.api.entity.history.*;
import com.caimao.gjs.api.entity.req.*;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GJSProductDao extends SqlSessionDaoSupport {
    public List<GJSProductEntity> queryExchangeGoods(String exchange) {
        Map<String, Object> params = new HashMap<>();
        params.put("exchange", exchange);
        return getSqlSession().selectList("GJSProduct.queryExchangeGoods", params);
    }

    public void updateExchangeGoods(GJSProductEntity entity) {
        getSqlSession().update("GJSProduct.updateExchangeGoods", entity);
    }
}
