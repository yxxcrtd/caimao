package com.caimao.bana.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class EmailUtils {
    private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);
    private MailSender mailSender;
    private SimpleMailMessage simpleMailMessage;

    /**
     * sendMail
     * @param subject 邮件主题
     * @param content 邮件主题内容
     * @param to 收件人Email地址
     */
    public void sendMail(String subject, String content, String to) throws Exception{
        try{
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setText(content);
            mailSender.send(simpleMailMessage);
        }catch(Exception e){
            logger.error("邮件发送失败{}", e);
            throw new Exception("邮件发送失败");
        }
    }

    //Spring 依赖注入
    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }

    //Spring 依赖注入
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
}
