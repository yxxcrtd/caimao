package com.caimao.bana.jobs.tasks;

import com.caimao.bana.api.service.IZeusStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户资产汇总日报
 */
@Component
public class ZeusUserBalanceDaily {

    @Resource
    private IZeusStatisticsService zeusStatisticsService;
    private static final Logger logger = LoggerFactory.getLogger(ZeusUserBalanceDaily.class);

    @Scheduled(cron = "0 0 0 * * ?")
    public void autoExtTargetTask() throws Exception {
        logger.info("开始用户资产汇总日报job");
        zeusStatisticsService.userBalanceDailySave();
        logger.info("结束用户资产汇总日报");
    }
}
