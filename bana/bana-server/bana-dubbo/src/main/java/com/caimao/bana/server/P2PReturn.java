/*
*Charge.java
*Created on 2015/5/8 10:35
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server;

import com.caimao.bana.api.entity.res.other.FOtherP2PReturnFeeRes;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.server.dao.CmOther.CmOtherActivityDao;
import com.caimao.bana.server.service.adjustOrder.AdjustOrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class P2PReturn {

    public static void main(String[] args) throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/META-INF/spring/application.xml",
                "classpath:/META-INF/spring/application-mybatis.xml",
                "classpath:/META-INF/spring/application-redis.xml");
        AdjustOrderService adjustOrderService = (AdjustOrderService) ac.getBean("adjustOrderService");

        CmOtherActivityDao cmOtherActivityDao = (CmOtherActivityDao) ac.getBean("cmOtherActivityDao");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**
         * 第一个参数： 开始的日期，格式 2015-01-01
         * 第二个参数： 开始的时间，格式 02:01:03
         * 第三个参数： 结束的日期，格式 2015-02-02
         * 第四个参数： 结束的时间，格式 03:02:01
         * 第五个参数： 返回的类型，0 正常返回，1 补差返回
         * 第六个参数： 是否执行  1 打印用户并执行 0 只打印用户不执行
         */
        String beginDate = args[0] + " " + args[1];
        String endDate = args[2] + " " + args[3];
        String returnType = args[4];
        String doSwitch = args[5];

        System.out.println("开始时间 ：" + beginDate);
        System.out.println("结束时间 ：" + endDate);
        System.out.println("返回类型 " + returnType + " 是否执行 " + doSwitch);

        List<FOtherP2PReturnFeeRes> list = cmOtherActivityDao.queryP2PReturnFeeList();

        System.out.println("共查找到首次投资条件的记录数：" + list.size());

        ConcurrentLinkedDeque<FOtherP2PReturnFeeRes> strQueue = new ConcurrentLinkedDeque<>();
        strQueue.addAll(list);

        while ( ! strQueue.isEmpty()) {
            FOtherP2PReturnFeeRes res = strQueue.poll();

            // 判断时间的 开始时间小于投资时间  并且 结束时间大于投资时间
            if (sdf.parse(beginDate).compareTo(res.getInvestCreated()) <= 0 && sdf.parse(endDate).compareTo(res.getInvestCreated()) == 1) {
                // 这个是符合条件的
            } else {
                System.out.println("时间不符：" + res.getUserRealName() + " --  " + res.getInvestCreated());
                continue;
            }

            // 判断加多少钱
            Long retFee = 0L;
            if (returnType.equals("1")) {
                // 将原来的  100000 、 500000的那些差额进行返还
                if (res.getInvestValue() == 500000L) {
                    retFee = 6000L;
                } else if (res.getInvestValue() == 100000L) {
                    retFee = 2000L;
                } else {
                    System.out.println("金额不符：" + res.getUserRealName() + " --  " + res.getInvestValue());
                    continue;
                }
            } else {
                if (res.getInvestValue() >= 500000L) {
                    retFee = 8000L;
                } else if (res.getInvestValue() >= 100000L) {
                    retFee = 2000L;
                } else {
                    System.out.println("金额不符：" + res.getUserRealName() + " --  " + res.getInvestValue());
                    continue;
                }
            }

            System.out.println(
                    String.format("用户ID：%s, 名称：%s， 手机号：%s， 投资额：%s， 返利: %s",
                            res.getUserId(), res.getUserRealName(), res.getMobile(), res.getInvestValue() / 100, retFee));

            // 开启执行，并且 fee 大于 0
            if ("1".equals(doSwitch) && retFee > 0L) {
                Long adjustId = null;
                // 调用蓝补给用户进行补钱操作
                try {
                    adjustId = adjustOrderService.doSaveAdjustOrder(
                            res.getUserId(), retFee, "P2P投资首次返利", ESeqFlag.COME.getCode(), "admin", false
                    );
                } catch (Exception e) {
                    System.out.println("用户 "+res.getUserRealName()+" 添加失败，稍后进行重试");
                    System.out.println("错误信息：" + e.getMessage());
                    strQueue.offer(res);
                    continue;
                }
                System.out.println("订单号： " + adjustId + " USER ID " + res.getUserId() + " 用户 " + res.getUserRealName() + " 执行成功");
            }
        }

        System.out.println("执行完毕");

    }



}
