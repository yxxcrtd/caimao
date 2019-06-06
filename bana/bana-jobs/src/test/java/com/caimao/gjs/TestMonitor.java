package com.caimao.gjs;

import com.caimao.bana.jobs.utils.HttpHelper;
import com.google.gson.Gson;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TestMonitor {
    @Test
    public void checkNJSTraderIdNum() throws Exception{
        Boolean isWarning = false;
        try{
            String html = HttpHelper.doGet("http://127.0.0.1:8080/api/gjs_account/queryCanUseNum");
            System.out.println(html);
            Gson gson = new Gson();
            Map<String, Object> result = gson.fromJson(html, Map.class);
            Boolean success = (Boolean) result.get("success");
            Integer num = new BigDecimal(result.get("data").toString()).intValue();
            if(!success || num < 50) isWarning = true;
        }catch(Exception e){
            isWarning = true;
        }
        try{
            if(isWarning){
                System.out.println("已经触发报警了");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        /*Boolean isWarning = false;
        try{
            String html = HttpHelper.doGet("http://183.62.138.186:21632/MIS-Adapter/openJqAdapter.action?ADAPTER=011043");
            if(html.equals("")) throw new Exception("返回结果为空");
            System.out.println(html);
            Gson gson = new Gson();
            Map<String, Object> result = gson.fromJson(html, Map.class);
            String state = result.get("STATE").toString();
            if(state == null || state.equals("")) isWarning = true;
        }catch(Exception e){
            isWarning = true;
        }
        try{
            if(isWarning){
                System.out.println("已经触发报警了");
            }
        }catch(Exception e){
            e.printStackTrace();
        }*/
    }
}
