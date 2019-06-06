package com.fmall.bana.controller.api.user.helper;

import com.caimao.hq.api.entity.OwnProduct;
import com.caimao.hq.api.service.IHQService;
import com.fmall.bana.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 用户服务的附属方法
 * Created by Administrator on 2015/11/9.
 */
@Repository
public class UserApiHelper {

    @Resource
    private RedisUtils redisUtils;
    @Autowired
    private IHQService hqService;

    /**
     * 贵金属默认的几个自选股添加
     *
     * @param userId 用户ID
     */
    public void gjsDefaultOwnProductOpt(Long userId) {
        String redisKey = "hGjsDefaultOwnProductKeys";
        // 判断下redis中的值，是否已经添加过自选了
        Object redisValue = this.redisUtils.hGet(redisKey, userId.toString());
        if (redisValue == null) {
            // 没有添加过，进行添加
            Integer index = 4;
            String exchangeStrs = "NJS,SJS,SJS,SJS";
            String codeStrs = "AG,Ag(T+D),mAu(T+D),Au(T+D)";
            String[] exchanges = exchangeStrs.split(",");
            String[] codes = codeStrs.split(",");

            for (Integer i = 0; i < index; i++) {
                OwnProduct product = new OwnProduct();
                product.setUserId(userId);
                product.setExchange(exchanges[i]);
                product.setProdCode(codes[i]);
                product.setSort(i);
                try {
                    hqService.insertOwnProduct(product);
                } catch (Exception ignore) {
                }
            }
            this.redisUtils.hSet(redisKey, userId.toString(), "1");
        }
    }
}
