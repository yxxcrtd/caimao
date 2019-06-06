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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信调仓的处理逻辑
 * Created by Administrator on 2016/1/5.
 */
@Component
public class WXTiaoCang {
    private static final Logger logger = LoggerFactory.getLogger(WXTiaoCang.class);
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
        this.redisUtils.del(openId+"_stock_positions");
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
     * 点击调仓按钮处理逻辑
     * @param reqMap
     * @return
     */
    public String clickProcess(Map<String, String> reqMap) throws Exception {
        // 获取当前用户的openId
        String formOpenId = reqMap.get("FromUserName");
        // 设置当前用户处理过程的标记
        String key = WXBaseService.redisCurrentProcKey + formOpenId;
        String val = "tiaocang";

        // 根据用户的 OpenId 获取用户的持仓与现金
        GujiUserEntity gujiUserEntity = this.gujiService.getUserByOpenId(formOpenId);
        this.setRedis(formOpenId+"_wxid", gujiUserEntity.getWxId().toString());
        List<GujiUserStockEntity> userStockEntityList = this.gujiService.getMyStockList(gujiUserEntity.getWxId());
        Integer nowHoldStock = 0;
        String stockListStr = "";
        for (GujiUserStockEntity stockEntity : userStockEntityList) {
            if (stockEntity.getStockType().equals(EGujiStockType.XJ.getCode())) {
                // 记录用户的现金比例
                this.setRedis(formOpenId+"_xianjin", stockEntity.getPositions().toString());
            }
            if (stockEntity.getPositions() > 0 && stockEntity.getStockType().equals(EGujiStockType.GP.getCode())) {
                nowHoldStock++;
                stockListStr += nowHoldStock+" "+stockEntity.getStockName()+" "+stockEntity.getStockCode()+" "+stockEntity.getPositions()+"% \n";
            }
        }
        if (nowHoldStock == 0) {
            return "您当前没有持仓，先去建仓吧";
        }
        this.redisUtils.set(key, val, WXBaseService.redisCacheUserInfoExpires);
        return "当前持仓：\n" +
                stockListStr +
                "输入前面序号，即可调仓，取消调仓回复c";
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
            resMap.put("msg", "您取消了调仓");
            return resMap;
        }

        switch (currentProc) {
            case "tiaocang":
                /** 用户这一步输入的股票代码 或者是序号 （1） ，查询这个股票代码信息，并返回信息 */
                String code = reqMap.get("Content");
                String StockCode = null;
                String StockName = null;
                Integer StockPositions = null;
                String StockPrice = null;
                String TargetPrice = null;

                String wxId = this.getRedis(formOpenId+"_wxid");
                if (wxId == null) {
                    this.clearAllRedisKey(formOpenId);// 出现问题，重置
                    resMap.put("type", "text");
                    resMap.put("msg", "出问题了，请大侠重新来过！-_#");
                    return resMap;
                }
                List<GujiUserStockEntity> userStockEntityList = this.gujiService.getMyStockList(Long.valueOf(wxId));

                // 看是否是序号的
                Pattern NoPattern = Pattern.compile("^[1-9]{1}$");
                Matcher NoMatcher = NoPattern.matcher(code);
                if (NoMatcher.matches()) {
                    // 用户输入的是代码
                    Integer index = Integer.valueOf(code);
                    Integer tmpIndex = 0;
                    for (GujiUserStockEntity stockEntity: userStockEntityList) {
                        if (stockEntity.getStockType().equals(EGujiStockType.GP.getCode()) && stockEntity.getPositions() > 0) {
                            tmpIndex++;
                            if (Objects.equals(tmpIndex, index)) {
                                StockCode = stockEntity.getStockCode();
                                StockName = stockEntity.getStockName();
                                StockPositions = stockEntity.getPositions();
                                TargetPrice = stockEntity.getTargetPrice();
                            }
                        }
                    }
                }
                // 看是否是股票代码
                Pattern StockPattern = Pattern.compile("^[0-9]{6}$");
                Matcher StockMatcher = StockPattern.matcher(code);
                if (StockMatcher.matches()) {
                    for (GujiUserStockEntity stockEntity: userStockEntityList) {
                        if (stockEntity.getStockCode().equalsIgnoreCase(code) && stockEntity.getPositions() > 0) {
                            StockCode = stockEntity.getStockCode();
                            StockName = stockEntity.getStockName();
                            StockPositions = stockEntity.getPositions();
                            TargetPrice = stockEntity.getTargetPrice();
                        }
                    }
                }
                if (StockCode == null) {
                    resMap.put("type", "text");
                    resMap.put("msg", "我装作看不懂的样子，请回复正确的序号或股票代码");
                    return resMap;
                }
                TickerEntity tickerEntity = this.stockHQService.getTicket(StockCode);
                if (tickerEntity == null) {
                    this.clearAllRedisKey(formOpenId);
                    resMap.put("type", "text");
                    resMap.put("msg", "获取股票代码行情错误，请稍后再试");
                    return resMap;
                }
                StockPrice = String.valueOf(tickerEntity.getCurPrice());

                // 现金比例
                String xjPosition = this.getRedis(formOpenId+"_xianjin");
                if (xjPosition == null) {
                    this.clearAllRedisKey(formOpenId);// 出现问题，重置
                    resMap.put("type", "text");
                    resMap.put("msg", "出问题了，请大侠重新来过！-_#");
                    return resMap;
                }
                Integer maxPositions = StockPositions + Integer.valueOf(xjPosition);
                maxPositions = maxPositions > 100 ? 100 : maxPositions;
                String resMsg = "您当前持有"+StockPositions+"%的"+StockName+"，最多可持有"+maxPositions+"%，请输入目标仓位“x%”，取消调仓回复c";
                resMap.put("type", "text");
                resMap.put("msg", resMsg);

                // 记录用户的股票代码、名称、当前价格
                this.setRedis(formOpenId+"_stock_name", StockName);
                this.setRedis(formOpenId+"_stock_code", StockCode);
                this.setRedis(formOpenId+"_stock_price", StockPrice);
                this.setRedis(formOpenId+"_target_price", TargetPrice);
                this.setRedis(formOpenId+"_stock_positions", StockPositions.toString());

                // 成功的话，记录下一步
                this.redisUtils.set(key, "tiaocang-1", WXBaseService.redisCacheUserInfoExpires);
                break;
            case "tiaocang-1":
                /** 用户这一步输入的股票比例， 需要验证并记录这个比例 */
                String positionStr = reqMap.get("Content");
                Pattern positionsPattern = Pattern.compile("^[0-9]{1,9}%?$");
                Matcher positionsMatcher = positionsPattern.matcher(positionStr);
                if ( ! positionsMatcher.matches()) {
                    resMap.put("type", "text");
                    resMap.put("msg", "啊……看不懂你在说什么，请输入目标仓位，格式“x%”，取消调仓回复c");
                    return resMap;
                }
                Integer positionInt = Integer.valueOf(positionStr.replace("%", ""));
                if (positionInt > 100) {
                    resMap.put("type", "text");
                    resMap.put("msg", "仓位怎么可以超过100%呢！请输入仓位，格式“x%”，取消调仓回复c");
                    return resMap;
                }
                String xjPositions = this.getRedis(formOpenId+"_xianjin");
                String stockPosition = this.getRedis(formOpenId+"_stock_positions");
                if (xjPositions == null || stockPosition == null) {
                    this.clearAllRedisKey(formOpenId);// 出现问题，重置
                    resMap.put("type", "text");
                    resMap.put("msg", "出问题了，请大侠重新来过！-_#");
                    return resMap;
                }
                // 仓位不能大于最大仓位（当前仓位加上现金仓位）
                Integer maxPositions1 = Integer.valueOf(stockPosition) + Integer.valueOf(xjPositions);
                maxPositions1 = maxPositions1 > 100 ? 100 : maxPositions1;
                if (positionInt > maxPositions1) {
                    resMap.put("type", "text");
                    resMap.put("msg", "子弹不够哦，目标仓位不能超过"+maxPositions1+"%，请输入目标仓位“x%”，取消调仓回复c");
                    return resMap;
                }
                this.setRedis(formOpenId+"_positions", positionInt.toString());
                if (positionInt == 0) {
                    resMap.put("type", "text");
                    resMap.put("msg", "请输入清仓理由，无脑清仓回复n，取消回复c");
                    // 成功的话，记录下下一步
                    this.redisUtils.set(key, "tiaocang-3", WXBaseService.redisCacheUserInfoExpires);
                } else {
                    if (positionInt > Integer.valueOf(stockPosition)) {
                        resMap.put("type", "text");
                        resMap.put("msg", "仓位"+positionInt+"%，请输入目标价，维持原目标价回复n，取消回复c");
                    } else if (positionInt < Integer.valueOf(stockPosition)) {
                        resMap.put("type", "text");
                        resMap.put("msg", "仓位"+positionInt+"%，请输入目标价，维持原目标价回复n，取消回复c");
                    } else {
                        resMap.put("type", "text");
                        resMap.put("msg", "仓位"+positionInt+"%，请输入目标价，维持原目标价回复n，取消回复c");
                    }
                    // 成功的话，记录下一步
                    this.redisUtils.set(key, "tiaocang-2", WXBaseService.redisCacheUserInfoExpires);
                }
                break;
            case "tiaocang-2":
                /** 用户这一步输入的目标价格 */
                String targetPrice = reqMap.get("Content");
                if (targetPrice.equalsIgnoreCase("n")) {
                    targetPrice = this.getRedis(formOpenId+"_target_price");
                } else {
                    Pattern pricePattern = Pattern.compile("^[0-9]+[.]?[0-9]{0,2}$");
                    Matcher priceMatcher = pricePattern.matcher(targetPrice);
                    if (! priceMatcher.matches()) {
                        resMap.put("type", "text");
                        resMap.put("msg", "请输入正确的目标价，取消调仓回复c");
                        return resMap;
                    }
                }

                resMap.put("type", "text");
                resMap.put("msg", "目标价"+targetPrice+"，请输入调仓理由，无脑调仓回复n，取消调仓回复c");

                this.setRedis(formOpenId+"_target_price", targetPrice);

                this.redisUtils.set(key, "jiancang-3", WXBaseService.redisCacheUserInfoExpires);
                break;
            case "tiaocang-3":
                /** 用户这一步输入的是调仓理由，记录并返回内容 */
                String content = reqMap.get("Content");
                String reason = null;
                String targetPositions = this.getRedis(formOpenId+"_positions");
                String nowStockPositions = this.getRedis(formOpenId+"_stock_positions");
                if (targetPositions == null || nowStockPositions == null) {
                    this.clearAllRedisKey(formOpenId);// 出现问题，重置
                    resMap.put("type", "text");
                    resMap.put("msg", "出问题了，请大侠重新来过！-_#");
                    return resMap;
                }
                if (content.equalsIgnoreCase("n")) {
                    if (Integer.valueOf(targetPositions) > Integer.valueOf(nowStockPositions)) {
                        reason = "无脑加仓";
                    } else if (Integer.valueOf(targetPositions) < Integer.valueOf(nowStockPositions)) {
                        reason = "无脑减仓";
                    } else if (Integer.valueOf(targetPositions) == 0) {
                        reason = "无脑清仓";
                    } else {
                        reason = "无脑调仓";
                    }
                } else {
                    reason = WXBaseService.nl2br(content);
                }
                this.setRedis(formOpenId+"_reason", reason);

                // 记录到数据表中了
                return this.writeDB(formOpenId);

//                resMap.put("type", "text");
//                resMap.put("msg", "发布后就不能修改啦，确认一下您的调仓理由哦。发布调仓回复p，取消调仓回复c");
//                // 成功的话，记录下一步
//                this.redisUtils.set(key, "tiaocang-4", WXBaseService.redisCacheUserInfoExpires);
//                break;
            case "tiaocang-4":
                String content2 = reqMap.get("Content");
                if (content2.equalsIgnoreCase("p")) {
                    return this.writeDB(formOpenId);
                }
                resMap.put("type", "text");
                resMap.put("msg", "发布调仓回复p，取消调仓回复c");
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
        shareRecordEntity.setOperType(EGujiShareType.TC.getCode());

        this.gujiService.addShareStockInfo(shareRecordEntity);

        // 清除所有的redis中记录数据
        this.clearAllRedisKey(openId);

        resMap.put("type", "text");
        resMap.put("msg",
                "调仓页面创建完成，打开页面转发给好友吧\n" +
                        "* <a href='"+this.wxBaseService.getDomain()+"/weixin/guji/recommendStock.html?wxId="+wxId+"&stockCode="+stockCode+"'>打开调仓页面</a>\n" +
                        "\n" +
                        "将主页分享给好友，好友可关注您的实时更新\n" +
                        "* <a href='"+this.wxBaseService.getDomain()+"/weixin/guji/analystIndex.html?wxId="+wxId+"'>我的主页</a>"
        );
        return resMap;
    }

}
