package com.caimao.bana.api.service;

import com.caimao.bana.common.api.exception.CustomerException;

/**
 * 比特币抵押接口
 */
public interface IBtcPledgeService {

    /**
     * 比特币抵押接口
     * @param userId    用户ID
     * @param orderAmount   变更数量（*100）
     * @param orderAbstract 变更说明
     * @param seqFlag   变更类型
     * @param operUser  操作用户
     * @throws CustomerException
     * @throws Exception
     * @return  Long  返回订单号
     */
    public Long doSavePledgeOrder(Long userId, Long orderAmount, String orderAbstract, String seqFlag, String operUser) throws Exception;
}
