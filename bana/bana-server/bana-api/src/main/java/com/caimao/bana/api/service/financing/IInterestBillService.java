package com.caimao.bana.api.service.financing;

import com.caimao.bana.api.entity.req.F830201Req;
import com.caimao.bana.api.entity.req.F830202Req;

/**
 * 利息相关的业务逻辑类
 * @author yanjg
 * 2015年6月5日
 */
public interface IInterestBillService {
    public abstract F830201Req queryInterestBillPage(F830201Req req) throws Exception;

    public abstract F830202Req queryAccruedInterestBillPage(F830202Req req) throws Exception;

}
