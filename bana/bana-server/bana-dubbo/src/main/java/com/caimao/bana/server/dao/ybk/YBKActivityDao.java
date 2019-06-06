package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.req.ybk.FYBKQueryActivityListReq;
import com.caimao.bana.api.entity.ybk.YBKActivityEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 邮币卡活动
 */
@Repository
public class YBKActivityDao extends SqlSessionDaoSupport {

    public List<YBKActivityEntity> queryActivityWithPage(FYBKQueryActivityListReq req) {
        return this.getSqlSession().selectList("YBKActivity.queryListWithPage", req);
    }

    public void insert(YBKActivityEntity entity) throws Exception{
        this.getSqlSession().insert("YBKActivity.insert", entity);
    }

    public void update(YBKActivityEntity entity) throws Exception{
        this.getSqlSession().update("YBKActivity.update", entity);
    }

    public YBKActivityEntity queryById(Long id) throws Exception{
        return this.getSqlSession().selectOne("YBKActivity.queryById", id);
    }

    public void delete(Long id) throws Exception{
        this.getSqlSession().delete("YBKActivity.delete", id);
    }
}