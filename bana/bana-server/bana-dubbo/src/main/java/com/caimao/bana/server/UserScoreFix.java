/*
*Charge.java
*Created on 2015/5/8 10:35
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server;

import com.caimao.bana.api.entity.InviteReturnHistoryEntity;
import com.caimao.bana.api.service.InviteReturnHistoryService;
import com.caimao.bana.api.service.IUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * 返回多收的日融资利息
 * @author Administrator
 * @version 1.0.1
 */
public class UserScoreFix {
    public static void main(String[] args) throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/META-INF/spring/application.xml",
                "classpath:/META-INF/spring/application-mybatis.xml",
                "classpath:/META-INF/spring/application-redis.xml");
        InviteReturnHistoryService inviteReturnHistoryService = (InviteReturnHistoryService) ac.getBean("inviteReturnHistoryService");
        IUserService userService = (IUserService) ac.getBean("userService");
        List<InviteReturnHistoryEntity> returnList = inviteReturnHistoryService.getInviteReturnHistoryByType(0L);
        if(returnList != null){
            for(InviteReturnHistoryEntity inviteReturnHistoryEntity:returnList){
                try{
                    if(inviteReturnHistoryEntity.getUserId()  == 800831045632001L) continue;
                    userService.addScoreNoRecord(inviteReturnHistoryEntity.getUserId(), inviteReturnHistoryEntity.getReturnAmount().intValue() * 19);
                }catch(Exception e){
                    throw new Exception(inviteReturnHistoryEntity.getId() + "增加积分失败！");
                }
            }
        }
    }
}
