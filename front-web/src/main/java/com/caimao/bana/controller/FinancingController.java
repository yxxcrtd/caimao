package com.caimao.bana.controller;

import com.caimao.bana.api.entity.req.loan.FHisContractReq;
import com.caimao.bana.api.entity.req.loan.FLoanApplyReq;
import com.caimao.bana.api.entity.req.loan.FLoanContractReq;
import com.caimao.bana.api.entity.res.F830216Res;
import com.caimao.bana.api.entity.res.loan.FHisContractRes;
import com.caimao.bana.api.entity.res.loan.FLoanApplyRes;
import com.caimao.bana.api.entity.res.loan.FLoanContractRes;
import com.caimao.bana.api.service.ILoanService;
import com.hsnet.pz.ao.account.IHomsAccountAO;
import com.hsnet.pz.ao.financing.IFinancingAO;
import com.hsnet.pz.ao.infrastructure.IBasicDataAO;
import com.hsnet.pz.biz.base.dto.res.F830911Res;
import com.hsnet.pz.biz.base.dto.res.F830912Res;
import com.hsnet.pz.biz.loan.dto.req.F830202Req;
import com.hsnet.pz.biz.loan.dto.req.F830212Req;
import com.hsnet.pz.biz.loan.dto.res.*;
import com.hsnet.pz.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资 Controller
 * 
 */
@Controller
@RequestMapping(value = "/financing")
public class FinancingController extends BaseController {

    @Autowired
    private IFinancingAO financingAO;
    @Resource
    private ILoanService loanService;
    @Autowired
    IBasicDataAO basicDataAO;
    @Autowired
    private IHomsAccountAO homsAccountAO;

    /************** 借款 start **************/

    /**
     * (进入)借款(界面的)前提
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/loan/premise", method = RequestMethod.POST)
    @ResponseBody
    public boolean judgeLoanPremise(@RequestParam("produce_id") Long produceId) throws Exception {
        // financingAO.judgeLoanPremise(getSessionUser().getUser_id(), produceId);
        loanService.canLoanApply(getSessionUser().getUser_id(), produceId);
        return true;
    }

    /**
     * 借款界面数据准备
     */
    @RequestMapping(value = "/loan/data", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doLoanData(@RequestParam("produce_id") long produceId) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 保证金
        // 涉及访问homs
        long depositAmount = financingAO.getLoanDeposit(getSessionUser().getUser_id());
        map.put("deposit", depositAmount);
        // 最大可借金额
        // long maxLoanAmount = financingAO.getMaxLoanAmount(this.getSessionUser().getUser_id(), produceId,
        // depositAmount);
        long maxLoanAmount = loanService.getMaxLoanAmount(this.getSessionUser().getUser_id(), produceId, depositAmount);
        map.put("maxLoan", maxLoanAmount);
        return map;
    }

    /**
     * 申请借款
     */
    @RequestMapping(value = "/loan/apply", method = RequestMethod.POST)
    @ResponseBody
    public Long applyLoan(@RequestParam("trade_pwd") String tradePwd, @RequestParam("produce_id") Long produceId,
            @RequestParam("produce_term") Integer produceTerm, @RequestParam("deposit_amount") Long depositAmount,
            @RequestParam("loan_amount") Long loanAmount,
            @RequestParam(value = "abstract", required = false) String orderAbstract) {
        return financingAO.applyLoan(getSessionUser().getUser_id(), tradePwd, produceId, produceTerm, depositAmount,
                loanAmount, orderAbstract);
    }

    /************** 借款 end **************/
    /**
     * 追加借款前提
     */
    @RequestMapping(value = "/add/premise", method = RequestMethod.POST)
    @ResponseBody
    public boolean judgeAddPremise(@RequestParam("contract_no") Long contractNo) {
        financingAO.judgeAddPremise(getSessionUser().getUser_id(), contractNo);
        return true;
    }
    /************** 还款 start ************/

    /**
     * 用于还款前提
     */
    @RequestMapping(value = "/repay/premise", method = RequestMethod.POST)
    @ResponseBody
    public boolean judgeRepayPremise(@RequestParam("contract_no") Long contractNo) {
        financingAO.judgeRepayPremise(this.getSessionUser().getUser_id(), contractNo);
        return true;
    }

    /**
     * 还款界面数据准备
     */
    @RequestMapping(value = "/repay/data", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doRepayData(@RequestParam("contract_no") Long contractNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 还款合约
//        F830204Res f830204Res = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        FLoanContractRes res = loanService.getContract(getSessionUser().getUser_id(), contractNo);
        map.put("contract", res);
        // 可用于还款的金额
        long availableAmount = financingAO.getRepayAmount(contractNo);
        map.put("availableAmount", availableAmount);
        return map;

    }

    /**
     * 还款本金对应利息的计算
     */
    @RequestMapping(value = "/repayinterest", method = RequestMethod.POST)
    @ResponseBody
    public F830219Res doCalculateRepayInterest(@RequestParam("repay_amount") Long repayAmount,
            @RequestParam("contract_no") Long contractNo) {
        return financingAO.doCalculateRepayInterest(repayAmount, contractNo);
    }

    /**
     * 还款
     */
    @RequestMapping(value = "/repay", method = RequestMethod.POST)
    @ResponseBody
    public boolean doRepay(@RequestParam("trade_pwd") String tradePwd, @RequestParam("contract_no") Long contractNo,
            @RequestParam("repay_amount") Long repayAmount) {
        financingAO.doRepay(getSessionUser().getUser_id(), tradePwd, contractNo, repayAmount);
        return true;
    }

    /************** 还款 end **************/

    /************** 展期 start **************/
    /**
     * 借款展期前提
     */
    @RequestMapping(value = "/defered/premise", method = RequestMethod.POST)
    @ResponseBody
    public boolean judgeDeferedPremise(@RequestParam("contract_no") Long contractNo) {
        financingAO.judgeDeferedPremise(getSessionUser().getUser_id(), contractNo);
        return true;
    }

    /**
     * 借款展期数据准备
     */
    @RequestMapping(value = "/defered/data", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doDeferedLoanData(@RequestParam("contract_no") Long contractNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 合约
        //F830204Res f830204Res = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        FLoanContractRes res = loanService.getContract(getSessionUser().getUser_id(), contractNo);
        map.put("contract", res);
        // 产品信息
        F830911Res f830911Res = basicDataAO.getProduct(res.getProdId());
        map.put("product", f830911Res);
        List<F830912Res> F830912ResList = basicDataAO.queryProdDetailList(res.getProdId());
        map.put("productdetail", F830912ResList);
        // 保证金
        long depositAmount = financingAO.getDeferedDeposit(getSessionUser().getUser_id(), contractNo);
        map.put("deposit", depositAmount);
        return map;
    }

    /**
     * 申请借款展期
     */
    @RequestMapping(value = "/defered/apply", method = RequestMethod.POST)
    @ResponseBody
    public Long applyDeferedLoan(@RequestParam("contract_no") Long contractNo,
            @RequestParam("trade_pwd") String tradePwd, @RequestParam("produce_id") Long produceId,
            @RequestParam("produce_term") Integer produceTerm) {
        return financingAO
                .applyDeferedLoan(contractNo, getSessionUser().getUser_id(), tradePwd, produceId, produceTerm);
    }

    /************** 展期 end **************/
    /************** 查询 start **************/
    /**
     * 追加时获取最大可追加金额
     */
    @RequestMapping(value = "add/maxamount", method = RequestMethod.GET)
    @ResponseBody
    public long getAddMaxAmount(@RequestParam("contract_no") Long contractNo) {
//        return financingAO.getAddAmountMax(getSessionUser().getUser_id(), contractNo);
        return loanService.getAddAmountMax(getSessionUser().getUser_id(), contractNo);
    }

    /**
     * 只有手机版用到了
     * 获取用户总融资信息
     */
    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    @ResponseBody
    public F830216Res getStatisticInfo() {
        //return financingAO.getTotalLoanAndBill(getSessionUser().getUser_id());
        return loanService.getTotalLoanAndBill(getSessionUser().getUser_id());
    }
//
    /**
     * 根据子账户查合约列表
     */
    @RequestMapping(value = "/contract/list", method = RequestMethod.GET)
    @ResponseBody
    public FLoanContractReq queryContractList(@RequestParam("homs_fund_account") String homsFundAccount,
            @RequestParam("homs_combine_id") String homsCombineId) {
        //return financingAO.queryContractList(getSessionUser().getUser_id(), homsFundAccount, homsCombineId);
        return loanService.queryContractList(getSessionUser().getUser_id(), homsFundAccount, homsCombineId);
    }

    /**
     * 根据合约的展期信息
     */
    @RequestMapping(value = "/defered/page", method = RequestMethod.GET)
    @ResponseBody
    public FLoanApplyReq queryContractDeferedList(@RequestParam("rel_contract_no") Long relContractNo,
            @RequestParam("start") int start, @RequestParam("limit") int limit) {
        // return financingAO.queryContractDeferedList(getSessionUser().getUser_id(), relContractNo, start, limit);
        return loanService.queryContractDeferedList(getSessionUser().getUser_id(), relContractNo, start, limit);
    }

    /**
     * 借款列表（支持分页）
     */
    @RequestMapping(value = "/contract/page", method = RequestMethod.GET)
    @ResponseBody
    public FLoanContractReq queryPaginableContract(
            @RequestParam(value = "product_type", required = false) String productType,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate, @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
        // return financingAO.queryPaginableContract(getSessionUser().getUser_id(), productType, startDate, endDate,
        // start, limit);
        return loanService.queryLoanContractWithPage(getSessionUser().getUser_id(), productType, startDate, endDate,
                start, limit);
    }

    /**
     * 借款详情
     */
    @RequestMapping(value = "/contract/detail", method = RequestMethod.GET)
    @ResponseBody
    public F830204Res getContract(@RequestParam("contract_no") Long contractNo) {
        return financingAO.getContract(getSessionUser().getUser_id(), contractNo);
        //return loanService.getContract(getSessionUser().getUser_id(), contractNo);
    }

    /**
     * 历史借款列表（支持分页）
     */
    @RequestMapping(value = "/hiscontract/page", method = RequestMethod.GET)
    @ResponseBody
    public FHisContractReq queryPaginableHisContract(@RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate, @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
//        return financingAO.queryPaginableHisContract(getSessionUser().getUser_id(), startDate, endDate, start, limit);
        return loanService.queryPaginableHisContract(getSessionUser().getUser_id(), startDate, endDate, start, limit);
    }

    /**
     * 历史借款详情
     */
    @RequestMapping(value = "/hiscontract/detail", method = RequestMethod.GET)
    @ResponseBody
    public FHisContractRes getHisContract(@RequestParam("contract_no") Long contractNo) {
//        return financingAO.getHisContract(getSessionUser().getUser_id(), contractNo);
        return loanService.getHisContract(getSessionUser().getUser_id(), contractNo);
    }

    /**
     * 借款申请列表（支持分页）
     */
    @RequestMapping(value = "/loanapply/page", method = RequestMethod.GET)
    @ResponseBody
    public FLoanApplyReq queryPaginableLoanApply(@RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate, @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
        // return financingAO.queryPaginableLoanApply(getSessionUser().getUser_id(), status, startDate, endDate, start,
        // limit);
        return loanService.queryLoanContractApplyWithPage(getSessionUser().getUser_id(), status, startDate, endDate,
                start, limit);
    }

    /**
     * 当前借款申请详情
     */
    @RequestMapping(value = "/loanapply/detail", method = RequestMethod.GET)
    @ResponseBody
    public FLoanApplyRes getLoanApply(@RequestParam("loan_apply_no") Long loanApplyNo) {
        //return financingAO.getLoanApply(loanApplyNo);
        return loanService.getLoanApply(loanApplyNo);
    }

    /**
     * 利息列表（支持分页）
     */
    @RequestMapping(value = "/interest/page", method = RequestMethod.GET)
    @ResponseBody
    public F830202Req queryPaginableInterest(@RequestParam("contract_no") Long contractNo,
            @RequestParam("start_date") String startDate, @RequestParam("end_date") String endDate,
            @RequestParam("start") int start, @RequestParam("limit") int limit) {
        return financingAO.queryPaginableInterest(contractNo, startDate, endDate, start, limit);
    }

    /**
     * 利息详情
     */
    @RequestMapping(value = "/interest/detail", method = RequestMethod.GET)
    @ResponseBody
    public F830202Res getInterest(@RequestParam("interest_no") Long interestNo) {
        return financingAO.getInterest(interestNo);
    }

    /**
     * 还款列表（支持分页）
     */
    @RequestMapping(value = "/repay/page", method = RequestMethod.GET)
    @ResponseBody
    public F830212Req queryPaginableRepay(@RequestParam("contract_no") Long contractNo,
            @RequestParam("start_date") String startDate, @RequestParam("end_date") String endDate,
            @RequestParam("start") int start, @RequestParam("limit") int limit) {
        return financingAO.queryPaginableRepay(contractNo, startDate, endDate, start, limit);
    }

    /**
     * 还款详情
     */
    @RequestMapping(value = "/repay/detail", method = RequestMethod.GET)
    @ResponseBody
    public F830212Res getRepayOrder(@RequestParam("repay_no") Long repayNo) {
        return financingAO.getRepay(repayNo);
    }

    /************** 查询 end **************/

    /**
     * 获取推荐融资人个数
     */

    @RequestMapping(value = "/getLoanUserCount")
    @ResponseBody
    public F830223Res getLoanUserCount(@RequestParam(value = "userId", required = false) Long userId) {
        return financingAO.getLoanUserCount(userId);
    }

    /**
     * 获取总融资
     */

    @RequestMapping(value = "/getLoanTotalAmount")
    @ResponseBody
    public F830224Res getLoanTotalAmount() {
        return financingAO.getLoanTotalAmount();
    }
    
    
    @RequestMapping(value = "/contract.htm", method = RequestMethod.GET)
    public ModelAndView contract(@RequestParam(value = "product_type", required = false) String productType,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate, @RequestParam(value="start",defaultValue="0") int start,
            @RequestParam(value = "limit" ,defaultValue="10") int limit) {
        ModelAndView mav = new ModelAndView("financing/contract");
        FLoanContractReq req = loanService.queryLoanContractWithPage(getSessionUser().getUser_id(), productType,
                startDate, endDate, start, limit);
        List<com.hsnet.pz.biz.homs.dto.res.F830312Res> list = new ArrayList<com.hsnet.pz.biz.homs.dto.res.F830312Res>();
        for(FLoanContractRes flc : req.getItems()){
            com.hsnet.pz.biz.homs.dto.res.F830312Res res = homsAccountAO.getHomsAssetsInfo(this.getSessionUser().getUser_id(), flc.getHomsFundAccount(), flc.getHomsCombineId());
            list.add(res);
        }
        mav.addObject("list", list);
        mav.addObject("req", req);
        return mav;
    }
    @RequestMapping(value = "/apply.htm", method = RequestMethod.GET)
    public ModelAndView apply(@RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate, @RequestParam(value="start",defaultValue="0") int start,
            @RequestParam(value = "limit" ,defaultValue="10") int limit) {
        ModelAndView mav = new ModelAndView("financing/apply");
        FLoanApplyReq req = loanService.queryLoanContractApplyWithPage(getSessionUser().getUser_id(), status, startDate, endDate,
                start, limit);
        mav.addObject("req", req);
        return mav;
    }
    
    @RequestMapping(value = "/hiscontract.htm", method = RequestMethod.GET)
    public ModelAndView hiscontract(@RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate, @RequestParam(value="start",defaultValue="0") int start,
            @RequestParam(value = "limit" ,defaultValue="10") int limit) {
        ModelAndView mav = new ModelAndView("financing/hiscontract");
        FHisContractReq req = loanService.queryPaginableHisContract(getSessionUser().getUser_id(), startDate, endDate, start, limit);
        mav.addObject("req", req);
        return mav;
    }
}
