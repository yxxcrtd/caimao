package com.caimao.bana.server.service.deposit;

import com.caimao.bana.api.entity.*;
import com.caimao.bana.api.enums.*;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IHeepayRecharge;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.depositDao.TpzChargeOrderDao;
import com.caimao.bana.server.dao.depositDao.TpzPayChannelDao;
import com.caimao.bana.server.utils.AccountManager;
import com.caimao.bana.server.utils.ChannelUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * 汇付宝充值相关接口方法
 * Created by WangXu on 2015/4/22.
 */
@Service("heepayRecharge")
public class HeepayRechargeImpl implements IHeepayRecharge {

    private static final Logger logger = LoggerFactory.getLogger(HeepayRechargeImpl.class);

    @Autowired
    private TpzPayChannelDao tpzPayChannelDao;
    @Autowired
    private TpzChargeOrderDao tpzChargeOrderDao;
    @Autowired
    private TpzAccountDao tpzAccountDao;
    @Autowired
    private ChannelUtil channelUtil;
    @Autowired
    private AccountManager accountManager;


    /**
     * 汇付宝实际充值到账方法
     * 先锁定用户订单，在锁定用户资产
     *
     * @param heepayRequestEntity 汇付宝回调返回的数据
     * @return 返回充值完成后的订单信息
     * @throws Exception
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public TpzChargeOrderEntity doCharge(HeepayRequestEntity heepayRequestEntity) throws Exception {
        this.logger.info("汇付宝充值到账执行操作 {}", ToStringBuilder.reflectionToString(heepayRequestEntity));
        Date date = new Date();
        Long orderNo = heepayRequestEntity.getOrderNo();
        // 锁定用户订单记录
        TpzChargeOrderEntity tpzChargeOrderEntity = this.tpzChargeOrderDao.getChargeOrderByOrderNo(orderNo);
        if (tpzChargeOrderEntity == null) {
            this.logger.error("充值订单 {} 不存在", heepayRequestEntity.getOrderNo());
            throw new CustomerException("充值订单不存在!", 830409);
        }
        // 锁定用户资产记录
        TpzAccountEntity tpzAccountEntity = this.tpzAccountDao.getAccountByUserId(tpzChargeOrderEntity.getUserId());
        if (tpzAccountEntity == null) {
            this.logger.error("充值的用户 {} 不存在", heepayRequestEntity.getUserId());
            throw new CustomerException("用户不存在!", 830409);
        }

        // 判断用户充值订单是否已经完成
        if (EOrderStatus.SUCCESS.getCode().equalsIgnoreCase(tpzChargeOrderEntity.getOrderStatus())) {
            // 打印LOG
            this.logger.error("订单 {} 状态已经为充值成功，汇付宝返回的状态为 {}", heepayRequestEntity.getOrderNo(), heepayRequestEntity.getPayResult());
            return tpzChargeOrderEntity;
        }
        tpzChargeOrderEntity.setBankResultCode(heepayRequestEntity.getBankResultCode());
        tpzChargeOrderEntity.setBankResultCodeNote(heepayRequestEntity.getBankResultCode());
        tpzChargeOrderEntity.setCheckStatus(ECheckStatus.UNCHECK.getCode());
        tpzChargeOrderEntity.setWorkDate(heepayRequestEntity.getWorkDate());
        tpzChargeOrderEntity.setCurrencyType(ECurrency.CNY.getCode());
        // 以实际支付的情况为准
        if (heepayRequestEntity.getOrderAmount() != null) {
            tpzChargeOrderEntity.setOrderAmount(heepayRequestEntity.getOrderAmount());
        }

        if (heepayRequestEntity.getPayResult().equals(EOrderStatus.SUCCESS.getCode())) {
            tpzChargeOrderEntity.setOrderStatus(EOrderStatus.SUCCESS.getCode());
            tpzChargeOrderEntity.setBankSuccessDatetime(date);
        } else {
            tpzChargeOrderEntity.setOrderStatus(EOrderStatus.FAIL.getCode());
        }
        tpzChargeOrderEntity.setRemark(heepayRequestEntity.getRemark());
        tpzChargeOrderEntity.setUpdateDatetime(new Date());
        // 设置那些时间们
        tpzChargeOrderEntity.setPayResultDatetime(date);


        this.logger.info("充值更新订单信息 {}", ToStringBuilder.reflectionToString(tpzChargeOrderEntity));
        this.tpzChargeOrderDao.update(tpzChargeOrderEntity);

        if (heepayRequestEntity.getPayResult().equals(EOrderStatus.SUCCESS.getCode())) {
            // 给用户加钱
            this.logger.info("给用户 {} 加钱 {}", tpzChargeOrderEntity.getUserId(), tpzChargeOrderEntity.getOrderAmount());
            this.accountManager.doUpdateAvaiAmount(heepayRequestEntity.getOrderNo().toString(), tpzChargeOrderEntity.getPzAccountId(), tpzChargeOrderEntity.getOrderAmount(), EAccountBizType.CHARGE.getCode(), ESeqFlag.COME.getCode());
        }
        return tpzChargeOrderEntity;
    }

    /**
     * 检查汇付宝返回的信息是否正确
     * @param heepayNoticeEntity 汇付宝回调返回的数据
     * @return 检查通过返回true，反之返回false
     * @throws Exception
     */
    public Boolean checkeHeepayNoticeSign(HeepayNoticeEntity heepayNoticeEntity) throws Exception {
        this.logger.info("验证汇付宝返回的信息 {}", ToStringBuilder.reflectionToString(heepayNoticeEntity));
        // 这里使用汇付宝充值
        Long channelId = 3L;
        TpzPayChannelEntity tpzPayChannelEntity = this.tpzPayChannelDao.getPayById(channelId);
        String signStr = String.format("result=%s&agent_id=%s&jnet_bill_no=%s&agent_bill_id=%s&pay_type=%s&pay_amt=%s&remark=%s&key=%s",
                heepayNoticeEntity.getResult(),
                heepayNoticeEntity.getAgentId(),
                heepayNoticeEntity.getJnetBillNo(),
                heepayNoticeEntity.getAgentBillNo(),
                heepayNoticeEntity.getPayType(),
                this.channelUtil.longToString(heepayNoticeEntity.getPayAmt()),
                heepayNoticeEntity.getRemark(),
                tpzPayChannelEntity.getSignKey()
        );
        String signMD5Str = this.channelUtil.MD5(signStr).toLowerCase();
        this.logger.info("自己生成的SignMD5 {}", signMD5Str);
        if (signMD5Str.equals(heepayNoticeEntity.getSign())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询汇付宝充值结果，并执行充值操作
     *
     * @param orderNo 汇付宝充值订单ID
     * @return
     */
    public boolean checkHeepayPayResult(Long orderNo) throws Exception {
        // 调用汇付宝的查询接口进行查询
        TpzChargeOrderEntity tpzChargeOrderEntity = this.tpzChargeOrderDao.getChargeOrderByOrderNo(orderNo);
        if (tpzChargeOrderEntity == null) {
            throw new CustomerException("充值订单不存在", 88888);
        }
        if (EOrderStatus.SUCCESS.getCode().equalsIgnoreCase(tpzChargeOrderEntity.getOrderStatus())) {
            // 打印LOG
            this.logger.error("订单 {} 状态已经为充值成功", orderNo);
            throw new CustomerException("充值订单已经充值成功", 88888);
        }
        TpzPayChannelEntity tpzPayChannelEntity = this.tpzPayChannelDao.getPayById(3L);
        Map<String, Object> heepayReturnMap = this.channelUtil.checkHeepayPayResult(tpzPayChannelEntity, tpzChargeOrderEntity);
        if (heepayReturnMap == null) {
            throw new CustomerException("请求汇付宝查询支付状态失败", 88888);
        }

        HeepayRequestEntity heepayRequestEntity = new HeepayRequestEntity();
        if (heepayReturnMap.get("result").equals("1")) {
            // 充值成功了
            heepayRequestEntity.setOrderAmount(this.channelUtil.stringToLong(heepayReturnMap.get("pay_amt").toString()));
            heepayRequestEntity.setPayResult(EOrderStatus.SUCCESS.getCode());
        } else {
            // 充值失败了
            heepayRequestEntity.setPayResult(EOrderStatus.FAIL.getCode());
        }
        heepayRequestEntity.setUserId(tpzChargeOrderEntity.getUserId());
        heepayRequestEntity.setChannelId(3L);

        heepayRequestEntity.setOrderNo(orderNo);
        heepayRequestEntity.setWorkDate("");
        heepayRequestEntity.setBankResultCode(heepayReturnMap.get("result").toString());
        heepayRequestEntity.setRemark("");
        // 执行充值操作
        this.doCharge(heepayRequestEntity);

        return true;
    }


}
