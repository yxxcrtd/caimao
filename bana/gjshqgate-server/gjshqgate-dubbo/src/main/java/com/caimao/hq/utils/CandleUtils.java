package com.caimao.hq.utils;

import com.caimao.hq.api.entity.Candle;
import com.caimao.hq.api.entity.CandleCycle;
import com.caimao.hq.api.entity.Snapshot;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/14.
 */
@Service("candleUtils")
public class CandleUtils {


    public static Map<String,CandleCycle> mapCycle=new HashMap();
    static{

        mapCycle.put("1",CandleCycle.Minute1);
        mapCycle.put("2",CandleCycle.Minute5);
        mapCycle.put("3",CandleCycle.Minute15);
        mapCycle.put("4",CandleCycle.Minute30);
        mapCycle.put("5",CandleCycle.Hour1);
        mapCycle.put("6",CandleCycle.DayCandle);
        mapCycle.put("7",CandleCycle.Week);
        mapCycle.put("8",CandleCycle.Month);
        mapCycle.put("9",CandleCycle.Year);
        mapCycle.put("10",CandleCycle.Snap);
        mapCycle.put("11",CandleCycle.Snap5);

    }
    public  void initCandleFromSnapshot(CandleCycle cycle,Candle candle,Snapshot baseSnapshot) {
        if(null!=candle&&baseSnapshot!=null){
            if(cycle==CandleCycle.DayCandle){


                candle.setHighPx(baseSnapshot.getHighPx());
                candle.setLowPx(baseSnapshot.getLowPx());
                candle.setBusinessAmount(baseSnapshot.getBusinessAmount());
                candle.setBusinessBalance(baseSnapshot.getBusinessBalance());
                candle.setLastPx(baseSnapshot.getLastPx());
                candle.setPxChange(baseSnapshot.getPxChange());
                candle.setPxChangeRate(baseSnapshot.getPxChangeRate());
                candle.setPreClosePx(baseSnapshot.getPreclosePx());
                candle.setCycle(cycle);
                candle.setRedisKey(baseSnapshot.getRedisKey());
                candle.setExchange(baseSnapshot.getExchange());
                candle.setProdCode(baseSnapshot.getProdCode());
                candle.setOpenPx(baseSnapshot.getOpenPx());
                candle.setClosePx(baseSnapshot.getLastPx());
                candle.setProdName(baseSnapshot.getProdName());

                candle.setIsGoods(baseSnapshot.getIsGoods());
                candle.setSettle(baseSnapshot.getLastPx());//日K 结算价=现价 ，只有昨结算管用
                candle.setLastSettle(baseSnapshot.getLastSettle());
                candle.setAveragePx(baseSnapshot.getAveragePx());
            }else{
                //candle.setOpen_px(baseSnapshot.getLastPx());//开盘价=最新价

                candle.setBusinessBalance(baseSnapshot.getLastAmount() * baseSnapshot.getLastPx());//最新价*最新成交数量
                candle.setBusinessAmount(baseSnapshot.getLastAmount());//最新成交数量
                candle.setClosePx(baseSnapshot.getLastPx());
                candle.setExchange(baseSnapshot.getExchange());
                candle.setHighPx(baseSnapshot.getLastPx());
                candle.setLowPx(baseSnapshot.getLastPx());
                candle.setLastPx(baseSnapshot.getLastPx());
                candle.setProdCode(baseSnapshot.getProdCode());
                candle.setProdName(baseSnapshot.getProdName());
                candle.setCycle(cycle);
                candle.setOpenPx(baseSnapshot.getLastPx());
                candle.setRedisKey(baseSnapshot.getRedisKey());
                candle.setIsGoods(baseSnapshot.getIsGoods());
                candle.setSettle(baseSnapshot.getLastPx());
                candle.setLastSettle(baseSnapshot.getLastPx());
            }
            candle.setMinTime(baseSnapshot.getCycleTimeDate());

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
                candle.setPxChange(baseSnapshot.getPxChange());
                candle.setPxChangeRate(baseSnapshot.getPxChangeRate());
                candle.setCycle(cycle);
                setPreClosePx(candle, baseSnapshot);//设置昨收
                candle.setOpenPx(baseSnapshot.getOpenPx());
                candle.setClosePx(baseSnapshot.getLastPx());
                candle.setProdName(baseSnapshot.getProdName());
                candle.setRedisKey(baseSnapshot.getRedisKey());
                candle.setIsGoods(baseSnapshot.getIsGoods());
                candle.setSettle(baseSnapshot.getLastPx());
                candle.setLastSettle(baseSnapshot.getLastSettle());
                candle.setAveragePx(baseSnapshot.getAveragePx());
            }else{

                setHighpx(candle, baseSnapshot);
                setLowpx(candle, baseSnapshot);
                setCpx(candle, baseSnapshot);
                setBusinessAmount(candle, baseSnapshot);
                setBusinessBalance(candle, baseSnapshot);
                setLastpx(candle, baseSnapshot);
                setPxChange(candle, baseSnapshot);
                setPxChangeRate(candle, baseSnapshot);
                candle.setCycle(cycle);
                candle.setRedisKey(baseSnapshot.getRedisKey());
                candle.setIsGoods(baseSnapshot.getIsGoods());
                candle.setSettle(baseSnapshot.getSettle());
                candle.setLastSettle(baseSnapshot.getLastSettle());
                //差一个设置均价的方法
            }
            candle.setMinTime(baseSnapshot.getCycleTimeDate());
            //candle.setOpen_px(baseSnapshot.getOpen_px());//因传入的数据分笔按分钟给出了开盘价，所以不同周期的都可以沿用
        }
    }




//设置昨收
  public void setPreClosePx(Candle candle,Snapshot baseSnapshot){

      if(null!=candle&&null!=baseSnapshot){
          if(CandleCycle.DayCandle==candle.getCycle()){

              candle.setPreClosePx(baseSnapshot.getPreclosePx());

          }
      }

  }



    //设置价格涨跌  当前价-昨收
    public   void  setPxChange(Candle candle,Snapshot baseSnapshot){
        if(null!=candle&&null!=baseSnapshot){

            if(CandleCycle.DayCandle==candle.getCycle()&&candle.getIsGoods()==2){
                candle.setPxChange(DoubleOperationUtil.sub(candle.getLastPx(), candle.getLastSettle()));

            }else{
                candle.setPxChange(DoubleOperationUtil.sub(candle.getLastPx(), candle.getPreClosePx()));

            }


        }
    }


    //设置价格涨跌幅度   （当前价-昨收/昨收）*100%

    public   void  setPxChangeRate(Candle candle,Snapshot baseSnapshot){
        if(null!=candle&&null!=baseSnapshot){

            if(CandleCycle.DayCandle==candle.getCycle()&&candle.getIsGoods()==2){
                if( candle.getLastSettle()>0){
                    candle.setPxChangeRate(DoubleOperationUtil.round(DoubleOperationUtil.div(candle.getPxChange(), candle.getLastSettle(), 4) * 100, 2));

                }

            }else{

                if(candle.getPreClosePx()>0){
                    candle.setPxChangeRate(DoubleOperationUtil.round(DoubleOperationUtil.div(candle.getPxChange(), candle.getPreClosePx(),4)*100,2));
                }

            }
        }
    }



    //设置最高价
    public   void  setHighpx(Candle candle,Snapshot baseSnapshot){
        if(null!=candle&&null!=baseSnapshot){
            if(CandleCycle.DayCandle==candle.getCycle()){

                candle.setHighPx(baseSnapshot.getHighPx());

            }else{
                if(candle.getHighPx()<baseSnapshot.getLastPx()){
                    candle.setHighPx(baseSnapshot.getLastPx());
                }
            }

        }
    }


    public  void  setLowpx(Candle candle,Snapshot baseSnapshot){
        if(null!=candle&&null!=baseSnapshot){

            if(CandleCycle.DayCandle==candle.getCycle()){
                candle.setLowPx(baseSnapshot.getLowPx());
            }else{
                //设置最低价
                if(candle.getLowPx()>baseSnapshot.getLastPx()){
                    candle.setLowPx(baseSnapshot.getLastPx());
                }
            }

        }
    }

    //设置收盘价
    public  void  setCpx(Candle candle,Snapshot baseSnapshot){

        if(null!=candle&&null!=baseSnapshot){
            candle.setClosePx(baseSnapshot.getLastPx());
        }
    }
    //设置成交数量
    public   void  setBusinessAmount(Candle candle,Snapshot baseSnapshot){
        if(CandleCycle.DayCandle==candle.getCycle()){

            candle.setBusinessAmount(baseSnapshot.getBusinessAmount());

        }else{
            if(null!=candle&&null!=baseSnapshot){
                //当前成交量+最新成交数量
                // System.out.println("更新成交数量：当前="+candle.getBusinessAmount()+",新生添加的="+baseSnapshot.getLastAmount()+",结果="+DoubleOperationUtil.add(candle.getBusinessAmount(), baseSnapshot.getLastAmount()));
                candle.setBusinessAmount(DoubleOperationUtil.add(candle.getBusinessAmount(), baseSnapshot.getLastAmount()));
            }
        }

    }

    //设置成交金额   最新价*成交数量
    public  void  setBusinessBalance(Candle candle,Snapshot baseSnapshot){
        if(null!=candle&&null!=baseSnapshot){
            if(CandleCycle.DayCandle==candle.getCycle()){
                candle.setBusinessBalance(baseSnapshot.getBusinessBalance());
            }else{
                candle.setBusinessBalance(DoubleOperationUtil.add(candle.getBusinessBalance(), baseSnapshot.getLastAmount() * baseSnapshot.getLastPx()));
            }

        }
    }
    //设置最新价
    public   void  setLastpx(Candle candle,Snapshot baseSnapshot){
        if(null!=candle&&null!=baseSnapshot){
            candle.setLastPx(baseSnapshot.getLastPx());
        }
    }

}
