package com.caimao.hq.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.api.service.INJSCandleService;
import com.caimao.hq.api.service.IOwnProductService;
import com.caimao.hq.api.service.ISJSCandleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.RuntimeMBeanException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2015/10/29.
 */
@Service("holidayUtil")
public   class HolidayUtil {
    private static Logger logger = LoggerFactory.getLogger(HolidayUtil.class);
    @Autowired
    private GJSProductUtils gjsProductUtils;
    @Autowired
    private INJSCandleService njsCandleService;
    @Autowired
    private ISJSCandleService sjsCandleService;
    @Autowired
    private IHQService hqService;

    //格式化为nowTime yyyyMMddHHmm
    public   TradeTime getTradeTimeNow(String exchange,String nowTime){
        TradeTime temp=null;
        if(!StringUtils.isBlank(exchange)&&!StringUtils.isBlank(nowTime)){

            List<TradeTime> tradeTime=gjsProductUtils.mapHoliday.get(exchange);
            if(null!=tradeTime){

                Long tickerNow=DateUtils.getTickTime(nowTime,"yyyyMMddHHmm");
                for(TradeTime tt:tradeTime){

                    if(null!=tt){

                        if(tt.getOpentime()<=tickerNow&&tt.getClosetime()>=tickerNow){
                            temp=tt;
                            break;
                        }
                    }
                }
            }
        }
        return  temp;
    }

   public  List<Candle> getCandle(TradeTime tradeTime,String exchange,String prodCode,String cycle){
       List<Candle> resultCandle=null;
       if(null!=tradeTime&&!StringUtils.isBlank(exchange)&&!StringUtils.isBlank(prodCode)&&!StringUtils.isBlank(cycle)){
           CandleReq req=new CandleReq();
           req.setCycle(cycle);
           req.setNumber(5000);
           req.setExchange(exchange);
           req.setProdCode(prodCode);
           req.setBeginDate(DateUtils.convert(tradeTime.getOpentime(), "yyyyMMddHHmm"));
           req.setEndDate(DateUtils.convert(tradeTime.getClosetime(),"yyyyMMddHHmm"));
           resultCandle=hqService.queryCandleRedisHistory(req);
       }
      return resultCandle;
   }

    public void fillData(String exchange,String prodCode,String nowTime,String cycle){
        try{
            TradeTime tradeTime=getTradeTimeNow( exchange, nowTime);
            if(null!=tradeTime){
                List<Candle> candleList=getCandle( tradeTime, exchange, prodCode, cycle);
                List subCandle=getCandleSubCycle(candleList);//找出有间隔的数据
                if(null!=subCandle&&subCandle.size()>0){
                    insertRedisCandle(subCandle);
                    insertDBCandle(subCandle);
                }
            }
        }catch (Exception ex){
            logger.error("填充数据失败:"+ex.getMessage());
        }
    }
    public  void  insertDBCandle(List<Candle> list){

        if(null!=list&&list.size()>0){
            Candle candle=list.get(0);
            if(candle instanceof  NJSCandle){
                njsCandleService.insertPatch(list);
            }else if(candle instanceof  SJSCandle){
                sjsCandleService.insertPatch(list);
            }else{
                logger.error("填充数据,批量插入数据库失败:不支持的K线类型 "+list.get(0));
            }

        }

    }
    public  void  insertRedisCandle(List<Candle> list){

        if(null!=list){
            for(Candle candle:list){
                if(null!=candle){
                    try{
                        hqService.insertRedisCandleHistory(candle.getCycle(),candle);
                    }catch (Exception ex){
                        logger.error("填充K线异常:"+ex.getMessage()+candle.toString());
                    }

                }
            }
        }
    }
    //Candle排列顺序是由大到小 ，日期
    public List<Candle>  getCandleSubCycle(List<Candle> candleList){

        List<Candle>  resultCandleList=new ArrayList();
        if(null!=candleList){
                Candle  tempBegin=null;
                Candle  tempEnd=null;
                for(int i=0;i<candleList.size()-1;i++){
                    tempBegin=candleList.get(i + 1);
                    tempEnd=candleList.get(i);
                    if(null==tempBegin||null==tempEnd){
                        continue;
                    }
                    int number=getDifferCycle(tempBegin.getMinTime(),tempEnd.getMinTime(), "yyyyMMddHHmm", candleList.get(i).getCycle());
                    if(number>1){

                        for(int k=0;k<number-1;k++){

                            try {
                                Candle temp=(Candle)tempBegin.clone();
                                if(null!=temp){

                                    temp.setHighPx(temp.getClosePx());
                                    temp.setLowPx(temp.getClosePx());
                                    temp.setClosePx(temp.getClosePx());
                                    temp.setPreClosePx(temp.getClosePx());
                                    temp.setMinTime(DateUtils.addMinue(temp.getMinTime(), getMinu1FromCycle(temp.getCycle()) * (k + 1)));
                                    temp.setLastSettle(temp.getClosePx());
                                    temp.setSettle(temp.getClosePx());
                                    temp.setBusinessAmount(0);
                                    temp.setBusinessBalance(0);
                                    temp.setPxChange(0);
                                    temp.setPxChangeRate(0);
                                    resultCandleList.add(temp);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        return resultCandleList;
    }

    //计算ticker
    public static int getTimeFromCycle(CandleCycle cycle){
        int time=0;
        switch (cycle){

            case Hour1:
                time=60 * 60 * 1000;
                break;
            case Hour4:
                time=240 * 60 * 1000;
                break;
            case Minute1:
                time=1 * 60 * 1000;
                break;
            case Minute30:
                time=30 * 60 * 1000;;
                break;
            case Minute5:
                time=5 * 60 * 1000;
                break;
            case DayCandle:
                time=24*60 * 60 * 1000;
                break;
        }
        return time;
    }


    public static int getMinu1FromCycle(CandleCycle cycle){
        int time=0;
        switch (cycle){

            case Hour1:
                time=60;
                break;
            case Hour4:
                time=240;
                break;
            case Minute1:
                time=1;
                break;
            case Minute30:
                time=30;
                break;
            case Minute5:
                time=5;
                break;
            case DayCandle:
                time=24*60;
                break;
        }
        return time;
    }
    /**
     * 传入格式为12位   只支持日内的周期
     * */
    public static int  getDifferCycle(String beginDate,String endDate,String formate,CandleCycle cycle){
        int result=0;
        try{
            int timeFromCycle=getTimeFromCycle(cycle);
            long startT=DateUtils.getTickTime(beginDate, formate);
            long endT=DateUtils.getTickTime(endDate, formate);
            result = (int)(endT-startT)/timeFromCycle;
        }catch (Exception ex){
            logger.error("补全时间获取时间间隔失败："+ex.getMessage());
        }
        return result;
    }


    public static void main(String args[]){

        System.out.println(getDifferCycle("201601131651","201601131652","yyyyMMddHHmm",CandleCycle.Minute1));
        System.out.println(getDifferCycle("201601131651","201601131651","yyyyMMddHHmm",CandleCycle.Minute1));
        System.out.println(getDifferCycle("201601131630","201601131640","yyyyMMddHHmm",CandleCycle.DayCandle));
    }
}
