/*
*HeepayController.java
*Created on 2015/4/23 16:08
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.controller;

import com.caimao.bana.api.entity.p2p.P2PConfigEntity;
import com.caimao.bana.api.entity.p2p.P2PInvestRecordEntity;
import com.caimao.bana.api.entity.p2p.P2PLoanRecordEntity;
import com.caimao.bana.api.entity.req.*;
import com.caimao.bana.api.entity.res.FP2PParameterRes;
import com.caimao.bana.api.entity.res.FP2PQueryLoanPageInvestWithUserRes;
import com.caimao.bana.api.entity.res.FP2PQueryUserPageInvestRes;
import com.caimao.bana.api.entity.res.FP2PQueryUserPageLoanRes;
import com.caimao.bana.api.enums.EP2PLoanStatus;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IP2PService;
import com.caimao.bana.api.utils.DateUtil;
import com.caimao.bana.utils.PageUtils;
import com.hsnet.pz.ao.infrastructure.IBasicDataAO;
import com.hsnet.pz.ao.util.RSAUtils;
import com.hsnet.pz.biz.account.dto.res.F830101Res;
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
import java.util.*;

@Controller
@RequestMapping(value = "/p2p")
public class P2PController extends BaseController {
    @Resource
    private IP2PService p2PService;
    @Autowired
    IBasicDataAO basicDataAO;

    // 获取产品的融资的配置
    @ResponseBody
    @RequestMapping(value = "/product/config", method = RequestMethod.GET)
    public Map<String, Object> configList(@RequestParam(value = "product_id") Long prodId) {
        List<P2PConfigEntity> configEntityList = this.p2PService.getProdSettingByProdId(prodId);
        List<FP2PParameterRes> parameterResList = this.p2PService.getP2PParameter();
        Map<String, Object> mapRes = new TreeMap<>();
        mapRes.put("productConfig", configEntityList);
        for(FP2PParameterRes parameterRes: parameterResList) {
            mapRes.put(parameterRes.getParamCode(), parameterRes.getParamValue());
        }
        return mapRes;
    }

    // 申请新的那个借贷融资
    @ResponseBody
    @RequestMapping(value = "/loan/apply", method = RequestMethod.POST)
    public Long addLoanApply(@RequestParam("trade_pwd") String tradePwd,
                             @RequestParam("produce_id") Long produceId,
                             @RequestParam("deposit_amount") Long depositAmount,
                             @RequestParam("produce_term") Integer produceTerm,
                             @RequestParam("loan_amount") Long loanAmount,
                             @RequestParam("lever") Integer lever,
                             @RequestParam("cmValue") Long cmValue,
                             @RequestParam("custumRate") Double custumRate
    ) throws Exception {
        FP2PAddLoanReq req = new FP2PAddLoanReq();
        req.setProdId(produceId);
        req.setUserId(this.getSessionUser().getUser_id());
        req.setPeroid(produceTerm);
        req.setMargin(depositAmount);
        req.setLoanValue(loanAmount);
        req.setCaimaoValue(cmValue);
        req.setLever(lever);
        req.setCustomRate(custumRate);
        req.setTradePwd(RSAUtils.decryptStringByJs(tradePwd));
        return this.p2PService.doAddLoan(req);
    }

    /**
     * BEGIN 列表页面
     */

    // 开始投资页面
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public ModelAndView queryPageLoan(
            @RequestParam(value = "targetStatus", required = false, defaultValue = "0") int targetStatus,
            @RequestParam(value = "order", required = false, defaultValue = "yearRate") String orderBy,
            @RequestParam(value = "asc", required = false, defaultValue = "0") int asc,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) throws Exception{
        ModelAndView mav = new ModelAndView("/p2p/index");
        Integer limit = 10;
        Integer start = (page - 1) * limit;

        List<String> orderByList = new ArrayList<>();
        orderByList.add("yearRate");
        orderByList.add("liftTime");
        orderByList.add("targetRate");
        orderByList.add("targetOver");
        if(!orderByList.contains(orderBy)) orderBy = "yearRate";

        FP2PQueryPageLoanReq fp2PQueryPageLoanReq = new FP2PQueryPageLoanReq();
        fp2PQueryPageLoanReq.setTargetStatus(new Byte(String.valueOf(targetStatus)));
        fp2PQueryPageLoanReq.setStart(start);
        fp2PQueryPageLoanReq.setLimit(limit);
        fp2PQueryPageLoanReq.setOrderColumn(orderBy);
        fp2PQueryPageLoanReq.setOrderDir(asc == 1 ? "ASC" : "DESC");

        if(targetStatus == 1){
            fp2PQueryPageLoanReq = p2PService.queryPageFullLoan(fp2PQueryPageLoanReq);
        }else{
            fp2PQueryPageLoanReq = p2PService.queryPageLoan(fp2PQueryPageLoanReq);
        }
        if (fp2PQueryPageLoanReq.getItems() != null) {
            for (int i = 0, j = fp2PQueryPageLoanReq.getItems().size(); i < j; i++) {
                fp2PQueryPageLoanReq.getItems().get(i).setTargetName(fp2PQueryPageLoanReq.getItems().get(i).getTargetName().replace("股票抵押借款", "续约借款"));
            }
        }

        if(fp2PQueryPageLoanReq.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, fp2PQueryPageLoanReq.getTotalCount(),
                    String.format("/p2p/index.html?targetStatus=%s&order=%s&asc=%s&page=", targetStatus, orderBy, asc));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }

        mav.addObject("targetStatus", targetStatus);
        mav.addObject("order", orderBy);
        mav.addObject("asc", asc);
        // 查询各个标的的数量
        mav.addObject("loanFullCount", p2PService.queryLoanFullCount());
        mav.addObject("loanUnfullCount", p2PService.queryLoanCount(0));
        mav.addObject("loan", fp2PQueryPageLoanReq);

        return mav;
    }

    // 投资页面详情
    @RequestMapping(value = "/targetDetail.html", method = RequestMethod.GET)
    public ModelAndView queryLoanDetail(
            @RequestParam(value = "target_id") Long targetId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) throws Exception{
        ModelAndView mav = new ModelAndView("/p2p/targetDetail");
        Integer limit = 10;
        Integer start = (page - 1) * limit;

        // 标的详情
        P2PLoanRecordEntity loanRecordEntity = p2PService.queryLoanRecord(targetId);

        // 查询标的投资列表
        FP2PQueryLoanPageInvestWithUserReq fp2PQueryLoanPageInvestWithUserReq = new FP2PQueryLoanPageInvestWithUserReq();
        fp2PQueryLoanPageInvestWithUserReq.setTargetId(targetId);
        fp2PQueryLoanPageInvestWithUserReq.setStart(start);
        fp2PQueryLoanPageInvestWithUserReq.setLimit(limit);
        fp2PQueryLoanPageInvestWithUserReq.setOrderColumn("invest_created");
        fp2PQueryLoanPageInvestWithUserReq.setOrderDir("DESC");

        FP2PQueryLoanPageInvestWithUserReq result = p2PService.queryLoanPageInvestWithUser(fp2PQueryLoanPageInvestWithUserReq);
        if(result.getItems() != null){
            for (FP2PQueryLoanPageInvestWithUserRes fp2PQueryLoanPageInvestWithUserRes:result.getItems()){
                fp2PQueryLoanPageInvestWithUserRes.setTargetName(fp2PQueryLoanPageInvestWithUserRes.getTargetName().replace("股票抵押借款", "借款"));
                fp2PQueryLoanPageInvestWithUserRes.setIsAdmin(false);
            }
        }

        if(result.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, result.getTotalCount(),
                    String.format("/p2p/targetDetail.html?target_id=%s&page=", targetId));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }

        mav.addObject("target", loanRecordEntity);
        mav.addObject("investList", result);

        return mav;
    }

    // 我的投资页面
    @RequestMapping(value = "/myInvest.html", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView queryUserPageInvest(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "status", required = false, defaultValue = "all") String investStatus,
            @RequestParam(value = "date_type", required = false, defaultValue = "0") Integer dateType
            ) throws Exception{
        ModelAndView mav = new ModelAndView("/p2p/myInvest");
        Integer limit = 10;
        Integer start = (page - 1) * limit;
        String startDate = null;
        String endDate = null;
        Integer subDays = null;
        Byte status = null;
        switch (dateType) {
            case 3:
            case 7:
            case 30:
            case 90:
            case 365:
                subDays = dateType;
                break;
            default:
        }
        if (subDays != null) {
            Date curDate = new Date();
            startDate =  DateUtil.convertDateToString(DateUtil.DISPLAY_DATE_FORMAT_STRING, DateUtil.addDays(curDate, -subDays));
            endDate = DateUtil.convertDateToString(DateUtil.DISPLAY_DATE_FORMAT_STRING, curDate);
        }
        status = investStatus.equals("all") ? null : new Byte(investStatus);
        mav.addObject("dateType", dateType);
        mav.addObject("investStatus", investStatus);

        // 查询我的投资汇总信息
        Map<String, Object> summaryInfo = p2PService.queryUserSummaryInfo(this.getSessionUser().getUser_id());

        // 查询我的投资项目
        FP2PQueryUserPageInvestReq fp2PQueryUserPageInvestReq = new FP2PQueryUserPageInvestReq();
        fp2PQueryUserPageInvestReq.setUserId(this.getSessionUser().getUser_id());
        fp2PQueryUserPageInvestReq.setStart(start);
        fp2PQueryUserPageInvestReq.setLimit(limit);
        fp2PQueryUserPageInvestReq.setOrderColumn("invest_created");
        fp2PQueryUserPageInvestReq.setOrderDir("DESC");
        fp2PQueryUserPageInvestReq.setInvestStatus(status);
        if (startDate != null) startDate += " 00:00:00";
        if (endDate != null) endDate += " 23:59:59";
        fp2PQueryUserPageInvestReq.setStartDate(startDate);
        fp2PQueryUserPageInvestReq.setEndDate(endDate);
        fp2PQueryUserPageInvestReq = p2PService.queryUserPageInvest(fp2PQueryUserPageInvestReq);
        if(fp2PQueryUserPageInvestReq.getItems() != null) {
            for (FP2PQueryUserPageInvestRes res : fp2PQueryUserPageInvestReq.getItems()) {
                res.setTargetName(res.getTargetName().replace("股票抵押借款", "续约借款"));
            }
        }
        if(fp2PQueryUserPageInvestReq.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, fp2PQueryUserPageInvestReq.getTotalCount(),
                    String.format("/p2p/myInvest.html?status=%s&date_type=%s&page=", status == null ? "all" : start, dateType));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }
        // 查询账户信息
        F830101Res accountInfo = null;
        try {
            accountInfo = basicDataAO.getPZAccount(this.getSessionUser().getUser_id());
        } catch (Exception e) {}

        mav.addObject("accountInfo", accountInfo);
        mav.addObject("summaryInfo", summaryInfo);
        mav.addObject("investList", fp2PQueryUserPageInvestReq);

        return mav;
    }

    @RequestMapping(value = "/target/userInvestRecord", method = RequestMethod.GET)
    @ResponseBody
    public P2PInvestRecordEntity queryUserInvestRecord(@RequestParam("investId") Long investId) throws Exception{
        P2PInvestRecordEntity entity = p2PService.queryUserInvestRecord(this.getSessionUser().getUser_id(), investId);
        if (entity != null) {
            entity.setTargetName(entity.getTargetName().replace("股票抵押借款", "续约借款"));
        }
        return entity;
    }


    @RequestMapping(value = "/investDetail.html", method = RequestMethod.GET)
    public ModelAndView investDetail(
            @RequestParam("invest_id") Long investId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) throws Exception{
        ModelAndView mav = new ModelAndView("/p2p/investDetail");
        Integer limit = 10;
        Integer start = (page - 1) * limit;
        // 用户投资信息
        P2PInvestRecordEntity recordEntity = p2PService.queryUserInvestRecord(this.getSessionUser().getUser_id(), investId);
        // 获取标的信息
        P2PLoanRecordEntity loanRecordEntity = p2PService.queryLoanRecord(recordEntity.getTargetId());
        // 用户发现记录
        FP2PQueryUserPageInterestReq fp2PQueryUserPageInterestReq = new FP2PQueryUserPageInterestReq();
        fp2PQueryUserPageInterestReq.setUserId(this.getSessionUser().getUser_id());
        fp2PQueryUserPageInterestReq.setInvestId(investId);
        fp2PQueryUserPageInterestReq.setStart(start);
        fp2PQueryUserPageInterestReq.setLimit(limit);
        fp2PQueryUserPageInterestReq.setOrderColumn("create_time");
        fp2PQueryUserPageInterestReq.setOrderDir("DESC");
        fp2PQueryUserPageInterestReq = p2PService.queryUserPageInterest(fp2PQueryUserPageInterestReq);

        if(fp2PQueryUserPageInterestReq.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, fp2PQueryUserPageInterestReq.getTotalCount(),
                    String.format("/p2p/investDetail.html?invest_id=%s&page=", investId));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }

        mav.addObject("investInfo", recordEntity);
        mav.addObject("target", loanRecordEntity);
        mav.addObject("interestList", fp2PQueryUserPageInterestReq);

        return mav;
    }


    /**
     * EDN 列表页面
     */


    @RequestMapping(value = "/target/userTargetPage", method = RequestMethod.GET)
    @ResponseBody
    public FP2PQueryUserPageLoanReq queryUserPageLoan(
            @RequestParam(value = "targetStatus", required = false) int targetStatus,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) throws Exception{

        FP2PQueryUserPageLoanReq fp2PQueryUserPageLoanReq = new FP2PQueryUserPageLoanReq();
        fp2PQueryUserPageLoanReq.setUserId(this.getSessionUser().getUser_id());
        fp2PQueryUserPageLoanReq.setTargetStatus(new Byte(String.valueOf(targetStatus)));
        fp2PQueryUserPageLoanReq.setStart(start);
        fp2PQueryUserPageLoanReq.setLimit(limit);
        fp2PQueryUserPageLoanReq.setOrderColumn("created");
        fp2PQueryUserPageLoanReq.setOrderDir("DESC");

        fp2PQueryUserPageLoanReq = p2PService.queryUserPageLoan(fp2PQueryUserPageLoanReq);
        if (fp2PQueryUserPageLoanReq.getItems() != null) {
            for (FP2PQueryUserPageLoanRes res : fp2PQueryUserPageLoanReq.getItems()) {
                res.setTargetName(res.getTargetName().replace("股票抵押借款", "续约借款"));
            }
        }
        return fp2PQueryUserPageLoanReq;
    }

    @RequestMapping(value = "/target/cancelTarget", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String cancelTarget(
            HttpServletResponse response,
            @RequestParam("targetId") Long targetId) throws Exception{
        try{
            p2PService.doFailedTarget(targetId, this.getSessionUser().getUser_id(), EP2PLoanStatus.CANCEL);
            response.sendRedirect("/pz/apply.html");
            return "取消成功";
        }catch(Exception e){
            throw new BizServiceException("10332", "取消失败,请刷新后尝试");
        }
    }
	
	@RequestMapping(value = "/invest/apply", method = RequestMethod.POST)
    @ResponseBody
    public Long applyInvest(@RequestParam("trade_pwd") String tradePwd,
            @RequestParam("target_id") Long targetId,
            @RequestParam("invest_value") Long investValue) throws Exception {
        if (investValue <= 0) {
            throw new CustomerException("投资金额错误", 888888);
        }
        FP2PAddinvestReq req = new FP2PAddinvestReq();
        req.setUserId(getSessionUser().getUser_id());
        req.setInvestValue(investValue*100L);
        req.setTradePwd(RSAUtils.decryptStringByJs(tradePwd));
        req.setTargetId(targetId);
        return p2PService.doAddInvest(req);
	}

    @RequestMapping(value = "/target/queryLoanCount", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Integer queryLoanCount(@RequestParam("targetStatus") Integer targetStatus) throws Exception {
        if(targetStatus == 1){
            return p2PService.queryLoanFullCount();
        }else{
            return p2PService.queryLoanCount(targetStatus);
        }

    }
}
