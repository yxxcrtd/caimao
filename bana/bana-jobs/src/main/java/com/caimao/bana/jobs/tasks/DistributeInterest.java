package com.caimao.bana.jobs.tasks;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.caimao.bana.api.service.IP2PJobService;

/**
 * 发息脚本，从计息开始时间算起，支付利息周期结束后，进行发息
 * 扫描p2p_invest_record有效期内收益中的的记录，给用户发息。如果到期的话，同时返还本金，修改状态为4已还款
 * @author yanjg
 * 2015年6月8日
 */
@Component
public class DistributeInterest {
    @Resource
    private IP2PJobService p2pJobService;
    private static final Logger logger = LoggerFactory.getLogger(DistributeInterest.class);

    /**
     * 扫描p2p_invest_record有效期内的满标记录，给用户发息
     * 每6小时执行一次
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0/6 * * ?")
    public void doDistributeInterest() throws Exception {
        logger.info("开始自动提交借贷申请job。。。");
        p2pJobService.doDistributeInterest();
        logger.info("自动提交借贷申请job结束");
    }
}
