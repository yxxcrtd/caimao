package com.caimao.bana.api.service;

/**
 * 邮件服务
 */
public interface IEmailService {
    /**
     * 发送邮件
     * @param subject 标题
     * @param content 内容
     * @param to 收件人
     * @throws Exception
     */
    public void doSendEmail(String subject, String content, String to)throws Exception;


    /**
     * 发送邮件任务
     * @throws Exception
     */
    public void sendEmailTask() throws Exception;
}