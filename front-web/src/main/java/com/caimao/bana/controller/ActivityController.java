/*
*HeepayController.java
*Created on 2015/4/23 16:08
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.controller;

import com.caimao.bana.api.entity.InviteInfoEntity;
import com.caimao.bana.api.entity.WeChatForecastCountEntity;
import com.caimao.bana.api.entity.WeChatForecastEntity;
import com.caimao.bana.api.entity.WeChatUserEntity;
import com.caimao.bana.api.service.IActivityService;
import com.caimao.bana.api.service.InviteInfoService;
import com.caimao.bana.api.service.WeChatUserService;
import com.caimao.bana.utils.WeChatUtils;
import com.hsnet.pz.controller.BaseController;
import com.hsnet.pz.core.exception.BizServiceException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/activity")
public class ActivityController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Resource
    private WeChatUtils weChatUtils;

    @Resource
    private WeChatUserService weChatUserService;

    @Resource
    private InviteInfoService inviteInfoService;
	
	@Resource
    private IActivityService activityService;

    @RequestMapping(value = "/stockMarketForecast", method = {RequestMethod.GET, RequestMethod.POST})
    public String stockMarketForecast(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session){
        try{
            WeChatUserEntity weChatUserEntity = (WeChatUserEntity) session.getAttribute("WeChatUser");
            if(weChatUserEntity == null){
                weChatUserEntity = this.weChatUtils.getWeChatUser(request, response);
                session.setAttribute("WeChatUser", weChatUserEntity);
            }
            //查询是否预测过
            WeChatForecastEntity weChatForecastEntity = weChatUserService.getUserMarketForecast(WeChatUtils.getTimesToday() + 86400, weChatUserEntity.getId());
            if(weChatForecastEntity != null) {
                response.sendRedirect("/activity/tomorrowStockMarketForecast");
            }

            //获取用户连续天数信息
            WeChatForecastCountEntity weChatForecastCountEntity = weChatUserService.getUserMarketForecastCount(weChatUserEntity.getId());
            model.addAttribute("weChatForecastCount", weChatForecastCountEntity);

            //获取微信分享
            Map<String, String> weChatSign = this.weChatUtils.getSign(request.getRequestURL().toString());
            model.addAttribute("weChatSign", weChatSign);
        }catch (Exception e){
            if(!e.getMessage().equals("获取终止")){
                logger.error("获取微信用户信息异常. 异常信息{}", e);
                throw new BizServiceException("84000", "获取微信用户信息失败");
            }
        }
        return "stockMarketForecast";
    }

    @RequestMapping(value = "/doStockMarketForecast", method = RequestMethod.POST)
    @ResponseBody
    public void doStockMarketForecast(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        try{
            WeChatUserEntity weChatUserEntity = (WeChatUserEntity) session.getAttribute("WeChatUser");
            if(weChatUserEntity == null){
                response.sendRedirect("/activity/stockMarketForecast");
            }else{
                String forecastType = request.getParameter("forecastType");
                String[] forecastTypeALL = {"1", "2", "3"};
                Arrays.sort(forecastTypeALL);
                if(Arrays.binarySearch(forecastTypeALL, forecastType) < 0){
                    throw new Exception("预测类型不正确");
                }
                //查询是否预测过
                WeChatForecastEntity weChatForecastEntity = weChatUserService.getUserMarketForecast(WeChatUtils.getTimesToday() + 86400, weChatUserEntity.getId());
                if(weChatForecastEntity == null) {
                    weChatUtils.createMarketForecast(weChatUserEntity.getId(), new Byte(forecastType));
                }
                response.sendRedirect("/activity/tomorrowStockMarketForecast");
            }
        }catch (Exception e){
            logger.error("提交预测异常. 异常信息{}", e);
            throw new BizServiceException("84001", "提交预测失败");
        }
    }

    @RequestMapping(value = "/tomorrowStockMarketForecast", method = RequestMethod.GET)
    public String tomorrowStockMarketForecast(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session){
        try{
            WeChatUserEntity weChatUserEntity = (WeChatUserEntity) session.getAttribute("WeChatUser");
            if(weChatUserEntity == null) {
                response.sendRedirect("/activity/stockMarketForecast");
            }else{
                Map<String, String> forecastResult = weChatUserService.getMarketForecastByDate(WeChatUtils.getTimesToday() + 86400);
                model.addAttribute("forecastResult", forecastResult);

                //获取用户连续天数信息
                WeChatForecastCountEntity weChatForecastCountEntity = weChatUserService.getUserMarketForecastCount(weChatUserEntity.getId());
                model.addAttribute("weChatForecastCount", weChatForecastCountEntity);

                //获取微信分享
                Map<String, String> weChatSign = this.weChatUtils.getSign(request.getRequestURL().toString());
                model.addAttribute("weChatSign", weChatSign);
            }
            return "tomorrowStockMarketForecast";
        }catch (Exception e){
            logger.error("明日预测查询异常. 异常信息{}", e);
            throw new BizServiceException("84002", "明日预测查询失败");
        }
    }

    @RequestMapping(value = "/todayStockMarketForecast", method = RequestMethod.GET)
    public String todayStockMarketForecast(Model model, HttpServletRequest request){
        try{
            Map<String, String> forecastResult = weChatUserService.getMarketForecastByDate(WeChatUtils.getTimesToday());
            model.addAttribute("forecastResult", forecastResult);
            //获取今日大盘涨跌
            String correctForecast = "2";
            model.addAttribute("correctForecast", correctForecast);

            //获取微信分享
            Map<String, String> weChatSign = this.weChatUtils.getSign(request.getRequestURL().toString());
            model.addAttribute("weChatSign", weChatSign);

            return "todayStockMarketForecast";
        }catch (Exception e){
            logger.error("获取今日大盘异常. 异常信息{}", e);
            throw new BizServiceException("84003", "今日预测查询失败");
        }
    }

    @RequestMapping(value = "/getInviteInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public InviteInfoEntity getInviteInfo(){
        try {
            return inviteInfoService.getInviteInfo(getSessionUser().getUser_id());
        } catch (Exception e) {
            throw new BizServiceException("830408", "获取信息失败");
        }
    }

    @RequestMapping(value = "/brokerage", method = {RequestMethod.GET, RequestMethod.POST})
    public String brokerage() throws Exception{
        return "account/brokerage";
    }

    @RequestMapping(value = "/getBrokerageRank", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<Map<String, Object>> getBrokerageRank(){
        try {
            List<Map<String, Object>> brokerageRank = inviteInfoService.getBrokerageRankCnt();
            if(brokerageRank != null){
                for (Map<String, Object> aBrokerageRank:brokerageRank){
                    if(aBrokerageRank.get("mobile").toString().length() > 10){
                        String mobile = aBrokerageRank.get("mobile").toString();
                        aBrokerageRank.put("mobile", mobile.substring(0, 3) + StringUtils.rightPad("", 4, '*') + mobile.substring(7, 11));
                    }
                }
            }
            return brokerageRank;
        } catch (Exception e) {
            throw new BizServiceException("830408", "获取信息失败");
        }
	}

    @RequestMapping(value = "/getBrokerageRank2", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<Map<String, Object>> getBrokerageRank2(){
        try {
            List<Map<String, Object>> brokerageRank = inviteInfoService.getBrokerageRankMoney();
            if(brokerageRank != null){
                for (Map<String, Object> aBrokerageRank:brokerageRank){
                    if(aBrokerageRank.get("mobile").toString().length() > 10){
                        String mobile = aBrokerageRank.get("mobile").toString();
                        aBrokerageRank.put("mobile", mobile.substring(0, 3) + StringUtils.rightPad("", 4, '*') + mobile.substring(7, 11));
                    }
                }
            }
            return brokerageRank;
        } catch (Exception e) {
            throw new BizServiceException("830408", "获取信息失败");
        }
    }

    /**
     * 保存微信跑酷的数据
     * @param response
     * @param phone
     * @param pzValue
     * @return
     */
    @RequestMapping(value = "/api_wx_pao", method = {RequestMethod.GET, RequestMethod.POST})
    public String wxPaokuDataApi(HttpServletResponse response,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("pz_value") String pzValue) {
        int res = 0;
        try {
            res = this.activityService.saveWeixinPaoKu(phone, pzValue, getRemoteHost());
        } catch (Exception e) {
            this.logger.error("保存微信跑酷运动数据异常 {}", e);
        }
        try {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().print(res);
            response.getWriter().flush();
        } catch (Exception e) {

        }
        return null;
    }

    @RequestMapping(value = "/get_show_rp", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Boolean getShowRedPackage() throws Exception{
        try{
            return activityService.getShowRedPackage(getSessionUser().getUser_id());
        }catch(Exception e){
            throw new BizServiceException("111111", e.getMessage());
        }
    }

    @RequestMapping(value = "/open_rp", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Long openRedPackage() throws Exception{
        try{
            return activityService.openRedPackage(getSessionUser().getUser_id());
        }catch(Exception e){
            throw new BizServiceException("222222", e.getMessage());
        }
    }
}
