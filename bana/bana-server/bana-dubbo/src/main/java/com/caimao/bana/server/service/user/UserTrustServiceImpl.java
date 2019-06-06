package com.caimao.bana.server.service.user;

import com.caimao.bana.api.entity.TpzUserAuthJourEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.enums.EAuthStatus;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IUserTrustService;
import com.caimao.bana.server.dao.userDao.TpzUserAuthJourDao;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.utils.AccountManager;
import com.caimao.bana.server.utils.CheckIdCard;
import com.caimao.bana.server.utils.Constants;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 用户实名认证接口方法
 * Created by WangXu on 2015/5/18.
 */
@Service("userTrustService")
public class UserTrustServiceImpl implements IUserTrustService {

    @Autowired
    private TpzUserDao userDao;
    @Autowired
    private TpzUserAuthJourDao userAuthJourDao;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private MemoryDbidGenerator dbidGenerator;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public Long doVerifyIdentity(Long userId, String realName, String idCardKind, String idCard) throws Exception{
        TpzUserEntity userEntity = this.userDao.getUserById(userId);
        if (userEntity == null) {
            throw new CustomerException("无法获取用户信息", 83032105);
        }
        if (userEntity.getIsTrust().equals(EAuthStatus.YES.getCode())) {
            throw new CustomerException("该用户已经实名认证", 83032105);
        }
        // 查询这个类型的号码是不是被别人给绑定了
        TpzUserAuthJourEntity authJour = new TpzUserAuthJourEntity();
        authJour.setIdcard(idCard);
        authJour.setIdcardKind(idCardKind);
        TpzUserAuthJourEntity userAuthJour = this.userAuthJourDao.getPassedUserAuthJour(authJour);
        if (userAuthJour != null) {
            throw new CustomerException("该身份证信息已经被验证,如果不是本人操作请联系客服", 83032105);
        }
        // 查询这个证件号码是不是在别人的用户表中已经存在了，这个是查询的user表中的
        TpzUserEntity user = new TpzUserEntity();
        user.setIdcard(idCard);
        List<TpzUserEntity> list = this.userDao.queryUser(user);
        if ((list != null) && (list.size() > 0) && ( ! userId.equals((list.get(0)).getUserId()))) {
            throw new CustomerException("您的身份证号，已经认被其他用户证过过，如非本人操作，请联系平台管理员！", 830412);
        }
        // TODO (去掉这个功能先) 如果用户验证大于等于3次，必须使用上传身份证的方式进行
//        int num = this.userAuthJourDao.countAuthNum(userId);
//        if (num >= 3) {
//            throw new CustomerException("由于您多次验证无法通过,请选择上传身份证验证", 83061224);
//        }

        CheckIdCard card = new CheckIdCard(idCard);
        Boolean result = card.validate();
        if (result) {
            Date Birthdate = card.getBirthDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(Birthdate);
            cal.add(1, 18);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(new Date());
            int i = cal.compareTo(c2);
            if (i > 0)
                throw new CustomerException("未满18岁禁止使用", 83032105);
        } else {
            throw new CustomerException("身份证格式不正确,请重新输入", 83032105);
        }
        userAuthJour = new TpzUserAuthJourEntity();
        Date date = new Date();
        userAuthJour.setId(this.dbidGenerator.getNextId());
        userAuthJour.setCreateDatetime(date);
        userAuthJour.setIdcard(idCard);
        userAuthJour.setIdcardKind(idCardKind);
        userAuthJour.setUpdateDatetime(date);
        userAuthJour.setUserId(userId);
        userAuthJour.setUserRealName(realName);

        this.userAuthJourDao.save(userAuthJour);

        userAuthJour = this.doId5Check(userAuthJour);

        this.userAuthJourDao.update(userAuthJour);

        String authStatus = userAuthJour.getAuthStatus();
        // 如果实名认证通过，则创建用户的资产账户
        if (EAuthStatus.YES.getCode().equalsIgnoreCase(authStatus)) {
            Long pzaccountId = this.accountManager.doGenerateAccount(userId, realName);

            user = new TpzUserEntity();
            user.setIdcard(idCard);
            user.setIdcardKind(idCardKind);
            user.setIsTrust(EAuthStatus.YES.getCode());
            user.setUserAddress(null);
            user.setUserRealName(realName);
            user.setUserId(userId);
            this.userDao.update(user);
            return pzaccountId;
        }
        throw new CustomerException("实名认证错误，错误信息：" + userAuthJour.getAuthFailReason(), 83032105);
    }

    /**
     * 是否需要使用外部服务进行验证身份证件
     * @param userAuthJour  实名认证信息
     * @return  实名认证信息
     * @throws Exception
     */
    private TpzUserAuthJourEntity doId5Check(TpzUserAuthJourEntity userAuthJour) throws Exception {
        if (Constants.TRUST_ID5TEST) {
            userAuthJour.setAuthStatus(EAuthStatus.YES.getCode());
            return userAuthJour;
        }
        throw new CustomerException("无法识别身份验证器通道，请联系客服", 83032106);
    }
}
