package com.fmall.bana.utils.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/29.
 */
public class ControllerContext {
    private static ThreadLocal<Map<String, Object>> controllerContext = new ThreadLocal();
    public static final String HTTP_SERVLET_RQUEST = "request";
    public static final String HTTP_SERVELET_RESPONSE = "response";

    public ControllerContext() {
    }

    public static void set(Map<String, Object> map) {
        controllerContext.set(map);
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) ((Map) controllerContext.get()).get("request");
    }

    public static HttpSession getSession() {
        HttpServletRequest reqest = getRequest();
        return reqest.getSession(false);
    }

    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) ((Map) controllerContext.get()).get("response");
    }

    public static void remove() {
        controllerContext.remove();
    }
}
