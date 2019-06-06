package com.caimao.zeus.controller.User;

import com.caimao.bana.api.entity.ybk.YBKAccountEntity;
import com.caimao.bana.api.service.IUserBankCardService;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.service.ybk.IYBKAccountService;
import com.caimao.gjs.api.entity.GJSAccountEntity;
import com.caimao.gjs.api.enums.ENJSBankNo;
import com.caimao.gjs.api.enums.ESJSBankNo;
import com.caimao.gjs.api.service.IAccountService;
import com.caimao.gjs.api.service.ITradeManageService;
import com.caimao.zeus.admin.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 用户详情
 */
@Controller
@RequestMapping("user")
public class UserDetailController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(UserDetailController.class);

    private static ResourceBundle appBundle = PropertyResourceBundle.getBundle("conf/application");

    @Resource
    private IUserService userService;

    @Resource
    private IAccountService gjsAccountService;

    @Resource
    public IYBKAccountService ybkAccountService;

    @Resource
    public IUserBankCardService userBankCardService;

    @Resource
    private ITradeManageService tradeManageService;

    /**
     * 用户详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public ModelAndView detail(@RequestParam(value = "id", required = false) Long id, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("user/operation/userProfileDetail");
        // 图片链接
        mav.addObject("ybkUrl", appBundle.getString("ybkUrl"));
        if (null != id) {
            // 用户基本信息
            mav.addObject("user", userService.getById(id));

            // 用户扩展信息
            mav.addObject("userExt", userService.getUserExtById(id));

            // 邮币卡开户信息
            List<YBKAccountEntity> list = ybkAccountService.queryByUserId(id);
            this.logger.info("用户信息：" + list.size());
            YBKAccountEntity entity = null;
            if (null != list && 0 < list.size()) {
                entity = list.get(0);
                // 根据开户银行的代码获取银行名称
                if (null != entity.getBankCode()) {
                    mav.addObject("bankTypeInfo", userBankCardService.getBankInfoById(entity.getBankCode(), 3L));
                }
                mav.addObject("ybkAccount", entity);
            }

            // 开户信息
            List<GJSAccountEntity> gjsAccountList = gjsAccountService.queryGJSAccountByUserId(id);
            if (null != gjsAccountList && 0 < gjsAccountList.size()) {
                for (GJSAccountEntity account : gjsAccountList) {
                    // NJS开户信息
                    if ("NJS".equals(account.getExchange())) {
                        mav.addObject("njs", account);
                        ENJSBankNo nsjBankNo = ENJSBankNo.findByCode(account.getBankId());
                        if (null != nsjBankNo) {
                            mav.addObject("njsBankName", nsjBankNo.getName());
                        }
                    }
                    // SJS开户信息
                    if ("SJS".equals(account.getExchange())) {
                        mav.addObject("sjs", account);
                        ESJSBankNo sjsBankNo = ESJSBankNo.findByCode(account.getBankId());
                        if (null != sjsBankNo) {
                            mav.addObject("sjsBankName", sjsBankNo.getName());
                        }
                    }
                }
            }
            return mav;
        } else {
            return jumpForFail(request, "用户ID参数不正确！", "userList");
        }
    }

    /**
     * 用户历史委托
     *
     * @param traderId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "historyEntrust", method = RequestMethod.GET)
    public ModelAndView historyEntrust(@RequestParam(value = "traderId", required = false) String traderId, @RequestParam(value = "exchange", required = false) String exchange, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        if (null != traderId && !"".equals(traderId)) {
            if ("njs".equals(exchange)) {
                mav.setViewName("user/operation/userNJSHistoryEntrust");
                mav.addObject("list", this.tradeManageService.queryNJSHistoryEntrustListByTraderIdForManage(traderId));
            }
            if ("sjs".equals(exchange)) {
                mav.setViewName("user/operation/userSJSHistoryEntrust");
                mav.addObject("list", this.tradeManageService.querySJSHistoryEntrustListByTraderIdForManage(traderId));
            }
            return mav;
        } else {
            return jumpForFail(request, "历史委托的参数不正确！", "trade");
        }
    }

    /**
     * 用户历史成交
     *
     * @param traderId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "historyTrade", method = RequestMethod.GET)
    public ModelAndView historyTrade(@RequestParam(value = "traderId", required = false) String traderId, @RequestParam(value = "exchange", required = false) String exchange, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        if (null != traderId && !"".equals(traderId)) {
            if ("njs".equals(exchange)) {
                mav.setViewName("user/operation/userNJSHistoryTrade");
                mav.addObject("list", this.tradeManageService.queryNJSHistoryTradeListByTraderIdForManage(traderId));
            }
            if ("sjs".equals(exchange)) {
                mav.setViewName("user/operation/userSJSHistoryTrade");
                mav.addObject("list", this.tradeManageService.querySJSHistoryTradeListByTraderIdForManage(traderId));
            }
            return mav;
        } else {
            return jumpForFail(request, "历史成交的参数不正确！", "trade");
        }
    }

    /**
     * 用户历史出入金
     *
     * @param traderId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "historyTransfer", method = RequestMethod.GET)
    public ModelAndView historyTransfer(@RequestParam(value = "traderId", required = false) String traderId, @RequestParam(value = "exchange", required = false) String exchange, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        if (null != traderId && !"".equals(traderId)) {
            if ("njs".equals(exchange)) {
                mav.setViewName("user/operation/userNJSHistoryTransfer");
                mav.addObject("list", this.tradeManageService.queryNJSHistoryTransferListByTraderIdForManage(traderId));
            }
            if ("sjs".equals(exchange)) {
                mav.setViewName("user/operation/userSJSHistoryTransfer");
                mav.addObject("list", this.tradeManageService.querySJSHistoryTransferListByTraderIdForManage(traderId));
            }
            return mav;
        } else {
            return jumpForFail(request, "历史出入金的参数不正确！", "trade");
        }
    }

}