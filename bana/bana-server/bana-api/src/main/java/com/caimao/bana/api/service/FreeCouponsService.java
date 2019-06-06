package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.CouponChannelEntity;
import com.caimao.bana.api.entity.CouponReceiveEntity;
import com.caimao.bana.api.entity.CouponsEntity;
import com.caimao.bana.api.enums.CouponChannelType;
import com.caimao.bana.api.enums.CouponReceiveStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 免息券
 */
public interface FreeCouponsService {
    public void insertCouponChannel(CouponChannelType couponChannelType, BigDecimal money, BigDecimal scale, Integer amount, Byte status) throws Exception;

    public void updateCouponChannel(Long id, Map<String, Object> updateInfo) throws Exception;

    public CouponChannelEntity queryCouponChannel(Long id) throws Exception;

    public CouponChannelEntity lotteryCouponChannel(CouponChannelType couponChannelType, BigDecimal money) throws Exception;

    public List<CouponChannelEntity> queryCouponChannelList(Byte status, Integer limitStart, Integer limitEnd) throws Exception;

    public Long insertCoupon(Long userId, Long channelId, BigDecimal money, Integer amount, Long extId) throws Exception;

    public void updateCoupon(Long id, Map<String, Object> updateInfo) throws Exception;

    public CouponsEntity queryCoupons(Long id) throws Exception;

    public List<CouponsEntity> queryCouponsList(Long userId, Byte status, Integer limitStart, Integer limitEnd) throws Exception;

    public void insertCouponReceive(Long couponId, Byte receiveType, Long phone) throws Exception;

    public void updateCouponReceive(Long id, Map<String, Object> updateInfo) throws Exception;

    public CouponReceiveEntity queryCouponReceive(Long id) throws Exception;

    public List<CouponReceiveEntity> queryCouponReceiveList(Long couponId, Long userId, Byte receiveType, Long phone, CouponReceiveStatus statusValue, Integer limitStart, Integer limitEnd) throws Exception;
}