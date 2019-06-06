package com.caimao.hq.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.caimao.hq.api.entity.Candle;
import com.caimao.hq.api.entity.FieldEntity;
import com.caimao.hq.api.entity.Snapshot;
import org.apache.commons.collections.map.LinkedMap;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.*;

/**
 * dozer单例的wrapper.
 * <p/>
 * dozer在同一jvm里使用单例即可,无需重复创建.
 * 但Dozer4.0自带的DozerBeanMapperSingletonWrapper必须使用dozerBeanMapping
 * .xml作参数初始化,因此重新实现.
 * <p/>
 * 实现PO到VO的深层次复制
 */
public class DozerMapperSingleton {

    private static Mapper instance;

    private DozerMapperSingleton() {
        // shoudn't invoke.
    }

    public static synchronized Mapper getInstance() {
        if (instance == null) {
            instance = new DozerBeanMapper();
        }
        return instance;
    }


    public static void listCopy(List<Object> sourc, List dest, String className)
            throws Exception {

        if (null != sourc && null != dest) {
            dest.clear();
            for (Object obj : sourc) {
                Object obdest = Class.forName(className).newInstance();
                DozerMapperSingleton.getInstance().map(obj, obdest);
                dest.add(obdest);
            }
        }
    }

    public static List fillProperty(Object bean) {


        return getFiledsList(bean);
    }

      //父类属性获取不到
      public static LinkedHashMap<String, FieldEntity> getFiledsMap(Object object)
                throws SecurityException, IllegalArgumentException, NoSuchMethodException,
                IllegalAccessException, InvocationTargetException {
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            LinkedHashMap<String, FieldEntity> map = new LinkedHashMap<String, FieldEntity>();

            for (int i = 0; i < fields.length; i++) {
                Object resultObject = invokeMethod(object, fields[i].getName(), null);
                map.put(fields[i].getName(), new FieldEntity(fields[i].getName(), resultObject, fields[i].getType()));
            }

            return map;
       }


    public static List getFiledsList(Object object) {


        Field[] fields = object.getClass().getDeclaredFields();
        Field.setAccessible(fields, true);
        List list=new ArrayList();
        Object obj = null;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                obj = field.get(object);
                list.add(obj);

            } catch (Exception e) {
                list.add("");
                e.printStackTrace();
            }
        }

        return list;
    }

    public static void setObjParaKeys(LinkedMap result,String jsonParaKeys,Object bean){
        if(StringUtils.isBlank(jsonParaKeys)){
            if(bean instanceof  Candle){
                Candle temp=(Candle)bean;
                result.put("prodCode",temp.getProdCode());
                result.put("exchange",temp.getExchange());
                result.put("minTime", temp.getMinTime());
                result.put("preClosePx", temp.getPreClosePx());
                result.put("lastSettle", temp.getLastSettle());
                result.put("isGoods", temp.getIsGoods());
            }else if(bean instanceof Snapshot){
                Snapshot temp=(Snapshot)bean;
                result.put("prodCode",temp.getProdCode());
                result.put("exchange",temp.getExchange());
                result.put("prodName",temp.getProdName());
            }else{
                throw new RuntimeException("参数设置只支持K线，和分时数据，setObjParaKeys");
            }
        }else{
            DozerMapperSingleton.fillProperty( result, jsonParaKeys, bean);
        }
    }
    public static LinkedHashMap getFiledsAllMap(Object object) {


        Field[] fields = object.getClass().getDeclaredFields();
        Field.setAccessible(fields, true);
        LinkedHashMap map = new LinkedHashMap();
        Object obj = null;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                obj = field.get(object);
                map.put(field.getName(),obj);

            } catch (Exception e) {
                map.put(field.getName(),"");
                e.printStackTrace();
            }
        }
        return map;
    }
        public static Object invokeMethod(Object owner, String fieldname,
                                          Object[] args) throws SecurityException, NoSuchMethodException,
                IllegalArgumentException, IllegalAccessException, InvocationTargetException {
            Class ownerClass = owner.getClass();

            Method method = null;
            method = ownerClass.getMethod(GetterUtil.toGetter(fieldname));

            Object object = null;
            object = method.invoke(owner);

            return object;
        }
    public static Object invokeMethod(Object owner, String methodName) throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class ownerClass = owner.getClass();

        Method method = null;
        method = ownerClass.getMethod(methodName);
        Object object = null;
        object = method.invoke(owner);

        return object;
    }


    public  static void fillProperty(LinkedMap result,String jsonParaKeys,Object bean){

        if(null!=result&&null!=bean&&!StringUtils.isBlank(jsonParaKeys)){

            LinkedHashMap map=getFiledsAllMap(bean);

            JSONArray array=JSON.parseArray(jsonParaKeys);
            if(null!=array){

                for(int k=0;k<array.size();k++){
                    String reqPara="";
                    Object obj = null;
                    try {
                        reqPara=array.get(k).toString();
                        if("ret".equalsIgnoreCase(reqPara)){
                            continue;
                        }
                        obj=map.get(reqPara.toLowerCase())==null?"":map.get(reqPara.toLowerCase());
                    } catch (Exception e) {
                        obj=null;
                        reqPara=array.get(k).toString();
                        e.printStackTrace();
                    }
                    result.put(reqPara,obj);
                }
            }


        }

    }
    public static List fillProperty(Object bean,String jsonParaKeys) {


        LinkedHashMap map=getFiledsAllMap(bean);
        List list=new ArrayList();
        if(!StringUtils.isBlank(jsonParaKeys)){
            JSONArray array=JSON.parseArray(jsonParaKeys);
            if(null!=array){
                String reqPara="";

                for(int k=0;k<array.size();k++){
                    Object obj = null;
                    try {
                        reqPara=array.get(k).toString();
                        obj=map.get(reqPara.toLowerCase())==null?"":map.get(reqPara.toLowerCase());
                    } catch (Exception e) {
                        obj=null;
                        e.printStackTrace();
                    }
                    list.add(obj);
                }
            }
        }
        return list;
    }
    public static List fillProperty(List beanList) {

      List result=new ArrayList();
      if(null!=beanList){

          for(Object obj:beanList){
                if(null!=obj){
                    result.add(fillProperty(obj));
                }
          }
      }
        return  result;
    }

    public static List fillProperty(List beanList,String jsonParaKeys) {

        List result=new ArrayList();
        if(null!=beanList&&null!=result){

            for(Object obj:beanList){
                if(null!=obj){
                    result.add(fillProperty(obj,jsonParaKeys));
                }
            }

        }
        return  result;
    }
}
