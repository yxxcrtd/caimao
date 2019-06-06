package com.caimao.bana.jobs.tasks;

import com.caimao.bana.api.service.IP2PJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * p2p自动满标
 */
@Component
public class P2PAutoFullTarget {

    @Resource
    private IP2PJobService p2pJobService;
    private static final Logger logger = LoggerFactory.getLogger(P2PAutoFullTarget.class);

    @Scheduled(cron = "0 0/10 * * * ?")
    public void autoExtTargetTask() throws Exception {
        logger.info("开始p2p自动满标job");
        p2pJobService.autoFullTarget();
        logger.info("结束p2p自动满标job");
    }
}
