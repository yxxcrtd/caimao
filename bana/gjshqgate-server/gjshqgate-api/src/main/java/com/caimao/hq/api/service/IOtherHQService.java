package com.caimao.hq.api.service;

import com.caimao.hq.api.entity.CandleReq;
import com.caimao.hq.api.entity.OtherCandle;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.entity.TradeAmountReq;

import java.io.IOException;
import java.util.List;

public interface IOtherHQService {

    int insertDB(OtherCandle candle);

    int insertBatchDB(List<OtherCandle> candleList);

    OtherCandle queryLastFromRedis(CandleReq candleReq);

    void updateDayOtherHQToRedis() throws IOException;

    void updateDayHistoryOtherHQToRedis() throws IOException;

    void updateMinOtherHQToRedis() throws IOException;

    void updateDay5OtherHQToRedis() throws IOException;

    List getMultiDaySnapshotRedis(TradeAmountReq tradeAmountReq);

    void updateSnapshotOtherHQToRedis() throws IOException;

    List<Snapshot> queryByExchange(String financeMic);

}