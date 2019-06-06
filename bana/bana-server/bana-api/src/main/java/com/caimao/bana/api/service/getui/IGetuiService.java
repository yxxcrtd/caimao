package com.caimao.bana.api.service.getui;

import com.caimao.bana.api.entity.getui.GetuiPushMessageEntity;
import com.caimao.bana.api.entity.getui.GetuiUserIdMapEntity;
import com.caimao.bana.api.entity.req.getui.FGetuiPushMessageReq;

/**
 * 个推服务接口
 * Created by Administrator on 2015/11/4.
 */
public interface IGetuiService {

    /**
     * 绑定客户端与用户之间的关联关系
     * @param entity
     * @return
     */
    public Boolean bindCidAndUserId(GetuiUserIdMapEntity entity);

    /**
     * 解绑客户端与用户之间的关连关系
     * @param entity
     * @return
     */
    public Boolean unbindCidAndUserId(GetuiUserIdMapEntity entity);

    /**
     * 添加单个用户的推送消息
     * @param req
     * @return
     */
    public Boolean pushMessageToSingle(FGetuiPushMessageReq req);

    /**
     * 添加app托送消息
     * @param req
     * @return
     */
    public Boolean pushMessageToApp(FGetuiPushMessageReq req);
}
