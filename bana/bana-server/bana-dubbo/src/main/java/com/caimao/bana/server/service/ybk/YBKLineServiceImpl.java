package com.caimao.bana.server.service.ybk;

import com.caimao.bana.api.entity.ybk.*;
import com.caimao.bana.api.service.ybk.IYBKLineService;
import com.caimao.bana.server.dao.ybk.*;
import com.caimao.bana.server.utils.KLineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 邮币卡服务
 */
@Service("YBKLineService")
public class YBKLineServiceImpl implements IYBKLineService {
    private static final Logger logger = LoggerFactory.getLogger(YBKLineServiceImpl.class);

    @Resource
    private YBKTimeLineDao ybkTimeLineDao;
    @Resource
    private YBKKLineDao ybkkLineDao;
    @Resource
    private YBKMACDDao ybkmacdDao;
    @Resource
    private YBKKDJDao ybkkdjDao;
    @Resource
    private YBKRSIDao ybkrsiDao;

    @Override
    public void updateKLine(YBKTimeLineEntity ybkTimeLineEntity) throws Exception {
        try{
            //更新日K
            this.processKLine(ybkTimeLineEntity);

            //插入分时数据
            ybkTimeLineDao.insert(ybkTimeLineEntity);

            //查询最近35天的日K数据
            List<YBKKLineEntity> dataList = ybkkLineDao.queryNumOfDays(ybkTimeLineEntity, 34);
            Collections.reverse(dataList);

            //计算并更新MACD
            this.processMACD(ybkTimeLineEntity, dataList);

            //计算并更新KDJ
            this.processKDJ(ybkTimeLineEntity, dataList);

            //计算并更新RSI
            this.processRSI(ybkTimeLineEntity, dataList);
        }catch(Exception e){
            logger.error("更新K线数据失败了,原因{}", e);
            throw e;
        }
    }

    //处理日K
    private void processKLine(YBKTimeLineEntity ybkTimeLineEntity) throws Exception{
        //整理数据
        YBKKLineEntity ybkkLineEntity = new YBKKLineEntity();
        ybkkLineEntity.setExchangeName(ybkTimeLineEntity.getExchangeName());
        ybkkLineEntity.setCode(ybkTimeLineEntity.getCode());
        ybkkLineEntity.setDate(ybkTimeLineEntity.getDatetime());
        ybkkLineEntity.setUpdateTime(ybkTimeLineEntity.getDatetime());
        ybkkLineEntity.setOpenPrice(ybkTimeLineEntity.getOpenPrice());
        ybkkLineEntity.setHighPrice(ybkTimeLineEntity.getHighPrice());
        ybkkLineEntity.setLowPrice(ybkTimeLineEntity.getLowPrice());
        ybkkLineEntity.setCurPrice(ybkTimeLineEntity.getCurPrice());
        ybkkLineEntity.setClosePrice(ybkTimeLineEntity.getYesterBalancePrice());
        ybkkLineEntity.setCurrentGains(ybkTimeLineEntity.getCurrentGains());
        ybkkLineEntity.setTotalAmount(ybkTimeLineEntity.getTotalAmount());
        ybkkLineEntity.setTotalMoney(ybkTimeLineEntity.getTotalMoney());

        //更新或者插入数据
        YBKKLineEntity todayKLine = ybkkLineDao.queryExist(ybkkLineEntity);
        if(todayKLine == null){
            ybkkLineDao.insert(ybkkLineEntity);
        }else{
            ybkkLineDao.update(ybkkLineEntity);
        }
    }

    //处理MACD
    private void processMACD(YBKTimeLineEntity ybkTimeLineEntity, List<YBKKLineEntity> dataList) throws Exception{
        List<KLineEntity> kLineData = KLineUtils.processData(dataList);
        YBKMACDEntity ybkmacdEntity = KLineUtils.calculateMACD(kLineData);

        ybkmacdEntity.setExchangeName(ybkTimeLineEntity.getExchangeName());
        ybkmacdEntity.setCode(ybkTimeLineEntity.getCode());
        ybkmacdEntity.setDate(ybkTimeLineEntity.getDatetime());
        ybkmacdEntity.setUpdateTime(ybkTimeLineEntity.getDatetime());

        //更新或插入数据
        YBKMACDEntity ybkMACDData = ybkmacdDao.queryExist(ybkmacdEntity);
        if(ybkMACDData == null){
            ybkmacdDao.insert(ybkmacdEntity);
        }else{
            ybkmacdDao.update(ybkmacdEntity);
        }
    }

    //处理KDJ
    private void processKDJ(YBKTimeLineEntity ybkTimeLineEntity, List<YBKKLineEntity> dataList) throws Exception{
        List<KLineEntity> kLineData = KLineUtils.processData(dataList);
        YBKKDJEntity ybkkdjEntity = KLineUtils.calculateKDJ(kLineData);

        ybkkdjEntity.setExchangeName(ybkTimeLineEntity.getExchangeName());
        ybkkdjEntity.setCode(ybkTimeLineEntity.getCode());
        ybkkdjEntity.setDate(ybkTimeLineEntity.getDatetime());
        ybkkdjEntity.setUpdateTime(ybkTimeLineEntity.getDatetime());

        //更新或插入数据
        YBKKDJEntity ybkKDJData = ybkkdjDao.queryExist(ybkkdjEntity);
        if(ybkKDJData == null){
            ybkkdjDao.insert(ybkkdjEntity);
        }else{
            ybkkdjDao.update(ybkkdjEntity);
        }
    }

    //处理RSI
    private void processRSI(YBKTimeLineEntity ybkTimeLineEntity, List<YBKKLineEntity> dataList) throws Exception{
        List<KLineEntity> kLineData = KLineUtils.processData(dataList);
        YBKRSIEntity ybkrsiEntity = KLineUtils.calculateRSI(kLineData);

        ybkrsiEntity.setExchangeName(ybkTimeLineEntity.getExchangeName());
        ybkrsiEntity.setCode(ybkTimeLineEntity.getCode());
        ybkrsiEntity.setDate(ybkTimeLineEntity.getDatetime());
        ybkrsiEntity.setUpdateTime(ybkTimeLineEntity.getDatetime());

        //更新或插入数据
        YBKRSIEntity ybkRSIData = ybkrsiDao.queryExist(ybkrsiEntity);
        if(ybkRSIData == null){
            ybkrsiDao.insert(ybkrsiEntity);
        }else{
            ybkrsiDao.update(ybkrsiEntity);
        }
    }
}