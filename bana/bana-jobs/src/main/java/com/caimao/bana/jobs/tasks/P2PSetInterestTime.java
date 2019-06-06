package com.caimao.bana.jobs.tasks;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.caimao.bana.api.service.IP2PJobService;

/**
 * 满标后，自动提交借贷申请，只插入借贷申请，不涉及到资产操作
 * @author yanjg
 * 2015年6月8日
 */
@Component
public class P2PSetInterestTime {

    @Resource
    private IP2PJobService p2pJobService;
    private static final Logger logger = LoggerFactory.getLogger(P2PSetInterestTime.class);

    /**
     *当P2P借贷满标并审核通过时，设置p2p_invest_record的计息时间job
     * 将状态修改为收益中
     * 2分钟运行一次
     * @throws Exception
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void doCommitLoanApply() throws Exception {
        logger.info("开始设置计息时间job。。。");
        p2pJobService.doSetInterestTime();
        logger.info("设置计息时间job结束");
    }
}
