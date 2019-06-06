package com.caimao.hq.service;


import com.caimao.hq.api.entity.GjsPriceAlertEntity;
import com.caimao.hq.api.entity.req.FQueryGjsPriceAlertReq;
import com.caimao.hq.api.enums.EPriceAlertType;
import com.caimao.hq.api.service.IGjsPriceAlertService;
import com.caimao.hq.dao.GjsPriceAlertDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* GjsPriceAlertEntity 服务接口实现
*
* Created by wangxu@huobi.com on 2015-11-23 11:10:10 星期一
*/
@Service("gjsPriceAlertService")
public class GjsPriceAlertServiceImpl implements IGjsPriceAlertService {

    @Autowired
    private GjsPriceAlertDAO gjsPriceAlertDAO;

    /**
     * 重置符合条件的触发价格
     * @param condition 条件
     * @param nowPrice  现价
     * @throws Exception
     */
    @Override
    public void resetPriceAlertTriggerPrice(String condition, String nowPrice) throws Exception {
        this.gjsPriceAlertDAO.resetTriggerPrice(condition, nowPrice);
    }

    /**
    * 查询GjsPriceAlert列表
    *
    * @param req
    * @return
    * @throws Exception
    */
    @Override
    public List<GjsPriceAlertEntity> queryGjsPriceAlertList(FQueryGjsPriceAlertReq req) throws Exception {
        List<GjsPriceAlertEntity> list = new ArrayList<>();
        list = this.gjsPriceAlertDAO.queryPriceAlertList(req);
        return list;
    }

    /**
     * 设置价格提箱
     * @return
     * @throws Exception
     */
    @Override
    public Boolean setPriceAlert(GjsPriceAlertEntity entity) throws Exception {
        GjsPriceAlertEntity dbPriceAlertEntity = this.gjsPriceAlertDAO.getPriceAlertInfo(entity);
        if (dbPriceAlertEntity == null) {
            // 执行插入操作
            this.insert(entity);
        } else {
            // 执行更新操作
            entity.setId(dbPriceAlertEntity.getId());
            this.update(entity);
        }
        return true;
    }

    /**
    * 查询指定GjsPriceAlert内容
    *
    * @param id
    * @return
    * @throws Exception
    */
    @Override
    public GjsPriceAlertEntity selectById(Long id) throws Exception {
        return this.gjsPriceAlertDAO.selectById(id);
    }

    /**
     * 根据userId，查询提醒设置
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<GjsPriceAlertEntity> selectByUserId(Long userId) throws Exception {
        return this.gjsPriceAlertDAO.selectByUserId(userId);
    }

    /**
    * 添加GjsPriceAlert的接口
    *
    * @param entity
    * @throws Exception
    */
    @Override
    public void insert(GjsPriceAlertEntity entity) throws Exception {
        entity.setLastSendTime(null);
        entity.setCreated(new Date());
        this.gjsPriceAlertDAO.insert(entity);
    }

   /**
    * 删除GjsPriceAlert的接口
    *
    * @param id
    * @throws Exception
    */
    @Override
    public void deleteById(Long id) throws Exception {
        this.gjsPriceAlertDAO.deleteById(id);
    }

   /**
    * 更新GjsPriceAlert的接口
    *
    * @param entity
    * @throws Exception
    */
    @Override
    public void update(GjsPriceAlertEntity entity) throws Exception {
        this.gjsPriceAlertDAO.update(entity);
    }

}
