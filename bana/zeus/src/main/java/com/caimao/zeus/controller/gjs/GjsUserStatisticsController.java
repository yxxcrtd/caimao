package com.caimao.zeus.controller.gjs;

import com.caimao.bana.api.entity.req.FUserListReq;
import com.caimao.bana.api.service.IUserService;
import com.caimao.gjs.api.service.IAccountService;
import com.caimao.gjs.api.service.ITradeManageService;
import com.caimao.zeus.admin.controller.BaseController;
import com.huobi.commons.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.Date;

/**
 * 用户基本信息统计
 */
@RestController
@RequestMapping("gjs/statistics")
public class GjsUserStatisticsController extends BaseController {

    /** 日志 */
    private Logger logger = LoggerFactory.getLogger(GjsUserStatisticsController.class);

    @Resource
    private IUserService userService;

    @Resource
    private IAccountService gjsAccountService;

    @Resource
    private ITradeManageService tradeManageService;

    /**
     * 用户基本信息统计
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "user", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView user(FUserListReq req) throws Exception {
        ModelAndView mav = new ModelAndView("gjs/statistics/UserStatisticsList");
        String originalStartDate = req.getStartDate();
        String originalEndDate = req.getEndDate();
        addOneDayForEndDate(req);

        // 累计用户数
        mav.addObject("userTotalCount", this.userService.queryUserCount());

        // 正常查询
        req.setLimit(50);
        req.setCurrentPage(req.getCurrentPage());
        req.setOrderColumn("register_datetime");
        req.setOrderDir("DESC");
        if ("2".equals(req.getStatus1()) || "3".equals(req.getStatus1()) || "4".equals(req.getStatus1())) {
            req.setStartDate(req.getStartDate().replaceAll("-", ""));
            req.setEndDate(req.getEndDate().replaceAll("-", ""));
        }
        req = this.userService.queryGjsUserStatisticsListWithPage(req);
        req.setStartDate(originalStartDate);
        req.setEndDate(originalEndDate);
        mav.addObject("list", req);
        return mav;
    }

    /**
     * 交易
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "trade", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView trade(FUserListReq req) throws Exception {
        String e = req.getE();
        if (null == e || "".equals(e)) {
            e = "njs";
        }
        ModelAndView mav = new ModelAndView("gjs/statistics/UserTradeStatisticsList");
        String originalStartDate = req.getStartDate();
        String originalEndDate = req.getEndDate();

        // 正常查询
        req.setLimit(50);
        req.setCurrentPage(req.getCurrentPage());
        req.setExchange(e);

        if ("sjs".equals(e) || null != req.getStartDate() && null != req.getEndDate() && (null == req.getStatus1() || "".equals(req.getStatus1()) || "4".equals(req.getStatus1()))) {
            req.setStartDate(req.getStartDate().replaceAll("-", ""));
            req.setEndDate(req.getEndDate().replaceAll("-", ""));
        }

        req = this.userService.queryGjsUserTradeStatisticsListWithPage(req);

        // NJS交易额
        mav.addObject("njsTotalMoney", this.tradeManageService.getHistoryTradeTotalMoney("njs"));
        // NJS交易次数
        mav.addObject("njsTotalCount", this.tradeManageService.getHistoryTradeTotalCount("njs"));

        // 上金所09:00-11:30交易额
        mav.addObject("sjsTotalMoney1", this.tradeManageService.getHistoryTradeTotalMoney1());
        // 上金所09:00-11:30交易次数
        mav.addObject("sjsTotalCount1", this.tradeManageService.getHistoryTradeTotalCount1());

        // 上金所13:30-15:30交易额
        mav.addObject("sjsTotalMoney2", this.tradeManageService.getHistoryTradeTotalMoney2());
        // 上金所13:30-15:30交易次数
        mav.addObject("sjsTotalCount2", this.tradeManageService.getHistoryTradeTotalCount2());

        // 上金所20:00-02:30交易额
        mav.addObject("sjsTotalMoney3", this.tradeManageService.getHistoryTradeTotalMoney3());
        // 上金所20:00-02:30交易次数
        mav.addObject("sjsTotalCount3", this.tradeManageService.getHistoryTradeTotalCount3());

        req.setStartDate(originalStartDate);
        req.setEndDate(originalEndDate);
        mav.addObject("list", req);
        return mav;
    }

    /**
     * 用户开户统计
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "openAccount", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView openAccount(FUserListReq req) throws Exception {
        ModelAndView mav = new ModelAndView("gjs/statistics/UserOpenAccountStatisticsList");
        String e = req.getE();
        if (null == e || "".equals(e)) {
            e = "njs";
        }
        String originalStartDate = req.getStartDate();
        String originalEndDate = req.getEndDate();
        addOneDayForEndDate(req);

        // 南交所累计开户数
        FUserListReq njsReq = new FUserListReq();
        njsReq.setBankId("1");// 只要有值就会在sql中添加：AND a.bank_id IS NOT NULL
        njsReq.setExchange("NJS");
        njsReq = this.userService.queryGjsUserStatisticsListWithPage(njsReq);
        this.logger.info("南交所累计开户数：" + njsReq.getTotalCount());
        mav.addObject("njsOpenAccountTotalCount", njsReq.getTotalCount());

        // 上金所累计开户数
        FUserListReq sjsReq = new FUserListReq();
        sjsReq.setBankId("1");// 只要有值就会在sql中添加：AND a.bank_id IS NOT NULL
        sjsReq.setExchange("SJS");
        sjsReq = this.userService.queryGjsUserStatisticsListWithPage(sjsReq);
        this.logger.info("上金所累计开户数：" + sjsReq.getTotalCount());
        mav.addObject("sjsOpenAccountTotalCount", sjsReq.getTotalCount());

        // 正常查询
        req.setLimit(50);
        req.setCurrentPage(req.getCurrentPage());
        req.setOrderColumn("create_datetime");
        req.setOrderDir("DESC");
        req.setExchange(e);
        if ("2".equals(req.getStatus1()) || "3".equals(req.getStatus1()) || "4".equals(req.getStatus1())) {
            req.setStartDate(req.getStartDate().replaceAll("-", ""));
            req.setEndDate(req.getEndDate().replaceAll("-", ""));
        }
        req = this.userService.queryGjsUserOpenAccountStatisticsListWithPage(req);
        req.setE(e);
        req.setStartDate(originalStartDate);
        req.setEndDate(originalEndDate);
        mav.addObject("list", req);

        // 南交所开户数
        req.setBankId("1");// 只要有值就会在sql中添加：AND a.bank_id IS NOT NULL
        req.setExchange("NJS");
        req = this.userService.queryGjsUserStatisticsListWithPage(req);
        this.logger.info("南交所累计开户数：" + req.getTotalCount());
        mav.addObject("njsOpenAccountCount", req.getTotalCount());

        // 上金所开户数
        req.setExchange("SJS");
        req = this.userService.queryGjsUserStatisticsListWithPage(req);
        this.logger.info("上金所累计开户数：" + req.getTotalCount());
        mav.addObject("sjsOpenAccountCount", req.getTotalCount());
        return mav;
    }

    /**
     * 用户资金统计
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "money", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView money(FUserListReq req) throws Exception {
        String originalStartDate = req.getStartDate();
        String originalEndDate = req.getEndDate();
        String e = req.getE();
        if (null == e || "".equals(e)) {
            e = "njs";
        }
        ModelAndView mav = new ModelAndView("gjs/statistics/UserMoneyStatisticsList");
        req.setLimit(50);
        req.setCurrentPage(req.getCurrentPage());
        req.setExchange(e);
        if (("njs".equals(e) && null != req.getStartDate() && "" != req.getStartDate()) || "sjs".equals(e) || "4".equals(req.getStatus1())) {
            req.setStartDate(req.getStartDate().replaceAll("-", ""));
            req.setEndDate(req.getEndDate().replaceAll("-", ""));
        }
        req = this.userService.queryGjsUserMoneyStatisticsListWithPage(req);
        req.setStartDate(originalStartDate);
        req.setEndDate(originalEndDate);
        mav.addObject("list", req);

        // NJS累计入金
        mav.addObject("njsTotalA", this.tradeManageService.getHistoryTransferTotalMoneySum("njs", "A"));
        // NJS累计入金次数
        mav.addObject("njsCountA", this.tradeManageService.getHistoryTransferCount("njs", "A"));
        // NJS累计出金
        mav.addObject("njsTotalB", this.tradeManageService.getHistoryTransferTotalMoneySum("njs", "B"));
        // NJS累计出金次数
        mav.addObject("njsCountB", this.tradeManageService.getHistoryTransferCount("njs", "B"));

        // SJS累计入金
        mav.addObject("sjsTotalA", this.tradeManageService.getHistoryTransferTotalMoneySum("sjs", "A"));
        // SJS累计入金次数
        mav.addObject("sjsCountA", this.tradeManageService.getHistoryTransferCount("sjs", "A"));
        // SJS累计出金
        mav.addObject("sjsTotalB", this.tradeManageService.getHistoryTransferTotalMoneySum("sjs", "B"));
        // SJS累计出金次数
        mav.addObject("sjsCountB", this.tradeManageService.getHistoryTransferCount("sjs", "B"));

        req.setStartDate(originalStartDate);
        req.setEndDate(originalEndDate);
        return mav;
    }

    /**
     * 用户转化统计
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "transform", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView transform(FUserListReq req) throws Exception {
        String original = req.getEndDate();
        addOneDayForEndDate(req);
        String e = req.getE();
        if (null == e || "".equals(e)) {
            e = "njs";
        }
        ModelAndView mav = new ModelAndView("gjs/statistics/UserTransformStatisticsList");
        req.setLimit(50);
        req.setCurrentPage(req.getCurrentPage());
        req.setExchange(e);
        req = this.userService.queryGjsUserTransformStatisticsListWithPage(req);
        mav.addObject("list", req);

        // 转换率（根据查询日期的第一天算）
        String njsCount1 = "0.00%";
        String njsCount3 = "0.00%";
        String njsCount7 = "0.00%";
        String sjsCount1 = "0.00%";
        String sjsCount3 = "0.00%";
        String sjsCount7 = "0.00%";
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);
        if (null != req.getStartDate() && !"".equals(req.getStartDate()) && null != req.getEndDate() && !"".equals(req.getEndDate())) {
            // NJS首日转换率
            double njsOpenAccountCount1 = this.gjsAccountService.queryNJSOpenAccountCount(req.getStartDate(), req.getEndDate()); // 开户数
            double registerCount1 = this.userService.queryRegisterCount(req.getStartDate(), req.getEndDate()); // 注册用户数
            if (!Double.isNaN(njsOpenAccountCount1 / registerCount1)) {
                njsCount1 = nt.format(njsOpenAccountCount1 / registerCount1); // X日内注册的开户人数 除以 X日内注册人数
            }
            // NJS3日转换率
            double njsOpenAccountCount3 = this.gjsAccountService.queryNJSOpenAccountCount(getAfterDate(req.getStartDate(), 3), getAfterDate(req.getEndDate(), 3));
            double registerCount3 = this.userService.queryRegisterCount(getAfterDate(req.getStartDate(), 3), getAfterDate(req.getEndDate(), 3));
            if (!Double.isNaN(njsOpenAccountCount3 / registerCount3)) {
                njsCount3 = nt.format(njsOpenAccountCount3 / registerCount3);
            }
            // NJS7日转换率
            double njsOpenAccountCount7 = this.gjsAccountService.queryNJSOpenAccountCount(getAfterDate(req.getStartDate(), 7), getAfterDate(req.getEndDate(), 7));
            double registerCount7 = this.userService.queryRegisterCount(getAfterDate(req.getStartDate(), 7), getAfterDate(req.getEndDate(), 7));
            if (!Double.isNaN(njsOpenAccountCount7 / registerCount7)) {
                njsCount7 = nt.format(njsOpenAccountCount7 / registerCount7);
            }
            // SJS首日转换率
            double sjsOpenAccountCount1 = this.gjsAccountService.querySJSOpenAccountCount(req.getStartDate(), req.getEndDate()); // 开户数
            if (!Double.isNaN(sjsOpenAccountCount1 / registerCount1)) {
                sjsCount1 = nt.format(sjsOpenAccountCount1 / registerCount1);
            }
            // SJS3日转换率
            double sjsOpenAccountCount3 = this.gjsAccountService.querySJSOpenAccountCount(getAfterDate(req.getStartDate(), 3), getAfterDate(req.getEndDate(), 3));
            if (!Double.isNaN(sjsOpenAccountCount3 / registerCount3)) {
                sjsCount3 = nt.format(sjsOpenAccountCount3 / registerCount3);
            }
            // SJS7日转换率
            double sjsOpenAccountCount7 = this.gjsAccountService.querySJSOpenAccountCount(getAfterDate(req.getStartDate(), 7), getAfterDate(req.getEndDate(), 7));
            if (!Double.isNaN(sjsOpenAccountCount7 / registerCount7)) {
                sjsCount7 = nt.format(sjsOpenAccountCount7 / registerCount7);
            }
        }
        mav.addObject("njsCount1", njsCount1);
        mav.addObject("njsCount3", njsCount3);
        mav.addObject("njsCount7", njsCount7);
        mav.addObject("sjsCount1", sjsCount1);
        mav.addObject("sjsCount3", sjsCount3);
        mav.addObject("sjsCount7", sjsCount7);
        req.setEndDate(original);
        return mav;
    }

    /**
     * 将请求中的结束日期 + 1天
     *
     * @param req
     */
    private void addOneDayForEndDate(FUserListReq req) {
        if (null != req.getEndDate() && !"".equals(req.getEndDate())) {
            req.setEndDate(com.caimao.zeus.util.DateUtil.addDays(req.getEndDate(), com.caimao.zeus.util.DateUtil.DATA_TIME_PATTERN_5, 1));
        }
    }

    /**
     * 获取多少天之后的字符串日期
     *
     * @param date
     * @param day
     * @return
     */
    private String getAfterDate(String date, int day) {
        // 当前天的日期格式
        Date currentDate = DateUtil.toDate(date, DateUtil.DEFAULT_SHORT_FORMAT);
        // 多少天之后的日期格式
        Date afterDays = DateUtil.addDays(currentDate, day);
        return DateUtil.toString(afterDays, DateUtil.DEFAULT_SHORT_FORMAT);
    }

}