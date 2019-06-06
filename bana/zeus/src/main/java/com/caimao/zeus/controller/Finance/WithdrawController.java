package com.caimao.zeus.controller.Finance;

import com.caimao.bana.api.entity.req.F831411Req;
import com.caimao.bana.api.entity.res.F831411Res;
import com.caimao.bana.api.enums.EOrderStatus;
import com.caimao.bana.api.enums.EVerifyStatus;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IHeepayWithdraw;
import com.caimao.bana.api.service.IZeusStatisticsService;
import com.caimao.zeus.entity.AjaxResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/finance/withdraw")
public class WithdrawController {
    private static final String WITHDRAW_LIST_MODEL_NAME = "withdrawList";
    private static final String F831411REQ_MODEL_NAME = "f831411req";
    private static final String VERIFY_STATUS = "verify_status";
    private static final String ORDER_STATUS = "order_status";
    private static final Logger logger = LoggerFactory.getLogger(WithdrawController.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    private IHeepayWithdraw heepayWithdraw;
    @Resource
    private IZeusStatisticsService zeusStatistics;

    @RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView list(@ModelAttribute(F831411REQ_MODEL_NAME) F831411Req f831411Req, Model model) throws Exception {
        if (StringUtils.isEmpty(f831411Req.getCreateDatetimeBegin())) {
            f831411Req.setCreateDatetimeBegin(sdf.format(new Date()));
        }

        if (StringUtils.isEmpty(f831411Req.getCreateDatetimeEnd())) {
            f831411Req.setCreateDatetimeEnd(sdf.format(new Date()));
        }

        List<F831411Res> f831411ResList = heepayWithdraw.queryWithdrawOrders(f831411Req);

        if(f831411ResList != null){
            for(F831411Res f831411Res:f831411ResList){
                if(!f831411Res.getOrderStatus().equals("02")) continue;
                //查询充值总计
                Long totalDeposit = zeusStatistics.queryUserDepositTotal(f831411Res.getUserId());
                f831411Res.setTotalDeposit(totalDeposit);
                if(totalDeposit == null) f831411Res.setTotalDeposit(0L);
                //查询提现总计
                Long totalWithdraw = zeusStatistics.queryUserWithdrawTotal(f831411Res.getUserId());
                f831411Res.setTotalWithdraw(totalWithdraw);
                if(totalWithdraw == null) f831411Res.setTotalWithdraw(0L);
                //查询最后一笔提现成功的时间 如果没有即为所有记录
                Date searchDate = zeusStatistics.queryLastWithdrawSuccess(f831411Res.getUserId());
                if(searchDate == null) searchDate = new Date(1342772212000L);
                //查询在一定时间段内流水是否有位数不正确的记录
                Long totalBad = zeusStatistics.queryIsHasBadJour(f831411Res.getUserId(), searchDate, new Date());
                f831411Res.setTotalBad(totalBad);
                if(totalBad == null) f831411Res.setTotalBad(0L);
            }
        }

        model.addAttribute(WITHDRAW_LIST_MODEL_NAME, f831411ResList);
        model.addAttribute(F831411REQ_MODEL_NAME, f831411Req);
        model.addAttribute(VERIFY_STATUS, EVerifyStatus.values());
        model.addAttribute(ORDER_STATUS, EOrderStatus.values());
        return new ModelAndView("finance/withdraw/list");
    }

    @RequestMapping(value = "/payOnline", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse withdrawPayOnline(@RequestParam Long orderNo) throws Exception {
        try {
            heepayWithdraw.doWithdraw(orderNo);
            return AjaxResponse.OK;
        } catch (Exception e) {
            if (e instanceof CustomerException) {
                return new AjaxResponse(((CustomerException) e).getCode(), e.getMessage());
            }
            logger.error("支付过程发生错误, error:{}", e.getMessage());
            return AjaxResponse.FAILED;
        }
    }

    @RequestMapping(value = "/checkStatus", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse withdrawPayOnlineStatus(@RequestParam Long orderNo) throws Exception {
        try {
            Map<String, Object> resultMap = heepayWithdraw.doWithdrawQuery(orderNo);
            if (Boolean.getBoolean(resultMap.get("result").toString())) {
                return AjaxResponse.OK;
            }
            return new AjaxResponse(-1, resultMap.get("msg").toString());
        } catch (Exception e) {
            if (e instanceof CustomerException) {
                return new AjaxResponse(((CustomerException) e).getCode(), e.getMessage());
            }
            logger.error("查询过程发生错误, error:{}", e.getMessage());
            return AjaxResponse.FAILED;
        }
    }
}
