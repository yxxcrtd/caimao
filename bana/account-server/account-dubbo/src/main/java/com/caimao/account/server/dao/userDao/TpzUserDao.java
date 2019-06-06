package com.caimao.account.server.dao.userDao;


import com.caimao.account.api.entity.user.TpzUserEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 用户管理操作
 * Created by WangXu on 2015/4/22.
 */
@Repository
public class TpzUserDao extends SqlSessionDaoSupport {

    /**
     * 根据手机号码获取用户信息
     *
     * @param phone
     * @return
     */
    public TpzUserEntity queryUserByPhone(String phone) {
        return getSqlSession().selectOne("TpzUser.queryUserByPhone", phone);
    }


}
