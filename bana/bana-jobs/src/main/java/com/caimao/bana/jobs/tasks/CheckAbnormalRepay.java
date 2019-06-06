package com.caimao.bana.jobs.tasks;

import com.caimao.bana.api.service.IHomsAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 异常还款冻结账户脚本
 */
@Component
public class CheckAbnormalRepay {

    @Resource
    private IHomsAccountService homsAccountService;
    private static final Logger logger = LoggerFactory.getLogger(CheckAbnormalRepay.class);

    /**
     * 扫描异常还款冻结账户脚本
     * 每1分钟更新一次。
     * @throws Exception
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void doCommitLoanApply() throws Exception {
        logger.info("开始检查异常还款job。。。");
        homsAccountService.checkAbnormalRepayAccountJour();
        logger.info("检查异常还款job结束");
    }
}
