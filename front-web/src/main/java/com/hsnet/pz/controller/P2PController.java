/**
 * @Title P2PController.java 
 * @Package com.hsnet.pz.controller 
 * @Description 
 * @author miyb  
 * @date 2015-1-1 上午9:57:57 
 * @version V1.0   
 */
package com.hsnet.pz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hsnet.pz.ao.p2p.IP2PAO;
import com.hsnet.pz.biz.p2p.dto.req.F830501Req;
import com.hsnet.pz.biz.p2p.dto.req.F830503Req;
import com.hsnet.pz.biz.p2p.dto.req.F830505Req;
import com.hsnet.pz.biz.p2p.dto.res.F830502Res;
import com.hsnet.pz.biz.p2p.dto.res.F830506Res;

/** 
 * @author: miyb 
 * @since: 2015-1-1 上午9:57:57 
 * @history:
 */
//@Controller
//@RequestMapping(value = "/p2p")
public class P2PController extends BaseController {
    @Autowired
    private IP2PAO p2pAO;

    @RequestMapping(value = "/subject/pay", method = RequestMethod.POST)
    @ResponseBody
    public boolean doPayInvs(@RequestParam("invs_id") Long invsId,
            @RequestParam("pay_amount") Long payAmount,
            @RequestParam("trade_pwd") String tradePwd) {
        p2pAO.doPayInvs(getSessionUser().getUser_id(), invsId, payAmount,
            tradePwd);
        return true;
    }

    @RequestMapping(value = "/subject/page", method = RequestMethod.GET)
    @ResponseBody
    public F830501Req queryInvs(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "rate", required = false) String rate,
            @RequestParam(value = "during", required = false) String during,
            @RequestParam(value = "date_publish", required = false) String datePublish,
            @RequestParam(value = "is_able_pay", required = false) Integer isAblePay,
            @RequestParam(value = "order_first_column", required = false) String orderFirstColumn,
            @RequestParam(value = "order_first_dir", required = false) String orderFirstDir,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
        return p2pAO.queryInvs(status, rate, during, datePublish, isAblePay,
            orderFirstColumn, orderFirstDir, start, limit);
    }
    
    @RequestMapping(value = "/subject/personalInvs", method = RequestMethod.GET)
    @ResponseBody
    public F830505Req queryPersonalInvs(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "rate", required = false) String rate,
            @RequestParam(value = "during", required = false) String during,
            @RequestParam(value = "date_publish", required = false) String datePublish,
            @RequestParam(value = "is_able_pay", required = false) Integer isAblePay,
            @RequestParam(value = "order_first_column", required = false) String orderFirstColumn,
            @RequestParam(value = "order_first_dir", required = false) String orderFirstDir,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
    	return p2pAO.queryMyInvs(getSessionUser().getUser_id(), status.trim().equals("") ? null : status.trim(), start, limit);
    }

    @RequestMapping(value = "/subject/detail", method = RequestMethod.GET)
    @ResponseBody
    public F830502Res getInvsById(@RequestParam("id") Long invsId) {
        return p2pAO.getInvsById(invsId);
    }

    @RequestMapping(value = "/subject/top", method = RequestMethod.GET)
    @ResponseBody
    public List<F830506Res> queryInvsTop(
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate,
            @RequestParam(value = "order_column", required = false) String orderColumn,
            @RequestParam(value = "order_dir", required = false) String orderDir,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
        return p2pAO.queryInvrTop(startDate, endDate, orderColumn, orderDir,
            start, limit);
    }

    @RequestMapping(value = "/payuser", method = RequestMethod.GET)
    @ResponseBody
    public F830503Req queryPayUserByInvsId(
            @RequestParam("invs_id") Long invsId,
            @RequestParam("start") int start, @RequestParam("limit") int limit) {
        return p2pAO.queryPayUserByInvsId(invsId, start, limit);
    }

    @RequestMapping(value = "/payed/page", method = RequestMethod.GET)
    @ResponseBody
    public F830505Req queryMyInvs(@RequestParam("status") String status,
            @RequestParam("start") int start, @RequestParam("limit") int limit) {
        return p2pAO.queryMyInvs(getSessionUser().getUser_id(), status, start,
            limit);
    }
}
