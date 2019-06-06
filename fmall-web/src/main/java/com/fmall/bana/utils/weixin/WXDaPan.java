package com.fmall.bana.utils.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.caimao.bana.api.entity.guji.GujiShareRecordEntity;
import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.enums.guji.EGujiShareType;
import com.caimao.bana.api.enums.guji.EGujiStockType;
import com.caimao.bana.api.service.guji.IGujiService;
import com.fmall.bana.utils.RedisUtils;
import com.fmall.bana.utils.weixin.entity.TickerEntity;
import com.fmall.bana.utils.weixin.hq.StockHQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信大盘的处理逻辑
 * Created by Administrator on 2016/1/5.
 */
@Component
public class WXDaPan {
    private static final Logger logger = LoggerFactory.getLogger(WXDaPan.class);

    @Resource
    private WXBaseService wxBaseService;
    @Resource
    private IGujiService gujiService;
    @Resource
    private StockHQService stockHQService;
    @Resource
    private RedisUtils redisUtils;

    private String DPCode = "000001";

    private void clearAllRedisKey(String openId) {
        this.redisUtils.del(WXBaseService.redisCurrentProcKey+openId);
        this.redisUtils.del(openId + "_viewpoint");
        this.redisUtils.del(openId + "_target_price");
        this.redisUtils.del(openId + "_reason");
    }

    /**
     * 为了解决Redis中文的问题，单独写的方法
     * @param key
     * @param val
     */
    private void setRedis(String key, String val) {
        this.redisUtils.set(key, JSON.toJSONString(val, SerializerFeature.BrowserCompatible), WXBaseService.redisCacheUserInfoExpires);
    }

    private String getRedis(String key) {
        Object val = this.redisUtils.get(key);
        if (val == null) return null;
        return JSON.parseObject(val.toString(), String.class);
    }

    /**
     * 点击大盘按钮处理逻辑
     * @param reqMap
     * @return
     */
    public String clickProcess(Map<String, String> reqMap) {
        // 获取当前用户的openId
        String formOpenId = reqMap.get("FromUserName");
        // 设置当前用户处理过程的标记
        String key = WXBaseService.redisCurrentProcKey + formOpenId;
        String val = "dapan";
        this.redisUtils.set(key, val, WXBaseService.redisCurrentExpires);
        // TODO 根据用户的 OpenId 获取用户的持仓与现金
        return "输入前面序号，选择对大盘的看法\n" +
                "1 看多\n" +
                "2 中性\n" +
                "3 看空";
    }

    /**
     * 用户输入的文本的处理流程
     * @param reqMap
     * @param currentProc
     * @return
     */
    public Map<String, String> textProcess(Map<String, String> reqMap, String currentProc) throws Exception {
        Map<String, String> resMap = new HashMap<>();
        String formOpenId = reqMap.get("FromUserName");
        String key = WXBaseService.redisCurrentProcKey + formOpenId;
        Long expires = WXBaseService.redisCurrentExpires;

        logger.info("大盘接收到的内容：{}", reqMap);
        logger.info("大盘当前所在的过程：{}", currentProc);

        if (reqMap.get("Content").equalsIgnoreCase("c")) {
            this.clearAllRedisKey(formOpenId);
            resMap.put("type", "text");
            resMap.put("msg", "您取消了点评");
            return resMap;
        }

        switch (currentProc) {
            case "dapan":
                // 用户这一步输入的是对大盘的看法
                String code = reqMap.get("Content");
                Pattern NoPattern = Pattern.compile("^[1-3]{1}$");
                Matcher NoMatcher = NoPattern.matcher(code);
                if ( ! NoMatcher.matches()) {
                    resMap.put("type", "text");
                    resMap.put("msg", "啊……看不懂你在说什么，输入前面序号，选择对当前大盘的看法\n" +
                            "1 看多\n" +
                            "2 中性\n" +
                            "3 看空");
                    return resMap;
                }
                Integer viewPoint = Integer.valueOf(code);
                if (viewPoint <= 0 || viewPoint > 3) {
                    resMap.put("type", "text");
                    resMap.put("msg", "啊……看不懂你在说什么，输入前面序号，选择对当前大盘的看法\n" +
                            "1 看多\n" +
                            "2 中性\n" +
                            "3 看空");
                    return resMap;
                }
                Map<Integer, String> viewPointsMap = new HashMap<>();
                viewPointsMap.put(1, "看多");
                viewPointsMap.put(2, "中性");
                viewPointsMap.put(3, "看空");

                resMap.put("type", "text");
                resMap.put("msg", "你现在“"+viewPointsMap.get(viewPoint)+"”，请回复目标点位，不设置目标点位回复n，取消点评回复c");

                this.setRedis(formOpenId+"_viewpoint", viewPoint.toString());

                // 成功的话，记录下一步
                this.redisUtils.set(key, "dapan-1", expires);
                break;
            case "dapan-1":
                /** 用户这一步输入的目标点数 */
                String targetPrice = reqMap.get("Content");
                if (targetPrice.equalsIgnoreCase("n")) {
                    targetPrice = "0";
                } else {
                    Pattern pricePattern = Pattern.compile("^[0-9]+[.]?[0-9]{0,2}$");
                    Matcher priceMatcher = pricePattern.matcher(targetPrice);
                    if (! priceMatcher.matches()) {
                        resMap.put("type", "text");
                        resMap.put("msg", "请回复目标点位，不设置目标点位回复n，取消点评回复c");
                        return resMap;
                    }
                }

                resMap.put("type", "text");
                resMap.put("msg", "点位："+targetPrice+"，请输入理由，建议20-500字，没有理由回复n，取消点评回复c");

                this.setRedis(formOpenId+"_target_price", targetPrice);

                // 成功的话，记录下一步
                this.redisUtils.set(key, "dapan-2", expires);
                break;
            case "dapan-2":
                // 用户这一步输入的是理由
                String content = reqMap.get("Content");
                String reason = null;
                if (content.equalsIgnoreCase("n")) {
                    reason = "直觉";
                } else {
                    reason = WXBaseService.nl2br(content);
                }
                this.setRedis(formOpenId+"_reason", reason);

                // 记录到数据表中了
                return this.writeDB(formOpenId);

//                if (content.equalsIgnoreCase("n")) {
//                    // 记录到数据表中了
//                    return this.writeDB(formOpenId);
//                }
//                resMap.put("type", "text");
//                resMap.put("msg", "发布后就不能修改啦，确认一下您的点评哦。发布点评回复p，取消点评回复c");
//
//                // 成功的话，记录下一步
//                this.redisUtils.set(key, "dapan-3", expires);
//                break;
            case "dapan-3":
                String content2 = reqMap.get("Content");
                if (content2.equalsIgnoreCase("p")) {
                    return this.writeDB(formOpenId);
                }
                resMap.put("type", "text");
                resMap.put("msg", "发布推荐回复p，取消推荐回复c");
                break;
            default:
                // 成功的话，删除key
                this.redisUtils.del(key);
                return null;
        }

        return resMap;
    }

    /**
     * 记录用户添加的到数据库中
     * @param openId
     * @return
     */
    private Map<String, String> writeDB(String openId) throws Exception {
        Map<String, String> resMap = new HashMap<>();

        GujiUserEntity userEntity = this.gujiService.getUserByOpenId(openId);

        String viewpoint = this.getRedis(openId + "_viewpoint");
        String reason = this.getRedis(openId + "_reason");
        String targetPrice = this.getRedis(openId+"_target_price");
        if (viewpoint == null || reason == null || targetPrice == null) {
            this.clearAllRedisKey(openId);
            resMap.put("type", "text");
            resMap.put("msg", "出问题了，请大侠重新来过！-_#");
            return resMap;
        }
        TickerEntity tickerEntity = this.stockHQService.getTicket(this.DPCode);
        if (tickerEntity == null) {
            this.clearAllRedisKey(openId);
            resMap.put("type", "text");
            resMap.put("msg", "出问题了，请大侠重新来过！-_#");
            return resMap;
        }
        GujiShareRecordEntity shareRecordEntity = new GujiShareRecordEntity();
        shareRecordEntity.setWxId(userEntity.getWxId());
        shareRecordEntity.setOpenId(openId);
        shareRecordEntity.setStockType(EGujiStockType.DP.getCode());
        shareRecordEntity.setStockCode(this.DPCode);
        shareRecordEntity.setStockName("大盘点评");
        shareRecordEntity.setStockPrice(String.valueOf(tickerEntity.getCurPrice()));
        shareRecordEntity.setTargetPrice(targetPrice);
        shareRecordEntity.setPositions(Integer.valueOf(viewpoint));
        shareRecordEntity.setReason(reason);
        shareRecordEntity.setOperType(EGujiShareType.DP.getCode());

        this.gujiService.addShareStockInfo(shareRecordEntity);

        // 清除所有的redis中记录数据
        this.clearAllRedisKey(openId);

        resMap.put("type", "text");
        resMap.put("msg",
                "点评页面创建完成，打开页面转发给好友吧\n" +
                        "* <a href='"+this.wxBaseService.getDomain()+"/weixin/guji/recommendStock.html?wxId="+userEntity.getWxId()+"&stockCode="+this.DPCode+"'>打开点评页面</a>\n" +
                        "\n" +
                        "将主页分享给好友，好友可关注您的实时更新\n" +
                        "* <a href='"+this.wxBaseService.getDomain()+"/weixin/guji/analystIndex.html?wxId="+userEntity.getWxId()+"'>我的主页</a>"
        );
        return resMap;
    }

}
