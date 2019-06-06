package com.caimao.hq.utils;

import com.alibaba.dubbo.common.utils.StringUtils;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2015/10/1.
 */
public class QueueUtils {

    //存储socket接收到的消息的队列
    public static LinkedBlockingQueue<String> readQueueNJS = new LinkedBlockingQueue<String>();

    //存储socket接收到的消息的队列
    public static LinkedBlockingQueue<String> readQueueSJS= new LinkedBlockingQueue<String>();
    public static void add(String message,String exchange){

        if(!StringUtils.isBlank(exchange)){
            if(exchange.equalsIgnoreCase("NJS")){
                QueueUtils.readQueueNJS.add(message);
            }else if(exchange.equalsIgnoreCase("SJS")){
                QueueUtils.readQueueSJS.add(message);
            }

        }
    }
}
