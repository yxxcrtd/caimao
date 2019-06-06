/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.captcha.controller;

import com.caimao.zeus.captcha.service.CaptchaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @author 任培伟
 * @version 1.0
 */
@Controller
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    @RequestMapping(value = "/img/captcha.jpg", method = RequestMethod.GET)
    public ModelAndView generateCaptchaImage() {
        captchaService.generateCaptchaImage();
        return null;
    }
}