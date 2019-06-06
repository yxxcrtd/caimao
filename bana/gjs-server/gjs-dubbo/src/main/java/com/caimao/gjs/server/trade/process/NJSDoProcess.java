package com.caimao.gjs.server.trade.process;

import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.gjs.server.utils.Constants;
import com.caimao.gjs.server.utils.cookiePost.HttpHelper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component("NJSDoProcess")
public class NJSDoProcess {
    private static final Logger logger = LoggerFactory.getLogger(NJSDoProcess.class);

    public <T> T getResult(Class<T> clazz, String adapter, Object req) throws Exception {
        try{
            Gson gson = new Gson();
            String receiveData = this.doSend(adapter, req);
            return gson.fromJson(receiveData, clazz);
        }catch (CustomerException e){
            throw e;
        }catch(Exception e){
            logger.error("njs socket error, error msg:{}" + e);
            throw new Exception("请求响应错误");
        }
    }

    private String doSend(String adapter, Object req) throws Exception{
        Gson gson = new Gson();
        String reqStr = gson.toJson(req);
        Map<String, Object> paramsMap = gson.fromJson(reqStr, Map.class);

        String serverUrl;
        String[] server = Constants.getHTTPServerAll();
        serverUrl = "http://" + server[0] + ":" + server[1] + Constants.NJS_TRADE_URL_ALL;

        String paramsStr = "ADAPTER=" + adapter;
        for(Map.Entry<String, Object> entry:paramsMap.entrySet()){
            paramsStr += "&" + entry.getKey() + "=" + entry.getValue().toString();
        }
        logger.info("请求参数: " + serverUrl + "?" + paramsStr);
        String html = HttpHelper.doGet(serverUrl + "?" + paramsStr);
        logger.info("响应结果: " + html);
        return this.replaceResult(html);
    }

    private String replaceResult(String html) throws Exception{
        try{
            Map<String, String> replaceMap = new HashMap<>();
            replaceMap.put("资金密码校验失败", "资金密码错误");
            replaceMap.put("1341:账户余额不足", "银行卡余额不足");
            replaceMap.put("资金账户余额不足", "转账金额超过可转出资金，请您调整数值后重试");
            replaceMap.put("\"STATE\":\"6\",\"MSG\":\"您尚未登陆、请您先登陆！", "\"STATE\":\"300001\",\"MSG\":\"交易所未登录");
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
