package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.TpzBankTypeEntity;
import com.caimao.bana.api.entity.TpzUserBankCardEntity;

import java.util.List;

/**
 * 银行卡相关操作
 * Created by WangXu on 2015/5/7.
 */
public interface IUserBankCardService {

    /**
     * 查询用户绑定银行卡列表信息
     * @param userId    用户ID
     * @param bankCardStatus    状态
     * @return  绑定银行卡列表
     */
    public List<TpzUserBankCardEntity> queryUserBankList(Long userId, String bankCardStatus);

    /**
     * 执行绑定银行卡
     * @param userId    用户ID
     * @param bankNo    银行代码
     * @param bankCardNo    银行卡号
     * @param province  省份
     * @param city  城市
     * @param openBank  开户行
     * @return  成功 or 失败
     */
    public boolean doBindBankCard(Long userId, String bankNo, String bankCardNo, String province, String city, String openBank) throws Exception;

    /**
     * 获取指定通道的银行卡列表
     * @param channelId
     * @return
     */
    public List<TpzBankTypeEntity> queryBankList(Long channelId);

    /**
     * 获取银行卡信息
     * @param bankNo
     * @param channelId
     * @return
     */
    public TpzBankTypeEntity getBankInfoById(String bankNo, Long channelId);

}
