package com.caimao.zeus.controller.ybk;

import com.caimao.bana.api.entity.res.ybk.FYbkNavigationRes;
import com.caimao.bana.api.entity.ybk.YbkNavigationEntity;
import com.caimao.bana.api.service.ybk.IYbkNavigationService;
import com.caimao.zeus.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 邮币卡导航控制器
 */
@Controller
@RequestMapping("/ybk/navigation")
public class YBKNavigationController extends BaseController {
    @Resource
    private IYbkNavigationService ybkNavigationService;

    /**
     * 导航列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() throws Exception {
        ModelAndView mav = new ModelAndView("ybk/navigation/list");
        List<YbkNavigationEntity> navigationRes = this.ybkNavigationService.selectAllStr();

        mav.addObject("list", navigationRes);
        return mav;
    }


    /**
     * 保存，修改
     * @param id
     * @param name
     * @param urls
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "name") String name,
                             @RequestParam(value = "urls") String urls,
                             HttpServletRequest request) throws Exception {

        YbkNavigationEntity entity = new YbkNavigationEntity();
        entity.setName(name);
        entity.setUrls(urls);
        if (id != null && id != 0) {
            entity.setId(id);
            this.ybkNavigationService.update(entity);
        } else {
            entity.setCreated(new Date());
            this.ybkNavigationService.insert(entity);
        }
        return jumpForSuccess(request, "保存、修改成功", "/ybk/navigation/list");
    }

    /**
     * 删除
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public ModelAndView del(@RequestParam(value = "id") Integer id, HttpServletRequest request) throws Exception {
        this.ybkNavigationService.deleteById(id);
        return jumpForSuccess(request, "删除成功", "/ybk/navigation/list");
    }

}
