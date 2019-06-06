package com.caimao.zeus.controller.ybk;

import com.caimao.bana.api.entity.req.ybk.FQueryYbkHelpDocReq;
import com.caimao.bana.api.entity.ybk.YbkHelpDocEntity;
import com.caimao.bana.api.enums.ybk.EYbkHelpDocType;
import com.caimao.bana.api.service.ybk.IYbkHelpDocService;
import com.caimao.zeus.admin.controller.BaseController;
import com.caimao.zeus.util.PageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

/**
 * 邮币卡帮助文档控制器
 */
@Controller
@RequestMapping("/ybk/helpdoc")
public class YBKHelpDocController extends BaseController {
    @Resource
    private IYbkHelpDocService ybkHelpDocService;

    /**
     * 帮助文档列表
     *
     * @param categoryId
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "categoryId", required = false, defaultValue = "0") Integer categoryId,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) throws Exception {
        ModelAndView mav = new ModelAndView("ybk/helpdoc/list");
        FQueryYbkHelpDocReq req = new FQueryYbkHelpDocReq();
        Integer limit = 20;
        req.setStart((page - 1) * limit);
        req.setLimit(limit);
        if (categoryId == null) categoryId = 0;
        if (categoryId != 0) req.setCategoryId(categoryId);
        req = this.ybkHelpDocService.queryYbkHelpDocList(req);
        if (req.getItems() != null) {
            for (YbkHelpDocEntity entity : req.getItems()) {
                entity.setCategoryStr(EYbkHelpDocType.findByCode(entity.getCategoryId()).getValue());
            }
            PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(), String.format("/ybk/helpdoc/list?categoryId=%s&page=", categoryId));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }
        // 帮助分类
        HashMap<Integer, String> categoryList = new HashMap<>();
        for (EYbkHelpDocType type : EYbkHelpDocType.values()) {
            categoryList.put(type.getCode(), type.getValue());
        }
        mav.addObject("list", req);
        mav.addObject("categoryList", categoryList);
        mav.addObject("categoryId", categoryId);
        return mav;
    }

    /**
     * 帮助文档保存
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ModelAndView save(@RequestParam(value = "id", required = false) Integer id) throws Exception {
        ModelAndView mav = new ModelAndView("ybk/helpdoc/save");
        // 帮助分类
        HashMap<Integer, String> categoryList = new HashMap<>();
        for (EYbkHelpDocType type : EYbkHelpDocType.values()) {
            categoryList.put(type.getCode(), type.getValue());
        }
        mav.addObject("categoryList", categoryList);
        if (id != null && id != 0) {
            YbkHelpDocEntity entity = this.ybkHelpDocService.selectById(id);
            mav.addObject("articleDetail", entity);
        }
        return mav;
    }

    /**
     * 保存，修改
     *
     * @param id
     * @param categoryId
     * @param title
     * @param content
     * @param isShow
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "categoryId") Integer categoryId,
                             @RequestParam(value = "title") String title,
                             @RequestParam(value = "content") String content,
                             @RequestParam(value = "isShow") Integer isShow,
                             HttpServletRequest request) throws Exception {
        YbkHelpDocEntity entity = new YbkHelpDocEntity();
        entity.setCategoryId(categoryId);
        entity.setTitle(title);
        entity.setContent(content);
        entity.setIsShow(isShow);
        if (id != null && id != 0) {
            entity.setId(id);
            this.ybkHelpDocService.update(entity);
        } else {
            entity.setCreated(new Date());
            this.ybkHelpDocService.insert(entity);
        }
        return jumpForSuccess(request, "保存、修改成功", "/ybk/helpdoc/list");
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
    public ModelAndView del(@RequestParam(value = "id") Long id, HttpServletRequest request) throws Exception {
        this.ybkHelpDocService.deleteById(id);
        return jumpForSuccess(request, "删除成功", "/ybk/helpdoc/list");
    }

}
