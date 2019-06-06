package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.req.ybk.FYbkMarketReq;
import com.caimao.bana.api.entity.req.ybk.FYbkQueryCollectionRankingReq;
import com.caimao.bana.api.entity.res.ybk.FYBKCollectionRankingRes;
import com.caimao.bana.api.entity.res.ybk.FYBKKLineRes;
import com.caimao.bana.api.entity.ybk.YBKKLineEntity;
import com.caimao.bana.api.entity.ybk.YBKTimeLineEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮币卡日K数据表
 */
@Repository
public class YBKKLineDao extends SqlSessionDaoSupport {
    public Integer insert(YBKKLineEntity ybkkLineEntity) throws Exception{
        return this.getSqlSession().insert("YBKKLine.insert", ybkkLineEntity);
    }

    public Integer update(YBKKLineEntity ybkkLineEntity) throws Exception{
        return this.getSqlSession().update("YBKKLine.update", ybkkLineEntity);
    }

    public YBKKLineEntity queryExist(YBKKLineEntity ybkkLineEntity) throws Exception{
        return this.getSqlSession().selectOne("YBKKLine.queryExist", ybkkLineEntity);
    }

    public List<YBKKLineEntity> queryNumOfDays(YBKTimeLineEntity ybkTimeLineEntity, Integer days) throws Exception{
        Map<String, Object> params = new HashMap<>();
        params.put("exchangeName", ybkTimeLineEntity.getExchangeName());
        params.put("code", ybkTimeLineEntity.getCode());
        params.put("date", ybkTimeLineEntity.getDatetime());
        params.put("days", days);
        return this.getSqlSession().selectList("YBKKLine.queryNumOfDays", params);
    }

    public List<FYBKKLineRes> queryKLine(FYbkMarketReq req) throws Exception{
        return this.getSqlSession().selectList("YBKKLine.queryKLine", req);
    }

    public List<FYBKCollectionRankingRes> queryCollectionRankingWithPage(FYbkQueryCollectionRankingReq req) throws Exception{
        if (req.getHqDate() == null) {
            req.setHqDate(this.getLastDate(req.getExchangeShortName()));
        }
        return this.getSqlSession().selectList("YBKKLine.queryCollectionRankingWithPage", req);
    }

    public FYBKCollectionRankingRes getCollectionInfo(String exchangeShortName, String code) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("exchangeShortName", exchangeShortName);
        params.put("code", code);
        return this.getSqlSession().selectOne("YBKKLine.getCollectionInfo", params);
    }

    private Date getLastDate(String exchangeShortName) {
        return this.getSqlSession().selectOne("YBKKLine.getLastDate", exchangeShortName);
    }
}