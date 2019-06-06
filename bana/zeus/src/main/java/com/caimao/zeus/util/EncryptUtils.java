/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author 任培伟
 * @version 1.0
 */
@SuppressWarnings("restriction")
public class EncryptUtils {
    private static Logger logger = LoggerFactory.getLogger(EncryptUtils.class);

    private static final String DEFAULT_ENCODING_NAME = "UTF-8";

    private static final int DEFAULT_RSA_KEY_SIZE = 1024;

    public static SecretKeyFactory getSecretKeyFactory(String algorithm) {
        SecretKeyFactory secretKeyFactory = null;

        try {
            secretKeyFactory = SecretKeyFactory.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            logger.error("初始化keyGenerator实例时出错,无" + algorithm + "所对应的加密算法!", e);
        }
        return secretKeyFactory;
    }

    public static SecretKey getDesKey(String cipherKey) {
        SecretKeyFactory keyFactory = getSecretKeyFactory("DES");

        DESKeySpec desKeySpec = null;
        try {
            desKeySpec = new DESKeySpec(cipherKey.getBytes());
        } catch (InvalidKeyException e) {
            logger.error("新建desKeySpec时出错!", e);
        }

        SecretKey secretKey = null;
        if (desKeySpec != null) {
            try {
                secretKey = keyFactory.generateSecret(desKeySpec);
            } catch (InvalidKeySpecException e) {
                logger.error("产生secretKey对象实例时出错!", e);
            }
        }

        return secretKey;
    }

    public static Cipher getCipher(String transformation) {
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance(transformation);
        } catch (NoSuchAlgorithmException e) {
            logger.error("初始化cipher实例时出错,无" + transformation + "所对应的加密算法!", e);
        } catch (NoSuchPaddingException e) {
            logger.error("初始化cipher实例时出错,无" + transformation + "所对应的填充机制!", e);
        }
        return cipher;
    }

    public static byte[] getCipherFinalBytes(byte[] cipherBytes, Cipher cipher) {
        byte[] cipherFinalBytes = null;

        try {
            cipherFinalBytes = cipher.doFinal(cipherBytes);
        } catch (IllegalBlockSizeException e) {
            logger.error("利用cipher得到字节数组时出错，输出数据块大小异常!", e);
        } catch (BadPaddingException e) {
            logger.error("利用cipher得到字节数组时由于错误的填充机制出错!", e);
        }
        return cipherFinalBytes;
    }

    public static byte[] getDESEncryptBytes(byte[] cipherBytes, String cipherKey) {
        Cipher cipher = getCipher("DES");
        SecureRandom secureRandom = new SecureRandom();

        try {
            cipher.init(Cipher.ENCRYPT_MODE, getDesKey(cipherKey), secureRandom);
        } catch (InvalidKeyException e) {
            logger.error("在初始化cipher实例时，提供的" + cipherKey + "非法!", e);
        }

        return getCipherFinalBytes(cipherBytes, cipher);
    }

    public static byte[] getDESDecryptBytes(byte[] encodedCipherBytes,
                                            String cipherKey) {
        Cipher cipher = getCipher("DES");
        SecureRandom secureRandom = new SecureRandom();

        try {
            cipher.init(Cipher.DECRYPT_MODE, getDesKey(cipherKey), secureRandom);
        } catch (InvalidKeyException e) {
            logger.error("在初始化cipher实例时，提供的" + cipherKey + "非法!", e);
        }

        return getCipherFinalBytes(encodedCipherBytes, cipher);
    }

    public static String getBase64EncodeStr(byte[] encryptBytes) {
        return Base64.encodeBase64String(encryptBytes);
//        BASE64Encoder base64Encoder = new BASE64Encoder();
//        return base64Encoder.encode(encryptBytes);
    }

    public static String getDESEncryptStr(String cipherStr, String cipherKey,
                                          String encodingName) {
        String desEncryptStr = StringUtils.EMPTY;

        byte[] encryptBytes = null;
        try {
            encryptBytes = getDESEncryptBytes(cipherStr.getBytes(encodingName), cipherKey);
        } catch (UnsupportedEncodingException e) {
            logger.error("将明文字符串用DES加密为字节数组时发生IO异常,不支持" + encodingName + "编码!", e);
        }

        if (encryptBytes != null) {
            desEncryptStr = getBase64EncodeStr(encryptBytes);
        }

        return desEncryptStr;
    }

    public static String getDESEncryptStr(String cipherStr, String cipherKey) {
        return getDESEncryptStr(cipherStr, cipherKey, DEFAULT_ENCODING_NAME);
    }

    public static byte[] getBase64DecodeBytes(String cipherStr) {
        byte[] base64DecodeBytes = null;
        return Base64.decodeBase64(cipherStr);
//        BASE64Decoder base64Decoder = new BASE64Decoder();
//        try {
//            base64DecodeBytes = base64Decoder.decodeBuffer(cipherStr);
//        } catch (IOException e) {
//            logger.error("将" + cipherStr + "用base64解码为字节数组时发生IO异常!", e);
//        }
//        return base64DecodeBytes;
    }

    public static String getDESDecryptStr(String encodedCipherStr, String cipherKey, String encodingName) {
        String desDecryptStr = StringUtils.EMPTY;

        byte[] decryptBytes = getDESDecryptBytes(getBase64DecodeBytes(encodedCipherStr), cipherKey);

        if (decryptBytes != null) {
            try {
                desDecryptStr = new String(decryptBytes, encodingName);
            } catch (UnsupportedEncodingException e) {
                logger.error("将密文字符串转化为明文时出错,不支持" + encodingName + "编码的字符串格式!", e);
            }
        }
        return desDecryptStr;
    }

    public static String getDESDecryptStr(String encodedCipherStr, String cipherKey) {
        return getDESDecryptStr(encodedCipherStr, cipherKey, DEFAULT_ENCODING_NAME);
    }

    public static KeyFactory getKeyFactory(String algorithm) {
        KeyFactory keyFactory = null;

        try {
            keyFactory = KeyFactory.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            logger.error("初始化keyFactory实例时出错,无" + algorithm + "所对应的加密算法!", e);
        }
        return keyFactory;
    }

    public static PublicKey getRSAPublicKey(String cipherStr) {
        KeyFactory keyFactory = getKeyFactory("RSA");
        PublicKey publicKey = null;

        byte[] cipherBytes = getBase64DecodeBytes(cipherStr);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(cipherBytes);

        try {
            publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            if (logger.isErrorEnabled()) {
                logger.error("在产生RSA公共密钥时，提供的keySpec非法!", e);
            }
        }
        return publicKey;
    }

    public static PrivateKey getRSAPrivateKey(String cipherStr) {
        KeyFactory keyFactory = getKeyFactory("RSA");
        PrivateKey privateKey = null;

        byte[] cipherBytes = getBase64DecodeBytes(cipherStr);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(cipherBytes);

        try {
            privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            logger.error("在产生RSA私钥时，提供的keySpec非法!", e);
        }
        return privateKey;
    }

    public static String getRSAKeyStr(Key key) {
        byte[] keyBytes = key.getEncoded();
        return getBase64EncodeStr(keyBytes);
    }

    public static KeyPairGenerator getKeyPairGenerator(int keysize) {
        KeyPairGenerator keyPairGenerator = null;

        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            logger.error("初始化keyPairGenerator实例时出错,无所对应的加密算法!", e);
        }

        if (keyPairGenerator != null) {
            keyPairGenerator.initialize(keysize);
        }
        return keyPairGenerator;
    }

    public static KeyPair getRSAKeyPair(int keysize) {
        KeyPairGenerator keyPairGenerator = getKeyPairGenerator(keysize);
        return keyPairGenerator.generateKeyPair();
    }

    public static KeyPair getRSAKeyPair() {
        return getRSAKeyPair(DEFAULT_RSA_KEY_SIZE);
    }

    public static byte[] getRSAEncryptBytes(String cipherStr,
                                            String encodingName) {
        byte[] rsaEncryptBytes = null;
        Cipher cipher = getCipher("RSA");

        PublicKey publicKey = getRSAPublicKey(cipherStr);

        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        } catch (InvalidKeyException e) {
            logger.error("在初始化cipher实例时，提供的" + publicKey + "非法!", e);
        }

        try {
            rsaEncryptBytes = getCipherFinalBytes(
                    cipherStr.getBytes(encodingName), cipher);
        } catch (UnsupportedEncodingException e) {
            logger.error("将密文字符串转化为明文时出错,不支持" + encodingName + "编码的字符串格式!", e);
        }
        return rsaEncryptBytes;
    }

    public static byte[] getRSAEncryptBytes(String cipherStr) {
        return getRSAEncryptBytes(cipherStr, DEFAULT_ENCODING_NAME);
    }

    public static String getRSAEncrypt(String cipherStr, String encodingName) {
        return new String(getRSAEncryptBytes(cipherStr, encodingName));
    }

    public static String getRSAEncrypt(String cipherStr) {
        return getRSAEncrypt(cipherStr, DEFAULT_ENCODING_NAME);
    }

    public static byte[] getRSADecryptBytes(String encodedCipherStr,
                                            String encodingName) {
        byte[] rsaDecryptBytes = null;
        Cipher cipher = getCipher("RSA");

        PrivateKey privateKey = getRSAPrivateKey(encodedCipherStr);

        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (InvalidKeyException e) {
            logger.error("在初始化cipher实例时，提供的" + privateKey + "非法!", e);
        }

        try {
            rsaDecryptBytes = getCipherFinalBytes(encodedCipherStr.getBytes(encodingName), cipher);
        } catch (UnsupportedEncodingException e) {
            logger.error("将密文字符串转化为明文时出错,不支持" + encodingName + "编码的字符串格式!", e);
        }
        return rsaDecryptBytes;
    }

    public static byte[] getRSADecryptBytes(String encodedCipherStr) {
        return getRSADecryptBytes(encodedCipherStr, DEFAULT_ENCODING_NAME);
    }

    public static String getRSADecrypt(String encodedCipherStr, String encodingName) {
        return new String(getRSADecryptBytes(encodedCipherStr, encodingName));
    }

    public static String getRSADecrypt(String encodedCipherStr) {
        return getRSADecrypt(encodedCipherStr, DEFAULT_ENCODING_NAME);
    }

    public static String md5Encode(String origin) {
        return DigestUtils.md5Hex(origin);
    }

    public static String encodePassword(String password) {
        return md5Encode(password + Constants.USER_PASSWORD_KEY);
    }

}