package com.caimao.bana.jobs.tasks;

import com.caimao.bana.api.service.InviteInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InviteInfo {

    @Resource
    private InviteInfoService inviteInfoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(InviteInfo.class);

    /**
     * 用户返佣信息更新
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void doUpdateInviteInfo() throws Exception {
        try {
            inviteInfoService.updateInviteInfoTask();
            LOGGER.info("更新用户返佣数据成功 - -");
        } catch (Exception e) {
            LOGGER.error("更新用户积分失败{}", e);
        }
    }
}
