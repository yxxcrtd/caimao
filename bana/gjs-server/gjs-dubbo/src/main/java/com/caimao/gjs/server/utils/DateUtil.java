package com.caimao.gjs.server.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.lang.StringUtils;

/**
 * Created by WangXu on 2015/4/23.
 */
public class DateUtil {
    public static final String TIME_PATTERN = "HH:mm";
    public static final String TIME_THRESHOLD_MIN = "00:00:00";
    public static final String TIME_THRESHOLD_MAX = "23:59:59";
    public static final String DATE_FORMAT_STRING = "yyyyMMdd";
    public static final String DISPLAY_DATE_FORMAT_STRING = "yyyy-MM-dd";
    public static final String DATA_TIME_PATTERN_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATA_TIME_PATTERN_2 = "yyyy-MM-dd HH:mm";
    public static final String DATA_TIME_PATTERN_3 = "yyyyMMddHHmmss";
    public static final String DATA_TIME_PATTERN_4 = "HHmmss";
    public static final String DATA_TIME_PATTERN_5 = "HHmm";
    public static final String DATA_TIME_PATTERN_6 = "MM-dd HH:mm";
    public static final String DATA_TIME_PATTERN_7 = "MM月dd日";
    public static final String TIME_BEGIN = " 00:00:00";
    public static final String TIME_END = " 23:59:59";

    public static String getTimestampToString(Timestamp obj)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String str = df.format(obj);
        return str;
    }

    public static String getTimestampToString(String formatPattern, Timestamp obj)
    {
        SimpleDateFormat df = new SimpleDateFormat(formatPattern);
        String str = df.format(obj);
        return str;
    }

    public static Timestamp getStringToTimestamp(String str)
    {
        Timestamp ts = Timestamp.valueOf(str);
        return ts;
    }

    public static Date strToDate(String str, String pattern) {
        Date dateTemp = null;
        SimpleDateFormat formater2 = new SimpleDateFormat(pattern);
        try {
            dateTemp = formater2.parse(str);
        } catch (Exception e) {
        }
        return dateTemp;
    }

    public static String getDatePattern()
    {
        return "yyyy-MM-dd";
    }

    public static String getDateTimePattern() {
        return getDatePattern() + " HH:mm:ss.S";
    }

    public static String getDate(Date aDate)
    {
        String returnValue = "";

        if (aDate != null) {
            SimpleDateFormat df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(aDate);
        }

        return returnValue;
    }

    public static Date convertStringToDate(String aMask, String strDate)
    {
        if (StringUtils.isBlank(strDate)) return null;

        Date date = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(aMask);
            date = df.parse(strDate);
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return date;
    }

    public static String getTimeNow(Date theTime)
    {
        return getDateTime("HH:mm", theTime);
    }

    public static Calendar getToday()
            throws ParseException
    {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    public static Calendar getCurrentDay() throws ParseException {
        Calendar cal = Calendar.getInstance();
        return cal;
    }

    public static String getDateTime(String aMask, Date aDate)
    {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null)
        {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return returnValue;
    }

    public static String convertDateToString(Date aDate)
    {
        return getDateTime(getDatePattern(), aDate);
    }

    public static Date convertStringToDate(String strDate)
            throws ParseException
    {
        Date aDate = null;

        aDate = convertStringToDate(getDatePattern(), strDate);

        return aDate;
    }

    public static String convertDateToString(String pattern, Date aDate)
    {
        return getDateTime(pattern, aDate);
    }

    public static Date getRelativeDate(Date startDate, int day)
    {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(startDate);
            calendar.add(5, day);
            return calendar.getTime(); } catch (Exception e) {
        }
        return startDate;
    }

    public static int getDay(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(7) - 1;
    }

    public static int countDays(String beginStr, String endStr, String Foramt)
    {
        Date end = strToDate(endStr, Foramt);
        Date begin = strToDate(beginStr, Foramt);
        long times = end.getTime() - begin.getTime();
        return (int)(times / 60L / 60L / 1000L / 24L);
    }

    public static int getDayNum()
    {
        Calendar cal = Calendar.getInstance();
        String day = convertDateToString("yyyyMMdd", cal.getTime());
        return Integer.parseInt(day);
    }

    public static int getTimeNum()
    {
        Calendar cal = Calendar.getInstance();
        String time = convertDateToString("HHmmss", cal.getTime());
        return Integer.parseInt(time);
    }

    public static String getNowTime()
    {
        Calendar cal = Calendar.getInstance();
        String time = convertDateToString("yyyyMMddHHmmss", cal.getTime());
        return time;
    }

    public static String getNowTime1() {
        Calendar cal = Calendar.getInstance();
        String time = convertDateToString("yyyy-MM-dd HH:mm:ss", cal.getTime());
        return time;
    }

    public static int getRelativeDate(Date startDate, int month, int day)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(5, day);
        calendar.add(2, month);
        return Integer.parseInt(convertDateToString("yyyyMMdd", calendar.getTime()));
    }

    public static Date getRelativeDateByMonth(Date startDate, int month)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(2, month);
        return calendar.getTime();
    }

    public static long convertDateNumToDatetime(String date) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.parse(date).getTime();
    }

    public static String formateDateStr(String dateStr, String template, String showTemplate)
    {
        SimpleDateFormat df = new SimpleDateFormat(template);
        try {
            Date date = df.parse(dateStr);
            SimpleDateFormat display_df = new SimpleDateFormat(showTemplate);
            return display_df.format(date); } catch (Exception e) {
        }
        return dateStr;
    }

    public static String formateDateStr(String dateStr)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = df.parse(dateStr);
            SimpleDateFormat display_df = new SimpleDateFormat("yyyy-MM-dd");

            return display_df.format(date); } catch (Exception e) {
        }
        return dateStr;
    }

    public static String formateDateStrT(String dateStr)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(dateStr);
            SimpleDateFormat display_df = new SimpleDateFormat("yyyy-MM-dd");

            return display_df.format(date); } catch (Exception e) {
        }
        return dateStr;
    }

    public static String fillUpLeftZero(String dateStr)
    {
        int length = dateStr.length();
        StringBuffer date = new StringBuffer();
        while (length < 6) {
            date.append("0");
            length++;
        }
        return date.append(dateStr).toString();
    }

    public static String getFormatDate(String pattern)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    public static Date translate2Date(String dateStr, boolean addOneDay)
    {
        Date date = null;
        if ((dateStr != null) && (dateStr.length() > 8)) {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            if (addOneDay) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(5, 1);
                date = calendar.getTime();
            }
        }
        return date;
    }

    public static String addDays(String beginDate, String pattern, int days)
    {
        try
        {
            Date date = new SimpleDateFormat(pattern).parse(beginDate);
            if (days != 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(5, days);
                date = calendar.getTime();
            }
            return convertDateToString(pattern, date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beginDate;
    }

    public static Date addDays(Date beginDate, int days)
    {
        Date date = beginDate;
        if (days != 0) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(5, days);
                date = calendar.getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static int daysBetween(Date smdate, Date bdate)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / 86400000L;
            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            e.printStackTrace();
        }return -1;
    }

    public static int daysBetween(String smdate, String bdate, String pattern)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / 86400000L;
            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            e.printStackTrace();
        }return -1;
    }

    public static String convertDateNumToStr(String date)
    {
        if ((date != null) && (date.length() == 8)) {
            return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
        }

        return date;
    }

    public static String convertStrToDateNum(String date)
    {
        if ((date != null) && (date.length() == 10)) {
            return date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
        }

        return date;
    }

    public static Date getTodayStart()
    {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(11, 0);
        currentDate.set(12, 0);
        currentDate.set(13, 0);
        return (Date)currentDate.getTime().clone();
    }

    public static Date getTodayEnd()
    {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(11, 23);
        currentDate.set(12, 59);
        currentDate.set(13, 59);
        return (Date)currentDate.getTime().clone();
    }

    public static String trimMillis(String dateTime)
    {
        if ((dateTime != null) && (dateTime.length() > 19)) {
            dateTime = dateTime.substring(0, 19);
        }
        return dateTime;
    }

    public static String convertDateToDateTime(String date, String time)
    {
        if ((StringUtils.isNotEmpty(date)) && (date.length() == 10)) {
            date = date + " " + time;
        }
        return date;
    }

    public static Long getTimesToday(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis()/1000;
    }

}
