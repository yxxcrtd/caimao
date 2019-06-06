package com.caimao.bana.controller.other;

import com.caimao.bana.api.entity.zeus.ZeusProhibitStockEntity;
import com.caimao.bana.api.service.content.IContentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 各个协议
 * Created by xavier on 15/7/7.
 */
@Controller
@RequestMapping(value = "/protocol")
public class ProtocolController {

    @Resource
    private IContentService contentService;

    // 借贷融资的协议
    @RequestMapping(value = "/loan")
    public ModelAndView loan() {
        return new ModelAndView("/protocol/loan");
    }

    // P2P的协议
    @RequestMapping(value = "/p2p")
    public ModelAndView p2p() {
        return new ModelAndView("/protocol/p2p");
    }

    // 注册协议
    @RequestMapping(value = "/regiester")
    public ModelAndView regiester() {
        return new ModelAndView("/protocol/regiester");
    }

    // 今日禁买股
    @RequestMapping(value = "/unableStock")
    public ModelAndView unableStock() throws Exception {
        ModelAndView mav = new ModelAndView("/protocol/unableStock");
        List<ZeusProhibitStockEntity> prohStockList = this.contentService.listProhibitStock();
        mav.addObject("list", prohStockList);
        return mav;
    }
}
