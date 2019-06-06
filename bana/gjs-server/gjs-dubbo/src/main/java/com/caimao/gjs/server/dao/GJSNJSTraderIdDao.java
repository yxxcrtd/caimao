package com.caimao.gjs.server.dao;

import com.caimao.gjs.api.entity.GJSNJSTraderIdEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class GJSNJSTraderIdDao extends SqlSessionDaoSupport {
    public GJSNJSTraderIdEntity getCanUseForUpdate() {
        return getSqlSession().selectOne("GJSNJSTraderId.getCanUseForUpdate");
    }

    public GJSNJSTraderIdEntity queryByUserId(Long userId) {
        return getSqlSession().selectOne("GJSNJSTraderId.queryByUserId", userId);
    }

    public Integer queryCanUseNum() {
        return getSqlSession().selectOne("GJSNJSTraderId.queryCanUseNum");
    }

    public void addUserId(String firmId, String traderId, Long userId){
        HashMap<String, Object> params = new HashMap<>();
        params.put("firmId", firmId);
        params.put("traderId", traderId);
        params.put("userId", userId);
        getSqlSession().update("GJSNJSTraderId.addUserId", params);
    }

    public void delTraderId(Long userId, String traderId) throws Exception{
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("traderId", traderId);
        getSqlSession().delete("GJSNJSTraderId.delTraderId", params);
    }
}
