package com.caimao.zeus.controller.homs;

import com.caimao.bana.api.entity.TpzAccountJourEntity;
import com.caimao.bana.api.entity.TpzHomsAccountJourEntity;
import com.caimao.bana.api.entity.req.*;
import com.caimao.bana.api.entity.zeus.BanaHomsFinanceHistoryEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountAssetsEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsRepaymentExcludeEntity;
import com.caimao.bana.api.enums.EAccountBizType;
import com.caimao.bana.api.service.IHomsAccountService;
import com.caimao.bana.api.service.IZeusStatisticsService;
import com.caimao.zeus.admin.controller.BaseController;
import com.caimao.zeus.util.DateUtil;
import com.caimao.zeus.util.HomsExcelUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 公告列表
 * Created by WangXu on 2015/6/18.
 */
@Controller
@RequestMapping("/homs")
public class HomsController  extends BaseController {

    private Logger logger = LoggerFactory.getLogger(HomsController.class);

    @Resource
    private IHomsAccountService homsAccountService;

    @Resource
    private IZeusStatisticsService zeusStatisticsService;

    /**
     * 获取HOMS账户资产信息
     * @param req
     * @return
     */
    @RequestMapping(value = "/assets_list", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView HomsAccountAssetsList(FZeusHomsAccountAssetsReq req) throws Exception {
        System.out.println(ToStringBuilder.reflectionToString(req));
        ModelAndView mav = new ModelAndView("homs/list/asstes");
        if (req.getUpdateDate() == null) {
            req.setUpdateDate(DateUtil.convertDateToString(DateUtil.DISPLAY_DATE_FORMAT_STRING, new Date()));
        }
        req.setLimit(50);
        req.setCurrentPage(req.getCurrentPage());
        req = this.homsAccountService.searchZeusHomsAssetsList(req);
        mav.addObject("list", req);

        for (ZeusHomsAccountAssetsEntity entity: req.getItems()) {
            System.out.println(entity.getExposureRatio());
        }

        return mav;
    }

    /**
     * HOMS持仓列表
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hold_list", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView HomsAccountHoldList(FZeusHomsAccountHoldReq req) throws Exception {
        ModelAndView mav = new ModelAndView("homs/list/hold");
        req.setLimit(1000);
        req.setCurrentPage(req.getCurrentPage());
        req = this.homsAccountService.searchZeusHomsHoldList(req);
        mav.addObject("list", req);
        return mav;
    }

    /**
     * HOMS财务流水
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jour_list", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView HomsAccountJourList(FTPZQueryHomsAccountJourReq req, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(req.getDateStart())) {
            req.setDateStart(sdf.format(new Date()));
        }
        if(StringUtils.isEmpty(req.getDateEnd())) {
            req.setDateEnd(sdf.format(new Date(System.currentTimeMillis() + 86400000)));
        }
        ModelAndView mav = new ModelAndView("homs/list/jour");
        req.setLimit(100);
        req.setCurrentPage(req.getCurrentPage());

        String isExp = request.getParameter("isExp");
        if(isExp != null && isExp.equals("1")){
            req.setLimit(100000);
            response.setContentType("application/msexcel;");
            response.setHeader("Content-Disposition", new String(("attachment;filename=" + "accountJour.xls").getBytes("GB2312"), "UTF-8"));
            mav.setViewName("homs/list/jourExp");
        }
        req = this.zeusStatisticsService.queryHomsAccountJourListWithPage(req);
        if(req.getItems() != null){
            for (TpzHomsAccountJourEntity tpzHomsAccountJourEntity:req.getItems()){
				EAccountBizType bizType = EAccountBizType.findByCode(tpzHomsAccountJourEntity.getAccountBizType());
				if(bizType != null){
					tpzHomsAccountJourEntity.setAccountBizType(bizType.getValue());
				}
            }
        }
        HashMap<String, String> accountBizTypeList = new HashMap<>();
        for (EAccountBizType eAccountBizType : EAccountBizType.values()) {
            accountBizTypeList.put(eAccountBizType.getCode(), eAccountBizType.getValue());
        }
        mav.addObject("accountBizTypeList", accountBizTypeList);
        mav.addObject("list", req);
        return mav;
    }

    @RequestMapping(value = "/user_jour_list", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView UserAccountJourList(FTPZQueryUserAccountJourReq req, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(req.getDateStart())) {
            req.setDateStart(sdf.format(new Date()));
        }
        if(StringUtils.isEmpty(req.getDateEnd())) {
            req.setDateEnd(sdf.format(new Date(System.currentTimeMillis() + 86400000)));
        }
        ModelAndView mav = new ModelAndView("homs/list/userJour");
        req.setLimit(100);
        req.setCurrentPage(req.getCurrentPage());

        String isExp = request.getParameter("isExp");
        if(isExp != null && isExp.equals("1")){
            req.setLimit(100000);
            response.setContentType("application/msexcel;");
            response.setHeader("Content-Disposition", new String(("attachment;filename=" + "userAccountJour.xls").getBytes("GB2312"), "UTF-8"));
            mav.setViewName("homs/list/userJourExp");
        }
        req = this.zeusStatisticsService.queryUserAccountJourListWithPage(req);

        if(req.getItems() != null){
            for (TpzAccountJourEntity tpzAccountJourEntity:req.getItems()){
                EAccountBizType bizType = EAccountBizType.findByCode(tpzAccountJourEntity.getAccountBizType());
                if(bizType != null){
                    tpzAccountJourEntity.setAccountBizType(bizType.getValue());
                }
            }
        }
        HashMap<String, String> accountBizTypeList = new HashMap<>();
        for (EAccountBizType eAccountBizType : EAccountBizType.values()) {
            accountBizTypeList.put(eAccountBizType.getCode(), eAccountBizType.getValue());
        }
        mav.addObject("accountBizTypeList", accountBizTypeList);
        mav.addObject("list", req);
        return mav;
    }

    @RequestMapping(value = "/user_jour_biz_type_change", method = RequestMethod.POST)
    public ModelAndView UserAccountJourBizTypeChange(@RequestParam(value = "id") Long id, @RequestParam(value = "bizType") String bizType, HttpServletRequest request) throws Exception {
        try{
            this.zeusStatisticsService.changeUserAccountJourBizType(id, bizType);
            return this.jumpForSuccess(request, "修改成功", "/homs/user_jour_list");
        }catch(Exception e){
            return this.jumpForFail(request, e.getMessage(), "/homs/user_jour_list");
        }
    }

    /**
     * 有持仓可以进行还款的排除列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exclude/list", method = RequestMethod.GET)
    public ModelAndView HomsRepaymentExcludeList() throws Exception {
        ModelAndView mav = new ModelAndView("homs/exclude/list");
        List<ZeusHomsRepaymentExcludeEntity> excludeEntityList = this.homsAccountService.queryHomsRepaymentExcludeList(null, null);
        mav.addObject("list", excludeEntityList);
        return mav;
    }

    /**
     * 有持仓可以进行还款保存
     * @throws Exception
     */
    @RequestMapping(value = "/exclude/save", method = RequestMethod.GET)
    public void HomsRepaymentExcludeSave(
            HttpServletResponse response,
            @RequestParam(value = "homsCombineId") String homsCombineId) throws Exception {
        this.homsAccountService.saveHomsRepaymentExclude(homsCombineId);
        response.sendRedirect("/homs/exclude/list");

    }

    /**
     * 有持仓可以进行还款删除
     * @throws Exception
     */
    @RequestMapping(value = "/exclude/delete", method = RequestMethod.GET)
    public void HomsRepaymentExcludeDelete(
            HttpServletResponse response,
            @RequestParam(value = "homsFundAccount") String homsFundAccount,
            @RequestParam(value = "homsCombineId") String homsCombineId) throws Exception {
        this.homsAccountService.deleteHomsRepayMentExclude(homsFundAccount, homsCombineId);
        response.sendRedirect("/homs/exclude/list");
    }

    @RequestMapping(value = "/homs_jour_list", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView HomsJourList(FZeusHomsJourReq req, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("homs/list/homsJour");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(req.getDateStart())) {
            req.setDateStart(sdf.format(new Date()));
        }
        if(StringUtils.isEmpty(req.getDateEnd())) {
            req.setDateEnd(sdf.format(new Date(System.currentTimeMillis() + 86400000)));
        }
        req.setLimit(100);
        req.setCurrentPage(req.getCurrentPage());
        req.setOrderDir("DESC");
        req.setOrderColumn("trans_no");

        String isExp = request.getParameter("isExp");
        if(isExp != null && isExp.equals("1")){
            req.setLimit(1000000);
            response.setContentType("application/msexcel;");
            response.setHeader("Content-Disposition", new String(("attachment;filename=" + "HomsJour.xls").getBytes("GB2312"), "UTF-8"));
            mav.setViewName("homs/list/homsJourExp");
        }

        if(req.getTransAmount() != null && !req.getTransAmount().equals("")){
            req.setTransAmount(new BigDecimal(req.getTransAmount()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN).toString());
        }


        req = this.zeusStatisticsService.queryHomsJourListWithPage(req);
        mav.addObject("list", req);

        if(req.getTransAmount() != null && !req.getTransAmount().equals("")){
            req.setTransAmount(new BigDecimal(req.getTransAmount()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN).toString());
        }

        return mav;
    }

    @RequestMapping(value = "/homs_jour_import", method = RequestMethod.POST)
    @ResponseBody
    public String HomsJourImport(MultipartHttpServletRequest multipartRequest) throws Exception {
        MultipartFile file = multipartRequest.getFile("file");
        InputStream inputStream = file.getInputStream();
        Integer error = 0;
        Integer success = 0;

        List<HashMap<String, Object>> recordList = HomsExcelUtils.getExcelData(inputStream);
        List<HashMap<String, Object>> recordBatch = new ArrayList<>();

        for(HashMap<String, Object> record:recordList){
            try{
                if(record.get("transAmount").equals("")) record.put("transAmount", 0);
                if(record.containsKey("transAmount")) record.put("transAmount", new BigDecimal(record.get("transAmount").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("postAmount").equals("")) record.put("postAmount", 0);
                if(record.containsKey("postAmount")) record.put("postAmount", new BigDecimal(record.get("postAmount").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("entrustPrice").equals("")) record.put("entrustPrice", 0);
                if(record.containsKey("entrustPrice")) record.put("entrustPrice", new BigDecimal(record.get("entrustPrice").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("entrustAmount").equals("")) record.put("entrustAmount", 0);
                if(record.containsKey("entrustAmount")) record.put("entrustAmount", new BigDecimal(record.get("entrustAmount").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("tradeFee").equals("")) record.put("tradeFee", 0);
                if(record.containsKey("tradeFee")) record.put("tradeFee", new BigDecimal(record.get("tradeFee").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("stampDuty").equals("")) record.put("stampDuty", 0);
                if(record.containsKey("stampDuty")) record.put("stampDuty", new BigDecimal(record.get("stampDuty").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("transferFee").equals("")) record.put("transferFee", 0);
                if(record.containsKey("transferFee")) record.put("transferFee", new BigDecimal(record.get("transferFee").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("commission").equals("")) record.put("commission", 0);
                if(record.containsKey("commission")) record.put("commission", new BigDecimal(record.get("commission").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("handlingFee").equals("")) record.put("handlingFee", 0);
                if(record.containsKey("handlingFee")) record.put("handlingFee", new BigDecimal(record.get("handlingFee").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("secCharges").equals("")) record.put("secCharges", 0);
                if(record.containsKey("secCharges")) record.put("secCharges", new BigDecimal(record.get("secCharges").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("subjectTransAmount").equals("")) record.put("subjectTransAmount", 0);
                if(record.containsKey("subjectTransAmount")) record.put("subjectTransAmount", new BigDecimal(record.get("subjectTransAmount").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("subjectPostAmount").equals("")) record.put("subjectPostAmount", 0);
                if(record.containsKey("subjectPostAmount")) record.put("subjectPostAmount", new BigDecimal(record.get("subjectPostAmount").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(record.get("technicalServices").equals("")) record.put("technicalServices", 0);
                if(record.containsKey("technicalServices")) record.put("technicalServices", new BigDecimal(record.get("technicalServices").toString()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
                if(recordBatch.size() == 500){
                    zeusStatisticsService.saveHomsFinanceHistoryBatch(recordBatch);
                    recordBatch = new ArrayList<>();
                    success++;
                }
                recordBatch.add(record);
            }catch(Exception e){
                error++;
                break;
            }
        }
        if(recordBatch.size() > 0){
            zeusStatisticsService.saveHomsFinanceHistoryBatch(recordBatch);
        }
        String errorMsg = error > 0?"在第 " + success * 500 + " 条数据后面的500条内有数据格式异常，导致后面无法进行":"全部成功";

        return "Total: " + recordList.size() + "," + errorMsg;
    }
}