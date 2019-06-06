package com.caimao.bana.server.dao.guji;

import com.caimao.bana.api.entity.guji.GujiFocusRecordEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 股计用户关注表 DAO实现
*
* Created by wangxu@huobi.com on 2016-01-07 17:37:28 星期四
*/
@Repository
public class GujiFocusRecordDAO extends SqlSessionDaoSupport {

   /**
    * 查询指定GujiFocusRecord内容
    *
    * @param wxId
    * @return List
    */
    public List<GujiFocusRecordEntity> selectByWxId(Long wxId) {
        return this.getSqlSession().selectList("GujiFocusRecord.selectByWxId", wxId);
    }

    /**
     * 查询是否是关注关系
     *
     * @param openId
     * @param focusWxId
     * @return
     */
    public GujiFocusRecordEntity selectByWxIdAndFocusWxId(String openId, Long focusWxId) {
        Map<String, Object> params = new HashMap<>();
        params.put("openId", openId);
        params.put("focusWxId", focusWxId);
        return this.getSqlSession().selectOne("GujiFocusRecord.selectByWxIdAndFocusWxId", params);
    }

   /**
    * 添加GujiFocusRecord的接口
    *
    * @param entity
    */
    public Integer insert(GujiFocusRecordEntity entity) {
        return this.getSqlSession().insert("GujiFocusRecord.insert", entity);
    }

   /**
    * 删除GujiFocusRecord的接口
    *
    * @param openId
    * @param focusWxId
    * @param focusWxId
    */
    public Integer delete(String openId, Long focusWxId) {
        Map<String, Object> params = new HashMap<>();
        params.put("openId", openId);
        params.put("focusWxId", focusWxId);
        return this.getSqlSession().delete("GujiFocusRecord.deleteById", params);
    }

    /**
     * 我关注的人
     *
     * @param openId
     * @return
     */
    public List<GujiFocusRecordEntity> myFocus(String openId) {
        return this.getSqlSession().selectList("GujiFocusRecord.myFocus", openId);
    }

    /**
     * 关注我的人
     *
     * @param focusOpenId
     * @return
     */
    public List<GujiFocusRecordEntity> focusMe(String focusOpenId) {
        return this.getSqlSession().selectList("GujiFocusRecord.focusMe", focusOpenId);
    }

}