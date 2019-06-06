/*
 * Timeloit.com Inc.
 * Copyright (c) 2012 时代凌宇物联网数据平台. All Rights Reserved
 */
package com.caimao.bana.server.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.utils.RedisUtils;
import com.hsnet.pz.core.exception.BizServiceException;

/**
 * SmsSendCount拦截器，所有被@smsSendCount注解的方法，都经过这个类进行处理。
 * @author yanjigang
 */
@Aspect
@Component
public class SmsSendCountInterceptor {
	Logger logger = LoggerFactory.getLogger(SmsSendCountInterceptor.class);
	//一个手机号每小时发送20条
	private int MOBILE_SMS_SEND_COUNT=20;
	private String TTL="3600";
	@Autowired
	RedisUtils redisUtils;

	/**
	 * 拦截方法
	 * @param pjp 连接点
	 * @param timecost注解配置
	 * @return 调用目标方法返回的对象
	 * @throws Throwable 
	 */
	@Around("@annotation(smsSendCount)")
	public Object checkSmsSendCount(ProceedingJoinPoint pjp, SmsSendCount smsSendCount) throws Throwable {
		if(checkTimeout(pjp.getArgs()[0].toString())){
		    Object target=pjp.proceed();
		    return target;
		}else{
		    throw new CustomerException("您发送的短信数已经超过限制，请稍后重试",84090);
		}
		
	}
	//防止 redis 异常
    public boolean checkTimeout(String mobile){
        String key = "caimao_mobile_sms_count_" +mobile;
        try {
            Long num = redisUtils.incr(0, key);
            Long ttl = redisUtils.getTtl(0, key);
            if(ttl == null || ttl < 0) {
                redisUtils.set(0,key, "" + num, Long.parseLong(TTL));
            }
                if (num > MOBILE_SMS_SEND_COUNT) {
                    return false;
                }
        } catch (Exception e) {
            logger.error("redis error :", e);
            try {
                redisUtils.del(0, key);
            } catch (Exception e1) {
                logger.error("redis error1 :", e);
            }
        }
        return true;
    }
}