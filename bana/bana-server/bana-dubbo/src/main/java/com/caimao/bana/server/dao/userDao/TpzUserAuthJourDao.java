package com.caimao.bana.server.dao.userDao;

import com.caimao.bana.api.entity.TpzUserAuthJourEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 用户实名认证的记录
 * Created by WangXu on 2015/5/18.
 */
@Repository
public class TpzUserAuthJourDao extends SqlSessionDaoSupport {

    /**
     * 获取卡号已经通过的实名认证信息
     * @param userAuthJourEntity    用户实名认证信息
     * @return 被实名认证的信息
     */
    public TpzUserAuthJourEntity getPassedUserAuthJour(TpzUserAuthJourEntity userAuthJourEntity) {
        return getSqlSession().selectOne("TpzUserAuthJour.getPassedUserAuthJour", userAuthJourEntity);
    }

    /**
     * 用户一共验证过多少次
     * @param userId    用户ID
     * @return  验证次数
     */
    public int countAuthNum(Long userId) {
        return getSqlSession().selectOne("TpzUserAuthJour.countAuthNum", userId);
    }

    /**
     * 保存用户的实名认证信息
     * @param userAuthJourEntity    实名认证信息
     * @return 返回值
     */
    public int save(TpzUserAuthJourEntity userAuthJourEntity) {
        return getSqlSession().insert("TpzUserAuthJour.save", userAuthJourEntity);
    }

    /**
     * 更新用户的实名认证信息
     * @param userAuthJourEntity 实名认证信息
     * @return  返回值
     */
    public int update(TpzUserAuthJourEntity userAuthJourEntity) {
        return getSqlSession().insert("TpzUserAuthJour.update", userAuthJourEntity);
    }
}
