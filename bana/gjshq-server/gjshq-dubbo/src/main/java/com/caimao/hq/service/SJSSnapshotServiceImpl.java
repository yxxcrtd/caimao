package com.caimao.hq.service;


import com.alibaba.fastjson.JSON;
import com.caimao.hq.api.entity.*;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.api.service.ISJSSnapshotService;
import com.caimao.hq.dao.SJSSnapshotDao;
import com.caimao.hq.utils.*;
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
 * 上交所行情数据实现类
 * Created by Administrator on 2015/9/25.
 *
 */

@Service("sjsSnapshotService")
public class SJSSnapshotServiceImpl implements ISJSSnapshotService {
    private Logger logger = LoggerFactory.getLogger(SJSSnapshotServiceImpl.class);
    @Autowired
    private SJSSnapshotDao sjsSnapshotDao;
    @Autowired
    public JRedisUtil jredisUtil;
    @Autowired
    private GJSProductUtils gjsProductUtils;
    @Autowired
    private IHQService hqService;
    @Override
    public void insert(Snapshot snapshot) {


        if(null!=snapshot){

            hqService.insertRedisSnapshotHistory(snapshot);//插入成交数量数据到Redis
            hqService.insertRedisMultiDayHistory(snapshot);//保存交易日
            //sjsSnapshotDao.insert(snapshot);
           // logger.info(JSON.toJSONString(snapshot));

        }
    }


    public List<Snapshot> tradeAmountQueryHistory(TradeAmountReq tradeAmountReq) {
        return sjsSnapshotDao.tradeAmountQueryHistory(tradeAmountReq);
    }

    @Override
    public List<Snapshot> queryByProdCode(String prodCode) {

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
        return resultlist;
    }

    @Override
    public List<Snapshot> queryByExchange(String financeMic) {
        String productAll=GJSProductUtils.mapProduct.get(financeMic);
        if(!StringUtils.isBlank(productAll)){
            return queryByProdCode( productAll);
        }else{
            return null;
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
            snapshot= JSON.parseObject(redisStr, SJSSnapshot.class);
        }
        return snapshot;
    }


    private void convertStrToObject(List<String> redisStr, List<TradeAmountRes> list){
        TradeAmountRes snapshot=null;
        if(null!=redisStr&&redisStr.size()>0&&null!=redisStr){
                for(String str:redisStr){
                    if(!StringUtils.isBlank(str)){
                        snapshot= JSON.parseObject(str, TradeAmountRes.class);
                        if(null!=snapshot){
                            list.add(snapshot);
                        }
                    }
                }
        }
    }
    private String[] getProdCode(String prodCode){
        String[] prodCodArray=null;
        if(!StringUtils.isBlank(prodCode)){
            prodCodArray=prodCode.split(",");
        }
        return prodCodArray;
    }
    public List<Snapshot> queryDB(String financeMic) {
        return sjsSnapshotDao.selectNew(financeMic);
    }

    @Override
    public void redisInit(String financeMic) {

        List<Snapshot> snapshotsList=queryDB(financeMic);
        for(Snapshot snapshot:snapshotsList){
            if(null!=snapshot){
                String redisKey=MinTimeUtil.getRedisKey(CandleCycle.Snap,snapshot.getExchange(),snapshot.getProdCode());;
                if(!jredisUtil.exists(redisKey)){//如果redis没有，就用数据库的初始化
                    jredisUtil.setex(redisKey, JSON.toJSONString(snapshot), 0);
                }
            }
        }
    }

    private List<String> selectDateNear5(TradeAmountReq tradeAmountReq){

        return sjsSnapshotDao.selectDateNear5(tradeAmountReq);
    }

    public Map selectSnapshotFive(TradeAmountReq tradeAmountReq){

        Map map=new HashMap();

        List<String> dateList=selectDateNear5(tradeAmountReq);
        if(null!=dateList){
            SJSSnapshot temp=null;
            for(String strDate:dateList){

                if(!StringUtils.isBlank(strDate)){

                    temp=new SJSSnapshot();
                    temp.setExchange(tradeAmountReq.getExchange());
                    temp.setProdCode(tradeAmountReq.getProdCode());
                    temp.setTradeDate(strDate);
                    List  list=sjsSnapshotDao.query(temp);
                    List  responseList=new ArrayList<>();
                    try {
                        DozerMapperSingleton.listCopy(list, responseList, "SnapshotRes");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    map.put(strDate,responseList);
                }
            }
        }
        return map;

    }
}
