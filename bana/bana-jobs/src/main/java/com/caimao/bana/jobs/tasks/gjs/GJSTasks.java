package com.caimao.bana.jobs.tasks.gjs;

import com.caimao.gjs.api.service.IAccountService;
import com.caimao.gjs.api.service.ITradeJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * GJS定时任务
 */
@Component
public class GJSTasks {
    private static final Logger logger = LoggerFactory.getLogger(GJSTasks.class);

    @Resource
    private IAccountService accountService;
    @Resource
    private ITradeJobService tradeJobService;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void updateAccountSignNJS() throws Exception {
        logger.info("开始南交所贵金属更新开户信息");
        accountService.doUpdateAccountSignNJS();
        logger.info("结束南交所贵金属更新开户信息");
    }

    //@Scheduled(cron = "0 0/1 * * * ?")
    public void updateAccountSignSJS() throws Exception {
        logger.info("开始上金所贵金属更新开户信息");
        accountService.doUpdateAccountSignSJS();
        logger.info("结束上金所贵金属更新开户信息");
    }

    @Scheduled(cron = "0 0 7 * * ?")
    public void updateGoods() throws Exception {
        logger.info("开始更新贵金属商品列表");
        tradeJobService.updateGoods();
        logger.info("结束更新贵金属商品列表");
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void updateTraderId() throws Exception {
        logger.info("开始贵金属更新用户交易员编号对应关系");
        tradeJobService.updateTraderId();
        logger.info("结束贵金属更新用户交易员编号对应关系");
    }

    @Scheduled(cron = "0 0 9,10,11,12 * * ?")
    public void parseNJSEntrustHistory() throws Exception {
        logger.info("开始更新南交所历史委托数据");
        tradeJobService.parseNJSHistory("wt", "");
        logger.info("结束更新南交所历史委托数据");
    }

    @Scheduled(cron = "0 0 9,10,11,12 * * ?")
    public void parseNJSMatchHistory() throws Exception {
        logger.info("开始更新南交所历史成交数据");
        tradeJobService.parseNJSHistory("cj", "");
        logger.info("结束更新南交所历史成交数据");
    }

    @Scheduled(cron = "0 0 9,10,11,12 * * ?")
    public void parseNJSTransferHistory() throws Exception {
        logger.info("开始更新南交所历史出入金数据");
        tradeJobService.parseNJSHistory("cr", "");
        logger.info("结束更新南交所历史出入金数据");
    }

    @Scheduled(cron = "0 0 20 * * ?")
    public void parseSJSEntrustHistory() throws Exception{
        logger.info("开始更新上金所历史委托数据");
        tradeJobService.parseSJSHistory("wt");
        logger.info("结束更新上金所历史委托数据");
    }

    @Scheduled(cron = "0 0 20 * * ?")
    public void parseSJSMatchHistory() throws Exception {
        logger.info("开始更新上金所历史成交数据");
        tradeJobService.parseSJSHistory("cj");
        logger.info("结束更新上金所历史成交数据");
    }

    @Scheduled(cron = "0 0 20 * * ?")
    public void parseSJSTransferHistory() throws Exception {
        logger.info("开始更新上金所历史出入金数据");
        tradeJobService.parseSJSHistory("cr");
        logger.info("结束更新上金所历史出入金数据");
    }

    @Scheduled(cron = "0 30 9 * * ?")
    public void sendNJSRiskTPL1() throws Exception {
        logger.info("开始发送南交所风险提示");
        tradeJobService.sendRiskUser(1);
        logger.info("结束发送南交所风险提示");
    }

    @Scheduled(cron = "0 30 14 * * ?")
    public void sendNJSRiskTPL2() throws Exception {
        logger.info("开始发送南交所风险提示");
        tradeJobService.sendRiskUser(2);
        logger.info("结束发送南交所风险提示");
    }

    @Scheduled(cron = "0 15 15 * * ?")
    public void sendNJSRiskTPL3() throws Exception {
        logger.info("开始发送南交所风险提示");
        tradeJobService.sendRiskUser(3);
        logger.info("结束发送南交所风险提示");
    }
}
