/*
 * Huobi.com Inc.
 * Copyright (c) 火币天下. All Rights Reserved
 */
package com.caimao.bana.server.dao;

import com.caimao.bana.api.entity.CouponChannelEntity;
import com.caimao.bana.api.entity.CouponReceiveEntity;
import com.caimao.bana.api.entity.CouponsEntity;
import com.caimao.bana.api.enums.CouponReceiveStatus;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("freeCouponsDao")
public class FreeCouponsDao extends SqlSessionDaoSupport{
    public int insertCouponsChannel(CouponChannelEntity couponChannel) throws Exception {
        return getSqlSession().insert("FreeCoupons.insertCouponsChannel", couponChannel);
    }

    public int updateCouponsChannel(Long id, Map<String, Object> updateInfo) throws Exception {
        Map<String, Object> paramMap = new HashMap<>(updateInfo);
        paramMap.put("id", id);
        return getSqlSession().update("FreeCoupons.updateCouponsChannel", paramMap);
    }

    public CouponChannelEntity getCouponChannel(Long id) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        return getSqlSession().selectOne("FreeCoupons.getCouponChannel", paramMap);
    }

    public CouponChannelEntity lotteryCouponChannel(Integer channelType, BigDecimal money) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("channelType", channelType);
        paramMap.put("money", money);
        return getSqlSession().selectOne("FreeCoupons.lotteryCouponChannel", paramMap);
    }

    public List<CouponChannelEntity> queryCouponChannelList(Byte status, Integer limitStart, Integer limitEnd) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("status", status);
        paramMap.put("limitStart", limitStart);
        paramMap.put("limitEnd", limitEnd);
        return getSqlSession().selectList("FreeCoupons.getCouponChannelList", paramMap);
    }

    public int insertCoupons(CouponsEntity coupons) throws Exception{
        return getSqlSession().insert("FreeCoupons.insertCoupons", coupons);
    }

    public CouponsEntity queryCoupons(Long id) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        return getSqlSession().selectOne("FreeCoupons.getCoupons", paramMap);
    }

    public CouponsEntity queryCouponsForUpdate(Long id) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        return getSqlSession().selectOne("FreeCoupons.getCouponsForUpdate", paramMap);
    }

    public int updateCoupons(Long id, Map<String, Object> updateInfo) throws Exception {
        Map<String, Object> paramMap = new HashMap<>(updateInfo);
        paramMap.put("id", id);
        return getSqlSession().update("FreeCoupons.updateCoupons", paramMap);
    }

    public List<CouponsEntity> queryCouponsList(Long userId, Byte status, Integer limitStart, Integer limitEnd) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("status", status);
        paramMap.put("limitStart", limitStart);
        paramMap.put("limitEnd", limitEnd);
        return getSqlSession().selectList("FreeCoupons.getCouponsList", paramMap);
    }

    public boolean queryCouponsSend(Long userId, Long channelId, Long extId){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("channelId", channelId);
        paramMap.put("extId", extId);
        CouponsEntity coupons = getSqlSession().selectOne("FreeCoupons.queryCouponsSend", paramMap);
        return coupons != null;
    }

    public boolean queryCouponsReceiveSend(Long couponId, Byte receiveType, Long phone){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("couponId", couponId);
        paramMap.put("receiveType", receiveType);
        paramMap.put("phone", phone);
        CouponReceiveEntity couponsReceive = getSqlSession().selectOne("FreeCoupons.queryCouponsReceiveSend", paramMap);
        return couponsReceive != null;
    }

    public int insertCouponReceive(CouponReceiveEntity couponReceive){
        return getSqlSession().insert("FreeCoupons.insertCouponReceive", couponReceive);
    }

    public int updateCouponReceive(Long id, Map<String, Object> updateInfo) throws Exception {
        Map<String, Object> paramMap = new HashMap<>(updateInfo);
        paramMap.put("id", id);
        return getSqlSession().update("FreeCoupons.updateCouponReceive", paramMap);
    }

    public CouponReceiveEntity queryCouponReceive(Long id) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        return getSqlSession().selectOne("FreeCoupons.getCouponReceive", paramMap);
    }

    public CouponReceiveEntity queryCouponReceiveForUpdate(Long id) throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        return getSqlSession().selectOne("FreeCoupons.getCouponReceiveForUpdate", paramMap);
    }

    public List<CouponReceiveEntity> queryCouponReceiveList(Long couponId, Long userId, Byte receiveType, Long phone, CouponReceiveStatus statusValue, Integer limitStart, Integer limitEnd) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("couponId", couponId);
        paramMap.put("userId", userId);
        paramMap.put("receiveType", receiveType);
        paramMap.put("phone", phone);
        paramMap.put("status", statusValue.getValue());
        paramMap.put("limitStart", limitStart);
        paramMap.put("limitEnd", limitEnd);
        return getSqlSession().selectList("FreeCoupons.getCouponReceiveList", paramMap);
    }
}
