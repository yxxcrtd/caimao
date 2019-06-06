/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.service.loan;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.caimao.bana.api.entity.*;
import com.caimao.bana.api.entity.req.FTpzQueryLoanContractAllPageReq;
import com.caimao.bana.api.entity.req.FTpzQueryLoanContractPageReq;
import com.caimao.bana.api.entity.req.loan.FHisContractReq;
import com.caimao.bana.api.entity.req.loan.FLoanApplyReq;
import com.caimao.bana.api.entity.req.loan.FLoanContractReq;
import com.caimao.bana.api.entity.req.loan.FLoanP2PApplyReq;
import com.caimao.bana.api.entity.res.F830216Res;
import com.caimao.bana.api.entity.res.FTpzQueryLoanContractAllPageRes;
import com.caimao.bana.api.entity.res.loan.FHisContractRes;
import com.caimao.bana.api.entity.res.loan.FLoanApplyRes;
import com.caimao.bana.api.entity.res.loan.FLoanContractRes;
import com.caimao.bana.api.entity.res.loan.FLoanP2PApplyRes;
import com.caimao.bana.api.enums.EContractType;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.ILoanService;
import com.caimao.bana.server.dao.HisLoanContractDao;
import com.caimao.bana.server.dao.LoanApplyDao;
import com.caimao.bana.server.dao.LoanContractDao;
import com.caimao.bana.server.dao.accruedInterest.TpzAccruedInterestBillDao;
import com.caimao.bana.server.dao.p2p.P2PLoanRecordDao;
import com.caimao.bana.server.dao.prod.TpzProdDao;
import com.caimao.bana.server.service.helper.LoanRuleHelper;
import com.caimao.bana.server.utils.Constants;
import com.caimao.bana.server.utils.DateUtil;
import com.hsnet.pz.core.exception.BizServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yanjg
 * 2015年5月13日
 */
@Service("loanService")
public class LoanServiceImpl implements ILoanService{
    @Resource
    private LoanApplyDao loanApplyDAO;
    @Resource
    private LoanContractDao loanContractDao;
    @Resource
    private HisLoanContractDao hisLoanContractDao;
    @Resource
    private P2PLoanRecordDao P2PLoanRecordDao;
    @Autowired
    private TpzProdDao prodDao;
    @Autowired
    private LoanRuleHelper loanRuleHelper;
    @Autowired
    private LoanContractDao loanContractDAO;
    @Resource
    private TpzAccruedInterestBillDao tpzAccruedInterestBillDao;


    /**
     * 获取所有没有合约编号的p2p借贷
     * @return
     * @throws Exception
     */
    public List<TpzLoanApplyEntity> getP2PContractNoNullList() throws Exception{
        return loanApplyDAO.getP2PContractNoNullList();
    }

    /**
     * 查找并回写合约编号
     * @param tpzLoanApplyEntity
     * @throws Exception
     */
    public void doSearchP2PContractNo(TpzLoanApplyEntity tpzLoanApplyEntity) throws Exception{
        //先在当前合约中查找
        TpzLoanContractEntity tpzLoanContractEntity = loanContractDao.getContractByApplyRecord(tpzLoanApplyEntity);
        if(tpzLoanContractEntity != null){
            //更新借款申请中的合约编号
            loanApplyDAO.updateApplyContractNo(tpzLoanApplyEntity.getOrderNo(), tpzLoanContractEntity.getContractNo());
            //更新p2p借款表的合约编号
            P2PLoanRecordDao.updateP2PLoanContractNo(tpzLoanApplyEntity.getOrderNo(), tpzLoanContractEntity.getContractNo());
        }
        //再在历史合约中查找
        if(tpzLoanContractEntity == null){
            HisLoanContractEntity hisLoanContractEntity = hisLoanContractDao.getHisContractByApplyRecord(tpzLoanApplyEntity);
            if(hisLoanContractEntity != null){
                //更新借款申请中的合约编号
                loanApplyDAO.updateApplyContractNo(tpzLoanApplyEntity.getOrderNo(), hisLoanContractEntity.getContractNo());
                //更新p2p借款表的合约编号
                P2PLoanRecordDao.updateP2PLoanContractNo(tpzLoanApplyEntity.getOrderNo(), hisLoanContractEntity.getContractNo());
            }
        }
    }

    /**
     * 清除合约下一次结息日
     * @throws Exception
     */
    @Override
    public void cleanNextSettleInterestDate() throws Exception{
        if(Constants.P2P_LOAN_PZ_PRODUCT_ID != null && Constants.P2P_LOAN_PZ_PRODUCT_ID > 0){
            loanContractDao.cleanNextSettleInterestDate(Constants.P2P_LOAN_PZ_PRODUCT_ID);
        }
    }

    // 获取申请历史（普通配资申请与P2P配资申请合并）
    @Override
    public FLoanP2PApplyReq queryLoanP2PApplyList(FLoanP2PApplyReq req) throws Exception {
        List<FLoanP2PApplyRes> resList = this.loanApplyDAO.queryLoanP2PLiseWithPage(req);
        if (resList != null) {
            for (FLoanP2PApplyRes res : resList) {
                if (res.getLoanApplyAction().equals("9")) {
                    // 特殊处理下P2P的这个
                    if (res.getContractBeginDate() == null) {
                        res.setContractBeginDate(DateUtil.convertDateToString(DateUtil.DATA_TIME_PATTERN_1, res.getApplyDatetime()));
                    }
                    res.setContractEndDate(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, DateUtil.addDays(DateUtil.convertStringToDate(DateUtil.DATA_TIME_PATTERN_1, res.getContractBeginDate()), res.getProdTerm())));
                }
            }
        }
        req.setItems(resList);
        return req;
    }

    @Override
    public void canLoanApply(Long userId, Long prodId) throws CustomerException {
        TpzProdEntity prod = prodDao.getProd(prodId);

        loanRuleHelper.checkProdStatus(prod);
        loanRuleHelper.checkExistLoanApply(userId, prodId);
        loanRuleHelper.checkLoanMaxCount(userId, prod);
        loanRuleHelper.checkCashAmount(userId, prod);

    }

    /**
     * 涉及访问homs
     */
    // @Override
    // public long getDeposit(Long userId, String loanApplyAction, Long contractNo) throws CustomerException {
    // F830101Res res = accountDao.getByUserId(userId);
    // if (res == null) {
    // throw new CustomerException("", "账户不存在", "");
    // }
    // if (loanApplyAction.equals(ELoanApplyAction.ADD.getCode())) {
    // LoanContractEntity lc = (LoanContractEntity) loanContractDAO.getById(contractNo);
    // if ((lc.getContractType().equals(EContractType.SUB.getCode())) && (lc.getRelContractNo().longValue() != 0L)) {
    // contractNo = lc.getRelContractNo();
    // }
    // TpzHomsAccountLoan accountLoan = homsManager.getHomsAccountLoanByContractNo(contractNo);
    //
    // F830312Req req = new F830312Req();
    // req.setUserId(accountLoan.getUserId());
    // req.setHomsFundAccount(accountLoan.getHomsFundAccount());
    // req.setHomsCombineId(accountLoan.getHomsCombineId());
    // F830312Res f830312Res = accountHoms.getHomsAssetsInfo(req);
    // if (f830312Res.getTotalProfit().longValue() > 0L) {
    // return (res.getAvalaibleAmount().longValue() - res.getFreezeAmount().longValue() + f830312Res
    // .getTotalProfit().longValue());
    // }
    // }
    // return (res.getAvalaibleAmount().longValue() - res.getFreezeAmount().longValue());
    // }

    public long getMaxLoanAmount(Long userId, Long produceId, Long depositAmount) {
        TpzProdEntity prod = prodDao.getProd(produceId);
        long loanAmount = depositAmount * prod.getProdLoanRatioMax().longValue();
        if (loanAmount > prod.getProdAmountMax()) {
            loanAmount = prod.getProdAmountMax();
        }
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("prodId", produceId);
        long hasLoanAmount = this.loanContractDAO.getHasLoanAmount(param);
        return (loanAmount - hasLoanAmount);
    }

    public FLoanApplyReq queryLoanContractApplyWithPage(Long userId, String status, String startDate, String endDate,
                                                        int start, int limit) {
        FLoanApplyReq loanApplyReq = new FLoanApplyReq();
        loanApplyReq.setUserId(userId);
        loanApplyReq.setStart(start);
        loanApplyReq.setLimit(limit);
        loanApplyReq.setApplyDatetimeBegin(startDate);
        loanApplyReq.setApplyDatetimeEnd(endDate);
        loanApplyReq.setVerifyStatus(status);
        List<FLoanApplyRes> list = loanApplyDAO.queryLoanContractApplyWithPage(loanApplyReq);
        loanApplyReq.setItems(list);
        return loanApplyReq;
    }

    @Override
    public FLoanApplyReq queryContractDeferedList(Long userId, Long relContractNo, int start, int limit) {
        FLoanApplyReq req = new FLoanApplyReq();
        req.setUserId(userId);
        req.setRelContractNo(relContractNo);
        req.setStart(start);
        req.setLimit(limit);
        req.setLoanApplyAction("2");
        List<FLoanApplyRes> list = loanApplyDAO.queryLoanContractApplyWithPage(req);
        req.setItems(list);
        return req;
    }

    @Override
    public FLoanApplyRes getLoanApply(Long loanApplyNo) {
        FLoanApplyReq req = new FLoanApplyReq();
        req.setOrderNo(loanApplyNo);
        List<FLoanApplyRes> list = loanApplyDAO.queryLoanContractApplyWithPage(req);
        if (CollectionUtils.isNotEmpty(list)) {
            return ((FLoanApplyRes) list.get(0));
        }
        return new FLoanApplyRes();
    }

    @Override
    public FLoanContractReq queryLoanContractWithPage(Long userId, String productType, String startDate,
                                                      String endDate, int start, int limit) {
        FLoanContractReq req = new FLoanContractReq();
        req.setUserId(userId);
        req.setProdTypeId(productType);
        req.setSignDatetimeBegin(startDate);
        req.setSignDatetimeEnd(endDate);
        req.setStart(start);
        req.setLimit(limit);
        List<FLoanContractRes> list = this.loanContractDAO.queryLoanContractWithPage(req);
        req.setItems(list);
        return req;
    }
    /**
     * 查询合约列表(可选历史)
     * @param req
     * @param contractTable
     * @return
     * @throws Exception
     */
    @Override
    public FTpzQueryLoanContractPageReq queryLoanContractPage(FTpzQueryLoanContractPageReq req, String contractTable) throws Exception {
        if(contractTable.equals("his_tpz_loan_contract")){
            req.setItems(hisLoanContractDao.queryLoanHisContractWithPage(req));
        }else{
            req.setItems(loanContractDao.queryLoanContractWithPage(req));
        }
        return req;
    }

    @Override
    public FLoanContractReq queryContractList(Long userId, String homsFundAccount, String homsCombineId) {
        FLoanContractReq req = new FLoanContractReq();
        req.setUserId(userId);
        req.setHomsFundAccount(homsFundAccount);
        req.setHomsCombineId(homsCombineId);
        List<FLoanContractRes> list = this.loanContractDAO.queryLoanContractWithPage(req);
        req.setItems(list);
        return req;
    }

    @Override
    public FLoanContractRes getContract(Long userId, Long contractNo) {
        FLoanContractReq req = new FLoanContractReq();
        req.setUserId(userId);
        req.setContractNo(contractNo);
        List<FLoanContractRes> list = this.loanContractDAO.queryLoanContractWithPage(req);
        if (CollectionUtils.isNotEmpty(list)) {
            return ((FLoanContractRes) list.get(0));
        }
        return new FLoanContractRes();
    }

    @Override
    public FHisContractRes getHisContract(Long userId, Long contractNo) {
        FHisContractReq req = new FHisContractReq();
        req.setUserId(userId);
        req.setContractNo(contractNo);
        List<FHisContractRes> list = hisLoanContractDao.queryHisLoanContractPage(req);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return new FHisContractRes();
    }

    @Override
    public FHisContractReq queryPaginableHisContract(Long userId, String startDate, String endDate, int start, int limit) {
        FHisContractReq req = new FHisContractReq();
        req.setUserId(userId);
        req.setSignDatetimeBegin(startDate);
        req.setSignDatetimeEnd(endDate);
        req.setLimit(limit);
        req.setStart(start);
        List<FHisContractRes> list = hisLoanContractDao.queryHisLoanContractPage(req);
        req.setItems(list);
        return req;
    }

    @Override
    public long getAddAmountMax(Long userId, Long contractNo) {
        TpzLoanContractEntity lc = loanContractDAO.getById(contractNo);
        if (lc.getContractType().equals(EContractType.SUB.getCode())) {
            contractNo = lc.getRelContractNo();
        }
        TpzProdEntity prod = prodDao.getProd(lc.getProdId());
        Long amount = loanContractDAO.getTotalLoanAmount(contractNo);
        return (prod.getProdAmountMax() - amount);
    }

    @Override
    public F830216Res getTotalLoanAndBill(Long userId) {
        if (userId == null) {
            throw new BizServiceException("83021601", "用户编号不能为空");
        }
        F830216Res f830216Res = loanContractDAO.getTotalLoanAndBill(userId);
        if (f830216Res == null) {
            f830216Res = new F830216Res();
        }
        return f830216Res;
    }

    @Override
    public FTpzQueryLoanContractAllPageReq queryLoanContractAllPage(FTpzQueryLoanContractAllPageReq req) throws Exception {
        List<FTpzQueryLoanContractAllPageRes> list = loanContractDao.queryLoanContractAllWithPage(req);
        if(list != null){
            for (FTpzQueryLoanContractAllPageRes res:list){
                //计算应收利息
                Long interestTotal = 0L;
                //设置结束时间
                Date nowDate = new Date();
                Calendar calendar2 = Calendar.getInstance();
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                if(res.getContractStopDatetime().after(nowDate)){
                    calendar2.setTime(sf.parse(sf.format(new Date())));
                    calendar2.add(Calendar.DATE, 1);
                }else{
                    calendar2.setTime(res.getContractStopDatetime());
                }
                //设置开始时间
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(res.getContractSignDatetime());

                for(int i = 0; i <= 10000; i++){
                    if(i > 0){
                        if(res.getInterestAccrualMode().equals("1")){
                            calendar.add(Calendar.DATE, 1);
                            if(i > 0 && (calendar.get(Calendar.DAY_OF_WEEK) == 7 || calendar.get(Calendar.DAY_OF_WEEK) == 1)){
                                continue;
                            }
                        }else{
                            calendar.add(Calendar.DATE, 30);
                        }
                    }
                    if(calendar.compareTo(calendar2) > 0){
                        break;
                    }else{
                        interestTotal += new BigDecimal(res.getLoanAmount()).multiply(res.getInterestRate()).setScale(0, BigDecimal.ROUND_DOWN).longValue();
                    }
                }
                res.setInterestShould(interestTotal);
                //查询已收利息
                Integer billAmount = tpzAccruedInterestBillDao.queryBillAmountByContractNo(res.getContractNo());
                if(billAmount == null) billAmount = 0;
                res.setInterestAlready(billAmount.longValue());
            }
        }
        req.setItems(list);
        return req;
    }
}
