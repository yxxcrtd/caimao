package com.caimao.hq.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.hq.api.entity.Candle;
import com.caimao.hq.api.entity.CandleCycle;
import com.caimao.hq.api.entity.Snapshot;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * 用来生成新线的日期
 *
 * K线时间返回的时间格式取决于请求的K线类型对于分钟级别的K线，返回格式为YYYYmmddHHMM对于其它周日的K线，返回格式为YYYYmmdd
 *
 * @author
 *
 */
public class NJSMinTimeUtil {

    static Logger logger = Logger.getLogger(NJSMinTimeUtil.class.getName());
    // 格式为交易所代码+产品代码+日期（yyyyMMddHHmm）type=0 表示向前，1表示向后
//    public static String getRedisKey(CandleCycle cycle, BaseSnapshot baseSnapshot,String type) {
//
//        String time="";
//        StringBuffer sb = new StringBuffer();
//        if (null != baseSnapshot) {
//            if (SnapshotFormate.isAuth(baseSnapshot)) {
//                if("0".equals(type)){
//                    time=getPreTime(cycle,baseSnapshot);
//                }else{
//                    time=getTime(cycle,baseSnapshot);
//                }
//
//                if(!StringUtils.isBlank(time)){
//                    sb.append(baseSnapshot.getFinance_mic());
//                    sb.append(baseSnapshot.getProd_code());
//                    sb.append(time);
//                }
//            }
//        }
//        return sb.toString();
//    }
    //格式为交易所代码+产品代码+cycle
    public static String getRedisKey(CandleCycle cycle, Snapshot baseSnapshot) {

        StringBuffer sb = new StringBuffer();
        if (null != baseSnapshot) {
            if (SnapshotFormate.isAuth(baseSnapshot)) {
                sb.append(baseSnapshot.getExchange());
                sb.append(baseSnapshot.getProdCode());
                sb.append(cycle);
            }
        }
        return sb.toString();
    }
    public static String getRedisKeyImport(CandleCycle cycle, Snapshot baseSnapshot,String importField) {

        StringBuffer sb = new StringBuffer();
        if (null != baseSnapshot) {
            if (SnapshotFormate.isAuth(baseSnapshot)) {
                sb.append(importField);//用过的关键词： import1
                sb.append(baseSnapshot.getExchange());
                sb.append(baseSnapshot.getProdCode());
                sb.append(cycle);
            }
        }
        return sb.toString();
    }

    public static String getRedisKey(CandleCycle cycle, Candle candle) {

        StringBuffer sb = new StringBuffer();
        if (null != candle) {
            sb.append(candle.getExchange());
            sb.append(candle.getProdCode());
            sb.append(cycle);
        }
        return sb.toString();
    }


    //格式为交易所代码+产品代码+cycle
    public static String getRedisKey(CandleCycle cycle, String exchange,String prodCode) {

        StringBuffer sb = new StringBuffer();
        if (!StringUtils.isBlank(exchange)&&!StringUtils.isBlank(prodCode)) {
            sb.append(exchange);
            sb.append(prodCode);
            sb.append(cycle);
        }
        return sb.toString();
    }
    // 获取行情数据rediskey
    public static String getRedisKeySnapshot(Snapshot baseSnapshot) {

        String time="";
        StringBuffer sb = new StringBuffer();
        if (null != baseSnapshot) {

            if (SnapshotFormate.isAuth(baseSnapshot)) {
                time=getDayCommon(baseSnapshot);
                if(!StringUtils.isBlank(time)){
                    sb.append(baseSnapshot.getExchange());
                    sb.append(baseSnapshot.getProdCode());
                    sb.append(time);
                    sb.append(CandleCycle.Snap);
                }
            }
        }
        return sb.toString();
    }

    public static String getTime(CandleCycle cycle, Snapshot baseSnapshot){

        String resultDate="";
        if(null!=cycle&&null!=baseSnapshot){

            switch (cycle){
                case DayCandle:
                    resultDate=getDay(baseSnapshot);
                    break;
                case Hour1:
                    resultDate=getHour1(baseSnapshot);
                    break;
                case Hour4:
                    resultDate=getHour4(baseSnapshot);
                    break;
                case Minute1:
                    resultDate=getMinue1(baseSnapshot);
                    break;
                case Minute30:
                    resultDate=getMinue30(baseSnapshot);
                    break;
                case Minute5:
                    resultDate=getMinue5(baseSnapshot);
                    break;
                case Week:
                    resultDate=getWeek(baseSnapshot);
                    break;
                case Month:
                    resultDate=getMonth(baseSnapshot);
                    break;
                case Year:
                    resultDate=getYear(baseSnapshot);
                    break;
                case Snap:
                    resultDate=getSnap(baseSnapshot);
                    break;
            }
        }
        return resultDate;
    }




    public static String getPreTime(CandleCycle cycle, Snapshot baseSnapshot){

        String resultDate="";
        if(null!=cycle&&null!=baseSnapshot) {

            switch (cycle){
                case DayCandle:
                    resultDate=getPreDay(baseSnapshot);
                    break;
                case Hour1:
                    resultDate=getPreHour1(baseSnapshot);
                    break;
                case Hour4:
                    resultDate=getPreHour4(baseSnapshot);
                    break;
                case Minute1:
                    resultDate=getPreMinue1(baseSnapshot);
                    break;
                case Minute30:
                    resultDate=getPreMinue30(baseSnapshot);
                    break;
                case Minute5:
                    resultDate=getPreMinue5(baseSnapshot);
                    break;
                case Week:
                    resultDate=getPreWeek(baseSnapshot);
                    break;
                case Month:
                    resultDate=getPreMonth(baseSnapshot);
                    break;

                case Snap:
                    resultDate=getSnap(baseSnapshot);
                    break;
            }
        }
        return resultDate;
    }

    /**
     * 获取12位格式化后的 1分钟行情时间
     */
    public static String getMinue1(Snapshot baseSnapshot) {

        String day = getMinueCommon(baseSnapshot);
        String resultDay = "";//转换后的分钟
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = format.parse(day);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, 1);// 24小时制
            date = cal.getTime();
            resultDay = format.format(date);
            resultDay = resultDay.substring(0, 12);
        } catch (Exception ex) {
            logger.error("1分钟行情日期格式化错误，传入的行情时间转换错误:" + ex.getMessage());
            throw new RuntimeException("1分钟行情日期格式化错误，传入的行情时间转换错误:" + ex.getMessage());
        }


        return resultDay;
    }
    /**
     * 获取12位格式化后的 1分钟行情时间
     */
    public static String getPreMinue1(Snapshot baseSnapshot) {

        String day = getMinueCommon(baseSnapshot);

        String resultDay = "";//转换后的分钟
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = format.parse(day);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, -1);// 24小时制
            date = cal.getTime();
            resultDay = format.format(date);
            resultDay = resultDay.substring(0, 12);
        } catch (Exception ex) {
            logger.error("pre 1分钟行情日期格式化错误，传入的行情时间转换错误:" + ex.getMessage());
            throw new RuntimeException("1分钟行情日期格式化错误，传入的行情时间转换错误:" + ex.getMessage());
        }
        return resultDay;
    }

    private static String getMinueCommon(Snapshot baseSnapshot) {

        String miniTime = "";
        String nowTime = "";
        if (null != baseSnapshot) {

            miniTime = baseSnapshot.getMinTime();
//            nowTime = getDateNowe("yyyyMMddHHmmss");
//            if (!StringUtils.isBlank(miniTime)) {
//                //长度如果为14位，20150929093545，非日K线日期,正确日期
//                if (miniTime.length() == 14) {
//                    //判断日期是否合法,合法就返回0
//                    if (compare_date(miniTime, nowTime) != 0) {
//                        miniTime = "";
//                        throw new RuntimeException("南交所传入的行情数据日期格式不合法，必须与当前时间间隔为不超过10分钟");
//                    }
//                } else {
//                    logger.error("南交所传入的行情数据日期格式不合法，长度必须为14位");
//                    throw new RuntimeException("南交所传入的行情数据日期格式不合法，长度必须为14位");
//                }
//            }
        }
        return miniTime;
    }

    public static Boolean  isNewDay(String time){
        Boolean isNewTime=false;
        long time1=0;
        long time2=0;
        if(!StringUtils.isBlank(time)){
            time1=DateUtils.getTickTime(time, "yyyyMMddHHmmss");
            time2=DateUtils.getTickTime(DateUtils.getNoTime("yyyyMMdd") + "190000", "yyyyMMddHHmmss");

            if(DoubleOperationUtil.sub(time1,time2)>=0){
                isNewTime=true;

            }

        }
        return isNewTime;
    }
    private static String getDayCommon(Snapshot baseSnapshot) {

        String miniTime = "";
        if (null != baseSnapshot) {

            miniTime = baseSnapshot.getMinTime();

            if (!StringUtils.isBlank(miniTime)) {
                if (miniTime.length() == 14) {
                    if(isNewDay(miniTime)){
                        miniTime=miniTime.substring(0, 8);
                        miniTime= DateUtils.addDay(miniTime,1,"yyyyMMdd");
                    }else{
                        miniTime=miniTime.substring(0, 8);
                    }
                } else {
                    logger.error("南交所传入的行情数据日期格式不合法，长度必须为14位");
                    throw new RuntimeException("南交所传入的行情数据日期格式不合法，长度必须为14位");
                }
            }else {
                logger.error("南交所传入的行情数据日期格式不合法，日期为空,getMin_time is null");
                throw new RuntimeException("南交所传入的行情数据日期格式不合法，日期为空,getMin_time is null");
            }
        }
        return miniTime+"0000";
    }


    private static String getMonthCommon(Snapshot baseSnapshot) {

        String miniTime = "";
        if (null != baseSnapshot) {

            miniTime = baseSnapshot.getMinTime();
            if (!StringUtils.isBlank(miniTime)) {
                //长度如果为14位，20150929093545，非日K线日期,正确日期
                if (miniTime.length() == 14) {
                    miniTime=miniTime.substring(0,6);
                } else {
                    logger.error("南交所传入的行情数据日期格式不合法，长度必须为14位");
                    throw new RuntimeException("南交所传入的行情数据日期格式不合法，长度必须为14位");
                }
            }else {
                logger.error("南交所传入的行情数据日期格式不合法，日期为空,getMin_time is null");
                throw new RuntimeException("南交所传入的行情数据日期格式不合法，日期为空,getMin_time is null");
            }
        }
        return miniTime+"000000";
    }

    private static String getYearCommon(Snapshot baseSnapshot) {

        String miniTime = "";
        String nowTime = "";
        if (null != baseSnapshot) {

            miniTime = baseSnapshot.getMinTime();
            if (!StringUtils.isBlank(miniTime)) {
                //长度如果为14位，20150929093545，非日K线日期,正确日期
                if (miniTime.length() == 14) {

                    miniTime=miniTime.substring(0,4);

                } else {
                    logger.error("南交所传入的行情数据日期格式不合法，长度必须为14位");
                    throw new RuntimeException("南交所传入的行情数据日期格式不合法，长度必须为14位");
                }
            }else {
                logger.error("南交所传入的行情数据日期格式不合法，日期为空,getMin_time is null");
                throw new RuntimeException("南交所传入的行情数据日期格式不合法，日期为空,getMin_time is null");
            }
        }
        return miniTime+"00000000";
    }
    private Boolean checkMinue(Snapshot baseSnapshot){
        Boolean isRight=false;
        if(null==baseSnapshot){
            isRight=false;
        }
        try{
            String  minit_time=getMinueCommon(baseSnapshot);
            if(!StringUtils.isBlank(minit_time)){
                isRight=true;
            }
        }catch (Exception ex){

            logger.error("获取日期错误，传入的对象为baseSnapshot="+baseSnapshot.toString());
            isRight=false;
            throw new RuntimeException("获取日期错误，传入的对象为baseSnapshot="+baseSnapshot.toString());
        }
        return isRight;
    }
    /**
     * 获取12位格式化后的 5分钟行情时间
     */
    public static String getMinue5(Snapshot baseSnapshot) {

        if(null==baseSnapshot){
            return "";
        }
        String result="";
        String miniTime = "";//12位长度
        int tempMinue = 0;//2位分钟数
        miniTime = getMinueCommon(baseSnapshot);
        if (!StringUtils.isBlank(miniTime)&&miniTime.length()==14) {

            StringBuffer tempTime=new StringBuffer();
            tempTime.append(miniTime.substring(0,11));
            tempTime.append("0");
            //获取日期最后2位，为分钟数
            tempMinue = Integer.parseInt(miniTime.substring(10, 12));
            if (tempMinue < 0 || tempMinue > 60) {
                logger.error("获取5分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
                throw new RuntimeException("获取5分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
            } else {
                //判断个位是否大于5
                if (tempMinue % 10 < 5) {
                    result= timeAdd(tempTime.toString(), 5);
                    //判断个位是否大于5，就分钟数加5
                } else {
                    //判断个位是否大于5，就分钟数加10
                    result= timeAdd(tempTime.toString(), 10);
                }
            }

        }else{

            logger.error("获取30分钟日期格式错误：长度必须为12位，miniTime="+miniTime);
            throw new RuntimeException("获取30分钟日期格式错误：长度必须为12位，miniTime="+miniTime);
        }
        return result;
    }
    public static String getPreMinue5(Snapshot baseSnapshot) {

        if(null==baseSnapshot){
            return "";
        }
        String result="";
        String miniTime = "";//12位长度
        int tempMinue = 0;//2位分钟数
        miniTime = getMinueCommon(baseSnapshot);

        if (!StringUtils.isBlank(miniTime)&&miniTime.length()==14) {

            StringBuffer tempTime=new StringBuffer();
            tempTime.append(miniTime.substring(0,11));
            tempTime.append("0");
            //获取日期最后2位，为分钟数
            tempMinue = Integer.parseInt(miniTime.substring(10, 12));
            if (tempMinue < 0 || tempMinue > 60) {
                logger.error("获取pre 5分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
                throw new RuntimeException("获取 pre 5分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
            } else {
                //判断个位是否大于5
                if (tempMinue % 10 < 5) {
                    result= timeAdd(tempTime.toString(), -5);
                    //判断个位是否大于5，就分钟数加5
                } else {
                    //判断个位是否大于5，就分钟数加10
                    result= timeAdd(tempTime.toString(), -10);
                }
            }
        }else{
            logger.error("获取pre 5分钟日期格式错误：长度必须为12位，miniTime="+miniTime);
            throw new RuntimeException("获取pre 5分钟日期格式错误：长度必须为12位，miniTime="+miniTime);
        }
        return result;
    }
    /**
     *
     * sourceTime  12位日期格式
     * */
    private static String  timeAdd(String sourceTime,int intMinue){

        if(StringUtils.isBlank(sourceTime)){
            return "";
        }

        String resultDay = "";//转换后的分钟
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = null;
        try {
            date = format.parse(sourceTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, intMinue);// 24小时制
            date = cal.getTime();
            resultDay = format.format(date);
            cal = null;
        } catch (Exception ex) {
            logger.error("日期格式化错误，传入的行情时间转换错误:" + ex.getMessage());
            throw new RuntimeException("日期格式化错误，传入的行情时间转换错误:" + ex.getMessage());
        }
        return resultDay;
    }








    public static String getMinue30(Snapshot baseSnapshot) {
        if(null==baseSnapshot){
            return "";
        }
        String result="";
        String miniTime = "";//12位长度
        int tempMinue = 0;//2位分钟数

        miniTime = getMinueCommon(baseSnapshot);
        if (!StringUtils.isBlank(miniTime)&&miniTime.length()==14) {
            //初始化时间格式为201509280900
            StringBuffer tempTime=new StringBuffer();
            tempTime.append(miniTime.substring(0,10));
            tempTime.append("00");
            //获取日期最后2位，为分钟数
            tempMinue = Integer.parseInt(miniTime.substring(10, 12));
            if (tempMinue < 0 || tempMinue > 60) {

                logger.error("获取30分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
                throw new RuntimeException("获取30分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
            } else {

                if (tempMinue < 30) {
                    result= timeAdd (tempTime.toString(),30);
                } else {
                    result= timeAdd(tempTime.toString(),60);
                }
            }

        }else{

            logger.error("获取30分钟日期格式错误：长度必须为12位，miniTime="+miniTime);
            throw new RuntimeException("获取30分钟日期格式错误：长度必须为12位，miniTime="+miniTime);
        }
        return result;
    }
    public static String getPreMinue30(Snapshot baseSnapshot) {
        if(null==baseSnapshot){
            return "";
        }
        String result="";
        String miniTime = "";//12位长度
        int tempMinue = 0;//2位分钟数

        miniTime = getMinueCommon(baseSnapshot);
        if (!StringUtils.isBlank(miniTime)&&miniTime.length()==14) {

            //初始化时间格式为201509280900
            StringBuffer tempTime=new StringBuffer();
            tempTime.append(miniTime.substring(0,10));
            tempTime.append("00");
            //获取日期最后2位，为分钟数
            tempMinue = Integer.parseInt(miniTime.substring(10, 12));
            if (tempMinue < 0 || tempMinue > 60) {
                logger.error("获取pre 30分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
                throw new RuntimeException("获取pre 30分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
            } else {

                if (tempMinue < 30) {
                    result= timeAdd(tempTime.toString(), -30);
                } else {
                    result= timeAdd(tempTime.toString(), -60);
                }
            }
        }else{
            logger.error("获取30分钟日期格式错误：长度必须为12位，miniTime="+miniTime);
            throw new RuntimeException("获取30分钟日期格式错误：长度必须为12位，miniTime="+miniTime);
        }
        return result;
    }
    public static String getHour1(Snapshot baseSnapshot) {

        if(null==baseSnapshot){
            return "";
        }
        String result="";
        String miniTime = "";//12位长度
        int tempMinue = 0;//2位分钟数
        miniTime = getMinueCommon(baseSnapshot);
        if (!StringUtils.isBlank(miniTime)&&miniTime.length()==14) {
            StringBuffer tempTime=new StringBuffer();
            tempTime.append(miniTime.substring(0,10));
            tempTime.append("00");
            //获取日期最后2位，为分钟数
            tempMinue = Integer.parseInt(miniTime.substring(10, 12));
            if (tempMinue < 0 || tempMinue > 60) {
                logger.error("获取60分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
                throw new RuntimeException("获取60分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
            } else {
                result= timeAdd(tempTime.toString(), 60);
            }
        }else{
            logger.error("获取60分钟日期格式错误：长度必须为12位，miniTime=" + miniTime);
            throw new RuntimeException("获取60分钟日期格式错误：长度必须为12位，miniTime=" + miniTime);
        }
        return result;
    }





    public static String getPreHour1(Snapshot baseSnapshot) {

        if(null==baseSnapshot){
            return "";
        }
        String result="";
        String miniTime = "";//12位长度
        int tempMinue = 0;//2位分钟数
        miniTime = getMinueCommon(baseSnapshot);
        if (!StringUtils.isBlank(miniTime)&&miniTime.length()==14) {

            //初始化时间格式为201509280900
            StringBuffer tempTime=new StringBuffer();
            tempTime.append(miniTime.substring(0,10));
            tempTime.append("00");
            //获取日期最后2位，为分钟数
            tempMinue = Integer.parseInt(miniTime.substring(10, 12));
            if (tempMinue < 0 || tempMinue > 60) {
                logger.error("获取pre 60分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
                throw new RuntimeException("获取pre 60分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
            } else {
                result= timeAdd(tempTime.toString(), -60);
            }
        }else{
            logger.error("获取pre 60分钟日期格式错误：长度必须为12位，miniTime=" + miniTime);
            throw new RuntimeException("获取pre 60分钟日期格式错误：长度必须为12位，miniTime=" + miniTime);
        }
        return result;
    }
    public static String getHour4(Snapshot baseSnapshot) {
        return "";
    }
    public static String getPreHour4(Snapshot baseSnapshot) {
        return "";
    }
    public static String getDay(Snapshot baseSnapshot) {


        if(null==baseSnapshot){
            return "";
        }
        return getDayCommon(baseSnapshot);

    }
    public static String getPreDay(Snapshot baseSnapshot) {


        if(null==baseSnapshot){
            return "";
        }
        return getDayCommon(baseSnapshot);

    }
    public static String getWeek(Snapshot baseSnapshot) {
            if(null==baseSnapshot){
            return "";
        }
        return getDayCommon(baseSnapshot);
    }
    public static String getPreWeek(Snapshot baseSnapshot) {
        return null;
    }
    public static String getMonth(Snapshot baseSnapshot) {
        if(null==baseSnapshot){
            return "";
        }
        return getDayCommon(baseSnapshot);
    }

    public static String getYear(Snapshot baseSnapshot) {
        if(null==baseSnapshot){
            return "";
        }
        return getDayCommon(baseSnapshot);
    }


    public static String getPreMonth(Snapshot baseSnapshot) {
        return null;
    }
    public static String getSnap(Snapshot baseSnapshot) {
        String time="";
        if(null!=baseSnapshot){
            time=baseSnapshot.getMinTime();
        }
        return time;
    }
    public static String getDateNowe(String pattern) {

        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyyMMddHHmm";
        }
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String dateString = formatter.format(currentTime);
        return dateString;

    }

    public static void main(String args[]) {
//        Snapshot baseSnapshot = new NJSSnapshot();
//        Date currentTime = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String dateString = formatter.format(currentTime);
//        baseSnapshot.setMinTime(dateString);
//        baseSnapshot.setExchange("NJS");
//        baseSnapshot.setProdCode("002425");
//        SJSMinTimeUtil minTimeUtil = new SJSMinTimeUtil();
//        minTimeUtil.getRedisKey(CandleCycle.Minute5, baseSnapshot);
//        //System.out.println(minTimeUtil.getRedisKey(CandleCycle.Hour1, baseSnapshot));
        Snapshot baseSnapshot=new Snapshot();
        baseSnapshot.setMinTime("20151231101144");
        System.out.println(NJSMinTimeUtil.getYear(baseSnapshot));
    }


    public static int compare_date(String DATE1, String DATE2) {

        int result = -1;
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
  //      try {
//            Date dt1 = df.parse(DATE1);
//            Date dt2 = df.parse(DATE2);
//            long time = dt1.getTime() - dt2.getTime();
//            //如果data1比data2高出10分钟，就认为行情时间有问题，返回-1
//            if (time > 600000 || time < -600000) {
//                logger.error("行情时间有问题，与当前时间间隔超过13分钟，数据不录入");
//                throw new RuntimeException("行情时间有问题，与当前时间间隔超过13分钟，数据不录入");
//            } else {
//                result = 0;
//            }
//        } catch (Exception exception) {
//            logger.error("数据异常：行情时间有问题，与当前时间间隔超过13分钟，数据不录入:" + exception.getMessage());
//            throw new RuntimeException("数据异常：行情时间有问题，与当前时间间隔超过13分钟，数据不录入:" + exception.getMessage());
//        }
        return 0;
    }


    public static  Boolean  isNewMonth(String newDate,String oldeDate){

        Boolean isNewMonth=false;

        int newMonth = Integer.parseInt(DateUtils.getMonth(newDate, "yyyyMMddHHmm"));
        int oldMonth = Integer.parseInt(DateUtils.getMonth(oldeDate, "yyyyMMddHHmm"));

        int newYear = Integer.parseInt(DateUtils.getYear(newDate, "yyyyMMddHHmm"));
        int oldYear = Integer.parseInt(DateUtils.getYear(oldeDate, "yyyyMMddHHmm"));
        if(newYear==oldYear){
            if(newMonth>oldMonth){
                isNewMonth=true;
            }
        }else if(newYear>oldYear){
            isNewMonth=true;
        }else{
            throw new RuntimeException("数据异常，传入的时间比Redis的时间小");
        }
        return  isNewMonth;
    }


    public static  Boolean  isNewYear(String newDate,String oldeDate){

        Boolean isNewYear=false;


        int newYear = Integer.parseInt(DateUtils.getYear(newDate, "yyyyMMddHHmm"));
        int oldYear = Integer.parseInt(DateUtils.getYear(oldeDate, "yyyyMMddHHmm"));
        if(newYear>oldYear){
            isNewYear=true;
        }else if(newYear==oldYear){
            isNewYear=false;
        }else{
            throw new RuntimeException("数据异常，传入的时间比Redis的时间小");
        }
        return  isNewYear;
    }



    public static  Boolean  isNewWeek(String newDate,String oldeDate,String formate) {

        Boolean isNewWeek = false;

        long newTick = DateUtils.getTickTime(newDate, formate);
        long oldTick =DateUtils.getTickTime(oldeDate, formate);
        int newWeek = Integer.parseInt(DateUtils.getWeekOfDate(newDate, formate));
        int oldWeek = Integer.parseInt(DateUtils.getWeekOfDate(oldeDate,formate));
        if(newTick>oldTick){

            if (newWeek < oldWeek) {
                isNewWeek = true;
            }

        }

        return isNewWeek;

    }
}
