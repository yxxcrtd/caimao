package com.caimao.bana.server.service.guji;

import com.caimao.bana.api.entity.guji.GujiConfigEntity;
import com.caimao.bana.api.entity.guji.GujiShareRecordEntity;
import com.caimao.bana.api.entity.guji.GujiUserEntity;
import com.caimao.bana.api.entity.guji.GujiUserStockEntity;
import com.caimao.bana.api.entity.req.guji.FQueryAdminShareListReq;
import com.caimao.bana.api.entity.req.guji.FQueryAdminUserListReq;
import com.caimao.bana.api.enums.guji.EGujiAuthStatus;
import com.caimao.bana.api.enums.guji.EGujiStockType;
import com.caimao.bana.api.exception.CustomerException;
import com.caimao.bana.api.service.guji.IGujiAdminService;
import com.caimao.bana.server.dao.guji.GujiShareRecordDAO;
import com.caimao.bana.server.dao.guji.GujiUserDAO;
import com.caimao.bana.server.dao.guji.GujiUserStockDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 股计后台服务接口实现
 * Created by Administrator on 2016/1/14.
 */
@Service("gujiAdminService")
public class GujiAdminServiceImpl implements IGujiAdminService {

    @Autowired
    private GujiUserDAO gujiUserDAO;
    @Autowired
    private GujiShareRecordDAO gujiShareRecordDAO;
    @Autowired
    private GujiUserStockDAO gujiUserStockDAO;

    /**
     * 查询用户列表
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FQueryAdminUserListReq queryUserList(FQueryAdminUserListReq req) throws Exception {
        List<GujiUserEntity> userEntityList = this.gujiUserDAO.queryAdminUserListWithPage(req);
        req.setItems(userEntityList);
        return req;
    }

    /**
     * 查询用户推荐股票列表
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FQueryAdminShareListReq queryShareList(FQueryAdminShareListReq req) throws Exception {
        List<GujiShareRecordEntity> shareRecordEntityList = this.gujiShareRecordDAO.queryAdminShareListWithPage(req);
        req.setItems(shareRecordEntityList);
        return req;
    }

    /**
     * 更新认证用户状态
     * @param wxId
     * @param authStatus
     * @throws Exception
     */
    @Override
    public void authUserStatus(Long wxId, Integer authStatus) throws Exception {
        GujiUserEntity userEntity = this.gujiUserDAO.selectByWxId(wxId);
        if (authStatus.equals(EGujiAuthStatus.OK.getCode())) {
            if (userEntity.getCertificationAuth() == null || userEntity.getCertificationAuth().equals("")
                    || userEntity.getCardPic() == null || userEntity.getCardPic().equals("")) {
                throw new CustomerException("用户认证信息不全", 888888);
            }
        }
        GujiUserEntity updateUserEntity = new GujiUserEntity();
        updateUserEntity.setWxId(wxId);
        updateUserEntity.setAuthStatus(authStatus);
        this.gujiUserDAO.update(updateUserEntity);
    }

    /**
     * 删除推荐文章
     * @param id
     * @throws Exception
     */
    @Override
    public void delShare(Long id) throws Exception {
        GujiShareRecordEntity shareRecordEntity = this.gujiShareRecordDAO.selectById(id);
        if (shareRecordEntity == null) return;
        // 查询是哪个人的哪只股票、查询出这个人的这个股票，进行清仓，现金仓位增加，文章删除
        GujiUserStockEntity userStockEntity = this.gujiUserStockDAO.selectByWxIdAndCode(shareRecordEntity.getWxId(), shareRecordEntity.getStockCode());
        // 查询 现金仓位
        GujiUserStockEntity xjUserStockEntity = this.gujiUserStockDAO.selectByWxIdAndCode(shareRecordEntity.getWxId(), "000000");

        if (userStockEntity != null && xjUserStockEntity != null && userStockEntity.getStockType().equals(EGujiStockType.GP.getCode())) {
            xjUserStockEntity.setPositions(xjUserStockEntity.getPositions() + userStockEntity.getPositions());
            this.gujiUserStockDAO.update(xjUserStockEntity);
            userStockEntity.setPositions(0);
            this.gujiUserStockDAO.update(userStockEntity);
        }
        this.gujiShareRecordDAO.delete(id);

        // 查询是否还有这个股票的推荐
        List<GujiShareRecordEntity> shareRecordEntityList = this.gujiShareRecordDAO.selectByWxIdAndCode(shareRecordEntity.getWxId(), shareRecordEntity.getStockCode());
        if (shareRecordEntityList == null || shareRecordEntityList.size() == 0) {
            // 没有推荐的文章，就删除
            if (userStockEntity != null) {
                this.gujiUserStockDAO.delete(userStockEntity.getId());
            }
        }
    }

    /**
     * 更新推荐分享是否公开
     * @param shareId
     * @param isPublic
     * @throws Exception
     */
    @Override
    public void updateShareIsPublic(Long shareId, Integer isPublic) throws Exception {
        GujiShareRecordEntity gujiShareRecordEntity = new GujiShareRecordEntity();
        gujiShareRecordEntity.setShareId(shareId);
        gujiShareRecordEntity.setIsPublic(isPublic);
        this.gujiShareRecordDAO.update(gujiShareRecordEntity);
    }

    /**
     * 根据主键查询
     * @param wxId
     * @throws Exception
     */
    @Override
    public GujiUserEntity selectByWxId(Long wxId) {
        return this.gujiUserDAO.selectByWxId(wxId);
    }

    @Override
    public Integer update(GujiUserEntity entity) {
        return this.gujiUserDAO.update(entity);
    }

    /**
     * 配置列表
     *
     * @return
     */
    @Override
    public List<GujiConfigEntity> queryAdminConfigList() {
        return gujiUserDAO.queryAdminConfigList();
    }

    @Override
    public String configSet(Long id, String value) {
        try {
            GujiConfigEntity entity = gujiUserDAO.selectById(id);
            entity.setValue(value);
            gujiUserDAO.update(entity);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

}
