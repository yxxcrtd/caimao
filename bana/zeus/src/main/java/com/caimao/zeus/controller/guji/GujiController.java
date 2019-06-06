package com.caimao.zeus.controller.guji;

import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.entity.req.guji.FQueryAdminShareListReq;
import com.caimao.bana.api.entity.req.guji.FQueryAdminUserListReq;
import com.caimao.bana.api.service.guji.IGujiAdminService;
import com.caimao.zeus.util.JSONUtils;
import com.caimao.zeus.util.PageUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import com.caimao.bana.api.enums.guji.EGujiAuthStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * 股计后台服务控制器
 * Created by Administrator on 2016/1/14.
 */
@Controller
@RequestMapping(value = "/guji")
public class GujiController {

    @Autowired
    private IGujiAdminService gujiAdminService;

    private static ResourceBundle appBundle = PropertyResourceBundle.getBundle("conf/application");

    /**
     * 用户列表
     * @param nickName      昵称，支持模糊查询
     * @param authStatus    认证状态
     * @param page          当前页数
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user_list.html")
    public ModelAndView userList(
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "authStatus", required = false) Integer authStatus,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/guji/user_list");
        if (nickName != null && nickName.equals("")) nickName = null;
        if (authStatus != null && authStatus.equals(0)) authStatus = null;
        Integer limit = 50;
        FQueryAdminUserListReq req = new FQueryAdminUserListReq();
        req.setNickName(nickName);
        req.setAuthStatus(authStatus);
        req.setStart((page - 1) * limit);
        req.setLimit(limit);
        req = this.gujiAdminService.queryUserList(req);

        if(req.getItems() != null && req.getItems().size() > 0){
            PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(),
                    String.format("/guji/user_list.html?nickName=%s&authStatus=%s&page=", nickName == null ? "" : nickName, authStatus == null ? "" : authStatus));
            mav.addObject("pageHtml", pageUtils.show());
        }

        mav.addObject("domainUrl", appBundle.getString("ybkUrl"));
        mav.addObject("nickName", nickName);
        mav.addObject("authStatus", authStatus);
        mav.addObject("authStatusMap",getauthStatusMap());
        mav.addObject("userList", req);
        return mav;
    }

    /**
     * 股计编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam(value = "id", required = true) Long id) throws Exception {
        ModelAndView mav = new ModelAndView("guji/user_edit");
        GujiUserEntity entity = null;
        FQueryAdminUserListReq req = new FQueryAdminUserListReq();

        if (null != id && 0 != id) {
            entity = this.gujiAdminService.selectByWxId(id);
            mav.addObject("entity", entity);

        }
        mav.addObject("authStatus",getauthStatusMap());

        return mav;
    }

    private Map getauthStatusMap(){

        EGujiAuthStatus[] allLight = EGujiAuthStatus.values();
        Map map=new HashMap();
        for (EGujiAuthStatus aLight : allLight) {

            map.put(aLight.getCode(), aLight.getValue());
        }
        return map;
    }

    /**
     * 推荐股票列表
     * @param nickName      昵称，支持模糊查询
     * @param stockType     股票、大盘
     * @param operType      操作类型
     * @param stockCode     股票代码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/share_list.html")
    public ModelAndView shareList(
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "stockType", required = false) String stockType,
            @RequestParam(value = "operType", required = false) Integer operType,
            @RequestParam(value = "stockCode", required = false) String stockCode,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/guji/share_list");
        if (nickName != null && nickName.equals("")) nickName = null;
        if (stockCode != null && stockCode.equals("")) stockCode = null;
        if (stockType != null && stockType.equals("")) stockType = null;
        if (operType != null && operType.equals(0)) operType = null;
        Integer limit = 50;
        FQueryAdminShareListReq req = new FQueryAdminShareListReq();
        req.setNickName(nickName);
        req.setStockCode(stockCode);
        req.setStockType(stockType);
        req.setOperType(operType);
        req.setStart((page-1)*limit);
        req.setLimit(limit);
        req = this.gujiAdminService.queryShareList(req);


        if(req.getItems() != null && req.getItems().size() > 0){
            PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(),
                    String.format("/guji/share_list.html?nickName=%s&stockType=%s&operType=%s&stockCode=%s&page=",
                            nickName == null ? "" : nickName, stockType == null ? "" : stockType, operType == null ? "" : operType, stockCode == null ? "" : stockCode));
            mav.addObject("pageHtml", pageUtils.show());
        }
        mav.addObject("nickName", nickName);
        mav.addObject("stockType", stockType);
        mav.addObject("operType", operType);
        mav.addObject("stockCode", stockCode);
        mav.addObject("shareList", req);
        return mav;
    }

    /**
     * 更新用户认证状态
     * @param wxId
     * @param authStatus
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/auth_user", method = RequestMethod.POST)
    public ModelAndView authUser(
            @RequestParam(value = "wxId", required = true) Long wxId,
            @RequestParam(value = "authStatus", required = true) Integer authStatus,
            @RequestParam(value = "certificationAuth", required = false) String certificationAuth,
            HttpServletRequest request
    ) throws Exception {
        String message="";
        ModelAndView mav = new ModelAndView("/guji/user_list");
        GujiUserEntity enty=new GujiUserEntity();
        enty.setWxId(wxId);
        enty.setAuthStatus(authStatus);
        enty.setCertificationAuth(certificationAuth);
        enty.setUpdateTime(new Date());
        Boolean isSuccess=true;
        try{
            gujiAdminService.update(enty);
        }catch (Exception ex){
            message=ex.getMessage();
            isSuccess=false;
        }
        if(isSuccess){
            return jumpForSuccess(request, "编辑成功", "/guji/user_list.html");
        }else{
            return jumpForSuccess(request, "编辑失败:"+message, "/guji/user_list.html");
        }
    }

    /**
     * 删除文章
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/del_share")
    public Boolean delShare(
            @RequestParam(value = "id") Long id
    ) throws Exception {
        this.gujiAdminService.delShare(id);
        return true;
    }
    public ModelAndView jumpForFail(HttpServletRequest request, String msg, String url) throws Exception{
        if(isAjax(request)){
            Map<String, String> ajaxData = new HashMap<>();
            ajaxData.put("code", "1");
            ajaxData.put("msg", msg);
            JSONUtils jsonUtils = new JSONUtils(JsonInclude.Include.ALWAYS);

            Map<String, String> model = new HashMap<>();
            model.put("ajaxData", jsonUtils.jsonToString(ajaxData));
            return new ModelAndView("common/ajax", model);
        }else{
            Map<String, String> model = new HashMap<>();
            model.put("failMsg", msg);
            model.put("url", url);
            return new ModelAndView("common/jump", model);
        }
    }
    public Boolean isAjax(HttpServletRequest request) throws Exception{
        String header = request.getHeader("X-Requested-With");
        return header != null && header.toLowerCase().equals("xmlhttprequest");
    }
    public ModelAndView jumpForSuccess(HttpServletRequest request, String msg, String url) throws Exception{
        if(isAjax(request)){
            Map<String, String> ajaxData = new HashMap<>();
            ajaxData.put("code", "0");
            ajaxData.put("msg", msg);
            JSONUtils jsonUtils = new JSONUtils(JsonInclude.Include.ALWAYS);

            Map<String, String> model = new HashMap<>();
            model.put("ajaxData", jsonUtils.jsonToString(ajaxData));
            return new ModelAndView("common/ajax", model);
        }else{
            Map<String, String> model = new HashMap<>();
            model.put("successMsg", msg);
            model.put("url", url);
            return new ModelAndView("common/jump", model);
        }
    }
    /**
     * 更新文章是否公开到大厅
     * @param id
     * @param isPublic
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/update_is_public")
    public Boolean update(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "isPublic") Integer isPublic
    ) throws Exception {
        this.gujiAdminService.updateShareIsPublic(id, isPublic);
        return true;
    }



}

