package com.caimao.zeus.controller.content;

import com.caimao.bana.api.entity.content.BanaNoticeEntity;
import com.caimao.bana.api.entity.req.content.FNoticeQueryListReq;
import com.caimao.bana.api.service.content.INoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 公告列表
 * Created by WangXu on 2015/6/18.
 */
@Controller
@RequestMapping("/content/notice")
public class NoticeController {

    private Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Resource
    private INoticeService noticeService;

    // 公告的列表
    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page) throws Exception {
        ModelAndView mav = new ModelAndView("content/notice/list");
        // 获取列表
        Integer limit = 20;
        Integer start = (page - 1) * 20;
        FNoticeQueryListReq req = new FNoticeQueryListReq();
        req.setStart(start);
        req.setLimit(limit);
        req.setOrderColumn("created");
        req.setOrderDir("DESC");

        req = this.noticeService.queryList(req);
        mav.addObject("req", req);
        return mav;
    }

    // 添加公告的控制器
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ModelAndView save() throws Exception {
        ModelAndView mav = new ModelAndView("content/notice/save");
        return mav;
    }
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RedirectView save(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "source") String source,
            @RequestParam(value = "source_href") String sourceHref,
            @RequestParam(value = "list_show") Integer listShow,
            @RequestParam(value = "top_show") Integer topShow
    ) throws Exception {
        BanaNoticeEntity entity = new BanaNoticeEntity();
        entity.setTitle(title);
        entity.setContent(content);
        entity.setSource(source);
        entity.setSourceHref(sourceHref);
        entity.setCreated(new Date());
        entity.setListShow(listShow);
        entity.setTopShow(topShow);
        this.noticeService.save(entity);

        return new RedirectView("/content/notice/list");
    }

    // 修改公告
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam(value = "id") Long id) throws Exception {
        ModelAndView mav = new ModelAndView("/content/notice/edit");
        BanaNoticeEntity entity = this.noticeService.queryById(id);

        mav.addObject("noticeInfo", entity);
        return mav;
    }
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RedirectView edit(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "source") String source,
            @RequestParam(value = "source_href") String sourceHref,
            @RequestParam(value = "list_show") Integer listShow,
            @RequestParam(value = "top_show") Integer topShow
    ) throws Exception {
        BanaNoticeEntity entity = new BanaNoticeEntity();
        entity.setId(id);
        entity.setTitle(title);
        entity.setContent(content);
        entity.setSource(source);
        entity.setSourceHref(sourceHref);
        entity.setListShow(listShow);
        entity.setTopShow(topShow);
        this.noticeService.update(entity);

        return new RedirectView("/content/notice/list");
    }




}
