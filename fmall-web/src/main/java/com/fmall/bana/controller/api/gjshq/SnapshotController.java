package com.fmall.bana.controller.api.gjshq;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.hq.api.entity.SnapshotReq;
import com.caimao.hq.api.entity.TradeAmountReq;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 贵金属行情接口
 * Created by Administrator on 2015/10/20.
 */
@Controller
@RequestMapping(value = "/api/gjshq/snapshot")
public class SnapshotController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TickerController.class);

    @Autowired
    private IHQService hqService;

    /**
     * <p>查询行情列表信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/snapshot/query</p>
     * <p>请求方式：GET，POST</p>
     *
     * @param prodCode 最多支持200个产品，格式为AG.NJS 需用URLDecoder ，"UTF-8"编码
     * @param exchange 交易所代码，如果prodCode,exchange都传，优先以exchange为准 ，需用URLDecoder ，"UTF-8"编码
     * @param version  1沿用老版本 2用新版本,默认使用版本version=1的接口
     * @param objParaKeys 格式为json array  ：例子  objParaKeys=['aa','bb']   用来表示获取对象的aa,和bb属性 ，  支持的属性参考对象NJSCandle或者SJSCandle, 什么交易所就查看对应的K线对象
     * @param retParaKeys  格式为json array  ：例子  retParaKeys=['aa','bb']   用来控制返回的List  ret里面的属性。 具体可用值根据功能参考对应的K线或者分时对象
     * @return 查询列表
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public List query(
            @RequestParam(value = "prodCode", required = false) String prodCode,
            @RequestParam(value = "exchange", required = false) String exchange,
            @RequestParam(value = "version", required = false) String version,
            @RequestParam(value = "objParaKeys", required = false) String objParaKeys,
            @RequestParam(value = "retParaKeys", required = false) String retParaKeys
            ) throws Exception {

        SnapshotReq  req=new SnapshotReq();
        req.setObjParaKeys(objParaKeys);
        req.setRetParaKeys(retParaKeys);
        req.setExchange(exchange);
        req.setProdCode(prodCode);

        List snapshotResList = null;
        List snapshotList = null;

        try {

            if(!StringUtils.isBlank(version)&&"2".equalsIgnoreCase(version)){

                snapshotResList=hqService.queryLastSnapshotFormate(req);

            }else{

                snapshotResList = new ArrayList();
                snapshotList = hqService.queryLastSnapshot(exchange, prodCode);
                DozerMapperSingleton.listCopy(snapshotList, snapshotResList, "com.caimao.hq.api.entity.SnapshotRes");
            }
        } catch (Exception ex) {
            logger.error("查询行情列表信息{}：", ex.getMessage());
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return snapshotResList;
    }


    /**
     * <p>成交明细查询</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/snapshot/tradeAmountQueryHistory</p>
     * <p>请求方式：GET，POST</p>
     *
     * @param prodCode 产品代码 格式为产品code.交易所code,eg:AG.NJS，支持单个产品查询，需用URLDecoder ，"UTF-8"编码
     * @param number   查询数据条数，默认50,最多500条
     * @param version  1沿用老版本 2用新版本,默认使用版本version=1的接口
     * @param objParaKeys 格式为json array  ：例子  objParaKeys=['aa','bb']   用来表示获取对象的aa,和bb属性 ，  支持的属性参考对象NJSCandle或者SJSCandle, 什么交易所就查看对应的K线对象
     * @param retParaKeys  格式为json array  ：例子  retParaKeys=['aa','bb']   用来控制返回的List  ret里面的属性。 具体可用值根据功能参考对应的K线或者分时对象
     * @return 查询列表
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/tradeAmountQueryHistory", method = {RequestMethod.POST, RequestMethod.GET})
    public List queryHistory(@RequestParam(value = "prodCode", required = true) String prodCode,
                             @RequestParam(value = "number", required = true, defaultValue = "50") Integer number,
                             @RequestParam(value = "version", required = false) String version,
                             @RequestParam(value = "objParaKeys", required = false) String objParaKeys,
                             @RequestParam(value = "retParaKeys", required = false) String retParaKeys
                             ) throws Exception {


        TradeAmountReq tradeAmountReq = new TradeAmountReq();
        List snapShotList = null;
        List snapShotResList = null;
        List snapShotResListFormate = null;
        try {


            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
            String beginDate = df.format(new Date());
            String endDate = df.format(new Date());
            tradeAmountReq.setProdCode(prodCode);
            tradeAmountReq.setEndDate(endDate);
            tradeAmountReq.setBeginDate(beginDate);
            if (number > 500) {
                number = 500;
            }
            tradeAmountReq.setNumber(number);
            Map<String, String> map = ProductUtils.convertProductSingle(tradeAmountReq.getProdCode());
            tradeAmountReq.setExchange(map.get("exchange"));
            tradeAmountReq.setProdCode(map.get("prodCode"));
            tradeAmountReq.setObjParaKeys(objParaKeys);
            tradeAmountReq.setRetParaKeys(retParaKeys);

            if(!StringUtils.isBlank(version)&&"2".equalsIgnoreCase(version)){

                snapShotResList=hqService.queryTradeAmountRedisHistoryFormate(tradeAmountReq);

            }else{
                snapShotResList = new ArrayList();
                snapShotList = hqService.queryTradeAmountRedisHistory(tradeAmountReq);
                DozerMapperSingleton.listCopy(snapShotList, snapShotResList, "com.caimao.hq.api.entity.TradeAmountRes");

            }

        } catch (Exception ex) {
            logger.error("成交明细查询错误{}：", ex.getMessage());
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage()+ex.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return snapShotResList;
    }

    /**
     * <p>多日分时查询</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/snapshot/getMultiDaySnapshot</p>
     * <p>请求方式：GET，POST</p>
     *
     * @param prodCode 产品代码 格式为产品code.交易所code,eg:AG.NJS，支持单个产品查询，需用URLDecoder ，"UTF-8"编码
     * @param version  1沿用老版本 2用新版本,默认使用版本version=1的接口
     * @param type  表示获取几日分时，1表示1日分时，2表示2日分时，默认为1
     * @param cycle  表示分时周期，默认为1分钟，可以设置其他周期
     * @param objParaKeys 格式为json array  ：例子  objParaKeys=['aa','bb']   用来表示获取对象的aa,和bb属性 ，  支持的属性参考对象NJSCandle或者SJSCandle, 什么交易所就查看对应的K线对象
     * @param retParaKeys  格式为json array  ：例子  retParaKeys=['aa','bb']   用来控制返回的List  ret里面的属性。 具体可用值根据功能参考对应的K线或者分时对象
     * @return 查询列表
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getMultiDaySnapshot", method = {RequestMethod.POST, RequestMethod.GET})
    public List getMultiDaySnapshot(@RequestParam(value = "prodCode", required = true) String prodCode,
                                    @RequestParam(value = "type", required = true, defaultValue = "1") Integer type,
                                    @RequestParam(value = "cycle", required = false, defaultValue = "1") String cycle,
                                    @RequestParam(value = "version", required = false) String version,
                                    @RequestParam(value = "objParaKeys", required = false) String objParaKeys,
                                    @RequestParam(value = "retParaKeys", required = false) String retParaKeys
    ) throws Exception {

        TradeAmountReq tradeAmountReq = new TradeAmountReq();
        List snapShotList = new ArrayList();
        try {
            Map<String, String> map = ProductUtils.convertProductSingle(prodCode);
            tradeAmountReq.setExchange(map.get("exchange"));
            tradeAmountReq.setProdCode(map.get("prodCode"));
            tradeAmountReq.setType(type);
            tradeAmountReq.setObjParaKeys(objParaKeys);
            tradeAmountReq.setRetParaKeys(retParaKeys);
            tradeAmountReq.setCycle(cycle);
            if(!StringUtils.isBlank(version)&&"2".equalsIgnoreCase(version)){
                snapShotList=hqService.getMultiDaySnapshotFormate(tradeAmountReq);
            }else{
                List temp=new ArrayList();
                temp = hqService.getMultiDaySnapshot(tradeAmountReq);
                snapShotList.add(temp);
            }
        } catch (Exception ex) {
            logger.error("查询行情ticker信息异常  {}", ex);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return snapShotList;
    }

}
