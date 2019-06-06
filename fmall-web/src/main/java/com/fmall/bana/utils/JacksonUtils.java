package com.fmall.bana.utils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2015/9/29.
 */
public abstract class JacksonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JacksonUtils.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        objectMapper.setFilters((new SimpleFilterProvider()).setFailOnUnknownId(false));
    }

    public JacksonUtils() {
    }

    public static final String toJsonString(Object object, String... properties) {
        try {
            SimpleFilterProvider e = new SimpleFilterProvider();
            e.addFilter(((JsonFilter) AnnotationUtils.findAnnotation(object.getClass(), JsonFilter.class)).value(), SimpleBeanPropertyFilter.filterOutAllExcept(properties));
            return objectMapper.writer(e).writeValueAsString(object);
        } catch (JsonProcessingException var3) {
            logger.error("转换对象【" + object.toString() + "】到json格式字符串失败：" + var3);
            return null;
        }
    }

    public static final String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException var2) {
            logger.error("转换对象【" + object.toString() + "】到json格式字符串失败：" + var2);
            return null;
        }
    }
}
