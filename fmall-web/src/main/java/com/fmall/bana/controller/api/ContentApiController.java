package com.fmall.bana.controller.api;

import com.caimao.bana.api.entity.content.BanaBannerEntity;
import com.caimao.bana.api.entity.req.content.FMsgQueryListReq;
import com.caimao.bana.api.entity.req.content.FQueryBannerListReq;
import com.caimao.bana.api.service.content.IBannerService;
import com.caimao.bana.api.service.content.IMessageService;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.gjs.api.entity.GjsArticleIndexEntity;
import com.caimao.gjs.api.entity.article.GjsArticleEntity;
import com.caimao.gjs.api.entity.article.GjsArticleJin10Entity;
import com.caimao.gjs.api.entity.req.FQueryArticleIndexReq;
import com.caimao.gjs.api.entity.req.FQueryArticleReq;
import com.caimao.gjs.api.entity.req.FQueryGjsArticleJin10Req;
import com.caimao.gjs.api.service.IArticleService;
import com.caimao.gjs.api.utils.DateUtil;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.CommonStringUtils;
import com.fmall.bana.utils.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * 网站内容相关的API接口（消息、系统公告）
 * </p>
 * Created by Administrator on 2015/10/10.
 */
@Controller
@RequestMapping(value = "/api/content")
public class ContentApiController extends BaseController {

    @Value("${ybk_url}")
    private String fmallUrl;

    @Value("${domainUrl}")
    protected String DOMAIN_URL;

    @Resource
    private IMessageService messageService;

    @Resource
    private IArticleService articleService;

    @Resource
    private IBannerService bannerService;

    /**
     * <p>消息列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/content/message_list</p>
     * <p>请求方式：GET</p>
     *
     * @param token     登陆后返回的token
     * @param pushTypes 需要返回的推送消息类型，多个值使用 , 号分隔
     * @param limit     每页显示的条数
     * @param page      当前第几页
     * @param appType   app类型 2贵金属
     * @return 消息列表
     * @throws Exception
     * @see com.caimao.bana.api.entity.req.content.FMsgQueryListReq
     * @see com.caimao.bana.api.entity.content.TpzPushMsgEntity
     */
    @ResponseBody
    @RequestMapping(value = "/message_list", method = {RequestMethod.GET, RequestMethod.POST})
    public FMsgQueryListReq queryMessageList(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "pushTypes", required = false) String pushTypes,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "appType", required = false) Integer appType
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FMsgQueryListReq req = new FMsgQueryListReq();
            if (pushTypes != null) {
                List<String> typeList = new ArrayList<>();
                String[] pushStrArr = pushTypes.split(",");
                for (String p : pushStrArr) {
                    if (!Objects.equals(p, "")) typeList.add(p);
                }
                req.setPushTypes(typeList);
            }
            if(appType != null && appType == 2){
                List<String> typeList = new ArrayList<>();
                typeList.add("12");
                typeList.add("14");
                typeList.add("15");
                typeList.add("16");
                typeList.add("17");
                req.setPushTypes(typeList);
            }
            req.setPushUserId(userId.toString());
            req.setLimit(limit);
            req.setStart((page - 1) * limit);

            return this.messageService.queryMsgList(req);

        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>变更所有消息未已读</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/content/msg_read_all</p>
     * <p>请求方式：GET</p>
     *
     * @param token 登陆后返回的token
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/msg_read_all", method = {RequestMethod.GET, RequestMethod.POST})
    public Map msgReadAll(
            @RequestParam(value = "token") String token
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            this.messageService.msgReadAll(userId);
            return CommonStringUtils.mapReturn(true);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>获取未读的消息数量</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/content/get_not_read_num</p>
     * <p>请求方式：GET</p>
     *
     * @param token 登陆后返回的token
     * @param pushTypes 类型
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/get_not_read_num", method = {RequestMethod.GET, RequestMethod.POST})
    public Integer getNotReadNum(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "pushTypes", required = false) String pushTypes
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            List<String> typeList = new ArrayList<>();
            if (pushTypes != null) {
                String[] pushStrArr = pushTypes.split(",");
                for (String p : pushStrArr) {
                    if (!Objects.equals(p, "")) typeList.add(p);
                }
            }
            return this.messageService.getNotReadNum(userId, typeList);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>贵金属公告列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/content/gjs_notice_list</p>
     * <p>请求方式：POST</p>
     *
     * @param type  公告文章的类型编号，选填，默认返回所有的
     * @param isHot 是否是热门文章 0 不热门 1 热门
     * @param limit 每页显示的条数，默认是20
     * @param page  当前显示第几页
     * @param id 返回当前id之后的最新数据
     * @return 公告列表
     * @throws Exception
     * @see com.caimao.gjs.api.entity.req.FQueryArticleReq
     * @see com.caimao.gjs.api.entity.article.GjsArticleEntity
     */
    @ResponseBody
    @RequestMapping(value = "/gjs_notice_list", method = {RequestMethod.GET, RequestMethod.POST})
    public FQueryArticleReq queryGjsArticleList(
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "isHot", required = false) Integer isHot,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "id", required = false) Integer id
    ) throws Exception {
        try {
            FQueryArticleReq req = new FQueryArticleReq();
            req.setCategoryId(type);
            req.setIsShow(0);
            req.setIsHot(isHot);
            req.setLimit(limit);
            req.setStart((page - 1) * limit);
            if (null != id && 0 < id) {
                GjsArticleEntity entity = this.articleService.getArticleById(Long.valueOf(id));
                if (null != entity) {
                    req.setDateStart(DateUtil.addDateSecond(entity.getCreated(), 1));
                }
            }
            return this.articleService.queryArticleList(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>金十首页资讯</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/content/gjs_jin10_list</p>
     * <p>请求方式：GET</p>
     *
     * @param limit 每页显示的条数，默认是20
     * @param page  当前显示第几页
     * @param id 返回当前id之后的最新数据
     * @throws Exception
     * @see com.caimao.gjs.api.entity.req.FQueryGjsArticleJin10Req
     * @see com.caimao.gjs.api.entity.article.GjsArticleJin10Entity
     * <p/>
     * id - 主键ID
     * timeId - 时间ID（年月日时分秒（14位） + 毫秒（3位） + 100）
     * category - 1：一般新闻或普通新闻；2：重要新闻；3：一般数据；4：重要数据
     * time - 时间
     * content - 内容
     * star - 1,2,3,4,5 星级数据
     * beforeValue - 前值
     * expect - 预期
     * infact - 实际值
     * result - 1：利空；2：利多；3：影响较小；4：利多原油；5：利空原油；
     */
    @ResponseBody
    @RequestMapping(value = "/gjs_jin10_list", method = RequestMethod.GET)
    public FQueryGjsArticleJin10Req queryGjsArticleJin10List(
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "id", required = false) Integer id
    ) throws Exception {
        try {
            FQueryGjsArticleJin10Req req = new FQueryGjsArticleJin10Req();
            req.setLimit(limit);
            req.setStart((page - 1) * limit);
            if (null != id && 0 < id) {
                GjsArticleJin10Entity entity = this.articleService.getArticleJin10ById(Long.valueOf(id));
                if (null != entity) {
                    req.setTimeId(entity.getTimeId());
                }
            }
            return articleService.queryGjsArticleJin10List(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>贵金属公告内容接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/content/gjs_notice</p>
     * <p>请求方式：POST</p>
     *
     * @param id 公告文章ID
     * @return 公告文章信息
     * @throws Exception
     * @see com.caimao.gjs.api.entity.article.GjsArticleEntity
     */
    @ResponseBody
    @RequestMapping(value = "/gjs_notice", method = {RequestMethod.GET, RequestMethod.POST})
    public GjsArticleEntity getGjsArticleInfo(
            @RequestParam(value = "id") Long id
    ) throws Exception {
        try {
            return this.articleService.getArticleById(id);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>Banner 列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/content/banner</p>
     * <p>请求方式：POST</p>
     *
     * @param appType banner类型，gjs 贵金属的 ybk 邮币卡的
     * @return banner List
     * @throws Exception
     * @see com.caimao.bana.api.entity.req.content.FQueryBannerListReq
     * @see com.caimao.bana.api.entity.content.BanaBannerEntity
     */
    @ResponseBody
    @RequestMapping(value = "/banner", method = {RequestMethod.GET, RequestMethod.POST})
    public FQueryBannerListReq queryBanner(
            @RequestParam(value = "appType") String appType
    ) throws Exception {
        try {
            FQueryBannerListReq req = new FQueryBannerListReq();
            req.setAppType(appType);
            req.setIsShow(0);
            req = this.bannerService.queryBannerList(req);
            if (req.getItems() != null) {
                for (BanaBannerEntity entity : req.getItems()) {
                    if (entity.getPcPic() != null && !Objects.equals(entity.getPcPic(), "")) {
                        entity.setPcPic(this.fmallUrl + entity.getPcPic());
                    }
                    if (entity.getAppPic() != null && !Objects.equals(entity.getAppPic(), "")) {
                        entity.setAppPic(this.fmallUrl + entity.getAppPic());
                    }
                }
            }
            return req;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>获取最新盈利接口</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/content/profit</p>
     * <p>请求方式：GET</p>
     *
     * @return 返回手机号和盈利金额（格式：137****1234 1000元）
     * @throws Exception
     */
    @RequestMapping(value = "profit", method = RequestMethod.GET)
    public
    @ResponseBody
    String profit() throws Exception {
        return generateRandomPhone() + " " + getRandomNumber();
    }

    /**
     * 生成随机的手机号码
     * 第1位规则：1
     * 第2位规则：3,4,5,7,8 （可以自己定义）
     * 第3位规则："0", "1", "2", "3", "4", "5", "6", "7", "8", "9"（可以自己定义）
     * 第4-7位规则：4个*
     * 第8-11位规则：4个随机数
     *
     * @return String
     */
    private String generateRandomPhone() {
        String result = "1";
        String[] array1 = {"3", "4", "5", "7", "8"};
        String[] array2 = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        result += getRandomChar(array1);
        result += getRandomChar(array2);
        result += "****";
        DecimalFormat df = new DecimalFormat("0000");
        result = result + df.format((int) (Math.random() * 9999));
        return result;
    }

    /**
     * 从一个数组中返回随机的字符（支持字母）
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
     * 生成随机的金额
     * 备注：100元-1000元的出现概率是90%
     * 备注：1000元-5000元的出现概率是10%
     *
     * @return String
     */
    private String getRandomNumber() {
        String returnString = "";
        Random random = new Random();
        Double d = random.nextDouble();
        // 概率小于0.9时，输出100-1000
        if (0.9 >= d) {
            returnString = ThreadLocalRandom.current().nextInt(100, 1000) + "元";
        } else {
            returnString = ThreadLocalRandom.current().nextInt(1001, 5000) + "元";
        }
        return returnString;
    }

    /**
     * <p>首页文章列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/content/gjs_article_list</p>
     * <p>请求方式：GET</p>
     *
     * @param limit 每页显示的条数，默认是20
     * @param page  当前显示第几页
     * @throws Exception
     * @see com.caimao.gjs.api.entity.req.FQueryArticleIndexReq
     * @see com.caimao.gjs.api.entity.GjsArticleIndexEntity
     * <p/>
     * id - 主键Id
     * category - 文章标签
     * title - 文章标题
     * content - 文章内容
     * status - 置顶1、2、3 ...
     * view - 浏览次数
     * user - 发布者：0财猫官方；1多李大师
     * created - 创建时间
     */
    @ResponseBody
    @RequestMapping(value = "gjs_article_list", method = RequestMethod.GET)
    public FQueryArticleIndexReq queryGjsArticleIndexList(@RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
                                                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception {
        try {
            FQueryArticleIndexReq req = new FQueryArticleIndexReq();
            req.setLimit(limit);
            req.setStart((page - 1) * limit);
            return articleService.queryGjsArticleIndexListForInterface(req, DOMAIN_URL);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>首页文章详情页</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/content/gjs_article</p>
     * <p>请求方式：POST</p>
     *
     * @param id 首页文章ID
     * @return 首页文章详情页
     * @throws Exception
     * @see com.caimao.gjs.api.entity.GjsArticleIndexEntity
     */
    @ResponseBody
    @RequestMapping(value = "gjs_article", method = { RequestMethod.GET, RequestMethod.POST} )
    public GjsArticleIndexEntity getGjsArticleIndex(@RequestParam(value = "id") Long id) throws Exception {
        try {
            return this.articleService.getArticleIndexById(id);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

}
