package com.caimao.bana.api.service.ybk;

/**
 * 邮币卡定时
 */
public interface IYBKJobService {
    /**
     * 行情数据抓取
     * @throws Exception
     */
    public void quotationData() throws Exception;

    /**
     * 文章抓取
     * @throws Exception
     */
    public void articleData() throws Exception ;
}
