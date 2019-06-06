/**
 * @Title HomsAccountController.java 
 * @Package com.hsnet.pz.controller 
 * @Description 
 * @author miyb  
 * @date 2014-12-16 下午7:46:56 
 * @version V1.0   
 */
package com.caimao.bana.controller.other;

import com.caimao.bana.api.service.IHomsAccountService;
import com.hsnet.pz.ao.account.IHomsAccountAO;
import com.hsnet.pz.biz.homs.dto.res.F830301Res;
import com.hsnet.pz.biz.homs.dto.res.F830302Res;
import com.hsnet.pz.biz.homs.dto.res.F830312Res;
import com.hsnet.pz.controller.BaseController;
import com.hsnet.pz.core.exception.BizServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** 
 * @author: miyb 
 * @since: 2014-12-16 下午7:46:56 
 * @history:
 */
@Controller
@RequestMapping(value = "/homs")
public class HomsAccountController extends BaseController {
    @Autowired
    private IHomsAccountAO homsAccountAO;
    @Resource
    private IHomsAccountService homsAccountService;


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
