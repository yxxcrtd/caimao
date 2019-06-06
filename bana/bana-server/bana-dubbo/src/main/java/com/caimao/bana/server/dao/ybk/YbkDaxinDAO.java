package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.ybk.YbkDaxinEntity;
import com.caimao.bana.api.entity.req.ybk.FQueryYbkDaxinReq;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* YbkDaxinEntity DAO实现
*
* Created by yangxinxin@huobi.com on 2015-11-17 17:47:39 星期二
*/
@Repository
public class YbkDaxinDAO extends SqlSessionDaoSupport {

   /**
    * 查询YbkDaxin列表
    *
    * @param req
    * @return
    */
    public List<YbkDaxinEntity> queryYbkDaxinWithPage(FQueryYbkDaxinReq req) {
        return this.getSqlSession().selectList("YbkDaxin.queryYbkDaxinWithPage", req);
    }

   /**
    * 查询指定YbkDaxin内容
    *
    * @param id
    * @return
    */
    public YbkDaxinEntity selectById(Long id) {
        return this.getSqlSession().selectOne("YbkDaxin.selectById", id);
    }

   /**
    * 添加YbkDaxin的接口
    *
    * @param entity
    */
    public Integer insert(YbkDaxinEntity entity) {
        return this.getSqlSession().insert("YbkDaxin.insert", entity);
    }

   /**
    * 删除YbkDaxin的接口
    *
    * @param id
    */
    public Integer deleteById(Long id) {
        return this.getSqlSession().delete("YbkDaxin.deleteById", id);
    }

   /**
    * 更新YbkDaxin的接口
    *
    * @param entity
    */
    public Integer update(YbkDaxinEntity entity) {
        return this.getSqlSession().update("YbkDaxin.update", entity);
    }

}