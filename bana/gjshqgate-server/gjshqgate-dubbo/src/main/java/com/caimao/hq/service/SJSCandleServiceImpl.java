/**
 *
 */
package com.caimao.hq.service;

import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.api.service.IGjsErrorService;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.api.service.ISJSCandleService;
import com.caimao.hq.dao.SJSCandleDao;
import com.caimao.hq.utils.*;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author yzc
 */
@Service("sjsCandleService")
public class SJSCandleServiceImpl extends CandleUtils implements ISJSCandleService {
    private Logger logger = LoggerFactory.getLogger(SJSCandleServiceImpl.class);
    @Autowired
    private GJSProductUtils gjsProductUtils;

    @Autowired
    private SJSCandleDao sjsCandleDao;
    @Autowired
    private JRedisUtil jredisUtil;

    @Autowired
    private IGjsErrorService gjsErrorService;
    private  double d = 0.0001;//doubule 精度
    @Autowired
    public HolidayUtil holidayUtil;

    @Autowired
    private IHQService hqService;
    @Value("${sjs.multisnap.time.begin}")
    protected String sjsMultiSnapBeginTime;

    @Value("${sjs.multisnap.time.end}")
    protected String sjsMultiSnapEndTime;


    @Override
    public void redisInit(String financeMic) {

        List<Candle> snapshotsList=queryDB(financeMic);
        for(Candle candle:snapshotsList){
            if(null!=candle){
                if(!jredisUtil.exists(candle.getRedisKey())){//如果redis没有，就用数据库的初始化
                    jredisUtil.setex(candle.getRedisKey(), JSON.toJSONString(candle), 0);
                }
            }
        }
    }
    public List<Candle> queryDB(String financeMic) {
        return sjsCandleDao.selectNew(financeMic);
    }
    public List<Candle> queryDB(CandleReq candleReq){

        return sjsCandleDao.selectList(candleReq);
    }

    public Candle queryRedis(CandleReq candleReq){

        String strCycle=candleReq.getCycle();
        CandleCycle cycle=null;
        Candle candle=null;
        if(!StringUtils.isBlank(strCycle)){
            cycle=mapCycle.get(strCycle);
            if(null==cycle){
                logger.error("查询周期错误,不支持该周期,cycle is null");
                throw new RuntimeException("查询周期错误,不支持该周期,cycle is null");
            }
            candle=queryRedis(candleReq.getExchange(), candleReq.getProdCode(), cycle);
            if(null==candle){//Redis查询不到，就查询DB,然后同步到Redis
                candle= queryDB(candleReq).get(0);
                if(null!=candle){
                    saveRedis(candle.getRedisKey(),candle);
                }
            }
        }
        return candle;
    }


    private SJSCandle queryRedis(String financeMic, String prodCode, CandleCycle cycle){
        SJSCandle candleRedis=null;
        String redisKey=SJSMinTimeUtil.getRedisKey(cycle, financeMic, prodCode);
        if(!StringUtils.isBlank(redisKey)){
            candleRedis= getValueFromRedis(redisKey);
        }
        return candleRedis;

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

                    sjsCandleDao.insertBatch(insertList);
                    insertList.clear();
                }
            }
            sjsCandleDao.insertBatch(insertList);
        }
    }
    private void fillData(Candle redisCandle,long begin, long end,CandleCycle cycle,String formate){


    }
    public  void save(Snapshot baseSnapshot) {
        try {


            SJSSnapshot minute1=(SJSSnapshot)baseSnapshot.clone();
            SJSSnapshot minute5=(SJSSnapshot)baseSnapshot.clone();
            SJSSnapshot minute30=(SJSSnapshot)baseSnapshot.clone();
            SJSSnapshot hour1=(SJSSnapshot)baseSnapshot.clone();
            SJSSnapshot dayCandle=(SJSSnapshot)baseSnapshot.clone();
            SJSSnapshot week=(SJSSnapshot)baseSnapshot.clone();
            SJSSnapshot month=(SJSSnapshot)baseSnapshot.clone();
            SJSSnapshot year=(SJSSnapshot)baseSnapshot.clone();

            //格式化交易所的跨天日期,日以下周期的MinTime为用apdRecvTime
            week.setMinTime(week.getTradeDate()+week.getTradeTime());
            month.setMinTime(month.getTradeDate()+month.getTradeTime());
            year.setMinTime(year.getTradeDate()+year.getTradeTime());
            dayCandle.setMinTime(dayCandle.getTradeDate()+dayCandle.getTradeTime());


            save(CandleCycle.Minute1, minute1);
            save(CandleCycle.Minute5,minute5);
            save(CandleCycle.Minute30, minute30);
            save(CandleCycle.Hour1, hour1);
            save(CandleCycle.DayCandle, dayCandle);
            save(CandleCycle.Week, week);
            save(CandleCycle.Month, month);
            save(CandleCycle.Year, year);


        } catch (Exception e) {
            logger.error("生成K线异常"+e.getMessage()+"data="+baseSnapshot);
            throw new RuntimeException("生成K线异常"+e.getMessage());
        }

    }

    @Override
    public Candle getPreCandl(CandleCycle cycle, String finance_mic, String prod_code,String miniTime) {

        return sjsCandleDao.getPreCandl( cycle,  finance_mic,  prod_code, miniTime);
    }
    @Transactional
    private void save(CandleCycle cycle,Snapshot baseSnapshot) {

        if(null==cycle||null==baseSnapshot){
            return;
        }
        SnapshotFormate.formateSnapshotSJS(cycle, baseSnapshot);//格式化时间和rediskey
        //如果redis有对应的key
        if(isExistRedis(baseSnapshot.getRedisKey())){

            existRedis(cycle, baseSnapshot);

        } else {
            notExistRedis(cycle, baseSnapshot);

        }
    }
    public  void saveRedis(String redisKey,Candle redisCandle) {
        if(null!=redisCandle){
            String jsonString = JSON.toJSONString(redisCandle);
            jredisUtil.setex(redisKey, jsonString, 0);

        }
    }
    private  void  existRedis(CandleCycle cycle,Snapshot baseSnapshot){

        SJSCandle redisCandle=getValueFromRedis(baseSnapshot.getRedisKey());

        if(cycle==CandleCycle.Week){

            existRedisWeek(cycle, baseSnapshot, redisCandle);
        }else if(cycle==CandleCycle.Month){
            existRedisMonth(cycle,baseSnapshot,redisCandle);
        }else if(cycle==CandleCycle.Year){

            existRedisYear(cycle, baseSnapshot, redisCandle);

        }else if(cycle==CandleCycle.DayCandle){

            existRedisDay(cycle,baseSnapshot,redisCandle);
        }else{
            existRedisCommon(cycle, baseSnapshot, redisCandle);
        }

    }

    public void  updateDay(CandleCycle cycle,Candle candle,Snapshot baseSnapshot){

        if(null!=candle&&null!=baseSnapshot){
            if(cycle==CandleCycle.DayCandle){
                candle.setHighPx(baseSnapshot.getHighPx());
                candle.setLowPx(baseSnapshot.getLowPx());
                candle.setBusinessAmount(baseSnapshot.getBusinessAmount());
                candle.setBusinessBalance(baseSnapshot.getBusinessBalance());
                candle.setPxChange(baseSnapshot.getPxChange());
                candle.setPxChangeRate(DoubleOperationUtil.round(baseSnapshot.getPxChangeRate(), 2));
                candle.setLastPx(baseSnapshot.getLastPx());
                candle.setClosePx(baseSnapshot.getLastPx());
                candle.setPreClosePx(baseSnapshot.getPreclosePx());
            }
            //candle.setOpen_px(baseSnapshot.getOpen_px());//因传入的数据分笔按分钟给出了开盘价，所以不同周期的都可以沿用
        }
    }
    private void existRedisDay(CandleCycle cycle,Snapshot baseSnapshot,  SJSCandle redisCandle){

        if(null!=redisCandle&&cycle==CandleCycle.DayCandle){


            long redisDatel=DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 8), "yyyyMMdd");
            long snapshotDatel=DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 8), "yyyyMMdd");


            if(redisDatel==snapshotDatel){//如果跟Redis相等就修改,否则就添加

                updateDay(cycle, redisCandle, baseSnapshot);//更新Redis
                saveRedis(baseSnapshot.getRedisKey(), redisCandle);

            }else if(snapshotDatel>redisDatel){

                //如果不等,新生成一个
                SJSCandle redisCandleNew = new SJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);

                redisCandle.setSettle(baseSnapshot.getLastSettle());
                redisCandle.setClosePx(baseSnapshot.getPreclosePx());
                redisCandle.setLastPx(redisCandle.getClosePx());

                redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价
                redisCandleNew.setLastSettle(redisCandle.getSettle());
                setPxChange(redisCandle,baseSnapshot);
                setPxChangeRate(redisCandle,baseSnapshot);
                saveDB(redisCandle);//把原来的Redis保存到数据库
                saveRedis(baseSnapshot.getRedisKey(), redisCandleNew);
            }
        }else{
            logger.error("根据rediskey 获取数据异常:当前分时日期小于Redis日期");
        }

    }

    private void existRedisWeek(CandleCycle cycle,Snapshot baseSnapshot, SJSCandle redisCandle){

        if(cycle==CandleCycle.Week){//如果是周K
            long redisDatel=DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 8), "yyyyMMdd");
            long snapshotDatel=DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 8), "yyyyMMdd");


            int newWeek= Integer.parseInt(DateUtils.getWeekOfDate(baseSnapshot.getCycleTimeDate(), "yyyyMMddHHmm")) ;
            int oldWeek= Integer.parseInt(DateUtils.getWeekOfDate(redisCandle.getMinTime(), "yyyyMMddHHmm")) ;

            if(snapshotDatel>=redisDatel){
                if(newWeek<oldWeek) {//说明是新的一周

                    //如果不等,新生成一个
                    SJSCandle redisCandleNew=new SJSCandle();
                    initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                    redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                    redisCandleNew.setStatus("0");


                    redisCandle.setSettle(baseSnapshot.getLastSettle());
                    redisCandle.setClosePx(baseSnapshot.getPreclosePx());
                    redisCandle.setLastPx(redisCandle.getClosePx());
                    redisCandleNew.setLastSettle(redisCandle.getSettle());
                    setPxChange(redisCandle, baseSnapshot);
                    setPxChangeRate(redisCandle, baseSnapshot);
                    redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价
                    redisCandleNew.setStatus("0");
                    //老K线的结算价=新K线的昨结
                    saveDB(redisCandle);//把原来的Redis保存到数据库
                    saveRedis(baseSnapshot.getRedisKey(), redisCandleNew);

                }else{
                    update(cycle,redisCandle, baseSnapshot);//更新Redis
                    saveRedis(baseSnapshot.getRedisKey(), redisCandle);
                }
            }
        }

    }
    @Override
    public  void update(CandleCycle cycle,Candle candle,Snapshot baseSnapshot) {


        if(null!=candle&&null!=baseSnapshot){
            if(cycle==CandleCycle.DayCandle){

                candle.setHighPx(baseSnapshot.getHighPx());
                candle.setLowPx(baseSnapshot.getLowPx());
                candle.setBusinessAmount(baseSnapshot.getBusinessAmount());
                candle.setBusinessBalance(baseSnapshot.getBusinessBalance());
                candle.setLastPx(baseSnapshot.getLastPx());
                candle.setPreClosePx(baseSnapshot.getPreclosePx());
                candle.setOpenPx(baseSnapshot.getOpenPx());
                candle.setLastPx(baseSnapshot.getLastPx());
                candle.setAveragePx(baseSnapshot.getAveragePx());
                candle.setSettle(baseSnapshot.getSettle());
                candle.setLastSettle(baseSnapshot.getLastSettle());
                candle.setPxChange(baseSnapshot.getPxChange());
                candle.setPxChangeRate(baseSnapshot.getPxChangeRate());
            } else {

                setHighpx(candle, baseSnapshot);
                setLowpx(candle, baseSnapshot);

                setBusinessAmount(candle, baseSnapshot);
                setBusinessBalance(candle, baseSnapshot);
                setLastpx(candle, baseSnapshot);
                setPxChange(candle, baseSnapshot);
                setPxChangeRate(candle, baseSnapshot);

                //差一个设置均价的方法
            }
            setCpx(candle, baseSnapshot);
            candle.setRedisKey(baseSnapshot.getRedisKey());
            candle.setIsGoods(baseSnapshot.getIsGoods());
            candle.setCycle(cycle);
            candle.setMinTime(baseSnapshot.getCycleTimeDate());
            //candle.setOpen_px(baseSnapshot.getOpen_px());//因传入的数据分笔按分钟给出了开盘价，所以不同周期的都可以沿用
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
                candle.setPxChangeRate(DoubleOperationUtil.round(DoubleOperationUtil.div(candle.getPxChange(), candle.getPreClosePx(),4)*100,2));
            }
        }
    }

    private void existRedisMonth(CandleCycle cycle,Snapshot baseSnapshot,  SJSCandle redisCandle){

        if(cycle==CandleCycle.Month){

            long redisDatel=DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 6), "yyyyMM");
            long snapshotDatel=DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 6), "yyyyMM");

            if (snapshotDatel>redisDatel) {//说明是新的一月

                //如果不等,新生成一个
                SJSCandle redisCandleNew = new SJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                redisCandleNew.setStatus("0");
                redisCandle.setSettle(baseSnapshot.getLastSettle());
                redisCandle.setClosePx(baseSnapshot.getPreclosePx());
                redisCandle.setLastPx(redisCandle.getClosePx());
                redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价
                redisCandleNew.setLastSettle(redisCandle.getSettle());
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

    @Override
    public  void initCandleFromSnapshot(CandleCycle cycle,Candle candle,Snapshot baseSnapshot) {
        if(null!=candle&&baseSnapshot!=null){
            if(cycle==CandleCycle.DayCandle){
                candle.setHighPx(baseSnapshot.getHighPx());
                candle.setLowPx(baseSnapshot.getLowPx());
                candle.setBusinessAmount(baseSnapshot.getBusinessAmount());
                candle.setBusinessBalance(baseSnapshot.getBusinessBalance());
//                candle.setPxChange(baseSnapshot.getPxChange());
//                candle.setPxChangeRate(DoubleOperationUtil.round(baseSnapshot.getPxChangeRate(), 2));
                candle.setPreClosePx(baseSnapshot.getPreclosePx());
                candle.setOpenPx(baseSnapshot.getOpenPx());
                candle.setClosePx(baseSnapshot.getClosePx());
                candle.setSettle(baseSnapshot.getLastPx());//日K 结算价=现价 ，只有昨结算管用
                candle.setLastSettle(baseSnapshot.getLastSettle());
                candle.setAveragePx(baseSnapshot.getAveragePx());
            }else{
                //candle.setOpen_px(baseSnapshot.getLastPx());//开盘价=最新价

                candle.setBusinessBalance(baseSnapshot.getLastAmount() * baseSnapshot.getLastPx());//最新价*最新成交数量
                candle.setBusinessAmount(baseSnapshot.getLastAmount());//最新成交数量
                candle.setClosePx(baseSnapshot.getLastPx());
                candle.setHighPx(baseSnapshot.getLastPx());
                candle.setLowPx(baseSnapshot.getLastPx());
                candle.setOpenPx(baseSnapshot.getLastPx());
                candle.setSettle(baseSnapshot.getLastPx());
                candle.setLastSettle(baseSnapshot.getLastPx());
            }
            candle.setIsGoods(baseSnapshot.getIsGoods());
            candle.setRedisKey(baseSnapshot.getRedisKey());
            candle.setCycle(cycle);
            candle.setProdName(baseSnapshot.getProdName());
            candle.setProdCode(baseSnapshot.getProdCode());
            candle.setLastPx(baseSnapshot.getLastPx());
            candle.setExchange(baseSnapshot.getExchange());
            candle.setMinTime(baseSnapshot.getCycleTimeDate());

        }
    }
//    @Override
//    //设置价格涨跌  当前价-昨收
//    public   void  setPxChange(Candle candle,Snapshot baseSnapshot){
//
//        SJSSnapshot sjsSnapshot=null;
//        if(baseSnapshot instanceof  SJSSnapshot){
//            sjsSnapshot=(SJSSnapshot)baseSnapshot;
//        }
//        if(null!=candle&&null!=sjsSnapshot){
//
//            if(CandleCycle.DayCandle==candle.getCycle()){
//
//                candle.setPxChange(sjsSnapshot.getPxChange());
//
//            }else{
//
//                if(sjsSnapshot.getIsGoods()==0){//如果是现货
//
//                    candle.setPxChange(DoubleOperationUtil.sub(sjsSnapshot.getLastPx(),candle.getPreClosePx()));
//
//                }else{//如果是期货
//
//                    candle.setPxChange(DoubleOperationUtil.sub(sjsSnapshot.getLastSettle(),candle.getPreClosePx()));
//
//                }
//
//            }
//
//        }
//    }

//    //设置昨收:期货昨收为结算价
//    @Override
//    public void setPreClosePx(Candle candle,Snapshot baseSnapshot){
//        SJSSnapshot sjsSnapshot=null;
//        if(baseSnapshot instanceof  SJSSnapshot){
//            sjsSnapshot=(SJSSnapshot)baseSnapshot;
//        }
//        if(null!=candle&&null!=sjsSnapshot){
//
//            if(CandleCycle.DayCandle==candle.getCycle()){
//
//                if(sjsSnapshot.getIsGoods()==0){//如果是现货
//
//                    candle.setPreClosePx(sjsSnapshot.getPreclosePx());
//
//                }else{//如果为期货
//
//                    candle.setPreClosePx(sjsSnapshot.getLastSettle());
//
//                }
//
//            }
//        }
//
//    }


    private void existRedisYear(CandleCycle cycle,Snapshot baseSnapshot, SJSCandle redisCandle) {

        if(cycle==CandleCycle.Year){//如果是年
            long redisDatel=DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 4), "yyyy");
            long snapshotl=DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 4), "yyyy");

            if (snapshotl>redisDatel) {//说明是新的一年


                //如果不等,新生成一个
                SJSCandle redisCandleNew = new SJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                redisCandleNew.setStatus("0");
                redisCandleNew.setOpenPx(baseSnapshot.getOpenPx());
                redisCandle.setSettle(baseSnapshot.getLastSettle());
                redisCandle.setClosePx(baseSnapshot.getPreclosePx());
                redisCandle.setLastPx(redisCandle.getClosePx());
                redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价
                redisCandleNew.setLastSettle(redisCandle.getSettle());
                setPxChange(redisCandle,baseSnapshot);
                setPxChangeRate(redisCandle,baseSnapshot);
                redisCandleNew.setStatus("0");
                saveDB(redisCandle);//把原来的Redis保存到数据库
                saveRedis(baseSnapshot.getRedisKey(), redisCandleNew);
            }else if(snapshotl==redisDatel){
                update(cycle,redisCandle, baseSnapshot);//更新Redis
                saveRedis(baseSnapshot.getRedisKey(), redisCandle);

            }
        }

    }
    //适合日K以下周期
    private void existRedisCommon(CandleCycle cycle,Snapshot baseSnapshot,  SJSCandle redisCandle){

        if(null!=redisCandle){

            long redisDatel=DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 12), "yyyyMMddHHmm");
            long snapshotl=DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 12), "yyyyMMddHHmm");


            if(redisDatel==snapshotl){//如果跟Redis相等就修改,否则就添加
                update(cycle, redisCandle, baseSnapshot);//更新Redis
                saveRedis(baseSnapshot.getRedisKey(), redisCandle);

            }else if(snapshotl>redisDatel){

                try{

                    fillData(redisCandle,snapshotl,redisDatel,cycle,"yyyyMMddHHmm");

                }catch (Exception ex){
                    logger.error("批量填充数据错误:"+ex.getMessage());
                }
                //如果不等,新生成一个
                SJSCandle redisCandleNew=new SJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                redisCandleNew.setStatus("0");

                setPxChange(redisCandle, baseSnapshot);
                setPxChangeRate(redisCandle, baseSnapshot);
                redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价


                saveDB(redisCandle);//把原来的Redis保存到数据库
                saveRedis(redisCandleNew.getRedisKey(), redisCandleNew);
            }
        }else{
            logger.error("根据rediskey 获取数据异常:");
        }


    }

    private void  fillCandleMinue1(List<String> miniTimeList,Candle redisCandle){

        if(null!=redisCandle&&miniTimeList!=null){
            int length=miniTimeList.size();
            for(int i=0;i<length;i++){
                try{
                    Candle temp=(Candle)redisCandle.clone();
                    temp.setMinTime(miniTimeList.get(i));//设置分时K的交易时间
                    temp.setOpenPx(redisCandle.getClosePx());//开盘价等于上一分钟的收盘价
                    temp.setClosePx(redisCandle.getClosePx());// 开盘价=收盘价
                    temp.setHighPx(redisCandle.getClosePx());// 最高价=收盘价
                    temp.setLowPx(redisCandle.getClosePx());// 最高价=收盘价
                }catch (Exception ex){
                    logger.error("自动填充行情分时数据异常：克隆redisCandle异常,"+ex.getMessage()+",redisCandle="+redisCandle);
                    throw new RuntimeException("自动填充行情分时数据异常：克隆redisCandle异常,"+ex.getMessage()+",redisCandle="+redisCandle);
                }
            }

        }
    }

    private void fillCandleSetMiniTime(Candle candleClone){

    }
    private  void  notExistRedis(CandleCycle cycle,Snapshot baseSnapshot){

        //Redis没有当前品种的周期，就新建一个放入redis
        SJSCandle redisCandleNew=new SJSCandle();
        initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
        redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
        redisCandleNew.setRedisKey(baseSnapshot.getRedisKey());
        redisCandleNew.setStatus("0");

//        Candle candle=getPreCandl( cycle,redisCandleNew.getExchange(),redisCandleNew.getProdCode(),redisCandleNew.getMinTime());//设置收盘价
//        if(null!=candle){
//            redisCandleNew.setPreClosePx(candle.getClosePx());
//        }
        redisCandleNew.setPreClosePx(baseSnapshot.getPreclosePx());
        saveRedis(baseSnapshot.getRedisKey(), redisCandleNew);

    }

    private void  initOpenPx(Candle redisCandleNew){
        if(null!=redisCandleNew){

            Candle preCandl=getPreCandl(redisCandleNew.getCycle(),redisCandleNew.getExchange(),redisCandleNew.getProdCode(),redisCandleNew.getMinTime());
            if(null==preCandl){
                redisCandleNew.setOpenPx(redisCandleNew.getLastPx());
            }else{
                redisCandleNew.setOpenPx(preCandl.getClosePx());
            }
        }
    }
    //获取上一个周期时间
    private  String  getPreMineCycle(CandleCycle cycle,Snapshot baseSnapshot){

        return  SJSMinTimeUtil.getPreTime(cycle,baseSnapshot);

    }


    private void saveDB(SJSCandle redisCandle){

        if(null!=redisCandle){

            hqService.insertRedisCandleHistory(redisCandle.getCycle(),redisCandle);//保存到Redis history
            // logger.info(JSON.toJSONString(redisCandle));
            insert(redisCandle);
        }

    }

    /**
     * 判断该key是否在redis已经有
     *
     * */
    private Boolean isExistRedis(String redisKey){

        Boolean isExist=false;
        if(!StringUtils.isBlank(redisKey)){

            if(jredisUtil.exists(redisKey)){
                isExist=true;
            }
        }
        return isExist;
    }

    private SJSCandle getValueFromRedis(String strRedisKey){
        SJSCandle candle=null;
        try{
            if(!StringUtils.isBlank(strRedisKey)){
                Object redisObj= jredisUtil.get(strRedisKey);
                if(null!=redisObj){

                    candle=JSON.parseObject((String)redisObj, SJSCandle.class);
                }
            }
        }catch (Exception ex){

            logger.error("redis from string to obj error,不能解析为SJSCandle，strRedisKey="+strRedisKey,ex.getMessage());
            candle=null;
        }
        return candle;
    }

    public int insert(SJSCandle candle) {
        return sjsCandleDao.insert(candle);
    }

    private Set<String> selectDateNear5(TradeAmountReq tradeAmountReq){
        //查询redis，获取最近几个交易日的日期
        return hqService.queryRedisMultiDayHistory(tradeAmountReq);
        //查询Mysql，获取最近几个交易日的日期
        //return sjsCandleDao.selectDateNear5(tradeAmountReq);
    }



    public List getMultiDaySnapshotRedis(TradeAmountReq tradeAmountReq){

        List listAll=new ArrayList();

        Set<String> dateSet=selectDateNear5(tradeAmountReq);
        List<String> dateList=new ArrayList();
        dateList.addAll(dateSet);
        Collections.reverse(dateList);//日期降序

        if(null!=dateList){
            CandleReq candleReq=new CandleReq();
            candleReq.setExchange(tradeAmountReq.getExchange());
            candleReq.setProdCode(tradeAmountReq.getProdCode());
            if(StringUtils.isBlank(tradeAmountReq.getCycle())){
                candleReq.setCycle("1");
            }else{
                candleReq.setCycle(tradeAmountReq.getCycle());
            }
            //防止意外情况，可能数据只有一个交易日
            if(null!=dateList&&dateList.size()==1){

                dateList.add(DateUtils.addDay(String.valueOf(dateList.get(0)), -1, "yyyyMMdd"));
                Collections.reverse(dateList);

            }
            for(int i=1;i<dateList.size();i++){

                if(!StringUtils.isBlank(dateList.get(i))){

                    setSelectSnapshotFiveDate(tradeAmountReq, dateList.get(i), dateList.get(i - 1));
                    if(null!=tradeAmountReq){
                        candleReq.setBeginDate(tradeAmountReq.getBeginDate());
                        candleReq.setEndDate(tradeAmountReq.getEndDate());
                        List  list=hqService.queryCandleRedisHistory(candleReq);
                        if(null!=list&&list.size()>0){
                            listAll.addAll(list);
                        }
                    }
                }
            }
        }
        return listAll;
    }


    //简化接口
    public List getMultiDaySnapshotRedisFormate(TradeAmountReq tradeAmountReq){

        List listAll=new ArrayList();
        //因为上金所分时是按前一个交易日到当前交易日，算一个交易日。  所以查询交易日期的时候 ，多查询了一天
        int tempDayNumber=tradeAmountReq.getType()+1;
        tradeAmountReq.setType(tempDayNumber);
        Set<String> dateSet=selectDateNear5(tradeAmountReq);
        List<String> dateList=new ArrayList();
        dateList.addAll(dateSet);
        Collections.reverse(dateList);//日期降序
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
            //防止意外情况，可能数据只有一个交易日
            if(null!=dateList&&dateList.size()==1){

                dateList.add(DateUtils.addDay(String.valueOf(dateList.get(0)), -1, "yyyyMMdd"));
                Collections.reverse(dateList);

            }
            for(int i=1;i<dateList.size();i++){
                LinkedMap map=new LinkedMap();
                if(!StringUtils.isBlank(dateList.get(i))){

                    setSelectSnapshotFiveDate(tradeAmountReq, dateList.get(i), dateList.get(i - 1));
                    if(null!=tradeAmountReq){

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
                            if(null==temp){
                                temp=new SJSCandle();
                            }
                            if(StringUtils.isBlank(tradeAmountReq.getRetParaKeys())){
                                map.put("ret", DozerMapperSingleton.fillProperty(convertList));
                            }else{
                                map.put("ret", DozerMapperSingleton.fillProperty(convertList, tradeAmountReq.getRetParaKeys()));
                            }
                            DozerMapperSingleton.setObjParaKeys(map, tradeAmountReq.getObjParaKeys(), temp);
                            listAll.add(map);
                        }
                    }
                }
            }
        }
        Collections.reverse(listAll);
        return listAll;
    }

    //设置分时线历史查询的跨天日期     上金所：昨天20点到今天15点，算作今天的日分时（20:00-02:30, 09:00-11:30, 13:30-15:30）
    private void setSelectSnapshotFiveDate(TradeAmountReq tradeAmountReq,String nowDate,String beforDate){

        if (null != tradeAmountReq && !StringUtils.isBlank(nowDate)) {

            StringBuffer beginStrBuffer = new StringBuffer();
            StringBuffer endStrBuffer = new StringBuffer();

            String beginTime="";
            if(!StringUtils.isBlank(sjsMultiSnapBeginTime)){
                beginTime=sjsMultiSnapBeginTime;
            }else{
                beginTime="2000";
            }

            String endTime="";
            if(!StringUtils.isBlank(sjsMultiSnapEndTime)){

                endTime=sjsMultiSnapEndTime;

            }else{
                endTime="1530";
            }
            if(!StringUtils.isBlank(beforDate)){
                beginStrBuffer.append(beforDate);
                beginStrBuffer.append(beginTime);
            }else{

                beginStrBuffer.append(DateUtils.addDay(nowDate, -1, "yyyyMMdd"));
                beginStrBuffer.append(beginTime);
            }

            endStrBuffer.append(nowDate);
            endStrBuffer.append(endTime);

            tradeAmountReq.setBeginDate(beginStrBuffer.toString());
            tradeAmountReq.setEndDate(endStrBuffer.toString());
        }

    }
    //判断是否是新的交易日 比如6点后算新的一天
    private  Boolean  isNewTradeDate(){

        Boolean isNewTradeDate=false;
        String nowTime=DateUtils.getNoTime("yyyyMMddHHmm");
        String tempTime=DateUtils.getNoTime("yyyyMMdd")+"1955";

        long nowTimeTicker=DateUtils.getTickTime(nowTime,"yyyyMMddHHmm");
        long tempTimeTicker=DateUtils.getTickTime(tempTime,"yyyyMMddHHmm");
        if(nowTimeTicker>tempTimeTicker){
            isNewTradeDate=true;
        }
        return isNewTradeDate;
    }

}
