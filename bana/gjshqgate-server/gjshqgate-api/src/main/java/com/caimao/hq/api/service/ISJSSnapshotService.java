package com.caimao.hq.api.service;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.entity.TradeAmountReq;
import com.caimao.hq.api.entity.TradeAmountRes;

import java.util.List;
import java.util.Map;

public interface ISJSSnapshotService {

    public void insert(Snapshot snapshot);

    public List<Snapshot> queryByProdCode(String prodCode);
    public List<Snapshot> queryByExchange(String financeMic);
    public Snapshot trick(String prodCode);
    public List<Snapshot> queryDB(String financeMic);
    public void redisInit(String financeMic);
    public List<Snapshot> tradeAmountQueryHistory(TradeAmountReq tradeAmountReq);

    /**
     * 查询5日分时
     * */
    public Map selectSnapshotFive(TradeAmountReq tradeAmountReq);
}
