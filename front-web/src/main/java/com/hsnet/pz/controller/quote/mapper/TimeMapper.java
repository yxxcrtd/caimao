package com.hsnet.pz.controller.quote.mapper;

import org.dozer.CustomConverter;

public class TimeMapper implements CustomConverter {

    @Override
    public Object convert(Object existingDestinationFieldValue,
            Object sourceFieldValue, Class<?> destinationClass,
            Class<?> sourceClass) {
        Object returnVale = null;
        if (sourceFieldValue != null) {
            returnVale = convertTime(sourceFieldValue.toString());
        }
        return returnVale;
    }

    private String convertTime(String time) {
        String result = time;
        if (!"0".equals(time)) {
            for (int i = time.length(); i < 6; i++) {
                time = "0" + time;
            }
            result = time.substring(0, 2) + ":" + time.substring(2, 4) + ":"
                    + time.substring(4, 6);
        }
        return result;
    }
}
