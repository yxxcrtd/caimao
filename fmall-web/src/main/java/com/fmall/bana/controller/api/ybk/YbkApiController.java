package com.fmall.bana.controller.api.ybk;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.dubbo.common.json.JSONObject;
import com.caimao.bana.api.entity.TpzBankTypeEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.req.ybk.*;
import com.caimao.bana.api.entity.res.ybk.*;
import com.caimao.bana.api.entity.ybk.YBKAccountEntity;
import com.caimao.bana.api.entity.ybk.YBKArticleEntity;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import com.caimao.bana.api.service.IUserBankCardService;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.service.ybk.IYBKAccountService;
import com.caimao.bana.api.service.ybk.IYBKService;
import com.caimao.bana.api.utils.DateUtil;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.bana.common.api.exception.CustomerException;
import com.fmall.bana.controller.BaseController;
import com.fmall.bana.utils.ImageUtils;
import com.fmall.bana.utils.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 邮币卡相关API服务接口控制器
 * Created by wangxu on 2015/9/10.
 */
@Controller
@RequestMapping(value = "/api/ybk/")
public class YbkApiController extends BaseController {

    @Value("${picturePath}")
    private String picturePath;

    @Resource
    private IYBKService ybkService;

    @Resource
    private IUserService userService;

    @Resource
    private IUserBankCardService userBankCardService;

    @Resource
    private IYBKAccountService ybkAccountService;

    /**
     * <p>根据邮币卡交易所ID获取交易所信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/get_exchange_info?id=16</p>
     * <p>请求方式：GET</p>
     *
     * @param id 交易所ID
     * @return YbkExchangeEntity    交易所实体信息
     * @throws Exception
     * @see com.caimao.bana.api.entity.ybk.YbkExchangeEntity
     */
    @ResponseBody
    @RequestMapping(value = "/get_exchange_info", method = RequestMethod.GET)
    public YbkExchangeEntity getExchangeInfo(@RequestParam(value = "id") Integer id) throws Exception {
        try {
            return this.ybkService.getExchangeById(id);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询交易所列表</p>
     * <p>status为可传参数，不传返回所有状态的交易所</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/query_exchange_list?status=2</p>
     * <p>请求方式：GET</p>
     *
     * @param status 交易所状态  1 不显示  2 显示  3 删除
     * @return List<YbkExchangeEntity> 交易所列表
     * @throws Exception
     * @see com.caimao.bana.api.entity.ybk.YbkExchangeEntity
     */
    @ResponseBody
    @RequestMapping(value = "/query_exchange_list", method = RequestMethod.GET)
    public List<YbkExchangeEntity> queryExchangeList(@RequestParam(value = "status", required = false) Integer status) throws Exception {
        try {
            FYbkExchangeQueryListReq req = new FYbkExchangeQueryListReq();
            if (status != null) req.setStatus(status);
            return this.ybkService.queryExchangeList(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>根据银行卡编号，获取银行信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/get_bank_info?bank_no=001</p>
     * <p>请求方式：GET</p>
     *
     * @param bankNo 银行编号 例如 : 001
     * @return TpzBankTypeEntity   银行信息
     * @throws Exception
     * @see com.caimao.bana.api.entity.TpzBankTypeEntity
     */
    @ResponseBody
    @RequestMapping(value = "/get_bank_info", method = RequestMethod.GET)
    public TpzBankTypeEntity getBankInfo(@RequestParam(value = "bank_no") String bankNo) throws Exception {
        try {
            return this.userBankCardService.getBankInfoById(bankNo, 3L);
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>获取支持的银行卡列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/query_bank_list</p>
     * <p>请求方式：GET</p>
     *
     * @return List<TpzBankTypeEntity>  银行卡列表数据
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/query_bank_list", method = RequestMethod.GET)
    public List<TpzBankTypeEntity> queryBankList() throws Exception {
        try {
            return this.userBankCardService.queryBankList(3L);
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>根据银行卡编号，获取支持的交易所列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/query_exchange_by_bankno?bank_no=001</p>
     * <p>请求方式：GET</p>
     *
     * @param bankNo 银行编号 例如 : 001
     * @return List<YbkExchangeEntity>
     * @throws Exception
     * @see com.caimao.bana.api.entity.ybk.YbkExchangeEntity
     */
    @ResponseBody
    @RequestMapping(value = "/query_exchange_by_bankno", method = RequestMethod.GET)
    public List<YbkExchangeEntity> queryExchangeByBankNo(@RequestParam(value = "bank_no") String bankNo) throws Exception {
        try {
            return this.ybkService.queryExchangeByBankNo(bankNo);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询所有交易所的综合指数信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/query_composite_index</p>
     * <p>请求方式：GET</p>
     *
     * @return List<FYBKCompositeIndexRes> 交易所综合指数列表
     * @throws Exception
     * @see com.caimao.bana.api.entity.res.ybk.FYBKCompositeIndexRes
     */
    @ResponseBody
    @RequestMapping(value = "/query_composite_index", method = RequestMethod.GET)
    public List<FYBKCompositeIndexRes> queryCompositeIndex() throws Exception {
        try {
            return this.ybkService.queryCompositeIndex();
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询交易所商品行情信息列表</p>
     * <p>默认查询最新一天的行情数据，默认按照涨幅排行，默认每页显示10个</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/query_collection_ranking?exchange_short_name=nfwjs</p>
     * <p>请求方式：GET</p>
     *
     * @param exchageShortName 交易所简称  例如：nfwjs
     * @param hqDate           获取哪一天的行情数据列表，默认为空，获取最新一天的数据
     * @param orderColumn      以什么字段排序，默认 changeRate 涨跌幅排序（currentPrice 当前价 openPrice 开盘价 ...）
     * @param orderDir         排序类型，默认 DESC   （DESC 降序  ASC 升序）
     * @param limit            返回条数，默认10条
     * @param page             当前页数，默认第一页
     * @return FYbkQueryCollectionRankingReq  藏品行情信息
     * @throws Exception
     * @see com.caimao.bana.api.entity.res.ybk.FYBKCollectionRankingRes
     * @see com.caimao.bana.api.entity.req.ybk.FYbkQueryCollectionRankingReq
     */
    @ResponseBody
    @RequestMapping(value = "/query_collection_ranking", method = RequestMethod.GET)
    public FYbkQueryCollectionRankingReq queryCollectionRanking(
            @RequestParam(value = "exchange_short_name") String exchageShortName,
            @RequestParam(value = "hq_date", required = false) Date hqDate,
            @RequestParam(value = "order_column", required = false, defaultValue = "changeRate") String orderColumn,
            @RequestParam(value = "order_dir", required = false, defaultValue = "DESC") String orderDir,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) throws Exception {
        try {
            FYbkQueryCollectionRankingReq req = new FYbkQueryCollectionRankingReq();
            req.setExchangeShortName(exchageShortName);
            req.setHqDate(hqDate);
            req.setOrderColumn(orderColumn);
            req.setOrderDir(orderDir);
            req.setLimit(limit);
            req.setStart((page - 1) * limit);
            return this.ybkService.queryCollectionRanking(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>获取指定交易所下，指定商品的最新一天的行情信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/get_collection_info?exchange_short_name=nfwjs&code=101001</p>
     * <p>请求方式：GET</p>
     *
     * @param exchangeShortName 交易所简称
     * @param code              商品代码
     * @return FYBKCollectionRankingRes    商品最新的行情信息
     * @throws Exception
     * @see com.caimao.bana.api.entity.res.ybk.FYBKCollectionRankingRes
     */
    @ResponseBody
    @RequestMapping(value = "/get_collection_info", method = RequestMethod.GET)
    public FYBKCollectionRankingRes getCollectionInfo(
            @RequestParam(value = "exchange_short_name") String exchangeShortName,
            @RequestParam(value = "code") String code
    ) throws Exception {
        try {
            return this.ybkService.getCollectionInfo(exchangeShortName, code);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>获取指定交易所下，指定商品的最新一天的行情信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/get_collection_infos?condition={"njwjs":[100001],"nfwjs":[101001,101002]"}</p>
     * <p>请求方式：POST</p>
     *
     * @param condition 查询条件，json串，例如{"njwjs":[100001],"nfwjs":[101001,101002]"}
     * @return List<FYBKCollectionRankingRes>    商品最新的行情信息
     * @throws Exception
     * @see com.caimao.bana.api.entity.res.ybk.FYBKCollectionRankingRes
     */
    @ResponseBody
    @RequestMapping(value = "/get_collection_infos", method = {RequestMethod.POST, RequestMethod.GET})
    public List<FYBKCollectionRankingRes> getCollectionInfos(@RequestParam(value = "condition") String condition) throws Exception {
        try {
            List<FYBKCollectionRankingRes> list = new ArrayList<>();
            JSONObject jsonObject = (JSONObject) JSON.parse(condition);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                JSONArray jsonArray = jsonObject.getArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(this.ybkService.getCollectionInfo(key, jsonArray.getString(i)));
                }
            }
            return list;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询交易所指定藏品的分时行情数据</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/query_timeline?exchange_short_name=nfwjs&code=101001</p>
     * <p>请求方式：GET</p>
     *
     * @param exchangeShortName 交易所简称
     * @param code              藏品代码
     * @param limit             查询多少条记录，默认300条
     * @return List<FYBKTimeLineRes>    分时数据列表
     * @throws Exception
     * @see com.caimao.bana.api.entity.res.ybk.FYBKTimeLineRes
     */
    @ResponseBody
    @RequestMapping(value = "/query_timeline", method = RequestMethod.GET)
    public List<FYBKTimeLineRes> queryTimeLine(
            @RequestParam(value = "exchange_short_name") String exchangeShortName,
            @RequestParam(value = "code") String code,
            @RequestParam(value = "limit", required = false, defaultValue = "300") Integer limit
    ) throws Exception {
        try {
            FYbkMarketReq req = new FYbkMarketReq();
            req.setExchangeShortName(exchangeShortName);
            req.setCode(code);
            req.setLimit(800);
            if (DateUtil.getTimeNum() > 93000) {//只取今天的值
                req.setDatetime(DateUtil.getTodayStart());
            }
            return this.ybkService.queryTimeLine(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询交易所指定藏品的日K行情数据</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/query_kline?exchange_short_name=nfwjs&code=101001</p>
     * <p>请求方式：GET</p>
     *
     * @param exchangeShortName 交易所简称
     * @param code              藏品代码
     * @param limit             查询多少条记录，默认300条
     * @return List<FYBKKLineRes>    日K数据列表
     * @throws Exception
     * @see com.caimao.bana.api.entity.res.ybk.FYBKKLineRes
     */
    @ResponseBody
    @RequestMapping(value = "/query_kline", method = RequestMethod.GET)
    public List<FYBKKLineRes> queryKLine(
            @RequestParam(value = "exchange_short_name") String exchangeShortName,
            @RequestParam(value = "code") String code,
            @RequestParam(value = "limit", required = false, defaultValue = "300") Integer limit
    ) throws Exception {
        try {
            FYbkMarketReq req = new FYbkMarketReq();
            req.setExchangeShortName(exchangeShortName);
            req.setCode(code);
            req.setLimit(limit);
            return this.ybkService.queryKLine(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询交易所指定藏品的MACD指标数据</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/query_macd?exchange_short_name=nfwjs&code=101001</p>
     * <p>请求方式：GET</p>
     *
     * @param exchangeShortName 交易所简称
     * @param code              藏品代码
     * @param limit             查询多少条记录，默认300条
     * @return List<FYBKKLineRes>    MACD数据列表
     * @throws Exception
     * @see com.caimao.bana.api.entity.res.ybk.FYBKMACDRes
     */
    @ResponseBody
    @RequestMapping(value = "/query_macd", method = RequestMethod.GET)
    public List<FYBKMACDRes> queryMACD(
            @RequestParam(value = "exchange_short_name") String exchangeShortName,
            @RequestParam(value = "code") String code,
            @RequestParam(value = "limit", required = false, defaultValue = "300") Integer limit
    ) throws Exception {
        try {
            FYbkMarketReq req = new FYbkMarketReq();
            req.setExchangeShortName(exchangeShortName);
            req.setCode(code);
            req.setLimit(limit);
            return this.ybkService.queryMACD(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询交易所指定藏品的KDJ指标数据</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/query_kdj?exchange_short_name=nfwjs&code=101001</p>
     * <p>请求方式：GET</p>
     *
     * @param exchangeShortName 交易所简称
     * @param code              藏品代码
     * @param limit             查询多少条记录，默认300条
     * @return List<FYBKKDJRes>    KDJ数据列表
     * @throws Exception
     * @see com.caimao.bana.api.entity.res.ybk.FYBKKDJRes
     */
    @ResponseBody
    @RequestMapping(value = "/query_kdj", method = RequestMethod.GET)
    public List<FYBKKDJRes> queryKDJ(
            @RequestParam(value = "exchange_short_name") String exchangeShortName,
            @RequestParam(value = "code") String code,
            @RequestParam(value = "limit", required = false, defaultValue = "300") Integer limit
    ) throws Exception {
        try {
            FYbkMarketReq req = new FYbkMarketReq();
            req.setExchangeShortName(exchangeShortName);
            req.setCode(code);
            req.setLimit(limit);
            return this.ybkService.queryKDJ(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>查询交易所指定藏品的RSI指标数据</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/query_rsi?exchange_short_name=nfwjs&code=101001</p>
     * <p>请求方式：GET</p>
     *
     * @param exchangeShortName 交易所简称
     * @param code              藏品代码
     * @param limit             查询多少条记录，默认300条
     * @return List<FYBKRSIRes>    RSI数据列表
     * @throws Exception
     * @see com.caimao.bana.api.entity.res.ybk.FYBKRSIRes
     */
    @ResponseBody
    @RequestMapping(value = "/query_rsi", method = RequestMethod.GET)
    public List<FYBKRSIRes> queryRSI(
            @RequestParam(value = "exchange_short_name") String exchangeShortName,
            @RequestParam(value = "code") String code,
            @RequestParam(value = "limit", required = false, defaultValue = "300") Integer limit
    ) throws Exception {
        try {
            FYbkMarketReq req = new FYbkMarketReq();
            req.setExchangeShortName(exchangeShortName);
            req.setCode(code);
            req.setLimit(limit);
            return this.ybkService.queryRSI(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>获取指定类型、交易所的文章列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/query_article_list?category_id=5&exchange_id=15</p>
     * <p>请求方式：GET</p>
     *
     * @param categoryId 公告文章类型  （1 打新申购 2 市场价格 3 停牌公告 4 网站公告 5 网站百科）
     * @param exchangeId 交易所唯一ID
     * @param limit      每页显示条数，默认10条
     * @param page       当前页数，默认1
     * @return FYBKQueryArticleSimpleListReq
     * @throws Exception
     * @see com.caimao.bana.api.entity.req.ybk.FYBKQueryArticleSimpleListReq
     */
    @ResponseBody
    @RequestMapping(value = "/query_article_list", method = RequestMethod.GET)
    public FYBKQueryArticleSimpleListReq queryArticleList(
            @RequestParam(value = "category_id") Integer categoryId,
            @RequestParam(value = "exchange_id", required = false) Integer exchangeId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) throws Exception {
        try {
            FYBKQueryArticleSimpleListReq req = new FYBKQueryArticleSimpleListReq();
            req.setCategoryId(categoryId);
            req.setExchangeId(exchangeId);
            req.setIsShow(0);
            req.setLimit(limit);
            req.setStart((page - 1) * limit);
            return this.ybkService.queryArticleSimpleList(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>根据文章公告ID，获取文章信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/get_article?a_id=16</p>
     * <p>请求方式：GET</p>
     *
     * @param aId 文章公告ID
     * @return YBKArticleEntity
     * @throws Exception
     * @see com.caimao.bana.api.entity.ybk.YBKArticleEntity
     */
    @ResponseBody
    @RequestMapping(value = "/get_article", method = RequestMethod.GET)
    public YBKArticleEntity getArticleInfo(@RequestParam(value = "a_id") Long aId) throws Exception {
        try {
            this.ybkService.readArticle(aId);
            return this.ybkService.queryArticleById(aId);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>根据藏品名称或藏品code 搜索藏品</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/searchGoods?goodCondition=xxx</p>
     * <p>请求方式：GET</p>
     *
     * @param goodCondition，藏品名称或藏品code
     * @return Map<String, Object> 藏品列表
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/searchGoods", method = RequestMethod.GET)
    public Map<String, Object> searchGoods(
            @RequestParam(value = "goodCondition", required = true) String goodCondition,
            @RequestParam(value = "timestamp", required = false) Long timestamp
    ) throws Exception {
        try {
            Map<String, Object> res = new HashMap<>();
            if (timestamp == null) timestamp = System.currentTimeMillis();
            res.put("goods", ybkService.searchGoods(goodCondition));
            res.put("timestamp", timestamp);
            return res;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>邮币卡账户注册</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/register</p>
     * <p>请求方式：POST</p>
     *
     * @param token        （token）登陆后返回的token
     * @param userName     （user_name）用户姓名
     * @param userIdcard   （user_idcard）用户身份证号码
     * @param bankCode     （bank_code）银行代码
     * @param bankNum      （bank_num）银行号码
     * @param bankPicture  （bank_picture）银行照片
     * @param cardPositive （card_positive）证件正面照片
     * @param cardOpposite （card_opposite）证件反面照片
     * @param exchangeIds  （exchange_id）申请的交易所ID,多个交易所id之间用,分隔
     * @return Boolean  申请成功返回 true
     * @throws Exception
     * @see com.caimao.bana.api.entity.ybk.YBKAccountEntity
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Boolean register(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "user_name", required = false) String userName,
            @RequestParam(value = "user_idcard", required = false) String userIdcard,
            @RequestParam(value = "bank_code") String bankCode,
            @RequestParam(value = "bank_num") String bankNum,
            @RequestParam(value = "bank_picture") String bankPicture,
            @RequestParam(value = "card_positive") String cardPositive,
            @RequestParam(value = "card_opposite") String cardOpposite,
            @RequestParam(value = "exchange_id") String exchangeIds
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);

            // 获取用户信息
            TpzUserEntity userEntity = this.userService.getById(userId);
            YBKAccountEntity entity = new YBKAccountEntity();
            entity.setUserId(userId);
            if (userName != null && userIdcard != null) {
                entity.setUserName(userName);
                entity.setCardNumber(userIdcard);
            } else {
                entity.setUserName(userEntity.getUserRealName());
                entity.setCardNumber(userEntity.getIdcard());
            }
            entity.setPhoneNo(userEntity.getMobile());
            entity.setCardType(1);
            entity.setCardPath(cardPositive);
            entity.setCardOppositePath(cardOpposite);
            entity.setBankCode(bankCode);
            entity.setBankNum(bankNum);
            entity.setBankPath(bankPicture);
//        entity.setProvince(province);
//        entity.setCity(city);
//        entity.setStreet(street);
//        entity.setContactMan(contactMan);
//        entity.setContacterPhoneNo(contacterPhoneNo);
//        entity.setSex(sex);
            for (String exchangeId : exchangeIds.split(",")) {
                if (!Objects.equals(exchangeId, "")) {
                    entity.setExchangeIdApply(Integer.parseInt(exchangeId));
                    ybkAccountService.insert(entity);
                }
            }
            // 更新证件路径
            userService.updateCardPath(userId, cardPositive, cardOpposite);
            return true;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>图片上传</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/uploadIMG</p>
     * <p>请求方式：POST</p>
     *
     * @param token 登陆后返回的token
     * @param file  上传的文件
     * @return String  文件保存路径
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/uploadIMG", method = RequestMethod.POST)
    public String uploadIMG(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "file") MultipartFile file
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            // 小于 5M
            String imgFilePath = ImageUtils.uploadImag(file, picturePath + "/" + userId.toString(), 5120L);

            return "/" + imgFilePath.substring(imgFilePath.indexOf("upload"));
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>iFrame图片上传</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/iFrameUploadIMG</p>
     * <p>请求方式：POST</p>
     *
     * @param token 登陆后返回的token
     * @param file  上传的文件
     * @return String  文件保存路径
     * @throws Exception
     */
    @RequestMapping(value = "/iFrameUploadIMG", method = RequestMethod.POST)
    public String iframeUploadIMG(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "file") MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        try {
            Long userId = this.getUserIdByToken(token);
            // 小于 5M
            String imgFilePath = ImageUtils.uploadImag(file, picturePath + "/" + userId.toString(), 5120L);

            String callBack = request.getParameter("callback");

            resMap.put("success", true);
            resMap.put("msg", "/" + imgFilePath.substring(imgFilePath.indexOf("upload")));

            String res = "<script>" + callBack + "(" + JSON.json(resMap) + ")</script>";

            response.getWriter().print(res);
            response.getWriter().flush();
        } catch (CustomerException e) {
            String callBack = request.getParameter("callback");
            resMap.put("success", false);
            resMap.put("msg", e.getMessage());
            String res = "<script>" + callBack + "(" + JSON.json(resMap) + ")</script>";
            response.getWriter().print(res);
            response.getWriter().flush();
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
        return null;
    }

    /**
     * <p>获取用户邮币卡申请的账户列表</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/apply_account_list?token=******</p>
     * <p>请求方式：POST</p>
     *
     * @param token      登陆后返回的token
     * @param exchangeId 申请的交易所ID，可选，不传获取所有
     * @param status     状态，可选，不传获取所有 （1 待审核  2 审核中  3 开通成功  4 不予通过）
     * @return FYbkApiQueryAccountListReq
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/apply_account_list", method = RequestMethod.GET)
    public FYbkApiQueryAccountListReq applyAccountList(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchange_id", required = false) Integer exchangeId,
            @RequestParam(value = "status", required = false) Integer status
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            FYbkApiQueryAccountListReq req = new FYbkApiQueryAccountListReq();
            req.setUserId(userId);
            req.setExchangeId(exchangeId);
            req.setStatus(status);

            return this.ybkAccountService.queryApiAccountApply(req);
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


    /**
     * <p>获取用户可直接开户的信息</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/may_open_exchange?token=******</p>
     * <p>请求方式：GET</p>
     *
     * @param token 登陆后返回的token
     * @return exchangeShortNo：交易所简称代码    exchangeName：交易所名称
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/may_open_exchange", method = RequestMethod.GET)
    public List<Map<String, Object>> mayOpenExchange(
            @RequestParam(value = "token") String token
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            List<YbkExchangeEntity> list = this.ybkAccountService.mayOpenExchange(userId);
            List<Map<String, Object>> res = new ArrayList<>();

            for (YbkExchangeEntity entity : list) {
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("exchangeShortNo", entity.getShortName());
                tmpMap.put("exchangeName", entity.getName());
                res.add(tmpMap);
            }
            return res;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }

    /**
     * <p>一步开户</p>
     * <p/>
     * <p>请求地址：http://172.32.1.218:8097/api/ybk/one_step_open_exchange?token=******</p>
     * <p>请求方式：POST</p>
     *
     * @param token           登陆后返回的token
     * @param exchangeShortNo 交易所简称
     * @return 成功，result返回true
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/one_step_open_exchange", method = RequestMethod.POST)
    public Map<String, Object> oneStepOpenExchange(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "exchangeShortNo") String exchangeShortNo
    ) throws Exception {
        try {
            Long userId = this.getUserIdByToken(token);
            Map<String, Object> res = new HashMap<>();
            this.ybkAccountService.oneStepOpenExchange(userId, exchangeShortNo);

            res.put("result", true);

            return res;
        } catch (CustomerException e) {
            throw new ApiException(e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new ApiException(EErrorCode.ERROR_CODE_999999.getMessage(), EErrorCode.ERROR_CODE_999999.getCode());
        }
    }


}
