package com.caimao.gjs.server.trade.process;

import com.caimao.gjs.server.trade.mina.MinaPlugin;
import com.caimao.gjs.server.trade.mina.MinaServer;
import com.caimao.gjs.server.trade.mina.sjs.SJSTradeMsg;
import com.caimao.gjs.server.trade.mina.sjsOpen.SJSOpenMsg;
import com.caimao.gjs.server.utils.XMLForBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SJSDoProcess {
    private static final Logger logger = LoggerFactory.getLogger(SJSDoProcess.class);
    private String pluginName;
    public SJSDoProcess(String pluginNameStr){
        pluginName = pluginNameStr;
    }

    public <T> T getResult(Class<T> clazz, String adapter, String traderId, Object req) throws Exception {
        try{
            System.out.println("traderId:" + traderId);
            MinaPlugin plugin = MinaServer.getPlugin(pluginName);
            Object response = plugin.sendAndRev(adapter, new Object[]{traderId, req});
            if(response instanceof SJSOpenMsg){
                SJSOpenMsg sjsOpenMsg = (SJSOpenMsg)response;
                String dataStr = sjsOpenMsg.getData().toString();
                Integer stringStart = dataStr.indexOf("</head><body><record><sms_validatecode>");
                if(stringStart > 0){
                    dataStr = dataStr.substring(0, stringStart) + "<h_rsp_code>HJ0000</h_rsp_code><h_rsp_msg>成功</h_rsp_msg>" + dataStr.substring(stringStart, dataStr.length());
                }
                return XMLForBean.toObject(dataStr, clazz);
            }else if(response instanceof SJSTradeMsg){
                SJSTradeMsg sjsTradeMsg = (SJSTradeMsg)response;
                String dataStr = sjsTradeMsg.getData().toString();
                dataStr = this.replaceResult(dataStr, traderId);
                return XMLForBean.toObject(dataStr, clazz);
            }else{
                return null;
            }
        }catch(Exception e){
            logger.error("sjs socket error, error msg:{}" + e);
            throw new Exception("请求响应错误");
        }
    }

    private String replaceResult(String html, String traderId) throws Exception{
        try{
            Map<String, String> replaceMap = new HashMap<>();
            replaceMap.put("客户号[" + traderId + "]校验密码失败", "资金密码错误");
            Iterator<Map.Entry<String, String>> it = replaceMap.entrySet().iterator();
            for (;it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                if(html.contains(entry.getKey())){
                    try{
                        html = html.replace(entry.getKey(), entry.getValue());
                    }catch(Exception e){
                        logger.error("替换文字失败：", e);
                    }
                }
            }
        }catch(Exception e){
            logger.error("替换文字失败：", e);
        }
        return html;
    }
}
