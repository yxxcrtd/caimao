package com.caimao.gjs.server.service;

import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.bana.common.api.enums.gjs.EGJSExchange;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.gjs.api.entity.GJSProductEntity;
import com.caimao.gjs.api.entity.GjsHolidayEntity;
import com.caimao.gjs.api.entity.req.*;
import com.caimao.gjs.api.entity.res.*;
import com.caimao.gjs.api.service.IGjsHolidayService;
import com.caimao.gjs.api.service.ITradeService;
import com.caimao.gjs.server.dao.GJSProductDao;
import com.caimao.gjs.server.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 贵金属交易服务
 */
@Service("tradeService")
public class TradeServiceImpl implements ITradeService {
    private static final Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);
    @Resource
    private TradeServiceHelper tradeServiceHelper;
    @Resource
    private IGjsHolidayService holidayService;
    @Resource
    private GJSProductDao gjsProductDao;

    /**
     * 查询交易所登录状态(已过)
     * @param req 请求对象
     * @return 是否登录
     * @throws Exception
     */
    @Override
    public Boolean queryTradeLogin(FQueryTradeCommonReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //查询是否登录
        return tradeServiceHelper.queryLogin(egjsExchange.getCode(), traderId);
    }

    /**
     * 退出交易所(已过)
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doTradeLogOut(FQueryTradeCommonReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //退出交易所
        tradeServiceHelper.doLogOut(egjsExchange.getCode(), traderId);
    }

    /**
     * 查询商品列表(已过)
     * @param req 请求对象
     * @return 商品列表
     * @throws Exception
     */
    @Override
    public List<HashMap<String, Object>> queryGoodsList(FQueryGoodsListReq req) throws Exception {
        List<HashMap<String, Object>> goodsList = new ArrayList<>();
        LinkedHashMap<String, Map<String, Object>> goodsMap;
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            goodsMap = tradeServiceHelper.getGoodsCache("njs_goods");
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())) {
            goodsMap = tradeServiceHelper.getGoodsCache("sjs_goods");
        }else if(req.getExchange().equals(EGJSExchange.LIFFE.getCode())) {
            goodsMap = tradeServiceHelper.getGoodsCache("liffe_goods");
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }

        for(Map.Entry<String, Map<String, Object>> entry:goodsMap.entrySet()){
            if(req.getDataType() == 1 && entry.getValue().get("isGoods") == 0){
                continue;
            }
            HashMap<String, Object> subData = new HashMap<>();
            subData.put("prodCode", entry.getValue().get("prodCode"));
            subData.put("prodName", entry.getValue().get("prodName"));
            subData.put("isGoods", entry.getValue().get("isGoods"));
            subData.put("tradeType", entry.getValue().get("tradeType"));
            subData.put("priceUnit", entry.getValue().get("priceUnit"));
            subData.put("handUnit", entry.getValue().get("handUnit"));
            subData.put("priceChangeUnit", Double.parseDouble(entry.getValue().get("priceChangeUnit").toString()) / 100);
            subData.put("priceLimit", Double.parseDouble(entry.getValue().get("priceLimit").toString()) / 100);
            subData.put("marginRatio", Double.parseDouble(entry.getValue().get("marginRatio").toString()) / 100);
            subData.put("feeRate", Double.parseDouble(entry.getValue().get("feeRate").toString()) / 10000);
            subData.put("exchange", req.getExchange());
            goodsList.add(subData);
        }
        return goodsList;
    }

    /**
     * 交易所登录(已过)
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doTradeLogin(FDoTradeLoginReq req) throws Exception{
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //登录并保存token
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            tradeServiceHelper.doNJSTradeLogin(traderId, req.getTradePwd());
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())){
            tradeServiceHelper.doSJSTradeLogin(traderId, req.getTradePwd());
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 委托下单(南交所已过、上金所未过)
     * @param req 请求对象
     * @return String
     * @throws Exception
     */
    @Override
    public String doEntrust(FDoEntrustReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //下单
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            if(req.getPrice().compareTo(BigDecimal.ZERO) != 0){
                return tradeServiceHelper.doNJSEntrustLimit(token, traderId, req);
            }else{
                return tradeServiceHelper.doNJSEntrustMarket(token, traderId, req);
            }
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())){
            return tradeServiceHelper.doSJSEntrustLimit(token, req);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 委托撤销(南交所已过、上金所未过)
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doEntrustCancel(FDoEntrustCancelReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //撤单
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())) {
            tradeServiceHelper.doNJSEntrustCancel(token, traderId, req);
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())){
            tradeServiceHelper.doSJSEntrustCancel(token, req);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 止盈止损下单(已过)
     * @param req 请求对象
     * @return String
     * @throws Exception
     */
    @Override
    public String doFullStop(FDoFullStopReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //验证参数
        if(req.getUpPrice().compareTo(new BigDecimal(0)) == 0 && req.getDownPrice().compareTo(new BigDecimal("0")) == 0){
            throw new CustomerException(EErrorCode.ERROR_CODE_300014.getMessage(), EErrorCode.ERROR_CODE_300014.getCode());
        }
        //止盈止损下单
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            return tradeServiceHelper.doNJSFullStop(token, traderId, req);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 止盈止损撤单(已过)
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doFullStopCancel(FDoFullStopCancelReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //止盈止损撤单
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            tradeServiceHelper.doNJSFullStopCancel(token, traderId, req);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询止盈止损单(已过)
     * @param req 请求对象
     * @return List
     * @throws Exception
     */
    @Override
    public List<FQueryFullStopRes> queryFullStop(FQueryTradeCommonReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //查询止盈止损订单列表
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())) {
            return tradeServiceHelper.queryNJSFullStop(token, traderId);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 条件单下单(已过)
     * @param req 请求对象
     * @return String
     * @throws Exception
     */
    @Override
    public String doCondition(FDoConditionReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //止盈止损下单
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            return tradeServiceHelper.doNJSCondition(token, traderId, req);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 条件单撤单(已过)
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doConditionCancel(FDoConditionCancelReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //止盈止损撤单
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            tradeServiceHelper.doNJSConditionCancel(token, traderId, req);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询条件单(已过)
     * @param req 请求对象
     * @return List
     * @throws Exception
     */
    @Override
    public List<FQueryConditionRes> queryCondition(FQueryTradeCommonReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //查询条件订单列表
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())) {
            return tradeServiceHelper.queryNJSCondition(token, traderId);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询持仓
     * @param req 请求对象
     * @return List
     * @throws Exception
     */
    @Override
    public List<FQueryHoldRes> queryHold(FQueryTradeCommonReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //查询条件订单列表
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())) {
            return tradeServiceHelper.queryNJSHold(token, traderId);
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())) {
            return tradeServiceHelper.querySJSHold(token);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询当日委托(已过)
     * @param req 请求对象
     * @return List
     * @throws Exception
     */
    @Override
    public List<FQueryEntrustRes> queryEntrust(FQueryTradeCommonReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //下单
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            return tradeServiceHelper.queryNJSEntrust(token, traderId, false);
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())) {
            return tradeServiceHelper.querySJSEntrust(token, false);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询当日成交(已过)
     * @param req 请求对象
     * @return List
     * @throws Exception
     */
    @Override
    public List<FQueryMatchRes> queryMatch(FQueryTradeCommonReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //下单
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            return tradeServiceHelper.queryNJSMatch(token, traderId);
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())) {
            return tradeServiceHelper.querySJSMatch(token);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询资产
     * @param req 请求对象
     * @return FQueryFundsRes
     * @throws Exception
     */
    public FQueryFundsRes queryFunds(FQueryTradeCommonReq req) throws Exception{
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //获取资产
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            return tradeServiceHelper.queryNJSFunds(token, traderId);
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())){
            return tradeServiceHelper.querySJSFunds(token);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询资产简单版
     * @param req 请求对象
     * @return FQueryFundsRes
     * @throws Exception
     */
    public FQueryFundsSimpleRes queryFundsSimple(FQueryTradeCommonReq req) throws Exception{
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //获取资产
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            return tradeServiceHelper.queryNJSFundsSimple(token, traderId);
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())){
            return tradeServiceHelper.querySJSFundsSimple(token);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询历史委托
     * @param req 请求对象
     * @return List
     * @throws Exception
     */
    @Override
    public FQueryHistoryEntrustReq queryHistoryEntrustList(FQueryHistoryEntrustReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //设置交易员编号
        req.setTraderId(traderId);
        //查询历史委托
        if (EGJSExchange.NJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.queryNJSHistoryEntrust(req);
        } else if (EGJSExchange.SJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.querySJSHistoryEntrust(req);
        } else {
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询委托整合列表
     * @param req 请求对象
     * @return FQueryHistoryEntrustReq
     * @throws Exception
     */
    @Override
    public FQueryHistoryEntrustReq queryEntrustIntegrate(FQueryHistoryEntrustReq req) throws Exception{
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //设置交易员编号
        req.setTraderId(traderId);
        //查询历史委托
        if (EGJSExchange.NJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.queryNJSEntrustIntegrate(req, token);
        } else if (EGJSExchange.SJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.querySJSEntrustIntegrate(req, token);
        } else {
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询历史交易
     *
     * @param req 请求对象
     * @return List
     * @throws Exception
     */
    @Override
    public FQueryHistoryTradeReq queryHistoryTradeList(FQueryHistoryTradeReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //设置交易员编号
        req.setTraderId(traderId);
        //查询历史成交
        if (EGJSExchange.NJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.queryNJSHistoryTrade(req);
        } else if (EGJSExchange.SJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.querySJSHistoryTrade(req);
        } else {
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询成交整合列表
     * @param req 请求对象
     * @return FQueryHistoryTradeReq
     * @throws Exception
     */
    @Override
    public FQueryHistoryTradeReq queryTradeIntegrate(FQueryHistoryTradeReq req) throws Exception{
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //设置交易员编号
        req.setTraderId(traderId);
        //查询历史成交
        if (EGJSExchange.NJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.queryNJSTradeIntegrate(req, token);
        } else if (EGJSExchange.SJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.querySJSTradeIntegrate(req, token);
        } else {
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询历史出入金
     *
     * @param req 请求对象
     * @return List
     * @throws Exception
     */
    public FQueryHistoryTransferReq queryHistoryTransferList(FQueryHistoryTransferReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //设置交易员编号
        req.setTraderId(traderId);
        //查询历史吃如今
        if (EGJSExchange.NJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.queryNJSHistoryTransfer(req);
        } else if (EGJSExchange.SJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.querySJSHistoryTransfer(req);
        } else {
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 处理推送消息
     * @param exchange 交易所代码
     * @param object 数据对象
     * @throws Exception
     */
    @Override
    public void pushMatchMsg(String exchange, Object object) throws Exception {
        try{
            //判断交易所是否存在
            tradeServiceHelper.getExchange(exchange);
            //推送成交信息
            if (EGJSExchange.NJS.getCode().equals(exchange)) {
                this.tradeServiceHelper.pushNJSMatchMsg(object);
            } else if (EGJSExchange.SJS.getCode().equals(exchange)) {
                this.tradeServiceHelper.pushSJSMatchMsg(object);
            } else {
                throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
            }
        }catch(Exception e){
            logger.error("推送消息失败", e);
        }
    }

    /**
     * 是否休市（后台没有设置时间的话，即为休市）
     *
     * @param exchange 交易所代码
     * @return 交易所状态（1 交易 2 休市） message 休市 9:00-11:30,13:00-15:30,20:00-2:30可交易
     * @throws Exception
     */
    @Override
    public Map<String, String> isTrade(String exchange) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("status", "2");
        map.put("message", TradeServiceHelper.getMessageByExchange(exchange));
        long time = System.currentTimeMillis();
        String str = String.format("%tF %<tT", time);
        //logger.info("接收到的参数：" + exchange + "," + str);
        Date srcDate = DateUtil.strToDate(str, DateUtil.DATA_TIME_PATTERN_1);
        SimpleDateFormat df1 = new SimpleDateFormat(DateUtil.DATE_FORMAT_STRING);
        String d = df1.format(time);
        //logger.info("拆分的年月日：" + d);
        SimpleDateFormat df2 = new SimpleDateFormat(DateUtil.DATA_TIME_PATTERN_5);
        String srcTime = df2.format(time);
        //logger.info("拆分的时分秒:" + srcTime);
        String dbTradeTime = this.holidayService.queryGjsHolidayByExchangeAndHoliday(exchange, d);
        logger.info("后台设置的交易时间段：" + dbTradeTime); // TradeServiceHelper.tradeTimeList.toString());

        // 将节假日设置为0的时候，直接返回
        if ("0".equals(dbTradeTime)) {
            map.put("status", "3");
            map.put("message", "节假日不交易");
            return map;
        }

        if (TradeServiceHelper.initTradeTime(dbTradeTime)) {
            // 是否是交易时间
            //if (isTradeDuring(dbTradeDate, srcTime, srcDate)) {
                //returnBoolean = true;
            //}
            for (String s : dbTradeTime.split(",")) {
                if (!StringUtils.isBlank(s) && 9 == s.length()) {
                    logger.info("star：" + s.substring(0, 4));
                    logger.info("end：" + s.substring(5, s.length()));
                    logger.info("compare：" + srcTime);
                    if (TradeServiceHelper.isInterval(s.substring(0, 4), s.substring(5, s.length()), srcTime, "HHmm")) {
                        map.put("status", "1");
                        map.put("message", "可交易");
                        break;
                    }
                }
            }
        }
        logger.info("是否休市接口返回的map是：" + map);
        return map;
    }

    /**
     * 查询套餐列表
     * @param exchange 交易所代码
     * @return 套餐列表
     * @throws Exception
     */
    @Override
    public List<FQueryPackageListRes> queryPackageList(String exchange) throws Exception {
        //判断交易所是否存在
        tradeServiceHelper.getExchange(exchange);
        //查询套餐列表
        if (EGJSExchange.NJS.getCode().equals(exchange)) {
            return this.tradeServiceHelper.queryNJSPackageList();
        }else {
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询套餐信息
     * @param req 请求对象
     * @return FQueryPackageInfoRes
     * @throws Exception
     */
    @Override
    public FQueryPackageInfoRes queryPackageInfo(FQueryTradeCommonReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //查询套餐列表
        if (EGJSExchange.NJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.queryNJSPackageInfo(token, traderId);
        }else {
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询套餐申请记录
     * @param req 请求对象
     * @return FQueryPackageHistoryRes
     * @throws Exception
     */
    @Override
    public List<FQueryPackageHistoryRes> queryPackageHistory(FQueryPackageHistoryReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //查询套餐列表
        if (EGJSExchange.NJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.queryNJSPackageHistory(token, traderId, req.getStartDate(), req.getEndDate());
        }else {
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 交易套餐购买
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doPackageBuy(FDoPackageBuyReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //购买套餐
        if (EGJSExchange.NJS.getCode().equals(req.getExchange())) {
            this.tradeServiceHelper.doNJSPackageBuy(token, traderId, req.getPackageId());
        }else {
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询交易所商品列表
     * @param exchange 交易所代码
     * @return List
     * @throws Exception
     */
    @Override
    public List<GJSProductEntity> queryProductList(String exchange) throws Exception {
        return gjsProductDao.queryExchangeGoods(exchange);
    }

    /**
     * 更新商品
     * @param entity 商品实体
     * @throws Exception
     */
    @Override
    public void updateProduct(GJSProductEntity entity) throws Exception {
        gjsProductDao.updateExchangeGoods(entity);
    }

    /**
     * 查询单个品种下单信息
     * @param req 请求对象
     * @return FQueryProdSingleRes
     * @throws Exception
     */
    @Override
    public FQueryProdSingleRes queryProdSingle(FQueryProdSingleReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //查询套餐列表
        if (EGJSExchange.NJS.getCode().equals(req.getExchange())) {
            return this.tradeServiceHelper.queryNJSProdSingle(token, traderId, req.getProdCode());
        }else {
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }
}