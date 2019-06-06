package com.caimao.hq.core;

import com.caimao.bana.api.entity.req.getui.FGetuiPushMessageReq;
import com.caimao.bana.api.entity.req.message.FPushMsgAddReq;
import com.caimao.bana.api.enums.EPushModel;
import com.caimao.bana.api.enums.EPushType;
import com.caimao.bana.api.enums.getui.EGetuiActionType;
import com.caimao.bana.api.service.content.IMessageService;
import com.caimao.bana.api.service.getui.IGetuiService;
import com.caimao.hq.api.entity.Candle;
import com.caimao.hq.api.entity.GjsPriceAlertEntity;
import com.caimao.hq.api.entity.req.FQueryGjsPriceAlertReq;
import com.caimao.hq.api.enums.EPriceAlertType;
import com.caimao.hq.api.service.IGjsPriceAlertService;
import com.caimao.hq.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 处理商品价格提醒的子线程任务
 * Created by Administrator on 2015/11/25.
 */
public class GjsPriceAlertRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GjsPriceAlertRunnable.class);

    /**
     * 要处理的对象数据
     */
    private Candle candle;

    private IGjsPriceAlertService gjsPriceAlertService;
    private IGetuiService getuiService;
    private IMessageService messageService;

    public void setCandle(Candle candle) {
        this.candle = candle;
    }

    @Override
    public void run() {
        try {
            // Spring 注入对象
            this.gjsPriceAlertService = (IGjsPriceAlertService) SpringUtil.getBean("gjsPriceAlertService");
            this.getuiService = (IGetuiService) SpringUtil.getBean("getuiService");
            this.messageService = (IMessageService) SpringUtil.getBean("messageService");

            // 重置符合条件的那些价格提醒的触发价格
            this.gjsPriceAlertService.resetPriceAlertTriggerPrice(EPriceAlertType.DY.getValue(), String.valueOf(this.candle.getLastPx()));
            this.gjsPriceAlertService.resetPriceAlertTriggerPrice(EPriceAlertType.XY.getValue(), String.valueOf(this.candle.getLastPx()));
            this.gjsPriceAlertService.resetPriceAlertTriggerPrice(EPriceAlertType.ZF.getValue(), String.valueOf(this.candle.getPxChangeRate()));

            List<GjsPriceAlertEntity> priceAlertEntityList = new ArrayList<>();

            FQueryGjsPriceAlertReq priceAlertReq = new FQueryGjsPriceAlertReq();
            priceAlertReq.setExchange(this.candle.getExchange());
            priceAlertReq.setGoodCode(this.candle.getProdCode());
            priceAlertReq.setOn("1");

            // 先触发大于这个价格的
            priceAlertReq.setCondition(EPriceAlertType.DY.getValue());
            priceAlertReq.setPrice(String.valueOf(this.candle.getLastPx()));
            priceAlertEntityList.addAll(this.gjsPriceAlertService.queryGjsPriceAlertList(priceAlertReq));

            // 在触发小于这个价格的
            priceAlertReq.setCondition(EPriceAlertType.XY.getValue());
            priceAlertReq.setPrice(String.valueOf(this.candle.getLastPx()));
            priceAlertEntityList.addAll(this.gjsPriceAlertService.queryGjsPriceAlertList(priceAlertReq));

            // 在触发涨跌幅的
            priceAlertReq.setCondition(EPriceAlertType.ZF.getValue());
            priceAlertReq.setPrice(String.valueOf(this.candle.getPxChangeRate()));
            priceAlertEntityList.addAll(this.gjsPriceAlertService.queryGjsPriceAlertList(priceAlertReq));

            for (GjsPriceAlertEntity entity: priceAlertEntityList) {
                try {
                    this.processAlert(entity);
                } catch (Exception e) {
                    logger.error(" 发送消息 {} 异常 {}", entity.getId(), e);
                }
            }
        } catch (Exception e) {
            logger.error(" 处理指定商品的价格提醒错误 {}", e);
        }
    }

    @Transactional
    private void processAlert(GjsPriceAlertEntity entity) throws Exception {
        // 再次获取这个价格提醒的信息，进行判断
        GjsPriceAlertEntity dbEntity = this.gjsPriceAlertService.selectById(entity.getId());
        if (dbEntity.getLastSendTime() != null) {
            // 计算时间差是不是大于5分钟
            Date now = new Date();
            Long diff = now.getTime() - dbEntity.getLastSendTime().getTime();
            Long minutes = diff / 1000 / 60;
            if (minutes <= 5) {
                return;
            }
        }
        logger.info("触发器提醒的任务，进行消息提醒 {}, {}, {}, {}", entity.getId(), entity.getGoodCode(), entity.getGoodName(), entity.getUserId());

        String getuiPushMsg;
        String webMessge;
        if (entity.getCondition().equals(EPriceAlertType.DY.getValue())) {
            getuiPushMsg = String.format("%s已上涨到您设定的%.2f元,请关注", this.candle.getProdName(), entity.getPrice());
            webMessge = String.format("%s已上涨到您设定的%.2f元,请关注", this.candle.getProdName(), entity.getPrice());
        } else if (entity.getCondition().equals(EPriceAlertType.XY.getValue())) {
            getuiPushMsg = String.format("%s已下跌到您设定的%.2f元,请关注", this.candle.getProdName(), entity.getPrice());
            webMessge = String.format("%s已下跌到您设定的%.2f元,请关注", this.candle.getProdName(), entity.getPrice());
        } else if (entity.getCondition().equals(EPriceAlertType.ZF.getValue())) {
            if (this.candle.getPxChangeRate() < 0) {
                getuiPushMsg = String.format("%s已下跌到您设定的%.2f%%,请关注", this.candle.getProdName(), entity.getPrice());
                webMessge = String.format("%s已下跌到您设定的%.2f%%,请关注", this.candle.getProdName(), entity.getPrice());
            } else {
                getuiPushMsg = String.format("%s已上涨到您设定的%.2f%%,请关注", this.candle.getProdName(), entity.getPrice());
                webMessge = String.format("%s已上涨到您设定的%.2f%%,请关注", this.candle.getProdName(), entity.getPrice());
            }
        } else {
            return;
        }
        try {
            // 发送个推消息
            FGetuiPushMessageReq getuiPushMessageReq = new FGetuiPushMessageReq();
            getuiPushMessageReq.setUserId(entity.getUserId());
            getuiPushMessageReq.setActionType(EGetuiActionType.TYPE_OPENAPP.getValue());
            getuiPushMessageReq.setSource("gjs_price_alert");
            getuiPushMessageReq.setTitle("价格提醒");
            getuiPushMessageReq.setContent(getuiPushMsg);
            this.getuiService.pushMessageToSingle(getuiPushMessageReq);
        } catch (Exception e) {
            logger.error("{} 发送个推消息失败 {}", entity.getId(), e);
        }
        try {
            //站内信
            FPushMsgAddReq pushMsgAddReq = new FPushMsgAddReq();
            pushMsgAddReq.setPushModel(EPushModel.GJS.getCode());
            pushMsgAddReq.setPushType(EPushType.PRICEALERT.getCode());
            pushMsgAddReq.setPushMsgKind("1");
            pushMsgAddReq.setPushMsgTitle(webMessge);
            pushMsgAddReq.setPushMsgContent(webMessge);
            pushMsgAddReq.setPushMsgDigest(webMessge);
            pushMsgAddReq.setPushExtend("");
            pushMsgAddReq.setPushUserId(entity.getUserId().toString());
            pushMsgAddReq.setIsRead("0");
            this.messageService.addPushMsg(pushMsgAddReq);
        } catch (Exception e) {
            logger.error("{} 发送站内信失败 {}", entity.getId(), e);
        }

        // 更新这个发送状态
        GjsPriceAlertEntity updatePriceAlertEntity = new GjsPriceAlertEntity();
        updatePriceAlertEntity.setId(entity.getId());
        updatePriceAlertEntity.setLastSendTime(new Date());
        updatePriceAlertEntity.setSendNum(entity.getSendNum()+1);
        if (entity.getCondition().equals(EPriceAlertType.ZF.getValue())) {
            updatePriceAlertEntity.setTriggerPrice(new BigDecimal(this.candle.getPxChangeRate()));
        } else {
            updatePriceAlertEntity.setTriggerPrice(new BigDecimal(this.candle.getLastPx()));
        }
        this.gjsPriceAlertService.update(updatePriceAlertEntity);
    }
}
