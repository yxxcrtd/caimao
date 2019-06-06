package com.caimao.bana.jobs.tasks;

import com.caimao.bana.api.service.ybk.IYBKJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ybk行情数据自动抓取
 *
 */
@Component
public class YbkQuotationTask {

    @Resource
    private IYBKJobService ybkJobService;
    private static final Logger logger = LoggerFactory.getLogger(YbkQuotationTask.class);

    @Scheduled(cron = "0 0/1 * * * ?")
    public void quotationDataTask() throws Exception {
        logger.info("开始行情数据抓取job");
        ybkJobService.quotationData();
        logger.info("结束行情数据抓取job");
    }

    @Scheduled(cron = "0 0/60 * * * ?")
    public void articleGetTask() throws Exception {
        logger.info("开始文章公告数据抓取job");
        ybkJobService.articleData();
        logger.info("结束文章公告数据抓取job");
    }
}
