package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.req.ybk.FYbkMarketReq;
import com.caimao.bana.api.entity.res.ybk.FYBKRSIRes;
import com.caimao.bana.api.entity.ybk.YBKRSIEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮币卡日K RSI数据表
 */
@Repository
public class YBKRSIDao extends SqlSessionDaoSupport {
    public Integer insert(YBKRSIEntity ybkrsiEntity) throws Exception{
        return this.getSqlSession().insert("YBKRSI.insert", ybkrsiEntity);
    }

    public Integer update(YBKRSIEntity ybkrsiEntity) throws Exception{
        return this.getSqlSession().update("YBKRSI.update", ybkrsiEntity);
    }

    public YBKRSIEntity queryExist(YBKRSIEntity ybkrsiEntity) throws Exception{
        return this.getSqlSession().selectOne("YBKRSI.queryExist", ybkrsiEntity);
    }

    public List<FYBKRSIRes> queryRSI(FYbkMarketReq req) throws Exception {
        return this.getSqlSession().selectList("YBKRSI.queryRSI", req);
    }
}