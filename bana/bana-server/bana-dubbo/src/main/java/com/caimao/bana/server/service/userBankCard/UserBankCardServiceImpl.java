package com.caimao.bana.server.service.userBankCard;

import com.caimao.bana.api.entity.TpzBankTypeEntity;
import com.caimao.bana.api.entity.TpzUserBankCardEntity;
import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.enums.EAuthStatus;
import com.caimao.bana.api.enums.EBankCardStatus;
import com.caimao.bana.api.enums.EUserBankDefault;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.IUserBankCardService;
import com.caimao.bana.server.dao.userBankCardDao.TpzBankTypeDao;
import com.caimao.bana.server.dao.userBankCardDao.TpzUserBankCardDao;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.utils.ChannelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 银行卡相关操作
 * Created by WangXu on 2015/5/7.
 */
@Service("userBankCardService")
public class UserBankCardServiceImpl implements IUserBankCardService {

    private static final Logger logger = LoggerFactory.getLogger(UserBankCardServiceImpl.class);

    @Autowired
    private TpzUserBankCardDao tpzUserBankCardDao;
    @Autowired
    private TpzUserDao tpzUserDao;
    @Autowired
    private TpzBankTypeDao tpzBankTypeDao;

    @Override
    public List<TpzUserBankCardEntity> queryUserBankList(Long userId, String bankCardStatus) {
        List<TpzUserBankCardEntity> userBankList = this.tpzUserBankCardDao.queryUserBankCardList(userId, bankCardStatus);

        List<TpzUserBankCardEntity> newUserBankCardList = new ArrayList<>();
        for (int i = 0; i < userBankList.size(); i++) {
            TpzUserBankCardEntity userbankCard = userBankList.get(i);
            userbankCard.setBankCardNo(ChannelUtil.hide(userbankCard.getBankCardNo(), 4, 4));
            userbankCard.setIdCardHide(ChannelUtil.hide(userbankCard.getIdcard(), 3, 3));
            userbankCard.setIdCardNameHide(ChannelUtil.hide(userbankCard.getBankCardName(), 1, 0));
            newUserBankCardList.add(i, userbankCard);
        }
        return newUserBankCardList;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = java.lang.Throwable.class)
    public boolean doBindBankCard(Long userId, String bankNo, String bankCardNo, String province, String city, String openBank) throws Exception {
        this.logger.info("绑定 {} 用户的银行卡 {}，银行{}，省{}，市{}，开户行{}", userId, bankCardNo, bankNo, province, city, openBank);
        // 查询用户信息
        TpzUserEntity userEntity = this.tpzUserDao.getUserById(userId);
        if (userEntity == null) {
            throw new CustomerException("无法获取用户信息", 830405);
        }
        // 判断用户是否已经实名认证
        if ((userEntity.getIsTrust() != null) && (!userEntity.getIsTrust().equals(EAuthStatus.YES.getCode()))) {
            throw new CustomerException("请先进行实名认证", 830408);
        }
        // 获取银行卡号信息，如果银行卡已经被其他人绑定，则不允许进行再次绑定
        List<TpzUserBankCardEntity> bindBankCards = this.tpzUserBankCardDao.getBindBankByCardNo(bankNo);

        if ((bindBankCards != null) && (bindBankCards.size() > 0)) {
            TpzUserBankCardEntity bankCard = bindBankCards.get(0);
            if (bankCard.getUserId().equals(userId)) {
                this.logger.info("用户已使用该银行卡号 {}，更新操作", bankCard);
                this.tpzUserBankCardDao.updateUserBank(userId);
                bankCard.setBankCardStatus(EBankCardStatus.YES.getCode());
                bankCard.setIsDefault(EUserBankDefault.YES.getCode());
                bankCard.setBankCode(bankCardNo);
                bankCard.setBankNo(bankNo);
                bankCard.setProvince(province);
                bankCard.setCity(city);
                bankCard.setOpenBank(openBank);
                this.tpzUserBankCardDao.update(bankCard);
                return true;
            }
            throw new CustomerException("银行卡已经被其他账户绑定,请联系客服", 830405);
        }
        // 新增加用户的绑定银行卡信息
        Date createDatetime = new Date();
        // TODO 这里使用写死的渠道3
        TpzBankTypeEntity bankTypeEntity = this.tpzBankTypeDao.getBankInfo(bankNo, 3L);
        if (bankTypeEntity == null) {
            throw new CustomerException("银行信息获取失败,请联系客服", 830405);
        }
        TpzUserBankCardEntity userBankCardEntity = new TpzUserBankCardEntity();
        userBankCardEntity.setUserId(userId);
        userBankCardEntity.setBankNo(bankNo);
        userBankCardEntity.setBankCode("");
        userBankCardEntity.setBankName(bankTypeEntity.getBankName());
        userBankCardEntity.setBankCardNo(bankCardNo);
        userBankCardEntity.setBankCardName(userEntity.getUserRealName());
        userBankCardEntity.setIdcardKind(userEntity.getIdcardKind());
        userBankCardEntity.setIdcard(userEntity.getIdcard());
        userBankCardEntity.setProvince(province);
        userBankCardEntity.setCity(city);
        userBankCardEntity.setOpenBank(openBank);
        userBankCardEntity.setIsDefault(EUserBankDefault.YES.getCode());
        userBankCardEntity.setBankCardStatus(EBankCardStatus.YES.getCode());
        userBankCardEntity.setCurDatetime(createDatetime);
        userBankCardEntity.setCreateDatetime(createDatetime);
        userBankCardEntity.setBankAddress("");

        // 不删除原来的卡片，将原来的变为副卡就好了
        this.tpzUserBankCardDao.updateUserBank(userId);
        int insertId = this.tpzUserBankCardDao.save(userBankCardEntity);
        this.logger.info("绑定银行卡成功，返回ID " + insertId);
        return true;
    }


    /**
     * 获取银行卡信息
     * @param bankNo
     * @param channelId
     * @return
     */
    public TpzBankTypeEntity getBankInfoById(String bankNo, Long channelId) {
        return this.tpzBankTypeDao.getBankInfo(bankNo, channelId);
    }

    /**
     * 获取指定通道的银行卡列表
     * @param channelId
     * @return
     */
    @Override
    public List<TpzBankTypeEntity> queryBankList(Long channelId) {
        return this.tpzBankTypeDao.queryBankList(channelId);
    }
}
