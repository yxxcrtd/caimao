package com.caimao.bana.api.service;


/**
 * 用户邀请服务类
 *
 * @author yanjg
 *         2015年4月13日
 */
public interface UserInviteService {
    /**
     * @param userId
     * @param phone
     * @throws Exception
     */
    public void createUserInvite(Long userId, String countryCode,String phone) throws Exception;
}