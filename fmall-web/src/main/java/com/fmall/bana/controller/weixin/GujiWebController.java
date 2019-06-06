package com.fmall.bana.controller.weixin;

import com.caimao.bana.api.entity.guji.GujiFocusRecordEntity;
import com.caimao.bana.api.entity.guji.GujiShareRecordEntity;
import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.entity.guji.GujiUserStockEntity;
import com.caimao.bana.api.entity.req.guji.FQueryGujiFollowShareListReq;
import com.caimao.bana.api.entity.req.guji.FQueryGujiHallShareListReq;
import com.caimao.bana.api.enums.guji.EGujiStockType;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.weixin.WXBaseService;
import com.fmall.bana.utils.weixin.entity.TickerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 股计中的各个H5页面
 * Created by Administrator on 2016/1/6.
 */
@Controller
@RequestMapping(value = "/weixin/guji")
public class GujiWebController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(GujiWebController.class);

    /**
     * 分析师的主页
     *
     * 进入的时候
     *  如果有传 wxId，使用base的授权作用域，静默方式进入
     *  如果没有传 wxId，必须使用 userInfo 的授权作用域，需要授权并获取用户的基本信息
     * @return
     */
    @RequestMapping(value = "analystIndex.html")
    public ModelAndView analystIndex(
            @RequestParam(value = "wxId", required = false) Long wxId,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "source", required = false) String source
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/weixin/guji/analystIndex");
        String openId = null;

        Long _startTime1 = System.currentTimeMillis();
        /**
         * 这一段是必须的，流程是这样的，且听我慢慢道来
         * 根据你这个页面的特性，如果不需要强的获取当前访问用户的信息，使用 scopeBase 静默获取用户授权，如果需要用到用户信息，使用 scopeUser 获取用户授权，需要用户确认（如无关注）
         * 1. 如果有传递微信授权code，进行登录（获取当前用户的accessToken、openId），并记录到session中，避免重复授权登陆
         * 2. 从session中获取用户accessToken和openId，如果没有，返回null，并跳转到授权的链接页面
         * * 如果authInfo返回null，代表当前没有授权，没有授权，那就返回null，让页面进行跳转
         */
        Map<String, String> authInfo = null;
        if (code != null) this.saveAccessTokenAndOpenIdByCode(code, WXBaseService.scopeBase);
        authInfo = this.verificationAccessToken(WXBaseService.scopeBase);
        mav.addObject("authInfo", authInfo);
        if (authInfo == null) return null;  // 获取null，则返回null进行跳转授权页面
        openId = authInfo.get("openId");

        // 查询看的这个人是否关注了股计服务号
        Integer isFollowGuji = 0;
        GujiUserEntity checkGujiUserEntity = this.gujiService.getUserByOpenId(authInfo.get("openId"));
        if (checkGujiUserEntity != null && checkGujiUserEntity.getSubscribe().equals(1)) {
            isFollowGuji = 1;
            // 当前登录的人
            mav.addObject("loginOpenId", checkGujiUserEntity.getOpenId());
        }
        mav.addObject("isFollowGuji", isFollowGuji);

        Long _startTime2 = System.currentTimeMillis();

        // 获取JS SDK 需要的配置
        mav.addObject("jsApiTicket", this.wxBaseService.getJsApiTicket(this.getNowUrl()));
        /** 必须的结束 */

        // 根据获取的Web授权，获取用户信息，如果没有注册，则进行注册到数据库
//        Map<String, String> wxUserInfo = this.wxBaseService.getWebUserInfoByOpenId(authInfo.get("accessToken"), authInfo.get("openId"));
//        this.wxBaseService.registerGujiUser(wxUserInfo);

        Long _startTime3 = System.currentTimeMillis();

        // 获取分析师的个人信息
        GujiUserEntity gujiUserEntity = null;
        if (wxId == null) {
            gujiUserEntity = this.gujiService.getUserByOpenId(openId);
        } else {
            gujiUserEntity = this.gujiService.getUserById(wxId);
        }

        // 获取分析师的粉丝
        if (null != gujiUserEntity) {
            List<GujiFocusRecordEntity> focusMes = gujiService.focusMe(gujiUserEntity.getOpenId());
            logger.info("当前分析师的粉丝数：{}", focusMes.size());
            mav.addObject("focusMe", focusMes.size());
        }

        Long _startTime4 = System.currentTimeMillis();

        // 更新用户数据库中的微信用户基本信息
        this.wxBaseService.updateWeixinUserInfo(gujiUserEntity);

        // 获取分析师的持仓股票列表
        List<GujiUserStockEntity> userStockEntityList = this.gujiService.getMyStockList(gujiUserEntity.getWxId());

        // 获取是否关注
        Boolean isFollow = this.gujiService.checkIsFollow(openId, gujiUserEntity.getWxId());

        Long _startTime5 = System.currentTimeMillis();

        /**
         * 微信分享的信息
         */
        String shareDesc = "";
        TickerEntity tickerEntity = null;
        for (GujiUserStockEntity stockEntity: userStockEntityList) {
            if (stockEntity.getStockType().equals(EGujiStockType.GP.getCode())) {
                shareDesc += "、" + stockEntity.getStockName();
            }
            try {
                tickerEntity = this.stockHQService.getTicket(stockEntity.getStockCode());
                if (null != tickerEntity) {
                    stockEntity.setCurrentPrice(tickerEntity.getCurPrice());

                    float inc = (tickerEntity.getCurPrice() - Float.parseFloat(tickerEntity.getClosePrice())) / Float.parseFloat(tickerEntity.getClosePrice()) * 100;
                    stockEntity.setIncrease(inc);
                    logger.info("当前股票：{}，现价：{}，时价：{}，涨幅：{}", tickerEntity.getCode(), tickerEntity.getCurPrice(), tickerEntity.getClosePrice(), inc);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        shareDesc = !shareDesc.equals("") ? shareDesc.substring(1, shareDesc.length()) : "股计，精英分析师社群";
        Map<String, String> shareInfo = new HashMap<>();
        shareInfo.put("title", gujiUserEntity.getNickname() + "的个人荐股主页");
        shareInfo.put("desc", "当前推荐：" + shareDesc);
        shareInfo.put("link", this.wxBaseService.getDomain()+"/weixin/guji/analystIndex.html?wxId="+gujiUserEntity.getWxId());
        shareInfo.put("imgUrl", gujiUserEntity.getHeadimgurl());
        shareInfo.put("type", "link");

        Long _startTime6 = System.currentTimeMillis();

        mav.addObject("shareInfo", shareInfo);
        mav.addObject("userInfo", gujiUserEntity);
        mav.addObject("stockList", userStockEntityList);
        mav.addObject("isFollow", isFollow);

        logger.info("分析师主页时间点：总{}，第一个点{}, 第二个点{}，第三个点{}，第四个点{}，第五个点{}",
                _startTime6 - _startTime1,
                _startTime2 - _startTime1,
                _startTime3 - _startTime2,
                _startTime4 - _startTime3,
                _startTime5 - _startTime4,
                _startTime6 - _startTime5);

        return mav;
    }

    /**
     * 推荐个股的页面
     * @param wxId  微信用户ID
     * @param stockCode 股票代码
     * @param code  微信授权代码
     * @return
     */
    @RequestMapping(value = "/recommendStock.html")
    public ModelAndView RecommendStock(
            @RequestParam(value = "wxId") Long wxId,
            @RequestParam(value = "stockCode") String stockCode,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "source", required = false) String source
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/weixin/guji/recommendStock");
        /**
         * 这一段是必须的，流程是这样的，且听我慢慢道来
         * 根据你这个页面的特性，如果不需要强的获取当前访问用户的信息，使用 scopeBase 静默获取用户授权，如果需要用到用户信息，使用 scopeUser 获取用户授权，需要用户确认（如无关注）
         * 1. 如果有传递微信授权code，进行登录（获取当前用户的accessToken、openId），并记录到session中，避免重复授权登陆
         * 2. 从session中获取用户accessToken和openId，如果没有，返回null，并跳转到授权的链接页面
         * * 如果authInfo返回null，代表当前没有授权，没有授权，那就返回null，让页面进行跳转
         */
        Map<String, String> authInfo = null;
        if (code != null) this.saveAccessTokenAndOpenIdByCode(code, WXBaseService.scopeBase);
        authInfo = this.verificationAccessToken(WXBaseService.scopeBase);
        if (authInfo == null) return null;
        mav.addObject("authInfo", authInfo);
        // 获取JS SDK 需要的配置
        mav.addObject("jsApiTicket", this.wxBaseService.getJsApiTicket(this.getNowUrl()));
        /** 这段必须的结束了 */

        // 查询看的这个人是否关注了股计服务号
        Integer isFollowGuji = 0;
        GujiUserEntity checkGujiUserEntity = this.gujiService.getUserByOpenId(authInfo.get("openId"));
        if (checkGujiUserEntity != null && checkGujiUserEntity.getSubscribe().equals(1)) {
            isFollowGuji = 1;
        }
        mav.addObject("isFollowGuji", isFollowGuji);

        /**
         * 剩下的这些写业务逻辑了
         */
        // 获取推荐股票这个人的信息，需要从数据库中先获取这个人的信息
        GujiUserEntity gujiUserEntity = this.gujiService.getUserById(wxId);
        // 更新用户数据库中的微信用户基本信息
        this.wxBaseService.updateWeixinUserInfo(gujiUserEntity);
        List<GujiShareRecordEntity> shareRecordEntityList = this.gujiService.queryStockShareHistoryList(wxId, stockCode, authInfo.get("openId"));
        // 获取是否关注
        Boolean isFollow = this.gujiService.checkIsFollow(authInfo.get("openId"), gujiUserEntity.getWxId());

        Map<String, String> stockInfo = new HashMap<>();
        stockInfo.put("name", shareRecordEntityList.get(0).getStockName());
        stockInfo.put("code", shareRecordEntityList.get(0).getStockCode());
        stockInfo.put("targetPrice", shareRecordEntityList.get(0).getTargetPrice());
        stockInfo.put("stockType", shareRecordEntityList.get(0).getStockType());

        TickerEntity ticker = null;
        try {
            // 获取当前股票信息
            ticker = this.stockHQService.getTicket(shareRecordEntityList.get(0).getStockCode());
            ticker.setIncrease((ticker.getCurPrice() - Float.parseFloat(ticker.getClosePrice())) / Float.parseFloat(ticker.getClosePrice()) * 100);
            mav.addObject("ticker", ticker);

            float inc = (ticker.getCurPrice() - Float.parseFloat(shareRecordEntityList.get(0).getStockPrice())) / Float.parseFloat(shareRecordEntityList.get(0).getStockPrice()) * 100;
            mav.addObject("inc", inc);
            logger.info("当前股票：{}，现价：{}，时价：{}，涨幅：{}", stockCode, ticker.getCurPrice(), shareRecordEntityList.get(0).getStockPrice(), inc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取分析师的持仓股票列表
//        List<GujiUserStockEntity> userStockEntityList = this.gujiService.getMyStockList(gujiUserEntity.getWxId());
//        for (GujiUserStockEntity userStockEntity : userStockEntityList) {
//            if (stockCode.equals(userStockEntity.getStockCode())) {
//                float inc = (ticker.getCurPrice() - Float.parseFloat(userStockEntity.getStockPrice())) / Float.parseFloat(userStockEntity.getStockPrice()) * 100;
//                mav.addObject("inc", inc);
//                logger.error("当前股票：{}，现价：{}，时价：{}，涨幅：{}", stockCode, ticker.getCurPrice(), userStockEntity.getStockPrice(), inc);
//            }
//        }

        //微信分享的信息
        String reason = shareRecordEntityList.get(0).getReason();
        String title = String.format("%s操作了%s(%s)", gujiUserEntity.getNickname(), stockInfo.get("name"), stockInfo.get("code"));
        if (stockInfo.get("stockType").equals("DP")) {
            title = gujiUserEntity.getNickname()+"点评了大盘";
        }
        Map<String, String> shareInfo = new HashMap<>();
        shareInfo.put("title", title);
        shareInfo.put("desc", reason.length() > 25 ? reason.substring(0, 25)+"..." : reason);
        shareInfo.put("link", this.wxBaseService.getDomain()+"/weixin/guji/recommendStock.html?wxId="+gujiUserEntity.getWxId()+"&stockCode="+stockInfo.get("code"));
        shareInfo.put("imgUrl", gujiUserEntity.getHeadimgurl());
        shareInfo.put("type", "link");

        mav.addObject("userInfo", gujiUserEntity);
        mav.addObject("stockInfo", stockInfo);
        mav.addObject("shareList", shareRecordEntityList);
        mav.addObject("isFollow", isFollow);
        mav.addObject("shareInfo", shareInfo);

        return mav;
    }

    /**
     * 公开的大厅主页
     * @return
     */
    @RequestMapping(value = "/hallIndex.html")
    public ModelAndView hallIndex(
            @RequestParam(value = "code", required = false) String code
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/weixin/guji/hallIndex");

        /**
         * 这一段是必须的，流程是这样的，且听我慢慢道来
         * 根据你这个页面的特性，如果不需要强的获取当前访问用户的信息，使用 scopeBase 静默获取用户授权，如果需要用到用户信息，使用 scopeUser 获取用户授权，需要用户确认（如无关注）
         * 1. 如果有传递微信授权code，进行登录（获取当前用户的accessToken、openId），并记录到session中，避免重复授权登陆
         * 2. 从session中获取用户accessToken和openId，如果没有，返回null，并跳转到授权的链接页面
         * * 如果authInfo返回null，代表当前没有授权，没有授权，那就返回null，让页面进行跳转
         */
        Map<String, String> authInfo = null;
        if (code != null) this.saveAccessTokenAndOpenIdByCode(code, WXBaseService.scopeBase);
        authInfo = this.verificationAccessToken(WXBaseService.scopeBase);
        if (authInfo == null) return null;
        mav.addObject("authInfo", authInfo);
        // 获取JS SDK 需要的配置
        mav.addObject("jsApiTicket", this.wxBaseService.getJsApiTicket(this.getNowUrl()));
        /** 这段必须的结束了 */

        /**
         * 这以下去写业务逻辑
         */
        FQueryGujiHallShareListReq hallShareListReq = new FQueryGujiHallShareListReq();
        hallShareListReq.setCheckOpenId(authInfo.get("openId"));
        hallShareListReq.setLimit(5);
        hallShareListReq.setStart(0);
        hallShareListReq = this.gujiService.queryHallShareList(hallShareListReq);

        mav.addObject("hallReq", hallShareListReq);

        /**
         * 微信分享的信息
         */
        Map<String, String> shareInfo = new HashMap<>();
        shareInfo.put("title", "欢迎来到股计");
        shareInfo.put("desc", "关注股计服务号，查看靠谱的荐股");
        shareInfo.put("link", this.wxBaseService.getDomain()+"/weixin/guji/hallIndex.html");
        shareInfo.put("imgUrl", this.wxBaseService.getDomain()+"/static/weixin/img/logo.png");
        shareInfo.put("type", "link");
        mav.addObject("shareInfo", shareInfo);

        return mav;
    }

    /**
     * 订阅的大厅主页
     * @return
     */
    @RequestMapping(value = "/subscribeHallIndex.html")
    public ModelAndView subscribeHallIndex(
            @RequestParam(value = "code", required = false) String code
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/weixin/guji/subscribeHallIndex");

        Long _startTime1 = System.currentTimeMillis();

        /**
         * 这一段是必须的，流程是这样的，且听我慢慢道来
         * 根据你这个页面的特性，如果不需要强的获取当前访问用户的信息，使用 scopeBase 静默获取用户授权，如果需要用到用户信息，使用 scopeUser 获取用户授权，需要用户确认（如无关注）
         * 1. 如果有传递微信授权code，进行登录（获取当前用户的accessToken、openId），并记录到session中，避免重复授权登陆
         * 2. 从session中获取用户accessToken和openId，如果没有，返回null，并跳转到授权的链接页面
         * * 如果authInfo返回null，代表当前没有授权，没有授权，那就返回null，让页面进行跳转
         */
        Map<String, String> authInfo = null;
        if (code != null) this.saveAccessTokenAndOpenIdByCode(code, WXBaseService.scopeUser);
        authInfo = this.verificationAccessToken(WXBaseService.scopeUser);
        if (authInfo == null) return null;
        mav.addObject("authInfo", authInfo);
        // 获取JS SDK 需要的配置
        mav.addObject("jsApiTicket", this.wxBaseService.getJsApiTicket(this.getNowUrl()));
        /** 这段必须的结束了 */

        Long _startTime2 = System.currentTimeMillis();

        // 根据获取的Web授权，获取用户信息，如果没有注册，则进行注册到数据库
        Map<String, String> wxUserInfo = this.wxBaseService.getWebUserInfoByOpenId(authInfo.get("accessToken"), authInfo.get("openId"));
        this.wxBaseService.registerGujiUser(wxUserInfo);

        GujiUserEntity gujiUserEntity = this.gujiService.getUserByOpenId(authInfo.get("openId"));

        Long _startTime3 = System.currentTimeMillis();

        FQueryGujiFollowShareListReq followShareListReq = new FQueryGujiFollowShareListReq();

        if (gujiUserEntity != null) {
            followShareListReq.setWxId(gujiUserEntity.getWxId());
            followShareListReq.setOpenId(gujiUserEntity.getOpenId());
            followShareListReq.setLimit(20);
            followShareListReq.setStart(0);
            followShareListReq = this.gujiService.queryFollowShareList(followShareListReq);
        }

        Long _startTime4 = System.currentTimeMillis();

        mav.addObject("followReq", followShareListReq);
        mav.addObject("gujiUserInfo", gujiUserEntity);

        /**
         * 微信分享的信息
         */
        Map<String, String> shareInfo = new HashMap<>();
        shareInfo.put("title", "欢迎来到股计");
        shareInfo.put("desc", "关注股计服务号，查看靠谱的荐股");
        shareInfo.put("link", this.wxBaseService.getDomain()+"/weixin/guji/subscribeHallIndex.html");
        shareInfo.put("imgUrl", this.wxBaseService.getDomain()+"/static/weixin/img/logo.png");
        shareInfo.put("type", "link");
        mav.addObject("shareInfo", shareInfo);

        logger.error("关注页面时间点，总{}，第一个点{}， 第二个点{}，第三个点{}",
                _startTime4 - _startTime1,
                _startTime2 - _startTime1,
                _startTime3 - _startTime2,
                _startTime4 - _startTime3);

        return mav;
    }

    /** ajax分页查询加载页面 */

    // 分页加载大厅的东西
    @ResponseBody
    @RequestMapping(value = "/ajax_hall_list.html")
    public FQueryGujiHallShareListReq ajaxQueryHallList(
            @RequestParam(value = "checkOpenId", required = false) String checkOpenId,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit
    ) throws Exception {
        FQueryGujiHallShareListReq req = new FQueryGujiHallShareListReq();
        req.setCheckOpenId(checkOpenId);
        req.setStart((page - 1) * limit);
        req.setLimit(limit);
        return this.gujiService.queryHallShareList(req);
    }

    // 分页加载我的订阅的东西
    @ResponseBody
    @RequestMapping(value = "/ajax_subscribe_list.html")
    public FQueryGujiFollowShareListReq ajaxQueryFollowList(
            @RequestParam(value = "wxId") Long wxId,
            @RequestParam(value = "openId") String openId,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit
    ) throws Exception {
        FQueryGujiFollowShareListReq req = new FQueryGujiFollowShareListReq();
        req.setWxId(wxId);
        req.setOpenId(openId);
        req.setStart((page - 1) * limit);
        req.setLimit(limit);
        return this.gujiService.queryFollowShareList(req);
    }

    // openId 点心
    @ResponseBody
    @RequestMapping(value = "/ajax_like.html")
    public Boolean addLike(
            @RequestParam(value = "openId") String openId,
            @RequestParam(value = "shareId") Long shareId
    ) throws Exception {
        try {
            this.gujiService.giveLike(openId, shareId);
        } catch (Exception ignored) {}
        return true;
    }

    // 添加/取消 关注
    @ResponseBody
    @RequestMapping(value = "/ajax_follow.html")
    public Boolean follow(
            @RequestParam(value = "openId") String openId,
            @RequestParam(value = "focusWxId") Long focusWxId,
            @RequestParam(value = "opt") String opt) {
        try {
            if (opt.equals("add")) {
                this.gujiService.addFollow(openId, focusWxId);
            } else if (opt.equals("del")) {
                this.gujiService.delFollow(openId, focusWxId);
            }
        } catch (Exception ignored) {}
        return true;
    }

}
