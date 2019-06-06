package com.caimao.hq.api.service;

import com.caimao.hq.api.entity.*;

import java.util.List;
import java.util.Set;

/**
 * Created by yzc on 2015/11/19.
 */
public interface IHQService {

    public Candle queryLastCandle(CandleReq candleReq);
    public List queryLastCandleFormate(CandleReq candleReq);


    public List<Candle>  queryHistoryCandle(CandleReq candleReq);
    public List<Snapshot>    queryLastSnapshot(String exchange, String prodCode);
    public List   queryLastSnapshotFormate(SnapshotReq req);
    /**
     * 查询多日分时，老接口，没有简化返回参数
     * @param tradeAmountReq 请求对象
     * @throws Exception
     */
    public List<Snapshot> getMultiDaySnapshot(TradeAmountReq tradeAmountReq);

    /**
     * 查询多日分时，新接口，简化返回参数
     * @param tradeAmountReq 请求对象
     * @throws Exception
     */
    public List getMultiDaySnapshotFormate(TradeAmountReq tradeAmountReq);

    public List<OwnProduct> queryOwnProductByUserId(Long userId);
    public int insertOwnProduct(OwnProduct ownProduct);

    public int updateOwnProduct(List<OwnProduct> ownProduct);
    public int deleteOwnProduct(List<OwnProduct> list) ;
    public int deleteOwnProduct(String userId,String ownProductId);
    public List  wizard(String field);
    public Snapshot queryTicker(String exchange, String prodCode);
    public List queryTickerFormate(SnapshotReq req);
    public void insertRedisSnapshotHistory(Snapshot snapshot) ;
    public void insertRedisCandleHistory(CandleCycle cycle,Candle candle) ;
    public List<Candle> queryCandleRedisHistory(CandleReq candleReq);
    public List queryCandleRedisHistoryFormate(CandleReq candleReq);

    public List<Snapshot> queryTradeAmountRedisHistory(TradeAmountReq tradeAmountReq);
    public List queryTradeAmountRedisHistoryFormate(TradeAmountReq tradeAmountReq);

    public void insertRedisMultiDayHistory(Snapshot snapshot) ;
    public Set<String> queryRedisMultiDayHistory(TradeAmountReq tradeAmountReq);
    public  void insertRedisSnapshotAll(List<Snapshot> snapshotList);
    public Set<String>  getSnapshotFromRedisAllAndDelete(String exchange);

    public  void   redisCleanLast(String exchange);
    public void redisCleanHistory(String exchange,String prodCode,String strCycle,String miniTime);
    public void setMaster(String appName,String status);
}
