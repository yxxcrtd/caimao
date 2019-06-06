package com.caimao.hq.api.service;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.entity.TradeAmountReq;

import java.util.List;

public interface INJSSnapshotService {

    public void insert(Snapshot snapshot);
    public List<Snapshot> queryByProdCode(String prodCode);
    public List<Snapshot> queryByExchange(String financeMic);
    public Snapshot trick(String prodCode);
    public List<Snapshot> queryDB(String financeMic);
    public void redisInit(String financeMic);

    public List<Snapshot> tradeAmountQueryHistory(TradeAmountReq tradeAmountReq);

}
