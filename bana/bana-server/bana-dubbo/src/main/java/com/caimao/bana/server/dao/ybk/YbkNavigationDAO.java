package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.ybk.YbkNavigationEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* YbkNavigationEntity DAO实现
*
* Created by wangxu@huobi.com on 2015-12-07 10:02:47 星期一
*/
@Repository
public class YbkNavigationDAO extends SqlSessionDaoSupport {

    /**
     * 查询所有导航信息
     * @return
     */
    public List<YbkNavigationEntity> selectAll() {
        return this.getSqlSession().selectList("YbkNavigation.selectAll");
    }

   /**
    * 查询指定YbkNavigation内容
    *
    * @param id
    * @return
    */
    public YbkNavigationEntity selectById(Integer id) {
        return this.getSqlSession().selectOne("YbkNavigation.selectById", id);
    }

   /**
    * 添加YbkNavigation的接口
    *
    * @param entity
    */
    public Integer insert(YbkNavigationEntity entity) {
        return this.getSqlSession().insert("YbkNavigation.insert", entity);
    }

   /**
    * 删除YbkNavigation的接口
    *
    * @param id
    */
    public Integer deleteById(Integer id) {
        return this.getSqlSession().delete("YbkNavigation.deleteById", id);
    }

   /**
    * 更新YbkNavigation的接口
    *
    * @param entity
    */
    public Integer update(YbkNavigationEntity entity) {
        return this.getSqlSession().update("YbkNavigation.update", entity);
    }

}