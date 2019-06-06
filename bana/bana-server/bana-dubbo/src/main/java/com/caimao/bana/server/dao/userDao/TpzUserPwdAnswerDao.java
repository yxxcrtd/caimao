package com.caimao.bana.server.dao.userDao;

import com.caimao.bana.api.entity.TpzUserPwdAnswerEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * tpz_user_pwd_answer操作类
 * yanjg
 */
@Repository
public class TpzUserPwdAnswerDao extends SqlSessionDaoSupport {

    /**
     * @param userId
     * @return
     */
    public List<TpzUserPwdAnswerEntity> queryByUser(Long userId) {
        return getSqlSession().selectList("TpzUserPwdAnswer.queryByUser",userId);
    }

    /**
     * @param userId
     */
    public void deleteById(Long userId) {
        getSqlSession().selectList("TpzUserPwdAnswer.deleteById",userId);
    }

    /**
     * @param list
     */
    public void batchInsert(List list) {
        getSqlSession().insert("TpzUserPwdAnswer.batchInsert",list); 
    }

}
