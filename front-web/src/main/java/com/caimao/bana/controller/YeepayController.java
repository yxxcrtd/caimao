/*
*YeepayController.java
*Created on 2015/4/23 16:08
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.controller;

import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IYeepayRecharge;
import com.hsnet.pz.controller.BaseController;
import com.hsnet.pz.core.exception.BizServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;

/**
 * 易宝充值、提现相关
 * @author Administrator
 * @version 1.0.1
 */
@Controller
@RequestMapping(value = "/account")
public class YeepayController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(YeepayController.class);

    @Resource
    private IYeepayRecharge yeepayRecharge;

    /**
     * 易宝手机版充值
     * @param request   请求对象
     * @param payCompanyNo  充值通道
     * @param bankNo    银行代码
     * @param chargeAmount  充值金额
     * @param terminalType  终端类型
     * @param payType   充值类型
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/charge/yeepay_mobile", method = RequestMethod.POST)
    public Map<String, String> doMobileCharge(HttpServletRequest request, @RequestParam("pay_company_no") Long payCompanyNo,
                                       @RequestParam(value = "bank_no", required = false) String bankNo,
                                       @RequestParam("charge_amount") Long chargeAmount,
                                       @RequestParam("terminal_type") String terminalType,
                                       @RequestParam("pay_type") Integer payType
                                       ) {
        try {
            Long userId = getSessionUser().getUser_id();
            String agent = request.getHeader("user-agent");
            String payUrl = this.yeepayRecharge.doPreMobileCharge(userId, chargeAmount, getRemoteHost(), agent);
            Map<String, String> yeepayRsp = new TreeMap<>();
            yeepayRsp.put("url", payUrl);
            return yeepayRsp;
        } catch (Exception e) {
            if (e instanceof CustomerException) {
                throw new BizServiceException(((CustomerException) e).getCode().toString(), e.getMessage());
            }
            logger.error("预充值发生异常, 返回未知异常. 异常信息{}", e);
            throw new BizServiceException("830408", "亲, 充值通道发生未知错误, 请联系客服");
        }
    }

    /**
     * 易宝手机同步异步处理方法
     * @param request   请求对象
     * @param response  响应对象
     * @param data  密文
     * @param encryptkey    密文
     * @return
     */
    @RequestMapping(value = {"/charge/yeepay_mobile/review/async", "/charge/yeepay_mobile/review/sync"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String doMobileChargeAsyncReview(HttpServletRequest request, HttpServletResponse response,
                                            @RequestParam("data") String data,
                                            @RequestParam("encryptkey") String encryptkey) {
        boolean passValid = false;
        try {
            response.setContentType("text/html; charset=utf-8");
            passValid = this.yeepayRecharge.doMobileCharge(data, encryptkey);
            if (request.getRequestURI().endsWith("async")) {
                if (passValid) {
                    response.getWriter().print("ok");
                    response.getWriter().flush();
                } else {
                    response.getWriter().print("error");
                    response.getWriter().flush();
                }
            } else if (request.getRequestURI().endsWith("sync")) {
                response.sendRedirect("/mobile/user/index.htm");
            }
        } catch (Exception e) {
            if (request.getRequestURI().endsWith("async")) {
                try {
                    response.getWriter().print("error");
                    response.getWriter().flush();
                } catch (Exception error) {
                    logger.error("充值失败检查订单状态返回异常:{}", error);
                }
            } else if (request.getRequestURI().endsWith("sync")) {
                try {
                    response.sendRedirect("/mobile/user/index.htm");
                } catch (Exception error) {
                    logger.error("充值失败检查订单状态返回异常:{}", error);
                }
            }
        }
        return null;
    }



}
