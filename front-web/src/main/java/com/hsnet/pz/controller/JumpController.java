/**
 * @Title JumpController.java 
 * @Package com.hsnet.pz.controller 
 * @Description 
 * @author miyb  
 * @date 2014-12-11 下午2:06:23 
 * @version V1.0   
 */
package com.hsnet.pz.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hsnet.frontcore.utils.JacksonUtils;
import com.hsnet.pz.ao.infrastructure.IBasicDataAO;
import com.hsnet.pz.ao.session.SessionProvider;
import com.hsnet.pz.biz.user.dto.res.F830017Res;

/** 
 * @author: miyb 
 * @since: 2014-12-11 下午2:06:23 
 * @history:
 */
@Controller
@RequestMapping(value = "/premise")
public class JumpController extends BaseController {
    @Autowired
    IBasicDataAO basicDataAO;

    @RequestMapping(value = "/changemail", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView premiseChangemail() {
        F830017Res userTrade = basicDataAO.doGetTradePwd(getSessionUser()
            .getUser_id());
        String flag = "0";
        if (userTrade != null) {
            flag = "1";
        }
        return premiseTrade(flag, "user/changemail");
    }

    /**
     * 是否进行了交易密码的设置，如果没有，跳入中间页（导航页） 修改手机号码
     */
    @RequestMapping(value = "/changemobile", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView premiseChangemobile() {
        F830017Res userTrade = basicDataAO.doGetTradePwd(getSessionUser()
            .getUser_id());
        String flag = "0";
        if (userTrade != null) {
            flag = "1";
        }
        return premiseTrade(flag, "user/changemobile");
    }

    /**
     * 是否进行了实名认证、交易密码，绑定银行卡设置，如果没有，跳入中间页（导航页）     
     */
    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    public ModelAndView premiseRecharge() {
        return premise("", "", "", "account/recharge");
    }

    /**
     * 是否进行了实名认证、交易密码，绑定银行卡设置，如果没有，跳入中间页（导航页）     
     */
    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView premiseWithdraw() {
        return premise("", "", "", "account/withdraw");
    }

    /**
     * 是否进行了交易密码的设置，如果没有，跳入中间页（导航页）     
     * 找回密保问题
     */
    @RequestMapping(value = "/tradepwd/findquestion", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView premiseFindquestion() {

        return premiseTrade("", "account/tradepwd/findquestion");
    }

    /**
     * 我的融资前提
     * @return
     */
    @RequestMapping(value = "/loan.htm", method = RequestMethod.GET)
    public ModelAndView premiseLoan() {
        return premise("", "", "", "financing/loan");
    }

    /**
     * 借款前提
     * 短线宝借款
     */
    @RequestMapping(value = "/loan/dxb.htm", method = RequestMethod.GET)
    public ModelAndView preLoandxb() {
        return premise("", "", "", "financing/loan/dxb");
    }

    /**
     * 借款前提
     */
    @RequestMapping(value = "/loan/borrow.htm", method = RequestMethod.GET)
    public ModelAndView preLoan() {
        return premise("", "", "", "financing/loan/borrow");
    }

    /**
     * 还款前提
     */
    @RequestMapping(value = "/repayment/payback.htm", method = RequestMethod.GET)
    public ModelAndView preRepay() {
        return premise("", "", "", "financing/repayment/payback");
    }

    @RequestMapping(value = "/assets.htm", method = RequestMethod.GET)
    public ModelAndView assets() {
        return premise("", "", "", "trade/assets");
    }

    @RequestMapping(value = "/buy.htm", method = RequestMethod.GET)
    public ModelAndView buy() {
        return premise("", "", "", "trade/buy");
    }

    @RequestMapping(value = "/sell.htm", method = RequestMethod.GET)
    public ModelAndView sell() {
        return premise("", "", "", "trade/sell");
    }

    @RequestMapping(value = "/withdrawal.htm", method = RequestMethod.GET)
    public ModelAndView withdrawal() {
        return premise("", "", "", "trade/withdrawal");
    }

    @RequestMapping(value = "/entrust.htm", method = RequestMethod.GET)
    public ModelAndView entrust() {
        return premise("", "", "", "trade/entrust");
    }

    @RequestMapping(value = "/deal.htm", method = RequestMethod.GET)
    public ModelAndView deal() {
        return premise("", "", "", "trade/deal");
    }

    /**
     * 必须满足条件 为空表示不满足前提条件
     * 
     * @param tradePwdFlag
     * @param isTrust
     * @param bankCardFlag
     * @param url
     * @return
     */
    private ModelAndView premise(String tradePwdFlag, String isTrust,
            String bankCardFlag, String url) {
        if (!"1".equals(tradePwdFlag) || !"1".equals(isTrust)
                || !"1".equals(bankCardFlag)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("tradepwdFlag", tradePwdFlag);
            map.put("bankCardFlag", bankCardFlag);
            map.put("isTrust", isTrust);
            return new ModelAndView("prompt/warning", "map",
                JacksonUtils.toJson(map));
        }
        return new ModelAndView(url);
    }

    /**
     * 必须满足条件 为空表示不满足前提条件
     * 
     * @param tradePwdFlag
     * @param url
     * @return
     */
    private ModelAndView premiseTrade(String tradePwdFlag, String url) {
        if (!"1".equals(tradePwdFlag)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("tradepwdFlag", tradePwdFlag);
            return new ModelAndView("prompt/warningtrade", "map",
                JacksonUtils.toJson(map));
        }
        return new ModelAndView(url);
    }

    // cms ajax返回头部信息
    @RequestMapping(value = "/cms/topbar", method = RequestMethod.GET)
    public ModelAndView top() {
        return new ModelAndView("cms/topbar");
    }

    @RequestMapping(value = "/cms/loginB", method = RequestMethod.GET)
    public ModelAndView userLogin() {
        return new ModelAndView("cms/loginB");
    }

    // 在cms中点击退出按钮，清空session，并重定向到登录页面
    @RequestMapping(value = "/cms/logout.htm")
    @ResponseBody
    public ModelAndView logoutHtm() {
        sessionProvider.removeAttribute(SessionProvider.SESSION_KEY_USER);
        return new ModelAndView(new RedirectView("user/login"));
    }
}
