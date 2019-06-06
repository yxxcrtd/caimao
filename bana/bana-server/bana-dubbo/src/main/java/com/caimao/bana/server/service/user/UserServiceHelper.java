/*
 * Timeloit.com Inc.
 * Copyright (c) 2012 时代凌宇物联网数据平台. All Rights Reserved
 */
package com.caimao.bana.server.service.user;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.TpzUserTradeEntity;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.dao.userDao.TpzUserTradeDao;
import com.caimao.bana.server.utils.MD5Util;

/**
 * @author yanjg 2015年3月24日
 */
@Service("userServiceHelper")
public class UserServiceHelper {
    @Autowired
    private TpzUserDao tpzUserDao;
    @Autowired
    private TpzUserTradeDao tpzUserTradeDao;
    /**
     * 验证用户的安全密码
     * @param userId
     * @param tradePwd
     * @throws CustomerException 
     */
    public void validateUserTradePwd(Long userId, String tradePwd) throws CustomerException {
        if (null == userId){
            throw new CustomerException("用户ID不能为空。", 83000910, "BizServiceException");
        }
        if (StringUtils.isBlank(tradePwd)){
            throw new CustomerException("安全密码不能为空。", 83000910, "BizServiceException");
        }
        TpzUserEntity user = tpzUserDao.getById(userId);
        if (null == user){
            throw new CustomerException("此用户不存在。", 83000910, "BizServiceException");
        }
        TpzUserTradeEntity trade = (TpzUserTradeEntity) tpzUserTradeDao.getById(userId);
        if (null != trade && !trade.getUserTradePwd().equals(MD5Util.md5(tradePwd))) {
            tpzUserTradeDao.updateErrorCountById(userId);
            throw new CustomerException("安全密码错误。",83000910,"BizServiceException");
        }
        if (null == trade){
            throw new CustomerException("您还未设置安全密码，请先设置。",83000910,"BizServiceException");
        }else
            return;
    }
    /**
     * @param userId
     * @return
     * @throws CustomerException 
     */
    public TpzUserEntity getUser(Long userId) throws CustomerException {
        if (null == userId) {
            throw new CustomerException("用户ID不能为空",830018,"BizServiceException");
        }
        return this.tpzUserDao.getById(userId);
    }
}
