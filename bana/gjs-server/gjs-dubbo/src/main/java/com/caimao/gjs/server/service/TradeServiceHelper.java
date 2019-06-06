package com.caimao.gjs.server.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.req.getui.FGetuiPushMessageReq;
import com.caimao.bana.api.entity.req.message.FPushMsgAddReq;
import com.caimao.bana.api.enums.EPushModel;
import com.caimao.bana.api.enums.EPushType;
import com.caimao.bana.api.enums.getui.EGetuiActionType;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.service.SmsService;
import com.caimao.bana.api.service.content.IMessageService;
import com.caimao.bana.api.service.getui.IGetuiService;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.bana.common.api.enums.gjs.EGJSExchange;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.common.api.utils.StringUtils;
import com.caimao.gjs.api.entity.GJSAccountEntity;
import com.caimao.gjs.api.entity.GJSProductEntity;
import com.caimao.gjs.api.entity.history.*;
import com.caimao.gjs.api.entity.req.*;
import com.caimao.gjs.api.entity.res.*;
import com.caimao.gjs.api.enums.EGJSTradeType;
import com.caimao.gjs.api.enums.EGJSUploadStatus;
import com.caimao.gjs.api.enums.ENJSBankNo;
import com.caimao.gjs.api.enums.ENJSOPENBANK;
import com.caimao.gjs.server.dao.*;
import com.caimao.gjs.server.trade.protocol.njs.entity.*;
import com.caimao.gjs.server.trade.protocol.njs.entity.res.*;
import com.caimao.gjs.server.trade.protocol.sjs.entity.*;
import com.caimao.gjs.server.trade.protocol.sjs.entity.res.*;
import com.caimao.gjs.server.utils.*;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.service.IHQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("tradeServiceHelper")
public class TradeServiceHelper {
    private static final Logger logger = LoggerFactory.getLogger(TradeServiceHelper.class);

    protected static List<String> tradeTimeList = new ArrayList<>();

    //身份证图片路径
    private String ID_CARD_FILE_PATH = "";

    @Resource
    private TradeServiceCCHelper tradeServiceCCHelper;
    @Resource
    RedisUtils redisUtils;
    @Resource
    private IHQService hqService;
    @Resource
    private IGetuiService getuiService;
    @Autowired
    private IMessageService messageService;
    @Resource
    private SmsService smsService;
    @Resource
    private IUserService userService;

    @Resource
    private GJSAccountDao gjsAccountDao;
    @Resource
    private GJSNJSHistoryEntrustDao gjsNJSHistoryEntrustDao;
    @Resource
    private GJSSJSHistoryEntrustDao gjsSJSHistoryEntrustDao;
    @Resource
    private GJSNJSHistoryTradeDao gjsNJSHistoryTradeDao;
    @Resource
    private GJSSJSHistoryTradeDao gjsSJSHistoryTradeDao;
    @Resource
    private GJSNJSHistoryTransferDao gjsNJSHistoryTransferDao;
    @Resource
    private GJSSJSHistoryTransferDao gjsSJSHistoryTransferDao;

    /**
     * 获取redis中token的key
     * @param exchange 交易所代码
     * @param traderId 交易员编号
     * @return token
     * @throws Exception
     */
    public String getTokenKey(String exchange, String traderId) throws Exception {
        return "traderToken_" + exchange + "_" + traderId;
    }

    /**
     * 获取redis中traderId的key
     * @param exchange 交易所代码
     * @return String
     * @throws Exception
     */
    public String getTraderIdKey(String exchange) throws Exception{
        return "traderId_" + exchange;
    }

    /**
     * 获取redis中userId的key
     * @param exchange 交易所代码
     * @return String
     * @throws Exception
     */
    public String getUserIdKey(String exchange) throws Exception{
        return "userId_" + exchange;
    }

    /**
     * 获取交易所
     * @param exchange 交易所代码
     * @return 交易所枚举
     * @throws Exception
     */
    public EGJSExchange getExchange(String exchange) throws Exception {
        EGJSExchange egjsExchange = EGJSExchange.findByCode(exchange);
        if (egjsExchange == null) throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        return egjsExchange;
    }

    /**
     * 设置登录token
     * @param exchange 交易所代码
     * @param traderId 交易员编号
     * @param token 登录token
     * @throws Exception
     */
    public void setToken(String exchange, String traderId, String token) throws Exception {
        redisUtils.set(0, this.getTokenKey(exchange, traderId), token, 9000);
    }

    /**
     * 获取登录token
     * @param exchange 交易所代码
     * @param traderId 交易员编号
     * @return String
     * @throws Exception
     */
    public String getToken(String exchange, String traderId) throws Exception {
        Object tokenObj = redisUtils.get(0, this.getTokenKey(exchange, traderId));
        String token = tokenObj == null ? "" : tokenObj.toString();
        if (token.equals("")) throw new CustomerException(EErrorCode.ERROR_CODE_300001.getMessage(), EErrorCode.ERROR_CODE_300001.getCode());
        return token;
    }

    /**
     * 注销交易所登录
     * @param exchange 交易所代码
     * @param traderId 交易员编号
     * @throws Exception
     */
    public void logoutToken(String exchange, String traderId) throws Exception{
        redisUtils.del(0, this.getTokenKey(exchange, traderId));
    }

    /**
     * 设置 userId对应的traderId
     * @param exchange 交易所代码
     * @param userId 用户编号
     * @param traderId 交易员编号
     * @throws Exception
     */
    public void setTraderId(String exchange, Long userId, String traderId) throws Exception{
        redisUtils.hSet(0, this.getTraderIdKey(exchange), userId.toString(), traderId);
    }

    /**
     * 获取用户相关的交易员编号
     * @param exchange 交易所代码
     * @param userId 用户编号
     * @return String
     * @throws Exception
     */
    public String getTraderId(String exchange, Long userId) throws Exception {
        if(userId == null || userId == 0) throw new CustomerException(EErrorCode.ERROR_CODE_100002.getMessage(), EErrorCode.ERROR_CODE_100002.getCode());
        Object traderIdObj = redisUtils.hGet(0, this.getTraderIdKey(exchange), userId.toString());
        String traderId = traderIdObj == null ? "" : traderIdObj.toString();
        if (traderId.equals("")) {
            throw new CustomerException(EErrorCode.ERROR_CODE_300003.getMessage(), EErrorCode.ERROR_CODE_300003.getCode());
        } else {
            return traderId;
        }
    }

    /**
     * 删除用户相关的交易员编号
     * @param exchange 交易所代码
     * @param userId 用户编号
     * @throws Exception
     */
    public void delTraderId(String exchange, Long userId) throws Exception{
        if(userId == null || userId == 0) throw new CustomerException(EErrorCode.ERROR_CODE_100002.getMessage(), EErrorCode.ERROR_CODE_100002.getCode());
        redisUtils.hDel(0, this.getTraderIdKey(exchange), userId.toString());
    }

    /**
     * 设置 traderId对应的userId
     * @param exchange 交易所代码
     * @param traderId 交易员编号
     * @param userId 用户编号
     * @throws Exception
     */
    public void setUserId(String exchange, String traderId, Long userId) throws Exception{
        redisUtils.hSet(0, this.getUserIdKey(exchange), traderId, userId.toString());
    }

    /**
     * 获取用户id根据userId
     * @param exchange 交易所代码
     * @param traderId 交易员编号
     * @return 用户编号
     * @throws Exception
     */
    public Long getUserId(String exchange, String traderId) throws Exception {
        if(traderId == null || traderId.equals("")) throw new CustomerException(EErrorCode.ERROR_CODE_100002.getMessage(), EErrorCode.ERROR_CODE_100002.getCode());
        Object userIdObj = redisUtils.hGet(0, this.getUserIdKey(exchange), traderId);
        Long userId = userIdObj == null ? 0 : Long.parseLong(userIdObj.toString());
        if (userId == 0) {
            throw new CustomerException(EErrorCode.ERROR_CODE_300003.getMessage(), EErrorCode.ERROR_CODE_300003.getCode());
        } else {
            return userId;
        }
    }

    /**
     * 删除 traderId对应的userId
     * @param exchange 交易所代码
     * @param traderId 交易员编号
     * @throws Exception
     */
    public void delUserId(String exchange, String traderId) throws Exception{
        if(traderId == null || traderId.equals("")) throw new CustomerException(EErrorCode.ERROR_CODE_100002.getMessage(), EErrorCode.ERROR_CODE_100002.getCode());
        redisUtils.hDel(0, this.getUserIdKey(exchange), traderId);
    }

    /**
     * 设置商品列表缓存
     * @param key 缓存key
     * @param data 缓存的数据
     * @throws Exception
     */
    public void setGoodsCache(String key, List<GJSProductEntity> data) throws Exception {
        LinkedHashMap<String, Map<String, Object>> productMap = new LinkedHashMap<>();
        if (data != null) {
            for (GJSProductEntity product : data) {
                Map<String, Object> subData = new LinkedHashMap<>();
                Field[] field = product.getClass().getDeclaredFields();
                for (Field aField : field) {
                    String name = aField.getName();
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    Method method = product.getClass().getMethod("get" + name);
                    subData.put(aField.getName(), method.invoke(product));
                    subData.put("pinyinQ", StringUtils.toPinyin(product.getProdName(), false, false));
                    subData.put("pinyinJ", StringUtils.toPinyin(product.getProdName(), true, false));
                    subData.put("pinyinS", StringUtils.toPinyin(product.getProdName(), false, false).substring(0, 1));
                }
                productMap.put(product.getProdCode(), subData);
            }
        }
        redisUtils.set(0, key, JSON.toJSONString(productMap, SerializerFeature.BrowserCompatible), 0);
    }

    /**
     * 获取商品列表缓存
     * @param key 缓存key
     * @return HashMap
     * @throws Exception
     */
    public LinkedHashMap<String, Map<String, Object>> getGoodsCache(String key) throws Exception {
        Object goodsCacheObj = redisUtils.get(0, key);
        if (goodsCacheObj != null) {
            String goodsCacheStr = goodsCacheObj.toString();
            try {
                return JSON.parseObject(goodsCacheStr, LinkedHashMap.class);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取商品的名称
     * @param prodCode 商品代码
     * @param exchange 交易所代码
     * @return String
     * @throws Exception
     */
    public String getGoodsName(String prodCode, String exchange) throws Exception {
        String goodsName = "";
        try {
            if (exchange.equals(EGJSExchange.NJS.getCode())) {
                LinkedHashMap<String, Map<String, Object>> goodsList = this.getGoodsCache("njs_goods");
                if (goodsList != null && goodsList.containsKey(prodCode)) {
                    return goodsList.get(prodCode).get("prodName").toString();
                }
            } else if (exchange.equals(EGJSExchange.SJS.getCode())) {
                Map<String, String> goodsMap = new HashMap<>();
                goodsMap.put("201", "Au99.99");
                goodsMap.put("202", "Au99.95");
                goodsMap.put("203", "Pt99.95");
                goodsMap.put("204", "Au50g");
                goodsMap.put("206", "Ag99.9");
                goodsMap.put("207", "Au100g");
                if(goodsMap.containsKey(prodCode)) return goodsMap.get(prodCode);

                LinkedHashMap<String, Map<String, Object>> goodsList = this.getGoodsCache("sjs_goods");
                if (goodsList != null && goodsList.containsKey(prodCode)) {
                    return goodsList.get(prodCode).get("prodName").toString();
                }
            }
        } catch (Exception e) {
            logger.error("获取商品名称失败，code " + prodCode + "失败原因{}", e);
        }
        return goodsName;
    }

    /**
     * 格式化输出时间
     * @param date 日期
     * @param time 时间
     * @return String
     * @throws Exception
     */
    public String getFormatDatetime(String date, String time) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Date formatDate = sdf.parse(date + time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf2.format(formatDate);
        } catch (Exception e) {
            return "";
        }
    }

    public String getFormatTime(String time) throws Exception{
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            Date formatDate = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            return sdf2.format(formatDate);
        } catch (Exception e) {
            return time;
        }
    }

    public String getFormatDate(String time) throws Exception{
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date formatDate = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            return sdf2.format(formatDate);
        } catch (Exception e) {
            return time;
        }
    }

    public String getFormatDataToday() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 发送短信
     * @param userId 用户编号
     * @param smsContent 短信内容
     * @throws Exception
     */
    public void sendSms(Long userId, String smsContent) throws Exception{
        try{
            TpzUserEntity user = userService.getById(userId);
            smsService.doSendSmsCustom(user.getMobile(), smsContent);
        }catch(Exception e){
            logger.error("发送短信失败 {}", e);
        }
    }

    /**
     * 发送站内信
     * @param userId 用户编号
     * @param ePushType 类型
     * @param msg 内容
     * @throws Exception
     */
    public void sendMsg(Long userId, EPushType ePushType, String msg) throws Exception{
        try {
            FPushMsgAddReq pushMsgAddReq = new FPushMsgAddReq();
            pushMsgAddReq.setPushModel(EPushModel.GJS.getCode());
            pushMsgAddReq.setPushType(ePushType.getCode());
            pushMsgAddReq.setPushMsgKind("1");
            pushMsgAddReq.setPushMsgTitle(msg);
            pushMsgAddReq.setPushMsgContent(msg);
            pushMsgAddReq.setPushMsgDigest(msg);
            pushMsgAddReq.setPushExtend("");
            pushMsgAddReq.setPushUserId(userId.toString());
            pushMsgAddReq.setIsRead("0");
            this.messageService.addPushMsg(pushMsgAddReq);
        } catch (Exception e) {
            logger.error("发送站内信失败 {}", e);
        }
    }

    /**
     * 发送个推消息
     * @param userId 用户编号
     * @param eGetuiActionType 个推类型
     * @param source 来源
     * @param title 标题
     * @param content 内容
     * @throws Exception
     */
    public void sendPushMsg(Long userId, EGetuiActionType eGetuiActionType, String source, String title, String content) throws Exception{
        try {
            FGetuiPushMessageReq getuiPushMessageReq = new FGetuiPushMessageReq();
            getuiPushMessageReq.setUserId(userId);
            getuiPushMessageReq.setActionType(eGetuiActionType.getValue());
            getuiPushMessageReq.setSource(source);
            getuiPushMessageReq.setTitle(title);
            getuiPushMessageReq.setContent(content);
            this.getuiService.pushMessageToSingle(getuiPushMessageReq);
        } catch (Exception e) {
            logger.error("发送个推失败 {}", e);
        }
    }

    /**
     * 判断是否可以出入金
     * @param exchange 交易所编号
     * @param bankId 银行编号
     * @throws Exception
     */
    public void queryTimeCanTransfer(String exchange, String bankId) throws Exception{
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        if(exchange.equals(EGJSExchange.NJS.getCode())){
            if(week < 1 || week > 5) throw new CustomerException(EErrorCode.ERROR_CODE_300016.getMessage(), EErrorCode.ERROR_CODE_300016.getCode());
            if(bankId.equals(ENJSBankNo.CNCB.getCode())){
                if(hour < 9 || hour >= 22) throw new CustomerException(EErrorCode.ERROR_CODE_300018.getMessage(), EErrorCode.ERROR_CODE_300018.getCode());
            }else{
                if(hour < 9 || hour > 15 || (hour == 15 && minute > 30)) throw new CustomerException(EErrorCode.ERROR_CODE_300016.getMessage(), EErrorCode.ERROR_CODE_300016.getCode());
            }
        }else if(exchange.equals(EGJSExchange.SJS.getCode())){
            if(week == 0 || (hour > 4 && hour < 8) || (hour > 16 && hour < 19) || (hour == 8 && minute < 30) || (week == 6 && hour > 4)) throw new CustomerException(EErrorCode.ERROR_CODE_300017.getMessage(), EErrorCode.ERROR_CODE_300017.getCode());
        }
    }

    /**
     * 南交所登录
     * @param traderId 交易员编号
     * @param tradePwd 登录密码
     * @throws Exception
     */
    public void doNJSTradeLogin(String traderId, String tradePwd) throws Exception {
        FNJSDoTradeLoginRes res = tradeServiceCCHelper.doNJSTradeLoginCC(traderId, tradePwd);
        this.setToken(EGJSExchange.NJS.getCode(), traderId, res.getTOKEN());
    }

    /**
     * 上金所登录
     * @param traderId 交易员编号
     * @param tradePwd 登录密码
     * @throws Exception
     */
    public void doSJSTradeLogin(String traderId, String tradePwd) throws Exception {
        tradeServiceCCHelper.doSJSTradeLoginCC(traderId, tradePwd);
        this.setToken(EGJSExchange.SJS.getCode(), traderId, traderId);
    }

    /**
     * 南交所委托下单 限价单
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @return String
     * @throws Exception
     */
    public String doNJSEntrustLimit(String token, String traderId, FDoEntrustReq req) throws Exception {
        FNJSDoEntrustLimitRes res = tradeServiceCCHelper.doNJSEntrustLimitCC(token, traderId, req);
        return res.getDATAS().get(0).getSERIALNO();
    }

    /**
     * 南交所委托下单 市价单
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @return String
     * @throws Exception
     */
    public String doNJSEntrustMarket(String token, String traderId, FDoEntrustReq req) throws Exception {
        FNJSDoEntrustMarketRes res = tradeServiceCCHelper.doNJSEntrustMarketCC(token, traderId, req);
        return res.getDATAS().get(0).getSERIALNO();
    }

    /**
     * 上金所委托下单 限价单
     * @param token 登录token
     * @param req 请求对象
     * @return String
     * @throws Exception
     */
    public String doSJSEntrustLimit(String token, FDoEntrustReq req) throws Exception {
        FSJSDoEntrustLimitRes res = tradeServiceCCHelper.doSJSEntrustLimitCC(token, req);
        return res.getRecord().get(0).getLocal_order_no();
    }

    /**
     * 南交所委托撤销
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @throws Exception
     */
    public void doNJSEntrustCancel(String token, String traderId, FDoEntrustCancelReq req) throws Exception {
        tradeServiceCCHelper.doNJSEntrustCancelCC(token, traderId, req);
    }

    /**
     * 上金所委托撤销
     * @param token 登录token
     * @param req 请求对象
     * @throws Exception
     */
    public void doSJSEntrustCancel(String token, FDoEntrustCancelReq req) throws Exception {
        tradeServiceCCHelper.doSJSEntrustCancelCC(token, req);
    }

    /**
     * 南交所止盈止损下单
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @return String
     * @throws Exception
     */
    public String doNJSFullStop(String token, String traderId, FDoFullStopReq req) throws Exception {
        FNJSDoFullStopRes res = tradeServiceCCHelper.doNJSFullStopCC(token, traderId, req);
        return res.getDATAS().get(0).getSERIALNO();
    }

    /**
     * 南交所止盈止损撤单
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @throws Exception
     */
    public void doNJSFullStopCancel(String token, String traderId, FDoFullStopCancelReq req) throws Exception {
        tradeServiceCCHelper.doNJSFullStopCancelCC(token, traderId, req);
    }

    /**
     * 南交所止盈止损查询
     * @param token 登录token
     * @param traderId 用户编号
     * @return List
     * @throws Exception
     */
    public List<FQueryFullStopRes> queryNJSFullStop(String token, String traderId) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        List<FQueryFullStopRes> result = new ArrayList<>();
        FNJSQueryFullStopRes res = tradeServiceCCHelper.queryNJSFullStopCC(token, traderId);
        if (res.getDATAS() != null && res.getDATAS().size() > 0) {
            for (NJSQueryFullStopEntity entity : res.getDATAS()) {
                FQueryFullStopRes resResult = new FQueryFullStopRes();
                resResult.setApplyDate((entity.getFDATE() == null || entity.getFDATE().equals(""))?dateFormat.format(new Date()):entity.getFDATE());
                resResult.setOrderNo(entity.getBILLNO());
                resResult.setProdCode(entity.getWAREID());
                resResult.setProdName(this.getGoodsName(entity.getWAREID(), EGJSExchange.NJS.getCode()));
                resResult.setTradeType(entity.getBUYORSAL());
                resResult.setUpPrice(entity.getUPPRICE());
                resResult.setDownPrice(entity.getDOWNPRICE());
                resResult.setAmount(entity.getNUM());
                resResult.setState(entity.getSTATE());
                resResult.setSetCreateTime(this.getFormatTime(entity.getSETTIME()));
                resResult.setCancelTime(this.getFormatTime(entity.getCANCELTIME()));
                resResult.setExecTime(this.getFormatTime(entity.getEXECTIME()));
                resResult.setValidDate(entity.getVALIDDATE());
                resResult.setEntrustNo(entity.getSERIALNO());
                resResult.setEntrustNum(entity.getSERIALNUM());
                result.add(resResult);
            }
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * 南交所条件单下单
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @return String
     * @throws Exception
     */
    public String doNJSCondition(String token, String traderId, FDoConditionReq req) throws Exception {
        FNJSDoConditionRes res = tradeServiceCCHelper.doNJSConditionCC(token, traderId, req);
        return res.getDATAS().get(0).getSERIALNO();
    }

    /**
     * 南交所条件单撤销
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @throws Exception
     */
    public void doNJSConditionCancel(String token, String traderId, FDoConditionCancelReq req) throws Exception {
        tradeServiceCCHelper.doNJSConditionCancelCC(token, traderId, req);
    }

    /**
     * 南交所条件单查询
     * @param token 登录token
     * @param traderId 用户编号
     * @return List
     * @throws Exception
     */
    public List<FQueryConditionRes> queryNJSCondition(String token, String traderId) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        List<FQueryConditionRes> result = new ArrayList<>();
        FNJSQueryConditionRes res = tradeServiceCCHelper.queryNJSConditionCC(token, traderId);
        if (res.getDATAS() != null && res.getDATAS().size() > 0) {
            for (NJSQueryConditionEntity entity : res.getDATAS()) {
                FQueryConditionRes resResult = new FQueryConditionRes();
                resResult.setApplyDate((entity.getFDATE() == null || entity.getFDATE().equals(""))?dateFormat.format(new Date()):entity.getFDATE());
                resResult.setOrderNo(entity.getBILLNO());
                resResult.setProdCode(entity.getWAREID());
                resResult.setProdName(this.getGoodsName(entity.getWAREID(), EGJSExchange.NJS.getCode()));
                resResult.setTradeType(entity.getBUYORSAL());
                resResult.setPrice(entity.getPRICE());
                resResult.setAmount(entity.getNUM());
                resResult.setTouchPrice(entity.getTOUCHPRICE());
                resResult.setCondition(entity.getCONDITION());
                resResult.setState(entity.getSTATE());
                resResult.setSetCreateTime(this.getFormatTime(entity.getSETTIME()));
                resResult.setCancelTime(this.getFormatTime(entity.getCANCELTIME()));
                resResult.setExecTime(this.getFormatTime(entity.getEXECTIME()));
                resResult.setValidDate(entity.getVALIDDATE());
                resResult.setEntrustNo(entity.getSERIALNO());
                result.add(resResult);
            }
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * 南交所当日委托查询
     * @param token 登录token
     * @param traderId 用户编号
     * @return List
     * @throws Exception
     */
    public List<FQueryEntrustRes> queryNJSEntrust(String token, String traderId, Boolean showCancel) throws Exception {
        List<FQueryEntrustRes> result = new ArrayList<>();
        FNJSQueryEntrustRes res = tradeServiceCCHelper.queryNJSEntrustCC(token, traderId);
        if (res.getDATAS() != null && res.getDATAS().size() > 0) {
            for (NJSQueryEntrustEntity entity : res.getDATAS()) {
                if(!showCancel && entity.getCSTATUS().equals("D")) continue;
                FQueryEntrustRes resResult = new FQueryEntrustRes();
                resResult.setOrderNo(entity.getSERIALNO());
                resResult.setProdCode(entity.getWAREID());
                resResult.setProdName(this.getGoodsName(entity.getWAREID(), EGJSExchange.NJS.getCode()));
                resResult.setTradeType(entity.getBUYORSAL());
                if(entity.getPRICE().equals("0") || entity.getPRICE().equals("0.00")){
                    entity.setPRICE("市价");
                }
                resResult.setPrice(entity.getPRICE());
                resResult.setAmount(entity.getNUM());
                resResult.setProcessedAmount(entity.getCONTNUM());
                resResult.setBailMoney(entity.getBAILMONEY());
                resResult.setFeeMoney(entity.getTMPMONEY());
                resResult.setState(entity.getCSTATUS());
                resResult.setCreateTime(showCancel?this.getFormatDataToday() + " " + this.getFormatTime(entity.getTIME()):this.getFormatTime(entity.getTIME()));
                resResult.setCancelTime(showCancel?this.getFormatDataToday() + " " + this.getFormatTime(entity.getSCANCLETIME()):this.getFormatTime(entity.getSCANCLETIME()));
                resResult.setIsForce((entity.getORDERSTY() != null && entity.getORDERSTY().equals("151"))?"1":"0");
                result.add(resResult);
            }
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * 上金所当日委托查询
     * @param token 登录token
     * @return List
     * @throws Exception
     */
    public List<FQueryEntrustRes> querySJSEntrust(String token, Boolean showCancel) throws Exception {
        List<FQueryEntrustRes> result = new ArrayList<>();
        FSJSQueryEntrustRes res = tradeServiceCCHelper.querySJSEntrustCC(token);
        if (res.getRecord() != null && res.getRecord().size() > 0) {
            for (SJSQueryEntrustEntity entity : res.getRecord()) {
                if(!showCancel && (entity.getEntr_stat().equals("d") || entity.getEntr_stat().equals("3"))) continue;
                FQueryEntrustRes resResult = new FQueryEntrustRes();
                resResult.setOrderNo(entity.getOrder_no());
                resResult.setProdCode(entity.getProd_code());
                resResult.setProdName(this.getGoodsName(entity.getProd_code(), EGJSExchange.SJS.getCode()));
                resResult.setTradeType(entity.getExch_type());
                resResult.setPrice(entity.getEntr_price());
                resResult.setAmount(entity.getEntr_amt());
                resResult.setProcessedAmount(String.valueOf(Integer.parseInt(entity.getEntr_amt()) - Integer.parseInt(entity.getUnmatch_amt())));
                resResult.setBailMoney("");
                resResult.setFeeMoney("");
                resResult.setState(entity.getEntr_stat());
                resResult.setCreateTime(showCancel?this.getFormatDataToday() + " " + this.getFormatTime(entity.getEntr_time()):this.getFormatTime(entity.getEntr_time()));
                resResult.setCancelTime(showCancel?this.getFormatDataToday() + " " + this.getFormatTime(entity.getCancel_time()):this.getFormatTime(entity.getCancel_time()));
                resResult.setIsForce((entity.getEntr_term_type() != null && entity.getEntr_term_type().equals("12"))?"1":"0");
                result.add(resResult);
            }
        }
        return result;
    }

    /**
     * 南交所查询当日成交
     * @param token 登录token
     * @param traderId 交易员编号
     * @return List
     * @throws Exception
     */
    public List<FQueryMatchRes> queryNJSMatch(String token, String traderId) throws Exception{
        List<FQueryMatchRes> result = new ArrayList<>();
        FNJSQueryMatchYesRes res = tradeServiceCCHelper.queryNJSMatchCC(token, traderId);
        if (res.getDATAS() != null && res.getDATAS().size() > 0) {
            for (NJSQueryMatchYesEntity entity : res.getDATAS()) {
                FQueryMatchRes resResult = new FQueryMatchRes();
                resResult.setOrderNo(entity.getSERIALNO());
                resResult.setSerialNo(entity.getCONTNO());
                resResult.setProdCode(entity.getWAREID());
                resResult.setProdName(this.getGoodsName(entity.getWAREID(), EGJSExchange.NJS.getCode()));
                resResult.setTradeType(entity.getBUYORSAL());
                resResult.setPrice(entity.getCONPRICE());
                resResult.setAmount(entity.getCONTNUM());
                resResult.setFeeMoney(entity.getTMPMONEY());
                resResult.setProcessedTime(this.getFormatDataToday() + " " + this.getFormatTime(entity.getFTIME()));
                resResult.setTotalMoney(entity.getCONTQTY());
                resResult.setIsForce((entity.getORDERSTY() != null && entity.getORDERSTY().equals("151"))?"1":"0");
                result.add(resResult);
            }
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * 上金所查询当日成交
     * @param token 登录token
     * @return List
     * @throws Exception
     */
    public List<FQueryMatchRes> querySJSMatch(String token) throws Exception{
        List<FQueryMatchRes> result = new ArrayList<>();
        FSJSQueryMatchRes res = tradeServiceCCHelper.querySJSMatchCC(token);
        if (res.getRecord() != null && res.getRecord().size() > 0) {
            for (SJSQueryMatchEntity entity : res.getRecord()) {
                FQueryMatchRes resResult = new FQueryMatchRes();
                resResult.setOrderNo(entity.getLocal_order_no());
                resResult.setSerialNo(entity.getMatch_no());
                resResult.setProdCode(entity.getProd_code());
                resResult.setProdName(this.getGoodsName(entity.getProd_code(), EGJSExchange.SJS.getCode()));
                resResult.setTradeType(entity.getExch_type());
                resResult.setPrice(entity.getMatch_price());
                resResult.setAmount(entity.getMatch_amount());
                resResult.setFeeMoney((entity.getExch_fare().length() > 0 && entity.getExch_fare().substring(0, 1).equals("."))?"0" + entity.getExch_fare():entity.getExch_fare());
                resResult.setProcessedTime(this.getFormatDataToday() + " " + this.getFormatTime(entity.getExch_time()));
                resResult.setTotalMoney(entity.getExch_bal());
                resResult.setOffsetFlag(entity.getOffset_flag());
                resResult.setIsForce((entity.getTerm_type() != null && entity.getTerm_type().equals("12"))?"1":"0");
                result.add(resResult);
            }
        }
        return result;
    }

    /**
     * 所持仓查询
     * @param token 登录token
     * @param traderId 用户编号
     * @return List
     * @throws Exception
     */
    public List<FQueryHoldRes> queryNJSHold(String token, String traderId) throws Exception {
        List<FQueryHoldRes> result = new ArrayList<>();
        FNJSQueryHoldRes res = tradeServiceCCHelper.queryNJSHoldCC(token, traderId);
        if (res.getDATAS() != null && res.getDATAS().size() > 0) {
            List<FQueryHoldRes> subHold = this.formatNJSHold(res);
            if(subHold != null) result.addAll(subHold);
        }
        return result;
    }

    /**
     * 处理南交所持仓数据
     * @param res 数据源
     * @return List<FQueryHoldRes>
     * @throws Exception
     */
    private List<FQueryHoldRes> formatNJSHold(FNJSQueryHoldRes res) throws Exception{
        try{
            List<FQueryHoldRes> result = new ArrayList<>();
            for (NJSQueryHoldEntity entity : res.getDATAS()) {
                FQueryHoldRes resResult = new FQueryHoldRes();
                resResult.setProdCode(entity.getWAREID());
                resResult.setProdName(entity.getSWARENAME());
                resResult.setAmount(new BigDecimal(entity.getGOODSNUM()).subtract(new BigDecimal(entity.getRHNUMBER())).abs().toString());
                resResult.setSurplus(entity.getCONSULTFLAT());
                resResult.setSurplusRate(entity.getFLATSCALE());
                resResult.setCostsPrice(entity.getCONSULTCOST());
                resResult.setCostsValue(new BigDecimal(entity.getCONSULTMARKVAL()).subtract(new BigDecimal(entity.getCONSULTFLAT())).setScale(2, BigDecimal.ROUND_DOWN).toString());
                resResult.setMarketValue(entity.getCONSULTMARKVAL());
                String tradeType = EGJSTradeType.NJS_BUY.getCode();
                if(new BigDecimal(entity.getGOODSNUM()).compareTo(new BigDecimal(entity.getRHNUMBER())) < 0){
                    tradeType = EGJSTradeType.NJS_SELL.getCode();
                }
                resResult.setTradeType(tradeType);
                resResult.setNewPrice(entity.getNEWPRICE());
                BigDecimal amount = new BigDecimal(entity.getGOODSNUM()).subtract(new BigDecimal(entity.getRHNUMBER()));
                resResult.setUseAmount(amount.abs().toString());

                BigDecimal cost = new BigDecimal(entity.getCONSULTCOST());
                BigDecimal feeAdd = new BigDecimal("1").add(new BigDecimal(Constants.NJS_TRADE_FEE)).setScale(6, BigDecimal.ROUND_DOWN);
                BigDecimal feeSub = new BigDecimal("1").subtract(new BigDecimal(Constants.NJS_TRADE_FEE)).setScale(6, BigDecimal.ROUND_DOWN);
                BigDecimal protectPrice;
                if(amount.compareTo(new BigDecimal(0)) > 0){
                    protectPrice = cost.multiply(feeAdd).setScale(6, BigDecimal.ROUND_DOWN).divide(feeSub, 2, BigDecimal.ROUND_DOWN);
                }else{
                    protectPrice = cost.multiply(feeSub).setScale(6, BigDecimal.ROUND_DOWN).divide(feeAdd, 2, BigDecimal.ROUND_DOWN);
                }
                resResult.setProtectPrice(protectPrice.toString());
                resResult.setExchange(EGJSExchange.NJS.getCode());
                result.add(resResult);
            }
            return result;
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 上金所持仓查询
     * @param token 登录token
     * @return List
     * @throws Exception
     */
    public List<FQueryHoldRes> querySJSHold(String token) throws Exception {
        List<FQueryHoldRes> result = new ArrayList<>();
        //查询延期持仓
        FSJSQueryHoldRes res = tradeServiceCCHelper.querySJSHoldCC(token);
        if (res.getRecord() != null && res.getRecord().size() > 0) {
            List<FQueryHoldRes> subHold = this.formatSJSHold(res);
            if(subHold != null) result.addAll(subHold);
        }

        //查询现货持仓
        FSJSQueryProdRes res2 = tradeServiceCCHelper.querySJSHoldProdCC(token);
        if (res2.getRecord() != null && res2.getRecord().size() > 0) {
            List<FQueryHoldRes> subHold = this.formatSJSHoldProd(res2);
            if(subHold != null){
                result.addAll(subHold);
            }
        }
        return result;
    }

    /**
     * 处理上金所所持仓数据（延期）
     * @param res 数据源
     * @return List<FQueryHoldRes>
     * @throws Exception
     */
    private List<FQueryHoldRes> formatSJSHold(FSJSQueryHoldRes res) throws Exception{
        try{
            List<FQueryHoldRes> result = new ArrayList<>();
            for (SJSQueryHoldEntity entity : res.getRecord()) {
                Snapshot ticker = hqService.queryTicker("SJS", entity.getProd_code());
                double newPrice = ticker == null?0:ticker.getLastPx();

                if(Integer.parseInt(entity.getLong_amt()) > 0){
                    FQueryHoldRes resResultBuy = new FQueryHoldRes();
                    resResultBuy.setProdCode(entity.getProd_code());
                    resResultBuy.setProdName(this.getGoodsName(entity.getProd_code(), EGJSExchange.SJS.getCode()));
                    resResultBuy.setAmount(entity.getLong_amt());
                    BigDecimal surplus = new BigDecimal(newPrice).subtract(new BigDecimal(entity.getLong_open_price())).multiply(new BigDecimal(entity.getLong_amt())).setScale(2, BigDecimal.ROUND_DOWN);
                    resResultBuy.setSurplus(surplus.toString());
                    BigDecimal surplusRate = surplus.divide(new BigDecimal(entity.getLong_open_price()).multiply(new BigDecimal(entity.getLong_amt())).setScale(2, BigDecimal.ROUND_DOWN), 4, BigDecimal.ROUND_DOWN);
                    resResultBuy.setSurplusRate(surplusRate.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).toString());
                    resResultBuy.setCostsPrice(entity.getLong_open_price());
                    resResultBuy.setCostsValue(new BigDecimal(entity.getLong_open_price()).multiply(new BigDecimal(entity.getLong_amt())).setScale(2, BigDecimal.ROUND_DOWN).toString());
                    resResultBuy.setMarketValue(new BigDecimal(newPrice).multiply(new BigDecimal(entity.getLong_amt())).setScale(2, BigDecimal.ROUND_DOWN).toString());
                    resResultBuy.setTradeType(EGJSTradeType.SJS_BUY_YQKDC.getCode());

                    resResultBuy.setNewPrice(String.valueOf(newPrice));
                    resResultBuy.setUseAmount(entity.getCan_use_long());

                    BigDecimal cost = new BigDecimal(entity.getLong_open_price());
                    BigDecimal feeAdd = new BigDecimal("1").add(new BigDecimal(Constants.NJS_TRADE_FEE)).setScale(6, BigDecimal.ROUND_DOWN);
                    BigDecimal feeSub = new BigDecimal("1").subtract(new BigDecimal(Constants.NJS_TRADE_FEE)).setScale(6, BigDecimal.ROUND_DOWN);
                    BigDecimal protectPrice = cost.multiply(feeAdd).setScale(6, BigDecimal.ROUND_DOWN).divide(feeSub, 2, BigDecimal.ROUND_DOWN);
                    resResultBuy.setProtectPrice(protectPrice.toString());
                    resResultBuy.setExchange(EGJSExchange.SJS.getCode());
                    result.add(resResultBuy);
                }

                if(Integer.parseInt(entity.getShort_amt()) > 0){
                    FQueryHoldRes resResultSell = new FQueryHoldRes();
                    resResultSell.setProdCode(entity.getProd_code());
                    resResultSell.setProdName(this.getGoodsName(entity.getProd_code(), EGJSExchange.SJS.getCode()));
                    resResultSell.setAmount(entity.getShort_amt());
                    BigDecimal surplus = new BigDecimal(entity.getShort_open_price()).subtract(new BigDecimal(newPrice)).multiply(new BigDecimal(entity.getShort_amt())).setScale(2, BigDecimal.ROUND_DOWN);
                    BigDecimal surplusRate = surplus.divide(new BigDecimal(entity.getShort_open_price()).multiply(new BigDecimal(entity.getShort_amt())).setScale(2, BigDecimal.ROUND_DOWN), 4, BigDecimal.ROUND_DOWN);
                    resResultSell.setSurplus(surplus.toString());
                    resResultSell.setSurplusRate(surplusRate.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).toString());
                    resResultSell.setCostsPrice(entity.getShort_open_price());
                    resResultSell.setCostsValue(new BigDecimal(entity.getShort_open_price()).multiply(new BigDecimal(entity.getShort_amt())).setScale(2, BigDecimal.ROUND_DOWN).toString());
                    resResultSell.setMarketValue(new BigDecimal(newPrice).multiply(new BigDecimal(entity.getShort_amt())).setScale(2, BigDecimal.ROUND_DOWN).toString());
                    resResultSell.setTradeType(EGJSTradeType.SJS_SELL_YQKKC.getCode());
                    resResultSell.setNewPrice(String.valueOf(newPrice));
                    resResultSell.setUseAmount(entity.getCan_use_short());

                    BigDecimal cost = new BigDecimal(entity.getShort_open_price());
                    BigDecimal feeAdd = new BigDecimal("1").add(new BigDecimal(Constants.SJS_TRADE_FEE)).setScale(6, BigDecimal.ROUND_DOWN);
                    BigDecimal feeSub = new BigDecimal("1").subtract(new BigDecimal(Constants.SJS_TRADE_FEE)).setScale(6, BigDecimal.ROUND_DOWN);
                    BigDecimal protectPrice = cost.multiply(feeSub).setScale(6, BigDecimal.ROUND_DOWN).divide(feeAdd, 2, BigDecimal.ROUND_DOWN);
                    resResultSell.setProtectPrice(protectPrice.toString());
                    resResultSell.setExchange(EGJSExchange.SJS.getCode());
                    result.add(resResultSell);
                }
            }
            return result;
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 处理上金所所持仓数据（现货）
     * @param res 数据源
     * @return List<FQueryHoldRes>
     * @throws Exception
     */
    private List<FQueryHoldRes> formatSJSHoldProd(FSJSQueryProdRes res) throws Exception{
        try{
            List<FQueryHoldRes> result = new ArrayList<>();

            for (SJSQueryProdEntity entity : res.getRecord()) {
                FQueryHoldRes resResultBuy = new FQueryHoldRes();

                resResultBuy.setProdCode(entity.getVariety_id());
                resResultBuy.setProdName(this.getGoodsName(entity.getVariety_id(), EGJSExchange.SJS.getCode()));
                resResultBuy.setAmount(entity.getCurr_amt());

                Snapshot ticker = hqService.queryTicker("SJS", entity.getVariety_id());
                double newPrice = ticker == null?0:ticker.getLastPx();
                BigDecimal costsValue = new BigDecimal(entity.getStorage_price()).multiply(new BigDecimal(entity.getCurr_amt())).setScale(2, BigDecimal.ROUND_DOWN);
                BigDecimal marketValue = new BigDecimal(newPrice).multiply(new BigDecimal(entity.getCurr_amt())).setScale(2, BigDecimal.ROUND_DOWN);
                BigDecimal surplus = marketValue.subtract(costsValue).setScale(2, BigDecimal.ROUND_DOWN);
                BigDecimal surplusRate = surplus.divide(costsValue, 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN);
                resResultBuy.setSurplus(surplus.toString());
                resResultBuy.setSurplusRate(surplusRate.toString());
                resResultBuy.setCostsPrice(entity.getStorage_price());
                resResultBuy.setCostsValue(costsValue.toString());
                resResultBuy.setMarketValue(marketValue.toString());
                resResultBuy.setTradeType(EGJSTradeType.SJS_BUY.getCode());
                resResultBuy.setNewPrice(String.valueOf(newPrice));
                resResultBuy.setUseAmount(entity.getCurr_can_use());

                BigDecimal cost = new BigDecimal(entity.getStorage_price());
                BigDecimal feeAdd = new BigDecimal("1").add(new BigDecimal(Constants.NJS_TRADE_FEE)).setScale(6, BigDecimal.ROUND_DOWN);
                BigDecimal feeSub = new BigDecimal("1").subtract(new BigDecimal(Constants.NJS_TRADE_FEE)).setScale(6, BigDecimal.ROUND_DOWN);
                BigDecimal protectPrice = cost.multiply(feeAdd).setScale(6, BigDecimal.ROUND_DOWN).divide(feeSub, 2, BigDecimal.ROUND_DOWN);
                resResultBuy.setProtectPrice(protectPrice.toString());
                resResultBuy.setExchange(EGJSExchange.SJS.getCode());
                result.add(resResultBuy);
            }
            return result;
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 南交所查询资产
     * @param token 登录token
     * @param traderId 用户编号
     * @return FQueryFundsRes
     * @throws Exception
     */
    public FQueryFundsRes queryNJSFunds(String token, String traderId) throws Exception {
        //查询资产
        FNJSQueryFundsRes res = tradeServiceCCHelper.queryNJSFundsCC(token, traderId);
        NJSQueryFundsEntity fundsEntity = res.getDATAS().get(0);
        //查询持仓
        BigDecimal holdCosts = new BigDecimal("0");
        BigDecimal holdMarket = new BigDecimal("0");
        BigDecimal totalGain = new BigDecimal("0");
        BigDecimal balance = new BigDecimal(fundsEntity.getBALANCEMONEY()).setScale(2, BigDecimal.ROUND_DOWN);
        List<FQueryHoldRes> holdResList = new ArrayList<>();
        FNJSQueryHoldRes res2 = tradeServiceCCHelper.queryNJSHoldCC(token, traderId);
        if (res2.getDATAS() != null && res2.getDATAS().size() > 0) {
            for (NJSQueryHoldEntity HoldEntity : res2.getDATAS()) {
                //统计持仓信息
                totalGain = totalGain.add(new BigDecimal(HoldEntity.getCONSULTFLAT())).setScale(2, BigDecimal.ROUND_DOWN);
                holdMarket = holdMarket.add(new BigDecimal(HoldEntity.getCONSULTMARKVAL())).setScale(2, BigDecimal.ROUND_DOWN);

                BigDecimal amount = new BigDecimal(HoldEntity.getGOODSNUM()).subtract(new BigDecimal(HoldEntity.getRHNUMBER())).abs();
                BigDecimal subCost = new BigDecimal(HoldEntity.getCONSULTCOST()).multiply(amount).setScale(2, BigDecimal.ROUND_DOWN);

                holdCosts = holdCosts.add(subCost).setScale(2, BigDecimal.ROUND_DOWN);

                if(new BigDecimal(HoldEntity.getGOODSNUM()).compareTo(new BigDecimal(HoldEntity.getRHNUMBER())) > 0){
                    balance = balance.add(subCost).setScale(2, BigDecimal.ROUND_DOWN);
                }else{
                    balance = balance.subtract(subCost).setScale(2, BigDecimal.ROUND_DOWN);
                }
            }
            List<FQueryHoldRes> subHold = this.formatNJSHold(res2);
            if(subHold != null) holdResList.addAll(subHold);
        }

        holdCosts = holdCosts.divide(new BigDecimal(10), 2, BigDecimal.ROUND_DOWN);

        //合并数据
        FQueryFundsRes fundsRes = new FQueryFundsRes();
        fundsRes.setEnableMoney(fundsEntity.getENABLEMONEY());
        fundsRes.setEnableOutMoney(fundsEntity.getENABLEOUTMONEY());
        fundsRes.setFreezeMoney(new BigDecimal(fundsEntity.getBAILMONEY()).abs().setScale(2, BigDecimal.ROUND_DOWN).toString());
        fundsRes.setBalanceMoney(fundsEntity.getBALANCEMONEY());
        fundsRes.setOccupancyMoney(holdCosts.toString());
        fundsRes.setHoldMarket(holdMarket.toString());
        fundsRes.setSafeRate(new BigDecimal(fundsEntity.getSAFERATE()).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN).toString());
        fundsRes.setTotalGain(totalGain.toString());
        fundsRes.setNetValue(fundsEntity.getASSETNETVALUE());
        fundsRes.setHoldList(holdResList);
        if(new BigDecimal(fundsEntity.getASSETNETVALUE()).compareTo(new BigDecimal("0")) == 0){
            fundsRes.setTransferAD("入金1000元，一天可赚100元");
        }
        return fundsRes;
    }

    /**
     * 上金所查询资产
     * @param token 登录token
     * @return FQueryFundsRes
     * @throws Exception
     */
    public FQueryFundsRes querySJSFunds(String token) throws Exception {
        //查询资产
        FSJSQueryFundsRes res = tradeServiceCCHelper.querySJSFundsCC(token);
        SJSQueryFundsEntity fundsEntity = res.getRecord().get(0);
        //查询持仓
        BigDecimal holdMarket = new BigDecimal("0");
        BigDecimal prodMoney = new BigDecimal("0");
        BigDecimal gainMoney = new BigDecimal("0");
        List<FQueryHoldRes> holdResList = new ArrayList<>();
        //延期持仓
        FSJSQueryHoldRes res2 = tradeServiceCCHelper.querySJSHoldCC(token);
        if (res2.getRecord() != null && res2.getRecord().size() > 0) {
            for (SJSQueryHoldEntity holdEntity : res2.getRecord()) {
                Snapshot ticker = hqService.queryTicker("SJS", holdEntity.getProd_code());
                double newPrice = ticker == null?0:ticker.getLastPx();
                //计算持仓成本(市值)
                BigDecimal longMarket = new BigDecimal(holdEntity.getLong_amt()).multiply(new BigDecimal(newPrice)).setScale(2, BigDecimal.ROUND_DOWN);
                BigDecimal shortMarket = new BigDecimal(holdEntity.getShort_amt()).multiply(new BigDecimal(newPrice)).setScale(2, BigDecimal.ROUND_DOWN);
                holdMarket = holdMarket.add(longMarket.add(shortMarket).setScale(2, BigDecimal.ROUND_DOWN)).setScale(2, BigDecimal.ROUND_DOWN);
            }
            List<FQueryHoldRes> subHold = this.formatSJSHold(res2);
            if(subHold != null) holdResList.addAll(subHold);
        }
        //现货持仓
        FSJSQueryProdRes res3 = tradeServiceCCHelper.querySJSHoldProdCC(token);
        if (res3.getRecord() != null && res3.getRecord().size() > 0) {
            for (SJSQueryProdEntity prodEntity : res3.getRecord()) {
                Snapshot ticker = hqService.queryTicker("SJS", prodEntity.getVariety_id());
                double newPrice = ticker == null?0:ticker.getLastPx();
                BigDecimal prodMarket = new BigDecimal(prodEntity.getCurr_amt()).multiply(new BigDecimal(newPrice)).setScale(2, BigDecimal.ROUND_DOWN);
                //市值
                holdMarket = holdMarket.add(prodMarket).setScale(2, BigDecimal.ROUND_DOWN);
                //成本
                prodMoney = prodMoney.add(new BigDecimal(prodEntity.getStorage_price()).multiply(new BigDecimal(prodEntity.getCurr_amt())).setScale(2, BigDecimal.ROUND_DOWN)).setScale(2, BigDecimal.ROUND_DOWN);
                //盈亏
                gainMoney = holdMarket.subtract(prodMoney).setScale(2, BigDecimal.ROUND_DOWN);
            }
            List<FQueryHoldRes> subHold = this.formatSJSHoldProd(res3);
            if(subHold != null) holdResList.addAll(subHold);
        }
        //合并数据
        FQueryFundsRes fundsRes = new FQueryFundsRes();
        fundsRes.setEnableMoney(fundsEntity.getCurr_can_use());
        fundsRes.setEnableOutMoney(fundsEntity.getCurr_can_get());
        fundsRes.setFreezeMoney(fundsEntity.getExch_froz_bal());
        fundsRes.setBalanceMoney(fundsEntity.getCurr_can_use());
        fundsRes.setHoldMarket(holdMarket.toString());
        prodMoney = prodMoney.add(new BigDecimal(fundsEntity.getPosi_margin()).abs()).setScale(2, BigDecimal.ROUND_DOWN);
        fundsRes.setOccupancyMoney(prodMoney.toString());
        gainMoney = gainMoney.add(new BigDecimal(fundsEntity.getFloat_surplus())).setScale(2, BigDecimal.ROUND_DOWN);
        fundsRes.setTotalGain(gainMoney.toString());

        BigDecimal total = new BigDecimal(fundsEntity.getCurr_can_use()).add(new BigDecimal(fundsEntity.getExch_froz_bal())).setScale(2, BigDecimal.ROUND_DOWN);
        total = total.add(prodMoney).setScale(2, BigDecimal.ROUND_DOWN);
        total = total.add(gainMoney).setScale(2, BigDecimal.ROUND_DOWN);
        fundsRes.setNetValue(total.toString());
        fundsRes.setSafeRate(this.querySafeSJS(token));
        fundsRes.setHoldList(holdResList);
        if(total.compareTo(new BigDecimal("0")) == 0){
            fundsRes.setTransferAD("入金10000元，一天可赚1000元");
        }
        return fundsRes;
    }

    /**
     * 南交所查询资产简单版
     * @param token 登录token
     * @param traderId 用户编号
     * @return FQueryFundsRes
     * @throws Exception
     */
    public FQueryFundsSimpleRes queryNJSFundsSimple(String token, String traderId) throws Exception {
        //查询资产
        FNJSQueryFundsRes res = tradeServiceCCHelper.queryNJSFundsCC(token, traderId);
        NJSQueryFundsEntity fundsEntity = res.getDATAS().get(0);

        FQueryFundsSimpleRes fundsRes = new FQueryFundsSimpleRes();
        BigDecimal holdCosts = new BigDecimal(fundsEntity.getASSETTOTALVALUE()).divide(new BigDecimal(10), 2, BigDecimal.ROUND_DOWN);
        BigDecimal enableMoney = new BigDecimal(fundsEntity.getENABLEMONEY()).subtract(holdCosts).setScale(2, BigDecimal.ROUND_DOWN);
        fundsRes.setEnableMoney(enableMoney.toString());
        fundsRes.setBalanceMoney(fundsEntity.getBALANCEMONEY());
        fundsRes.setEnableOutMoney(fundsEntity.getENABLEOUTMONEY());
        return fundsRes;
    }

    /**
     * 上金所查询资产简单版
     * @param token 登录token
     * @return FQueryFundsRes
     * @throws Exception
     */
    public FQueryFundsSimpleRes querySJSFundsSimple(String token) throws Exception {
        //查询资产
        FSJSQueryFundsRes res = tradeServiceCCHelper.querySJSFundsCC(token);
        SJSQueryFundsEntity fundsEntity = res.getRecord().get(0);

        FQueryFundsSimpleRes fundsRes = new FQueryFundsSimpleRes();
        fundsRes.setEnableMoney(fundsEntity.getCurr_can_use());
        fundsRes.setEnableOutMoney(fundsEntity.getCurr_can_get());
        fundsRes.setBalanceMoney(fundsEntity.getCurr_can_use());
        return fundsRes;
    }

    /**
     * 上金所查询安全率
     * @param token 登录token
     * @return String
     * @throws Exception
     */
    public String querySafeSJS(String token) throws Exception{
        try{
            FSJSQueryRiskRes res = tradeServiceCCHelper.querySafeSJSCC(token);
            return res.getRecord().get(0).getRisk_degree();
        }catch(Exception e){
            return "0";
        }
    }

    /**
     * 南交所修改资金密码
     * @param token 登录token
     * @param traderId 交易员编号
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @param bankId 银行编号
     * @throws Exception
     */
    public void doNJSModifyFundsPwd(String token, String traderId, String oldPwd, String newPwd, String bankId) throws Exception{
        tradeServiceCCHelper.doNJSModifyFundsPwdCC(token, traderId, oldPwd, newPwd, bankId);
    }

    /**
     * 上金所修改资金密码
     * @param token 登录token
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @throws Exception
     */
    public void doSJSModifyFundsPwd(String token, String oldPwd, String newPwd) throws Exception{
        tradeServiceCCHelper.doSJSModifyFundsPwdCC(token, oldPwd, newPwd);
    }

    /**
     * 南交所修改交易密码
     * @param token 登录token
     * @param traderId 用户编号
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @throws Exception
     */
    public void doNJSModifyTradePwd(String token, String traderId, String oldPwd, String newPwd) throws Exception{
        tradeServiceCCHelper.doNJSModifyTradePwdCC(token, traderId, oldPwd, newPwd);
        this.logoutToken(EGJSExchange.NJS.getCode(), traderId);
    }

    /**
     * 上金所修改交易密码
     * @param token 登录token
     * @param traderId 交易员编号
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @throws Exception
     */
    public void doSJSModifyTradePwd(String token, String traderId, String oldPwd, String newPwd) throws Exception{
        tradeServiceCCHelper.doSJSModifyTradePwdCC(token, oldPwd, newPwd);
        this.logoutToken(EGJSExchange.SJS.getCode(), traderId);
    }

    /**
     * 南交所重置资金密码
     * @param traderId 交易员编号
     * @param newPwd 密码
     * @param firmId 交易商编号
     * @throws Exception
     */
    public void doNJSResetFundsPwd(String traderId, String newPwd, String firmId) throws Exception{
        //管理员登录
        FNJSAdminDoLoginRes res = tradeServiceCCHelper.doNJSResetLoginCC();
        //重置
        tradeServiceCCHelper.doNJSResetFundsPwdCC(traderId, newPwd, firmId, res.getTOKEN());
    }

    /**
     * 上金所重置资金密码
     * @param traderId 交易员编号
     * @param newPwd 新密码
     * @throws Exception
     */
    public void doSJSResetFundsPwd(String traderId, String newPwd) throws Exception{
        tradeServiceCCHelper.doSJSResetFundsPwdCC(traderId, newPwd);
    }

    /**
     * 南交所重置交易密码
     * @param traderId 交易员编号
     * @param newPwd 密码
     * @param firmId 交易商编号
     * @throws Exception
     */
    public void doNJSResetTradePwd(String traderId, String newPwd, String firmId) throws Exception{
        //管理员登录
        FNJSAdminDoLoginRes res = tradeServiceCCHelper.doNJSResetLoginCC();
        //重置
        tradeServiceCCHelper.doNJSResetTradePwdCC(traderId, newPwd, firmId, res.getTOKEN());
    }

    /**
     * 上金所重置交易密码
     * @param traderId 交易员编号
     * @param newPwd 新密码
     * @throws Exception
     */
    public void doSJSResetTradePwd(String traderId, String newPwd) throws Exception{
        tradeServiceCCHelper.doSJSResetTradePwdCC(traderId, newPwd);
    }

    /**
     * 南交所入金
     * @param token 登录token
     * @param traderId 交易员编号
     * @param req 请求对象
     * @param gjsAccountEntity 用户实体
     * @throws Exception
     */
    public void doNJSTransferIn(String token, String traderId, FDoTransferInReq req, GJSAccountEntity gjsAccountEntity) throws Exception{
        tradeServiceCCHelper.doNJSTransferInCC(token, traderId, req, gjsAccountEntity);
    }

    /**
     * 上金所入金
     * @param token 登录token
     * @param req 请求对象
     * @throws Exception
     */
    public void doSJSTransferIn(String token, FDoTransferInReq req) throws Exception{
        tradeServiceCCHelper.doSJSTransferInCC(token, req);
    }

    /**
     * 南交所出金
     * @param token 登录token
     * @param traderId 交易员编号
     * @param req 请求对象
     * @param gjsAccountEntity 用户实体
     * @throws Exception
     */
    public void doNJSTransferOut(String token, String traderId, FDoTransferOutReq req, GJSAccountEntity gjsAccountEntity) throws Exception{
        tradeServiceCCHelper.doNJSTransferOutCC(token, traderId, req, gjsAccountEntity);
    }

    /**
     * 上金所出金
     * @param token 登录token
     * @param req 请求对象
     * @throws Exception
     */
    public void doSJSTransferOut(String token, FDoTransferOutReq req) throws Exception{
        tradeServiceCCHelper.doSJSTransferOutCC(token, req);
    }

    /**
     * 南交所查询今日出入金记录
     * @param token 登录token
     * @param traderId 交易员编号
     * @return List
     * @throws Exception
     */
    public List<FQueryTransferRes> queryNJSTransfer(String token, String traderId) throws Exception{
        List<FQueryTransferRes> result = new ArrayList<>();
        FNJSQueryTransferRes res = tradeServiceCCHelper.queryNJSTransferCC(token, traderId);
        if (res.getDATAS() != null && res.getDATAS().size() > 0) {
            for (NJSQueryTransferEntity entity : res.getDATAS()) {
                FQueryTransferRes resResult = new FQueryTransferRes();
                resResult.setSerialNo(entity.getBANKWATERID());
                resResult.setAmount(entity.getCHANGEMONEY());
                resResult.setAccessWay(entity.getCHANGETYPE());
                resResult.setState(entity.getSTYLE());
                resResult.setCreateTime(this.getFormatDatetime(entity.getDEALDATE(), entity.getDEALTIME()));
                result.add(resResult);
            }
        }
        return result;
    }

    /**
     * 上金所查询今日出入金记录
     * @param token 用户token
     * @return List
     * @throws Exception
     */
    public List<FQueryTransferRes> querySJSTransfer(String token) throws Exception{
        List<FQueryTransferRes> result = new ArrayList<>();
        FSJSQueryTransferRes res = tradeServiceCCHelper.querySJSTransferCC(token);
        if (res.getRecord() != null && res.getRecord().size() > 0) {
            for (SJSQueryTransferEntity entity : res.getRecord()) {
                FQueryTransferRes resResult = new FQueryTransferRes();
                resResult.setSerialNo(entity.getSerial_no());
                resResult.setAmount(new BigDecimal(entity.getExch_bal()).abs().toString());
                resResult.setAccessWay(entity.getAccess_way());
                resResult.setState(entity.getIn_account_flag());
                resResult.setCreateTime(entity.getExch_date());
                result.add(resResult);
            }
        }
        return result;
    }

    /**
     * 南交所开户
     * @param req 请求对象
     * @param mobile 用户手机号码
     * @param bankNo 银行实体
     * @param userTraderId 用户交易员编号
     * @return String
     * @throws Exception
     */
    public String doOpenAccountNJS(FDoOpenAccountNJSReq req, String mobile, ENJSBankNo bankNo, List<String> userTraderId, ENJSOPENBANK openBank) throws Exception{
        FNJSDoOpenAccountRes res = tradeServiceCCHelper.doOpenAccountNJSCC(req, mobile, bankNo, userTraderId, openBank);
        return res.getTRADERID();
    }

    /**
     * 上金所开户
     * @param req 请求对象
     * @param mobile 用户手机号码
     * @return String
     * @throws Exception
     */
    public String doOpenAccountSJS(FDoOpenAccountSJSReq req, String mobile) throws Exception{
        FSJSDoOpenAccountRes res = tradeServiceCCHelper.doOpenAccountSJSCC(req, mobile);
        return res.getRecord().get(0).getAcct_no();
    }

    /**
     * 更新南交所是否签约
     * @param accountEntity 开户表信息
     * @throws Exception
     */
    public void queryIsSignNJS(GJSAccountEntity accountEntity) throws Exception{
        FNJSQueryTradeUserIsSignRes res = tradeServiceCCHelper.queryIsSignNJSCC(accountEntity);
        if(res.getSIGNUP().equals("Y")){
            //变更记录
            GJSAccountEntity gjsAccountEntity = new GJSAccountEntity();
            gjsAccountEntity.setUserId(accountEntity.getUserId());
            gjsAccountEntity.setExchange(accountEntity.getExchange());
            gjsAccountEntity.setIsSign(1);
            gjsAccountDao.updateAccount(gjsAccountEntity);
        }
    }

    /**
     * 更新南交所是否签约推送
     * @param object 接收信息
     * @throws Exception
     */
    public void pushMsgIsSignNJS(Object object) throws Exception{
        String receiveData = object.toString();
        System.out.println("接收信息：" + receiveData);
        String feedBackType = receiveData.substring(5, 6);
        if(feedBackType.equals("C")){
            List<Map<String, String>> result = JSON.parseObject(receiveData.substring(19), List.class);
            if(result != null && result.size() > 0){
                for (Map<String, String> subRecord:result){
                    try{
                        logger.info("推送签约信息 trader_id:" + subRecord.get("TRADERID"));
                        Long userId = this.getUserId(EGJSExchange.NJS.getCode(), subRecord.get("TRADERID"));
                        if(userId != null && userId != 0){
                            GJSAccountEntity accountEntity = gjsAccountDao.queryAccountByUserId(userId, EGJSExchange.NJS.getCode());
                            if(accountEntity != null){
                                //变更记录
                                GJSAccountEntity gjsAccountEntity = new GJSAccountEntity();
                                gjsAccountEntity.setUserId(accountEntity.getUserId());
                                gjsAccountEntity.setExchange(accountEntity.getExchange());
                                gjsAccountEntity.setIsSign(1);
                                gjsAccountDao.updateAccount(gjsAccountEntity);
                            }
                        }
                    }catch (CustomerException e){
                        logger.error("南交所推送签约信息消息失败，原因" + e.getMessage());
                    }catch(Exception e){
                        logger.error("南交所推送签约信息消息失败", e);
                    }
                }
            }
        }
    }

    /**
     * 更新上金所是否签约
     * @param accountEntity 开户表信息
     * @throws Exception
     */
    public void queryIsSignSJS(GJSAccountEntity accountEntity) throws Exception{
        GJSAccountEntity gjsAccountEntity = new GJSAccountEntity();
        gjsAccountEntity.setUserId(accountEntity.getUserId());
        gjsAccountEntity.setExchange(accountEntity.getExchange());
        gjsAccountEntity.setTraderId(accountEntity.getTraderId());

        //处理身份证验证
        if(accountEntity.getIsSign() == 1){
            if(accountEntity.getUploadStatus().toString().equals(EGJSUploadStatus.UPLOAD_YES.getCode().toString())){
                //合并文件
                InputStream is = CommonUtils.mergerImg(this.ID_CARD_FILE_PATH + accountEntity.getCardPositive(), this.ID_CARD_FILE_PATH + accountEntity.getCardObverse());
                //上传文件
                tradeServiceCCHelper.doUploadImgSJSCC(is, accountEntity.getIdCard());
                gjsAccountEntity.setUploadStatus(EGJSUploadStatus.SUBMIT_YES.getCode());
                gjsAccountDao.updateAccount(gjsAccountEntity);
            }else if(accountEntity.getUploadStatus().toString().equals(EGJSUploadStatus.SUBMIT_YES.getCode().toString())){
                String state = tradeServiceCCHelper.queryUploadStatusSJSCC(accountEntity.getTraderId());
                if(state.equals("1")){
                    gjsAccountEntity.setUploadStatus(EGJSUploadStatus.ACCESS_SUCCESS.getCode());
                    gjsAccountDao.updateAccount(gjsAccountEntity);
                }else if(state.equals("2")){
                    gjsAccountEntity.setUploadStatus(EGJSUploadStatus.ACCESS_FAIL.getCode());
                    gjsAccountDao.updateAccount(gjsAccountEntity);
                }
            }
        }else{
            //处理签约
            FSJSQuerySignedBankCodeRes res = tradeServiceCCHelper.queryIsSignSJSCC(accountEntity.getTraderId());
            //如果已经签约
            if(!res.getRecord().get(0).getBank_no().equals("")){
                //开通出入金
                tradeServiceCCHelper.doOpenFinanceSJSCC(accountEntity);
                //变更记录
                gjsAccountEntity.setIsSign(1);
                gjsAccountDao.updateAccount(gjsAccountEntity);
            }
        }
    }

    /**
     * 查询是否登录
     * @param exchange 交易所代码
     * @param traderId 交易员编号
     * @return 是否登录
     * @throws Exception
     */
    public Boolean queryLogin(String exchange, String traderId) throws Exception{
        try{
            String token = this.getToken(exchange, traderId);
            if(exchange.equals(EGJSExchange.NJS.getCode())){
                return !token.equals("");
            }
            return !token.equals("");
        }catch(Exception e){
            return false;
        }
    }

    /**
     * 退出交易所
     * @param exchange 交易所代码
     * @param traderId 交易员编号
     * @throws Exception
     */
    public void doLogOut(String exchange, String traderId) throws Exception{
        try{
            this.logoutToken(exchange, traderId);
        }catch(Exception e){
            throw new CustomerException(EErrorCode.ERROR_CODE_300004.getMessage(), EErrorCode.ERROR_CODE_300004.getCode());
        }
    }

    /**
     * 变更银行卡号码
     * @param token 登录token
     * @param traderId 交易员编号
     * @param bankCode 银行卡编号
     * @param bankNo 银行卡号码
     * @throws Exception
     */
    public void doNJSChangeBankCard(String token, String traderId, String bankCode, String bankNo) throws Exception{
        tradeServiceCCHelper.doNJSChangeBankCardCC(token, traderId, bankCode, bankNo);
    }

    /**
     * NJS历史委托
     * @param req 请求对象
     * @return FQueryHistoryEntrustReq
     * @throws Exception
     */
    public FQueryHistoryEntrustReq queryNJSHistoryEntrust(FQueryHistoryEntrustReq req) throws Exception {
        List<FQueryHistoryEntrustRes> result = new ArrayList<>();
        List<GjsNJSHistoryEntrustEntity> list = this.gjsNJSHistoryEntrustDao.queryNJSHistoryEntrustList(req);
        for (GjsNJSHistoryEntrustEntity entity : list) {
            FQueryHistoryEntrustRes resResult = new FQueryHistoryEntrustRes();
            resResult.setOrderNo(entity.getSerialNo());
            resResult.setProdCode(entity.getWareId());
            resResult.setProdName(this.getGoodsName(entity.getWareId(), EGJSExchange.NJS.getCode()));
            resResult.setTradeType(entity.getBuyOrSal());
            if(entity.getPrice().equals("0") || entity.getPrice().equals("0.00")){
                entity.setPrice("市价");
            }
            resResult.setPrice(entity.getPrice());
            resResult.setAmount(entity.getNum());
            resResult.setProcessedAmount(entity.getContNum());
            resResult.setBailMoney("");
            resResult.setFeeMoney("");
            resResult.setState(entity.getcStatus());
            resResult.setDate(this.getFormatDatetime(entity.getDate(), entity.getTime()));
            resResult.setIsForce((entity.getOrderSty() != null && entity.getOrderSty().equals("151"))?"1":"0");
            resResult.setCancelTime(this.getFormatDatetime(entity.getDate(), entity.getsCancleTime()));
            result.add(resResult);
        }
        req.setItems(result);
        return req;
    }

    /**
     * NJS委托整合
     * @param req 请求对象
     * @param token 登录token
     * @return FQueryHistoryEntrustReq
     * @throws Exception
     */
    public FQueryHistoryEntrustReq queryNJSEntrustIntegrate(FQueryHistoryEntrustReq req, String token) throws Exception {
        req = this.queryNJSHistoryEntrust(req);
        if(req.getStart() == 0){
            req.setTodayData(this.queryNJSEntrust(token, req.getTraderId(), true));
        }
        return req;
    }

    /**
     * SJS历史委托
     * @param req 请求对象
     * @return FQueryHistoryEntrustReq
     * @throws Exception
     */
    public FQueryHistoryEntrustReq querySJSHistoryEntrust(FQueryHistoryEntrustReq req) throws Exception {
        List<FQueryHistoryEntrustRes> result = new ArrayList<>();
        List<GjsSJSHistoryEntrustEntity> list = this.gjsSJSHistoryEntrustDao.querySJSHistoryEntrustList(req);
        for (GjsSJSHistoryEntrustEntity entity : list) {
            FQueryHistoryEntrustRes resResult = new FQueryHistoryEntrustRes();
            resResult.setOrderNo(entity.getOrder_no());
            resResult.setProdCode(entity.getProd_code());
            resResult.setProdName(this.getGoodsName(entity.getProd_code(), EGJSExchange.SJS.getCode()));
            resResult.setTradeType(entity.getExch_type());
            resResult.setPrice(new BigDecimal(entity.getEntr_price()).setScale(2, BigDecimal.ROUND_DOWN).toString());
            resResult.setAmount(String.valueOf(entity.getEntr_amount()));
            resResult.setProcessedAmount(String.valueOf(entity.getEntr_amount() - entity.getRemain_amount()));
            resResult.setBailMoney("");
            resResult.setFeeMoney("");
            resResult.setState(entity.getEntr_stat());
            resResult.setDate(this.getFormatDatetime(entity.getEntr_date(), entity.getE_exch_time()));
            resResult.setCancelTime(this.getFormatDatetime(entity.getEntr_date(), entity.getC_exch_time()));
            resResult.setIsForce((entity.getE_term_type() != null && entity.getE_term_type().equals("12"))?"1":"0");
            result.add(resResult);
        }
        req.setItems(result);
        return req;
    }

    /**
     * 上金所委托整合
     * @param req 请求对象
     * @param token 登录token
     * @return FQueryHistoryEntrustReq
     * @throws Exception
     */
    public FQueryHistoryEntrustReq querySJSEntrustIntegrate(FQueryHistoryEntrustReq req, String token) throws Exception {
        req = this.querySJSHistoryEntrust(req);
        if(req.getStart() == 0){
            req.setTodayData(this.querySJSEntrust(token, true));
        }
        return req;
    }

    /**
     * NJS历史交易
     * @param req 请求对象
     * @return FQueryHistoryTradeReq
     * @throws Exception
     */
    public FQueryHistoryTradeReq queryNJSHistoryTrade(FQueryHistoryTradeReq req) throws Exception {
        List<FQueryHistoryTradeRes> result = new ArrayList<>();
        List<GjsNJSHistoryTradeEntity> list = this.gjsNJSHistoryTradeDao.queryNJSHistoryTradeWithPage(req);
        for (GjsNJSHistoryTradeEntity entity : list) {
            FQueryHistoryTradeRes resResult = new FQueryHistoryTradeRes();
            resResult.setOrderNo(entity.getSerialNo());
            resResult.setSerialNo(entity.getContNo());
            resResult.setProdCode(entity.getWareId());
            resResult.setProdName(this.getGoodsName(entity.getWareId(), EGJSExchange.NJS.getCode()));
            resResult.setTradeType(entity.getBuyOrSal());
            resResult.setPrice(entity.getConPrice());
            resResult.setAmount(entity.getContNum());
            resResult.setFeeMoney(entity.getTmpMoney());
            resResult.setProcessedTime(this.getFormatDatetime(entity.getDate(), entity.getfTime()));
            resResult.setTotalMoney(new BigDecimal(entity.getContQty()).abs().toString());
            resResult.setOffsetFlag("");
            resResult.setDate(entity.getDate());
            resResult.setIsForce((entity.getOrderSty() != null && entity.getOrderSty().equals("151"))?"1":"0");
            result.add(resResult);
        }
        req.setItems(result);
        return req;
    }

    /**
     * NJS成交整合
     * @param req 请求对象
     * @param token 登录token
     * @return FQueryHistoryTradeReq
     * @throws Exception
     */
    public FQueryHistoryTradeReq queryNJSTradeIntegrate(FQueryHistoryTradeReq req, String token) throws Exception {
        req = this.queryNJSHistoryTrade(req);
        if(req.getStart() == 0){
            req.setTodayData(this.queryNJSMatch(token, req.getTraderId()));
        }
        return req;
    }

    /**
     * SJS历史交易
     * @param req 请求对象
     * @return FQueryHistoryTradeReq
     * @throws Exception
     */
    public FQueryHistoryTradeReq querySJSHistoryTrade(FQueryHistoryTradeReq req) throws Exception {
        List<FQueryHistoryTradeRes> result = new ArrayList<>();
        List<GjsSJSHistoryTradeEntity> list = this.gjsSJSHistoryTradeDao.querySJSHistoryTradeList(req);
        for (GjsSJSHistoryTradeEntity entity : list) {
            FQueryHistoryTradeRes resResult = new FQueryHistoryTradeRes();
            resResult.setOrderNo(entity.getOrder_no());
            resResult.setSerialNo(entity.getMatch_no());
            resResult.setProdCode(entity.getProd_code());
            resResult.setProdName(this.getGoodsName(entity.getProd_code(), EGJSExchange.SJS.getCode()));
            resResult.setTradeType(entity.getExch_type());
            resResult.setPrice(String.valueOf(entity.getMatch_price()));
            resResult.setAmount(String.valueOf(entity.getMatch_amount()));
            resResult.setFeeMoney(String.valueOf(entity.getExch_fare()));
            resResult.setProcessedTime(this.getFormatDate(entity.getExch_date()) + " " + entity.getExch_time());
            resResult.setTotalMoney(String.valueOf(entity.getExch_bal()));
            resResult.setOffsetFlag(entity.getOffset_flag());
            resResult.setDate(entity.getExch_date());
            resResult.setIsForce((entity.getTerm_type() != null && entity.getTerm_type().equals("12"))?"1":"0");
            result.add(resResult);
        }
        req.setItems(result);
        return req;
    }

    /**
     * 上金所成交整合
     * @param req 请求对象
     * @param token 登录token
     * @return FQueryHistoryTradeReq
     * @throws Exception
     */
    public FQueryHistoryTradeReq querySJSTradeIntegrate(FQueryHistoryTradeReq req, String token) throws Exception {
        req = this.querySJSHistoryTrade(req);
        if(req.getStart() == 0){
            req.setTodayData(this.querySJSMatch(token));
        }
        return req;
    }

    /**
     * NJS历史出入金
     * @param req 请求对象
     * @return FQueryHistoryTransferReq
     * @throws Exception
     */
    public FQueryHistoryTransferReq queryNJSHistoryTransfer(FQueryHistoryTransferReq req) throws Exception {
        List<FQueryHistoryTransferRes> result = new ArrayList<>();
        List<GjsNJSHistoryTransferEntity> list = this.gjsNJSHistoryTransferDao.queryNJSHistoryTransferWithPage(req);
        for (GjsNJSHistoryTransferEntity entity : list) {
            FQueryHistoryTransferRes resResult = new FQueryHistoryTransferRes();
            resResult.setSerialNo(entity.getBank_water_id());
            resResult.setAccessWay(entity.getChange_type());
            resResult.setAmount(entity.getChange_money());
            resResult.setState(entity.getStyle());
            resResult.setCheckState("");
            resResult.setCreateTime(this.getFormatDatetime(entity.getDeal_date(), entity.getDeal_time()));
            result.add(resResult);
        }
        req.setItems(result);
        return req;
    }

    /**
     * SJS历史出入金
     * @param req 请求对象
     * @return FQueryHistoryTransferReq
     * @throws Exception
     */
    public FQueryHistoryTransferReq querySJSHistoryTransfer(FQueryHistoryTransferReq req) throws Exception {
        List<FQueryHistoryTransferRes> result = new ArrayList<>();
        List<GjsSJSHistoryTransferEntity> list = this.gjsSJSHistoryTransferDao.querySJSHistoryTransferList(req);
        for (GjsSJSHistoryTransferEntity entity : list) {
            FQueryHistoryTransferRes resResult = new FQueryHistoryTransferRes();
            resResult.setSerialNo(entity.getSerial_no());
            resResult.setAccessWay(entity.getAccess_way());
            resResult.setAmount(String.valueOf(entity.getExch_bal()));
            resResult.setState(entity.getIn_account_flag());
            resResult.setCheckState(entity.getCheck_stat1());
            resResult.setCreateTime(entity.getExch_date());
            result.add(resResult);
        }
        req.setItems(result);
        return req;
    }

    /**
     * 南交所推送成交
     * @param object 数据对象
     * @throws Exception
     */
    public void pushNJSMatchMsg(Object object) throws Exception{
        try{
            String receiveData = object.toString();
            System.out.println("接收信息：" + receiveData);
            String feedBackType = receiveData.substring(5, 6);
            if(feedBackType.equals("B")){
                List<Map<String, String>> result = JSON.parseObject(receiveData.substring(19), List.class);
                if(result != null && result.size() > 0){
                    for (Map<String, String> subRecord:result){
                        try{
                            if(!subRecord.get("BUYORSAL").equals("B") && !subRecord.get("BUYORSAL").equals("S")) continue;
                            logger.info("推送成交信息 firm_id:" + subRecord.get("FIRMID"));
                            Long userId = this.getUserId(EGJSExchange.NJS.getCode(), subRecord.get("FIRMID"));
                            if(userId != null && userId != 0){
                                String key = "NJS" + subRecord.get("DATE") + subRecord.get("CONTNO");
                                Boolean isSend = redisUtils.setNX(0, key, "1");
                                redisUtils.expired(0, key, 60L);
                                if(!isSend) continue;

                                String date = this.getFormatDatetime(subRecord.get("DATE"), subRecord.get("FTIME"));
                                String price = subRecord.get("CONPRICE");
                                String amount = subRecord.get("CONTNUM");
                                String goodsName = this.getGoodsName(subRecord.get("WAREID"), EGJSExchange.NJS.getCode());
                                String tradeType = subRecord.get("BUYORSAL").equals("B")?"买入":"卖出";
                                String pushTitle = "成交提醒";
                                String msgPre = "";

                                String pushMsg = String.format("您于%s以%s元价格%s%s手%s", date, price, tradeType, amount, goodsName);

                                Boolean sendMsg = false;
                                Boolean sendSms = false;

                                //强制平仓单
                                if(subRecord.get("ORDERSTY").equals("151")){
                                    pushTitle = "强制平仓提醒";
                                    msgPre = "【强制平仓】";
                                    pushMsg = String.format("您的南交所账户于%s有%s手%s被强制平仓，成交价格%s，请立即查看。", date, amount, goodsName, price);
                                    sendMsg = true;
                                    sendSms = true;
                                }

                                //发送个推消息
                                this.sendPushMsg(userId, EGetuiActionType.TYPE_OPENAPP, "gjs_trade_notice", pushTitle, msgPre + pushMsg);

                                //站内信
                                if(sendMsg) this.sendMsg(userId, EPushType.FORCEPC, msgPre + pushMsg);

                                //发送短信
                                if(sendSms) this.sendSms(userId, "（财猫贵金属）" + pushMsg);
                            }
                        }catch (CustomerException e){
                            logger.error("南交所推送成交信息消息失败，原因" + e.getMessage());
                        }catch(Exception e){
                            logger.error("南交所推送成交信息消息失败", e);
                        }
                    }
                }
            }
        }catch(Exception e){
            logger.error("南交所推送成交信息消息失败{}", e);
        }
    }

    /**
     * 推送上金所成交信息
     * @param object 数据对象
     * @throws Exception
     */
    public void pushSJSMatchMsg(Object object) throws Exception{
        try{
            Map<String, String> dataMap = new HashMap<>();
            String data = object.toString();
            if(data != null && !data.equals("")){
                for (String subData:data.split("#")){
                    String[] params = subData.split("=");
                    if(params.length == 2){
                        dataMap.put(params[0], params[1]);
                    }
                }
            }

            if(!dataMap.containsKey("clientID") || dataMap.get("clientID").equals("")) return;
            logger.info("推送成交信息 clientID:" + dataMap.get("clientID"));

            Long userId = this.getUserId(EGJSExchange.SJS.getCode(), dataMap.get("clientID"));
            if(userId == null || userId == 0) return;

            String sign;
            if(dataMap.containsKey("ApiName") && dataMap.get("ApiName").equals("onRecvRtnDeferMatch") && dataMap.containsKey("DataType") && dataMap.get("DataType").equals("TDeferMatch")){
                sign = (dataMap.get("offsetFlag") != null && dataMap.get("offsetFlag").equals("0"))?"开仓":"平仓";
            }else if(dataMap.containsKey("ApiName") && dataMap.get("ApiName").equals("onRecvRtnSpotMatch") && dataMap.containsKey("DataType") && dataMap.get("DataType").equals("TSpotMatch")){
                sign = "";
            }else{
                return;
            }

            String date = this.getFormatDatetime(dataMap.get("matchDate"), dataMap.get("matchTime").replace(":", ""));
            String price = new BigDecimal(dataMap.get("price")).setScale(2, BigDecimal.ROUND_DOWN).toString();
            String amount = dataMap.get("volume");
            String goodsName = this.getGoodsName(dataMap.get("instID"), EGJSExchange.SJS.getCode());
            String tradeType = (dataMap.get("buyOrSell").equals("b"))?"买入":"卖出";
            String pushTitle = "成交提醒";
            String msgPre = "";
            String pushMsg = String.format("您于%s以%s元价格%s%s%s手%s", date, price, sign, tradeType, amount, goodsName);

            Boolean sendMsg = false;
            Boolean sendSms = false;

            if(dataMap.get("offsetFlag").equals("2")){
                pushTitle = "强制平仓提醒";
                msgPre = "【强制平仓】";
                pushMsg = String.format("您的上金所账户于%s有%s手%s被强制平仓，成交价格%s，请立即查看。", date, amount, goodsName, price);
                sendMsg = true;
                sendSms = true;
            }

            if(!pushMsg.equals("")){
                String key = "SJS" + dataMap.get("matchDate") + dataMap.get("orderNo");
                Boolean isSend = redisUtils.setNX(0, key, "1");
                redisUtils.expired(0, key, 60L);
                if(!isSend) return;

                //发送个推消息
                this.sendPushMsg(userId, EGetuiActionType.TYPE_OPENAPP, "gjs_trade_notice", pushTitle, msgPre + pushMsg);

                //站内信
                if(sendMsg) this.sendMsg(userId, EPushType.FORCEPC, msgPre + pushMsg);

                //发送短信
                if(sendSms) this.sendSms(userId, "（财猫贵金属）" + pushMsg);
            }

        }catch (CustomerException e){
            logger.error("上金所推送成交信息消息失败，原因" + e.getMessage());
        }catch(Exception e){
            logger.error("上金所推送成交信息消息失败", e);
        }
    }

    /**
     * 根据交易所获取提示信息
     * @param exchange 交易所编号
     * @return String
     */
    protected static String getMessageByExchange(String exchange) {
        String returnString = "";
        if (exchange.equals(EGJSExchange.NJS.getCode())) {
            returnString = "休市 工作日9:00到次日6:00可交易";
        } else if (exchange.equals(EGJSExchange.SJS.getCode())) {
            returnString = "休市 工作日9:00-11:30,13:30-15:00,20:00-2:30可交易";
        } else {
            returnString = "交易所不正确！";
        }
        return returnString;
    }


    /**
     * 验证数据库中的交易时间段是否正确
     * @param tradeTime 时间段
     * @return Boolean
     */
    protected static Boolean initTradeTime(String tradeTime) {
        // 交易时间格式：0900-1100,1300-1500
        if (!org.apache.commons.lang3.StringUtils.isBlank(tradeTime)) {
            String[] allArray = tradeTime.split(",");
            for(String str : allArray) {
                if(str.length() == 9 && str.contains("-")) {
                    tradeTimeList.add(str);
                } else {
                    throw new RuntimeException("交易时间格式设置错误：正确格式为0900-1100,1300-1500当前格式为：" + tradeTime);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * compare 是否在star-end 之间，如果是返回true,否则返回false
     * System.out.println(DateUtils.isInterval("0900", "1130", "1130", "hhmm"));
     * System.out.println(DateUtils.isInterval("20151008", "20151010", "20151011", "yyyyMMdd"));
     * System.out.println(DateUtils.isInterval("20151009113758", "20151009133758", "20151009143758", "yyyyMMdd"));
     */
    protected static Boolean isInterval(String star, String end, String compare, String formate) {
        Boolean isInterval = false;
        SimpleDateFormat localTime = new SimpleDateFormat(formate);
        try{
            Date sdate = localTime.parse(star);
            Date edate = localTime.parse(end);
            Date scompare = localTime.parse(compare);
            // Date1.after(Date2),当Date1大于Date2时，返回TRUE，当小于等于时，返回false；
            // Date1.before(Date2)，当Date1小于Date2时，返回TRUE，当大于等于时，返回false；
            if (!scompare.after(edate)&& !scompare.before(sdate)) {
                isInterval = true;
            } else {
                isInterval = false;
            }
        } catch (Exception e) {
            System.out.println("日期比较异常：" + e.getMessage());
            throw new RuntimeException("日期比较异常：" + e.getMessage());
        }
        return isInterval;
    }

    /**
     * 南交所查询交易套餐
     * @return List
     * @throws Exception
     */
    public List<FQueryPackageListRes> queryNJSPackageList() throws Exception{
        List<FQueryPackageListRes> result = new ArrayList<>();
        FNJSQueryPackageListRes res = tradeServiceCCHelper.queryNJSPackageListCC();
        if(res.getDATAS() != null && res.getDATAS().size() > 0){
            for (NJSQueryPackageListEntity entity:res.getDATAS()){
                FQueryPackageListRes subRes = new FQueryPackageListRes();
                subRes.setPackageId(entity.getTAOCANID());
                subRes.setPackageName(entity.getTAOCANNAME());
                subRes.setComm(entity.getCOMM());
                subRes.setContMoney(entity.getCONTMONEY());
                subRes.setTimeLimit(entity.getTIMELIMIT());
                subRes.setMemo(entity.getMEMO());
                result.add(subRes);
            }
        }
        return result;
    }

    /**
     * 查询南交所套餐信息
     * @param token 登录token
     * @param traderId 交易员编号
     * @return FQueryPackageInfoRes
     * @throws Exception
     */
    public FQueryPackageInfoRes queryNJSPackageInfo(String token, String traderId) throws Exception{
        FQueryPackageInfoRes result = new FQueryPackageInfoRes();
        FNJSQueryPackageInfoRes res = tradeServiceCCHelper.queryNJSPackageInfoCC(token, traderId);
        NJSQueryPackageInfoEntity entity = res.getDATAS().get(0);
        result.setValidDate(entity.getVALID_DATE());
        result.setQuota(entity.getQUOTA());
        result.setUseQuota(entity.getUSE_QUOTA());
        return result;
    }

    /**
     * 查询套餐申请记录
     * @param token 登录token
     * @param traderId 交易员编号
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return List
     * @throws Exception
     */
    public List<FQueryPackageHistoryRes> queryNJSPackageHistory(String token, String traderId, String startDate, String endDate) throws Exception{
        List<FQueryPackageHistoryRes> result = new ArrayList<>();
        FNJSQueryPackageHistoryRes res = tradeServiceCCHelper.queryNJSPackageHistoryCC(token, traderId, startDate, endDate);
        if(res.getDATAS() != null && res.getDATAS().size() > 0){
            for (NJSQueryPackageHistoryEntity entity:res.getDATAS()){
                FQueryPackageHistoryRes subRes = new FQueryPackageHistoryRes();
                subRes.setDatetime(this.getFormatDatetime(entity.getFDATE(), entity.getREQTIME()));
                subRes.setOrderId(entity.getWATERID());
                subRes.setPackageId(entity.getTAOCANID());
                subRes.setPackageName(entity.getTAOCANNAME());
                subRes.setContMoney(entity.getCONTMONEY());
                result.add(subRes);
            }
        }
        return result;
    }

    /**
     * 交易套餐购买
     * @param token 登录token
     * @param traderId 交易员编号
     * @param packageId 套餐编号
     * @throws Exception
     */
    public void doNJSPackageBuy(String token, String traderId, String packageId) throws Exception{
        tradeServiceCCHelper.doNJSPackageBuyCC(token, traderId, packageId);
    }

    /**
     * 签订代交收协议
     * @param token 登录token
     * @param traderId 交易员编号
     * @throws Exception
     */
    public void doNJSSignAgreement(String token, String traderId) throws Exception{
        tradeServiceCCHelper.doNJSSignAgreementCC(token, traderId);
    }

    /**
     * 查询上金所用户信息
     * @param userId 用户编号
     * @param traderId 交易员编号
     * @return GJSAccountEntity
     * @throws Exception
     */
    public GJSAccountEntity querySJSTrader(Long userId, String traderId) throws Exception{
        FSJSQueryTraderRes res = tradeServiceCCHelper.querySJSTraderCC(traderId);
        SJSQueryTraderEntity traderEntity = res.getRecord().get(0);

        GJSAccountEntity gjsAccountEntity = new GJSAccountEntity();
        gjsAccountEntity.setUserId(userId);
        gjsAccountEntity.setExchange(EGJSExchange.SJS.getCode());
        gjsAccountEntity.setSaltKey(MD5Util.md5(userId.toString() + String.valueOf(System.currentTimeMillis())).substring(0, 8));
        gjsAccountEntity.setFirmId("");
        gjsAccountEntity.setTraderId(traderId);
        gjsAccountEntity.setRealName(traderEntity.getCust_name());
        gjsAccountEntity.setIdCard(traderEntity.getCert_num());
        String[] bankArray = traderEntity.getBk_acct_no().split(":");
        gjsAccountEntity.setBankId(bankArray[0]);
        gjsAccountEntity.setBankCard(bankArray[1]);
        gjsAccountEntity.setIsSign(1);
        gjsAccountEntity.setUploadStatus(EGJSUploadStatus.ACCESS_SUCCESS.getCode());
        gjsAccountEntity.setCardPositive("");
        gjsAccountEntity.setCardObverse("");
        gjsAccountEntity.setIsSignAgreement(1);
        gjsAccountEntity.setCreateDatetime(new Date());
        return gjsAccountEntity;
    }

    /**
     * 南交所中信银行快速签约
     * @param token 登录token
     * @param traderId 交易员编号
     * @param gjsAccountEntity 开户信息
     * @throws Exception
     */
    public void doNJSBankSign(String token, String traderId, GJSAccountEntity gjsAccountEntity) throws Exception{
        tradeServiceCCHelper.doNJSCNCBSignCC(token, traderId, gjsAccountEntity);
        //变更记录
        GJSAccountEntity gjsAccount = new GJSAccountEntity();
        gjsAccount.setUserId(gjsAccountEntity.getUserId());
        gjsAccount.setExchange(gjsAccountEntity.getExchange());
        gjsAccount.setIsSign(1);
        gjsAccountDao.updateAccount(gjsAccount);
    }

    /**
     * 南交所中信银行入金
     * @param token 登录token
     * @param traderId 交易员编号
     * @param req 请求对象
     * @throws Exception
     */
    public FDoCreateTransferInNoRes doNJSCreateTransferInNo(String token, String traderId, FDoTransferInReq req) throws Exception{
        //登录资金端
        tradeServiceCCHelper.queryNJSFundsManageCC(token, traderId, req.getFundsPwd());
        //获取入金编号
        FNJSDoCreateTransferNoRes res = tradeServiceCCHelper.doNJSCreateTransferInNoCC(token, traderId, req);
        //整理数据
        FDoCreateTransferInNoRes result = new FDoCreateTransferInNoRes();
        result.setMERID(res.getMERID());
        result.setORDERNO(res.getORDERNO());
        return result;
    }

    /**
     * 检测用户风险率并发送
     * @param tplNo 模版编号
     * @throws Exception
     */
    public void queryNJSRisk(Integer tplNo) throws Exception{
        FNJSQueryRiskRes result = tradeServiceCCHelper.queryNJSRiskCC();
        try{
            if(result.getDATAS().size() > 0){
                for (NJSQueryRiskEntity entity:result.getDATAS()){
                    try{
                        String traderId = entity.getFIRMID();
                        Integer risk = new BigDecimal(entity.getSAFERATE()).intValue();
                        if(risk < 89) continue;

                        Long userId = this.getUserId(EGJSExchange.NJS.getCode(), traderId);

                        String msg = "";
                        switch (tplNo){
                            case 1:
                                if(risk >= 90) msg = "您的南交所账户风险率达到90%，在交易所清算期间（15:30-17:30/交易日）会被强制平仓，请立即查看，及时减仓或入金操作。";
                                break;
                            case 2:
                                if(risk >= 90) msg = "您的南交所账户风险率达到90%，在交易所清算期间（15:30-17:30/交易日）会被强制平仓，请立即查看，及时减仓或入金操作。";
                                break;
                            case 3:
                                if(risk >= 90){
                                    msg = "您的南交所账户风险率达到90%，15分钟后交易所进入清算时间（15:30-17:30/交易日）将对其强制平仓！请立即查看，及时减仓或入金操作。";
                                }else if(risk == 89){
                                    msg = "您的南交所账户风险率达到89%，若短时间内行情出现大波动，将有爆仓危险。15分钟后转账时间截止，请及时关注。";
                                }
                                break;
                            default:
                                break;
                        }

                        if(msg.equals("")) continue;
                        //发送个推
                        this.sendPushMsg(userId, EGetuiActionType.TYPE_OPENAPP, "gjs_risk_notice", "风险提示", "【持仓预警】" + msg);
                        //发送站内信
                        this.sendMsg(userId, EPushType.RISKNOTICE, "【持仓预警】" + msg);
                        //发送短信
                        this.sendSms(userId,  "（财猫贵金属）" + msg);
                    }catch (CustomerException e){
                        logger.error("检测风险率失败了:" + e.getMessage());
                    }catch(Exception e){
                        logger.error("检测风险率失败了", e);
                    }
                }
            }
        }catch(Exception e){
            throw new CustomerException("风险检测失败了", 1021420);
        }
    }

    /**
     * 查询单个品种下单信息
     * @param token 登录token
     * @param traderId 交易员编号
     * @param prodCode 商品代码
     * @throws Exception
     */
    public FQueryProdSingleRes queryNJSProdSingle(String token, String traderId, String prodCode) throws Exception{
        FNJSQueryWareSingleRes res = tradeServiceCCHelper.queryNJSProdSingleCC(token, traderId, prodCode);
        NJSQueryWareSingleEntity singleEntity = res.getDATAS().get(0);

        FQueryProdSingleRes result = new FQueryProdSingleRes();
        result.setBuyMax(singleEntity.getMAXBQTY());
        result.setSellMax(singleEntity.getMAXSQTY());

        return result;
    }
}