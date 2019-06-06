package com.caimao.gjs.server.service;

import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.gjs.api.entity.GJSAccountEntity;
import com.caimao.gjs.api.entity.history.*;
import com.caimao.gjs.api.service.ITradeManageService;
import com.caimao.gjs.server.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 贵金属交易管理服务
 */
@Service("tradeManageService")
public class TradeManageServiceImpl implements ITradeManageService {
    private static final Logger logger = LoggerFactory.getLogger(TradeManageServiceImpl.class);

    @Resource
    private TradeServiceHelper tradeServiceHelper;
    @Resource
    private GJSNJSHistoryEntrustDao gjsNJSHistoryEntrustDao;
    @Resource
    private GJSNJSHistoryTradeDao gjsNJSHistoryTradeDao;
    @Resource
    private GJSNJSHistoryTransferDao gjsNJSHistoryTransferDao;
    @Resource
    private GJSSJSHistoryEntrustDao gjsSJSHistoryEntrustDao;
    @Resource
    private GJSSJSHistoryTradeDao gjsSJSHistoryTradeDao;
    @Resource
    private GJSSJSHistoryTransferDao gjsSJSHistoryTransferDao;
    @Resource
    private GJSAccountDao gjsAccountDao;
    @Resource
    private GJSNJSTraderIdDao gjsnjsTraderIdDao;

    /**
     * 根据TraderId查询NJS历史委托列表
     * @param traderId 交易员编号
     * @return List
     * @throws Exception
     */
    @Override
    public List<GjsNJSHistoryEntrustEntity> queryNJSHistoryEntrustListByTraderIdForManage(String traderId) throws Exception {
        return this.gjsNJSHistoryEntrustDao.queryNJSHistoryEntrustListByTraderIdForManage(traderId);
    }

    /**
     * 根据TraderId查询NJS历史交易列表
     * @param traderId 交易员编号
     * @return List
     * @throws Exception
     */
    @Override
    public List<GjsNJSHistoryTradeEntity> queryNJSHistoryTradeListByTraderIdForManage(String traderId) throws Exception {
        return this.gjsNJSHistoryTradeDao.queryNJSHistoryTradeListByTraderIdForManage(traderId);
    }

    /**
     * 根据TraderId查询NJS历史出入金列表
     * @param traderId 交易员编号
     * @return List
     * @throws Exception
     */
    @Override
    public List<GjsNJSHistoryTransferEntity> queryNJSHistoryTransferListByTraderIdForManage(String traderId) throws Exception {
        return this.gjsNJSHistoryTransferDao.queryNJSHistoryTransferListByTraderIdForManage(traderId);
    }

    /**
     * 根据TraderId查询SJS历史委托列表
     * @param traderId 交易员编号
     * @return List
     * @throws Exception
     */
    @Override
    public List<GjsSJSHistoryEntrustEntity> querySJSHistoryEntrustListByTraderIdForManage(String traderId) throws Exception {
        return this.gjsSJSHistoryEntrustDao.querySJSHistoryEntrustListByTraderIdForManage(traderId);
    }

    /**
     * 根据TraderId查询SJS历史交易列表
     * @param traderId 交易员编号
     * @return List
     * @throws Exception
     */
    @Override
    public List<GjsSJSHistoryTradeEntity> querySJSHistoryTradeListByTraderIdForManage(String traderId) throws Exception {
        return this.gjsSJSHistoryTradeDao.querySJSHistoryTradeListByTraderIdForManage(traderId);
    }

    /**
     * 根据TraderId查询SJS历史出入金列表
     *
     * @param traderId 交易员编号
     * @return List
     * @throws Exception
     */
    @Override
    public List<GjsSJSHistoryTransferEntity> querySJSHistoryTransferListByTraderIdForManage(String traderId) throws Exception {
        return this.gjsSJSHistoryTransferDao.querySJSHistoryTransferListByTraderIdForManage(traderId);
    }

    /**
     * 获取历史出入金金额
     * @param exchange 交易所编号
     * @param type 类型
     * @return Long
     * @throws Exception
     */
    @Override
    public Long getHistoryTransferTotalMoneySum(String exchange, String type) throws Exception {
        Long returnSum = 0L;
        if ("njs".equalsIgnoreCase(exchange)) {
            returnSum = this.gjsNJSHistoryTransferDao.getNJSHistoryTransferTotalMoneySum(type);
        }
        if ("sjs".equalsIgnoreCase(exchange)) {
            returnSum = this.gjsSJSHistoryTransferDao.getSJSHistoryTransferTotalMoneySum(type);
        }
        return returnSum;
    }

    /**
     * 获取历史出入金次数
     * @param exchange 交易所编号
     * @param type 类型
     * @return Long
     * @throws Exception
     */
    @Override
    public Long getHistoryTransferCount(String exchange, String type) throws Exception {
        Long returnSum = 0L;
        if ("njs".equalsIgnoreCase(exchange)) {
            returnSum = this.gjsNJSHistoryTransferDao.getNJSHistoryTransferCount(type);
        }
        if ("sjs".equalsIgnoreCase(exchange)) {
            returnSum = this.gjsSJSHistoryTransferDao.getSJSHistoryTransferCount(type);
        }
        return returnSum;
    }

    /**
     * 获取交易额
     * @param exchange 交易所编号
     * @return String
     * @throws Exception
     */
    @Override
    public String getHistoryTradeTotalMoney(String exchange) throws Exception {
        String returnSum = "";
        if ("njs".equalsIgnoreCase(exchange)) {
            returnSum = this.gjsNJSHistoryTradeDao.getHistoryTradeTotalMoney();
        }
        return returnSum;
    }

    /**
     * 获取交易次数
     * @param exchange 交易所编号
     * @return Long
     * @throws Exception
     */
    @Override
    public Long getHistoryTradeTotalCount(String exchange) throws Exception {
        Long returnSum = 0L;
        if ("njs".equalsIgnoreCase(exchange)) {
            returnSum = this.gjsNJSHistoryTradeDao.getHistoryTradeTotalCount();
        }
        return returnSum;
    }

    /**
     * 上金所09:00-11:30交易额
     * @throws Exception
     */
    @Override
    public String getHistoryTradeTotalMoney1() throws Exception {
        return this.gjsSJSHistoryTradeDao.getHistoryTradeTotalMoney1();
    }

    /**
     * 上金所09:00-11:30交易次数
     * @throws Exception
     */
    @Override
    public Long getHistoryTradeTotalCount1() throws Exception {
        return this.gjsSJSHistoryTradeDao.getHistoryTradeTotalCount1();
    }

    /**
     * 上金所13:30-15:30交易额
     * @throws Exception
     */
    @Override
    public String getHistoryTradeTotalMoney2() throws Exception {
        return this.gjsSJSHistoryTradeDao.getHistoryTradeTotalMoney2();
    }

    /**
     * 上金所13:30-15:30交易次数
     * @throws Exception
     */
    @Override
    public Long getHistoryTradeTotalCount2() throws Exception {
        return this.gjsSJSHistoryTradeDao.getHistoryTradeTotalCount2();
    }

    /**
     * 上金所20:00-02:30交易额
     * @throws Exception
     */
    @Override
    public String getHistoryTradeTotalMoney3() throws Exception {
        return this.gjsSJSHistoryTradeDao.getHistoryTradeTotalMoney3();
    }

    /**
     * 上金所20:00-02:30交易次数
     * @throws Exception
     */
    @Override
    public Long getHistoryTradeTotalCount3() throws Exception {
        return this.gjsSJSHistoryTradeDao.getHistoryTradeTotalCount3();
    }

    /**
     * 销户
     * @param userId 用户编号
     * @param exchange 交易所编号
     * @param traderId 交易员编号
     * @throws Exception
     */
    @Override
    public void removeAccount(Long userId, String exchange, String traderId) throws Exception {
        try{
            //查询用户开户信息
            GJSAccountEntity gjsAccountEntity = gjsAccountDao.queryAccountByUserId(userId, exchange);
            if(gjsAccountEntity == null) throw new CustomerException("用户未开户", 10215);
            if(traderId.equals("") || !traderId.equals(gjsAccountEntity.getTraderId())) throw new CustomerException("请输入正确的交易员编号", 10215);

            //清除数据
            gjsAccountDao.delAccountByUserId(userId, exchange);
            gjsnjsTraderIdDao.delTraderId(userId, traderId);

            //清除缓存
            tradeServiceHelper.delTraderId(exchange, userId);
            tradeServiceHelper.delUserId(exchange, traderId);
        }catch (CustomerException e){
            throw e;
        }catch(Exception e){
            logger.error("删除用户开户失败, 失败原因：", e);
            throw new Exception("删除用户开户失败");
        }
    }
}