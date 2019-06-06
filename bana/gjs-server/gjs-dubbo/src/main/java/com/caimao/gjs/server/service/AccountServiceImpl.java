package com.caimao.gjs.server.service;

import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.enums.EPushType;
import com.caimao.bana.api.enums.ESmsBizType;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.common.api.enums.EErrorCode;
import com.caimao.bana.common.api.enums.gjs.EGJSExchange;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.gjs.api.entity.GJSAccountEntity;
import com.caimao.gjs.api.entity.GJSNJSTraderIdEntity;
import com.caimao.gjs.api.entity.req.*;
import com.caimao.gjs.api.entity.res.FDoCreateTransferInNoRes;
import com.caimao.gjs.api.entity.res.FQueryTransferRes;
import com.caimao.gjs.api.enums.EGJSUploadStatus;
import com.caimao.gjs.api.enums.ENJSBankNo;
import com.caimao.gjs.api.enums.ENJSOPENBANK;
import com.caimao.gjs.api.enums.ESJSBankNo;
import com.caimao.gjs.api.service.IAccountService;
import com.caimao.gjs.server.dao.GJSAccountDao;
import com.caimao.gjs.server.dao.GJSNJSTraderIdDao;
import com.caimao.gjs.server.utils.CommonUtils;
import com.caimao.gjs.server.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 贵金属账户服务
 */
@Service("gjsAccountService")
public class AccountServiceImpl implements IAccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Resource
    private TradeServiceHelper tradeServiceHelper;
    @Resource
    private GJSAccountDao gjsAccountDao;
    @Resource
    private GJSNJSTraderIdDao gjsnjsTraderIdDao;
    @Resource
    private IUserService userService;

    /**
     * 修改资金密码(南交所已过、上金所已过)
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doModifyFundsPwd(FDoModifyFundsPwdReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //修改资金密码
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            //查询用户开户信息
            GJSAccountEntity gjsAccount = gjsAccountDao.queryAccountByUserId(req.getUserId(), req.getExchange());
            if(gjsAccount == null) throw new CustomerException(EErrorCode.ERROR_CODE_300003.getMessage(), EErrorCode.ERROR_CODE_300003.getCode());
            tradeServiceHelper.doNJSModifyFundsPwd(token, traderId, req.getOldPwd(), req.getNewPwd(), gjsAccount.getBankId());
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())){
            tradeServiceHelper.doSJSModifyFundsPwd(token, req.getOldPwd(), req.getNewPwd());
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 修改交易密码(南交所已过、上金所已过)
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doModifyTradePwd(FDoModifyTradePwdReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //修改登录密码
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            tradeServiceHelper.doNJSModifyTradePwd(token, traderId, req.getOldPwd(), req.getNewPwd());
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())) {
            tradeServiceHelper.doSJSModifyTradePwd(token, traderId, req.getOldPwd(), req.getNewPwd());
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 重置资金密码(南交所已过、上金所已过)
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doResetFundsPwd(FDoResetFundsPwdReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //验证手机验证码
        userService.checkSmsCode(req.getUserId(), ESmsBizType.GJSRESETFUNDS.getCode(), req.getSmsCode());
        //重置资金密码
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            //查询用户开户信息
            GJSAccountEntity gjsAccount = gjsAccountDao.queryAccountByUserId(req.getUserId(), req.getExchange());
            if(gjsAccount == null) throw new CustomerException(EErrorCode.ERROR_CODE_300003.getMessage(), EErrorCode.ERROR_CODE_300003.getCode());
            tradeServiceHelper.doNJSResetFundsPwd(traderId, req.getNewPwd(), gjsAccount.getFirmId());
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())) {
            tradeServiceHelper.doSJSResetFundsPwd(traderId, req.getNewPwd());
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 重置交易密码(南交所已过、上金所已过)
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doResetTradePwd(FDoResetTradePwdReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //验证手机验证码
        userService.checkSmsCode(req.getUserId(), ESmsBizType.GJSRESETTRADE.getCode(), req.getSmsCode());
        //重置资金密码
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            //查询用户开户信息
            GJSAccountEntity gjsAccount = gjsAccountDao.queryAccountByUserId(req.getUserId(), req.getExchange());
            if(gjsAccount == null) throw new CustomerException(EErrorCode.ERROR_CODE_300003.getMessage(), EErrorCode.ERROR_CODE_300003.getCode());
            tradeServiceHelper.doNJSResetTradePwd(traderId, req.getNewPwd(), gjsAccount.getFirmId());
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())) {
            tradeServiceHelper.doSJSResetTradePwd(traderId, req.getNewPwd());
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 入金(南交所已过、南交所已过)
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doTransferIn(FDoTransferInReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //入金
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            //查询用户开户信息
            GJSAccountEntity gjsAccount = gjsAccountDao.queryAccountByUserId(req.getUserId(), req.getExchange());
            if(gjsAccount == null) throw new CustomerException(EErrorCode.ERROR_CODE_300003.getMessage(), EErrorCode.ERROR_CODE_300003.getCode());
            //判断出入金
            tradeServiceHelper.queryTimeCanTransfer(req.getExchange(), gjsAccount.getBankId());
            tradeServiceHelper.doNJSTransferIn(token, traderId, req, gjsAccount);
            //增加提醒
            String msg = "（财猫贵金属）您已发起转账到市场" + req.getMoney() + "元(南交所)，若非本人操作，请致电4000898000。";
            tradeServiceHelper.sendMsg(req.getUserId(), EPushType.TRANSFERMONEY, msg);
            tradeServiceHelper.sendSms(req.getUserId(), msg);
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())){
            tradeServiceHelper.doSJSTransferIn(token, req);
            //增加提醒
            String msg = "（财猫贵金属）您已发起转账到市场" + req.getMoney() + "元(上金所)，若非本人操作，请致电4000898000。";
            tradeServiceHelper.sendMsg(req.getUserId(), EPushType.TRANSFERMONEY, msg);
            tradeServiceHelper.sendSms(req.getUserId(), msg);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 出金(南交所已过、南交所已过)
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doTransferOut(FDoTransferOutReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //出金
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            //查询用户开户信息
            GJSAccountEntity gjsAccount = gjsAccountDao.queryAccountByUserId(req.getUserId(), req.getExchange());
            if(gjsAccount == null) throw new CustomerException(EErrorCode.ERROR_CODE_300003.getMessage(), EErrorCode.ERROR_CODE_300003.getCode());
            //判断出入金
            tradeServiceHelper.queryTimeCanTransfer(req.getExchange(), gjsAccount.getBankId());
            tradeServiceHelper.doNJSTransferOut(token, traderId, req, gjsAccount);
            //增加提醒
            String msg = "（财猫贵金属）您已发起转账到银行卡" + req.getMoney() + "元(南交所)，若非本人操作，请致电4000898000。";
            tradeServiceHelper.sendMsg(req.getUserId(), EPushType.TRANSFERMONEY, msg);
            tradeServiceHelper.sendSms(req.getUserId(), msg);
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())){
            tradeServiceHelper.doSJSTransferOut(token, req);
            //增加提醒
            String msg = "（财猫贵金属）您已发起转账到银行卡" + req.getMoney() + "元(上金所)，若非本人操作，请致电4000898000。";
            tradeServiceHelper.sendMsg(req.getUserId(), EPushType.TRANSFERMONEY, msg);
            tradeServiceHelper.sendSms(req.getUserId(), msg);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询当日出入金记录(南交所未过、南交所已过)
     * @param req 请求对象
     * @return List
     * @throws Exception
     */
    @Override
    public List<FQueryTransferRes> queryTransfer(FQueryTradeCommonReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //今日出入金记录
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            return tradeServiceHelper.queryNJSTransfer(token, traderId);
        }else if(req.getExchange().equals(EGJSExchange.SJS.getCode())){
            return tradeServiceHelper.querySJSTransfer(token);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询用户开户信息(已过)
     * @param userId 用户编号
     * @return List
     * @throws Exception
     */
    @Override
    public Map<String, GJSAccountEntity> queryGJSAccount(Long userId) throws Exception {
        if(userId == null || userId == 0) throw new CustomerException(EErrorCode.ERROR_CODE_100002.getMessage(), EErrorCode.ERROR_CODE_100002.getCode());
        try{
            List<GJSAccountEntity> accountList = gjsAccountDao.queryAccountList(userId);
            Map<String, GJSAccountEntity> result = new HashMap<>();
            if(accountList != null){
                for (GJSAccountEntity entity:accountList){
                    entity.setIdCard(CommonUtils.hideString(entity.getIdCard(), 6, 3, true));
                    entity.setBankCard(CommonUtils.hideString(entity.getBankCard(), 0, 4, false));
                    result.put(entity.getExchange(), entity);
                }
            }
            return result;
        }catch(Exception e){
            throw new CustomerException(EErrorCode.ERROR_CODE_300008.getMessage(), EErrorCode.ERROR_CODE_300008.getCode());
        }
    }

    /**
     * 查询用户开户信息（该方法源自上面的queryGJSAccount方法，仅供后台显示用户详情用，因此不需要处理用户的身份证和银行卡号）
     *
     * @param userId
     * @return
     */
    @Override
    public List<GJSAccountEntity> queryGJSAccountByUserId(Long userId) {
        return gjsAccountDao.queryAccountList(userId);
    }

    /**
     * 绑定南交所交易员编号(已过)
     * @param userId 用户编号
     * @return list
     * @throws Exception
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public List<String> bindNJSTraderId(Long userId) throws Exception{
        List<String> userTraderId = new ArrayList<>();
        try{
            //查询用户是否存在
            TpzUserEntity user = userService.getById(userId);
            if(user == null) throw new CustomerException(EErrorCode.ERROR_CODE_100002.getMessage(), EErrorCode.ERROR_CODE_100002.getCode());
            //分配编号
            GJSNJSTraderIdEntity gjsnjsTraderIdEntity = gjsnjsTraderIdDao.queryByUserId(userId);
            if(gjsnjsTraderIdEntity == null){
                //获取一个可用的交易员编号锁定
                GJSNJSTraderIdEntity lockEntity = gjsnjsTraderIdDao.getCanUseForUpdate();
                //更新用户编号进入
                gjsnjsTraderIdDao.addUserId(lockEntity.getFirmId(), lockEntity.getTraderId(), userId);
                userTraderId.add(lockEntity.getFirmId());
                userTraderId.add(lockEntity.getTraderId());
                return userTraderId;
            }else{
                userTraderId.add(gjsnjsTraderIdEntity.getFirmId());
                userTraderId.add(gjsnjsTraderIdEntity.getTraderId());
                return userTraderId;
            }
        }catch(Exception e){
            logger.error("南交所分配交易员编号错误，错误信息{}", e);
            throw new CustomerException(EErrorCode.ERROR_CODE_300007.getMessage(), EErrorCode.ERROR_CODE_300007.getCode());
        }
    }

    /**
     * 南交所开户(已过)
     * @param req 请求对象
     * @return 交易员编号
     * @throws Exception
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public String doOpenAccountNJS(FDoOpenAccountNJSReq req) throws Exception {
        try{
            //查询用户是否存在
            TpzUserEntity user = userService.getById(req.getUserId());
            if(user == null) throw new CustomerException(EErrorCode.ERROR_CODE_100002.getMessage(), EErrorCode.ERROR_CODE_100002.getCode());
            //查询是否已经开户
            GJSAccountEntity gjsAccount = gjsAccountDao.queryAccountByUserId(req.getUserId(), EGJSExchange.NJS.getCode());
            if(gjsAccount != null) return gjsAccount.getTraderId();
            //查询银行卡相关信息
            ENJSBankNo bankNo = ENJSBankNo.findByCode(req.getBankNo());
            if(bankNo == null) throw new CustomerException(EErrorCode.ERROR_CODE_300005.getMessage(), EErrorCode.ERROR_CODE_300005.getCode());
            //验证是否是中信异度开户
            ENJSOPENBANK openBank = null;
            if(bankNo.getCode().equals("212")){
                if(req.getOpenBankCode() == null || req.getOpenBankCode().equals("")) throw new CustomerException(EErrorCode.ERROR_CODE_300019.getMessage(), EErrorCode.ERROR_CODE_300019.getCode());
                //检测开户银行字段
                openBank = ENJSOPENBANK.findByCode(req.getOpenBankCode());
                if(openBank == null) throw new CustomerException(EErrorCode.ERROR_CODE_300019.getMessage(), EErrorCode.ERROR_CODE_300019.getCode());
            }
            //获取用户交易员编号
            List<String> userTraderId = this.bindNJSTraderId(req.getUserId());
            if(userTraderId == null || userTraderId.size() != 2|| userTraderId.get(0).equals("") || userTraderId.get(1).equals("")) throw new CustomerException("南交所开户中获取用户交易员编号失败", 5111221);
            //开户
            String traderId = tradeServiceHelper.doOpenAccountNJS(req, user.getMobile(), bankNo, userTraderId, openBank);

            //保存数据
            GJSAccountEntity userAccount = new GJSAccountEntity();
            userAccount.setUserId(req.getUserId());
            userAccount.setExchange(EGJSExchange.NJS.getCode());
            userAccount.setSaltKey(MD5Util.md5(req.getUserId().toString() + String.valueOf(System.currentTimeMillis())).substring(0, 8));
            userAccount.setFirmId(userTraderId.get(0));
            userAccount.setTraderId(userTraderId.get(1));
            userAccount.setRealName(req.getRealName());
            userAccount.setIdCard(req.getIdCard());
            userAccount.setBankId(req.getBankNo());
            userAccount.setBankCard(req.getBankCard());
            userAccount.setCreateDatetime(new Date());
            userAccount.setIsSign(0);
            userAccount.setUploadStatus(EGJSUploadStatus.ACCESS_SUCCESS.getCode());
            userAccount.setCardPositive("");
            userAccount.setCardObverse("");
            userAccount.setIsSignAgreement(0);
            userAccount.setOpenBankName(openBank == null?"":openBank.getCode());
            gjsAccountDao.saveAccount(userAccount);
            //设置userId和traderId的关系
            tradeServiceHelper.setTraderId(EGJSExchange.NJS.getCode(), req.getUserId(), userTraderId.get(1));
            tradeServiceHelper.setUserId(EGJSExchange.NJS.getCode(), userTraderId.get(1), req.getUserId());

            //发送短信
            String smsContent = "（财猫贵金属）尊敬的财猫用户，您已在南交所开户成功，交易账号为" + traderId + "。1000元白银将于下一交易日派送到您的账户持仓中。[财猫-4000898000]";
            tradeServiceHelper.sendSms(user.getUserId(), smsContent);
            //发送消息
            String msgContent = "【开户提醒】恭喜您南交所账户开户成功。1000元白银正在派送中，预计下一交易日9:00到达您的持仓中。南交所热门交易品类为白银，快去体验吧!";
            tradeServiceHelper.sendMsg(user.getUserId(), EPushType.OPENACCOUNT, msgContent);
            return traderId;
        }catch (CustomerException e){
            throw e;
        }catch(Exception e){
            logger.error("南交所开户失败了，失败原因{}", e);
            throw new CustomerException(EErrorCode.ERROR_CODE_300006.getMessage(), EErrorCode.ERROR_CODE_300006.getCode());
        }
    }

    /**
     * 更新账户状态南交所
     * @throws Exception
     */
    @Override
    public void doUpdateAccountSignNJS() throws Exception {
        List<GJSAccountEntity> accountList = gjsAccountDao.queryNotSignList(EGJSExchange.NJS.getCode());
        for (GJSAccountEntity account:accountList){
            try{
                tradeServiceHelper.queryIsSignNJS(account);
                Thread.sleep(500);
            }catch(Exception e){
                logger.error(account.getUserId() + ":" + account.getTraderId() + "更新账户状态南交所，失败原因{}", e.getMessage());
            }
        }
    }

    /**
     * 通过推送信息更新用户开户信息
     * @param receiveData 接收信息
     * @throws Exception
     */
    @Override
    public void pushMsgIsSign(String exchange, String receiveData) throws Exception{
        try{
            //判断交易所是否存在
            tradeServiceHelper.getExchange(exchange);
            //推送成交信息
            if (EGJSExchange.NJS.getCode().equals(exchange)) {
                this.tradeServiceHelper.pushMsgIsSignNJS(receiveData);
            } else if (EGJSExchange.SJS.getCode().equals(exchange)) {
              //暂时没有
            } else {
                throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
            }
        }catch(Exception e){
            logger.error("推送消息失败", e);
        }
    }

    /**
     * 更新账户状态上金所
     * @throws Exception
     */
    @Override
    public void doUpdateAccountSignSJS() throws Exception {
        List<GJSAccountEntity> accountList = gjsAccountDao.queryNotSignList(EGJSExchange.SJS.getCode());
        for (GJSAccountEntity account:accountList){
            try{
                tradeServiceHelper.queryIsSignSJS(account);
            }catch(Exception e){
                logger.error(account.getUserId() + ":" + account.getTraderId() + "更新账户状态上金所，失败原因{}", e.getMessage());
            }
        }
    }

    /**
     * 上金所开户
     * @param req 请求对象
     * @return 交易员编号
     * @throws Exception
     */
    @Override
    public String doOpenAccountSJS(FDoOpenAccountSJSReq req) throws Exception {
        try{
            //查询用户是否存在
            TpzUserEntity user = userService.getById(req.getUserId());
            if(user == null) throw new CustomerException(EErrorCode.ERROR_CODE_100002.getMessage(), EErrorCode.ERROR_CODE_100002.getCode());
            //查询是否已经开户
            GJSAccountEntity gjsAccount = gjsAccountDao.queryAccountByUserId(req.getUserId(), EGJSExchange.SJS.getCode());
            if(gjsAccount != null) return gjsAccount.getTraderId();
            //查询银行卡相关信息
            ENJSBankNo bankNo = ENJSBankNo.findByCode(req.getBankNo());
            if(bankNo == null) throw new CustomerException(EErrorCode.ERROR_CODE_300005.getMessage(), EErrorCode.ERROR_CODE_300005.getCode());
            //开户
            String traderId = tradeServiceHelper.doOpenAccountSJS(req, user.getMobile());
            if(traderId != null && !traderId.equals("")){
                //保存数据
                GJSAccountEntity userAccount = new GJSAccountEntity();
                userAccount.setUserId(req.getUserId());
                userAccount.setExchange(EGJSExchange.SJS.getCode());
                userAccount.setSaltKey(MD5Util.md5(req.getUserId().toString() + String.valueOf(System.currentTimeMillis())).substring(0, 8));
                userAccount.setFirmId("");
                userAccount.setTraderId(traderId);
                userAccount.setRealName(req.getRealName());
                userAccount.setIdCard(req.getIdCard());
                userAccount.setBankId(req.getBankNo());
                userAccount.setBankCard(req.getBankCard());
                userAccount.setCreateDatetime(new Date());
                userAccount.setIsSign(0);
                userAccount.setUploadStatus(EGJSUploadStatus.UPLOAD_NO.getCode());
                userAccount.setCardPositive("");
                userAccount.setCardObverse("");
                userAccount.setIsSignAgreement(0);
                gjsAccountDao.saveAccount(userAccount);
                //设置userId和traderId的关系
                tradeServiceHelper.setTraderId(EGJSExchange.SJS.getCode(), req.getUserId(), traderId);
                tradeServiceHelper.setUserId(EGJSExchange.SJS.getCode(), traderId, req.getUserId());
            }
            //发送消息
            String msgContent = "【开户提醒】恭喜您在上金所成功开户。上金所热门品类为白银延期，mini黄金。";
            tradeServiceHelper.sendMsg(user.getUserId(), EPushType.OPENACCOUNT, msgContent);
            return traderId;
        }catch (CustomerException e){
            throw e;
        }catch(Exception e){
            logger.error("上金所开户失败了，失败原因{}", e);
            throw new CustomerException(EErrorCode.ERROR_CODE_300006.getMessage(), EErrorCode.ERROR_CODE_300006.getCode());
        }
    }

    /**
     * 照片上传
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doUploadCard(FDoUploadCardReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //查询是否已经开户
        GJSAccountEntity gjsAccount = gjsAccountDao.queryAccountByUserId(req.getUserId(), egjsExchange.getCode());
        if(gjsAccount == null) throw new CustomerException(EErrorCode.ERROR_CODE_300003.getMessage(), EErrorCode.ERROR_CODE_300003.getCode());
        //更新信息
        GJSAccountEntity gjsAccountEntity = new GJSAccountEntity();
        gjsAccountEntity.setUserId(req.getUserId());
        gjsAccountEntity.setExchange(egjsExchange.getCode());
        gjsAccountEntity.setCardPositive(req.getCardPositive());
        gjsAccountEntity.setCardObverse(req.getCardObverse());
        gjsAccountEntity.setUploadStatus(EGJSUploadStatus.UPLOAD_YES.getCode());
        gjsAccountDao.updateAccount(gjsAccountEntity);
    }

    /**
     * 查询支持的银行列表(已过)
     * @param exchange 交易所代码
     * @return List
     * @throws Exception
     */
    @Override
    public List<Map<String, String>> queryExchangeBankList(String exchange) throws Exception {
        List<Map<String, String>> bankList = new ArrayList<>();
        //输出信息
        if(exchange.equals(EGJSExchange.NJS.getCode())){
            for (ENJSBankNo sCode : ENJSBankNo.values()){
                Map<String, String> subMap = new TreeMap<>();
                subMap.put("bankNo", sCode.getCode());
                subMap.put("bankName", sCode.getName());
                subMap.put("needCardPwd", sCode.getIsCardPwd());
                bankList.add(subMap);
            }
            return bankList;
        }else if(exchange.equals(EGJSExchange.SJS.getCode())){
            for (ESJSBankNo sCode : ESJSBankNo.values()){
                Map<String, String> subMap = new TreeMap<>();
                subMap.put("bankNo", sCode.getCode());
                subMap.put("bankName", sCode.getName());
                subMap.put("needCardPwd", sCode.getIsCardPwd());
                bankList.add(subMap);
            }
            return bankList;
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询开户银行列表(已过)
     * @param exchange 交易所代码
     * @return List
     * @throws Exception
     */
    @Override
    public List<Map<String, String>> queryOpenBankList(String exchange) throws Exception {
        List<Map<String, String>> bankList = new ArrayList<>();
        //输出信息
        if(exchange.equals(EGJSExchange.NJS.getCode())){
            for (ENJSOPENBANK sCode : ENJSOPENBANK.values()){
                Map<String, String> subMap = new TreeMap<>();
                subMap.put("bankNo", sCode.getCode());
                subMap.put("bankName", sCode.getShortName());
                bankList.add(subMap);
            }
            return bankList;
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 变更银行卡号码
     * @param req 请求编号
     * @throws Exception
     */
    @Override
    public void doChangeBankCard(FDoChangeBankCardReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //查询银行卡相关信息
        ENJSBankNo bankNo = ENJSBankNo.findByCode(req.getBankId());
        if(bankNo == null) throw new CustomerException(EErrorCode.ERROR_CODE_300005.getMessage(), EErrorCode.ERROR_CODE_300005.getCode());
        //变更银行卡号码
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            //查询是否已经开户
            GJSAccountEntity gjsAccount = gjsAccountDao.queryAccountByUserId(req.getUserId(), EGJSExchange.NJS.getCode());
            if(gjsAccount == null) throw new CustomerException(EErrorCode.ERROR_CODE_300003.getMessage(), EErrorCode.ERROR_CODE_300003.getCode());
            if(gjsAccount.getIsSign() == 1) throw new CustomerException(EErrorCode.ERROR_CODE_300011.getMessage(), EErrorCode.ERROR_CODE_300011.getCode());
            tradeServiceHelper.doNJSChangeBankCard(token, traderId, bankNo.getCode(), req.getBankNo());
            //变更用户开户信息
            //更新信息
            GJSAccountEntity gjsAccountEntity = new GJSAccountEntity();
            gjsAccountEntity.setUserId(req.getUserId());
            gjsAccountEntity.setExchange(egjsExchange.getCode());
            gjsAccountEntity.setBankCard(req.getBankNo());
            gjsAccountDao.updateAccount(gjsAccountEntity);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 签订代交收协议
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doSignAgreement(FQueryTradeCommonReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //签订交收协议
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            tradeServiceHelper.doNJSSignAgreement(token, traderId);
            //更新信息
            GJSAccountEntity gjsAccountEntity = new GJSAccountEntity();
            gjsAccountEntity.setUserId(req.getUserId());
            gjsAccountEntity.setExchange(egjsExchange.getCode());
            gjsAccountEntity.setIsSignAgreement(1);
            gjsAccountDao.updateAccount(gjsAccountEntity);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 上金所绑定账号
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void doBindAccountSJS(FDoBindAccountSJSReq req) throws Exception{
        try{
            //查询是否已开户
            GJSAccountEntity gjsAccount = gjsAccountDao.queryAccountByUserId(req.getUserId(), EGJSExchange.SJS.getCode());
            if(gjsAccount == null){
                //登录交易所
                tradeServiceHelper.doSJSTradeLogin(req.getTraderId(), req.getTraderPwd());
                //获取个人信息
                GJSAccountEntity gjsAccountEntity = tradeServiceHelper.querySJSTrader(req.getUserId(), req.getTraderId());
                if(gjsAccountEntity != null){
                    //插入信息
                    gjsAccountDao.saveAccount(gjsAccountEntity);
                    //设置userId和traderId的关系
                    tradeServiceHelper.setTraderId(EGJSExchange.SJS.getCode(), req.getUserId(), req.getTraderId());
                    tradeServiceHelper.setUserId(EGJSExchange.SJS.getCode(), req.getTraderId(), req.getUserId());
                }else{
                    throw new CustomerException(EErrorCode.ERROR_CODE_300013.getMessage(), EErrorCode.ERROR_CODE_300013.getCode());
                }
            }
            this.tradeServiceHelper.doSJSTradeLogin(req.getTraderId(), req.getTraderPwd());
        }catch(CustomerException e){
            throw e;
        }catch(Exception e){
            logger.error("上金所绑定账号失败，失败原因{}", e);
            throw new CustomerException(EErrorCode.ERROR_CODE_300012.getMessage(), EErrorCode.ERROR_CODE_300012.getCode());
        }
    }


    @Override
    public int queryNJSOpenAccountCount(String startDate, String endDate) throws Exception {
        return this.gjsAccountDao.queryNJSOpenAccountCount(startDate, endDate);
    }

    @Override
    public int querySJSOpenAccountCount(String startDate, String endDate) throws Exception {
        return this.gjsAccountDao.querySJSOpenAccountCount(startDate, endDate);
    }

    /**
     * 银行签约
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public void doBankSign(FQueryTradeCommonReq req) throws Exception {
        //判断交易所是否存在
        EGJSExchange egjsExchange = tradeServiceHelper.getExchange(req.getExchange());
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(egjsExchange.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(egjsExchange.getCode(), traderId);
        //执行快速签约
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())) {
            //查询是否已经开户
            GJSAccountEntity gjsAccount = gjsAccountDao.queryAccountByUserId(req.getUserId(), EGJSExchange.NJS.getCode());
            if(gjsAccount == null) throw new CustomerException(EErrorCode.ERROR_CODE_300003.getMessage(), EErrorCode.ERROR_CODE_300003.getCode());
            if(gjsAccount.getIsSign() == 0){
                tradeServiceHelper.doNJSBankSign(token, traderId, gjsAccount);
            }
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 南交所中信银行入金
     * @param req 请求对象
     * @throws Exception
     */
    @Override
    public FDoCreateTransferInNoRes doCreateTransferInNo(FDoTransferInReq req) throws Exception {
        //获取在交易所中的用户编号
        String traderId = tradeServiceHelper.getTraderId(EGJSExchange.NJS.getCode(), req.getUserId());
        //获取token
        String token = tradeServiceHelper.getToken(EGJSExchange.NJS.getCode(), traderId);
        //入金
        if(req.getExchange().equals(EGJSExchange.NJS.getCode())){
            return tradeServiceHelper.doNJSCreateTransferInNo(token, traderId, req);
        }else{
            throw new CustomerException(EErrorCode.ERROR_CODE_300002.getMessage(), EErrorCode.ERROR_CODE_300002.getCode());
        }
    }

    /**
     * 查询南交所账号可用数量
     * @return 可用数量
     * @throws Exception
     */
    @Override
    public Integer queryCanUseNum() throws Exception {
        return gjsnjsTraderIdDao.queryCanUseNum();
    }
}