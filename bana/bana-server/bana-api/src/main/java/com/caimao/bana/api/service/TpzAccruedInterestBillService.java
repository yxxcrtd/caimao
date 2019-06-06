/*
*TpzAccruedInterestBillService.java
*Created on 2015/5/23 11:54
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.TpzAccruedInterestBillEntity;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0.1
 */
public interface TpzAccruedInterestBillService {

    public List<TpzAccruedInterestBillEntity> queryBillList(TpzAccruedInterestBillEntity tpzAccruedInterestBillEntity) throws Exception;

    public List<TpzAccruedInterestBillEntity> queryBillListByWorkDate(String workDate) throws Exception;
}
