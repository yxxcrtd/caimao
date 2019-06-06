/*
*ChargeController.java
*Created on 2015/5/11 16:05
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.zeus.controller.User;

import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.TpzUserExtEntity;
import com.caimao.bana.api.entity.req.FAccountQueryAccountFrozenJourReq;
import com.caimao.bana.api.entity.req.FSmsListReq;
import com.caimao.bana.api.entity.req.FUserListReq;
import com.caimao.bana.api.entity.req.account.FAccountChangeReq;
import com.caimao.bana.api.entity.req.user.FUserProhiWithdrawReq;
import com.caimao.bana.api.entity.req.user.FUserQueryProhiWithdrawLogReq;
import com.caimao.bana.api.entity.res.FAccountQueryAccountFrozenJourRes;
import com.caimao.bana.api.entity.res.user.FUserQueryProhiWithdrawLogRes;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.api.enums.EUserProhiWithdrawStatus;
import com.caimao.bana.api.enums.EUserProhiWithdrawType;
import com.caimao.bana.api.service.IUserBankCardService;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.service.IZeusStatisticsService;
import com.caimao.bana.api.service.SmsService;
import com.caimao.bana.api.service.ybk.IYBKAccountService;
import com.caimao.zeus.admin.controller.BaseController;
import com.caimao.zeus.util.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户相关控制器
 */
@Controller
@RequestMapping(value = "/user/operation")
public class OperationController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(OperationController.class);

    @Resource
    private IUserService userService;

    @Resource
    private com.caimao.bana.api.service.IAccountService accountService;

    @Resource
    private IZeusStatisticsService zeusStatisticsService;

    @Resource
    public IYBKAccountService ybkAccountService;

    @Resource
    public IUserBankCardService userBankCardService;

    private static ResourceBundle appBundle = PropertyResourceBundle.getBundle("conf/application");

    @Resource
    private SmsService smsService;

    @RequestMapping(value = "/inviteChange", method = RequestMethod.GET)
    public ModelAndView inviteChange() throws Exception {
        return new ModelAndView("user/operation/inviteChange");
    }

    @RequestMapping(value = "/inviteChange", method = RequestMethod.POST)
    public ModelAndView doInviteChange(@RequestParam Long userId, @RequestParam Long refUserId, HttpServletRequest request) throws Exception {
        userService.doChangeUserRefUserId(userId, refUserId);
        return jumpForSuccess(request, "变更成功", "/user/operation/inviteChange");
    }

    @RequestMapping(value = "/prohi", method = {RequestMethod.GET})
    public ModelAndView prohiUser(
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/user/operation/prohi");
        Integer limit = 20;
        Integer start = (page - 1) * limit;
        mobile = mobile == "" ? null : mobile;
        type = type == "" ? null : type;
        status = status == "" ? null : status;

        TpzUserEntity userEntity = null;
        TpzUserExtEntity userExtEntity = null;
        Long userId = null;
        if (mobile != null) {
            userId = this.userService.getUserIdByPhone(mobile);
            if (userId != null) {
                userEntity = this.userService.getById(userId);
                userExtEntity = this.userService.getUserExtById(userId);
            }
        }
        // 查询用户的变动流水
        FUserQueryProhiWithdrawLogReq req = new FUserQueryProhiWithdrawLogReq();
        req.setUserId(userId);
        req.setType(type);
        req.setStatus(status);
        req.setLimit(limit);
        req.setStart(start);
        req.setOrderColumn("created");
        req.setOrderDir("DESC");
        req = this.userService.queryUserWithdrawStatusLog(req);
        if (req.getItems() != null && req.getItems().size() > 0) {
            for (FUserQueryProhiWithdrawLogRes entity : req.getItems()) {
                entity.setType(EUserProhiWithdrawType.findByCode(entity.getType()).getValue());
                entity.setStatus(EUserProhiWithdrawStatus.findByCode(entity.getStatus()).getValue());
            }
            PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(),
                    String.format("/user/operation/prohi?mobile=%s&type=%s&status=%s&page=", mobile == null ? "" : mobile, type == null ? "" : type, status == null ? "" : status));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }

        mav.addObject("mobile", mobile);
        mav.addObject("type", type);
        mav.addObject("status", status);
        mav.addObject("user", userEntity);
        mav.addObject("userExt", userExtEntity);
        mav.addObject("list", req);
        return mav;
    }

    @RequestMapping(value = "/prohi", method = {RequestMethod.POST})
    public ModelAndView doProhiUser(HttpServletRequest request,
                                    @RequestParam Long userId,
                                    @RequestParam(value = "type") Integer type,
                                    @RequestParam(value = "prohi_withdraw") Integer prohiWithdraw,
                                    @RequestParam(value = "memo") String memo) throws Exception {

        FUserProhiWithdrawReq req = new FUserProhiWithdrawReq();
        req.setUserId(userId);
        req.setType(type.toString());
        req.setStatus(prohiWithdraw.toString());
        req.setMemo(memo);

        this.userService.setUserWithdrawStatus(req);

        TpzUserEntity userEntity = this.userService.getById(userId);

        return jumpForSuccess(request, "变更成功", "/user/operation/prohi?mobile=" + userEntity.getMobile());
    }

    // 查看用户资产
    @RequestMapping(value = "/user_account", method = RequestMethod.GET)
    public ModelAndView userAccount(
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "bizType", required = false, defaultValue = "all") String bizType,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/user/operation/account");
        Integer limit = 20;
        Integer start = (page - 1) * limit;
        FAccountQueryAccountFrozenJourReq req = new FAccountQueryAccountFrozenJourReq();
        if (startDate == null) startDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (endDate == null) endDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (mobile != null) {
            Long userId = this.userService.getUserIdByPhone(mobile);
            if (userId != null) {
                TpzUserEntity userEntity = this.userService.getById(userId);
                // 获取用户的资金账户信息
                TpzAccountEntity accountEntity = this.accountService.getAccount(userId);
                mav.addObject("user", userEntity);
                mav.addObject("account", accountEntity);

                // 获取资金变动流水
                req.setUserId(userId);
                if (!bizType.equals("all")) req.setBizType(bizType);
                if (startDate != null) req.setStartDate(startDate + " 00:00:00");
                if (endDate != null) req.setEndDate(endDate + " 23:59:59");
                req.setLimit(limit);
                req.setStart(start);
                req = this.accountService.queryAccountFrozenJour(req);
                if (req.getItems() != null && req.getItems().size() > 0) {
                    for (FAccountQueryAccountFrozenJourRes Jourres : req.getItems()) {
                        Jourres.setAccountBizType(EAccountBizType.findByCode(Jourres.getAccountBizType()).getValue());
                    }
                    PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(),
                            String.format("/user/operation/user_account?mobile=%s&bizType=%s&startDate=%s&endDate=%s&page=", mobile, bizType, startDate, endDate));
                    String pageHtml = pageUtils.show();
                    mav.addObject("pageHtml", pageHtml);
                }
            }
        }
        mav.addObject("req", req);
        mav.addObject("mobile", mobile);
        mav.addObject("bizType", bizType);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);

        return mav;
    }

    // 修改用户冻结保证金
    @RequestMapping(value = "/user_account", method = RequestMethod.POST)
    public ModelAndView doUserAccount(HttpServletRequest request,
                                      @RequestParam(value = "userId") Long userId,
                                      @RequestParam(value = "bizType") String bizType,
                                      @RequestParam(value = "frozen") Double frozen) throws Exception {
        TpzUserEntity userEntity = this.userService.getById(userId);
        TpzAccountEntity accountEntity = this.accountService.getAccount(userId);
        FAccountChangeReq req = new FAccountChangeReq();
        req.setPzAccountId(accountEntity.getPzAccountId());
        req.setAmount(new BigDecimal(Math.abs(frozen * 100)).longValue());
        req.setAccountBizType(bizType);
        req.setSeqFlag(frozen < 0 ? ESeqFlag.GO.getCode() : ESeqFlag.COME.getCode());
        this.accountService.frozenAvailableAmount(req);

        return jumpForSuccess(request, "修改用户冻结资产成功", "/user/operation/user_account?mobile=" + userEntity.getMobile());
    }

    //查询每日新增数据
    @RequestMapping(value = "/dailyNewUser", method = RequestMethod.GET)
    public ModelAndView dailyNewUser(
            @RequestParam(value = "dateStart", required = false) String dateStart,
            @RequestParam(value = "dateEnd", required = false) String dateEnd,
            @RequestParam(value = "selectId", defaultValue = "1", required = false) Integer selectId,
            @RequestParam(value = "customId", required = false) String customId) throws Exception {
        //if(date == null) date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        ModelAndView mav = new ModelAndView("/user/operation/dailyNewUser");
        List<HashMap> dailyNewUserData = zeusStatisticsService.queryDailyNewUser(dateStart, dateEnd, selectId, customId);
        mav.addObject("dailyNewUserData", dailyNewUserData);
        mav.addObject("dateStart", dateStart);
        mav.addObject("dateEnd", dateEnd);
        mav.addObject("selectId", selectId);
        mav.addObject("customId", customId);
        return mav;
    }

    /**
     * 设置用户的昵称
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/set_user_nick_name", method = RequestMethod.GET)
    public boolean updateUserNickName(@RequestParam(value = "userId") Long userId, @RequestParam(value = "nickName") String nickName) throws Exception {
        this.userService.doSetUserNickName(userId, nickName);
        return true;
    }

    //用户列表
    @RequestMapping(value = "/userList", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView userList(FUserListReq req) throws Exception {
        ModelAndView mav = new ModelAndView("/user/operation/userList");
        if (req.getWithdrawStatus() == null || req.getWithdrawStatus().equals("-1") || req.getWithdrawStatus().equals("")) {
            req.setWithdrawStatus(null);
        }
        req.setLimit(50);
        req.setCurrentPage(req.getCurrentPage());
        req.setOrderColumn("register_datetime");
        req.setOrderDir("DESC");
        req = this.userService.queryUserList(req);
        mav.addObject("list", req);
        if (req.getWithdrawStatus() == null || req.getWithdrawStatus().equals("")) {
            req.setWithdrawStatus("-1");
        }
        return mav;
    }

    //查询邀请的实名认证数量
    @ResponseBody
    @RequestMapping(value = "/refRealUserList", method = {RequestMethod.POST, RequestMethod.GET}, produces = "text/html;charset=UTF-8")
    public String refRealUserList(
            @RequestParam(value = "refUserId") String refUserId,
            @RequestParam(value = "dateStart") String dateStart,
            @RequestParam(value = "dateEnd") String dateEnd) throws Exception {
        String returnHtml = "<table class=\"table table-hover table-bordered\"><thead><tr><th>用户编号</th><th>手机号</th><th>姓名</th><th>注册时间</th><th>邀请编号</th></tr></thead><tbody>";
        List<TpzUserEntity> dataList = zeusStatisticsService.queryDateRealUserList(refUserId, dateStart, dateEnd);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (dataList != null) {
            for (TpzUserEntity tpzUserEntity : dataList) {
                returnHtml += "<tr><td>" + tpzUserEntity.getUserId() + "</td><td>" + tpzUserEntity.getMobile() + "</td><td>" + tpzUserEntity.getUserRealName() + "</td><td>" + formatter.format(tpzUserEntity.getRegisterDatetime()) + "</td><td>" + tpzUserEntity.getRefUserId() + "</td></tr>";
            }
        }
        returnHtml += " </tbody></table>";
        return returnHtml;
    }

    /**
     * 已发短信但未注册的用户
     *
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "unRegister", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView unRegister(FSmsListReq req) throws Exception {
        ModelAndView mav = new ModelAndView("/user/operation/unRegister");
        req.setLimit(50);
        req.setCurrentPage(req.getCurrentPage());
        req = this.smsService.queryUnRegisterMobileWithPage(req);
        mav.addObject("list", req);
        return mav;
    }

}