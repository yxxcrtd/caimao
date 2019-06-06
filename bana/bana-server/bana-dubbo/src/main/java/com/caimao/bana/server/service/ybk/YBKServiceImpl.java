package com.caimao.bana.server.service.ybk;

import com.caimao.bana.api.entity.req.ybk.*;
import com.caimao.bana.api.entity.res.ybk.*;
import com.caimao.bana.api.entity.ybk.*;
import com.caimao.bana.api.service.ybk.IYBKService;
import com.caimao.bana.server.dao.ybk.*;
import com.caimao.bana.server.utils.DateUtil;
import com.caimao.bana.server.utils.YbkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 邮币卡服务
 */
@Service("YBKService")
public class YBKServiceImpl implements IYBKService {
    private static final Logger logger = LoggerFactory.getLogger(YBKServiceImpl.class);

    @Resource
    private YBKArticleDao ybkArticleDao;
    @Resource
    private YBKExchangeDao exchangeDao;
    @Resource
    private YBKTimeLineDao ybkTimeLineDao;
    @Resource
    private YBKKLineDao ybkkLineDao;
    @Resource
    private YBKMACDDao ybkmacdDao;
    @Resource
    private YBKKDJDao ybkkdjDao;
    @Resource
    private YBKRSIDao ybkrsiDao;
    @Resource
    private YBKGoodsDao ybkGoodsDao;
    @Resource
    private YBKSummaryDao ybkSummaryDao;
    @Resource
    private YBKActivityDao ybkActivityDao;

    @Override
    public void articleInsert(YBKArticleEntity ybkArticleEntity) throws Exception {
        try{
            ybkArticleDao.insert(ybkArticleEntity);
        }catch(Exception e){
            logger.error("邮币卡插入文章失败，失败原因{}", e);
            throw new Exception("插入失败");
        }
    }

    @Override
    public void articleDelete(Long id) throws Exception {
        try{
            ybkArticleDao.delete(id);
        }catch(Exception e){
            logger.error("邮币卡删除文章失败，失败原因{}", e);
            throw new Exception("删除失败");
        }
    }

    @Override
    public void articleUpdate(YBKArticleEntity ybkArticleEntity) throws Exception {
        try{
            ybkArticleDao.update(ybkArticleEntity);
        }catch(Exception e){
            logger.error("邮币卡更新文章失败，失败原因{}", e);
            throw new Exception("更新失败");
        }
    }

    @Override
    public YBKArticleEntity queryArticleById(Long id) throws Exception {
        return ybkArticleDao.queryById(id);
    }

    /**
     * 阅读文章，文章阅读数加一
     * @param id    文章ID
     * @throws Exception
     */
    @Override
    public void readArticle(Long id) throws Exception {
        this.ybkArticleDao.readArticle(id);
    }

    /**
     * 查询列表分页
     * @param req req类
     * @return FYBKQueryArticleListReq
     * @throws Exception
     */
    @Override
    public FYBKQueryArticleListReq queryArticleWithPage(FYBKQueryArticleListReq req) throws Exception {
        req.setItems(ybkArticleDao.queryListWithPage(req));
        return req;
    }

    /**
     * 前台API用到的，返回简单的文章列表页面
     * @param req   请求对象
     * @return
     * @throws Exception
     */
    @Override
    public FYBKQueryArticleSimpleListReq queryArticleSimpleList(FYBKQueryArticleSimpleListReq req) throws Exception {
        req.setItems(ybkArticleDao.querySimpleListWithPage(req));
        return req;
    }

    /**
     * 获得上一篇或下一篇的id
     * @param req 请求对象
     * @return YBKArticleEntity 里面只有id和标题
     * @throws Exception
     */
    @Override
    public YBKArticleEntity queryArticleId(FYBKQueryArticleIdReq req) throws Exception {
        return ybkArticleDao.queryId(req);
    }

    /**
     * 查询交易所的列表
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public List<YbkExchangeEntity> queryExchangeList(FYbkExchangeQueryListReq req) throws Exception {
        List<YbkExchangeEntity> list = this.exchangeDao.queryList(req);
        if (list != null) for (YbkExchangeEntity entity: list) {
            entity.setOpenOrClose(YbkUtil.checkExchangeOpen(entity));
            // 获取交易所的活动
            FYBKQueryActivityListReq activityListReq = new FYBKQueryActivityListReq();
            activityListReq.setLimit(100);
            activityListReq.setExchangeId(entity.getId());
            activityListReq.setIsShow(0);
            entity.setActivityList(this.queryActivityWithPage(activityListReq).getItems());
        }
        return list;
    }

    /**
     * 添加邮币卡交易所信息
     * @param ybkExchange
     * @throws Exception
     */
    @Override
    public void addExchange(YbkExchangeEntity ybkExchange) throws Exception {
        ybkExchange.setAddDatetime(new Date());
        this.exchangeDao.insertSelective(ybkExchange);
    }

    /**
     * 更新邮币卡交易所信息
     * @param ybkExchange
     * @throws Exception
     */
    @Override
    public void updateExchange(YbkExchangeEntity ybkExchange) throws Exception {
        this.exchangeDao.updateByPrimaryKeySelective(ybkExchange);
    }

    /**
     * 删除邮币卡交易所信息
     * @param id
     * @throws Exception
     */
    @Override
    public void deleteExchange(Integer id) throws Exception {
        this.exchangeDao.deleteByPrimaryKey(id);
    }

    /**
     * 获取指定的交易所信息，根据ID来获取
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public YbkExchangeEntity getExchangeById(Integer id) throws Exception {
        YbkExchangeEntity entity = this.exchangeDao.selectByPrimaryKey(id);
        if (entity != null) entity.setOpenOrClose(YbkUtil.checkExchangeOpen(entity));
        return entity;
    }

    /**
     * 获取指定的交易所信息，根据简称来获取
     * @param shortName
     * @return
     * @throws Exception
     */
    @Override
    public YbkExchangeEntity getExchangeByShortName(String shortName) throws Exception {
        YbkExchangeEntity entity = this.exchangeDao.selectByShortName(shortName);
        if (entity != null) entity.setOpenOrClose(YbkUtil.checkExchangeOpen(entity));
        return entity;
    }

    /**
     * 根据银行卡代号，获取所支持的交易所列表
     * @param bankNo
     * @return
     * @throws Exception
     */
    @Override
    public List<YbkExchangeEntity> queryExchangeByBankNo(String bankNo) throws Exception {
        List<YbkExchangeEntity> list = this.exchangeDao.queryListByBankNo(bankNo);
        if (list != null) for (YbkExchangeEntity entity: list) {
            entity.setOpenOrClose(YbkUtil.checkExchangeOpen(entity));
        }
        return list;
    }

    /**
     * 查询综合指数
     * @return 查询综合指数
     * @throws Exception
     */
    @Override
    public List<FYBKCompositeIndexRes> queryCompositeIndex() throws Exception {
        List<FYBKCompositeIndexRes> result = new ArrayList<>();
        try{
            List<HashMap<String, Object>> dataList = ybkSummaryDao.queryCompositeIndex();
            if(dataList != null){
                for (HashMap<String, Object> data:dataList){
                    try{
                        FYBKCompositeIndexRes fybkCompositeIndexRes = new FYBKCompositeIndexRes();
                        fybkCompositeIndexRes.setExchangeId(Integer.parseInt(data.get("exchangeId").toString()));
                        fybkCompositeIndexRes.setExchangeName(data.get("exchangeName").toString());
                        fybkCompositeIndexRes.setShortName(data.get("shortName").toString());
                        fybkCompositeIndexRes.setYesterBalancePrice(Long.parseLong(data.get("yesterBalancePrice").toString()));
                        fybkCompositeIndexRes.setCurrentPrice(Long.parseLong(data.get("curPrice").toString()));
                        fybkCompositeIndexRes.setChangeRate(Long.parseLong(data.get("currentGains").toString()));
                        fybkCompositeIndexRes.setTotalAmount(Long.parseLong(data.get("totalAmount").toString()));
                        fybkCompositeIndexRes.setTotalMoney(Long.parseLong(data.get("totalMoney").toString()));
                        fybkCompositeIndexRes.setHighPrice(Long.parseLong(data.get("highPrice").toString()));
                        fybkCompositeIndexRes.setLowPrice(Long.parseLong(data.get("lowPrice").toString()));
                        fybkCompositeIndexRes.setShenGouNum(Integer.valueOf(data.get("shenGouNum").toString()));

                        YbkExchangeEntity ybkExchangeEntity = new YbkExchangeEntity();
                        ybkExchangeEntity.setTradeDayType(Integer.valueOf(data.get("tradeDayType").toString()));
                        ybkExchangeEntity.setAmBeginTime(data.get("amBeginTime").toString());
                        ybkExchangeEntity.setAmEndTime(data.get("amEndTime").toString());
                        ybkExchangeEntity.setPmBeginTime(data.get("pmBeginTime").toString());
                        ybkExchangeEntity.setPmEndTime(data.get("pmEndTime").toString());
                        Boolean isOpen = YbkUtil.checkExchangeOpen(ybkExchangeEntity);
                        fybkCompositeIndexRes.setIsOpen(isOpen ? 1 : 0);
                        // 交易所活动
                        FYBKQueryActivityListReq activityListReq = new FYBKQueryActivityListReq();
                        activityListReq.setLimit(100);
                        activityListReq.setExchangeId(Integer.parseInt(data.get("exchangeId").toString()));
                        activityListReq.setIsShow(0);
                        fybkCompositeIndexRes.setActivityList(this.queryActivityWithPage(activityListReq).getItems());

                        result.add(fybkCompositeIndexRes);
                    }catch(Exception e){
                        logger.error("查询综合指数失败组装数据失败，失败原因{}", e);
                    }
                }
            }
            return result;
        }catch(Exception e){
            logger.error("查询综合指数失败，失败原因{}", e);
            throw new Exception("查询综合指数失败");
        }
    }

    /**
     * 查询指定交易所下的藏品商品行情信息列表
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FYbkQueryCollectionRankingReq queryCollectionRanking(FYbkQueryCollectionRankingReq req) throws Exception {
        try{
            if (req.getOrderColumn() == null) {
                req.setOrderColumn("changeRate");
                req.setOrderDir("DESC");
            }
            if (!Objects.equals(req.getOrderDir(), "DESC") && !Objects.equals(req.getOrderDir(), "ASC")) {
                req.setOrderDir("DESC");
            }

            List<FYBKCollectionRankingRes> resultData = new ArrayList<>();
            //查询涨幅数据
            List<FYBKCollectionRankingRes> data = ybkkLineDao.queryCollectionRankingWithPage(req);
            //查询所有藏品名称
            List<YBKGoodsEntity> goodsData = ybkGoodsDao.queryAllGoods(req.getExchangeShortName());
            Map<String, String> goodsList = new HashMap<>();
            if(goodsData != null){
                for(YBKGoodsEntity ybkGoodsEntity:goodsData){
                    goodsList.put(ybkGoodsEntity.getGoodCode(), ybkGoodsEntity.getGoodName());
                }
            }
            //组装数据
            for (FYBKCollectionRankingRes fybkCollectionRankingRes:data){
                if(goodsList.containsKey(fybkCollectionRankingRes.getCode())){
                    String goodName = goodsList.get(fybkCollectionRankingRes.getCode());
//                    if(!goodName.contains("指数") && resultData.size() < 10){
//                    if(resultData.size() < 10){
                        fybkCollectionRankingRes.setName(goodName);
                        resultData.add(fybkCollectionRankingRes);
//                    }
                }
            }
            req.setItems(resultData);
            return req;
        }catch(Exception e){
            logger.error("获取涨幅前十失败，失败原因{}", e);
            throw new Exception("获取涨幅前十失败");
        }
    }

    /**
     * 查询指定交易所下的指定商品行情信息
     * @param exchangeShortName 交易所简称
     * @param code  藏品代码
     * @return
     * @throws Exception
     */
    @Override
    public FYBKCollectionRankingRes getCollectionInfo(String exchangeShortName, String code) throws Exception {
        return this.ybkkLineDao.getCollectionInfo(exchangeShortName, code);
    }

    /**
     * 查询分时
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public List<FYBKTimeLineRes> queryTimeLine(FYbkMarketReq req) throws Exception {
        try{
            YbkExchangeEntity exchangeEntity = this.exchangeDao.selectByShortName(req.getExchangeShortName());
            YBKGoodsEntity goodsEntity = this.ybkGoodsDao.queryGood(exchangeEntity.getId(), req.getCode());
            // 先获取分时数据最后一笔的信息
            YBKTimeLineEntity timeLineEntity = this.ybkTimeLineDao.getLastLine(req.getExchangeShortName(), req.getCode());
            if (timeLineEntity != null) {
                // 设置获取最后有值的一天的数据
                req.setDatetime(
                        DateUtil.convertStringToDate(
                                DateUtil.DATE_FORMAT_STRING, DateUtil.convertDateToString(DateUtil.DATE_FORMAT_STRING, timeLineEntity.getDatetime())
                        ));
            }
            List<FYBKTimeLineRes> resList = ybkTimeLineDao.queryTimeLine(req);
            String type = goodsEntity.getGoodName().contains("指数") ? "0" : "1";
            for (FYBKTimeLineRes res : resList) {
                res.setType(type);
            }
            return resList;
        }catch(Exception e){
            logger.error("查询分时失败，失败原因{}", e);
            throw new Exception("查询分时失败");
        }
    }

    /**
     * 查询日K
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public List<FYBKKLineRes> queryKLine(FYbkMarketReq req) throws Exception {
        try{
            return ybkkLineDao.queryKLine(req);
        }catch(Exception e){
            logger.error("查询日K失败，失败原因{}", e);
            throw new Exception("查询日K失败");
        }
    }

    /**
     * 查询MACD
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public List<FYBKMACDRes> queryMACD(FYbkMarketReq req) throws Exception {
        try{
            return ybkmacdDao.queryMACD(req);
        }catch(Exception e){
            logger.error("查询MACD失败，失败原因{}", e);
            throw new Exception("查询MACD失败");
        }
    }

    /**
     * 查询KDJ
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public List<FYBKKDJRes> queryKDJ(FYbkMarketReq req) throws Exception {
        try{
            return ybkkdjDao.queryKDJ(req);
        }catch(Exception e){
            logger.error("查询KDJ失败，失败原因{}", e);
            throw new Exception("查询KDJ失败");
        }
    }

    /**
     * 查询RSI
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public List<FYBKRSIRes> queryRSI(FYbkMarketReq req) throws Exception {
        try{
            return ybkrsiDao.queryRSI(req);
        }catch(Exception e){
            logger.error("查询RSI失败，失败原因{}", e);
            throw new Exception("查询RSI失败");
        }
    }

    /**
     * 搜索藏品
     * @param condition
     * @return
     * @throws Exception
     */
    @Override
    public List<YBKGoodsEntity> searchGoods(String condition) throws Exception {
        return ybkGoodsDao.searchGoods(condition);
    }

    /**
     * 查询活动分页
     * @param req 请求对象
     * @return FYBKQueryActivityListReq
     * @throws Exception
     */
    @Override
    public FYBKQueryActivityListReq queryActivityWithPage(FYBKQueryActivityListReq req) throws Exception {
        List<YBKActivityEntity> data = ybkActivityDao.queryActivityWithPage(req);
        req.setItems(data);
        return req;
    }

    @Override
    public void activityInsert(YBKActivityEntity entity) throws Exception {
        ybkActivityDao.insert(entity);
    }

    @Override
    public void activityUpdate(YBKActivityEntity entity) throws Exception {
        ybkActivityDao.update(entity);
    }

    @Override
    public void activityDel(Long id) throws Exception {
        ybkActivityDao.delete(id);
    }

    @Override
    public YBKActivityEntity queryActivityById(Long id) throws Exception {
        return ybkActivityDao.queryById(id);
    }
}