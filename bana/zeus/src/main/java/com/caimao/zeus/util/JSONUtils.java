/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;

/**
 * @author 任培伟
 * @version 1.0
 */
public class JSONUtils {
    private static Logger logger = LoggerFactory.getLogger(JSONUtils.class);

    private ObjectMapper objectMapper;

    public JSONUtils() {
        super();
    }

    public JSONUtils(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;
    }

    public JSONUtils(JsonInclude.Include include) {
        super();
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(include);
    }

    public JSONUtils(JsonInclude.Include include, SerializationFeature serializationFeature) {
        super();
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(include);
        objectMapper.configure(serializationFeature, true);
    }

    public String jsonToString(Object object) {
        String jsonStr = StringUtils.EMPTY;

        try {
            jsonStr = objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.debug("在转换输入值为JSON字符串时出错!", e);
        }

        return jsonStr;
    }

    public <T> T fromJson(String JSONStr, Class<T> clazz) {
        if (StringUtils.isEmpty(JSONStr)) {
            return null;
        }

        T t = null;

        try {
            t = objectMapper.readValue(JSONStr, clazz);
        } catch (JsonParseException e) {
            logger.error("在转化JSON字符串为JAVA BEAN时发生JSON转化错误!", e);
        } catch (JsonMappingException e) {
            logger.error("在转化JSON字符串为JAVA BEAN时JSON字符串对应JAVA BEAN错误!", e);
        } catch (IOException e) {
            logger.error("在转化JSON字符串为JAVA BEAN时读取" + JSONStr + "时出错!", e);
        }
        return t;
    }

    public <T> T fromJson(String JSONStr, JavaType javaType) {
        if (StringUtils.isEmpty(JSONStr)) {
            return null;
        }

        T t = null;
        try {
            t = objectMapper.readValue(JSONStr, javaType);
        } catch (JsonParseException e) {
            logger.error("在转化JSON字符串为JAVA BEAN时发生JSON转化错误!", e);
        } catch (JsonMappingException e) {
            logger.error("在转化JSON字符串为JAVA BEAN时JSON字符串对应JAVA BEAN错误!", e);
        } catch (IOException e) {
            logger.error("在转化JSON字符串为JAVA BEAN时读取" + JSONStr + "时出错!", e);
        }
        return t;
    }

    public <E> JavaType creatCollectionType(Class<? extends Collection<E>> collectionClazz, Class<E> elementClazz) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClazz, elementClazz);
    }

    public <T> T read(InputStream inputStream, Class<T> clazz) {
        T t = null;

        try {
            t = objectMapper.readValue(inputStream, clazz);
        } catch (JsonGenerationException e) {
            logger.error("在通过JSON输入流转化为JAVA BEAN时,生成JSON时出错!", e);
        } catch (JsonMappingException e) {
            logger.error("在通过JSON输入流转化为JAVA BEAN时,JSON对应JAVA BEAN错误!", e);
        } catch (IOException e) {
            logger.error("在输出JSON时,发生IO异常", e);
        }
        return t;
    }

    public <T> T read(Reader reader, Class<T> clazz) {
        T t = null;

        try {
            t = objectMapper.readValue(reader, clazz);
        } catch (JsonGenerationException e) {
            logger.error("在通过JSON输入流转化为JAVA BEAN时,生成JSON时出错!", e);
        } catch (JsonMappingException e) {
            logger.error("在通过JSON输入流转化为JAVA BEAN时,JSON对应JAVA BEAN错误!", e);
        } catch (IOException e) {
            logger.error("在输出JSON时,发生IO异常", e);
        }
        return t;
    }

    public void write(Writer writer, Object object) {
        try {
            objectMapper.writeValue(writer, object);
        } catch (JsonGenerationException e) {
            logger.error("在输出JSON时,生成JSON错误!", e);
        } catch (JsonMappingException e) {
            logger.error("在输出JSON时,Object数据转化为JSON时出错!", e);
        } catch (IOException e) {
            logger.error("在输出JSON时,发生IO异常", e);
        }
    }

    public void write(OutputStream outputStream, Object object) {
        try {
            objectMapper.writeValue(outputStream, object);
        } catch (JsonGenerationException e) {
            logger.error("在输出JSON时,生成JSON错误!", e);
        } catch (JsonMappingException e) {
            logger.error("在输出JSON时,Object数据转化为JSON时出错!", e);
        } catch (IOException e) {
            logger.error("在输出JSON时,发生IO异常", e);
        }
    }

    public <T> T update(Object object, String jsonStr) {
        T t = null;

        try {
            t = objectMapper.readerForUpdating(object).readValue(jsonStr);
        } catch (JsonProcessingException e) {
            logger.error("在用" + jsonStr + "更新JAVA BEAN时出错!", e);
        } catch (IOException e) {
            logger.error("在用" + jsonStr + "更新JAVA BEAN时发生IO异常!", e);
        }
        return t;
    }

    public String jsonP(String function, Object object) {
        return jsonToString(new JSONPObject(function, object));
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}