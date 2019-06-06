package com.caimao.bana.controller;

import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.req.account.FAccountChangeReq;
import com.caimao.bana.api.entity.req.loan.FLoanP2PApplyReq;
import com.caimao.bana.api.entity.res.product.FProductDetailRes;
import com.caimao.bana.api.entity.res.product.FProductRes;
import com.caimao.bana.api.entity.session.SessionUser;
import com.caimao.bana.api.entity.zeus.ZeusHomsRepaymentExcludeEntity;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.*;
import com.caimao.bana.api.service.product.IProductService;
import com.caimao.bana.utils.PageUtils;
import com.caimao.bana.utils.Session.SessionProvider;
import com.hsnet.pz.ao.account.IHomsAccountAO;
import com.hsnet.pz.ao.financing.IFinancingAO;
import com.hsnet.pz.ao.infrastructure.IBasicDataAO;
import com.hsnet.pz.ao.stock.IStockAO;
import com.hsnet.pz.biz.account.dto.res.F830101Res;
import com.hsnet.pz.biz.base.dto.res.F830911Res;
import com.hsnet.pz.biz.base.dto.res.F830912Res;
import com.hsnet.pz.biz.homs.dto.res.F830309Res;
import com.hsnet.pz.biz.homs.dto.res.F830312Res;
import com.hsnet.pz.biz.loan.dto.req.F830202Req;
import com.hsnet.pz.biz.loan.dto.req.F830204Req;
import com.hsnet.pz.biz.loan.dto.req.F830205Req;
import com.hsnet.pz.biz.loan.dto.res.F830204Res;
import com.hsnet.pz.biz.loan.dto.res.F830205Res;
import com.hsnet.pz.controller.BaseController;
import com.hsnet.pz.core.exception.BizServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 融资控制器
 * Created by WangXu on 2015/6/24.
 */
@Controller
@RequestMapping(value = "/pz")
public class PZController extends BaseController {
    private static final String SESSION_KEY_USER = "user";
    @Autowired
    protected SessionProvider sessionProvider;
    @Resource
    IProductService productService;
    @Resource
    private IFinancingAO financingAO;
    @Resource
    private IP2PService p2PService;
    @Autowired
    private IHomsAccountAO homsAccountAO;
    @Resource
    private IBasicDataAO basicDataAO;
    @Resource
    private ILoanService loanService;
    @Resource
    private IHomsAccountService homsAccountService;
    @Resource
    private IAccountService accountService;
    @Autowired
    private IStockAO stockAO;
    @Resource
    private IOperationService operationService;
    @Resource
    private IUserService userService;
    @Resource
    SysParameterService parameterService;

    /**
     * 获取产品配置
     */
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    @ResponseBody
    public FProductRes getProduct(@RequestParam("product_id") Long productId) throws CustomerException {
        return productService.getProduct(productId);
    }

    @RequestMapping(value = "/proddetail", method = RequestMethod.GET)
    @ResponseBody
    public List<FProductDetailRes> queryProdDetailList(
            @RequestParam("product_id") Long productId) throws CustomerException {
        return productService.queryProdDetailList(productId);
    }

    /**
     * 根据用户选择的去调整到制定的配资合约页面
     */
    @RequestMapping(value = "/pz_select", method = RequestMethod.GET)
    public ModelAndView jumpPz(
            HttpServletResponse response,
            @RequestParam(value = "t", required = true, defaultValue = "p1") String type
    ) throws Exception {
        String url = "/pz/p1.html";
        String action = "pzDay";
        Long prodId = 4L;
        switch (type) {
            case "p2ppz":
                url = "/pz/pzp2p.html";
                action = "pzMonth";
                prodId = 3L;
                break;
            case "p8":
                url = "/pz/p8.html";
                action = "pzMonth";
                prodId = 800461779107841L;
                break;
            case "p10":
                url = "/pz/p10.html";
                action = "pzMonthDi";
                prodId = 800461779107843L;
                break;
        }
        // 获取用户是否有合约
        //是否登录
        SessionUser user = (SessionUser) sessionProvider.getAttribute(SESSION_KEY_USER);
        if (user != null) {
            F830204Req req = financingAO.queryPaginableContract(user.getUser_id(), null, null, null, 0, 10);
            if (req.getItems() == null || req.getItems().size() == 0) {
                response.sendRedirect(url);
                return null;
            }
        } else {
            response.sendRedirect(url);
            return null;
        }

        ModelAndView mav = new ModelAndView("/pz/pz_select");
        mav.addObject("url", url);
        mav.addObject("curLeftMenu", action);
        return mav;
    }

    /**
     * 按日融资
     */
    @RequestMapping(value = "/p1.html", method = RequestMethod.GET)
    public ModelAndView p1View() throws Exception {
        ModelAndView mav = new ModelAndView("pz/p1");
//        Long productId = 4L;
//        // 产品产品信息与详情
//        FProductRes productRes = productService.getProduct(productId);
//        List<FProductDetailRes> productDetailResList = productService.queryProdDetailList(productId);
//
//        mav.addObject("productRes", productRes);
//        mav.addObject("productDetailRes", productDetailResList);
        return mav;
    }


    /**
     * 月融资，P2P版本
     */
    @RequestMapping(value = "/pzp2p.html", method = RequestMethod.GET)
    public ModelAndView pzp2pView() throws Exception {
        // 活动是否结束
        Integer activityEnd = 0;
        if (new Date().getTime() > new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-08-17 00:00:00").getTime()) {
            activityEnd = 1;
        }
        ModelAndView mav = new ModelAndView("pz/pzp2p");
        mav.addObject("activityEnd", activityEnd);
        return mav;
    }

    /**
     * 月融资
     */
    @RequestMapping(value = "/p8.html", method = RequestMethod.GET)
    public ModelAndView pzp8View() throws Exception {
        return new ModelAndView("pz/p8");
    }
    /**
     * 月融资
     */
    @RequestMapping(value = "/p10.html", method = RequestMethod.GET)
    public ModelAndView pzp10View() throws Exception {
        return new ModelAndView("pz/p10");
    }

    /**
     * 申请借款
     */
    @RequestMapping(value = "/loan/apply", method = RequestMethod.POST)
    @ResponseBody
    public Long applyLoan(@RequestParam("trade_pwd") String tradePwd,
                          @RequestParam("produce_id") Long produceId,
                          @RequestParam("produce_term") Integer produceTerm,
                          @RequestParam("deposit_amount") Long depositAmount,
                          @RequestParam("loan_amount") Long loanAmount,
                          @RequestParam(value = "abstract", required = false) String orderAbstract) {
        try {
            this.p2PService.checkIsP2PLoan(produceId, (loanAmount.intValue() / depositAmount.intValue()));
        } catch (Exception e) {
            throw new BizServiceException(((CustomerException) e).getCode().toString(), e.getMessage());
        }
        return financingAO.applyLoan(getSessionUser().getUser_id(), tradePwd,
                produceId, produceTerm, depositAmount, loanAmount, orderAbstract);
    }


    /**
     * BEGIN 已融资列表页面
     */
    // 合约详情
    @RequestMapping(value = "/detail.html", method = RequestMethod.GET)
    public ModelAndView contractDetail(
            @RequestParam(value = "contract_no") Long contractNo,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        ModelAndView mav = new ModelAndView("pz/detail");
        Integer limit = 20;
        Integer start = (page - 1) * limit;
        String startDate = null;
        String endDate = null;
        Integer isHisContract = 0;
        // 获取合约详情
        F830204Res conDetail = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        F830205Res conHisDetail = null;
        if (conDetail.getContractNo() == null) {
            conHisDetail = financingAO.getHisContract(getSessionUser().getUser_id(), contractNo);
            isHisContract = 1;
        }
        // 获取HOMS资金账户
        F830312Res assetsInfo = null;
        if (isHisContract.equals(0)) {
            try {
                assetsInfo = homsAccountAO.getHomsAssetsInfo(this.getSessionUser().getUser_id(), conDetail.getHomsFundAccount(), conDetail.getHomsCombineId());
            } catch (Exception e) {
            }
        }
        // 获取利息列表
        F830202Req interestList = financingAO.queryPaginableInterest(contractNo, startDate, endDate, start, limit);
        if(interestList.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, interestList.getTotalCount(),
                    String.format("/pz/detail.html?contract_no=%s&page=", contractNo));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }
        mav.addObject("isHisContract", isHisContract);
        if (isHisContract.equals(0)) {
            mav.addObject("contractDetail", conDetail);
        } else {
            mav.addObject("contractDetail", conHisDetail);
        }
        mav.addObject("assetsInfo", assetsInfo);
        mav.addObject("interestList", interestList);

        return mav;
    }

    // 当前合约
    @RequestMapping(value = "/contracts.html", method = RequestMethod.GET)
    public ModelAndView contracts() {
        ModelAndView mav = new ModelAndView("pz/contracts");
        String productType = null;
        String startDate = null;
        String endDate = null;
        Integer start = 0;
        Integer limit = 50;

        // 获取当前所有的合约借款信息
        F830204Req req = financingAO.queryPaginableContract(getSessionUser().getUser_id(), productType, startDate, endDate, start, limit);

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
    // 历史合约
    @RequestMapping(value = "/hiscontracts.html", method = RequestMethod.GET)
    public ModelAndView hiscontracts(@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        ModelAndView mav = new ModelAndView("/pz/hiscontracts");
        Integer limit = 10;
        Integer start = (page - 1) * limit;
        String startDate = null;
        String endDate = null;

        F830205Req req = financingAO.queryPaginableHisContract(getSessionUser().getUser_id(), startDate, endDate, start, limit);
        if(req.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(),
                    String.format("/pz/hiscontracts.html?page=%s", ""));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }

        mav.addObject("contractList", req);

        return mav;
    }

    // 申请合约
    @RequestMapping(value = "/apply.html", method = RequestMethod.GET)
    public ModelAndView apply(@RequestParam(value = "status", required = false) String status,
                              @RequestParam(value = "start_date", required = false) String startDate,
                              @RequestParam(value = "end_date", required = false) String endDate,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int page) throws Exception {
        ModelAndView mav = new ModelAndView("/pz/apply");
        Integer limit = 10;
        Integer start = (page - 1) * limit;
        FLoanP2PApplyReq req = new FLoanP2PApplyReq();
        if (startDate != null) startDate += " 00:00:00";
        if (endDate != null) endDate += " 23:59:59";
        req.setUserId(this.getSessionUser().getUser_id());
        req.setVerifyStatus(status);
        req.setApplyDatetimeBegin(startDate);
        req.setApplyDatetimeEnd(endDate);
        req.setLimit(10);
        req.setStart(start);
        req = this.loanService.queryLoanP2PApplyList(req);
        //F830203Req req = financingAO.queryPaginableLoanApply(getSessionUser().getUser_id(), status, startDate, endDate, start, limit);
        if(req.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(),
                    String.format("/pz/apply.html?page=%s", ""));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }

        mav.addObject("apply", req);

        return mav;
    }

    /**
     * END 已融资列表页面
     */

    /**
     * BEGIN 融资对话框的那些东西
     */

    // 追加保证金对话框
    @RequestMapping(value = "/dialog/fundin.html", method = RequestMethod.GET)
    public ModelAndView dialogIn(@RequestParam("contract_no") Long contractNo) throws Exception {
        ModelAndView mav = new ModelAndView("/pz/dialog_fundin");
        // 获取用户账户余额
        TpzAccountEntity accountEntity = this.accountService.getAccount(this.getSessionUser().getUser_id());
        //Long availableAmount = financingAO.getAddAmountMax(getSessionUser().getUser_id(), contractNo);
        mav.addObject("availableAmount", accountEntity.getAvalaibleAmount() - accountEntity.getFreezeAmount());
        mav.addObject("contractNo", contractNo);
        return mav;
    }

    // 提盈利的对话框
    @RequestMapping(value = "/dialog/fundout.html", method = RequestMethod.GET)
    public ModelAndView dialogFundOut(@RequestParam(value = "contract_no") Long contractNo) {
        ModelAndView mav = new ModelAndView("/pz/dialog_fundout");

        // 获取合约详情
        F830204Res conDetail = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        // 获取HOMS资金账户
        F830312Res assetsInfo = null;
        //  嗯，一个用户1万配4万，一共5万操盘资金。   如果盈利超20%， 即资产达6万以上，超过6万部分的能提现，否则不能提。
        // 超过盈利的 20% 可以进行提赢
        long canTrans = 0L;
        //TsysParameterEntity parameterEntity = this.parameterService.getSysparameterById("homsoutrate");
        try {
            assetsInfo = homsAccountAO.getHomsAssetsInfo(this.getSessionUser().getUser_id(), conDetail.getHomsFundAccount(), conDetail.getHomsCombineId());
//            if (parameterEntity != null) {
//                assetsInfo.setTotalProfit(new BigDecimal((assetsInfo.getTotalProfit() * Double.valueOf(parameterEntity.getParamValue()))).longValue());
//            }
            // 开始资产 * 1.2 - 净值资产，在与当前可用值去最小值
            canTrans = new BigDecimal(assetsInfo.getTotalNetAssets()).subtract(new BigDecimal(assetsInfo.getBeginAmount()).multiply(new BigDecimal(1.2))).longValue();
            canTrans = Math.min(canTrans, assetsInfo.getCurAmount());
        } catch (Exception e) {
        }

        mav.addObject("canTrans", canTrans < 0 ? 0L : canTrans);
        mav.addObject("contractNo", contractNo);
        mav.addObject("assetsInfo", assetsInfo);
        return mav;
    }

    // 还款的对话框
    @RequestMapping(value = "/dialog/repay.html", method = RequestMethod.GET)
    public ModelAndView dialogRepay(@RequestParam(value = "contract_no") Long contractNo) throws Exception {
        ModelAndView mav = new ModelAndView("/pz/dialog_repay");

        Map<String, Object> map = new HashMap<String, Object>();
        // 还款合约
        F830204Res contractInfo = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        map.put("contract", contractInfo);

        // 进行账户验证，验证homs账户所属用户是否是指定的USER_ID
        this.homsAccountService.valideUserHomsAccount(this.getSessionUser().getUser_id(), contractInfo.getHomsCombineId(), contractInfo.getHomsFundAccount());

        // 可用于还款的金额
        long availableAmount = 0L;
        Boolean enableRepay = true;
        String repayMsg = "可还款";
        // 获取HOMS资金账户
        F830312Res assetsInfo = null;
        try {
            assetsInfo = homsAccountAO.getHomsAssetsInfo(this.getSessionUser().getUser_id(), contractInfo.getHomsFundAccount(), contractInfo.getHomsCombineId());
            availableAmount = assetsInfo.getEnableWithdraw();
            if (assetsInfo.getTotalMarketValue() > 0) {
                repayMsg = "当前有持仓，不可还款";
                enableRepay = false;
            }
        } catch (Exception e) {
            repayMsg = "获取资产失败，请稍后重试";
            enableRepay = false;
        }
        try {
            // 查询是否有持仓，即使今天交易了，不结算，股票卖出还有存在的
            List<F830309Res> holdList = stockAO.queryChildHolding(this.getSessionUser().getUser_id(), contractInfo.getHomsCombineId(), contractInfo.getHomsFundAccount());
            if (holdList.size() > 0) {
                repayMsg = "今日有股票交易，不可还款";
                enableRepay = false;
                for (F830309Res h : holdList) {
                    if (h.getEnableAmount() > 0) {
                        repayMsg = "当前有持仓，不可还款";
                        enableRepay = false;
                    }
                }
            }
        } catch (Exception e) {
            repayMsg = "获取资产失败，请稍后重试";
            enableRepay = false;
        }

        map.put("assetsInfo", assetsInfo);
        map.put("enableRepay", enableRepay);
        map.put("repayMsg", repayMsg);
        map.put("availableAmount", availableAmount);
        mav.addObject("repay", map);

        return mav;
    }

    // 追加合约的对话框
    @RequestMapping(value = "/dialog/add.html", method = RequestMethod.GET)
    public ModelAndView dialogAdd(@RequestParam("contract_no") Long contractNo) throws Exception {
        ModelAndView mav = new ModelAndView("/pz/dialog_add");

        // 合约
        F830204Res f830204Res = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        mav.addObject("contract", f830204Res);

        // 产品信息
        F830911Res f830911Res = basicDataAO.getProduct(f830204Res.getProdId());
        mav.addObject("product", f830911Res);


        String[] terms = f830911Res.getProdTerms().split(",");
        List termsList = java.util.Arrays.asList(terms);
        mav.addObject("terms", termsList);

        List<F830912Res> F830912ResList = basicDataAO.queryProdDetailList(f830204Res.getProdId());
        mav.addObject("productdetail", F830912ResList);

        return mav;
    }


    // TODO 展期的对话框
    @RequestMapping(value = "/dialog/defered.html", method = RequestMethod.GET)
    public ModelAndView dialogDefered(@RequestParam("contract_no") Long contractNo) {
        ModelAndView mav = new ModelAndView("/pz/dialog_defered");
        // 合约
        F830204Res f830204Res = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        mav.addObject("contract", f830204Res);
        // 产品信息
        F830911Res f830911Res = basicDataAO.getProduct(f830204Res.getProdId());
        mav.addObject("product", f830911Res);

        String[] terms = f830911Res.getProdTerms().split(",");
        mav.addObject("terms", java.util.Arrays.asList(terms));

        List<F830912Res> F830912ResList = basicDataAO.queryProdDetailList(f830204Res.getProdId());
        mav.addObject("productdetail", F830912ResList);
        // 保证金
        long depositAmount = financingAO.getDeferedDeposit(getSessionUser().getUser_id(), contractNo);
        mav.addObject("deposit", depositAmount);
        return mav;
    }

    // 资金密码的对话框
    @RequestMapping(value = "dialog/tradepwd.html", method = RequestMethod.GET)
    public ModelAndView dialogTradePwd() {
        return new ModelAndView("/pz/dialog_tradepwd");
    }


    /**
     * END 融资对话框的那些东西
     */


    /**
     * BEGIN 合约附属的那些操作的东西
     */
    // 追加保证金
    @RequestMapping(value = "/operation/in", method = RequestMethod.POST)
    @ResponseBody
    public boolean homsIn(
            @RequestParam("contract_no") Long contractNo,
            @RequestParam("trans_amount") Long transAmount) {
        if (transAmount <= 0) {
            throw new BizServiceException("888888", "追加保证金数量错误");
        }
        F830204Res f830204Res = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        if (f830204Res == null) {
            throw new BizServiceException("888888", "合约未找到");
        }
        homsAccountAO.homsIn(getSessionUser().getUser_id(), f830204Res.getHomsFundAccount(), f830204Res.getHomsCombineId(), transAmount);

        // 发送通知信息
        try {
            TpzUserEntity userEntity = this.userService.getById(getSessionUser().getUser_id());
            String subject = String.format("%s(%s)追加%s元保证金(操作账户:%s)", userEntity.getUserRealName(), userEntity.getMobile(), (transAmount / 100), f830204Res.getHomsCombineId());
            this.operationService.addAlarmTask("homs_in_money", subject, subject);
        } catch (Exception e) {

        }
        return true;
    }
    // 提赢操作
    @RequestMapping(value = "/operation/out", method = RequestMethod.POST)
    @ResponseBody
    public boolean homsOut(
            @RequestParam("contract_no") Long contractNo,
            @RequestParam("trans_amount") Long transAmount) {
        if (transAmount <= 0) {
            throw new BizServiceException("888888", "转出数量错误");
        }
        //提盈限制时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Integer currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        if(currentHour < 10 || currentHour > 15 || currentDay == 1 || currentDay == 7){
            throw new BizServiceException("830409", "目前只允许交易日 10 - 15点提盈");
        }
        F830204Res f830204Res = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        //  嗯，一个用户1万配4万，一共5万操盘资金。   如果盈利超20%， 即资产达6万以上，超过6万部分的能提现，否则不能提。
        // 查询HOMS账户资产
        F830312Res assetsInfo = homsAccountAO.getHomsAssetsInfo(this.getSessionUser().getUser_id(), f830204Res.getHomsFundAccount(), f830204Res.getHomsCombineId());
        // 开始资产 * 1.2 - 净值资产，在与当前可用值去最小值
        Long canTrans = new BigDecimal(assetsInfo.getTotalNetAssets()).subtract(new BigDecimal(assetsInfo.getBeginAmount()).multiply(new BigDecimal(1.2))).longValue();
        canTrans = Math.min(canTrans, assetsInfo.getCurAmount());
        if (transAmount > canTrans) {
            throw new BizServiceException("830409", "超过最大可提金额");
        }

        homsAccountAO.homsOut(getSessionUser().getUser_id(), f830204Res.getHomsFundAccount(), f830204Res.getHomsCombineId(), transAmount);
        return true;
    }

    // 还款操作
    @RequestMapping(value = "/operation/repay", method = RequestMethod.POST)
    @ResponseBody
    public boolean doRepay(@RequestParam("trade_pwd") String tradePwd,
                           @RequestParam("contract_no") Long contractNo) throws Exception {
        // 获取合约详情
        F830204Res contractInfo = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        if (contractInfo == null) {
            throw new BizServiceException("888888", "合约未找到");
        }
        // 进行账户验证，验证homs账户所属用户是否是指定的USER_ID
        this.homsAccountService.valideUserHomsAccount(this.getSessionUser().getUser_id(), contractInfo.getHomsCombineId(), contractInfo.getHomsFundAccount());

        // 获取用户的资金账户，减仓当前是否有股票市值
        F830312Res assetsInfo = homsAccountAO.getHomsAssetsInfo(this.getSessionUser().getUser_id(), contractInfo.getHomsFundAccount(), contractInfo.getHomsCombineId());
        if (assetsInfo == null) {
            throw new CustomerException("获取用户资产失败，请稍后尝试", 888888);
        }

        // 获取他是否在排除列表中
        List<ZeusHomsRepaymentExcludeEntity> excludeEntityList = this.homsAccountService.queryHomsRepaymentExcludeList(contractInfo.getHomsFundAccount(), contractInfo.getHomsCombineId());
        if (excludeEntityList.size() == 0) {
            // 他不在排除列表，不进行判断
            if (assetsInfo.getTotalMarketValue() > 0) {
                throw new CustomerException("当前有持仓，不可还款", 80080);
            }
            // 查询是否有持仓，即使今天交易了，不结算，股票卖出还有存在的
            List<F830309Res> holdList = stockAO.queryChildHolding(this.getSessionUser().getUser_id(), contractInfo.getHomsCombineId(), contractInfo.getHomsFundAccount());
            if (holdList.size() > 0) {
                for (F830309Res h : holdList) {
                    if (h.getEnableAmount() > 0) {
                        throw new CustomerException("当前有持仓，不可还款", 80080);
                    }
                }
                throw new CustomerException("今日有股票交易，不可还款", 80080);
            }
        }

        if (assetsInfo.getEnableWithdraw() < contractInfo.getLoanAmount()) {
            throw new CustomerException("资金余额不足，请追加保证金", 80080);
        }

        /**
         * 获取财猫主账户的钱，先冻结财猫主账户可用的钱，事后在返回给他
         */
        F830101Res accountInfo = basicDataAO.getPZAccount(this.getSessionUser().getUser_id());
        Long caimaoAvailableAmount = accountInfo.getAvalaibleAmount() - accountInfo.getFreezeAmount();
        FAccountChangeReq accountReq = new FAccountChangeReq();
        if (caimaoAvailableAmount > 0) {
            // 冻结财猫的这些钱
            accountReq.setRefSerialNo(contractNo.toString());
            accountReq.setPzAccountId(accountInfo.getPzAccountId());
            accountReq.setAmount(caimaoAvailableAmount);
            accountReq.setSeqFlag(ESeqFlag.COME.getCode());
            accountReq.setAccountBizType(EAccountBizType.REPAY.getCode());
            this.accountService.frozenAvailableAmount(accountReq);
        }
        try {
            financingAO.doRepay(getSessionUser().getUser_id(), tradePwd, contractNo, contractInfo.getLoanAmount());
        } catch (Exception e) {
            // 解冻那些钱
            if (caimaoAvailableAmount > 0) {
                accountReq.setSeqFlag(ESeqFlag.GO.getCode());
                try {
                    this.accountService.frozenAvailableAmount(accountReq);
                } catch (Exception e1) {}
            }
            throw e;
        }
        // 解冻那些钱
        if (caimaoAvailableAmount > 0) {
            accountReq.setSeqFlag(ESeqFlag.GO.getCode());
            try {
                this.accountService.frozenAvailableAmount(accountReq);
            } catch (Exception e) {}
        }
        return true;

    }

    // 申请追加合约
    @RequestMapping(value = "/operation/add", method = RequestMethod.POST)
    @ResponseBody
    public Long applyAddLoan(@RequestParam("contract_no") Long contractNo,
                             @RequestParam("trade_pwd") String tradePwd,
                             @RequestParam("produce_term") Integer produceTerm,
                             @RequestParam("deposit_amount") Long depositAmount,
                             @RequestParam("add_amount") Long addAmount) {
        F830204Res f830204Res = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        if (f830204Res == null) {
            throw new BizServiceException("888888", "合约未找到");
        }
        return financingAO.applyAddLoan(contractNo, getSessionUser().getUser_id(), tradePwd, f830204Res.getProdId(), produceTerm, depositAmount, addAmount);
    }

    // TODO 申请借款展期
    @RequestMapping(value = "/operation/defered", method = RequestMethod.POST)
    @ResponseBody
    public Long applyDeferedLoan(@RequestParam("contract_no") Long contractNo,
                                 @RequestParam("trade_pwd") String tradePwd,
                                 @RequestParam("produce_term") Integer produceTerm) {
        F830204Res f830204Res = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        return financingAO.applyDeferedLoan(contractNo, getSessionUser().getUser_id(), tradePwd, f830204Res.getProdId(), produceTerm);
    }

    /**
     * END 合约附属的那些操作的东西
     */

}
