/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.util;

import org.dozer.DozerBeanMapper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 任培伟
 * @version 1.0
 */
public class DozerUtils {
    private static DozerBeanMapper beanMapper = new DozerBeanMapper();

    public static <T> T map(Object source, Class<T> destinationClazz) {
        return beanMapper.map(source, destinationClazz);
    }

    public static <T> List<T> mapList(Collection<?> sourceCollection, Class<T> destinationClazz) {
        List<T> destinationList = new LinkedList<T>();

        for (Object object : sourceCollection) {
            T t = beanMapper.map(object, destinationClazz);
            destinationList.add(t);
        }
        return destinationList;
    }

    public static void copy(Object source, Object destination) {
        beanMapper.map(source, destination);
    }
}