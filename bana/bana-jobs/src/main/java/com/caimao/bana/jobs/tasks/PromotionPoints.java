/*
*PromotionPoints.java
*Created on 2015/5/23 11:49
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.jobs.tasks;

import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.service.TpzAccruedInterestBillService;
import com.caimao.bana.jobs.utils.PromotionPointsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Administrator
 * @version 1.0.1
 */
@Component
public class PromotionPoints {

    private static final Logger logger = LoggerFactory.getLogger(PromotionPoints.class);

    private static final SimpleDateFormat LOGGER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private TpzAccruedInterestBillService tpzAccruedInterestBillService;

    @Resource
    private IUserService userService;

    @Resource
    private PromotionPointsUtils promotionPointsUtils;

    /**
     * 每天两点钟执行该脚本
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void addPoints() {
        try {
            promotionPointsUtils.addScore(new Date());
        } catch (Exception e) {
            logger.error("返佣脚本运行出现错误, 请检查. 错误信息:{}", e);
        }
    }

}
