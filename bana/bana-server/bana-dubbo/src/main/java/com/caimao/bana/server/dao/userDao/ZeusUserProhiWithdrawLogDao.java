package com.caimao.bana.server.dao.userDao;

import com.caimao.bana.api.entity.req.user.FUserQueryProhiWithdrawLogReq;
import com.caimao.bana.api.entity.res.user.FUserQueryProhiWithdrawLogRes;
import com.caimao.bana.api.entity.zeus.ZeusUserProhiWithdrawLogEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户禁止提现状态数据表操作
 * Created by Administrator on 2015/8/14.
 */
@Repository
public class ZeusUserProhiWithdrawLogDao extends SqlSessionDaoSupport {

    /**
     * 保存用户提现状态变更操作
     * @param entity
     */
    public void save(ZeusUserProhiWithdrawLogEntity entity) {
        this.getSqlSession().insert("ZeusUserProhiWithdrawLog.save", entity);
    }

    /**
     * 查询用户禁止提现变动历史
     * @param req
     */
    public List<FUserQueryProhiWithdrawLogRes> queryUserLogWithPage(FUserQueryProhiWithdrawLogReq req) {
        return this.getSqlSession().selectList("ZeusUserProhiWithdrawLog.queryUserLogWithPage", req);
    }

}
