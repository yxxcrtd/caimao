package com.caimao.bana.jobs.tasks;

import com.caimao.bana.api.service.IP2PJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * p2p自动展期
 */
@Component
public class P2PUpdateRepaymentTarget {

    @Resource
    private IP2PJobService p2pJobService;
    private static final Logger logger = LoggerFactory.getLogger(P2PUpdateRepaymentTarget.class);

    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateRepaymentTarget() throws Exception {
        logger.info("开始p2p更新提前还款的标的job");
        p2pJobService.updateRepaymentTarget();
        logger.info("结束p2p更新提前还款的标的job");
    }
}
