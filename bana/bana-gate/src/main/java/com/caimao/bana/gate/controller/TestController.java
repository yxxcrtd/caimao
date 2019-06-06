/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.gate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 期货交易api
 *
 * @author yanjg
 *         2015年2月15日
 */
@Controller
@RequestMapping(value = "/account", method = {RequestMethod.POST, RequestMethod.GET})
public class TestController {

    @RequestMapping("/{operate}")
    public String all(@PathVariable String operate) {
        return "account/account_" + operate;
    }

    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("大公猫");
        return "account/account_add";
    }

}
