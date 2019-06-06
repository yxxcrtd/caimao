package com.caimao.gjs.server.utils.sjs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class StringUtil {
    /**
     * @param s
     * @param c
     * @param n
     * @param f
     * @return
     */
    public static String FILL(String s, char c, int n, char f) {
        int iByteLen = StringToBytes(s).length;
        if (iByteLen >= n) {
            return s;
        } else {
            byte[] fillChars = new byte[n - iByteLen];
            for (int i = 0; i < fillChars.length; i++)
                fillChars[i] = (byte) c;

            if (f == 'L') //左补
            {
                return new String(fillChars) + s;
            } else //右补
            {
                return s + new String(fillChars);
            }

        }
    }

    public static byte[] StringToBytes(String str) {
        try {
            if (str == null || str.length() <= 0)
                return new byte[0];
            else
                return str.getBytes("GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ByteToString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "GBK");
    }

    public static String ByteToString1(String bytes) throws UnsupportedEncodingException {
        return new String(bytes.getBytes("GBK"), "UTF-8");
    }

    /**
     * 追加并替换字符串
     *
     * @param parma
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String AppendString(String parma) throws UnsupportedEncodingException {
        StringBuffer resString = new StringBuffer();
        resString.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
        parma = parma.replaceAll("<request xmlns=\"\">", "<request>");
        resString.append(parma);
        return resString.toString();
    }

    /**
     * 日期字符串格式处理
     *
     * @param date
     * @param type
     * @return
     */
    public static String DateTime(Date date, int type) {
        String dateTime = null;
        if (type == 1) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            dateTime = format.format(date);
        }
        return dateTime;
    }

    ;

    /**
     * 处理字符串日期格式
     *
     * @param dateTime
     * @return
     * @throws ParseException
     */
    public static String ToDateTime(String dateTime) throws ParseException {
        dateTime = dateTime.replace("-", "");
        return dateTime;
    }

    ;


    /**
     * 生成随机文件名：当前年月日时分秒+五位随机数
     *
     * @return
     */
    public static String GetRandomFileName() {

        SimpleDateFormat simpleDateFormat;

        simpleDateFormat = new SimpleDateFormat("mmss");

        Date date = new Date();

        String str = simpleDateFormat.format(date);
        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数  
        return rannum + str.substring(0, str.length() - 1);// 当前时间
    }

    /**
     * json字符串转换为map
     *
     * @param jsonString
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getMapJson(String jsonString) {
        /*JSONObject jsonObject = JSONObject.fromObject(jsonString);
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (Iterator iter = jsonObject.keys(); iter.hasNext(); ) {
            String key = (String) iter.next();
            map.put(key, jsonObject.get(key));
        }*/

        HashMap<String, Object> map = JSON.parseObject(jsonString, HashMap.class);


        return map;
    }


}
