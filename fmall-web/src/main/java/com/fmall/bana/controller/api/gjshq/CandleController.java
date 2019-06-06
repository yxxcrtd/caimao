package com.fmall.bana.controller.api.gjshq;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.hq.api.entity.Candle;
import com.caimao.hq.api.entity.CandleReq;
import com.caimao.hq.api.entity.CandleRes;
import com.caimao.hq.api.service.IHQService;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.exception.ApiException;
import com.fmall.bana.utils.gjs.DozerMapperSingleton;
import com.fmall.bana.utils.gjs.ProductUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 贵金属行情数据接口
 * Created by Administrator on 2015/10/20.
 */
@Controller
@RequestMapping(value = "/api/gjshq/candle")
public class CandleController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TickerController.class);

    @Autowired
    private IHQService hqService;

    /**
     * <p>查询最新K线</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/candle/queryLast</p>
     * <p>请求方式：GET，POST</p>
     * <p>其他行情：伦敦金：AU.LIFFE；伦敦银：AG.LIFFE；</p>
     *
     * @param prodCode 产品代码 格式为产品code.交易所code,eg:AG.NJS，支持单个产品查询, URLDecoder.decode(prodCode, "UTF-8")
     * @param cycle    查询周期 数字为1-11的,cycle 只能为数字，并且1：1分钟K线，2：5分钟K线，3：15分钟K线，4：30分钟K线，5：60分钟K线，6：日K线，7：周K线，8：月K线，9：年K线
     * @param version  1沿用老版本 2用新版本,默认使用版本version=1的接口，新版K线返回2条最新的K线数据，数据参数可以灵活设置
     * @param objParaKeys 格式为json array  ：例子  objParaKeys=['aa','bb']   用来表示获取对象的aa,和bb属性 ，  支持的属性参考对象NJSCandle或者SJSCandle, 什么交易所就查看对应的K线对象
     * @param retParaKeys  格式为json array  ：例子  retParaKeys=['aa','bb']   用来控制返回的List  ret里面的属性。 具体可用值根据功能参考对应的K线或者分时对象
     * @return 返回K线数据
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryLast", method = {RequestMethod.POST, RequestMethod.GET})
    public List queryLast(
            @RequestParam(value = "prodCode", required = false) String prodCode,
            @RequestParam(value = "cycle", required = false) String cycle,
            @RequestParam(value = "version", required = false) String version,
            @RequestParam(value = "objParaKeys", required = false) String objParaKeys,
            @RequestParam(value = "retParaKeys", required = false) String retParaKeys
    ) throws Exception {
        CandleReq obj = new CandleReq();
        List resList = new ArrayList();
        try {

            obj.setProdCode(prodCode);
            obj.setCycle(cycle);
            obj.setObjParaKeys(objParaKeys);
            obj.setRetParaKeys(retParaKeys);
            Map<String, String> map = ProductUtils.convertProductSingle(obj.getProdCode());

            if (null != map) {
                obj.setExchange(map.get("exchange"));
                obj.setProdCode(map.get("prodCode"));
                if(!StringUtils.isBlank(version)&&"2".equalsIgnoreCase(version)){
                    resList=hqService.queryLastCandleFormate(obj);
                }else{
                    Candle candle = hqService.queryLastCandle(obj);
                    CandleRes res =  new CandleRes();
                    DozerMapperSingleton.getInstance().map(candle, res);
                    resList.add(res);
                }
            }

        } catch (Exception ex) {
            logger.error("查询最新K线异常  {}", ex);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return resList;
    }

    /**
     * <p>查询K线历史数据</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/candle/queryHistory</p>
     * <p>请求方式：GET，POST</p>
     * <p>其他行情：伦敦金：AU.LIFFE；伦敦银：AG.LIFFE；</p>
     *
     * @param prodCode  产品代码 格式为产品code.交易所code,eg:AG.NJS，支持单个产品查询, 需用URLDecoder ，"UTF-8"编码
     * @param endDate   K线结束日期
     * @param beginDate K线开始日期
     * @param cycle     查询周期 数字为1-11的
     * @param number    查询数据条数，默认20
     * @param version  1沿用老版本 2用新版本,默认使用版本version=1的接口，新版历史K线返回的last表示最新K线数据
     * @param objParaKeys 格式为json array  ：例子  objParaKeys=['aa','bb']   用来表示获取对象的aa,和bb属性 ，  支持的属性参考对象NJSCandle或者SJSCandle, 什么交易所就查看对应的K线对象
     * @param retParaKeys  格式为json array  ：例子  retParaKeys=['aa','bb']   用来控制返回的List  ret里面的属性。 具体可用值根据功能参考对应的K线或者分时对象
     * @param searchDirection    查询方向，1表示向前，2表示向后，配合number使用。  可以传入beginDate 然后指定方向和查询条数。
     * @return 返回K线数据
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryHistory", method = {RequestMethod.POST, RequestMethod.GET})
    public List queryHistory(@RequestParam(value = "prodCode", required = true) String prodCode,
                             @RequestParam(value = "endDate", required = false) String endDate,
                             @RequestParam(value = "cycle", required = true) String cycle,
                             @RequestParam(value = "beginDate", required = false) String beginDate,
                             @RequestParam(value = "number", required = true, defaultValue = "20") Integer number,
                             @RequestParam(value = "version", required = false) String version,
                             @RequestParam(value = "objParaKeys", required = false) String objParaKeys,
                             @RequestParam(value = "retParaKeys", required = false) String retParaKeys,
                             @RequestParam(value = "searchDirection", required = false) String searchDirection
    ) throws Exception {
        List candleList = null;
        List candleResList = null;
        List candleResListFormate = null;
        CandleReq obj = new CandleReq();
        try {
//            if(!StringUtils.isBlank(prodCode)){
//                prodCode= URLDecoder.decode(prodCode, "UTF-8");
//            }
            obj.setProdCode(prodCode);
            obj.setEndDate(endDate);
            obj.setCycle(cycle);
            obj.setBeginDate(beginDate);
            obj.setNumber(number);
            obj.setObjParaKeys(objParaKeys);
            obj.setRetParaKeys(retParaKeys);
            obj.setSearchDirection(searchDirection);
            Map<String, String> map = ProductUtils.convertProductSingle(obj.getProdCode());
            obj.setExchange(map.get("exchange"));
            obj.setProdCode(map.get("prodCode"));
            if(!StringUtils.isBlank(version)&&"2".equalsIgnoreCase(version)){

                candleResList=hqService.queryCandleRedisHistoryFormate(obj);

            }else{
                candleList = hqService.queryCandleRedisHistory(obj);//查询Redis
                if (null != candleList) {
                    candleResList = new ArrayList();
                    DozerMapperSingleton.listCopy(candleList, candleResList, "com.caimao.hq.api.entity.CandleRes");
                }
            }

        } catch (Exception ex) {
            logger.error("查询K线历史数据异常  {}", ex);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return candleResList;
    }
}
