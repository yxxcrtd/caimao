package com.caimao.bana.server.service.user;

import com.caimao.bana.api.entity.*;
import com.caimao.bana.api.entity.req.*;
import com.caimao.bana.api.entity.req.user.FUserProhiWithdrawReq;
import com.caimao.bana.api.entity.req.user.FUserQueryProhiWithdrawLogReq;
import com.caimao.bana.api.entity.res.FUserListRes;
import com.caimao.bana.api.entity.res.user.FUserQueryProhiWithdrawLogRes;
import com.caimao.bana.api.entity.zeus.ZeusUserProhiWithdrawLogEntity;
import com.caimao.bana.api.enums.AddScoreType;
import com.caimao.bana.api.enums.ESmsBizType;
import com.caimao.bana.api.enums.EUserInit;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.service.InviteReturnHistoryService;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.dao.inviteInfo.InviteInfoDao;
import com.caimao.bana.server.dao.userDao.*;
import com.caimao.bana.server.service.helper.SmsManager;
import com.caimao.bana.server.utils.MD5Util;
import com.caimao.bana.server.utils.UserManager;
import com.caimao.gjs.api.enums.ENJSBankNo;
import com.caimao.gjs.api.enums.ESJSBankNo;
import com.hsnet.pz.core.util.PwdUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("userService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private TpzUserDao userDao;
    @Autowired
    private TpzUserExtDao userExtDao;
    @Autowired
    private TpzUserTradeDao userTradeDao;
    @Autowired
    private TpzUserPwdAnswerDao userPwdAnswerDao;
    @Autowired
    private TpzUserPwdQuestionDao userPwdQuestionDao;
    @Autowired
    private TpzUserLoginLogDao userLoginLogDao;
    @Autowired
    private ZeusUserProhiWithdrawLogDao userProhiWithdrawLogDao;
    @Autowired
    private SmsManager smsManager;
    @Autowired
    private UserManager userManager;

    @Resource
    private InviteInfoDao inviteInfoDao;

    @Resource
    private InviteReturnHistoryService inviteReturnHistoryService;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    // 火币登陆服务
    @Autowired
    private HuobiUserLoginHelper huobiUserLoginHelper;


    @Override
    public TpzUserTradeEntity getUserTrade(Long userId) {
        return this.userTradeDao.getById(userId);
    }

    /**
     * 根据手机号，查找用户的user_id
     *
     * @param phone 手机号
     * @return
     * @throws Exception
     */
    @Override
    public Long getUserIdByPhone(String phone) throws Exception {
        if (StringUtils.isBlank(phone)) {
            throw new CustomerException("手机号不能为空", 830007, "BizServiceException");
        }
        TpzUserEntity user = this.userDao.queryUserByPhone(phone);
        if (user == null) {
            return null;
        }
        return user.getUserId();
    }

    // 获取用户信息，根据用户ID
    @Override
    public TpzUserEntity getById(Long userId) throws Exception {
        return this.userDao.getById(userId);
    }

    /**
     * 根据手机号，获取火币的单一用户的uid
     * @param phone
     * @return
     * @throws Exception
     */
    @Override
    public Long getHuobiIdByPhone(String phone) throws Exception {
        return this.huobiUserLoginHelper.getUidByPhone(phone, "127.0.0.1");
    }

    // 获取用户信息，根据财猫ID
    @Override
    public TpzUserEntity getByCaimaoId(Long caimaoId) throws Exception {
        return this.userDao.getUserByCaimaoId(caimaoId);
    }

    @Override
    public boolean doCheckSmsCode(String mobile, String smsCheckCode, ESmsBizType eSmsBizType) throws Exception {
        // 不需要查询这个手机号是否存在
        //this.userManager.isMobileExist(mobile);
        this.smsManager.checkSmsCode(mobile, eSmsBizType.getCode(), smsCheckCode);
        return true;
    }

    @Override
    public TpzUserExtEntity getUserExtById(Long userId) throws Exception {
        return this.userExtDao.getById(userId);
    }

    @Override
    public void updateUserExt(TpzUserExtEntity userExtEntity) throws Exception {
        this.userExtDao.update(userExtEntity);
    }

    // 设置用户的提现状态
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void setUserWithdrawStatus(FUserProhiWithdrawReq req) throws Exception {
        // 设置用户的禁止提现状态
        TpzUserExtEntity userExtEntity = new TpzUserExtEntity();
        userExtEntity.setUserId(req.getUserId());
        userExtEntity.setProhiWithdraw(Integer.valueOf(req.getStatus()));
        this.userExtDao.update(userExtEntity);
        // 写入历史
        ZeusUserProhiWithdrawLogEntity userProhiWithdrawLogEntity = new ZeusUserProhiWithdrawLogEntity();
        userProhiWithdrawLogEntity.setUserId(req.getUserId());
        userProhiWithdrawLogEntity.setType(req.getType());
        userProhiWithdrawLogEntity.setStatus(req.getStatus());
        userProhiWithdrawLogEntity.setMemo(req.getMemo());
        userProhiWithdrawLogEntity.setCreated(new Date());
        this.userProhiWithdrawLogDao.save(userProhiWithdrawLogEntity);
    }

    // 查询用户禁止提现的操作记录
    @Override
    public FUserQueryProhiWithdrawLogReq queryUserWithdrawStatusLog(FUserQueryProhiWithdrawLogReq req) throws Exception {
        List <FUserQueryProhiWithdrawLogRes> list = this.userProhiWithdrawLogDao.queryUserLogWithPage(req);
        req.setItems(list);
        return req;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void doRegisterNew(FUserRegisterReq userRegisterReq) throws Exception {
        this.userManager.isMobileExist(userRegisterReq.getMobile());
        // 保存用户表的信息
        TpzUserEntity userEntity = this.userManager.assemblyRegisterDataUser(userRegisterReq.getMobile(), userRegisterReq.getUserPwd(), userRegisterReq.getRegisterIp(), userRegisterReq.getRefUserId(), userRegisterReq.getUserInit());
        this.userDao.save(userEntity);
        // 保存用户扩展信息表
        TpzUserExtEntity userExtEntity = new TpzUserExtEntity();
        userExtEntity.setUserPhone(userEntity.getMobile());
        userExtEntity.setUserId(userEntity.getUserId());
        this.userExtDao.save(userExtEntity);
    }

    // 用户注册接口服务
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void doRegister(FUserRegisterReq userRegisterReq) throws Exception {
        this.userManager.isMobileExist(userRegisterReq.getMobile());
        // 检查火币的手机号
        Long huobiUID = this.getHuobiIdByPhone(userRegisterReq.getMobile());
        if (huobiUID != null) {
            throw new CustomerException("请使用火币手机号密码登陆。", 830002);
        }
        this.smsManager.checkSmsCode(userRegisterReq.getMobile(), ESmsBizType.REGISTER.getCode(), userRegisterReq.getCheckCode());
        // 保存用户表的信息
        TpzUserEntity userEntity = this.userManager.assemblyRegisterDataUser(userRegisterReq.getMobile(), userRegisterReq.getUserPwd(), userRegisterReq.getRegisterIp(), userRegisterReq.getRefUserId(), userRegisterReq.getUserInit());
        this.userDao.save(userEntity);
        // 保存用户扩展信息表
        TpzUserExtEntity userExtEntity = new TpzUserExtEntity();
        userExtEntity.setUserPhone(userEntity.getMobile());
        userExtEntity.setUserId(userEntity.getUserId());
        userExtEntity.setUserQq(userRegisterReq.getQq());
        this.userExtDao.save(userExtEntity);

        try {
            if (userRegisterReq.getUserInit().equals(EUserInit.YBK.getCode())) {
                // 注册类型是邮币卡的，发送注册短信
                this.smsManager.doSaveSmsOut(userRegisterReq.getMobile(), "已为您自动注册财猫账户，初始密码为证件号后六位数字。", ESmsBizType.REGISTER.getCode());
            }
        } catch (Exception e) {
            
        }
    }

    // 用户登陆接口服务
    @Override
    //@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Long doLogin(FUserLoginReq userLoginReq) throws Exception {
        TpzUserEntity userEntity = new TpzUserEntity();
        userEntity.setUserName(userLoginReq.getLoginName());
        userEntity.setMobile(userLoginReq.getLoginName());
        return this.userManager.operationLogin(userEntity, userLoginReq.getLoginPwd(), userLoginReq.getLoginIP(), userLoginReq.getSource() == null ?  "0" : userLoginReq.getSource());
    }

    // 设置资金密码
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void setTradePwd(FUserSetTradePwdReq userSetTradePwdReq) throws Exception {
        this.smsManager.checkSmsCode(userSetTradePwdReq.getMobile(), ESmsBizType.SETTRADEPWD.getCode(), userSetTradePwdReq.getCode());

        TpzUserEntity userEntity = this.userDao.getById(userSetTradePwdReq.getUserId());

        if (null == userEntity) {
            throw new CustomerException("此用户不存在,设置安全密码错误失败。", 830009);
        }
        TpzUserTradeEntity userTradeEntity = this.userTradeDao.getById(userSetTradePwdReq.getUserId());
        if ((null != userTradeEntity) && (StringUtils.isNotEmpty(userTradeEntity.getUserTradePwd()))) {
            throw new CustomerException("已经设置安全密码，不能重复设置。", 830009);
        }

        if (MD5Util.md5(userSetTradePwdReq.getTradePwd()).equals(userEntity.getUserPwd())) {
            throw new CustomerException("为了你的密码安全，安全密码不能与登录密码一致", 830009);
        }

        TpzUserTradeEntity tradeEntity = new TpzUserTradeEntity();
        tradeEntity.setUserId(userSetTradePwdReq.getUserId());
        tradeEntity.setUserTradePwdStrength(PwdUtils.GetPwdSecurityLevel(userSetTradePwdReq.getTradePwd()));

        tradeEntity.setUserTradePwd(MD5Util.md5(userSetTradePwdReq.getTradePwd()));
        tradeEntity.setErrorCount(0);
        this.userTradeDao.save(tradeEntity);
    }

    // 重置资金密码
    @Override
    public void resetTradePwd(FUserResetTradePwdReq userResetTradePwdReq) throws Exception {
        Long userId = userResetTradePwdReq.getUserId();
        String newTradePwd = userResetTradePwdReq.getNewTradePwd();
        String oldTradePwd = userResetTradePwdReq.getOldTradePwd();

        TpzUserEntity userEntity = this.userDao.getById(userId);

        if (null == userEntity) {
            throw new CustomerException("此用户不存在，不能修改安全密码错误。", 830010);
        }
        TpzUserTradeEntity userTradeEntity = null;
        TpzUserTradeEntity tradeEntity = this.userTradeDao.getById(userId);
        if (null == tradeEntity) {
            throw new CustomerException("此用户还没设置安全密码错误，不能进行修改。", 830010);
        }
        if (tradeEntity.getUserTradePwd().equals(MD5Util.md5(newTradePwd))) {
            throw new CustomerException("新密码与旧密码不能一致。", 830010);
        }
        if (!tradeEntity.getUserTradePwd().equals(MD5Util.md5(oldTradePwd))) {
            throw new CustomerException("旧密码错误。", 830010);
        }
        if (MD5Util.md5(newTradePwd).equals(userEntity.getUserPwd())) {
            throw new CustomerException("为了你的密码安全，安全密码不能与登录密码一致", 830009);
        }
        userTradeEntity = new TpzUserTradeEntity();
        userTradeEntity.setUserId(userId);
        userTradeEntity.setUserTradePwd(MD5Util.md5(newTradePwd));
        userTradeEntity.setUserTradePwdStrength(PwdUtils.GetPwdSecurityLevel(newTradePwd));
        this.userTradeDao.update(userTradeEntity);
    }

    // 找回资金密码
    @Override
    public void findTradePwd(FUserFindTradePwdReq userFindTradePwdReq) throws Exception {
        Long userId = userFindTradePwdReq.getUserId();
        String mobile = userFindTradePwdReq.getMobile();
        String checkCode = userFindTradePwdReq.getCheckCode();
        String questionId = userFindTradePwdReq.getQuestionId();
        String answerResult = userFindTradePwdReq.getAnswerResult();
        String userTradePwd = userFindTradePwdReq.getUserTradePwd();

        TpzUserEntity userEntity = this.userDao.getById(userId);

        if (null == userEntity) {
            throw new CustomerException("此用户不存在，不能修改安全密码。", 830010);
        }
        this.smsManager.checkSmsCode(mobile, ESmsBizType.FINDTRADEPWD.getCode(), checkCode);

        List<TpzUserPwdAnswerEntity> userAnswerList = this.userPwdAnswerDao.queryByUser(userId);
        if (null == userAnswerList) {
            throw new CustomerException("还没设置密保问题，不能找回安全密码错误", 830011);
        }
        if (CollectionUtils.isNotEmpty(userAnswerList)) {
            boolean flag = false;
            for (TpzUserPwdAnswerEntity pwdAnswerEntity : userAnswerList) {
                if ((pwdAnswerEntity.getQuestionId().equals(questionId)) && (pwdAnswerEntity.getAnswerResult().equals(answerResult))) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                logger.error("问题答案不正确，不能修改密码");
                throw new CustomerException("问题答案不正确，不能修改密码", 830011);
            }
            String md5UserTradePwd = MD5Util.md5(userTradePwd);
            if (userEntity.getUserPwd().equals(md5UserTradePwd)) {
                throw new CustomerException("为了你的密码安全，安全密码不能与登录密码一致", 830011);
            }
            TpzUserTradeEntity userTradeEntity = new TpzUserTradeEntity();

            userTradeEntity.setUserId(userId);
            userTradeEntity.setUserTradePwdStrength(PwdUtils.GetPwdSecurityLevel(userTradePwd));
            userTradeEntity.setUserTradePwd(md5UserTradePwd);
            this.userTradeDao.update(userTradeEntity);
        }
    }

    // 重置登陆密码
    @Override
    public void resetLoginpwd(FUserResetLoginPwdReq userResetLoginPwdReq) throws Exception {
        Long userId = userResetLoginPwdReq.getUserId();
        String oldPwd = userResetLoginPwdReq.getOldPwd();
        String newPwd = userResetLoginPwdReq.getNewPwd();

        if (null == userId) {
            throw new CustomerException("用户ID不能为空。", 830005);
        }
        if (StringUtils.isBlank(oldPwd)) {
            throw new CustomerException("旧密码不能为空。", 830005);
        }
        if (StringUtils.isBlank(newPwd)) {
            throw new CustomerException("新密码不能为空。", 830005);
        }
        TpzUserEntity user = this.userDao.getById(userId);

        if (null == user) {
            throw new CustomerException("此用户不存在，不能修改登陆密码。", 830005);
        }

        if (user.getHuobiUid() != 0) {
            // 火币的用户，验证火币的密码
            // 避免用户修改了手机号，所以使用UID查下用户信息，然后用返回的手机号进行验证
            String huobiPhone = this.huobiUserLoginHelper.getUserByUid(user.getHuobiUid(), "127.0.0.1");
            if (huobiPhone == null) {
                throw new CustomerException("帐号异常，请联系管理员。", 80321);
            }
            Boolean validRes = this.huobiUserLoginHelper.validUserPwd(huobiPhone, UserManager.huobiPasswordToMd5(oldPwd), "127.0.0.1");
            if (!validRes) {
                throw new CustomerException("输入的原密码错误。", 830005);
            }
        } else {
            if (!MD5Util.md5(oldPwd).equals(user.getUserPwd())) {
                throw new CustomerException("输入的原密码错误。", 830005);
            }
            if (MD5Util.md5(newPwd).equals(user.getUserPwd())) {
                throw new CustomerException("输入的新密码不能和旧密码一致。", 830005);
            }
        }

        TpzUserTradeEntity tradeEntity = this.userTradeDao.getById(userId);
        if (null != tradeEntity) {
            if (MD5Util.md5(newPwd).equals(tradeEntity.getUserTradePwd())) {
                throw new CustomerException("为了你的密码安全，安全密码不能与登录密码一致", 830009);
            }
        }

        if (user.getHuobiUid() != 0) {
            boolean huobiRes = this.huobiUserLoginHelper.updateUserPwd(user.getHuobiUid(), UserManager.huobiPasswordToMd5(oldPwd), UserManager.huobiPasswordToMd5(newPwd), "127.0.0.1");
            if (!huobiRes) {
                throw new CustomerException("修改密码失败，请稍后尝试", 830005);
            }
        } else {
            user.setUserPwd(MD5Util.md5(newPwd));
            user.setUserPwdStrength(PwdUtils.GetPwdSecurityLevel(newPwd));
            user.setUserId(userId);
            this.userDao.update(user);
        }
    }

    //绑定&修改邮箱地址
    @Override
    public void doBindOrChangeEmail(FUserBindOrChangeEmailReq userBindOrChangeEmailReq) throws Exception {
        Long userId = userBindOrChangeEmailReq.getUserId();
        String email = userBindOrChangeEmailReq.getEmail();
        String tradePwd = userBindOrChangeEmailReq.getTradePwd();

        TpzUserEntity user = null;
        if (null == userId) {
            throw new CustomerException("用户ID不能为空", 830007);
        }
        if (StringUtils.isBlank(email)) {
            throw new CustomerException("邮箱不能为空", 830007);
        }
        if (StringUtils.isBlank(tradePwd)) {
            throw new CustomerException("安全密码不能为空", 830007);
        }
        user = this.userDao.getById(userId);

        if (null == user) {
            throw new CustomerException("此用户不存在，不能绑定或更改邮箱。", 830007);
        }

        TpzUserTradeEntity userTradeEntity = this.userTradeDao.getById(userId);
        if ((userTradeEntity == null) || (StringUtils.isBlank(userTradeEntity.getUserTradePwd()))) {
            throw new CustomerException("当前用户还未设置安全密码，不能绑定邮箱", 830007);
        }

        String userTradePwd = userTradeEntity.getUserTradePwd();
        if (!userTradePwd.equals(MD5Util.md5(tradePwd))) {
            throw new CustomerException("安全密码错误，不能绑定邮箱", 830007);
        }
        TpzUserEntity condition = new TpzUserEntity();
        condition.setEmail(email.trim());
        List userList = this.userDao.queryUser(condition);
        if (userList.size() > 0) {
            throw new CustomerException("该邮箱已经被绑定，不能被再次绑定", 830007);
        }
        user = new TpzUserEntity();
        user.setUserId(userId);
        user.setEmail(email);
        this.userDao.updateEmail(user);
    }

    // 设置密保问题
    @Override
    public void doSetPwdAnswer(FUserSetPwdAnswerReq userSetPwdAnswerReq) throws Exception {
        Long userId = userSetPwdAnswerReq.getUserId();
        String qestion1 = userSetPwdAnswerReq.getQestion1();
        String answer1 = userSetPwdAnswerReq.getAnswer1();
        String qestion2 = userSetPwdAnswerReq.getQestion2();
        String answer2 = userSetPwdAnswerReq.getAnswer2();
        String qestion3 = userSetPwdAnswerReq.getQestion3();
        String answer3 = userSetPwdAnswerReq.getAnswer3();

        TpzUserEntity userEntity = this.userDao.getById(userId);
        if (null == userEntity) {
            throw new CustomerException("此用户不存在，不能重置密码问题。", 830012);
        }
        PwdQuestionEntity pwdQuestionEntity = null;
        pwdQuestionEntity = this.userPwdQuestionDao.getById(qestion1);
        if (pwdQuestionEntity == null) {
            throw new CustomerException("设置密保问题不在合法范围内。", 830012);
        }
        pwdQuestionEntity = this.userPwdQuestionDao.getById(qestion2);
        if (pwdQuestionEntity == null) {
            throw new CustomerException("设置密保问题不在合法范围内。", 830012);
        }
        pwdQuestionEntity = this.userPwdQuestionDao.getById(qestion3);
        if (pwdQuestionEntity == null) {
            throw new CustomerException("设置密保问题不在合法范围内。", 830012);
        }
        List<TpzUserPwdAnswerEntity> listPwdAnswer = this.userPwdAnswerDao.queryByUser(userId);
        if (CollectionUtils.isNotEmpty(listPwdAnswer)) {
            throw new CustomerException("当前用户已经有密保问题，只能重置密保问题", 830012);
        }
        List list = new ArrayList();
        TpzUserPwdAnswerEntity pwdanser1 = new TpzUserPwdAnswerEntity();
        pwdanser1.setUserId(userId);
        pwdanser1.setQuestionId(qestion1);
        pwdanser1.setAnswerResult(answer1);
        TpzUserPwdAnswerEntity pwdanser2 = new TpzUserPwdAnswerEntity();
        pwdanser2.setUserId(userId);
        pwdanser2.setQuestionId(qestion2);
        pwdanser2.setAnswerResult(answer2);
        TpzUserPwdAnswerEntity pwdanser3 = new TpzUserPwdAnswerEntity();
        pwdanser3.setUserId(userId);
        pwdanser3.setQuestionId(qestion3);
        pwdanser3.setAnswerResult(answer3);
        list.add(pwdanser1);
        list.add(pwdanser2);
        list.add(pwdanser3);
        this.userPwdAnswerDao.batchInsert(list);
    }

    // 重新设置密保问题
    @Override
    public void doResetPwdAnswer(FUserResetPwdAnswerReq userResetPwdAnswerReq) throws Exception {
        Long userId = userResetPwdAnswerReq.getUserId();
        String mobile = userResetPwdAnswerReq.getMobile();
        String code = userResetPwdAnswerReq.getCode();
        String tradePwd = userResetPwdAnswerReq.getTradePwd();
        String qestion1 = userResetPwdAnswerReq.getQestion1();
        String answer1 = userResetPwdAnswerReq.getAnswer1();
        String qestion2 = userResetPwdAnswerReq.getQestion2();
        String answer2 = userResetPwdAnswerReq.getAnswer2();
        String qestion3 = userResetPwdAnswerReq.getQestion3();
        String answer3 = userResetPwdAnswerReq.getAnswer3();

        TpzUserEntity userEntity = this.userDao.getById(userId);
        if (null == userEntity) {
            throw new CustomerException("此用户不存在，不能重置密码问题。", 830005);
        }
        if ((qestion1.equalsIgnoreCase(qestion2)) || (qestion1.equalsIgnoreCase(qestion3)) || (qestion2.equalsIgnoreCase(qestion3))) {
            throw new CustomerException("密保问题不能重复。", 830012);
        }
        this.smsManager.checkSmsCode(mobile, ESmsBizType.RESETPWDANSWER.getCode(), code);

        TpzUserTradeEntity tradeEntity = this.userTradeDao.getById(userId);
        if ((null == tradeEntity) || (StringUtils.isEmpty(tradeEntity.getUserTradePwd()))) {
            logger.error("The current user is not set password transactions, unable to reset your security question");
            throw new CustomerException("没有设置安全密码，不能进行密保设置。", 830013);
        }
        String userTradePwd = tradeEntity.getUserTradePwd();
        if (!userTradePwd.equals(MD5Util.md5(tradePwd))) {
            logger.error("Trading password mistake, unable to reset your security question");
            throw new CustomerException("安全密码错误，不能进行密保设置。", 830013);
        }
        PwdQuestionEntity pwdQuestionEntity = null;
        pwdQuestionEntity = this.userPwdQuestionDao.getById(qestion1);
        if (pwdQuestionEntity == null) {
            throw new CustomerException("设置密保问题不在合法范围内。", 830012);
        }
        pwdQuestionEntity = this.userPwdQuestionDao.getById(qestion2);
        if (pwdQuestionEntity == null) {
            throw new CustomerException("设置密保问题不在合法范围内。", 830012);
        }
        pwdQuestionEntity = this.userPwdQuestionDao.getById(qestion3);
        if (pwdQuestionEntity == null) {
            throw new CustomerException("设置密保问题不在合法范围内。", 830012);
        }
        this.userPwdAnswerDao.deleteById(userId);
        List list = new ArrayList();
        TpzUserPwdAnswerEntity pwdanser1 = new TpzUserPwdAnswerEntity();
        pwdanser1.setUserId(userId);
        pwdanser1.setQuestionId(qestion1);
        pwdanser1.setAnswerResult(answer1);
        TpzUserPwdAnswerEntity pwdanser2 = new TpzUserPwdAnswerEntity();
        pwdanser2.setUserId(userId);
        pwdanser2.setQuestionId(qestion2);
        pwdanser2.setAnswerResult(answer2);
        TpzUserPwdAnswerEntity pwdanser3 = new TpzUserPwdAnswerEntity();
        pwdanser3.setUserId(userId);
        pwdanser3.setQuestionId(qestion3);
        pwdanser3.setAnswerResult(answer3);
        list.add(pwdanser1);
        list.add(pwdanser2);
        list.add(pwdanser3);
        this.userPwdAnswerDao.batchInsert(list);
    }

    // 找回密保问题的验证
    @Override
    public boolean findQuestionCheck(FUserFindQuestionCheckReq req) throws Exception {
        TpzUserEntity userEntity = this.userDao.getById(req.getUserId());
        if (null == userEntity) {
            throw new CustomerException("此用户不存在，不能重置密码问题。", 830005);
        }
        this.smsManager.checkSmsCode(req.getMobile(), ESmsBizType.RESETPWDANSWER.getCode(), req.getCode());

        TpzUserTradeEntity tradeEntity = this.userTradeDao.getById(req.getUserId());
        if ((null == tradeEntity) || (StringUtils.isEmpty(tradeEntity.getUserTradePwd()))) {
            logger.error("The current user is not set password transactions, unable to reset your security question");
            throw new CustomerException("没有设置安全密码，不能进行密保设置。", 830013);
        }
        String userTradePwd = tradeEntity.getUserTradePwd();
        if (!userTradePwd.equals(MD5Util.md5(req.getTradePwd()))) {
            logger.error("Trading password mistake, unable to reset your security question");
            throw new CustomerException("安全密码错误，不能进行密保设置。", 830013);
        }
        return true;
    }

    @Override
    public void doFindLoginPwdNew(FUserFindLoginPwdReq userFindLoginPwdReq) throws Exception {
        String mobile = userFindLoginPwdReq.getMobile();
        String userLoginPwd = userFindLoginPwdReq.getUserLoginPwd();

        TpzUserEntity user = null;
        if (StringUtils.isBlank(mobile))
            throw new CustomerException("手机号不能为空", 830007, "BizServiceException");
        if (StringUtils.isBlank(userLoginPwd))
            throw new CustomerException("登录密码不能为空", 830007, "BizServiceException");
        user = new TpzUserEntity();
        user.setMobile(mobile);
        user = this.userDao.queryUserByNameOrMobile(user);
        if (null == user) {
            throw new CustomerException("此用户不存在，不能找回登陆密码。", 830007, "BizServiceException");
        }
        if (user.getHuobiUid() != 0) {
            throw new CustomerException("请在火币www.huobi.com网站进行找回密码。", 830007, "BizServiceException");
        }

        TpzUserTradeEntity trade = this.userTradeDao.getById(user.getUserId());
        if (null != trade && MD5Util.md5(userLoginPwd).equals(trade.getUserTradePwd())) {
            throw new CustomerException("为了你的密码安全，安全密码不能与登录密码一致", 830009, "BizServiceException");
        } else {
            user.setUserPwdStrength(PwdUtils.GetPwdSecurityLevel(userLoginPwd));
            user.setUserPwd(MD5Util.md5(userLoginPwd));
            user.setPwdSaltKey(this.userManager.createSaltKey());
            this.userDao.updateLoginPwd(user);
        }

    }

    // 用户找回密码
    @Override
    public void doFindLoginPwd(FUserFindLoginPwdReq userFindLoginPwdReq) throws CustomerException {
        String mobile = userFindLoginPwdReq.getMobile();
        String checkCode = userFindLoginPwdReq.getCheckCode();
        String userLoginPwd = userFindLoginPwdReq.getUserLoginPwd();

        TpzUserEntity user = null;
        if (StringUtils.isBlank(mobile))
            throw new CustomerException("手机号不能为空", 830007, "BizServiceException");
        if (StringUtils.isBlank(checkCode))
            throw new CustomerException("手机号不能为空", 830007, "BizServiceException");
        if (StringUtils.isBlank(userLoginPwd))
            throw new CustomerException("手机号不能为空", 830007, "BizServiceException");
        user = new TpzUserEntity();
        user.setMobile(mobile);
        user = this.userDao.queryUserByNameOrMobile(user);
        if (null == user) {
            throw new CustomerException("此用户不存在，不能找回登陆密码。", 830007, "BizServiceException");
        }
        if (user.getHuobiUid() != 0) {
            throw new CustomerException("请在火币www.huobi.com网站进行找回密码。", 830007, "BizServiceException");
        }
        this.smsManager.checkSmsCode(mobile, ESmsBizType.FINDLONGINPWD.getCode(), checkCode);
        TpzUserTradeEntity trade = this.userTradeDao.getById(user.getUserId());
        if (null != trade && MD5Util.md5(userLoginPwd).equals(trade.getUserTradePwd())) {
            throw new CustomerException("为了你的密码安全，安全密码不能与登录密码一致", 830009, "BizServiceException");
        } else {
            user.setUserPwdStrength(PwdUtils.GetPwdSecurityLevel(userLoginPwd));
            user.setUserPwd(MD5Util.md5(userLoginPwd));
            user.setPwdSaltKey(this.userManager.createSaltKey());
            this.userDao.updateLoginPwd(user);
        }
    }

    //重置用户手机号码
    @Override
    public void doResetMobile(FUserResetMobileReq userResetMobileReq) throws Exception {
        Long userId = userResetMobileReq.getUserId();
        String mobile = userResetMobileReq.getMobile();
        String checkCode = userResetMobileReq.getCheckCode();
        String tradePwd = userResetMobileReq.getTradePwd();

        if (null == userId) {
            throw new CustomerException("用户ID不能为空", 830006);
        }
        if (StringUtils.isBlank(mobile)) {
            throw new CustomerException("手机号不能为空", 830006);
        }
        if (StringUtils.isBlank(checkCode)) {
            throw new CustomerException("验证码不能为空", 830006);
        }
        if (StringUtils.isBlank(tradePwd)) {
            throw new CustomerException("安全密码不能为空", 830006);
        }
        TpzUserEntity userEntity = null;
        userEntity = this.userDao.getById(userId);
        if (null == userEntity) {
            throw new CustomerException("此用户不存在，不能修改手机号码。", 830006);
        }
        this.smsManager.checkSmsCode(mobile, ESmsBizType.CHANGEMOBILE.getCode(), checkCode);
        TpzUserTradeEntity userTradeEntity = this.userTradeDao.getById(userId);
        if ((null == userTradeEntity) || (StringUtils.isEmpty(userTradeEntity.getUserTradePwd()))) {
            throw new CustomerException("为了您的安全，请先设置安全密码，才能修改手机号码。", 830006);
        }
        if (!userTradeEntity.getUserTradePwd().equals(MD5Util.md5(tradePwd))) {
            throw new CustomerException("安全密码错误，不能修改手机号码。", 830006);
        }
        userEntity = this.userDao.queryUserByPhone(mobile);
        if (null != userEntity) {
            throw new CustomerException("该手机号已被注册，不能修改手机号。", 830006);
        }
        userEntity = new TpzUserEntity();
        userEntity.setUserId(userId);
        userEntity.setMobile(mobile);
        this.userDao.update(userEntity);
    }

    // 设置用户的其他信息
    @Override
    public void doRefreshUserExt(FUserRefreshUserExtReq userRefreshUserExtReq) throws Exception {
        Long userId = userRefreshUserExtReq.getUserId();
        String nickName = userRefreshUserExtReq.getNickName();
        String comefrom = userRefreshUserExtReq.getComefrom();
        String address = userRefreshUserExtReq.getAddress();
        String occupation = userRefreshUserExtReq.getOccupation();
        String education = userRefreshUserExtReq.getEducation();
        String inveExperience = userRefreshUserExtReq.getInveExperience();
        String qq = userRefreshUserExtReq.getQq();
        String weixin = userRefreshUserExtReq.getWeixin();

        TpzUserEntity userEntity = this.userDao.getById(userId);
        if (null == userEntity) {
            throw new CustomerException("此用户不存在。", 830015);
        }
        TpzUserExtEntity userExt = null;
        if (null != userId) {
            userExt = new TpzUserExtEntity();
            userExt.setUserId(userId);
            userExt.setUserOccupation(occupation);
            userExt.setUserComefrom(comefrom);
            userExt.setUserEducation(education);
            userExt.setInvrEmpiric(inveExperience);
            userExt.setUserQq(qq);
            userExt.setUserWeixin(weixin);
        }
        this.userExtDao.update(userExt);
        TpzUserEntity userInfo = new TpzUserEntity();
        userInfo.setUserId(userId);
        userInfo.setUserNickName(nickName);
        userInfo.setUserAddress(address);
        this.userDao.update(userInfo);
    }

    // 设置用户姓名
    @Override
    public void doSetUserName(Long userId, String userName) throws Exception {
        if (null == userId) {
            throw new CustomerException("用户ID不能为空", 830021);
        }
        if (StringUtils.isBlank(userName)) {
            throw new CustomerException("用户名不能为空", 830021);
        }
        TpzUserEntity userEntity = this.userDao.getById(userId);
        if ((null != userEntity) && (StringUtils.isNotBlank(userEntity.getUserName()))) {
            throw new CustomerException("用户名只能修改一次，不能多次修改", 830021);
        }
        TpzUserEntity userInfo = new TpzUserEntity();
        userInfo.setUserId(userId);
        userInfo.setUserName(userName);
        this.userDao.update(userInfo);
    }

    // 设置用户的昵称
    @Override
    public void doSetUserNickName(Long userId, String userNickName) throws Exception {
        if (null == userId) {
            throw new CustomerException("用户ID不能为空", 830021);
        }
        if (StringUtils.isBlank(userNickName)) {
            throw new CustomerException("用户名不能为空", 830021);
        }
        TpzUserEntity userInfo = new TpzUserEntity();
        userInfo.setUserId(userId);
        userInfo.setUserNickName(userNickName);
        this.userDao.update(userInfo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public boolean addScore(Long userId, Integer score, AddScoreType addScoreType, InviteReturnHistoryEntity inviteReturnHistoryEntity) throws Exception {
        InviteInfoEntity inviteInfoEntity = inviteInfoDao.getInviteInfoForUpdate(userId);
        Map<String, Object> paramMap = new HashMap<>();
        if (addScoreType == AddScoreType.COMMISSION_RETURN) {
            paramMap.put("commissionTotal", inviteInfoEntity.getCommissionTotal() + score);
        } else {
            System.out.println("增加邀请30分:\n" + ToStringBuilder.reflectionToString(inviteInfoEntity, ToStringStyle.MULTI_LINE_STYLE));
            paramMap.put("inviteTotal", inviteInfoEntity.getInviteTotal() + score);
        }
        inviteInfoDao.updateInviteInfo(userId, paramMap);

        TpzUserEntity tpzUserEntity = userDao.getByIdForUpdate(userId);
        tpzUserEntity.setUserScore((tpzUserEntity.getUserScore() == null ? 0 : tpzUserEntity.getUserScore()) + score);
        userDao.updateScore(tpzUserEntity);

        inviteReturnHistoryService.saveInviteReturnHistoryEntity(inviteReturnHistoryEntity);
        return false;
    }

    // 设置用户头像
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void updatePhotoByUserId(Long userId, String userPhoto) throws Exception {
        if (null == userId) {
            logger.error("用户ID不能为空。");
            throw new CustomerException("传入参数不能为空。", 888888);
        }
        TpzUserEntity user = this.userDao.getUserById(userId);
        if (null == user) {
            throw new CustomerException("此用户不存在。", 888888);
        }
        TpzUserExtEntity entity = new TpzUserExtEntity();
        entity.setUserId(userId);
        entity.setUserPhoto(userPhoto);
        this.userExtDao.updatePhotoByUserId(entity);
    }

    /**
     * 更新设置证件路径
     *
     * @param userId 用户ID
     * @param positivePath 正面证件路径
     * @param oppositePath 反面证件路径
     * @throws Exception
     */
    @Override
    public void updateCardPath(Long userId, String positivePath, String oppositePath) throws Exception {
        if (null == userId) {
            logger.error("用户ID不能为空。");
            throw new CustomerException("传入参数不能为空。", 888888);
        }
        TpzUserEntity user = this.userDao.getUserById(userId);
        if (null == user) {
            throw new CustomerException("此用户不存在。", 888888);
        }
        TpzUserExtEntity entity = new TpzUserExtEntity();
        entity.setUserId(userId);
        entity.setIdcardPositivePath(positivePath);
        entity.setIdcardOppositePath(oppositePath);
        this.userExtDao.updateCardPath(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public void addScoreNoRecord(Long userId, Integer score) throws Exception {
        InviteInfoEntity inviteInfoEntity = inviteInfoDao.getInviteInfoForUpdate(userId);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("commissionTotal", inviteInfoEntity.getCommissionTotal() + score);
        inviteInfoDao.updateInviteInfo(userId, paramMap);

        TpzUserEntity tpzUserEntity = userDao.getByIdForUpdate(userId);
        tpzUserEntity.setUserScore((tpzUserEntity.getUserScore() == null ? 0 : tpzUserEntity.getUserScore()) + score);
        userDao.updateScore(tpzUserEntity);
    }

    @Override
    public void doChangeUserRefUserId(Long userId, Long refUserId) throws Exception {
        if (userId == refUserId) throw new Exception("用户ID不能和邀请人ID相同");
        TpzUserEntity tpzUserEntity1 = userDao.getById(userId);
        if (tpzUserEntity1 == null) throw new Exception("用户不存在");
        TpzUserEntity tpzUserEntity2 = userDao.getById(refUserId);
        if (tpzUserEntity2 == null) throw new Exception("邀请人不存在");
        userDao.doChangeUserRefUserId(userId, refUserId);
    }


    // 获取几小时内错误的登录次数
    @Override
    public Integer queryLoginErrorCount(Long userId, Integer hours) throws Exception {
        TpzUserLoginLogEntity userLoginLogEntity = this.userLoginLogDao.queryLastLoginSuccessInfo(userId);
        Date beginDate = userLoginLogEntity == null ? new Date() : userLoginLogEntity.getLoginDatetime();
        Date endDate = new Date();
        Date searchBeginDate = new Date(System.currentTimeMillis() - (hours * 60 * 60 * 1000));
        beginDate = searchBeginDate.before(beginDate) ? beginDate : searchBeginDate;
        return this.userLoginLogDao.queryLoginErrorCount(userId, beginDate, endDate);
    }

    // 获取指定用户指定时间后的邀请用户
    @Override
    public List<TpzUserEntity> getUserRefList(Long userId, Date date) throws Exception {
        return this.userDao.getUserRefList(userId, date);
    }

    @Override
    public FUserListReq queryUserList(FUserListReq req) throws Exception {
        req.setItems(userDao.queryUserListWithPage(req));
        return req;
    }

    @Override
    public void checkSmsCode(Long userId, String smsType, String smsCode) throws Exception {
        TpzUserEntity userEntity = userDao.getById(userId);
        if(userEntity == null) throw new CustomerException("用户不存在", 5000001);
        this.smsManager.checkSmsCode(userEntity.getMobile(), smsType, smsCode);
    }

    /**
     * 查询用户总数
     *
     * @return
     * @throws Exception
     */
    @Override
    public int queryUserCount() throws Exception {
        return this.userDao.queryUserCount();
    }

    /**
     * 根据开始时间和结束时间统计注册用户数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    @Override
    public int queryRegisterCount(String startDate, String endDate) throws Exception {
        return this.userDao.queryRegisterCount(startDate, endDate);
    }

    /**
     * 查询贵金属用户统计列表
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FUserListReq queryGjsUserStatisticsListWithPage(FUserListReq req) throws Exception {
        req.setItems(userDao.queryGjsUserStatisticsListWithPage(req));
        return req;
    }

    /**
     * 用户开户统计
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FUserListReq queryGjsUserOpenAccountStatisticsListWithPage(FUserListReq req) throws Exception {
        List<FUserListRes> result = new ArrayList<>();
        List<FUserListRes> list = userDao.queryGjsUserOpenAccountStatisticsListWithPage(req);
        if (null != list) {
            for (FUserListRes res : list) {
                if ("NJS".equals(res.getExchange1())) {
                    ENJSBankNo njsBankNo = ENJSBankNo.findByCode(res.getBankId());
                    if (null != njsBankNo) {
                        res.setBankId(njsBankNo.getName());
                    }
                }
                if ("SJS".equals(res.getExchange1())) {
                    ESJSBankNo sjsBankNo = ESJSBankNo.findByCode(res.getBankId());
                    if (null != sjsBankNo) {
                        res.setBankId(sjsBankNo.getName());
                    }
                }
                res.setBankCard(hideString(res.getBankCard(), 0, 4, false));
                result.add(res);
            }
            req.setItems(result);
        }
        return req;
    }

    /**
     * 截取字符串
     * @param pString 字符串
     * @param start 前显示位数
     * @param end 后显示位数
     * @return String
     */
    private String hideString(String pString, Integer start, Integer end, Boolean isMasks){
        if(pString != null && !pString.equals("") && pString.length() >= start + end){
            return pString.substring(0, start) + (isMasks?"********":"") + pString.substring(pString.length() - end, pString.length());
        }else{
            return null;
        }
    }

    /**
     * NJS交易统计
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FUserListReq queryGjsUserTradeStatisticsListWithPage(FUserListReq req) throws Exception {
        req.setItems(userDao.queryGjsUserTradeStatisticsListWithPage(req));
        return req;
    }

    /**
     * 资金
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FUserListReq queryGjsUserMoneyStatisticsListWithPage(FUserListReq req) throws Exception {
        req.setItems(userDao.queryGjsUserMoneyStatisticsListWithPage(req));
        return req;
    }

    /**
     * 转化
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FUserListReq queryGjsUserTransformStatisticsListWithPage(FUserListReq req) throws Exception {
        req.setItems(userDao.queryGjsUserTransformStatisticsListWithPage(req));
        return req;
    }

}
