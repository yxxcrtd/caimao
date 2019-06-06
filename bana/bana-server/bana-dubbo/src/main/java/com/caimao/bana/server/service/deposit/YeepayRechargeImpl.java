package com.caimao.bana.server.service.deposit;

import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.TpzChargeOrderEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.enums.*;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IYeepayRecharge;
import com.caimao.bana.server.dao.accountDao.TpzAccountDao;
import com.caimao.bana.server.dao.depositDao.TpzChargeOrderDao;
import com.caimao.bana.server.dao.depositDao.TpzPayChannelDao;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.utils.AccountManager;
import com.caimao.bana.server.utils.YeepayChargeUtils;
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
 * 易宝支付接口服务
 * Created by WangXu on 2015/5/21.
 */
@Service("yeepayRecharge")
public class YeepayRechargeImpl implements IYeepayRecharge {

    private static final Logger logger = LoggerFactory.getLogger(YeepayRechargeImpl.class);

    @Autowired
    private TpzUserDao userDao;
    @Autowired
    private TpzAccountDao accountDao;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private TpzChargeOrderDao chargeOrderDao;
    @Autowired
    private TpzPayChannelDao payChannelDao;
    @Autowired
    private YeepayChargeUtils yeepayChargeUtils;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public String doPreMobileCharge(Long userId, Long orderAmount, String userIp, String userUA) throws Exception {
        // 先查询用户是否存在
        TpzUserEntity userEntity = this.userDao.getUserById(userId);
        if (userEntity == null) {
            throw new CustomerException("用户未找到", 888888);
        }
        if (userEntity.getIsTrust().equals(EUserIsTrust.NO.getCode())) {
            throw new CustomerException("用户未完成实名认证", 888888);
        }
        // 查询用户账户是否存在
        TpzAccountEntity accountEntity = this.accountDao.getAccountByUserId(userId);
        if (accountEntity == null) {
            throw new CustomerException("用户没有创建资产账户", 888888);
        }
        // 写死，使用易宝支付渠道
        Long payChannelId = 2L;

        // 生成充值订单
        Date curDate = new Date();
        TpzChargeOrderEntity chargeOrderEntity = new TpzChargeOrderEntity();
        chargeOrderEntity.setPzAccountId(accountEntity.getPzAccountId());    // 设置融资账户的id
        chargeOrderEntity.setOrderAmount(orderAmount);  // 设置充值的数量
        chargeOrderEntity.setOrderName("财猫-手机充值");  // 设置订单名称
        chargeOrderEntity.setOrderAbstract("财猫网站手机充值 " + (orderAmount / 100) + "元");  // 设置订单描述
        chargeOrderEntity.setUserId(userId); // 用户ID
        chargeOrderEntity.setCurrencyType(ECurrency.CNY.getCode());  // 设置货币类型
        chargeOrderEntity.setOrderStatus(EOrderStatus.ING.getCode());    // 设置订单状态
        chargeOrderEntity.setCheckStatus(ECheckStatus.UNCHECK.getCode());    // 设置对账的状态
        chargeOrderEntity.setCreateDatetime(curDate);
        chargeOrderEntity.setUpdateDatetime(curDate);
        chargeOrderEntity.setChannelId(payChannelId);   // 设置渠道ID
        chargeOrderEntity.setPayType("12");   // 设置支付方式
        chargeOrderEntity.setPaySubmitDatetime(curDate);
        chargeOrderEntity.setChannelServiceCharge(0L);   // 这个是充值手续费

        this.logger.info("易宝手机充值写入预充值信息：{}", chargeOrderEntity);
        this.chargeOrderDao.save(chargeOrderEntity);

        // 生成返回的支付链接
        return this.yeepayChargeUtils.createMobileChargeUrl(userEntity, chargeOrderEntity, userIp, userUA);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public boolean doMobileCharge(String data, String encryptkey) throws Exception {
        Map<String, Object> yeepayRes = this.yeepayChargeUtils.decryptYeepayReturnData(data, encryptkey);
        if (yeepayRes == null) {
            throw new CustomerException("易宝返回值解析失败", 888888);
        }
        return executeYeepayCharge(yeepayRes);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public boolean checkMobilePayRes(Long orderNo) throws Exception {
        TpzChargeOrderEntity chargeOrderEntity = this.chargeOrderDao.getChargeOrderByOrderNo(orderNo);
        if (chargeOrderEntity == null) {
            this.logger.error("充值订单 {} 不存在", orderNo);
            throw new CustomerException("充值订单信息未找到", 888888);
        }

        // 请求易宝手机充值查询接口
        Map<String, Object> yeepayData = this.yeepayChargeUtils.queryYeepayMobilePayRes(chargeOrderEntity);
        this.logger.info("易宝查询返回解密结果：{}", yeepayData);

        return this.executeYeepayCharge(yeepayData);
    }

    /**
     * 处理易宝返回的值，进行充值操作
     * @param yeepayRes 易宝返回的值
     * @return  处理结果
     * @throws Exception
     */
    private boolean executeYeepayCharge(Map<String, Object> yeepayRes) throws Exception {
        Date curDate = new Date();
        if (yeepayRes.get("orderid") == null) {
            this.logger.error("易宝返回解密数据： {}", yeepayRes);
            throw new CustomerException("易宝返回数据不完整", 888888);
        }
        Long orderNo = Long.valueOf(yeepayRes.get("orderid").toString());
        // 查询订单信息
        TpzChargeOrderEntity chargeOrderEntity = this.chargeOrderDao.getChargeOrderByOrderNo(orderNo);
        if (chargeOrderEntity == null) {
            this.logger.error("充值订单 {} 不存在", orderNo);
            throw new CustomerException("充值订单信息未找到", 888888);
        }
        // 锁定用户资产记录
        TpzAccountEntity accountEntity = this.accountDao.getAccountByUserId(chargeOrderEntity.getUserId());
        if (accountEntity == null) {
            this.logger.error("充值的用户 {} 不存在", chargeOrderEntity.getUserId());
            throw new CustomerException("用户不存在!", 830409);
        }
        // 判断用户充值订单是否已经完成
        if (EOrderStatus.SUCCESS.getCode().equalsIgnoreCase(chargeOrderEntity.getOrderStatus())) {
            // 打印LOG
            this.logger.error("订单 {} 状态已经为充值成功，易宝返回的状态为 {}", orderNo, yeepayRes.get("status").toString());
            return true;
        }
        if (yeepayRes.get("bankcode") != null) {
            chargeOrderEntity.setBankCode(yeepayRes.get("bankcode").toString());
            chargeOrderEntity.setBankNo(yeepayRes.get("bankcode").toString());
        }
        if (yeepayRes.get("lastno") != null) {
            chargeOrderEntity.setBankCardNo(yeepayRes.get("lastno").toString());
        }
        if (yeepayRes.get("bank") != null) {
            chargeOrderEntity.setBankCardName(yeepayRes.get("bank").toString());
        }
        // 以实际支付的情况为准
        if (yeepayRes.get("amount") != null) {
            chargeOrderEntity.setOrderAmount(Long.valueOf(yeepayRes.get("amount").toString()));
        }
        chargeOrderEntity.setBankResultCode(yeepayRes.get("status").toString());
        chargeOrderEntity.setBankResultCodeNote(yeepayRes.get("status").toString());
        chargeOrderEntity.setCheckStatus(ECheckStatus.UNCHECK.getCode());
        chargeOrderEntity.setCurrencyType(ECurrency.CNY.getCode());
        // 支付状态是否成功
        if (yeepayRes.get("status").toString().equals("1")) {
            chargeOrderEntity.setOrderStatus(EOrderStatus.SUCCESS.getCode());
            chargeOrderEntity.setBankSuccessDatetime(curDate);
        } else {
            chargeOrderEntity.setOrderStatus(EOrderStatus.FAIL.getCode());
        }
        chargeOrderEntity.setPayId(yeepayRes.get("yborderid").toString());
        chargeOrderEntity.setRemark("");
        chargeOrderEntity.setUpdateDatetime(curDate);
        // 设置那些时间们
        chargeOrderEntity.setPayResultDatetime(curDate);


        this.logger.info("充值更新订单信息 {}", ToStringBuilder.reflectionToString(chargeOrderEntity));
        this.chargeOrderDao.update(chargeOrderEntity);

        if (yeepayRes.get("status").toString().equals("1")) {
            // 给用户加钱
            this.logger.info("给用户 {} 加钱 {}", chargeOrderEntity.getUserId(), chargeOrderEntity.getOrderAmount());
            this.accountManager.doUpdateAvaiAmount(String.valueOf(chargeOrderEntity.getOrderNo()), accountEntity.getPzAccountId(), chargeOrderEntity.getOrderAmount(), EAccountBizType.CHARGE.getCode(), ESeqFlag.COME.getCode());
        }
        return true;
    }


}
