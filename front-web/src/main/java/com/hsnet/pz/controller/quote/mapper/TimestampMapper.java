package com.hsnet.pz.controller.quote.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.dozer.CustomConverter;

/**
 * 时间转化为毫秒数，供前端绘图使用
 * @author: zhouqs07071 
 * @since: 2014-6-27 下午4:41:21 
 * @history:
 */
public class TimestampMapper implements CustomConverter {

    @Override
    public Object convert(Object existingDestinationFieldValue,
            Object sourceFieldValue, Class<?> destinationClass,
            Class<?> sourceClass) {
        String sfv = sourceFieldValue.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Long time = 0L;
        try {
            time = sdf.parse(sfv).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time.toString();
    }

}
