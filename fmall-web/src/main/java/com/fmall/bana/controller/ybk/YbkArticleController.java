package com.fmall.bana.controller.ybk;

import com.caimao.bana.api.entity.req.content.FNoticeQueryListReq;
import com.caimao.bana.api.entity.req.ybk.*;
import com.caimao.bana.api.entity.res.content.FNoticeInfoRes;
import com.caimao.bana.api.entity.ybk.YBKArticleEntity;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import com.caimao.bana.api.enums.EYbkExchangeStatus;
import com.caimao.bana.api.enums.ybk.EArticleCategory;
import com.caimao.bana.api.enums.ybk.EYbkHelpDocType;
import com.caimao.bana.api.service.content.INoticeService;
import com.caimao.bana.api.service.ybk.IYBKService;
import com.caimao.bana.api.service.ybk.IYbkHelpDocService;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.PageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 文章相关控制器
 * Created by Administrator on 2015/9/10.
 */
@Controller
@RequestMapping(value = "/ybk/article")
public class YbkArticleController extends BaseController {

    @Resource
    private IYBKService ybkService;

    @Resource
    private IYbkHelpDocService ybkHelpDocService;

    @Resource
    private INoticeService noticeService;

    // 获取置顶的公告
    @ResponseBody
    @RequestMapping(value = "/top_notice", method = RequestMethod.GET)
    public FNoticeInfoRes getTopNotice() throws Exception {
        FNoticeQueryListReq req = new FNoticeQueryListReq();
        req.setListShow(1);
        req.setTopShow(1);
        req.setStart(0);
        req.setLimit(1);
        req.setOrderColumn("created");
        req.setOrderDir("DESC");
        req = this.noticeService.queryList(req);
        return req.getItems().get(0);
    }

    /**
     * 移动端请求公告列表
     *
     * @param categoryId 公告类型
     * @param exchangeId 交易所id
     * @param limit      每页显示条目数
     * @param page       第几页
     * @return 返回列表页面
     * @throws Exception
     */
    @RequestMapping(value = "/m_list.html")
    public ModelAndView queryNoticeList(
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "exchangeId", required = false) Integer exchangeId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/article/m_list");

        FYBKQueryArticleSimpleListReq fYbkQueryArticleListReq = new FYBKQueryArticleSimpleListReq();
        fYbkQueryArticleListReq.setCategoryId(categoryId);
        fYbkQueryArticleListReq.setExchangeId(exchangeId);
        fYbkQueryArticleListReq.setIsShow(0);
        fYbkQueryArticleListReq.setLimit(limit);
        fYbkQueryArticleListReq.setStart((page - 1) * limit);

        FYBKQueryArticleSimpleListReq list = this.ybkService.queryArticleSimpleList(fYbkQueryArticleListReq);
        mav.addObject("list", list);

        mav.addObject("categoryId", categoryId);
        mav.addObject("exchangeId", exchangeId);
        mav.addObject("limit", limit);
        mav.addObject("page", page);
        mav.addObject("totalPage", Math.ceil(list.getTotalCount() / list.getLimit()));

        return mav;
    }

    /**
     * 移动端请求公告详情
     *
     * @param id 公告id
     * @return 返回公告详情页面
     * @throws Exception
     */
    @RequestMapping(value = "/m_detail.html")
    public ModelAndView queryNoticeDetail(@RequestParam(value = "id", required = false) Long id) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/article/m_detail");
        YBKArticleEntity detail = this.ybkService.queryArticleById(id);
        mav.addObject("detail", detail);
        this.ybkService.readArticle(id);
        return mav;
    }

    /**
     * pc端请求公告列表
     *
     * @param categoryId 公告类型
     * @param exchangeId 交易所id
     * @param limit      每页显示条目数
     * @param page       第几页
     * @return 返回列表页面
     * @throws Exception
     */
    @RequestMapping(value = "/list.html")
    public ModelAndView queryNewsList(
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "exchangeId", required = false, defaultValue = "0") Integer exchangeId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/article/list");

        // 获取所有交易所
        if (categoryId != Integer.parseInt(EArticleCategory.WZBK.getCode()) &&
                categoryId != Integer.parseInt(EArticleCategory.SCJG.getCode())) {
            FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
            req.setStatus(EYbkExchangeStatus.NORMAL.getCode());
            List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(req);
            mav.addObject("eList", exchangeEntityList);
        }

        mav.addObject("leftExchangeId", exchangeId);
        mav.addObject("categoryId", categoryId);

        FYBKQueryArticleListReq fYbkQueryArticleListReq = new FYBKQueryArticleListReq();
        fYbkQueryArticleListReq.setCategoryId(categoryId);
        if (exchangeId > 0) {
            fYbkQueryArticleListReq.setExchangeId(exchangeId);
        }
        fYbkQueryArticleListReq.setIsShow(0);
        fYbkQueryArticleListReq.setLimit(limit);
        fYbkQueryArticleListReq.setStart((page - 1) * limit);

        FYBKQueryArticleListReq list = this.ybkService.queryArticleWithPage(fYbkQueryArticleListReq);
        if (list.getItems() != null) {
            PageUtils pageUtils = new PageUtils(page, limit, list.getTotalCount(), String.format("/ybk/article/list.html?categoryId=%s&exchangeId=%s&page=", categoryId, exchangeId));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }
        mav.addObject("list", list);
        return mav;
    }

    /**
     * pc端请求公告详情
     *
     * @param id 公告id
     * @return 返回公告详情页面
     * @throws Exception
     */
    @RequestMapping(value = "/detail.html")
    public ModelAndView queryNewDetail(@RequestParam(value = "id", required = false) Long id,
                                       @RequestParam(value = "exchangeId", required = false) Integer exchangeId) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/article/detail");
        YBKArticleEntity detail = this.ybkService.queryArticleById(id);
        mav.addObject("detail", detail);
        mav.addObject("exchangeId", exchangeId);
        if (detail != null) {
            this.ybkService.readArticle(id);
            FYBKQueryArticleIdReq req = new FYBKQueryArticleIdReq();
            req.setCategoryId(detail.getCategoryId());
            req.setOrder(1);
            req.setThan(1);
            if (exchangeId != null && exchangeId > 0) {
                req.setExchangeId(exchangeId);
            }
            req.setCreated(detail.getCreated());
            mav.addObject("pre", this.ybkService.queryArticleId(req));
            req.setOrder(-1);
            req.setThan(-1);
            mav.addObject("next", this.ybkService.queryArticleId(req));
        }
        return mav;
    }

    /**
     * 綁定銀行卡的幫助文檔下載
     *
     * @return
     */
    @RequestMapping(value = "/bind_help.html", method = RequestMethod.GET)
    public ModelAndView bindBankHelpDoc() {
        return new ModelAndView("/ybk/article/bind_help");
    }

    /**
     * 客户端下载页面
     *
     * @return
     */
    @RequestMapping(value = "/download.html", method = RequestMethod.GET)
    public ModelAndView download() {
        return new ModelAndView("/ybk/article/download");
    }

    /**
     * 打新的教程页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/daxin.html")
    public ModelAndView daxin() throws Exception {
        return new ModelAndView("/ybk/article/daxin");
    }

    /**
     * 帮助文档列表
     *
     * @param categoryId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/help_doc_list.html")
    public ModelAndView helpDocList(
            @RequestParam(value = "categoryId", required = false, defaultValue = "1") Integer categoryId
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/help/list");
        Integer limit = 100;
        FQueryYbkHelpDocReq req = new FQueryYbkHelpDocReq();
        req.setCategoryId(categoryId);
        req.setLimit(limit);
        req.setStart(0);
        req = this.ybkHelpDocService.queryYbkHelpDocList(req);

        mav.addObject("list", req);

        // 帮助分类
        List<HashMap<String, String>> categoryList = new ArrayList<>();
        for (EYbkHelpDocType type : EYbkHelpDocType.values()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code", type.getCode().toString());
            map.put("val", type.getValue());
            categoryList.add(map);
        }
        mav.addObject("categoryList", categoryList);
        mav.addObject("curCatId", categoryId);
        return mav;
    }

    /**
     * 显示单独的一个帮助页面
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/help_doc.html")
    public ModelAndView helpDoc(
            @RequestParam(value = "id") Integer id
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/ybk/help/detail");
        mav.addObject("detail", this.ybkHelpDocService.selectById(id));
        // 帮助分类
        List<HashMap<String, String>> categoryList = new ArrayList<>();
        for (EYbkHelpDocType type : EYbkHelpDocType.values()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code", type.getCode().toString());
            map.put("val", type.getValue());
            categoryList.add(map);
        }
        mav.addObject("categoryList", categoryList);

        return mav;
    }

}
