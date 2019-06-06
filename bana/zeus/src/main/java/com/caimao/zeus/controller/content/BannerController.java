package com.caimao.zeus.controller.content;

import com.caimao.bana.api.entity.content.BanaBannerEntity;
import com.caimao.bana.api.entity.req.content.FQueryBannerListReq;
import com.caimao.bana.api.service.content.IBannerService;
import com.caimao.zeus.util.ImageUtils;
import com.caimao.zeus.util.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Banner管理列表
 * Created by WangXu on 2015/6/18.
 */
@Controller
@RequestMapping("/content/banner")
public class BannerController {

    private Logger logger = LoggerFactory.getLogger(BannerController.class);

    @Resource
    private IBannerService bannerService;

    private static ResourceBundle appBundle = PropertyResourceBundle.getBundle("conf/application");

    // Banner的列表
    @RequestMapping(value = "/list")
    public ModelAndView list(
            @RequestParam(value = "appType", required = false) String appType,
            @RequestParam(value = "isShow", required = false) Integer isShow,
            @RequestParam(value = "page", defaultValue = "1") Integer page
    ) throws Exception {
        ModelAndView mav = new ModelAndView("content/banner/list");
        // 获取列表
        Integer limit = 20;
        Integer start = (page - 1) * 20;
        FQueryBannerListReq req = new FQueryBannerListReq();
        req.setStart(start);
        req.setLimit(limit);
        req.setIsShow(isShow);
        req.setAppType(appType);

        req = this.bannerService.queryBannerList(req);

        if (req.getItems() != null){
            PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(),String.format("/content/banner/list?appType=%s&isShow=%s&page=", appType, isShow));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }

        mav.addObject("req", req);
        mav.addObject("ybkUrl", appBundle.getString("ybkUrl"));

        mav.addObject("appType", appType);
        mav.addObject("isShow", isShow);

        return mav;
    }

    // 添加banner的控制器
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ModelAndView save() throws Exception {
        ModelAndView mav = new ModelAndView("content/banner/save");
        return mav;
    }
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RedirectView save(
            @RequestParam(value = "appType") String appType,
            @RequestParam(value = "name") String name,
            //@RequestParam(value = "pcPicFile") MultipartFile pcPicFile,
            //@RequestParam(value = "appPicFile") MultipartFile appPicFile,
            @RequestParam(value = "pcJumpUrl") String pcJumpUrl,
            @RequestParam(value = "appJumpUrl") String appJumpUrl,
            @RequestParam(value = "sort") Integer sort,
            @RequestParam(value = "isShow") Integer isShow,
            MultipartHttpServletRequest request
    ) throws Exception {
        String pcPic = null;
        String appPic = null;
        String savePath = appBundle.getString("picturePath") + "banner";
        MultipartFile pcPicFile = request.getFile("pcPicFile");
        if (!pcPicFile.isEmpty()) {
            pcPic = ImageUtils.uploadImag(pcPicFile, savePath, 5120L, false);
            pcPic = "/" + pcPic.substring(pcPic.indexOf("upload"));
        }
        MultipartFile appPicFile = request.getFile("appPicFile");
        if (!appPicFile.isEmpty()) {
            appPic = ImageUtils.uploadImag(appPicFile, savePath, 5210L, false);
            appPic = "/" + appPic.substring(appPic.indexOf("upload"));
        }
        BanaBannerEntity entity = new BanaBannerEntity();
        entity.setAppType(appType);
        entity.setName(name);
        entity.setPcPic(pcPic);
        entity.setPcJumpUrl(pcJumpUrl);
        entity.setAppPic(appPic);
        entity.setAppJumpUrl(appJumpUrl);
        entity.setSort(sort);
        entity.setIsShow(isShow);
        entity.setCreateTime(new Date());
        this.bannerService.addBanner(entity);

        return new RedirectView("/content/banner/list");
    }

    // 修改banner
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam(value = "id") Integer id) throws Exception {
        ModelAndView mav = new ModelAndView("/content/banner/edit");
        BanaBannerEntity entity = this.bannerService.selectById(id);

        mav.addObject("bannerInfo", entity);
        mav.addObject("ybkUrl", appBundle.getString("ybkUrl"));
        return mav;
    }
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RedirectView edit(
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "appType") String appType,
            @RequestParam(value = "name") String name,
            //@RequestParam(value = "pcPicFile") MultipartFile pcPicFile,
            //@RequestParam(value = "appPicFile") MultipartFile appPicFile,
            @RequestParam(value = "pcJumpUrl") String pcJumpUrl,
            @RequestParam(value = "appJumpUrl") String appJumpUrl,
            @RequestParam(value = "sort") Integer sort,
            @RequestParam(value = "isShow") Integer isShow,
            MultipartHttpServletRequest request
    ) throws Exception {
        BanaBannerEntity entity = new BanaBannerEntity();
        entity.setId(id);
        String savePath = appBundle.getString("picturePath") + "banner";
        MultipartFile pcPicFile = request.getFile("pcPicFile");
        if (!pcPicFile.isEmpty()) {
            String pcPic = ImageUtils.uploadImag(pcPicFile, savePath, 5120L, false);
            entity.setPcPic("/" + pcPic.substring(pcPic.indexOf("upload")));
        }
        MultipartFile appPicFile = request.getFile("appPicFile");
        if (!appPicFile.isEmpty()) {
            String appPic = ImageUtils.uploadImag(appPicFile, savePath, 5210L, false);
            entity.setAppPic("/" + appPic.substring(appPic.indexOf("upload")));
        }
        entity.setAppType(appType);
        entity.setName(name);
        entity.setPcJumpUrl(pcJumpUrl);
        entity.setAppJumpUrl(appJumpUrl);
        entity.setSort(sort);
        entity.setIsShow(isShow);
        this.bannerService.updatgeBanner(entity);

        return new RedirectView("/content/banner/list");
    }

    // 删除 Banner
    @RequestMapping(value = "/del")
    public void del(
            @RequestParam(value = "id") Integer id,
            HttpServletResponse response
    ) throws Exception {
        this.bannerService.delById(id);
        response.sendRedirect("/content/banner/list");
    }



}
