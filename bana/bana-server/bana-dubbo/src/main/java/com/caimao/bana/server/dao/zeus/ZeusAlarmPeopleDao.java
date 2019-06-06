package com.caimao.bana.server.dao.zeus;

import com.caimao.bana.api.entity.zeus.ZeusAlarmPeopleEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报警通知人的数据操作
 * Created by Administrator on 2015/8/19.
 */
@Repository
public class ZeusAlarmPeopleDao extends SqlSessionDaoSupport {


    public void save(ZeusAlarmPeopleEntity entity) {
        this.getSqlSession().insert("ZeusAlarmPeople.save", entity);
    }

    public void del(String key) {
        this.getSqlSession().delete("ZeusAlarmPeople.del", key);
    }

    public void update(ZeusAlarmPeopleEntity entity) {
        this.getSqlSession().update("ZeusAlarmPeople.update", entity);
    }

    public ZeusAlarmPeopleEntity getByKey(String key) {
        return this.getSqlSession().selectOne("ZeusAlarmPeople.getByKey", key);
    }

    public List<ZeusAlarmPeopleEntity> list() {
        return this.getSqlSession().selectList("ZeusAlarmPeople.list");
    }

}
