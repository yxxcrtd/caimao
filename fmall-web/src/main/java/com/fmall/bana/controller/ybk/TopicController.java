package com.fmall.bana.controller.ybk;

import com.fmall.bana.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 邮币卡专题页面的东东
 * Created by Administrator on 2015/10/12.
 */
@Controller
@RequestMapping(value = "/ybk/topic/")
public class TopicController extends BaseController {

    /**
     * 新手引导页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/lead.html")
    public ModelAndView lead() throws Exception {
        return new ModelAndView("/ybk/topic/lead");
    }

    @RequestMapping(value = "/lead2.html")
    public ModelAndView lead2() throws Exception {
        return new ModelAndView("/ybk/topic/lead2");
    }


    @RequestMapping(value = "/test.html")
    public ModelAndView test() throws Exception {
        return new ModelAndView("/ybk/topic/test");
    }

    /**
     * 开户送礼活动页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/open_gift.html")
    public ModelAndView openGift() throws Exception {
        return new ModelAndView("/ybk/topic/open_gift");
    }

    /**
     * 邮币卡APP下载页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/download.html")
    public ModelAndView download(@RequestParam(value = "channelId", required = false) Integer channelId) throws Exception {
        if (channelId == null || channelId < 1 || channelId > 10) {
            channelId = null;
        }
        return new ModelAndView("/ybk/topic/download", "channelId", channelId);
    }

    @RequestMapping(value = "/topic20151028.html")
    public ModelAndView topic20151028(@RequestParam(value = "channelId", required = false) Integer channelId) throws Exception {
        if (channelId == null || channelId < 1 || channelId > 10) {
            channelId = null;
        }
        return new ModelAndView("/ybk/topic/topic20151028", "channelId", channelId);
    }
}
