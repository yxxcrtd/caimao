/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author 任培伟
 * @version 1.0
 */
public class SessionAware {
    private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<HttpServletRequest>();

    private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<HttpServletResponse>();

    public static ThreadLocal<HttpServletRequest> getRequestLocal() {
        return requestLocal;
    }

    public static void setRequestLocal(ThreadLocal<HttpServletRequest> requestLocal) {
        SessionAware.requestLocal = requestLocal;
    }

    public static ThreadLocal<HttpServletResponse> getResponseLocal() {
        return responseLocal;
    }

    public static void setResponseLocal(ThreadLocal<HttpServletResponse> responseLocal) {
        SessionAware.responseLocal = responseLocal;
    }

    public static HttpServletRequest getRequest() {
        return requestLocal.get();
    }

    public static void setRequest(HttpServletRequest request) {
        requestLocal.set(request);
    }

    public static HttpServletResponse getResponse() {
        return responseLocal.get();
    }

    public static void setResponse(HttpServletResponse response) {
        responseLocal.set(response);
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }
}