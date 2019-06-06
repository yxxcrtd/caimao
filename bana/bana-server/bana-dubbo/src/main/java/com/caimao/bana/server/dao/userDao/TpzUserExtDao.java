package com.caimao.bana.server.dao.userDao;

import com.caimao.bana.api.entity.TpzUserExtEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 用户扩展表数据访问操作
 * Created by WangXu on 2015/5/18.
 */
@Repository
public class TpzUserExtDao extends SqlSessionDaoSupport {

    /**
     * 保存用户扩展信息
     * @param userExtEntity 用户扩展信息
     * @return  返回值
     */
    public int save(TpzUserExtEntity userExtEntity) {
        return getSqlSession().insert("TpzUserExt.save", userExtEntity);
    }

    /**
     * 更新用户的扩展信息
     * @param userExtEntity
     * @return
     */
    public int update(TpzUserExtEntity userExtEntity) {
        return getSqlSession().update("TpzUserExt.update", userExtEntity);
    }

    /**
     * 更新用户头像
     * @param userExtEntity
     * @return
     */
    public int updatePhotoByUserId(TpzUserExtEntity userExtEntity) {
        return getSqlSession().update("TpzUserExt.updatePhotoByUserId", userExtEntity);
    }

    /**
     * 更新证件路径
     * @param userExtEntity
     * @return
     */
    public int updateCardPath(TpzUserExtEntity userExtEntity) {
        return getSqlSession().update("TpzUserExt.updateCardPath", userExtEntity);
    }

    public TpzUserExtEntity getById(Long userId) {
        return getSqlSession().selectOne("TpzUserExt.getById", userId);
    }

}
