/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.dao.sms;

import com.caimao.bana.api.entity.TpzSmsOutEntity;
import com.caimao.bana.api.entity.req.FSmsListReq;
import com.caimao.bana.api.entity.res.FSmsListRes;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanjg
 * 2015年4月28日
 */
@Repository
public class TpzSmsOutDao extends SqlSessionDaoSupport {
    @Autowired
    private MemoryDbidGenerator dbidGenerator;

    /**
     * @param so
     */
    public void save(TpzSmsOutEntity so) {
        so.setId(dbidGenerator.getNextId());
        getSqlSession().insert("TpzSmsOut.save", so);
    }

    /**
     * 获取指定号码，在指定时间后发送的短信条数
     * @param mobile    手机号
     * @param smsType   验证码类型
     * @param beginTime 开始时间
     * @return
     */
    public Integer getSmsCount(String mobile, String smsType, Date beginTime) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("mobile", mobile);
        paramsMap.put("smsType", smsType);
        paramsMap.put("beginTime", beginTime);
        return getSqlSession().selectOne("TpzSmsOut.getSmsCount", paramsMap);
    }

    /**
     * 查询已发短信但未注册的用户
     *
     * @param req
     * @return
     */
    public List<FSmsListRes> queryUnRegisterMobileWithPage(FSmsListReq req) {
        return getSqlSession().selectList("TpzSmsOut.queryUnRegisterMobileWithPage", req);
    }

}
