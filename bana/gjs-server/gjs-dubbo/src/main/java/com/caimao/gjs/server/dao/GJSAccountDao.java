package com.caimao.gjs.server.dao;

import com.caimao.gjs.api.entity.GJSAccountEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GJSAccountDao extends SqlSessionDaoSupport {
    public GJSAccountEntity queryAccountByUserId(Long userId, String exchange) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("exchange", exchange);
        return getSqlSession().selectOne("GJSAccount.queryAccountByUserId", params);
    }

    public void delAccountByUserId(Long userId, String exchange) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("exchange", exchange);
        getSqlSession().delete("GJSAccount.delAccountByUserId", params);
    }

    public List<GJSAccountEntity> queryNotSignList(String exchange) throws Exception{
        return getSqlSession().selectList("GJSAccount.queryNotSignList", exchange);
    }

    public List<GJSAccountEntity> queryAccountAllList() throws Exception{
        return getSqlSession().selectList("GJSAccount.queryAccountAllList");
    }

    public List<GJSAccountEntity> queryAccountListByExchange(String exchange) throws Exception{
        return getSqlSession().selectList("GJSAccount.queryAccountListByExchange", exchange);
    }

    public List<GJSAccountEntity> queryAccountList(Long userId){
        return getSqlSession().selectList("GJSAccount.queryAccountList", userId);
    }

    public void saveAccount(GJSAccountEntity gjsAccountEntity){
        getSqlSession().insert("GJSAccount.saveAccount", gjsAccountEntity);
    }

    public void updateAccount(GJSAccountEntity gjsAccountEntity){
        getSqlSession().update("GJSAccount.updateAccount", gjsAccountEntity);
    }

    public int queryNJSOpenAccountCount(String startDate, String endDate) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return this.getSqlSession().selectOne("GJSAccount.queryNJSOpenAccountCount", params);
    }

    public int querySJSOpenAccountCount(String startDate, String endDate) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return this.getSqlSession().selectOne("GJSAccount.querySJSOpenAccountCount", params);
    }

}
