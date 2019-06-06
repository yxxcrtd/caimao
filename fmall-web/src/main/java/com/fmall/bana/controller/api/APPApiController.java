package com.fmall.bana.controller.api;

import com.caimao.bana.api.entity.getui.GetuiUserIdMapEntity;
import com.caimao.bana.api.service.getui.IGetuiService;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.exception.ApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * APP 需要的相关接口
 * Created by Administrator on 2015/9/24.
 */
@RestController
@RequestMapping(value = "/api/app/")
public class APPApiController extends BaseController {

    @Resource
    private IGetuiService getuiService;

    /**
     * <p>绑定用户与手机的关连关系</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/app/bind_cid</p>
     * <p>请求方式：GET、POST</p>
     *
     * @param userId      用户ID
     * @param cid         机器CID
     * @param deviceType  手机类型 1 安卓 2 苹果
     * @param deviceToken 苹果的那个东西
     * @return 成功返回true，失败返回false
     * @throws Exception
     * @see com.caimao.bana.api.entity.getui.GetuiUserIdMapEntity
     * @see com.caimao.bana.api.enums.getui.EGetuiDeviceType
     */
    @ResponseBody
    @RequestMapping(value = "bind_cid")
    public Map<String, Object> bindGetuiCid(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "cid") String cid,
            @RequestParam(value = "deviceType") String deviceType,
            @RequestParam(value = "deviceToken", required = false, defaultValue = "") String deviceToken
    ) throws Exception {
        try {
            GetuiUserIdMapEntity userIdMapEntity = new GetuiUserIdMapEntity();
            userIdMapEntity.setUserId(userId);
            userIdMapEntity.setCid(cid);
            userIdMapEntity.setDeviceType(deviceType);
            userIdMapEntity.setDeviceToken(deviceToken);

            Boolean res = this.getuiService.bindCidAndUserId(userIdMapEntity);
            Map<String, Object> ret = new HashMap<>();
            ret.put("result", res);
            return ret;
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>解绑用户与手机的关连关系</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/app/unbind_cid</p>
     * <p>请求方式：GET、POST</p>
     *
     * @param userId 用户ID
     * @param cid    机器CID
     * @return 成功返回true，失败返回false
     * @throws Exception
     * @see com.caimao.bana.api.entity.getui.GetuiUserIdMapEntity
     * @see com.caimao.bana.api.enums.getui.EGetuiDeviceType
     */
    @ResponseBody
    @RequestMapping(value = "unbind_cid")
    public Map<String, Object> unbindGetuiCid(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "cid") String cid
    ) throws Exception {
        try {
            GetuiUserIdMapEntity userIdMapEntity = new GetuiUserIdMapEntity();
            userIdMapEntity.setUserId(userId);
            userIdMapEntity.setCid(cid);

            Boolean res = this.getuiService.unbindCidAndUserId(userIdMapEntity);
            Map<String, Object> ret = new HashMap<>();
            ret.put("result", res);
            return ret;
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>邮币卡APP版本信息查询</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/app/ybk_version</p>
     * <p>请求方式：GET</p>
     *
     * @return Map  version 版本号 title 更新的标题 content 更新的内容  isForce  （0 不强制  1 强制）   downloadUrl 下载链接
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "ybk_version", method = RequestMethod.GET)
    public Map<String, String> ybkVersion() throws Exception {
        Map<String, String> version = new HashMap<>();
        version.put("version", "105");
        version.put("title", "版本升级");
        version.put("content", "1、修复部分BUG\n");
        version.put("isForce", "0");
        version.put("downloadUrl", "https://ybk.caimao.com/ybk/topic/download.html");

        return version;
    }

    /**
     * <p>邮币卡APP版本信息查询</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/app/ybk_ios_version</p>
     * <p>请求方式：GET</p>
     *
     * @return Map  version 版本号 title 更新的标题 content 更新的内容  isForce  （0 不强制  1 强制）   downloadUrl 下载链接
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "ybk_ios_version", method = RequestMethod.GET)
    public Map<String, String> ybkiOsVersion() throws Exception {
        Map<String, String> version = new HashMap<>();
        version.put("version", "104");
        version.put("title", "版本升级");
        version.put("content", "1、修复部分BUG\n");
        version.put("isForce", "0");
        version.put("downloadUrl", "https://ybk.caimao.com/ybk/topic/download.html");

        return version;
    }

    /**
     * <p>贵金属Android APP版本信息查询</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/app/jin_android_version</p>
     * <p>请求方式：GET</p>
     *
     * @return Map  version 版本号 title 更新的标题 content 更新的内容  isForce  （0 不强制  1 强制）   downloadUrl 下载链接
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "jin_android_version", method = RequestMethod.GET)
    public Map<String, String> jinAndroidVersion() throws Exception {
        Map<String, String> version = new HashMap<>();
        version.put("version", "103");
        version.put("title", "版本升级");
        version.put("content", "1、全新页面，焕然一新\n2、分析师大神，为您指点迷津\n3、免去复杂流程，三步开户秒入金");
        version.put("isForce", "1");
        version.put("downloadUrl", "https://www.caimao.com/tpl?tpl=/gjs/article/gjsDownload");

        return version;
    }

    /**
     * <p>贵金属IOS APP版本信息查询</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/app/jin_ios_version</p>
     * <p>请求方式：GET</p>
     *
     * @return Map  version 版本号 title 更新的标题 content 更新的内容  isForce  （0 不强制  1 强制）   downloadUrl 下载链接
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "jin_ios_version", method = RequestMethod.GET)
    public Map<String, String> jiniOsVersion() throws Exception {
        Map<String, String> version = new HashMap<>();
        version.put("version", "103");
        version.put("title", "版本升级");
        version.put("content", "1、全新页面，焕然一新\n2、分析师大神，为您指点迷津\n3、免去复杂流程，三步开户秒入金");
        version.put("isForce", "1");
        version.put("downloadUrl", "https://www.caimao.com/tpl?tpl=/gjs/article/gjsDownload");

        return version;
    }
}
