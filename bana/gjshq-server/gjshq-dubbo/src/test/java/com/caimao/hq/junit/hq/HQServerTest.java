package com.caimao.hq.junit.hq;

import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.entity.CandleReq;
import com.caimao.hq.api.entity.SnapshotReq;
import com.caimao.hq.api.entity.TradeAmountReq;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.junit.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by yzc on 2015/12/18.
 */
public class HQServerTest extends BaseTest {
    @Autowired
    private IHQService hqService;

    @Test
    public void redisCleanHistory() {


        hqService.redisCleanHistory("NJS", "AG", "1", "201512172249");

    }

    @Test
    public void getMultiDaySnapshotFormate() {

        TradeAmountReq tradeAmountReq=new TradeAmountReq();
        tradeAmountReq.setExchange("SJS");
        tradeAmountReq.setProdCode("Ag(T+D)");
        tradeAmountReq.setType(1);
        tradeAmountReq.setCycle("1");
        System.out.println(JSON.toJSON(hqService.getMultiDaySnapshotFormate(tradeAmountReq)));
    }

    @Test
    public void getMultiDaySnapshot() {

        TradeAmountReq tradeAmountReq=new TradeAmountReq();
        tradeAmountReq.setExchange("SJS");
        tradeAmountReq.setProdCode("mAu(T+D)");
        tradeAmountReq.setType(1);
        tradeAmountReq.setCycle("1");
        System.out.println(JSON.toJSON(hqService.getMultiDaySnapshot(tradeAmountReq)));
    }
    @Test
    public void queryTradeAmountRedisHistoryFormate() {

        TradeAmountReq tradeAmountReq=new TradeAmountReq();
        tradeAmountReq.setExchange("NJS");
        tradeAmountReq.setProdCode("AG");
        tradeAmountReq.setNumber(50);
        System.out.println(JSON.toJSON(hqService.queryTradeAmountRedisHistoryFormate(tradeAmountReq)));
    }


    @Test
    public void queryLastCandleFormate() {

        CandleReq tradeAmountReq=new CandleReq();
        tradeAmountReq.setExchange("SJS");
        tradeAmountReq.setProdCode("Au(T+D)");
        tradeAmountReq.setCycle("6");
        System.out.println(JSON.toJSON(hqService.queryLastCandleFormate(tradeAmountReq)));
    }

    @Test
    public void queryCandleRedisHistoryFormate() {

        CandleReq tradeAmountReq=new CandleReq();
        tradeAmountReq.setExchange("SJS");
        tradeAmountReq.setProdCode("Au(T+D)");
        tradeAmountReq.setCycle("6");
        tradeAmountReq.setNumber(10);
        System.out.println(JSON.toJSON(hqService.queryCandleRedisHistoryFormate(tradeAmountReq)));
    }

    @Test
    public void queryCandleRedisHistory() {

        CandleReq tradeAmountReq=new CandleReq();
        tradeAmountReq.setExchange("SJS");
        tradeAmountReq.setProdCode("Au(T+D)");
        tradeAmountReq.setCycle("1");
        tradeAmountReq.setNumber(10);
        System.out.println(JSON.toJSON(hqService.queryCandleRedisHistory(tradeAmountReq)));
    }

    @Test
    public void queryLastSnapshotFormate() {

        SnapshotReq tradeAmountReq=new SnapshotReq();
        tradeAmountReq.setExchange("LIFFE");
        tradeAmountReq.setProdCode("AU");
        System.out.println(JSON.toJSON(hqService.queryLastSnapshotFormate(tradeAmountReq)));
    }

    @Test
    public void queryTickerFormate() {

        SnapshotReq tradeAmountReq=new SnapshotReq();
        tradeAmountReq.setExchange("SJS");
        tradeAmountReq.setProdCode("Au(T+D)");
        System.out.println(JSON.toJSON(hqService.queryTickerFormate(tradeAmountReq)));
    }




}
