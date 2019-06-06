package com.caimao.hq.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtils {
    public static boolean isMobile(String str){
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^1[34578]\\d{9}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
}