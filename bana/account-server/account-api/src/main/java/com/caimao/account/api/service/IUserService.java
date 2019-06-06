package com.caimao.account.api.service;

/**
 * 用户服务类接口
 * 2015年4月13日
 */
public interface IUserService {

    /**
     * 根据手机号，查找用户的user_id
     *
     * @param phone 手机号
     * @return 返回用户的USER_ID
     */
    public Long getUserIdByPhone(String phone) throws Exception;

}