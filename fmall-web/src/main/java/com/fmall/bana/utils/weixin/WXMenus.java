package com.fmall.bana.utils.weixin;

import com.fmall.bana.utils.weixin.utils.HttpHelper;
import com.fmall.bana.utils.weixin.utils.WXMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 微信自定义菜单功能
 * Created by Administrator on 2016/1/5.
 */
@Component
public class WXMenus {

    private static final Logger logger = LoggerFactory.getLogger(WXMenus.class);

    @Resource
    private WXBaseService wxBaseService;

    private Map<String, Object> menus;


    /**
     * 删除所有自定义菜单
     */
    public void delAll() throws Exception {
        String accessToken = this.wxBaseService.getAccessToken();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=%s", accessToken);
        String httpRes = HttpHelper.doGet(url);
        logger.info("删除自定义菜单http请求返回：{}", httpRes);
        WXMessageUtil.verifiedResults(httpRes);
    }

    /**
     * 创建微信自定义菜单
     * @throws Exception
     */
    public void createMeuns() throws Exception {
        menus = new TreeMap<>();
        List<Map<String, Object>> menusList = new ArrayList<>();

        // 查看荐股
        Map<String, Object> ckMenus = new TreeMap<>();
        ckMenus.put("type", "view");
        ckMenus.put("name", "看股"); // 首页 -> 看股
        ckMenus.put("url", this.wxBaseService.getDomain()+"/weixin/guji/subscribeHallIndex.html");
        menusList.add(ckMenus);

        // 荐股的子菜单
        List<Map<String, String>> jgSubMenus = new ArrayList<>();
        Map<String, String> jgM1 = new TreeMap<>();
        jgM1.put("type", "click");
        jgM1.put("name", "首次荐股");
        jgM1.put("key", "C_JIANCANG");
        jgSubMenus.add(jgM1);
        Map<String, String> jgM2 = new TreeMap<>();
        jgM2.put("type", "click");
        jgM2.put("name", "持续跟踪");
        jgM2.put("key", "C_TIAOCANG");
        jgSubMenus.add(jgM2);
        Map<String, String> jgM3 = new TreeMap<>();
        jgM3.put("type", "click");
        jgM3.put("name", "大盘");
        jgM3.put("key", "C_DAPAN");
        jgSubMenus.add(jgM3);

        Map<String, Object> jgMenus = new TreeMap<>();
        jgMenus.put("name", "荐股");
        jgMenus.put("sub_button", jgSubMenus);
        menusList.add(jgMenus);

        // 我的
        Map<String, Object> wdMenus = new TreeMap<>();
        List<Map<String, String>> wdSubMenus = new ArrayList<>();
        Map<String, String> wdM1 = new TreeMap<>();
        wdM1.put("type", "view");
        wdM1.put("name", "我的主页");
        wdM1.put("url", this.wxBaseService.getDomain()+"/weixin/guji/analystIndex.html");
        wdSubMenus.add(wdM1);
        Map<String, String> wdM2 = new TreeMap<>();
        wdM2.put("type", "click");
        wdM2.put("name", "认证");
        wdM2.put("key", "C_AUTH");
        wdSubMenus.add(wdM2);
        Map<String, String> wdM3 = new TreeMap<>();
        wdM3.put("type", "click");
        wdM3.put("name", "隐私设置");
        wdM3.put("key", "C_SETTING");
        wdSubMenus.add(wdM3);
        Map<String, String> wdM4 = new TreeMap<>();
        wdM4.put("type", "click");
        wdM4.put("name", "意见反馈");
        wdM4.put("key", "C_FEEDBACK");
        wdSubMenus.add(wdM4);
        wdMenus.put("name", "我的");
        wdMenus.put("sub_button", wdSubMenus);
        menusList.add(wdMenus);

        menus.put("button", menusList);

        String menusJSON = WXMessageUtil.mapToJson(menus);
        logger.info("创建微信菜单请求的JSON字符串：{}", menusJSON);
        String accessToken = this.wxBaseService.getAccessToken();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s", accessToken);

        String httpRes = HttpHelper.doPost(url, menusJSON);
        logger.info("创建微信菜单服务器返回数据：{}", httpRes);

        WXMessageUtil.verifiedResults(httpRes);

    }

}
