/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.zeus.controller.p2p;

import com.caimao.bana.api.entity.p2p.P2PConfigEntity;
import com.caimao.bana.api.entity.p2p.P2PLoanRecordEntity;
import com.caimao.bana.api.entity.req.FP2PQueryLoanPageInvestWithUserReq;
import com.caimao.bana.api.entity.req.FP2PQueryPageInvestListReq;
import com.caimao.bana.api.entity.req.FP2PQueryPageLoanAndUserReq;
import com.caimao.bana.api.entity.req.FP2PQueryStatisticsByUserReq;
import com.caimao.bana.api.entity.req.product.FQueryProductReq;
import com.caimao.bana.api.entity.res.F830910Res;
import com.caimao.bana.api.entity.res.FP2PParameterRes;
import com.caimao.bana.api.entity.res.product.FProductRes;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IP2PService;
import com.caimao.bana.api.service.product.IProductService;
import com.caimao.zeus.admin.controller.BaseController;
import com.caimao.zeus.util.PageUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zxd $Id$
 * 
 */
@Controller
@RequestMapping("/p2p/setting")
public class P2PSettingController extends BaseController {

    @Resource
    private IProductService prodService;

    @Resource
    private IP2PService p2pService;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 添加自定义的P2P借款记录
     * @return
     */
    @RequestMapping(value = "/add_custom_loan", method = RequestMethod.GET)
    public ModelAndView addCustomLoan() {
        return new ModelAndView("p2p/setting/add_custom_loan");
    }

    @RequestMapping(value = "/add_custom_loan", method = RequestMethod.POST)
    public ModelAndView addCustomLong(HttpServletRequest request, P2PLoanRecordEntity entity) throws Exception {
        entity.setTargetAmount(entity.getTargetAmount() * 100);
        this.p2pService.addCustomLoanRecord(entity);
        return jumpForSuccess(request, "添加自定义标的成功", "/p2p/setting/product/list");
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ModelAndView globalSetting() {
        ModelAndView mav = new ModelAndView("p2p/setting/globalSetting");
        List<FP2PParameterRes> list = p2pService.getP2PParameter();
        mav.addObject("list", list);
        return mav;
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ModelAndView productSetting() throws CustomerException {
        ModelAndView mav = new ModelAndView("p2p/setting/productList");
        FQueryProductReq req = new FQueryProductReq();
        // req.setProdStatus(EProdStatus.OPENING.getCode());
        List<F830910Res> list = prodService.getProdListByStatusWithPage(req);
        mav.addObject("list", list);
        return mav;
    }

    @RequestMapping(value = "/product/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam("prod_id") Long prodId) throws CustomerException {
        ModelAndView mav = new ModelAndView("p2p/setting/productP2PList");
        FProductRes prod = prodService.getProduct(prodId);
        List<P2PConfigEntity> list = p2pService.getProdSettingByProdId(prodId);
        Map<Integer,P2PConfigEntity> map = new HashMap<Integer,P2PConfigEntity>();
        for(P2PConfigEntity en : list){
            map.put(en.getProdLever(), en);
        }
        mav.addObject("map", map);
        mav.addObject("prodId", prodId);
        mav.addObject("min", prod.getProdLoanRatioMin());
        mav.addObject("max", prod.getProdLoanRatioMax());
        return mav;
    }

    @RequestMapping(value = "/product/leverEdit", method = RequestMethod.GET)
    public ModelAndView leverEdit(@RequestParam("prod_id") Long prodId, @RequestParam("lever") Integer lever) throws CustomerException {
        ModelAndView mav = new ModelAndView("p2p/setting/leverEdit");
        FProductRes prod = prodService.getProduct(prodId);
        P2PConfigEntity setting = null;
        boolean insert = false;
        if (!insert) {
            setting = p2pService.getProdSetting(prodId, lever);
        }
        if (setting == null) {
            setting = new P2PConfigEntity();
            setting.setProdId(prodId);
            setting.setProdLever(lever);
            setting.setIsAvailable(false);
            insert = true;
        }
        mav.addObject("setting", setting);
        mav.addObject("insert", insert);
        mav.addObject("prod", prod);
        return mav;
    }
    
    
    @RequestMapping(value = "/product/leverSave", method = RequestMethod.POST)
    public RedirectView leverSave(P2PConfigEntity config, @RequestParam(value = "insert")Boolean insert) throws CustomerException {
        RedirectView rv = new RedirectView("/p2p/setting/product/edit?prod_id="+config.getProdId());
        config.setChuziMax(config.getChuziMax()*100L);
        config.setChuziMin(config.getChuziMin()*100L);
        config.setManageFee(config.getManageFee()*100L);
        if(insert){
            p2pService.saveProdSetting(config);
        }else{
            p2pService.updateProdSetting(config);
        }
        return rv;
    }
    
    @RequestMapping(value = "/invest/list", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView investList(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) throws Exception {
        Integer limit = 100;
        Integer start = (page - 1) * limit;
        ModelAndView rv = new ModelAndView("p2p/list/investList");
        FP2PQueryStatisticsByUserReq req = new FP2PQueryStatisticsByUserReq();
        req.setUserName(userName);
        req.setMobile(mobile);
        req.setLimit(limit);
        req.setStart(start);
        req = p2pService.queryStatisticsByUserWithPage(req);
        if(req.getItems() != null && req.getItems().size() > 0){
            PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(),
                    String.format("/p2p/setting/invest/list?userName=%s&mobile=%s&page=", userName == null ? "" : userName, mobile == null ? "" : mobile));
            String pageHtml = pageUtils.show();
            rv.addObject("pageHtml", pageHtml);
        }
        rv.addObject("req", req);
        rv.addObject("userName", userName);
        return rv;
    }

    @RequestMapping(value = "/invest/list2")
    public ModelAndView investList2(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "beginDateTime", required = false) String beginDateTime,
            @RequestParam(value = "endDateTime", required = false) String endDateTime,
            @RequestParam(value = "targetId", required = false) Long targetId,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("p2p/list/investList2");
        Integer limit = 100;
        Integer start = (page - 1) * limit;
        FP2PQueryPageInvestListReq req = new FP2PQueryPageInvestListReq();
        req.setUserName(userName);
        req.setMobile(mobile);
        req.setBeginDateTime(beginDateTime);
        req.setEndDateTime(endDateTime);
        req.setTargetId(targetId);
        req.setLimit(limit);
        req.setStart(start);

        if (StringUtils.isEmpty(req.getBeginDateTime())) {
            req.setBeginDateTime(sdf.format(new Date()));
        }
        if (StringUtils.isEmpty(req.getEndDateTime())) {
            req.setEndDateTime(sdf.format(new Date()));
        }
        req.setBeginDateTime(req.getBeginDateTime()+" 00:00:00");
        req.setEndDateTime(req.getEndDateTime() + " 23:59:59");

        String isExp = request.getParameter("isExp");
        if(isExp != null && isExp.equals("1")){
            req.setLimit(1000000);
            response.setContentType("application/msexcel;");
            response.setHeader("Content-Disposition", new String(("attachment;filename=" + "investList.xls").getBytes("GB2312"), "UTF-8"));
            mav.setViewName("p2p/list/investList2Exp");
        }
        req = p2pService.queryPageInvestListWithUser(req);
        if(req.getItems() != null && req.getItems().size() > 0){
            PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(),
                    String.format("/p2p/setting/invest/list2?userName=%s&mobile=%s&beginDateTime=%s&endDateTime=%s&targetId=%s&page=",
                            userName == null ? "" : userName, mobile == null ? "" : mobile, beginDateTime == null ? "" : beginDateTime, endDateTime == null ? "" : endDateTime, targetId == null ? "" : targetId));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }
        mav.addObject("req", req);

        return mav;
    }
    
    @RequestMapping(value = "/product/list")
    public ModelAndView productList(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "beginDateTime", required = false) String beginDateTime,
            @RequestParam(value = "endDateTime", required = false) String endDateTime,
            @RequestParam(value = "targetId", required = false) Long targetId,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("p2p/list/productList");
        Integer limit = 100;
        Integer start = (page - 1) * limit;
        FP2PQueryPageLoanAndUserReq req = new FP2PQueryPageLoanAndUserReq();
        req.setUserName(userName);
        req.setMobile(mobile);
        req.setBeginDateTime(beginDateTime);
        req.setEndDateTime(endDateTime);
        req.setTargetId(targetId);
        req.setLimit(limit);
        req.setStart(start);
        if (StringUtils.isEmpty(req.getBeginDateTime())) {
            req.setBeginDateTime(sdf.format(new Date()));
        }
        if (StringUtils.isEmpty(req.getEndDateTime())) {
            req.setEndDateTime(sdf.format(new Date()));
        }
        req.setBeginDateTime(req.getBeginDateTime()+" 00:00:00");
        req.setEndDateTime(req.getEndDateTime() + " 23:59:59");

        String isExp = request.getParameter("isExp");
        if(isExp != null && isExp.equals("1")){
            req.setLimit(1000000);
            response.setContentType("application/msexcel;");
            response.setHeader("Content-Disposition", new String(("attachment;filename=" + "productList.xls").getBytes("GB2312"), "UTF-8"));
            mav.setViewName("p2p/list/productListExp");
        }

        req = p2pService.queryPageLoanWithUser(req);
        if(req.getItems() != null && req.getItems().size() > 0){
            PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(),
                    String.format("/p2p/setting//product/list?userName=%s&mobile=%s&beginDateTime=%s&endDateTime=%s&targetId=%s&page=",
                            userName == null ? "" : userName, mobile == null ? "" : mobile, beginDateTime == null ? "" : beginDateTime, endDateTime == null ? "" : endDateTime, targetId == null ? "" : targetId));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }

        mav.addObject("req", req);
        return mav;
    }

    @RequestMapping(value = "/product/caimao_full", method = {RequestMethod.GET, RequestMethod.POST})
    public void caimaoFullTarget(HttpServletResponse response, @RequestParam(value = "target_id") Long targetId) throws Exception {
        this.p2pService.doFullCaimaoP2PInvest(targetId);
        response.sendRedirect("/p2p/setting/product/list");
    }
    
    @RequestMapping(value = "/product/invest", method = RequestMethod.GET)
    public ModelAndView productInvest(FP2PQueryLoanPageInvestWithUserReq req) throws Exception {
        ModelAndView rv = new ModelAndView("p2p/list/productInvest");
        req = p2pService.queryLoanPageInvestWithUser(req);
        rv.addObject("req", req);
        return rv;
    }
}
