package com.caimao.bana.common.api.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class StringUtils {
    public static String toPinyin(String chines, Boolean onlyFirst, Boolean keepChines){
        try{
            //返回拼音
            String resultPinyin = "";
            //设置参数
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 没有音调数字
            format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);// u显示
            //转换拼音
            char[] chars = chines.toCharArray();
            for (char aChar : chars) {
                if(aChar > 128){
                    String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(aChar, format);
                    if(pinyin != null){
                        resultPinyin += onlyFirst?pinyin[0].charAt(0):pinyin[0];
                    }
                }else{
                    resultPinyin += keepChines?"":aChar;
                }
            }
            return resultPinyin;
        }catch(Exception e){
            return "";
        }
    }
}
