/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.fmall.bana.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StringHandleUtils {
    public static String inputFilter(String input) {
        if (input == null) return null;
        HashMap<String, String> filterList = new HashMap<>();
        filterList.put("%20", "");
        filterList.put("%27", "");
        filterList.put("%2527", "");
        filterList.put("*", "");
        filterList.put("\"", "&quot;");
        filterList.put("'", "&#039");
        filterList.put("<", "&lt;");
        filterList.put(">", "&gt;");
        filterList.put("{", "");
        filterList.put("}", "");
        filterList.put("\\", "");
        filterList.put("<script", "");
        filterList.put("<iframe", "");
        filterList.put("<frame", "");
        filterList.put("</script>", "");
        filterList.put("</iframe>", "");
        filterList.put("</frame>", "");
        filterList.put("script", "");
        filterList.put("iframe", "");
        filterList.put("frame", "");

        for (Map.Entry<String, String> entry : filterList.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue());
        }
        return input;
    }

    public static void main(String[] args) {
        System.out.println(StringHandleUtils.inputFilter("<script>alert</script>"));
    }

    public static String getStartDate(Integer stepDate) {
        if (stepDate == null) return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        switch (stepDate) {
            case 0:
                calendar.add(Calendar.YEAR, -10);
                break;
            case 1:
                break;
            case 7:
                calendar.add(Calendar.DATE, -7);
                break;
            case 30:
                calendar.add(Calendar.MONTH, -1);
                break;
            case 90:
                calendar.add(Calendar.MONTH, -3);
                break;
            case 180:
                calendar.add(Calendar.MONTH, -6);
                break;
            default:
                break;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }
}
