package com.caimao.bana.api.service.guji;

import com.caimao.bana.api.entity.guji.*;
import com.caimao.bana.api.entity.req.guji.FQueryGujiFollowShareListReq;
import com.caimao.bana.api.entity.req.guji.FQueryGujiHallShareListReq;

import java.util.List;

/**
 * 股计服务接口
 * Created by Administrator on 2016/1/7.
 */
public interface IGujiService {

    /**
     * 根据数据库中的ID获取微信用户信息
     * @param wxId
     * @return
     * @throws Exception
     */
    public GujiUserEntity getUserById(Long wxId) throws Exception;

    /**
     * 根据微信唯一标示获取数据库中的用户信息
     * @param openId
     * @return
     * @throws Exception
     */
    public GujiUserEntity getUserByOpenId(String openId) throws Exception;

    /**
     * 添加/更新微信用户个人信息
     * @param gujiUserEntity
     * @return
     * @throws Exception
     */
    public Boolean addOrUpdateUserInfo(GujiUserEntity gujiUserEntity) throws Exception;

    /**
     * 添加股票推荐的信息
     * @param shareRecordEntity
     * @return
     * @throws Exception
     */
    public Boolean addShareStockInfo(GujiShareRecordEntity shareRecordEntity) throws Exception;

    /**
     * 查询大厅中用户的股票推荐列表
     * @param req
     * @return
     * @throws Exception
     */
    public FQueryGujiHallShareListReq queryHallShareList(FQueryGujiHallShareListReq req) throws Exception;

    /**
     * 查询我关注用户的股票推荐列表
     * @param req
     * @return
     * @throws Exception
     */
    public FQueryGujiFollowShareListReq queryFollowShareList(FQueryGujiFollowShareListReq req) throws Exception;

    /**
     * 根据Id查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    GujiUserStockEntity findById(Long id) throws Exception;

    /**
     * 获取我的股票持仓列表
     * @param wxId
     * @return
     * @throws Exception
     */
    public List<GujiUserStockEntity> getMyStockList(Long wxId) throws Exception;

    /**
     * 查询特定用户推荐的股票历史列表
     * @param wxId
     * @param stockCode
     * @param checkOpenId
     * @return
     * @throws Exception
     */
    public List<GujiShareRecordEntity> queryStockShareHistoryList(Long wxId, String stockCode, String checkOpenId) throws Exception;

    /**
     * 检查用户之间是否关注关系
     * @param openId    要关注比人的人
     * @param focusWxId   他要关注的人
     * @return
     * @throws Exception
     */
    public Boolean checkIsFollow(String openId, Long focusWxId) throws Exception;

    /**
     * 添加一个关注
     * @param openId
     * @param focusWxId
     * @return
     * @throws Exception
     */
    public Boolean addFollow(String openId, Long focusWxId) throws Exception;

    /**
     * 取消一个关注
     * @param openId
     * @param focusWxId
     * @return
     * @throws Exception
     */
    public Boolean delFollow(String openId, Long focusWxId) throws Exception;

    /**
     * 我给你一个赞 ！
     * @param openId    给赞的人
     * @param shareId   给哪个推荐了
     * @return
     * @throws Exception
     */
    public Boolean giveLike(String openId, Long shareId) throws Exception;

    /**
     * findByKey
     *
     * @param key
     * @return
     */
    GujiConfigEntity findByKey(String key);

    /**
     * 我关注的人
     *
     * @param openId
     * @return
     */
    List<GujiFocusRecordEntity> myFocus(String openId);

    /**
     * 关注我的人
     *
     * @param focusOpenId
     * @return
     */
    List<GujiFocusRecordEntity> focusMe(String focusOpenId);

}
