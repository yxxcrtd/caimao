/*
*Charge.java
*Created on 2015/5/8 10:35
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server;

import com.caimao.bana.api.entity.HisLoanContractEntity;
import com.caimao.bana.api.entity.TpzHomsAccountChildEntity;
import com.caimao.bana.api.entity.TpzHomsAccountJourEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountLoanLogEntity;
import com.caimao.bana.server.dao.HisLoanContractDao;
import com.caimao.bana.server.dao.homs.TpzHomsAccountChildDao;
import com.caimao.bana.server.dao.homs.TpzHomsAccountDao;
import com.caimao.bana.server.dao.zeusStatistics.ZeusStatisticsDao;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * homs借款历史纪录修复
 */
public class HomsLoanLogFix {
    public static void main(String[] args) throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/META-INF/spring/application.xml",
                "classpath:/META-INF/spring/application-mybatis.xml",
                "classpath:/META-INF/spring/application-redis.xml",
                "classpath:/META-INF/spring/application-email.xml");
        HisLoanContractDao hisLoanContractDao = (HisLoanContractDao) ac.getBean("hisLoanContractDao");
        TpzHomsAccountDao tpzHomsAccountDao = (TpzHomsAccountDao) ac.getBean("tpzHomsAccountDao");
        TpzHomsAccountChildDao tpzHomsAccountChildDao = (TpzHomsAccountChildDao) ac.getBean("tpzHomsAccountChildDao");
        ZeusStatisticsDao zeusStatisticsDao = (ZeusStatisticsDao) ac.getBean("zeusStatisticsDao");

        //查询历史合约
        List<HisLoanContractEntity> hisLoanContractList = hisLoanContractDao.queryAllHisLoanContract();
        if(hisLoanContractList != null){
            for (HisLoanContractEntity hisLoanContractEntity:hisLoanContractList){
                try{
                    //根据合约查找相关流水
                    Long transAmount = hisLoanContractEntity.getCashAmount() + hisLoanContractEntity.getLoanAmount();
                    TpzHomsAccountJourEntity tpzHomsAccountJourEntity = tpzHomsAccountDao.queryRecordByContractInfo(hisLoanContractEntity.getUserId(), transAmount, hisLoanContractEntity.getCreateDatetime());
                    if(tpzHomsAccountJourEntity != null){
                        //根据流水查找资金账户id
                        TpzHomsAccountChildEntity tpzHomsAccountChildEntity = tpzHomsAccountChildDao.queryAccountChildByAccount(tpzHomsAccountJourEntity.getHomsFundAccount(), tpzHomsAccountJourEntity.getHomsCombineId());
                        if(tpzHomsAccountChildEntity != null){
                            ZeusHomsAccountLoanLogEntity zeusHomsAccountLoanLogEntity = new ZeusHomsAccountLoanLogEntity();
                            zeusHomsAccountLoanLogEntity.setUserId(hisLoanContractEntity.getUserId());
                            zeusHomsAccountLoanLogEntity.setHomsCombineId(tpzHomsAccountJourEntity.getHomsCombineId());
                            zeusHomsAccountLoanLogEntity.setCreateDatetime(hisLoanContractEntity.getCreateDatetime());
                            zeusHomsAccountLoanLogEntity.setAssetId(tpzHomsAccountChildEntity.getAssetId());
                            //检测是否存在
                            List<ZeusHomsAccountLoanLogEntity> homsAccountLoanLogExit = zeusStatisticsDao.queryHomsAccountLoanLog(zeusHomsAccountLoanLogEntity);
                            if(homsAccountLoanLogExit.size() == 0){
                                //保存数据
                                zeusHomsAccountLoanLogEntity.setPzAccountId(tpzHomsAccountJourEntity.getPzAccountId());
                                zeusHomsAccountLoanLogEntity.setContractNo(hisLoanContractEntity.getContractNo());
                                zeusHomsAccountLoanLogEntity.setHomsFundAccount(tpzHomsAccountJourEntity.getHomsFundAccount());
                                zeusHomsAccountLoanLogEntity.setHomsManageId(tpzHomsAccountJourEntity.getHomsManageId());
                                zeusHomsAccountLoanLogEntity.setBeginAmount(transAmount);
                                zeusHomsAccountLoanLogEntity.setOperatorNo(tpzHomsAccountChildEntity.getOperatorNo());
                                zeusStatisticsDao.saveHomsAccountLoanLog(zeusHomsAccountLoanLogEntity);
                            }
                        }
                    }
                }catch(Exception e){
                    System.out.println("修复历史合约编号 "+ hisLoanContractEntity.getContractNo() + "失败");
                }
            }
        }
    }
}
