package com.caimao.bana.server.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by WangXu on 2015/4/23.
 */
public class MD5Util {
    public static String md5(String pswd) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            StringBuffer strbuf = new StringBuffer();

            md5.update(pswd.getBytes(), 0, pswd.length());
            byte[] digest = md5.digest();

            for (int i = 0; i < digest.length; i++) {
                strbuf.append(byte2Hex(digest[i]));
            }
            return strbuf.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private static String byte2Hex(byte b) {
        int value = (b & 0x7F) + (b < 0 ? 128 : 0);
        return new StringBuilder().append(value < 16 ? "0" : "").append(Integer.toHexString(value).toLowerCase()).toString();
    }
}