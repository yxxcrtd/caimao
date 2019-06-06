package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.req.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 后台数据统计查询
 */
public interface IZeusStatisticsService {
    /**
     * 获取用户充值总额
     * @param userId
     * @return
     * @throws Exception
     */
    public Long queryUserDepositTotal(Long userId) throws Exception;

    /**
     * 获取用户提现总额
     * @param userId
     * @return
     * @throws Exception
     */
    public Long queryUserWithdrawTotal(Long userId) throws Exception;

    /**
     * 获取用户最后一笔成功的提现记录
     * @param userId
     * @return
     * @throws Exception
     */
    public Date queryLastWithdrawSuccess(Long userId) throws Exception;

    /**
     * 获取用户坏的财务流水
     * @param userId
     * @param dateStart
     * @param dateEnd
     * @return
     * @throws Exception
     */
    public Long queryIsHasBadJour(Long userId, Date dateStart, Date dateEnd) throws Exception;

    /**
     * 获取HOMS财务流水列表
     * @param req
     * @return
     * @throws Exception
     */
    public FTPZQueryHomsAccountJourReq queryHomsAccountJourListWithPage(FTPZQueryHomsAccountJourReq req) throws Exception;

    /**
     * 获取用户财务流水列表
     * @param req
     * @return
     * @throws Exception
     */
    public FTPZQueryUserAccountJourReq queryUserAccountJourListWithPage(FTPZQueryUserAccountJourReq req) throws Exception;

    /**
     * 保存homs系统流水
     * @param record
     * @return
     * @throws Exception
     */
    public Integer saveHomsFinanceHistory(HashMap<String, Object> record) throws Exception;

    /**
     * 批量保存homs系统流水
     * @param recordList
     * @return
     * @throws Exception
     */
    public void saveHomsFinanceHistoryBatch(List<HashMap<String, Object>> recordList) throws Exception;


    /**
     * 查询homs系统流水
     * @param req
     * @return
     * @throws Exception
     */
    public FZeusHomsJourReq queryHomsJourListWithPage(FZeusHomsJourReq req) throws Exception;

    /**
     * 查询用户资产汇总日报
     * @param req
     * @return
     * @throws Exception
     */
    public FZeusUserBalanceDailyReq queryUserBalanceDailyList(FZeusUserBalanceDailyReq req) throws Exception;

    /**
     * 用户资产汇总日报保存
     * @throws Exception
     */
    public void userBalanceDailySave() throws Exception;

    /**
     * 用户现金流量
     * @param req
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> queryUserFinanceStream(FTPZQueryAccountJourStreamReq req) throws Exception;

    /**
     * 更新homs账户借款log
     * @throws Exception
     */
    public void updateHomsAccountLoanLog() throws Exception;

    /**
     * 获取用户homs流水统计
     * @param userId
     * @return
     * @throws Exception
     */
    public List<HashMap<String, Object>> queryUserHomsStatistics(Long userId) throws Exception;

    /**
     * 变更用户资产流水的业务类型
     * @param id
     * @param bizType
     * @throws Exception
     */
    public void changeUserAccountJourBizType(Long id, String bizType) throws Exception;

    /**
     * 查询每日新增数据
     * @param dateStart
     * @param dateEnd
     * @param selectId
     * @param customId
     * @return
     * @throws Exception
     */
    public List<HashMap> queryDailyNewUser(String dateStart, String dateEnd, Integer selectId, String customId) throws Exception;

    /**
     * 查询指定邀请的实名认证列表
     * @param refUserId
     * @param dateStart
     * @param dateEnd
     * @return
     * @throws Exception
     */
    public List<TpzUserEntity> queryDateRealUserList(String refUserId, String dateStart, String dateEnd) throws Exception;

    /**
     * 清除合约
     * @param contractNo
     * @throws Exception
     */
    public void removeContract(Long contractNo) throws Exception;
}
