package com.hsnet.pz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hsnet.pz.ao.message.IMessageAO;
import com.hsnet.pz.biz.base.dto.req.F830904Req;
import com.hsnet.pz.biz.base.entity.PushMsgContent;

@Controller
@RequestMapping(value = "/msg")
public class MessageController extends BaseController {

    @Autowired
    private IMessageAO messageAO;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public F830904Req queryPaginableMsg(
            @RequestParam("push_type") String pushType,
            @RequestParam(value="start_date",required=false) String startDate,
            @RequestParam(value="end_date",required=false) String endDate,
            @RequestParam("start") int start, @RequestParam("limit") int limit,
            @RequestParam(value="isRead",required=false) String isRead) {
        return messageAO.queryPushMsg(getSessionUser().getUser_id(), pushType,
            startDate, endDate, start, limit, isRead);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public PushMsgContent getMsgContent(@RequestParam("msg_no") Long msgNo) {
        messageAO.doSetRead(msgNo);
        return messageAO.getPushMsgContent(msgNo);
    }
}
