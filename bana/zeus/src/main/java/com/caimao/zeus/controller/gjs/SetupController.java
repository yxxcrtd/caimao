package com.caimao.zeus.controller.gjs;

import com.caimao.gjs.api.entity.GjsHolidayEntity;
import com.caimao.gjs.api.entity.req.FQueryGjsHolidayReq;
import com.caimao.gjs.api.service.IGjsHolidayService;
import com.caimao.gjs.api.utils.DateUtil;
import com.caimao.zeus.admin.controller.BaseController;
import com.caimao.zeus.util.RedisUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统设置
 */
@RestController
@RequestMapping("/setup")
public class SetupController extends BaseController {

    @Resource
    private IGjsHolidayService gjsHolidayService;

    @Resource
    private RedisUtils redisUtils;

    private String redisMainKey = "_gjsExchangeTradeTime";

    /**
     * 节假日列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "holiday", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView holiday(FQueryGjsHolidayReq req) throws Exception {
        ModelAndView mav = new ModelAndView("admin/holidayList");
        // 没有指定节假日，就查询从今天以后的50条数据
        if (req.getHoliday() == null) {
            req.setBeginHoliday(DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, new Date()));
        }
        req.setLimit(50);
        req.setCurrentPage(req.getCurrentPage());
        req = this.gjsHolidayService.queryGjsHolidayList(req);
        mav.addObject("list", req);
        mav.addObject("exchangeMap", getExchangeMap());

        mav.addObject("njsHours", this.redisUtils.hGet(0, this.redisMainKey, "njsHours"));
        mav.addObject("sjsHours", this.redisUtils.hGet(0, this.redisMainKey, "sjsHours"));

        return mav;
    }

    /**
     * 节假日编辑
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/holidayEdit", method = RequestMethod.GET)
    public ModelAndView holidayEdit(@RequestParam(value = "id") long id) throws Exception {
        ModelAndView mav = new ModelAndView();
        GjsHolidayEntity entity = null;
        if (0 == id) {
            entity = new GjsHolidayEntity();
            entity.setId(id);
            entity.setOptDate(DateUtil.convertDateToString(new Date()));
        } else {
            entity = this.gjsHolidayService.selectById(id);
        }
        mav.addObject("entity", entity);
        mav.addObject("exchangeMap", getExchangeMap());
        mav.setViewName("admin/holidayEdit");
        return mav;
    }

    /**
     * 节假日设置保存
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/holidaySave", method = {RequestMethod.POST, RequestMethod.GET})
    public Map<String, Object> holidaySave(
            @RequestParam(value = "id") long id,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "holiday") String holiday,
            @RequestParam(value = "tradeTime") String tradeTime,
            HttpServletRequest request) throws Exception {

        Map<String, Object> res = new HashMap<>();

        if (null == holiday || "".equals(holiday)) {
            res.put("result", false);
            res.put("msg", "日期不能为空！");
            return res;
            //return jumpForFail(request, "日期不能为空！", "/setup/holidayEdit?id=" + id);
        }
        if (null == tradeTime || "".equals(tradeTime)) {
            res.put("result", false);
            res.put("msg", "时间段不能为空！");
            return res;
            //return jumpForFail(request, "时间段不能为空！", "/setup/holidayEdit?id=" + id);
        }
        GjsHolidayEntity entity = new GjsHolidayEntity();
        entity.setExchange(exchange);
        entity.setHoliday(holiday);
        entity.setTradeTime(tradeTime);
        entity.setOptDate(DateUtil.getFormatDate(DateUtil.DATA_TIME_PATTERN_1));
        if (0 < id) {
            entity.setId(id);
            this.gjsHolidayService.update(entity);
        } else {
            this.gjsHolidayService.insert(entity);
        }
        res.put("result", true);
        res.put("msg", "");
        return res;
        //return jumpForSuccess(request, "保存成功", "/setup/holiday");
    }

    /**
     * 保存贵金属交易所交易时常的设置
     * @param njsHours
     * @param sjsHours
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save_trade_time")
    public ModelAndView saveExchangeTradeTime(
            @RequestParam(value = "njsHours") String njsHours,
            @RequestParam(value = "sjsHours") String sjsHours,
            HttpServletRequest request
    ) throws Exception {
        this.redisUtils.hSet(0, this.redisMainKey, "njsHours", njsHours);
        this.redisUtils.hSet(0, this.redisMainKey, "sjsHours", sjsHours);
        return jumpForSuccess(request, "保存成功", "/setup/holiday");
    }


    /**
     * 删除节假日
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "del", method = RequestMethod.GET)
    public ModelAndView holidayDelete(@RequestParam(value = "id") Long id, HttpServletRequest request) throws Exception {
        this.gjsHolidayService.deleteById(id);
        return jumpForSuccess(request, "删除成功", "/setup/holiday");
    }

}