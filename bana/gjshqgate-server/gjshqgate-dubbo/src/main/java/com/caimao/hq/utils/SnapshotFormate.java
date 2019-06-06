package com.caimao.hq.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.hq.api.entity.CandleCycle;
import com.caimao.hq.api.entity.NJSSnapshot;
import com.caimao.hq.api.entity.Snapshot;

/**
 * Created by yzc on 2015/9/26.
 */
public class SnapshotFormate{

      public static Boolean isAuth(Snapshot baseSnapshot){
            return  true;
      }
      public static void formateSnapshot(CandleCycle cycle,Snapshot baseSnapshot){
            if(null!=baseSnapshot){
                  MinTimeUtil.checkMiniTime(baseSnapshot);
                  baseSnapshot.setCycle(cycle);
                  String cycleTimeDate=MinTimeUtil.getTime(cycle, baseSnapshot);
                  String redisKey= MinTimeUtil.getRedisKey(cycle, baseSnapshot);
                  String redisKeyHistory=MinTimeUtil.getRedisKeyHistory(cycle, baseSnapshot);
                  if(!StringUtils.isBlank(cycleTimeDate)&&!StringUtils.isBlank(redisKey)){
                        baseSnapshot.setCycleTimeDate(cycleTimeDate);
                        baseSnapshot.setRedisKey(redisKey);
                        baseSnapshot.setRedisKeyHistory(redisKeyHistory);
                  }else{
                        throw new RuntimeException("格式化日期错误：获取cycleTimeDate或者redisKey为空"+baseSnapshot.toString());
                  }
            }
      }


      public static void formateSnapshotSJS(CandleCycle cycle,Snapshot baseSnapshot){
            if(null!=baseSnapshot){
                  SJSMinTimeUtil.checkMiniTime(baseSnapshot);
                  baseSnapshot.setCycle(cycle);
                  String cycleTimeDate=SJSMinTimeUtil.getTime(cycle, baseSnapshot);
                  String redisKey= SJSMinTimeUtil.getRedisKey(cycle, baseSnapshot);
                  String redisKeyHistory=SJSMinTimeUtil.getRedisKeyHistory(cycle, baseSnapshot);

                  if(!StringUtils.isBlank(cycleTimeDate)&&!StringUtils.isBlank(redisKey)){
                        baseSnapshot.setCycleTimeDate(cycleTimeDate);
                        baseSnapshot.setRedisKey(redisKey);
                        baseSnapshot.setRedisKeyHistory(redisKeyHistory);
                  }else{
                        throw new RuntimeException("格式化日期错误：获取cycleTimeDate或者redisKey为空"+baseSnapshot.toString());
                  }
            }
      }

      public static void formateSnapshotNJS(CandleCycle cycle,Snapshot baseSnapshot){
            if(null!=baseSnapshot){
                  baseSnapshot.setCycle(cycle);
                  String cycleTimeDate=NJSMinTimeUtil.getTime(cycle, baseSnapshot);
                  String redisKey= SJSMinTimeUtil.getRedisKey(cycle, baseSnapshot);
                  if(!StringUtils.isBlank(cycleTimeDate)&&!StringUtils.isBlank(redisKey)){
                        baseSnapshot.setCycleTimeDate(cycleTimeDate);
                        baseSnapshot.setRedisKey(redisKey);
                  }else{
                        throw new RuntimeException("格式化日期错误：获取cycleTimeDate或者redisKey为空"+baseSnapshot.toString());
                  }
            }
      }

     //南交所导入数据的时候  ，避免重复 ，rediskey不一样
      public static void formateSnapshotImport(CandleCycle cycle,Snapshot baseSnapshot,String importField){
            if(null!=baseSnapshot){
                  baseSnapshot.setCycle(cycle);
                  String cycleTimeDate=MinTimeUtil.getTime(cycle, baseSnapshot);
                  String redisKey= MinTimeUtil.getRedisKeyImport(cycle, baseSnapshot, importField);
                  if(!StringUtils.isBlank(cycleTimeDate)&&!StringUtils.isBlank(redisKey)){
                        baseSnapshot.setCycleTimeDate(cycleTimeDate);
                        baseSnapshot.setRedisKey(redisKey);
                  }else{
                        throw new RuntimeException("格式化日期错误：获取cycleTimeDate或者redisKey为空"+baseSnapshot.toString());
                  }
            }
      }

      public static void main(String args[]){


            Snapshot baseSnapshot=new NJSSnapshot();
            baseSnapshot.setMinTime("20151209235959");
            baseSnapshot.setExchange("NJS");
            baseSnapshot.setProdCode("AG");
            baseSnapshot.setCycle(CandleCycle.Minute1);
            formateSnapshot(CandleCycle.Minute1, baseSnapshot);
      }

}