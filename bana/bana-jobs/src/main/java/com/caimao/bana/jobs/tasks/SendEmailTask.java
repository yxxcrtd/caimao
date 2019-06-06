package com.caimao.bana.jobs.tasks;

import com.caimao.bana.api.service.IEmailService;
import com.caimao.bana.api.service.IRiskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送邮件
 */
@Component
public class SendEmailTask {

    @Resource
    private IEmailService emailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailTask.class);

    /**
     * 发送邮件
     * 每分钟执行
     * @throws Exception
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void doSendRiskSms() throws Exception {
        try {
            this.emailService.sendEmailTask();
        } catch (Exception e) {
            LOGGER.error("用户邮件发送失败，{}", e);
        }
    }
}
