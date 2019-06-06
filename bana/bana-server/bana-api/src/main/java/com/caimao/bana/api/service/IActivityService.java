package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.activity.BanaActivityRecordEntity;

import java.util.List;

/**
 * 相关活动接口
 * Created by WangXu on 2015/5/22.
 */
public interface IActivityService {

    /**
     * 保存微信融资额
     * @param phone 手机号
     * @param pzValue   融资额
     * @param userIP    用户IP
     * @return  成功返回 1 失败返回 0
     */
    public int saveWeixinPaoKu(String phone, String pzValue, String userIP);

    /**
     * 获取是否可以抽取红包
     * @param userId 用户ID
     * @return 是否可以抽取
     * @throws Exception
     */
    public Boolean getShowRedPackage(Long userId) throws Exception;

    /**
     * 开启红包
     * @param userId 用户ID
     * @return 金额
     * @throws Exception
     */
    public Long openRedPackage(Long userId) throws Exception;

    /**
     * 获取用户参加的活动记录
     * @param userId
     * @param actId
     * @return
     * @throws Exception
     */
    public List<BanaActivityRecordEntity> getUserActivityRecord(Long userId, Integer actId) throws Exception;

    /**
     * 添加用户的活动记录
     * @param entity
     * @throws Exception
     */
    public void addActivityRecord(BanaActivityRecordEntity entity) throws Exception;

    /**
     * 二维码活动的那个啥
     * @throws Exception
     */
    public void jobsEWMActivity() throws Exception;
}
