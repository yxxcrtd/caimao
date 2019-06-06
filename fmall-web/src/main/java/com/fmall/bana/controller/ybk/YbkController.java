package com.fmall.bana.controller.ybk;

import com.caimao.bana.api.entity.TpzBankTypeEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.req.content.FQueryBannerListReq;
import com.caimao.bana.api.entity.req.ybk.*;
import com.caimao.bana.api.entity.res.ybk.FYbkNavigationRes;
import com.caimao.bana.api.entity.session.SessionUser;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import com.caimao.bana.api.enums.EYbkExchangeStatus;
import com.caimao.bana.api.service.IUserBankCardService;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.service.content.IBannerService;
import com.caimao.bana.api.service.ybk.IYBKAccountService;
import com.caimao.bana.api.service.ybk.IYBKService;
import com.caimao.bana.api.service.ybk.IYbkDaxinService;
import com.caimao.bana.api.service.ybk.IYbkNavigationService;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 邮币卡功能的相关控制器
 * Created by Administrator on 2015/9/10.
 */
@Controller
@RequestMapping(value = "/ybk")
public class YbkController extends BaseController {

    @Value("${home_url}")
    private String homeUrl;
    @Value("${ybk_url}")
    private String ybkUrl;

    @Resource
    private IYBKService ybkService;
    @Resource
    private IYbkDaxinService ybkDaxinService;
    @Resource
    private IUserService userService;
    @Resource
    private IYBKAccountService ybkAccountService;
    @Resource
    private IYbkNavigationService ybkNavigationService;
    @Resource
    private IUserBankCardService userBankCardService;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private IBannerService bannerService;


    /**
     * 邮币卡首页的控制器
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/homenew");
        // 获取所有交易所
        FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
        req.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(req);
        mav.addObject("eList", exchangeEntityList);
        // 公告文章
        FYBKQueryArticleListReq fYbkQueryArticleListReq = new FYBKQueryArticleListReq();
        fYbkQueryArticleListReq.setIsShow(0);
        fYbkQueryArticleListReq.setLimit(6);
        fYbkQueryArticleListReq.setStart(0);
        // 打新申购
        fYbkQueryArticleListReq.setCategoryId(1);
        mav.addObject("dxsgList", this.ybkService.queryArticleWithPage(fYbkQueryArticleListReq));
        // 市场价格
        fYbkQueryArticleListReq.setCategoryId(2);
        mav.addObject("scjgList", this.ybkService.queryArticleWithPage(fYbkQueryArticleListReq));
        // 停牌公告
        fYbkQueryArticleListReq.setCategoryId(3);
        mav.addObject("tpggList", this.ybkService.queryArticleWithPage(fYbkQueryArticleListReq));
        // 邮币百科
        fYbkQueryArticleListReq.setCategoryId(5);
        mav.addObject("ybbkList", this.ybkService.queryArticleWithPage(fYbkQueryArticleListReq));
        // 获取活动
        FYBKQueryActivityListReq ybkQueryActivityListReq = new FYBKQueryActivityListReq();
        ybkQueryActivityListReq.setIsShow(0);
        ybkQueryActivityListReq.setLimit(4);
        ybkQueryActivityListReq.setStart(0);
        mav.addObject("activityList", this.ybkService.queryActivityWithPage(ybkQueryActivityListReq));
        // 獲取頁面導航
        List<FYbkNavigationRes> navigationResList = this.ybkNavigationService.selectAll();
        mav.addObject("navigationList", navigationResList);

        FQueryYbkDaxinReq daxinReq = new FQueryYbkDaxinReq();
        daxinReq.setIsShow("0");
        daxinReq.setLimit(5);
        mav.addObject("daxinList", this.ybkDaxinService.queryYbkDaxinList(daxinReq));

        // 获取邮币卡首页banner图片
        FQueryBannerListReq bannerListReq = new FQueryBannerListReq();
        bannerListReq.setIsShow(0);
        bannerListReq.setAppType("ybk");
        bannerListReq.setLimit(10);
        bannerListReq = this.bannerService.queryBannerList(bannerListReq);
        mav.addObject("bannerList", bannerListReq);

        return mav;
    }

    /**
     * 返回用户信息，如果登录的话
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/get_user_info.html", method = RequestMethod.GET)
    public Map<String, Object> getUserInfo() throws Exception {
        return this.getUserInfoMap();
    }

//    /**
//     * 邮币卡这个网站没有登录，跳转到财猫进行登录
//     *
//     * @param response
//     * @throws Exception
//     */
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public void caimaoLogin(
//            @RequestParam(value = "return", required = false) String returnUrl,
//            HttpServletResponse response,
//            HttpServletRequest request
//    ) throws Exception {
//        String baseUrl = ybkUrl;
//        String referer = returnUrl == null ? request.getHeader("Referer") : returnUrl;
//        if (referer != null) referer = URLEncoder.encode(referer.replace(baseUrl, ""), "UTF-8");
//
//        response.sendRedirect(this.homeUrl + "/user/login.html?return="+ baseUrl + referer);
//    }

    /**
     * 行情页面，所有交易所指数行情列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exchange_index.html", method = RequestMethod.GET)
    public ModelAndView exchangeIndex() throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/hq/exchangeIndex");
        // 获取所有交易所
        FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
        req.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(req);
        mav.addObject("eList", exchangeEntityList);

        return mav;
    }

    /**
     * 自选股
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/optional.html", method = RequestMethod.GET)
    public ModelAndView optional() throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/hq/optional");
        // 获取所有交易所
        FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
        req.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(req);
        mav.addObject("eList", exchangeEntityList);
        return mav;
    }

    /**
     * 交易所行情列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/market.html", method = RequestMethod.GET)
    public ModelAndView market(@RequestParam(value = "name") String exchangeShortName) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/hq/market");
        mav.addObject("shortName", exchangeShortName);
        // 获取所有交易所
        FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
        req.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(req);
        mav.addObject("eList", exchangeEntityList);
        mav.addObject("leftMenu", exchangeShortName);
        return mav;
    }

    /**
     * 个股行情列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/market_char.html", method = RequestMethod.GET)
    public ModelAndView marketChar(
            @RequestParam(value = "name") String exchangeShortName,
            @RequestParam(value = "code") String code
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/hq/marketChar");
        // 获取所有交易所
        FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
        req.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(req);
        mav.addObject("eList", exchangeEntityList);
        mav.addObject("leftMenu", exchangeShortName);
        mav.addObject("shortName", exchangeShortName);
        mav.addObject("code", code);
        return mav;
    }

    /**
     * 邮币卡开户
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/open_account2.html", method = RequestMethod.GET)
    public ModelAndView openAccount2(HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/openAccount");
        //是否登录
        SessionUser user = (SessionUser) sessionProvider.getAttribute("user");

        if (user != null) {
            // 用户已经登陆，查询用户的信息
            TpzUserEntity userEntity = this.userService.getById(user.getUser_id());
            // 生成一个token，存到redis中，并返回
            String token = this.getUserToken();
            mav.addObject("token", token);
            mav.addObject("mobile", userEntity.getMobile());
            mav.addObject("name", userEntity.getUserRealName());
            mav.addObject("idcard", userEntity.getIdcard());
        }
        // 看看是否有邀请的
        String u = (String) request.getSession().getAttribute("u");
        if (u != null && !Objects.equals(u, "")) {
            TpzUserEntity refUser = this.userService.getById(Long.valueOf(u));
            if (refUser != null) {
                mav.addObject("refCaimaoId", refUser.getCaimaoId());
            }
        }
        return mav;
    }

    /**
     * 邮币卡开户，新用户的
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/open_account.html", method = RequestMethod.GET)
    public ModelAndView openAccountNew(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/openAccountNew");
//        // 获取一个token
//        String token = this.getUserToken();
//        mav.addObject("token", token);
        // 获取用户的开户列表
        //是否登录
        SessionUser user = (SessionUser) sessionProvider.getAttribute("user");
        if (user != null) {
            FYbkApiQueryAccountListReq accountListReq = new FYbkApiQueryAccountListReq();
            accountListReq.setUserId(user.getUser_id());
            accountListReq = this.ybkAccountService.queryApiAccountApply(accountListReq);
            if (accountListReq.getItems() != null && accountListReq.getItems().size() > 0) {
                // 跳转到 old 页面
                response.sendRedirect("/ybk/open_account_old.html");
                return null;
            }
        }

        // 获取交易所列表
        FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
        req.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(req);
        List<Map<String, Object>> exchangeList = new ArrayList<>();
        // 获取银行列表
        List<TpzBankTypeEntity> bankTypeEntities = this.userBankCardService.queryBankList(3L);
        Map<String, String> bankMap = new HashMap<>();
        for (TpzBankTypeEntity bankTypeEntity : bankTypeEntities) {
            bankMap.put(bankTypeEntity.getBankNo(), bankTypeEntity.getBankName());
        }
        for (YbkExchangeEntity entity : exchangeEntityList) {
            if (entity.getNumber() == null || Objects.equals(entity.getNumber(), "")) continue;
            Map<String, Object> tmpMap = new HashMap<>();
            tmpMap.put("name", entity.getName());
            tmpMap.put("openProcessTimes", entity.getOpenProcessTimes());
            tmpMap.put("comeInTimes", entity.getComeInTimes());
            tmpMap.put("bankBindTimes", entity.getBankBindTimes());
            tmpMap.put("tradeTimes", entity.getTradeTimes());
            tmpMap.put("tradeFee", entity.getTradeFee());
            // 支持的银行
            String supportBank = "";
            for (String bank : entity.getSupportBank().split(",")) {
                supportBank += bankMap.get(bank) + ",";
            }
            tmpMap.put("supportBank", supportBank.substring(0, supportBank.length() - 1));
            // 活动
            Map<String, String> activityMap = new HashMap<>();
            if (entity.getActivityList() != null && entity.getActivityList().size() >= 1) {
                activityMap.put("url", entity.getActivityList().get(0).getActivityUrl());
                activityMap.put("name", entity.getActivityList().get(0).getActivityName());
            }
            tmpMap.put("activity", activityMap);
            exchangeList.add(tmpMap);
        }
        mav.addObject("exchangeList", exchangeList);

        return mav;
    }


    /**
     * 邮币卡开户，老用户的
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/open_account_old.html", method = RequestMethod.GET)
    public ModelAndView openAccountOld(HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/openAccountOld");
        // 获取一个token
        String token = this.getUserToken();
        mav.addObject("token", token);
        return mav;
    }

    /**
     * 邮币卡开户
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/open_account_step.html", method = RequestMethod.GET)
    public ModelAndView openAccountStep(HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/openAccountStep");
        // 获取一个token
        String token = this.getUserToken();
        mav.addObject("token", token);
        return mav;
    }


    /**
     * 个人中心
     *
     * @return 返回
     * @throws Exception
     */
    @RequestMapping(value = "/ucenter.html", method = RequestMethod.GET)
    public ModelAndView ucenter() throws Exception {
        Long userId = this.getSessionUser().getUser_id();

        ModelAndView mav = new ModelAndView("/ybk/account/ucenter");
        // 获取我所有的申请
        FYbkApiQueryAccountListReq req = new FYbkApiQueryAccountListReq();
        req.setUserId(userId);
        req = this.ybkAccountService.queryApiAccountApply(req);

        mav.addObject("list", req);

        SessionUser user = this.getSessionUser();
        mav.addObject("user", user);

        // 获取各个交易所的简称
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(new FYbkExchangeQueryListReq());
        Map<String, Object> exchangeShortName = new HashMap<>();
        for (YbkExchangeEntity entity : exchangeEntityList) {
            exchangeShortName.put(entity.getName(), entity.getShortName());
        }
        mav.addObject("shortNames", exchangeShortName);


        return mav;
    }

//    /**
//     * 个人资料
//     * @return 返回
//     * @throws Exception
//     */
//    @RequestMapping(value = "/user_info.html", method = RequestMethod.GET)
//    public ModelAndView userInfo() throws Exception {
////        SessionUser sessionUser = getSessionUser();
////        if(sessionUser==null){// 没有session的需要转向登录页面
////            return new ModelAndView("/ybk/account/ucenter");
////        }
//        long userId = 802184463646721L;
//        TpzUserEntity userEntity = this.userService.getById(userId);
//
//        ModelAndView mav = new ModelAndView("/ybk/account/userInfo");
//        mav.addObject("user", userEntity);
//
//        return mav;
//    }
//
//    /**
//     * 修改用户资料
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "/user_info.html", method = RequestMethod.POST)
//    public ModelAndView saveUserInfo(
//            @RequestParam(value = "userName") String userName,
//            @RequestParam(value = "cardNo") String cardNo,
//            @RequestParam(value = "phoneNo") String phoneNo) throws Exception {
////        SessionUser sessionUser = getSessionUser();
////        if(sessionUser==null){// 没有session的需要转向登录页面
////            return new ModelAndView("/ybk/account/ucenter");
////        }
//        long userId = 802184463646721L;
//
//        return userInfo();
//    }

    /**
     * 通用页面控制器
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/page/{view}.html", method = RequestMethod.GET)
    public ModelAndView page(@PathVariable("view") String view) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/page/" + view);
        mav.addObject("token", this.getUserToken());
        return mav;
    }
}
