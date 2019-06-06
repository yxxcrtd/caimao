package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.req.ybk.FYbkMarketReq;
import com.caimao.bana.api.entity.res.ybk.FYBKMACDRes;
import com.caimao.bana.api.entity.ybk.YBKMACDEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮币卡日K MACD数据表
 */
@Repository
public class YBKMACDDao extends SqlSessionDaoSupport {
    public Integer insert(YBKMACDEntity ybkmacdEntity) throws Exception{
        return this.getSqlSession().insert("YBKMACD.insert", ybkmacdEntity);
    }

    public Integer update(YBKMACDEntity ybkmacdEntity) throws Exception{
        return this.getSqlSession().update("YBKMACD.update", ybkmacdEntity);
    }

    public YBKMACDEntity queryExist(YBKMACDEntity ybkmacdEntity) throws Exception{
        return this.getSqlSession().selectOne("YBKMACD.queryExist", ybkmacdEntity);
    }

    public List<FYBKMACDRes> queryMACD(FYbkMarketReq req) throws Exception{
        return this.getSqlSession().selectList("YBKMACD.queryMACD", req);
    }
}