/*
*ChargeController.java
*Created on 2015/5/11 16:05
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.zeus.controller.optionalStock;

import com.caimao.bana.api.entity.OptionalStockEntity;
import com.caimao.bana.api.service.IOptionalStockService;
import com.caimao.bana.api.service.IUserService;
import com.caimao.zeus.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 自选股
 */
@Controller
@RequestMapping(value = "/optionalStock")
public class OptionalStockController extends BaseController{

    @Resource
    private IOptionalStockService optionalStockService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView stockList() throws Exception {
        List<OptionalStockEntity> optionalStockList = optionalStockService.queryStockByUserId(0L);
        return new ModelAndView("optionalStock/list", "optionalStockList", optionalStockList);
    }

    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    public ModelAndView stockSort(HttpServletRequest request, @RequestParam(value = "id") Long id, @RequestParam(value = "sort") Long sort) throws Exception {
        optionalStockService.sortStock(0L, id, sort);
        return jumpForSuccess(request, "排序成功", "/optionalStock/list");
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public ModelAndView stockDel(HttpServletRequest request, @RequestParam(value = "id") Long id) throws Exception {
        optionalStockService.deleteStock(0L, id);
        return jumpForSuccess(request, "删除成功", "/optionalStock/list");
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView stockAdd(HttpServletRequest request, @RequestParam(value = "stockCode") String stockCode) throws Exception {
        stockCode = stockCode.trim();
        String stockName = optionalStockService.queryStockNameFromSina(stockCode);
        if(stockName == null){
            return jumpForFail(request, "股票不存在", "/optionalStock/list");
        }else{
            optionalStockService.insertStock(0L, stockCode, stockName, 0L);
            return jumpForSuccess(request, "添加成功", "/optionalStock/list");
        }
    }
}
