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

public class URLTool {
    public static String pingURL(String urlStr, String key, String value) {
        if (urlStr.indexOf("?") > 0) {
            if (urlStr.endsWith("?")) {
                urlStr = new StringBuffer().append(urlStr).append(key).append("=").append(value).toString();
            } else {
                urlStr = new StringBuffer().append(urlStr).append("&amp;").append(key).append("=").append(value).toString();
            }
        } else {
            urlStr = new StringBuffer().append(urlStr).append("?").append(key).append("=").append(value).toString();
        }
        return urlStr;
    }

    public static String append(String urlStr, String queryStr) {
        if (urlStr.indexOf("?") > 0) {
            urlStr = new StringBuffer().append(urlStr.substring(0, urlStr.indexOf("?"))).append(queryStr).toString();
        } else {
            urlStr = new StringBuffer().append(urlStr).append("?").append(queryStr).toString();
        }
        return urlStr;
    }
}
