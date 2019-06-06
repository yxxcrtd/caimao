/*
*IHeepayWithdraw.java
*Created on 2015/5/13 8:58
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.api.service;


import com.caimao.bana.api.entity.req.F831411Req;
import com.caimao.bana.api.entity.res.F831411Res;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0.1
 */
public interface IHeepayWithdraw {

    /**
     * 处理汇付宝提现的方法
     * @param orderNo 处理的提现订单号码
     * @return 提现结果
     * @throws Exception
     */
    public boolean doWithdraw(Long orderNo) throws Exception;

    /**
     * 汇付宝回调通知的处理方法
     * @param withdrawReviewParams 回调请求的参数
     * @param sign                 签名
     * @return 成功与否
     * @throws Exception
     */
    public boolean doWithdrawReview(LinkedHashMap<String, Object> withdrawReviewParams, String sign) throws Exception;

    /**
     * 汇付宝批付主动查询接口
     * @param orderNo 订单
     * @return 返回结果
     */
    public Map<String, Object> doWithdrawQuery(Long orderNo) throws Exception;

    /**
     * 查询提现列表
     * @param f831411Req
     * @return
     * @throws Exception
     */
    public List<F831411Res> queryWithdrawOrders(F831411Req f831411Req) throws Exception;


}
