package com.caimao.bana.server.dao.userDao;

import com.caimao.bana.api.entity.PwdQuestionEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * tpz_user_pwd_answer操作类
 * yanjg
 */
@Repository
public class TpzUserPwdQuestionDao extends SqlSessionDaoSupport {
    /**
     * @return
     */
    public List<PwdQuestionEntity> getPwdQuestionAll() {
        return getSqlSession().selectList("PwdQuestion.getPwdQuestionAll");
    }

    /**
     * @param questionId
     * @return
     */
    public PwdQuestionEntity getById(String questionId) {
        return getSqlSession().selectOne("PwdQuestion.getById",questionId);
    }

}
