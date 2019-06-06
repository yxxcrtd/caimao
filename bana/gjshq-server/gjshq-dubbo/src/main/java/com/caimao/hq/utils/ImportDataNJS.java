package com.caimao.hq.utils;


import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.core.HQDataInit;
import com.caimao.hq.dao.NJSCandleDao;
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

@Service("importDataNJS")
public class ImportDataNJS extends  CandleUtils{
    private Logger logger = Logger.getLogger(ImportDataNJS.class);

    @Autowired
    private NJSCandleDao njsCandleDao;

    @Autowired
    private IHQService hqService;

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
    public static double parseDouble(String value) {
        if (StringUtils.isBlank(value)) {
            // This is an invalid value.
            return 0.00;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {

        }
        // This is an invalid value.
        return 0.00;
    }


    public  void   readDataFromDB(){



    }
    public  void importDataSingle(){

        try {
            ReadExcel excel = new ReadExcel("E:\\南交所\\history\\20141030.xls");
            List<Object> root = excel.excelToListList(0);
            root.remove(0);

            List<String>  temp=null;

            for (Object l2 : root) {
                try{
                    List dataList=new ArrayList();

                    temp=(List)l2;
                    if(null!=temp) {
                        NJSSnapshot njsCandle = new NJSSnapshot();
                        njsCandle.setExchange("NJS");
                        njsCandle.setMinTime(String.valueOf(temp.get(0)) + "000000");
                        njsCandle.setProdCode(temp.get(1));
                        njsCandle.setProdName(temp.get(2));
                        njsCandle.setOpenPx(parseDouble(String.valueOf(temp.get(3))));
                        njsCandle.setHighPx(parseDouble(String.valueOf(temp.get(4))));
                        njsCandle.setLowPx(parseDouble(String.valueOf(temp.get(5))));
                        njsCandle.setPxChange(parseDouble(String.valueOf(temp.get(7))));
                        njsCandle.setPxChangeRate(DoubleOperationUtil.round(parseDouble(String.valueOf(temp.get(8))), 2));
                        njsCandle.setBusinessAmount(parseDouble(String.valueOf(temp.get(10))));
                        njsCandle.setBusinessBalance(parseDouble(String.valueOf(temp.get(11))));
                        njsCandle.setLastSettle(parseDouble(String.valueOf(temp.get(12))));
                        njsCandle.setPreclosePx(parseDouble(String.valueOf(temp.get(12))));
                        njsCandle.setLastPx(parseDouble(String.valueOf(temp.get(6))));
                        njsCandle.setClosePx(parseDouble(String.valueOf(temp.get(6))));
                        njsCandle.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                        njsCandle.setStatus("0");
                        njsCandle.setIsGoods(1);
                        njsCandle.setLastAmount(njsCandle.getBusinessAmount());

                        if(njsCandle.getLowPx()==0||njsCandle.getOpenPx()==0){
                            continue;
                        }

                        if(isDiv(temp.get(1))){
                            if(njsCandle.getLowPx()>1000){

                                njsCandle.setOpenPx(parseDouble(String.valueOf(temp.get(3))) / 100);
                                njsCandle.setHighPx(parseDouble(String.valueOf(temp.get(4))) / 100);
                                njsCandle.setLowPx(parseDouble(String.valueOf(temp.get(5))) / 100);
                                njsCandle.setPxChange(parseDouble(String.valueOf(temp.get(7))) / 100);
                                njsCandle.setBusinessAmount(parseDouble(String.valueOf(temp.get(10))) * 100);
                                njsCandle.setLastSettle(parseDouble(String.valueOf(temp.get(12))) / 100);
                                njsCandle.setPreclosePx(parseDouble(String.valueOf(temp.get(12))) / 100);
                                njsCandle.setLastPx(parseDouble(String.valueOf(temp.get(6)))/100);
                                njsCandle.setClosePx(parseDouble(String.valueOf(temp.get(6)))/100);

                            }
                        }
                        save(njsCandle);
                    }
                }catch(Exception ex){

                    System.out.println(ex);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public Boolean isDiv(String prodCode){

        Boolean isDiv=false;
        if("AG".equalsIgnoreCase(prodCode)||"DY2O3".equalsIgnoreCase(prodCode)||"LU2O3".equalsIgnoreCase(prodCode)||"TM2O3".equalsIgnoreCase(prodCode)
                ||"TB4O7".equalsIgnoreCase(prodCode)||"EU2O3".equalsIgnoreCase(prodCode)||"IN".equalsIgnoreCase(prodCode)){
            isDiv=true;
        }
        return  isDiv;
    }

    private void save(CandleCycle cycle,Snapshot baseSnapshot) {

        if(null==cycle||null==baseSnapshot){
            return;
        }
        SnapshotFormate.formateSnapshotNJS(cycle, baseSnapshot);//格式化时间和rediskey
        //如果redis有对应的key
        if(isExistRedis(baseSnapshot.getRedisKey())){

            existRedis(cycle,baseSnapshot);

        } else {
            notExistRedis(cycle, baseSnapshot);

        }
    }

    private  void  notExistRedis(CandleCycle cycle,Snapshot baseSnapshot){

        //Redis没有当前品种的周期，就新建一个放入redis
        NJSCandle redisCandleNew=new NJSCandle();
        initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
        redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
        redisCandleNew.setRedisKey(baseSnapshot.getRedisKey());
        redisCandleNew.setStatus("0");
        //只适合导入日K数据使用，因为日K的最低价，最高价 不是最新价
        redisCandleNew.setLowPx(baseSnapshot.getLowPx());
        redisCandleNew.setHighPx(baseSnapshot.getHighPx());
        redisCandleNew.setOpenPx(baseSnapshot.getOpenPx());
        candleMap.put(baseSnapshot.getRedisKey(), redisCandleNew);


    }

    private void existRedisWeek(CandleCycle cycle,Snapshot baseSnapshot, NJSCandle redisCandle){

        if(cycle==CandleCycle.Week){//如果是周K

            int newWeek= Integer.parseInt(DateUtils.getWeekOfDate(baseSnapshot.getCycleTimeDate(), "yyyyMMddHHmm")) ;
            int oldWeek= Integer.parseInt(DateUtils.getWeekOfDate(redisCandle.getMinTime(), "yyyyMMddHHmm")) ;
            if(newWeek<oldWeek){//说明是新的一周

                //如果不等,新生成一个
                NJSCandle redisCandleNew=new NJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                redisCandleNew.setRedisKey(baseSnapshot.getRedisKey());
                redisCandleNew.setStatus("0");
                redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价
                redisCandleNew.setOpenPx(baseSnapshot.getOpenPx());
                redisCandleNew.setHighPx(baseSnapshot.getHighPx());
                redisCandleNew.setLowPx(baseSnapshot.getLowPx());
                writeFile(redisCandle.getCycle(), JSON.toJSONString(redisCandle));
                candleMap.put(baseSnapshot.getRedisKey(),redisCandleNew);
            }else{
                update(cycle, redisCandle, baseSnapshot);//更新Redis
                candleMap.put(baseSnapshot.getRedisKey(), redisCandle);
                // saveRedis(baseSnapshot.getRedisKey(), redisCandle);
            }

        }

    }
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
                candle.setPxChange(baseSnapshot.getPxChange());
                candle.setPxChangeRate(baseSnapshot.getPxChangeRate());
            }else{

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
            candle.setSettle(baseSnapshot.getSettle());
            candle.setLastSettle(baseSnapshot.getLastSettle());
            candle.setCycle(cycle);
            candle.setMinTime(baseSnapshot.getCycleTimeDate());
            //candle.setOpen_px(baseSnapshot.getOpen_px());//因传入的数据分笔按分钟给出了开盘价，所以不同周期的都可以沿用
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
    private void existRedisYear(CandleCycle cycle,Snapshot baseSnapshot,  NJSCandle redisCandle){

        if(cycle==CandleCycle.Year){//如果是年

            if (SJSMinTimeUtil.isNewYear(baseSnapshot.getCycleTimeDate(),redisCandle.getMinTime())) {//说明是新的一年

                //如果不等,新生成一个
                NJSCandle redisCandleNew=new NJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                redisCandleNew.setRedisKey(baseSnapshot.getRedisKey());
                redisCandleNew.setStatus("0");
                redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价
                redisCandleNew.setOpenPx(baseSnapshot.getOpenPx());
                redisCandleNew.setHighPx(baseSnapshot.getHighPx());
                redisCandleNew.setLowPx(baseSnapshot.getLowPx());
                writeFile(redisCandle.getCycle(), JSON.toJSONString(redisCandle));
                candleMap.put(baseSnapshot.getRedisKey(), redisCandleNew);


//                saveDB(redisCandle);//把原来的Redis保存到数据库
//                saveRedis(baseSnapshot.getRedisKey(), redisCandleNew);
            }else{
                update(cycle, redisCandle, baseSnapshot);//更新Redis


                candleMap.put(baseSnapshot.getRedisKey(), redisCandle);

               // saveRedis(baseSnapshot.getRedisKey(), redisCandle);
            }
        }

    }

    public void  writeFile(CandleCycle cycle,String str){

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
       return "F:\\data\\njs\\"+fileName;

    }
    private void existRedisMonth(CandleCycle cycle,Snapshot baseSnapshot,  NJSCandle redisCandle){

        if(cycle==CandleCycle.Month){

            if (SJSMinTimeUtil.isNewMonth(baseSnapshot.getCycleTimeDate(), redisCandle.getMinTime())) {//说明是新的一月

                //如果不等,新生成一个
                NJSCandle redisCandleNew=new NJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                redisCandleNew.setRedisKey(baseSnapshot.getRedisKey());
                redisCandleNew.setStatus("0");
                redisCandleNew.setPreClosePx(redisCandle.getClosePx());//设置收盘价
                redisCandleNew.setOpenPx(baseSnapshot.getOpenPx());
                redisCandleNew.setHighPx(baseSnapshot.getHighPx());
                redisCandleNew.setLowPx(baseSnapshot.getLowPx());
//                saveDB(redisCandle);//把原来的Redis保存到数据库
//                saveRedis(baseSnapshot.getRedisKey(), redisCandleNew);
                writeFile(redisCandle.getCycle(), JSON.toJSONString(redisCandle));
                candleMap.put(baseSnapshot.getRedisKey(), redisCandleNew);
            }else{

                update(cycle, redisCandle, baseSnapshot);//更新Redis
                candleMap.put(baseSnapshot.getRedisKey(), redisCandle);
                //saveRedis(baseSnapshot.getRedisKey(), redisCandle);
            }
        }

    }

    private NJSCandle getValueFromRedis(String strRedisKey){
        NJSCandle candle=null;
        try{
            if(!org.apache.commons.lang.StringUtils.isBlank(strRedisKey)){
                 candle=(NJSCandle)candleMap.get(strRedisKey);

            }
        }catch (Exception ex){
            candle=null;
        }
        return candle;
    }
    private  void  existRedis(CandleCycle cycle,Snapshot baseSnapshot){

        NJSCandle redisCandle=getValueFromRedis(baseSnapshot.getRedisKey());
        if(cycle==CandleCycle.Week){

            existRedisWeek(cycle, baseSnapshot, redisCandle);
        }else if(cycle==CandleCycle.Month){
            existRedisMonth(cycle,baseSnapshot,redisCandle);
        }else if(cycle==CandleCycle.Year){

            existRedisYear(cycle, baseSnapshot, redisCandle);

        }else{
            existRedisCommon(cycle, baseSnapshot, redisCandle);
        }

    }


    //适合日K以下周期
    private void existRedisCommon(CandleCycle cycle,Snapshot baseSnapshot,  NJSCandle redisCandle){

        if(null!=redisCandle){

            if(redisCandle.getMinTime().equals(baseSnapshot.getCycleTimeDate())){//如果跟Redis相等就修改,否则就添加



                update(cycle, redisCandle, baseSnapshot);//更新Redis
                candleMap.put(baseSnapshot.getRedisKey(), redisCandle);

            }else{

                //如果不等,新生成一个
                NJSCandle redisCandleNew=new NJSCandle();
                initCandleFromSnapshot(cycle, redisCandleNew, baseSnapshot);
                redisCandleNew.setOptDate(DateUtils.getNoTime("yyyyMMddHHmmss"));
                redisCandleNew.setRedisKey(baseSnapshot.getRedisKey());
                redisCandleNew.setStatus("0");
                candleMap.put(baseSnapshot.getRedisKey(), redisCandleNew);
                writeFile(redisCandle.getCycle(), JSON.toJSONString(redisCandle));
                   //用于导入日K的数据，最后一天，直接写入到文件
//                if(redisCandleNew.getMinTime().contains("20151123")){
//
//                    writeFile(redisCandleNew.getCycle(), JSON.toJSONString(redisCandleNew));
//                }
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
    public  void save(Snapshot baseSnapshot) {



        try {



            NJSSnapshot dayCandle=(NJSSnapshot)baseSnapshot.clone();
            save(CandleCycle.DayCandle, dayCandle);


            NJSSnapshot week=(NJSSnapshot)baseSnapshot.clone();
            save(CandleCycle.Week, week);

            NJSSnapshot month=(NJSSnapshot)baseSnapshot.clone();
            save(CandleCycle.Month, month);


            NJSSnapshot year=(NJSSnapshot)baseSnapshot.clone();
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
            if (ImportDataNJS.isRepeat.containsKey(redisIsRepeatKey)){
                isRepeat=true;
            }

        }
        return isRepeat;
    }
    public  List<String>  getAllDataFileName(String filePath){

        List list=FileUtils.getListFiles(filePath, "", true);;
        List<String> newFileName=new ArrayList();
        remove(list, newFileName);
        return newFileName;
    }
    public  List<String>  getAllDBFileName(String filePath){

        List list=FileUtils.getListFiles(filePath, "", true);;
       // List<String> newFileName=new ArrayList();
        return list;
    }

    public void createData(){


        importDataSingle();
    }
    public static void main(String[] args){
        ImportDataNJS importDataNJS=new ImportDataNJS();
        importDataNJS.createData();
    }


    public  void importRedisCandle(){
        List<String> list=   getAllDBFileName(HQDataInit.getRedisFilePath()+File.separator+"candle"+File.separator+"njs"+File.separator);

        if(null!=list) {
            for (String fileName : list) {
                File file = new File(fileName);
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    // 一次读入一行，直到读入null为文件结束
                    while ((tempString = reader.readLine()) != null) {
                        if(!StringUtils.isBlank(tempString)){

                           NJSCandle candle=JSON.parseObject(tempString,NJSCandle.class);
                            hqService.insertRedisCandleHistory(candle.getCycle(),candle);
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
    public  void importDBCandle(){
        List<String> list=   getAllDBFileName(HQDataInit.getDbFilePath() +File.separator+"candle"+File.separator+"njs"+File.separator);
        if(null!=list) {
            String candleId="";
            for (String fileName : list) {

                File file = new File(fileName);
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    // 一次读入一行，直到读入null为文件结束
                    while ((tempString = reader.readLine()) != null) {
                        if(!StringUtils.isBlank(tempString)){

                            NJSCandle candle=JSON.parseObject(tempString,NJSCandle.class);
                            NJSCandle   queryDBOBJ=new NJSCandle();
                            queryDBOBJ.setExchange(candle.getExchange());
                            queryDBOBJ.setProdCode(candle.getProdCode());
                            queryDBOBJ.setMinTime(candle.getMinTime());
                            queryDBOBJ.setCycle(candle.getCycle());
                            queryDBOBJ.setNumber(10);
                            List<NJSCandle> queryResult=njsCandleDao.selectList(queryDBOBJ);
                            if(null!=queryResult&&queryResult.size()==0){
                                candleId=String.valueOf(queryResult.get(0).getCandleId()) ;
                                if(!StringUtils.isBlank(candleId)){
                                    njsCandleDao.deleteByPK(candleId);
                                }
                            }
                             njsCandleDao.insert(candle);
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
