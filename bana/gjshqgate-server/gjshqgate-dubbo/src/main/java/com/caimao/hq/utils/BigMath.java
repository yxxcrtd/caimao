package com.caimao.hq.utils;

import java.math.BigDecimal;

/**
 * 大整数的加减乘除
 * Created by WangXu on 2015/6/9.
 */
public class BigMath {

    // 保留的有效小数位
    public static int scale = 5;
    // 进位取舍的模式
    public static int roundingMode = BigDecimal.ROUND_HALF_DOWN;

    /* 各个类型的大整形的加法 */
//    public static BigDecimal add(int a, int b) {
//        return new BigDecimal(a).add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(long a, long b) {
//        return new BigDecimal(a).add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(double a, double b) {
//        return new BigDecimal(a).add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(String a, String b) {
//        return new BigDecimal(a).add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(BigDecimal a, BigDecimal b) {
//        return a.add(b).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(int a, long b) {
//        return new BigDecimal(a).add(new BigDecimal(b)).setScale(5, BigMath.roundingMode);
//    }
//    public static BigDecimal add(int a, double b) {
//        return new BigDecimal(a).add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(int a, String b) {
//        return new BigDecimal(a).add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(String a, long b) {
//        return new BigDecimal(a).add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(String a, double b) {
//        return new BigDecimal(a).add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(long a, double b) {
//        return new BigDecimal(a).add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(BigDecimal a, int b) {
//        return a.add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(BigDecimal a, long b) {
//        return a.add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(BigDecimal a, double b) {
//        return a.add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal add(BigDecimal a, String b) {
//        return a.add(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
    public static BigDecimal add(Object a, Object b) {
        return new BigDecimal(a.toString()).add(new BigDecimal(b.toString())).setScale(BigMath.scale, BigMath.roundingMode);
    }
    public static BigDecimal add(Object a, Object b, int scale) {
        return new BigDecimal(a.toString()).add(new BigDecimal(b.toString())).setScale(scale, BigMath.roundingMode);
    }

    /* 各个类型的大整形的减法 */
//    public static BigDecimal subtract(int a, int b) {
//        return new BigDecimal(a).subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(long a, long b) {
//        return new BigDecimal(a).subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(double a, double b) {
//        return new BigDecimal(a).subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(String a, String b) {
//        return new BigDecimal(a).subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
//        return a.subtract(b).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(int a, long b) {
//        return new BigDecimal(a).subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(int a, double b) {
//        return new BigDecimal(a).subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(int a, String b) {
//        return new BigDecimal(a).subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(String a, long b) {
//        return new BigDecimal(a).subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(String a, double b) {
//        return new BigDecimal(a).subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(long a, double b) {
//        return new BigDecimal(a).subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(BigDecimal a, int b) {
//        return a.subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(BigDecimal a, long b) {
//        return a.subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(BigDecimal a, double b) {
//        return a.subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal subtract(BigDecimal a, String b) {
//        return a.subtract(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
    public static BigDecimal subtract(Object a, Object b) {
        return new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString())).setScale(BigMath.scale, BigMath.roundingMode);
    }
    public static BigDecimal subtract(Object a, Object b, int scale) {
        return new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString())).setScale(scale, BigMath.roundingMode);
    }

    /* 各个类型的大整形的乘法 */
//    public static BigDecimal multiply(int a, int b) {
//        return new BigDecimal(a).multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(long a, long b) {
//        return new BigDecimal(a).multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(double a, double b) {
//        return new BigDecimal(a).multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(String a, String b) {
//        return new BigDecimal(a).multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
//        return a.multiply(b).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(int a, long b) {
//        return new BigDecimal(a).multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(int a, double b) {
//        return new BigDecimal(a).multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(int a, String b) {
//        return new BigDecimal(a).multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(String a, long b) {
//        return new BigDecimal(a).multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(String a, double b) {
//        return new BigDecimal(a).multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(long a, double b) {
//        return new BigDecimal(a).multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(BigDecimal a, int b) {
//        return a.multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(BigDecimal a, long b) {
//        return a.multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(BigDecimal a, double b) {
//        return a.multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal multiply(BigDecimal a, String b) {
//        return a.multiply(new BigDecimal(b)).setScale(BigMath.scale, BigMath.roundingMode);
//    }
    public static BigDecimal multiply(Object a, Object b) {
        return new BigDecimal(a.toString()).multiply(new BigDecimal(b.toString())).setScale(BigMath.scale, BigMath.roundingMode);
    }
    public static BigDecimal multiply(Object a, Object b, int scale) {
        return new BigDecimal(a.toString()).multiply(new BigDecimal(b.toString())).setScale(scale, BigMath.roundingMode);
    }

    /* 各个类型的大整形的除法 */
//    public static BigDecimal divide(int a, int b) {
//        return new BigDecimal(a).divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(long a, long b) {
//        return new BigDecimal(a).divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(double a, double b) {
//        return new BigDecimal(a).divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(String a, String b) {
//        return new BigDecimal(a).divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
//        return a.divide(b, BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(int a, long b) {
//        return new BigDecimal(a).divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(int a, double b) {
//        return new BigDecimal(a).divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(int a, String b) {
//        return new BigDecimal(a).divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(String a, long b) {
//        return new BigDecimal(a).divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(String a, double b) {
//        return new BigDecimal(a).divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(long a, double b) {
//        return new BigDecimal(a).divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(BigDecimal a, int b) {
//        return a.divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(BigDecimal a, long b) {
//        return a.divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(BigDecimal a, double b) {
//        return a.divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
//    public static BigDecimal divide(BigDecimal a, String b) {
//        return a.divide(new BigDecimal(b), BigMath.scale, BigMath.roundingMode);
//    }
    public static BigDecimal divide(Object a, Object b) {
        return new BigDecimal(a.toString()).divide(new BigDecimal(b.toString()), BigMath.scale, BigMath.roundingMode);
    }
    public static BigDecimal divide(Object a, Object b, int scale) {
        return new BigDecimal(a.toString()).divide(new BigDecimal(b.toString()), scale, BigMath.roundingMode);
    }

    /* 比较类型的 */
//    public static int compareTo(int a, int b) {
//        return new BigDecimal(a).compareTo(new BigDecimal(b));
//    }
//    public static int compareTo(long a, long b) {
//        return new BigDecimal(a).compareTo(new BigDecimal(b));
//    }
//    public static int compareTo(double a, double b) {
//        return new BigDecimal(a).compareTo(new BigDecimal(b));
//    }
//    public static int compareTo(String a, String b) {
//        return new BigDecimal(a).compareTo(new BigDecimal(b));
//    }
//    public static int compareTo(BigDecimal a, BigDecimal b) {
//        return a.compareTo(b);
//    }
    public static int compareTo(Object a, Object b) {
        return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
    }
}
