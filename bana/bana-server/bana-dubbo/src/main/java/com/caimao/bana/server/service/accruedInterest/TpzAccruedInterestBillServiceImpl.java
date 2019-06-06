/*
*TpzAccruedInterestBillServiceImpl.java
*Created on 2015/5/23 11:57
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server.service.accruedInterest;

import com.caimao.bana.api.entity.TpzAccruedInterestBillEntity;
import com.caimao.bana.api.service.TpzAccruedInterestBillService;
import com.caimao.bana.server.dao.accruedInterest.TpzAccruedInterestBillDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0.1
 */
@Service(value = "tpzAccruedInterestBillService")
public class TpzAccruedInterestBillServiceImpl implements TpzAccruedInterestBillService {

    @Resource
    private TpzAccruedInterestBillDao tpzAccruedInterestBillDao;

    @Override
    public List<TpzAccruedInterestBillEntity> queryBillList(TpzAccruedInterestBillEntity tpzAccruedInterestBillEntity) throws Exception {
        return tpzAccruedInterestBillDao.queryAccruedInterestBillList(tpzAccruedInterestBillEntity);
    }

    @Override
    public List<TpzAccruedInterestBillEntity> queryBillListByWorkDate(String workDate) throws Exception {
        return tpzAccruedInterestBillDao.queryAccruedInterestBillListByWorkDate(workDate);
    }
}
