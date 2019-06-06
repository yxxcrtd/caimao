package com.fmall.bana.controller.api.gjshq;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.hq.api.entity.OwnProduct;
import com.caimao.hq.api.entity.ProductRes;
import com.caimao.hq.api.service.IHQService;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.controller.api.user.helper.UserApiHelper;
import com.fmall.bana.utils.exception.ApiException;
import com.fmall.bana.utils.gjs.DozerMapperSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贵金属自选股的接口
 * Created by Administrator on 2015/10/20.
 */
@Controller
@RequestMapping(value = "/api/gjshq/ownProduct")
public class OwnProductController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TickerController.class);

    @Autowired
    private IHQService hqService;

    @Resource
    private UserApiHelper userApiHelper;

    /**
     * <p>自选股查询</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/ownProduct/get</p>
     * <p>请求方式：GET，POST</p>
     *
     * @param token 用户ID
     * @return 用户的自选股
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/get", method = {RequestMethod.POST, RequestMethod.GET})
    public List<OwnProduct> getOwnProduct(
            @RequestParam(value = "token", required = true) String token
    ) throws Exception {
        List<OwnProduct> ownProduct = null;
        try {
            Long userId = this.getUserIdByToken(token);
            // 第一次那个啥的时候，添加默认的几个自选股
            this.userApiHelper.gjsDefaultOwnProductOpt(userId);
            ownProduct = hqService.queryOwnProductByUserId(userId);

        } catch (Exception e) {
            logger.error("自选股查询异常  {}", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return ownProduct;
    }

    /**
     * <p>添加自选股</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/ownProduct/save</p>
     * <p>请求方式：GET，POST</p>
     *
     * @param token    用户ID
     * @param exchange 交易所编码 需用URLDecoder ，"UTF-8"编码
     * @param prodCode 产品编码 需用URLDecoder ，"UTF-8"编码
     * @return 添加成功或者失败
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.GET})
    public Boolean saveOwnProduct(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "exchange", required = true) String exchange,
            @RequestParam(value = "prodCode", required = true) String prodCode

    ) throws Exception {
        Boolean isRight = false;
        OwnProduct ownProduct = new OwnProduct();
        try {
            Long userId = this.getUserIdByToken(token);
            ownProduct.setUserId(userId);
            ownProduct.setExchange(exchange);
            ownProduct.setProdCode(prodCode);
            hqService.insertOwnProduct(ownProduct);
            isRight = true;
        } catch (Exception ex) {
            logger.error("添加自选股异常  {}", ex);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());

        }
        return isRight;
    }

    /**
     * <p>修改自选股</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/ownProduct/update</p>
     * <p>请求方式：POST，GET</p>
     * exchange，prodCode  效果一样，可以只传其中一个
     *
     * @param token
     * @param data  json Array   [{"exchange":"","prodCode":"","sort":"","ownProductId":""},{"exchange":"","prodCode":"","sort":""]
     * @return 修改成功或者失败
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
    public Boolean update(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "data", required = true) String data
    ) throws Exception {

        Boolean isRight = false;
        Long userId = getUserIdByToken(token);
        // Long userId = Long.parseLong("808614298124289");
        try {

            List<OwnProduct> paraList = JSON.parseArray(data, OwnProduct.class);
            for (OwnProduct ownProduct : paraList) {
                if (null != ownProduct) {
                    ownProduct.setUserId(userId);
                    if (StringUtils.isBlank(ownProduct.getExchange()) || StringUtils.isBlank(ownProduct.getProdCode())) {
                        throw new RuntimeException("参数错误，交易所代码或者产品代码为空");
                    }
                }
            }
            hqService.updateOwnProduct(paraList);
            isRight = true;
        } catch (Exception ex) {
            logger.error("修改自选股异常  {}", ex);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return isRight;
    }

    /**
     * <p>删除自选股</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/ownProduct/delete</p>
     * <p>请求方式：GET,POST</p>
     * 可以根据ownProductId 删除，或者传入data =[{"prodCode":""，"exchange":""}]  删除。
     *
     * @param ownProductId 根据自选股主键删除,多个自选股以“，”分割
     * @param token
     * @return 删除成功或者失败
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public Boolean delete(
            @RequestParam(value = "ownProductId", required = false) String ownProductId,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "data", required = false) String data
    ) throws Exception {
        Boolean isRight = false;
        Long userId = this.getUserIdByToken(token);
        try {
            if (StringUtils.isBlank(ownProductId) && StringUtils.isBlank(data)) {
                throw new RuntimeException("参数异常,ownProductId或者data必须输入一种");
            }
            if (!StringUtils.isBlank(ownProductId)) {
                hqService.deleteOwnProduct(String.valueOf(userId), ownProductId);
            } else {

                List<OwnProduct> paraList = JSON.parseArray(data, OwnProduct.class);
                for (OwnProduct ownProduct : paraList) {
                    if (null != ownProduct) {
                        ownProduct.setUserId(userId);
                    }
                }
                hqService.deleteOwnProduct(paraList);
            }
            isRight = true;
        } catch (Exception ex) {
            logger.error("删除自选股异常  {}", ex);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return isRight;
    }

    /**
     * <p>按键精灵</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/ownProduct/wizard</p>
     * <p>请求方式：GET,POST</p>
     *
     * @param field 查询关键字 需用URLDecoder ，"UTF-8"编码
     * @param ts 请求时间戳，服务器端原样返回
     * @param version 接口版本号，默认为1位老版本，新版本version=2，对应的响应结构有编号
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/wizard", method = {RequestMethod.POST, RequestMethod.GET})
    public Object wizard(
            @RequestParam(value = "field", required = true) String field,
            @RequestParam(value = "ts", required = false) String ts,
            @RequestParam(value = "version", required = false) String version
    ) throws Exception {
        List productList = null;
        List productResList = null;
        Map resultMap=new HashMap();
        try {

            productList = hqService.wizard(field);
            productResList = new ArrayList<>();
            DozerMapperSingleton.listCopy(productList, productResList, "com.caimao.hq.api.entity.ProductRes");

        } catch (Exception ex) {
            logger.error("按键精灵异常  {}", ex);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        if(!StringUtils.isBlank(version)){
            resultMap.put("ret", productResList);
            resultMap.put("ts",ts);
            return resultMap;
        }else{
            return productResList;
        }

    }


    @ResponseBody
    @RequestMapping(value = "/setMaster", method = {RequestMethod.POST, RequestMethod.GET})
    public Boolean setMaster(
            @RequestParam(value = "status", required = true) String status,
            @RequestParam(value = "appName", required = true) String appName

    ) throws Exception {
        Boolean isRight = false;
        try {
            hqService.setMaster(appName,status);
            isRight=true;
        } catch (Exception ex) {
            logger.error("stop 失败:"+ex.getMessage());
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage()+ex.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return isRight;
    }


    @ResponseBody
    @RequestMapping(value = "/redisCleanLast", method = {RequestMethod.POST, RequestMethod.GET})
    public Boolean redisCleanLast(
            @RequestParam(value = "exchange", required = true) String exchange
    ) throws Exception {
        Boolean isRight = false;
        try {
            hqService.redisCleanLast(exchange);
            isRight=true;
        } catch (Exception ex) {
            logger.error("redisCleanLast失败:"+ex.getMessage());
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage()+ex.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return isRight;
    }
    @ResponseBody
    @RequestMapping(value = "/redisCleanHistory", method = {RequestMethod.POST, RequestMethod.GET})
    public Boolean redisCleanHistory(
            @RequestParam(value = "exchange", required = true) String exchange,
            @RequestParam(value = "prodCode", required = true) String prodCode,
            @RequestParam(value = "cycle", required = true) String cycle,
            @RequestParam(value = "miniTime", required = true) String miniTime

            ) throws Exception {
        Boolean isRight = false;
        try {
            hqService.redisCleanHistory( exchange, prodCode, cycle, miniTime);
            isRight=true;
        } catch (Exception ex) {
            logger.error("redisCleanHistory 失败:"+ex.getMessage());
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage()+ex.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return isRight;
    }

}
