package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.req.ybk.FQueryYbkHelpDocReq;
import com.caimao.bana.api.entity.ybk.YbkHelpDocEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* YbkHelpDocEntity 邮币卡帮助文档dao
*
* Created by wangxu@huobi.com on 2015-11-16 17:26:31 ����һ
*/
@Repository
public class YbkHelpDocDAO extends SqlSessionDaoSupport {

   /**
    * 根据查询条件，查询帮助列表
    *
    * @param req
    * @return
    */
    public List<YbkHelpDocEntity> queryYbkHelpDocWithPage(FQueryYbkHelpDocReq req) {
        return this.getSqlSession().selectList("YbkHelpDoc.queryYbkHelpDocWithPage", req);
    }

   /**
    * 根据ID获取帮助文档
    *
    * @param id
    * @return
    */
    public YbkHelpDocEntity selectById(Integer id) {
        return this.getSqlSession().selectOne("YbkHelpDoc.selectById", id);
    }

   /**
    * 添加帮助文档
    *
    * @param entity
    */
    public Integer insert(YbkHelpDocEntity entity) {
        return this.getSqlSession().insert("YbkHelpDoc.insert", entity);
    }

   /**
    * 删除帮助文档
    *
    * @param id
    */
    public Integer deleteById(Long id) {
        return this.getSqlSession().delete("YbkHelpDoc.deleteById", id);
    }

   /**
    * 更新帮助文档
    *
    * @param entity
    */
    public Integer update(YbkHelpDocEntity entity) {
        return this.getSqlSession().update("YbkHelpDoc.update", entity);
    }

}