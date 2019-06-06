package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.TpzSmsOutEntity;
import com.caimao.bana.api.entity.req.FSmsListReq;

/**
 * 发送短信相关
 *
 * @author yanjg
 * 2015年4月13日
 */
public interface SmsService {

    /**
     * 发送短信码
     */
    public void doSendSmsCheckCode(String mobile,String smsBizType)throws Exception;

    /**
     * 查询已发短信但未注册的用户
     *
     * @param req
     * @return
     * @throws Exception
     */
    FSmsListReq queryUnRegisterMobileWithPage(FSmsListReq req) throws Exception;

    /**
     * 发送短信普通
     * @param mobile 手机号码
     * @param content 内容
     * @throws Exception
     */
    public void doSendSmsCustom(String mobile,String content) throws Exception;
}