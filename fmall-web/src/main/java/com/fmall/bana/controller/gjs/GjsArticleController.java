package com.fmall.bana.controller.gjs;

import com.fmall.bana.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 贵金属功能的相关控制器
 * Created by Administrator on 2015/9/10.
 */
@Controller
@RequestMapping(value = {"/gjs/article", "/jin/article"})
public class GjsArticleController extends BaseController {
    /**
     * 新闻列表
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public ModelAndView articleList() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/article/list");
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 新闻详情
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/detail.html", method = RequestMethod.GET)
    public ModelAndView detail() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/article/detail");
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 帮助
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/help.html", method = RequestMethod.GET)
    public ModelAndView help() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/article/help");
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * m站帮助
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/m_help.html", method = RequestMethod.GET)
    public ModelAndView mHelp() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/article/m_help");
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 帮助内容
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/data.html", method = RequestMethod.GET)
    public ModelAndView helpData() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/article/data");
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 关于我们
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/about.html", method = RequestMethod.GET)
    public ModelAndView about() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/article/about");
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 法律风险
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/protocol_gjs.html", method = RequestMethod.GET)
    public ModelAndView protocolGjs() throws Exception {
        ModelAndView mav = new ModelAndView("/protocol/gjs");
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 附加内容
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/contentExt.html", method = RequestMethod.GET)
    public ModelAndView contentExt() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/article/contentExt");
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 帮助数据
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/dataExt.html", method = RequestMethod.GET)
    public ModelAndView dataExt() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/article/dataExt");
        mav.addObject("token", this.getUserToken());
        return mav;
    }

    /**
     * 实时快讯列表
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/flash.html", method = RequestMethod.GET)
    public ModelAndView flashList() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/article/flash");
        mav.addObject("token", this.getUserToken());
        return mav;
    }
}
