package com.caimao.gjs.server.dao;

import com.caimao.gjs.api.entity.GjsArticleCategoryEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* GjsArticleCategoryEntity DAO实现
*
* Created by yangxinxin@huobi.com on 2016-01-07 16:02:03 星期四
*/
@Repository
public class GjsArticleCategoryDAO extends SqlSessionDaoSupport {

   /**
    * 查询GjsArticleCategory列表
    *
    * @return
    */
    public List<GjsArticleCategoryEntity> queryGjsArticleCategoryAllList() {
        return this.getSqlSession().selectList("GjsArticleCategory.queryGjsArticleCategoryAllList");
    }

   /**
    * 查询指定GjsArticleCategory内容
    *
    * @param id
    * @return
    */
    public GjsArticleCategoryEntity selectById(Long id) {
        return this.getSqlSession().selectOne("GjsArticleCategory.selectById", id);
    }

   /**
    * 添加GjsArticleCategory的接口
    *
    * @param entity
    */
    public Integer insert(GjsArticleCategoryEntity entity) {
        return this.getSqlSession().insert("GjsArticleCategory.insert", entity);
    }

   /**
    * 删除GjsArticleCategory的接口
    *
    * @param id
    */
    public Integer deleteById(Long id) {
        return this.getSqlSession().delete("GjsArticleCategory.deleteById", id);
    }

   /**
    * 更新GjsArticleCategory的接口
    *
    * @param entity
    */
    public Integer update(GjsArticleCategoryEntity entity) {
        return this.getSqlSession().update("GjsArticleCategory.update", entity);
    }

}