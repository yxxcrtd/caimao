/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao;

import com.caimao.bana.api.entity.WeChatForecastCountEntity;
import com.caimao.bana.api.entity.WeChatForecastEntity;
import com.caimao.bana.api.entity.WeChatUserEntity;
import com.caimao.bana.server.utils.DateUtil;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("WeChatUserDao")
public class WeChatUserDao extends SqlSessionDaoSupport {
    public WeChatUserEntity getWeChatUserByOpenid(String openid) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("openid", openid);
        return getSqlSession().selectOne("WeChatUser.getWeChatUserByOpenid", paramMap);
    }

    public int createWeChatUser(WeChatUserEntity weChatUserEntity) throws Exception {
        return getSqlSession().insert("WeChatUser.createWeChatUser", weChatUserEntity);
    }

    public int updateWeChatUser(String openid, Map<String, Object> updateInfo) throws Exception{
        Map<String, Object> paramMap = new HashMap<>(updateInfo);
        paramMap.put("openid", openid);
        return getSqlSession().update("WeChatUser.updateWeChatUser", paramMap);
    }

    public int createMarketForecast(WeChatForecastEntity weChatForecastEntity) throws Exception {
        return getSqlSession().insert("WeChatUser.createMarketForecast", weChatForecastEntity);
    }

    public WeChatForecastEntity getUserMarketForecast(Long forecastDate, Long WeChatUserId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("forecastDate", forecastDate);
        paramMap.put("weChatUserId", WeChatUserId);
        return getSqlSession().selectOne("WeChatUser.getUserMarketForecast", paramMap);
    }

    public List<Map<String,Object>> getMarketForecastByDate(Long forecastDate){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("forecastDate", forecastDate);
        return getSqlSession().selectList("WeChatUser.getMarketForecastByDate", paramMap);
    }

    public int createUserMarketForecastCount(WeChatForecastCountEntity weChatForecastCountEntity) throws Exception {
        return getSqlSession().insert("WeChatUser.createUserMarketForecastCount", weChatForecastCountEntity);
    }

    public int updateUserMarketForecastCount(Long weChatUserId, Map<String, Object> updateInfo) throws Exception{
        Map<String, Object> paramMap = new HashMap<>(updateInfo);
        paramMap.put("weChatUserId", weChatUserId);
        return getSqlSession().update("WeChatUser.updateUserMarketForecastCount", paramMap);
    }

    public WeChatForecastCountEntity getUserMarketForecastCount(Long WeChatUserId) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("weChatUserId", WeChatUserId);
        return getSqlSession().selectOne("WeChatUser.getUserMarketForecastCount", paramMap);
    }

    public List<WeChatForecastEntity> getTodayForecastSuccess(Byte forecastType) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("forecastDate", DateUtil.getTimesToday());
        paramMap.put("forecastType", forecastType);
        return getSqlSession().selectList("WeChatUser.getTodayForecastSuccess", paramMap);
    }

    public List<WeChatForecastCountEntity> getAllWeChatForecastCount() throws Exception{
        return getSqlSession().selectList("WeChatUser.getAllWeChatForecastCount");
    }

    public Integer getUserBeatCnt(Integer correctDays) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("correctDays", correctDays);
        return getSqlSession().selectOne("WeChatUser.getUserBeatCnt", paramMap);
    }

    public Integer getWeChatForecastCountCnt() throws Exception{
        return getSqlSession().selectOne("WeChatUser.getWeChatForecastCountCnt");
    }
}
