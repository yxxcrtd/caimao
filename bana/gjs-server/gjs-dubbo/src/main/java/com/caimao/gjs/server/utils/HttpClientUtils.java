package com.caimao.gjs.server.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Administrator on 2015/9/7.
 */
public class HttpClientUtils {
    public static String getString(String url){
        String result = "";
        if(url==null || url.isEmpty()){
            return result;
        }
        try{
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
