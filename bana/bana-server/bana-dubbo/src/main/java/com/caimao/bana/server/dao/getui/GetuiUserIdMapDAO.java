package com.caimao.bana.server.dao.getui;

import com.caimao.bana.api.entity.getui.GetuiUserIdMapEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* GetuiUserIdMapEntity DAO实现
*
* Created by Xavier on 2015-11-04 16:26:37 星期三
*/
@Repository
public class GetuiUserIdMapDAO extends SqlSessionDaoSupport {


   /**
    * 根据设备ID查找记录
    * @param cid    设备ID
    * @return
    */
    public GetuiUserIdMapEntity selectByCid(String cid) {
        return this.getSqlSession().selectOne("GetuiUserIdMap.selectByCid", cid);
    }

    /**
     * 根据用户ID查找记录
     * @param userId    用户ID
     * @return
     */
    public List<GetuiUserIdMapEntity> selectByUserid(Long userId) {
        return this.getSqlSession().selectList("GetuiUserIdMap.selectByUserid", userId);
    }

    /**
     * 根据用户与设备类型搜索
     * @param userId
     * @param deviceType
     * @return
     */
    public GetuiUserIdMapEntity selectByUserIdAndType(Long userId, String deviceType) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("deviceType", deviceType);
        return this.getSqlSession().selectOne("GetuiUserIdMap.selectByUserIdAndType", params);
    }

   /**
    * 添加GetuiUserIdMap的接口
    * @param entity
    */
    public Integer insert(GetuiUserIdMapEntity entity) {
        return this.getSqlSession().insert("GetuiUserIdMap.insert", entity);
    }

   /**
    * 删除GetuiUserIdMap的接口
    * @param id
    */
    public Integer deleteById(Long id) {
        return this.getSqlSession().delete("GetuiUserIdMap.deleteById", id);
    }

   /**
    * 更新GetuiUserIdMap的接口
    * @param entity
    */
    public Integer update(GetuiUserIdMapEntity entity) {
        return this.getSqlSession().update("GetuiUserIdMap.update", entity);
    }

}