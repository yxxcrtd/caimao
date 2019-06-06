package com.caimao.bana.server.service.guji;

import com.caimao.bana.api.entity.guji.*;
import com.caimao.bana.api.entity.req.guji.FQueryGujiFollowShareListReq;
import com.caimao.bana.api.entity.req.guji.FQueryGujiHallShareListReq;
import com.caimao.bana.api.exception.CustomerException;
import com.caimao.bana.api.service.guji.IGujiService;
import com.caimao.bana.server.dao.guji.*;
import com.caimao.bana.server.utils.guji.GujiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 股计接口服务
 * Created by Administrator on 2016/1/7.
 */
@Service("gujiService")
public class GujiServiceImpl implements IGujiService {

    private static final Logger logger = LoggerFactory.getLogger(GujiServiceImpl.class);

    @Autowired
    private GujiUserDAO gujiUserDAO;
    @Autowired
    private GujiFavourRecordDAO gujiFavourRecordDAO;
    @Autowired
    private GujiFocusRecordDAO gujiFocusRecordDAO;
    @Autowired
    private GujiShareRecordDAO gujiShareRecordDAO;
    @Autowired
    private GujiUserStockDAO gujiUserStockDAO;


    /**
     * 根据数据库中的ID获取微信用户信息
     * @param wxId
     * @return
     * @throws Exception
     */
    @Override
    public GujiUserEntity getUserById(Long wxId) throws Exception {
        return this.gujiUserDAO.selectByWxId(wxId);
    }

    /**
     * 根据微信唯一标示获取数据库中的用户信息
     * @param openId
     * @return
     * @throws Exception
     */
    @Override
    public GujiUserEntity getUserByOpenId(String openId) throws Exception {
        return this.gujiUserDAO.selectByOpenId(openId);
    }

    /**
     * 添加/更新微信用户个人信息
     * @param gujiUserEntity
     * @return
     * @throws Exception
     */
    @Override
    public Boolean addOrUpdateUserInfo(GujiUserEntity gujiUserEntity) throws Exception {
        if (gujiUserEntity.getOpenId() == null) {
            throw new CustomerException("更新/添加微信用户的个人信息请求参数错误", 888888);
        }
        GujiUserEntity userEntity = this.gujiUserDAO.selectByOpenId(gujiUserEntity.getOpenId());
        if (userEntity == null) {
            // 添加
            gujiUserEntity.setUpdateTime(new Date());
            gujiUserEntity.setCreateTime(new Date());
            this.gujiUserDAO.insert(gujiUserEntity);

            /** 添加用户的现金账户，默认是100%仓位
            GujiUserStockEntity userStockEntity = new GujiUserStockEntity();
            userStockEntity.setWxId(gujiUserEntity.getWxId());
            userStockEntity.setOpenId(gujiUserEntity.getOpenId());
            userStockEntity.setStockType(EGujiStockType.XJ.getCode());
            userStockEntity.setStockCode("000000");
            userStockEntity.setStockName("现金");
            userStockEntity.setStockPrice("0");
            userStockEntity.setTargetPrice("0");
            userStockEntity.setPositions(100);
            userStockEntity.setRemark("初始化现金账户");
            userStockEntity.setUpdateTime(new Date());
            userStockEntity.setCreateTime(new Date());
            this.gujiUserStockDAO.insert(userStockEntity); */

            /** 用户添加，默认加上自己的关注 */
            GujiFocusRecordEntity focusRecordEntity = new GujiFocusRecordEntity();
            focusRecordEntity.setWxId(gujiUserEntity.getWxId());
            focusRecordEntity.setOpenId(gujiUserEntity.getOpenId());
            focusRecordEntity.setFocusWxId(gujiUserEntity.getWxId());
            focusRecordEntity.setFocusOpenId(gujiUserEntity.getOpenId());
            focusRecordEntity.setCreateTime(new Date());
            this.gujiFocusRecordDAO.insert(focusRecordEntity);
        } else {
            // 更新
            gujiUserEntity.setWxId(userEntity.getWxId());
            gujiUserEntity.setUpdateTime(new Date());
            this.gujiUserDAO.update(gujiUserEntity);
        }
        return true;
    }

    /**
     * 添加股票推荐的信息
     *
     * @param shareRecordEntity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Boolean addShareStockInfo(GujiShareRecordEntity shareRecordEntity) throws Exception {
        this.gujiShareRecordDAO.insert(shareRecordEntity);
        GujiUserStockEntity userStockEntity = this.gujiUserStockDAO.selectByWxIdAndCode(shareRecordEntity.getWxId(), shareRecordEntity.getStockCode());
        // 新增
        if (userStockEntity == null) {
            userStockEntity = new GujiUserStockEntity();
            userStockEntity.setWxId(shareRecordEntity.getWxId());
            userStockEntity.setOpenId(shareRecordEntity.getOpenId());
            userStockEntity.setStockType(shareRecordEntity.getStockType());
            userStockEntity.setStockCode(shareRecordEntity.getStockCode());
            userStockEntity.setStockName(shareRecordEntity.getStockName());
            userStockEntity.setStockPrice(shareRecordEntity.getStockPrice());
            userStockEntity.setTargetPrice(shareRecordEntity.getTargetPrice());
            userStockEntity.setUpdateTime(new Date());
            userStockEntity.setCreateTime(new Date());
            this.gujiUserStockDAO.insert(userStockEntity);
        } else {
            // 更新
            GujiUserStockEntity updateUserStockEntity = new GujiUserStockEntity();
            updateUserStockEntity.setId(userStockEntity.getId());
            updateUserStockEntity.setStockPrice(shareRecordEntity.getStockPrice());
            updateUserStockEntity.setTargetPrice(shareRecordEntity.getTargetPrice());
            updateUserStockEntity.setUpdateTime(new Date());
            this.gujiUserStockDAO.update(updateUserStockEntity);
        }


//        // 先查询用户添加这个股票的原始仓位
//        Integer prePositions = 0;
//        GujiUserStockEntity userStockEntity = this.gujiUserStockDAO.selectByWxIdAndCode(shareRecordEntity.getWxId(), shareRecordEntity.getStockCode());
//        if (userStockEntity != null) {
//            prePositions = userStockEntity.getPositions();
//        }
//        // 更新现金仓位的对象
//        GujiUserStockEntity updateUserXJStock = null;
//        GujiUserStockEntity xjStockEntity = this.gujiUserStockDAO.selectByWxIdAndCode(shareRecordEntity.getWxId(), "000000");
//
//        shareRecordEntity.setPrePositions(prePositions);
//        shareRecordEntity.setCreateTime(new Date());
//        shareRecordEntity.setIsPublic(1);
//        shareRecordEntity.setFavour(0);
//        this.gujiShareRecordDAO.insert(shareRecordEntity);
//        if (userStockEntity == null) {
//            // 之前没有仓位，那就添加一下这个东西
//            userStockEntity = new GujiUserStockEntity();
//            userStockEntity.setWxId(shareRecordEntity.getWxId());
//            userStockEntity.setOpenId(shareRecordEntity.getOpenId());
//            userStockEntity.setStockType(shareRecordEntity.getStockType());
//            userStockEntity.setStockCode(shareRecordEntity.getStockCode());
//            userStockEntity.setStockName(shareRecordEntity.getStockName());
//            userStockEntity.setStockPrice(shareRecordEntity.getStockPrice());
//            userStockEntity.setTargetPrice(shareRecordEntity.getTargetPrice());
//            userStockEntity.setPositions(shareRecordEntity.getPositions());
//            userStockEntity.setRemark("");
//            userStockEntity.setUpdateTime(new Date());
//            userStockEntity.setCreateTime(new Date());
//            this.gujiUserStockDAO.insert(userStockEntity);
//            // 如果更新的是股票，那就更新现金的仓位
//            if (shareRecordEntity.getStockType().equals(EGujiStockType.GP.getCode())) {
//                updateUserXJStock = new GujiUserStockEntity();
//                updateUserXJStock.setId(xjStockEntity.getId());
//                updateUserXJStock.setPositions(xjStockEntity.getPositions() - shareRecordEntity.getPositions());
//            }
//        } else {
//            // 有这个仓位，那就更新一下这个东西
//            GujiUserStockEntity updateUserStockEntity = new GujiUserStockEntity();
//            updateUserStockEntity.setId(userStockEntity.getId());
//            updateUserStockEntity.setStockPrice(shareRecordEntity.getStockPrice());
//            updateUserStockEntity.setTargetPrice(shareRecordEntity.getTargetPrice());
//            updateUserStockEntity.setPositions(shareRecordEntity.getPositions());
//            updateUserStockEntity.setUpdateTime(new Date());
//            this.gujiUserStockDAO.update(updateUserStockEntity);
//            // 如果更新的是股票的话，那就需要更新一下现金仓位
//            if (shareRecordEntity.getStockType().equals(EGujiStockType.GP.getCode())) {
//                if (shareRecordEntity.getPositions() > prePositions) {
//                    // 加仓，现金减去对应的仓位
//                    updateUserXJStock = new GujiUserStockEntity();
//                    updateUserXJStock.setId(xjStockEntity.getId());
//                    updateUserXJStock.setPositions(xjStockEntity.getPositions() - (shareRecordEntity.getPositions() - prePositions));
//                } else if (shareRecordEntity.getPositions() < prePositions) {
//                    // 减仓，现金加上对应的仓位
//                    updateUserXJStock = new GujiUserStockEntity();
//                    updateUserXJStock.setId(xjStockEntity.getId());
//                    updateUserXJStock.setPositions(xjStockEntity.getPositions() + (prePositions - shareRecordEntity.getPositions()));
//                }
//            }
//        }
//        if (updateUserXJStock != null) {
//            this.gujiUserStockDAO.update(updateUserXJStock);
//        }
        return true;
    }

    /**
     * 查询大厅中用户的股票推荐列表
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FQueryGujiHallShareListReq queryHallShareList(FQueryGujiHallShareListReq req) throws Exception {
        List<GujiShareRecordEntity> shareRecordEntityList = this.gujiShareRecordDAO.selectHallListWithPage(req);
        if (shareRecordEntityList != null) {
            if (req.getCheckOpenId() != null) {
                for (GujiShareRecordEntity entity: shareRecordEntityList) {
                    // 是否关注、是否点赞、发布格式化时间
                    this.formatShareRecord(entity, req.getCheckOpenId());
                }
            }
        }

        req.setItems(shareRecordEntityList);
        return req;
    }

    /**
     * 查询我关注用户的股票推荐列表
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FQueryGujiFollowShareListReq queryFollowShareList(FQueryGujiFollowShareListReq req) throws Exception {
        List<GujiShareRecordEntity> shareRecordEntityList = this.gujiShareRecordDAO.selectMyFollowListWithPage(req);
        if (shareRecordEntityList != null) {
            for (GujiShareRecordEntity entity : shareRecordEntityList) {
                // 是否关注、是否点赞、发布格式化时间
                this.formatShareRecord(entity, req.getOpenId());
            }
        }
        req.setItems(shareRecordEntityList);
        return req;
    }

    /**
     * 根据Id查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public GujiUserStockEntity findById(Long id) throws Exception {
        return gujiUserStockDAO.findById(id);
    }

    /**
     * 获取我的股票持仓列表
     * @param wxId
     * @return
     * @throws Exception
     */
    @Override
    public List<GujiUserStockEntity> getMyStockList(Long wxId) throws Exception {
        List<GujiUserStockEntity> userStockEntityList = this.gujiUserStockDAO.selectByWxId(wxId);
        for (GujiUserStockEntity stockEntity: userStockEntityList) {
            stockEntity.setUpdateStr(GujiUtils.formatDateTime(stockEntity.getUpdateTime()));
        }
        return userStockEntityList;
    }

    /**
     * 查询特定用户推荐的股票历史列表
     * @param wxId
     * @param stockCode
     * @param checkOpenId
     * @return
     * @throws Exception
     */
    @Override
    public List<GujiShareRecordEntity> queryStockShareHistoryList(Long wxId, String stockCode, String checkOpenId) throws Exception {
        List<GujiShareRecordEntity> shareRecordEntityList = this.gujiShareRecordDAO.selectByWxIdAndCode(wxId, stockCode);
        if (shareRecordEntityList != null  && checkOpenId != null) {
            for (GujiShareRecordEntity entity: shareRecordEntityList) {
                this.formatShareRecord(entity, checkOpenId);
            }
        }
        return shareRecordEntityList;
    }

    /**
     * 检查用户之间是否关注关系
     * @param openId    要关注比人的人
     * @param focusWxId   他要关注的人
     * @return
     * @throws Exception
     */
    @Override
    public Boolean checkIsFollow(String openId, Long focusWxId) throws Exception {
        GujiFocusRecordEntity isFollow = this.gujiFocusRecordDAO.selectByWxIdAndFocusWxId(openId, focusWxId);
        return isFollow != null;
    }

    /**
     * 添加一个关注
     * @param openId
     * @param focusWxId
     * @return
     * @throws Exception
     */
    @Override
    public Boolean addFollow(String openId, Long focusWxId) throws Exception {
        GujiUserEntity userEntity = this.getUserByOpenId(openId);
        GujiUserEntity focusUserEntity = this.getUserById(focusWxId);
        GujiFocusRecordEntity focusRecordEntity = new GujiFocusRecordEntity();
        focusRecordEntity.setWxId(userEntity != null ? userEntity.getWxId() : 0L);
        focusRecordEntity.setOpenId(openId);
        focusRecordEntity.setFocusWxId(focusWxId);
        focusRecordEntity.setFocusOpenId(focusUserEntity != null ? focusUserEntity.getOpenId() : "");
        focusRecordEntity.setCreateTime(new Date());
        this.gujiFocusRecordDAO.insert(focusRecordEntity);
        return null;
    }

    /**
     * 取消一个关注
     * @param openId
     * @param focusWxId
     * @return
     * @throws Exception
     */
    @Override
    public Boolean delFollow(String openId, Long focusWxId) throws Exception {
        this.gujiFocusRecordDAO.delete(openId, focusWxId);
        return true;
    }

    /**
     * 我给你一个赞 ！
     * @param openId    给赞的人
     * @param shareId   给哪个推荐了
     * @return
     * @throws Exception
     */
    @Override
    public Boolean giveLike(String openId, Long shareId) throws Exception {
        GujiFavourRecordEntity favourRecordEntity = this.gujiFavourRecordDAO.selectByShareIdAndOpenId(shareId, openId);
        if (favourRecordEntity == null) {
            favourRecordEntity = new GujiFavourRecordEntity();
            favourRecordEntity.setShareId(shareId);
            favourRecordEntity.setOpenId(openId);
            favourRecordEntity.setCreateTime(new Date());
            this.gujiFavourRecordDAO.insert(favourRecordEntity);
            this.gujiShareRecordDAO.addLikeNum(shareId);
        }
        return null;
    }

    /**
     * 格式化那个东东
     * @param entity
     * @param openId
     * @return
     */
    private GujiShareRecordEntity formatShareRecord(GujiShareRecordEntity entity, String openId) {
        // 是否关注、是否点赞、发布格式化时间
        GujiFocusRecordEntity isFollow = this.gujiFocusRecordDAO.selectByWxIdAndFocusWxId(openId, entity.getWxId());
        entity.setIsFollow(isFollow == null ? 0 : 1);
        GujiFavourRecordEntity isLike = this.gujiFavourRecordDAO.selectByShareIdAndOpenId(entity.getShareId(), openId);
        entity.setIsLike(isLike == null ? 0 : 1);
        entity.setPublishTimeStr(GujiUtils.formatDateTime(entity.getCreateTime()));
        return entity;
    }

    /**
     * findByKey
     *
     * @param key
     * @return
     */
    @Override
    public GujiConfigEntity findByKey(String key) {
        return gujiUserDAO.findByKey(key);
    }

    /**
     * 我关注的人
     *
     * @param openId
     * @return
     */
    @Override
    public List<GujiFocusRecordEntity> myFocus(String openId) {
        return gujiFocusRecordDAO.myFocus(openId);
    }

    /**
     * 关注我的人
     *
     * @param focusOpenId
     * @return
     */
    @Override
    public List<GujiFocusRecordEntity> focusMe(String focusOpenId) {
        return gujiFocusRecordDAO.focusMe(focusOpenId);
    }

}
