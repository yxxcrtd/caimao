package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.InviteInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户返佣服务
 */
public interface InviteInfoService {
    /**
     * 更新用户返佣信息任务
     * @throws Exception
     */
    public void updateInviteInfoTask() throws Exception;

    /**
     * 获取用户返佣信息记录
     * @param userId 用户userId
     * @return
     * @throws Exception
     */
    public InviteInfoEntity getInviteInfo(Long userId) throws Exception;

    /**
     * 获取用户返佣排行
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getBrokerageRankCnt() throws Exception;

    /**
     * 获取用户返佣排行
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getBrokerageRankMoney() throws Exception;

    /**
     * 获取用户融资总额度
     * @param userId 用户userId
     * @return
     * @throws Exception
     */
    public Long getUserPzAmount(Long userId) throws Exception;
}
