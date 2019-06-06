/**
 * @Title SpreadController.java 
 * @Package com.hsnet.pz.controller 
 * @Description 
 * @author miyb  
 * @date 2015-1-14 下午2:33:08 
 * @version V1.0   
 */
package com.hsnet.pz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hsnet.pz.ao.spread.ISpreadAO;

/** 
 * @author: miyb 
 * @since: 2015-1-14 下午2:33:08 
 * @history:
 */
@Controller
@RequestMapping(value = "/spread")
public class SpreadController {
    @Autowired
    private ISpreadAO spreadAO;

//    @RequestMapping(value = "/usercount", method = RequestMethod.GET)
//    @ResponseBody
//    public int getWebUserCount() {
//        return spreadAO.getWebUserCount();
//    }
}
