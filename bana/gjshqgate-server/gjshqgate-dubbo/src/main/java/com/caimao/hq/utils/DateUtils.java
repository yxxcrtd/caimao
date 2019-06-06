package com.caimao.hq.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/9/26.
 */
public class
        DateUtils {

    private static Logger logger = Logger.getLogger(DateUtils.class);
    public static String getNoTime(String templete) {

        if(StringUtils.isBlank(templete)){
            templete="yyyyMMddHHmm";
        }
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(templete);
        String dateString = formatter.format(currentTime);
        return dateString;
    }



    public static String getDate(Date date,String templete) {

        if(StringUtils.isBlank(templete)){
            templete="yyyyMMdd";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(templete);
        String dateString = formatter.format(date);
        return dateString;
    }

    public static int compare_date(String DATE1, String DATE2,String formate) {

        DateFormat df = new SimpleDateFormat(formate);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);

            if (dt1.getTime() -dt2.getTime()>600000) {
               // System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
               // System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


    /**
     * 传入格式为12位
     * */
    public static int  getDifferMinue(Date beginDate,Date endDate,String formate){


        long startT=fromDateStringToLong(getDate(beginDate,formate),formate); //定义上机时间
        long endT=fromDateStringToLong(getDate(endDate,formate),formate);  //定义下机时间
        long ss=(startT-endT)/(1000); //共计秒数
        int MM = (int)ss/60;
        return MM;
    }


    /**
     * 传入格式为12位
     * */
    public static int  getDifferMinue(long beginDate,long endDate){


        long startT=beginDate;
        long endT=endDate;
        long ss=(startT-endT)/(1000); //共计秒数
        int MM = (int)ss/60;
        return MM;
    }

    /**
     * 传入格式为12位
     * */
    public static int  getDifferMinue(String beginDate,String endDate,String formate){


        long startT=getTickTime(beginDate,formate);
        long endT=getTickTime(endDate,formate);
        long ss=(startT-endT)/(1000); //共计秒数
        int MM = (int)ss/60;
        return MM;
    }
    public static String addMinue(Date date,int minue) {


        String resultDay = "";//转换后的分钟
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, minue);// 24小时制
            date = cal.getTime();
            resultDay = format.format(date);
            resultDay = resultDay.substring(0, 12);
        } catch (Exception ex) {
            logger.error("1分钟行情日期格式化错误，传入的行情时间转换错误:" + ex.getMessage());
            throw new RuntimeException("1分钟行情日期格式化错误，传入的行情时间转换错误:" + ex.getMessage());
        }

        return resultDay;
    }

    public static String addMinue(String strDate,int minue) {


        String resultDay = "";//转换后的分钟
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            Date date=parse(strDate,"yyyyMMddHHmm");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, minue);// 24小时制
            date = cal.getTime();
            resultDay = format.format(date);
            resultDay = resultDay.substring(0, 12);
        } catch (Exception ex) {
            logger.error("1分钟行情日期格式化错误，传入的行情时间转换错误:" + ex.getMessage());
            throw new RuntimeException("1分钟行情日期格式化错误，传入的行情时间转换错误:" + ex.getMessage());
        }

        return resultDay;
    }
    public static String addSECOND(String formate,String strDate,int SECOND) {


        String resultDay = "";//转换后的分钟
        SimpleDateFormat format = new SimpleDateFormat(formate);
        try {
            Date date=parse(strDate,formate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.SECOND, SECOND);// 24小时制
            date = cal.getTime();
            resultDay = format.format(date);
            resultDay = resultDay.substring(0, 14);
        } catch (Exception ex) {
            logger.error("1分钟行情日期格式化错误，传入的行情时间转换错误:" + ex.getMessage());
            throw new RuntimeException("1分钟行情日期格式化错误，传入的行情时间转换错误:" + ex.getMessage());
        }

        return resultDay;
    }
    public static String addDay(String strDate, int number,String formate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formate);

            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(strDate));
            cd.add(Calendar.DATE, number);//增加一天
            //cd.add(Calendar.MONTH, n);//增加一个月

            return sdf.format(cd.getTime());

        } catch (Exception e) {
            return null;
        }

    }


    public static long fromDateStringToLong(String inVal,String formate) { //此方法计算时间毫秒
        Date date = null;   //定义时间类型
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            date = inputFormat.parse(inVal); //将字符型转换成日期型
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime();   //返回毫秒数
    }
   /**
    * 获取当前时间是星期几
    * 礼拜天是1，礼拜一是2...礼拜六是 7!
    * */
    public static String getWeekDay(String nowDate,String formate){

        SimpleDateFormat inputFormat = new SimpleDateFormat(formate);
        Date date = null;
        try {
            date = inputFormat.parse(nowDate); //将字符型转换成日期型
        } catch (Exception e) {
            e.printStackTrace();
        }
        int week = date.getDay()+1;
        return String.valueOf(week);
    }

    /**
     * 获取几号
     *
     * */
    public static String getDay(String nowDate,String formate){

        SimpleDateFormat inputFormat = new SimpleDateFormat(formate);
        Date date = null;
        try {
            date = inputFormat.parse(nowDate); //将字符型转换成日期型
        } catch (Exception e) {
            e.printStackTrace();
        }
        int month =date.getDate();
        return String.valueOf(month);
    }

    /**
     * 获取年份
     *
     * */
    public static String getYear(String nowDate,String formate){

        SimpleDateFormat inputFormat = new SimpleDateFormat(formate);
        Date date = null;
        try {
            date = inputFormat.parse(nowDate); //将字符型转换成日期型
        } catch (Exception e) {
            e.printStackTrace();
        }
        int year =date.getYear();
        return String.valueOf(year);
    }


    /**
     * 获取月份
     *
     * */
    public static String getMonth(String nowDate,String formate){

        SimpleDateFormat inputFormat = new SimpleDateFormat(formate);
        Date date = null;
        try {
            date = inputFormat.parse(nowDate); //将字符型转换成日期型
        } catch (Exception e) {
            e.printStackTrace();
        }
        int month =date.getMonth();
        return String.valueOf(month);
    }
    /**
     * 经过格式化后的日期  周一-周日  分别为1,2,3,4,5,6,7
     * 获取当前日期是星期几 <br>
     *
     * @param nowDate
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(String nowDate,String formate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(formate);
        Date date = null;
        try {
            date = inputFormat.parse(nowDate); //将字符型转换成日期型
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    /**
     *compare 是否在star-end 之间，如果是返回true,否则返回false
     *       System.out.println(DateUtils.isInterval("0900", "1130", "1130", "hhmm"));
     *       System.out.println(DateUtils.isInterval("20151008", "20151010", "20151011", "yyyyMMdd"));
     *        System.out.println(DateUtils.isInterval("20151009113758", "20151009133758", "20151009143758", "yyyyMMdd"));
     * */
    public static Boolean isInterval (String star,String end,String compare,String formate){

        Boolean isInterval=false;
        SimpleDateFormat localTime=new SimpleDateFormat(formate);

        try{
            Date sdate = localTime.parse(star);
            Date edate=localTime.parse(end);
            Date scompare=localTime.parse(compare);
           // Date1.after(Date2),当Date1大于Date2时，返回TRUE，当小于等于时，返回false；
            //Date1.before(Date2)，当Date1小于Date2时，返回TRUE，当大于等于时，返回false；l
            if(!scompare.after(edate)&& !scompare.before(sdate)){
                isInterval=true;
            }else{
                isInterval=false;
            }
        }catch(Exception e){

            logger.error("日期比较异常：" + e.getMessage());
            throw new RuntimeException("日期比较异常："+e.getMessage());
        }
        return isInterval;
    }

    public static Date parse(String strDate, String pattern) {

        Date date=null;
        try{
            date= StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(pattern).parse(strDate);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return date;
    }
    public static String convert(long mill,String formate){
        Date date=new Date(mill);
        String strs="";
        try {
            SimpleDateFormat sdf=new SimpleDateFormat(formate);
            strs=sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }

    //获取时间戳
    public static Long getTickTime(String time,String formate) {

        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        Long l=null;
        Date d;
        try {
            d = sdf.parse(time);
             l = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  l;
    }
  //获取2个时间戳相差时间
   public static  Long getMinueSub(String beginTime,String endTime,String formate){

       Long beginTick=getTickTime(beginTime,formate);
       Long endTick=getTickTime(endTime,formate);

       Long s = (endTick - beginTick) / (1000 * 60);
       return s;
   }

    //获取2个时间戳相差时间
    public static  Long getMinue30Sub(String beginTime,String endTime,String formate){

        Long beginTick=getTickTime(beginTime,formate);
        Long endTick=getTickTime(endTime,formate);

        Long s = (endTick - beginTick) / (1000 * 60*30);
        return s;
    }
    //获取2个时间戳相差时间
    public static  Long getMinue5Sub(String beginTime,String endTime,String formate){

        Long beginTick=getTickTime(beginTime,formate);
        Long endTick=getTickTime(endTime,formate);

        Long s = (endTick - beginTick) / (1000 * 60*5);
        return s;
    }

    //获取2个时间戳相差时间
    public static  Long getDaySub(String beginTime,String endTime,String formate){

        Long beginTick=getTickTime(beginTime,formate);
        Long endTick=getTickTime(endTime,formate);

        Long s = (long)((endTick - beginTick) / (1000 * 60 * 60 * 24));
        return s;
    }

  public static void main(String args[]){


      System.out.println(getTickTime("201512030000","yyyyMMddHHmm"));

  }

}
