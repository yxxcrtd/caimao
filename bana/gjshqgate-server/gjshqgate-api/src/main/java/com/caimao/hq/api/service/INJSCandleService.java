/**
 *
 */
package com.caimao.hq.api.service;
import com.caimao.hq.api.entity.*;

import java.util.List;

/**
 * @author yzc
 */
public interface INJSCandleService {

    public List<Candle> queryDB(CandleReq candleReq);
    public Candle queryRedis(CandleReq candleReq);
    public void save(Snapshot baseSnapshot);
    //获取指定周期的前一个k线
    public Candle getPreCandl(CandleCycle cycle, String finance_mic, String prod_code, String miniTime);
    public void redisInit(String financeMic);
    public List getMultiDaySnapshotRedis(TradeAmountReq tradeAmountReq);
    public List getMultiDaySnapshotRedisFormate(TradeAmountReq tradeAmountReq);
    public void  insertPatch(List<Candle> candleList);

}