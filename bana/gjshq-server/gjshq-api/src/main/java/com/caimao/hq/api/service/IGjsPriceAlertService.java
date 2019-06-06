package com.caimao.hq.api.service;


import com.caimao.hq.api.entity.GjsPriceAlertEntity;
import com.caimao.hq.api.entity.req.FQueryGjsPriceAlertReq;

import java.util.List;

/**
* GjsPriceAlertEntity 服务接口
*
* Created by wangxu@huobi.com on 2015-11-23 11:10:10 星期一
*/
public interface IGjsPriceAlertService {

    /**
     * 重置符合条件的触发价格
     * @param condition 条件
     * @param nowPrice  现价
     * @throws Exception
     */
    void resetPriceAlertTriggerPrice(String condition, String nowPrice) throws Exception;

   /**
    * 查询列表
    *
    * @param req
    * @return
    * @throws Exception
    */
   List<GjsPriceAlertEntity> queryGjsPriceAlertList(FQueryGjsPriceAlertReq req) throws Exception;



    /**
     * 设置价格提醒
     * @return
     * @throws Exception
     */
    Boolean setPriceAlert(GjsPriceAlertEntity entity) throws Exception;

   /**
    * 查询指定内容
    *
    * @param id
    * @return
    * @throws Exception
    */
    GjsPriceAlertEntity selectById(Long id) throws Exception;

    /**
     * 根据userId，查询提醒设置
     * @param userId
     * @return
     * @throws Exception
     */
    List<GjsPriceAlertEntity> selectByUserId(Long userId) throws Exception ;


   /**
    * 添加的接口
    *
    * @param entity
    * @throws Exception
    */
    void insert(GjsPriceAlertEntity entity) throws Exception;

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
    void update(GjsPriceAlertEntity entity) throws Exception;

}
