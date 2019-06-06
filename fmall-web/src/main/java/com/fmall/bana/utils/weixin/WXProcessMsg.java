package com.fmall.bana.utils.weixin;

import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.enums.guji.EGujiAuthStatus;
import com.caimao.bana.api.exception.CustomerException;
import com.caimao.bana.api.service.guji.IGujiService;
import com.fmall.bana.utils.RedisUtils;
import com.fmall.bana.utils.weixin.entity.TextMessageEntity;
import com.fmall.bana.utils.weixin.utils.WXMessageUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 微信荐股的业务逻辑处理层
 * Created by Administrator on 2016/1/4.
 */
@Component
public class WXProcessMsg {

    @Resource
    private WXBaseService wxBaseService;
    @Resource
    private WXJianCang wxJianCang;
    @Resource
    private WXTiaoCang wxTiaoCang;
    @Resource
    private WXDaPan wxDaPan;
    @Resource
    private WXAuth wxAuth;
    @Resource
    private WXSetting wxSetting;
    @Resource
    private IGujiService gujiService;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 处理用户发送的文本消息
     * @param reqMap
     * @return
     */
    public String processTextMessage(Map<String, String> reqMap) throws Exception {
        // 获取用户当前的处理过程
        String formOpenId = reqMap.get("FromUserName");
        String key = WXBaseService.redisCurrentProcKey + formOpenId;
        Object redisProc = this.redisUtils.get(key);
        if (redisProc == null) {
            // 没有过程，默认返回
            String msg = "欢迎您来到股计，股计是于千股之中取上涨个股的好帮手。\n" +
                    "[看股] 查看分析师荐股。\n" +
                    "[荐股] 推荐股票、点评大盘。";
            return WXProcessMsg.createResTextMessage(reqMap, msg);
        }

        /** 认证的处理逻辑 */
        if (redisProc.toString().contains("auth")) {
            String res = this.wxAuth.process(reqMap, redisProc.toString());
            if (res.equals("success")) return "success";
            return WXProcessMsg.createResTextMessage(reqMap, res);
        }
        /** 设置的处理逻辑 */
        if (redisProc.toString().contains("setting")) {
            String res = this.wxSetting.process(reqMap, redisProc.toString());
            if (res.equals("success")) return "success";
            return WXProcessMsg.createResTextMessage(reqMap, res);
        }
        /** 建仓、调仓、大盘的处理逻辑 */
        Map<String, String> resMap = null;
        if (redisProc.toString().contains("jiancang")) {
            // 建仓的处理逻辑
            resMap = this.wxJianCang.textProcess(reqMap, redisProc.toString());
        } else if (redisProc.toString().contains("tiaocang")) {
            // 调仓的处理逻辑
            resMap = this.wxTiaoCang.textProcess(reqMap, redisProc.toString());
        } else if (redisProc.toString().contains("dapan")) {
            resMap = this.wxDaPan.textProcess(reqMap, redisProc.toString());
        }
        if (resMap == null) {
            return "success";
        }

        if (resMap.get("type").equals("text")) {
            // 返回文本信息
            return WXProcessMsg.createResTextMessage(reqMap, resMap.get("msg"));
        } else if (resMap.get("type").equals("template")) {
            // TODO 发送模板信息
            return WXProcessMsg.createResTextMessage(reqMap, "这里发送模板消息，需要认证完成才可");
        } else {
            return "success";
        }
    }

    /**
     * 处理用户发送的图片消息
     * @param reqMap
     * @return
     */
    public String processImageMessage(Map<String, String> reqMap) throws Exception {
        // 获取用户当前的处理过程
        String formOpenId = reqMap.get("FromUserName");
        String key = WXBaseService.redisCurrentProcKey + formOpenId;
        Object redisProc = this.redisUtils.get(key);
        if (redisProc == null) {
            // 没有过程，默认返回
            String msg = "欢迎您来到股计，股计是于千股之中取上涨个股的好帮手。\n" +
                    "[看股] 查看分析师荐股。\n" +
                    "[荐股] 推荐股票、点评大盘。";
            return WXProcessMsg.createResTextMessage(reqMap, msg);
        } else {
            /** 认证的处理逻辑 */
            if (redisProc.toString().contains("auth")) {
                String res = this.wxAuth.process(reqMap, redisProc.toString());
                if (res.equals("success")) {
                    return "success";
                }
                return WXProcessMsg.createResTextMessage(reqMap, res);
            }
        }
        return "success";
    }

    /**
     * 处理用户发送的语音消息
     * @param reqMap
     * @return
     */
    public String processVoiceMessage(Map<String, String> reqMap) {
        return "success";
        //return WXProcessMsg.createResTextMessage(reqMap, "你发送了一段语音");
    }

    /**
     * 处理用户发送的视频信息
     * @param reqMap
     * @return
     */
    public String processVideoMessage(Map<String, String> reqMap) {
        return "success";
        //return WXProcessMsg.createResTextMessage(reqMap, "你发送了一段视频");
    }

    /**
     * 处理用户发送的小视频信息
     * @param reqMap
     * @return
     */
    public String processShortVideoMessage(Map<String, String> reqMap) {
        return "success";
        //return WXProcessMsg.createResTextMessage(reqMap, "你发送了一段小视频");
    }

    /**
     * 处理用户发送的地理位置
     * @param reqMap
     * @return
     */
    public String processLocationMessage(Map<String, String> reqMap) {
        return "success";
        //return WXProcessMsg.createResTextMessage(reqMap, "你发送了一个地理位置");
    }

    /**
     * 处理用户发送的链接消息
     * @param reqMap
     * @return
     */
    public String processLinkMessage(Map<String, String> reqMap) {
        return "success";
        //return WXProcessMsg.createResTextMessage(reqMap, "你发送了一段链接");
    }

    /**
     * 处理用户的事件消息（订阅、取消订阅、点击）
     * @param reqMap
     * @return
     */
    public String processEventMessage(Map<String, String> reqMap) throws Exception {
        switch (reqMap.get("Event")) {
            case WXMessageUtil.EVENT_TYPE_SUBSCRIBE :
                return this.processSubscribeMessage(reqMap);
            case WXMessageUtil.EVENT_TYPE_UNSUBSCRIBE :
                return this.processUnSubscribeMessage(reqMap);
            case WXMessageUtil.EVENT_TYPE_CLICK :
                return this.processClickMessage(reqMap);
            case WXMessageUtil.EVENT_TYPE_VIEW :
                return this.processViewMessage(reqMap);
        }
        return "success";
    }

    // 关注服务号的事件处理
    private String processSubscribeMessage(Map<String, String> reqMap) throws Exception {
        String openId = reqMap.get("FromUserName");
        Map<String, String> wxMap = this.wxBaseService.getUserInfoByOpenId(openId);
        if (wxMap == null) {
            throw new CustomerException("用户关注服务号，根据openID获取用户信息，返回数据失败", 888888);
        }
        // 将关注用户添加到自己的用户表中
        GujiUserEntity userEntity = this.gujiService.getUserByOpenId(openId);
        if (userEntity == null) {
            userEntity = new GujiUserEntity();
            userEntity.setOpenId(wxMap.get("openid"));
            userEntity.setNickname(wxMap.get("nickname"));
            userEntity.setHeadimgurl(wxMap.get("headimgurl"));
            userEntity.setSubscribe(wxMap.get("subscribe") == null ? 0 : Integer.valueOf(wxMap.get("subscribe")));
            userEntity.setPublicRecom(1);
            userEntity.setCertificationAuth("");
            userEntity.setCardPic("");
            userEntity.setAuthStatus(EGujiAuthStatus.NO.getCode());
        } else {
            userEntity.setNickname(wxMap.get("nickname"));
            userEntity.setHeadimgurl(wxMap.get("headimgurl"));
            userEntity.setSubscribe(wxMap.get("subscribe") == null ? 0 : Integer.valueOf(wxMap.get("subscribe")));
        }
        userEntity.setSubscribe(1);
        Boolean res = this.gujiService.addOrUpdateUserInfo(userEntity);

        String resStr = "欢迎您来到股计，股计是于千股之中取上涨个股的好帮手。\n" +
                "[看股] 查看分析师荐股。\n" +
                "[荐股] 推荐股票、点评大盘。";
        return WXProcessMsg.createResTextMessage(reqMap, resStr);
    }

    // 取消服务号关注处理的事件
    private String processUnSubscribeMessage(Map<String, String> reqMap) throws Exception {
        // 将关注用户添加到自己的用户表中
        GujiUserEntity userEntity = new GujiUserEntity();
        userEntity.setOpenId(reqMap.get("FromUserName"));
        userEntity.setSubscribe(0);
        this.gujiService.addOrUpdateUserInfo(userEntity);
        return "success";
    }

    // 点击自定义菜单的事件处理
    private String processClickMessage(Map<String, String> reqMap) throws Exception {
        switch (reqMap.get("EventKey")) {
            case "C_JIANCANG":
                // 荐股 -> 首次荐股
                String jcStr = this.wxJianCang.clickProcess(reqMap);
                return WXProcessMsg.createResTextMessage(reqMap, jcStr);
            case "C_TIAOCANG":
                // 调仓 -> 持续跟踪
                String tcStr = this.wxTiaoCang.clickProcess(reqMap);
                return WXProcessMsg.createResTextMessage(reqMap, tcStr);
            case "C_DAPAN":
                // 大盘
                String dpStr = this.wxDaPan.clickProcess(reqMap);
                return WXProcessMsg.createResTextMessage(reqMap, dpStr);
            case "C_FEEDBACK":
                // 意见反馈
                return WXProcessMsg.createResTextMessage(reqMap, "有任何意见和建议，都扔过来吧！直接回复就会被记录");
            case "C_AUTH":
                return WXProcessMsg.createResTextMessage(reqMap, this.wxAuth.clickProcess(reqMap));
            case "C_SETTING":
                return WXProcessMsg.createResTextMessage(reqMap, this.wxSetting.clickProcess(reqMap));
        }
        return "success";
    }

    // 点击菜单跳转链接时的事件处理
    private String processViewMessage(Map<String, String> reqMap) {
        return "success";
        //return WXProcessMsg.createResTextMessage(reqMap, "你跳转到了一个链接");
    }

    /**
     * 创建返回的文本xml对象
     * @param reqMap
     * @param msg
     * @return
     */
    public static String createResTextMessage(Map<String, String> reqMap, String msg) {
        TextMessageEntity textMessageEntity = new TextMessageEntity();
        textMessageEntity.setToUserName(reqMap.get("FromUserName"));
        textMessageEntity.setFromUserName(reqMap.get("ToUserName"));
        textMessageEntity.setCreateTime(System.currentTimeMillis() / 1000);
        textMessageEntity.setContent(msg);
        return WXMessageUtil.textMessageToXml(textMessageEntity);
    }

}
