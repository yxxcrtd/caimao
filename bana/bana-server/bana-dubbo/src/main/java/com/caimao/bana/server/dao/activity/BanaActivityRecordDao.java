package com.caimao.bana.server.dao.activity;

import com.caimao.bana.api.entity.activity.BanaActivityRecordEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户活动记录数据操作
 * Created by Administrator on 2015/7/31.
 */
@Repository
public class BanaActivityRecordDao extends SqlSessionDaoSupport {

    /**
     * 获取用户指定活动的活动列表
     * @param userId
     * @param actId
     * @return
     */
    public List<BanaActivityRecordEntity> getUserActivityRecordList(Long userId, Integer actId) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("actId", actId);
        return this.getSqlSession().selectList("BanaActivityRecord.getUserActivityRecordList", paramsMap);
    }

    /**
     * 添加活动记录
     * @param entity
     */
    public void save(BanaActivityRecordEntity entity) {
        this.getSqlSession().insert("BanaActivityRecord.save", entity);
    }
}
