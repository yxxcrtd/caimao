/*
*HeepayWithdraw.java
*Created on 2015/5/13 9:00
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server.service.withdraw;

import com.caimao.bana.api.entity.TpzPayChannelEntity;
import com.caimao.bana.api.entity.TpzWithdrawOrderEntity;
import com.caimao.bana.api.entity.req.F831411Req;
import com.caimao.bana.api.entity.res.F831411Res;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.enums.EOrderStatus;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.api.enums.EVerifyStatus;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IHeepayWithdraw;
import com.caimao.bana.server.dao.depositDao.TpzPayChannelDao;
import com.caimao.bana.server.dao.withdrawDao.TpzWithdrawOrderDao;
import com.caimao.bana.server.utils.AccountManager;
import com.caimao.bana.server.utils.ChannelUtil;
import com.caimao.bana.server.utils.Constants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 汇付宝提现的方法
 *
 * @author Administrator
 * @version 1.0.1
 */
@Service("heepayWithdraw")
public class HeepayWithdrawImpl implements IHeepayWithdraw {

    private static final Logger logger = LoggerFactory.getLogger(HeepayWithdrawImpl.class);

    @Autowired
    private TpzPayChannelDao payChannelDao;
    @Autowired
    private ChannelUtil channelUtil;
    @Autowired
    private TpzWithdrawOrderDao withdrawDao;
    @Autowired
    private AccountManager accountManager;



    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public boolean doWithdraw(Long orderNo) throws Exception {
        TpzWithdrawOrderEntity withdrawOrderEntity = this.withdrawDao.getWithdrawOrderByOrderNo(orderNo);
        if (withdrawOrderEntity == null) {
            throw new CustomerException("无此订单。", 8310402);
        }
        if (!EVerifyStatus.PASS.getCode().equals(withdrawOrderEntity.getVerifyStatus())) {
            throw new CustomerException("该笔订单未通过审核,不能支付。", 8310402);
        }
        if (EOrderStatus.SUCCESS.getCode().equals(withdrawOrderEntity.getOrderStatus())) {
            throw new CustomerException("该笔订单已支付成功，不能再次支付。", 8310402);
        }
        if (EOrderStatus.WAIT_SURE.getCode().equals(withdrawOrderEntity.getOrderStatus())) {
            throw new CustomerException("该笔订单等待确认,在获取最终状态前,暂不能再次支付。", 8310402);
        }
        if (EOrderStatus.CACAL.getCode().equals(withdrawOrderEntity.getOrderStatus())) {
            throw new CustomerException("该笔订单已被撤销,不能支付。", 8310402);
        }
        if (EOrderStatus.FAIL.getCode().equals(withdrawOrderEntity.getOrderStatus())) {
            throw new CustomerException("该笔订单已失败,不能支付。", 8310402);
        }

        Date curDate = new Date();
        withdrawOrderEntity.setChannelId(3L);
        withdrawOrderEntity.setOrderStatus(EOrderStatus.WAIT_SURE.getCode());
        withdrawOrderEntity.setPaySubmitDatetime(curDate);
        withdrawOrderEntity.setUpdateDatetime(curDate);
        this.withdrawDao.update(withdrawOrderEntity);

        TpzPayChannelEntity tpzPayChannelEntity = this.payChannelDao.getPayById(3L);

        boolean result = this.channelUtil.withdrawByHeepay(tpzPayChannelEntity, withdrawOrderEntity);
        if (result == false) {
            throw new CustomerException("汇付宝批付请求失败，请查看原因", 8310402);
        }
        return true;
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public boolean doWithdrawReview(LinkedHashMap<String, Object> withdrawReviewParams, String sign) throws Exception {
        Date curDate = new Date();
        this.logger.info("汇付宝批付的返回值 {} 签名 {}", withdrawReviewParams, sign);
        TpzPayChannelEntity tpzPayChannelEntity = this.payChannelDao.getPayById(3L);
        StringBuffer originalSign = new StringBuffer();
        for (String key : withdrawReviewParams.keySet()) {
            originalSign.append(key).append("=").append(withdrawReviewParams.get(key).toString()).append("&");
        }
        originalSign.append("key=").append(tpzPayChannelEntity.getSignKey());
        if (channelUtil.MD5(originalSign.toString().toLowerCase()).toLowerCase().equals(sign)) {
            List<Map<String, Object>> detailList = ChannelUtil.analysisHeepayBatchDetailData(withdrawReviewParams.get("detail_data").toString());
            for (int i = 0; i < detailList.size(); i++) {
                TpzWithdrawOrderEntity order = this.withdrawDao.getWithdrawOrderByOrderNo(Long.valueOf(detailList.get(i).get("orderNo").toString()));
                if (order == null) {
                    throw new CustomerException("提现订单未找到 " + detailList.get(i).get("orderNo").toString(), 888888);
                }
                if (!EVerifyStatus.PASS.getCode().equals(order.getVerifyStatus())) {
                    throw new CustomerException("该笔订单未通过审核,并没有支付。", 8310402);
                }
                if (EOrderStatus.SUCCESS.getCode().equals(order.getOrderStatus())) {
                    throw new CustomerException("该笔订单已支付成功。", 8310402);
                }
                if (EOrderStatus.CACAL.getCode().equals(order.getOrderStatus())) {
                    throw new CustomerException("该笔订单已被撤销,不能支付。", 8310402);
                }
                if (EOrderStatus.FAIL.getCode().equals(order.getOrderStatus())) {
                    throw new CustomerException("该笔订单已失败,不能支付。", 8310402);
                }
                if (!"S".equals(detailList.get(i).get("status"))) {
                    // 返回值不等于 S，没有提现成功呢，记录日志，不进行操作
                    this.logger.warn("订单 {} 支付结果未成功，继续等待", detailList.get(i).get("orderNo"));
                    continue;
                }
                // 提现成功到账了，进行成功的操作
                order.setChannelServiceCharge(0L);    // todo 这是渠道服务费用
                order.setPayId(detailList.get(i).get("orderNo").toString());
                order.setBankSuccessDatetime(curDate);
                order.setUpdateDatetime(curDate);
                order.setPayResultDatetime(new Date());
                order.setBankResultCode(detailList.get(i).get("status").toString());
                order.setOrderStatus(EOrderStatus.SUCCESS.getCode());
                this.payWithdrawSuccess(order);
                this.withdrawDao.update(order);
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Map<String, Object> doWithdrawQuery(Long orderNo) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        // 获取订单表中的记录
        TpzWithdrawOrderEntity withdrawOrderEntity = this.withdrawDao.getWithdrawOrderByOrderNo(orderNo);
        if (withdrawOrderEntity == null) {
            throw new CustomerException("订单记录不存在", 888888);
        }
        if (!EVerifyStatus.PASS.getCode().equals(withdrawOrderEntity.getVerifyStatus())) {
            throw new CustomerException("该笔订单未通过审核,并没有支付。", 8310402);
        }
        if (EOrderStatus.SUCCESS.getCode().equals(withdrawOrderEntity.getOrderStatus())) {
            throw new CustomerException("该笔订单已支付成功。", 8310402);
        }
        if (EOrderStatus.CACAL.getCode().equals(withdrawOrderEntity.getOrderStatus())) {
            throw new CustomerException("该笔订单已被撤销,不能支付。", 8310402);
        }
        if (EOrderStatus.FAIL.getCode().equals(withdrawOrderEntity.getOrderStatus())) {
            throw new CustomerException("该笔订单已失败,不能支付。", 8310402);
        }
        Date curDate = new Date();
        TpzPayChannelEntity payChannelEntity = this.payChannelDao.getPayById(3L);

        // 调用汇付宝批付查询接口
        List<Map<String, Object>> heepayResultList = this.channelUtil.checkHeepayBatchResult(payChannelEntity, withdrawOrderEntity);
        if (heepayResultList.size() == 0) {
            throw new CustomerException("汇付宝批付查询返回结果条数为0", 888888);
        }
        this.logger.info("汇付宝批付查询返回的接口数据：{}", heepayResultList);
        for (int i = 0; i < heepayResultList.size(); i++) {
            TpzWithdrawOrderEntity order = this.withdrawDao.getWithdrawOrderByOrderNo(Long.valueOf(heepayResultList.get(i).get("orderNo").toString()));
            if (order == null) {
                throw new CustomerException("提现订单未找到 " + heepayResultList.get(i).get("orderNo").toString(), 888888);
            }
            if (!"S".equals(heepayResultList.get(i).get("status"))) {
                if ("F".equals(heepayResultList.get(i).get("status"))) {
                    // 处理失败了
                    order.setPayId(heepayResultList.get(i).get("orderNo").toString());
                    order.setUpdateDatetime(curDate);
                    order.setPayResultDatetime(new Date());
                    order.setBankResultCode(heepayResultList.get(i).get("status").toString());
                    order.setOrderStatus(EOrderStatus.FAIL.getCode());
                    this.payWithdrawFail(order);
                    this.withdrawDao.update(order);
                    returnMap.put("result", false);
                    returnMap.put("msg", "订单支付失败，请用户重新进行提现");
                    return returnMap;
                } else {
                    // 返回值不等于 S，没有提现成功呢，记录日志，不进行操作
                    this.logger.warn("订单 {} 支付结果未成功，继续等待", heepayResultList.get(i).get("orderNo"));
                    returnMap.put("result", false);
                    returnMap.put("msg", "订单支付未到账，请继续等待");
                    return returnMap;
                }
            }
            // 提现成功到账了，进行成功的操作
            order.setChannelServiceCharge(0L);    // todo 这是渠道服务费用
            order.setPayId(heepayResultList.get(i).get("orderNo").toString());
            order.setBankSuccessDatetime(curDate);
            order.setUpdateDatetime(curDate);
            order.setPayResultDatetime(new Date());
            order.setBankResultCode(heepayResultList.get(i).get("status").toString());
            order.setOrderStatus(EOrderStatus.SUCCESS.getCode());
            this.payWithdrawSuccess(order);
            this.withdrawDao.update(order);
            returnMap.put("result", true);
            returnMap.put("msg", "订单提现到账成功");
            return returnMap;
        }
        returnMap.put("result", false);
        returnMap.put("msg", "未执行任何操作");
        return returnMap;
    }

    // 提现成功的资金操作
    private void payWithdrawSuccess(TpzWithdrawOrderEntity order) throws Exception {
        if (Constants.WITHDRAW_FROZEN) {
            this.accountManager.dofreezeAmount(String.valueOf(order.getOrderNo()), order.getPzAccountId(), order.getOrderAmount(), ESeqFlag.GO.getCode(), EAccountBizType.WITHDRAW.getCode());
        }
        this.accountManager.doUpdateAvaiAmount(String.valueOf(order.getOrderNo()), order.getPzAccountId(), order.getOrderAmount(), EAccountBizType.WITHDRAW.getCode(), ESeqFlag.GO.getCode());
    }

    // 提现失败的资金操作
    private void payWithdrawFail(TpzWithdrawOrderEntity order) throws Exception {
        if (Constants.WITHDRAW_FROZEN) {
            this.accountManager.dofreezeAmount(String.valueOf(order.getOrderNo()), order.getPzAccountId(), order.getOrderAmount(), ESeqFlag.GO.getCode(), EAccountBizType.WITHDRAW.getCode());
        }
    }

    @Override
    public List<F831411Res> queryWithdrawOrders(F831411Req f831411Req) throws Exception {
        if (StringUtils.isNotEmpty(f831411Req.getCreateDatetimeBegin())) {
            f831411Req.setCreateDatetimeBegin(f831411Req.getCreateDatetimeBegin().concat(" 00:00:00"));
        }
        if (StringUtils.isNotEmpty(f831411Req.getCreateDatetimeEnd())) {
            f831411Req.setCreateDatetimeEnd(f831411Req.getCreateDatetimeEnd().concat(" 23:59:59"));
        }
        return withdrawDao.queryWithdraws(f831411Req);
    }

}
