/**
 * @Title HomsAccountController.java 
 * @Package com.hsnet.pz.controller 
 * @Description 
 * @author miyb  
 * @date 2014-12-16 下午7:46:56 
 * @version V1.0   
 */
package com.hsnet.pz.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caimao.bana.api.service.IHomsAccountService;
import com.hsnet.pz.ao.account.IHomsAccountAO;
import com.hsnet.pz.biz.homs.dto.res.F830301Res;
import com.hsnet.pz.biz.homs.dto.res.F830302Res;
import com.hsnet.pz.biz.homs.dto.res.F830312Res;
import com.hsnet.pz.core.exception.BizServiceException;

import javax.annotation.Resource;

/** 
 * @author: miyb 
 * @since: 2014-12-16 下午7:46:56 
 * @history:
 */
//@Controller
//@RequestMapping(value = "/homs")
public class HomsAccountController extends BaseController {
    @Autowired
    private IHomsAccountAO homsAccountAO;
    @Resource
    private IHomsAccountService homsAccountService;
    @RequestMapping(value = "/in", method = RequestMethod.POST)
    @ResponseBody
    public boolean homsIn(
            @RequestParam("homs_fund_account") String homsFundAccount,
            @RequestParam("homs_combine_id") String homsCombineId,
            @RequestParam("trans_amount") Long transAmount) throws Exception {
        if(transAmount<=0){
            throw new BizServiceException("830408", "亲, 追加金额不能为负");
        }
        homsAccountService.valideUserHomsAccount(getSessionUser().getUser_id(), homsCombineId,homsFundAccount);
        homsAccountAO.homsIn(getSessionUser().getUser_id(), homsFundAccount,
            homsCombineId, transAmount);
        return true;
    }

    @RequestMapping(value = "/out", method = RequestMethod.POST)
    @ResponseBody
    public boolean homsOut(
            @RequestParam("homs_fund_account") String homsFundAccount,
            @RequestParam("homs_combine_id") String homsCombineId,
            @RequestParam("trans_amount") Long transAmount) throws Exception {
        if(transAmount<=0){
            throw new BizServiceException("830408", "亲, 提赢金额不能为负");
        }
        homsAccountService.valideUserHomsAccount(getSessionUser().getUser_id(), homsCombineId,homsFundAccount);
        //提盈限制时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Integer currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        if(currentHour < 10 || currentHour > 15 || currentDay == 1 || currentDay == 7){
            throw new BizServiceException("830409", "目前只允许交易日 10 - 15点提盈");
        }
        homsAccountAO.homsOut(getSessionUser().getUser_id(), homsFundAccount,
            homsCombineId, transAmount);
        return true;
    }

    @RequestMapping(value = "/fundaccount", method = RequestMethod.GET)
    @ResponseBody
    public F830302Res getHomsFundAccount() {
        return homsAccountAO.getHomsFundAccount(this.getSessionUser()
            .getUser_id());
    }

    @ResponseBody
    @RequestMapping(value = "/combineid", method = RequestMethod.GET)
    public List<F830301Res> queryhomsCombineId() {
        return homsAccountAO.queryhomsCombineId(this.getSessionUser()
            .getUser_id());
    }

    @RequestMapping(value = "/assetsinfo", method = RequestMethod.GET)
    @ResponseBody
    public F830312Res getHomsAssetsInfo(
            @RequestParam("homs_fund_account") String homsFundAccount,
            @RequestParam("homs_combine_id") String homsCombineId) {
        return homsAccountAO.getHomsAssetsInfo(this.getSessionUser()
            .getUser_id(), homsFundAccount, homsCombineId);
    }

}
