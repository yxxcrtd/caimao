/**
 * @Title RZAccountController.java
 * @Package com.hsnet.pz
 * @Description
 * @author miyb
 * @date 2014-12-16 下午7:45:13 
 * @version V1.0
 */
package com.hsnet.pz.controller;

import com.hsnet.pz.ao.account.IAccountAO;
import com.hsnet.pz.ao.infrastructure.IBasicDataAO;
import com.hsnet.pz.biz.account.dto.req.F830103Req;
import com.hsnet.pz.biz.pay.dto.req.F830407Req;
import com.hsnet.pz.biz.pay.dto.req.F830410Req;
import com.hsnet.pz.biz.pay.dto.res.F830408Res;
import com.hsnet.pz.biz.pay.dto.res.F830415Res;
import com.hsnet.pz.biz.pay.util.YiPayRSAUtil;
import com.hsnet.pz.biz.user.dto.res.F830018Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: miyb
 * @since: 2014-12-16 下午7:45:13
 * @history:
 */
//@Controller
//@RequestMapping(value = "/account")
public class RZAccountController extends BaseController {

    @Autowired
    private IAccountAO accountAO;

    @Autowired
    IBasicDataAO basicDataAO;

    // *********充值 start****

    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    @ResponseBody
    public F830408Res doCharge(
            @RequestParam("pay_company_no") Long payCompanyNo,
            @RequestParam(value = "bank_no", required = false) String bankNo,
            @RequestParam("charge_amount") Long chargeAmount,
            @RequestParam("terminal_type") String terminalType,
            @RequestParam("pay_type") Integer payType,
            @RequestParam(value = "charge_sync_url", required = false) String chargeSyncUrl,
            @RequestParam(value = "order_abstract", required = false) String orderAbstract) {
        return accountAO.doCharge(getSessionUser().getUser_id(), payCompanyNo,
                bankNo, chargeAmount, terminalType, payType, chargeSyncUrl,
                orderAbstract);
    }

    @RequestMapping(value = "/againCharge", method = RequestMethod.POST)
    @ResponseBody
    public F830415Res doAgainCharge(
            @RequestParam("no") Long no,
            @RequestParam(value = "terminal_type", required = false) String terminalType,
            @RequestParam(value = "charge_sync_url", required = false) String chargeSyncUrl) {
        return accountAO.doAgainCharge(getSessionUser().getUser_id(), no, terminalType, chargeSyncUrl);
    }

    @RequestMapping(value = "/charge/page", method = RequestMethod.GET)
    @ResponseBody
    public F830407Req queryChargeOrder(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
        return accountAO.queryChargeOrder(getSessionUser().getUser_id(),
                status, startDate, endDate, start, limit);
    }

    // *********充值 end****

    // *********取现 start****

//    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
//    @ResponseBody
//    public Long doWithdraw(@RequestParam("trade_pwd") String tradePwd,
//                           @RequestParam("withdraw_amount") long withdrawAmount) {
//        return accountAO.doWithdraw(getSessionUser().getUser_id(),
//                withdrawAmount, tradePwd);
//    }

    @RequestMapping(value = "/yiwithdraw/pre", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> doPreYiWithdraw(
            @RequestParam("trade_pwd") String tradePwd,
            @RequestParam("withdraw_amount") long withdrawAmount) {
        F830018Res user = getUser();
        return accountAO.doPreYiWithdraw(user.getUserId(),
                user.getUserRealName(), withdrawAmount, tradePwd);
    }

    @RequestMapping(value = "/yiwithdraw", method = RequestMethod.POST)
    public String doYiWithdraw(@RequestParam("jyjr") String jyjr,
                               @RequestParam("tk") String tk) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("jyjr", jyjr);
        map.put("tk", tk);
        try {
            map = YiPayRSAUtil.checkDate(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("1".equalsIgnoreCase(map.get("status"))) {
            accountAO.doYiWithdraw(map.get("ddhm")).get("withdrawSyncUrl");
        }
        return "home/index";
    }

    @RequestMapping(value = "/withdraw/page", method = RequestMethod.GET)
    @ResponseBody
    public F830410Req queryWithdrawOrder(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
        return accountAO.queryWithdrawOrder(getSessionUser().getUser_id(),
                status, startDate, endDate, start, limit);
    }

    // *********取现 end****

    // *********资金流水 start****
    @RequestMapping(value = "jour/page", method = RequestMethod.GET)
    @ResponseBody
    public F830103Req queryRZAccountJour(
            @RequestParam(value = "biz_type", required = false) String bizType,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate,
            @RequestParam("start") int start,
            @RequestParam(value = "limit", required = false) int limit) {
        return accountAO.queryRZAccountJour(getSessionUser().getUser_id(),
                bizType, startDate, endDate, start, limit);
    }

    // *********资金流水 end****

}
