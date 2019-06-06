/*
*Charge.java
*Created on 2015/5/8 10:35
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server;

import com.caimao.bana.api.entity.other.OTReturnDayInterestEntity;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.server.dao.CmOther.CmOtherActivityDao;
import com.caimao.bana.server.service.adjustOrder.AdjustOrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 返回多收的日融资利息
 * @author Administrator
 * @version 1.0.1
 */
public class ReturnDayInterest {

    public static void main(String[] args) throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/META-INF/spring/application.xml",
                "classpath:/META-INF/spring/application-mybatis.xml",
                "classpath:/META-INF/spring/application-redis.xml",
                "classpath:/META-INF/spring/application-email.xml");
        AdjustOrderService adjustOrderService = (AdjustOrderService) ac.getBean("adjustOrderService");

        CmOtherActivityDao cmOtherActivityDao = (CmOtherActivityDao) ac.getBean("cmOtherActivityDao");

        /**
         * 第一个参数： 返那一天的，格式 20150101
         * 第二个参数： 是否执行  1 打印用户并执行 0 只打印用户不执行
         */
        String workDate = args[0];
        String doSwitch = args[1];

        System.out.println("返回日期参数 ：" + workDate);
        System.out.println("是否执行 " + doSwitch);

        List<OTReturnDayInterestEntity> list = cmOtherActivityDao.queryDayInterestList(workDate);

        System.out.println("共查找到收取利息的记录数：" + list.size());

        ConcurrentLinkedDeque<OTReturnDayInterestEntity> strQueue = new ConcurrentLinkedDeque<>();
        strQueue.addAll(list);

        while ( ! strQueue.isEmpty()) {
            OTReturnDayInterestEntity entity = strQueue.poll();

            System.out.println(
                    String.format("用户ID：%s, 合约ID：%s，返息：%s， 收取时间：%s",
                            entity.getUserId(), entity.getContractNo(), entity.getOrderAmount(), entity.getCreateDatetime()));

            // 开启执行，并且 fee 大于 0
            if ("1".equals(doSwitch)) {
                Long adjustId = null;
                // 调用蓝补给用户进行补钱操作
                try {
                    adjustId = adjustOrderService.doSaveAdjustOrder(
                            entity.getUserId(), entity.getOrderAmount(), "假日多收日融资利息返息", ESeqFlag.COME.getCode(), "admin", false
                    );
                } catch (Exception e) {
                    System.out.println("用户 "+entity.getUserId()+" 添加失败，稍后进行重试");
                    System.out.println("错误信息：" + e.getMessage());
                    strQueue.offer(entity);
                    continue;
                }
                System.out.println("订单号： " + adjustId + " USER ID " + entity.getUserId() + " 执行成功");
            }
        }

        System.out.println("执行完毕");

    }



}
