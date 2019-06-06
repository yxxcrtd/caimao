/*
*HeepayController.java
*Created on 2015/4/23 16:08
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.controller;

import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.service.IUserService;
import com.hsnet.pz.ao.member.IMemberAO;
import com.hsnet.pz.controller.BaseController;
import com.hsnet.pz.core.exception.BizServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/specialInterface")
public class SpecialInterfaceController extends BaseController {
    @Autowired
    IMemberAO memberAO;

    @Resource
    private IUserService userService;

    @RequestMapping(value = "/isMobileExist", method = RequestMethod.GET)
    @ResponseBody
    public String isMobileExist(@RequestParam("mobile") String mobile) {
        memberAO.isMobileExist(mobile);
        return "1";
    }

    @RequestMapping(value = "/getCaimaoId", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Long getUserCaimaoId(){
        try {
            TpzUserEntity tpzUserEntity = userService.getById(getSessionUser().getUser_id());
            return tpzUserEntity.getCaimaoId();
        } catch (Exception e) {
            throw new BizServiceException("830408", "用户不存在");
        }
    }
}
