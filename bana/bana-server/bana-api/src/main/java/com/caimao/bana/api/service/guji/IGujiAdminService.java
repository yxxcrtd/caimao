package com.caimao.bana.api.service.guji;

import com.caimao.bana.api.entity.guji.GujiConfigEntity;
import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.entity.req.guji.FQueryAdminShareListReq;
import com.caimao.bana.api.entity.req.guji.FQueryAdminUserListReq;

import java.util.List;

/**
 * 股计后台相关服务接口
 * Created by Administrator on 2016/1/14.
 */
public interface IGujiAdminService {

    /**
     * 查询用户列表
     * @param req
     * @return
     * @throws Exception
     */
    public FQueryAdminUserListReq queryUserList(FQueryAdminUserListReq req) throws Exception;

    /**
     * 查询用户推荐股票列表
     * @param req
     * @return
     * @throws Exception
     */
    public FQueryAdminShareListReq queryShareList(FQueryAdminShareListReq req) throws Exception;

    /**
     * 更新认证用户状态
     * @param wxId
     * @param authStatus
     * @throws Exception
     */
    public void authUserStatus(Long wxId, Integer authStatus) throws Exception;

    /**
     * 删除推荐文章
     * @param id
     * @throws Exception
     */
    public void delShare(Long id) throws Exception;

    /**
     * 更新推荐分享是否公开
     * @param shareId
     * @param isPublic
     * @throws Exception
     */
    public void updateShareIsPublic(Long shareId, Integer isPublic) throws Exception;

    /**
     * 根据主键查询
     * @param wxId
     * @throws Exception
     */
    public GujiUserEntity selectByWxId(Long wxId);

    /**
     * 更新股计
     * @param entity
     * @throws Exception
     */
    public Integer update(GujiUserEntity entity);

    /**
     * 配置列表
     *
     * @return
     */
    List<GujiConfigEntity> queryAdminConfigList();

    /**
     * 开关设置
     *
     * @param id
     * @param value
     * @return
     */
    String configSet(Long id, String value);

}
