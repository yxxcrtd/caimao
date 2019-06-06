package com.caimao.bana.server.service.impl;

import com.caimao.bana.api.entity.WeChatForecastCountEntity;
import com.caimao.bana.api.entity.WeChatForecastEntity;
import com.caimao.bana.api.entity.WeChatUserEntity;
import com.caimao.bana.api.service.WeChatUserService;
import com.caimao.bana.server.dao.WeChatUserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信与预测活动实现类
 */
@Service("weChatUserService")
public class WeChatUserServiceImpl implements WeChatUserService {
    @Resource
    private WeChatUserDao weChatUserDao;

    @Override
    public WeChatUserEntity getWeChatUserByOpenid(String openid) throws Exception {
        return weChatUserDao.getWeChatUserByOpenid(openid);
    }

    @Override
    public Integer updateWeChatUser(String openid, Map<String, Object> updateInfo) throws Exception {
        return weChatUserDao.updateWeChatUser(openid, updateInfo);
    }

    @Override
    public Integer createWeChatUser(WeChatUserEntity weChatUserEntity) throws Exception {
        return weChatUserDao.createWeChatUser(weChatUserEntity);
    }

    @Override
    public Integer createMarketForecast(WeChatForecastEntity weChatForecastEntity) throws Exception {
        return weChatUserDao.createMarketForecast(weChatForecastEntity);
    }

    @Override
    public WeChatForecastEntity getUserMarketForecast(Long forecastDate, Long WeChatUserId) throws Exception {
        return weChatUserDao.getUserMarketForecast(forecastDate, WeChatUserId);
    }

    @Override
    public Map<String,String> getMarketForecastByDate(Long forecastDate) throws Exception {
        HashMap<String, String> hashMap = new HashMap();
        List<Map<String,Object>> dataList = weChatUserDao.getMarketForecastByDate(forecastDate);
        Long totalCnt = 0L;
        if(dataList != null){
            for (Map<String, Object> aDataList : dataList) {
                totalCnt += Long.valueOf(aDataList.get("cnt").toString());
            }
        }
        if(dataList != null){
            for (Map<String, Object> aDataList : dataList) {
                BigDecimal forecastScale = new BigDecimal(aDataList.get("cnt").toString()).divide(new BigDecimal(totalCnt), 4, BigDecimal.ROUND_DOWN);
                hashMap.put(aDataList.get("forecastType").toString(), forecastScale.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN).toString());
            }
            hashMap.put("total", totalCnt.toString());
        }
        return hashMap;
    }

    @Override
    public Integer createUserMarketForecastCount(WeChatForecastCountEntity weChatForecastCountEntity) throws Exception {
        return weChatUserDao.createUserMarketForecastCount(weChatForecastCountEntity);
    }

    @Override
    public Integer updateUserMarketForecastCount(Long weChatUserId, Map<String, Object> updateInfo) throws Exception {
        return weChatUserDao.updateUserMarketForecastCount(weChatUserId, updateInfo);
    }

    @Override
    public WeChatForecastCountEntity getUserMarketForecastCount(Long weChatUserId) throws Exception {
        return weChatUserDao.getUserMarketForecastCount(weChatUserId);
    }

    @Override
    public List<WeChatForecastEntity> getTodayForecastSuccess(Byte forecastType) throws Exception {
        return weChatUserDao.getTodayForecastSuccess(forecastType);
    }

    @Override
    public List<WeChatForecastCountEntity> getAllWeChatForecastCount() throws Exception {
        return weChatUserDao.getAllWeChatForecastCount();
    }

    @Override
    public Integer getUserBeatCnt(Integer correctDays) throws Exception {
        return weChatUserDao.getUserBeatCnt(correctDays);
    }

    @Override
    public Integer getWeChatForecastCountCnt() throws Exception {
        return weChatUserDao.getWeChatForecastCountCnt();
    }
}
