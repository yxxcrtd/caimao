package com.fmall.bana.utils.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.caimao.bana.api.entity.guji.GujiShareRecordEntity;
import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.entity.guji.GujiUserStockEntity;
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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信建仓的处理逻辑
 * Created by Administrator on 2016/1/5.
 */
@Component
public class WXJianCang {
    private static final Logger logger = LoggerFactory.getLogger(WXJianCang.class);

    @Resource
    private IGujiService gujiService;
    @Resource
    private StockHQService stockHQService;
    @Resource
    private WXBaseService wxBaseService;
    @Resource
    private RedisUtils redisUtils;

    private void clearAllRedisKey(String openId) {
        this.redisUtils.del(WXBaseService.redisCurrentProcKey+openId);
        this.redisUtils.del(openId+"_xianjin");
        this.redisUtils.del(openId + "_wxid");
        this.redisUtils.del(openId + "_stock_code");
        this.redisUtils.del(openId + "_stock_name");
        this.redisUtils.del(openId + "_stock_price");
        this.redisUtils.del(openId + "_target_price");
        this.redisUtils.del(openId + "_positions");
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
     * 点击建仓按钮处理逻辑
     * @param reqMap
     * @return
     */
    public String clickProcess(Map<String, String> reqMap) throws Exception {
        // 获取当前用户的openId
        String formOpenId = reqMap.get("FromUserName");
        // 设置当前用户处理过程的标记
        String key = WXBaseService.redisCurrentProcKey + formOpenId;
        String val = "jiancang";

        // 根据用户的 OpenId 获取用户的持仓与现金
        GujiUserEntity gujiUserEntity = this.gujiService.getUserByOpenId(formOpenId);
        this.setRedis(formOpenId+"_wxid", gujiUserEntity.getWxId().toString());
        List<GujiUserStockEntity> userStockEntityList = this.gujiService.getMyStockList(gujiUserEntity.getWxId());
        Integer nowHoldStock = 0;
        for (GujiUserStockEntity stockEntity : userStockEntityList) {
            if (stockEntity.getStockType().equals(EGujiStockType.XJ.getCode())) {
                // 记录用户的现金比例
                this.setRedis(formOpenId+"_xianjin", stockEntity.getPositions().toString());
//                if (stockEntity.getPositions() == 0) {
//                    return "子弹已用光，需要卖出股票重新填弹才能推荐新票";
//                }
            }
//            if (stockEntity.getPositions() > 0 && stockEntity.getStockType().equals(EGujiStockType.GP.getCode())) {
//                nowHoldStock++;
//            }
        }
        if (nowHoldStock >= 5) {
            return "只能同时推荐5只股票，需要卖出股票才能继续推荐";
        }
        this.redisUtils.set(key, val, WXBaseService.redisCacheUserInfoExpires);
        if (userStockEntityList.size() == 1) {
            // 只有一个现金
            return "推荐股票请回复【股票代码】，如:600001";
        } else {
            return "推荐股票请回复【股票代码】，如:600001";
        }
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

        logger.info("调仓接收到的内容：{}", reqMap);
        logger.info("调仓当前所在的过程：{}", currentProc);

        if (reqMap.get("Content").equalsIgnoreCase("c")) {
            this.clearAllRedisKey(formOpenId);
            resMap.put("type", "text");
            resMap.put("msg", "您取消了推荐");
            return resMap;
        }

        switch (currentProc) {
            case "jiancang":
                /** 用户这一步输入的股票代码 ，查询这个股票代码信息，并返回信息 */
                String stockCode = reqMap.get("Content");
                Pattern pattern = Pattern.compile("^[0-9]{6}$");
                Matcher matcher = pattern.matcher(stockCode);
                if ( ! matcher.matches()) {
                    resMap.put("type", "text");
                    resMap.put("msg", "我装作看不懂的样子，请回复正确的股票代码");
                    return resMap;
                }
                String wxId = this.getRedis(formOpenId+"_wxid");
                if (wxId == null) {
                    this.clearAllRedisKey(formOpenId);// 出现问题，重置
                    resMap.put("type", "text");
                    resMap.put("msg", "出问题了，请大侠重新来过！-_#");
                    return resMap;
                }
                // 如果已经在推荐的里面，去让他调仓
//                List<GujiUserStockEntity> userStockEntityList = this.gujiService.getMyStockList(Long.valueOf(wxId));
//                for (GujiUserStockEntity stockEntity : userStockEntityList) {
//                    if (stockEntity.getStockCode().equals(stockCode) && stockEntity.getPositions() > 0) {
//                        this.clearAllRedisKey(formOpenId);
//                        resMap.put("type", "text");
//                        resMap.put("msg", "您当前已持有“"+stockEntity.getStockName()+" "+stockEntity.getStockCode()+"”，需要调整请选择调仓");
//                        return resMap;
//                    }
//                }
                TickerEntity tickerEntity = this.stockHQService.getTicket(reqMap.get("Content"));
                if (tickerEntity == null) {
                    resMap.put("type", "text");
                    resMap.put("msg", "我装作看不懂的样子，请回复正确的股票代码");
                    return resMap;
                }
                // 现金比例
                String xjPosition = this.getRedis(formOpenId+"_xianjin");
                if (xjPosition == null) {
                    this.clearAllRedisKey(formOpenId);// 出现问题，重置
                    resMap.put("type", "text");
                    resMap.put("msg", "出问题了，请大侠重新来过！-_#");
                    return resMap;
                }
                String resMsg = "您要推荐的是“"+tickerEntity.getName()+" "+tickerEntity.getCode()+"”，请输入仓位，格式“x%”，您还有"+xjPosition+"%的子弹，取消推荐回复c";
                resMap.put("type", "text");
                resMap.put("msg", resMsg);

                // 记录用户的股票代码、名称、当前价格
                this.setRedis(formOpenId+"_stock_name", tickerEntity.getName());
                this.setRedis(formOpenId+"_stock_code", tickerEntity.getCode());
                this.setRedis(formOpenId+"_stock_price", String.valueOf(tickerEntity.getCurPrice()));

                // 成功的话，记录下一步
                this.redisUtils.set(key, "jiancang-1", WXBaseService.redisCacheUserInfoExpires);
                break;
            case "jiancang-1":
                /** 用户这一步输入的股票比例， 需要验证并记录这个比例 */
                String positionStr = reqMap.get("Content");
                Pattern pattern1 = Pattern.compile("^[0-9]{1,9}%?$");
                Matcher matcher1 = pattern1.matcher(positionStr);
                if ( ! matcher1.matches()) {
                    resMap.put("type", "text");
                    resMap.put("msg", "啊……看不懂你在说什么，请输入仓位，格式“x%”，取消推荐回复c");
                    return resMap;
                }
                Integer positionInt = Integer.valueOf(positionStr.replace("%", ""));
                if (positionInt > 100) {
                    resMap.put("type", "text");
                    resMap.put("msg", "仓位怎么可以超过100%呢！请输入仓位，格式“x%”，取消推荐回复c");
                    return resMap;
                }
                String xjPositions = this.getRedis(formOpenId+"_xianjin");
                if (xjPositions == null) {
                    this.clearAllRedisKey(formOpenId);// 出现问题，重置
                    resMap.put("type", "text");
                    resMap.put("msg", "出问题了，请大侠重新来过！-_#");
                    return resMap;
                }
                if (positionInt > Integer.valueOf(xjPositions)) {
                    resMap.put("type", "text");
                    resMap.put("msg", "您还有"+xjPositions+"%的子弹，仓位不能超过哦！请输入仓位，格式“x%”，取消推荐回复c");
                    return resMap;
                }
                resMap.put("type", "text");
                resMap.put("msg", "仓位"+positionInt+"%，请输入目标价，如：100.00，取消推荐回复c");

                this.setRedis(formOpenId+"_positions", positionInt.toString());

                // 成功的话，记录下一步
                this.redisUtils.set(key, "jiancang-2", WXBaseService.redisCacheUserInfoExpires);
                break;
            case "jiancang-2":
                /** 用户这一步输入的目标价格 */
                String targetPrice = reqMap.get("Content");
                Pattern pricePattern = Pattern.compile("^[0-9]+[.]?[0-9]{0,2}$");
                Matcher priceMatcher = pricePattern.matcher(targetPrice);
                if (! priceMatcher.matches()) {
                    resMap.put("type", "text");
                    resMap.put("msg", "请输入正确的目标价，取消推荐回复c");
                    return resMap;
                }

                resMap.put("type", "text");
                resMap.put("msg", "目标价"+targetPrice+"，请输入推荐理由，建议20-500字。无脑推荐回复n，取消推荐回复c");

                this.setRedis(formOpenId+"_target_price", targetPrice);

                this.redisUtils.set(key, "jiancang-3", WXBaseService.redisCacheUserInfoExpires);
                break;
            case "jiancang-3":
                /** 用户这一步输入的是推荐理由，记录并返回内容 */
                String content = reqMap.get("Content");
                String reason = null;
                if (content.equalsIgnoreCase("n")) {
                    reason = "无脑推荐";
                } else {
                    reason = WXBaseService.nl2br(content);
                }
                this.setRedis(formOpenId+"_reason", reason);
                // 记录到数据表中了
                return this.writeDB(formOpenId);

//                if (content.equalsIgnoreCase("n")) {
//                }
//                resMap.put("type", "text");
//                resMap.put("msg", "发布后就不能修改啦，确认一下您的推荐理由哦。发布推荐回复p，取消推荐回复c");
//                // 成功的话，记录下一步
//                this.redisUtils.set(key, "jiancang-4", WXBaseService.redisCacheUserInfoExpires);
//                break;
            case "jiancang-4":
                String content2 = reqMap.get("Content");
                if (content2.equalsIgnoreCase("p")) {
                    return this.writeDB(formOpenId);
                }
                resMap.put("type", "text");
                resMap.put("msg", "发布推荐回复p，取消推荐回复c");
                break;
            default:
                // 成功的话，删除key
                this.clearAllRedisKey(formOpenId);
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
        String wxId = this.getRedis(openId + "_wxid");
        String stockCode = this.getRedis(openId + "_stock_code");
        String stockName = this.getRedis(openId + "_stock_name");
        String stockPrice = this.getRedis(openId + "_stock_price");
        String targetPrice = this.getRedis(openId + "_target_price");
        String positions = this.getRedis(openId + "_positions");
        String reason = this.getRedis(openId + "_reason");
        if (wxId == null || stockCode == null || stockName == null || stockPrice == null || targetPrice == null || positions == null || reason == null) {
            this.clearAllRedisKey(openId);
            resMap.put("type", "text");
            resMap.put("msg", "出问题了，请大侠重新来过！-_#");
            return resMap;
        }
        GujiShareRecordEntity shareRecordEntity = new GujiShareRecordEntity();
        shareRecordEntity.setWxId(Long.valueOf(wxId));
        shareRecordEntity.setOpenId(openId);
        shareRecordEntity.setStockType(EGujiStockType.GP.getCode());
        shareRecordEntity.setStockCode(stockCode);
        shareRecordEntity.setStockName(stockName);
        shareRecordEntity.setStockPrice(stockPrice);
        shareRecordEntity.setTargetPrice(targetPrice);
        shareRecordEntity.setPositions(Integer.valueOf(positions));
        shareRecordEntity.setReason(reason);
        shareRecordEntity.setOperType(EGujiShareType.JC.getCode());

        this.gujiService.addShareStockInfo(shareRecordEntity);

        // 清除所有的redis中记录数据
        this.clearAllRedisKey(openId);

        resMap.put("type", "text");
        resMap.put("msg",
                "推荐页面创建完成，打开页面转发给好友吧\n" +
                        "* <a href='"+this.wxBaseService.getDomain()+"/weixin/guji/recommendStock.html?wxId="+wxId+"&stockCode="+stockCode+"'>打开推荐页面</a>\n" +
                        "\n" +
                        "将主页分享给好友，好友可关注您的实时更新\n" +
                        "* <a href='"+this.wxBaseService.getDomain()+"/weixin/guji/analystIndex.html?wxId="+wxId+"'>我的主页</a>"
        );
        return resMap;
    }


}
