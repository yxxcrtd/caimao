package com.caimao.hq.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.api.service.IOtherHQService;
import com.caimao.hq.dao.OtherHQDao;
import com.caimao.hq.utils.*;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service("otherHQService")
public class OtherHQServiceImpl extends CandleUtils implements IOtherHQService {

    /**
     * Log
     */
    private Logger logger = LoggerFactory.getLogger(OtherHQServiceImpl.class);

    /**
     * 日K
     */
    @Value("${url.otherCandle.kline.DayCandle}")
    protected String URL_OTHER_CANDLE_KLINE_DAY_CANDLE;

    /**
     * 分时
     */
    @Value("${url.otherCandle.kline.min}")
    protected String URL_OTHER_CANDLE_KLINE_MIN;

    /**
     * 5日分时
     */
    @Value("${url.otherCandle.kline.min.Day5}")
    protected String URL_OTHER_CANDLE_KLINE_MIN_DAY5;

    /**
     * Ticker
     */
    @Value("${url.otherCandle.ticker}")
    protected String URL_OTHER_CANDLE_TICKER;

    @Autowired
    private OtherHQDao otherHQDao;

    @Autowired
    private IHQService hqService;

    @Autowired
    private JRedisUtil jredisUtil;

    @Autowired
    private GJSProductUtils gjsProductUtils;

    @Override
    public int insertDB(OtherCandle candle) {
        return otherHQDao.insertDB(candle);
    }

    @Override
    public int insertBatchDB(List<OtherCandle> candleList) {
        return otherHQDao.insertBatchDB(candleList);
    }

    @Override
    public OtherCandle queryLastFromRedis(CandleReq candleReq) {
        String strCycle = candleReq.getCycle();
        CandleCycle cycle = null;
        if (!StringUtils.isBlank(strCycle)) {
            cycle = mapCycle.get(strCycle);
            if (null == cycle) {
                throw new RuntimeException("查询周期不能为空！");
            }
        }
        return queryRedis(candleReq.getExchange(), candleReq.getProdCode(), cycle);
    }

    private OtherCandle queryRedis(String exchange, String code, CandleCycle cycle) {
        OtherCandle candleRedis = null;
        String redisKey = MinTimeUtil.getRedisKey(cycle, exchange, code);
        if (!StringUtils.isBlank(redisKey)) {
            candleRedis = getValueFromRedis(redisKey);
        }
        return candleRedis;
    }

    private OtherCandle getValueFromRedis(String strRedisKey) {
        OtherCandle candle = null;
        try {
            if (!StringUtils.isBlank(strRedisKey)) {
                Object redisObj = jredisUtil.get(strRedisKey);
                if (null != redisObj) {
                    candle = JSON.parseObject((String) redisObj, OtherCandle.class);
                }
            }
        } catch (Exception e) {
            logger.error("解析错误：" + e);
            candle = null;
        }
        return candle;
    }

    /**
     * 日K
     */
    @Override
    public void updateDayOtherHQToRedis() throws IOException {
        // AU
        String auBody = Jsoup.connect(URL_OTHER_CANDLE_KLINE_DAY_CANDLE + "&goodsId=AU&num=1").ignoreContentType(true).execute().body();
        WyHqMonthRes auObject = JSON.parseObject(auBody, WyHqMonthRes.class);
        if ("200".equals(auObject.getRetCode())) {
            List list = auObject.getRet();
            String[] str = list.get(0).toString().split(",");
            OtherCandle otherCandle = new OtherCandle();
            otherCandle.setProdCode("AU");
            otherCandle.setProdName("伦敦金(美元/盎司)");
            otherCandle.setRedisKey("LIFFEAUDayCandle");
            OtherHQServiceHelper.setOtherCandleDayData(otherCandle, str);
            jredisUtil.set("LIFFEAUDayCandle", JSON.toJSONString(otherCandle));
        }
        logger.info(" AU 日K 更新完成：" + String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()));

        // AG
        String agBody = Jsoup.connect(URL_OTHER_CANDLE_KLINE_DAY_CANDLE + "&goodsId=AG&num=1").ignoreContentType(true).execute().body();
        WyHqMonthRes agObject = JSON.parseObject(agBody, WyHqMonthRes.class);
        if ("200".equals(agObject.getRetCode())) {
            List list = agObject.getRet();
            String[] str = list.get(0).toString().split(",");
            OtherCandle otherCandle = new OtherCandle();
            otherCandle.setProdCode("AG");
            otherCandle.setProdName("伦敦银(美元/盎司)");
            otherCandle.setRedisKey("LIFFEAGDayCandle");
            OtherHQServiceHelper.setOtherCandleDayData(otherCandle, str);
            jredisUtil.set("LIFFEAGDayCandle", JSON.toJSONString(otherCandle));
        }
        logger.info(" AG 日K 更新完成：" + String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()));
    }

    /**
     * 日K 历史
     */
    @Override
    public void updateDayHistoryOtherHQToRedis() throws IOException {
        // AU
        String auBody = Jsoup.connect(URL_OTHER_CANDLE_KLINE_DAY_CANDLE + "&goodsId=AU&num=10000").ignoreContentType(true).execute().body();
        WyHqMonthRes auObject = JSON.parseObject(auBody, WyHqMonthRes.class);
        if ("200".equals(auObject.getRetCode())) {
            List list = auObject.getRet();
            int length = list.size();
            logger.info(" AU总记录数：" + length);
            for (int i = 0; i < length; i++) {
                String[] str = list.get(i).toString().split(",");
                OtherCandle otherCandle = new OtherCandle();
                OtherHQServiceHelper.setOtherCandleDayData(otherCandle, str);
                otherCandle.setProdCode("AU");
                hqService.insertRedisCandleHistory(CandleCycle.DayCandle, otherCandle);
            }
            logger.info(" AU 日K 历史 Redis 批量写入成功！");
        }

        // AG
        String agBody = Jsoup.connect(URL_OTHER_CANDLE_KLINE_DAY_CANDLE + "&goodsId=AG&num=10000").ignoreContentType(true).execute().body();
        WyHqMonthRes agObject = JSON.parseObject(agBody, WyHqMonthRes.class);
        if ("200".equals(agObject.getRetCode())) {
            List list = agObject.getRet();
            int length = list.size();
            logger.info(" AG总记录数：" + length);
            for (int i = 0; i < length; i++) {
                String[] str = list.get(i).toString().split(",");
                OtherCandle otherCandle = new OtherCandle();
                OtherHQServiceHelper.setOtherCandleDayData(otherCandle, str);
                otherCandle.setProdCode("AG");
                hqService.insertRedisCandleHistory(CandleCycle.DayCandle, otherCandle);
            }
            logger.info(" AG 日K 历史 Redis 批量写入成功！");
        }
    }

    /**
     * 分时
     */
    @Override
    public void updateMinOtherHQToRedis() throws IOException {
        // AU
        String auNow = String.format("%1$tY%1$tm%1$td%1$tH%1$tM", new Date());
        String auBody = Jsoup.connect(URL_OTHER_CANDLE_KLINE_MIN + "&goodsId=AU&num=1&date=" + DateUtils.addMinue(auNow, -2)).ignoreContentType(true).execute().body();
        JSONObject auJsonObject = JSONObject.parseObject(auBody);
        if ("200".equals(auJsonObject.get("retCode")) && !"".equals(auJsonObject.get("retCode"))) {
            WyHqMonthRes object = JSON.parseObject(auJsonObject.get("ret").toString(), WyHqMonthRes.class);
            List list = object.getData();
            String[] str = list.get(0).toString().split(",");
            OtherCandle otherCandle = new OtherCandle();
            otherCandle.setOptDate(auNow);
            OtherHQServiceHelper.setOtherCandleMinData(otherCandle, str);
            otherCandle.setProdCode("AU");
            otherCandle.setProdName("伦敦金(美元/盎司)");
            otherCandle.setRedisKey("LIFFEAUMinute1");
            jredisUtil.set("LIFFEAUMinute1", JSON.toJSONString(otherCandle));
            logger.info(" AU 分时 更新完成：" + String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()));
            hqService.insertRedisCandleHistory(CandleCycle.Minute1, otherCandle);
            logger.info(" AU 分时历史 更新完成：" + String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()));
        }

        // AG
        String agNow = String.format("%1$tY%1$tm%1$td%1$tH%1$tM", new Date());
        String agBody = Jsoup.connect(URL_OTHER_CANDLE_KLINE_MIN + "&goodsId=AG&num=1&date=" + DateUtils.addMinue(auNow, -2)).ignoreContentType(true).execute().body();
        JSONObject agJsonObject = JSONObject.parseObject(agBody);
        if ("200".equals(agJsonObject.get("retCode")) && !"".equals(agJsonObject.get("retCode"))) {
            WyHqMonthRes object = JSON.parseObject(agJsonObject.get("ret").toString(), WyHqMonthRes.class);
            List list = object.getData();
            String[] str = list.get(0).toString().split(",");
            OtherCandle otherCandle = new OtherCandle();
            otherCandle.setOptDate(agNow);
            OtherHQServiceHelper.setOtherCandleMinData(otherCandle, str);
            otherCandle.setProdCode("AG");
            otherCandle.setProdName("伦敦银(美元/盎司)");
            otherCandle.setRedisKey("LIFFEAGMinute1");
            jredisUtil.set("LIFFEAGMinute1", JSON.toJSONString(otherCandle));
            logger.info(" AG 分时 更新完成：" + String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()));
            hqService.insertRedisCandleHistory(CandleCycle.Minute1, otherCandle);
            logger.info(" AG 分时历史 更新完成：" + String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()));
        }
    }

    /**
     * 5日分时
     */
    @Override
    public void updateDay5OtherHQToRedis() throws IOException {
        // AU
        String auNow = String.format("%1$tY%1$tm%1$td%1$tH%1$tM", new Date());
        String auBody = Jsoup.connect(URL_OTHER_CANDLE_KLINE_MIN_DAY5 + "&goodsId=AU" + DateUtils.addMinue(auNow, -2)).ignoreContentType(true).execute().body();
        JSONObject auJsonObject = JSONObject.parseObject(auBody);
        if ("200".equals(auJsonObject.get("retCode")) && !"".equals(auJsonObject.get("retCode"))) {
            WyHqMonthRes object = JSON.parseObject(auJsonObject.get("ret").toString(), WyHqMonthRes.class);
            List list = object.getData();
            String[] str = list.get(0).toString().split(",");
            OtherCandle otherCandle = new OtherCandle();
            otherCandle.setOptDate(auNow);
            OtherHQServiceHelper.setOtherCandleMinData(otherCandle, str);
            otherCandle.setProdCode("AU");
            otherCandle.setProdName("伦敦金(美元/盎司)");
            otherCandle.setRedisKey("LIFFEAUMinute1");
            hqService.insertRedisCandleHistory(CandleCycle.Minute1, otherCandle);
            logger.info(" AU 5日分时 更新完成：" + String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()));
        }

        // AG
        String agNow = String.format("%1$tY%1$tm%1$td%1$tH%1$tM", new Date());
        String agBody = Jsoup.connect(URL_OTHER_CANDLE_KLINE_MIN_DAY5 + "&goodsId=AG" + DateUtils.addMinue(auNow, -2)).ignoreContentType(true).execute().body();
        JSONObject agJsonObject = JSONObject.parseObject(agBody);
        if ("200".equals(agJsonObject.get("retCode")) && !"".equals(agJsonObject.get("retCode"))) {
            WyHqMonthRes object = JSON.parseObject(agJsonObject.get("ret").toString(), WyHqMonthRes.class);
            List list = object.getData();
            String[] str = list.get(0).toString().split(",");
            OtherCandle otherCandle = new OtherCandle();
            otherCandle.setOptDate(agNow);
            OtherHQServiceHelper.setOtherCandleMinData(otherCandle, str);
            otherCandle.setProdCode("AG");
            otherCandle.setProdName("伦敦银(美元/盎司)");
            otherCandle.setRedisKey("LIFFEAGMinute1");
            hqService.insertRedisCandleHistory(CandleCycle.Minute1, otherCandle);
            logger.info(" AG 5日分时 更新完成：" + String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()));
        }
    }

    /**
     * 分时
     */
    @Override
    public void updateSnapshotOtherHQToRedis() throws IOException {
        // AU
        String auBody = Jsoup.connect(URL_OTHER_CANDLE_TICKER + "&goodsId=AU").ignoreContentType(true).execute().body();
        JSONObject auJsonObject = JSONObject.parseObject(auBody);
        if ("200".equals(auJsonObject.get("retCode"))) {
            JSONObject auJsonObject1 = JSONObject.parseObject(auJsonObject.get("ret").toString());
            OtherSnapshot otherSnapshot = new OtherSnapshot();
            otherSnapshot.setProdCode("AU");
            otherSnapshot.setProdName("伦敦金(美元/盎司)");
            otherSnapshot.setRedisKey("LIFFEAUDayCandle");
            OtherHQServiceHelper.setOtherSnapshotData(otherSnapshot, auJsonObject1);

            SnapshotFormate.formateSnapshot(CandleCycle.Snap, otherSnapshot);//格式化时间和rediskey
            String jsonString = JSON.toJSONString(otherSnapshot);
            jredisUtil.setex(otherSnapshot.getRedisKey(), jsonString, 0);

            hqService.insertRedisSnapshotHistory(otherSnapshot);//插入成交数量数据到Redis
            hqService.insertRedisMultiDayHistory(otherSnapshot);//保存交易日
        }
        logger.info(" AU 分时 更新完成：" + String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()));

        // AG
        String agBody = Jsoup.connect(URL_OTHER_CANDLE_TICKER + "&goodsId=AG").ignoreContentType(true).execute().body();
        JSONObject agJsonObject = JSONObject.parseObject(agBody);
        if ("200".equals(agJsonObject.get("retCode"))) {
            JSONObject agJsonObject1 = JSONObject.parseObject(agJsonObject.get("ret").toString());
            OtherSnapshot otherSnapshot = new OtherSnapshot();
            otherSnapshot.setProdCode("AG");
            otherSnapshot.setProdName("伦敦银(美元/盎司)");
            otherSnapshot.setRedisKey("LIFFEAGDayCandle");
            OtherHQServiceHelper.setOtherSnapshotData(otherSnapshot, agJsonObject1);

            SnapshotFormate.formateSnapshot(CandleCycle.Snap, otherSnapshot);//格式化时间和rediskey
            String jsonString = JSON.toJSONString(otherSnapshot);
            jredisUtil.setex(otherSnapshot.getRedisKey(), jsonString, 0);

            hqService.insertRedisSnapshotHistory(otherSnapshot);//插入成交数量数据到Redis
            hqService.insertRedisMultiDayHistory(otherSnapshot);//保存交易日
        }
        logger.info(" AG 分时 更新完成：" + String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()));
    }




    @Override
    public List<Snapshot> queryByExchange(String financeMic) {
        List<Snapshot> list = null;
        String productAll=GJSProductUtils.mapProduct.get(financeMic);
        if(!StringUtils.isBlank(productAll)){
            list= queryByProdCode(productAll);
        }
        return list;
    }

    public List<Snapshot> queryByProdCode(String prodCode) {

        Map<String,Snapshot> resultMap=new HashMap();
        String[] prodCodArray =getProdCode(prodCode);
        if(null==prodCodArray||prodCodArray.length>200){
            throw new RuntimeException("单个行情查询，传入产品代码为Null或者传入产品个数大于200个");
        }

        List<Snapshot> resultlist=new ArrayList();
        List<String> redisStr=null;
        String[] redisKeyArray = new String[prodCodArray.length];
        String redisKey="";
        for(int i=0;i<prodCodArray.length;i++){
            if(!StringUtils.isBlank(prodCodArray[i])){
                redisKey = MinTimeUtil.getRedisKey(CandleCycle.Snap, prodCodArray[i]);
                redisKeyArray[i]=redisKey;
            }
        }
        redisStr=jredisUtil.mget(redisKeyArray);
        gjsProductUtils.convertStrToSnapObject(redisStr, resultlist);
        //gjsProductUtils.convertMapToList(resultMap, prodCodArray, resultlist);
        return resultlist;
    }

    private String[] getProdCode(String prodCode){
        String[] prodCodArray=null;
        if(!StringUtils.isBlank(prodCode)){
            prodCodArray=prodCode.split(",");
        }
        return prodCodArray;
    }


    public List getMultiDaySnapshotRedis(TradeAmountReq tradeAmountReq) {
        List listAll=new ArrayList();
        Set<String> dateSet=selectDateNear5(tradeAmountReq);
        List<String> dateList=new ArrayList();
        dateList.addAll(dateSet);
        Collections.sort(dateList);//升序，小日期在前面
        if(null!=dateSet){
            CandleReq candleReq=new CandleReq();
            candleReq.setExchange(tradeAmountReq.getExchange());
            candleReq.setProdCode(tradeAmountReq.getProdCode());
            if(StringUtils.isBlank(candleReq.getCycle())){
                candleReq.setCycle("1");
            }
            for(int i=0;i<dateList.size();i++){

                if(!StringUtils.isBlank(dateList.get(i))){

                    if(i==dateList.size()-1){
                        setSelectSnapshotFiveDate(tradeAmountReq, dateList.get(i),null);
                    }else{
                        setSelectSnapshotFiveDate(tradeAmountReq, dateList.get(i), dateList.get(i));
                    }

                    candleReq.setBeginDate(tradeAmountReq.getBeginDate());
                    candleReq.setEndDate(tradeAmountReq.getEndDate());
                    List  list=hqService.queryCandleRedisHistory(candleReq);
                    if(null!=list&&list.size()>0){
                        listAll.addAll(list);
                    }
                }
            }
        }
        return listAll;
    }

    private Set<String> selectDateNear5(TradeAmountReq tradeAmountReq) {
        return hqService.queryRedisMultiDayHistory(tradeAmountReq);
    }

    //设置分时线历史查询的跨天日期     南交所：早上九点到次日六点，算做今天的日分时
    private void setSelectSnapshotFiveDate(TradeAmountReq tradeAmountReq, String nowDate,String afterDate) {
        if (null != tradeAmountReq && !StringUtils.isBlank(nowDate)) {
            StringBuffer beginStrBuffer = new StringBuffer();
            StringBuffer endStrBuffer = new StringBuffer();
            beginStrBuffer.append(nowDate);
            beginStrBuffer.append("0600");
            if(!StringUtils.isBlank(afterDate)){
                endStrBuffer.append(afterDate);
            }else{
                //择中办法，20天目的是为了查询当前时间以后的所有数据，如果只是+1的话遇到节假日或者周末可能就会错。
                endStrBuffer.append(DateUtils.addDay(nowDate, 20, "yyyyMMdd"));
            }
            endStrBuffer.append("2359");
            tradeAmountReq.setBeginDate(beginStrBuffer.toString());
            tradeAmountReq.setEndDate(endStrBuffer.toString());
        } else {
            tradeAmountReq = null;
        }
    }

}
