package com.caimao.bana.utils.Session;

import com.caimao.bana.api.entity.session.UserDetail;
import com.caimao.bana.utils.exception.SessionTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2015/9/29.
 */
public class SessionContextInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(SessionContextInterceptor.class);
    private SessionProvider sessionProvider;

    public SessionContextInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDetail user = this.sessionProvider.getUserDetail();
        if(user != null) {
            UserDetailHolder.setUserDeail(user);
            return super.preHandle(request, response, handler);
        } else {
            log.debug("请求的Controller的类是---------->" + handler.getClass().getCanonicalName());
            log.debug("请求的Controller的方法是---------->" + handler.getClass().getSimpleName());
            String url = request.getRequestURI();
            System.out.print(url);
            throw new SessionTimeoutException(new String[]{"登录链接已超时，请重新登录."});
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserDetailHolder.remove();
        if(ex != null) {
            log.error("系统发生异常信息------------>" + ex.getStackTrace());
        }

    }

    public void setSessionProvider(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }
}