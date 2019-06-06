package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.TpzAccountEntity;
import com.caimao.bana.api.entity.req.*;
import com.caimao.bana.api.entity.req.account.FAccountChangeReq;

import javax.validation.Valid;
import java.util.Map;

/**
 * 用户账户相关的接口服务
 * Created by WangXu on 2015/5/26.
 */
public interface IAccountService {

    /**
     * 查询用户资产列表
     * @param req
     * @return
     * @throws Exception
     */
    public FZeusUserBalanceReq queryUserBalanceList(FZeusUserBalanceReq req) throws Exception;

    /**
     * 冻结用户可用的人民币
     * @param req   请求对象
     */
    public Long frozenAvailableAmount(FAccountChangeReq req) throws Exception;

    /**
     * 用户预充值接口
     * @param accountChargeReq   充值请求对象
     * @return  表单值
     * @throws Exception
     */
    public Map<String, Object> doPreCharge(FAccountPreChargeReq accountChargeReq) throws Exception;

    /**
     * 申请提现接口
     * @param accountApplyWithdrawReq 提现请求对象
     * @return  提现ID
     * @throws Exception
     */
    public Long doApplyWithdraw(FAccountApplyWithdrawReq accountApplyWithdrawReq) throws Exception;

    /**
     * 查询用户的充值订单列表
     * @param accountQueryChargeOrderReq    查询充值订单列表对象
     * @return  充值订单列表
     * @throws Exception
     */
    public FAccountQueryChargeOrderReq queryChargeOrder(FAccountQueryChargeOrderReq accountQueryChargeOrderReq) throws Exception;

    /**
     * 查询用户的提现订单列表
     * @param accountQueryWithdrawOrderReq    查询提现订单列表
     * @return  提现订单列表
     * @throws Exception
     */
    public FAccountQueryWithdrawOrderReq queryWithdrawOrder(FAccountQueryWithdrawOrderReq accountQueryWithdrawOrderReq) throws Exception;

    /**
     * 获取用户资金流水
     * @param accountQueryAccountJourReq    查询资金流水的对象
     * @return  资金流水列表数据
     * @throws Exception
     */
    public FAccountQueryAccountJourReq queryAccountJour(FAccountQueryAccountJourReq accountQueryAccountJourReq) throws Exception;

    /**
     * 获取用户资金冻结流水
     * @param req    查询资金流水的对象
     * @return  资金流水列表数据
     * @throws Exception
     */
    public FAccountQueryAccountFrozenJourReq queryAccountFrozenJour(FAccountQueryAccountFrozenJourReq req) throws Exception;

    /**
     * 取消提现的功能操作
     * @param accountCancelWithdrawReq    取消提现的对象
     */
    public void doCancelWithdraw(FAccountCancelWithdrawReq accountCancelWithdrawReq) throws Exception;

    /**
     * 获取用户的资金账户
     * @param userId
     * @return
     * @throws Exception
     */
    public TpzAccountEntity getAccount(Long userId) throws Exception;

    /**
     * 锁定用户资产账户
     * @param pzAccountId
     * @throws Exception
     */
    public void lockingAccount(Long pzAccountId) throws Exception;
}
