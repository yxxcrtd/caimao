package com.caimao.hq.dao.impl;

import com.caimao.hq.api.entity.OtherCandle;
import com.caimao.hq.dao.OtherHQDao;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("otherHQDao")
public class OtherCandleDaoImpl extends SqlSessionDaoSupport implements OtherHQDao {

    @Override
    public int insertDB(OtherCandle candle) {
        return getSqlSession().insert("Other_Candle.insert", candle);
    }

    @Override
    public int insertBatchDB(List<OtherCandle> candleList) {
        return getSqlSession().insert("Other_Candle.insertBatch", candleList);
    }

}
