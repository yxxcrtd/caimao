package com.fmall.bana.controller.gjs;

import com.fmall.bana.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 贵金属app html开户出入金
 * Created by Administrator on 2015/9/10.
 */
@Controller
@RequestMapping(value = "/gjs/appHtml")
public class GjsAppController extends BaseController {

    /**
     * 开户 银行
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/openAccount/bank.html", method = RequestMethod.GET)
    public ModelAndView openAccountBank() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/app/openAccount/bank");
        return mav;
    }

    /**
     * 开户 完成
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/openAccount/complete.html", method = RequestMethod.GET)
    public ModelAndView openAccountComlete() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/app/openAccount/complete");
        return mav;
    }

    /**
     * 开户 姓名
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/openAccount/name.html", method = RequestMethod.GET)
    public ModelAndView openAccountName() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/app/openAccount/name");
        return mav;
    }

    /**
     * 开户 通知
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/openAccount/notice.html", method = RequestMethod.GET)
    public ModelAndView openAccountNotice() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/app/openAccount/notice");
        return mav;
    }

    /**
     * 开户 开户
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/openAccount/open.html", method = RequestMethod.GET)
    public ModelAndView openAccountOpen() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/app/openAccount/open");
        return mav;
    }

    /**
     * 开户 密码
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/openAccount/passWord.html", method = RequestMethod.GET)
    public ModelAndView openAccountPassWord() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/app/openAccount/passWord");
        return mav;
    }

    /**
     * 出入金 入金
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/intoOut/into.html", method = RequestMethod.GET)
    public ModelAndView intoOutInto() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/app/intoOut/into");
        return mav;
    }

    /**
     * 出入金 出金
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/intoOut/out.html", method = RequestMethod.GET)
    public ModelAndView intoOutOut() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/app/intoOut/out");
        return mav;
    }

    /**
     * 出入金 完成
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/intoOut/payOk.html", method = RequestMethod.GET)
    public ModelAndView intoOutPayOk() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/app/intoOut/payOk");
        return mav;
    }

    /**
     * 出入金 查询
     *
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/intoOut/queryFunds.html", method = RequestMethod.GET)
    public ModelAndView intoOutQueryFunds() throws Exception {
        ModelAndView mav = new ModelAndView("/gjs/app/intoOut/queryFunds");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30);
        mav.addObject("startDate", (new SimpleDateFormat("yyyyMMdd")).format(calendar.getTime()));
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        mav.addObject("endDate", (new SimpleDateFormat("yyyyMMdd")).format(calendar2.getTime()));
        return mav;
    }
}
