package com.caimao.gjs.api.service;

import com.caimao.gjs.api.entity.history.*;

import java.util.List;

/**
 * 贵金属交易管理服务
 */
public interface ITradeManageService {

    /**
     * 根据TraderId查询NJS历史委托列表
     *
     * @param traderId
     * @return
     * @throws Exception
     */
    List<GjsNJSHistoryEntrustEntity> queryNJSHistoryEntrustListByTraderIdForManage(String traderId) throws Exception;

    /**
     * 根据TraderId查询NJS历史交易列表
     *
     * @param traderId
     * @return
     * @throws Exception
     */
    List<GjsNJSHistoryTradeEntity> queryNJSHistoryTradeListByTraderIdForManage(String traderId) throws Exception;

    /**
     * 根据TraderId查询NJS历史出入金列表
     *
     * @param traderId
     * @return
     * @throws Exception
     */
    List<GjsNJSHistoryTransferEntity> queryNJSHistoryTransferListByTraderIdForManage(String traderId) throws Exception;

    /**
     * 根据TraderId查询SJS历史委托列表
     *
     * @param traderId
     * @return
     * @throws Exception
     */
    List<GjsSJSHistoryEntrustEntity> querySJSHistoryEntrustListByTraderIdForManage(String traderId) throws Exception;

    /**
     * 根据TraderId查询SJS历史交易列表
     *
     * @param traderId
     * @return
     * @throws Exception
     */
    List<GjsSJSHistoryTradeEntity> querySJSHistoryTradeListByTraderIdForManage(String traderId) throws Exception;

    /**
     * 根据TraderId查询SJS历史出入金列表
     *
     * @param traderId
     * @return
     * @throws Exception
     */
    List<GjsSJSHistoryTransferEntity> querySJSHistoryTransferListByTraderIdForManage(String traderId) throws Exception;

    /**
     * 获取历史出入金金额
     *
     * @param exchange
     * @param type
     * @return
     * @throws Exception
     */
    Long getHistoryTransferTotalMoneySum(String exchange, String type) throws Exception;

    /**
     * 获取历史出入金次数
     *
     * @param exchange
     * @param type
     * @return
     * @throws Exception
     */
    Long getHistoryTransferCount(String exchange, String type) throws Exception;

    /**
     * 获取交易额
     *
     * @param exchange
     * @return
     * @throws Exception
     */
    String getHistoryTradeTotalMoney(String exchange) throws Exception;

    /**
     * 获取交易次数
     *
     * @param exchange
     * @return
     * @throws Exception
     */
    Long getHistoryTradeTotalCount(String exchange) throws Exception;

    /**
     * 上金所09:00-11:30交易额
     *
     * @return
     * @throws Exception
     */
    String getHistoryTradeTotalMoney1() throws Exception;

    /**
     * 上金所09:00-11:30交易额
     *
     * @return
     * @throws Exception
     */
    Long getHistoryTradeTotalCount1() throws Exception;

    /**
     * 上金所13:30-15:30交易额
     *
     * @return
     * @throws Exception
     */
    String getHistoryTradeTotalMoney2() throws Exception;

    /**
     * 上金所13:30-15:30交易次数
     *
     * @return
     * @throws Exception
     */
    Long getHistoryTradeTotalCount2() throws Exception;

    /**
     * 上金所20:00-02:30交易额
     *
     * @return
     * @throws Exception
     */
    String getHistoryTradeTotalMoney3() throws Exception;

    /**
     * 上金所20:00-02:30交易次数
     *
     * @return
     * @throws Exception
     */
    Long getHistoryTradeTotalCount3() throws Exception;

    /**
     * 销户
     * @param userId 用户编号
     * @param exchange 交易所编号
     * @param traderId 交易员编号
     * @throws Exception
     */
    public void removeAccount(Long userId, String exchange, String traderId) throws Exception;
}
