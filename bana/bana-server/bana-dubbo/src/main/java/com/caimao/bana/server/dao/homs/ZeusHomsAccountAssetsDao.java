package com.caimao.bana.server.dao.homs;

import com.caimao.bana.api.entity.req.FZeusHomsAccountAssetsReq;
import com.caimao.bana.api.entity.res.other.FHomsNeedUpdateAssetsRes;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountAssetsEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * HOMS操盘账户资产数据操作
 * Created by xavier on 15/7/8.
 */
@Repository
public class ZeusHomsAccountAssetsDao extends SqlSessionDaoSupport {
    /**
     * 搜索使用的根据姓名或手机号模糊搜索
     * @param req
     * @return
     */
    public List<ZeusHomsAccountAssetsEntity> searchUserHomsAssetsListWithPage(FZeusHomsAccountAssetsReq req) {
        return getSqlSession().selectList("ZeusHomsAccountAssets.searchUserHomsAssetsListWithPage", req);
    }

    /**
     * 保存homs操盘账户的资产信息
     * @param entity
     * @return
     */
    public int save(ZeusHomsAccountAssetsEntity entity) {
        return getSqlSession().insert("ZeusHomsAccountAssets.save", entity);
    }

    /**
     * 根据条件查找记录
     * @param entity
     * @return
     */
    public ZeusHomsAccountAssetsEntity getHomsAssets(ZeusHomsAccountAssetsEntity entity) {
        return getSqlSession().selectOne("ZeusHomsAccountAssets.getHomsAssets", entity);
    }

    /**
     * 获取指定日期需要更新的资产的记录
     * @param updateDate
     * @return
     */
    public List<FHomsNeedUpdateAssetsRes> queryNeedUpdateAssetsList(String updateDate) {
        return getSqlSession().selectList("ZeusHomsAccountAssets.queryNeedUpdateAssetsList", updateDate);
    }
}
