package com.caimao.bana.server.service.message;

import com.caimao.bana.api.entity.TpzPushMsgContentEntity;
import com.caimao.bana.api.entity.content.TpzPushMsgEntity;
import com.caimao.bana.api.entity.req.F830904Req;
import com.caimao.bana.api.entity.req.content.FMsgQueryListReq;
import com.caimao.bana.api.entity.req.message.FPushMsgAddReq;
import com.caimao.bana.api.enums.EIsRead;
import com.caimao.bana.api.enums.EPushType;
import com.caimao.bana.api.service.content.IMessageService;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.dao.message.TpzPushMsgContentDao;
import com.caimao.bana.server.dao.message.TpzPushMsgDao;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("messageService")
public class MessageServiceImpl implements IMessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    @Autowired
    private TpzPushMsgDao pushMsgDAO;
    @Autowired
    private TpzPushMsgContentDao pushMsgContentDAO;

    @Autowired
    private MemoryDbidGenerator memoryDbidGenerator;

    /**
     * 清空用户所有消息
     * @param userId
     * @throws Exception
     */
    @Override
    public void cleanAll(Long userId) throws Exception {
        this.pushMsgDAO.clearMsg(userId);
    }

    /**
     * 删除用户指定的消息
     * @param userId
     * @param pushMsgId
     * @throws Exception
     */
    @Override
    public void del(Long userId, Long pushMsgId) throws Exception {
        this.pushMsgDAO.del(userId, pushMsgId);
    }

    /*
         * 查询消息列表
         */
    @Override
    public F830904Req queryPushMsg(F830904Req req) {
        List list = pushMsgDAO.queryF830904ResWithPage(req);
        req.setItems(list);
        return req;
    }

    /*
     * 获取消息详情
     */
    @Override
    public TpzPushMsgContentEntity getPushMsgContent(Long pushMsgId) throws CustomerException {
        if (pushMsgId == null) {
            throw new CustomerException("主推消息ID不能为空", 830905, "BizServiceException");
        }
        TpzPushMsgContentEntity content = pushMsgContentDAO.getById(pushMsgId);
        if (content != null)
            return content;
        else
            return null;
    }

    /*
     * 
     * 将消息设置为已读
     */
    @Override
    public void doSetReadFlag(Long pushMsgId) throws Exception {
        if (pushMsgId == null){
            throw new CustomerException("主推消息ID不能为空", 830906, "BizServiceException");
        }
        TpzPushMsgEntity pushMsg = this.pushMsgDAO.getById(pushMsgId);
        if (pushMsg == null){
            throw new CustomerException("不存在此消息！", 830906, "BizServiceException");
        }
        TpzPushMsgEntity update = new TpzPushMsgEntity();
        update.setIsRead(EIsRead.READ.getCode());
        update.setPushMsgId(pushMsgId);
        this.pushMsgDAO.update(update);

    }

    // 获取未读的消息数量
    @Override
    public FMsgQueryListReq queryMsgList(FMsgQueryListReq req) throws Exception {
        List<TpzPushMsgEntity> entityList = this.pushMsgDAO.queryMsgListWithPage(req);
        if(entityList != null && entityList.size() > 0){
            for (TpzPushMsgEntity entity:entityList){
                EPushType ePushType = EPushType.findByCode(entity.getPushType());
                entity.setTypeStr(ePushType != null?ePushType.getValue():"");
            }
        }
        req.setItems(entityList);
        return req;
    }

    //变更所有消息为已读
    @Override
    public void msgReadAll(Long userId) throws Exception {
        this.pushMsgDAO.msgReadAll(userId);
    }

    // 查询用户消息列表
    @Override
    public Integer getNotReadNum(Long userId) throws Exception {
        return this.pushMsgDAO.getNotReadNum(userId);
    }

    //查询用户消息列表
    @Override
    public Integer getNotReadNum(Long userId, List<String> pushTypes) throws Exception {
        return this.pushMsgDAO.getNotReadNum(userId, pushTypes);
    }

    /**
     * 添加用户消息
     * @param req
     * @throws Exception
     */
    @Override
    public void addPushMsg(FPushMsgAddReq req) throws Exception {
        TpzPushMsgEntity pushMsgEntity = new TpzPushMsgEntity();
        pushMsgEntity.setPushMsgId(this.memoryDbidGenerator.getNextId());
        pushMsgEntity.setPushModel(req.getPushModel());
        pushMsgEntity.setPushType(req.getPushType());
        pushMsgEntity.setPushMsgKind(req.getPushMsgKind());
        pushMsgEntity.setPushMsgTitle(req.getPushMsgTitle());
        pushMsgEntity.setPushMsgDigest(req.getPushMsgDigest());
        pushMsgEntity.setPushExtend(req.getPushExtend());
        pushMsgEntity.setPushUserId(req.getPushUserId());
        pushMsgEntity.setCreateDatetime(new Date());
        pushMsgEntity.setIsRead(req.getIsRead());

        // 存
        this.pushMsgDAO.save(pushMsgEntity);

        TpzPushMsgContentEntity pushMsgContentEntity = new TpzPushMsgContentEntity();
        pushMsgContentEntity.setPushMsgId(pushMsgEntity.getPushMsgId());
        pushMsgContentEntity.setMsgContent(req.getPushMsgContent());

        // 存
        this.pushMsgContentDAO.save(pushMsgContentEntity);
    }
}
