package com.caimao.bana.server;

import com.caimao.bana.api.entity.p2p.P2PInvestRecordEntity;
import com.caimao.bana.api.service.IP2PService;
import com.caimao.bana.server.dao.p2p.P2PInvestRecordDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class P2PPrepayment {

    public static void main(String[] args) throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                "classpath:/META-INF/spring/application.xml",
                "classpath:/META-INF/spring/application-mybatis.xml",
                "classpath:/META-INF/spring/application-redis.xml",
                "classpath:/META-INF/spring/application-email.xml");

        P2PInvestRecordDao investRecordDao = (P2PInvestRecordDao) ac.getBean("P2PInvestRecordDao");
        IP2PService ip2PService = (IP2PService) ac.getBean("P2PService");

        List<P2PInvestRecordEntity> list = investRecordDao.queryPrepaymentList();
        System.out.println("总计 " + list.size() + "条记录需要执行");

        ConcurrentLinkedDeque<P2PInvestRecordEntity> strQueue = new ConcurrentLinkedDeque<>();
        strQueue.addAll(list);

        while (!strQueue.isEmpty()) {
            P2PInvestRecordEntity investRecord = strQueue.poll();
            try {
                ip2PService.doPrepayment(investRecord);
                System.out.println(investRecord.getInvestId() + " 成功");
            } catch (Exception e) {
                System.out.println(investRecord.getInvestId() + " 失败");
            }
        }

        System.out.println("执行完毕");
    }
}
