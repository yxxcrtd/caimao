package com.fmall.bana.controller.weixin;

import com.caimao.bana.api.entity.guji.GujiFocusRecordEntity;
import com.caimao.bana.api.entity.guji.GujiShareRecordEntity;
import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.entity.guji.GujiUserStockEntity;
import com.caimao.bana.api.enums.guji.EGujiShareType;
import com.caimao.bana.api.enums.guji.EGujiStockType;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.weixin.WXBaseService;
import com.fmall.bana.utils.weixin.entity.TickerEntity;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的点评
 */
@RestController
@RequestMapping("weixin/guji")
public class GujiWebMyCommentController extends BaseController {

    /**
     * 我的点评
     *
     * @param wxId - 当前账户的微信Id
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping("myComment")
    public ModelAndView myComment(@RequestParam(value = "wxId", required = false) Long wxId, @RequestParam(value = "code", required = false) String code) throws Exception {
        ModelAndView mav = new ModelAndView("/weixin/guji/MyComment");
        String openId = null;

        Map<String, String> authInfo = null;
        if (code != null) this.saveAccessTokenAndOpenIdByCode(code, WXBaseService.scopeBase);
        authInfo = this.verificationAccessToken(WXBaseService.scopeBase);
        mav.addObject("authInfo", authInfo);
        if (authInfo == null) return null;  // 获取null，则返回null进行跳转授权页面
        openId = authInfo.get("openId");

        // 查询看的这个人是否关注了股计服务号
        Integer isFollowGuji = 0;
        GujiUserEntity checkGujiUserEntity = this.gujiService.getUserByOpenId(authInfo.get("openId"));
        if (checkGujiUserEntity != null && checkGujiUserEntity.getSubscribe().equals(1)) {
            isFollowGuji = 1;
            // 当前登录的人
            mav.addObject("loginOpenId", checkGujiUserEntity.getOpenId());
        }
        mav.addObject("isFollowGuji", isFollowGuji);

        // 获取JS SDK 需要的配置
        mav.addObject("jsApiTicket", this.wxBaseService.getJsApiTicket(this.getNowUrl()));




        // 获取分析师的个人信息
        GujiUserEntity gujiUserEntity = null;
        if (wxId == null) {
            gujiUserEntity = this.gujiService.getUserByOpenId(openId);
        } else {
            gujiUserEntity = this.gujiService.getUserById(wxId);
        }

        // 获取分析师的粉丝
        if (null != gujiUserEntity) {
            List<GujiFocusRecordEntity> focusMes = gujiService.focusMe(gujiUserEntity.getOpenId());
            logger.info("当前分析师的粉丝数：{}", focusMes.size());
            mav.addObject("focusMe", focusMes.size());
        }

        // 更新用户数据库中的微信用户基本信息
        this.wxBaseService.updateWeixinUserInfo(gujiUserEntity);

        // 获取分析师的持仓股票列表
        List<GujiUserStockEntity> userStockEntityList = this.gujiService.getMyStockList(gujiUserEntity.getWxId());
        // 获取是否关注
        Boolean isFollow = this.gujiService.checkIsFollow(openId, gujiUserEntity.getWxId());

        /**
         * 微信分享的信息
         */
        String shareDesc = "";
        TickerEntity tickerEntity = null;
        for (GujiUserStockEntity stockEntity: userStockEntityList) {
            if (stockEntity.getStockType().equals(EGujiStockType.GP.getCode())) {
                shareDesc += "、" + stockEntity.getStockName();
            }
            try {
                tickerEntity = this.stockHQService.getTicket(stockEntity.getStockCode());
                if (null != tickerEntity) {
                    stockEntity.setCurrentPrice(tickerEntity.getCurPrice());

                    float inc = (tickerEntity.getCurPrice() - Float.parseFloat(tickerEntity.getClosePrice())) / Float.parseFloat(tickerEntity.getClosePrice()) * 100;
                    stockEntity.setIncrease(inc);
                    logger.info("当前股票：{}，现价：{}，时价：{}，涨幅：{}", tickerEntity.getCode(), tickerEntity.getCurPrice(), tickerEntity.getClosePrice(), inc);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        shareDesc = !shareDesc.equals("") ? shareDesc.substring(1, shareDesc.length()) : "股计，精英分析师社群";
        Map<String, String> shareInfo = new HashMap<>();
        shareInfo.put("title", gujiUserEntity.getNickname() + "的个人荐股主页");
        shareInfo.put("desc", "当前推荐：" + shareDesc);
        shareInfo.put("link", this.wxBaseService.getDomain()+"/weixin/guji/analystIndex.html?wxId="+gujiUserEntity.getWxId());
        shareInfo.put("imgUrl", gujiUserEntity.getHeadimgurl());
        shareInfo.put("type", "link");
        mav.addObject("shareInfo", shareInfo);
        mav.addObject("userInfo", gujiUserEntity);
        mav.addObject("stockList", userStockEntityList);
        mav.addObject("isFollow", isFollow);

        return mav;
    }

    /**
     * 点评个股的首页
     *
     * @param wxId
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping("myCommentGP")
    public ModelAndView myCommentGP(@RequestParam(value = "wxId", required = false) Long wxId, @RequestParam(value = "code", required = false) String code) throws Exception {
        ModelAndView mav = new ModelAndView();

//        if (null != code) {
//            this.saveAccessTokenAndOpenIdByCode(code, WXBaseService.scopeBase);
//        }
//        Map<String, String> authInfo = this.verificationAccessToken(WXBaseService.scopeBase);
//        mav.addObject("authInfo", authInfo);
//
//        if (null == authInfo) {
//            return null;  // 获取null，则返回null进行跳转授权页面
//        }
//        String openId = authInfo.get("openId");
//
//        // 获取分析师的个人信息
//        GujiUserEntity gujiUserEntity = null;
//        if (null == wxId) {
//            gujiUserEntity = this.gujiService.getUserByOpenId(openId);
//        } else {
//            gujiUserEntity = this.gujiService.getUserById(wxId);
//        }

        // 获取分析师的个人信息
        GujiUserEntity gujiUserEntity = getGujiUserEntityByAccessToken(code, mav, wxId);
        mav.addObject("wxId", gujiUserEntity.getWxId());

        // 获取分析师的持仓股票列表
        List<GujiUserStockEntity> userStockEntityList = this.gujiService.getMyStockList(gujiUserEntity.getWxId());
        if (0 < userStockEntityList.size()) {
            mav.addObject("userStockEntityList", userStockEntityList);
            mav.setViewName("/weixin/guji/MyCommentGPList");
        } else {
            mav.setViewName("/weixin/guji/MyCommentGPEdit");
        }
        return mav;
    }

    /**
     * 点评个股的编辑
     *
     * @param id
     * @param stock
     * @return
     * @throws Exception
     */
    @RequestMapping("myCommentGPEdit")
    public ModelAndView myCommentGPEdit(@RequestParam(value = "wxId", required = false) Long wxId, @RequestParam(value = "code", required = false) String code, @RequestParam(value = "id", required = true) Long id, @RequestParam(value = "stock", required = true) String stock) throws Exception {
        logger.info("当前用户的微信Id：{}", wxId);
        GujiShareRecordEntity entity = new GujiShareRecordEntity();
        entity.setStockType("GP");
        ModelAndView mav = new ModelAndView();
        GujiUserEntity gujiUserEntity = getGujiUserEntityByAccessToken(code, mav, wxId);
        if (null != gujiUserEntity) {
            entity.setOpenId(gujiUserEntity.getOpenId());
        }

        GujiUserStockEntity userStockEntity = null;
        if (null != id && !"".equals(id)) {
            userStockEntity = gujiService.findById(id);
            entity.setOpenId(userStockEntity.getOpenId());
        }

        if (null != wxId) {
            entity.setWxId(wxId);
        }

        mav.addObject("entity", entity);
        mav.setViewName("/weixin/guji/MyCommentGPEdit");
        return mav;
    }

    /**
     * 点评个股的保存
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("myCommentGPSave")
    public ModelAndView myCommentGPSave(@ModelAttribute("entity") @Valid GujiShareRecordEntity entity, BindingResult result) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("entity", entity);
        mav.setViewName("/weixin/guji/MyCommentGPEdit");
        if (result.hasErrors()) {
            return mav;
        }

        if (null == entity.getStockCode() || "".equals(entity.getStockCode())) {
            mav.addObject("tips", "股票代码不能为空！");
            return mav;
        }
        if (null == entity.getReason() || "".equals(entity.getReason())) {
            mav.addObject("tips", "点评不能为空！");
            return mav;
        }

        // 验证股票是否存在
        if (null != entity.getStockCode() && !"".equals(entity.getStockCode())) {
            TickerEntity ticker = this.stockHQService.getTicket(entity.getStockCode());
            if (null == ticker) {
                mav.addObject("tips", "股票不存在！");
                return mav;
            } else {
                entity.setStockName(ticker.getName());
                entity.setStockPrice(String.valueOf(ticker.getCurPrice()));
            }
        }

        // 保存
        entity.setCreateTime(new Date());
        gujiService.addShareStockInfo(entity);


        return new ModelAndView("redirect:/weixin/guji/MyCommentDPEdit");
    }

    /**
     * 点评大盘
     *
     * @param wxId
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping("myCommentDP")
    public ModelAndView myCommentDP(@RequestParam(value = "wxId", required = false) Long wxId, @RequestParam(value = "code", required = false) String code) throws Exception {
        ModelAndView mav = new ModelAndView("/weixin/guji/MyCommentDP");

        return mav;
    }

}
