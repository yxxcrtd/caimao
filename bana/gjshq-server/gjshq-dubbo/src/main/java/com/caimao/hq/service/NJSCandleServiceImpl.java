
package com.caimao.hq.service;

import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.api.service.IGjsErrorService;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.api.service.INJSCandleService;
import com.caimao.hq.dao.NJSCandleDao;
import com.caimao.hq.utils.*;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author yzc
 */
@Service("njsCandleService")
public class NJSCandleServiceImpl  extends CandleUtils implements INJSCandleService {
    private Logger logger = LoggerFactory.getLogger(NJSCandleServiceImpl.class);
    @Autowired
    private NJSCandleDao njsCandleDao;
    @Autowired
    public HolidayUtil holidayUtil;
    @Autowired
    private GJSProductUtils gjsProductUtils;
    @Autowired
    private JRedisUtil jredisUtil;
    @Autowired
    private IHQService hqService;
    @Autowired
    private IGjsErrorService gjsErrorService;
    //private String cycleTimeDate="";//根据周期格式化的时间
    //private String redisKey="";
    private double d = 0.0001;//doubule 精度
    // private CandleCycle cycle=null;


    private List<Product> getProduct(String finance_mic) {

        List<Product> listProduct = null;
        if (!StringUtils.isBlank(finance_mic)) {

            listProduct = gjsProductUtils.getProductList(finance_mic);
        }
        return listProduct;

    }
    public List getMultiDaySnapshotRedis(TradeAmountReq tradeAmountReq){

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
                        setSelectSnapshotFiveDate(tradeAmountReq, dateList.get(i), dateList.get(i + 1));
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


    public List getMultiDaySnapshotRedisFormate(TradeAmountReq tradeAmountReq){

        List listAll=new ArrayList();

        Set<String> dateSet=selectDateNear5(tradeAmountReq);
        List<String> dateList=new ArrayList();
        dateList.addAll(dateSet);
        Collections.sort(dateList);//升序，小日期在前面

        if(null!=dateSet){
            CandleReq candleReq=new CandleReq();
            candleReq.setExchange(tradeAmountReq.getExchange());
            candleReq.setProdCode(tradeAmountReq.getProdCode());
            if(StringUtils.isBlank(tradeAmountReq.getCycle())){
                candleReq.setCycle("1");
            }else{
                candleReq.setCycle(tradeAmountReq.getCycle());
            }
            //用来查询日 K
            CandleReq candleReqDay=new CandleReq();
            candleReqDay.setCycle("6");
            candleReqDay.setExchange(tradeAmountReq.getExchange());
            candleReqDay.setProdCode(tradeAmountReq.getProdCode());
            for(int i=0;i<dateList.size();i++){

                if(!StringUtils.isBlank(dateList.get(i))){
                        LinkedMap map=new LinkedMap();
                        //南交所多日分时，从当前交易日到下个交易日，上金所从昨天开始算
                        if(i==dateList.size()-1){
                            setSelectSnapshotFiveDate(tradeAmountReq, dateList.get(i),null);
                        }else{
                            setSelectSnapshotFiveDate(tradeAmountReq, dateList.get(i), dateList.get(i + 1));
                        }
                        //查询分时数据
                        candleReq.setBeginDate(tradeAmountReq.getBeginDate());
                        candleReq.setEndDate(tradeAmountReq.getEndDate());
                        List  list=hqService.queryCandleRedisHistory(candleReq);
                        List convertList=new ArrayList();
                        try {
                            DozerMapperSingleton.listCopy(list, convertList, "com.caimao.hq.api.entity.SnapshotResFormate");
                        } catch (Exception e) {
                            logger.error("getMultiDaySnapshotRedisFormate CandleResFormate 异常: "+e.getMessage());
                        }
                        if(null!=convertList&&convertList.size()>0){
                            //查询日K，设置昨收
                            candleReqDay.setBeginDate(tradeAmountReq.getBeginDate().substring(0, 8) + "0000");
                            candleReqDay.setEndDate(tradeAmountReq.getBeginDate().substring(0, 8) + "0000");
                            List<Candle> candles=hqService.queryCandleRedisHistory(candleReqDay);
                            Candle  temp=null;
                            if(candles==null||candles.size()<1){
                                temp=hqService.queryLastCandle(candleReqDay);
                                if(null!=temp){
                                    if(!candleReqDay.getBeginDate().equalsIgnoreCase(temp.getMinTime())){
                                        temp=null;
                                    }
                                }
                            }else{
                                temp=candles.get(0);
                            }
                            //组装数据
                            if(null==temp){
                                temp=new NJSCandle();
                            }
                            //过滤掉属性名,只取值到list

                            if(StringUtils.isBlank(tradeAmountReq.getRetParaKeys())){
                                map.put("ret", DozerMapperSingleton.fillProperty(convertList));
                            }else{
                                map.put("ret", DozerMapperSingleton.fillProperty(convertList, tradeAmountReq.getRetParaKeys()));
                            }
                            DozerMapperSingleton.setObjParaKeys(map,tradeAmountReq.getObjParaKeys(),temp);
                            listAll.add(map);
                        }
                    }

                }
            }
        Collections.reverse(listAll);
        return listAll;
    }

    @Override
    public void insertPatch(List<Candle> candleList) {
        if(null!=candleList){


            List insertList=new ArrayList();
            if(candleList.size()>1450){
                logger.error("填充数据异常：需要添加的数据超过1000条，可能数据有异常"+candleList);
                return;
            }
            for(Candle njsCandle:candleList){

                insertList.add(njsCandle);
                if(insertList.size()==100){

                    njsCandleDao.insertBatch(insertList);
                    insertList.clear();
                }
            }
            njsCandleDao.insertBatch(insertList);
        }
    }

    private void fillRedisKeyFromProductList(List redisKeyList, List<Product> listProduct) {

        if (null != listProduct) {

            if (null == redisKeyList) {

                redisKeyList = new ArrayList();

            }
            for (Product product : listProduct) {

                fillRedisKeyFromProduct(redisKeyList, product);

            }

        }

    }

    public void saveRedis(String redisKey, Candle redisCandle) {
        if (null != redisCandle) {
            String jsonString = JSON.toJSONString(redisCandle);
            jredisUtil.setex(redisKey, jsonString, 0);
        }
    }



    public List<Candle> queryDB(CandleReq candleReq) {

        return njsCandleDao.selectList(candleReq);
    }

    @Override
    public Candle queryRedis(CandleReq candleReq) {
        String strCycle = candleReq.getCycle();
        CandleCycle cycle = null;
        Candle candle = null;
        if (!StringUtils.isBlank(strCycle)) {
            cycle = mapCycle.get(strCycle);
            if (null == cycle) {
                logger.error("查询周期错误,不支持该周期,cycle is null");
                throw new RuntimeException("查询周期错误,不支持该周期,cycle is null");
            }
            candle = queryRedis(candleReq.getExchange(), candleReq.getProdCode(), cycle);
            if(null==candle){//Redis查询不到，就查询DB,然后同步到Redis
                candle= queryDB(candleReq).get(0);
                if(null!=candle){
                    saveRedis(candle.getRedisKey(),candle);
                }
            }
        }
        return candle;
    }

    public Candle queryRedis(Map<String, String> mapPara) {
        String strCycle = mapPara.get("cycle");
        CandleCycle cycle = null;
        Candle candle = null;
        if (!StringUtils.isBlank(strCycle)) {
            cycle = mapCycle.get(strCycle);
            if (null == cycle) {
                logger.error("查询周期错误,不支持该周期,cycle is null");
                throw new RuntimeException("查询周期错误,不支持该周期,cycle is null");
            }
            candle = queryRedis(mapPara.get("financeMic"), mapPara.get("prodCode"), cycle);
        }
        return candle;
    }


    private Map<String, String> getProdCode(String prodCode) {//避免重复的代码
        String[] prodCodArray = null;
        Map<String, String> mapPara = new HashMap<>();
        if (!StringUtils.isBlank(prodCode)) {
            prodCodArray = prodCode.split(",");
            for (String str : prodCodArray) {
                if (!StringUtils.isBlank(str)) {
                    mapPara.put(str, str);
                }
            }
        }
        return mapPara;
    }

    private Candle queryRedis(String financeMic, String prodCode, CandleCycle cycle) {
        NJSCandle candleRedis = null;
        String redisKey = MinTimeUtil.getRedisKey(cycle,financeMic, prodCode);
        if (!StringUtils.isBlank(redisKey)) {
            candleRedis = getValueFromRedis(redisKey);
        }
        return candleRedis;

    }

    private NJSCandle getValueFromRedis(String strRedisKey) {
        NJSCandle candle = null;
        try {
            if (!StringUtils.isBlank(strRedisKey)) {
                Object redisObj = jredisUtil.get(strRedisKey);
                if (null != redisObj) {

                    candle = JSON.parseObject((String) redisObj, NJSCandle.class);
                }
            }
        } catch (Exception ex) {
            logger.error("redis from string to obj error,不能解析为SJSCandle，strRedisKey=" + strRedisKey + ex.getMessage());
            candle = null;
        }
        return candle;
    }

    private NJSCandle queryDB(String financeMic, String prodCode, CandleCycle cycle) {

        return njsCandleDao.getPreCandl(cycle, financeMic, prodCode, null);
    }

    private void fillRedisKeyFromProduct(List redisKeyList, Product product) {

        if (null != product) {
            if (redisKeyList == null) {
                redisKeyList = new ArrayList();
            }
            Snapshot temp = new Snapshot();
            temp.setExchange(product.getExchange());
            temp.setProdCode(product.getProdCode());
            redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Minute1, temp));
            redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Snap, temp));
            redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.DayCandle, temp));
            redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Hour1, temp));
            redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Hour4, temp));
            redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Minute30, temp));
            redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Minute5, temp));
            redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Month, temp));
            redisKeyList.add(MinTimeUtil.getRedisKey(CandleCycle.Week, temp));
        }
    }


    @Transactional
    public synchronized void save(Snapshot baseSnapshot) {


        try {
            NJSSnapshot minute1 = (NJSSnapshot) baseSnapshot.clone();
            NJSSnapshot minute5 = (NJSSnapshot) baseSnapshot.clone();
            NJSSnapshot minute30 = (NJSSnapshot) baseSnapshot.clone();
            NJSSnapshot hour1 = (NJSSnapshot) baseSnapshot.clone();
            NJSSnapshot dayCandle = (NJSSnapshot) baseSnapshot.clone();
            NJSSnapshot week = (NJSSnapshot) baseSnapshot.clone();
            NJSSnapshot month = (NJSSnapshot) baseSnapshot.clone();
            NJSSnapshot year = (NJSSnapshot) baseSnapshot.clone();


            //格式化交易所的跨天日期,日以下周期的MinTime为用apdRecvTime
            week.setMinTime(week.getTradeDate()+week.getTradeTime());
            month.setMinTime(month.getTradeDate()+month.getTradeTime());
            year.setMinTime(year.getTradeDate()+year.getTradeTime());
            dayCandle.setMinTime(dayCandle.getTradeDate()+dayCandle.getTradeTime());

            save(CandleCycle.Minute1, minute1);
            save(CandleCycle.Minute5, minute5);
            save(CandleCycle.Minute30, minute30);
            save(CandleCycle.Hour1, hour1);
            save(CandleCycle.Week, week);
            save(CandleCycle.Month, month);
            save(CandleCycle.Year, year);
            save(CandleCycle.DayCandle, dayCandle);
        } catch (Exception e) {
            logger.error("生成K线异常"+e.getMessage()+"data="+baseSnapshot);
            throw new RuntimeException("生成K线异常" + e.getMessage());

        }


    }




    @Override
    public Candle getPreCandl(CandleCycle cycle, String finance_mic, String prod_code, String miniTime) {

        return njsCandleDao.getPreCandl(cycle, finance_mic, prod_code, miniTime);
    }

    private void save(CandleCycle cycle, Snapshot baseSnapshot) {

        if (null == cycle || null == baseSnapshot) {
            return;
        }
        SnapshotFormate.formateSnapshot(cycle, baseSnapshot);//格式化时间和rediskey

        //如果redis有对应的key
        if (isExistRedis(baseSnapshot.getRedisKey())) {

            existRedis(cycle, baseSnapshot);

        } else {
            notExistRedis(cycle, baseSnapshot);

        }

    }




    private void existRedisWeek(CandleCycle cycle, Snapshot baseSnapshot, NJSCandle redisCandle) {

        if (cycle == CandleCycle.Week) {//如果是周K
            long redisDatel = DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 8), "yyyyMMdd");
            long snapshotDatel = DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 8), "yyyyMMdd");

            int newWeek = Integer.parseInt(DateUtils.getWeekOfDate(baseSnapshot.getCycleTimeDate(), "yyyyMMddHHmm"));
            int oldWeek = Integer.parseInt(DateUtils.getWeekOfDate(redisCandle.getMinTime(), "yyyyMMddHHmm"));
            if (snapshotDatel >= redisDatel) {
                if (newWeek < oldWeek) {//说明是新的一周

                    NJSCandle redisCandleNew = new NJSCandle();
                    initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                    redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                    redisCandleNew.setStatus("0");

                    redisCandle.setClosePx(baseSnapshot.getPreclosePx());
                    redisCandle.setLastPx(redisCandle.getClosePx());
                    setPxChange(redisCandle, baseSnapshot);
                    setPxChangeRate(redisCandle, baseSnapshot);
                    redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价
                    redisCandleNew.setStatus("0");
                    saveDB(redisCandle);//把原来的Redis保存到数据库
                    saveRedis(baseSnapshot.getRedisKey(), redisCandleNew);
                } else {

                    update(cycle, redisCandle, baseSnapshot);//更新Redis
                    saveRedis(baseSnapshot.getRedisKey(), redisCandle);
                }
            }
        }
    }

    private void existRedisMonth(CandleCycle cycle, Snapshot baseSnapshot, NJSCandle redisCandle) {

        if (cycle == CandleCycle.Month) {//如果是月K
            long redisDatel=DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 6), "yyyyMM");
            long snapshotDatel=DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 6), "yyyyMM");
            if (snapshotDatel>redisDatel) {//说明是新的一月

                NJSCandle redisCandleNew = new NJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                redisCandleNew.setStatus("0");
                redisCandle.setClosePx(baseSnapshot.getPreclosePx());
                redisCandle.setLastPx(redisCandle.getClosePx());
                redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价
                setPxChange(redisCandle, baseSnapshot);
                setPxChangeRate(redisCandle, baseSnapshot);
                redisCandleNew.setStatus("0");

                saveDB(redisCandle);//把原来的Redis保存到数据库
                saveRedis(baseSnapshot.getRedisKey(), redisCandleNew);

            }else if(snapshotDatel==redisDatel){

                update(cycle, redisCandle, baseSnapshot);//更新Redis
                saveRedis(baseSnapshot.getRedisKey(), redisCandle);
            }
        }

    }


    private void existRedisYear(CandleCycle cycle, Snapshot baseSnapshot, NJSCandle redisCandle) {

        if (cycle == CandleCycle.Year) {//如果是年K
            long redisDatel=DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 4), "yyyy");
            long snapshotl=DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 4), "yyyy");
            if (snapshotl>redisDatel) {//说明是新的一年

                NJSCandle redisCandleNew = new NJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                redisCandleNew.setStatus("0");
                redisCandleNew.setOpenPx(baseSnapshot.getOpenPx());

                redisCandle.setSettle(baseSnapshot.getLastSettle());
                redisCandle.setClosePx(baseSnapshot.getPreclosePx());
                redisCandle.setLastPx(redisCandle.getClosePx());
                redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价
                redisCandleNew.setLastSettle(redisCandle.getSettle());
                redisCandleNew.setStatus("0");
                setPxChange(redisCandle, baseSnapshot);
                setPxChangeRate(redisCandle, baseSnapshot);


                saveDB(redisCandle);//把原来的Redis保存到数据库
                saveRedis(baseSnapshot.getRedisKey(), redisCandleNew);
            }else if(snapshotl==redisDatel){

                update(cycle, redisCandle, baseSnapshot);//更新Redis
                saveRedis(baseSnapshot.getRedisKey(), redisCandle);
            }
        }
    }

    private void existRedisDay(CandleCycle cycle, Snapshot baseSnapshot, NJSCandle redisCandle) {

        if (null!=redisCandle&&cycle == CandleCycle.DayCandle) {//如果是日

            long redisDatel=DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 8), "yyyyMMdd");
            long snapshotDatel=DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 8), "yyyyMMdd");

            if((snapshotDatel>redisDatel)){

                redisCandle.setSettle(baseSnapshot.getLastSettle());
                redisCandle.setClosePx(baseSnapshot.getPreclosePx());
                redisCandle.setLastPx(redisCandle.getClosePx());
                setPxChange(redisCandle, baseSnapshot);
                setPxChangeRate(redisCandle, baseSnapshot);

                NJSCandle redisCandleNew = new NJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                redisCandleNew.setMinTime(baseSnapshot.getCycleTimeDate());
                redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价
                redisCandleNew.setLastSettle(redisCandle.getSettle());
                redisCandleNew.setStatus("0");
                saveDB(redisCandle);//把原来的Redis保存到数据库
                saveRedis(redisCandleNew.getRedisKey(), redisCandleNew);

            } else if(redisDatel==snapshotDatel){

                update(cycle, redisCandle, baseSnapshot);//更新Redis
                saveRedis(redisCandle.getRedisKey(), redisCandle);
            }
        }
    }
    //设置价格涨跌  当前价-昨收
    @Override
    public   void  setPxChange(Candle candle,Snapshot baseSnapshot){
        if(null!=candle&&null!=baseSnapshot){

            if(candle.getPreClosePx()>0){
                candle.setPxChange(DoubleOperationUtil.sub(candle.getLastPx(), candle.getPreClosePx()));

            }

        }
    }

    //设置价格涨跌幅度   （当前价-昨收/昨收）*100%
    @Override
    public   void  setPxChangeRate(Candle candle,Snapshot baseSnapshot){
        if(null!=candle&&null!=baseSnapshot){

            if(candle.getPreClosePx()>0){
                candle.setPxChangeRate(DoubleOperationUtil.round(DoubleOperationUtil.div(candle.getPxChange(), candle.getPreClosePx(), 4) * 100, 2));
            }
        }
    }

    private void fillData(Candle redisCandle,long begin, long end,CandleCycle cycle,String formate){//补全丢失的数据，


    }
    //适合日K以下周期
    private void existRedisCommon(CandleCycle cycle, Snapshot baseSnapshot, NJSCandle redisCandle) {


        if (null != redisCandle) {

            long redisDatel=DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 12), "yyyyMMddHHmm");
            long snapshotl=DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 12), "yyyyMMddHHmm");
            if (redisDatel==snapshotl) {//如果跟Redis相等就修改,否则就添加
                update(cycle, redisCandle, baseSnapshot);//更新Redis
                saveRedis(baseSnapshot.getRedisKey(), redisCandle);
            } else if(snapshotl>redisDatel) {

                try{

                    fillData(redisCandle,snapshotl,redisDatel,cycle,"yyyyMMddHHmm");

                }catch (Exception ex){

                    logger.error("批量填充数据错误:"+ex.getMessage());
                }

                    //如果不等,新生成一个
                NJSCandle redisCandleNew = new NJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                redisCandleNew.setStatus("0");
                setPxChange(redisCandle, baseSnapshot);
                setPxChangeRate(redisCandle, baseSnapshot);
                redisCandleNew.setPreClosePx(redisCandle.getClosePx());
                saveDB(redisCandle);//把原来的Redis保存到数据库
                saveRedis(baseSnapshot.getRedisKey(), redisCandleNew);
            }

        } else {
            logger.error("根据rediskey 获取数据异常:");
        }

    }

    private void existRedis(CandleCycle cycle, Snapshot baseSnapshot) {

        NJSCandle redisCandle = getValueFromRedis(baseSnapshot.getRedisKey());
        if (cycle == CandleCycle.Week) {

            existRedisWeek(cycle, baseSnapshot, redisCandle);
        } else if (cycle == CandleCycle.Month) {
            existRedisMonth(cycle, baseSnapshot, redisCandle);
        } else if (cycle == CandleCycle.Year) {

            existRedisYear(cycle, baseSnapshot, redisCandle);

        } else if (cycle == CandleCycle.DayCandle) {
            existRedisDay(cycle, baseSnapshot, redisCandle);
        }else{
            existRedisCommon(cycle, baseSnapshot, redisCandle);
        }

    }

    private void fillCandleMinue1(List<String> miniTimeList, Candle redisCandle) {

        if (null != redisCandle && miniTimeList != null) {
            int length = miniTimeList.size();
            for (int i = 0; i < length; i++) {
                try {
                    Candle temp = (Candle) redisCandle.clone();
                    temp.setMinTime(miniTimeList.get(i));//设置分时K的交易时间
                    temp.setOpenPx(redisCandle.getClosePx());//开盘价等于上一分钟的收盘价
                    temp.setClosePx(redisCandle.getClosePx());// 开盘价=收盘价
                    temp.setHighPx(redisCandle.getClosePx());// 最高价=收盘价
                    temp.setLowPx(redisCandle.getClosePx());// 最高价=收盘价
                } catch (Exception ex) {
                    logger.error("自动填充行情分时数据异常：克隆redisCandle异常," + ex.getMessage() + ",redisCandle=" + redisCandle);
                    throw new RuntimeException("自动填充行情分时数据异常：克隆redisCandle异常," + ex.getMessage() + ",redisCandle=" + redisCandle);
                }
            }

        }
    }

    private void notExistRedis(CandleCycle cycle, Snapshot baseSnapshot) {

        //Redis没有当前品种的周期，就新建一个放入redis
        NJSCandle redisCandleNew = new NJSCandle();
        initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
        redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
        redisCandleNew.setStatus("0");
//        Candle candle = getPreCandl(cycle, redisCandleNew.getExchange(), redisCandleNew.getProdCode(), redisCandleNew.getMinTime());//设置收盘价
//        if (null != candle) {
//            redisCandleNew.setPreClosePx(candle.getClosePx());
//        }
        redisCandleNew.setPreClosePx(baseSnapshot.getPreclosePx());
        saveRedis(baseSnapshot.getRedisKey(), redisCandleNew);

    }

    private void initOpenPx(Candle redisCandleNew) {
        if (null != redisCandleNew) {

            Candle preCandl = getPreCandl(redisCandleNew.getCycle(), redisCandleNew.getExchange(), redisCandleNew.getProdCode(), redisCandleNew.getMinTime());
            if (null == preCandl) {
                redisCandleNew.setOpenPx(redisCandleNew.getLastPx());
            } else {
                redisCandleNew.setOpenPx(preCandl.getClosePx());
            }
        }
    }

    //获取上一个周期时间
    private String getPreMineCycle(CandleCycle cycle, Snapshot baseSnapshot) {

        return MinTimeUtil.getPreTime(cycle, baseSnapshot);

    }


    private void saveDB(NJSCandle redisCandle) {

        if (null != redisCandle) {
            hqService.insertRedisCandleHistory(redisCandle.getCycle(), redisCandle);
            insert(redisCandle);
          //  logger.info(JSON.toJSONString(redisCandle));


        }
    }

    /**
     * 判断该key是否在redis已经有
     */
    private Boolean isExistRedis(String redisKey) {


        Boolean isExist = false;
        if (!StringUtils.isBlank(redisKey)) {

            if (jredisUtil.exists(redisKey)) {
                isExist = true;
            }
        }
        return isExist;
    }


    public int insert(NJSCandle candle) {
        return njsCandleDao.insert(candle);

    }

    @Override
    public void redisInit(String financeMic) {

        List<Candle> candlesList = queryDB(financeMic);
        for (Candle candle : candlesList) {
            if (null != candle) {
                if (!jredisUtil.exists(candle.getRedisKey())) {//如果redis没有，就用数据库的初始化
                    jredisUtil.setex(candle.getRedisKey(), JSON.toJSONString(candle), 0);
                }
            }
        }
    }


    public List<Candle> queryDB(String financeMic) {
        return njsCandleDao.selectNew(financeMic);
    }

    private Set<String> selectDateNear5(TradeAmountReq tradeAmountReq) {

        return hqService.queryRedisMultiDayHistory(tradeAmountReq);
        // return njsCandleDao.selectDateNear5(tradeAmountReq);
    }




    //设置分时线历史查询的跨天日期     南交所：早上九点到次日六点，算做今天的日分时
    private void setSelectSnapshotFiveDate(TradeAmountReq tradeAmountReq, String nowDate,String afterDate) {

        if (null != tradeAmountReq && !StringUtils.isBlank(nowDate)) {
            StringBuffer beginStrBuffer = new StringBuffer();
            StringBuffer endStrBuffer = new StringBuffer();
            beginStrBuffer.append(nowDate);
            beginStrBuffer.append("0900");
            if(!StringUtils.isBlank(afterDate)){
                endStrBuffer.append(afterDate);
            }else{
                //择中办法，20天目的是为了查询当前时间以后的所有数据，如果只是+1的话遇到节假日或者周末可能就会错。
                endStrBuffer.append(DateUtils.addDay(nowDate, 20, "yyyyMMdd"));
            }
            endStrBuffer.append("0600");
            tradeAmountReq.setBeginDate(beginStrBuffer.toString());
            tradeAmountReq.setEndDate(endStrBuffer.toString());
        } else {
            tradeAmountReq = null;
        }
    }
    //判断是否是新的交易日 比如6点后算新的一天
    private  Boolean  isNewTradeDate(){

        Boolean isNewTradeDate=false;
        String nowTime=DateUtils.getNoTime("yyyyMMddHHmm");
        String tempTime=DateUtils.getNoTime("yyyyMMdd")+"0855";

        long nowTimeTicker=DateUtils.getTickTime(nowTime,"yyyyMMddHHmm");
        long tempTimeTicker=DateUtils.getTickTime(tempTime,"yyyyMMddHHmm");
        if(nowTimeTicker>tempTimeTicker){
            isNewTradeDate=true;
        }
        return isNewTradeDate;
    }
}
