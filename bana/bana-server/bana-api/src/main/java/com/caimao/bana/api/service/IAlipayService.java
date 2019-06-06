package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.AlipayRecordEntity;

/**
 * 支付宝
 */
public interface IAlipayService {
    /**
     * 保存支付宝交易记录
     * @param alipayRecordEntity 实体类
     * @throws Exception
     */
    public void saveTradeRecord(AlipayRecordEntity alipayRecordEntity) throws Exception;

    /**
     * 处理未完成的支付宝
     * @throws Exception
     */
    public void processAlipay() throws Exception;

    /**
     * 处理指定orderNo支付宝订单
     * @param orderNo
     * @throws Exception
     */
    public void alipayToSuccess(Long orderNo) throws Exception;
}