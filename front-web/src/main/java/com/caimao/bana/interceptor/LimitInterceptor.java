package com.caimao.bana.interceptor;

import com.caimao.bana.api.entity.session.SessionUser;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.utils.RedisUtils;
import com.caimao.bana.utils.Session.SessionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * controller的拦截器，用于对融资、提现等操作做限速操作
 * 
 * @author zxd
 */
public class LimitInterceptor extends HandlerInterceptorAdapter {
    private static final String PREFIX = "caimao_pz";
    private static final long LIMIT = 5l;

    @Autowired
    protected SessionProvider sessionProvider;
    @Autowired
    protected RedisUtils redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SessionUser user = (SessionUser) sessionProvider.getUserDetail();
        if (user == null) {
            return false;
        }
        String uri = request.getServletPath();
        return checkSpeed(user.getUser_id(), uri);
    }

    private boolean checkSpeed(Long userId, String uri) throws CustomerException {
        String key = PREFIX + userId + uri;
        Long num = redis.incr(key);
        synchronized (this) {
            Long ttl = redis.getTtl(key);
            if (ttl == null || ttl < 0) {
                redis.set(key, num + "", LIMIT);
                return true;
            }
        }
        throw new CustomerException("操作频繁，请稍后再试", 888888);
        //return false;
    }
}
