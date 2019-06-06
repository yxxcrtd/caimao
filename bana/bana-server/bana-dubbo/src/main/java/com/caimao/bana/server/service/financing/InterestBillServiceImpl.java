/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.server.service.financing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caimao.bana.api.entity.req.F830201Req;
import com.caimao.bana.api.entity.req.F830202Req;
import com.caimao.bana.api.service.financing.IInterestBillService;
import com.caimao.bana.server.dao.AccruedInterestBillDAO;
import com.caimao.bana.server.dao.InterestBillDAO;

/**
 * @author yanjg 2015年6月5日
 */
@Service("interestBillService")
public class InterestBillServiceImpl implements IInterestBillService {
    @Autowired
    private InterestBillDAO interestBillDAO;

    @Autowired
    private AccruedInterestBillDAO accruedInterestBillDAO;

    public F830201Req queryInterestBillPage(F830201Req req) {
        List list = this.interestBillDAO.queryF830201ResWithPage(req);
        req.setItems(list);
        return req;
    }

    public F830202Req queryAccruedInterestBillPage(F830202Req req) {
        List list = this.accruedInterestBillDAO.queryF830202ResWithPage(req);
        req.setItems(list);
        return req;
    }

}
