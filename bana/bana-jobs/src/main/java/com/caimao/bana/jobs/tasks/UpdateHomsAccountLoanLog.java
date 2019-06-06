package com.caimao.bana.jobs.tasks;

import com.caimao.bana.api.service.IZeusStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * p2p自动展期
 */
@Component
public class UpdateHomsAccountLoanLog {
    @Resource
    private IZeusStatisticsService zeusStatisticsService;
    private static final Logger logger = LoggerFactory.getLogger(ZeusUserBalanceDaily.class);

    @Scheduled(cron = "0 0/5 * * * ?")
    public void autoExtTargetTask() throws Exception {
        logger.info("开始更新homs账户借款log job");
        zeusStatisticsService.updateHomsAccountLoanLog();
        logger.info("结束更新homs账户借款log");
    }
}
