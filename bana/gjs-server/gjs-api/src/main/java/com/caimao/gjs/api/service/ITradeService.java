package com.caimao.gjs.api.service;

import com.caimao.gjs.api.entity.GJSProductEntity;
import com.caimao.gjs.api.entity.req.*;
import com.caimao.gjs.api.entity.res.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易服务
 */
public interface ITradeService {

    /**
     * 查询交易所是否登录
     * @param req 请求对象
     * @return 是否已经登录
     * @throws Exception
     */
    public Boolean queryTradeLogin(FQueryTradeCommonReq req) throws Exception;

    /**
     * 交易所退出
     * @param req 请求对象
     * @throws Exception
     */
    public void doTradeLogOut(FQueryTradeCommonReq req) throws Exception;

    /**
     * 查询商品列表
     * @return HashMap
     * @throws Exception
     */
    public List<HashMap<String, Object>> queryGoodsList(FQueryGoodsListReq req) throws Exception;

    /**
     * 登录交易所
     * @param req 请求对象
     * @throws Exception
     */
    public void doTradeLogin(FDoTradeLoginReq req) throws Exception;

    /**
     * 委托下单
     * @param req 请求对象
     * @return String
     * @throws Exception
     */
    public String doEntrust(FDoEntrustReq req) throws Exception;

    /**
     * 委托撤销
     * @param req 请求对象
     * @throws Exception
     */
    public void doEntrustCancel(FDoEntrustCancelReq req) throws Exception;

    /**
     * 止盈止损委托下单
     * @param req 请求对象
     * @return String
     * @throws Exception
     */
    public String doFullStop(FDoFullStopReq req) throws Exception;

    /**
     * 止盈止损委托撤销
     * @param req 请求对象
     * @throws Exception
     */
    public void doFullStopCancel(FDoFullStopCancelReq req) throws Exception;

    /**
     * 查询止盈止损委托
     * @param req 请求对象
     * @return FQueryFullStopRes
     * @throws Exception
     */
    public List<FQueryFullStopRes> queryFullStop(FQueryTradeCommonReq req) throws Exception;

    /**
     * 条件单委托下单
     * @param req 请求对象
     * @return String
     * @throws Exception
     */
    public String doCondition(FDoConditionReq req) throws Exception;

    /**
     * 条件单委托撤销
     * @param req 请求对象
     * @throws Exception
     */
    public void doConditionCancel(FDoConditionCancelReq req) throws Exception;

    /**
     * 查询条件单委托
     * @param req 请求对象
     * @return FQueryConditionRes
     * @throws Exception
     */
    public List<FQueryConditionRes> queryCondition(FQueryTradeCommonReq req) throws Exception;

    /**
     * 查询持仓
     * @param req 请求对象
     * @return FQueryHoldRes
     * @throws Exception
     */
    public List<FQueryHoldRes> queryHold(FQueryTradeCommonReq req) throws Exception;

    /**
     * 查询当日委托
     * @param req 请求对象
     * @return FQueryEntrustRes
     * @throws Exception
     */
    public List<FQueryEntrustRes> queryEntrust(FQueryTradeCommonReq req) throws Exception;

    /**
     * 查询当日成交
     * @param req 请求对象
     * @return FQueryMatchRes
     * @throws Exception
     */
    public List<FQueryMatchRes> queryMatch(FQueryTradeCommonReq req) throws Exception;

    /**
     * 查询资产
     * @param req 请求对象
     * @return FQueryFundsRes
     * @throws Exception
     */
    public FQueryFundsRes queryFunds(FQueryTradeCommonReq req) throws Exception;

    /**
     * 查询资产简单版
     * @param req 请求对象
     * @return FQueryFundsSimpleRes
     * @throws Exception
     */
    public FQueryFundsSimpleRes queryFundsSimple(FQueryTradeCommonReq req) throws Exception;

    /**
     * 查询历史委托列表
     * @return FQueryHistoryEntrustReq
     * @throws Exception
     */
    public FQueryHistoryEntrustReq queryHistoryEntrustList(FQueryHistoryEntrustReq req) throws Exception;

    /**
     * 查询委托整合列表
     * @param req 请求对象
     * @return FQueryHistoryEntrustReq
     * @throws Exception
     */
    public FQueryHistoryEntrustReq queryEntrustIntegrate(FQueryHistoryEntrustReq req) throws Exception;

    /**
     * 查询历史交易列表
     * @param req 请求对象
     * @return FQueryHistoryTradeReq
     * @throws Exception
     */
    public FQueryHistoryTradeReq queryHistoryTradeList(FQueryHistoryTradeReq req) throws Exception;

    /**
     * 查询成交整合列表
     * @param req 请求对象
     * @return FQueryHistoryTradeReq
     * @throws Exception
     */
    public FQueryHistoryTradeReq queryTradeIntegrate(FQueryHistoryTradeReq req) throws Exception;

    /**
     * 查询历史出入金列表
     * @param req 请求对象
     * @return FQueryHistoryTransferReq
     * @throws Exception
     */
    public FQueryHistoryTransferReq queryHistoryTransferList(FQueryHistoryTransferReq req) throws Exception;

    /**
     * 处理推送消息
     * @param exchange 交易所代码
     * @param object 数据对象
     * @throws Exception
     */
    public void pushMatchMsg(String exchange, Object object) throws Exception;

    /**
     * 交易所是否休市
     *
     * @param exchange 交易所代码
     * @return 是否休市
     */
    public Map<String, String> isTrade(String exchange) throws Exception;

    /**
     * 查询套餐列表
     * @param exchange 交易所代码
     * @return 套餐列表
     * @throws Exception
     */
    public List<FQueryPackageListRes> queryPackageList(String exchange) throws Exception;

    /**
     * 查询套餐信息
     * @param req 请求对象
     * @return FQueryPackageInfoRes
     * @throws Exception
     */
    public FQueryPackageInfoRes queryPackageInfo(FQueryTradeCommonReq req) throws Exception;

    /**
     * 查询套餐申请记录
     * @param req 请求对象
     * @return FQueryPackageHistoryRes
     * @throws Exception
     */
    public List<FQueryPackageHistoryRes> queryPackageHistory(FQueryPackageHistoryReq req) throws Exception;

    /**
     * 交易套餐购买
     * @param req 请求对象
     * @throws Exception
     */
    public void doPackageBuy(FDoPackageBuyReq req) throws Exception;

    /**
     * 查询交易所商品列表
     * @param exchange 交易所代码
     * @return List
     * @throws Exception
     */
    public List<GJSProductEntity> queryProductList(String exchange) throws Exception;

    /**
     * 更新商品
     * @param entity 商品实体
     * @throws Exception
     */
    public void updateProduct(GJSProductEntity entity) throws Exception;

    /**
     * 查询单个品种下单信息
     * @param req 请求对象
     * @return FQueryProdSingleRes
     * @throws Exception
     */
    public FQueryProdSingleRes queryProdSingle(FQueryProdSingleReq req) throws Exception;
}
