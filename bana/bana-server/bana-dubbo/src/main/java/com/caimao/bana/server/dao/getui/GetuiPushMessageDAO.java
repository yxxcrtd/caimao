package com.caimao.bana.server.dao.getui;

import com.caimao.bana.api.entity.getui.GetuiPushMessageEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
* GetuiPushMessageEntity DAO实现
*
* Created by Xavier on 2015-11-04 16:25:49 星期三
*/
@Repository
public class GetuiPushMessageDAO extends SqlSessionDaoSupport {


   /**
    * 查询指定GetuiPushMessage内容    FOR UPDATE
    *
    * @param id
    * @return
    */
    public GetuiPushMessageEntity selectById(Long id) {
        return this.getSqlSession().selectOne("GetuiPushMessage.selectById", id);
    }

    /**
     * 获取未发送的消息记录
     * @return
     */
    public List<GetuiPushMessageEntity> queryNoSendList(Integer limit) {
        return this.getSqlSession().selectList("GetuiPushMessage.queryNoSendList", limit);
    }

   /**
    * 添加GetuiPushMessage的接口
    *
    * @param entity
    */
    public Integer insert(GetuiPushMessageEntity entity) {
        return this.getSqlSession().insert("GetuiPushMessage.insert", entity);
    }

   /**
    * 删除GetuiPushMessage的接口
    *
    * @param id
    */
    public Integer deleteById(Long id) {
        return this.getSqlSession().delete("GetuiPushMessage.deleteById", id);
    }

   /**
    * 更新GetuiPushMessage的接口
    *
    * @param entity
    */
    public Integer update(GetuiPushMessageEntity entity) {
        return this.getSqlSession().update("GetuiPushMessage.update", entity);
    }

}