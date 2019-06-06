package com.caimao.gjs.server.trade.mina.njs;

import com.caimao.gjs.server.trade.mina.MinaPlugin;
import com.caimao.gjs.server.trade.mina.MinaServer;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NJSProcess {
    private static final Logger logger = LoggerFactory.getLogger(NJSProcess.class);

    public <T> T getResult(Class<T> clazz, String adapter, Object req) throws Exception {
        try{
            Gson gson = new Gson();
            MinaPlugin plugin = MinaServer.getPlugin("trade-njs");
            NJSTradeMsg njsTradeMsg = (NJSTradeMsg)plugin.sendAndRev(adapter, gson.toJson(req));
            String receiveData = njsTradeMsg.getData();
            return gson.fromJson(receiveData, clazz);
        }catch(Exception e){
            logger.error("njs socket error, error msg:{}" + e);
            throw new Exception("请求响应错误");
        }
    }
}
