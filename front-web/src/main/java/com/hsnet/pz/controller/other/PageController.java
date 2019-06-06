package com.hsnet.pz.controller.other;

import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.session.SessionUser;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.utils.Session.SessionProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 跳转页面Controller
 *
 * @author zhanggl10620
 */
//@Controller
public class PageController {

    private static final String SESSION_KEY_USER = "user";

    @Autowired
    protected SessionProvider sessionProvider;

    @Resource
    private IUserService userService;

    @RequestMapping(value = "/{module}.htm", method = RequestMethod.GET)
    public String indexAction(@PathVariable("module") String module, HttpServletRequest request) {

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
        return module;
    }

    @RequestMapping(value = "/mobile/{module}.htm", method = RequestMethod.GET)
    public String mobileIndexAction(@PathVariable("module") String module) {
        return "mobile/" + module;
    }

    @RequestMapping(value = "/home/{module}.htm")
    public String homeAction(@PathVariable("module") String module) {
        return "home/" + module;
    }

//    @RequestMapping(value = "/account/{page}.htm", method = RequestMethod.GET)
//    public ModelAndView accountAction(HttpServletRequest request, @PathVariable("page") String page) {
//        if ("recharge".equals(page)) {
//            Map<String, Object> modelMap = new HashMap<>();
//            modelMap.put("to", "recharge");
//            if ("rechargeList".equals(request.getParameter("to"))) {
//                modelMap.put("to", "rechargeList");
//            }
//            return new ModelAndView("account/" + page, modelMap);
//        }
//        return new ModelAndView("account/" + page);
//    }
//
    @RequestMapping(value = "/mobile/account/{page}.htm", method = RequestMethod.GET)
    public String mobileAccountAction(@PathVariable("page") String page) {
        return "mobile/account/" + page;
    }

    @RequestMapping(value = "/trade/{page}.htm", method = RequestMethod.GET)
    public String tradeAction(@PathVariable("page") String page) {
        return "trade/" + page;
    }

    @RequestMapping(value = "/mobile/trade/{page}.htm", method = RequestMethod.GET)
    public String mobileTradeAction(@PathVariable("page") String page) {
        return "mobile/trade/" + page;
    }

//    @RequestMapping(value = "/account/{module}/{page}.htm", method = RequestMethod.GET)
//    public String accountAction(@PathVariable("module") String module,
//                                @PathVariable("page") String page) {
//        return "account/" + module + "/" + page;
//    }
//
    @RequestMapping(value = "/mobile/account/{module}/{page}.htm", method = RequestMethod.GET)
    public String mobileAccountAction(@PathVariable("module") String module,
                                      @PathVariable("page") String page) {
        return "mobile/account/" + module + "/" + page;
    }

    @RequestMapping(value = "/user/register.htm", method = RequestMethod.GET)
    public String userAction(HttpServletRequest request) {
        String refuserId = (String) request.getSession().getAttribute("u");
        if (StringUtils.isBlank(refuserId)) {
            String u = (String) request.getParameter("u");
            request.getSession().setAttribute("u", u);
        }
        return "user/" + "register";
    }

    @RequestMapping(value = "/user/{view}", method = RequestMethod.GET)
    public String user1Action(@PathVariable("view") String view) {
        return "user/" + view;
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

//    @RequestMapping(value = "/financing/{page}.htm", method = RequestMethod.GET)
//    public String financingAction(@PathVariable("page") String page) {
//        return "financing/" + page;
//    }

    @RequestMapping(value = "/mobile/financing/{page}.htm", method = RequestMethod.GET)
    public String mobileFinancingAction(@PathVariable("page") String page) {
        return "mobile/financing/" + page;
    }

    @RequestMapping(value = "/financing/{module}/{page}.htm", method = RequestMethod.GET)
    public String financingAction(@PathVariable("module") String module,
                                  @PathVariable("page") String page) {
        return "financing/" + module + "/" + page;
    }

    @RequestMapping(value = "/mobile/financing/{module}/{page}.htm", method = RequestMethod.GET)
    public String mobileFinancingAction(@PathVariable("module") String module,
                                        @PathVariable("page") String page) {
        return "mobile/financing/" + module + "/" + page;
    }

    @RequestMapping(value = "/msg/{page}.htm", method = RequestMethod.GET)
    public String messageAction(@PathVariable("page") String page) {
        return "msg/" + page;
    }

    @RequestMapping(value = "/msg/{module}/{page}.htm", method = RequestMethod.GET)
    public String msgAction(@PathVariable("module") String module,
                            @PathVariable("page") String page) {
        return "msg/" + module + "/" + page;
    }

    @RequestMapping(value = "/prompt", method = RequestMethod.GET)
    public String prompt(@PathVariable("view") String view) {
        return "prompt/" + view;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String page(HttpServletRequest request) {
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
            return "redirect:/home/index.htm";
        }
        return "page/index";
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

    @RequestMapping(value = "/planning/{page}.htm", method = RequestMethod.GET)
    public String planningAction(@PathVariable("page") String page) {
        return "planning/" + page;
    }

    @RequestMapping(value = "/planning/{module}/{page}.htm", method = RequestMethod.GET)
    public String planning2Action(@PathVariable("module") String module,
                                  @PathVariable("page") String page) {
        return "planning/" + module + "/" + page;
    }

    @RequestMapping(value = "/heyi/{page}.htm", method = RequestMethod.GET)
    public String heyiPageAction(@PathVariable("page") String page) {
        return "heyi/" + page;
    }

    @RequestMapping(value = "/standard/{page}.htm", method = RequestMethod.GET)
    public String xingxiaPageAction(@PathVariable("page") String page) {
        return "standard/" + page;
    }

    @RequestMapping(value = "/popularize/{page}.htm", method = RequestMethod.GET)
    public String popularizeAction(@PathVariable("page") String page) {
        return "popularize/" + page;
    }

    @RequestMapping(value = "/tender/{page}.htm", method = RequestMethod.GET)
    public String tenderAction(@PathVariable("page") String page) {
        return "tender/" + page;
    }

    @RequestMapping(value = "/account/charge/return.htm")
    public String queryChargeOrder() {
        SessionUser user = (SessionUser) sessionProvider
                .getAttribute(SESSION_KEY_USER);
        if (null != user) {
            return "redirect:/home/index.htm";
        }
        return "user/login";
    }

    @RequestMapping(value = "/p2p/{page}.htm", method = RequestMethod.GET)
    public String P2PIndexAction(@PathVariable("page") String module) {
        return "p2p/" + module;
    }
}
