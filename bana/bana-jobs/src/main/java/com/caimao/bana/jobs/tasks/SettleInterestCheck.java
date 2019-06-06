package com.caimao.bana.jobs.tasks;

import com.caimao.bana.api.service.IOtherDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 未进行结息的检查与报警工作
 * Created by Administrator on 2015/8/28.
 */
@Component
public class SettleInterestCheck {

    @Resource
    private IOtherDataService otherDataService;

    private static final Logger logger = LoggerFactory.getLogger(SettleInterestCheck.class);

    /**
     * 每天早上9点进行未结息的检查，并进行报警
     * @throws Exception
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void check() throws Exception {
        logger.info("开始未结息的检查");
        this.otherDataService.settlePZInterestNotice();
        logger.info("结束未结息的检查");
    }
}
