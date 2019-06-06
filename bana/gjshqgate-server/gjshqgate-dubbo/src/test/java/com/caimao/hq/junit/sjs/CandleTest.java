package com.caimao.hq.junit.sjs;

import com.caimao.hq.junit.BaseTest;
import com.caimao.hq.api.entity.Candle;
import com.caimao.hq.api.entity.CandleReq;
import com.caimao.hq.api.entity.TradeAmountReq;
import com.caimao.hq.api.service.ISJSCandleService;
import com.caimao.hq.api.service.ISJSSnapshotService;
import com.caimao.hq.core.SJSDataHandleThread;
import com.caimao.hq.utils.DoubleOperationUtil;
import com.caimao.hq.utils.JRedisUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandleTest extends BaseTest {

    @Autowired
    public JRedisUtil jredisUtil;
    @Autowired
    public ISJSSnapshotService sjsSnapshotService;
    @Autowired
    public ISJSCandleService sjsCandleService;
    @Autowired
    private SJSDataHandleThread sjsDataHandleThread;

    @Test
    public void testCandle(){


    }

    /**
     * 测试Redis是否正常
     * Created by WangXu on 2015/10/19.
     */
    @Test
    public void jedisTest(){

        jredisUtil.mset(new String[]{"key2", "value3", "key1", "value33"});
        Assert.assertEquals(2, jredisUtil.mget(new String[]{"key2", "key1"}).size());
    }
    @Test
    public void testCandleQueryDB(){

        Map map=new HashMap();
        map.put("financeMic","SJS");
        map.put("prodCode","Au99.95");
        map.put("cycle", "2");
        map.put("number", "10");
       // System.out.println(sjsCandleService.queryDB(map));
    }


    @Test
    public void testCandleQueryRedis(){

        Map map=new HashMap();
        CandleReq candleReq=new CandleReq();
        candleReq.setProdCode("Ag(T+D)");
        candleReq.setCycle("6");
        candleReq.setExchange("SJS");

        Candle  sjsCandle= sjsCandleService.queryRedis(candleReq);
        sjsCandle.setPxChange(DoubleOperationUtil.sub(sjsCandle.getLastPx(), sjsCandle.getLastSettle()));
        System.out.println(sjsCandle);
     //   jredisUtil.setex(sjsCandle.getRedisKey(), JSON.toJSONString(sjsCandle), 0);

    }


}
