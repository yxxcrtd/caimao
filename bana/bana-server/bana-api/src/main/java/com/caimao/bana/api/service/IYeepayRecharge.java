package com.caimao.bana.api.service;

/**
 * 易宝充值相关接口方法
 * Created by WangXu on 2015/5/21.
 */
public interface IYeepayRecharge {

    /**
     * 生成手机充值的支付链接
     * @param userId    用户ID
     * @param orderAmount    充值金额
     * @param userIp    IP地址
     * @param userUA    手机浏览器标示
     * @return 手机支付链接
     * @throws Exception
     */
    public String doPreMobileCharge(Long userId, Long orderAmount, String userIp, String userUA) throws Exception;

    /**
     * 手机易宝支付回到返回的值
     * @param data  密文数据
     * @param encryptkey    密文数据
     * @return  支付结果
     * @throws Exception
     */
    public boolean doMobileCharge(String data, String encryptkey) throws Exception;

    /**
     * 查询易宝手机支付的结果
     * @param orderNo   订单ID
     * @return
     * @throws Exception
     */
    public boolean checkMobilePayRes(Long orderNo) throws Exception;

}
