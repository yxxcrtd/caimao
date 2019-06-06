package com.caimao.bana.api.service.getui;

/**
 * 个推服务任务接口
 * Created by Administrator on 2015/11/4.
 */
public interface IGetuiJobService {

    public void start();

    /**
     * 发送
     */
    public void send() throws Exception;
}
