/*
*HeepayController.java
*Created on 2015/4/23 16:08
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.controller;

import com.caimao.bana.api.entity.HeepayNoticeEntity;
import com.caimao.bana.api.entity.HeepayRequestEntity;
import com.caimao.bana.api.entity.TpzChargeOrderEntity;
import com.caimao.bana.api.enums.EOrderStatus;
import com.caimao.bana.api.service.IHeepayRecharge;
import com.caimao.bana.api.service.IHeepayWithdraw;
import com.caimao.bana.interceptor.IsMobileDevice;
import com.hsnet.pz.controller.BaseController;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * @author Administrator
 * @version 1.0.1
 */
@Controller
@RequestMapping(value = "/account")
public class HeepayController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HeepayController.class);

    private static final String DEFAULT_URL_ENCODER_CHARSET = "UTF-8";

//    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
//
//    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

    @Value("${review_host}")
    private String REVIEW_HOST;

    private static final String SYNC_REVIEW_URL = "/account/charge/heepay/review/sync";

    private static final String ASYNC_REVIEW_URL = "/account/charge/heepay/review/async";


    public static final String[] HEEPAY_WITHDRAW_PARAMS = {"ret_code", "ret_msg", "agent_id", "hy_bill_no", "status", "batch_no", "batch_amt", "batch_num", "detail_data", "ext_param1"};

    @Resource
    private IHeepayRecharge heepayRecharge;
    @Resource
    private IHeepayWithdraw heepayWithdraw;


//    @RequestMapping(value = "/charge/heepay", method = RequestMethod.POST)
//    @ResponseBody
//    public HeepaySubmitEntity doCharge(@RequestParam("pay_company_no") Long payCompanyNo,
//                                       @RequestParam(value = "bank_no", required = false) String bankNo,
//                                       @RequestParam("charge_amount") Long chargeAmount,
//                                       @RequestParam("terminal_type") String terminalType,
//                                       @RequestParam("pay_type") Integer payType,
//                                       @RequestParam(value = "charge_sync_url", required = false) String chargeSyncUrl,
//                                       @RequestParam(value = "order_abstract", required = false) String orderAbstract) {
//
//        HeepayRequestEntity heepayRequestEntity = new HeepayRequestEntity();
//        heepayRequestEntity.setUserId(getSessionUser().getUser_id());
//        heepayRequestEntity.setChannelId(payCompanyNo);
//        heepayRequestEntity.setBankNo(bankNo);
//        heepayRequestEntity.setOrderAmount(chargeAmount);
//        heepayRequestEntity.setTerminalType(terminalType);
//        heepayRequestEntity.setPayType(payType.toString());
//        heepayRequestEntity.setOrderName("财猫融资充值");
//        heepayRequestEntity.setOrderAbstract(orderAbstract);
//        heepayRequestEntity.setSyncUrl(REVIEW_HOST + SYNC_REVIEW_URL);
//        heepayRequestEntity.setAsyncUrl(REVIEW_HOST + ASYNC_REVIEW_URL);
//        heepayRequestEntity.setUserIp(getRemoteHost());
//        logger.info("sync:{}", REVIEW_HOST + SYNC_REVIEW_URL);
//        logger.info("async:{}", REVIEW_HOST + ASYNC_REVIEW_URL);
//        try {
//            HeepaySubmitEntity heepaySubmitEntity = heepayRecharge.doPreCharge(heepayRequestEntity);
//            heepaySubmitEntity.setGoodsName(URLEncoder.encode(heepaySubmitEntity.getGoodsName(), DEFAULT_URL_ENCODER_CHARSET));
//            return heepaySubmitEntity;
//        } catch (Exception e) {
//            if (e instanceof CustomerException) {
//                throw new BizServiceException(((CustomerException) e).getCode().toString(), e.getMessage());
//            }
//            logger.error("预充值发生异常, 返回未知异常. 异常信息{}", e);
//            throw new BizServiceException("830408", "亲, 充值通道发生未知错误, 请联系客服");
//        }
//    }

    /**
     * 异步请求返回汇付宝OK或者ERROR
     * @param response
     * @param result
     * @param payMessage
     * @param agentId
     * @param jnetBillNo
     * @param agentBillId
     * @param payType
     * @param payAmt
     * @param remark
     * @param sign
     */
    @RequestMapping(value = {"/charge/heepay/review/async", "/charge/heepay/review/sync"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String doChargeAsyncReview(HttpServletRequest request, HttpServletResponse response, @RequestParam("result") String result,
                                      @RequestParam(value = "pay_message", required = false, defaultValue = "") String payMessage,
                                      @RequestParam("agent_id") String agentId, @RequestParam("jnet_bill_no") String jnetBillNo,
                                      @RequestParam("agent_bill_id") String agentBillId, @RequestParam(value = "pay_type") String payType,
                                      @RequestParam(value = "pay_amt") String payAmt, @RequestParam String remark,
                                      @RequestParam String sign) {
        HeepayNoticeEntity heepayNoticeEntity = new HeepayNoticeEntity();
        heepayNoticeEntity.setResult(result);
        heepayNoticeEntity.setPayMessage(payMessage);
        heepayNoticeEntity.setAgentId(agentId);
        heepayNoticeEntity.setJnetBillNo(jnetBillNo);
        heepayNoticeEntity.setAgentBillNo(agentBillId);
        heepayNoticeEntity.setPayType(payType);
        heepayNoticeEntity.setPayAmt(new BigDecimal(payAmt).multiply(new BigDecimal("100")).longValue());
        heepayNoticeEntity.setRemark(remark);
        heepayNoticeEntity.setSign(sign);
        boolean passValid = false;
        try {
            response.setContentType("text/html; charset=utf-8");
            passValid = heepayRecharge.checkeHeepayNoticeSign(heepayNoticeEntity);
            if (passValid) {
                HeepayRequestEntity heepayRequestEntity = new HeepayRequestEntity();
                heepayRequestEntity.setOrderNo(Long.valueOf(agentBillId));
//                heepayRequestEntity.setWorkDate(sdf.format(new Date()));
                heepayRequestEntity.setPayResult(EOrderStatus.SUCCESS.getCode());
                heepayRequestEntity.setOrderAmount(new BigDecimal(payAmt).multiply(new BigDecimal("100")).longValue());
                heepayRequestEntity.setBankResultCode(result);
                heepayRequestEntity.setRemark(remark);
                // 应该还少一部分参数
                TpzChargeOrderEntity tpzChargeOrderEntity = heepayRecharge.doCharge(heepayRequestEntity);
                logger.info("充值成功, server端返回返回数据:{}", ToStringBuilder.reflectionToString(tpzChargeOrderEntity, ToStringStyle.MULTI_LINE_STYLE));
            }
            if (request.getRequestURI().endsWith("async")) {
                if (passValid) {
                    response.getWriter().print("ok");
                    response.getWriter().flush();
                } else {
                    response.getWriter().print("error");
                    response.getWriter().flush();
                    try {
                        heepayRecharge.checkHeepayPayResult(Long.valueOf(agentBillId));
                    } catch (Exception error) {
                        logger.error("充值失败检查订单状态返回异常:{}", error);
                    }
                }
            } else if (request.getRequestURI().endsWith("sync")) {

                IsMobileDevice isMobileDevice = new IsMobileDevice();
                if (isMobileDevice.preHandle(request, response, null)) {
                    response.sendRedirect("/account/recharge/history.htm");
                }

                if (!passValid) {
                    try {
                        heepayRecharge.checkHeepayPayResult(Long.valueOf(agentBillId));
                    } catch (Exception error) {
                        logger.error("充值失败检查订单状态返回异常:{}", error);
                    }
                }
            }
        } catch (Exception e) {
            if (request.getRequestURI().endsWith("async")) {
                try {
                    response.getWriter().print("error");
                    response.getWriter().flush();
                    heepayRecharge.checkHeepayPayResult(Long.valueOf(agentBillId));
                } catch (Exception error) {
                    logger.error("充值失败检查订单状态返回异常:{}", error);
                }
            } else if (request.getRequestURI().endsWith("sync")) {
                try {
                    response.sendRedirect("/account/recharge/history.htm");
                    heepayRecharge.checkHeepayPayResult(Long.valueOf(agentBillId));
                } catch (Exception error) {
                    logger.error("充值失败检查订单状态返回异常:{}", error);
                }
            }
        }
        return null;
    }

    // 汇付宝提现结果回调接口
    @RequestMapping(value = "/withdraw/heepay/review")
    public String doWithdrawReview(HttpServletRequest request, @RequestParam String sign) {
        LinkedHashMap<String, Object> withdrawParams = new LinkedHashMap<>();
        for (String heepayWithdrawParam : HEEPAY_WITHDRAW_PARAMS) {
            withdrawParams.put(heepayWithdrawParam, request.getParameter(heepayWithdrawParam));
        }
        try {
            heepayWithdraw.doWithdrawReview(withdrawParams, sign);
        } catch (Exception e) {
            logger.error("提现回调发生异常, 错误信息:{}", e);
        }
        return null;
    }


}
