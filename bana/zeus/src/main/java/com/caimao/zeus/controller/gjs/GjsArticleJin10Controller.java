package com.caimao.zeus.controller.gjs;

import com.caimao.gjs.api.entity.article.GjsArticleJin10Entity;
import com.caimao.gjs.api.entity.req.FQueryGjsArticleJin10Req;
import com.caimao.gjs.api.service.IArticleService;
import com.caimao.zeus.admin.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 实时快讯管理
 */
@RestController
@RequestMapping("gjs/articleJin10")
public class GjsArticleJin10Controller extends BaseController {

    @Resource
    private IArticleService articleService;

    /**
     * 列表
     */
    @RequestMapping(value = "list", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView list(FQueryGjsArticleJin10Req req) throws Exception {
        ModelAndView mav = new ModelAndView("gjs/article/Jin10List");
        req.setLimit(50);
        req.setCurrentPage(req.getCurrentPage());
        req = articleService.queryGjsArticleJin10List(req);
        mav.addObject("list", req);
        return mav;
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView save(@RequestParam(value = "id", required = false) Long id) throws Exception {
        ModelAndView mav = new ModelAndView("gjs/article/Jin10Edit");
        if (null != id && 0 != id) {
            GjsArticleJin10Entity entity = this.articleService.getArticleJin10ById(id);
            mav.addObject("entity", entity);
        }
        return mav;
    }

    /**
     * 保存
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam(value = "id", required = false) Long id, GjsArticleJin10Entity entity, HttpServletRequest request) throws Exception {
        if (id != null && id != 0) {
            this.articleService.updateArticleJin10(entity);
        } else {
            entity.setTime(new StringBuffer(String.format("%1$tH:%1$tM", new Date())).toString());
            entity.setTimeId(new StringBuffer(String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL100", new Date())).toString());
            this.articleService.addArticleJin10(entity);
        }
        return jumpForSuccess(request, "保存成功", "/gjs/articleJin10/list");
    }

    /**
     * 删除
     */
    @RequestMapping(value = "del", method = RequestMethod.GET)
    public String del(@RequestParam(value = "id") Long id, HttpServletRequest request) throws Exception {
        this.articleService.deleteByArticleJin10Id(id);
        return "ok";
    }

}
