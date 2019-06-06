package com.caimao.bana.gate.filter;

import com.caimao.bana.gate.utils.ApplicationContextUtils;
import com.caimao.bana.gate.utils.RedisUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class VisitLimitedFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(VisitLimitedFilter.class);

    private String initIntervals;

    private String maxLimited;

    private RedisUtils redisUtils;

    private MessageSource messageSource;

    private static final String KEY = "bitvc-api-visitLimited";

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String responseMessage = checkVisitTimes(request);
        if (responseMessage == null || "".equals(responseMessage)) {
            chain.doFilter(request, response);
        } else {
            responseErrorMessage(response, responseMessage);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.initIntervals = filterConfig.getInitParameter("interval");
        if (initIntervals == null) {
            initIntervals = "10";
        }

        this.maxLimited = filterConfig.getInitParameter("limited");
        if (maxLimited == null) {
            maxLimited = "30";
        }

        redisUtils = ApplicationContextUtils.getBean("redisUtils");
        messageSource = ApplicationContextUtils.getBean("messageSource");
        logger.info("visitlimitedfilter init!");
    }

    public String checkVisitTimes(HttpServletRequest request) {
        Long now = System.currentTimeMillis();
        String subKey = request.getParameter("access_key");
        String responseMessage = StringUtils.EMPTY;
        if (subKey == null) {
            responseMessage = "{\"code\":64,\"message\":\"" + messageSource.getMessage("error.64", null, null) + "\"}";
            return responseMessage;
        }
        String key = KEY + subKey;

        Object value = redisUtils.get(1, key);
        if (null == value) {
            redisUtils.set(2, key, now + ",1", 24 * 3600L);
        } else {
            String[] val = value.toString().split(",");
            long lastRecordTime = Long.parseLong(val[0]);
            int times = Integer.parseInt(val[1]);
            long interval = TimeUnit.MILLISECONDS.convert(Long.parseLong(initIntervals), TimeUnit.SECONDS);
            if (now - lastRecordTime < interval && times > Integer.parseInt(maxLimited)) {
                responseMessage = "{\"code\":71,\"message\":\"" + messageSource.getMessage("error.71", null, null)
                        + "\"}";
            } else {
                if (now - lastRecordTime < interval) {
                    redisUtils.set(1, key, lastRecordTime + "," + (++times), 24 * 3600L);
                } else {
                    redisUtils.set(1, key, now + ",1", 24 * 3600L);
                }
            }
        }
        return responseMessage;
    }

    public void destroy() {

    }

    private void responseErrorMessage(HttpServletResponse response, String responseMessage) {
        logger.info("responseMessage:" + responseMessage);
        try {
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print(responseMessage);
            response.getWriter().flush();
        } catch (Exception e) {
            response.setStatus(500);
            logger.error("RequestParamFilter 输出错误信息异常,错误信息:{}", e);
        }
    }

}
