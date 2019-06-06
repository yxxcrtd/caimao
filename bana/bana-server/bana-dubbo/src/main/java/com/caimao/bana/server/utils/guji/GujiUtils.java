package com.caimao.bana.server.utils.guji;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 股计工具类
 * Created by Administrator on 2016/1/8.
 */
@Component
public class GujiUtils {

    /**
     * 格式化时间为字符串
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        if (date == null) return "--";
        Long diff = (new Date().getTime() - date.getTime()) / 1000;
        Long day = diff / (60 * 60 * 24);
        Long hour = diff / (60 * 60) - day * 24;
        Long min = diff / 60 - day * 60 * 24 - hour * 60;
        if (day > 0) {
            if (day > 60) {
                // 返回 年月日时分
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                return df.format(date);
            }
            if (day >= 1) {
                // 返回 月日时分
                DateFormat df = new SimpleDateFormat("MM-dd HH:mm");
                return df.format(date);
            }
            return day + "天前";
        }
        if (hour > 0) {
            return hour + "小时前";
        }
        if (min > 1) {
            return min + "分钟前";
        }
        return "刚刚";
    }


}
