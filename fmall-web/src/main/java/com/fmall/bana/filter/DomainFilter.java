package com.fmall.bana.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DomainFilter implements Filter {
    private static final String SEP = "/";

    private static final Logger logger = LoggerFactory.getLogger(DomainFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Boolean isFilter = true;

        //域名列表
        List<String> domainList = new ArrayList<>();
        domainList.add("ybk");
        domainList.add("gjs");
        domainList.add("jin");
        domainList.add("api");
        //忽略列表
        List<String> ignoreList = new ArrayList<>();
        ignoreList.add("static");
        ignoreList.add("favicon.ico");
        ignoreList.add("upload");

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        // 获取请求路径
        String path = httpServletRequest.getRequestURI();

        String serverName = servletRequest.getServerName();
        if (!serverName.contains(".")) {
            isFilter = false;
        }
        String app = "";
        if (serverName.contains(".")) {
            app = serverName.substring(0, serverName.indexOf("."));
        }

        // 检查是否在转换的列表中
        if (isFilter) if (!domainList.contains(app)) {
            isFilter = false;
        }
        if (isFilter) {
            // 检查链接中，是否已经包含要添加的app标示，已经包含了，就直接返回，不进行添加
            Integer oneIndex = path.indexOf("/");
            Integer twoIndex = path.indexOf("/", 2);
            if (oneIndex >= 0) {
                String reqApp = null;
                if (twoIndex >= 0) {
                    reqApp = path.substring(oneIndex + 1, twoIndex);
                } else {
                    reqApp = path.substring(oneIndex + 1);
                }
                if (app.equals(reqApp)) {
                    isFilter = false;
                }
                if (ignoreList.contains(reqApp)) {
                    isFilter = false;
                }
                if (domainList.contains(reqApp)) {
                    isFilter = false;
                }
            }
            if (isFilter) {
                httpServletRequest.getRequestDispatcher(SEP + app + path).forward(servletRequest, servletResponse);
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
