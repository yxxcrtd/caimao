package com.caimao.bana.jobs.tasks;

import com.caimao.bana.api.service.IOtherDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 每天执行一遍，修复月融资不收利息的问题
 * Created by WangXu on 2015/7/14.
 */
@Component
public class ProdLoanInterestFix {
    private static final Logger logger = LoggerFactory.getLogger(ProdLoanInterestFix.class);
    @Resource
    private IOtherDataService otherDataService;

    /**
     * 修复那些没有下次结息日期的融资合约
     * 执行时间：每天下午三点半进行修复
     * @throws Exception
     */
    @Scheduled(cron = "0 30 15 * * ?")
    public void doCommitLoanApply() throws Exception {
        logger.info("开始修复月融资下一次结息时间的问题。。。");
        this.otherDataService.prodLoanInterestFix();
        logger.info("结束修复月融资下一次结息时间的问题");
    }

}
