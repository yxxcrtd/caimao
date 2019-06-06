package com.caimao.bana.jobs.tasks;

import com.caimao.gjs.api.service.IArticleJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 资讯抓取
 */
@Component
public class ArticleTask {

    private static final Logger logger = LoggerFactory.getLogger(ArticleTask.class);

    @Resource
    private IArticleJobService articleJobService;

    /**
     * 每隔两分钟抓取一次
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    public void quotationDataTask() throws Exception {
        this.logger.info("开始抓取黄金头条job");
        articleJobService.catchGoldTouTiaoNews();
        this.logger.info("结束抓取黄金头条job");

        this.logger.info("开始抓取金十job");
        articleJobService.catchJin10();
        this.logger.info("结束抓取金十job");
    }

}
