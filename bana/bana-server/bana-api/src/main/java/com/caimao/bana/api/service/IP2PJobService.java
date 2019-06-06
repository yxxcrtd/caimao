package com.caimao.bana.api.service;


/**
 * 融资job相关接口方法 Created by yanjg on 2015/6/8.
 */
public interface IP2PJobService {
    /**
     * 扫描p2p满标，生成loan_apply记录的job
     */
    public void doCommitP2PLoanApply() throws Exception;

    /**
     * 发息脚本job
     */
    public void doDistributeInterest() throws Exception;

    /**
     * 流标脚本job
     */
    public void doFailedTarget() throws Exception;

    /**
     * 当P2P借贷满标并审核通过时，设置p2p_invest_record的计息时间job
     */
    public void doSetInterestTime() throws Exception;

    /**
     * 自动满标的操作
     * @throws Exception
     */
    public void autoFullTarget() throws Exception;

    /**
     * 自动展期
     */
    public void autoExtTarget() throws Exception;

    /**
     * 自动展期的标满
     */
    public void extTargetFull() throws Exception;

    /**
     * 查找合约编号
     */
    public void searchP2PContractNo() throws Exception;

    /**
     * 更新提前还款的标的
     */
    public void updateRepaymentTarget() throws Exception;

    /**
     * 去除所有p2p合约的下一次结息日
     */
    public void cleanP2PNextSettleInterestDate() throws Exception;
}
