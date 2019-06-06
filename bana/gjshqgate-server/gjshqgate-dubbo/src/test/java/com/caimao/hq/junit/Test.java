package com.caimao.hq.junit;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.hq.utils.DateUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class Test {

    static Logger candle_njs_log = LoggerFactory.getLogger(Test.class);
    public Test() {
    }

    public long fromDateStringToLong(String inVal) { //此方法计算时间毫秒
        Date date = null;   //定义时间类型
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            date = inputFormat.parse(inVal); //将字符型转换成日期型
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime();   //返回毫秒数
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    private static  String dqsj() {  //此方法用于获得当前系统时间（格式类型2007-11-6 15:10:58）
        Date date = new Date();  //实例化日期类型
        String today = date.toLocaleString(); //获取当前时间
        System.out.println("获得当前系统时间 "+today);  //显示
        return today;  //返回当前时间
    }

//    public static void main(String[] args) {
//        String dqsj = dqsj();   //获得String dqsj = dqsj();   //获得当前系统时间
//        Test df = new Test();  //实例化方法
//        long startT=df.fromDateStringToLong("200503031651"); //定义上机时间
//        long endT=df.fromDateStringToLong("200503031450");  //定义下机时间
//
//        long ss=(startT-endT)/(1000); //共计秒数
//        int MM = (int)ss/60;   //共计分钟数
//        int hh=(int)ss/3600;  //共计小时数
//        int dd=(int)hh/24;   //共计天数
//
//        System.out.println(MM);
//
//    }
    public static void main(String[] args) {

           //writeLog();
//        InputStream is = HQServer.class.getResourceAsStream("/META-INF/conf/redis-conf.properties");
//        Properties pps = new Properties();
//        try {
//            pps.load(is);
//            System.out.println(pps.get("redis.host"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//
//        String[] aa="AA.BB".split("\\.");

        //System.out.println(JSON.toJSONString("(aaaa)"));
//        Double double1 = 20036680.01;
//        DecimalFormat decimalFormat = new DecimalFormat("###0.00");//格式化设置
//        System.out.println(decimalFormat.format(double1));
      //  System.out.println(DateUtils.getMonth("20150108090021", "yyyyMMddHHmmss"));
       // System.out.println(DateUtils.isInterval("201510080900", "201510081230", "201510081210", "hhmm"));
       // System.out.println(DateUtils.isInterval("20151008", "20151010", "20151010", "yyyyMMdd"));
       // System.out.println(DateUtils.isInterval("20151010", "20151011", "20151012", "yyyyMMdd"));
       //Date date=new Date();

      // System.out.println(DateUtils.getDate(date, "hhmm"));
      // System.out.println(DateUtils.compare_date("20151012'",DateUtils.getNoTime("yyyyMMdd"),"yyyyHHdd"));
        //System.out.println(DateUtils.getWeekDay("20151012133758", "yyyyMMddhhmmss"));
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
//
//
//       System.out.println(Test.convertProduct("aaaabbb.")) ;
//        System.out.println(Test.convertProduct("aaaabbb.")) ;
//
//        List list= converta("aaaa,bbbbb,cccc,,");
//        try {
//            System.out.println(URLEncoder.encode("Au(T+D).SJS","utf-8")) ;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//
//        System.out.println(list) ;

        //System.out.println(DateUtils.getMinueSub("201511161201","201511161130", "yyyyMMddHHmm"));

//        System.out.println(DateUtils.convert(Long.parseLong("1443542164074"),"yyyyMMddHHmmss"));
//        System.out.println(11/3);

       System.out.println(DateUtils.getTickTime("20151201150547", "yyyyMMddHHmmss")) ;
    }
    public static Map<String, String> convertProduct (String prodCode) {
        Map<String, String> mapPara = new HashMap<>();
        if (!StringUtils.isBlank(prodCode)) {
            if (prodCode.contains(".")) {
                int index=prodCode.lastIndexOf(".");

                mapPara.put("prodCode",prodCode.substring(0,index));
                mapPara.put("financeMic",prodCode.substring(index+1,prodCode.length()));
            }
        }
        return mapPara;
    }

    public static String  convert(String ownProductId){

        if(!StringUtils.isBlank(ownProductId)){
            if(ownProductId.substring(ownProductId.length()-1,ownProductId.length()).equalsIgnoreCase(",")){
                ownProductId=ownProductId.substring(0,ownProductId.length()-1);
            }
        }
        return ownProductId;
    }
    public static List converta(String ownProductId){
        List list=new ArrayList();
        if(!StringUtils.isBlank(ownProductId)){
            String [] idList=ownProductId.split(",");
            for(String str:idList){
                if(!StringUtils.isBlank(str)){
                    list.add(str);
                }
            }
        }
        return list;
    }

    public  static void writeLog(){

        for(int i=0;i<10;i++){
            candle_njs_log.info("test candle_njs_log log");
        }

    }
}

