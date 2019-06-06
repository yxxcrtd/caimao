package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.InviteReturnHistoryEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.TpzUserExtEntity;
import com.caimao.bana.api.entity.TpzUserTradeEntity;
import com.caimao.bana.api.entity.req.*;
import com.caimao.bana.api.entity.req.user.FUserProhiWithdrawReq;
import com.caimao.bana.api.entity.req.user.FUserQueryProhiWithdrawLogReq;
import com.caimao.bana.api.enums.AddScoreType;
import com.caimao.bana.api.enums.ESmsBizType;

import java.util.Date;
import java.util.List;


/**
 * 用户服务类
 *
 * @author yanjg
 *         2015年4月13日
 */
public interface IUserService {

    /**
     * 获取用户的交易密码信息
     * @param userId
     * @return
     */
    public TpzUserTradeEntity getUserTrade(Long userId);

    /**
     * 根据手机号，查找用户的user_id
     *
     * @param phone 手机号
     * @return 返回用户的USER_ID
     */
    public Long getUserIdByPhone(String phone) throws Exception;

    /**
     * 根据手机号，获取火币的单一用户的uid
     * @param phone
     * @return
     * @throws Exception
     */
    public Long getHuobiIdByPhone(String phone) throws Exception;

    /**
     * 获取用户信息，根据用户ID
     *
     * @param userId 用户ID
     * @return 用户信息
     * @throws Exception
     */
    public TpzUserEntity getById(Long userId) throws Exception;

    /**
     * 获取用户信息，根据财猫ID
     *
     * @param caimaoId 财猫ID
     * @return 用户信息
     * @throws Exception
     */
    public TpzUserEntity getByCaimaoId(Long caimaoId) throws Exception;

    /**
     * 用户注册校验短信
     */
    public boolean doCheckSmsCode(String mobile, String smsCode, ESmsBizType eSmsBizType) throws Exception;

    /**
     * 用户注册接口服务, 不再校验手机短信验证码
     *
     * @param userRegisterReq 用户注册信息请求
     * @throws Exception
     */
    public void doRegisterNew(FUserRegisterReq userRegisterReq) throws Exception;

    /**
     * 用户注册接口服务
     *
     * @param userRegisterReq 用户注册信息请求
     * @throws Exception
     */
    public void doRegister(FUserRegisterReq userRegisterReq) throws Exception;

    /**
     * 用户登陆接口服务
     *
     * @param userLoginReq 登陆信息请求
     * @return 用户ID
     */
    public Long doLogin(FUserLoginReq userLoginReq) throws Exception;

    /**
     * 设置资金密码
     *
     * @param userSetTradePwdReq 设置资金密码的请求
     * @throws Exception
     */
    public void setTradePwd(FUserSetTradePwdReq userSetTradePwdReq) throws Exception;

    /**
     * 重置资金密码
     *
     * @param userResetTradePwdReq 重置资金密码的请求
     * @throws Exception
     */
    public void resetTradePwd(FUserResetTradePwdReq userResetTradePwdReq) throws Exception;

    /**
     * 找回资金密码
     *
     * @param userFindTradePwdReq 找回资金密码的对象
     * @throws Exception
     */
    public void findTradePwd(FUserFindTradePwdReq userFindTradePwdReq) throws Exception;

    /**
     * 重置登陆密码
     *
     * @param userResetLoginPwdReq 重置登陆密码对象
     * @throws Exception
     */
    public void resetLoginpwd(FUserResetLoginPwdReq userResetLoginPwdReq) throws Exception;

    /**
     * 绑定&修改邮箱地址
     *
     * @param userBindOrChangeEmailReq 用户绑定&变更邮箱对象
     * @throws Exception
     */
    public void doBindOrChangeEmail(FUserBindOrChangeEmailReq userBindOrChangeEmailReq) throws Exception;

    /**
     * 设置密保问题
     *
     * @param userSetPwdAnswerReq 设置密保问题的对象
     * @throws Exception
     */
    public void doSetPwdAnswer(FUserSetPwdAnswerReq userSetPwdAnswerReq) throws Exception;

    /**
     * 重新设置密保问题
     *
     * @param userResetPwdAnswerReq 重置密保问题的对象
     * @throws Exception
     */
    public void doResetPwdAnswer(FUserResetPwdAnswerReq userResetPwdAnswerReq) throws Exception;

    /**
     * 找回密保问题的验证
     * @param req
     * @return
     * @throws Exception
     */
    public boolean findQuestionCheck(FUserFindQuestionCheckReq req) throws Exception;

    /**
     * 用户找回密码, 不再校验手机短信验证码
     *
     * @param userFindLoginPwdReq 找回密码请求对象
     * @throws Exception
     */
    public void doFindLoginPwdNew(FUserFindLoginPwdReq userFindLoginPwdReq) throws Exception;

    /**
     * 用户找回密码
     *
     * @param userFindLoginPwdReq 找回密码请求对象
     * @throws Exception
     */
    public void doFindLoginPwd(FUserFindLoginPwdReq userFindLoginPwdReq) throws Exception;

    /**
     * 重置用户手机号码
     *
     * @param userResetMobileReq 重置密码的请求
     * @throws Exception
     */
    public void doResetMobile(FUserResetMobileReq userResetMobileReq) throws Exception;

    /**
     * 设置用户的其他信息
     *
     * @param userRefreshUserExtReq 用户扩展信息
     * @throws Exception
     */
    public void doRefreshUserExt(FUserRefreshUserExtReq userRefreshUserExtReq) throws Exception;

    /**
     * 根据用户ID获取用户扩展信息
     * @param userId
     * @return
     * @throws Exception
     */
    public TpzUserExtEntity getUserExtById(Long userId) throws Exception;

    /**
     * 更新用户的扩展信息
     * @param userExtEntity
     * @throws Exception
     */
    public void updateUserExt(TpzUserExtEntity userExtEntity) throws Exception;

    /**
     * 设置用户的提现状态
     * @param req
     * @throws Exception
     */
    public void setUserWithdrawStatus(FUserProhiWithdrawReq req) throws Exception;

    /**
     * 查询用户禁止提现的操作记录
     * @param req
     * @return
     * @throws Exception
     */
    public FUserQueryProhiWithdrawLogReq queryUserWithdrawStatusLog(FUserQueryProhiWithdrawLogReq req) throws Exception;

    /**
     * 设置用户姓名
     *
     * @param userId   用户ID
     * @param userName 用户名
     * @throws Exception
     */
    public void doSetUserName(Long userId, String userName) throws Exception;

    /**
     * 设置用户的昵称
     * @param userId
     * @param userNickName
     * @throws Exception
     */
    public void doSetUserNickName(Long userId, String userNickName) throws Exception;

    /**
     * 为用户增加积分
     */
    public boolean addScore(Long userId, Integer score, AddScoreType addScoreType, InviteReturnHistoryEntity inviteReturnHistoryEntity) throws Exception;

    /**
     * 设置用户的头像
     *
     * @param userId    用户ID
     * @param userPhoto 头像文件名称
     */
    public void updatePhotoByUserId(Long userId, String userPhoto) throws Exception;


    /**
     * 更新设置证件路径
     * @param userId 用户ID
     * @param positivePath 正面证件路径
     * @param oppositePath 反面证件路径
     * @throws Exception
     */
    public void updateCardPath(Long userId, String positivePath,String oppositePath) throws Exception;


    /**
     * 获取几小时内错误的登录次数
     * @param userId
     * @param hours
     * @return
     * @throws Exception
     */
    public Integer queryLoginErrorCount(Long userId, Integer hours) throws Exception;


    /**
     * 为用户增加积分
     */
    public void addScoreNoRecord(Long userId, Integer score) throws Exception;

    /**
     * 变更用户邀请用户id
     * @param userId 用户id
     * @param refUserId 邀请人id
     * @return
     * @throws Exception
     */
    public void doChangeUserRefUserId(Long userId, Long refUserId) throws Exception;

    /**
     * 获取指定用户指定时间后的邀请用户
     * @param userId
     * @param date
     * @return
     * @throws Exception
     */
    public List<TpzUserEntity> getUserRefList(Long userId, Date date) throws Exception;

    /**
     * 验证手机验证码
     * @param userId 用户编号
     * @param smsType 验证码类型
     * @param smsCode 验证码
     * @throws Exception
     */
    public void checkSmsCode(Long userId, String smsType, String smsCode) throws Exception;

    /**
     * 查询用户列表
     * @param req
     * @return
     * @throws Exception
     */
    public FUserListReq queryUserList(FUserListReq req) throws Exception;

    /**
     * 查询用户总数
     *
     * @return
     * @throws Exception
     */
    int queryUserCount() throws Exception;

    /**
     * 根据开始时间和结束时间统计注册用户数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    int queryRegisterCount(String startDate, String endDate) throws Exception;

    /**
     * 查询贵金属用户统计列表
     *
     * @param req
     * @return
     * @throws Exception
     */
    FUserListReq queryGjsUserStatisticsListWithPage(FUserListReq req) throws Exception;

    /**
     * 用户开户统计
     *
     * @param req
     * @return
     * @throws Exception
     */
    FUserListReq queryGjsUserOpenAccountStatisticsListWithPage(FUserListReq req) throws Exception;

    /**
     * 交易统计
     *
     * @param req
     * @return
     * @throws Exception
     */
    FUserListReq queryGjsUserTradeStatisticsListWithPage(FUserListReq req) throws Exception;

    /**
     * 资金
     *
     * @param req
     * @return
     * @throws Exception
     */
    FUserListReq queryGjsUserMoneyStatisticsListWithPage(FUserListReq req) throws Exception;

    /**
     * 转化
     *
     * @param req
     * @return
     * @throws Exception
     */
    FUserListReq queryGjsUserTransformStatisticsListWithPage(FUserListReq req) throws Exception;

}