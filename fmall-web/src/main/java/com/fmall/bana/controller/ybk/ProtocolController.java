package com.fmall.bana.controller.ybk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 各个协议
 * Created by xavier on 15/7/7.
 */
@Controller
@RequestMapping(value = "/ybk/protocol")
public class ProtocolController {

    // 借贷融资的协议
    @RequestMapping(value = "/ybk")
    public ModelAndView loan() {
        return new ModelAndView("/ybk/protocol/ybk");
    }

    // QQ群的东西
    @RequestMapping(value = "/qq")
    public ModelAndView qq() {
        return new ModelAndView("/ybk/protocol/qq");
    }

}
