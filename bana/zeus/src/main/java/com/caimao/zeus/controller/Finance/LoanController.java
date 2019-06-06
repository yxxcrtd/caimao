package com.caimao.zeus.controller.Finance;

import com.caimao.bana.api.entity.req.FTpzQueryLoanContractAllPageReq;
import com.caimao.bana.api.entity.res.FTpzQueryLoanContractAllPageRes;
import com.caimao.bana.api.service.ILoanService;
import com.caimao.bana.api.service.IZeusStatisticsService;
import com.caimao.zeus.admin.controller.BaseController;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/finance/loan")
public class LoanController extends BaseController{
    @Resource
    private ILoanService loanService;
    @Resource
    private IZeusStatisticsService zeusStatisticsService;

    @RequestMapping(value = "/contractList", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView contractList(FTpzQueryLoanContractAllPageReq req, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("finance/loan/contractList");

        req.setLimit(100);
        req.setCurrentPage(req.getCurrentPage());
        req.setOrderDir("DESC");
        req.setOrderColumn("contractNo");

        if(req.getContractStatus() != null && req.getContractStatus().equals("-1")) req.setContractStatus(null);

        String isExp = request.getParameter("isExp");
        if(isExp != null && isExp.equals("1")){
            req.setLimit(1000000);
            response.setContentType("application/msexcel;");
            response.setHeader("Content-Disposition", new String(("attachment;filename=" + "loanContract.xls").getBytes("GB2312"), "UTF-8"));
            mav.setViewName("finance/loan/contractListExp");
        }

        req = this.loanService.queryLoanContractAllPage(req);

        if(req.getContractStatus() != null && req.getContractStatus().equals("-1")) req.setContractStatus("-1");
        mav.addObject("list", req);
        return mav;
    }

    @RequestMapping(value = "/removeContract", method = RequestMethod.GET)
    public ModelAndView removeContract() throws Exception{
        return new ModelAndView("finance/loan/removeContract");
    }

    @RequestMapping(value = "/removeContract", method = RequestMethod.POST)
    public ModelAndView removeContract(@RequestParam("contractNo") Long contractNo, HttpServletRequest request) throws Exception{
        zeusStatisticsService.removeContract(contractNo);
        return this.jumpForSuccess(request, "合约移除成功", "/finance/loan/removeContract");
    }
}
