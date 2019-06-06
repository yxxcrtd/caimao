package com.hsnet.pz.controller.quote.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dozer.CustomConverter;

/**
 * 日期转化，将8位数字或者8位字符串转化为 yyyy-MM-dd 格式日期
 * @author: zhouqs07071 
 * @since: 2014-6-26 下午8:10:48 
 * @history:
 */
public class DateMapper implements CustomConverter {

    @Override
    public Object convert(Object existingDestinationFieldValue,
            Object sourceFieldValue, Class<?> destinationClass,
            Class<?> sourceClass) {
        Object returnVale = null;
        if (sourceFieldValue != null) {
            returnVale = convertDate(sourceFieldValue.toString());
        }
        return returnVale;
    }

    private String convertDate(String str) {
        String returnVale = str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(str);
            returnVale = sdf1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnVale;
    }

}
