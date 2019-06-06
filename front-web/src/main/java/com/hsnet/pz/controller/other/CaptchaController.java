package com.hsnet.pz.controller.other;

import com.hsnet.frontcore.exception.BizException;
import com.hsnet.pz.ao.core.ControllerContext;
import com.octo.captcha.service.image.ImageCaptchaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class CaptchaController {

    @Resource(name = "imageCaptchaService")
    private ImageCaptchaService imageCaptchaService;

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void ImageCaptcha(HttpServletRequest request,
                             HttpServletResponse response, Model model) throws ServletException,
            IOException {

        byte[] captchaChallengeAsJpeg = null;
        // the output stream to render the captcha image as jpeg into
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // get the session id that will identify the generated captcha.
            // the same id must be used to validate the response, the session id
            // is a good candidate!
            String captchaId = request.getSession().getId();
            // call the ImageCaptchaService getChallenge method
            BufferedImage challenge = imageCaptchaService
                    .getImageChallengeForID(captchaId, request.getLocale());

            ImageIO.write(challenge, "jpeg", jpegOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        // flush it in the response
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @RequestMapping(value = "/captcha/check", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkCaptcha(String captcha) {
        String sessionId = ControllerContext.getRequest().getSession().getId();
        boolean flag = imageCaptchaService.validateResponseForID(sessionId,
                captcha);
        if (!flag) { // 验证码正确
            throw new BizException("83099901", "验证码不正确");
        }
        return true;
    }
}
