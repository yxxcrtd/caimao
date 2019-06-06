/*
 * Timeloit.com Inc.
 * Copyright (c) 2012 时代凌宇物联网数据平台. All Rights Reserved
 */
package com.fmall.bana.interceptor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * controller的拦截器，用于进入controller的拦截
 *
 * @author yulianyu
 * @Id $Id: CommonRequestInterceptor.java 3684 2012-01-14 02:46:28Z yulianyu $
 */
public class ReferInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(HandlerInterceptorAdapter.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String referUrl = StringUtils.trimToEmpty(request.getHeader("Referer"));
        if (referUrl.indexOf(".caimao.com") > 0 || referUrl.indexOf("127.0.0.1") > 0) {
            return true;
        } else {
            logger.error("the refer:{} is ilegal", referUrl);
            return false;
        }
    }
}
