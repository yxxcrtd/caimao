package com.fmall.bana.controller.api.gjshq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.hq.api.entity.GjsPriceAlertEntity;
import com.caimao.hq.api.entity.req.FQueryGjsPriceAlertReq;
import com.caimao.hq.api.service.IGjsPriceAlertService;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.CommonStringUtils;
import com.fmall.bana.utils.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 贵金属行情相关的服务API接口
 * Created by Administrator on 2015/11/24.
 */
@Controller
@RequestMapping(value = "/api/gjshq/")
public class GjsHqApiController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TickerController.class);

    @Resource
    private IGjsPriceAlertService gjsPriceAlertService;

    /**
     * <p>查询用户指定商品设置的价格提醒</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/price_alert_list</p>
     * <p>请求方式：GET，POST</p>
     *
     * @param token    登陆后返回的token
     * @param exchange 交易所简称
     * @param prodCode 商品代码
     * @return List
     * @throws Exception
     * @see com.caimao.hq.api.entity.GjsPriceAlertEntity
     * @see com.caimao.hq.api.enums.EPriceAlertType
     */
    @ResponseBody
    @RequestMapping(value = "/price_alert_list", method = {RequestMethod.GET, RequestMethod.POST})
    public Map queryPriceAlertList(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange", required = false) String exchange,
            @RequestParam(value = "prodCode", required = false) String prodCode
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FQueryGjsPriceAlertReq req = new FQueryGjsPriceAlertReq();
            req.setUserId(userId);
            req.setExchange(exchange);
            req.setGoodCode(prodCode);
            return CommonStringUtils.mapReturn(this.gjsPriceAlertService.queryGjsPriceAlertList(req));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("查询用户指定商品设置的价格提醒异常  {}", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>设置用户指定商品的价格提醒</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/set_price_alert</p>
     * <p>请求方式：POST</p>
     *
     * @param token    登陆后返回的token
     * @param exchange 交易所简称
     * @param prodCode 商品代码
     * @param prodName 商品名称
     * @param alertSet 提醒设置
     *                 条件：1 价格大于 2 价格小于 3 涨跌幅<br/>
     *                 开关：1 开 0 关<br/>
     *                 设置的价格条件/涨跌幅<br/>
     *                 例如 ：[{"condition":"1","on":"1","price":"123"},{"condition":"2","on":"0","price":"321"}]
     * @return Boolean
     * @throws Exception
     * @see com.caimao.hq.api.entity.GjsPriceAlertEntity
     * @see com.caimao.hq.api.enums.EPriceAlertType
     */
    @ResponseBody
    @RequestMapping(value = "/set_price_alert", method = RequestMethod.POST)
    public Map<String, Object> setPriceAlert(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange") String exchange,
            @RequestParam(value = "prodCode") String prodCode,
            @RequestParam(value = "prodName") String prodName,
            @RequestParam(value = "alertSet") String alertSet
    ) throws Exception {
        try {
            Map<String, Object> res = new HashMap<>();
            Long userId = this.getUserIdByToken(token);

            // 解析alertSet 的json字符串
            JSONArray alertSetObj = JSONArray.parseArray(alertSet);

            GjsPriceAlertEntity priceAlertEntity = new GjsPriceAlertEntity();
            priceAlertEntity.setUserId(userId);
            priceAlertEntity.setExchange(exchange);
            priceAlertEntity.setGoodCode(prodCode);
            priceAlertEntity.setGoodName(prodName);
            for (Object anAlertSetObj : alertSetObj) {
                JSONObject aObj = (JSONObject) anAlertSetObj;
                try {
                    priceAlertEntity.setCondition(aObj.get("condition").toString());
                    priceAlertEntity.setPrice(new BigDecimal(aObj.get("price").toString().isEmpty() ? "0" : aObj.get("price").toString()));
                    priceAlertEntity.setOn(aObj.get("on").toString());
                    this.gjsPriceAlertService.setPriceAlert(priceAlertEntity);
                } catch (Exception ignored) {
                }
            }
            res.put("result", true);
            return res;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("设置用户指定商品的价格提醒异常  {}", e);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

}
