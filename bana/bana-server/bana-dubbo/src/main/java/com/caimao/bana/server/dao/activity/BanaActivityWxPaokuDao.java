package com.caimao.bana.server.dao.activity;

import com.caimao.bana.api.entity.activity.BanaActivityWxPaokuEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 微信跑酷活动
 * Created by WangXu on 2015/5/22.
 */
@Repository
public class BanaActivityWxPaokuDao extends SqlSessionDaoSupport {

    /**
     * 保存数据
     * @param activityWxPaokuEntity
     * @return
     */
    public int save(BanaActivityWxPaokuEntity activityWxPaokuEntity) {
        return getSqlSession().insert("BanaActivityWxPaoku.save", activityWxPaokuEntity);
    }

    /**
     * 根据手机号获取数据
     * @param phone
     * @return
     */
    public BanaActivityWxPaokuEntity getByPhone(String phone) {
        return getSqlSession().selectOne("BanaActivityWxPaoku.getByPhone", phone);
    }
}
