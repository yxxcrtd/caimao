package com.fmall.bana.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * 配置静态文件工具类
 *
 * @author zhanggl10620
 */
public class ApplicationProperties implements ServletContextAware {

    private ServletContext servletContext;

    /**
     * cook配置
     */
    @Value("${web.cookies.path}")
    private String webCookiesPath;

    /**
     * cook配置
     */
    @Value("${web.cookies.domain}")
    private String webCookiesDomain;

    public String getWebCookiesPath() {
        return webCookiesPath;
    }

    public void setWebCookiesPath(String webCookiesPath) {
        this.webCookiesPath = webCookiesPath;
    }

    public String getWebCookiesDomain() {
        return webCookiesDomain;
    }

    public void setWebCookiesDomain(String webCookiesDomain) {
        this.webCookiesDomain = webCookiesDomain;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext context) {
    }

}
