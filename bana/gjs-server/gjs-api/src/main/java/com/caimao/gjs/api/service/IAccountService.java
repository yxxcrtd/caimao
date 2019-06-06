package com.caimao.gjs.api.service;

import com.caimao.gjs.api.entity.GJSAccountEntity;
import com.caimao.gjs.api.entity.req.*;
import com.caimao.gjs.api.entity.res.FDoCreateTransferInNoRes;
import com.caimao.gjs.api.entity.res.FQueryTransferRes;

import java.util.List;
import java.util.Map;

/**
 * 贵金属账户服务
 */
public interface IAccountService {
    /**
     * 修改资金密码
     * @param req 请求对象
     * @throws Exception
     */
    public void doModifyFundsPwd(FDoModifyFundsPwdReq req) throws Exception;

    /**
     * 修改交易密码
     * @param req 请求对象
     * @throws Exception
     */
    public void doModifyTradePwd(FDoModifyTradePwdReq req) throws Exception;

    /**
     * 重置资金密码
     * @param req 请求对象
     * @throws Exception
     */
    public void doResetFundsPwd(FDoResetFundsPwdReq req) throws Exception;

    /**
     * 重置交易密码
     * @param req 请求对象
     * @throws Exception
     */
    public void doResetTradePwd(FDoResetTradePwdReq req) throws Exception;

    /**
     * 入金
     * @param req 请求对象
     * @throws Exception
     */
    public void doTransferIn(FDoTransferInReq req) throws Exception;

    /**
     * 出金
     * @param req 请求对象
     * @throws Exception
     */
    public void doTransferOut(FDoTransferOutReq req) throws Exception;

    /**
     * 查询今日出入金记录
     * @param req 请求对象
     * @return List
     * @throws Exception
     */
    public List<FQueryTransferRes> queryTransfer(FQueryTradeCommonReq req) throws Exception;

    /**
     * 查询用户开户信息
     * @param userId 用户编号
     * @return List
     * @throws Exception
     */
    public Map<String, GJSAccountEntity> queryGJSAccount(Long userId) throws Exception;

    /**
     * 查询用户开户信息（该方法源自上面的queryGJSAccount方法，仅供后台显示用户详情用，因此不需要处理用户的身份证和银行卡号）
     *
     * @param userId
     * @return
     */
    List<GJSAccountEntity> queryGJSAccountByUserId(Long userId);

    /**
     * 绑定南交所交易员编号
     * @param userId 用户编号
     * @return List
     * @throws Exception
     */
    public List<String> bindNJSTraderId(Long userId) throws Exception;

    /**
     * 南交所开户
     * @param req 请求对象
     * @return 交易员编号
     * @throws Exception
     */
    public String doOpenAccountNJS(FDoOpenAccountNJSReq req) throws Exception;

    /**
     * 更新是否已经绑定银行
     * @throws Exception
     */
    public void doUpdateAccountSignNJS() throws Exception;

    /**
     * 通过推送信息更新用户开户信息
     * @param receiveData 接收信息
     * @throws Exception
     */
    public void pushMsgIsSign(String exchange, String receiveData) throws Exception;

    /**
     * 更新是否已经绑定银行
     * @throws Exception
     */
    public void doUpdateAccountSignSJS() throws Exception;

    /**
     * 上金所开户
     * @param req 请求对象
     * @return 交易员编号
     * @throws Exception
     */
    public String doOpenAccountSJS(FDoOpenAccountSJSReq req) throws Exception;

    /**
     * 上传照片
     * @param req 请求对象
     * @throws Exception
     */
    public void doUploadCard(FDoUploadCardReq req) throws Exception;

    /**
     * 查询交易所银行列表
     * @param exchange 交易所代码
     * @return List
     * @throws Exception
     */
    public List<Map<String, String>> queryExchangeBankList(String exchange) throws Exception;

    /**
     * 查询开户银行列表
     * @param exchange 交易所代码
     * @return List
     * @throws Exception
     */
    public List<Map<String, String>> queryOpenBankList(String exchange) throws Exception;

    /**
     * 变更银行卡号码
     * @param req 请求编号
     * @throws Exception
     */
    public void doChangeBankCard(FDoChangeBankCardReq req) throws Exception;

    /**
     * 签订代交收协议
     * @param req 请求对象
     * @throws Exception
     */
    public void doSignAgreement(FQueryTradeCommonReq req) throws Exception;

    /**
     * 绑定上金所账号
     * @param req 请求对象
     * @throws Exception
     */
    public void doBindAccountSJS(FDoBindAccountSJSReq req) throws Exception;

    int queryNJSOpenAccountCount(String startDate, String endDate) throws Exception;

    int querySJSOpenAccountCount(String startDate, String endDate) throws Exception;

    /**
     * 银行签约
     * @param req 请求对象
     * @throws Exception
     */
    public void doBankSign(FQueryTradeCommonReq req) throws Exception;

    /**
     * 南交所中信银行入金
     * @param req 请求对象
     * @throws Exception
     */
    public FDoCreateTransferInNoRes doCreateTransferInNo(FDoTransferInReq req) throws Exception;

    /**
     * 查询南交所账号可用数量
     * @return 可用数量
     * @throws Exception
     */
    public Integer queryCanUseNum() throws Exception;
}