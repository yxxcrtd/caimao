package com.fmall.bana.controller.api.gjs;

import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.bana.common.api.enums.gjs.EGJSExchange;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.gjs.api.entity.GJSAccountEntity;
import com.caimao.gjs.api.entity.req.*;
import com.caimao.gjs.api.entity.res.FDoCreateTransferInNoRes;
import com.caimao.gjs.api.entity.res.FQueryTransferRes;
import com.caimao.gjs.api.service.IAccountService;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.CommonStringUtils;
import com.fmall.bana.utils.crypto.RSA;
import com.fmall.bana.utils.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 贵金属账户API接口
 * Created by Administrator on 2015/10/16.
 */
@Controller
@RequestMapping(value = "/api/gjs_account/")
public class GjsAccountApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(GjsAccountApiController.class);

    @Value("${picturePath}")
    private String picturePath;
    @Resource
    private IAccountService accountService;

    /**
     * <p>修改资金密码</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doModifyFundsPwd</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param oldPwd   旧密码
     * @param newPwd   新密码
     * @return 修改是否成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoModifyFundsPwdReq
     */
    @ResponseBody
    @RequestMapping(value = "/doModifyFundsPwd", method = RequestMethod.POST)
    public Map doModifyFundsPwd(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "oldPwd") String oldPwd,
            @RequestParam(value = "newPwd") String newPwd
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoModifyFundsPwdReq req = new FDoModifyFundsPwdReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setOldPwd(RSA.decodeByPwd(oldPwd));
            req.setNewPwd(RSA.decodeByPwd(newPwd));
            this.accountService.doModifyFundsPwd(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>修改交易密码</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doModifyTradePwd</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param oldPwd   旧密码
     * @param newPwd   新密码
     * @return 修改是否成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoModifyTradePwdReq
     */
    @ResponseBody
    @RequestMapping(value = "/doModifyTradePwd", method = RequestMethod.POST)
    public Map doModifyTradePwd(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "oldPwd") String oldPwd,
            @RequestParam(value = "newPwd") String newPwd
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoModifyTradePwdReq req = new FDoModifyTradePwdReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setOldPwd(RSA.decodeByPwd(oldPwd));
            req.setNewPwd(RSA.decodeByPwd(newPwd));
            this.accountService.doModifyTradePwd(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>找回资金密码</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doResetFundsPwd</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param smsCode  短信验证码
     * @param newPwd   新密码
     * @return 找回是否成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoResetFundsPwdReq
     */
    @ResponseBody
    @RequestMapping(value = "/doResetFundsPwd", method = RequestMethod.POST)
    public Map doResetFundsPwd(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "smsCode") String smsCode,
            @RequestParam(value = "newPwd") String newPwd
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoResetFundsPwdReq req = new FDoResetFundsPwdReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setSmsCode(smsCode);
            req.setNewPwd(RSA.decodeByPwd(newPwd));
            this.accountService.doResetFundsPwd(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>找回交易密码</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doResetTradePwd</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param smsCode  短信验证码
     * @param newPwd   新密码
     * @return 找回是否成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoResetTradePwdReq
     */
    @ResponseBody
    @RequestMapping(value = "/doResetTradePwd", method = RequestMethod.POST)
    public Map doResetTradePwd(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "smsCode") String smsCode,
            @RequestParam(value = "newPwd") String newPwd
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoResetTradePwdReq req = new FDoResetTradePwdReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setSmsCode(smsCode);
            req.setNewPwd(RSA.decodeByPwd(newPwd));
            this.accountService.doResetTradePwd(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>入金</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doTransferIn</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param fundsPwd 资金密码
     * @param bankPwd  银行卡密码
     * @param money    金额
     * @return 入金是否成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoTransferInReq
     */
    @ResponseBody
    @RequestMapping(value = "/doTransferIn", method = RequestMethod.POST)
    public Map doTransferIn(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "fundsPwd") String fundsPwd,
            @RequestParam(value = "bankPwd") String bankPwd,
            @RequestParam(value = "money") String money
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoTransferInReq req = new FDoTransferInReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setFundsPwd(RSA.decodeByPwd(fundsPwd));
            req.setBankPwd(RSA.decodeByPwd(bankPwd));
            req.setMoney(new BigDecimal(money).setScale(2, BigDecimal.ROUND_DOWN));
            this.accountService.doTransferIn(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>出金</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doTransferOut</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param fundsPwd 资金密码
     * @param bankPwd  银行卡密码
     * @param money    金额
     * @return 出金是否成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoTransferOutReq
     */
    @ResponseBody
    @RequestMapping(value = "/doTransferOut", method = RequestMethod.POST)
    public Map doTransferOut(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "fundsPwd") String fundsPwd,
            @RequestParam(value = "bankPwd") String bankPwd,
            @RequestParam(value = "money") String money
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoTransferOutReq req = new FDoTransferOutReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setFundsPwd(RSA.decodeByPwd(fundsPwd));
            req.setBankPwd(RSA.decodeByPwd(bankPwd));
            req.setMoney(new BigDecimal(money).setScale(2, BigDecimal.ROUND_DOWN));
            this.accountService.doTransferOut(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询今日出入金记录</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/queryTransfer</p>
     * <p>请求方式：GET</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 今日出入金记录
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoTransferOutReq
     * <p/>
     * <p>返回对象</p>
     * @see FQueryTransferRes
     */
    @ResponseBody
    @RequestMapping(value = "/queryTransfer", method = {RequestMethod.GET, RequestMethod.POST})
    public Map queryTransfer(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            return CommonStringUtils.mapReturn(this.accountService.queryTransfer(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询用户开户信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/queryGJSAccount</p>
     * <p>请求方式：GET POST</p>
     *
     * @param token 登录返回token
     * @return 开户信息列表
     * <p/>
     * <p>返回对象</p>
     * @throws Exception
     * @see GJSAccountEntity
     */
    @ResponseBody
    @RequestMapping(value = "/queryGJSAccount", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, GJSAccountEntity> queryGJSAccount(
            @RequestParam(value = "token") String token
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            return this.accountService.queryGJSAccount(userId);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>南交所开户</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doOpenAccountNJS</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param realName 真实姓名
     * @param idCard   身份证账号
     * @param bankNo   银行编号
     * @param bankCard 银行卡账号
     * @param tradePwd 交易密码
     * @param fundsPwd 资金密码
     * @return 交易员编号
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoOpenAccountNJSReq
     */
    @ResponseBody
    @RequestMapping(value = "/doOpenAccountNJS", method = RequestMethod.POST)
    public Map doOpenAccountNJS(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "realName") String realName,
            @RequestParam(value = "idCard") String idCard,
            @RequestParam(value = "bankNo") String bankNo,
            @RequestParam(value = "bankCard") String bankCard,
            @RequestParam(value = "tradePwd") String tradePwd,
            @RequestParam(value = "fundsPwd") String fundsPwd,
            @RequestParam(value = "openBankCode", required = false) String openBankCode
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoOpenAccountNJSReq req = new FDoOpenAccountNJSReq();
            req.setUserId(userId);
            if(realName != null && !realName.equals("") && realName.equals(new String(realName.getBytes("iso8859-1"), "iso8859-1"))){
                realName = new String(realName.getBytes("iso8859-1"),"utf-8");
            }
            req.setRealName(realName);
            req.setIdCard(idCard);
            req.setBankNo(bankNo);
            req.setBankCard(bankCard);
            req.setTradePwd(RSA.decodeByPwd(tradePwd));
            req.setFundsPwd(RSA.decodeByPwd(fundsPwd));
            req.setOpenBankCode(openBankCode);
            return CommonStringUtils.mapReturn(this.accountService.doOpenAccountNJS(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>上金所开户</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doOpenAccountSJS</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param realName 真实姓名
     * @param idCard   身份证账号
     * @param bankNo   银行编号
     * @param bankCard 银行卡账号
     * @param tradePwd 交易密码
     * @param fundsPwd 资金密码
     * @return 交易员编号
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoOpenAccountNJSReq
     */
    @ResponseBody
    @RequestMapping(value = "/doOpenAccountSJS", method = RequestMethod.POST)
    public Map doOpenAccountSJS(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "realName") String realName,
            @RequestParam(value = "idCard") String idCard,
            @RequestParam(value = "bankNo") String bankNo,
            @RequestParam(value = "bankCard") String bankCard,
            @RequestParam(value = "tradePwd") String tradePwd,
            @RequestParam(value = "fundsPwd") String fundsPwd,
            @RequestParam(value = "risk") String risk,
            @RequestParam(value = "smsCode") String smsCode
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoOpenAccountSJSReq req = new FDoOpenAccountSJSReq();
            req.setUserId(userId);
            if(realName != null && !realName.equals("") && realName.equals(new String(realName.getBytes("iso8859-1"), "iso8859-1"))){
                realName = new String(realName.getBytes("iso8859-1"),"utf-8");
            }
            req.setRealName(realName);
            req.setIdCard(idCard);
            req.setBankNo(bankNo);
            req.setBankCard(bankCard);
            req.setTradePwd(RSA.decodeByPwd(tradePwd));
            req.setFundsPwd(RSA.decodeByPwd(fundsPwd));
            req.setRisk(risk);
            req.setSmsCode(smsCode);
            return CommonStringUtils.mapReturn(this.accountService.doOpenAccountSJS(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>提交身份证照片</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doUploadCard</p>
     * <p>请求方式：POST</p>
     *
     * @param token        登录token
     * @param exchange     交易所代码 NJS 南交所、SJS上金所
     * @param cardPositive 身份证正面
     * @param cardObverse  身份证反面
     * @return 提交是否成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoUploadCardReq
     */
    @ResponseBody
    @RequestMapping(value = "/doUploadCard", method = RequestMethod.POST)
    public Map doUploadCard(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "cardPositive") String cardPositive,
            @RequestParam(value = "cardObverse") String cardObverse
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoUploadCardReq req = new FDoUploadCardReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setCardPositive(cardPositive);
            req.setCardObverse(cardObverse);
            this.accountService.doUploadCard(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询交易所支持银行卡列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/queryExchangeBank</p>
     * <p>请求方式：POST</p>
     *
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 银行列表 List<Map<String, String>>
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryExchangeBank", method = RequestMethod.GET)
    public Map queryExchangeBank(
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            return CommonStringUtils.mapReturn(this.accountService.queryExchangeBankList(exchange));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询交易所开户行名称列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/queryOpenBankName</p>
     * <p>请求方式：POST</p>
     *
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 银行列表 List<Map<String, String>>
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryOpenBankName", method = RequestMethod.GET)
    public Map queryOpenBankName(
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            return CommonStringUtils.mapReturn(this.accountService.queryOpenBankList(exchange));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>变更银行卡号码</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doChangeBankCard</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @param bankId   银行编号
     * @param bankNo   银行卡号
     * @return 修改是否成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoChangeBankCardReq
     */
    @ResponseBody
    @RequestMapping(value = "/doChangeBankCard", method = RequestMethod.POST)
    public Map doChangeBankCard(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "bankId") String bankId,
            @RequestParam(value = "bankNo") String bankNo
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoChangeBankCardReq req = new FDoChangeBankCardReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setBankId(bankId);
            req.setBankNo(bankNo);
            this.accountService.doChangeBankCard(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>签订代交收协议</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doSignAgreement</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 签署是否成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryTradeCommonReq
     */
    @ResponseBody
    @RequestMapping(value = "/doSignAgreement", method = RequestMethod.POST)
    public Map doSignAgreement(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            this.accountService.doSignAgreement(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>上金所绑定账号</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doBindAccountSJS</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param traderId 交易原编号
     * @param tradePwd 交易密码
     * @return 是否登录并绑定成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoBindAccountSJSReq
     */
    @ResponseBody
    @RequestMapping(value = "/doBindAccountSJS", method = RequestMethod.POST)
    public Map doBindAccountSJS(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "traderId") String traderId,
            @RequestParam(value = "tradePwd") String tradePwd
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoBindAccountSJSReq req = new FDoBindAccountSJSReq();
            req.setUserId(userId);
            req.setTraderId(traderId);
            req.setTraderPwd(RSA.decodeByPwd(tradePwd));
            this.accountService.doBindAccountSJS(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>银行快速签约</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doBankSign</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param exchange 交易所代码 NJS 南交所、SJS上金所
     * @return 签署是否成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FQueryTradeCommonReq
     */
    @ResponseBody
    @RequestMapping(value = "/doBankSign", method = RequestMethod.POST)
    public Map doBankSign(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);

            FQueryTradeCommonReq req2 = new FQueryTradeCommonReq();
            req2.setUserId(userId);
            req2.setExchange(exchange);
            this.accountService.doSignAgreement(req2);
            FQueryTradeCommonReq req = new FQueryTradeCommonReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            this.accountService.doBankSign(req);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>南交所中信银行入金</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/doCreateTransferInNo</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登录返回token
     * @param fundsPwd 资金密码
     * @param money    金额
     * @param os 客户端类型 0：PC 1：ios 2:android
     * @return 入金是否成功
     * <p/>
     * <p>请求对象</p>
     * @throws Exception
     * @see FDoTransferInReq
     */
    @ResponseBody
    @RequestMapping(value = "/doCreateTransferInNo", method = RequestMethod.POST)
    public FDoCreateTransferInNoRes doCreateTransferInNo(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "fundsPwd") String fundsPwd,
            @RequestParam(value = "money") String money,
            @RequestParam(value = "os", required = false, defaultValue = "0") String os
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FDoTransferInReq req = new FDoTransferInReq();
            req.setUserId(userId);
            req.setExchange(EGJSExchange.NJS.getCode());
            req.setFundsPwd(RSA.decodeByPwd(fundsPwd));
            req.setMoney(new BigDecimal(money).setScale(2, BigDecimal.ROUND_DOWN));
            req.setOs(os);
            return this.accountService.doCreateTransferInNo(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询南交所可用账号数量</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjs_account/queryCanUseNum</p>
     * <p>请求方式：GET</p>
     */
    @ResponseBody
    @RequestMapping(value = "/queryCanUseNum", method = RequestMethod.GET)
    public Integer queryCanUseNum() throws Exception{
        try {
            return this.accountService.queryCanUseNum();
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("错误信息{}：", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }
}