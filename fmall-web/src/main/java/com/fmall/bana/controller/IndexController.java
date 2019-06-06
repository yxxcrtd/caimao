package com.fmall.bana.controller;


import com.caimao.bana.api.entity.req.content.FQueryBannerListReq;
import com.caimao.bana.api.entity.session.SessionUser;
import com.caimao.bana.api.service.content.IBannerService;
import com.fmall.bana.utils.crypto.RSA;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 首页的控制器
 * Created by WangXu on 2015/5/25.
 */
@Controller
@RequestMapping(value = "")
public class IndexController extends BaseController {

    @Resource
    private IBannerService bannerService;

    @RequestMapping(value = "")
    public ModelAndView home() throws Exception {
        ModelAndView mav = new ModelAndView("/home/home");
        // 获取主站首页banner图片
        FQueryBannerListReq bannerListReq = new FQueryBannerListReq();
        bannerListReq.setIsShow(0);
        bannerListReq.setAppType("home");
        bannerListReq.setLimit(10);
        bannerListReq = this.bannerService.queryBannerList(bannerListReq);
        mav.addObject("bannerList", bannerListReq);

        //是否登录
        try {
            SessionUser user = (SessionUser) sessionProvider.getAttribute("user");
            if (user != null) {
                mav.addObject("loginUserInfo", this.getUserInfoMap());
            }
        } catch (Exception ignored) {}

        return mav;
    }


    /**
     * 外部行情内容转发
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/eastmoney_hq")
    public String quoteGlobalHQ(HttpServletResponse response) throws IOException {
        HttpClient httpClient = new HttpClient();
        HttpMethod method = new GetMethod("http://quote.eastmoney.com/hq2data/gb/data/gb_globalindex.js");
        method.setRequestHeader("accept", "text/html,application/xhtml+xml,application/xml");
        httpClient.executeMethod(method);
        String res = method.getResponseBodyAsString();

        response.getWriter().print(new String(res.getBytes("ISO-8859-1"), "UTF-8"));
        response.getWriter().flush();
        return null;
    }

    /**
     * 前端使用临时模板
     * @param tpl
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/tpl")
    public ModelAndView tpl(@RequestParam(value = "tpl", defaultValue = "/tpl/default") String tpl) throws Exception {
        return new ModelAndView(tpl);
    }

    /**
     * RSA加密测试
     * @param mi
     * @return
     */
    @RequestMapping(value = "/_rsa_test")
    public ModelAndView rsaTest(
            @RequestParam(value = "mi", required = false) String mi
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/home/rsaTest");
        mav.addObject("mi", mi);
        if (mi != null) {
            mav.addObject("target", RSA.decodeByPwd(mi));
        }
        return mav;
    }

}
