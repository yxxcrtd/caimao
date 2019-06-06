package com.caimao.bana.server.service.getui;

import com.caimao.bana.api.entity.getui.GetuiPushMessageEntity;
import com.caimao.bana.api.service.getui.IGetuiJobService;
import com.caimao.bana.server.dao.getui.GetuiPushMessageDAO;
import com.caimao.bana.server.utils.ApplicationContextUtils;
import com.caimao.bana.server.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 个推任务接口实现
 * ** 废弃不用了
 * Created by Administrator on 2015/11/4.
 */
@Service("getuiJobService")
public class GetuiJobServiceImpl implements IGetuiJobService {

    private static final Logger logger = LoggerFactory.getLogger(GetuiJobServiceImpl.class);

    private static final Integer GETUI_PUSH_COUNT = Integer.valueOf(Constants.getMessage("GETUI_PUSH_COUNT"));

    @Resource
    private GetuiPushMessageDAO getuiPushMessageDAO;

    private ExecutorService pool;

    public void start() {
        try {
            logger.info("创建进程池");
            this.pool = Executors.newFixedThreadPool(GETUI_PUSH_COUNT);
            logger.info("开启进程，循环执行发送命令");
            new Thread(){
                @Override
                public void run() {
                    try {
                        logger.info("进程开启，Start");
                        send();
                    } catch (Exception e) {
                        logger.error("发送消息异常，End {}", e);
                    }
                }
            }.start();
        } catch (Exception e) {
            logger.error("个推消息启动服务异常 {}", e);
        }
    }

    /**
     * 发送
     */
    @Override
    public void send() throws Exception {
        while (true) {
            // 获取所有未发送的推送信息，获取多少条
            List<GetuiPushMessageEntity> msgList = this.getuiPushMessageDAO.queryNoSendList(GETUI_PUSH_COUNT);
            if (msgList != null) {
                for (GetuiPushMessageEntity msg : msgList) {
                    try {
                        logger.info("插入消息队列，发送消息 {} 开始", msg.getId());
                        // 开一个进程去执行
                        GetuiSendRunnable runnable = (GetuiSendRunnable)ApplicationContextUtils.getBean("getuiSendRunnable");
                        runnable.setPushMessageEntity(msg);
                        this.pool.execute(runnable);
                    } catch (Exception e) {
                        logger.error(" 个推发送消息失败 {}", e);
                    }
                }
            }
            Thread.sleep(2000);
        }
    }
}
