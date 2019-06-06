package com.caimao.zeus.controller.guji;

import com.caimao.bana.api.service.guji.IGujiAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 股计的配置
 */
@RestController
@RequestMapping("/guji")
public class GujiConfigController {

    @Autowired
    private IGujiAdminService gujiAdminService;

    /**
     * 列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("config")
    public ModelAndView config() throws Exception {
        ModelAndView mav = new ModelAndView("/guji/Config");
        mav.addObject("list", gujiAdminService.queryAdminConfigList());
        return mav;
    }

    /**
     * 开关设置
     *
     * @param id
     * @param v
     * @return
     * @throws Exception
     */
    @RequestMapping("config/set")
    public String set(@RequestParam(value = "id") Long id, @RequestParam(value = "v") String v) throws Exception {
        return gujiAdminService.configSet(id, v);
    }

}
