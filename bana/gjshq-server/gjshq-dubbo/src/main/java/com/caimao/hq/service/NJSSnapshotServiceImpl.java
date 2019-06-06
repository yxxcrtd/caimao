package com.caimao.hq.service;


import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.entity.CandleCycle;
import com.caimao.hq.api.entity.NJSSnapshot;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.entity.TradeAmountReq;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.api.service.INJSSnapshotService;
import com.caimao.hq.dao.NJSSnapshotDao;
import com.caimao.hq.utils.GJSProductUtils;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.MinTimeUtil;
import com.caimao.hq.utils.SnapshotFormate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 *
 * 南交所行情数据实现类
 * Created by Administrator on 2015/9/25.
 *
 */

@Service("njsSnapshotService")
public class NJSSnapshotServiceImpl implements INJSSnapshotService {
    private Logger logger = LoggerFactory.getLogger(NJSSnapshotServiceImpl.class);
    @Autowired
    public JRedisUtil jredisUtil;
    @Autowired
    private GJSProductUtils gjsProductUtils;
    @Autowired
    private NJSSnapshotDao njsSnapshotDao;
    @Autowired
    private IHQService hqService;

    @Override
    public void insert(Snapshot snapshot) {
        if(null!=snapshot){


            SnapshotFormate.formateSnapshot(CandleCycle.Snap, snapshot);//格式化时间和rediskey
            String jsonString = JSON.toJSONString(snapshot);
            jredisUtil.setex(snapshot.getRedisKey(), jsonString, 0);
            hqService.insertRedisSnapshotHistory(snapshot);//保存成交明细
            hqService.insertRedisMultiDayHistory(snapshot);//保存交易日
            //njsSnapshotDao.insert(snapshot);
           // logger.info(jsonString);

        }
    }

    public List<Snapshot> tradeAmountQueryHistory(TradeAmountReq tradeAmountReq) {
        return njsSnapshotDao.tradeAmountQueryHistory(tradeAmountReq);
    }

    @Override
    public List<Snapshot> queryByProdCode( String prodCode) {

        Map<String,Snapshot> resultMap=new HashMap();
        String[] prodCodArray =getProdCode(prodCode);
        if(null==prodCodArray||prodCodArray.length>200){
            throw new RuntimeException("单个行情查询，传入产品代码为Null或者传入产品个数大于200个");
        }

        List<Snapshot> resultlist=new ArrayList();
        List<String> redisStr=null;
        String[] redisKeyArray = new String[prodCodArray.length];
        String redisKey="";
        for(int i=0;i<prodCodArray.length;i++){
            if(!StringUtils.isBlank(prodCodArray[i])){
                redisKey = MinTimeUtil.getRedisKey(CandleCycle.Snap, prodCodArray[i]);
                redisKeyArray[i]=redisKey;
            }
        }
        redisStr=jredisUtil.mget(redisKeyArray);
        gjsProductUtils.convertStrToSnapObject(redisStr, resultlist);
        //gjsProductUtils.convertMapToList(resultMap, prodCodArray, resultlist);
        return resultlist;
    }

    @Override
    public List<Snapshot> queryByExchange(String financeMic) {
        List<Snapshot> list =null;
        String productAll=GJSProductUtils.mapProduct.get(financeMic);
        if(!StringUtils.isBlank(productAll)){
            list= queryByProdCode( productAll);
        }
        return list;

    }

    @Override
    public List<Snapshot> queryDB(String financeMic) {
        return njsSnapshotDao.selectNew(financeMic);
    }

    @Override
    public void redisInit(String financeMic) {

        List<Snapshot> snapshotsList = queryDB(financeMic);
        if(null!=snapshotsList){
            for(Snapshot snapshot:snapshotsList){
                if(null!=snapshot){
                    if(!jredisUtil.exists(snapshot.getRedisKey())){//如果redis没有，就用数据库的初始化

                        jredisUtil.setex(snapshot.getRedisKey(), JSON.toJSONString(snapshot), 0);
                    }
                }
            }
        }

    }

    private void convertStrToObject(List<String> redisStr, List<Snapshot> list){
        NJSSnapshot snapshot=null;
        if(null!=redisStr&&redisStr.size()>0&&null!=redisStr){
            for(String str:redisStr){
                if(!StringUtils.isBlank(str)){
                    snapshot= JSON.parseObject(str, NJSSnapshot.class);
                    if(null!=snapshot){
                        list.add(snapshot);
                    }
                }
            }
        }
    }


    @Override
    public Snapshot trick(String prodCode) {
        String redisKey="";
        String redisStr="";
        Snapshot snapshot=null;
        if(!StringUtils.isBlank(prodCode)){
            redisKey = MinTimeUtil.getRedisKey(CandleCycle.Snap,prodCode);
            redisStr=jredisUtil.get(redisKey);
            snapshot= JSON.parseObject(redisStr, NJSSnapshot.class);
        }
        return snapshot;
    }

    private String[] getProdCode(String prodCode){
        String[] prodCodArray=null;
        if(!StringUtils.isBlank(prodCode)){
            prodCodArray=prodCode.split(",");
        }
        return prodCodArray;
    }
}
