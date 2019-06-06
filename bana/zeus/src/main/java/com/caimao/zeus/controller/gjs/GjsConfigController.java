package com.caimao.zeus.controller.gjs;

import com.caimao.bana.common.api.enums.gjs.EGJSExchange;
import com.caimao.gjs.api.entity.GJSProductEntity;
import com.caimao.gjs.api.service.ITradeJobService;
import com.caimao.gjs.api.service.ITradeManageService;
import com.caimao.gjs.api.service.ITradeService;
import com.caimao.hq.api.service.IGJSHqJobService;
import com.caimao.hq.api.service.IHQService;
import com.caimao.zeus.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 贵金属配置
 */
@Controller
@RequestMapping("/gjs/config")
public class GjsConfigController extends BaseController{

    @Resource
    private ITradeService tradeService;
    @Resource
    private ITradeJobService tradeJobService;
    @Resource
    private IGJSHqJobService gjsHqJobService;
    @Resource
    private ITradeManageService iTradeManageService;

    @RequestMapping(value = "/productList", method = RequestMethod.GET)
    public ModelAndView productList(
            @RequestParam(value = "exchange", required = false, defaultValue = "NJS") String exchange
    ) throws Exception {
        ModelAndView mav = new ModelAndView("gjs/config/productList");
        if(exchange == null) exchange = "NJS";
        List<GJSProductEntity> list = tradeService.queryProductList(exchange);
        mav.addObject("list", list);
        return mav;
    }

    @RequestMapping(value = "/productOp", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView productOp(@RequestParam(value = "id") Long id,
                             @RequestParam(value = "sort", required = false) Integer sort,
                             HttpServletRequest request) throws Exception{
        GJSProductEntity gjsProductEntity = new GJSProductEntity();
        gjsProductEntity.setSort(sort);
        gjsProductEntity.setProductId(id);
        tradeService.updateProduct(gjsProductEntity);
        return jumpForSuccess(request, "操作成功", "/gjs/config/productList");
    }


    @RequestMapping(value = "/productUpdateCache", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView productUpdateCache(HttpServletRequest request) throws Exception{
        tradeJobService.updateGoods();
        gjsHqJobService.updateProduct();
        return jumpForSuccess(request, "操作成功", "/gjs/config/productList");
    }

    @RequestMapping(value = "/removeGJSAccount", method = RequestMethod.GET)
    public ModelAndView removeGJSAccountView() throws Exception{
        return new ModelAndView("gjs/config/removeGJSAccount");
    }

    @RequestMapping(value = "/removeGJSAccount", method = RequestMethod.POST)
    public ModelAndView removeGJSAccount(
            HttpServletRequest request,
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "traderId") String traderId
    ) throws Exception{
        iTradeManageService.removeAccount(userId, exchange, traderId);
        return jumpForSuccess(request, "操作成功", "/gjs/config/removeGJSAccount");
    }

    @RequestMapping(value = "/historyDataUpdate", method = RequestMethod.GET)
    public ModelAndView historyDataUpdate() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(System.currentTimeMillis() - 86400 * 1000));
        return new ModelAndView("gjs/config/historyDataUpdate", "date", date);
    }

    @RequestMapping(value = "/historyDataUpdate", method = RequestMethod.POST)
    public ModelAndView historyDataUpdate(
            HttpServletRequest request,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "dataType") String dataType,
            @RequestParam(value = "date") String date
    ) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date paramDate;
        try{
            paramDate = sdf.parse(date);
        }catch(Exception e){
            return jumpForFail(request, "日期格式不正确，请输入 2015-12-15", "/gjs/config/historyDataUpdate");
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        if(exchange.equals(EGJSExchange.NJS.getCode())){
            tradeJobService.parseNJSHistory(dataType, sdf2.format(paramDate));
        }
        return jumpForSuccess(request, "更新成功", "/gjs/config/historyDataUpdate");
    }
}
