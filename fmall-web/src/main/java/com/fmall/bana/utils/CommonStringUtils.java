package com.fmall.bana.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by WangXu on 2015/6/1.
 */
public class CommonStringUtils {
    /**
     * 生成随机的字符串
     *
     * @param length 生成的长度
     * @return 随机的字符串
     */
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 返回map
     *
     * @param object 字符串 布尔类型
     * @return map
     */
    public static Map mapReturn(Object object) {
        Map<String, Object> result = new HashMap<>();
        result.put("result", object);
        return result;
    }
}
