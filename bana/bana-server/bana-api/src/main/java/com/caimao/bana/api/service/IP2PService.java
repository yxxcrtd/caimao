package com.caimao.bana.api.service;

import java.util.List;
import java.util.Map;

import com.caimao.bana.api.entity.p2p.P2PConfigEntity;
import com.caimao.bana.api.entity.p2p.P2PInvestRecordEntity;
import com.caimao.bana.api.entity.p2p.P2PLoanRecordEntity;
import com.caimao.bana.api.entity.req.*;
import com.caimao.bana.api.entity.res.FP2PParameterRes;
import com.caimao.bana.api.enums.EP2PLoanStatus;

/**
 * 融资借贷相关接口方法 Created by WangXu on 2015/6/8.
 */
public interface IP2PService {
    /**
     * 获得p2p总配置
     * 
     * @return
     */
    public List<FP2PParameterRes> getP2PParameter();
    /**
     * 获得p2p总配置
     * 
     * @return
     */
    public Map<String,Object> getP2PGlobalConfig();

    /**
     * 检查这个这个产品是否是P2P借贷的产品
     * @param productId 产品ID
     * @param lever 杠杆
     */
    public void checkIsP2PLoan(Long productId, Integer lever) throws Exception;

    /**
     * 添加自定义的P2P借贷记录
     * @param entity
     * @return
     * @throws Exception
     */
    public Long addCustomLoanRecord(P2PLoanRecordEntity entity) throws Exception;

    /**
     * 添加融资抵押
     * 
     * @param req 请求对象
     */
    public Long doAddLoan(FP2PAddLoanReq req) throws Exception;
    /**
     * 购买理财
     * 
     * @param req 请求对象
     */
    public Long doAddInvest(FP2PAddinvestReq req) throws Exception;

    /**
     * 查询产品ID的配置
     * 
     * @param prodId 产品id
     * @param prodId 产品id
     * @return
     */
    public P2PConfigEntity getProdSetting(Long prodId, Integer prodLever);

    /**
     * 查询产品ID的配置
     * 
     * @param prodId 产品id
     * @param prodId 产品id
     * @return
     */
    public List<P2PConfigEntity> getProdSettingByProdId(Long prodId);

    /**
     * 保存产品p2p配置
     * 
     * @param config
     */
    public void saveProdSetting(P2PConfigEntity config);

    /**
     * 更新产品p2p配置
     * 
     * @param config
     */
    public void updateProdSetting(P2PConfigEntity config);

    /**
     * 获取P2P投资汇总信息
     * 
     * @param userId 用户id
     * @return Map
     * @throws Exception
     */
    public Map<String, Object> queryUserSummaryInfo(Long userId) throws Exception;

    /**
     * 获取我的投资列表
     * 
     * @param req FP2PQueryPageInvestReq
     * @return FP2PQueryPageInvestReq
     * @throws Exception
     */
    public FP2PQueryUserPageInvestReq queryUserPageInvest(FP2PQueryUserPageInvestReq req) throws Exception;

    /**
     * 获取我的投资记录
     * 
     * @param userId 用户id
     * @param investId 投资id
     * @return P2PInvestRecordEntity
     * @throws Exception
     */
    public P2PInvestRecordEntity queryUserInvestRecord(Long userId, Long investId) throws Exception;

    /**
     * 获取我的利息列表
     * 
     * @param req FP2PQueryPageInterestReq
     * @return FP2PQueryPageInterestReq
     * @throws Exception
     */
    public FP2PQueryUserPageInterestReq queryUserPageInterest(FP2PQueryUserPageInterestReq req) throws Exception;

    /**
     * 获取我的借款列表
     * 
     * @param req FP2PQueryPageLoanReq
     * @return FP2PQueryPageLoanReq
     * @throws Exception
     */
    public FP2PQueryUserPageLoanReq queryUserPageLoan(FP2PQueryUserPageLoanReq req) throws Exception;

    /**
     * 获取我的借贷记录
     * 
     * @param userId 用户id
     * @param loanId 借贷id
     * @return P2PLoanRecordEntity
     * @throws Exception
     */
    public P2PLoanRecordEntity queryUserLoanRecord(Long userId, Long loanId) throws Exception;

    /**
     * 获取借贷列表
     * 
     * @param req FP2PQueryPageLoanReq
     * @return FP2PQueryPageLoanReq
     * @throws Exception
     */
    public FP2PQueryPageLoanReq queryPageLoan(FP2PQueryPageLoanReq req) throws Exception;

    /**
     * 获取借贷满标列表
     *
     * @param req FP2PQueryPageLoanReq
     * @return FP2PQueryPageLoanReq
     * @throws Exception
     */
    public FP2PQueryPageLoanReq queryPageFullLoan(FP2PQueryPageLoanReq req) throws Exception;

    /**
     * 获取借贷列表(关联查询用户)
     * 
     * @param req FP2PQueryPageLoanReq
     * @return FP2PQueryPageLoanReq
     * @throws Exception
     */
    public FP2PQueryPageLoanAndUserReq queryPageLoanWithUser(FP2PQueryPageLoanAndUserReq req) throws Exception;

    /**
     * 获取借贷记录
     * 
     * @param loanId 借贷ID
     * @return P2PLoanRecordEntity
     * @throws Exception
     */
    public P2PLoanRecordEntity queryLoanRecord(Long loanId) throws Exception;

    /**
     * 获取借贷记录的投标列表
     * 
     * @param req FP2PQueryLoanPageInvestReq
     * @return FP2PQueryLoanPageInvestReq
     * @throws Exception
     */
    public FP2PQueryLoanPageInvestReq queryLoanPageInvest(FP2PQueryLoanPageInvestReq req) throws Exception;

    /**
     * 获取借贷记录的投标列表(关联用户)
     * 
     * @param req FP2PQueryLoanPageInvestReq
     * @return FP2PQueryLoanPageInvestReq
     * @throws Exception
     */
    public FP2PQueryLoanPageInvestWithUserReq queryLoanPageInvestWithUser(FP2PQueryLoanPageInvestWithUserReq req)
            throws Exception;
    /**
     * 统计用户投资次数与总额
     * 
     * @param req FP2PQueryStatisticsByUserReq
     * @return FP2PQueryStatisticsByUserReq
     * @throws Exception
     */
    public FP2PQueryStatisticsByUserReq queryStatisticsByUserWithPage(FP2PQueryStatisticsByUserReq req) throws Exception;

    /**
     * 后台  查询投资人的投资列表
     * @param req 查询条件
     * @return  投资人投资列表
     * @throws Exception
     */
    public FP2PQueryPageInvestListReq queryPageInvestListWithUser(FP2PQueryPageInvestListReq req) throws Exception;

    /**
     * 流标
     * @param targetId 标的ID
     * @param targetUserId 用户id
     * @param p2pLoanStatus 标的状态
     * @throws Exception
     */
    public void doFailedTarget(Long targetId,Long targetUserId,EP2PLoanStatus p2pLoanStatus) throws Exception;

    /**
     * 查询借贷数量
     * @param targetStatus 状态
     * @return int
     * @throws Exception
     */
    public Integer queryLoanCount(Integer targetStatus) throws Exception;

    /**
     * 查询满标数量
     * @return
     * @throws Exception
     */
    public Integer queryLoanFullCount() throws Exception;

    /**
     * 自动补满P2P投资的剩余数量
     * @param targetId  投资标的的ID
     * @return  成功返回true
     * @throws Exception
     */
    public boolean doFullCaimaoP2PInvest(Long targetId) throws Exception;

    /**
     * 自动展期
     * @param targetId
     * @throws Exception
     */
    public void doAutoExtTarget(Long targetId) throws Exception;

    /**
     * 自动展期满标
     * @param targetId
     * @throws Exception
     */
    public void doAutoExtTargetFull(Long targetId) throws Exception;

    /**
     * 变更借贷记录是否还款
     * @param targetId
     * @throws Exception
     */
    public void updateIsRepayment(Long targetId) throws Exception;

    /**
     * 提前还款给投资人
     * @param p2pInvestRecord
     * @throws Exception
     */
    public void doPrepayment(P2PInvestRecordEntity p2pInvestRecord) throws Exception;
}
