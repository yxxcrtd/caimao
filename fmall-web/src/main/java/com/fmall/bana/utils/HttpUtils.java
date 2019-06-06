package com.fmall.bana.utils;

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
    private static String PATH = "http://172.32.1.218:8097/api/gjshq/snapshot/query?prodCode=AG.NJS";
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
                        "application/x-www-form-urlencoded;charset=utf-8");
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
        testImediaRegister();
    }


    public static void testImediaRegister() {
        String LIMEI_HTTP = "http://172.32.1.218:8097/api/gjshq/candle/queryHistory?prodCode=AG.NJS&cycle=6";
        String requestUrl = LIMEI_HTTP;
        System.out.println(requestUrl);
        String result = "";
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8");
            connection.connect();
            //发送执行请求

            //接收返回请求
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
            String line = "";
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
            System.out.println("result=" + result);
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
