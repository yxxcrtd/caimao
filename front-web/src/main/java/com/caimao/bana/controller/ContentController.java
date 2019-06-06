package com.caimao.bana.controller;

import com.caimao.bana.api.entity.content.BanaNoticeEntity;
import com.caimao.bana.api.entity.req.content.FMsgQueryListReq;
import com.caimao.bana.api.entity.req.content.FNoticeQueryListReq;
import com.caimao.bana.api.entity.res.content.FNoticeInfoRes;
import com.caimao.bana.api.service.content.IMessageService;
import com.caimao.bana.api.service.content.INoticeService;
import com.caimao.bana.utils.PageUtils;
import com.hsnet.pz.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 网址内容相关控制器 Created by WangXu on 2015/6/18.
 */
@Controller
@RequestMapping("/content")
public class ContentController extends BaseController {

    @Resource
    private INoticeService noticeService;
    @Resource
    private IMessageService messageService;

    @RequestMapping(value = "/notice/{id}", method = RequestMethod.GET)
    public ModelAndView notice(@PathVariable(value = "id") Long id) throws Exception {
        ModelAndView mav = new ModelAndView("content/notice");
        BanaNoticeEntity notice = noticeService.queryById(id);
        FNoticeQueryListReq req = new FNoticeQueryListReq();
        req.setListShow(1);
        //req.setTopShow(0);
        req.setStart(0);
        req.setLimit(20);
        req.setOrderColumn("created");
        req.setOrderDir("DESC");
        req = this.noticeService.queryList(req);
        mav.addObject("notice", notice);
        mav.addObject("list", req.getItems());
        return mav;
    }

    // 获取置顶的公告
    @ResponseBody
    @RequestMapping(value = "/top_notice", method = RequestMethod.GET)
    public FNoticeInfoRes getTopNotice() throws Exception {
        FNoticeQueryListReq req = new FNoticeQueryListReq();
        req.setListShow(1);
        req.setTopShow(1);
        req.setStart(0);
        req.setLimit(1);
        req.setOrderColumn("created");
        req.setOrderDir("DESC");
        req = this.noticeService.queryList(req);
        return req.getItems().get(0);
    }

    // 查询置顶ID的公告信息
    @ResponseBody
    @RequestMapping(value = "/notice_info", method = RequestMethod.GET)
    public BanaNoticeEntity getNoticeInfo(@RequestParam(value = "id") Long id) throws Exception {
        return this.noticeService.queryById(id);
    }

    // 获取未读的数量
    @ResponseBody
    @RequestMapping(value = "/notread_msg_num", method = RequestMethod.GET)
    public Integer getNotReadMsgNum() throws Exception {
        return this.messageService.getNotReadNum(this.getSessionUser().getUser_id());
    }

    //变革所有消息为已读
    @ResponseBody
    @RequestMapping(value = "/msg_read_all", method = RequestMethod.GET)
    public void msgReadAll() throws Exception {
        this.messageService.msgReadAll(this.getSessionUser().getUser_id());
    }

    // 获取消息列表
    @RequestMapping(value = "/msg_list.html", method = RequestMethod.GET)
    public ModelAndView queryMsgList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception {
        ModelAndView mav = new ModelAndView("/content/dialog_msg");
        Integer limit = 3;
        FMsgQueryListReq req = new FMsgQueryListReq();
        req.setStart((page - 1) * limit);
        req.setLimit(limit);

        req.setPushUserId(this.getSessionUser().getUser_id().toString());
        req = this.messageService.queryMsgList(req);

        if(req.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(), String.format("/content/msg_list.html?page=%s", ""));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }

        mav.addObject("msg", req);
        // 未读的数量
        mav.addObject("notReadNum", this.messageService.getNotReadNum(this.getSessionUser().getUser_id()));

        return mav;
    }
}
