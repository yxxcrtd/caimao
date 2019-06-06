package com.caimao.bana.api.service.ybk;

import com.caimao.bana.api.entity.req.ybk.FYBKQueryAccountListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkApiQueryAccountListReq;
import com.caimao.bana.api.entity.req.ybk.FYbkNewAccountListReq;
import com.caimao.bana.api.entity.ybk.YBKAccountEntity;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;

import java.util.List;

/**
 * 邮币卡账号
 */
public interface IYBKAccountService {

    /**
     * 分页获取邮币卡账户列表
     * @param req
     * @return
     * @throws Exception
     */
    public FYBKQueryAccountListReq queryAccountWithPage(FYBKQueryAccountListReq req)throws Exception ;

    /**
     * 更新邮币卡账户信息
     * @param entity 实体类
     * @throws Exception
     */
    public void update(YBKAccountEntity entity)throws Exception ;

    /**
     * 新增邮币卡账户信息
     * @param entity 实体类
     * @throws Exception
     */
    public void insert(YBKAccountEntity entity)throws Exception ;

    /**
     * 根据id删除邮币卡账户信息
     * @param id 邮币卡账户id
     * @throws Exception
     */
    public void delete(Integer id)throws Exception ;

    /**
     * 根据id查询邮币卡账户信息
     * @param id 邮币卡账户id
     * @return
     * @throws Exception
     */
    public YBKAccountEntity queryById(Long id)throws Exception ;

    /**
     * 根据用户id获取个人开户信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public List<YBKAccountEntity> queryByUserId(Long userId) throws Exception;

    /**
     * 查询用户开户申请列表
     * @param req
     * @throws Exception
     */
    public FYbkApiQueryAccountListReq queryApiAccountApply(FYbkApiQueryAccountListReq req) throws Exception;

    /**
     * 查询这个用户还可以开通那些交易所
     * @param userId
     * @return
     * @throws Exception
     */
    public List<YbkExchangeEntity> mayOpenExchange(Long userId) throws Exception;

    /**
     * 一步开户
     * @param userId
     * @param exchangeShortCode
     * @return
     * @throws Exception
     */
    public YBKAccountEntity oneStepOpenExchange(Long userId, String exchangeShortCode) throws Exception;

    /**
     * 查询最新的用户开户申请列表
     * @param req
     * @throws Exception
     */
    public FYbkNewAccountListReq queryNewAccountApply(FYbkNewAccountListReq req) throws Exception;


}