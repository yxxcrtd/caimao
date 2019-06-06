package com.caimao.gjs.server.service;

import com.caimao.gjs.api.entity.GjsHolidayEntity;
import com.caimao.gjs.api.entity.req.FQueryGjsHolidayReq;
import com.caimao.gjs.api.service.IGjsHolidayService;
import com.caimao.gjs.server.dao.GjsHolidayDAO;
import com.caimao.gjs.server.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* GjsHolidayEntity 服务接口实现
*
* Created by yangxinxin@huobi.com on 2015-11-06 18:07:52 星期五
*/
@Service("gjsHolidayService")
public class GjsHolidayServiceImpl implements IGjsHolidayService {

    private static final Logger logger = LoggerFactory.getLogger(GjsHolidayServiceImpl.class);

    @Autowired
    private GjsHolidayDAO gjsHolidayDAO;

    @Autowired
    private RedisUtils redisUtils;

   /**
    * 查询GjsHoliday列表
    *
    * @param req
    * @return
    * @throws Exception
    */
    @Override
    public FQueryGjsHolidayReq queryGjsHolidayList(FQueryGjsHolidayReq req) throws Exception {
        req.setItems(this.gjsHolidayDAO.queryGjsHolidayWithPage(req));
        return req;
    }

    /**
     * 根据exchange和holiday查询列表
     *
     * @param exchange
     * @param holiday
     * @return
     */
    @Override
    public String queryGjsHolidayByExchangeAndHoliday(String exchange, String holiday) {
        String tradeTime = (String) redisUtils.get(0, holiday + exchange);
        logger.info(String.format(" %s 从Redis中根据%s获取到的交易时间段是：%s", String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", new Date()), holiday + exchange, tradeTime));
        if (null == tradeTime || "".equals(tradeTime)) {
            List<GjsHolidayEntity> list = this.gjsHolidayDAO.queryGjsHolidayByExchangeAndHoliday(exchange, holiday);
            if (null != list && 0 < list.size()) {
                tradeTime = list.get(0).getTradeTime();
                redisUtils.set(0, holiday + exchange, tradeTime, 2 * 60 * 60); // 在 Redis 中缓存2个小时
            }
        }
        return tradeTime;
    }

   /**
    * 查询指定GjsHoliday内容
    *
    * @param id
    * @return
    * @throws Exception
    */
    @Override
    public GjsHolidayEntity selectById(Long id) throws Exception {
        return this.gjsHolidayDAO.selectById(id);
    }

   /**
    * 添加GjsHoliday的接口
    *
    * @param entity
    * @throws Exception
    */
    @Override
    public void insert(GjsHolidayEntity entity) throws Exception {
        this.gjsHolidayDAO.insert(entity);
    }

   /**
    * 删除GjsHoliday的接口
    *
    * @param id
    * @throws Exception
    */
    @Override
    public void deleteById(Long id) throws Exception {
        this.gjsHolidayDAO.deleteById(id);
    }

   /**
    * 更新GjsHoliday的接口
    *
    * @param entity
    * @throws Exception
    */
    @Override
    public void update(GjsHolidayEntity entity) throws Exception {
        this.gjsHolidayDAO.update(entity);
    }

}
