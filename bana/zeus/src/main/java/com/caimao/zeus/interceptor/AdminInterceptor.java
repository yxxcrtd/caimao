package com.caimao.zeus.interceptor;

import com.caimao.zeus.admin.entity.Rule;
import com.caimao.zeus.admin.entity.User;
import com.caimao.zeus.admin.service.AdminService;
import com.caimao.zeus.util.AdminMenuUtils;
import com.caimao.zeus.util.Constants;
import com.caimao.zeus.util.SessionAware;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminInterceptor implements HandlerInterceptor {
    @Resource
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String currentUrl = httpServletRequest.getRequestURI();

        //公共页
        List<String> publicUrl = new ArrayList<>();
        publicUrl.add("/admin/login");
        publicUrl.add("/admin/logout");
        publicUrl.add("/favicon.ico");
        if(publicUrl.contains(currentUrl)){
            return true;
        }

        //登录验证
        HttpSession session = SessionAware.getSession();
        User user = (User)session.getAttribute(Constants.SESSION_USER_KEY);
        if(user == null){
            httpServletResponse.sendRedirect("/admin/login");
            return false;
        }

        httpServletRequest.setAttribute("adminUser", user);

        //权限验证
        if(!adminService.checkRule(user.getId(), currentUrl)) throw new Exception("权限未通过");

        Rule ruleCur = adminService.getRule(currentUrl);

        //获取管理员所有权限数据
        List<Rule> ruleList = adminService.getUserRules(user.getId());

        //输出菜单
        AdminMenuUtils adminMenuUtils = new AdminMenuUtils();
        Map<String, Object> ruleMap = adminMenuUtils.processMenu(ruleCur, ruleList);
        httpServletRequest.setAttribute("ruleMap", ruleMap);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
