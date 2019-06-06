package com.caimao.hq.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.api.service.*;
import com.caimao.hq.core.HQDataInit;
import com.caimao.hq.core.NJSDataHandleThread;
import com.caimao.hq.utils.*;
import com.caimao.jserver.mina.MinaServer;
import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by yzc on 2015/11/19.
 */

@Service("hqService")
public class HQServiceImpl implements IHQService {
    private Logger logger = LoggerFactory.getLogger(HQServiceImpl.class);
    @Autowired
    public JRedisUtil jredisUtil;
    @Autowired
    private GJSProductUtils gjsProductUtils;
    @Autowired
    private INJSCandleService njsCandleService;
    @Autowired
    private IOwnProductService ownProductService;
    @Autowired
    private ISJSCandleService sjsCandleService;

    @Autowired
    private ISJSSnapshotService sjsSnapshotService;
    @Autowired
    private INJSSnapshotService njsSnapshotService;

    @Autowired
    private IOtherHQService otherHQService;


    @Value("${redis.allNumber.cycle.Snap}")
    protected String REDIS_ALLNUMBER_CYCLE_Snap;

    @Value("${redis.allNumber.cycle.Snap5}")
    protected String REDIS_ALLNUMBER_CYCLE_Snap5;

    @Value("${redis.allNumber.cycle.Minute1}")
    protected String REDIS_ALLNUMBER_CYCLE_Minute1;

    @Value("${redis.allNumber.cycle.Minute5}")
    protected String REDIS_ALLNUMBER_CYCLE_Minute5;

    @Value("${redis.allNumber.cycle.Minute15}")
    protected String REDIS_ALLNUMBER_CYCLE_Minute15;

    @Value("${redis.allNumber.cycle.Minute30}")
    protected String REDIS_ALLNUMBER_CYCLE_Minute30;

    @Value("${redis.allNumber.cycle.Hour1}")
    protected String REDIS_ALLNUMBER_CYCLE_Hour1;

    @Value("${redis.allNumber.cycle.DayCandle}")
    protected String REDIS_ALLNUMBER_CYCLE_DayCandle;


    @Value("${redis.allNumber.cycle.Week}")
    protected String REDIS_ALLNUMBER_CYCLE_Week;

    @Value("${redis.allNumber.cycle.Month}")
    protected String REDIS_ALLNUMBER_CYCLE_Month;

    @Value("${redis.allNumber.cycle.Year}")
    protected String REDIS_ALLNUMBER_CYCLE_Year;

    @Value("${isDeleteCandle}")
    protected String isDeleteCandle;

    @Override
    public Candle queryLastCandle(CandleReq obj) {

        Candle candle = null;
        try{
            if(null!=obj){
                if ("SJS".equalsIgnoreCase(obj.getExchange())) {
                    candle = sjsCandleService.queryRedis(obj);
                } else if ("NJS".equalsIgnoreCase(obj.getExchange())) {
                    candle = njsCandleService.queryRedis(obj);
                } else if ("LIFFE".equalsIgnoreCase(obj.getExchange())) {
                    candle = otherHQService.queryLastFromRedis(obj);
                }
                if(null==candle){
                    candle=new Candle();
                    candle.setExchange(obj.getExchange());
                    candle.setProdCode(obj.getProdCode());
                    candle.setProdName("");
                }
            }
        }catch (Exception ex){
            logger.error("queryLastCandle信息异常  {}"+ex.getMessage());
        }
        return candle;
    }


    public List<Candle> queryLastCandleTwoFormate(CandleReq obj) {
        List alllist=new ArrayList();
        Candle newCandle = null;
        try{
            if(null!=obj){
                if ("SJS".equalsIgnoreCase(obj.getExchange())) {
                    newCandle = sjsCandleService.queryRedis(obj);
                } else if ("NJS".equalsIgnoreCase(obj.getExchange())) {
                    newCandle = njsCandleService.queryRedis(obj);
                } else if ("LIFFE".equalsIgnoreCase(obj.getExchange())) {
                    newCandle = otherHQService.queryLastFromRedis(obj);
                }
                if(null==newCandle){
                    newCandle=new Candle();
                    newCandle.setExchange(obj.getExchange());
                    newCandle.setProdCode(obj.getProdCode());
                    newCandle.setProdName("");
                }
                alllist.add(newCandle);
                setHistoryCandle(alllist,obj);
            }

        }catch (Exception ex){
            logger.error("queryLastCandle信息异常  {}"+ex.getMessage());
        }
        return alllist;
    }

    private void  setHistoryCandle(List allList,CandleReq obj){

        if(null!=allList&&null!=obj){

            CandleReq temp=new CandleReq();
            temp.setExchange(obj.getExchange());
            temp.setProdCode(obj.getProdCode());
            temp.setCycle(obj.getCycle());
            temp.setNumber(1);
            List list=queryCandleRedisHistory(temp);
            if(list!=null&&list.size()==1){
                allList.add(list.get(0));
            }
        }
    }

    @Override
    public List queryLastCandleFormate(CandleReq candleReq) {

        List  candleList= queryLastCandleTwoFormate(candleReq);
        List convertList=new ArrayList();
        try {
            DozerMapperSingleton.listCopy(candleList, convertList, "com.caimao.hq.api.entity.CandleResFormate");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List listAll=new ArrayList();
        LinkedMap map=new LinkedMap();
        Candle temp=null;
        if(null!=candleList&&candleList.size()>0){
            temp=(Candle)candleList.get(0);
            if(StringUtils.isBlank(candleReq.getRetParaKeys())){
                map.put("ret", DozerMapperSingleton.fillProperty(convertList));
            }else{
                map.put("ret", DozerMapperSingleton.fillProperty(convertList, candleReq.getRetParaKeys()));
            }
            DozerMapperSingleton.setObjParaKeys(map, candleReq.getObjParaKeys(), temp);
        }
        listAll.add(map);
        return  listAll;
    }

    @Override
    public List<Candle> queryHistoryCandle(CandleReq obj) {


        List candleList = null;
        try{
            if(null!=obj){
                if ("SJS".equalsIgnoreCase(obj.getExchange())) {
                    candleList = sjsCandleService.queryDB(obj);
                } else if ("NJS".equalsIgnoreCase(obj.getExchange())) {
                    candleList = njsCandleService.queryDB(obj);
                }
            }

        }catch (Exception ex){

            logger.error("queryHistoryCandle信息异常  {}"+ex.getMessage());
        }

        return candleList;
    }



     public List<Candle> queryCandleRedisHistory(CandleReq candleReq){


         long beginTime=0;
         long endTime=0;
         String redisKey="";
         Set<String> resultStr=null;
         List<Candle>  resultList=new ArrayList();
         try{
             if(null!=candleReq){
                 if(candleReq.getNumber()<1){
                     candleReq.setNumber(50);
                 }
                 if(StringUtils.isBlank(candleReq.getSearchDirection())){
                     candleReq.setSearchDirection("1");
                 }
                 redisKey=SJSMinTimeUtil.getRedisKeyHistory(CandleUtils.mapCycle.get(candleReq.getCycle()),candleReq.getExchange(),candleReq.getProdCode());
                 if(StringUtils.isBlank(candleReq.getBeginDate())&&StringUtils.isBlank(candleReq.getEndDate())){//时间都为空
                     resultStr=jredisUtil.zrevrange(redisKey, 0, candleReq.getNumber()-1);
                 }else if(!StringUtils.isBlank(candleReq.getBeginDate())&&!StringUtils.isBlank(candleReq.getEndDate())){//时间都不为空
                     beginTime=DateUtils.getTickTime(candleReq.getBeginDate(),"yyyyMMddHHmm");
                     endTime=DateUtils.getTickTime(candleReq.getEndDate(),"yyyyMMddHHmm");
                     resultStr=jredisUtil.zrangeByScore(redisKey,endTime,beginTime);
                 }else if(!StringUtils.isBlank(candleReq.getBeginDate())&&StringUtils.isBlank(candleReq.getEndDate())){//开始时间不为空，结束时间为空

                   beginTime=DateUtils.getTickTime(candleReq.getBeginDate(),"yyyyMMddHHmm");
                   long zrank=jredisUtil.zrankByScore(redisKey,beginTime);
                   if(zrank<0){
                       zrank=0;
                   }
                   if("1".equalsIgnoreCase(candleReq.getSearchDirection())){
                       resultStr=jredisUtil.zrevrange(redisKey, zrank, zrank+candleReq.getNumber()-1);
                   }else if("2".equalsIgnoreCase(candleReq.getSearchDirection())){
                       long begin=zrank-candleReq.getNumber()+1;
                       if(begin<0){
                           begin=0;
                       }
                       resultStr=jredisUtil.zrevrange(redisKey, begin, zrank);
                   }
                 }
                 if(null!=resultStr){
                     gjsProductUtils.convertStrToCandleObject(resultStr,candleReq.getExchange(), resultList);
                 }
             }
          }catch (Exception ex){

             logger.error("queryCandleRedisHistory信息异常  {}"+ex.getMessage());
        }

        return resultList;
     }

    @Override
    public List queryCandleRedisHistoryFormate(CandleReq candleReq) {

        List listSource=queryCandleRedisHistory(candleReq);
        Candle lastCandle=queryLastCandle(candleReq);
        List convertList=new ArrayList();
        try {
            DozerMapperSingleton.listCopy(listSource, convertList, "com.caimao.hq.api.entity.CandleResFormate");
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinkedMap map=new LinkedMap();
        Candle temp=null;
        CandleResFormate candleResFormate=new CandleResFormate();
        if(null!=listSource&&listSource.size()>0){
            temp=(Candle)listSource.get(0);
            if(StringUtils.isBlank(candleReq.getRetParaKeys())){
                try {
                    DozerMapperSingleton.getInstance().map(lastCandle, candleResFormate);
                } catch (Exception e) {
                   logger.error("转换queryCandleRedisHistoryFormate CandleResFormate 异常: "+e.getMessage());
                }
                map.put("ret", DozerMapperSingleton.fillProperty(convertList));
                map.put("last", DozerMapperSingleton.fillProperty(candleResFormate));
            }else{
                map.put("ret", DozerMapperSingleton.fillProperty(convertList, candleReq.getRetParaKeys()));
                map.put("last", DozerMapperSingleton.fillProperty(lastCandle, candleReq.getRetParaKeys()));
            }
            DozerMapperSingleton.setObjParaKeys(map, candleReq.getObjParaKeys(), temp);
        }
        List listAll=new ArrayList();
        listAll.add(map);
        return listAll;
    }

    public List<Snapshot> queryTradeAmountRedisHistory(TradeAmountReq tradeAmountReq) {

        List<Snapshot>  resultList=new ArrayList();
        try{
            if(null!=tradeAmountReq){

                if(tradeAmountReq.getNumber()<0){
                    tradeAmountReq.setNumber(50);
                }
                String redisKey=MinTimeUtil.getRedisKeyHistory(CandleCycle.Snap, tradeAmountReq.getExchange(),tradeAmountReq.getProdCode());
                List<String> resultStr=jredisUtil.lrange(redisKey, 0, tradeAmountReq.getNumber());

                gjsProductUtils.convertStrToSnapObject(resultStr,resultList);

            }
         }catch (Exception ex){

                 logger.error("queryCandleRedisHistory信息异常  {}"+ex.getMessage());
         }

        return resultList;
    }

    @Override
    public List queryTradeAmountRedisHistoryFormate(TradeAmountReq tradeAmountReq) {
        List list=queryTradeAmountRedisHistory(tradeAmountReq);
        List convertList=new ArrayList();
        try {
            DozerMapperSingleton.listCopy(list, convertList, "com.caimao.hq.api.entity.TradeAmountResFormate");
        } catch (Exception e) {
            logger.error("转换queryCandleRedisHistoryFormate CandleResFormate 异常: "+e.getMessage());
        }
        LinkedMap map=new LinkedMap();
        Snapshot temp=null;
        if(null!=list&&list.size()>0){
            temp=(Snapshot)list.get(0);
            if(StringUtils.isBlank(tradeAmountReq.getRetParaKeys())){

                map.put("ret", DozerMapperSingleton.fillProperty(convertList));
            }else{
                map.put("ret", DozerMapperSingleton.fillProperty(convertList, tradeAmountReq.getRetParaKeys()));
            }
            DozerMapperSingleton.setObjParaKeys(map, tradeAmountReq.getObjParaKeys(), temp);

        }
        List listAll=new ArrayList();
        listAll.add(map);
        return listAll;
    }

    //先获取多日分时的 最近的几个交易日期
    public Set<String> queryRedisMultiDayHistory(TradeAmountReq tradeAmountReq) {
        Set<String> set1=null;

        try{
            if(null!=tradeAmountReq){

                if(tradeAmountReq.getType()<=0){
                    tradeAmountReq.setType(1);
                }
                if(tradeAmountReq.getType()>10){
                    tradeAmountReq.setType(10);
                }
                String redisKey=MinTimeUtil.getRedisKeyMultiDaySnapshot(CandleCycle.DayCandle, tradeAmountReq.getExchange(), tradeAmountReq.getProdCode());
                set1= jredisUtil.zrevrange(redisKey, 0, tradeAmountReq.getType()-1);
            }
        }catch (Exception ex){

            logger.error("queryCandleRedisHistory信息异常  {}"+ex.getMessage());
        }

        return set1;
    }




    public List<Snapshot> queryLastSnapshot(String exchange, String prodCode) {

        List<Snapshot> snapshotList=null;
        try{
            if (!StringUtils.isBlank(exchange)) {
                snapshotList=queryLastSnapshotByExchange(exchange);
            }else{

                if (!StringUtils.isBlank(prodCode)) {
                    snapshotList=queryLastSnapshotByProdCode(prodCode);
                }
            }
        }catch (Exception ex){

            logger.error("queryCandleRedisHistory信息异常  {}"+ex.getMessage());
        }

        return  snapshotList;
    }

    @Override
    public List queryLastSnapshotFormate(SnapshotReq req) {

        List<Snapshot> list= queryLastSnapshot(req.getExchange(),req.getProdCode());
        List listAll=new ArrayList();
        if(null!=list&&list.size()>0){

                for(Snapshot snapshot:list){
                    SnapshotResFormate res=new SnapshotResFormate();
                    DozerMapperSingleton.getInstance().map(snapshot, res);
                    LinkedMap map=new LinkedMap();
                    if(StringUtils.isBlank(req.getRetParaKeys())){

                        map.put("ret", DozerMapperSingleton.fillProperty(res));
                    }else{
                        map.put("ret", DozerMapperSingleton.fillProperty(res, req.getRetParaKeys()));
                    }
                    DozerMapperSingleton.setObjParaKeys(map, req.getObjParaKeys(), snapshot);
                    listAll.add(map);
                }
        }
        return listAll;
    }


    private List<Snapshot>   queryLastSnapshotByExchange(String exchange){

        List<Snapshot>  snapshotList=null;
        try{
            if(!StringUtils.isBlank(exchange)){
                if ("SJS".equalsIgnoreCase(exchange)) {
                    snapshotList = sjsSnapshotService.queryByExchange("SJS");
                } else if ("NJS".equalsIgnoreCase(exchange)) {
                    snapshotList = njsSnapshotService.queryByExchange("NJS");
                } else if ("LIFFE".equalsIgnoreCase(exchange)) {
                    snapshotList = otherHQService.queryByExchange("LIFFE");
                }
            }
        }catch (Exception ex){

            logger.error("queryCandleRedisHistory信息异常  {}"+ex.getMessage());
        }

        return snapshotList;
    }

    public List<Snapshot> queryLastSnapshotByProdCode(String prodCode) {
        List<Snapshot> resultlist=new ArrayList();

        try{
            String[] prodCodArray =getProdCode(prodCode);
            if(null==prodCodArray||prodCodArray.length>200){
                logger.error("单个行情查询，传入产品代码为Null或者传入产品个数大于200个");
                throw new RuntimeException("单个行情查询，传入产品代码为Null或者传入产品个数大于200个");
            }
            Map<String,Snapshot> resultMap=new HashMap();
            List<String> redisStr=null;
            String[] redisKeyArray = new String[prodCodArray.length];
            String redisKey="";
            Map<String,String>  prodProperty=null;
            for(int i=0;i<prodCodArray.length;i++){
                if(!StringUtils.isBlank(prodCodArray[i])){
                    redisKey = MinTimeUtil.getRedisKey(CandleCycle.Snap,prodCodArray[i]);
                    redisKeyArray[i]=redisKey;
                }
            }
            redisStr=jredisUtil.mget(redisKeyArray);
            gjsProductUtils.convertStrToSnapObject(redisStr, resultlist);
            //gjsProductUtils.convertMapToList(resultMap, prodCodArray, resultlist);
        }catch (Exception ex){

            logger.error("queryLastSnapshotByProdCode信息异常  {}"+ex.getMessage());
        }

        return resultlist;

    }
    private String[] getProdCode(String prodCode){

        String[] prodCodArray=null;
        try{
            if(!StringUtils.isBlank(prodCode)){

                prodCodArray=prodCode.split(",");
            }
        }catch (Exception ex){

            logger.error("getProdCode信息异常  {}"+ex.getMessage());
        }

        return prodCodArray;
    }


    @Override
    public synchronized void insertRedisSnapshotHistory(Snapshot snapshot) {
        if(!StringUtils.isBlank(snapshot.getRedisKeyHistory())){
            try{

                if(snapshot.getLastAmount()==0||snapshot.getOpenPx()==0||snapshot.getLowPx()==0){

                    return;
                }
                if (!"LIFFE".equals(snapshot.getExchange())) {
                    checkIsExist(snapshot.getRedisKeyHistory(), snapshot);
                }
                String jsonString = JSON.toJSONString(snapshot);
                jredisUtil.lpush(snapshot.getRedisKeyHistory(), jsonString);
                jredisUtil.ltrim(snapshot.getRedisKeyHistory(), 0, 500);
            }catch (Exception ex){

                logger.error("insertRedisSnapshotHistory信息异常  {}"+ex.getMessage());
           }

        }
    }


    /***
     *
     * 保存多日分时的  自然日，用来获取最近的几个交易日列表
     *
     * */
    @Override
    public void insertRedisMultiDayHistory(Snapshot snapshot) {

        if(!StringUtils.isBlank(snapshot.getMinTime())){

            try{
                String redisKey=MinTimeUtil.getRedisKeyMultiDaySnapshot(CandleCycle.DayCandle, snapshot.getExchange(), snapshot.getProdCode());
                double score=DateUtils.getTickTime(snapshot.getTradeDate(), "yyyyMMdd");
                jredisUtil.zadd(redisKey, score, snapshot.getTradeDate());
            }catch (Exception ex){

                logger.error("insertRedisMultiDayHistory信息异常  {}"+ ex.getMessage());
            }


        }
    }

    private   void checkIsExist(String redisKey,Snapshot snapshot){

        try{
            if(!StringUtils.isBlank(redisKey)&&null!=snapshot){
                List<Snapshot>  resultList=new ArrayList();
                List<String> resultStr=jredisUtil.lrange(redisKey, 0, 5);
                if(null!=resultStr&&resultStr.size()>0){
                    Snapshot temp=null;
                    gjsProductUtils.convertStrToSnapObject(resultStr,resultList);
                    if(null!=resultList&&resultList.size()>0){
                        for(int i=0;i<resultList.size();i++){

                            temp=resultList.get(i);
                            if(null!=temp){
                                if(temp.getExchange().equalsIgnoreCase(snapshot.getExchange())
                                        &&temp.getProdCode().equalsIgnoreCase(snapshot.getProdCode())
                                        &&temp.getMinTime().equalsIgnoreCase(snapshot.getMinTime())
                                        &&temp.getLastAmount()==snapshot.getLastAmount()
                                        &&temp.getBusinessAmount()==snapshot.getBusinessAmount()
                                        ){
                                    throw new RuntimeException("重复数据异常："+snapshot.toString());
                                }
                            }

                        }
                    }
                }
            }
        } catch (Exception ex){

        logger.error("checkIsExist信息异常  {}"+ex.getMessage());
       }
    }
    @Override
    public void insertRedisCandleHistory(CandleCycle cycle, Candle candle) {

        try{
            String redisKey=SJSMinTimeUtil.getRedisKeyHistory(cycle,candle.getExchange(),candle.getProdCode());
            double  score=DateUtils.getTickTime(candle.getMinTime(), "yyyyMMddHHmm");
            jredisUtil.zadd(redisKey, score, JSON.toJSONString(candle), true);
            try{
                if(!StringUtils.isBlank(isDeleteCandle)&&"true".equalsIgnoreCase(isDeleteCandle)){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
                    if(hour >=1&&hour<=2){//每天1-2点的时候，开始设置redis数据
                        setAllNumber(redisKey,cycle);
                    }
                }
            }catch (Exception ex){
                logger.error("清理redis 数据异常:"+redisKey+"|cycle"+ex.getMessage());
            }

        }catch (Exception ex){

            logger.error("insertRedisCandleHistory信息异常  {}"+ex.getMessage());
        }

    }
    //清理多余的数据
    private void  setAllNumber(String redisKey,CandleCycle cycle){


        switch (cycle){
            case Minute1:
                if(!StringUtils.isBlank(REDIS_ALLNUMBER_CYCLE_Minute1)){
                    long number=Long.parseLong(REDIS_ALLNUMBER_CYCLE_Minute1);
                    if(number<=20000){
                        number=20000;
                    }
                    setAllNumber(redisKey,number);
                }

                break;
            case Minute5:
                if(!StringUtils.isBlank(REDIS_ALLNUMBER_CYCLE_Minute5)){
                    long number=Long.parseLong(REDIS_ALLNUMBER_CYCLE_Minute5);
                    if(number<=20000){
                        number=20000;
                    }
                    setAllNumber(redisKey,number);
                }
                break;
            case Minute15:
                if(!StringUtils.isBlank(REDIS_ALLNUMBER_CYCLE_Minute15)){
                    long number=Long.parseLong(REDIS_ALLNUMBER_CYCLE_Minute15);
                    if(number<=20000){
                        number=20000;
                    }
                    setAllNumber(redisKey,number);
                }
                break;
            case Minute30:
                if(!StringUtils.isBlank(REDIS_ALLNUMBER_CYCLE_Minute30)){
                    long number=Long.parseLong(REDIS_ALLNUMBER_CYCLE_Minute30);
                    if(number<=20000){
                        number=20000;
                    }
                    setAllNumber(redisKey,number);
                }
                break;
            case Hour1:
                if(!StringUtils.isBlank(REDIS_ALLNUMBER_CYCLE_Hour1)){
                    long number=Long.parseLong(REDIS_ALLNUMBER_CYCLE_Hour1);
                    if(number<=20000){
                        number=20000;
                    }
                    setAllNumber(redisKey,number);
                }
                break;
//
//            case DayCandle:
//                if(!StringUtils.isBlank(REDIS_ALLNUMBER_CYCLE_DayCandle)){
//                    long number=Long.parseLong(REDIS_ALLNUMBER_CYCLE_DayCandle);
//                    if(number<=20000){
//                        number=20000;
//                    }
//                    setAllNumber(redisKey,number);
//                }
//                break;
//            case Week:
//                if(!StringUtils.isBlank(REDIS_ALLNUMBER_CYCLE_Week)){
//                    long number=Long.parseLong(REDIS_ALLNUMBER_CYCLE_Week);
//                    if(number<=20000){
//                        number=20000;
//                    }
//                    setAllNumber(redisKey,number);
//                }
//                break;
//            case Month:
//                if(!StringUtils.isBlank(REDIS_ALLNUMBER_CYCLE_Month)){
//                    long number=Long.parseLong(REDIS_ALLNUMBER_CYCLE_Month);
//                    if(number>1000){
//                        setAllNumber(redisKey,number);
//                    }
//                }
//                break;
//            case Year:
//                if(!StringUtils.isBlank(REDIS_ALLNUMBER_CYCLE_Year)){
//                    long number=Long.parseLong(REDIS_ALLNUMBER_CYCLE_Year);
//                    if(number>100){
//                        setAllNumber(redisKey,number);
//                    }
//                }
//                break;
        }
    }

    private void setAllNumber(String redisKey,long number){
        try{
            long allNumber=jredisUtil.zcard(redisKey);
            if(allNumber>number){
                jredisUtil.zrangeAndDelete(redisKey, number, allNumber);
            }
        }catch (Exception ex){
            logger.error("清理redis 总数目失败：redisKey="+redisKey+"总条目为"+number);
        }
    }
    @Override
    public  void insertRedisSnapshotAll(List<Snapshot> snapshotList) {
        try{

            if(null!=snapshotList){
                for(Snapshot temp:snapshotList){

                    String redisKey=SJSMinTimeUtil.getRedisKeyAll(temp.getExchange());
                    double  score=DateUtils.getTickTime(temp.getMinTime(), "yyyyMMddHHmmss");
                    jredisUtil.zadd(redisKey, score, JSON.toJSONString(temp),172800);//缓存2天数据
                }

            }

        }catch (Exception ex){
            logger.error("insertRedisSnapshotAll信息异常  {}"+ex.getMessage());
        }
    }

    @Override
    public  synchronized Set<String>   getSnapshotFromRedisAllAndDelete(String exchange){
        Set<String> temp=null;
        if(!StringUtils.isBlank(exchange)){
            String redisKey=SJSMinTimeUtil.getRedisKeyAll(exchange);
            if(!StringUtils.isBlank(redisKey)){
                temp= jredisUtil.zrangeAndDelete(redisKey, 0, 10);
            }
        }
        return temp;
    }



        @Override
        public List<Snapshot> getMultiDaySnapshot(TradeAmountReq tradeAmountReq) {
            List<Snapshot>  snapShotList=null;
            try{
                if(null!=tradeAmountReq) {
                    if ("SJS".equalsIgnoreCase(tradeAmountReq.getExchange())) {
                        snapShotList = sjsCandleService.getMultiDaySnapshotRedis(tradeAmountReq);
                    } else if ("NJS".equalsIgnoreCase(tradeAmountReq.getExchange())) {
                        snapShotList = njsCandleService.getMultiDaySnapshotRedis(tradeAmountReq);
                    } else if ("LIFFE".equalsIgnoreCase(tradeAmountReq.getExchange())) {
                        snapShotList = otherHQService.getMultiDaySnapshotRedis(tradeAmountReq);
                    }
                }
            }catch (Exception ex){

                logger.error("getMultiDaySnapshot信息异常  {}"+ex.getMessage());
            }

            return snapShotList;
        }


    @Override
    public List getMultiDaySnapshotFormate(TradeAmountReq tradeAmountReq) {

        List  snapShotList=null;
        try{
            if(null!=tradeAmountReq) {
                if ("SJS".equalsIgnoreCase(tradeAmountReq.getExchange())) {
                    snapShotList = sjsCandleService.getMultiDaySnapshotRedisFormate(tradeAmountReq);
                } else if ("NJS".equalsIgnoreCase(tradeAmountReq.getExchange())) {
                    snapShotList = njsCandleService.getMultiDaySnapshotRedisFormate(tradeAmountReq);
                }
            }
        }catch (Exception ex){

            logger.error("getMultiDaySnapshotRedisFormate信息异常  {}"+ex.getMessage());
        }

        return snapShotList;
    }

    @Override
    public List<OwnProduct> queryOwnProductByUserId(Long userId) {
        return ownProductService.query(userId);
    }

    @Override
    public int insertOwnProduct(OwnProduct ownProduct) {
        int result=0;
        try{
            if(null!=ownProduct){
                result=ownProductService.insert(ownProduct);
            }
        }catch (Exception ex){

            logger.error("insertOwnProduct信息异常  {}"+ex.getMessage());
        }

        return result;
    }

    @Override
    public int updateOwnProduct(List<OwnProduct> ownProduct) {
        int result=0;
        try{
            if(null!=ownProduct){
                result= ownProductService.update(ownProduct);
            }
        }catch (Exception ex){

            logger.error("updateOwnProduct信息异常  {}"+ex.getMessage());
        }

        return result;
    }

    @Override
    public int deleteOwnProduct(List<OwnProduct> list) {

        int result=0;
        try{
            if(null!=list){
                result=ownProductService.delete(list);
            }
        }catch (Exception ex){

            logger.error("deleteOwnProduct信息异常  {}"+ex.getMessage());
        }

        return result;
    }

    @Override
    public int deleteOwnProduct(String userId, String ownProductId) {

        int result=0;
        try{
            if(!StringUtils.isBlank(userId)){
                result=ownProductService.delete(userId, ownProductId);
            }
        }catch (Exception ex){

            logger.error("deleteOwnProduct信息异常  {}"+ ex.getMessage());
        }

        return result;
    }

    @Override
    public List wizard(String field) {
        List result=null;
        try{
            if(!StringUtils.isBlank(field)){
                result = ownProductService.wizard(field);
            }
        }catch (Exception ex){

            logger.error("wizard信息异常  {}"+ex.getMessage());
        }

      return result;
    }

    @Override
    public Snapshot queryTicker(String exchange, String prodCode) {

        Snapshot snapshot=null;
        try{
            if(!StringUtils.isBlank(exchange)&&!StringUtils.isBlank(prodCode)){
                if ("SJS".equalsIgnoreCase(exchange)) {
                    snapshot = sjsSnapshotService.trick(prodCode+"."+"SJS");
                } else if ("NJS".equalsIgnoreCase(exchange)) {
                    snapshot = njsSnapshotService.trick(prodCode+"."+"NJS");
                }
            }
        }catch (Exception ex){

            logger.error("queryTicker信息异常  {}"+ex.getMessage());
        }

      return  snapshot;
    }

    @Override
    public List queryTickerFormate(SnapshotReq req) {
        Snapshot snapshot=queryTicker(req.getExchange(), req.getProdCode());
        List listAll=new ArrayList();
        LinkedMap map=new LinkedMap();
        SnapshotTrickRes res =new SnapshotTrickRes();
        DozerMapperSingleton.getInstance().map(snapshot, res);
        if(null!=snapshot){
            if(StringUtils.isBlank(req.getRetParaKeys())){
                map.put("ret", DozerMapperSingleton.fillProperty(res));
            }else{
                map.put("ret", DozerMapperSingleton.fillProperty(res,req.getRetParaKeys()));
            }

            DozerMapperSingleton.setObjParaKeys(map, req.getObjParaKeys(), snapshot);
        }
        listAll.add(map);
        return  listAll;
    }


    @Override
    public void redisCleanHistory(String exchange,String prodCode,String strCycle,String miniTime) {

        CandleCycle cycle=CandleUtils.mapCycle.get(strCycle);
        HQDataInit hqDataInit=(HQDataInit) SpringUtil.getBean("hqDataInit");
        hqDataInit.redisClearHistory(exchange,prodCode,cycle,miniTime);

    }

    @Override
    public void setMaster(String appName,String status) {
        jredisUtil.set(appName,status);
    }

    public  void   redisCleanLast(String exchange){
        HQDataInit hqDataInit=(HQDataInit) SpringUtil.getBean("hqDataInit");
        hqDataInit.redisClear(exchange);
    }


}
