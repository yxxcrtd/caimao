/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * @author 任培伟
 * @version 1.0
 */
public class ReflectUtils {
    private static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    private static final String DEFAULT_CGLIB_SEPARATOR = "$$";

    public static Class<?> getDefineClass(String className) {
        Class<?> clazz = null;

        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("由实体类名:" + className + "得到其Class对象时发生异常,未找到" + className + "对应的Class对象!", e);
        }
        return clazz;
    }

    public static <T> T clazzInitialize(Class<T> clazzImpl) {
        T t = null;

        try {
            t = (T) clazzImpl.newInstance();
        } catch (InstantiationException e) {
            logger.error("将java.lang.Class对象初始化时出错!", e);
        } catch (IllegalAccessException e) {
            logger.error("非法访问java.lang.Class对象，导致该对象初始化出错!", e);
        }
        return t;
    }

    public static Class<?> getRealClass(Object object) {
        Class<?> clazz = object.getClass();

        if (clazz != null && clazz.getName().contains(DEFAULT_CGLIB_SEPARATOR)) {
            Class<?> superClazz = clazz.getSuperclass();
            if (superClazz != null && !superClazz.equals(Object.class)) {
                clazz = superClazz;
            }
        }
        return clazz;
    }

    public static Class<?> getSuperClassGenericType(Class<?> clazz, int index) {
        Type superClassType = clazz.getGenericSuperclass();

        if (!(superClassType instanceof ParameterizedType)) {
            logger.warn(clazz.getName() + "的父类类型不是ParameterizedType类型!");
            return Object.class;
        }

        Type[] actualTypeArguments = ((ParameterizedType) superClassType)
                .getActualTypeArguments();

        if (index < 0
                || (actualTypeArguments != null && actualTypeArguments.length <= index)) {
            logger.warn("index :" + index + "但是父类类型的泛型数组长度是" + actualTypeArguments.length + "!");
            return Object.class;
        }

        if (!(actualTypeArguments[index] instanceof Class<?>)) {
            logger.warn(clazz.getName() + "其父类类型的泛型数组的第" + (index + 1) + "个元素没有设置成为泛型!");
            return Object.class;
        }
        return (Class<?>) actualTypeArguments[index];
    }

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getConstructor(String constructorName, Class<?>... classes) {
        Constructor<T> constructor = null;

        try {
            constructor = (Constructor<T>) getDefineClass(constructorName)
                    .getConstructor(classes);
        } catch (SecurityException e) {
            logger.error("由名称:" + constructorName + "及构造方法参数类型得到其构造方法时发生反射安全性异常!", e);
        } catch (NoSuchMethodException e) {
            logger.error("由名称:" + constructorName + "及构造方法参数类型得到其构造方法时发生异常,无此构造方法!", e);
        }
        return constructor;
    }

    public static <T> T clazzInitializeByConstructor(
            Constructor<T> constructor, Object... objects) {
        T t = null;

        try {
            t = constructor.newInstance(objects);
        } catch (IllegalArgumentException e) {
            logger.error("由构造方法" + constructor.getName() + "发射得到其实体实例时,因为非法的访问参数而出错!", e);
        } catch (InstantiationException e) {
            logger.error("由构造方法" + constructor.getName() + "发射得到其实体实例时,初始化实例实例时出错!", e);
        } catch (IllegalAccessException e) {
            logger.error("由构造方法" + constructor.getName() + "反射得到其实体实例时,非法访问构造方法而出错!", e);
        } catch (InvocationTargetException e) {
            logger.error("由构造方法" + constructor.getName() + "发射得到其实体实例时,执行构造函数时出错!", e);
        }
        return t;
    }

    public static <A extends Annotation> Annotation getAnnotationOnAnnoFromClass(
            String className, Class<A> annoClazz) {
        if (StringUtils.isBlank(className) || annoClazz == null) {
            throw new NullPointerException();
        }

        Annotation annotationOnAnno = null;

        Class<?> clazz = getDefineClass(className);
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(annoClazz)) {
                annotationOnAnno = annotation;
                break;
            }
        }

        return annotationOnAnno;
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(Method method, Object excuteObject,
                                     Object... parameters) {
        T t = null;
        try {
            t = (T) method.invoke(excuteObject, parameters);
        } catch (IllegalArgumentException e) {
            logger.error("执行" + method.getName() + "方法时,方法参数错误!", e);
        } catch (IllegalAccessException e) {
            logger.error("执行" + method.getName() + "方法时,非法访问此方法出错!", e);
        } catch (InvocationTargetException e) {
            logger.error("执行" + method.getName() + "方法时,对象" + excuteObject.toString() + "不能执行此方法!", e);
        }
        return t;
    }

    public static <T> T invokeStaticMethod(String className, String methodName,
                                           Class<?>[] parameterTypes, Object... parameters) {
        T t = null;

        if (StringUtils.isEmpty(className)) {
            logger.warn("类名为空,不能反射调用静态方法!");
        }

        if (StringUtils.isEmpty(methodName)) {
            logger.warn("静态方法名为空,不能反射调用静态方法!");
        }

        Class<?> invokeClazz = getDefineClass(className);
        Method method = null;

        try {
            method = invokeClazz.getMethod(methodName, parameterTypes);
        } catch (SecurityException e) {
            logger.error("执行" + invokeClazz.getName() + "类的静态方法:" + methodName + "时,发生安全访问错误!", e);
        } catch (NoSuchMethodException e) {
            logger.error(invokeClazz.getName() + "中无" + methodName + "所描述的静态方法!", e);
        }

        if (invokeClazz != null && method != null) {
            t = invokeMethod(method, invokeClazz, parameters);
        }
        return t;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Enum> T getEnum(String className,
                                             String constantName) {
        T t = null;

        Class<?> enumClazz = getDefineClass(className);
        for (Object enumObj : enumClazz.getEnumConstants()) {
            T enumConstant = (T) enumObj;
            if (enumConstant.name().equalsIgnoreCase(constantName)) {
                t = enumConstant;
                break;
            }
        }

        return t;
    }
}