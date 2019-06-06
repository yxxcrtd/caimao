package com.caimao.gjs.server.dao;

import com.caimao.gjs.api.entity.GjsHolidayEntity;
import com.caimao.gjs.api.entity.req.FQueryGjsHolidayReq;
import com.caimao.gjs.api.entity.res.FQueryGjsHolidayRes;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* GjsHolidayEntity DAO实现
*
* Created by yangxinxin@huobi.com on 2015-11-06 18:07:52 星期五
*/
@Repository
public class GjsHolidayDAO extends SqlSessionDaoSupport {

   /**
    * 查询GjsHoliday列表
    *
    * @param req
    * @return
    */
    public List<FQueryGjsHolidayRes> queryGjsHolidayWithPage(FQueryGjsHolidayReq req) {
        return this.getSqlSession().selectList("GjsHoliday.queryGjsHolidayWithPage", req);
    }

    /**
     * 根据exchange和holiday查询列表
     *
     * @param exchange
     * @param holiday
     * @return
     */
    public List<GjsHolidayEntity> queryGjsHolidayByExchangeAndHoliday(String exchange, String holiday) {
        Map<String, String> map = new HashMap<>();
        map.put("exchange", exchange);
        map.put("holiday", holiday);
        return this.getSqlSession().selectList("GjsHoliday.queryGjsHolidayByExchangeAndHoliday", map);
    }

   /**
    * 查询指定GjsHoliday内容
    *
    * @param id
    * @return
    */
    public GjsHolidayEntity selectById(Long id) {
        return this.getSqlSession().selectOne("GjsHoliday.selectById", id);
    }

   /**
    * 添加GjsHoliday的接口
    *
    * @param entity
    */
    public Integer insert(GjsHolidayEntity entity) {
        return this.getSqlSession().insert("GjsHoliday.insert", entity);
    }

   /**
    * 删除GjsHoliday的接口
    *
    * @param id
    */
    public Integer deleteById(Long id) {
        return this.getSqlSession().delete("GjsHoliday.deleteById", id);
    }

   /**
    * 更新GjsHoliday的接口
    *
    * @param entity
    */
    public Integer update(GjsHolidayEntity entity) {
        return this.getSqlSession().update("GjsHoliday.update", entity);
    }

}