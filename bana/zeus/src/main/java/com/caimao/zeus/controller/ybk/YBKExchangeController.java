package com.caimao.zeus.controller.ybk;

import com.caimao.bana.api.entity.TpzBankTypeEntity;
import com.caimao.bana.api.entity.req.ybk.FYbkExchangeQueryListReq;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import com.caimao.bana.api.enums.EYbkExchangeStatus;
import com.caimao.bana.api.enums.EYbkTradeDayType;
import com.caimao.bana.api.service.IUserBankCardService;
import com.caimao.bana.api.service.ybk.IYBKService;
import com.caimao.zeus.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮币卡交易所相关操作控制器
 * Created by Administrator on 2015/9/7.
 */
@Controller
@RequestMapping(value = "/ybk/exchange")
public class YBKExchangeController extends BaseController {

    @Resource
    public IYBKService ybkService;
    @Resource
    public IUserBankCardService userBankCardService;

    /**
     * 邮币卡交易所列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "status", required = false, defaultValue = "") Integer status
    ) throws Exception {
        ModelAndView mav = new ModelAndView("ybk/exchange/list");
        FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
        req.setStatus(status);
        List<YbkExchangeEntity> ybkExchangeList = this.ybkService.queryExchangeList(req);
        HashMap<String, List<TpzBankTypeEntity>> exchangeBanks = new HashMap<>();
        if (ybkExchangeList != null) {
            for (YbkExchangeEntity ybk : ybkExchangeList) {
                List<TpzBankTypeEntity> bankList = new ArrayList<>();
                String[] bankNos = ybk.getSupportBank().split(",");
                for (String no : bankNos) {
                    bankList.add(this.userBankCardService.getBankInfoById(no, 3L));
                }
                exchangeBanks.put(ybk.getShortName(), bankList);
            }
        }
        Map<Integer, String> statusMap = new HashMap<>();
        for (EYbkExchangeStatus eYbkExchangeStatus : EYbkExchangeStatus.values()) {
            statusMap.put(eYbkExchangeStatus.getCode(), eYbkExchangeStatus.getValue());
        }
        Map<Integer, String> tradeDayTypeMap = new HashMap<>();
        for (EYbkTradeDayType eYbkTradeDayType : EYbkTradeDayType.values()) {
            tradeDayTypeMap.put(eYbkTradeDayType.getCode(), eYbkTradeDayType.getValue());
        }
        mav.addObject("exchangeBanks", exchangeBanks);
        mav.addObject("status", status);
        mav.addObject("statusMap", statusMap);
        mav.addObject("tradeDayTypeMap", tradeDayTypeMap);
        mav.addObject("exchangeList", ybkExchangeList);
        return mav;
    }

    /**
     * 添加邮币卡交易所的方法
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ModelAndView save() throws Exception {
        ModelAndView mav = new ModelAndView("ybk/exchange/save");
        Map<Integer, String> statusMap = new HashMap<>();
        for (EYbkExchangeStatus eYbkExchangeStatus : EYbkExchangeStatus.values()) {
            statusMap.put(eYbkExchangeStatus.getCode(), eYbkExchangeStatus.getValue());
        }
        Map<Integer, String> tradeDayTypeMap = new HashMap<>();
        for (EYbkTradeDayType eYbkTradeDayType : EYbkTradeDayType.values()) {
            tradeDayTypeMap.put(eYbkTradeDayType.getCode(), eYbkTradeDayType.getValue());
        }
        // 获取所有银行信息
        List<TpzBankTypeEntity> bankTypeEntityList = this.userBankCardService.queryBankList(3L);
        mav.addObject("bankList", bankTypeEntityList);
        mav.addObject("statusMap", statusMap);
        mav.addObject("tradeDayTypeMap", tradeDayTypeMap);
        return mav;
    }

    /**
     * 添加邮币卡交易所的方法
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(
            HttpServletRequest request,
            YbkExchangeEntity ybkExchange,
            @RequestParam(value = "supportBanks") String[] supportBanks) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : supportBanks) {
            stringBuffer.append(s + ",");
        }
        ybkExchange.setSupportBank(stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1));
        this.ybkService.addExchange(ybkExchange);
        return jumpForSuccess(request, "添加成功", "/ybk/exchange/list");
    }

    /**
     * 修改编辑交易所信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam(value = "id") Integer id) throws Exception {
        ModelAndView mav = new ModelAndView("ybk/exchange/edit");
        YbkExchangeEntity ybkExchange = this.ybkService.getExchangeById(id);
        mav.addObject("exchangeInfo", ybkExchange);


        Map<Integer, String> statusMap = new HashMap<>();
        for (EYbkExchangeStatus eYbkExchangeStatus : EYbkExchangeStatus.values()) {
            statusMap.put(eYbkExchangeStatus.getCode(), eYbkExchangeStatus.getValue());
        }
        Map<Integer, String> tradeDayTypeMap = new HashMap<>();
        for (EYbkTradeDayType eYbkTradeDayType : EYbkTradeDayType.values()) {
            tradeDayTypeMap.put(eYbkTradeDayType.getCode(), eYbkTradeDayType.getValue());
        }
        // 获取所有银行信息
        List<TpzBankTypeEntity> bankTypeEntityList = this.userBankCardService.queryBankList(3L);
        // 将支持的银行分开
        HashMap<String, String> supportBankMap = new HashMap<>();
        String[] supportBankList = ybkExchange.getSupportBank().split(",");
        for (String s : supportBankList) {
            supportBankMap.put(s, s);
        }
        mav.addObject("exchangeSupportBanks", supportBankMap);
        mav.addObject("bankList", bankTypeEntityList);
        mav.addObject("statusMap", statusMap);
        mav.addObject("tradeDayTypeMap", tradeDayTypeMap);
        return mav;
    }

    /**
     * 修改编辑交易所信息的方法
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit(
            HttpServletRequest request,
            YbkExchangeEntity ybkExchange,
            @RequestParam(value = "supportBanks", required = false) String[] supportBanks
    ) throws Exception {
        if (supportBanks != null) {
            StringBuffer stringBuffer = new StringBuffer();
            for (String s : supportBanks) {
                stringBuffer.append(s + ",");
            }
            ybkExchange.setSupportBank(stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1));
        }
        this.ybkService.updateExchange(ybkExchange);
        return jumpForSuccess(request, "编辑成功", "/ybk/exchange/list");
    }

    /**
     * 删除邮币卡交易所
     *
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public ModelAndView del(HttpServletRequest request, @RequestParam(value = "id") Integer id) throws Exception {
        this.ybkService.deleteExchange(id);
        return jumpForSuccess(request, "删除成功", "/ybk/exchange/list");
    }

}
