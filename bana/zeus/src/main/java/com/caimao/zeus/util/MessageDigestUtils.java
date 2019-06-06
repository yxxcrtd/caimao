/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 任培伟
 * @version 1.0
 */
public class MessageDigestUtils {
    private static Logger logger = LoggerFactory.getLogger(MessageDigestUtils.class);

    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    public static MessageDigest getMessageDigestInstance(String digestName) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance(digestName);
        } catch (NoSuchAlgorithmException e) {
            logger.error("根据" + digestName + "无法得到MessageDigest实例!", e);
        }
        return messageDigest;
    }

    public static MessageDigest update(String digestName, String digest) {
        MessageDigest messageDigest = getMessageDigestInstance(digestName);
        messageDigest.update(digest.getBytes());
        return messageDigest;
    }

    public static MessageDigest update(String digestName, String digest, int offset, int len) {
        MessageDigest messageDigest = getMessageDigestInstance(digestName);
        messageDigest.update(digest.getBytes(), offset, len);
        return messageDigest;
    }

    public static byte[] digist(String digestName, String digest) {
        MessageDigest messageDigest = update(digestName, digest);
        return messageDigest.digest();
    }

    public static String getMessageDigestStr(String digestName, String digest) {
        MessageDigest messageDigest = update(digestName, digest);
        return messageDigest.toString();
    }

    public static String toHexString(byte[] bytes) {
        int length = bytes.length;
        char[] hexs = new char[length << 1];

        for (int i = 0, j = 0; i < length; i++) {
            hexs[j++] = DIGITS[(0xF0 & bytes[i]) >>> 4];
            hexs[j++] = DIGITS[0x0F & bytes[i]];
        }
        return new String(hexs);
    }
}