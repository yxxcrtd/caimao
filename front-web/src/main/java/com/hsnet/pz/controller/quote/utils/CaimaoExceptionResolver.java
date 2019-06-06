/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.hsnet.pz.controller.quote.utils;

/**
 * @author yanjg
 * 2015年4月29日
 */

import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.utils.exception.BaseRuntimeException;
import com.caimao.bana.utils.exception.DataValidationException;
import com.caimao.bana.utils.exception.ErrorMessage;
import com.caimao.bana.utils.exception.SystemException;
import com.hsnet.frontcore.utils.JacksonUtils;
import com.hsnet.pz.core.exception.BizServiceException;
import com.hundsun.jresplus.remoting.exception.RemotingServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

public class CaimaoExceptionResolver extends SimpleMappingExceptionResolver
{
  private static final Logger logger = LoggerFactory.getLogger(CaimaoExceptionResolver.class);

  @Autowired
  MessageSource messageSource;
  private String defaultErrorView;
  private Properties exceptionMappings;
  private Class<?>[] excludedExceptions;

  protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    BaseRuntimeException exception = exceptionWapper2(ex);

    convertErrorMessage(request, exception);

    if (isAjaxRequest(request, handler)) {
      return doResolveAjaxException(response, exception);
    }

    exception.toMessage();
    return super.doResolveException(request, response, handler, exception);
  }

  private boolean isAjaxRequest(HttpServletRequest request, Object handler)
  {
    if (request.getHeader("accept").indexOf("application/json") > -1) {
      return true;
    }
    if (request.getHeader("X-Requested-With") == null) {
      return false;
    }
    if (request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1) {
      return true;
    }
    return false;
  }

  private BaseRuntimeException exceptionWapper2(Exception exception)
  {
    if (exception instanceof RemotingServiceException) {
      BaseRuntimeException ex = new DataValidationException();
      RemotingServiceException rse = (RemotingServiceException)exception;
      ex.addErrorMessage(new String[] { rse.getErrorCode(), rse.getErrorMessage() });
      return ex;
    }else if(exception instanceof CustomerException){
        BaseRuntimeException ex = new DataValidationException();
        ex.addErrorMessage(new String[] { ((CustomerException) exception).getCode().toString(),
                ((CustomerException) exception).getMessage()});
        return ex;
    }
    if (isNotBaseRuntimeException(exception).booleanValue()) {
      BaseRuntimeException ex = new SystemException(
        "sys.default.error.info", "服务器发生内部错误，请稍后再试或报告给网站管理员。");
      return ex;
    }
    return (BaseRuntimeException)exception;
  }
  public static void main(String[] args){
      Exception be=new BizServiceException("32","test" );
      if(be instanceof RemotingServiceException){
          System.out.println("yes");
      }else{
          System.out.println("no");
      }
      
  }

  private void convertErrorMessage(HttpServletRequest request, BaseRuntimeException ex)
  {
    Locale locale = RequestContextUtils.getLocale(request);
    if ((isNotDataValidationException(ex).booleanValue()) && 
      (ex.getExceptions() != null))
      for (Iterator localIterator = ex.getExceptions().iterator(); localIterator.hasNext(); ) { Object er = localIterator.next();
        ErrorMessage error = (ErrorMessage)er;
        if (StringUtils.isNotBlank(error.getNo())) {
          String message = this.messageSource.getMessage(
            error.getNo(), null, error.getInfo(), locale);
          error.setInfo(message);
        }
      }
  }

  private ModelAndView doResolveAjaxException(HttpServletResponse response, BaseRuntimeException ex)
  {
    setResponse(response);
    try {
      PrintWriter writer = response.getWriter();
      String jsonStr = JacksonUtils.toJsonString(ex, ex.packProperties());
      if (StringUtils.isNotBlank("jsonStr")) {
        writer.write(jsonStr);
      }
      writer.flush();
    } catch (IOException e) {
      logger.error("处理ajax请求 返回异常信息失败:" + e);
    }
    return new ModelAndView();
  }

  private void setResponse(HttpServletResponse response)
  {
    response.setContentType("application/json;charset=UTF-8");
  }

  private Boolean isNotBaseRuntimeException(Exception exception) {
    return Boolean.valueOf(!isBaseRuntimeException(exception).booleanValue());
  }

  private Boolean isNotDataValidationException(Exception exception) {
    return Boolean.valueOf(!isDataValidationException(exception).booleanValue());
  }

  private Boolean isBaseRuntimeException(Exception exception) {
    return Boolean.valueOf(exception instanceof BaseRuntimeException);
  }

  private Boolean isDataValidationException(Exception exception) {
    return Boolean.valueOf(exception instanceof DataValidationException);
  }

  protected String determineViewName(Exception ex, HttpServletRequest request)
  {
    String viewName = null;
    if (this.excludedExceptions != null) {
      for (Class excludedEx : this.excludedExceptions) {
        if (excludedEx.equals(ex.getClass())) {
          return null;
        }
      }
    }

    if (this.exceptionMappings != null) {
      viewName = findMatchingViewName(this.exceptionMappings, ex);
      if (viewName != null) {
        viewName = viewName + "?return=" + request.getRequestURI();
      }
    }

    if ((viewName == null) && (this.defaultErrorView != null)) {
      if (logger.isDebugEnabled()) {
        logger.debug("Resolving to default view '" + this.defaultErrorView + "' for exception of type [" + 
          ex.getClass().getName() + "]");
      }
      viewName = this.defaultErrorView;
    }
    return viewName;
  }

  public String getDefaultErrorView()
  {
    return this.defaultErrorView;
  }

  public void setDefaultErrorView(String defaultErrorView) {
    this.defaultErrorView = defaultErrorView;
  }

  public Properties getExceptionMappings() {
    return this.exceptionMappings;
  }

  public void setExceptionMappings(Properties exceptionMappings) {
    this.exceptionMappings = exceptionMappings;
  }

  public Class<?>[] getExcludedExceptions() {
    return this.excludedExceptions;
  }
}