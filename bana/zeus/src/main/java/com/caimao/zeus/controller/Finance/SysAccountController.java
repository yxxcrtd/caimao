package com.caimao.zeus.controller.Finance;

import com.caimao.zeus.admin.entity.req.ChangeBalanceJourReq;
import com.caimao.zeus.admin.controller.BaseController;
import com.caimao.zeus.admin.entity.SysAccount;
import com.caimao.zeus.admin.enums.TransType;
import com.caimao.zeus.admin.service.SysAccountService;
import com.caimao.zeus.util.PageUtils;
import com.caimao.zeus.util.SessionAware;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/finance/sysAccount")
public class SysAccountController extends BaseController{
    @Resource
    private SysAccountService sysAccountService;

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ModelAndView save(HttpServletRequest request) throws Exception{
        String id = request.getParameter("id");
        if (id != null && !"".equals(id)) {
            SysAccount sysAccount = sysAccountService.getSysAccountById(Long.parseLong(id));
            if (sysAccount == null) {
                return jumpForFail(request, "系统账户不存在", "/finance/sysAccount/list");
            } else {
                request.setAttribute("sysAccount", sysAccount);
            }
        }
        return new ModelAndView("finance/sysAccount/save");
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView doSave(HttpServletRequest request) throws Exception{
        String id = request.getParameter("id");
        String aliasName = request.getParameter("aliasName");
        String memo = request.getParameter("memo");
        if ("".equals("aliasName") || "".equals("memo")) {
            return jumpForFail(request, "别名和备注不能为空", "/finance/sysAccount/list");
        }
        Long sysAccountId = (id == null || "".equals(id)) ? null : Long.parseLong(id);
        Integer isSuccess = sysAccountService.saveSysAccount(aliasName, memo, sysAccountId);
        if (isSuccess == 1) {
            return jumpForSuccess(request, "系统账户编辑成功", "/finance/sysAccount/list");
        } else {
            return jumpForFail(request, "系统账户编辑失败", "/finance/sysAccount/list");
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() throws Exception{
        SessionAware.getRequest().setAttribute("sysAccountList", sysAccountService.getSysAccountList());
        return new ModelAndView("finance/sysAccount/list");
    }

    @RequestMapping(value = "/changeBalance", method = RequestMethod.GET)
    public ModelAndView changeBalance(HttpServletRequest request, @RequestParam Long id) throws Exception{
        if (id != null && id != 0) {
            SysAccount sysAccount = sysAccountService.getSysAccountById(id);
            if (sysAccount == null) {
                return jumpForFail(request, "系统账户不存在", "/finance/sysAccount/list");
            } else {
                request.setAttribute("sysAccount", sysAccount);
            }
        }
        return new ModelAndView("finance/sysAccount/changeBalance");
    }

    @RequestMapping(value = "/changeBalance", method = RequestMethod.POST)
    public ModelAndView doChangeBalance(HttpServletRequest request, @RequestParam Long id, @RequestParam Long amount) throws Exception{
        if (id != null && id != 0) {
            SysAccount sysAccount = sysAccountService.getSysAccountById(id);
            if (sysAccount == null) {
                return jumpForFail(request, "系统账户不存在", "/finance/sysAccount/list");
            } else {
                try{
                    sysAccountService.doChangeBalance(id, amount, TransType.ADMIN_CHANGE, 0L, "后台变更系统账户资产");
                }catch(Exception e){
                    return jumpForSuccess(request, "资金变动失败，失败原因：" + e.getMessage(), "/finance/sysAccount/list");
                }
                return jumpForSuccess(request, "资金变动成功", "/finance/sysAccount/list");
            }
        }
        return jumpForFail(request, "系统账户不存在", "/finance/sysAccount/list");
    }

    @RequestMapping(value = "/changeJourList", method = RequestMethod.GET)
    public ModelAndView changeJourList(ChangeBalanceJourReq req) throws Exception{
        ModelAndView rv = new ModelAndView("finance/sysAccount/changeJourList");
        req.setOrderColumn("id");
        req.setOrderDir("DESC");
        req = sysAccountService.queryChangeBalanceJourResWithPage(req);
        rv.addObject("req", req);
        return rv;
    }
}
