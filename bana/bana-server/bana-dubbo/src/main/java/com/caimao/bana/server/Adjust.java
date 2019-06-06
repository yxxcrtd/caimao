/*
*Charge.java
*Created on 2015/5/8 10:35
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server;

import com.caimao.bana.api.entity.CmOtherActivityEntity;
import com.caimao.bana.api.enums.ESeqFlag;
import com.caimao.bana.server.dao.CmOther.CmOtherActivityDao;
import com.caimao.bana.server.service.adjustOrder.AdjustOrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class Adjust {

    public static void main(String[] args) throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/META-INF/spring/application.xml",
                "classpath:/META-INF/spring/application-mybatis.xml",
                "classpath:/META-INF/spring/application-redis.xml");
        AdjustOrderService adjustOrderService = (AdjustOrderService) ac.getBean("adjustOrderService");

        CmOtherActivityDao cmOtherActivityDao = (CmOtherActivityDao) ac.getBean("cmOtherActivityDao");

        // 活动的开始时间
        String activityBeginDate = "2015-05-11 20:00:00";

        /**
         * 第一个参数： 1 （日配） 30 （月配）
         * 第二个参数： 统计的开始时间，格式 2015-05-12，小时统一使用 16:00:00
         * 第三个参数： 统计的结束时间，格式 2015-05-13，小时统一使用 16:00:00
         * 第四个参数： 是否执行  1 打印用户并执行 0 只打印用户不执行
         */
        String contractType = args[0];
        String beginDate = args[1] + " 00:00:00";
        String endDate = args[2] + " 00:00:00";
        String doSwitch = args[3];

        List<CmOtherActivityEntity> cmContractList = null;
        if ("1".equals(contractType)) {
            cmContractList = cmOtherActivityDao.queryDaysContract(beginDate, endDate);
        } else if ("30".equals(contractType)) {
            cmContractList = cmOtherActivityDao.queryMonthsContract(beginDate, endDate);
        } else {
            System.out.println("期货合约类型输入错误");
            return;
        }

        System.out.println("共查找到符合条件的记录数：" + cmContractList.size());

        ConcurrentLinkedDeque<CmOtherActivityEntity> strQueue = new ConcurrentLinkedDeque<>();
        strQueue.addAll(cmContractList);

        while ( ! strQueue.isEmpty()) {
            CmOtherActivityEntity cmContract = strQueue.poll();
            // 检查用户之前是否已经进行过返息
            if ("1".equals(contractType)) {
                List oldDayList = cmOtherActivityDao.queryOldDaysContract(cmContract.getUserId(), activityBeginDate, beginDate);
                List oldDayHisList = cmOtherActivityDao.queryOldHisDaysContract(cmContract.getUserId(), activityBeginDate, beginDate);
                if (oldDayList.size() > 0 || oldDayHisList.size() > 0) {
                    System.out.println(String.format("用户 %s 已经进行过返息，当前进行的合约数 %s，历史融资的合约数 %s", cmContract.getUserRealName(), oldDayList.size(), oldDayHisList.size()));
                    continue;
                }
            } else if ("30".equals(contractType)) {
                List oldMonthsList = cmOtherActivityDao.queryOldMonthsContract(cmContract.getUserId(), activityBeginDate, beginDate);
                List oldHisMonthsList = cmOtherActivityDao.queryOldHisMonthsContract(cmContract.getUserId(), activityBeginDate, beginDate);
                if (oldMonthsList.size() > 0 || oldHisMonthsList.size() > 0) {
                    System.out.println(String.format("用户 %s 已经进行过返息，当前进行的合约数 %s，历史融资的合约数 %s", cmContract.getUserRealName(), oldMonthsList.size(), oldHisMonthsList.size()));
                    continue;
                }
            }
            System.out.println(
                    String.format("合约编号：%s, 用户编号：%s， 用户姓名：%s， 借款数量：%s， 利率：%s, 返利: %s",
                            cmContract.getContractNo(), cmContract.getUserId(), cmContract.getUserRealName(),
                            cmContract.getLoanAmount(), cmContract.getInterestRate(), cmContract.getFee()));
            // 开启执行，并且 fee 大于 0
            if ("1".equals(doSwitch) && cmContract.getFee() > 0) {
                Long adjustId = null;
                // 调用蓝补给用户进行补钱操作
                try {
                    adjustId = adjustOrderService.doSaveAdjustOrder(
                            cmContract.getUserId(), cmContract.getFee(), "三天免息活动，C "+cmContract.getContractNo(), ESeqFlag.COME.getCode(), "admin", false
                    );
                } catch (Exception e) {
                    System.out.println("合约 "+cmContract.getContractNo()+" 添加失败，稍后进行重试");
                    System.out.println("错误信息：" + e.getMessage());
                    strQueue.offer(cmContract);
                    continue;
                }
                System.out.println("订单号： " + adjustId + " 合约 " + cmContract.getContractNo() + " 用户 " + cmContract.getUserRealName() + " 执行成功");
            }
        }

        System.out.println("执行完毕");

    }



}
