package com.fmall.bana.utils.weixin;

import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.service.guji.IGujiService;
import com.fmall.bana.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户隐私设置功能
 * Created by Administrator on 2016/1/12.
 */
@Component
public class WXSetting {

    private static final Logger logger = LoggerFactory.getLogger(WXSetting.class);

    @Resource
    private WXBaseService wxBaseService;
    @Resource
    private IGujiService gujiService;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 用户点击认证的功能
     * @param reqMap
     * @return
     */
    public String clickProcess(Map<String, String> reqMap) throws Exception {
        String formOpenId = reqMap.get("FromUserName");
        // 设置当前用户处理过程的标记
        String key = WXBaseService.redisCurrentProcKey + formOpenId;
        String val = "setting";
        this.redisUtils.set(key, val, WXBaseService.redisCacheUserInfoExpires);

        GujiUserEntity userEntity = this.gujiService.getUserByOpenId(formOpenId);
        if (userEntity.getPublicRecom().equals(1)) {
            return "您当前设置为推荐分享到大厅\n" +
                    "回复0，您的推荐不会分享到大厅\n" +
                    "回复1，您的推荐会分享到大厅\n" +
                    "取消设置请回复c";
        } else {
            return "您当前设置为推荐不分享到大厅\n" +
                    "回复0，您的推荐不会分享到大厅\n" +
                    "回复1，您的推荐会分享到大厅\n" +
                    "取消设置请回复c";
        }
    }

    /**
     * 处理用户发送title的请求
     * @param reqMap
     * @return
     * @throws Exception
     */
    public String process(Map<String, String> reqMap, String currentProc) throws Exception {
        String formOpenId = reqMap.get("FromUserName");

        if (reqMap.get("MsgType").equals("text") && reqMap.get("Content").equalsIgnoreCase("c")) {
            this.redisUtils.del(WXBaseService.redisCurrentProcKey+formOpenId);
            return "您取消了设置";
        }
        switch (currentProc) {
            case "setting" :
                String isPublic = reqMap.get("Content");
                if (!(isPublic.equals("0") || isPublic.equals("1"))) {
                    return "回复0，您的推荐不会分享到大厅\n" +
                           "回复1，您的推荐会分享到大厅\n" +
                           "取消设置请回复c";
                }

                GujiUserEntity userEntity = this.gujiService.getUserByOpenId(formOpenId);
                GujiUserEntity updateUserEntity = new GujiUserEntity();
                updateUserEntity.setWxId(userEntity.getWxId());
                updateUserEntity.setOpenId(userEntity.getOpenId());
                updateUserEntity.setPublicRecom(Integer.valueOf(isPublic));
                this.gujiService.addOrUpdateUserInfo(updateUserEntity);

                this.redisUtils.del(WXBaseService.redisCurrentProcKey+formOpenId);
                return "设置成功";
            default:
                this.redisUtils.del(WXBaseService.redisCurrentProcKey+formOpenId);
                return "success";
        }
    }

}
