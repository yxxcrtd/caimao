package com.hsnet.pz.controller;

import com.caimao.bana.api.entity.req.account.FAccountChangeReq;
import com.caimao.bana.api.entity.zeus.ZeusHomsRepaymentExcludeEntity;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IAccountService;
import com.caimao.bana.api.service.IHomsAccountService;
import com.caimao.bana.api.service.IP2PService;
import com.hsnet.pz.ao.account.IHomsAccountAO;
import com.hsnet.pz.ao.financing.IFinancingAO;
import com.hsnet.pz.ao.infrastructure.IBasicDataAO;
import com.hsnet.pz.ao.stock.IStockAO;
import com.hsnet.pz.biz.account.dto.res.F830101Res;
import com.hsnet.pz.biz.base.dto.res.F830911Res;
import com.hsnet.pz.biz.base.dto.res.F830912Res;
import com.hsnet.pz.biz.homs.dto.res.F830309Res;
import com.hsnet.pz.biz.homs.dto.res.F830312Res;
import com.hsnet.pz.biz.loan.dto.req.*;
import com.hsnet.pz.biz.loan.dto.res.*;
import com.hsnet.pz.core.exception.BizServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资 Controller
 * 
 * @author: zhanggl10620
 * @since: 2014-8-14 上午11:08:29
 * @history:
 */
//@Controller
//@RequestMapping(value = "/financing")
public class FinancingController extends BaseController {

    @Resource
    private IFinancingAO financingAO;
    @Resource
    private IP2PService p2PService;
    @Resource
    IBasicDataAO basicDataAO;
    @Autowired
    private IHomsAccountAO homsAccountAO;
    @Resource
    private IHomsAccountService homsAccountService;
    @Autowired
    private IStockAO stockAO;
    @Resource
    private IAccountService accountService;

    /************** 借款 start **************/

    /**
     * (进入)借款(界面的)前提
     */
    @RequestMapping(value = "/loan/premise", method = RequestMethod.POST)
    @ResponseBody
    public boolean judgeLoanPremise(@RequestParam("produce_id") Long produceId) {
        financingAO.judgeLoanPremise(getSessionUser().getUser_id(), produceId);
        return true;
    }

    /**
     * 借款界面数据准备
     */
    @RequestMapping(value = "/loan/data", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doLoanData(
            @RequestParam("produce_id") long produceId) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 保证金
        long depositAmount = financingAO.getLoanDeposit(getSessionUser()
            .getUser_id());
        map.put("deposit", depositAmount);
        // 最大可借金额
        long maxLoanAmount = financingAO.getMaxLoanAmount(this.getSessionUser()
            .getUser_id(), produceId, depositAmount);
        map.put("maxLoan", maxLoanAmount);
        return map;
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

    /************** 借款 end **************/
    /************** 还款 start ************/

    /**
     * 用于还款前提
     */
    @RequestMapping(value = "/repay/premise", method = RequestMethod.POST)
    @ResponseBody
    public boolean judgeRepayPremise(
            @RequestParam("contract_no") Long contractNo) {
        financingAO.judgeRepayPremise(this.getSessionUser().getUser_id(),
            contractNo);
        return true;
    }

    /**
     * 还款界面数据准备
     */
    @RequestMapping(value = "/repay/data", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doRepayData(
            @RequestParam("contract_no") Long contractNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 还款合约
        F830204Res f830204Res = financingAO.getContract(getSessionUser()
            .getUser_id(), contractNo);
        map.put("contract", f830204Res);
        // 可用于还款的金额
        long availableAmount = financingAO.getRepayAmount(contractNo);
        // 获取财猫主账户的钱， 可用于还款的金额 - 财猫主账户的金额 = 实际可用户还款的金额
        F830101Res accountInfo = basicDataAO.getPZAccount(this.getSessionUser().getUser_id());
        Long caimaoAvailableAmount = accountInfo.getAvalaibleAmount() - accountInfo.getFreezeAmount();
        if (caimaoAvailableAmount > 0) {
            availableAmount -= caimaoAvailableAmount;
        }
        map.put("availableAmount", availableAmount);
        return map;

    }

    /**
     * 还款本金对应利息的计算
     */
    @RequestMapping(value = "/repayinterest", method = RequestMethod.POST)
    @ResponseBody
    public F830219Res doCalculateRepayInterest(
            @RequestParam("repay_amount") Long repayAmount,
            @RequestParam("contract_no") Long contractNo) {
        return financingAO.doCalculateRepayInterest(repayAmount, contractNo);
    }

    /**
     * 还款
     */
    @RequestMapping(value = "/repay", method = RequestMethod.POST)
    @ResponseBody
    public boolean doRepay(@RequestParam("trade_pwd") String tradePwd,
            @RequestParam("contract_no") Long contractNo,
            @RequestParam("repay_amount") Long repayAmount) throws Exception {
        // 获取合约详情
        F830204Res contractInfo = financingAO.getContract(getSessionUser().getUser_id(), contractNo);
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

        if (assetsInfo.getEnableWithdraw() < assetsInfo.getLoanAmount()) {
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
            financingAO.doRepay(getSessionUser().getUser_id(), tradePwd, contractNo, repayAmount);
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

    /************** 还款 end **************/
    /************** 追加 start **************/

    /**
     * 追加借款前提
     */
    @RequestMapping(value = "/add/premise", method = RequestMethod.POST)
    @ResponseBody
    public boolean judgeAddPremise(@RequestParam("contract_no") Long contractNo) {
        financingAO.judgeAddPremise(getSessionUser().getUser_id(), contractNo);
        return true;
    }

    /**
     * 申请追加借款
     */
    @RequestMapping(value = "/add/apply", method = RequestMethod.POST)
    @ResponseBody
    public Long applyAddLoan(@RequestParam("contract_no") Long contractNo,
            @RequestParam("trade_pwd") String tradePwd,
            @RequestParam("produce_id") Long produceId,
            @RequestParam("produce_term") Integer produceTerm,
            @RequestParam("deposit_amount") Long depositAmount,
            @RequestParam("add_amount") Long addAmount) {
        if(addAmount<=0 || depositAmount<=0){
            throw new BizServiceException("830408", "亲, 追加金额不能为负");
        }
        return financingAO.applyAddLoan(contractNo, getSessionUser()
            .getUser_id(), tradePwd, produceId, produceTerm, depositAmount,
            addAmount);
    }

    /************** 追加 end **************/
    /************** 展期 start **************/
    /**
     * 借款展期前提
     */
    @RequestMapping(value = "/defered/premise", method = RequestMethod.POST)
    @ResponseBody
    public boolean judgeDeferedPremise(
            @RequestParam("contract_no") Long contractNo) {
        financingAO.judgeDeferedPremise(getSessionUser().getUser_id(),
            contractNo);
        return true;
    }

    /**
     * 借款展期数据准备
     */
    @RequestMapping(value = "/defered/data", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doDeferedLoanData(
            @RequestParam("contract_no") Long contractNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 合约
        F830204Res f830204Res = financingAO.getContract(getSessionUser()
            .getUser_id(), contractNo);
        map.put("contract", f830204Res);
        // 产品信息
        F830911Res f830911Res = basicDataAO.getProduct(f830204Res.getProdId());
        map.put("product", f830911Res);
        List<F830912Res> F830912ResList = basicDataAO
            .queryProdDetailList(f830204Res.getProdId());
        map.put("productdetail", F830912ResList);
        // 保证金
        long depositAmount = financingAO.getDeferedDeposit(getSessionUser()
            .getUser_id(), contractNo);
        map.put("deposit", depositAmount);
        return map;
    }

    /**
     * 申请借款展期
     */
    @RequestMapping(value = "/defered/apply", method = RequestMethod.POST)
    @ResponseBody
    public Long applyDeferedLoan(@RequestParam("contract_no") Long contractNo,
            @RequestParam("trade_pwd") String tradePwd,
            @RequestParam("produce_id") Long produceId,
            @RequestParam("produce_term") Integer produceTerm) {
        return financingAO.applyDeferedLoan(contractNo, getSessionUser()
            .getUser_id(), tradePwd, produceId, produceTerm);
    }

    /************** 展期 end **************/
    /************** 查询 start **************/
    /**
     * 追加时获取最大可追加金额
     */
    @RequestMapping(value = "add/maxamount", method = RequestMethod.GET)
    @ResponseBody
    public long getAddMaxAmount(@RequestParam("contract_no") Long contractNo) {
        return financingAO.getAddAmountMax(getSessionUser().getUser_id(),
            contractNo);
    }

    /**
     * 获取用户总融资信息
     */
    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    @ResponseBody
    public F830216Res getStatisticInfo() {
        return financingAO.getTotalLoanAndBill(getSessionUser().getUser_id());
    }

    /**
     * 根据子账户查合约列表
     */
    @RequestMapping(value = "/contract/list", method = RequestMethod.GET)
    @ResponseBody
    public F830204Req queryContractList(
            @RequestParam("homs_fund_account") String homsFundAccount,
            @RequestParam("homs_combine_id") String homsCombineId) {
        return financingAO.queryContractList(getSessionUser().getUser_id(),
            homsFundAccount, homsCombineId);
    }

    /**
     * 根据合约的展期信息
     */
    @RequestMapping(value = "/defered/page", method = RequestMethod.GET)
    @ResponseBody
    public F830203Req queryContractDeferedList(
            @RequestParam("rel_contract_no") Long relContractNo,
            @RequestParam("start") int start, @RequestParam("limit") int limit) {
        return financingAO.queryContractDeferedList(getSessionUser()
            .getUser_id(), relContractNo, start, limit);
    }

    /**
     * 借款列表（支持分页）
     */
    @RequestMapping(value = "/contract/page", method = RequestMethod.GET)
    @ResponseBody
    public F830204Req queryPaginableContract(
            @RequestParam(value = "product_type", required = false) String productType,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
        return financingAO.queryPaginableContract(
            getSessionUser().getUser_id(), productType, startDate, endDate,
            start, limit);
    }

    /**
     * 借款详情
     */
    @RequestMapping(value = "/contract/detail", method = RequestMethod.GET)
    @ResponseBody
    public F830204Res getContract(@RequestParam("contract_no") Long contractNo) {
        return financingAO.getContract(getSessionUser().getUser_id(),
            contractNo);
    }

    /**
     * 历史借款列表（支持分页）
     */
    @RequestMapping(value = "/hiscontract/page", method = RequestMethod.GET)
    @ResponseBody
    public F830205Req queryPaginableHisContract(
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
        return financingAO.queryPaginableHisContract(getSessionUser()
            .getUser_id(), startDate, endDate, start, limit);
    }

    /**
     * 历史借款详情
     */
    @RequestMapping(value = "/hiscontract/detail", method = RequestMethod.GET)
    @ResponseBody
    public F830205Res getHisContract(
            @RequestParam("contract_no") Long contractNo) {
        return financingAO.getHisContract(getSessionUser().getUser_id(),
            contractNo);
    }

    /**
     * 借款申请列表（支持分页）
     */
    @RequestMapping(value = "/loanapply/page", method = RequestMethod.GET)
    @ResponseBody
    public F830203Req queryPaginableLoanApply(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
        return financingAO.queryPaginableLoanApply(getSessionUser()
            .getUser_id(), status, startDate, endDate, start, limit);
    }

    /**
     * 当前借款申请详情
     */
    @RequestMapping(value = "/loanapply/detail", method = RequestMethod.GET)
    @ResponseBody
    public F830203Res getLoanApply(
            @RequestParam("loan_apply_no") Long loanApplyNo) {
        return financingAO.getLoanApply(loanApplyNo);
    }

    /**
     * 利息列表（支持分页）
     */
    @RequestMapping(value = "/interest/page", method = RequestMethod.GET)
    @ResponseBody
    public F830202Req queryPaginableInterest(
            @RequestParam("contract_no") Long contractNo,
            @RequestParam("start_date") String startDate,
            @RequestParam("end_date") String endDate,
            @RequestParam("start") int start, @RequestParam("limit") int limit) {
        return financingAO.queryPaginableInterest(contractNo, startDate,
            endDate, start, limit);
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
    public F830212Req queryPaginableRepay(
            @RequestParam("contract_no") Long contractNo,
            @RequestParam("start_date") String startDate,
            @RequestParam("end_date") String endDate,
            @RequestParam("start") int start, @RequestParam("limit") int limit) {
        return financingAO.queryPaginableRepay(contractNo, startDate, endDate,
            start, limit);
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
    public F830223Res getLoanUserCount(@RequestParam(value="userId",required = false)Long userId){
    	return financingAO.getLoanUserCount(userId);
    }
    
    /**
     * 获取总融资
     */
    
    @RequestMapping(value = "/getLoanTotalAmount")
    @ResponseBody
    public F830224Res getLoanTotalAmount(){
		return financingAO.getLoanTotalAmount();
    }
}
