package com.fmall.bana.controller.gjs;

import com.caimao.bana.api.entity.req.content.FQueryBannerListReq;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.service.content.IBannerService;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 贵金属功能的相关控制器
 * Created by Administrator on 2015/9/10.
 */
@Controller
@RequestMapping(value = {"/gjs", "/jin"})
public class GjsController extends BaseController {
    @Value("${caimao_url}")
    private String caimaoUrl;
    @Value("${gjs_url}")
    private String gjsUrl;

    @Resource
    private IUserService userService;
    @Resource
    private IBannerService bannerService;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 首页
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/home");
        // 获取贵金属首页banner图片
        FQueryBannerListReq bannerListReq = new FQueryBannerListReq();
        bannerListReq.setIsShow(0);
        bannerListReq.setAppType("gjs");
        bannerListReq.setLimit(10);
        bannerListReq = this.bannerService.queryBannerList(bannerListReq);
        mav.addObject("bannerList", bannerListReq);

        mav.addObject("token", this.getUserToken());
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
//     * 贵金属这个网站没有登录，跳转到财猫进行登录
//     * @param response
//     * @throws Exception
//     */
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public void caimaoLogin(
//            @RequestParam(value = "return", required = false) String returnUrl,
//            HttpServletResponse response,
//            HttpServletRequest request
//    ) throws Exception {
//        String baseUrl = gjsUrl;
//        String referer = returnUrl == null ? request.getHeader("Referer") : returnUrl;
//        if (referer != null) referer = URLEncoder.encode(referer.replace(baseUrl, ""), "UTF-8");
//
//        response.sendRedirect(this.caimaoUrl + "/user/login.html?app=jin&return="+ referer);
//    }


    /**
     * 自选股
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/optional.html", method = RequestMethod.GET)
    public ModelAndView optional() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/market/optional");
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 行情
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/market.html", method = RequestMethod.GET)
    public ModelAndView market() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/market/market");
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 图表
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/chart.html", method = RequestMethod.GET)
    public ModelAndView chart() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/market/chart");
        mav.addObject("token", this.getUserToken());

        mav.addObject("njsHours", this.redisUtils.hGet("_gjsExchangeTradeTime", "njsHours"));
        mav.addObject("sjsHours", this.redisUtils.hGet("_gjsExchangeTradeTime", "sjsHours"));
        return mav;
    }

    /**
     * 开户
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/openAccount.html", method = RequestMethod.GET)
    public ModelAndView openAccount() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/account/openAccount");
        mav.addObject("userInfo", this.getUserInfoMap());
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 身份证上传
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/cardUpload.html", method = RequestMethod.GET)
    public ModelAndView cardUpload() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/account/cardUpload");
        mav.addObject("userInfo", this.getUserInfoMap());
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 通用页面控制器
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/page/{view}.html", method = RequestMethod.GET)
    public ModelAndView page(@PathVariable("view") String view) throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/page/" + view);
        mav.addObject("userInfo", this.getUserInfoMap());
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 贵金属账户页面
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/account.html", method = RequestMethod.GET)
    public ModelAndView account() throws Exception {
        ModelAndView mav = new ModelAndView("/home/user/gjsaccount");
        mav.addObject("userInfo", this.getUserInfoMap());
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 贵金属账户页面
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/account/resetPwd.html", method = RequestMethod.GET)
    public ModelAndView accountResetPwd() throws Exception {
        ModelAndView mav = new ModelAndView("/home/user/resetPassword");
        mav.addObject("userInfo", this.getUserInfoMap());
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 贵金属账户页面
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/account/modifyPwd.html", method = RequestMethod.GET)
    public ModelAndView accountModifyPwd() throws Exception {
        ModelAndView mav = new ModelAndView("/home/user/setgjspassword");
        mav.addObject("userInfo", this.getUserInfoMap());
        mav.addObject("token", this.getUserToken());
        return mav;
    }
}
