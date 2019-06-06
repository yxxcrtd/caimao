package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.HisLoanContractEntity;
import com.caimao.bana.api.entity.TpzLoanApplyEntity;
import com.caimao.bana.api.entity.req.FTpzQueryLoanContractAllPageReq;
import com.caimao.bana.api.entity.req.FTpzQueryLoanContractPageReq;
import com.caimao.bana.api.entity.req.loan.FHisContractReq;
import com.caimao.bana.api.entity.req.loan.FLoanApplyReq;
import com.caimao.bana.api.entity.req.loan.FLoanContractReq;
import com.caimao.bana.api.entity.req.loan.FLoanP2PApplyReq;
import com.caimao.bana.api.entity.res.F830216Res;
import com.caimao.bana.api.entity.res.loan.FHisContractRes;
import com.caimao.bana.api.entity.res.loan.FLoanApplyRes;
import com.caimao.bana.api.entity.res.loan.FLoanContractRes;
import com.caimao.bana.common.api.exception.CustomerException;

import java.util.List;

/**
 * 借款服务
 */
public interface ILoanService {
    /**
     * 获取所有没有合约编号的p2p借贷
     * @return
     * @throws Exception
     */
    public List<TpzLoanApplyEntity> getP2PContractNoNullList() throws Exception;

    /**
     * 查找并回写合约编号
     * @param tpzLoanApplyEntity
     * @throws Exception
     */
    public void doSearchP2PContractNo(TpzLoanApplyEntity tpzLoanApplyEntity) throws Exception;

    /**
     * 清除合约下一次结息日
     * @throws Exception
     */
    public void cleanNextSettleInterestDate() throws Exception;

    /**
     * 获取申请历史（普通配资申请与P2P配资申请合并）
     * @param req
     * @return
     * @throws Exception
     */
    public FLoanP2PApplyReq queryLoanP2PApplyList(FLoanP2PApplyReq req) throws Exception;

    public abstract void canLoanApply(Long userId, Long prodId) throws CustomerException;


    public abstract long getMaxLoanAmount(Long userId, Long produceId, Long depositAmount);

    public abstract FLoanApplyReq queryLoanContractApplyWithPage(Long userId, String status, String startDate, String endDate,
                                                                 int start, int limit);

    public abstract FLoanApplyReq queryContractDeferedList(Long userId, Long relContractNo, int start, int limit);

    public abstract FLoanApplyRes getLoanApply(Long loanApplyNo);

    public abstract FLoanContractReq queryLoanContractWithPage(Long userId, String productType, String startDate,
                                                               String endDate, int start, int limit);

    public abstract FLoanContractReq queryContractList(Long userId, String homsFundAccount, String homsCombineId);

    public abstract FLoanContractRes getContract(Long userId, Long contractNo);

    public abstract FHisContractRes getHisContract(Long userId, Long contractNo);

    public abstract FHisContractReq queryPaginableHisContract(Long userId, String startDate, String endDate, int start, int limit);

    public abstract long getAddAmountMax(Long userId, Long contractNo);

    public abstract F830216Res getTotalLoanAndBill(Long userId);

    /**
     * 查询合约列表(可选历史)
     * @param req
     * @param contractTable
     * @return
     * @throws Exception
     */
    public FTpzQueryLoanContractPageReq queryLoanContractPage(FTpzQueryLoanContractPageReq req, String contractTable) throws Exception;

    /**
     * 查询合约列表（当前+历史）
     * @param req
     * @return
     * @throws Exception
     */
    public FTpzQueryLoanContractAllPageReq queryLoanContractAllPage(FTpzQueryLoanContractAllPageReq req) throws Exception;
}
