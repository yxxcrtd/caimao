/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 任培伟
 * @version 1.0
 */
public class AspectUtils {
    private static Logger logger = LoggerFactory.getLogger(AspectUtils.class);

    public static String getInputValue(JoinPoint jp, Inclusion inclusion) {
        String inputValue = StringUtils.EMPTY;
        StringBuffer inputStringBuffer = new StringBuffer();

        JSONUtils jsonUtils = new JSONUtils(JsonInclude.Include.ALWAYS);
        jsonUtils.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        for (Object object : jp.getArgs()) {
            String jsonStr = jsonUtils.jsonToString(object);
            if (StringUtils.isEmpty(jsonStr)) {
                jsonStr = "不能转换" + object.getClass().getName()
                        + "对象为对应JSON对象,得到的JSON对象为空字符串,原因可能是此对象或其属性为非序列化对象";
            }
            inputStringBuffer.append(jsonStr);
            inputStringBuffer.append(",");
        }

        if (inputStringBuffer.lastIndexOf(",") > 0) {
            inputValue = inputStringBuffer.deleteCharAt(inputStringBuffer.lastIndexOf(",")).toString();
        }
        return inputValue;
    }

    public static String getInputValue(JoinPoint jp) {
        return getInputValue(jp, Inclusion.ALWAYS);
    }

    public static String getOutputValue(ProceedingJoinPoint pjp,
                                        Inclusion inclusion, Object... objects) {
        String output = StringUtils.EMPTY;
        JSONUtils jsonUtils = new JSONUtils(JsonInclude.Include.ALWAYS);
        jsonUtils.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        try {
            output = jsonUtils.jsonToString(pjp.proceed(objects));
        } catch (Throwable e) {
            logger.debug("在执行方法得到输出值时发生错误!", e);
        }

        if (StringUtils.isEmpty(output)) {
            output = "无法转换" + objects.toString() + "为对应JSON字符串";
        }
        return output;
    }

    public static String getOutputValue(ProceedingJoinPoint pjp, Object... objects) {
        return getOutputValue(pjp, Inclusion.ALWAYS, objects);
    }
}