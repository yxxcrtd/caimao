package com.caimao.bana.controller.other;

import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.session.SessionUser;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.utils.Session.SessionProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PageController {

    private static final String SESSION_KEY_USER = "user";

    @Autowired
    protected SessionProvider sessionProvider;

    @Resource
    private IUserService userService;

    @Value("${ybkUrl}")
    private String ybkUrl;
    @Value("${homeUrl}")
    private String homeUrl;

    //专题页面
    @RequestMapping(value = "/topic/ybk.html", method = RequestMethod.GET)
    public ModelAndView topicYbk(HttpServletRequest request) throws Exception {
        //邀请用户记录
        String i = request.getParameter("i");
        if(StringUtils.isNotBlank(i)){
            try{
                Long li = Long.parseLong(i);
                TpzUserEntity tpzUserEntity = userService.getByCaimaoId(li);
                if(tpzUserEntity != null) request.getSession().setAttribute("u", String.valueOf(tpzUserEntity.getUserId()));
            }catch (Exception ignored){}
        }
        return new ModelAndView("/topic/ybk");
    }

    @RequestMapping(value = "/topic/ybk2.html", method = RequestMethod.GET)
    public ModelAndView topicYbk2() throws Exception {
        return new ModelAndView("/topic/ybk2");
    }


    @RequestMapping(value = "/{module}.htm", method = RequestMethod.GET)
    public String indexAction(@PathVariable("module") String module, Model model) throws Exception{
        if(module.equals("index")) return "redirect:/";
        model.addAttribute("", "");
        return "page/" + module;
    }
    @RequestMapping(value = "/user/{view}", method = RequestMethod.GET)
    public String user1Action(@PathVariable("view") String view) {
        return "user/" + view;
    }

    @RequestMapping(value = "/home/{module}.htm")
    public String homeAction(@PathVariable("module") String module) {
        return "home/" + module;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView page(HttpServletRequest request, HttpServletResponse response) throws Exception{

        response.sendRedirect(this.homeUrl);
        return null;

//        //模板
//        ModelAndView mav = new ModelAndView("/page/index");
//
//        //是否登录
//        SessionUser user = (SessionUser) sessionProvider.getAttribute(SESSION_KEY_USER);
//        mav.addObject("userBase", user);
//
//        //查询公告
//        /*FNoticeQueryListReq req = new FNoticeQueryListReq();
//        req.setListShow(1);
//        req.setTopShow(1);
//        req.setStart(0);
//        req.setLimit(1);
//        req.setOrderColumn("created");
//        req.setOrderDir("DESC");
//        req = this.noticeService.queryList(req);
//        FNoticeInfoRes noticeInfoRes = null;
//        if(req.getItems() != null){
//            noticeInfoRes = req.getItems().get(0);
//        }
//        mav.addObject("noticeInfoRes", noticeInfoRes);*/
//
//        //邀请用户记录
//        String i = request.getParameter("i");
//        if(StringUtils.isNotBlank(i)){
//            try{
//                Long li = Long.parseLong(i);
//                TpzUserEntity tpzUserEntity = userService.getByCaimaoId(li);
//                if(tpzUserEntity != null) request.getSession().setAttribute("u", String.valueOf(tpzUserEntity.getUserId()));
//            }catch (Exception ignored){}
//        }
//
//        //查询可投
//        FP2PQueryPageLoanReq loanReq = new FP2PQueryPageLoanReq();
//        loanReq.setTargetStatus(new Byte(String.valueOf(0)));
//        loanReq.setStart(0);
//        loanReq.setLimit(4);
//        loanReq.setOrderColumn("yearRate");
//        loanReq.setOrderDir("DESC");
//        mav.addObject("loanPage", p2PService.queryPageLoan(loanReq));
//
//        //查询未满标数量
//        mav.addObject("loanCount", p2PService.queryLoanCount(0));
//
//        //查询满投
//        FP2PQueryPageLoanReq loanFullReq = new FP2PQueryPageLoanReq();
//        loanFullReq.setTargetStatus(new Byte(String.valueOf(1)));
//        loanFullReq.setStart(0);
//        loanFullReq.setLimit(4);
//        loanFullReq.setOrderColumn("yearRate");
//        loanFullReq.setOrderDir("DESC");
//        mav.addObject("loanFullPage", p2PService.queryPageFullLoan(loanFullReq));
//
//        //查询满标数量
//        mav.addObject("loanFullCount", p2PService.queryLoanFullCount());
//
//        //融资排行榜
//        mav.addObject("pzRankingResList", otherDataService.indexPzRankingList(5));
//
//        //实时动态
//        mav.addObject("pzRealTimeList", otherDataService.indexRealtimePZList(5));
//        mav.addObject("curTopMenu", "index");
//
//        if(new Date().after(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-08-19 00:00:00")) &&
//                new Date().before(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-09-01 00:00:00"))){
//            mav.addObject("activityOpen", 1);
//        }
//
//        return mav;
    }

    // 手机页面
    @RequestMapping(value = "/mobile/{module}.htm", method = RequestMethod.GET)
    public String mobileIndexAction(@PathVariable("module") String module) {
        return "mobile/" + module;
    }

    @RequestMapping(value = "/mobile/account/{page}.htm", method = RequestMethod.GET)
    public String mobileAccountAction(@PathVariable("page") String page) {
        return "mobile/account/" + page;
    }

    @RequestMapping(value = "/mobile/trade/{page}.htm", method = RequestMethod.GET)
    public String mobileTradeAction(@PathVariable("page") String page) {
        return "mobile/trade/" + page;
    }

    @RequestMapping(value = "/mobile/account/{module}/{page}.htm", method = RequestMethod.GET)
    public String mobileAccountAction(@PathVariable("module") String module,
                                      @PathVariable("page") String page) {
        return "mobile/account/" + module + "/" + page;
    }
    @RequestMapping(value = "/mobile/user/{view}", method = RequestMethod.GET)
    public String mobileUserAction(@PathVariable("view") String view, HttpServletRequest request) {
        if(view.equals("login")){
            String i = request.getParameter("i");
            if(StringUtils.isNotBlank(i)){
                try{
                    Long li = Long.parseLong(i);
                    TpzUserEntity tpzUserEntity = userService.getByCaimaoId(li);
                    if (tpzUserEntity != null) {
                        request.getSession().setAttribute("u", String.valueOf(tpzUserEntity.getUserId()));
                    }
                }catch (Exception ignored){}
            }
        }
        return "mobile/user/" + view;
    }
    @RequestMapping(value = "/mobile/financing/{page}.htm", method = RequestMethod.GET)
    public String mobileFinancingAction(@PathVariable("page") String page) {
        return "mobile/financing/" + page;
    }
    @RequestMapping(value = "/mobile/financing/{module}/{page}.htm", method = RequestMethod.GET)
    public String mobileFinancingAction(@PathVariable("module") String module,
                                        @PathVariable("page") String page) {
        return "mobile/financing/" + module + "/" + page;
    }
    @RequestMapping(value = "/mobile", method = RequestMethod.GET)
    public String mobilePage(HttpServletRequest request) {
        SessionUser user = (SessionUser) sessionProvider.getAttribute(SESSION_KEY_USER);
        String i = request.getParameter("i");
        if(StringUtils.isNotBlank(i)){
            try{
                Long li = Long.parseLong(i);
                TpzUserEntity tpzUserEntity = userService.getByCaimaoId(li);
                if (tpzUserEntity != null) {
                    request.getSession().setAttribute("u", String.valueOf(tpzUserEntity.getUserId()));
                }
            }catch (Exception ignored){}
        }
        if (null != user) {
            return "redirect:/mobile/index.htm";
        }
        return "mobile/user/login";
    }
}
