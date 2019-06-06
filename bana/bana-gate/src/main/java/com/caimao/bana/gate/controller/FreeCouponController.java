/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.gate.controller;

import com.caimao.bana.api.entity.CouponChannelEntity;
import com.caimao.bana.api.enums.CouponChannelType;
import com.caimao.bana.api.service.FreeCouponsService;
import com.caimao.bana.gate.utils.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/freeCoupon", method = {RequestMethod.POST, RequestMethod.GET})
public class FreeCouponController {
    @Resource
    private ExceptionUtils exceptionUtils;

    @Resource
    private FreeCouponsService freeCouponsService;

    @RequestMapping("/index")
    public String index(Model model) throws Exception {
        List<CouponChannelEntity> couponChannelList =  freeCouponsService.queryCouponChannelList(null, 0, 1000);
        model.addAttribute("couponChannelList", couponChannelList);
        return "freeCoupon/index";
    }

    @RequestMapping(value = "/addChannel", method = RequestMethod.POST)
    public String addChannel(WebRequest request) throws Exception {
        int channelType = Integer.parseInt(request.getParameter("channel_type"));
        BigDecimal money = new BigDecimal(request.getParameter("money"));
        BigDecimal scale = new BigDecimal(request.getParameter("scale"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        byte status = Byte.parseByte(request.getParameter("status"));
        freeCouponsService.insertCouponChannel(CouponChannelType.findByValue(channelType), money, scale, amount, status);
        return "redirect:/freeCoupon/index";
    }

    @RequestMapping(value = "/updateChannel", method = RequestMethod.POST)
    public String updateChannel(WebRequest request) throws Exception {
        Long id = Long.parseLong(request.getParameter("id"));
        int channelType = Integer.parseInt(request.getParameter("channel_type"));
        BigDecimal money = new BigDecimal(request.getParameter("money"));
        BigDecimal scale = new BigDecimal(request.getParameter("scale"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        byte status = Byte.parseByte(request.getParameter("status"));

        Map<String, Object> updateParamMap = new HashMap<>();
        updateParamMap.put("channelType", channelType);
        updateParamMap.put("money", money);
        updateParamMap.put("scale", scale);
        updateParamMap.put("amount", amount);
        updateParamMap.put("status", status);
        freeCouponsService.updateCouponChannel(id, updateParamMap);
        return "redirect:/freeCoupon/index";
    }

    @RequestMapping(value = "/addCoupon", method = RequestMethod.POST)
    public String addCoupon(WebRequest request) throws Exception{
        int channelType = Integer.parseInt(request.getParameter("channel_type"));
        BigDecimal money = new BigDecimal(request.getParameter("money"));
        CouponChannelEntity couponChannel = freeCouponsService.lotteryCouponChannel(CouponChannelType.findByValue(channelType), money);
        if(couponChannel == null){
            throw exceptionUtils.getCustomerException(1001);
        }
        return "redirect:/freeCoupon/index";
    }
}
