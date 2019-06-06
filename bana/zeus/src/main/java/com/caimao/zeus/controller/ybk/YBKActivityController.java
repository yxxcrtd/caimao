package com.caimao.zeus.controller.ybk;

import com.caimao.bana.api.entity.req.ybk.FYBKQueryActivityListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkExchangeQueryListReq;
import com.caimao.bana.api.entity.ybk.YBKActivityEntity;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import com.caimao.bana.api.enums.ybk.EArticleCategory;
import com.caimao.bana.api.service.ybk.IYBKService;
import com.caimao.zeus.admin.controller.BaseController;
import com.caimao.zeus.util.ImageUtils;
import com.caimao.zeus.util.PageUtils;
import com.caimao.zeus.util.RedisUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 邮币卡活动控制器
 */
@Controller
@RequestMapping("/ybk/activity")
public class YBKActivityController extends BaseController {
    @Resource
    private IYBKService ybkService;
    @Resource
    private RedisUtils redisUtils;

    private static ResourceBundle appBundle = PropertyResourceBundle.getBundle("conf/application");

    private String ybkActivityTopicRedisKey = "_ybk_activity_topic_key";

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "exchangeId", required = false, defaultValue = "0") Integer exchangeId,
            @RequestParam(value = "dateStart", required = false) String dateStart,
            @RequestParam(value = "dateEnd", required = false) String dateEnd,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ModelAndView mav = new ModelAndView("ybk/activity/list");
        FYBKQueryActivityListReq req = new FYBKQueryActivityListReq();
        Integer limit = 10;
        req.setStart((page - 1) * limit);
        req.setLimit(limit);
        if (exchangeId == null) exchangeId = 0;
        if (exchangeId != 0) req.setExchangeId(exchangeId);
        if (dateStart == null || dateStart.equals("")) {
            dateStart = sdf.format(new Date());
        }
        req.setDateStart(dateStart);
        if (dateEnd == null || dateEnd.equals("")) {
            dateEnd = sdf.format(new Date());
        }
        req.setDateEnd(dateEnd);

        FYBKQueryActivityListReq list = ybkService.queryActivityWithPage(req);
        mav.addObject("list", list);
        if (list.getItems() != null) {
            PageUtils pageUtils = new PageUtils(page, limit, list.getTotalCount(), String.format("/ybk/activity/list?exchangeId=%s&dateStart=%s&dateEnd=%s&page=", exchangeId, dateStart, dateEnd));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }

        HashMap<String, String> categoryList = new HashMap<>();
        for (EArticleCategory articleCategory : EArticleCategory.values()) {
            categoryList.put(articleCategory.getCode(), articleCategory.getValue());
        }
        // 获取交易所列表
        FYbkExchangeQueryListReq exchangeReq = new FYbkExchangeQueryListReq();
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(exchangeReq);
        HashMap<Integer, YbkExchangeEntity> exchangeEntityHashMap = new HashMap<>();
        for (YbkExchangeEntity entity : exchangeEntityList) {
            exchangeEntityHashMap.put(entity.getId(), entity);
        }
        mav.addObject("exchangeList", exchangeEntityList);
        mav.addObject("exchangeMap", exchangeEntityHashMap);
        mav.addObject("categoryList", categoryList);
        mav.addObject("exchangeId", exchangeId);
        mav.addObject("dateStart", dateStart);
        mav.addObject("dateEnd", dateEnd);

        // 网站链接
        mav.addObject("ybkUrl", appBundle.getString("ybkUrl"));

        // 活动那个单个提示语句
        mav.addObject("topic", this.redisUtils.get(0, this.ybkActivityTopicRedisKey));

        return mav;
    }

    /**
     * 活动聚合页提示语更新
     * @param topic
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update_topic", method = RequestMethod.POST)
    public ModelAndView saveYbkTopic(
            @RequestParam(value = "topic") String topic,
            HttpServletRequest request
    ) throws Exception {
        this.redisUtils.set(0, this.ybkActivityTopicRedisKey, topic, 0L);
        return jumpForSuccess(request, "保存成功", "/ybk/activity/list");
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ModelAndView save(@RequestParam(value = "id", required = false) Long id) throws Exception {
        ModelAndView mav = new ModelAndView("ybk/activity/save");
        if (id != null && id != 0) {
            YBKActivityEntity ybkActivityEntity = ybkService.queryActivityById(id);
            mav.addObject("activityDetail", ybkActivityEntity);
        }
        // 获取交易所列表
        FYbkExchangeQueryListReq exchangeReq = new FYbkExchangeQueryListReq();
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(exchangeReq);
        mav.addObject("exchangeList", exchangeEntityList);
        return mav;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam(value = "id", required = false) Long id,
                             @RequestParam(value = "exchangeId") Integer exchangeId,
                             @RequestParam(value = "activityName") String activityName,
                             @RequestParam(value = "endDatetime", required = false) String endDatetime,
                             @RequestParam(value = "ask", required = false) String ask,
                             @RequestParam(value = "sort", required = false) Integer sort,
                             @RequestParam(value = "reward", required = false) String reward,
                             @RequestParam(value = "rewardValue", required = false) String rewardValue,
                             @RequestParam(value = "activityUrl", required = false) String activityUrl,
                             @RequestParam(value = "isShow", required = false) Integer isShow,
                             HttpServletRequest request,
                             MultipartHttpServletRequest Mrequest) throws Exception {

        YbkExchangeEntity ybkExchangeEntity = ybkService.getExchangeById(exchangeId);

        String activityBanner = null;
        String savePath = appBundle.getString("picturePath") + "activity";
        MultipartFile activityBannerFile = Mrequest.getFile("activityBanner");
        if (!activityBannerFile.isEmpty()) {
            activityBanner = ImageUtils.uploadImag(activityBannerFile, savePath, 5120L, false);
            activityBanner = "/" + activityBanner.substring(activityBanner.indexOf("upload"));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        YBKActivityEntity ybkActivityEntity = new YBKActivityEntity();
        ybkActivityEntity.setExchangeId(exchangeId);
        ybkActivityEntity.setExchangeName(ybkExchangeEntity.getName());
        ybkActivityEntity.setActivityName(activityName);
        ybkActivityEntity.setEndDatetime(sdf.parse(endDatetime));
        ybkActivityEntity.setAsk(ask);
        ybkActivityEntity.setReward(reward);
        ybkActivityEntity.setRewardValue(rewardValue);
        ybkActivityEntity.setIsShow(isShow);
        ybkActivityEntity.setActivityUrl(activityUrl);
        ybkActivityEntity.setActivityBanner(activityBanner);
        ybkActivityEntity.setSort(sort);
        ybkActivityEntity.setCreated(new Date());

        if (id != null && id != 0) {
            ybkActivityEntity.setId(id);
            ybkService.activityUpdate(ybkActivityEntity);
        } else {
            ybkActivityEntity.setCreated(new Date());
            ybkService.activityInsert(ybkActivityEntity);
        }

        return jumpForSuccess(request, "保存成功", "/ybk/activity/list");
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public ModelAndView del(@RequestParam(value = "id") Long id, HttpServletRequest request) throws Exception {
        ybkService.activityDel(id);
        return jumpForSuccess(request, "删除成功", "/ybk/activity/list");
    }

    @RequestMapping(value = "/sort", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView sort(@RequestParam(value = "id") Long id,
                             @RequestParam(value = "sort") Integer sort,
                             HttpServletRequest request) throws Exception {
        YBKActivityEntity ybkActivityEntity = new YBKActivityEntity();
        ybkActivityEntity.setSort(sort);
        ybkActivityEntity.setId(id);
        ybkService.activityUpdate(ybkActivityEntity);
        return jumpForSuccess(request, "排序成功", "/ybk/activity/list");
    }
}
