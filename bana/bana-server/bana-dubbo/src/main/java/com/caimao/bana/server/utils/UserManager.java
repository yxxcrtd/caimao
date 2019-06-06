package com.caimao.bana.server.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.TpzUserExtEntity;
import com.caimao.bana.api.entity.TpzUserLoginLogEntity;
import com.caimao.bana.api.entity.TpzUserTradeEntity;
import com.caimao.bana.api.enums.*;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.dao.userDao.TpzUserExtDao;
import com.caimao.bana.server.dao.userDao.TpzUserLoginLogDao;
import com.caimao.bana.server.dao.userDao.TpzUserTradeDao;
import com.caimao.bana.server.service.user.HuobiUserLoginHelper;
import com.hsnet.pz.core.util.PwdUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * Created by WangXu on 2015/5/18.
 */
@Service
public class UserManager {

    @Autowired
    private TpzUserDao userDao;
    @Autowired
    private TpzUserExtDao userExtDao;
    @Autowired
    private TpzUserTradeDao userTradeDao;
    @Autowired
    private TpzUserLoginLogDao userLoginLogDao;
    @Autowired
    private MemoryDbidGenerator dbidGenerator;

    // 火币登陆服务
    @Autowired
    private HuobiUserLoginHelper huobiUserLoginHelper;

    /**
     * 验证用户的资金密码是否正确
     * @param userId    用户ID
     * @param tradePwd  用户资金密码
     * @throws Exception
     */
    public void validateUserTradePwd(Long userId, String tradePwd) throws Exception {
        if (null == userId) {
            throw new CustomerException("用户ID不能为空。", 83000910);
        }
        if (StringUtils.isBlank(tradePwd)) {
            throw new CustomerException("安全密码不能为空。", 83000910);
        }
        TpzUserEntity userEntity = this.userDao.getUserById(userId);
        if (null == userEntity) {
            throw new CustomerException("此用户不存在。", 83000910);
        }
        TpzUserTradeEntity trade = this.userTradeDao.getById(userId);
        if ((null != trade) && (!trade.getUserTradePwd().equals(MD5Util.md5(tradePwd)))) {
            this.userTradeDao.updateErrorCountById(userId);
            throw new CustomerException("安全密码错误。", 83000910);
        }
        if (null == trade) {
            throw new CustomerException("您还未设置安全密码，请先设置。", 83000910);
        }
    }

    /**
     * 手机号是否存在，存在则抛出异常
     * @param mobile    手机号码
     * @throws Exception
     */
    public void isMobileExist(String mobile) throws Exception {
        if (StringUtils.isBlank(mobile)) {
            throw new CustomerException("传入参数不能为空。", 830002);
        }
        if (!PhoneUtils.isMobile(mobile)) {
            throw new CustomerException("手机格式不正确。", 830002);
        }
        TpzUserEntity userEntity = this.userDao.queryUserByPhone(mobile);
        if (null != userEntity)
            throw new CustomerException("该手机号已被注册。", 830002);
    }

    /**
     * 用户名是否存在，存在则抛出异常
     * @param userName  用户名
     * @throws Exception
     */
    public void isUserNameExist(String userName) throws Exception {
        if (StringUtils.isBlank(userName)) {
            throw new CustomerException("用户名为空。", 830003);
        }
        TpzUserEntity userEntity = this.userDao.isUserNameExist(userName);
        if (null != userEntity) {
            throw new CustomerException("该用户名已存在。", 830003);
        }
    }

    /**
     * 装配用户注册的信息
     * @param mobile    手机号码
     * @param loginPwd  登陆密码
     * @param registerIp    注册IP
     * @param refUserId 邀请用户
     * @param userInit  用户注册来源
     * @return  用户信息
     * @throws Exception
     */
    public TpzUserEntity assemblyRegisterDataUser(String mobile, String loginPwd, String registerIp, Long refUserId, String userInit) throws Exception {
        TpzUserEntity userEntity = new TpzUserEntity();
        if (loginPwd.length() > 20) {
            throw new CustomerException("密码长度过长", 830001);
        }
        if ((!EUserInit.WEB.getCode().equals(userInit)) &&
                (!EUserInit.MOBILE.getCode().equals(userInit)) &&
                (!EUserInit.OSS.getCode().equals(userInit)) &&
                (!EUserInit.GJS.getCode().equals(userInit)) &&
                (!EUserInit.YBK.getCode().equals(userInit)) &&
                (!EUserInit.HUOBI.getCode().equals(userInit))) {
            throw new CustomerException("用户来源不合法。", 830001);
        }
        if (StringUtils.isNotEmpty(String.valueOf(refUserId))) {
            userEntity.setRefUserId(refUserId); // 注册用户推荐人
        }
        userEntity.setUserId(this.dbidGenerator.getNextId());   // 用户ID
        userEntity.setUserName(null);   // 用户名
        userEntity.setMobile(mobile);   // 用户手机号码
        userEntity.setUserGrade(Short.valueOf("0"));    // 等级
        userEntity.setUserScore(0); // 用户积分
        userEntity.setUserPwdStrength(PasswordUtils.GetPwdSecurityLevel(loginPwd));  // 用户密码安全等级
        userEntity.setUserPwd(MD5Util.md5(loginPwd));   // 用户登陆密码
        userEntity.setPwdSaltKey(this.createSaltKey());
        userEntity.setRegisterDatetime(new Date()); // 注册时间
        userEntity.setLoginCount(0);    // 登陆次数
        userEntity.setRegisterIp(registerIp);   // 注册ip
        userEntity.setLastLoginDatetime(new Date());    // 最后登陆时间
        userEntity.setLastLoginIp(registerIp);  // 最后登陆ip
        userEntity.setUserStatus(EUserStatus.NORMAL.getCode()); // 用户状态
        userEntity.setErrorCount(0);    // 错误次数
        userEntity.setIsTrust(EUserIsTrust.NO.getCode());   // 是否实名
        userEntity.setUserInit(userInit);   // 用户注册来源
        userEntity.setUserKind(EUserKind.INVESTOR.getCode());   // 账户性质
        return userEntity;
    }

    public String createSaltKey() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 用户登陆相关操作，验证密码、记录登陆日志等操作
     * @param userQueryEntity   用户信息
     * @param userPwd   登陆密码
     * @param loginIp   登陆IP
     * @param source    登陆来源
     * @return  用户ID
     * @throws Exception
     */
    public Long operationLogin(TpzUserEntity userQueryEntity, String userPwd, String loginIp, String source) throws Exception {
        // 先查询用户是否存在
        TpzUserEntity userInfo = this.userDao.queryUserByNameOrMobile(userQueryEntity);
        if (null == userInfo) {
            // 查询火币的手机号用户
            Long huobiUID = this.huobiUserLoginHelper.getUidByPhone(userQueryEntity.getMobile(), loginIp);
            if (huobiUID == null) {
                throw new CustomerException("帐号或密码错误。", 830004);
            }
            // 验证这个火币的账户和密码
            Boolean validRes = this.huobiUserLoginHelper.validUserPwd(userQueryEntity.getMobile(), UserManager.huobiPasswordToMd5(userPwd), loginIp);
            if (!validRes) {
                throw new CustomerException("帐号或密码错误。", 80321);
            }
            // 验证成功了，在数据库加那么一条数据
            TpzUserEntity userEntity = this.assemblyRegisterDataUser(userQueryEntity.getMobile(), userPwd, loginIp, null, EUserInit.HUOBI.getCode());
            // 设置火币对应的uid
            userEntity.setHuobiUid(huobiUID);
            this.userDao.save(userEntity);
            // 保存用户扩展信息表
            TpzUserExtEntity userExtEntity = new TpzUserExtEntity();
            userExtEntity.setUserPhone(userEntity.getMobile());
            userExtEntity.setUserId(userEntity.getUserId());
            this.userExtDao.save(userExtEntity);

            // 添加完了，在调用一下这个方法
            return this.operationLogin(userQueryEntity, userPwd, loginIp, source);

        }
        TpzUserEntity uniqueUserByHuobi = null;
        TpzUserEntity uniqueUserByName = null;
        TpzUserEntity uniqueUserByMobile = null;

        // 检查这个用户是否是火币的
        if (userInfo.getHuobiUid() != 0) {
            // 验证火币的密码
            // 避免用户修改了手机号，所以使用UID查下用户信息，然后用返回的手机号进行验证
            String huobiPhone = this.huobiUserLoginHelper.getUserByUid(userInfo.getHuobiUid(), loginIp);
            if (huobiPhone == null) {
                throw new CustomerException("帐号异常，请联系管理员。", 80321);
            }
            Boolean validRes = this.huobiUserLoginHelper.validUserPwd(huobiPhone, UserManager.huobiPasswordToMd5(userPwd), loginIp);
            if (validRes) {
                uniqueUserByHuobi = userInfo;
            }
        } else {
            userQueryEntity.setUserPwd(MD5Util.md5(userPwd));
            // 查询用户密码是否正确
            uniqueUserByName = this.userDao.queryUserBynameAndPwd(userQueryEntity);
            uniqueUserByMobile = this.userDao.queryUserBymobileAndPwd(userQueryEntity);
        }

        // 查询用户状态是否正常
        Long userId = userInfo.getUserId();
        if (EUserStatus.BLOCK.getCode().equalsIgnoreCase(userInfo.getUserStatus())) {
            throw new CustomerException("你已经被管理员锁定，不能登录。", 830004);
        }

        // 检查有没有密码盐，没有设置就设置并更新
        if (userInfo.getPwdSaltKey() == null || Objects.equals(userInfo.getPwdSaltKey(), "")) {
            TpzUserEntity userTmp = new TpzUserEntity();
            userTmp.setUserId(userInfo.getUserId());
            userTmp.setPwdSaltKey(this.createSaltKey());
            this.userDao.update(userTmp);
        }


        // 记录登陆日志
        TpzUserLoginLogEntity loginLogEntity = new TpzUserLoginLogEntity();
        loginLogEntity.setId(this.dbidGenerator.getNextId());
        loginLogEntity.setLoginDatetime(new Date());
        loginLogEntity.setLoginIp(loginIp);
        loginLogEntity.setUserId(userId);
        loginLogEntity.setSource(Integer.valueOf(source));

        // 登陆成功，记录成功信息
        if ((null != uniqueUserByName) || (null != uniqueUserByMobile) || (null != uniqueUserByHuobi)) {
            userInfo.setLastLoginDatetime(new Date());
            userInfo.setLastLoginIp(loginIp);
            userInfo.setUserId(userInfo.getUserId());

            this.userDao.updateLoginSuccess(userInfo);
            loginLogEntity.setIsSuccess(EUserLoginIsSuccess.SUCCESS.getCode());

            this.userLoginLogDao.save(loginLogEntity);
            return userId;
        }
        // 登陆失败，记录错误日志信息
        this.userDao.updateLoginFail(userInfo.getUserId());
        loginLogEntity.setIsSuccess(EUserLoginIsSuccess.FAILURE.getCode());
        this.userLoginLogDao.save(loginLogEntity);
        throw new CustomerException("帐号或密码错误。", 830004);
    }

    /**
     * 火币的密码加密的东东
     * @param password
     * @return
     */
    public static String huobiPasswordToMd5(String password){
        String pwd = "";
        if(password.indexOf("md5")==0 && password.length()==35){
            pwd= DigestUtils.md5Hex(DigestUtils.md5Hex(password.substring(3)) + "may the force be with you");
        }else{
            pwd=DigestUtils.md5Hex(DigestUtils.md5Hex(password+"hello, moto"));
            pwd = DigestUtils.md5Hex(pwd+"may the force be with you");
        }
        return pwd;
    }

}
