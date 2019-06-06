package com.caimao.gjs.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdCardUtils {
    public static Boolean IDCardValidate(String IDStr) throws ParseException {
        if(IDStr == null || IDStr.equals("")) return false;
        IDStr = IDStr.toUpperCase();
        String Ai = "";

        //判断号码的长度 15位或18位
        if(IDStr.length() != 15 && IDStr.length() != 18) return false;

        //18位身份证前17位位数字，如果是15位的身份证则所有号码都为数字
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (!isNumeric(Ai)) return false;

        // 判断出生年月是否有效
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 日期
        if (!isDate(strYear + "-" + strMonth + "-" + strDay)) return false;

        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                return false;
            }
        } catch (NumberFormatException | ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) return false;
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0)  return false;

        // 判断地区码是否有效
        Hashtable areaCode = GetAreaCode();
        //如果身份证前两位的地区码不在，则地区码有误
        if (areaCode.get(Ai.substring(0, 2)) == null) return false;

        if (!isVerifyCode(Ai, IDStr)) return false;

        return true;
    }


    /**
     * 判断第18位校验码是否正确
     * 第18位校验码的计算方式：
     * 　　1. 对前17位数字本体码加权求和
     * 　　公式为：S = Sum(Ai * Wi), i = 0, ... , 16
     * 　　其中Ai表示第i个位置上的身份证号码数字值，Wi表示第i位置上的加权因子，其各位对应的值依次为： 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 　　2. 用11对计算结果取模
     * 　　Y = mod(S, 11)
     * 　　3. 根据模的值得到对应的校验码
     * 　　对应关系为：
     * 　　 Y值：     0  1  2  3  4  5  6  7  8  9  10
     * 　　校验码： 1  0  X  9  8  7  6  5  4  3   2
     */
    private static boolean isVerifyCode(String Ai, String IDStr) {
        String[] VerifyCode = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = sum % 11;
        String strVerifyCode = VerifyCode[modValue];
        Ai = Ai + strVerifyCode;
        if (IDStr.length() == 18) {
            if (!Ai.equals(IDStr)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 地址编码
     */
    private static Hashtable GetAreaCode() {
        Hashtable<String, String> hashTable = new Hashtable<String, String>();
        hashTable.put("11", "北京");
        hashTable.put("12", "天津");
        hashTable.put("13", "河北");
        hashTable.put("14", "山西");
        hashTable.put("15", "内蒙古");
        hashTable.put("21", "辽宁");
        hashTable.put("22", "吉林");
        hashTable.put("23", "黑龙江");
        hashTable.put("31", "上海");
        hashTable.put("32", "江苏");
        hashTable.put("33", "浙江");
        hashTable.put("34", "安徽");
        hashTable.put("35", "福建");
        hashTable.put("36", "江西");
        hashTable.put("37", "山东");
        hashTable.put("41", "河南");
        hashTable.put("42", "湖北");
        hashTable.put("43", "湖南");
        hashTable.put("44", "广东");
        hashTable.put("45", "广西");
        hashTable.put("46", "海南");
        hashTable.put("50", "重庆");
        hashTable.put("51", "四川");
        hashTable.put("52", "贵州");
        hashTable.put("53", "云南");
        hashTable.put("54", "西藏");
        hashTable.put("61", "陕西");
        hashTable.put("62", "甘肃");
        hashTable.put("63", "青海");
        hashTable.put("64", "宁夏");
        hashTable.put("65", "新疆");
        return hashTable;
    }

    /**
     * 判断字符串是否为数字,0-9重复0次或者多次
     * @param strNum 字符串
     * @return boolean
     */
    private static boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(strNum);
        return isNum.matches();
    }

    /**
     * 功能：判断字符串出生日期是否符合正则表达式：包括年月日，闰年、平年和每月31天、30天和闰月的28天或者29天
     * @param strDate 日期
     * @return 是否正确
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
        Matcher m = pattern.matcher(strDate);
        return m.matches();
    }
}