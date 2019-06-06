package com.hsnet.pz.controller.quote.mapper;

import java.text.DecimalFormat;

import org.dozer.CustomConverter;

/**
 * 保留三位有效数字
 * @author: zhouqs07071 
 * @since: 2014-7-7 下午8:09:02 
 * @history:
 */
public class NumberFixed3Mapper implements CustomConverter {

    @Override
    public Object convert(Object existingDestinationFieldValue,
            Object sourceFieldValue, Class<?> destinationClass,
            Class<?> sourceClass) {
        DecimalFormat df = new DecimalFormat("0.000");
        return df.format(Double.parseDouble(sourceFieldValue.toString()));
    }
}
