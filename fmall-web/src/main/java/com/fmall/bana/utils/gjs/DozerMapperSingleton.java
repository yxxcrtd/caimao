package com.fmall.bana.utils.gjs;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        Field[] fields = bean.getClass().getDeclaredFields();
        Field.setAccessible(fields, true);
        List list=new ArrayList();
        Object obj = null;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                obj = field.get(bean);
                list.add(obj);

            } catch (Exception e) {
                list.add("");
                e.printStackTrace();
            }
        }

        return list;
    }

    public static List fillProperty(List beanList) {

      List result=new ArrayList();
      if(null!=beanList&&null!=result){

          for(Object obj:beanList){
                if(null!=obj){
                    result.add(fillProperty(obj));
                }
          }

      }
        return  result;
    }
}
