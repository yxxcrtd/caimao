package com.caimao.zeus.admin.controller;

import com.caimao.bana.common.api.enums.gjs.EGJSExchange;
import com.caimao.zeus.util.JSONUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class BaseController {
    /**
     * 欢迎界面
     * @return
     */
    @RequestMapping("/")
    public String welcome() {
        return "admin/welcome";
    }

    public Boolean isAjax(HttpServletRequest request) throws Exception{
        String header = request.getHeader("X-Requested-With");
        return header != null && header.toLowerCase().equals("xmlhttprequest");
    }

    public ModelAndView jumpForFail(HttpServletRequest request, String msg, String url) throws Exception{
        if(isAjax(request)){
            Map<String, String> ajaxData = new HashMap<>();
            ajaxData.put("code", "1");
            ajaxData.put("msg", msg);
            JSONUtils jsonUtils = new JSONUtils(JsonInclude.Include.ALWAYS);

            Map<String, String> model = new HashMap<>();
            model.put("ajaxData", jsonUtils.jsonToString(ajaxData));
            return new ModelAndView("common/ajax", model);
        }else{
            Map<String, String> model = new HashMap<>();
            model.put("failMsg", msg);
            model.put("url", url);
            return new ModelAndView("common/jump", model);
        }
    }

    public ModelAndView jumpForSuccess(HttpServletRequest request, String msg, String url) throws Exception{
        if(isAjax(request)){
            Map<String, String> ajaxData = new HashMap<>();
            ajaxData.put("code", "0");
            ajaxData.put("msg", msg);
            JSONUtils jsonUtils = new JSONUtils(JsonInclude.Include.ALWAYS);

            Map<String, String> model = new HashMap<>();
            model.put("ajaxData", jsonUtils.jsonToString(ajaxData));
            return new ModelAndView("common/ajax", model);
        }else{
            Map<String, String> model = new HashMap<>();
            model.put("successMsg", msg);
            model.put("url", url);
            return new ModelAndView("common/jump", model);
        }
    }

    protected Map<String, String> getExchangeMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(EGJSExchange.NJS.getCode(), EGJSExchange.NJS.getName());
        map.put(EGJSExchange.SJS.getCode(), EGJSExchange.SJS.getName());
        return map;
    }

}
