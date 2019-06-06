package com.caimao.bana.api.service;

/**
 * 用户实名认证相关方法
 * Created by WangXu on 2015/5/18.
 */
public interface IUserTrustService {

    /**
     * 用户实名认证的方法
     * @param userId    用户ID
     * @param realName  真实姓名
     * @param idCardKind    认证卡类型
     * @param idCard    卡号
     * @return  实名认证后ID
     */
    public Long doVerifyIdentity(Long userId, String realName, String idCardKind, String idCard) throws Exception;
}
