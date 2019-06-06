/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.captcha.service.impl;

import com.caimao.zeus.captcha.service.CaptchaService;
import com.caimao.zeus.util.SessionAware;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author 任培伟
 * @version 1.0
 */
@Service("captchaService")
public class CaptchaServiceImpl implements CaptchaService {

    private static Logger logger = LoggerFactory.getLogger(CaptchaServiceImpl.class);

    @Resource
    private DefaultKaptcha kaptchaProducer;

    public void generateCaptchaImage() {
        HttpServletResponse response = SessionAware.getResponse();
        HttpSession session = SessionAware.getSession();

        setResponse(response);

        BufferedImage bufferedImage = generateCaptchaText(session);

        printCaptchaImage(response, bufferedImage);
    }

    private void setResponse(HttpServletResponse response) {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
    }

    private BufferedImage generateCaptchaText(HttpSession session) {
        String captchaText = kaptchaProducer.createText();
        BufferedImage bi = kaptchaProducer.createImage(captchaText);
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, captchaText);
        return bi;
    }

    private void printCaptchaImage(HttpServletResponse response,
                                   BufferedImage bufferedImage) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            ImageIO.write(bufferedImage, "jpg", out);
            out.flush();
        } catch (IOException e) {
            logger.error("输出验证码的过程中发生IO异常!", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error("关闭验证码的输出流时发生IO异常!", e);
            }
        }
    }
}