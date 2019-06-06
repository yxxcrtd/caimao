package com.fmall.bana.controller.api.gjs;

import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.bana.common.api.enums.gjs.EGJSExchange;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.gjs.api.entity.req.*;
import com.caimao.gjs.api.entity.res.*;
import com.caimao.gjs.api.service.IAccountService;
import com.caimao.gjs.api.service.ITradeService;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.CommonStringUtils;
import com.fmall.bana.utils.crypto.RSA;
import com.fmall.bana.utils.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 贵金属交易API接口
 * Created by Administrator on 2015/10/16.
 */
@Controller
@RequestMapping(value = "/api/gjs_trade/")
public class GjsTradeApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(GjsTradeApiController.class);

    @Resource
    private ITradeService tradeService;
    @Resource
    private IAccountService accountService;

    /**
     * <p>查询商品列表接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryGoodsList</p>
     * <p>请求方式：GET</p>
     *
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param dataType 列表类型 0行情 1交易
     * @return 商品列表 List<HashMap<String, Object>>
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryGoodsListReq
     */
    @ResponseBody
    @RequestMapping(value = "/queryGoodsList", method = RequestMethod.GET)
    public Map queryGoodsList(
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "dataType") Integer dataType
    ) throws Exception {
        try {
            FQueryGoodsListReq req = new FQueryGoodsListReq();
            req.setExchange(exchange);
            req.setDataType(dataType);
            return CommonStringUtils.mapReturn(this.tradeService.queryGoodsList(req));
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询热门商品列表接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryHotGoodsList</p>
     * <p>请求方式：GET</p>
     *
     * @return 商品列表 List<Map<String, String>>
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryHotGoodsList", method = RequestMethod.GET)
    public Map queryHotGoods() throws Exception {
        try {
            List<Map<String, String>> goodsList = new ArrayList<>();
            Map<String, String> subMap1 = new TreeMap<>();
            subMap1.put("exchange", EGJSExchange.NJS.getCode());
            subMap1.put("prodCode", "AG");
            subMap1.put("prodName", "白银");
            goodsList.add(subMap1);

            Map<String, String> subMap2 = new TreeMap<>();
            subMap2.put("exchange", EGJSExchange.SJS.getCode());
            subMap2.put("prodCode", "Ag(T+D)");
            subMap2.put("prodName", "白银延期");
            goodsList.add(subMap2);

            Map<String, String> subMap3 = new TreeMap<>();
            subMap3.put("exchange", EGJSExchange.SJS.getCode());
            subMap3.put("prodCode", "mAu(T+D)");
            subMap3.put("prodName", "Mini黄金延期");
            goodsList.add(subMap3);
            return CommonStringUtils.mapReturn(goodsList);
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询是否登录</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryTradeLogin</p>
     * <p>请求方式：GET</p>
     *
     * @param token    用户登录token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 是否登录
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryTradeCommonReq
     */
    @ResponseBody
    @RequestMapping(value = "/queryTradeLogin", method = RequestMethod.GET)
    public Map queryTradeLogin(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setExchange(exchange);
            req.setUserId(userId);
            return CommonStringUtils.mapReturn(tradeService.queryTradeLogin(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>注销登录</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/doTradeLogOut</p>
     * <p>请求方式：POST</p>
     *
     * @param token    用户登录token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 是否注销成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryTradeCommonReq
     */
    @ResponseBody
    @RequestMapping(value = "/doTradeLogOut", method = RequestMethod.POST)
    public Map doTradeLogOut(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setExchange(exchange);
            req.setUserId(userId);
            this.tradeService.doTradeLogOut(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>交易所登录</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/doTradeLogin</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param tradePwd 交易密码
     * @return 是否登录成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoTradeLoginReq
     */
    @ResponseBody
    @RequestMapping(value = "/doTradeLogin", method = RequestMethod.POST)
    public Map doTradeLogin(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "tradePwd") String tradePwd
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoTradeLoginReq req = new FDoTradeLoginReq();
            req.setExchange(exchange);
            req.setUserId(userId);
            req.setTradePwd(RSA.decodeByPwd(tradePwd));
            this.tradeService.doTradeLogin(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>下单接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/doEntrust</p>
     * <p>请求方式：POST</p>
     *
     * @param token     登陆返回token
     * @param exchange  交易所代码 NJS 南交所、SJS上金所
     * @param prodCode  商品代码
     * @param tradeType 交易类型 南交所 B 现货买入 S现货卖出  上金所 4011 现货买入 4012 现货卖出 4041 延期开多仓 4042 延期开空仓 4043 延期平多仓 4044 延期平空仓
     * @param price     价格
     * @param amount    数量
     * @return 订单编号
     * <p/>
     * <p>下单的请求对象 </p>
     * @throws Exception
     * @see FDoEntrustReq
     */
    @ResponseBody
    @RequestMapping(value = "/doEntrust", method = RequestMethod.POST)
    public Map doEntrust(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "prodCode") String prodCode,
            @RequestParam(value = "tradeType") String tradeType,
            @RequestParam(value = "price") BigDecimal price,
            @RequestParam(value = "amount") Integer amount
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoEntrustReq req = new FDoEntrustReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setProdCode(prodCode);
            req.setTradeType(tradeType);
            req.setPrice(price);
            req.setAmount(amount);
            return CommonStringUtils.mapReturn(this.tradeService.doEntrust(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>撤单接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/doEntrustCancel</p>
     * <p>请求方式：POST</p>
     *
     * @param token     登录返回token
     * @param exchange  交易所代码 NJS 南交所、SJS上金所
     * @param prodCode  商品编码
     * @param orderNo   订单编号
     * @param tradeType 交易类型 南交所 B 现货买入 S现货卖出  上金所 4011 现货买入 4012 现货卖出 4041 延期开多仓 4042 延期开空仓 4043 延期平多仓 4044 延期平空仓
     * @return 是否撤销成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoEntrustCancelReq
     */
    @ResponseBody
    @RequestMapping(value = "/doEntrustCancel", method = RequestMethod.POST)
    public Map doEntrustCancel(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "prodCode") String prodCode,
            @RequestParam(value = "orderNo") String orderNo,
            @RequestParam(value = "tradeType") String tradeType
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoEntrustCancelReq req = new FDoEntrustCancelReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setProdCode(prodCode);
            req.setOrderNo(orderNo);
            req.setTradeType(tradeType);
            this.tradeService.doEntrustCancel(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>止盈止损下单</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/doFullStop</p>
     * <p>请求方式：POST</p>
     *
     * @param token     登录返回token
     * @param exchange  交易所代码 NJS 南交所、SJS上金所
     * @param prodCode  商品编码
     * @param upPrice   止盈价
     * @param downPrice 止损价
     * @param amount    下单数量
     * @return 订单编号
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoFullStopReq
     */
    @ResponseBody
    @RequestMapping(value = "/doFullStop", method = RequestMethod.POST)
    public Map doFullStop(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "prodCode") String prodCode,
            @RequestParam(value = "upPrice") String upPrice,
            @RequestParam(value = "downPrice") String downPrice,
            @RequestParam(value = "amount") Integer amount
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoFullStopReq req = new FDoFullStopReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setProdCode(prodCode);
            req.setUpPrice(new BigDecimal(upPrice).setScale(2, BigDecimal.ROUND_DOWN));
            req.setDownPrice(new BigDecimal(downPrice).setScale(2, BigDecimal.ROUND_DOWN));
            req.setAmount(amount);
            return CommonStringUtils.mapReturn(this.tradeService.doFullStop(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>止盈止损撤单</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/doFullStopCancel</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param prodCode 商品编码
     * @param orderNo  订单编号
     * @return 是否撤销成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoFullStopCancelReq
     */
    @ResponseBody
    @RequestMapping(value = "/doFullStopCancel", method = RequestMethod.POST)
    public Map doFullStopCancel(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "prodCode") String prodCode,
            @RequestParam(value = "orderNo") String orderNo,
            @RequestParam(value = "applyDate") String applyDate
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoFullStopCancelReq req = new FDoFullStopCancelReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setProdCode(prodCode);
            req.setOrderNo(orderNo);
            req.setApplyDate(applyDate);
            this.tradeService.doFullStopCancel(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询止盈止损列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryFullStop</p>
     * <p>请求方式：GET</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 止盈止损列表
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryTradeCommonReq
     * <p/>
     * <p>返回对象</p>
     * @see FQueryFullStopRes
     */
    @ResponseBody
    @RequestMapping(value = "/queryFullStop", method = RequestMethod.GET)
    public Map queryFullStop(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            return CommonStringUtils.mapReturn(this.tradeService.queryFullStop(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>条件单下单</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/doCondition</p>
     * <p>请求方式：POST</p>
     *
     * @param token      登录返回token
     * @param exchange   交易所代码 NJS 南交所、SJS上金所
     * @param tradeType  交易类型 南交所 B 现货买入 S现货卖出  上金所 4011 现货买入 4012 现货卖出 4041 延期开多仓 4042 延期开空仓 4043 延期平多仓 4044 延期平空仓
     * @param prodCode   商品编码
     * @param price      价格
     * @param amount     数量
     * @param touchPrice 触发价格
     * @param condition  触发条件 A. >=  B. <=
     * @return 订单编号
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoConditionReq
     */
    @ResponseBody
    @RequestMapping(value = "/doCondition", method = RequestMethod.POST)
    public Map doCondition(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "tradeType") String tradeType,
            @RequestParam(value = "prodCode") String prodCode,
            @RequestParam(value = "price") String price,
            @RequestParam(value = "amount") Integer amount,
            @RequestParam(value = "touchPrice") String touchPrice,
            @RequestParam(value = "condition") String condition
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoConditionReq req = new FDoConditionReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setTradeType(tradeType);
            req.setProdCode(prodCode);
            req.setPrice(new BigDecimal(price).setScale(2, BigDecimal.ROUND_DOWN));
            req.setTouchPrice(new BigDecimal(touchPrice).setScale(2, BigDecimal.ROUND_DOWN));
            req.setAmount(amount);
            req.setCondition(condition);
            return CommonStringUtils.mapReturn(this.tradeService.doCondition(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>条件单撤单</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/doConditionCancel</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param prodCode 商品编码
     * @param orderNo  订单编号
     * @return 是否撤销成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoConditionCancelReq
     */
    @ResponseBody
    @RequestMapping(value = "/doConditionCancel", method = RequestMethod.POST)
    public Map doConditionCancel(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "prodCode") String prodCode,
            @RequestParam(value = "orderNo") String orderNo,
            @RequestParam(value = "applyDate") String applyDate
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoConditionCancelReq req = new FDoConditionCancelReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setProdCode(prodCode);
            req.setOrderNo(orderNo);
            req.setApplyDate(applyDate);
            this.tradeService.doConditionCancel(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询条件单列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryCondition</p>
     * <p>请求方式：GET</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 条件单列表
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryTradeCommonReq
     * <p/>
     * <p>返回对象</p>
     * @see FQueryConditionRes
     */
    @ResponseBody
    @RequestMapping(value = "/queryCondition", method = RequestMethod.GET)
    public Map queryCondition(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            return CommonStringUtils.mapReturn(this.tradeService.queryCondition(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询持仓列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryHold</p>
     * <p>请求方式：GET</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 持仓列表
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryTradeCommonReq
     * <p/>
     * <p>返回对象</p>
     * @see FQueryHoldRes
     */
    @ResponseBody
    @RequestMapping(value = "/queryHold", method = RequestMethod.GET)
    public Map queryHold(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            return CommonStringUtils.mapReturn(this.tradeService.queryHold(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询今日委托列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryEntrust</p>
     * <p>请求方式：GET</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 委托列表
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryTradeCommonReq
     * <p/>
     * <p>返回对象</p>
     * @see FQueryEntrustRes
     */
    @ResponseBody
    @RequestMapping(value = "/queryEntrust", method = RequestMethod.GET)
    public Map queryEntrust(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            return CommonStringUtils.mapReturn(this.tradeService.queryEntrust(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询今日成交列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryMatch</p>
     * <p>请求方式：GET</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 成交列表
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryTradeCommonReq
     * <p/>
     * <p>返回对象</p>
     * @see FQueryMatchRes
     */
    @ResponseBody
    @RequestMapping(value = "/queryMatch", method = RequestMethod.GET)
    public Map queryMatch(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            return CommonStringUtils.mapReturn(this.tradeService.queryMatch(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询资产</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryFunds</p>
     * <p>请求方式：GET</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 南交所资产详情
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryTradeCommonReq
     * <p/>
     * <p>返回对象</p>
     * @see FQueryFundsRes
     */
    @ResponseBody
    @RequestMapping(value = "/queryFunds", method = RequestMethod.GET)
    public FQueryFundsRes queryFunds(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            return this.tradeService.queryFunds(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询商品交易数量</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryProdSingle</p>
     * <p>请求方式：GET</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param prod 商品代码
     * @return 最大可买 最大可卖
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryProdSingleReq
     * <p/>
     * <p>返回对象</p>
     * @see FQueryProdSingleRes
     */
    @ResponseBody
    @RequestMapping(value = "/queryProdSingle", method = RequestMethod.GET)
    public FQueryProdSingleRes queryProdSingle(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "prod") String prod
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryProdSingleReq req = new FQueryProdSingleReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setProdCode(prod);
            return this.tradeService.queryProdSingle(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询资产</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryFundsSimple</p>
     * <p>请求方式：GET</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 南交所资产详情
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryTradeCommonReq
     * <p/>
     * <p>返回对象</p>
     * @see FQueryFundsRes
     */
    @ResponseBody
    @RequestMapping(value = "/queryFundsSimple", method = RequestMethod.GET)
    public FQueryFundsSimpleRes queryFundsSimple(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            return this.tradeService.queryFundsSimple(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询历史委托列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/query_history_entrust</p>
     * <p>请求方式：GET</p>
     *
     * @param exchange  交易所代码 NJS 南交所、SJS上金所
     * @param token     登录token
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param limit     每页显示条数，默认10条
     * @param page      当前页数，默认1
     * @throws Exception
     * @see FQueryHistoryEntrustRes
     */
    @RequestMapping(value = "query_history_entrust", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public FQueryHistoryEntrustReq queryHistoryEntrust(
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryHistoryEntrustReq req = new FQueryHistoryEntrustReq();
            req.setExchange(exchange);
            req.setUserId(userId);
            req.setStartDate(startDate);
            req.setEndDate(endDate);
            req.setLimit(limit);
            req.setStart((page - 1) * limit);
            return this.tradeService.queryHistoryEntrustList(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询委托整合列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryEntrustIntegrate</p>
     * <p>请求方式：GET</p>
     *
     * @param exchange  交易所代码 NJS 南交所、SJS上金所
     * @param token     登录token
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param limit     每页显示条数，默认10条
     * @param page      当前页数，默认1
     * @throws Exception
     * @see FQueryHistoryEntrustRes
     */
    @RequestMapping(value = "queryEntrustIntegrate", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public FQueryHistoryEntrustReq queryEntrustIntegrate(
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryHistoryEntrustReq req = new FQueryHistoryEntrustReq();
            req.setExchange(exchange);
            req.setUserId(userId);
            req.setStartDate(startDate);
            req.setEndDate(endDate);
            req.setLimit(limit);
            req.setStart((page - 1) * limit);
            return this.tradeService.queryEntrustIntegrate(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询历史交易列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/query_history_trade</p>
     * <p>请求方式：GET</p>
     *
     * @param exchange  交易所代码 NJS 南交所、SJS上金所
     * @param token     登录token
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param limit     每页显示条数，默认10条
     * @param page      当前页数，默认1
     * @throws Exception
     * @see FQueryHistoryTradeRes
     */
    @RequestMapping(value = "query_history_trade", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public FQueryHistoryTradeReq queryHistoryTrade(
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryHistoryTradeReq req = new FQueryHistoryTradeReq();
            req.setExchange(exchange);
            req.setUserId(userId);
            req.setStartDate(startDate);
            req.setEndDate(endDate);
            req.setLimit(limit);
            req.setStart((page - 1) * limit);
            return this.tradeService.queryHistoryTradeList(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询成交整合列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryTradeIntegrate</p>
     * <p>请求方式：GET</p>
     *
     * @param exchange  交易所代码 NJS 南交所、SJS上金所
     * @param token     登录token
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param limit     每页显示条数，默认10条
     * @param page      当前页数，默认1
     * @throws Exception
     * @see FQueryHistoryTradeRes
     */
    @RequestMapping(value = "queryTradeIntegrate", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public FQueryHistoryTradeReq queryTradeIntegrate(
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryHistoryTradeReq req = new FQueryHistoryTradeReq();
            req.setExchange(exchange);
            req.setUserId(userId);
            req.setStartDate(startDate);
            req.setEndDate(endDate);
            req.setLimit(limit);
            req.setStart((page - 1) * limit);
            return this.tradeService.queryTradeIntegrate(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询历史出入金列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/query_history_transfer</p>
     * <p>请求方式：GET</p>
     *
     * @param exchange  交易所代码 NJS 南交所、SJS上金所
     * @param token     登录token
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param limit     每页显示条数，默认10条
     * @param page      当前页数，默认1
     * @throws Exception
     * @see FQueryHistoryTransferRes
     */
    @RequestMapping(value = "query_history_transfer", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public FQueryHistoryTransferReq queryHistoryTransfer(
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryHistoryTransferReq req = new FQueryHistoryTransferReq();
            req.setExchange(exchange);
            req.setUserId(userId);
            req.setStartDate(startDate);
            req.setEndDate(endDate);
            req.setLimit(limit);
            req.setStart((page - 1) * limit);
            return this.tradeService.queryHistoryTransferList(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>交易所是否休市</p>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/isTrade</p>
     * <p>请求方式：GET</p>
     *
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return message NJS 休市 工作日9:00到次日6:00可交易
     * @return message SJS 休市 工作日9:00-11:30,13:00-15:30,20:00-2:30可交易
     * @remark status 1:可交易;2:休市;3:节假日
     * @throws Exception
     */
    @RequestMapping(value = "isTrade", method = RequestMethod.GET)
    @ResponseBody
    public Map isTrade(@RequestParam(value = "exchange") String exchange) throws Exception {
        try {
            return this.tradeService.isTrade(exchange);
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询交易套餐列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryPackageList</p>
     * <p>请求方式：GET</p>
     *
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 委托列表
     * <p/>
     * <p>返回对象</p>
     * @throws Exception
     * @see FQueryPackageListRes
     */
    @ResponseBody
    @RequestMapping(value = "/queryPackageList", method = RequestMethod.GET)
    public Map queryPackageList(
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            return CommonStringUtils.mapReturn(this.tradeService.queryPackageList(exchange));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询套餐信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryPackageInfo</p>
     * <p>请求方式：GET</p>
     *
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param token    登录token
     * @return 委托列表
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryTradeCommonReq
     * <p/>
     * <p>返回对象</p>
     * @see FQueryPackageInfoRes
     */
    @ResponseBody
    @RequestMapping(value = "/queryPackageInfo", method = RequestMethod.GET)
    public FQueryPackageInfoRes queryPackageInfo(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            return this.tradeService.queryPackageInfo(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询套餐申请记录</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/queryPackageHistory</p>
     * <p>请求方式：GET</p>
     *
     * @param exchange  交易所代码 NJS 南交所、SJS上金所
     * @param token     登录token
     * @param startDate 开始时间 20101010
     * @param endDate   结束时间 20101010
     * @return 委托列表
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryPackageHistoryReq
     * <p/>
     * <p>返回对象</p>
     * @see FQueryPackageHistoryRes
     */
    @ResponseBody
    @RequestMapping(value = "/queryPackageHistory", method = RequestMethod.GET)
    public Map queryPackageHistory(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryPackageHistoryReq req = new FQueryPackageHistoryReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setStartDate(startDate);
            req.setEndDate(endDate);
            return CommonStringUtils.mapReturn(this.tradeService.queryPackageHistory(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>购买套餐</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_trade/doPackageBuy</p>
     * <p>请求方式：POST</p>
     *
     * @param exchange  交易所代码 NJS 南交所、SJS上金所
     * @param token     登录token
     * @param packageId 套餐编号
     * @return 委托列表
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoPackageBuyReq
     */
    @ResponseBody
    @RequestMapping(value = "/doPackageBuy", method = RequestMethod.POST)
    public Map doPackageBuy(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "packageId") String packageId
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoPackageBuyReq req = new FDoPackageBuyReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setPackageId(packageId);
            this.tradeService.doPackageBuy(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }
}