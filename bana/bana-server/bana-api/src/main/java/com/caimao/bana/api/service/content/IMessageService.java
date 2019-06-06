package com.caimao.bana.api.service.content;

import com.caimao.bana.api.entity.TpzPushMsgContentEntity;
import com.caimao.bana.api.entity.req.F830904Req;
import com.caimao.bana.api.entity.req.content.FMsgQueryListReq;
import com.caimao.bana.api.entity.req.message.FPushMsgAddReq;
import com.caimao.bana.common.api.exception.CustomerException;

import java.util.List;


/**
 * 用户消息服务类
 * @author yanjg
 * 2015年4月13日
 */
public interface IMessageService {
    public F830904Req queryPushMsg(F830904Req f830904req);

    public TpzPushMsgContentEntity getPushMsgContent(Long long1) throws CustomerException;

    /**
     * 清空用户所有消息
     * @param userId
     * @throws Exception
     */
    public void cleanAll(Long userId) throws Exception;

    /**
     * 删除用户指定的消息
     * @param userId
     * @param pushMsgId
     * @throws Exception
     */
    public void del(Long userId, Long pushMsgId) throws Exception;

    /**
     * 设置消息为已读
     * @param pushMsgId
     * @throws Exception
     */
    public void doSetReadFlag(Long pushMsgId) throws Exception;

    /**
     * 获取未读的消息数量
     * @param userId
     * @return
     * @throws Exception
     */
    public Integer getNotReadNum(Long userId) throws Exception;

    /**
     * 获取未读的消息数量
     * @param userId
     * @param pushTypes
     * @return
     * @throws Exception
     */
    public Integer getNotReadNum(Long userId, List<String> pushTypes) throws Exception;

    /**
     * 变更所有消息为已读
     * @param userId
     * @return
     * @throws Exception
     */
    public void msgReadAll(Long userId) throws Exception;

    /**
     * 查询用户消息列表
     * @param req
     * @return
     * @throws Exception
     */
    public FMsgQueryListReq queryMsgList(FMsgQueryListReq req) throws Exception;


    /**
     * 添加用户消息
     * @param req
     * @throws Exception
     */
    public void addPushMsg(FPushMsgAddReq req) throws Exception;

}