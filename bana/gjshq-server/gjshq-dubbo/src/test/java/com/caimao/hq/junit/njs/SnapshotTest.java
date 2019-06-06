package com.caimao.hq.junit.njs;

import com.caimao.hq.api.entity.TradeAmountRes;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.junit.BaseTest;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.entity.TradeAmountReq;
import com.caimao.hq.core.NJSDataHandleThread;
import com.caimao.hq.core.NJSQuoteParseUtils;
import com.caimao.hq.utils.DateUtils;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.QueueUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

public class SnapshotTest extends BaseTest {

    @Autowired
    public JRedisUtil jredisUtil;
    @Autowired
    private IHQService hqService;
    @Autowired
    private NJSDataHandleThread njsDataHandleThread;
    private void setData(){

        int i=1;
        String mini="11:16:50";
        String old="11:16:50";
        String testData="#MsgType=2#CheckFlag=0##ApiName=onRecvDeferQuotation#DataType=TDeferQuotation#Flag=L#NodeID=6030#NodeType=6#Posi=4775912#RootID=#RspCode=RSP000000#RspMsg=交易成功#Ts_NodeID=1003#ask1=3413.00000000#ask2=3414.00000000#ask3=3415.00000000#ask4=3416.00000000#ask5=3417.00000000#askLot1=1262#askLot2=1550#askLot3=542#askLot4=1071#askLot5=53#average=3426.00000000#bid1=3412.00000000#bid2=3411.00000000#bid3=3410.00000000#bid4=3409.00000000#bid5=3408.00000000#bidLot1=1870#bidLot2=3225#bidLot3=22855#bidLot4=1999#bidLot5=6959#close=0.00000000#high=3452.00000000#highLimit=3718.00000000#instID=Ag(T+D)#last=3413.00000000#lastClose=3435.00000000#lastSettle=3443.00000000#low=3406.00000000#lowLimit=3167.00000000#name=白银延期#open=3438.00000000#quoteDate=20151017#quoteTime=11:16:50#sequenceNo=189025#settle=0.00000000#turnOver=8191902844.00000000#upDown=-30.00000000#upDownRate=0.00871333#volume=2390662#weight=2390662.00000000#ApdSckCode=662185848#ApdRecvTime=1444973481406#";
        while(true){
            if(i>=30){
                break;
            }
            i++;

            try {

                testData=testData.replace(old, mini);
                old=mini;
                QueueUtils.readQueueSJS.add(testData);
                System.out.println("添加数据：" + testData);
                mini= DateUtils.getNoTime("HH:mm:ss");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 测试南交所行情数据是否正常录入
     * Created by WangXu on 2015/10/19.
     */
    @Test
    public void testSnapshot(){

    }

    /**
     * 测试Redis是否正常
     * Created by WangXu on 2015/10/19.
     */
    @Test
    public void jedisTest(){


        jredisUtil.mset(new String[]{"key2", "value3", "key1", "value33"});
        Assert.assertEquals(2, jredisUtil.mget(new String[]{"key2", "key1"}).size());

        System.out.println("redis repeat key===="+jredisUtil.get("AllNJSDY2O369002.00"));


    }
    /**
     * 测试从redis是否能读取数据
     * Created by WangXu on 2015/10/19.
     */
    @Test
    public void snapQuery(){


        List<Snapshot> list= hqService.queryLastSnapshot("","Ag(T+D).SJS,Ag(T+D).SJS,mAu(T+D).SJS,SB.NJS,aa.NJS");
        System.out.println(list);


//        System.out.println( hqService.queryTicker("NJS", "AG"));
    }
    @Test
    public void snapQueryByExchange(){


        List<Snapshot> list= hqService.queryLastSnapshot("NJS","Ag(T+D).SJS,Ag(T+D).SJS,mAu(T+D).SJS,SB.NJS,aa.NJS");
        System.out.println(list);


//        System.out.println( hqService.queryTicker("NJS", "AG"));
    }
    @Test
   public void tradeAmountQueryHistory(){
        //njsDataHandleThread.run();
        TradeAmountReq tradeAmountReq=new TradeAmountReq();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式


        tradeAmountReq.setExchange("NJS");
        tradeAmountReq.setProdCode("AG");
        tradeAmountReq.setNumber(10);


        List<Snapshot> list= hqService.queryTradeAmountRedisHistory(tradeAmountReq);
        System.out.println(list);


    }
    @Test
    public void ShnapshotConvert(){
        String str="|90|ZS|ZS|ZS|南交指数|531.56|531.81|-0.20|-0.04|533.45|530.51|0.00|30109346|0|532.01|0.00|0.00|0.00|0.00|0.00|0|0|0|0|0|0.00|0.00|0.00|0.00|0.00|0|0|0|0|0|1027607569.16|0|20151105|185455|1027607569.16|20151105|185501|\u0006aaa";
        List<Snapshot> list =NJSQuoteParseUtils.convert(str);
        System.out.println(list);

    }


}
