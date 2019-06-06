package com.fmall.bana.controller.user;

import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.enums.ESmsBizType;
import com.caimao.bana.api.service.SmsService;
import com.caimao.bana.common.api.exception.CustomerException;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.RedisUtils;
import com.fmall.bana.utils.Session.ControllerContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发送手机验证码的东东
 * Created by Administrator on 2015/12/25.
 */
@Controller
@RequestMapping(value = "/sms")
public class SMSController extends BaseController {

    @Resource
    protected SmsService smsService;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 检查是否是手机号格式
     * @param phone 手机号码
     * @throws CustomerException
     */
    public void isMobile(String phone) throws CustomerException {
        Pattern p = Pattern.compile("^[1][0-9]{10}$"); // 验证手机号
        Matcher m = p.matcher(phone);
        boolean b = m.matches();
        if (!b) {
            throw new CustomerException("手机号码格式错误", 888888);
        }
    }

    /**
     * 验证验证码
     * @param captcha 验证码
     * @throws Exception
     */
    public void checkCaptcha(String captcha) throws Exception{
        try{
            String sessionId = ControllerContext.getRequest().getSession().getId();
            boolean flag = imageCaptchaService.validateResponseForID(sessionId, captcha);
            if (!flag) { // 验证码正确
                throw new CustomerException("验证码不正确", 83099901);
            }
        }catch(Exception e){
            throw new CustomerException("验证码不正确", 83099901);
        }
    }

    /**
     * 短信发送限速终结者
     * 1分钟，请求的次数超过 5 次，就禁用IP 24个小时
     * @throws Exception
     */
    public void limiter() throws Exception {
        String ip = this.getRemoteHost();
        if (ip == null) return;
        Object forbid = this.redisUtils.get("forbid_"+ip);
        if (forbid != null) {
            throw new CustomerException("IP被禁用", 888888);
        }
        String redisKey = "limit_num_" + ip;
        Object callNum = this.redisUtils.get(redisKey);
        if (callNum == null) {
            callNum = 0;
        }
        Integer num = Integer.valueOf(callNum.toString());
        num++;
        if (num >= 5) {
            this.redisUtils.set("forbid_"+ip, "1", 86400L);
        }
        this.redisUtils.set(redisKey, num.toString(), 60L);
    }

    /**
     * 发送手机注册验证码
     * 请求地址：/sms/registercode
     *
     * @param mobile 手机号码
     * @return  成功返回true
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/registercode")
    public boolean sendRegisterCode(
            @RequestParam("mobile") String mobile,
            @RequestParam(value = "captcha", required = false) String captcha
    ) throws Exception {
        if(captcha == null || captcha.equals("")){
            throw new CustomerException("请输入验证码", 10021);
        }
        //此处增加验证码
        this.checkCaptcha(captcha);
        //限速
        this.limiter();
        sendPhoneCode(ESmsBizType.REGISTER.getCode(), mobile);
        return true;
    }

    /**
     * 发送找回密码的手机验证码
     * 请求地址：/sms/findpwdcode
     *
     * @param mobile    手机号码
     * @return  成功返回true
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/findpwdcode")
    public boolean sendLoginpwdCode(@RequestParam("mobile") String mobile) throws Exception {
        this.limiter();
        sendPhoneCode(ESmsBizType.FINDLONGINPWD.getCode(), mobile);
        return true;
    }

    /**
     * 发送修改手机号的验证码
     * 请求地址：/sms/changemobile
     *
     * @param mobile    手机号码
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/changemobile", method = RequestMethod.POST)
    public boolean sendChangeMobileCode(@RequestParam("mobile") String mobile) throws Exception {
        this.limiter();
        sendPhoneCode(ESmsBizType.CHANGEMOBILE.getCode(), mobile);
        return true;
    }

    /**
     * 登陆后发送其他类型的手机验证码
     * 请求地址 ： /sms/auth_code
     *
     * @param bizType   验证类型
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/auth_code", method = RequestMethod.POST)
    public boolean sendCheckCode(@RequestParam("bizType") String bizType) throws Exception {
        TpzUserEntity user = this.getCaimaoUser();
        sendPhoneCode(bizType, user.getMobile());
        return true;
    }

    /**
     * 发送验证码的方法
     * @param bizType   验证码类型
     * @param mobile    手机号
     * @throws Exception
     */
    private void sendPhoneCode(String bizType, String mobile) throws Exception {
        // 检查手机号格式是否正确
        this.isMobile(mobile);
        smsService.doSendSmsCheckCode(mobile, bizType);
    }

}
