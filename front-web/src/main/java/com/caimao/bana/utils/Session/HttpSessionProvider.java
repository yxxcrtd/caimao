package com.caimao.bana.utils.Session;

import com.caimao.bana.api.entity.session.UserDetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2015/9/29.
 */
public class HttpSessionProvider implements SessionProvider {
    private static final String USER_DETAIL_KEY = "user";

    public HttpSessionProvider() {
    }

    public void setUserDetail(UserDetail userDetail) {
        HttpSession session = this.getRequest().getSession();
        session.setAttribute("user", userDetail);
    }

    public void setAttribute(String name, Object value) {
        HttpSession session = this.getRequest().getSession();
        session.setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        HttpSession session = this.getRequest().getSession(false);
        if(session != null) {
            session.removeAttribute(name);
        }

    }

    public Object getAttribute(String name) {
        HttpSession session = this.getRequest().getSession(false);
        return session != null?session.getAttribute(name):null;
    }

    public void removeUserDetail() {
        HttpSession session = this.getRequest().getSession();
        session.removeAttribute("user");
    }

    private HttpServletRequest getRequest() {
        return ControllerContext.getRequest();
    }

    public UserDetail getUserDetail() {
        return (UserDetail)this.getRequest().getSession().getAttribute("user");
    }
}