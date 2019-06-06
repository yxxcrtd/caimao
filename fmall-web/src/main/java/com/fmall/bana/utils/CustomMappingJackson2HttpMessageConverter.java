package com.fmall.bana.utils;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * HTTP json输出转义
 * Created by Administrator on 2015/9/15.
 */
public class CustomMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
    public CustomMappingJackson2HttpMessageConverter() throws Exception {
    }

    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        String callbackParam = getRequestParam("callback");
        if (callbackParam != null) {
            callbackParam = callbackParam.replaceAll("[^0-9a-zA-Z\\._-]", "");
        }

        JsonObject jsonObject = new JsonObject(object);
        if (callbackParam != null && !callbackParam.equals("")) {
            MappingJacksonValue mjv = new MappingJacksonValue(jsonObject);
            mjv.setJsonpFunction(callbackParam);
            super.writeInternal(mjv, outputMessage);
        } else {
            super.writeInternal(jsonObject, outputMessage);
        }

    }

    private String getRequestParam(String paramName) {
        return getServletRequest().getParameter(paramName);
    }

    private HttpServletRequest getServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}