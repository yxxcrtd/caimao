package com.hsnet.pz.controller;

import com.caimao.bana.api.entity.session.SessionUser;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.utils.Session.SessionHelper;
import com.caimao.bana.utils.Session.SessionProvider;
import com.hsnet.pz.ao.infrastructure.IBasicDataAO;
import com.hsnet.pz.ao.member.IMemberAO;
import com.hsnet.pz.ao.util.ImageUtils;
import com.hsnet.pz.biz.pay.dto.res.F830405Res;
import com.hsnet.pz.biz.pay.util.YiPayRSAUtil;
import com.hsnet.pz.biz.user.dto.req.F830025Req;
import com.hsnet.pz.biz.user.dto.req.F831014Req;
import com.hsnet.pz.biz.user.dto.res.F830018Res;
import com.hsnet.pz.biz.user.dto.res.F830022Res;
import com.hsnet.pz.core.exception.BizServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

//@Controller
//@RequestMapping(value = "/user")
public class MemberController extends BaseController {
    @Autowired
    IMemberAO memberAO;

    @Autowired
    IBasicDataAO basicDataAO;

    @Autowired
    private SessionProvider sessionProvider;

    // ****注册start************
    @RequestMapping(value = "/mobile/check", method = RequestMethod.POST)
    @ResponseBody
    public boolean isMobileExist(@RequestParam("mobile") String mobile) {
        memberAO.isMobileExist(mobile);
        return true;
    }

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @ResponseBody
    public boolean register(@RequestParam("login_name") String mobile,
            @RequestParam("login_pwd") String loginPwd,
            @RequestParam("sms_code") String smsCode,
            @RequestParam(value = "u", required = false) Long refUserId,HttpServletRequest request) {
         String u = (String) request.getSession().getAttribute("u");
         if(null == u){
             memberAO.doRegister(mobile, loginPwd, getRemoteHost(), smsCode,
                 refUserId);
         }else{
             memberAO.doRegister(mobile, loginPwd, getRemoteHost(), smsCode,
                 Long.valueOf(u));
             request.getSession().removeAttribute("u");
         }

        return true;
    }

    // ****注册end************
    // ****登录登出start*******
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public boolean login(@RequestParam("login_name") String loginName,
                         @RequestParam("login_pwd") String loginPwd,
                         @RequestParam(value = "captcha", required = false, defaultValue = "") String captcha) {
        //先检查验证码
//        String sessionId = ControllerContext.getRequest().getSession().getId();
//        if(fastHashMapCaptchaStore.hasCaptcha(sessionId)){//如果有验证码，必须验证
//            boolean flag = imageCaptchaService.validateResponseForID(sessionId,captcha);
//                if (!flag) { // 验证码正确
//                    throw new BizException("83099901", "验证码不正确");
//                }
//        };
        F830018Res f830018Res = memberAO.doLogin(loginName, loginPwd,
            getRemoteHost());

        if (f830018Res.getUserKind().trim().equals("10")
                || f830018Res.getUserKind().trim().equals("11")) {
            throw new BizServiceException("321414", "你的角色为理财人，不允许登录!");
        }

        // 存session
        SessionUser sessionUser = SessionHelper.setIdentity(f830018Res
                .getUserId());
        sessionProvider.setUserDetail(sessionUser);
        return true;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public boolean logout() {
        sessionProvider.removeAttribute(SessionProvider.SESSION_KEY_USER);
        return true;
    }

    // ****登录登出end************
    // ****实名认证start**********
    @RequestMapping(value = "/identity", method = RequestMethod.POST)
    @ResponseBody
    public boolean identity(@RequestParam("real_name") String realName,
            @RequestParam("idcard") String idcard) {
        memberAO.doVerifyIdentity(this.getSessionUser().getUser_id(), realName,
            idcard);
        return true;
    }

    // ****实名认证end************
    // ****交易密码start************
    @RequestMapping(value = "/tradepwd/set", method = RequestMethod.POST)
    @ResponseBody
    public boolean setTradePwd(@RequestParam("mobile") String mobile,
            @RequestParam("code") String code,
            @RequestParam("trade_pwd") String tradePwd) {
        memberAO.setTradePwd(getSessionUser().getUser_id(), mobile, code,
                tradePwd);
        return true;
    }

    @RequestMapping(value = "/tradepwd/reset", method = RequestMethod.POST)
    @ResponseBody
    public boolean resetTradePwd(
            @RequestParam("old_trade_pwd") String oldTradePwd,
            @RequestParam("new_trade_pwd") String newTradePwd) {
        memberAO.resetTradePwd(getSessionUser().getUser_id(), oldTradePwd,
                newTradePwd);
        return true;
    }

    @RequestMapping(value = "/findtradepwd/check", method = RequestMethod.POST)
    @ResponseBody
    public boolean findTradePwdCheck(@RequestParam("mobile") String mobile,
            @RequestParam("code") String code,
            @RequestParam("question_id") String questionId,
            @RequestParam("answer") String answer) {
        memberAO.findTradePwdCheck(getSessionUser().getUser_id(), mobile, code,
                questionId, answer);
        return true;
    }

    @RequestMapping(value = "/tradepwd/find", method = RequestMethod.POST)
    @ResponseBody
    public boolean findTradePwd(@RequestParam("mobile") String mobile,
            @RequestParam("code") String code,
            @RequestParam("question_id") String questionId,
            @RequestParam("answer") String answer,
            @RequestParam("trade_pwd") String tradePwd) {
        memberAO.findTradePwd(getSessionUser().getUser_id(), mobile, code,
                questionId, answer, tradePwd);
        return true;
    }

    // ****交易密码end************
    // ****银行卡start************

    @RequestMapping(value = "/bankcard/bind", method = RequestMethod.POST)
    @ResponseBody
    public F830405Res doBindBankcard(@RequestParam("bank_no") String bankNo,
                                     @RequestParam("bank_card_no") String bankCardNo,
                                     @RequestParam("bank_province") String province,
                                     @RequestParam("bank_city") String city,
                                     @RequestParam("bank_reg") String bankName) {
        try {
            //userBankCardService.doBindBankCard(getSessionUser().getUser_id(), HtmlRegexpUtil.filterHtml(bankNo),bankCardNo.replaceAll("\\D", ""), HtmlRegexpUtil.filterHtml(province), HtmlRegexpUtil.filterHtml(city), HtmlRegexpUtil.filterHtml(bankName));
            //return new F830405Res();
        } catch (Exception e) {
            if (e instanceof CustomerException) {
                throw new BizServiceException(((CustomerException) e).getCode().toString(), e.getMessage());
            }
            throw new BizServiceException("830405", "未知异常, 绑定银行卡操作失败, 请重试!");
        }
        return null;
    }

    // ****银行卡end************
    // ****登录密码start************
    @RequestMapping(value = "/loginpwd/reset", method = RequestMethod.POST)
    @ResponseBody
    public boolean resetLoginpwd(@RequestParam("oldPwd") String oldPwd,
            @RequestParam("newPwd") String newPwd) {
        memberAO.resetLoginpwd(getSessionUser().getUser_id(), oldPwd, newPwd);
        // 重新登录
        return logout();
    }

    @RequestMapping(value = "/loginpwd/mobile/get", method = RequestMethod.POST)
    @ResponseBody
    public String getBindMobile(@RequestParam("company") String company,
            @RequestParam("user_name") String userName,
            @RequestParam("captcha") String captcha) {
        return memberAO.getBindMobile(company, userName);
    }

    @RequestMapping(value = "/loginpwd/find", method = RequestMethod.POST)
    @ResponseBody
    public boolean findLoginPwd(@RequestParam("mobile") String mobile,
            @RequestParam("check_code") String checkCode,
            @RequestParam("user_pwd") String userPwd) {
        memberAO.doFindLoginPwd(mobile, checkCode, userPwd);
        return true;
    }

    // ****登录密码end************
    // **** 完善个人资料start************
    @RequestMapping(value = "/username/set", method = RequestMethod.POST)
    @ResponseBody
    public boolean doSetUserName(@RequestParam("user_name") String userName) {
        memberAO.doSetUserName(getSessionUser().getUser_id(), userName);
        return true;
    }

    @RequestMapping(value = "/enrich", method = RequestMethod.POST)
    @ResponseBody
    public boolean doEnrichUser(@RequestParam("nick_name") String nickName,
            @RequestParam("come_from") String comefrom,
            @RequestParam("address") String address,
            @RequestParam("occupation") String occupation,
            @RequestParam("education") String education,
            @RequestParam("inve_experience") String inveExperience) {
        memberAO.doRefreshUserExt(getSessionUser().getUser_id(), nickName,
            comefrom, address, occupation, education, inveExperience);
        return true;
    }

    // **** 完善个人资料end************

    // ****更改手机号start************

    @RequestMapping(value = "/newmobile/check", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkNewmobile(@RequestParam("mobile") String mobile) {
        memberAO.isMobileExist(mobile);
        return true;
    }

    @RequestMapping(value = "/mobile/reset", method = RequestMethod.POST)
    @ResponseBody
    public boolean resetMobile(@RequestParam("mobile") String mobile,
            @RequestParam("check_code") String checkCode,
            @RequestParam("trade_pwd") String tradePwd) {
        memberAO.doResetMobile(getSessionUser().getUser_id(), mobile,
            checkCode, tradePwd);
        return true;
    }

    // **** 更改手机号end************

    // **** 绑定/变更邮箱号start************
    @RequestMapping(value = "/email/change", method = RequestMethod.POST)
    @ResponseBody
    public boolean doBindOrChangeEmail(@RequestParam("email") String email,
            @RequestParam("tradePwd") String tradePwd) {
        memberAO.doBindOrChangeEmail(getSessionUser().getUser_id(), email,
            tradePwd);
        return true;
    }

    // **** 绑定/变更邮箱号end************

    // **** 密保问题start************

    @RequestMapping(value = "/pwdquestion/set", method = RequestMethod.POST)
    @ResponseBody
    public boolean setPwdAnswer(@RequestParam("question1") String qestion1,
            @RequestParam("answer1") String answer1,
            @RequestParam("question2") String qestion2,
            @RequestParam("answer2") String answer2,
            @RequestParam("question3") String qestion3,
            @RequestParam("answer3") String answer3) {
        memberAO.doSetPwdAnswer(getSessionUser().getUser_id(), qestion1,
            answer1, qestion2, answer2, qestion3, answer3);
        return true;
    }

    @RequestMapping(value = "/pwdquestion/reset", method = RequestMethod.POST)
    @ResponseBody
    public boolean resetPwdAnswer(@RequestParam("mobile") String mobile,
            @RequestParam("code") String code,
            @RequestParam("tradePwd") String tradePwd,
            @RequestParam("question1") String qestion1,
            @RequestParam("answer1") String answer1,
            @RequestParam("question2") String qestion2,
            @RequestParam("answer2") String answer2,
            @RequestParam("question3") String qestion3,
            @RequestParam("answer3") String answer3) {
        memberAO.doResetPwdAnswer(getSessionUser().getUser_id(), mobile, code,
            tradePwd, qestion1, answer1, qestion2, answer2, qestion3, answer3);
        return true;
    }

    @RequestMapping(value = "/findpwdquestion/check", method = RequestMethod.POST)
    @ResponseBody
    public boolean findQuestionCheck(@RequestParam("mobile") String mobile,
            @RequestParam("code") String code,
            @RequestParam("tradePwd") String tradePwd) {
        memberAO.findQuestionCheck(
            String.valueOf(getSessionUser().getUser_id()), mobile, code,
            tradePwd);
        return true;
    }

    // **** 密保问题end************
    // ****积分start************
    @RequestMapping(value = "/score/apply", method = RequestMethod.POST)
    @ResponseBody
    public boolean applyExchangeScore(@RequestParam("score") Integer score,
            @RequestParam("tradePwd") String tradePwd) {
        memberAO.applyExchangeScore(getSessionUser().getUser_id(), score,
            tradePwd);
        return true;
    }

    @RequestMapping(value = "/score/exchange", method = RequestMethod.POST)
    @ResponseBody
    public boolean directExchangeScore(@RequestParam("score") Integer score,
            @RequestParam("trade_pwd") String tradePwd) {
        memberAO.directExchangeScore(getSessionUser().getUser_id(), score,
            tradePwd);
        return true;
    }

    /**
     * 积分兑换列表（支持分页）
     */
    @RequestMapping(value = "/score/page", method = RequestMethod.GET)
    @ResponseBody
    public F831014Req queryPaginableScore(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
        return memberAO.queryPaginableScore(getSessionUser().getUser_id(),
            status, startDate, endDate, start, limit);
    }

    // ****积分end************
    // **** 头像上传start************

    @RequestMapping(value = "/avatar/set", method = RequestMethod.POST)
    @ResponseBody
    public String setAvatar(HttpServletRequest request)
            throws IllegalStateException, IOException {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
            request.getSession().getServletContext());
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
            .resolveMultipart(request);
        String filePath = memberAO.getFilePathForAvatar(
            String.valueOf(getSessionUser().getUser_id()), multipartRequest);
        return memberAO.setAvatar(getSessionUser().getUser_id(), filePath);
    }

    // 上传图片 兼容IE8

    @RequestMapping(value = "/avatar/set1", method = RequestMethod.POST)
    @ResponseBody
    public String setAvatar1(@RequestParam("file_name") String fileName,
            @RequestParam("w") String w, @RequestParam("h") String h,
            @RequestParam("l") String l, @RequestParam("t") String t) {

        String filePath = memberAO
            .getFilePathForAvatar1(
                    String.valueOf(getSessionUser().getUser_id()), fileName, w, h,
                    l, t);
        return memberAO.setAvatar(getSessionUser().getUser_id(), filePath);
    }

    // 上传预览图片 IE8

    @RequestMapping(value = "/avatar/preset", method = RequestMethod.POST)
    public ModelAndView preSetAvatar(HttpServletRequest request) {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
            request.getSession().getServletContext());
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
            .resolveMultipart(request);
        String filePath = memberAO.getPreFilePathForAvatar(
            String.valueOf(getSessionUser().getUser_id()), multipartRequest);
        // String res = memberAO.setAvatar(getSessionUser().getUser_id(),
        // filePath);
        ModelAndView mav = new ModelAndView("success/success");
        mav.addObject("msg", filePath + "?" + new Date().getTime());
        return mav;
    }

    // **** 头像上传end************
    // ****身份证上传start************
    @RequestMapping(value = "/idcard/upload", method = RequestMethod.POST)
    public ModelAndView uploadIdcard(HttpServletRequest request)
            throws IllegalStateException, IOException {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
            request.getSession().getServletContext());
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
            .resolveMultipart(request);
        // 正面身份证照片
        MultipartFile positiveFile = multipartRequest.getFile("positive");
        String positive = null;
        if (null == positiveFile
                || null == (positive = ImageUtils.compress(
                    positiveFile.getInputStream(), 450, 450))) {
            String path = getFilePath(request);
            File imagFile = new File(path);
            positive = ImageUtils.compress(new FileInputStream(imagFile), 450,
                450);
        }

        // 反面身份证照片
        MultipartFile oppositeFile = multipartRequest.getFile("opposite");
        String opposite = null;
        if (null == oppositeFile
                || null == (opposite = ImageUtils.compress(
                    oppositeFile.getInputStream(), 450, 450))) {
            String path = getFilePath(request);
            File imagFile = new File(path);
            opposite = ImageUtils.compress(new FileInputStream(imagFile), 450,
                450);
        }
        // 真实姓名
        String userRealName = multipartRequest.getParameter("userRealName");
        // 身份证号
        String idcard = multipartRequest.getParameter("idcard");
        memberAO.doUploadIdCardPic(getSessionUser().getUser_id(), idcard,
            userRealName, positive, opposite);
        ModelAndView mav = new ModelAndView("success/success");
        mav.addObject("msg", "");
        return mav;
    }

    // ****身份证上传end************
    // ****易登录start************
    @RequestMapping(value = "/yilogin/pre", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> preYiLogin(@RequestParam("phone") String phone,
            @RequestParam("time") String time,
            @RequestParam("token") String token) {
        boolean ipCheck = YiPayRSAUtil.checkIp(getRemoteHost());
        boolean tokenCheck = YiPayRSAUtil.checkLoginToken(token, phone, time);
        if (ipCheck && tokenCheck) {
            return memberAO.preYiLogin(phone);
        }
        return null;
    }

    /**
     * 推广用户列表
     * @return
     */
    @RequestMapping(value = "/queryRefUser", method = RequestMethod.GET)
    @ResponseBody
    public F830025Req queryRefUser(@RequestParam("refUserId") Long refUserId,
            @RequestParam("start") Integer start,
            @RequestParam("limit") Integer limit) {
        return memberAO.queryRefUser(refUserId, start, limit);
    }

    @RequestMapping(value = "/yilogin", method = RequestMethod.GET)
    public String yiLogin(@RequestParam("session") String session) {
        Long userId = memberAO.yiLogin(session);
        // 存session
        SessionUser sessionUser = SessionHelper.setIdentity(userId);
        sessionProvider.setUserDetail(sessionUser);
        return "home/index";
    }

    // ****易登录end************

    /**
     * 获取会员数
     */

    @RequestMapping(value = "/getRegisterUserCount")
    @ResponseBody
    public F830022Res getRegisterUserCount(
            @RequestParam(value = "userId", required = false) Long userId) {
        return memberAO.getRegisterUserCount(userId);
    }

    // 获取web部署路径
    private static String getFilePath(HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("/");
        path = path + "app"+File.separator+"resources"+File.separator+"image"+File.separator+"default.png";
        return path;
    }
    
}
