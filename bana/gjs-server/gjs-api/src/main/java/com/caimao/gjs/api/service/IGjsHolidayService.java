package com.caimao.gjs.api.service;

import com.caimao.gjs.api.entity.GjsHolidayEntity;
import com.caimao.gjs.api.entity.req.FQueryGjsHolidayReq;

import java.util.List;

/**
* GjsHolidayEntity 服务接口
*
* Created by yangxinxin@huobi.com on 2015-11-06 18:07:52 星期五
*/
public interface IGjsHolidayService {

   /**
    * 查询列表
    *
    * @param req
    * @return
    * @throws Exception
    */
    FQueryGjsHolidayReq queryGjsHolidayList(FQueryGjsHolidayReq req) throws Exception;

    /**
     * 根据exchange和holiday查询列表
     *
     * @param exchange
     * @param holiday
     * @return
     */
    String queryGjsHolidayByExchangeAndHoliday(String exchange, String holiday);

   /**
    * 查询指定内容
    *
    * @param id
    * @return
    * @throws Exception
    */
    GjsHolidayEntity selectById(Long id) throws Exception;

   /**
    * 添加的接口
    *
    * @param entity
    * @throws Exception
    */
    void insert(GjsHolidayEntity entity) throws Exception;

   /**
    * 删除的接口
    *
    * @param id
    * @throws Exception
    */
    void deleteById(Long id) throws Exception;

   /**
    * 更新的接口
    *
    * @param entity
    * @throws Exception
    */
    void update(GjsHolidayEntity entity) throws Exception;

}
