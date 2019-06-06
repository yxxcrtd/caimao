package com.caimao.bana.controller;

import com.caimao.bana.api.entity.InviteInfoEntity;
import com.caimao.bana.api.entity.req.FInviteReturnPageReq;
import com.caimao.bana.api.service.InviteInfoService;
import com.caimao.bana.api.service.InviteReturnHistoryService;
import com.caimao.bana.utils.PageUtils;
import com.caimao.bana.utils.StringHandleUtils;
import com.hsnet.pz.biz.user.dto.req.F830025Req;
import com.hsnet.pz.biz.user.dto.req.F831014Req;
import com.hsnet.pz.biz.user.dto.res.F830025Res;
import com.hsnet.pz.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/popularize")
public class PopularizeController extends BaseController{
    @Resource
    private InviteInfoService inviteInfoService;
    @Resource
    private InviteReturnHistoryService inviteReturnHistoryService;

    @RequestMapping(value = "/index.htm", method = RequestMethod.GET)
    public String index(Model model) throws Exception{
        InviteInfoEntity inviteInfoEntity = inviteInfoService.getInviteInfo(getSessionUser().getUser_id());
        model.addAttribute("inviteInfo", inviteInfoEntity);
        model.addAttribute("userDetail", getCaimaoUser());
        model.addAttribute("pop_bar_cur", "1");
        return "popularize/index";
    }

    @RequestMapping(value = "/exchangeRecord.htm", method = RequestMethod.GET)
    public String ExchangeRecord(
            Model model,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "dateStep", required = false, defaultValue = "0") Integer dateStep,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception{
        String startDate = StringHandleUtils.getStartDate(dateStep);
        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() + 86400 * 1000));
        Integer limit = 10;

        F831014Req dataList = memberAO.queryPaginableScore(getSessionUser().getUser_id(), status, startDate, endDate, (page - 1) * limit, limit);
        if(dataList.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, dataList.getTotalCount(), String.format("/popularize/exchangeRecord.htm?status=%s&dateStep=%s&page=", status, dateStep));
            String pageHtml = pageUtils.show();
            model.addAttribute("pageHtml", pageHtml);
        }

        model.addAttribute("status", status);
        model.addAttribute("dateStep", dateStep);
        model.addAttribute("dataList", dataList);
        model.addAttribute("pop_bar_cur", "2");
        return "popularize/exchangeRecord";
    }

    @RequestMapping(value = "/refUser.htm", method = RequestMethod.GET)
    public String queryRefUser(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception{
        Integer limit = 10;
        F830025Req dataList = memberAO.queryRefUser(getSessionUser().getUser_id(), (page - 1) * limit, limit);
        if(dataList.getItems() != null){
            for (F830025Res f830025Res:dataList.getItems()){
                f830025Res.setMobile(f830025Res.getMobile().substring(0, 3) + StringUtils.rightPad("", 4, '*') + f830025Res.getMobile().substring(f830025Res.getMobile().length() - 3, f830025Res.getMobile().length()));
            }
            PageUtils pageUtils = new PageUtils(page, limit, dataList.getTotalCount(), "/popularize/refUser.htm?page=");
            String pageHtml = pageUtils.show();
            model.addAttribute("pageHtml", pageHtml);
        }

        model.addAttribute("dataList", dataList);
        model.addAttribute("pop_bar_cur", "3");
        return "popularize/refUser";
    }

    @RequestMapping(value = "/returnRecord.htm", method = RequestMethod.GET)
    public String queryReturnRecord(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception{
        Integer limit = 10;

        FInviteReturnPageReq returnPageReq = new FInviteReturnPageReq();
        returnPageReq.setUserId(getSessionUser().getUser_id());
        returnPageReq.setStart((page - 1) * limit);
        returnPageReq.setLimit(limit);
        FInviteReturnPageReq dataList = inviteReturnHistoryService.getInviteReturnHistoryListByUserId(returnPageReq);
        if(dataList.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, dataList.getTotalCount(), "/popularize/returnRecord.htm?page=");
            String pageHtml = pageUtils.show();
            model.addAttribute("pageHtml", pageHtml);
        }

        model.addAttribute("dataList", dataList);
        model.addAttribute("pop_bar_cur", "4");
        return "popularize/returnRecord";
    }
}
