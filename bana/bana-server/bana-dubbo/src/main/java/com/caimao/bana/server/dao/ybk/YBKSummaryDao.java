package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.ybk.YBKSummaryEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class YBKSummaryDao extends SqlSessionDaoSupport {
    public List<YBKSummaryEntity> queryList() {
        return this.getSqlSession().selectList("YBKSummary.queryList");
    }

    public int insert(YBKSummaryEntity ybkSummaryEntity) throws Exception{
        return this.getSqlSession().insert("YBKSummary.insert", ybkSummaryEntity);
    }

    public void update(YBKSummaryEntity entity) throws Exception{
        this.getSqlSession().update("YBKSummary.update", entity);
    }

    public YBKSummaryEntity queryById(Integer id) {
        return this.getSqlSession().selectOne("YBKSummary.queryById", id);
    }

    public List<HashMap<String, Object>> queryCompositeIndex() throws Exception{
        return this.getSqlSession().selectList("YBKSummary.queryCompositeIndex");
    }
}