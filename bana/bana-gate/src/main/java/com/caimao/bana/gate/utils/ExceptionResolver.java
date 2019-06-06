/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.gate.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 任培伟
 * @version 1.0.0
 */
@ControllerAdvice
public class ExceptionResolver {

    private static Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);

    @Resource
    private MessageSource messageSource;

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Object handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
}
