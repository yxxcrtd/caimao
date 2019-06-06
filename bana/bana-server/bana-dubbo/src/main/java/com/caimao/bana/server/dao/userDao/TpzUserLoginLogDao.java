package com.caimao.bana.server.dao.userDao;

import com.caimao.bana.api.entity.TpzUserLoginLogEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登陆日志
 * Created by WangXu on 2015/5/18.
 */
@Repository
public class TpzUserLoginLogDao extends SqlSessionDaoSupport {

    /**
     * 保存用户的登陆日志
     * @param userLoginLogEntity    登陆日志
     * @return  返回值
     */
    public int save(TpzUserLoginLogEntity userLoginLogEntity) {
        return getSqlSession().insert("TpzUserLoginLog.save", userLoginLogEntity);
    }

    /**
     * 查询用户最后一次登录成功的时间
     * @param userId
     * @return
     */
    public TpzUserLoginLogEntity queryLastLoginSuccessInfo(Long userId) {
        return getSqlSession().selectOne("TpzUserLoginLog.queryLastLoginSuccessInfo", userId);
    }

    /**
     * 查询用户指定时间内的错误登录次数
     * @param userId
     * @param beginDate
     * @param endDate
     * @return
     */
    public Integer queryLoginErrorCount(Long userId, Date beginDate, Date endDate) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("beginDate", beginDate);
        paramsMap.put("endDate", endDate);
        return getSqlSession().selectOne("TpzUserLoginLog.queryLoginErrorCount", paramsMap);
    }
}
