package com.caimao.bana.server.dao.depositDao;

import com.caimao.bana.api.entity.TpzPayChannelEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 充值通道管理
 * Created by WangXu on 2015/4/22.
 */
@Repository
public class TpzPayChannelDao extends SqlSessionDaoSupport {

    /**
     * 根据充值渠道ID获取充值qud
     * @param channelId
     * @return
     */
    public TpzPayChannelEntity getPayById(Long channelId) {
        return getSqlSession().selectOne("TpzPayChannel.getPayById", channelId);
    }
}
