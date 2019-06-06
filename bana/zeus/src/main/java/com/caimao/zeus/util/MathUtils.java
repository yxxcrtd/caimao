/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.util;

/**
 * @author 任培伟
 * @version 1.0
 */
public class MathUtils {
    public static int half(int num) {
        if (num % 2 == 0) {
            return num >> 1;
        }
        return (num - 1) >> 1;
    }

    public static int ceil(int devide, int devider) {
        if (devide % devider == 0) {
            return devide / devider;
        }
        return devide / devider + 1;
    }

    public static int mod(int devide, int devider) {
        return devide % devider;
    }
}