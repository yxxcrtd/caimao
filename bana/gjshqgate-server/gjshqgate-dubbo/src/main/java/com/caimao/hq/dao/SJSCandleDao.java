package com.caimao.hq.dao;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.utils.CandleUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * Created by Administrator on 2015/9/30.
 *
 */
@Service("sjsCandleDao")
public class SJSCandleDao extends SqlSessionDaoSupport {

    /**
     *
     * 保存实时数据
     * @param
     * @return  返回值
     *
     *
     */

    public int insert(SJSCandle candle) {

        return getSqlSession().insert("SJS_Candle.insert", candle);

    }
    public int insertBatch(List<SJSCandle> candleList) {
        int result=0;
        try{
            result= getSqlSession().insert("SJS_Candle.insertBatch", candleList);

        }catch (Exception ex){
            logger.error("批量添加异常："+ex.getMessage());
        }
        return result;
    }

    public List<Candle> selectList(CandleReq candleReq) {
        List<Candle> candList=null;
        try {
            String strCycle=candleReq.getCycle();
            CandleCycle cycle= CandleUtils.mapCycle.get(strCycle);
            Candle candle=new SJSCandle();
            candle.setExchange(candleReq.getExchange());
            candle.setBeginDate(candleReq.getBeginDate());
            candle.setEndDate(candleReq.getEndDate());
            candle.setCycle(cycle);
            candle.setProdCode(candleReq.getProdCode());
            candle.setNumber(candleReq.getNumber());
            candList=getSqlSession().selectList("SJS_Candle.selectList", candle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candList;

    }

    public List<SJSCandle> query(SJSCandle candle) {

        return getSqlSession().selectList("SJS_Candle.selectHistoryList", candle);

    }
    public List<Candle> selectNew(String financeMic) {
        return getSqlSession().selectList("SJS_Candle.selectNew", financeMic);
    }
    public SJSCandle getPreCandl(CandleCycle cycle, String finance_mic, String prod_code,String miniTime) {

        SJSCandle candle=null;
        if (null!=cycle&&!StringUtils.isBlank(finance_mic)&&!StringUtils.isBlank(prod_code)) {

              SJSCandle  temp=new SJSCandle();
              temp.setProdCode(prod_code);
              temp.setExchange(finance_mic);
              temp.setCycle(cycle);
              temp.setMinTime(miniTime);
              candle=getSqlSession().selectOne("SJS_Candle.selectPreCandle", temp);
        }
        return candle;
    }

    /**
     * 查询最近5日分时
     * */
    public List<String> selectDateNear5(TradeAmountReq tradeAmountReq){

        return getSqlSession().selectList("SJS_Candle.selectDateNear5", tradeAmountReq);
    }


    public List<MultiDaySnapshotRes> selectSnapshotFive(TradeAmountReq tradeAmountReq) {
        List<MultiDaySnapshotRes> candList=null;
        try {

            candList=getSqlSession().selectList("SJS_Candle.selectSnapshotFive", tradeAmountReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candList;

    }



}
