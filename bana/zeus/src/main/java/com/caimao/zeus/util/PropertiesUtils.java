/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author 任培伟
 * @version 1.0
 */

public class PropertiesUtils {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    private static final String DEFAULT_URL = "/conf/system.properties";

    private static Properties read(String url) {
        Properties properties = new Properties();
        InputStream inputStream = PropertiesUtils.class.getResourceAsStream(url);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("读取" + url + ".properties配置文件时出错!", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("关闭" + url + ".properties文件输入流过程中出错!", e);
            }
        }
        return properties;
    }

    public static String get(String url, String key) {
        Properties properties = read(url);
        return get(properties, key);
    }

    public static String get(Properties properties, String key) {
        String value = StringUtils.EMPTY;
        if (properties.containsKey(key)) {
            value = properties.getProperty(key);
        }
        return value;
    }

    public static String get(String key) {
        return get(DEFAULT_URL, key);
    }

    public static Set<Object> getKeySet(String url) {
        Properties properties = read(url);
        return getKeySet(properties);
    }

    public static Set<Object> getKeySet(Properties properties) {
        return properties.keySet();
    }

    public static void updateProperty(String url, String key, String value) {
        File file = new File(PropertiesUtils.class.getClassLoader()
                .getResource(".").getPath(), url);
        InputStream inputStream = null;
        PrintStream printStream = null;

        try {
            inputStream = new FileInputStream(file);
            Properties properties = new Properties();

            properties.load(inputStream);
            properties.setProperty(key, value);

            printStream = new PrintStream(file);
            properties.store(printStream, "update key:" + key + ",value:"
                    + value);
        } catch (FileNotFoundException e) {
            logger.error("写入" + url + ".properties配置文件时出错!", e);
        } catch (IOException e) {
            logger.error("写入" + url + ".properties配置文件时出错!", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("关闭" + url + ".properties文件输入流过程中出错!", e);
                }
            }
            if (printStream != null) {
                printStream.close();
            }
        }
    }

    public static void updateProperty(String key, String value) {
        updateProperty(DEFAULT_URL, key, value);
    }

    public static <K, V> void batchUpdate(String url, Map<K, V> map) {
        File file = new File(PropertiesUtils.class.getClassLoader().getResource(".").getPath(), url);
        InputStream inputStream = null;
        PrintStream printStream = null;

        try {
            inputStream = new FileInputStream(file);
            Properties properties = new Properties();

            properties.load(inputStream);
            for (K k : map.keySet()) {
                properties.setProperty(k.toString(), map.get(k).toString());
            }

            printStream = new PrintStream(file);
            properties.store(printStream, "batch update!");
        } catch (FileNotFoundException e) {
            logger.error("写入" + url + ".properties配置文件时出错!", e);
        } catch (IOException e) {
            logger.error("写入" + url + ".properties配置文件时出错!", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("关闭" + url + ".properties文件输入流过程中出错!", e);
                }
            }
            if (printStream != null) {
                printStream.close();
            }
        }
    }

    public static <K, V> void batchUpdate(Map<K, V> map) {
        batchUpdate(DEFAULT_URL, map);
    }

    public static void saveProperty(String notExistsFileURL, String key,
                                    String value) {
        File file = new File(PropertiesUtils.class.getClassLoader().getResource(".").getPath(), notExistsFileURL);
        PrintStream printStream = null;

        try {
            Properties properties = new Properties();
            properties.setProperty(key, value);

            printStream = new PrintStream(file);
            properties
                    .store(printStream, "save key:" + key + ",value:" + value);
        } catch (FileNotFoundException e) {
            logger.error("写入" + notExistsFileURL + ".properties配置文件时出错!", e);
        } catch (IOException e) {
            logger.error("写入" + notExistsFileURL + ".properties配置文件时出错!", e);
        } finally {
            if (printStream != null) {
                printStream.close();
            }
        }
    }

    public static <K, V> void batchSave(String notExistsFileURL, Map<K, V> map) {
        File file = new File(PropertiesUtils.class.getClassLoader().getResource(".").getPath(), notExistsFileURL);
        PrintStream printStream = null;

        try {
            Properties properties = new Properties();
            for (K k : map.keySet()) {
                properties.setProperty(k.toString(), map.get(k).toString());
            }

            printStream = new PrintStream(file);
            properties.store(printStream, "batch update!");
        } catch (FileNotFoundException e) {
            logger.error("写入" + notExistsFileURL + ".properties配置文件时出错!", e);
        } catch (IOException e) {
            logger.error("写入" + notExistsFileURL + ".properties配置文件时出错!", e);
        } finally {
            if (printStream != null) {
                printStream.close();
            }
        }
    }
}