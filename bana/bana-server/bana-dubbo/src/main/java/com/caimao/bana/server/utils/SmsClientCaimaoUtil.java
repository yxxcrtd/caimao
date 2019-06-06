package com.caimao.bana.server.utils;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class SmsClientCaimaoUtil {
    private static String smsUrl = "http://218.241.153.53/smsport/default.asmx/SendSms";
    private static String userName = "bjpl01";
    private static String password = "122131";
    private static String suffer = "【财猫】";
    private static String longnum = "";

    public static String getHttp(String mobile, String content) {
        String responseMsgString = "";
        HttpClient httpClient = new HttpClient();
        String sendurl = smsUrl + "?username=" + userName + "&password=" +
                password + "&phonelist=" + mobile + "&msg=" + content +
                suffer + "&longnum=" + longnum;

        GetMethod getMethod = new GetMethod(sendurl);
        getMethod.getParams().setParameter("http.method.retry-handler",
                new DefaultHttpMethodRetryHandler());
        try {
            httpClient.executeMethod(getMethod);

            byte[] responseBody = getMethod.getResponseBody();

            responseMsgString = new String(responseBody);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }
        return responseMsgString;
    }

    public static String postHttp(String mobile, String content) {
        String responseMsgString = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset("UTF-8");
        PostMethod postMethod = new PostMethod(smsUrl);

        postMethod.addParameter("username", userName);
        postMethod.addParameter("password", password);
        postMethod.addParameter("phonelist", mobile);
        postMethod.addParameter("msg", content + suffer);
        postMethod.addParameter("longnum", longnum);
        try {
            httpClient.executeMethod(postMethod);
            responseMsgString = postMethod.getResponseBodyAsString().trim();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsgString;
    }
}
