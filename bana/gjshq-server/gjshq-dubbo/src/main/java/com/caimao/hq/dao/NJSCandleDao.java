package com.caimao.hq.dao;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.caimao.gjs.api.entity.article.GjsArticleEntity;
import com.caimao.gjs.api.entity.req.FQueryArticleReq;
import com.caimao.hq.api.CandlePageReq;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.utils.CandleUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;


/**
 *
 * Created by Administrator on 2015/9/30.
 *
 */
@Service("njsCandleDao")
public class NJSCandleDao extends SqlSessionDaoSupport {

    /**
     *
     * 保存实时数据
     * @param
     * @return  返回值
     *
     *
     */

    public int deleteByPK(String candleId ) {

        return getSqlSession().insert("NJS_Candle.deleteByPK", candleId);

    }

    public int insert(NJSCandle candle) {

        return getSqlSession().insert("NJS_Candle.insert", candle);

    }
    public int insertBatch(List<NJSCandle> candleList) {

        return getSqlSession().insert("NJS_Candle.insertBatch", candleList);

    }
    public List<Candle> selectList(CandleReq candleReq) {
        List<Candle> candList=null;
        try {
            String strCycle=candleReq.getCycle();
            CandleCycle cycle=CandleUtils.mapCycle.get(strCycle);
            Candle candle=new NJSCandle();
            candle.setExchange(candleReq.getExchange());
            candle.setBeginDate(candleReq.getBeginDate());
            candle.setEndDate(candleReq.getEndDate());
            candle.setCycle(cycle);
            candle.setProdCode(candleReq.getProdCode());
            if(candleReq.getNumber()<1){
                candle.setNumber(100);
            }else{
                candle.setNumber(candleReq.getNumber());
            }
            candList=getSqlSession().selectList("NJS_Candle.selectList", candle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candList;

    }
    public List<NJSCandle> selectList(NJSCandle candle) {
        List<NJSCandle> candList=null;
        try {
            candList=getSqlSession().selectList("NJS_Candle.selectList", candle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candList;

    }
    public List<Candle> selectLimit(int number) {
        List<Candle> candList=null;
        try {
            Candle candle=new NJSCandle();
            candList=getSqlSession().selectList("NJS_Candle.selectList", candle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candList;

    }


    public List<Candle> queryArticleWithPage(CandlePageReq req) {

        return this.getSqlSession().selectList("NJS_Candle.queryCandleWithPage", req);

    }

    public int queryCandleCount(CandlePageReq req) {

        return this.getSqlSession().selectOne("NJS_Candle.queryCandleCount");

    }


    public List<Candle> selectNew(String financeMic) {
        return getSqlSession().selectList("NJS_Candle.selectNew", financeMic);
    }
    public NJSCandle getPreCandl(CandleCycle cycle, String finance_mic, String prod_code,String miniTime) {

        NJSCandle candle=null;
        if (null!=cycle&&!StringUtils.isBlank(finance_mic)&&!StringUtils.isBlank(prod_code)) {

            SJSCandle temp=new SJSCandle();
            temp.setProdCode(prod_code);
            temp.setExchange(finance_mic);
            temp.setCycle(cycle);
            temp.setMinTime(miniTime);
            candle=getSqlSession().selectOne("NJS_Candle.selectPreCandle", temp);
        }
        return candle;
    }


    /**
     * 查询最近5日分时
     * */
    public List<String> selectDateNear5(TradeAmountReq tradeAmountReq){

        return getSqlSession().selectList("NJS_Candle.selectDateNear5", tradeAmountReq);
    }


    public List<MultiDaySnapshotRes> selectSnapshotFive(TradeAmountReq tradeAmountReq) {
        List<MultiDaySnapshotRes> candList=null;
        try {

            candList=getSqlSession().selectList("NJS_Candle.selectSnapshotFive", tradeAmountReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candList;

    }

}
