package com.caimao.zeus.controller.gjs;

import com.alibaba.fastjson.JSONObject;
import com.caimao.gjs.api.entity.GjsArticleCategoryEntity;
import com.caimao.gjs.api.entity.GjsArticleIndexEntity;
import com.caimao.gjs.api.entity.article.GjsArticleEntity;
import com.caimao.gjs.api.entity.req.FQueryArticleIndexReq;
import com.caimao.gjs.api.entity.req.FQueryArticleReq;
import com.caimao.gjs.api.service.IArticleService;
import com.caimao.zeus.admin.controller.BaseController;
import com.caimao.zeus.util.DateUtil;
import com.caimao.zeus.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 贵金属文章管理
 */
@RestController
@RequestMapping("/gjs/article")
public class GjsArticleController extends BaseController {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(GjsArticleController.class);

    @Resource
    private IArticleService articleService;

    @Value("${picturePath}")
    protected String PICTURE_PATH;

    @Value("${pub.user}")
    protected String PUB_USER;

    @Value("${domainUrl}")
    protected String DOMAIN_URL;

    /**
     * 抓取的文章列表
     *
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView list(FQueryArticleReq req) throws Exception {
        String original = req.getDateEnd();
        ModelAndView mav = new ModelAndView("gjs/article/list");
        if (null != req.getDateEnd() && !"".equals(req.getDateEnd())) {
            req.setDateEnd(DateUtil.addDays(req.getDateEnd(), DateUtil.DATA_TIME_PATTERN_5, 1));
        }
        req.setLimit(50);
        req.setCurrentPage(req.getCurrentPage());
        req = articleService.queryArticleListForManage(req);
        req.setDateEnd(original);
        mav.addObject("list", req);
        return mav;
    }

    /**
     * 文章添加，编辑的页面
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ModelAndView save(
            @RequestParam(value = "id", required = false) Long id
    ) throws Exception{
        ModelAndView mav = new ModelAndView("gjs/article/save");

        if(id != null && id != 0){
            GjsArticleEntity articleEntity = this.articleService.getArticleById(id);
            mav.addObject("articleDetail", articleEntity);
        }
        return mav;
    }

    /**
     * 文章保存、添加的东东
     * @param id
     * @param categoryId
     * @param sourceName
     * @param sourceUrl
     * @param title
     * @param content
     * @param sort
     * @param viewCount
     * @param isShow
     * @param isHot
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam(value = "id", required = false) Long id,
                             @RequestParam(value = "categoryId") Integer categoryId,
                             @RequestParam(value = "sourceName") String sourceName,
                             @RequestParam(value = "sourceUrl") String sourceUrl,
                             @RequestParam(value = "title") String title,
                             @RequestParam(value = "content") String content,
                             @RequestParam(value = "sort", required = false) Integer sort,
                             @RequestParam(value = "viewCount", required = false) Integer viewCount,
                             @RequestParam(value = "isShow") Integer isShow,
                             @RequestParam(value = "isHot") Integer isHot,
                             HttpServletRequest request) throws Exception{
        GjsArticleEntity articleEntity = new GjsArticleEntity();
        articleEntity.setCategoryId(categoryId);
        articleEntity.setSourceName(sourceName);
        articleEntity.setSourceUrl(sourceUrl);
        articleEntity.setTitle(title);
        articleEntity.setContent(content);
        articleEntity.setSort(sort);
        articleEntity.setViewCount(viewCount);
        articleEntity.setIsShow(isShow);
        articleEntity.setIsHot(isHot);
        if(id != null && id != 0){
            articleEntity.setId(id);
            this.articleService.updateArticleInfo(articleEntity);
        }else{
            articleEntity.setCreated(DateUtil.convertDateToString(DateUtil.DATA_TIME_PATTERN_1, new Date()));
            this.articleService.addArticleInfo(articleEntity);
        }
        return jumpForSuccess(request, "保存成功", "/gjs/article/list");
    }

    /**
     * 删除的东东
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public ModelAndView del(@RequestParam(value = "id") Long id, HttpServletRequest request) throws Exception{
        this.articleService.delArticle(id);
        return jumpForSuccess(request, "删除成功", "/gjs/article/list");
    }

    /**
     * 文章标签列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "category", method = RequestMethod.GET)
    public ModelAndView category() throws Exception {
        ModelAndView mav = new ModelAndView("gjs/article/ArticleCategory");
        mav.addObject("list", articleService.queryGjsArticleCategoryList());
        return mav;
    }

    /**
     * 文章标签编辑
     */
    @RequestMapping(value = "category/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam(value = "id", required = false) Long id) throws Exception {
        ModelAndView mav = new ModelAndView("gjs/article/ArticleCategoryEdit");
        GjsArticleCategoryEntity entity = null;
        if (null != id && 0 != id) {
            entity = this.articleService.getArticleCategoryById(id);
            mav.addObject("entity", entity);
        } else {
            entity = new GjsArticleCategoryEntity();
            entity.setId(0L);
            mav.addObject("entity", entity);
        }
        return mav;
    }

    /**
     * 文章标签保存
     */
    @RequestMapping(value = "category/save", method = RequestMethod.POST)
    public ModelAndView categorySave(@RequestParam(value = "id", required = false) Long id, GjsArticleCategoryEntity entity, HttpServletRequest request) throws Exception {
        if (2 > entity.getName().length() || 6 < entity.getName().length()) {
            return jumpForFail(request, "标签长度必须在2-6之间！", "/gjs/article/category/edit?id=" + id);
        }

        if (id != null && id != 0) {
            this.articleService.updateArticleCategory(entity);
        } else {
            this.articleService.insertArticleCategory(entity);
        }
        return jumpForSuccess(request, "保存成功", "/gjs/article/category");
    }

    /**
     * 文章标签删除
     */
    @RequestMapping(value = "category/del", method = RequestMethod.GET)
    public ModelAndView categoryDel(@RequestParam(value = "id") Long id, HttpServletRequest request) throws Exception {
        GjsArticleCategoryEntity entity = articleService.getArticleCategoryById(id);
        if (null != entity) {
            int category = entity.getId().intValue();
            if (0 == articleService.queryGjsArticleIndexCategoryList(category).size()) {
                this.articleService.deleteArticleCategoryById(id);
                return jumpForSuccess(request, "删除成功", "/gjs/article/category");
            }
            return jumpForFail(request, "该标签已经被使用，不能删除！", "/gjs/article/category");
        }
        return jumpForFail(request, "参数不正确，删除失败！", "/gjs/article/category");
    }

    /**
     * 首页文章列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView index(FQueryArticleIndexReq req) throws Exception {
        ModelAndView mav = new ModelAndView("gjs/article/ArticleIndex");
        String originalCreated = req.getDateEnd();
        if (null != req.getDateEnd() && !"".equals(req.getDateEnd())) {
            req.setDateEnd(DateUtil.addDays(req.getDateEnd(), DateUtil.DATA_TIME_PATTERN_5, 1));
        }
        String originalPub = req.getPubEnd();
        if (null != req.getPubEnd() && !"".equals(req.getPubEnd())) {
            req.setPubEnd(DateUtil.addDays(req.getPubEnd(), DateUtil.DATA_TIME_PATTERN_5, 1));
        }
        req.setLimit(20);
        req.setCurrentPage(req.getCurrentPage());
        req = articleService.queryGjsArticleIndexList(req);
        req.setDateEnd(originalCreated);
        req.setPubEnd(originalPub);
        mav.addObject("list", req);
        return mav;
    }

    /**
     * 首页文章编辑
     */
    @RequestMapping(value = "index/edit", method = RequestMethod.GET)
    public ModelAndView indexEdit(@RequestParam(value = "id", required = false) Long id) throws Exception {
        ModelAndView mav = new ModelAndView("gjs/article/ArticleIndexEdit");
        GjsArticleIndexEntity entity = null;
        if (null != id && 0 != id) {
            entity = this.articleService.getArticleIndexById(id);
            mav.addObject("share_url", DOMAIN_URL + "/upload/article/html/index_" + id + ".html");
            if (entity.getTitle().contains("\"")) {
                entity.setTitle(entity.getTitle().replace("\"", "&quot;"));
            }
            mav.addObject("entity", entity);
        } else {
            entity = new GjsArticleIndexEntity();
            entity.setId(0L);
            entity.setIsShow(1);
            entity.setStatus(-1);
            entity.setView(0);
            entity.setCreated(String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()));
            mav.addObject("entity", entity);
        }
        mav.addObject("domain_url", DOMAIN_URL);
        mav.addObject("categoryList", articleService.queryGjsArticleCategoryList());

        String[] user = PUB_USER.split(";");
        List<String> userList = new ArrayList<>();
        for (String s : user) {
            userList.add(s);
        }
        mav.addObject("userList", userList);

        return mav;
    }

    /**
     * 首页文章保存
     */
    @RequestMapping(value = "index/save", method = RequestMethod.POST)
    public ModelAndView indexSave(@RequestParam(value = "id", required = false) Long id, GjsArticleIndexEntity entity, HttpServletRequest request) throws Exception {
        if (null == entity.getTitle().trim() || "".equals(entity.getTitle().trim()) || 0 == entity.getTitle().length()) {
            return jumpForFail(request, "文章标题不能为空！", "/gjs/article/index/edit?id=" + id);
        }
        if (null == entity.getCategory() || "".equals(entity.getCategory())) {
            return jumpForFail(request, "文章标签不能为空！", "/gjs/article/index/edit?id=" + id);
        }

        if (id != null && id != 0) {
            this.articleService.updateArticleIndex(entity);
        } else {
            entity = this.articleService.insertArticleIndex(entity);
        }

        Map<String, Object> map = new HashMap<>();
        entity.setPub(entity.getPub() + generateRandomSecond());
        map.put("entity", entity);
        map.put("request", request);
        map.put("htmlUrl", DOMAIN_URL);
        try {
            FileUtil.generateHTML("WEB-INF/jsp/gjs/article/", "ArticleIndexDetail.ftl", "index_" + entity.getId() + ".html", map, request.getSession().getServletContext(), PICTURE_PATH + "article" + File.separator + "html" + File.separator);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jumpForSuccess(request, "保存成功", "/gjs/article/index");
    }

    /**
     * 生成随机的秒
     *
     * @return String
     */
    private String generateRandomSecond() {
        String result = ":";
        String[] array = {"1", "2", "3", "4", "5"};
        result += getRandomChar(array);
        result += getRandomChar(array);
        return result;
    }

    /**
     * 从一个数组中返回随机的字符
     *
     * @param array 数组
     * @return String
     */
    private String getRandomChar(String[] array) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 1; i++) {
            sb.append(array[r.nextInt(5)]);
        }
        return sb.toString();
    }

    /**
     * 首页文章删除
     */
    @RequestMapping(value = "index/del", method = RequestMethod.GET)
    public ModelAndView indexDel(@RequestParam(value = "id") Long id, HttpServletRequest request) throws Exception {
        GjsArticleIndexEntity entity = articleService.getArticleIndexById(id);
        if (0 < entity.getStatus()) {
            return jumpForFail(request, "置顶的文章不能删除！", "/gjs/article/index");
        }
        this.articleService.deleteArticleIndexById(id);
        return jumpForSuccess(request, "删除成功", "/gjs/article/index");
    }

    /**
     * 首页文章中的图片上传
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "index/upload", method = RequestMethod.POST)
    public @ResponseBody String upload(MultipartHttpServletRequest request) throws Exception {
        JSONObject obj = new JSONObject();
        String dest = "";
        String infactDest = "";
        List<MultipartFile> files = request.getFiles("imgFile");
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String originalName = file.getOriginalFilename();
                StringBuffer fileName = new StringBuffer(String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL" + originalName.substring(originalName.lastIndexOf(".")).toLowerCase(), new Date()));
                infactDest = new StringBuffer().append("article").append(File.separatorChar).append("images").append(File.separatorChar).append(fileName).toString();
                dest = new StringBuffer(PICTURE_PATH).append(infactDest).toString();
                File destFile = new File(dest);
                if (!destFile.getParentFile().exists()) {
                    if (!destFile.getParentFile().mkdirs()) {
                        return "";
                    }
                }
                file.transferTo(destFile);
            }
        }
        obj.put("error", 0);
        obj.put("url", DOMAIN_URL + File.separator + "upload" + File.separator + infactDest);
        return obj.toJSONString();
    }

    /**
     * 根据ID修改是否显示
     */
    @RequestMapping(value = "index/show", method = RequestMethod.GET)
    public String show(@RequestParam(value = "id") Long id) throws Exception {
        GjsArticleIndexEntity entity = articleService.getArticleIndexById(id);
        int oldStatus = entity.getStatus();
        if (null != entity) {
            if (0 < entity.getStatus()) {
                entity.setStatus(0);
            }

            // 将原置顶-1
            if (0 < oldStatus) {
                List<GjsArticleIndexEntity> topList = articleService.queryTopGjsArticleIndexList();
                for (GjsArticleIndexEntity entity1 : topList) {
                    if (oldStatus < entity1.getStatus()) {
                        entity1.setStatus(entity1.getStatus() - 1);
                        articleService.updateArticleIndex(entity1);
                    }
                }
            }

            if (0 == entity.getIsShow()) {
                entity.setIsShow(1);
                articleService.updateArticleIndex(entity);
                return "ok";
            } else {
                return "no";
            }
        }
        return "error";
    }

    /**
     * 设置显示时间
     */
    @RequestMapping(value = "index/setShow", method = RequestMethod.GET)
    public String setShow(@RequestParam(value = "id") Long id, @RequestParam(value = "d") String d, HttpServletRequest request) throws Exception {
        GjsArticleIndexEntity entity = articleService.getArticleIndexById(id);
        if (null != entity) {
            entity.setPub(d);
            entity.setStatus(0);
            entity.setIsShow(0);
            articleService.updateArticleIndex(entity);

            Map<String, Object> map = new HashMap<>();
            entity.setPub(entity.getPub() + generateRandomSecond());
            map.put("entity", entity);
            map.put("request", request);
            map.put("htmlUrl", DOMAIN_URL);
            try {
                FileUtil.generateHTML("WEB-INF/jsp/gjs/article/", "ArticleIndexDetail.ftl", "index_" + entity.getId() + ".html", map, request.getSession().getServletContext(), PICTURE_PATH + "article" + File.separator + "html" + File.separator);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "ok";
        }
        return "error";
    }

    /**
     * 显示设置置顶
     */
    @RequestMapping(value = "index/top", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String top(@RequestParam(value = "id") Long id) throws Exception {
        String returnString = "<select id=\"select\" style=\"border: 1px solid #ccc; width: 200px;\">";
        List<GjsArticleIndexEntity> topList = articleService.queryTopGjsArticleIndexList();
        if (0 < topList.size()) {
            for (GjsArticleIndexEntity entity : topList) {
                returnString += "<option style=\"padding: 5px 10px;\" value=\"" + String.valueOf(entity.getStatus()) + "\">置顶" + String.valueOf(entity.getStatus()) + "</option>";
            }
        }
        return returnString + "<option value=\"99\">置顶最新</option></select>" +
                "&nbsp;<input type=\"button\" class=\"btn btn-default\" style=\"padding-bottom: 3px;\" value=\" 设 置 \" onClick=\"setTop('" + id + "');\" />" +
                "&nbsp;<input type=\"button\" class=\"btn btn-default\" style=\"padding-bottom: 3px;\" value=\"取消置顶\" onClick=\"unTop('" + id + "');\" />";
    }

    /**
     * 取消置顶
     */
    @RequestMapping(value = "index/unTop", method = RequestMethod.GET)
    public String unTop(@RequestParam(value = "id") Long id) throws Exception {
        GjsArticleIndexEntity entity = articleService.getArticleIndexById(id);
        int oldStatus = entity.getStatus();
        if (null != entity) {
            if (0 == entity.getStatus()) {
                return "no";
            }
            entity.setStatus(0);
            articleService.updateArticleIndex(entity);
            articleService.resetStatus(oldStatus);

            // 将原置顶-1
            List<GjsArticleIndexEntity> topList = articleService.queryTopGjsArticleIndexList();
            for (GjsArticleIndexEntity entity1 : topList) {
                if (oldStatus < entity1.getStatus()) {
                    entity1.setStatus(entity1.getStatus() - 1);
                    articleService.updateArticleIndex(entity1);
                }
            }

            return "ok";
        }
        return "error";
    }

    /**
     * 设置置顶
     */
    @RequestMapping(value = "index/setTop", method = RequestMethod.GET)
    public String setTop(@RequestParam(value = "id") Long id, @RequestParam(value = "status") int targetStatus) throws Exception {
        logger.info(" 目标状态：" + targetStatus);

        GjsArticleIndexEntity entity = articleService.getArticleIndexById(id);
        int currentStatus = entity.getStatus();
        logger.info(" 当前状态：" + currentStatus);

        if (null != entity) {
            // 不能重复设置
            if (targetStatus == currentStatus) {
                return "no";
            }

            List<GjsArticleIndexEntity> topList = articleService.queryTopGjsArticleIndexList();

            if (0 == currentStatus) {
                for (GjsArticleIndexEntity entity1 : topList) {
                    if (targetStatus < (entity1.getStatus() + 1)) {
                        entity1.setStatus(entity1.getStatus() + 1);
                        articleService.updateArticleIndex(entity1);
                    }
                }
            }

            // 小 -> 大
            if (targetStatus > currentStatus) {
                if (0 < currentStatus) {
                    for (GjsArticleIndexEntity entity1 : topList) {
                        if (currentStatus < entity1.getStatus() && targetStatus >= entity1.getStatus()) {
                            entity1.setStatus(entity1.getStatus() - 1);
                            articleService.updateArticleIndex(entity1);
                        }
                    }
                }
            }

            // 大 -> 小
            if (targetStatus < currentStatus) {
                for (GjsArticleIndexEntity entity1 : topList) {
                    if (currentStatus > entity1.getStatus() && targetStatus <= entity1.getStatus()) {
                        entity1.setStatus(entity1.getStatus() + 1);
                        articleService.updateArticleIndex(entity1);
                    }
                }
            }

            if (99 == targetStatus) {
                if (0 < topList.size() && topList.size() == currentStatus) {
                    return "no";
                }
                if (0 == currentStatus) {
                    entity.setStatus(topList.size() + 1);
                } else {
                    entity.setStatus(topList.size());
                }
            } else {
                entity.setStatus(targetStatus);
            }
            articleService.updateArticleIndex(entity);

            return "ok";
        }
        return "error";
    }

}
