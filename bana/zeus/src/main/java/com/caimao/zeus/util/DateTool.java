/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 任培伟
 * @version 1.0
 */

public class DateTool {
    private static Logger logger = LoggerFactory.getLogger(DateTool.class);

    private static final String DEFAULT_DAY_MATTER = "yyyy-MM-dd";

    private static final String MORNING = "上午";

    private static final String NOON = "中午";

    private static final String AFTERNOON = "下午";

    /*
     * 得到当前日期的下个星期几的日期时间
     * 
     * @param day_of_week int 当前日期在一周中的整数值
     */
    public static String getTheDay(int day_of_week, String matter) {
        Calendar calendar = Calendar.getInstance();
        int today_of_week = calendar.get(Calendar.DAY_OF_WEEK);

        int minusDay = getMinusNum(day_of_week, today_of_week);
        calendar.add(Calendar.DATE, minusDay);

        Date date = calendar.getTime();
        String theDay = getDate(date, matter);

        return theDay;
    }

    public static String getDate(Date date, String matter) {
        DateFormat format = new SimpleDateFormat(matter);

        return format.format(date);
    }

    public static String getDate(Date date) {
        return getDate(date, DEFAULT_DAY_MATTER);
    }

    public static Date getDate(String dateStr, String matter) {
        DateFormat format = new SimpleDateFormat(matter);
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            logger.debug("日期:" + dateStr + "转化为date类型时发生错误!", e);
        }
        return date;
    }

    public static Date getDate(String dateStr) {
        return getDate(dateStr, DEFAULT_DAY_MATTER);
    }

    /*
     * 取得两个日期间的相隔天数
     */
    public static int getMinusDays(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);

        if (start.after(end)) {
            start.setTime(endDate);
            end.setTime(startDate);
        }

        int minusYear = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);

        int minusDay = end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
        for (int i = 0; i < minusYear; i++) {
            start.add(Calendar.YEAR, i + 1);
            minusDay += start.getActualMaximum(Calendar.DAY_OF_YEAR);
        }
        return minusDay;
    }

    public static int getMinusNum(int day_of_week, int today_of_week) {
        if (day_of_week > today_of_week) return day_of_week - today_of_week;
        return 7 - today_of_week + day_of_week;
    }

    public static String morningOrAfterNoon(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateTool.getDate(date, "yyyy年MM月dd日 hh:mm"));

        int result = calendar.get(Calendar.HOUR_OF_DAY);
        if (result < 12) {
            return MORNING;
        } else if (result > 13) {
            return AFTERNOON;
        }
        return NOON;
    }
}
