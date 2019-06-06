package com.caimao.bana.server.service.getui;

import com.caimao.bana.api.entity.getui.GetuiPushMessageEntity;
import com.caimao.bana.api.enums.getui.EGetuiActionType;
import com.caimao.bana.api.enums.getui.EGetuiDeviceType;
import com.caimao.bana.api.enums.getui.EGetuiSendStatus;
import com.caimao.bana.server.dao.getui.GetuiPushMessageDAO;
import com.caimao.bana.server.utils.ApplicationContextUtils;
import com.caimao.bana.server.utils.Constants;
import com.caimao.bana.server.utils.redis.JedisUtil;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.Message;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Objects;

/**
 * 个推发送的运行任务
 * Created by Administrator on 2015/11/24.
 */
//@Component
public class GetuiSendRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GetuiSendRunnable.class);

    private static final String GETUI_URL = Constants.getMessage("GETUI_URL");
    private static final String GETUI_APPID = Constants.getMessage("GETUI_APPID");
    private static final String GETUI_APPKEY = Constants.getMessage("GETUI_APPKEY");
    private static final String GETUI_APPSECRET = Constants.getMessage("GETUI_APPSECRET");
    private static final String GETUI_MASTERSECRET = Constants.getMessage("GETUI_MASTERSECRET");
    private static final String GETUI_DT = Constants.getMessage("GETUI_DT");
    private static final Integer GETUI_OFFLINE_TIMEOUT = Integer.valueOf(Constants.getMessage("GETUI_OFFLINE_TIMEOUT"));
    private static final Integer GETUI_PUSH_COUNT = Integer.valueOf(Constants.getMessage("GETUI_PUSH_COUNT"));

    //@Resource
    private GetuiPushMessageDAO getuiPushMessageDAO;
    private JedisUtil jedisUtil;

    private GetuiPushMessageEntity pushMessageEntity;

    private Long msgId;
    private String redisKey;

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public void setPushMessageEntity(GetuiPushMessageEntity pushMessageEntity) {
        this.pushMessageEntity = pushMessageEntity;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void run() {
        getuiPushMessageDAO = ApplicationContextUtils.getBean("getuiPushMessageDAO");
        jedisUtil = ApplicationContextUtils.getBean("jedisUtil");
        try {
            System.out.println("开始个推消息发送 : " + this.msgId);
            // 锁住这条记录
            this.pushMessageEntity = this.getuiPushMessageDAO.selectById(this.msgId);
            // 如果这个不为 1 ，那就退出
            if (!Objects.equals(this.pushMessageEntity.getStatus(), EGetuiSendStatus.STATUS_INIT.getValue())) {
                return;
            }
            // 先更新这个的状态为 正在发送
            this.updateMsgStatus(this.pushMessageEntity.getId(), EGetuiSendStatus.STATUS_SEND.getValue());
            this.getuiSend(this.pushMessageEntity);
        } catch (Exception e) {
            logger.error("个推推送消息失败 {}", e);
            this.updateMsgStatus(this.pushMessageEntity.getId(), EGetuiSendStatus.STATUS_INIT.getValue());
            this.reSetRunnable();
        }
    }

    private void reSetRunnable() {
        Jedis jedis = null;
        try {
            jedis = this.jedisUtil.getJedis();
            jedis.lpush(this.redisKey, this.msgId.toString());
        } catch (Exception e1) {
            logger.error("个推发送失败，再次添加redis失败 {}", e1);
        } finally {
            this.jedisUtil.returnJedis(jedis);
        }
    }

    /**
     * 个推进行消息推送
     * @param entity
     * @return
     */
    private Boolean getuiSend(GetuiPushMessageEntity entity) {
        if (entity.getTryTimes() >=3) {
            // 重试超过三次，直接置为失败
            this.updateMsgStatus(entity.getId(), EGetuiSendStatus.STATUS_FAIL.getValue());
            return false;
        }
        // 创建推送对象
        IGtPush push = new IGtPush(GETUI_URL, GETUI_APPKEY, GETUI_MASTERSECRET);
        // 创建发送的对象
        Target target = new Target();
        target.setAppId(GETUI_APPID);
        target.setClientId(entity.getRecvId());

        ITemplate template = null;
        // 根据发送的消息类型，构建消息模板
        if (entity.getActionType().equals(EGetuiActionType.TYPE_OPENAPP.getValue())) {
            // 打开应用的消息模板
            template = this.createNotificationTemplate(entity);
        } else if (entity.getActionType().equals(EGetuiActionType.TYPE_OPENURL.getValue())) {
            // 打开URL的消息模板
            template = this.createLinkTemplate(entity);
        } else if (entity.getActionType().equals(EGetuiActionType.TYPE_TRANS.getValue())) {
            // 透传消息模板
            template = this.createTransmissionTemplate(entity);
        } else {
            // 消息类型错误，置为失败
            this.updateMsgStatus(entity.getId(), EGetuiSendStatus.STATUS_FAIL.getValue());
            return false;
        }
        // TODO 所有的iOS都走透传消息模板
        if (entity.getDeviceType().equals(EGetuiDeviceType.IPHONE.getValue())) {
            // 透传消息模板
            template = this.createAPNTemplate(entity);
        }

        IPushResult ret = null;

        if(entity.getRecvIdType() == 2){
            AppMessage message = new AppMessage();
            message.setData(template);
            message.setOffline(true);
            message.setOfflineExpireTime(GETUI_OFFLINE_TIMEOUT);
            try{
                ret = push.pushMessageToApp(message);
            }catch(Exception e){
                logger.error(" 个推推送APP消息异常消息 {}", e);
            }
        }else{
            SingleMessage message = new SingleMessage();
            message.setData(template);
            message.setOffline(true);
            message.setOfflineExpireTime(GETUI_OFFLINE_TIMEOUT);
            try {
                if (entity.getDeviceType().equals(EGetuiDeviceType.IPHONE.getValue())) {
                    ret = push.pushAPNMessageToSingle(GETUI_APPID, entity.getDeviceToken(), message);
                } else {
                    ret = push.pushMessageToSingle(message, target);
                }
            } catch(Exception e) {
                logger.error(" 个推推送单条消息异常消息 {}", e);
            }
        }

        if (ret != null){
            System.out.println("个推返回的信息 ： " + ret.getResponse().toString());
            // 成功的
            if (ret.getResponse().get("result").equals("ok")) {
                this.updateMsgStatus(entity.getId(), EGetuiSendStatus.STATUS_OK.getValue());
                return true;
            }
            logger.error("个推推送失败，返回错误码： {}", ret.getResponse().get("result"));
        } else {
            logger.error("个推推送无信息返回");
        }
        // 失败的， 增加重试次数，置为未发送状态
        this.updateMsgStatus(entity.getId(), EGetuiSendStatus.STATUS_INIT.getValue());
        // 重试次数加1
        GetuiPushMessageEntity updateEntity = new GetuiPushMessageEntity();
        updateEntity.setId(entity.getId());
        updateEntity.setTryTimes(entity.getTryTimes() + 1);
        this.getuiPushMessageDAO.update(updateEntity);
        this.reSetRunnable();
        return false;


//        // iPhone
//        APNTemplate iPhoneTemp = new APNTemplate();
//        APNPayload apnpayload = new APNPayload();
//        apnpayload.setBadge(1);
//        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
//        alertMsg.setTitle(entity.getTitle());
//        alertMsg.setBody(entity.getContent());
//        apnpayload.setAlertMsg(alertMsg);
//
//        iPhoneTemp.setAPNInfo(apnpayload);
//        SingleMessage sm = new SingleMessage();
//        sm.setData(iPhoneTemp);
//        sm.setOffline(true);
//        sm.setOfflineExpireTime(GETUI_OFFLINE_TIMEOUT);
//
//        try {
//            ret = push.pushAPNMessageToSingle(GETUI_APPID, entity.getDeviceToken(), sm);
//        } catch(RequestException e) {
//            logger.error(" 个推推送单条消息异常消息 {}", e);
//            ret = push.pushAPNMessageToSingle(GETUI_APPID, entity.getDeviceToken(), sm);
//        }

    }


    /**
     * 更新消息的状态方法
     * @param id    消息ID
     * @param status    消息状态
     */
    private void updateMsgStatus(Long id, String status) {
        GetuiPushMessageEntity updateEntity = new GetuiPushMessageEntity();
        updateEntity.setId(id);
        updateEntity.setStatus(status);
        this.getuiPushMessageDAO.update(updateEntity);
    }

    /**
     * 创建一个打开应用的消息模板
     * @param entity
     * @return
     */
    private NotificationTemplate createNotificationTemplate(GetuiPushMessageEntity entity) {
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(GETUI_APPID);
        template.setAppkey(GETUI_APPKEY);
        template.setTitle(entity.getTitle());
        template.setText(entity.getContent());
        template.setLogo("push.png");   //TODO 需要确认logo的名称是否一样
        template.setTransmissionType(1);
        if (entity.getDeviceType().equals(EGetuiDeviceType.IPHONE.getValue())) {
            APNPayload payload = new APNPayload();
            payload.setBadge(1);
            payload.setCategory(entity.getTitle());

            APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
            alertMsg.setTitle(entity.getTitle());
            alertMsg.setBody(entity.getContent());
            alertMsg.setTitleLocKey(entity.getTitle());
            alertMsg.setActionLocKey(entity.getContent());
            alertMsg.setLocKey("");
        }
        return template;
    }

    /**
     * 创建打开网页的消息推送模板
     * @param entity
     * @return
     */
    private LinkTemplate createLinkTemplate(GetuiPushMessageEntity entity) {
        LinkTemplate template = new LinkTemplate();
        template.setAppId(GETUI_APPID);
        template.setAppkey(GETUI_APPKEY);
        template.setTitle(entity.getTitle());
        template.setText(entity.getContent());
        template.setLogo("push.png");   //TODO 需要确认logo的名称是否一样
        template.setUrl(entity.getUrl());
        template.setLogoUrl("");
        if (entity.getDeviceType().equals(EGetuiDeviceType.IPHONE.getValue())) {
            APNPayload payload = new APNPayload();
            payload.setBadge(1);
            payload.setCategory(entity.getTitle());

            APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
            alertMsg.setTitle(entity.getTitle());
            alertMsg.setBody(entity.getContent());
            alertMsg.setTitleLocKey(entity.getTitle());
            alertMsg.setActionLocKey(entity.getContent());
            alertMsg.setLocKey("");
        }
        return template;
    }

    /**
     * 创建透传模板
     * @param entity
     * @return
     */
    private TransmissionTemplate createTransmissionTemplate(GetuiPushMessageEntity entity) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(GETUI_APPID);
        template.setAppkey(GETUI_APPKEY);
        template.setTransmissionContent(entity.getContent());
        template.setTransmissionType(2);
        if (entity.getDeviceType().equals(EGetuiDeviceType.IPHONE.getValue())) {
            APNPayload payload = new APNPayload();
            payload.setSound("");
            payload.setBadge(2);
            payload.setCategory(entity.getTitle());

            APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
            alertMsg.setTitle(entity.getTitle());
            alertMsg.setBody(entity.getContent());
            alertMsg.setTitleLocKey(entity.getTitle());
            alertMsg.setActionLocKey(entity.getContent());
            alertMsg.setLocKey("");

            template.setAPNInfo(payload);
        }
        return template;
    }

    private APNTemplate createAPNTemplate(GetuiPushMessageEntity entity) {
        APNTemplate template = new APNTemplate();
        APNPayload apnpayload = new APNPayload();
        APNPayload.SimpleAlertMsg alertMsg = new APNPayload.SimpleAlertMsg(entity.getContent());
        apnpayload.setAlertMsg(alertMsg);
        apnpayload.setBadge(1);
        apnpayload.setContentAvailable(1);
        apnpayload.setCategory("ACTIONABLE");
        apnpayload.setSound("");
        template.setAPNInfo(apnpayload);
        template.setAppId(GETUI_APPID);
        template.setAppkey(GETUI_APPKEY);

        return template;
    }


}
