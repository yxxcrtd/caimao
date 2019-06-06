package com.caimao.bana.gate.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 任培伟
 * @version 1.0.0
 */
public class SessionAwareFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        SessionAware.setRequest((HttpServletRequest) request);
        SessionAware.setResponse((HttpServletResponse) response);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }
}
