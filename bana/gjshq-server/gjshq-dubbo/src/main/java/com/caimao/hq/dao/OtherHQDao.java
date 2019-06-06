package com.caimao.hq.dao;

import com.caimao.hq.api.entity.OtherCandle;

import java.util.List;

public interface OtherHQDao {

    int insertDB(OtherCandle candle);

    int insertBatchDB(List<OtherCandle> candleList);

}
