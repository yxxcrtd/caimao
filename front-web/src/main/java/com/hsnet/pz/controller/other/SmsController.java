/**
 * @Title SmsController.java 
 * @Package com.hsnet.pz.controller.other 
 * @Description 
 * @author miyb  
 * @date 2014-8-19 下午3:15:08 
 * @version V1.0   
 */
package com.hsnet.pz.controller.other;

import com.caimao.bana.common.api.exception.CustomerException;
import com.hsnet.pz.controller.MemberController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caimao.bana.api.service.SmsService;
import com.hsnet.pz.ao.infrastructure.ISmsAO;
import com.hsnet.pz.biz.base.enums.ESmsBizType;
import com.hsnet.pz.biz.user.dto.res.F830018Res;
import com.hsnet.pz.controller.BaseController;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * @author: miyb 
 * @since: 2014-8-19 下午3:15:08 
 * @history:
 */
@Controller
@RequestMapping(value = "/sms")
public class SmsController extends BaseController {
    @Resource
    protected SmsService smsService;

    /**
     * 检查是否是手机号格式
     * @param phone 手机号码
     * @throws CustomerException
     */
    public static void isMobile(String phone) throws CustomerException {
        Pattern p = Pattern.compile("^[1][0-9]{10}$"); // 验证手机号
        Matcher m = p.matcher(phone);
        boolean b = m.matches();
        if (b == false) {
            throw new CustomerException("手机号码格式错误", 888888);
        }
    }

    @RequestMapping(value = "/registercode", method = RequestMethod.POST)
    @ResponseBody
    public boolean sendRegisterCode(@RequestParam("mobile") String mobile) throws Exception {
        sendPhoneCode(ESmsBizType.REGISTER.getCode(), mobile);
        return true;
    }

    @RequestMapping(value = "/loginpwdcode", method = RequestMethod.POST)
    @ResponseBody
    public boolean sendLoginpwdCode(@RequestParam("mobile") String mobile) throws Exception {
        sendPhoneCode(ESmsBizType.FINDLONGINPWD.getCode(), mobile);
        return true;
    }

    @RequestMapping(value = "/changemobile", method = RequestMethod.POST)
    @ResponseBody
    public boolean sendChangeMobileCode(@RequestParam("mobile") String mobile) throws Exception {
        sendPhoneCode(ESmsBizType.CHANGEMOBILE.getCode(), mobile);
        return true;
    }

    @RequestMapping(value = "/code", method = RequestMethod.POST)
    @ResponseBody
    public boolean sendCheckCode(@RequestParam("biz_type") String bizType) throws Exception {
        F830018Res user = getUser();
        sendPhoneCode(bizType, user.getMobile());
        return true;
    }

    private void sendPhoneCode(String bizType, String mobile) throws Exception {
        // 检查手机号格式是否正确
        SmsController.isMobile(mobile);
        smsService.doSendSmsCheckCode(mobile, bizType);
    }
}
