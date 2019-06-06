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
public class SJSMinTimeUtil {

    static Logger logger = Logger.getLogger(SJSMinTimeUtil.class.getName());
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

    public static String getRedisKeyHistory(CandleCycle cycle, Snapshot baseSnapshot) {

        StringBuffer sb = new StringBuffer();
        if (null != baseSnapshot) {
            if (SnapshotFormate.isAuth(baseSnapshot)) {
                sb.append(baseSnapshot.getExchange());
                sb.append(baseSnapshot.getProdCode());
                sb.append(cycle);
                sb.append("history");
            }
        }
        return sb.toString();
    }

    public static String getRedisKeyHistory(CandleCycle cycle, String exchange,String prodCode) {

        StringBuffer sb = new StringBuffer();
        if (!StringUtils.isBlank(exchange)&&!StringUtils.isBlank(prodCode)) {

                sb.append(exchange);
                sb.append(prodCode);
                sb.append(cycle);
                sb.append("history");

        }else{
         throw new RuntimeException("获取redis history key 异常,传入exchange,或者prodCode  is null");
        }
        return sb.toString();
    }
    //多个dubbo同时插入同样的数据
    public static String getRedisKeyAll(String exchange) {

        StringBuffer sb = new StringBuffer();
        if (!StringUtils.isBlank(exchange)) {

            sb.append(exchange);
            sb.append(CandleCycle.Snap);
            sb.append("all");

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
    /**
     * 如果是收盘 就修正下时间，减1秒
     * */
    private static String  updateMiniute(String miniTime){
        //收盘 0230
        String resultMiniTime="";
        if(!StringUtils.isBlank(miniTime)&&miniTime.length()==14){
            String strBegin0230=miniTime.substring(0,8)+"023000";
            long begin0230=DateUtils.getTickTime(strBegin0230,"yyyyMMddHHmmss");

            String str1530=miniTime.substring(0,8)+"153000";
            long begin1530=DateUtils.getTickTime(str1530,"yyyyMMddHHmmss");

            String str1130=miniTime.substring(0,8)+"113000";
            long begin1130=DateUtils.getTickTime(str1130,"yyyyMMddHHmmss");


            long source=DateUtils.getTickTime(miniTime,"yyyyMMddHHmmss");




            if(begin0230==source||begin1530==source||begin1130==source){
                resultMiniTime= DateUtils.addSECOND("yyyyMMddHHmmss", miniTime,-1);
            }else{
                resultMiniTime=miniTime;
            }
        }
        return resultMiniTime;
    }
    public static String getTime(CandleCycle cycle, Snapshot baseSnapshot){

        String resultDate="";
        if(null!=cycle&&null!=baseSnapshot){
            baseSnapshot.setMinTime(updateMiniute(baseSnapshot.getMinTime()));
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
        Boolean isTradeTime=isTradeTime(day);
        if(isTradeTime){
           // String convertTime=getMinue1Convert(day.substring(0, 12));
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


        }else{
           throw new RuntimeException("非交易时间段："+baseSnapshot);
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

       return baseSnapshot.getMinTime();
    }
    public static void checkMiniTime(Snapshot baseSnapshot) {

        if(null==baseSnapshot||StringUtils.isBlank(baseSnapshot.getMinTime())||baseSnapshot.getMinTime().length()!=14){
            logger.error("南交所传入的行情数据日期格式不合法，长度必须为14位,不能为空");
            throw new RuntimeException("南交所传入的行情数据日期格式不合法，长度必须为14位,不能为空");
        }
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
                    miniTime=miniTime.substring(0, 8);
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

            Boolean isTradeTime=isTradeTime(miniTime);
            if(isTradeTime){
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
                throw new RuntimeException("非交易时间段："+baseSnapshot);
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
            Boolean isTradeTime=isTradeTime(miniTime);
            if(isTradeTime){

              //  String convertTime=  getMinu30Convert(miniTime.substring(0,12));
                StringBuffer tempTime=new StringBuffer();
                tempTime.append(miniTime.substring(0,10));
                tempTime.append("00");
                //获取日期最后2位，为分钟数
                tempMinue = Integer.parseInt(miniTime.substring(10, 12));
                if (tempMinue < 0 || tempMinue > 60) {

                    logger.error("获取30分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
                    throw new RuntimeException("获取30分钟日期格式错误分钟必须在0-60之间：分钟数为" + tempMinue + "baseSnapshot=" + baseSnapshot.toString());
                } else {

                    if (tempMinue <30) {
                        result= timeAdd (tempTime.toString(),30);
                    } else {
                        result= timeAdd(tempTime.toString(),60);
                    }
                }

            }else{

                throw new RuntimeException("非交易时间段："+baseSnapshot);

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
            Boolean isTradeTime=isTradeTime(miniTime);
            if(isTradeTime){
                String convertTime=  getHour1Convert(miniTime.substring(0,12));
                if(StringUtils.isBlank(convertTime)){
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
                    result=convertTime;
                }

            }else{
              throw new RuntimeException("非交易时间段:"+baseSnapshot);
            }

        }else{
            logger.error("获取60分钟日期格式错误：长度必须为12位，miniTime=" + miniTime);
            throw new RuntimeException("获取60分钟日期格式错误：长度必须为12位，miniTime=" + miniTime);
        }
        return result;
    }


    private static String getMinue1Convert(String  time) {
        String resultTime = "";

        String strEnd0901 = time.substring(0, 8) + "0901";
        String strBegin0230 = time.substring(0, 8) + "0230";

        String strEnd2001= time.substring(0, 8) + "2001";
        String strBegin1530 = time.substring(0, 8) + "1530";

        String strEnd1331= time.substring(0, 8) + "1331";
        String strBegin1130 = time.substring(0, 8) + "1130";
        long sourceTime=DateUtils.getTickTime(time,"yyyyMMddHHmm");

        long begin0230=DateUtils.getTickTime(strBegin0230,"yyyyMMddHHmm");
        long end0901=DateUtils.getTickTime(strEnd0901,"yyyyMMddHHmm");

        long begin1530=DateUtils.getTickTime(strBegin1530,"yyyyMMddHHmm");
        long end2001=DateUtils.getTickTime(strEnd2001,"yyyyMMddHHmm");

        long begin1130=DateUtils.getTickTime(strBegin1130,"yyyyMMddHHmm");
        long end1331=DateUtils.getTickTime(strEnd1331,"yyyyMMddHHmm");


        if(sourceTime>=begin0230&&sourceTime<end0901){
            resultTime=strEnd0901;
        }else if(sourceTime>=begin1530&&sourceTime<end2001){
            resultTime=strEnd2001;
        }else if(sourceTime>=begin1130&&sourceTime<end1331){
            resultTime=strEnd1331;
        }else{
            resultTime="";
        }
        return resultTime;


    }
    private static String getHour1Convert(String  time){
        String resultTime="";

        String strEnd0930=time.substring(0,8)+"0930";
        String strBegin0200=time.substring(0,8)+"0200";

        String strEnd1030=time.substring(0,8)+"1030";
        String strBegin0930=time.substring(0,8)+"0930";

        String strEnd1130=time.substring(0,8)+"1130";
        String strBegin1030=time.substring(0,8)+"1030";

        String strEnd1430=time.substring(0,8)+"1430";
        String strBegin1130=time.substring(0,8)+"1130";

        String strEnd1530=time.substring(0,8)+"1530";
        String strBegin1430=time.substring(0,8)+"1430";

        String strEnd2100=time.substring(0,8)+"2100";
        String strBegin1530=time.substring(0,8)+"1530";

        long sourceTime=DateUtils.getTickTime(time,"yyyyMMddHHmm");
        long begin0200=DateUtils.getTickTime(strBegin0200,"yyyyMMddHHmm");
        long end0930=DateUtils.getTickTime(strEnd0930,"yyyyMMddHHmm");


        long begin0930=DateUtils.getTickTime(strBegin0930,"yyyyMMddHHmm");
        long end1030=DateUtils.getTickTime(strEnd1030,"yyyyMMddHHmm");


        long begin1030=DateUtils.getTickTime(strBegin1030,"yyyyMMddHHmm");
        long end1130=DateUtils.getTickTime(strEnd1130,"yyyyMMddHHmm");

        long begin1130=DateUtils.getTickTime(strBegin1130,"yyyyMMddHHmm");
        long end1430=DateUtils.getTickTime(strEnd1430,"yyyyMMddHHmm");

        long begin1430=DateUtils.getTickTime(strBegin1430,"yyyyMMddHHmm");
        long end1530=DateUtils.getTickTime(strEnd1530,"yyyyMMddHHmm");

        long begin1530=DateUtils.getTickTime(strBegin1530,"yyyyMMddHHmm");
        long end2100=DateUtils.getTickTime(strEnd2100,"yyyyMMddHHmm");



        if(sourceTime>=begin0200&&sourceTime<end0930){
            resultTime=strEnd0930;
        }else if(sourceTime>=begin0930&&sourceTime<end1030){
            resultTime=strEnd1030;
        }else if(sourceTime>=begin1030&&sourceTime<end1130){
            resultTime=strEnd1130;
        }else if(sourceTime>=begin1130&&sourceTime<end1430){
            resultTime=strEnd1430;
        }else if(sourceTime>=begin1430&&sourceTime<end1530){
            resultTime=strEnd1530;
        }else if(sourceTime>=begin1530&&sourceTime<end2100){
            resultTime=strEnd2100;
        }else{
            resultTime="";
        }
        return resultTime;


    }
//    public static String  getDayConvert(String time,String apdRecvTime){
//
//        String resultTime = "";
//        String tradeDate= time.substring(0, 8);
//        String strBegin = tradeDate + "2000";
//        String strEnd =tradeDate+ "2359";
//        String sourceTime=time.substring(0, 12);
//        long lsourceTime=DateUtils.getTickTime(sourceTime, "yyyyMMddHHmm");
//
//        long lbeginTime=DateUtils.getTickTime(strBegin, "yyyyMMddHHmm");
//        long lendTime=DateUtils.getTickTime(strEnd, "yyyyMMddHHmm");
//
//
//        if(lsourceTime>=lbeginTime&&lsourceTime<=lendTime){
//            //不能直接相减，因为交易日-1 可能是周末
//
//
//            if(!StringUtils.isBlank(apdRecvTime)){
//                resultTime=apdRecvTime;
//            }else{
//                resultTime=DateUtils.getNoTime("yyyyMMdd");//需要注意，如果导入历史数据的时候没有apdRecvTime
//               // resultTime= DateUtils.addDay(time.substring(0, 8),-1,"yyyyMMdd");//有个bug
//            }
//        }
//        return resultTime;
//    }
    private static String getMinu5Convert(String  time) {
        String resultTime = "";

        String strEnd0905 = time.substring(0, 8) + "0905";
        String strBegin0230 = time.substring(0, 8) + "0900";

        String strEnd2005= time.substring(0, 8) + "2005";
        String strBegin1530 = time.substring(0, 8) + "2000";

        String strEnd1335= time.substring(0, 8) + "1335";
        String strBegin1330 = time.substring(0, 8) + "1330";
        long sourceTime=DateUtils.getTickTime(time, "yyyyMMddHHmm");

        long begin0230=DateUtils.getTickTime(strBegin0230,"yyyyMMddHHmm");
        long end0905=DateUtils.getTickTime(strEnd0905,"yyyyMMddHHmm");

        long begin1530=DateUtils.getTickTime(strBegin1530, "yyyyMMddHHmm");
        long end2005=DateUtils.getTickTime(strEnd2005,"yyyyMMddHHmm");

        long begin1330=DateUtils.getTickTime(strBegin1330,"yyyyMMddHHmm");
        long end1335=DateUtils.getTickTime(strEnd1335,"yyyyMMddHHmm");


        if(sourceTime>=begin0230&&sourceTime<end0905){
            resultTime=strEnd0905;
        }else if(sourceTime>=begin1530&&sourceTime<end2005){
            resultTime=strEnd2005;
        }else if(sourceTime>=begin1330&&sourceTime<end1335){
            resultTime=strEnd1335;
        }else{
            resultTime="";
        }
        return resultTime;
    }

    private static String getMinu30Convert(String  time){
        String resultTime="";

        String strEnd0930=time.substring(0,8)+"0930";
        String strBegin0230=time.substring(0,8)+"0230";

        String strEnd1400=time.substring(0,8)+"1400";
        String strBegin1130=time.substring(0,8)+"1130";

        String strEnd2030=time.substring(0,8)+"2030";
        String strBegin1530=time.substring(0,8)+"1530";



        long sourceTime=DateUtils.getTickTime(time,"yyyyMMddHHmm");

        long begin0230=DateUtils.getTickTime(strBegin0230,"yyyyMMddHHmm");
        long end0930=DateUtils.getTickTime(strEnd0930,"yyyyMMddHHmm");


        long begin1130=DateUtils.getTickTime(strBegin1130,"yyyyMMddHHmm");
        long end1400=DateUtils.getTickTime(strEnd1400,"yyyyMMddHHmm");

        long begin1530=DateUtils.getTickTime(strBegin1530,"yyyyMMddHHmm");
        long end2030=DateUtils.getTickTime(strEnd2030,"yyyyMMddHHmm");


        if(sourceTime>=begin0230&&sourceTime<end0930){
            resultTime=strEnd0930;
        }else if(sourceTime>=begin1130&&sourceTime<end1400){
            resultTime=strEnd1400;
        }else if(sourceTime>=begin1530&&sourceTime<end2030){
            resultTime=strEnd2030;
        }else{
            resultTime="";
        }
        return resultTime;


    }

    public static Boolean isTradeTime(String  time){
//        Boolean isTradeTime=true;
//
//        long sourceTime=DateUtils.getTickTime(time,"yyyyMMddHHmmss");
//        String strEnd2000=time.substring(0,8)+"200000";
//        String strBegin1530=time.substring(0,8)+"153000";
//        long begin1530=DateUtils.getTickTime(strBegin1530,"yyyyMMddHHmmss");
//        long end2000=DateUtils.getTickTime(strEnd2000,"yyyyMMddHHmmss");
//
//
//        String strEnd0900=time.substring(0,8)+"090000";
//        String strBegin0230=time.substring(0,8)+"023000";
//        long begin0230=DateUtils.getTickTime(strBegin0230,"yyyyMMddHHmmss");
//        long end0900=DateUtils.getTickTime(strEnd0900,"yyyyMMddHHmmss");
//
//
//
//        String strEnd1330=time.substring(0,8)+"133000";
//        String strBegin1130=time.substring(0,8)+"113000";
//        long begin1130=DateUtils.getTickTime(strBegin1130,"yyyyMMddHHmmss");
//        long end1330=DateUtils.getTickTime(strEnd1330,"yyyyMMddHHmmss");
//
//        if(sourceTime>=begin1530&&sourceTime<=end2000){
//            isTradeTime=false;
//        }else if(sourceTime>=begin0230&&sourceTime<=end0900){
//
//            isTradeTime=false;
//        }else if(sourceTime>=begin1130&&sourceTime<=end1330){
//            isTradeTime=false;
//
//        }else{
//            isTradeTime=true;
//        }
//        return isTradeTime;
        return true;
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
            Boolean isTradeTime=isTradeTime(time);
            if(!isTradeTime){
                throw new RuntimeException("非交易时间段:"+baseSnapshot);
            }

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
//        Snapshot baseSnapshot=new Snapshot();
//        baseSnapshot.setMinTime("20151231101144");
//        System.out.println(SJSMinTimeUtil.getYear(baseSnapshot));
        System.out.println(updateMiniute("20151123153000"));

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
