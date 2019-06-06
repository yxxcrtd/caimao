package com.caimao.bana.server.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class StringProcessUtils {
    public static String getMarketTypeByCode(String stockCode) {
        if(stockCode.length() != 6) return "";
        String marketType = "";
        String stockPre = stockCode.substring(0, 1);
        switch (stockPre) {
            case "0":
            case "1":
            case "2":
            case "3":
                marketType = "2";
                break;
            case "5":
            case "6":
                marketType = "1";
                break;
        }
        return marketType;
    }

    public static String StringToPinyin(String hanzi) throws Exception{
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 没有音调数字
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);// u显示

        String result = "";
        if(hanzi != null && !hanzi.equals("")){
            char[] chars = hanzi.toCharArray();
            for (char aChar : chars) {
                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(aChar, format);
                if(pinyin != null){
                    result += pinyin[0];
                }else{
                    result += aChar;
                }
            }
        }
        return result;
    }
}