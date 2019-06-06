package com.caimao.gjs.api.service;

import com.caimao.gjs.api.entity.req.*;

/**
 * 交易数据服务
 */
public interface ITradeJobService {

    /**
     * 更新商品列表
     * @throws Exception
     */
    public void updateGoods() throws Exception;

    /**
     * 更新用户交易员编号关系
     * @throws Exception
     */
    public void updateTraderId() throws Exception;

    /**
     * 检测用户风险率并发送
     * @throws Exception
     */
    public void sendRiskUser(Integer tplNo) throws Exception;

    /**
     * 解析南交所历史数据
     * @param dataType 数据类型
     * @throws Exception
     */
    public void parseNJSHistory(String dataType, String date) throws Exception;

    /**
     * 解析上金所历史数据
     * @param dataType 数据类型
     * @throws Exception
     */
    public void parseSJSHistory(String dataType) throws Exception;
}