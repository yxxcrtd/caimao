package com.caimao.bana.api.service;

import com.caimao.bana.common.api.exception.CustomerException;

/**
 * Created by WangXu on 2015/4/28.
 */
public interface IAdjustOrderService {

    /**
     * 红冲蓝补的接口方法
     * @param userId    用户ID
     * @param orderAmount   变更数量（*100）
     * @param orderAbstract 变更说明
     * @param seqFlag   变更类型
     * @param operUser  操作用户
     * @param needAudit 是否需要审核
     * @throws CustomerException
     * @throws Exception
     * @return  Long  返回订单号
     */
    public Long doSaveAdjustOrder(Long userId, Long orderAmount, String orderAbstract, String seqFlag, String operUser, boolean needAudit) throws Exception, Exception;


}
