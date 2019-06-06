package com.caimao.gjs.server.dao;

import com.caimao.gjs.api.entity.GjsArticleIndexEntity;
import com.caimao.gjs.api.entity.req.FQueryArticleIndexReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* GjsArticleIndexEntity DAO实现
*/
@Repository
public class GjsArticleIndexDAO extends SqlSessionDaoSupport {

   /**
    * 查询GjsArticleIndex列表
    *
    * @return
    */
    public List<GjsArticleIndexEntity> queryGjsArticleIndexAllList(FQueryArticleIndexReq req) {
        return this.getSqlSession().selectList("GjsArticleIndex.queryArticleWithPage", req);
    }

   /**
    * 查询GjsArticleIndex接口列表
    *
    * @return
    */
    public List<GjsArticleIndexEntity> queryGjsArticleIndexListForInterface(FQueryArticleIndexReq req) {
        return this.getSqlSession().selectList("GjsArticleIndex.queryGjsArticleIndexListForInterfaceWithPage", req);
    }

   /**
    * 查询指定GjsArticleIndex内容
    *
    * @param id
    * @return
    */
    public GjsArticleIndexEntity selectById(Long id) {
        return this.getSqlSession().selectOne("GjsArticleIndex.selectById", id);
    }

   /**
    * 添加GjsArticleIndex的接口
    *
    * @param entity
    */
    public int insert(GjsArticleIndexEntity entity) {
        return this.getSqlSession().insert("GjsArticleIndex.insert", entity);
    }

   /**
    * 删除GjsArticleIndex的接口
    *
    * @param id
    */
    public Integer deleteById(Long id) {
        return this.getSqlSession().delete("GjsArticleIndex.deleteById", id);
    }

    /**
    * 更新GjsArticleIndex的接口
    *
    * @param entity
    */
    public Integer update(GjsArticleIndexEntity entity) {
        return this.getSqlSession().update("GjsArticleIndex.update", entity);
    }

    /**
     * 获取所有置顶
     *
     * @return
     */
    public List<GjsArticleIndexEntity> queryTopGjsArticleIndexList() {
        return this.getSqlSession().selectList("GjsArticleIndex.queryTopGjsArticleIndexList");
    }

    /**
     * 查询分类列表
     *
     * @return
     */
    public List<GjsArticleIndexEntity> queryGjsArticleIndexCategoryList(int category) {
        return this.getSqlSession().selectList("GjsArticleIndex.queryGjsArticleIndexCategoryList", category);
    }

}