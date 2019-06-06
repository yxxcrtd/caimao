package com.caimao.zeus.controller.Finance;

import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.req.FTPZQueryAccountJourStreamReq;
import com.caimao.bana.api.entity.req.FZeusUserBalanceDailyReq;
import com.caimao.bana.api.entity.req.FZeusUserBalanceReq;
import com.caimao.bana.api.service.IAccountService;
import com.caimao.bana.api.service.IZeusStatisticsService;
import com.caimao.zeus.admin.controller.BaseController;
import com.caimao.zeus.util.UserFinanceUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/finance/userBalance")
public class UserBalanceController extends BaseController{
    @Resource
    private IAccountService accountService;
    @Resource
    private IZeusStatisticsService zeusStatisticsService;

    @RequestMapping(value = "/balanceList", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView balanceList(FZeusUserBalanceReq req, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("finance/userBalance/balanceList");

        req.setLimit(100);
        req.setCurrentPage(req.getCurrentPage());
        req.setOrderDir("DESC");
        req.setOrderColumn("avalaible_amount");

        String isExp = request.getParameter("isExp");
        if(isExp != null && isExp.equals("1")){
            req.setLimit(1000000);
            response.setContentType("application/msexcel;");
            response.setHeader("Content-Disposition", new String(("attachment;filename=" + "userBalance.xls").getBytes("GB2312"), "UTF-8"));
            mav.setViewName("finance/userBalance/balanceListExp");
        }
        req = this.accountService.queryUserBalanceList(req);
        mav.addObject("list", req);
        return mav;
    }

    @RequestMapping(value = "/financeDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView financeDetail(FTPZQueryAccountJourStreamReq req) throws Exception {
        ModelAndView mav = new ModelAndView("finance/userBalance/financeDetail");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(req.getDateStart())) {
            Calendar theCa = Calendar.getInstance();
            theCa.setTime(new Date());
            theCa.add(Calendar.DATE, -30);
            req.setDateStart(sdf.format(theCa.getTime()));
        }
        if(StringUtils.isEmpty(req.getDateEnd())) {
            req.setDateEnd(sdf.format(new Date()));
        }
        mav.addObject("req", req);

        HashMap<String, Object> balanceDetail = this.zeusStatisticsService.queryUserFinanceStream(req);
        HashMap<String, List<HashMap<String, String>>> accountBizTypeMap = UserFinanceUtils.getAccountBizTypeMap();

        //查询总额

        //查询当前资金
        if(req.getUserId() != null){
            TpzAccountEntity tpzAccountEntity = accountService.getAccount(req.getUserId());
            mav.addObject("tpzAccount", tpzAccountEntity);
        }

        mav.addObject("accountIn", accountBizTypeMap.get("accountIn"));
        mav.addObject("accountOut", accountBizTypeMap.get("accountOut"));
        mav.addObject("financeDetail", balanceDetail);
        return mav;
    }

    @RequestMapping(value = "/balanceDailyList", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView balanceDailyList(FZeusUserBalanceDailyReq req, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("finance/userBalance/balanceDailyList");

        req.setLimit(100);
        req.setCurrentPage(req.getCurrentPage());
        req.setOrderDir("DESC");
        req.setOrderColumn("created");

        String isExp = request.getParameter("isExp");
        if(isExp != null && isExp.equals("1")){
            req.setLimit(1000000);
            response.setContentType("application/msexcel;");
            response.setHeader("Content-Disposition", new String(("attachment;filename=" + "userBalanceDaily.xls").getBytes("GB2312"), "UTF-8"));
            mav.setViewName("finance/userBalance/balanceDailyListExp");
        }
        req = this.zeusStatisticsService.queryUserBalanceDailyList(req);
        System.out.println(ToStringBuilder.reflectionToString(req.getItems().get(0)));
        mav.addObject("list", req);
        return mav;
    }

    @RequestMapping(value = "/homsLog", method = RequestMethod.GET)
    public ModelAndView homsLog(@RequestParam(value = "userId", required = false) Long userId) throws Exception {
        ModelAndView mav = new ModelAndView("finance/userBalance/homsLog");
        if(userId != null){
            List<HashMap<String, Object>> homsDetail = this.zeusStatisticsService.queryUserHomsStatistics(userId);
            mav.addObject("homsDetail", homsDetail);
        }
        mav.addObject("userId", userId);
        return mav;
    }
}