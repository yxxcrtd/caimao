package com.caimao.hq.junit.njs;

import com.caimao.gjs.api.service.ITradeService;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.dao.NJSCandleDao;
import com.caimao.hq.dao.SJSCandleDao;
import com.caimao.hq.junit.BaseTest;
import com.caimao.hq.utils.DateUtils;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.QueueUtils;
import com.caimao.jserver.mina.MinaPlugin;
import com.caimao.jserver.mina.MinaServer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CandleTest extends BaseTest {

    @Autowired
    public JRedisUtil jredisUtil;
    @Autowired
    private IHQService hqService;
    @Autowired
    private NJSCandleDao njsCandleDao;
    @Autowired
    private SJSCandleDao sjsCandleDao;
    @Test
    public void queryCandleFromDB() {
        CandleReq candleReq=new CandleReq();
        candleReq.setExchange("NJS");
        candleReq.setCycle("7");
        candleReq.setBeginDate("2015112700");
        System.out.println(njsCandleDao.selectList(candleReq));

    }
    @Test
    public void testMaster(){


    }
    @Test
    public void deleteByPK() {
        njsCandleDao.deleteByPK("488980");

    }

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

    @Test
    public void queryLastCandle(){

        CandleReq candleReq=new CandleReq();
        candleReq.setProdCode("AG");
        candleReq.setExchange("NJS");
        candleReq.setCycle("1");
        System.out.println("testCandleQueryRedis==" + hqService.queryLastCandle(candleReq));

    }
    @Test
    public void queryCandleRedisHistory(){
        CandleReq candleReq=new CandleReq();
        candleReq.setProdCode("mAu(T+D)");
        candleReq.setExchange("SJS");
        candleReq.setCycle("1");
        candleReq.setNumber(50);
        List list=hqService.queryCandleRedisHistory(candleReq);
       System.out.println(list);
    }
    @Test
    public void queryLastSnapshot(){

        List list=hqService.queryLastSnapshot("NJS", "");
        System.out.println(list);
    }
    @Test
    public void queryTradeAmountRedisHistory(){

        TradeAmountReq tradeAmountReq=new TradeAmountReq();
        tradeAmountReq.setExchange("NJS");
        tradeAmountReq.setProdCode("AG");
        tradeAmountReq.setNumber(100);
        List<Snapshot> list=hqService.queryTradeAmountRedisHistory(tradeAmountReq) ;
        System.out.println(list);
    }


    /**
     * 测试Redis是否正常
     * Created by WangXu on 2015/10/19.
     */
    @Test
    public void jedisTest(){
        //测试队列
//        jredisUtil.del("c");
//        jredisUtil.lpush("testtick", "test1");
//        jredisUtil.lpush("testtick","test2");
//        jredisUtil.lpush("testtick", "test3");
//        jredisUtil.lpush("testtick", "test4");
//        jredisUtil.lpush("testtick", "test5");
//        jredisUtil.lpush("testtick","test6");
//        jredisUtil.lpush("testtick", "test7");
//        jredisUtil.lpush("testtick","test8");
//        jredisUtil.lpush("testtick","test9");
//        jredisUtil.ltrim("testtick",0,7);


     //  System.out.println(jredisUtil.lrange("testtick", 0, -1));
        //Hash


//
//


//        Set<String> set= jredisUtil.zrangebyscore("user", String.valueOf(DateUtils.getTickTime("201511261930", "yyyyMMddHHmm")), String.valueOf(DateUtils.getTickTime("201511261930", "yyyyMMddHHmm")));
//       if(null!=set&&set.size()>0){
//
//           Iterator<String> it = set.iterator();
//           String value="";
//           long index=0;
//           while(it.hasNext()){
//               value= it.next();
//                jredisUtil.zrem("user", value);
//               System.out.println("===="+value);
//           }
//
//       }

        jredisUtil.zadd("user", DateUtils.getTickTime("201511261730", "yyyyMMddHHmm"), "James11");
        jredisUtil.zadd("user", DateUtils.getTickTime("201511261931", "yyyyMMddHHmm"), "James21");
        jredisUtil.zadd("user", DateUtils.getTickTime("201511262000", "yyyyMMddHHmm"), "aaaaaa");


//
//        Set<String> set1= jredisUtil.zrange("user", 0, -1);
//
//        // jredisUtil.zremrangeByRank("NJSAGDayCandlehistory", 0,0);
//        Iterator<String> it = set1.iterator();
//        String value="";
//        long index=0;
//        System.out.println("总的："+set1);
//        Set<String> last=null;
//        while(jredisUtil.zcard("user")>0){
//
//            Set<String> temp= jredisUtil.zrange("user", 0, 2);
//            System.out.println("删除值："+temp);
//            jredisUtil.zrem("user", (String[]) temp.toArray(new String[temp.size()]));
//
//            last= jredisUtil.zrange("user", 0, -1);
//            System.out.println("删除后"+last);
//        }
       // System.out.println(jredisUtil.get("NJSAGDayCandlehistory"));
        //jredisUtil.del("*Snaphistory");
      //  jredisUtil.flushdb();
       System.out.println(jredisUtil.zrevrange("user", 0, -1));
        System.out.println(jredisUtil.zrevrank("user", "James11")) ;
        System.out.println(jredisUtil.zrevrank("user", "James21")) ;
        System.out.println(jredisUtil.zrevrank("user","aaaaaa")) ;


        System.out.println("aaa="+jredisUtil.zrangeByScore("user", DateUtils.getTickTime("201511261931", "yyyyMMddHHmm"),DateUtils.getTickTime("201511261730", "yyyyMMddHHmm"))) ;
        System.out.println(jredisUtil.zrankByScore("user", DateUtils.getTickTime("201511261931", "yyyyMMddHHmm"))) ;

        setAllNumber("user", 2);
        System.out.println(jredisUtil.zrevrange("user", 0, -1));

        System.out.println("NJS:" + jredisUtil.get(0, "NJS"));

    }
    @Test
    public void jedisDeleteTest(){


//         String redisKey="NJSWMinute30history";
//
//        long allNumber=jredisUtil.zcard(redisKey);
//        System.out.println("allNumber:"+allNumber);
//        setAllNumber(redisKey, 700);
//        long allNumber1=jredisUtil.zcard(redisKey);
//        System.out.println("allNumber:"+allNumber1);
//        System.out.println(jredisUtil.zrevrange("NJSWMinute30history", 0, -1));
    }

    private void setAllNumber(String redisKey,long number){

        try{
            long allNumber=jredisUtil.zcard(redisKey);
            if(allNumber>number){
                jredisUtil.zrangeAndDelete(redisKey, number, allNumber);
            }
        }catch (Exception ex){
            System.out.println("清理redis 总数目失败：redisKey=" + redisKey + "总条目为"+number);
        }

    }

    @Test
    public void deleteRedisCandleHistory(){

        //查询出最新的1条数据
        Set<String> set= jredisUtil.zrevrange("NJSAGDayCandlehistory", 0,0);
        Iterator<String> it = set.iterator();
        String value="";
        while(it.hasNext()){
            value= it.next();
             jredisUtil.zrem("NJSAGDayCandlehistory", value);//循环删除
        }

    }

    @Test
    public void minaRestart(){

        MinaServer.start();

    }

    @Test
    public void readNJSSnapshotAll(){

        while (true){
            System.out.println(jredisUtil.zrange("NJSSnapall", 0, 5));
            try {
                Thread.sleep(Long.parseLong("1000"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
    @Test
    public void selectSnapshotFive(){

        //njsDataHandleThread.run();
        TradeAmountReq tradeAmountReq=new TradeAmountReq();
        tradeAmountReq.setExchange("NJS");
        tradeAmountReq.setProdCode("AG");
        tradeAmountReq.setType(1);
        List list= hqService.getMultiDaySnapshot(tradeAmountReq);
          System.out.print(list);
    }

    @Test
    public void  insertPatchSJS(){

        List<SJSCandle> candleList=new ArrayList();
        SJSCandle candle=new SJSCandle();
        candle.setExchange("SJS");
        candle.setProdCode("SB");
        candle.setProdName("AAAA");
        candleList.add(candle);
        //njsCandleDao.insert(candle);
        sjsCandleDao.insertBatch(candleList);

    }

    @Test
    public void  insertPatchNJS(){

        List<NJSCandle> candleList=new ArrayList();
        NJSCandle candle=new NJSCandle();
        candle.setExchange("NJS");
        candle.setProdCode("SB");
        candle.setProdName("AAAA");
        candleList.add(candle);
        //njsCandleDao.insert(candle);
        njsCandleDao.insertBatch(candleList);

    }
}
