package com.fmall.bana.controller.api.ybk;

import com.caimao.bana.api.entity.req.ybk.FQueryYbkDaxinReq;
import com.caimao.bana.api.entity.req.ybk.FQueryYbkHelpDocReq;
import com.caimao.bana.api.entity.req.ybk.FYBKQueryActivityListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkNewAccountListReq;
import com.caimao.bana.api.entity.ybk.YBKActivityEntity;
import com.caimao.bana.api.entity.ybk.YBKArticleEntity;
import com.caimao.bana.api.entity.ybk.YbkHelpDocEntity;
import com.caimao.bana.api.service.ybk.IYBKAccountService;
import com.caimao.bana.api.service.ybk.IYBKService;
import com.caimao.bana.api.service.ybk.IYbkDaxinService;
import com.caimao.bana.api.service.ybk.IYbkHelpDocService;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.bana.common.api.exception.CustomerException;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.RedisUtils;
import com.fmall.bana.utils.exception.ApiException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮币卡内容相关API服务接口控制器
 * Created by wangxu on 2015/9/10.
 */
@Controller
@RequestMapping(value = "/api/ybk/content/")
public class YbkContentController extends BaseController {

    @Resource
    private IYBKService ybkService;

    @Resource
    private IYbkHelpDocService ybkHelpDocService;

    @Resource
    private IYbkDaxinService ybkDaxinService;

    @Resource
    private IYBKAccountService ybkAccountService;

    @Resource
    private RedisUtils redisUtils;


    /**
     * <p>获取活动列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/content/query_activity_list?exchange_id=15</p>
     * <p>请求方式：GET</p>
     *
     * @param exchangeId 交易所唯一ID
     * @param limit      每页显示条数，默认10条
     * @param page       当前页数，默认1
     * @return FYBKQueryActivityListReq
     * @throws Exception
     * @see FYBKQueryActivityListReq
     */
    @ResponseBody
    @RequestMapping(value = "/query_activity_list", method = RequestMethod.GET)
    public FYBKQueryActivityListReq queryActivityList(
            @RequestParam(value = "exchange_id", required = false) Integer exchangeId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) throws Exception {
        try {
            FYBKQueryActivityListReq req = new FYBKQueryActivityListReq();
            req.setExchangeId(exchangeId);
            req.setIsShow(0);
            req.setLimit(limit);
            req.setStart((page - 1) * limit);

            req = this.ybkService.queryActivityWithPage(req);
            // 获取活动提示语
            Object topic = this.redisUtils.get("_ybk_activity_topic_key");
            req.setTopic(topic != null ? topic.toString() : "");

            return req;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>根据活动ID，获取活动信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/content/get_activity?id=16</p>
     * <p>请求方式：GET</p>
     *
     * @param id 活动ID
     * @return YBKArticleEntity
     * @throws Exception
     * @see YBKArticleEntity
     */
    @ResponseBody
    @RequestMapping(value = "/get_activity", method = RequestMethod.GET)
    public YBKActivityEntity getActivityInfo(@RequestParam(value = "id") Long id) throws Exception {
        try {
            return this.ybkService.queryActivityById(id);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>获取打新列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/content/query_daxin_list?exchange_id=15</p>
     * <p>请求方式：GET</p>
     *
     * @param exchangeId 交易所唯一ID
     * @param limit      每页显示条数，默认10条
     * @param page       当前页数，默认1
     * @return FYBKQueryActivityListReq
     * @throws Exception
     * @see FYBKQueryActivityListReq
     */
    @ResponseBody
    @RequestMapping(value = "/query_daxin_list", method = RequestMethod.GET)
    public FQueryYbkDaxinReq queryDaxinList(
            @RequestParam(value = "exchange_id", required = false) Integer exchangeId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) throws Exception {
        try {
            FQueryYbkDaxinReq req = new FQueryYbkDaxinReq();
            req.setExchangeId(exchangeId);
            req.setIsShow("0");
            req.setLimit(limit);
            req.setStart((page - 1) * limit);
            return this.ybkDaxinService.queryYbkDaxinList(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    // 邮币卡帮助文档

    /**
     * <p>获取指定类型的帮助文档列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/content/help_doc_list</p>
     * <p>请求方式：GET/POST</p>
     *
     * @param categoryId 类型，默认为 0，所有的类型
     * @param limit      每页显示的数量，默认为20
     * @param page       当前第几页
     * @return 返回值
     * @throws Exception
     * @see com.caimao.bana.api.enums.ybk.EYbkHelpDocType
     */
    @ResponseBody
    @RequestMapping(value = "/help_doc_list", method = {RequestMethod.GET, RequestMethod.POST})
    public FQueryYbkHelpDocReq queryHelpDocList(
            @RequestParam(value = "categoryId", required = false, defaultValue = "0") Integer categoryId,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) throws Exception {
        try {
            FQueryYbkHelpDocReq req = new FQueryYbkHelpDocReq();
            if (categoryId != 0) req.setCategoryId(categoryId);
            req.setLimit(limit);
            req.setStart((page - 1) * limit);
            return this.ybkHelpDocService.queryYbkHelpDocList(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>根据ID获取帮助文档列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/content/get_help_doc</p>
     * <p>请求方式：GET/POST</p>
     *
     * @param id 帮助文档ID
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/get_help_doc", method = {RequestMethod.GET, RequestMethod.POST})
    public YbkHelpDocEntity getHelpDoc(
            @RequestParam(value = "id") Integer id
    ) throws Exception {
        try {
            return this.ybkHelpDocService.selectById(id);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>获取最新的10条开户信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/content/new_account_list</p>
     * <p>请求方式：GET/POST</p>
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/new_account_list", method = {RequestMethod.GET, RequestMethod.POST})
    public FYbkNewAccountListReq queryNewAccount() throws Exception {
        try {
            FYbkNewAccountListReq req = new FYbkNewAccountListReq();
            req.setLimit(10);
            return this.ybkAccountService.queryNewAccountApply(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>获取APP首页显示的那一句话与链接信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/content/get_app_index_article</p>
     * <p>请求方式：GET/POST</p>
     *
     * @return Map  title： 显示的文本标题  url： 点击打开的网页地址
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/get_app_index_article", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, String> getAppIndexArticle() throws Exception {
        try {
            Map<String, String> resMap = new HashMap<>();
            Object title = this.redisUtils.hGet("_ybkAppIndexArticle", "title");
            Object url = this.redisUtils.hGet("_ybkAppIndexArticle", "url");
            resMap.put("title", title == null ? "" : title.toString());
            resMap.put("url", url == null ? "" : url.toString());
            return resMap;
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


}
