package com.caimao.bana.api.service;

import com.caimao.bana.api.enums.RiskMsgSendType;

/**
 * 风控服务
 */
public interface IRiskService {
    /**
     * 发送风控短信
     * @throws Exception
     */
    public void doSendRiskSms() throws Exception;

    /**
     * 保存风险发送记录
     * @return Integer
     * @throws Exception
     */
    public Integer insertRiskRecord(Long userId, String sendContent, RiskMsgSendType riskMsgSendType) throws Exception;
}
