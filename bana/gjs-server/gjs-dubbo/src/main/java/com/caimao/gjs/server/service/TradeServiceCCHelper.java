package com.caimao.gjs.server.service;

import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.gjs.api.entity.GJSAccountEntity;
import com.caimao.gjs.api.entity.req.*;
import com.caimao.gjs.api.entity.res.FQueryProdSingleRes;
import com.caimao.gjs.api.enums.ENJSBankNo;
import com.caimao.gjs.api.enums.ENJSOPENBANK;
import com.caimao.gjs.server.trade.process.NJSDoProcess;
import com.caimao.gjs.server.trade.process.SJSDoProcess;
import com.caimao.gjs.server.trade.protocol.njs.entity.req.*;
import com.caimao.gjs.server.trade.protocol.njs.entity.res.*;
import com.caimao.gjs.server.trade.protocol.sjs.entity.req.*;
import com.caimao.gjs.server.trade.protocol.sjs.entity.res.*;
import com.caimao.gjs.server.utils.*;
import com.huobi.commons.utils.JsonUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("tradeServiceCCHelper")
public class TradeServiceCCHelper {
    private static final Logger logger = LoggerFactory.getLogger(TradeServiceCCHelper.class);

    @Resource
    private NJSDoProcess njsDoProcess;
    @Resource
    RedisUtils redisUtils;

    /**
     * 南交所登录socket
     * @param traderId 交易员编号
     * @param tradePwd 登录密码
     * @return FNJSDoTradeLoginRes
     * @throws Exception
     */
    public FNJSDoTradeLoginRes doNJSTradeLoginCC(String traderId, String tradePwd) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoTradeLoginReq req = new FNJSDoTradeLoginReq();
        req.setTRADERID(traderId);
        req.setTRADEPASS(tradePwd);
        //处理
        FNJSDoTradeLoginRes res = doProcess.getResult(FNJSDoTradeLoginRes.class, "011001", req);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (res.getSTATE().equals("0") && res.getTOKEN() != null && !res.getTOKEN().equals("")) {
            return res;
        } else {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 上金所登录
     * @param traderId 交易员编号
     * @param tradePwd 登录密码
     * @return FSJSDoTradeLoginRes
     * @throws Exception
     */
    public FSJSDoTradeLoginRes doSJSTradeLoginCC(String traderId, String tradePwd) throws Exception {
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("trade-sjs");
        //构造请求对象
        FSJSDoTradeLoginReq req = new FSJSDoTradeLoginReq();
        req.setUser_pwd(MD5Util.md5(tradePwd).toLowerCase());
        //处理
        FSJSDoTradeLoginRes res = doProcess.getResult(FSJSDoTradeLoginRes.class, "800101", traderId, req);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (res.getH_rsp_code().equals("HJ0000")) {
            return res;
        } else {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            if(res.getH_rsp_msg().contains("超过限定")){
                throw new CustomerException(EErrorCode.ERROR_CODE_300015.getMessage(), EErrorCode.ERROR_CODE_300015.getCode());
            }else{
                throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
            }
        }
    }

    /**
     * 南交所委托下单 限价单
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @return FNJSDoEntrustLimitRes
     * @throws Exception
     */
    public FNJSDoEntrustLimitRes doNJSEntrustLimitCC(String token, String traderId, FDoEntrustReq req) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoEntrustLimitReq reqSend = new FNJSDoEntrustLimitReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        reqSend.setWAREID(req.getProdCode());
        reqSend.setBUYORSAL(req.getTradeType());
        reqSend.setPRICE(req.getPrice().toString());
        reqSend.setNUM(req.getAmount().toString());
        //处理
        FNJSDoEntrustLimitRes res = doProcess.getResult(FNJSDoEntrustLimitRes.class, "011013", reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (res.getSTATE().equals("0") && res.getDATAS() != null && res.getDATAS().get(0).getSERIALNO() != null && !res.getDATAS().get(0).getSERIALNO().equals("")) {
            return res;
        } else {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 南交所委托下单 市价单
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @return FNJSDoEntrustMarketRes
     * @throws Exception
     */
    public FNJSDoEntrustMarketRes doNJSEntrustMarketCC(String token, String traderId, FDoEntrustReq req) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoEntrustMarketReq reqSend = new FNJSDoEntrustMarketReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        reqSend.setWAREID(req.getProdCode());
        reqSend.setBUYORSAL(req.getTradeType());
        reqSend.setPRICE("0");
        reqSend.setNUM(req.getAmount().toString());
        //处理
        FNJSDoEntrustMarketRes res = doProcess.getResult(FNJSDoEntrustMarketRes.class, "011014", reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (res.getSTATE().equals("0") && res.getDATAS() != null && res.getDATAS().get(0).getSERIALNO() != null && !res.getDATAS().get(0).getSERIALNO().equals("")) {
            return res;
        } else {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 上金所委托下单 限价单
     * @param token 登录token
     * @param req 请求对象
     * @return FSJSDoEntrustLimitRes
     * @throws Exception
     */
    public FSJSDoEntrustLimitRes doSJSEntrustLimitCC(String token, FDoEntrustReq req) throws Exception {
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("trade-sjs");
        //构造请求对象
        FSJSDoEntrustLimitReq reqSend = new FSJSDoEntrustLimitReq();
        reqSend.setProd_code(req.getProdCode());
        reqSend.setExch_type(req.getTradeType());
        reqSend.setEntr_price(req.getPrice().toString());
        reqSend.setEntr_amount(req.getAmount().toString());
        reqSend.setClient_serial_no(String.valueOf(System.currentTimeMillis()));
        //处理
        FSJSDoEntrustLimitRes res = doProcess.getResult(FSJSDoEntrustLimitRes.class, "400101", token, reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (res.getH_rsp_code().equals("HJ0000") && res.getRecord() != null && res.getRecord().get(0).getLocal_order_no() != null && !res.getRecord().get(0).getLocal_order_no().equals("")) {
            return res;
        } else {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }
    }

    /**
     * 南交所委托撤销
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @throws Exception
     */
    public void doNJSEntrustCancelCC(String token, String traderId, FDoEntrustCancelReq req) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoEntrustCancelReq reqSend = new FNJSDoEntrustCancelReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        reqSend.setBUYORSAL(req.getTradeType());
        reqSend.setWAREID(req.getProdCode());
        reqSend.setSERIALNO(req.getOrderNo());

        //处理
        FNJSDoEntrustCancelRes res = doProcess.getResult(FNJSDoEntrustCancelRes.class, "011015", reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 上金所委托撤销
     * @param token 登录token
     * @param req 请求对象
     * @throws Exception
     */
    public void doSJSEntrustCancelCC(String token, FDoEntrustCancelReq req) throws Exception {
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("trade-sjs");
        //构造请求对象
        FSJSDoEntrustCancelReq reqSend = new FSJSDoEntrustCancelReq();
        reqSend.setOrder_no(req.getOrderNo());
        //处理
        FSJSDoEntrustCancelRes res = doProcess.getResult(FSJSDoEntrustCancelRes.class, "406101", token, reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }
    }

    /**
     * 南交所止盈止损下单
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @return FNJSDoFullStopRes
     * @throws Exception
     */
    public FNJSDoFullStopRes doNJSFullStopCC(String token, String traderId, FDoFullStopReq req) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoFullStopReq reqSend = new FNJSDoFullStopReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        reqSend.setWAREID(req.getProdCode());
        reqSend.setUPPRICE(req.getUpPrice().toString());
        reqSend.setDOWNPRICE(req.getDownPrice().toString());
        reqSend.setNUM(req.getAmount().toString());
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String validDate = df.format(new Date(System.currentTimeMillis() + 5 * 86400 * 1000));
        reqSend.setVALIDDATE(validDate);
        //处理
        FNJSDoFullStopRes res = doProcess.getResult(FNJSDoFullStopRes.class, "011016", reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (res.getSTATE().equals("0") && res.getDATAS() != null && res.getDATAS().get(0).getSERIALNO() != null && !res.getDATAS().get(0).getSERIALNO().equals("")) {
            return res;
        } else {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 南交所止盈止损撤单
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @throws Exception
     */
    public void doNJSFullStopCancelCC(String token, String traderId, FDoFullStopCancelReq req) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoFullStopCancelReq reqSend = new FNJSDoFullStopCancelReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        reqSend.setWAREID(req.getProdCode());
        reqSend.setSERIALNO(req.getOrderNo());
        reqSend.setFDATE(req.getApplyDate());
        //处理
        FNJSDoFullStopCancelRes res = doProcess.getResult(FNJSDoFullStopCancelRes.class, "011017", reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 南交所止盈止损查询
     * @param token 登录token
     * @param traderId 用户编号
     * @return FNJSQueryFullStopRes
     * @throws Exception
     */
    public FNJSQueryFullStopRes queryNJSFullStopCC(String token, String traderId) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryFullStopReq reqSend = new FNJSQueryFullStopReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        FNJSQueryFullStopRes res = doProcess.getResult(FNJSQueryFullStopRes.class, "011020", reqSend);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }

    /**
     * 南交所条件单下单
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @return FNJSDoConditionRes
     * @throws Exception
     */
    public FNJSDoConditionRes doNJSConditionCC(String token, String traderId, FDoConditionReq req) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoConditionReq reqSend = new FNJSDoConditionReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        reqSend.setBUYORSAL(req.getTradeType());
        reqSend.setWAREID(req.getProdCode());
        reqSend.setPRICE(req.getPrice().toString());
        reqSend.setNUM(req.getAmount().toString());
        reqSend.setTOUCHPRICE(req.getTouchPrice().toString());
        reqSend.setCONDITION(req.getCondition());
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String validDate = df.format(new Date(System.currentTimeMillis() + 5 * 86400 * 1000));
        reqSend.setVALIDDATE(validDate);

        //处理
        FNJSDoConditionRes res = doProcess.getResult(FNJSDoConditionRes.class, "011018", reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (res.getSTATE().equals("0") && res.getDATAS() != null && res.getDATAS().get(0).getSERIALNO() != null && !res.getDATAS().get(0).getSERIALNO().equals("")) {
            return res;
        } else {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 南交所条件单撤销
     * @param token 登录token
     * @param traderId 用户编号
     * @param req 请求对象
     * @throws Exception
     */
    public void doNJSConditionCancelCC(String token, String traderId, FDoConditionCancelReq req) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoFullStopCancelReq reqSend = new FNJSDoFullStopCancelReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        reqSend.setWAREID(req.getProdCode());
        reqSend.setSERIALNO(req.getOrderNo());
        reqSend.setFDATE(req.getApplyDate());
        //处理
        FNJSDoConditionCancelRes res = doProcess.getResult(FNJSDoConditionCancelRes.class, "011019", reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 南交所条件单查询
     * @param token 登录token
     * @param traderId 用户编号
     * @return FNJSQueryConditionRes
     * @throws Exception
     */
    public FNJSQueryConditionRes queryNJSConditionCC(String token, String traderId) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryConditionReq reqSend = new FNJSQueryConditionReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        FNJSQueryConditionRes res = doProcess.getResult(FNJSQueryConditionRes.class, "011021", reqSend);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }

    /**
     * 所持仓查询
     * @param token 登录token
     * @param traderId 用户编号
     * @return FNJSQueryHoldRes
     * @throws Exception
     */
    public FNJSQueryHoldRes queryNJSHoldCC(String token, String traderId) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryHoldReq reqSend = new FNJSQueryHoldReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        FNJSQueryHoldRes res = doProcess.getResult(FNJSQueryHoldRes.class, "011007", reqSend);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }

    /**
     * 上金所持仓查询（延期）
     * @param token 登录token
     * @return FSJSQueryHoldRes
     * @throws Exception
     */
    public FSJSQueryHoldRes querySJSHoldCC(String token) throws Exception {
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("tradeQuery-sjs");
        //构造请求对象
        FSJSQueryHoldReq reqSend = new FSJSQueryHoldReq();
        //处理
        FSJSQueryHoldRes res = doProcess.getResult(FSJSQueryHoldRes.class, "102003", token, reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        } else {
            return res;
        }
    }

    /**
     * 上金所持仓查询（现货）
     * @param token 登录token
     * @return FSJSQueryProdRes
     * @throws Exception
     */
    public FSJSQueryProdRes querySJSHoldProdCC(String token) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("tradeQuery-sjs");
        //构造请求对象
        FSJSQueryProdReq req = new FSJSQueryProdReq();
        //处理
        FSJSQueryProdRes res = doProcess.getResult(FSJSQueryProdRes.class, "102002", token, req);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        } else {
            return res;
        }
    }

    /**
     * 南交所当日委托查询
     * @param token 登录token
     * @param traderId 用户编号
     * @return FNJSQueryEntrustRes
     * @throws Exception
     */
    public FNJSQueryEntrustRes queryNJSEntrustCC(String token, String traderId) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryEntrustReq reqSend = new FNJSQueryEntrustReq();
            reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        FNJSQueryEntrustRes res = doProcess.getResult(FNJSQueryEntrustRes.class, "011011", reqSend);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }

    /**
     * 上金所当日委托查询
     * @param token 登录token
     * @return FSJSQueryEntrustRes
     * @throws Exception
     */
    public FSJSQueryEntrustRes querySJSEntrustCC(String token) throws Exception {
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("tradeQuery-sjs");
        //构造请求对象
        FSJSQueryEntrustReq reqSend = new FSJSQueryEntrustReq();
        reqSend.setPaginal_num("10000");
        reqSend.setCurr_page("1");
        //处理
        FSJSQueryEntrustRes res = doProcess.getResult(FSJSQueryEntrustRes.class, "600203", token, reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        } else {
            return res;
        }
    }

    /**
     * 南交所查询当日成交
     * @param token 登录token
     * @param traderId 交易员编号
     * @return FNJSQueryMatchYesRes
     * @throws Exception
     */
    public FNJSQueryMatchYesRes queryNJSMatchCC(String token, String traderId) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryMatchYesReq reqSend = new FNJSQueryMatchYesReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        FNJSQueryMatchYesRes res = doProcess.getResult(FNJSQueryMatchYesRes.class, "011003", reqSend);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }

    /**
     * 上金所查询当日成交
     * @param token 登录token
     * @return FSJSQueryMatchRes
     * @throws Exception
     */
    public FSJSQueryMatchRes querySJSMatchCC(String token) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("tradeQuery-sjs");
        //构造请求对象
        FSJSQueryMatchReq reqSend = new FSJSQueryMatchReq();
        reqSend.setPaginal_num("10000");
        reqSend.setCurr_page("1");
        //处理
        FSJSQueryMatchRes res = doProcess.getResult(FSJSQueryMatchRes.class, "600205", token, reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        } else {
            return res;
        }
    }

    /**
     * 南交所查询资产
     * @param token 登录token
     * @param traderId 用户编号
     * @return FQueryFundsRes
     * @throws Exception
     */
    public FNJSQueryFundsRes queryNJSFundsCC(String token, String traderId) throws Exception {
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryFundsReq reqSend = new FNJSQueryFundsReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        FNJSQueryFundsRes res = doProcess.getResult(FNJSQueryFundsRes.class, "011008", reqSend);
        System.out.println(ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getSTATE().equals("0") || res.getDATAS() == null || res.getDATAS().size() <= 0) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }

    /**
     * 上金所查询资产
     * @param token 登录token
     * @return FQueryFundsRes
     * @throws Exception
     */
    public FSJSQueryFundsRes querySJSFundsCC(String token) throws Exception {
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("tradeQuery-sjs");
        //构造请求对象
        FSJSQueryFundsReq req = new FSJSQueryFundsReq();
        FSJSQueryFundsRes res = doProcess.getResult(FSJSQueryFundsRes.class, "102001", token, req);
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000") || res.getRecord() == null || res.getRecord().size() <= 0) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        } else {
            return res;
        }
    }

    /**
     * 上金所查询安全率
     * @param token 登录token
     * @return FSJSQueryRiskRes
     * @throws Exception
     */
    public FSJSQueryRiskRes querySafeSJSCC(String token) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("tradeQuerySafe-sjs");
        //构造请求对象
        FSJSQueryRiskReq req = new FSJSQueryRiskReq();
        FSJSQueryRiskRes res = doProcess.getResult(FSJSQueryRiskRes.class, "309901", token, req);
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000") || res.getRecord() == null || res.getRecord().size() <= 0) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        } else {
            return res;
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
    public void doNJSModifyFundsPwdCC(String token, String traderId, String oldPwd, String newPwd, String bankId) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoModifyFundsPwdReq req = new FNJSDoModifyFundsPwdReq();
        req.setTOKEN(token);
        req.setCUSTTRADEID(traderId);
        req.setTRADERID(traderId);
        req.setBANKID(bankId);
        req.setCUSTMONEYPWD(oldPwd);
        req.setNCUSTMONEYPWD(newPwd);
        FNJSDoModifyFundsPwdRes res = doProcess.getResult(FNJSDoModifyFundsPwdRes.class, "013003", req);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 上金所修改资金密码
     * @param token 登录token
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @throws Exception
     */
    public void doSJSModifyFundsPwdCC(String token, String oldPwd, String newPwd) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("trade-sjs");
        //构造请求对象
        FSJSDoModifyPwdReq req = new FSJSDoModifyPwdReq();
        req.setOld_fund_pwd(MD5Util.md5(oldPwd).toLowerCase());
        req.setFund_pwd(MD5Util.md5(newPwd).toLowerCase());

        FSJSDoModifyPwdRes res = doProcess.getResult(FSJSDoModifyPwdRes.class, "202201", token, req);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }
    }

    /**
     * 南交所修改交易密码
     * @param token 登录token
     * @param traderId 用户编号
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @throws Exception
     */
    public void doNJSModifyTradePwdCC(String token, String traderId, String oldPwd, String newPwd) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoModifyTradePwdReq req = new FNJSDoModifyTradePwdReq();
        req.setTOKEN(token);
        req.setTRADERID(traderId);
        req.setOLDPWD(oldPwd);
        req.setNEWPWD(newPwd);
        req.setIPADDRESS("127.0.0.1");
        FNJSDoModifyFundsPwdRes res = doProcess.getResult(FNJSDoModifyFundsPwdRes.class, "011032", req);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 上金所修改交易密码
     * @param token 登录token
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @throws Exception
     */
    public void doSJSModifyTradePwdCC(String token, String oldPwd, String newPwd) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("trade-sjs");
        //构造请求对象
        FSJSDoModifyPwdReq req = new FSJSDoModifyPwdReq();
        req.setOld_exch_pwd(MD5Util.md5(oldPwd).toLowerCase());
        req.setExch_pwd(MD5Util.md5(newPwd).toLowerCase());

        FSJSDoModifyPwdRes res = doProcess.getResult(FSJSDoModifyPwdRes.class, "202201", token, req);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }
    }

    /**
     * 南交所管理员登录
     * @return FNJSAdminDoLoginRes
     * @throws Exception
     */
    public FNJSAdminDoLoginRes doNJSResetLoginCC() throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSAdminDoLoginReq req = new FNJSAdminDoLoginReq();
        req.setUSERID(Constants.NJS_ADMIN_USER_ID);
        req.setUSERPWD(Constants.NJS_ADMIN_USER_PWD);
        req.setIPADDRESS("127.0.0.1");
        //处理
        FNJSAdminDoLoginRes res = doProcess.getResult(FNJSAdminDoLoginRes.class, "016001", req);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (res.getSTATE().equals("0") && res.getTOKEN() != null && !res.getTOKEN().equals("")) {
            return res;
        } else {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 南交所重置资金密码
     * @param traderId 交易员编号
     * @param newPwd 密码
     * @param firmId 交易商编号
     * @param adminToken 管理员登录token
     * @throws Exception
     */
    public void doNJSResetFundsPwdCC(String traderId, String newPwd, String firmId, String adminToken) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSAdminDoResetPwdReq req = new FNJSAdminDoResetPwdReq();
        req.setTOKEN(adminToken);
        req.setUSERID(firmId);
        req.setTRADERID(traderId);
        req.setBANKPWD(newPwd);
        req.setIPADDRESS("127.0.0.1");
        //处理
        FNJSAdminDoResetPwdRes res = doProcess.getResult(FNJSAdminDoResetPwdRes.class, "016002", req);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 上金所重置资金密码
     * @param traderId 交易员编号
     * @param newPwd 新密码
     * @throws Exception
     */
    public void doSJSResetFundsPwdCC(String traderId, String newPwd) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("open-sjs");
        //构造请求对象
        FSJSAdminDoResetFundsPwdReq req = new FSJSAdminDoResetFundsPwdReq();
        req.setAcct_no(traderId);
        req.setNew_fund_pwd(MD5Util.md5(newPwd).toLowerCase());

        FSJSAdminDoResetFundsPwdRes res = doProcess.getResult(FSJSAdminDoResetFundsPwdRes.class, "880161", traderId, req);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }
    }

    /**
     * 南交所重置交易密码
     * @param traderId 交易员编号
     * @param newPwd 密码
     * @param firmId 交易商编号
     * @param adminToken 管理员登录token
     * @throws Exception
     */
    public void doNJSResetTradePwdCC(String traderId, String newPwd, String firmId, String adminToken) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSAdminDoResetPwdReq req = new FNJSAdminDoResetPwdReq();
        req.setTOKEN(adminToken);
        req.setUSERID(firmId);
        req.setTRADERID(traderId);
        req.setTRADEPWD(newPwd);
        req.setIPADDRESS("127.0.0.1");
        //处理
        FNJSAdminDoResetPwdRes res = doProcess.getResult(FNJSAdminDoResetPwdRes.class, "016002", req);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 上金所重置交易密码
     * @param traderId 交易员编号
     * @param newPwd 新密码
     * @throws Exception
     */
    public void doSJSResetTradePwdCC(String traderId, String newPwd) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("open-sjs");
        //构造请求对象
        FSJSAdminDoResetTradePwdReq req = new FSJSAdminDoResetTradePwdReq();
        req.setAcct_no(traderId);
        req.setNew_exch_pwd(MD5Util.md5(newPwd).toLowerCase());

        FSJSAdminDoResetTradePwdRes res = doProcess.getResult(FSJSAdminDoResetTradePwdRes.class, "880160", traderId, req);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }
    }

    /**
     * 南交所入金
     * @param token 登录token
     * @param traderId 交易员编号
     * @param req 请求对象
     * @param gjsAccountEntity 用户实体
     * @throws Exception
     */
    public void doNJSTransferInCC(String token, String traderId, FDoTransferInReq req, GJSAccountEntity gjsAccountEntity) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoBankTransferInReq reqSend = new FNJSDoBankTransferInReq();
        reqSend.setTOKEN(token);
        reqSend.setCUSTBANKACCTNO(gjsAccountEntity.getBankCard());
        reqSend.setCUSTTRADEID(traderId);
        reqSend.setCUSTBANKPASS(req.getBankPwd());
        reqSend.setCUSTMONEYPWD(req.getFundsPwd());
        reqSend.setBUSINTYPE("0");
        reqSend.setMONEYSTY("0");
        reqSend.setMONEYTYPE("0");
        reqSend.setCHANGEMONEY(req.getMoney().toString());
        reqSend.setMEMO("贵金属转入资金");
        reqSend.setBANKID(gjsAccountEntity.getBankId());
        reqSend.setTRADERID(traderId);
        //处理
        FNJSDoBankTransferInRes res = doProcess.getResult(FNJSDoBankTransferInRes.class, "013004", reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getSTATE().equals("0") || res.getBANKWATERID() == null || res.getBANKWATERID().equals("")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 上金所入金
     * @param token 登录token
     * @param req 请求对象
     * @throws Exception
     */
    public void doSJSTransferInCC(String token, FDoTransferInReq req) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("trade-sjs");
        //构造请求对象
        FSJSDoBankTransferReq reqSend = new FSJSDoBankTransferReq();
        reqSend.setAccess_way("1");
        reqSend.setExch_bal(req.getMoney().toString());
        reqSend.setFund_pwd(MD5Util.md5(req.getFundsPwd()).toLowerCase());
        //处理
        FSJSDoBankTransferRes res = doProcess.getResult(FSJSDoBankTransferRes.class, "302101", token, reqSend);
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }
    }

    /**
     * 南交所出金
     * @param token 登录token
     * @param traderId 交易员编号
     * @param req 请求对象
     * @param gjsAccountEntity 用户实体
     * @throws Exception
     */
    public void doNJSTransferOutCC(String token, String traderId, FDoTransferOutReq req, GJSAccountEntity gjsAccountEntity) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoBankTransferOutReq reqSend = new FNJSDoBankTransferOutReq();
        reqSend.setTOKEN(token);
        reqSend.setCUSTBANKACCTNO(gjsAccountEntity.getBankCard());
        reqSend.setCUSTTRADEID(traderId);
        reqSend.setCUSTBANKPASS(req.getBankPwd());
        reqSend.setCUSTMONEYPWD(req.getFundsPwd());
        reqSend.setBUSINTYPE("0");
        reqSend.setMONEYSTY("0");
        reqSend.setMONEYTYPE("0");
        reqSend.setCHANGEMONEY(req.getMoney().toString());
        reqSend.setMEMO("贵金属转出资金");
        reqSend.setBANKID(gjsAccountEntity.getBankId());
        reqSend.setTRADERID(traderId);
        //处理
        FNJSDoBankTransferInRes res = doProcess.getResult(FNJSDoBankTransferInRes.class, "013005", reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getSTATE().equals("0") || res.getBANKWATERID() == null || res.getBANKWATERID().equals("")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 上金所出金
     * @param token 登录token
     * @param req 请求对象
     * @throws Exception
     */
    public void doSJSTransferOutCC(String token, FDoTransferOutReq req) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("trade-sjs");
        //构造请求对象
        FSJSDoBankTransferReq reqSend = new FSJSDoBankTransferReq();
        reqSend.setAccess_way("2");
        reqSend.setExch_bal(req.getMoney().toString());
        reqSend.setFund_pwd(MD5Util.md5(req.getFundsPwd()).toLowerCase());
        //处理
        FSJSDoBankTransferRes res = doProcess.getResult(FSJSDoBankTransferRes.class, "302101", token, reqSend);
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }
    }

    /**
     * 南交所查询今日出入金记录
     * @param token 登录token
     * @param traderId 交易员编号
     * @return FNJSQueryTransferRes
     * @throws Exception
     */
    public FNJSQueryTransferRes queryNJSTransferCC(String token, String traderId) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryTransferReq reqSend = new FNJSQueryTransferReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        FNJSQueryTransferRes res = doProcess.getResult(FNJSQueryTransferRes.class, "011044", reqSend);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }

    /**
     * 上金所查询今日出入金记录
     * @param token 用户token
     * @return FSJSQueryTransferRes
     * @throws Exception
     */
    public FSJSQueryTransferRes querySJSTransferCC(String token) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("tradeQuery-sjs");
        //构造请求对象
        FSJSQueryTransferReq reqSend = new FSJSQueryTransferReq();
        reqSend.setPaginal_num("10000");
        reqSend.setCurr_page("1");
        //处理
        FSJSQueryTransferRes res = doProcess.getResult(FSJSQueryTransferRes.class, "600201", token, reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        } else {
            return res;
        }
    }

    /**
     * 南交所开户
     * @param req 请求对象
     * @param mobile 用户手机号码
     * @param bankNo 银行实体
     * @param userTraderId 用户交易员编号
     * @return FNJSDoOpenAccountRes
     * @throws Exception
     */
    public FNJSDoOpenAccountRes doOpenAccountNJSCC(FDoOpenAccountNJSReq req, String mobile, ENJSBankNo bankNo, List<String> userTraderId, ENJSOPENBANK openBank) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoOpenAccountReq reqSend = new FNJSDoOpenAccountReq();
        reqSend.setFULLNAME(req.getRealName());
        reqSend.setTELNO(mobile);
        reqSend.setFIRMCARDTYPE("1");
        reqSend.setFIRMCARDNO(req.getIdCard());
        reqSend.setSIGNBANKNAME(bankNo.getName());
        reqSend.setBANKNO(req.getBankCard());
        reqSend.setTRADERID(userTraderId.get(1));
        reqSend.setFIRMID(userTraderId.get(0));
        reqSend.setBANKCODE(bankNo.getCode());
        reqSend.setTRADEPWD(req.getTradePwd());
        reqSend.setBANKPWD(req.getFundsPwd());
        if(openBank != null){
            reqSend.setOPENBANKNAME(openBank.getName());
        }

        //处理
        FNJSDoOpenAccountRes res = doProcess.getResult(FNJSDoOpenAccountRes.class, "014009", reqSend);
        //处理异常
        if (!res.getSTATE().equals("0") || res.getTRADERID() == null || res.getTRADERID().equals("")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }

    /**
     * 上金所开户
     * @param req 请求对象
     * @param mobile 用户手机号码
     * @return String
     * @throws Exception
     */
    public FSJSDoOpenAccountRes doOpenAccountSJSCC(FDoOpenAccountSJSReq req, String mobile) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("open-sjs");
        //构造请求对象
        FSJSDoOpenAccountReq reqSend = new FSJSDoOpenAccountReq();
        reqSend.setBk_acct_no(req.getBankCard());
        reqSend.setCust_name(req.getRealName());
        reqSend.setCert_type_id("");
        reqSend.setCert_num(req.getIdCard());
        reqSend.setBranch_id("B0077001");
        reqSend.setGrade_id("007701");
        reqSend.setExch_pwd(MD5Util.md5(req.getTradePwd()).toLowerCase());
        reqSend.setFund_pwd(MD5Util.md5(req.getFundsPwd()).toLowerCase());
        reqSend.setArea_code("110000");
        reqSend.setMobile_phone(mobile);
        reqSend.setTel(mobile);
        reqSend.setAddr("北京市海淀区上地三街金隅嘉华大厦");
        reqSend.setZipcode("100000");
        reqSend.setBroker_list("00771");
        if(req.getSmsCode() == null || req.getSmsCode().equals("")) req.setSmsCode("######");
        reqSend.setSms_validatecode(req.getSmsCode());
        reqSend.setScore_riskevaluation(req.getRisk());
        //处理
        FSJSDoOpenAccountRes res = doProcess.getResult(FSJSDoOpenAccountRes.class, "880120", "", reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000") || res.getRecord() == null || res.getRecord().size() <= 0) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        } else {
            return res;
        }
    }

    /**
     * 更新南交所是否签约
     * @param accountEntity 开户表信息
     * @throws Exception
     */
    public FNJSQueryTradeUserIsSignRes queryIsSignNJSCC(GJSAccountEntity accountEntity) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryTradeUserIsSignReq req = new FNJSQueryTradeUserIsSignReq();
        req.setTRADERID(accountEntity.getTraderId());
        //处理
        FNJSQueryTradeUserIsSignRes res = doProcess.getResult(FNJSQueryTradeUserIsSignRes.class, "014007", req);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }

    /**
     * 上金所上传图片
     * @param is 图片输入流
     * @param idCard 身份证号码
     * @throws Exception
     */
    public void doUploadImgSJSCC(InputStream is, String idCard) throws Exception{
        //上传文件
        String postResult = CommonUtils.postImg(is, idCard + ".jpg", Constants.SJS_UPLOAD_URL);
        //判断
        HashMap<String, String> jsonResult = JsonUtil.toObject(postResult, HashMap.class);
        if(!jsonResult.containsKey("code") || !jsonResult.get("code").equals("001")){
            throw new CustomerException(EErrorCode.ERROR_CODE_300009.getMessage(), EErrorCode.ERROR_CODE_300009.getCode());
        }
    }

    /**
     * 上金所查询照片审核状态
     * @param traderId 交易员编号
     * @return 审核状态
     * @throws Exception
     */
    public String queryUploadStatusSJSCC(String traderId) throws Exception{
        String queryResult = HttpClientUtils.getString(Constants.SJS_QUERY_URL + "?acctNo=" + traderId);
        logger.info("查询审核状态信息服务返回结果：" + queryResult);
        HashMap<String, String> jsonResult = JsonUtil.toObject(queryResult, HashMap.class);
        if(jsonResult.containsKey("state")){
            return jsonResult.get("state");
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300010.getMessage(), EErrorCode.ERROR_CODE_300010.getCode());
        }
    }

    /**
     * 查询上金所签约状态
     * @param traderId 交易员编号
     * @return FSJSQuerySignedBankCodeRes
     * @throws Exception
     */
    public FSJSQuerySignedBankCodeRes queryIsSignSJSCC(String traderId) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("trade-sjs");
        //构造请求对象
        FSJSQuerySignedBankCodeReq req = new FSJSQuerySignedBankCodeReq();
        //处理
        FSJSQuerySignedBankCodeRes res = doProcess.getResult(FSJSQuerySignedBankCodeRes.class, "302401", traderId, req);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000") || res.getRecord() == null || res.getRecord().size() <= 0) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }else{
            return res;
        }
    }

    /**
     * 上金所开通出入金功能
     * @param accountEntity 用户信息
     * @throws Exception
     */
    public void doOpenFinanceSJSCC(GJSAccountEntity accountEntity) throws Exception{
        //初始化处理
        SJSDoProcess doProcessOpen = new SJSDoProcess("open-sjs");
        //构造请求对象
        FSJSDoOpenFinanceReq reqOpen = new FSJSDoOpenFinanceReq();
        reqOpen.setAcct_no(accountEntity.getTraderId());
        reqOpen.setBank_no(accountEntity.getBankId());
        reqOpen.setBk_mer_code("123456789");
        reqOpen.setBk_acct_name(accountEntity.getRealName());
        reqOpen.setBk_acct_no(accountEntity.getBankId() + ":" + accountEntity.getBankCard());
        reqOpen.setBk_cert_type("A");
        reqOpen.setBk_cert_no(accountEntity.getIdCard());
        FSJSDoOpenFinanceRes resOpen = doProcessOpen.getResult(FSJSDoOpenFinanceRes.class, "880170", "", reqOpen);
        if (!resOpen.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + resOpen.getH_rsp_code() + ", 上金所错误信息：" + resOpen.getH_rsp_msg());
            throw new CustomerException(resOpen.getH_rsp_code(), resOpen.getH_rsp_msg(), "");
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
    public void doNJSChangeBankCardCC(String token, String traderId, String bankCode, String bankNo) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoChangeBankCardReq req = new FNJSDoChangeBankCardReq();
        req.setTOKEN(token);
        req.setTRADERID(traderId);
        req.setBANKID(bankCode);
        req.setBANKNO(bankNo);
        FNJSDoChangeBankCardRes res = doProcess.getResult(FNJSDoChangeBankCardRes.class, "014010", req);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 南交所查询套餐列表
     * @return FNJSQueryPackageListRes
     * @throws Exception
     */
    public FNJSQueryPackageListRes queryNJSPackageListCC() throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryPackageListReq req = new FNJSQueryPackageListReq();
        //处理
        FNJSQueryPackageListRes res = doProcess.getResult(FNJSQueryPackageListRes.class, "011043", req);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }

    /**
     * 南交所查询套餐信息
     * @param token 登录token
     * @param traderId 交易员编号
     * @return FNJSQueryPackageInfoRes
     * @throws Exception
     */
    public FNJSQueryPackageInfoRes queryNJSPackageInfoCC(String token, String traderId) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryPackageInfoReq req = new FNJSQueryPackageInfoReq();
        req.setTOKEN(token);
        req.setTRADERID(traderId);
        //处理
        FNJSQueryPackageInfoRes res = doProcess.getResult(FNJSQueryPackageInfoRes.class, "011041", req);
        //处理异常
        if (!res.getSTATE().equals("0") || res.getDATAS() == null || res.getDATAS().size() <= 0) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }

    /**
     * 南交所查询套餐申请记录
     * @param token 登录token
     * @param traderId 交易员编号
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return FNJSQueryPackageHistoryRes
     * @throws Exception
     */
    public FNJSQueryPackageHistoryRes queryNJSPackageHistoryCC(String token, String traderId, String startDate, String endDate) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryPackageHistoryReq req = new FNJSQueryPackageHistoryReq();
        req.setTOKEN(token);
        req.setTRADERID(traderId);
        req.setBEGINDATE(startDate);
        req.setENDDATE(endDate);
        //处理
        FNJSQueryPackageHistoryRes res = doProcess.getResult(FNJSQueryPackageHistoryRes.class, "011040", req);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }

    /**
     * 南交所购买交易套餐
     * @param token 登录token
     * @param traderId 交易员编号
     * @param packageId 套餐编号
     * @throws Exception
     */
    public void doNJSPackageBuyCC(String token, String traderId, String packageId) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoPackageBuyReq req = new FNJSDoPackageBuyReq();
        req.setTOKEN(token);
        req.setTRADERID(traderId);
        req.setPACKAGEID(packageId);
        req.setIPADDRESS("127.0.0.1");
        //处理
        FNJSDoPackageBuyRes res = doProcess.getResult(FNJSDoPackageBuyRes.class, "011039", req);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 南交所代交收协议
     * @param token 登录token
     * @param traderId 交易员编号
     * @throws Exception
     */
    public void doNJSSignAgreementCC(String token, String traderId) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoSignAgreementReq req = new FNJSDoSignAgreementReq();
        req.setTOKEN(token);
        req.setTRADERID(traderId);
        //处理
        FNJSDoSignAgreementRes res = doProcess.getResult(FNJSDoSignAgreementRes.class, "011029", req);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 上金所查询用户个人信息
     * @param traderId 交易员编号
     * @throws Exception
     */
    public FSJSQueryTraderRes querySJSTraderCC(String traderId) throws Exception{
        //初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("open-sjs");
        //构造请求对象
        FSJSQueryTraderReq req = new FSJSQueryTraderReq();
        req.setAcct_no(traderId);

        FSJSQueryTraderRes res = doProcess.getResult(FSJSQueryTraderRes.class, "880140", traderId, req);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        //处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }else{
            return res;
        }
    }

    /**
     * 上金所查询历史委托
     * @param traderId 交易员编号
     * @return FSJSQueryEntrustHistoryRes
     * @throws Exception
     */
    public FSJSQueryEntrustHistoryRes querySJSEntrustHistoryCC(String traderId) throws Exception{
        // 初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("tradeQuery-sjs");
        // 构造请求对象
        FSJSQueryEntrustHistoryReq reqSend = new FSJSQueryEntrustHistoryReq();
        reqSend.setStart_date(DateUtil.addDays(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, new Date()), DateUtil.DATE_FORMAT_STRING, -1));
        reqSend.setEnd_date(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, new Date()));
        reqSend.setPaginal_num("100000");
        reqSend.setCurr_page("1");
        FSJSQueryEntrustHistoryRes res = doProcess.getResult(FSJSQueryEntrustHistoryRes.class, "600204", traderId, reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        // 处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }else{
            return res;
        }
    }

    /**
     * 上金所查询历史成交
     * @param traderId 交易员编号
     * @return FSJSQueryEntrustHistoryRes
     * @throws Exception
     */
    public FSJSQueryMatchHistoryRes querySJSMatchHistoryCC(String traderId) throws Exception{
        // 初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("tradeQuery-sjs");
        // 构造请求对象
        FSJSQueryMatchHistoryReq reqSend = new FSJSQueryMatchHistoryReq();
        reqSend.setStart_date(DateUtil.addDays(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, new Date()), DateUtil.DATE_FORMAT_STRING, -1));
        reqSend.setEnd_date(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, new Date()));
        reqSend.setPaginal_num("100000");
        reqSend.setCurr_page("1");
        FSJSQueryMatchHistoryRes res = doProcess.getResult(FSJSQueryMatchHistoryRes.class, "600206", traderId, reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        // 处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }else{
            return res;
        }
    }

    /**
     * 上金所查询历史出入金
     * @param traderId 交易员编号
     * @return FSJSQueryEntrustHistoryRes
     * @throws Exception
     */
    public FSJSQueryTransferHistoryRes querySJSTransferHistoryCC(String traderId) throws Exception{
        // 初始化处理
        SJSDoProcess doProcess = new SJSDoProcess("tradeQuery-sjs");
        // 构造请求对象
        FSJSQueryTransferHistoryReq reqSend = new FSJSQueryTransferHistoryReq();
        reqSend.setStart_date(DateUtil.addDays(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, new Date()), DateUtil.DATE_FORMAT_STRING, -1));
        reqSend.setEnd_date(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, new Date()));
        reqSend.setPaginal_num("100000");
        reqSend.setCurr_page("1");
        FSJSQueryTransferHistoryRes res = doProcess.getResult(FSJSQueryTransferHistoryRes.class, "600216", traderId, reqSend);
        logger.info("格式化返回:" + ToStringBuilder.reflectionToString(res));
        // 处理异常
        if (!res.getH_rsp_code().equals("HJ0000")) {
            logger.error("上金所错误代码：" + res.getH_rsp_code() + ", 上金所错误信息：" + res.getH_rsp_msg());
            throw new CustomerException(res.getH_rsp_code(), res.getH_rsp_msg(), "");
        }else{
            return res;
        }
    }

    /**
     * 南交所中信银行快速签约
     * @param token 登录token
     * @param traderId 交易员编号
     * @param gjsAccountEntity 开户信息
     * @throws Exception
     */
    public void doNJSCNCBSignCC(String token, String traderId, GJSAccountEntity gjsAccountEntity) throws Exception{
        if(gjsAccountEntity.getBankId() == null || gjsAccountEntity.getBankId().equals("")){
            throw new CustomerException(EErrorCode.ERROR_CODE_300005.getMessage(), EErrorCode.ERROR_CODE_300005.getCode());
        }
        ENJSBankNo enjsBankNo = ENJSBankNo.findByCode(gjsAccountEntity.getBankId());
        if(enjsBankNo == null){
            throw new CustomerException(EErrorCode.ERROR_CODE_300005.getMessage(), EErrorCode.ERROR_CODE_300005.getCode());
        }
        ENJSOPENBANK enjsopenbank = ENJSOPENBANK.findByCode(gjsAccountEntity.getOpenBankName());
        if(enjsopenbank == null){
            throw new CustomerException(EErrorCode.ERROR_CODE_300019.getMessage(), EErrorCode.ERROR_CODE_300019.getCode());
        }

        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoCNCBSignReq req = new FNJSDoCNCBSignReq();
        req.setTOKEN(token);
        req.setCUSTTRADEID(traderId);
        req.setBANKID(gjsAccountEntity.getBankId());
        req.setCUSTBANKACCTNO(gjsAccountEntity.getBankCard());
        req.setCARDIDTYPE("1");
        req.setCARDID(gjsAccountEntity.getIdCard());
        req.setFIRMNAME(gjsAccountEntity.getRealName());
        req.setACCBANKNAME(enjsopenbank.getName());
        req.setCUSTOPENTYPE("0");
        //处理
        FNJSDoCNCBSignRes res = doProcess.getResult(FNJSDoCNCBSignRes.class, "014011", req);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }
    }

    /**
     * 中信异度支付入金
     * @param token 登录token
     * @param traderId 交易员编号
     * @param req 请求对象
     * @return FNJSDoCreateTransferNoRes
     * @throws Exception
     */
    public FNJSDoCreateTransferNoRes doNJSCreateTransferInNoCC(String token, String traderId, FDoTransferInReq req) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSDoCreateTransferNoReq reqSend = new FNJSDoCreateTransferNoReq();
        reqSend.setTOKEN(token);
        reqSend.setCUSTTRADEID(traderId);
        reqSend.setCHANGEMONEY(req.getMoney().toString());
        reqSend.setTRADETYPE("1");
        reqSend.setSIGN("0");
        reqSend.setMEMO("财猫贵金属入金");
        reqSend.setTRADEDATE("");
        reqSend.setTRADETIME("");
        reqSend.setOS(req.getOs());
        reqSend.setBANKID("212");
        if(req.getOs() == null || (!req.getOs().equals("1") && !req.getOs().equals("2"))){
            reqSend.setOS("1");
        }
        //处理
        FNJSDoCreateTransferNoRes res = doProcess.getResult(FNJSDoCreateTransferNoRes.class, "013009", reqSend);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }else if(!res.getRETCODE().equals("00000")){
            logger.error("南交所错误代码：" + res.getRETCODE() + ", 南交所错误信息：" + res.getRETINFO());
            throw new CustomerException(res.getRETINFO(), Integer.parseInt(res.getRETCODE()));
        }else{
            return res;
        }
    }

    /**
     * 登录资金管理
     * @param traderId 交易员编号
     * @param fundsPwd 资金密码
     * @return FNJSQueryFundsManageRes
     * @throws Exception
     */
    public FNJSQueryFundsManageRes queryNJSFundsManageCC(String token, String traderId, String fundsPwd) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryFundsManageReq req = new FNJSQueryFundsManageReq();
        req.setTOKEN(token);
        req.setCUSTTRADEID(traderId);
        req.setCUSTMONEYPWD(fundsPwd);
        req.setTRADERID(traderId);
        //处理
        FNJSQueryFundsManageRes res = doProcess.getResult(FNJSQueryFundsManageRes.class, "013001", req);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }else{
            return res;
        }
    }

    /**
     * 查询风险交易商
     * @return FNJSQueryRiskRes
     * @throws Exception
     */
    public FNJSQueryRiskRes queryNJSRiskCC() throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryRiskReq req = new FNJSQueryRiskReq();
        //处理
        FNJSQueryRiskRes res = doProcess.getResult(FNJSQueryRiskRes.class, "011042", req);
        //处理异常
        if (!res.getSTATE().equals("0")) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        }else{
            return res;
        }
    }

    /**
     * 查询单个品种下单信息
     * @param token 登录token
     * @param traderId 交易员编号
     * @param prodCode 商品代码
     * @throws Exception
     */
    public FNJSQueryWareSingleRes queryNJSProdSingleCC(String token, String traderId, String prodCode) throws Exception{
        //初始化处理
        NJSDoProcess doProcess = njsDoProcess;
        //构造请求对象
        FNJSQueryWareSingleReq reqSend = new FNJSQueryWareSingleReq();
        reqSend.setTOKEN(token);
        reqSend.setTRADERID(traderId);
        reqSend.setWAREID(prodCode);
        FNJSQueryWareSingleRes res = doProcess.getResult(FNJSQueryWareSingleRes.class, "011010", reqSend);
        //处理异常
        if (!res.getSTATE().equals("0") || res.getDATAS() == null || res.getDATAS().size() <= 0) {
            logger.error("南交所错误代码：" + res.getSTATE() + ", 南交所错误信息：" + res.getMSG());
            throw new CustomerException(res.getMSG(), Integer.parseInt(res.getSTATE()));
        } else {
            return res;
        }
    }
}