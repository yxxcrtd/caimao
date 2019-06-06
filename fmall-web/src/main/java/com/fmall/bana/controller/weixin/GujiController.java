package com.fmall.bana.controller.weixin;

import com.fmall.bana.utils.weixin.WXBaseService;
import com.fmall.bana.utils.weixin.WXMenus;
import com.fmall.bana.utils.weixin.WXProcessMsg;
import com.fmall.bana.utils.weixin.aes.SHA1;
import com.fmall.bana.utils.weixin.utils.WXMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 微信股计的控制器页面
 */
@Controller
@RequestMapping(value = "/weixin")
public class GujiController {
    private final Logger logger = LoggerFactory.getLogger(GujiController.class);

    @Resource
    private WXBaseService wxBaseService;
    @Resource
    private WXProcessMsg wxProcessMsg;
    @Resource
    private WXMenus wxMenus;

    /**
     * 微信验证服务器有效性的方法
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @param response
     */
    @RequestMapping(value = "/gj.do", method = RequestMethod.GET)
    public void gj(
            @RequestParam(value = "signature") String signature,
            @RequestParam(value = "timestamp") String timestamp,
            @RequestParam(value = "nonce") String nonce,
            @RequestParam(value = "echostr") String echostr,
            HttpServletResponse response
    ) throws Exception {
        this.logger.info("验证服务器有效方法接收到参数 signature : {} timestamp : {} nonce : {} echostr : {}", signature, timestamp, nonce, echostr);
        //加密并验证是否相等
        String sha1Str = SHA1.getAuthSHA1(this.wxBaseService.getToken(), timestamp, nonce);
        this.logger.info("验证 SHA1 服务器加密后的字符串 : {}", sha1Str);
        if (sha1Str.equals(signature)) {
            this.logger.info("服务器验证通过");
            this.outputString(response, echostr);
        }
    }

    /**
     * 微信服务器事件处理
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/gj.do", method = RequestMethod.POST)
    public void gj(
            HttpServletResponse response,
            @RequestBody String xmlBody
    ) throws Exception {
        this.logger.info("微信发送的数据：{}", xmlBody);
        if (xmlBody == null) return;
        Map<String, String> xmlMap = WXMessageUtil.parseXml(xmlBody);
        if (xmlMap == null || xmlMap.isEmpty()) return;
        this.logger.info("接收到的数据解析值：{}", xmlMap);
        // 处理业务逻辑的类
        // 返回的结果
        String res = "success";
        switch (xmlMap.get("MsgType")) {
            case WXMessageUtil.REQ_MESSAGE_TYPE_TEXT :
                res = this.wxProcessMsg.processTextMessage(xmlMap);
                break;
            case WXMessageUtil.REQ_MESSAGE_TYPE_IMAGE :
                res = this.wxProcessMsg.processImageMessage(xmlMap);
                break;
            case WXMessageUtil.REQ_MESSAGE_TYPE_VOICE :
                res = this.wxProcessMsg.processVoiceMessage(xmlMap);
                break;
            case WXMessageUtil.REQ_MESSAGE_TYPE_VIDEO :
                res = this.wxProcessMsg.processVideoMessage(xmlMap);
                break;
            case WXMessageUtil.REQ_MESSAGE_TYPE_SHORT_VIDEO :
                res = this.wxProcessMsg.processShortVideoMessage(xmlMap);
                break;
            case WXMessageUtil.REQ_MESSAGE_TYPE_LOCATION :
                res = this.wxProcessMsg.processLocationMessage(xmlMap);
                break;
            case WXMessageUtil.REQ_MESSAGE_TYPE_LINK :
                res = this.wxProcessMsg.processLinkMessage(xmlMap);
                break;
            case WXMessageUtil.REQ_MESSAGE_TYPE_EVENT :
                res = this.wxProcessMsg.processEventMessage(xmlMap);
                break;
        }
        this.logger.info("微信处理结果返回值：{}", res);
        this.outputString(response, res);
    }

    /**
     * 创建菜单方法
     * @throws Exception
     */
    @RequestMapping(value = "/menus")
    public void test() throws Exception {
        // 删除并创建菜单
        this.wxMenus.delAll();
        Thread.sleep(5000);
        this.wxMenus.createMeuns();
    }

    /**
     * 用户本地代理测试的东西
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/proxy.pac")
    public ModelAndView proxy() throws Exception {
        return new ModelAndView("/weixin/proxy");
    }

    /**
     * 将指定的字符串输出到页面
     * @param response
     * @param str
     * @throws IOException
     */
    private void outputString(HttpServletResponse response, String str) throws IOException {
        response.getWriter().print(str);
        response.getWriter().flush();
    }
}
