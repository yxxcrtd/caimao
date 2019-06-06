package com.hsnet.pz.controller.quote.utils;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;

/**
 * 映射工具类
 * @author: zhouqs07071 
 * @since: 2014-6-26 下午7:35:53 
 * @history:
 */
public class DozerMapperManager extends DozerBeanMapper {

    /**
     * list<E> 映射到 List<T>
     * @param list
     * @param clazz
     * @return 
     * @create: 2014-6-26 下午4:10:50 zhouqs07071
     * @history:
     */
    public <T, E> List<T> mapper(List<E> list, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        for (E obj : list) {
            result.add(super.map(obj, clazz));
        }
        return result;
    }

    /**
     * Object映射到Object
     * @param obj
     * @param clazz
     * @return 
     * @create: 2014-6-26 下午4:11:26 zhouqs07071
     * @history:
     */
    public <T, E> T mapper(E obj, Class<T> clazz) {
        return super.map(obj, clazz);
    }

}
