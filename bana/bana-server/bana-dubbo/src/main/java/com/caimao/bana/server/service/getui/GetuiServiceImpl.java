package com.caimao.bana.server.service.getui;

import com.caimao.bana.api.entity.getui.GetuiPushMessageEntity;
import com.caimao.bana.api.entity.getui.GetuiUserIdMapEntity;
import com.caimao.bana.api.entity.req.getui.FGetuiPushMessageReq;
import com.caimao.bana.api.service.getui.IGetuiService;
import com.caimao.bana.server.dao.getui.GetuiPushMessageDAO;
import com.caimao.bana.server.dao.getui.GetuiUserIdMapDAO;
import com.caimao.bana.server.utils.ApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 个推服务接口实现
 * Created by Administrator on 2015/11/4.
 */
@Service("getuiService")
public class GetuiServiceImpl implements IGetuiService {

    private static Logger logger = LoggerFactory.getLogger(GetuiServiceImpl.class);

    @Resource
    private GetuiUserIdMapDAO getuiUserIdMapDAO;

    @Resource
    private GetuiPushMessageDAO getuiPushMessageDAO;

    @Resource
    private GetuiSendQueue getuiSendQueue;

    private ThreadPoolExecutor poolExecutor;

    // 初始化完成
    public void init() {
        poolExecutor = new ThreadPoolExecutor(20, 40, 0L, TimeUnit.SECONDS, getuiSendQueue);
    }


    /**
     * 绑定客户端与用户之间的关联关系
     * @param entity
     * @return
     */
    @Override
    public Boolean bindCidAndUserId(GetuiUserIdMapEntity entity) {
        // 查询这个设备id是否存在
        GetuiUserIdMapEntity userIdMapEntity = this.getuiUserIdMapDAO.selectByCid(entity.getCid());
        if (userIdMapEntity != null) {
            if (Objects.equals(entity.getUserId(), userIdMapEntity.getUserId())) {
                return true;
            } else {
                // 如果与原来的用户不一致，就删除掉
                this.getuiUserIdMapDAO.deleteById(userIdMapEntity.getId());
            }
        }

        userIdMapEntity = this.getuiUserIdMapDAO.selectByUserIdAndType(entity.getUserId(), entity.getDeviceType());
        if (userIdMapEntity != null) {
            // 存在进行更新操作
            userIdMapEntity.setUserId(entity.getUserId());
            userIdMapEntity.setCid(entity.getCid());
            userIdMapEntity.setDeviceToken(entity.getDeviceToken());
            userIdMapEntity.setDeviceType(entity.getDeviceType());
            userIdMapEntity.setTimeUpdate(new Date());
            this.getuiUserIdMapDAO.update(userIdMapEntity);
        } else {
            // 不存在进行添加
            entity.setTimeCreate(new Date());
            entity.setTimeUpdate(new Date());
            this.getuiUserIdMapDAO.insert(entity);
        }
        return true;
    }

    /**
     * 解绑客户端与用户之间的关连关系
     * @param entity
     * @return
     */
    @Override
    public Boolean unbindCidAndUserId(GetuiUserIdMapEntity entity) {
        // 查询是否有这个设备ID，存在查看用户是否一致，一致的话，进行删除
        GetuiUserIdMapEntity userIdMapEntity = this.getuiUserIdMapDAO.selectByCid(entity.getCid());
        if (userIdMapEntity != null) {
            if (userIdMapEntity.getUserId().equals(entity.getUserId())) {
                this.getuiUserIdMapDAO.deleteById(userIdMapEntity.getId());
            }
        }
        return true;
    }

    /**
     * 添加单个用户的推送消息
     * @param req
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Boolean pushMessageToSingle(FGetuiPushMessageReq req) {
        // 根据推送的USERID，获取设备信息，可能会查出多个设备，循环进行发送处理
        List<GetuiUserIdMapEntity> userIdMapEntityList = this.getuiUserIdMapDAO.selectByUserid(req.getUserId());
        if (userIdMapEntityList != null) {
            for (GetuiUserIdMapEntity userMapEntity: userIdMapEntityList) {
                GetuiPushMessageEntity messageEntity = new GetuiPushMessageEntity();
                messageEntity.setUserId(userMapEntity.getUserId());
                messageEntity.setRecvId(userMapEntity.getCid());
                messageEntity.setDeviceToken(userMapEntity.getDeviceToken());
                messageEntity.setRecvIdType(messageEntity.getRecvIdType() != null?messageEntity.getRecvIdType():1);
                messageEntity.setDeviceType(userMapEntity.getDeviceType());
                messageEntity.setActionType(req.getActionType());
                messageEntity.setSource(req.getSource());
                messageEntity.setTitle(req.getTitle());
                messageEntity.setContent(req.getContent());
                messageEntity.setUrl(req.getUrl());
                messageEntity.setStatus("1");
                messageEntity.setTryTimes(0);
                messageEntity.setTimePush(new Date());
                messageEntity.setTimeCreate(new Date());
                this.getuiPushMessageDAO.insert(messageEntity);

                try {
                    //GetuiSendRunnable runnable = ApplicationContextUtils.getBean("getuiSendRunnable");
                    GetuiSendRunnable runnable = new GetuiSendRunnable();
                    runnable.setMsgId(messageEntity.getId());
                    runnable.setRedisKey(getuiSendQueue.getRedisKey());
                    poolExecutor.execute(runnable);
                } catch (Exception e) {
                    logger.error("getuiService pushMessageToSingle {}", e);
                }
            }
        }
        return true;
    }

    /**
     * 添加单个用户的推送消息
     * @param req
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Boolean pushMessageToApp(FGetuiPushMessageReq req) {
        GetuiPushMessageEntity messageEntity = new GetuiPushMessageEntity();
        messageEntity.setRecvIdType(2);
        messageEntity.setActionType(req.getActionType());
        messageEntity.setSource(req.getSource());
        messageEntity.setTitle(req.getTitle());
        messageEntity.setContent(req.getContent());
        messageEntity.setUrl(req.getUrl());
        messageEntity.setStatus("1");
        messageEntity.setTryTimes(0);
        messageEntity.setTimePush(new Date());
        messageEntity.setTimeCreate(new Date());
        this.getuiPushMessageDAO.insert(messageEntity);

        try {
            //GetuiSendRunnable runnable = ApplicationContextUtils.getBean("getuiSendRunnable");
            GetuiSendRunnable runnable = new GetuiSendRunnable();
            runnable.setMsgId(messageEntity.getId());
            runnable.setRedisKey(getuiSendQueue.getRedisKey());
            poolExecutor.execute(runnable);
        } catch (Exception e) {
            logger.error("getuiService pushMessageToSingle {}", e);
        }
        return true;
    }
}
