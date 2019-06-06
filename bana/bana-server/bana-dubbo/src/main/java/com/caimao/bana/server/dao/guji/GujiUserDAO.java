package com.caimao.bana.server.dao.guji;

import com.caimao.bana.api.entity.guji.GujiConfigEntity;
import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.entity.req.guji.FQueryAdminUserListReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* GujiUserEntity DAO实现
*
* Created by wangxu@huobi.com on 2016-01-07 17:42:48 星期四
*/
@Repository
public class GujiUserDAO extends SqlSessionDaoSupport {

   /**
    * 查询指定GujiUser内容
    *
    * @param wxId
    * @return
    */
    public GujiUserEntity selectByWxId(Long wxId) {
        return this.getSqlSession().selectOne("GujiUser.selectByWxId", wxId);
    }

    public GujiUserEntity selectByOpenId(String openId) {
        return this.getSqlSession().selectOne("GujiUser.selectByOpenId", openId);
    }

    /**
     * 查询后台的用户列表
     * @param req
     * @return
     */
    public List<GujiUserEntity> queryAdminUserListWithPage(FQueryAdminUserListReq req) {
        return this.getSqlSession().selectList("GujiUser.queryAdminUserListWithPage", req);
    }

    /**
    * 添加GujiUser的接口
    * @param entity
    */
    public Integer insert(GujiUserEntity entity) {
        return this.getSqlSession().insert("GujiUser.insert", entity);
    }

   /**
    * 删除GujiUser的接口
    * @param wxId
    */
    public Integer deleteByWxId(Long wxId) {
        return this.getSqlSession().delete("GujiUser.deleteByWxId", wxId);
    }

   /**
    * 更新GujiUser的接口
    *
    * @param entity
    */
    public Integer update(GujiUserEntity entity) {
        return this.getSqlSession().update("GujiUser.update", entity);
    }

    /**
     * 配置列表
     *
     * @return
     */
    public List<GujiConfigEntity> queryAdminConfigList() {
        return this.getSqlSession().selectList("GujiConfig.queryAdminConfigList");
    }

    /**
     * 根据id查找
     *
     * @return
     */
    public GujiConfigEntity selectById(Long id) {
        return this.getSqlSession().selectOne("GujiConfig.selectById", id);
    }

    /**
     * 根据id查找
     *
     * @return
     */
    public GujiConfigEntity findByKey(String key) {
        return this.getSqlSession().selectOne("GujiConfig.findByKey", key);
    }

    /**
     * 修改配置
     *
     * @param entity
     * @return
     */
    public Integer update(GujiConfigEntity entity) {
        return this.getSqlSession().update("GujiConfig.update", entity);
    }

}