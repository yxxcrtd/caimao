package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.req.ybk.FYbkMarketReq;
import com.caimao.bana.api.entity.res.ybk.FYBKKDJRes;
import com.caimao.bana.api.entity.ybk.YBKKDJEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮币卡日K KDJ数据表
 */
@Repository
public class YBKKDJDao extends SqlSessionDaoSupport {
    public Integer insert(YBKKDJEntity ybkkdjEntity) throws Exception{
        return this.getSqlSession().insert("YBKKDJ.insert", ybkkdjEntity);
    }

    public Integer update(YBKKDJEntity ybkkdjEntity) throws Exception{
        return this.getSqlSession().update("YBKKDJ.update", ybkkdjEntity);
    }

    public YBKKDJEntity queryExist(YBKKDJEntity ybkkdjEntity) throws Exception{
        return this.getSqlSession().selectOne("YBKKDJ.queryExist", ybkkdjEntity);
    }

    public List<FYBKKDJRes> queryKDJ(FYbkMarketReq req) throws Exception{
        return this.getSqlSession().selectList("YBKKDJ.queryKDJ", req);
    }
}