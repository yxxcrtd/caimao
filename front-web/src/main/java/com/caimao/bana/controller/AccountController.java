    package com.caimao.bana.controller;

import com.caimao.bana.api.entity.TpzUserBankCardEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.req.*;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IAccountService;
import com.caimao.bana.api.service.IUserBankCardService;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.utils.DateUtil;
import com.caimao.bana.utils.PageUtils;
import com.caimao.bana.utils.StringHandleUtils;
import com.hsnet.pz.ao.account.IHomsAccountAO;
import com.hsnet.pz.ao.financing.IFinancingAO;
import com.hsnet.pz.ao.infrastructure.IBasicDataAO;
import com.hsnet.pz.ao.util.RSAUtils;
import com.hsnet.pz.biz.account.dto.res.F830101Res;
import com.hsnet.pz.biz.homs.dto.res.F830312Res;
import com.hsnet.pz.biz.loan.dto.req.F830204Req;
import com.hsnet.pz.biz.loan.dto.res.F830204Res;
import com.hsnet.pz.biz.user.dto.req.F830014Req;
import com.hsnet.pz.biz.user.dto.res.F830016Res;
import com.hsnet.pz.biz.user.dto.res.F830017Res;
import com.hsnet.pz.biz.user.dto.res.F830018Res;
import com.hsnet.pz.controller.BaseController;
import com.hsnet.pz.core.exception.BizServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 账户资产相关控制器方法
 * Created by WangXu on 2015/5/26.
 */
@Controller
@RequestMapping(value = "/account")
public class AccountController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Resource
    private IAccountService accountService;
    @Resource
    private IUserBankCardService userBankCardService;
    @Resource
    private IUserService userService;
    @Autowired
    IBasicDataAO basicDataAO;
    @Autowired
    private IHomsAccountAO homsAccountAO;
    @Autowired
    private IFinancingAO financingAO;

    /**
     * 我的账户页面控制器
     * @return
     */
    @RequestMapping(value = "/index.htm", method = RequestMethod.GET)
    public ModelAndView index() {
        String productType = null;
        String startDate = null;
        String endDate = null;
        Integer start = 0;
        Integer limit = 50;
        ModelAndView mav = new ModelAndView("account/index");
        // 账户信息
        F830101Res account = null;
        try {
            account = basicDataAO.getPZAccount(this.getSessionUser().getUser_id());
        } catch (Exception e) {

        }
        mav.addObject("pzAccount", account);//账户余额
        // 获取当前所有的合约借款信息
        F830204Req req = new F830204Req();
        try {
            req = financingAO.queryPaginableContract(getSessionUser().getUser_id(), productType, startDate, endDate, start, limit);
        } catch (Exception e) {}
        // 存放结果的变量
        List<Map<String, Object>> contracts = new ArrayList<>();
        if (req.getItems() != null) for (F830204Res res : req.getItems()) {
            Map<String, Object> map = new HashMap<>();
            // 合约ID，借款金额，保证金，费用，到期日，下次结息日，管理费，合约时间，合约类型，产品名称
            map.put("contractNo", res.getContractNo());
            map.put("loanAmount", res.getLoanAmount());
            map.put("marginAmount", res.getCashAmount());
            map.put("interestRate", res.getInterestRate());
            map.put("stopDatetime", res.getContractEndDate());
            map.put("nextSettleDate", res.getNextSettleInterestDate());
            map.put("settledInterest", res.getSettledInterest());
            map.put("createDatetime", res.getContractSignDatetime());
            map.put("contractType", res.getContractType());
            map.put("prodName", res.getProdName());
            map.put("prodType", res.getProdTypeId());
            // 获取合约资金信息
            F830312Res homsRes = null;
            try {
                homsRes = homsAccountAO.getHomsAssetsInfo(this.getSessionUser().getUser_id(), res.getHomsFundAccount(), res.getHomsCombineId());
            } catch (Exception e) {

            }
            // 总操盘资金，股票市值，可用金额，亏损警告线，亏损平仓线，浮动盈亏
            map.put("totalAsset", homsRes == null ? "--" : homsRes.getTotalAsset());
            map.put("totalMarketValue", homsRes == null ? "--" : homsRes.getTotalMarketValue());
            map.put("enableWithdraw", homsRes == null ? "--" : homsRes.getEnableWithdraw());
            map.put("curAmount", homsRes == null ? "--" : homsRes.getCurAmount());
            map.put("enableRatioLine", homsRes == null ? "--" : homsRes.getLoanAmount() * homsRes.getEnableRatio());
            map.put("exposureRatioLine", homsRes == null ? "--" : homsRes.getLoanAmount() * homsRes.getExposureRatio());
            map.put("totalProfit", homsRes == null ? "--" : homsRes.getTotalProfit());

            // 查询合约是否可以 追加、还款、展期、转出盈利
            //map.put("isRepay", financingAO.judgeRepayPremise(this.getSessionUser().getUser_id(), res.getContractNo()));

            contracts.add(map);
        }
        mav.addObject("contractList", contracts);
        return mav;
    }

    /**
     * 网银充值的页面
     * @return
     */
    @RequestMapping(value = "/recharge/wangyin.htm", method = RequestMethod.GET)
    public ModelAndView wangyin() {
        ModelAndView mav = new ModelAndView("account/recharge/wangyin");
        // 获得可用余额
        F830101Res res = null;
        try{
            res = basicDataAO.getPZAccount(this.getSessionUser().getUser_id());
        }catch(Exception ignored){}
        mav.addObject("balance", res);

        return mav;
    }

    /**
     * 支付宝充值的页面
     * @return
     */
    @RequestMapping(value = "/recharge/zhifubao.htm", method = RequestMethod.GET)
    public ModelAndView zhifubao() {
        ModelAndView mav = new ModelAndView("account/recharge/zhifubao");
        // 获得可用余额
        F830101Res res = null;
        try {
            res = basicDataAO.getPZAccount(this.getSessionUser().getUser_id());
        } catch (Exception e) {}
        mav.addObject("balance", res);//账户余额
        return mav;
    }

    /**
     * 银行转账充值的页面
     * @return
     */
    @RequestMapping(value = "/recharge/yinhang.htm", method = RequestMethod.GET)
    public ModelAndView yinhang() {
        ModelAndView mav = new ModelAndView("account/recharge/yinhang");
        // 获得可用余额
        F830101Res res = null;
        try {
            res = basicDataAO.getPZAccount(this.getSessionUser().getUser_id());
        } catch (Exception e) {}
        mav.addObject("balance", res);//账户余额
        return mav;
    }

    /**
     * 绑定银行卡的页面
     * @return
     */
    @RequestMapping(value = "/bankcard.htm", method = RequestMethod.GET)
    public ModelAndView bankcard() throws Exception {
        ModelAndView mav = new ModelAndView("account/bankcard");
        List<TpzUserBankCardEntity> list = userBankCardService.queryUserBankList(this.getSessionUser().getUser_id(), "1");
        TpzUserBankCardEntity card = null;
        if(list.size() != 0){
            card = list.get(0);
        }
        //System.out.println(ToStringBuilder.reflectionToString(card));
        mav.addObject("card", card);
        // 获取用户的实名认证信息
        TpzUserEntity userEntity = this.userService.getById(this.getSessionUser().getUser_id());
        mav.addObject("userEntity", userEntity);
        return mav;
    }

    /**
     * 充值历史记录页面
     * @return
     */
    @RequestMapping(value = "/recharge/history.htm", method = RequestMethod.GET)
    public String history(
            Model model,
            @RequestParam(value = "orderStatus", required = false) String orderStatus,
            @RequestParam(value = "dateStep", required = false, defaultValue = "0") Integer dateStep,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception{

        String startDate = StringHandleUtils.getStartDate(dateStep);
        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() + 86400 * 1000));
        Integer limit = 10;

        FAccountQueryChargeOrderReq req = new FAccountQueryChargeOrderReq();
        req.setUserId(getSessionUser().getUser_id());
        if(orderStatus != null && !orderStatus.equals("all")){
            req.setOrderStatus(orderStatus);
        }
        req.setStartDate(startDate);
        req.setEndDate(endDate);
        req.setStart((page - 1) * limit);
        req.setLimit(limit);
        FAccountQueryChargeOrderReq dataList = this.accountService.queryChargeOrder(req);

        if(dataList.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, dataList.getTotalCount(), String.format("/account/recharge/history.htm?orderStatus=%s&dateStep=%s&page=", orderStatus == null ? "all" : orderStatus, dateStep));
            String pageHtml = pageUtils.show();
            model.addAttribute("pageHtml", pageHtml);
        }

        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("dateStep", dateStep);
        model.addAttribute("dataList", dataList);

        return "account/recharge/history";
    }

    /**
     * 提现页面
     * @return
     */
    @RequestMapping(value = "/withdraw.htm", method = RequestMethod.GET)
    public ModelAndView withdraw() {
        ModelAndView mav = new ModelAndView("account/withdraw");
        // 获得可用余额
        F830101Res res = null;
        try {
            res = basicDataAO.getPZAccount(this.getSessionUser().getUser_id());
        } catch (Exception e) {}
        List<TpzUserBankCardEntity> list = userBankCardService.queryUserBankList(this.getSessionUser().getUser_id(), "1");
        mav.addObject("balance", res);//账户余额
        if(list!=null&&list.size()>0) {
            mav.addObject("card", list.get(0));//银行卡
        }
        return mav;
    }

    /**
     * 历史提现记录页面
     * @return
     */
    @RequestMapping(value = "/hiswithdraw.htm", method = RequestMethod.GET)
    public String hiswithdraw(
            Model model,
            @RequestParam(value = "orderStatus", required = false) String orderStatus,
            @RequestParam(value = "dateStep", required = false, defaultValue = "0") Integer dateStep,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception{

        String startDate = StringHandleUtils.getStartDate(dateStep);
        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() + 86400 * 1000));
        Integer limit = 10;

        FAccountQueryWithdrawOrderReq req = new FAccountQueryWithdrawOrderReq();
        req.setUserId(getSessionUser().getUser_id());
        if(orderStatus != null && !orderStatus.equals("all")){
            req.setOrderStatus(orderStatus);
        }
        req.setStartDate(startDate);
        req.setEndDate(endDate);
        req.setStart((page - 1) * limit);
        req.setLimit(limit);
        FAccountQueryWithdrawOrderReq dataList = this.accountService.queryWithdrawOrder(req);

        if(dataList.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, dataList.getTotalCount(), String.format("/account//hiswithdraw.htm?orderStatus=%s&dateStep=%s&page=", orderStatus == null ? "all" : orderStatus, dateStep));
            String pageHtml = pageUtils.show();
            model.addAttribute("pageHtml", pageHtml);
        }

        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("dateStep", dateStep);
        model.addAttribute("dataList", dataList);

        return "account/hiswithdraw";
    }

    /**
     * 资金明细页面
     * @return
     */
    @RequestMapping(value = "/fund.htm", method = RequestMethod.GET)
    public String fund(
            Model model,
            @RequestParam(value = "bizType", required = false) String bizType,
            @RequestParam(value = "dateStep", required = false, defaultValue = "0") Integer dateStep,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception{

        String startDate = StringHandleUtils.getStartDate(dateStep);
        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() + 86400 * 1000));
        Integer limit = 10;

        FAccountQueryAccountJourReq req = new FAccountQueryAccountJourReq();
        req.setUserId(getSessionUser().getUser_id());
        if(bizType != null && !bizType.equals("all")){
            req.setBizType(bizType);
        }
        req.setStartDate(startDate.replace("-", ""));
        req.setEndDate(endDate.replace("-", ""));
        req.setStart((page - 1) * limit);
        req.setLimit(limit);
        FAccountQueryAccountJourReq dataList = this.accountService.queryAccountJour(req);

        if(dataList.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, dataList.getTotalCount(), String.format("/account/fund.htm?bizType=%s&dateStep=%s&page=", bizType == null ? "all" : bizType, dateStep));
            String pageHtml = pageUtils.show();
            model.addAttribute("pageHtml", pageHtml);
        }

        // 所有资产变动类型  有些东西可能不想进行展示 所以还是单写吧
        Map<String, String> bizTypes = new HashMap<>();
        for (EAccountBizType biz : EAccountBizType.values()) {
            bizTypes.put(biz.getCode(), biz.getValue());
        }
        //model.addAttribute("bizTypes", bizTypes);

        model.addAttribute("bizType", bizType);
        model.addAttribute("dateStep", dateStep);
        model.addAttribute("dataList", dataList);

        return "account/fund";
    }

    /**
     * 个人资料页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/info.htm", method = RequestMethod.GET)
    public ModelAndView info() throws Exception {
        ModelAndView mav = new ModelAndView("account/info");
        //TpzUserEntity user = getCaimaoUser();
        F830016Res extra = basicDataAO.getUserExtra(getSessionUser().getUser_id());
//        mav.addObject("user", user);
        mav.addObject("extra", extra);
        return mav;
    }

    /**
     * 账户安全页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/safe.htm", method = RequestMethod.GET)
    public ModelAndView safe() throws Exception {
        ModelAndView mav = new ModelAndView("account/safe");
        F830018Res user = getUser();
        // 登录密码的安全级别
        mav.addObject("loginPwdStrength", user.getUserPwdStrength());
        // 安全密码的级别
        if("1".equals(user.getIsSetTradePwd())) {
            F830017Res trade = basicDataAO.doGetTradePwd(getSessionUser().getUser_id());
            if(trade != null) {
                mav.addObject("tradeStrength", trade.getUserTradePwdStrength());
            }
        } else {
            mav.addObject("tradeStrength", null);
        }
        // 密保问题
        F830014Req userPwdQuestion = basicDataAO.queryUserPwdQuestion(getSessionUser().getUser_id());
        mav.addObject("userPwdQuestion", userPwdQuestion);
        mav.addObject("questionIndex", Math.round(Math.random()*2+0));
        // 用户基本信息
        mav.addObject("cuser", user);
        return mav;
    }

    @RequestMapping(value = "/charge/heepay", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doChargeHeepay(@RequestParam(value = "bank_no", required = false) String bankNo,
                                       @RequestParam("charge_amount") Long chargeAmount) {

        FAccountPreChargeReq fAccountPreChargeReq = new FAccountPreChargeReq();
        fAccountPreChargeReq.setUserId(getSessionUser().getUser_id());
        fAccountPreChargeReq.setPayCompanyNo(3L);
        fAccountPreChargeReq.setBankNo(bankNo);
        fAccountPreChargeReq.setChargeAmount(chargeAmount);
        fAccountPreChargeReq.setTerminalType(0L);
        fAccountPreChargeReq.setPayType(20);
        fAccountPreChargeReq.setOrderName("财猫融资充值");
        fAccountPreChargeReq.setOrderAbstract(getSessionUser().getUser_id() + "财猫融资充值" + chargeAmount + "元");
        fAccountPreChargeReq.setUserIp(getRemoteHost());
        try {
            Map<String, Object> result = accountService.doPreCharge(fAccountPreChargeReq);
            result.put("goodsName", URLEncoder.encode("财猫融资充值", "UTF-8"));
            return result;
        } catch (Exception e) {
            if (e instanceof CustomerException) {
                throw new BizServiceException(((CustomerException) e).getCode().toString(), e.getMessage());
            }
            logger.error("预充值发生异常, 返回未知异常. 异常信息{}", e);
            throw new BizServiceException("830408", "亲, 充值通道发生未知错误, 请联系客服");
        }
    }

    @RequestMapping(value = "/charge/zhifubao", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doChargeZhifubao(@RequestParam(value = "order_abstract", required = false) String orderAbstract,
                                        @RequestParam("charge_amount") Long chargeAmount,
                                                @RequestParam(value = "trade_no", required = false) String tradeNo) {

        FAccountPreChargeReq fAccountPreChargeReq = new FAccountPreChargeReq();
        fAccountPreChargeReq.setUserId(getSessionUser().getUser_id());
        fAccountPreChargeReq.setPayCompanyNo(-1L);
        fAccountPreChargeReq.setBackCardName("");
        fAccountPreChargeReq.setPayType(-1);
        fAccountPreChargeReq.setChargeAmount(chargeAmount * 100);
        fAccountPreChargeReq.setTerminalType(0L);
        fAccountPreChargeReq.setOrderAbstract(orderAbstract);
        fAccountPreChargeReq.setOrderName("财猫融资充值");
        fAccountPreChargeReq.setOrderAbstract(getSessionUser().getUser_id() + "财猫融资充值" + chargeAmount + "元");
        fAccountPreChargeReq.setUserIp(getRemoteHost());
        try {
            return accountService.doPreCharge(fAccountPreChargeReq);
        } catch (Exception e) {
            if (e instanceof CustomerException) {
                throw new BizServiceException(((CustomerException) e).getCode().toString(), e.getMessage());
            }
            logger.error("预充值发生异常, 返回未知异常. 异常信息{}", e);
            throw new BizServiceException("830408", "亲, 充值通道发生未知错误, 请联系客服");
        }
    }

    @RequestMapping(value = "/charge/yinhang", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doChargeRemark(@RequestParam(value = "remark", required = false) String remark,
                                                @RequestParam("charge_amount") Long chargeAmount) {

        FAccountPreChargeReq fAccountPreChargeReq = new FAccountPreChargeReq();
        fAccountPreChargeReq.setUserId(getSessionUser().getUser_id());
        fAccountPreChargeReq.setPayCompanyNo(-2L);
        fAccountPreChargeReq.setPayType(-2);
        fAccountPreChargeReq.setChargeAmount(chargeAmount);
        fAccountPreChargeReq.setTerminalType(0L);
        fAccountPreChargeReq.setOrderAbstract("入账银行: 招商银行西二旗支行 备注：" + remark);
        fAccountPreChargeReq.setOrderName("财猫融资充值");
        fAccountPreChargeReq.setOrderAbstract(getSessionUser().getUser_id() + "财猫融资充值" + chargeAmount + "元");
        fAccountPreChargeReq.setUserIp(getRemoteHost());
        try {
            return accountService.doPreCharge(fAccountPreChargeReq);
        } catch (Exception e) {
            if (e instanceof CustomerException) {
                throw new BizServiceException(((CustomerException) e).getCode().toString(), e.getMessage());
            }
            logger.error("预充值发生异常, 返回未知异常. 异常信息{}", e);
            throw new BizServiceException("830408", "亲, 充值通道发生未知错误, 请联系客服");
        }
    }
    
    
    // *********充值 start****
    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doCharge(
            @RequestParam("pay_company_no") Long payCompanyNo,
            @RequestParam(value = "bank_no", required = false) String bankNo,
            @RequestParam("charge_amount") Long chargeAmount,
            @RequestParam("terminal_type") String terminalType,
            @RequestParam("pay_type") Integer payType,
            @RequestParam(value = "order_abstract", required = false) String orderAbstract) throws Exception {
        FAccountPreChargeReq accountPreChargeReq = new FAccountPreChargeReq();
        accountPreChargeReq.setUserId(this.getSessionUser().getUser_id());
        accountPreChargeReq.setPayCompanyNo(payCompanyNo);
        accountPreChargeReq.setBankNo(bankNo);
        accountPreChargeReq.setChargeAmount(chargeAmount);
        accountPreChargeReq.setTerminalType(Long.valueOf(terminalType));
        accountPreChargeReq.setPayType(payType);
        accountPreChargeReq.setOrderName("财猫融资充值");
        accountPreChargeReq.setOrderAbstract(orderAbstract);
        accountPreChargeReq.setUserIp(getRemoteHost());
        return this.accountService.doPreCharge(accountPreChargeReq);
    }

    @RequestMapping(value = "/charge/page", method = RequestMethod.GET)
    @ResponseBody
    public FAccountQueryChargeOrderReq queryChargeOrder(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) throws Exception {

        FAccountQueryChargeOrderReq req = new FAccountQueryChargeOrderReq();
        req.setUserId(getSessionUser().getUser_id());
        req.setOrderStatus(status.equalsIgnoreCase("all") ? null : status);
        req.setStartDate(DateUtil.convertDateToDateTime(startDate, "00:00:00"));
        req.setEndDate(DateUtil.convertDateToDateTime(endDate, "23:59:59"));
        req.setStart(start);
        req.setLimit(limit);
        return this.accountService.queryChargeOrder(req);
    }

    // *********充值 end****

    // *********取现 start****
    /**
     * 网站提现控制器
     * @param tradePwd       资金密码
     * @param withdrawAmount 提现数量
     */
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    @ResponseBody
    public Long doWithdraw(@RequestParam("trade_pwd") String tradePwd,
                           @RequestParam("withdraw_amount") long withdrawAmount) {
        Long userId = this.getSessionUser().getUser_id();
        String orderName = "取现申请";
        String orderAbstract = userId + "申请取现" + withdrawAmount;
        tradePwd = RSAUtils.decryptStringByJs(tradePwd);
        try {
            FAccountApplyWithdrawReq accountApplyWithdrawReq = new FAccountApplyWithdrawReq();
            accountApplyWithdrawReq.setUserId(userId);
            accountApplyWithdrawReq.setOrderAmount(withdrawAmount);
            accountApplyWithdrawReq.setOrderAbstract(orderAbstract);
            accountApplyWithdrawReq.setOrderName(orderName);
            accountApplyWithdrawReq.setUserTradePwd(tradePwd);
            return this.accountService.doApplyWithdraw(accountApplyWithdrawReq);
        } catch (Exception e) {
            logger.error("用户申请提现出现异常：{}", e);
            if (e instanceof CustomerException) {
                throw new BizServiceException(((CustomerException) e).getCode().toString(), e.getMessage());
            }
            throw new BizServiceException("830408", "亲, 提现出现未知异常, 请联系客服");
        }
    }
    // 取消提现
    @RequestMapping(value = "/withdraw/cancel", method = RequestMethod.GET)
    public String doCancelWithdrawOrder(Model model, @RequestParam Long orderNo, HttpServletResponse response) {
        try {
            FAccountCancelWithdrawReq req = new FAccountCancelWithdrawReq();
            req.setUserId(getSessionUser().getUser_id());
            req.setOrderId(orderNo);
            req.setRemark("用户申请取消提现");
            this.accountService.doCancelWithdraw(req);
            response.sendRedirect("/account/hiswithdraw.htm");
            return null;
        } catch (Exception e) {
            logger.error("用户申请取消提现出现异常：{}", e);
            String error = "取消提现出现未知异常";
            if (e instanceof CustomerException) {
                error = e.getMessage();
                //throw new BizServiceException(((CustomerException) e).getCode().toString(), e.getMessage());
            }
            model.addAttribute("msg", error);
            return "account/withdraw_cancel";
            //throw new BizServiceException("830408", "取消提现出现未知异常");
        }
    }
    // 提现订单记录
    @RequestMapping(value = "/withdraw/page", method = RequestMethod.GET)
    @ResponseBody
    public FAccountQueryWithdrawOrderReq queryWithdrawOrder(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) throws Exception {
        FAccountQueryWithdrawOrderReq req = new FAccountQueryWithdrawOrderReq();
        req.setUserId(this.getSessionUser().getUser_id());
        req.setOrderStatus(status.equalsIgnoreCase("all") ? null : status);
        req.setStartDate(DateUtil.convertDateToDateTime(startDate, "00:00:00"));
        req.setEndDate(DateUtil.convertDateToDateTime(endDate, "23:59:59"));
        req.setStart(start);
        req.setLimit(limit);
        return this.accountService.queryWithdrawOrder(req);
    }

    // *********取现 end****

    // *********资金流水 start****
    @RequestMapping(value = "jour/page", method = RequestMethod.GET)
    @ResponseBody
    public FAccountQueryAccountJourReq queryRZAccountJour(
            @RequestParam(value = "biz_type", required = false) String bizType,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) throws Exception {
        FAccountQueryAccountJourReq req = new FAccountQueryAccountJourReq();
        req.setUserId(getSessionUser().getUser_id());
        req.setBizType(bizType.equalsIgnoreCase("all") ? null : bizType);
        req.setStartDate(startDate.replace("-",""));
        req.setEndDate(endDate.replace("-", ""));
        req.setStart(start);
        req.setLimit(limit);
        return this.accountService.queryAccountJour(req);
    }

    // *********资金流水 end****

}
