package com.caimao.bana.gate.controller.insiderApi;

import com.caimao.bana.api.entity.AlipayRecordEntity;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IAlipayService;
import com.caimao.bana.api.service.IBtcPledgeService;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.gate.utils.ValidUtils;
import com.huobi.commons.utils.JsonUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "insideer_api", method = {RequestMethod.POST, RequestMethod.GET})
public class InsiderApiController {

    @Resource
    IBtcPledgeService btcPledgeService;

    @Resource
    IUserService userService;

    @Resource
    private IAlipayService alipayService;

    /**
     * 红冲蓝补的API接口
     * URL: /insideer_api/adjust_order
     * @param request
     * @param response
     * @param userId
     * @param orderAmount
     * @param seqFlag
     * @param orderAbstract
     * @param operUser
     * @param needAudit
     * @param sign
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "adjust_order", method = {RequestMethod.POST, RequestMethod.GET})
    public String adjustOrder(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "user_id", required = true) String userId,
                            @RequestParam(value = "order_amount", required = true) String orderAmount,
                            @RequestParam(value = "seq_flag", required = true) String seqFlag,
                            @RequestParam(value = "order_abstract", required = true) String orderAbstract,
                            @RequestParam(value = "oper_user", required = true) String operUser,
                            @RequestParam(value = "need_audit", required = true) String needAudit,
                            @RequestParam(value = "sign", required = true) String sign
                            ) throws Exception {
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("user_id", userId);
        signMap.put("order_amount", orderAmount);
        signMap.put("seq_flag", seqFlag);
        signMap.put("order_abstract", orderAbstract);
        signMap.put("oper_user", operUser);
        signMap.put("need_audit", needAudit);

        Map<String , Object> returnMap = new HashMap<>();

        if ( ! sign.equalsIgnoreCase(ValidUtils.insiderApiSign(signMap))) {
            returnMap.put("result", 0);
            returnMap.put("msg", "签名验证失败");
            String json = JsonUtil.toString(returnMap);
            response.getWriter().print(json);
            response.getWriter().flush();
            return null;
        }

        Long orderAmountLong = new BigDecimal(orderAmount).multiply(new BigDecimal("100")).longValue();
        try {
            Long orderNo = btcPledgeService.doSavePledgeOrder(Long.valueOf(userId), orderAmountLong, orderAbstract, seqFlag, operUser);
            returnMap.put("result", 1);
            returnMap.put("order_no", orderNo.toString());
        } catch (CustomerException e) {
            returnMap.put("result", 0);
            returnMap.put("msg", e.getMessage());
        }

        String json = JsonUtil.toString(returnMap);

        response.getWriter().print(json);
        response.getWriter().flush();

        return null;
    }

    /**
     * 根据手机号码获取用户的user_id
     * URL: /insideer_api/get_user_id_by_phone
     * @param request
     * @param response
     * @param phone
     * @param sign
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "get_user_id_by_phone", method = {RequestMethod.GET, RequestMethod.POST})
    public String getUserIdByPhone(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "phone", required = true) String phone,
                                   @RequestParam(value = "sign", required = true) String sign) throws Exception {
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("phone", phone);

        Map<String , Object> returnMap = new HashMap<>();

        if ( ! sign.equalsIgnoreCase(ValidUtils.insiderApiSign(signMap))) {
            returnMap.put("result", 0);
            returnMap.put("msg", "签名验证失败");
            String json = JsonUtil.toString(returnMap);
            response.getWriter().print(json);
            response.getWriter().flush();
            return null;
        }

        try {
            Long userId = this.userService.getUserIdByPhone(phone);
            if (userId == null) {
                returnMap.put("result", 0);
                returnMap.put("msg", "未找到用户");
            } else {
                returnMap.put("result", 1);
                returnMap.put("user_id", userId.toString());
            }
        } catch (CustomerException e) {
            returnMap.put("result", 0);
            returnMap.put("msg", e.getMessage());
        }

        String json = JsonUtil.toString(returnMap);
        response.getWriter().print(json);
        response.getWriter().flush();
        return null;
    }

    @RequestMapping(value = "saveDepositByAlipay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void saveDepositByAlipay(@RequestParam(value = "dataText", required = true) String dataText) throws Exception{
        List dataTextList = JsonUtil.toObject(dataText, List.class);
        String text = dataTextList.get(0).toString();
        String[] tmpListArray = text.split("交易成功");
        for (String s:tmpListArray){
            try{
                s = s.replace("\r", "");
                String[] tmpTradeArray = s.split("\n");
                Integer tradeLength = tmpTradeArray.length;
                if(tradeLength >= 5){
                    String amount = tmpTradeArray[tradeLength - 1].replace(" ", "");
                    String tradeNo = tmpTradeArray[tradeLength - 3].replace("流水号:", "");
                    tradeNo = tradeNo.replace("交易号:", "");

                    AlipayRecordEntity alipayRecordEntity = new AlipayRecordEntity();
                    alipayRecordEntity.setAmount(new BigDecimal(amount).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN).longValue());
                    alipayRecordEntity.setRealName(tmpTradeArray[tradeLength - 2]);
                    alipayRecordEntity.setTradeId(new BigDecimal(tradeNo));
                    alipayRecordEntity.setFinishTime(new SimpleDateFormat("yyyy.MM.dd HH:mm").parse(tmpTradeArray[tradeLength - 6] + " " + tmpTradeArray[tradeLength - 5]));
                    alipayRecordEntity.setCreateTime(new Date());
                    alipayService.saveTradeRecord(alipayRecordEntity);
                }
            }catch(Exception ignored){

            }
        }
    }
}
