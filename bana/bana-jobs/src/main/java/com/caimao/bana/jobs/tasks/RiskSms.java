package com.caimao.bana.jobs.tasks;

import com.caimao.bana.api.service.IRiskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RiskSms {

    @Resource
    private IRiskService riskService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RiskSms.class);

    /**
     * 用户返佣信息更新
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void doSendRiskSms() throws Exception {
        try {
            riskService.doSendRiskSms();
            LOGGER.info("用户风控短信发送成功");
        } catch (Exception e) {
            LOGGER.error("用户风控短信发送失败，{}", e);
        }
    }
}
