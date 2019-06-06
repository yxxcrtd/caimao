package com.caimao.hq.utils;


import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.core.HQDataInit;
import com.caimao.hq.core.SJSQuoteParseUtils;
import com.caimao.hq.dao.SJSCandleDao;
import com.caimao.hq.dao.SJSSnapshotDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by yzc on 2015/11/10.
 */

@Service("importDataSJS")
public class ImportDataSJS extends  CandleUtils{
    private Logger logger = Logger.getLogger(ImportDataSJS.class);

    @Autowired
    private SJSCandleDao sjsCandleDao;
    @Autowired
    private IHQService hqService;

    @Autowired
    private SJSSnapshotDao sjsSnapshotDao;



    public String[] readfile(String filepath) throws Exception {
        FileReader fr = new FileReader(filepath);
        // 将无法识别的字节赋值为'?'
        int c = 63;
        String errmessage = "文件编码不是GBK，不能解析";
        try {
            // 从文件中读取一个字符
            c = fr.read();

        } catch (Exception e) {
            try {
                fr.skip(1);
            } catch (Exception ex) {
                throw new Exception(errmessage, ex);
            }
            c = 63;
        }
        StringBuffer sb = new StringBuffer();
        List list = new ArrayList();
        while (c != -1) {
            // 遇到回车符时保存该行内容，刷新缓存
            if (c == 10) {
                list.add(sb.toString());
                sb = new StringBuffer();
                try {
                    // 从文件中继续读取数据
                    c = fr.read();
                } catch (Exception e) {
                    try {
                        fr.skip(1);
                    } catch (Exception ex) {
                        throw new Exception(errmessage, ex);
                    }
                    c = 63;
                }
                continue;
            }
            sb.append((char) c);
            try {
                // 从文件中继续读取数据
                c = fr.read();
            } catch (Exception e) {
                try {
                    fr.skip(1);
                } catch (Exception ex) {
                    throw new Exception(errmessage, ex);
                }
                c = 63;
            }
        }
        // 保存最后一行内容
        if (c == -1 && sb.length() > 0) {
            list.add(sb.toString());
        }
        fr.close();
        // 返回从文本文件中读取的内容
        Object[] obj = list.toArray();
        String[] objs = new String[obj.length];
        for (int i = 0; i < obj.length; i++) {
            objs[i] = (String) obj[i];
        }
        return objs;
    }


    public static Map isRepeat=new HashMap();
    public static List<Snapshot> removeRepeatLst=new ArrayList<>();

    public static Map candleMap=new HashMap();
    public static Map snapMap=new HashMap();

    public  void importDataSingle(String path){

        isRepeat.clear();
        removeRepeatLst.clear();
        try {
            String[] line = readfile(path);

            for (int i = 0; i < line.length; i++) {
                try{
                    List<Snapshot> list= SJSQuoteParseUtils.convertImport(line[i]);
                    removeRepeatLst=removeRepeat(list);
                    if(null!=removeRepeatLst){
                        for(Snapshot Snapshot:removeRepeatLst){
                            save(Snapshot);
                        }
                    }
                }catch(Exception ex){
                    //   System.out.println(ex.getMessage());
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void save(CandleCycle cycle,Snapshot baseSnapshot) {

        if(null==cycle||null==baseSnapshot){
            return;
        }
        SnapshotFormate.formateSnapshotSJS(cycle, baseSnapshot);//格式化时间和rediskey
        //如果redis有对应的key
        if(isExistRedis(baseSnapshot.getRedisKey())){

            existRedis(cycle,baseSnapshot);

        } else {
            notExistRedis(cycle, baseSnapshot);

        }
    }

    private  void  notExistRedis(CandleCycle cycle,Snapshot baseSnapshot){

        //Redis没有当前品种的周期，就新建一个放入redis
        SJSCandle redisCandleNew=new SJSCandle();
        initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
        redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
        redisCandleNew.setRedisKey(baseSnapshot.getRedisKey());
        redisCandleNew.setStatus("0");
        candleMap.put(baseSnapshot.getRedisKey(), redisCandleNew);


    }

    private void existRedisWeek(CandleCycle cycle,Snapshot baseSnapshot, SJSCandle redisCandle){

        if(cycle==CandleCycle.Week) {//如果是周K

            long redisDatel = DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 8), "yyyyMMdd");
            long snapshotDatel = DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 8), "yyyyMMdd");


            int newWeek = Integer.parseInt(DateUtils.getWeekOfDate(baseSnapshot.getCycleTimeDate(), "yyyyMMddHHmm"));
            int oldWeek = Integer.parseInt(DateUtils.getWeekOfDate(redisCandle.getMinTime(), "yyyyMMddHHmm"));
            if (snapshotDatel >= redisDatel) {
                if (newWeek < oldWeek) {//说明是新的一周


                    //如果不等,新生成一个
                    SJSCandle redisCandleNew = new SJSCandle();
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

                    //老K线的结算价=新K线的昨结

                    writeFile(redisCandle.getCycle(), JSON.toJSONString(redisCandle));
                    candleMap.put(baseSnapshot.getRedisKey(), redisCandleNew);
                } else {
                    updateWeek(cycle, redisCandle, baseSnapshot);//更新Redis
                    candleMap.put(baseSnapshot.getRedisKey(), redisCandle);
                }
            }
        }
    }

    public void  updateWeek(CandleCycle cycle,Candle candle,Snapshot baseSnapshot){

        if(null!=candle&&null!=baseSnapshot){
            if(cycle==CandleCycle.Week){

                setHighpx(candle, baseSnapshot);
                setLowpx(candle, baseSnapshot);
                setBusinessAmount(candle, baseSnapshot);
                setBusinessBalance(candle, baseSnapshot);
                setLastpx(candle, baseSnapshot);
                setPxChange(candle, baseSnapshot);
                setPxChangeRate(candle, baseSnapshot);
                setCpx(candle, baseSnapshot);
                candle.setRedisKey(baseSnapshot.getRedisKey());
                candle.setIsGoods(baseSnapshot.getIsGoods());
                candle.setSettle(baseSnapshot.getSettle());
                candle.setLastSettle(baseSnapshot.getLastSettle());
                candle.setCycle(cycle);
                candle.setMinTime(baseSnapshot.getCycleTimeDate());

            }
            //candle.setOpen_px(baseSnapshot.getOpen_px());//因传入的数据分笔按分钟给出了开盘价，所以不同周期的都可以沿用
        }
    }

    public void  updateMonth(CandleCycle cycle,Candle candle,Snapshot baseSnapshot){

        if(null!=candle&&null!=baseSnapshot){
            if(cycle==CandleCycle.Month){

                setHighpx(candle, baseSnapshot);
                setLowpx(candle, baseSnapshot);
                setBusinessAmount(candle, baseSnapshot);
                setBusinessBalance(candle, baseSnapshot);
                setLastpx(candle, baseSnapshot);
                setPxChange(candle, baseSnapshot);
                setPxChangeRate(candle, baseSnapshot);
                setCpx(candle, baseSnapshot);
                candle.setMinTime(baseSnapshot.getMinTime());
            }
            //candle.setOpen_px(baseSnapshot.getOpen_px());//因传入的数据分笔按分钟给出了开盘价，所以不同周期的都可以沿用
        }
    }
    public void  updateYear(CandleCycle cycle,Candle candle,Snapshot baseSnapshot){

        if(null!=candle&&null!=baseSnapshot){
            if(cycle==CandleCycle.Year){

                setHighpx(candle, baseSnapshot);
                setLowpx(candle, baseSnapshot);
                setBusinessAmount(candle, baseSnapshot);
                setBusinessBalance(candle, baseSnapshot);
                setLastpx(candle, baseSnapshot);
                setPxChange(candle, baseSnapshot);
                setPxChangeRate(candle, baseSnapshot);
                setCpx(candle, baseSnapshot);
                candle.setMinTime(baseSnapshot.getMinTime());
            }
            //candle.setOpen_px(baseSnapshot.getOpen_px());//因传入的数据分笔按分钟给出了开盘价，所以不同周期的都可以沿用
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
    public  void updateCommon(CandleCycle cycle,Candle candle,Snapshot baseSnapshot) {
        if(null!=candle&&null!=baseSnapshot){
            setHighpx(candle, baseSnapshot);
            setLowpx(candle, baseSnapshot);
            setBusinessAmount(candle, baseSnapshot);
            setBusinessBalance(candle, baseSnapshot);
            setLastpx(candle, baseSnapshot);
            setCpx(candle, baseSnapshot);
            setPxChange(candle, baseSnapshot);
            setPxChangeRate(candle, baseSnapshot);
        }

    }
    public  void initCandleFromSnapshot(CandleCycle cycle,Candle candle,Snapshot baseSnapshot) {
        if(null!=candle&&baseSnapshot!=null){
            if(cycle==CandleCycle.DayCandle){

                candle.setHighPx(baseSnapshot.getHighPx());
                candle.setLowPx(baseSnapshot.getLowPx());
                candle.setBusinessAmount(baseSnapshot.getBusinessAmount());
                candle.setBusinessBalance(baseSnapshot.getBusinessBalance());
                candle.setPxChange(baseSnapshot.getPxChange());
                candle.setPxChangeRate(DoubleOperationUtil.round(baseSnapshot.getPxChangeRate(), 2));
                candle.setPreClosePx(baseSnapshot.getPreclosePx());
                candle.setOpenPx(baseSnapshot.getOpenPx());
                candle.setLastSettle(baseSnapshot.getLastSettle());
                candle.setAveragePx(baseSnapshot.getAveragePx());
            }else{
                //candle.setOpen_px(baseSnapshot.getLastPx());//开盘价=最新价
                candle.setBusinessAmount(baseSnapshot.getLastAmount());//最新成交数量
                candle.setBusinessBalance(baseSnapshot.getLastAmount() * baseSnapshot.getLastPx());//最新价*最新成交数量

                candle.setClosePx(baseSnapshot.getLastPx());
                candle.setHighPx(baseSnapshot.getLastPx());
                candle.setLowPx(baseSnapshot.getLastPx());
                candle.setOpenPx(baseSnapshot.getLastPx());
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

                writeFile(redisCandle.getCycle(), JSON.toJSONString(redisCandle));
                candleMap.put(baseSnapshot.getRedisKey(), redisCandleNew);
            }else if(snapshotl==redisDatel){
                updateYear(cycle, redisCandle, baseSnapshot);//更新Redis
                candleMap.put(baseSnapshot.getRedisKey(), redisCandle);

            }
        }

    }
    private void existRedisDay(CandleCycle cycle,Snapshot baseSnapshot,  SJSCandle redisCandle){

        if(null!=redisCandle&&cycle==CandleCycle.DayCandle){
            long redisDatel=DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 8), "yyyyMMdd");
            long snapshotDatel=DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 8), "yyyyMMdd");
            if(redisDatel==snapshotDatel){//如果跟Redis相等就修改,否则就添加

                updateDay(cycle, redisCandle, baseSnapshot);//更新Redis
                candleMap.put(baseSnapshot.getRedisKey(), redisCandle);

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
                candleMap.put(redisCandleNew.getRedisKey(), redisCandleNew);
                writeFile(redisCandle.getCycle(), JSON.toJSONString(redisCandle));
            }
        }else{
            logger.error("根据rediskey 获取数据异常:");
        }

    }

    //设置价格涨跌  当前价-昨收
    public   void  setPxChange(Candle candle,Snapshot baseSnapshot){
        if(null!=candle&&null!=baseSnapshot){

            if(candle.getPreClosePx()>0){
                candle.setPxChange(DoubleOperationUtil.sub(candle.getLastPx(), candle.getPreClosePx()));

            }

        }
    }


    //设置价格涨跌幅度   （当前价-昨收/昨收）*100%

    public   void  setPxChangeRate(Candle candle,Snapshot baseSnapshot){
        if(null!=candle&&null!=baseSnapshot){

            if(candle.getPreClosePx()>0){
                candle.setPxChangeRate(DoubleOperationUtil.round(DoubleOperationUtil.div(candle.getPxChange(), candle.getPreClosePx(),4)*100,2));
            }
        }
    }
    private void  writeFile(CandleCycle cycle,String str){

        FileUtils.appendWrite(getPath(cycle),str);

    }

    public String getPath(CandleCycle cycle){
        String fileName="";
        switch (cycle){
            case DayCandle:
                fileName="njsdata_day.txt";
                break;
            case Hour1:
                fileName="njsdata_hour1.txt";
                break;

            case Hour4:
                fileName="njsdata_hour4.txt";
                break;

            case Minute1:
                fileName="njsdata_minute1.txt";
                break;

            case Minute30:
                fileName="njsdata_minute30.txt";
                break;

            case Minute5:
                fileName="njsdata_minute5.txt";
                break;

            case Month:
                fileName="njsdata_month.txt";
                break;

            case Week:
                fileName="njsdata_week.txt";
                break;

            case Year:
                fileName="njsdata_year.txt";
                break;
            case Snap:
                fileName="njsdata_snap.txt";
                break;
        }
        return "F:\\data\\sjs\\data\\"+fileName;

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
                setPxChange(redisCandle,baseSnapshot);
                setPxChangeRate(redisCandle,baseSnapshot);

                writeFile(redisCandle.getCycle(), JSON.toJSONString(redisCandle));
                candleMap.put(baseSnapshot.getRedisKey(), redisCandleNew);
            }else if(snapshotDatel==redisDatel){

                updateMonth(cycle, redisCandle, baseSnapshot);//更新Redis
                candleMap.put(baseSnapshot.getRedisKey(), redisCandle);
            }
        }

    }

    private SJSCandle getValueFromRedis(String strRedisKey){
        SJSCandle candle=null;
        try{
            if(!org.apache.commons.lang.StringUtils.isBlank(strRedisKey)){
                candle=(SJSCandle)candleMap.get(strRedisKey);

            }
        }catch (Exception ex){
            candle=null;
        }
        return candle;
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


    //适合日K以下周期
    private void existRedisCommon(CandleCycle cycle,Snapshot baseSnapshot,  SJSCandle redisCandle){

        if(null!=redisCandle){

            long redisDatel=DateUtils.getTickTime(redisCandle.getMinTime().substring(0, 12), "yyyyMMddHHmm");
            long snapshotl=DateUtils.getTickTime(baseSnapshot.getCycleTimeDate().substring(0, 12), "yyyyMMddHHmm");

            if(redisDatel==snapshotl){//如果跟Redis相等就修改,否则就添加
                updateCommon(cycle, redisCandle, baseSnapshot);//更新Redis
                candleMap.put(baseSnapshot.getRedisKey(), redisCandle);

            }else if(snapshotl>redisDatel){


                //如果不等,新生成一个
                SJSCandle redisCandleNew=new SJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                redisCandleNew.setStatus("0");

                setPxChange(redisCandle, baseSnapshot);
                setPxChangeRate(redisCandle, baseSnapshot);
                redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价


                candleMap.put(baseSnapshot.getRedisKey(), redisCandleNew);
                writeFile(redisCandle.getCycle(), JSON.toJSONString(redisCandle));
            }
        }else{
            logger.error("根据rediskey 获取数据异常:");
        }


    }
    /**
     * 判断该key是否在redis已经有
     *
     * */
    private Boolean isExistRedis(String redisKey){

        Boolean isExist=false;
        if(!org.apache.commons.lang.StringUtils.isBlank(redisKey)){

            if(candleMap.containsKey(redisKey)){
                isExist=true;
            }
        }
        return isExist;
    }
    public  void save(Snapshot baseSnapshot) throws CloneNotSupportedException {

        SJSSnapshot snapshot=(SJSSnapshot)baseSnapshot.clone();
        //修改当前成交量
        SnapshotFormate.formateSnapshotSJS(CandleCycle.Snap, snapshot);//格式化时间和rediskey
        if(candleMap.containsKey(snapshot.getRedisKey())){
            SJSSnapshot snapshotOld = (SJSSnapshot)candleMap.get(snapshot.getRedisKey());
            if(null!=snapshotOld){

                if(snapshot.getBusinessAmount()>snapshotOld.getBusinessAmount()){
                    snapshot.setLastAmount(DoubleOperationUtil.sub(snapshot.getBusinessAmount(), snapshotOld.getBusinessAmount()));
                }

            }
        }
        baseSnapshot.setLastAmount(snapshot.getLastAmount());
        SJSSnapshot minute1=(SJSSnapshot)baseSnapshot.clone();
        SJSSnapshot minute5=(SJSSnapshot)baseSnapshot.clone();
        SJSSnapshot minute30=(SJSSnapshot)baseSnapshot.clone();
        SJSSnapshot hour1=(SJSSnapshot)baseSnapshot.clone();
        SJSSnapshot dayCandle=(SJSSnapshot)baseSnapshot.clone();
        SJSSnapshot week=(SJSSnapshot)baseSnapshot.clone();
        SJSSnapshot month=(SJSSnapshot)baseSnapshot.clone();
        SJSSnapshot year=(SJSSnapshot)baseSnapshot.clone();

        week.setMinTime(dayCandle.getTradeDate()+dayCandle.getTradeTime());
        month.setMinTime(dayCandle.getTradeDate()+dayCandle.getTradeTime());
        year.setMinTime(dayCandle.getTradeDate()+dayCandle.getTradeTime());
        dayCandle.setMinTime(dayCandle.getTradeDate()+dayCandle.getTradeTime());
        //生成分时
       // writeFile(baseSnapshot.getCycle(), JSON.toJSONString(snapshot));
//        candleMap.put(baseSnapshot.getRedisKey(), baseSnapshot);


        try {

            candleMap.put(snapshot.getRedisKey(), snapshot);
             writeFile(snapshot.getCycle(), JSON.toJSONString(snapshot));


            save(CandleCycle.Minute1, minute1);
            save(CandleCycle.Minute5,minute5);
            save(CandleCycle.Minute30, minute30);
            save(CandleCycle.Hour1, hour1);
            save(CandleCycle.DayCandle, dayCandle);
            save(CandleCycle.Week, week);
            save(CandleCycle.Month, month);
            save(CandleCycle.Year, year);

        } catch (Exception e) {

            throw new RuntimeException("生成K线异常"+e.getMessage());
        }

    }
    private DecimalFormat decimalFormat = new DecimalFormat("###0.00");//格式化设置
    private String getIsRepeatKey(Snapshot snapshot){
        StringBuffer  sb=new StringBuffer();
        if(null!=snapshot) {
            SJSSnapshot temp = (SJSSnapshot) snapshot;
            if (null != snapshot) {
                sb.append(temp.getExchange());
                sb.append(temp.getProdCode());
                if(temp.getBusinessAmount()==0){
                    throw new RuntimeException("数据异常");
                }
                sb.append(decimalFormat.format(temp.getBusinessAmount()));
            }

        }
        return sb.toString();
    }

    private List<Snapshot> removeRepeat(List<Snapshot> list){
        List<Snapshot> listResult=new ArrayList();
        if(null!=list&&list.size()>0){

            for(Snapshot snapshot:list){

                if(!isRepeat(snapshot)){
                    listResult.add(snapshot);

                    isRepeat.put(getIsRepeatKey(snapshot), getIsRepeatKey(snapshot));
                    // jredisUtil.setex(getIsRepeatKey(snapshot), "true", 10000);//如果不重复，就添加

                }
            }

        }
        return listResult;
    }
    private Boolean  isRepeat(Snapshot snapshot){

        Boolean isRepeat=false;
        String redisIsRepeatKey=getIsRepeatKey(snapshot);
        if(!org.apache.commons.lang3.StringUtils.isBlank(redisIsRepeatKey)) {
            if (ImportDataSJS.isRepeat.containsKey(redisIsRepeatKey)){
                isRepeat=true;
            }

        }
        return isRepeat;
    }
    public  List<String>  getAllDataFileName(String filePath){

        List list=FileUtils.getListFiles(filePath, "", true);;
        List<String> newFileName=new ArrayList();
        remove(list,newFileName);
        return newFileName;
    }
    public  List<String>  getAllDBFileName(String filePath){

        List list=FileUtils.getListFiles(filePath, "", true);;
        // List<String> newFileName=new ArrayList();
        return list;
    }

    public void createData(){


        String filePath= "E:\\上海黄金交易所\\历史数据\\历史行情数据\\历史行情数据\\log";
        //String filePath= "F:\\data\\新建文件夹\\20151123\\111";
        List<String> list=   getAllDataFileName(filePath);
        for(String path :list){

            if(!StringUtils.isBlank(path)){
                importDataSingle(path);
            }
        }
    }
    public static void main(String[] args){
        ImportDataSJS importDataSJS=new ImportDataSJS();
        importDataSJS.createData();
    }

    public  void importRedisSnap(){

        List<String> list=   getAllDBFileName(HQDataInit.getRedisFilePath()+File.separator+"snapshot"+File.separator+"sjs"+File.separator);
        if(null!=list) {
            for (String fileName : list) {

                File file = new File(fileName);
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    List resultList=new ArrayList();
                    while ((tempString = reader.readLine()) != null) {
                        if(!StringUtils.isBlank(tempString)){

                            SJSSnapshot snap=JSON.parseObject(tempString,SJSSnapshot.class);
                            hqService.insertRedisSnapshotHistory(snap);
                        }
                    }
                    reader.close();
                    file.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e1) {
                            System.out.println(e1.getMessage());
                        }
                    }
                }
            }

        }
    }
    public  void importDBSnap(){

        List<String> list=   getAllDBFileName(HQDataInit.getDbFilePath()+File.separator+"snapshot"+File.separator+"sjs"+File.separator);
        if(null!=list) {
            for (String fileName : list) {

                File file = new File(fileName);
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    List resultList=new ArrayList();
                    while ((tempString = reader.readLine()) != null) {
                        if(!StringUtils.isBlank(tempString)){

                            SJSSnapshot snap=JSON.parseObject(tempString,SJSSnapshot.class);
                            resultList.add(snap);
                            if(resultList.size()==50){
                                sjsSnapshotDao.insertBatch(resultList);
                                resultList.clear();
                            }


                        }
                    }
                    sjsSnapshotDao.insertBatch(resultList);
                    reader.close();
                    file.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e1) {
                            System.out.println(e1.getMessage());
                        }
                    }
                }
            }

        }
    }
    public  void importRedisCandle(){

        List<String> list=   getAllDBFileName(HQDataInit.getRedisFilePath()+File.separator+"candle"+File.separator+"sjs"+File.separator);
        if(null!=list) {
            for (String fileName : list) {

                File file = new File(fileName);
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    List insertList=new ArrayList();

                    // 一次读入一行，直到读入null为文件结束
                    while ((tempString = reader.readLine()) != null) {
                        if(!StringUtils.isBlank(tempString)){

                            SJSCandle candle=JSON.parseObject(tempString,SJSCandle.class);
                            hqService.insertRedisCandleHistory(candle.getCycle(), candle);
                        }
                    }
                    reader.close();
                    file.delete();//写完Redis就删除

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e1) {
                            System.out.println(e1.getMessage());
                        }
                    }
                }
            }

        }
    }

    public  void importDBCandle(){

        List<String> list=   getAllDBFileName(HQDataInit.getDbFilePath()+File.separator+"candle"+File.separator+"sjs"+File.separator);

        if(null!=list) {
            for (String fileName : list) {

                File file = new File(fileName);
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    List insertList=new ArrayList();

                    // 一次读入一行，直到读入null为文件结束
                    while ((tempString = reader.readLine()) != null) {
                        if(!StringUtils.isBlank(tempString)){

                            SJSCandle candle=JSON.parseObject(tempString,SJSCandle.class);
                            insertList.add(candle);
                            if(insertList.size()==100){
                                sjsCandleDao.insertBatch(insertList);
                                insertList.clear();
                            }
                        }
                    }
                    sjsCandleDao.insertBatch(insertList);
                    insertList.clear();
                    reader.close();
                    file.delete();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e1) {
                            System.out.println(e1.getMessage());
                        }
                    }
                }
            }

        }
    }

    //获取行情文件
    private static void remove(List<String> listFileName,List<String> newFileName){

        if(null!=listFileName&&null!=newFileName){

            for(String str:listFileName){


                if(!StringUtils.isBlank(str)){
                    if(str.contains("AirSave_")){
                        newFileName.add(str);
                    }
                }
            }
            Collections.sort(newFileName);
        }
    }


}
