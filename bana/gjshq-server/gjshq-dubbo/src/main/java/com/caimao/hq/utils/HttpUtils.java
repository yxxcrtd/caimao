package com.caimao.hq.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.caimao.hq.api.entity.WyHqMonthRes;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/27.
 */
public class HttpUtils {

    // 表示服务器端的url
    private static String PATH = "http://fa.163.com/interfaces/ngxcache/priceinfo/kline/getPreDailyList.do?apiLevel=9&channel=appstore&deviceId=7AE6E911-0080-47B8-B4B7-E58B2910EAE2&deviceType=iphone&productVersion=2.10&systemName=iPhone%20OS&systemVersion=9.1&uniqueId=7AE6E911-0080-47B8-B4B7-E58B2910EAE2&date=&goodsId=AU&num=100000&partnerId=ods#userconsent#";
    private static URL url;

    public HttpUtils() {
        // TODO Auto-generated constructor stub
    }

    static {
        try {
            url = new URL(PATH);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * params 填写的URL的参数 encode 字节编码
     */
    public static String sendPostMessage(Map<String, String> params,
                                         String encode) {

        StringBuffer stringBuffer = new StringBuffer();

        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    stringBuffer
                            .append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(), encode))
                            .append("&");

                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            // 删掉最后一个 & 字符
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            System.out.println("-->>" + stringBuffer.toString());

            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url
                        .openConnection();
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setDoInput(true);// 从服务器获取数据
                httpURLConnection.setDoOutput(true);// 向服务器写入数据

                // 获得上传信息的字节大小及长度
                byte[] mydata = stringBuffer.toString().getBytes();
                // 设置请求体的类型
                httpURLConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Content-Lenth",
                        String.valueOf(mydata.length));

                // 获得输出流，向服务器输出数据
                OutputStream outputStream = (OutputStream) httpURLConnection
                        .getOutputStream();
                outputStream.write(mydata);

                // 获得服务器响应的结果和状态码
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == 200) {

                    // 获得输入流，从服务器端获得数据
                    InputStream inputStream = (InputStream) httpURLConnection
                            .getInputStream();
                    return (changeInputStream(inputStream, encode));

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return "";
    }

    /*
     * // 把从输入流InputStream按指定编码格式encode变成字符串String
     */
    public static String changeInputStream(InputStream inputStream,
                                           String encode) {

        // ByteArrayOutputStream 一般叫做内存流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if (inputStream != null) {

            try {
                while ((len = inputStream.read(data)) != -1) {
                    byteArrayOutputStream.write(data, 0, len);

                }
                result = new String(byteArrayOutputStream.toByteArray(), encode);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return result;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {


//        // TODO Auto-generated method stub
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("token", "SJS");
//        OwnProduct ownProduct=new OwnProduct();
//        ownProduct.setExchange("SJS");
//        ownProduct.setProdCode("Ag(T+D)");
//        ownProduct.setSort(15);
//
//
//        OwnProduct ownProduct1=new OwnProduct();
//        ownProduct1.setExchange("SJS");
//        ownProduct1.setProdCode("Au99.99");
//        ownProduct1.setSort(16);
//
//
//        List list=new ArrayList();
//        list.add(ownProduct);
//        list.add(ownProduct1);
//
//        String jsonstr=JSON.json(list);
//        params.put("data",jsonstr);
//        String result = sendPostMessage(params, "utf-8");
//
//        System.out.println("-result->>" + URLDecoder.decode(result,"gbk"));
        getMonthList("NJS","AG");
    }



    public static WyHqMonthRes getMonthList(String exchange,String prodCode){
        String requestPath="http://fa.163.com/interfaces/ngxcache/priceinfo/kline/getPreDailyList.do?apiLevel=9&channel=appstore&deviceId=7AE6E911-0080-47B8-B4B7-E58B2910EAE2&deviceType=iphone&productVersion=2.10&systemName=iPhone%20OS&systemVersion=9.1&uniqueId=7AE6E911-0080-47B8-B4B7-E58B2910EAE2&date=&goodsId=AU&num=100000&partnerId=ods#userconsent#";

        String result="";
        WyHqMonthRes  res=null;
        try {
            URL url=new URL(requestPath);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.connect();
            //发送执行请求
            //接收返回请求
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String line="";
            StringBuffer buffer=new StringBuffer();
            while((line=reader.readLine())!=null){
                buffer.append(line);
            }
            result=buffer.toString();
            res=JSON.parseObject(result, WyHqMonthRes.class);
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


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
