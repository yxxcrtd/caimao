package com.fmall.bana.controller.api.gjshq;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.entity.SnapshotReq;
import com.caimao.hq.api.entity.SnapshotTrickRes;
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

import java.util.List;
import java.util.Map;

/**
 * 贵金属行情数据
 * Created by Administrator on 2015/10/20.
 */
@Controller
@RequestMapping(value = "/api/gjshq/ticker")
public class TickerController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TickerController.class);

    @Autowired
    private IHQService hqService;

    /**
     * <p>查询行情ticker信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/gjshq/ticker/query</p>
     * <p>请求方式：GET，POST</p>
     *
     * @param prodCode 最多支持1个产品，格式为AG.NJS，需用URLDecoder ，"UTF-8"编码
     * @param version  1沿用老版本 2用新版本,默认使用版本version=1的接口
     * @param objParaKeys 格式为json array  ：例子  objParaKeys=['aa','bb']   用来表示获取对象的aa,和bb属性 ，  支持的属性参考对象NJSCandle或者SJSCandle, 什么交易所就查看对应的K线对象
     * @param retParaKeys  格式为json array  ：例子  retParaKeys=['aa','bb']   用来控制返回的List  ret里面的属性。 具体可用值根据功能参考对应的K线或者分时对象
     * @return 查询列表
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public Object queryTicker(
            @RequestParam(value = "prodCode", required = false) String prodCode,
            @RequestParam(value = "version", required = false) String version,
            @RequestParam(value = "objParaKeys", required = false) String objParaKeys,
            @RequestParam(value = "retParaKeys", required = false) String retParaKeys
    ) throws Exception {
        Snapshot snapshot = null;
        Object snapshotTrickRes = null;

        try {
            Map map = ProductUtils.convertProductSingle(prodCode);
            String strExchange = (String) map.get("exchange");
            String strProdCode = (String) map.get("prodCode");
            SnapshotReq req=new SnapshotReq();
            req.setExchange(strExchange);
            req.setProdCode(strProdCode);
            req.setObjParaKeys(objParaKeys);
            req.setRetParaKeys(retParaKeys);

            if(!StringUtils.isBlank(version)&&"2".equalsIgnoreCase(version)){
                snapshotTrickRes=hqService.queryTickerFormate(req);
            }else{
                snapshot = hqService.queryTicker(strExchange, strProdCode);
                if (null != snapshot) {
                    snapshotTrickRes = new SnapshotTrickRes();
                    DozerMapperSingleton.getInstance().map(snapshot, snapshotTrickRes);
                }
            }

        } catch (Exception ex) {
            logger.error("查询行情ticker信息异常  {}", ex);
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return snapshotTrickRes;
    }
}
