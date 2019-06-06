package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.WeChatForecastCountEntity;
import com.caimao.bana.api.entity.WeChatForecastEntity;
import com.caimao.bana.api.entity.WeChatUserEntity;

import java.util.List;
import java.util.Map;

public interface WeChatUserService {
    /**
     * 根据openid获取微信用户信息
     * @param openid openid
     * @return
     * @throws Exception
     */
    public WeChatUserEntity getWeChatUserByOpenid(String openid) throws Exception;

    /**
     * 更新微信用户信息
     * @param openid openid
     * @param updateInfo 更新信息map
     * @return
     * @throws Exception
     */
    public Integer updateWeChatUser(String openid, Map<String, Object> updateInfo) throws Exception;

    /**
     * 创建微信用户信息
     * @param weChatUserEntity 微信用户实体类
     * @return
     * @throws Exception
     */
    public Integer createWeChatUser(WeChatUserEntity weChatUserEntity) throws Exception;

    /**
     * 创建大盘预测记录
     * @param weChatForecastEntity 预测记录实体类
     * @return
     * @throws Exception
     */
    public Integer createMarketForecast(WeChatForecastEntity weChatForecastEntity) throws Exception;

    /**
     * 获取用户预测记录
     * @param forecastDate 预测日期
     * @param WeChatUserId 微信用户userId
     * @return
     * @throws Exception
     */
    public WeChatForecastEntity getUserMarketForecast(Long forecastDate, Long WeChatUserId) throws Exception;

    /**
     * 获取预测记录列表
     * @param forecastDate 日期
     * @return
     * @throws Exception
     */
    public Map<String,String> getMarketForecastByDate(Long forecastDate) throws Exception;

    /**
     * 创建微信预测统计记录
     * @param weChatForecastCountEntity 预测统计记录实体类
     * @return
     * @throws Exception
     */
    public Integer createUserMarketForecastCount(WeChatForecastCountEntity weChatForecastCountEntity) throws Exception;

    /**
     * 更新微信预测统计记录
     * @param weChatUserId userId
     * @param updateInfo 更新map
     * @return
     * @throws Exception
     */
    public Integer updateUserMarketForecastCount(Long weChatUserId, Map<String, Object> updateInfo) throws Exception;

    /**
     * 获取用户预测统计记录
     * @param weChatUserId userId
     * @return
     * @throws Exception
     */
    public WeChatForecastCountEntity getUserMarketForecastCount(Long weChatUserId) throws Exception;

    /**
     * 获取预测成功的列表
     * @param forecastType 预测类型
     * @return
     * @throws Exception
     */
    public List<WeChatForecastEntity> getTodayForecastSuccess(Byte forecastType) throws Exception;

    /**
     * 获取所有预测统计列表
     * @return
     * @throws Exception
     */
    public List<WeChatForecastCountEntity> getAllWeChatForecastCount() throws Exception;

    /**
     * 获取用户击败数量
     * @param correctDays 成功天数
     * @return
     * @throws Exception
     */
    public Integer getUserBeatCnt(Integer correctDays) throws Exception;

    /**
     * 获取用户预测统计信息条数
     * @return
     * @throws Exception
     */
    public Integer getWeChatForecastCountCnt() throws Exception;
}