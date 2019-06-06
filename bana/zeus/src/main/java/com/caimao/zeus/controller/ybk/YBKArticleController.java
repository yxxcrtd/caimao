package com.caimao.zeus.controller.ybk;

import com.baidu.ueditor.ActionEnter;
import com.caimao.bana.api.entity.req.ybk.FYBKQueryArticleListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkExchangeQueryListReq;
import com.caimao.bana.api.entity.ybk.YBKArticleEntity;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import com.caimao.bana.api.enums.ybk.EArticleCategory;
import com.caimao.bana.api.service.ybk.IYBKService;
import com.caimao.zeus.admin.controller.BaseController;
import com.caimao.zeus.util.PageUtils;
import com.caimao.zeus.util.RedisUtils;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 邮币卡文章控制器
 */
@Controller
@RequestMapping("/ybk/article")
public class YBKArticleController extends BaseController {
    @Resource
    private IYBKService ybkService;

    @Resource
    private RedisUtils redisUtils;

    /**
     * UEDITOR 编辑器的功能
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ueditor", method = RequestMethod.GET)
    public void ueditorConfig(HttpServletRequest request, HttpServletResponse response, String action) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "text/html");
        String rootPath = "/";
        //System.out.println(rootPath);
        String exec = new ActionEnter(request, rootPath).exec();
        PrintWriter writer = response.getWriter();
        writer.write(exec);
        writer.flush();
        writer.close();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "categoryId", required = false, defaultValue = "0") Integer categoryId,
                             @RequestParam(value = "exchangeId", required = false, defaultValue = "0") Integer exchangeId,
                             @RequestParam(value = "dateStart", required = false) String dateStart,
                             @RequestParam(value = "dateEnd", required = false) String dateEnd,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ModelAndView mav = new ModelAndView("ybk/article/list");
        FYBKQueryArticleListReq req = new FYBKQueryArticleListReq();
        Integer limit = 10;
        req.setStart((page - 1) * limit);
        req.setLimit(limit);
        if (categoryId == null) categoryId = 0;
        if (exchangeId == null) exchangeId = 0;
        if (categoryId != 0) req.setCategoryId(categoryId);
        if (exchangeId != 0) req.setExchangeId(exchangeId);
        if (dateStart == null || dateStart.equals("")) {
            dateStart = sdf.format(new Date());
        }
        req.setDateStart(dateStart);
        if (dateEnd == null || dateEnd.equals("")) {
            dateEnd = sdf.format(new Date());
        }
        req.setDateEnd(dateEnd);

        FYBKQueryArticleListReq list = ybkService.queryArticleWithPage(req);
        mav.addObject("list", list);
        if (list.getItems() != null) {
            PageUtils pageUtils = new PageUtils(page, limit, list.getTotalCount(), String.format("/ybk/article/list?categoryId=%s&exchangeId=%s&dateStart=%s&dateEnd=%s&page=", categoryId, exchangeId, dateStart, dateEnd));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }

        HashMap<String, String> categoryList = new HashMap<>();
        for (EArticleCategory articleCategory : EArticleCategory.values()) {
            categoryList.put(articleCategory.getCode(), articleCategory.getValue());
        }
        // 获取交易所列表
        FYbkExchangeQueryListReq exchangeReq = new FYbkExchangeQueryListReq();
        //exchangeReq.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(exchangeReq);
        HashMap<Integer, YbkExchangeEntity> exchangeEntityHashMap = new HashMap<>();
        for (YbkExchangeEntity entity : exchangeEntityList) {
            exchangeEntityHashMap.put(entity.getId(), entity);
        }
        mav.addObject("exchangeList", exchangeEntityList);
        mav.addObject("exchangeMap", exchangeEntityHashMap);
        mav.addObject("categoryList", categoryList);
        mav.addObject("categoryId", categoryId);
        mav.addObject("exchangeId", exchangeId);
        mav.addObject("dateStart", dateStart);
        mav.addObject("dateEnd", dateEnd);

        // APP首页显示的自定义活动标题与链接设置
        mav.addObject("_title", this.redisUtils.hGet(0, "_ybkAppIndexArticle", "title"));
        mav.addObject("_url", this.redisUtils.hGet(0, "_ybkAppIndexArticle", "url"));

        return mav;
    }

    @RequestMapping(value = "/saveAppArticle", method = RequestMethod.POST)
    public ModelAndView saveAppArticle(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "url") String url,
            HttpServletRequest request
    ) throws Exception {
        this.redisUtils.hSet(0, "_ybkAppIndexArticle", "title", title);
        this.redisUtils.hSet(0, "_ybkAppIndexArticle", "url", url);
        return this.jumpForSuccess(request, "保存成功", "/ybk/article/list");
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ModelAndView save(@RequestParam(value = "id", required = false) Long id) throws Exception {
        ModelAndView mav = new ModelAndView("ybk/article/save");
        HashMap<String, String> categoryList = new HashMap<>();
        for (EArticleCategory articleCategory : EArticleCategory.values()) {
            categoryList.put(articleCategory.getCode(), articleCategory.getValue());
        }
        mav.addObject("categoryList", categoryList);
        if (id != null && id != 0) {
            YBKArticleEntity ybkArticleEntity = ybkService.queryArticleById(id);
            mav.addObject("articleDetail", ybkArticleEntity);
        }
        // 获取交易所列表
        FYbkExchangeQueryListReq exchangeReq = new FYbkExchangeQueryListReq();
        //exchangeReq.setStatus(EYbkExchangeStatus.NORMAL.getCode());
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(exchangeReq);
        mav.addObject("exchangeList", exchangeEntityList);
        return mav;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam(value = "id", required = false) Long id,
                             @RequestParam(value = "categoryId") Integer categoryId,
                             @RequestParam(value = "exchangeId") Integer exchangeId,
                             @RequestParam(value = "title") String title,
                             @RequestParam(value = "content") String content,
                             @RequestParam(value = "sort", required = false) Integer sort,
                             @RequestParam(value = "viewCount", required = false) Integer viewCount,
                             @RequestParam(value = "isShow") Integer isShow,
                             HttpServletRequest request) throws Exception {
        YBKArticleEntity ybkArticleEntity = new YBKArticleEntity();
        ybkArticleEntity.setCategoryId(categoryId);
        ybkArticleEntity.setExchangeId(exchangeId);
        ybkArticleEntity.setTitle(title);
        ybkArticleEntity.setContent(content);
        ybkArticleEntity.setSort(sort);
        ybkArticleEntity.setViewCount(viewCount);
        ybkArticleEntity.setIsShow(isShow);
        if (id != null && id != 0) {
            ybkArticleEntity.setId(id);
            ybkService.articleUpdate(ybkArticleEntity);
        } else {
            ybkArticleEntity.setCreated(new Date());
            ybkService.articleInsert(ybkArticleEntity);
        }
        return jumpForSuccess(request, "保存成功", "/ybk/article/list");
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public ModelAndView del(@RequestParam(value = "id") Long id, HttpServletRequest request) throws Exception {
        ybkService.articleDelete(id);
        return jumpForSuccess(request, "删除成功", "/ybk/article/list");
    }

    @RequestMapping(value = "/sort", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView sort(@RequestParam(value = "id") Long id,
                             @RequestParam(value = "sort") Integer sort,
                             HttpServletRequest request) throws Exception {
        YBKArticleEntity ybkArticleEntity = new YBKArticleEntity();
        ybkArticleEntity.setSort(sort);
        ybkArticleEntity.setId(id);
        ybkService.articleUpdate(ybkArticleEntity);
        return jumpForSuccess(request, "排序成功", "/ybk/article/list");
    }
}
