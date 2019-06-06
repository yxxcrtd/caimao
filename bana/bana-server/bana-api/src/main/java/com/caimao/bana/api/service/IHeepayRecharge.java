package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.HeepayNoticeEntity;
import com.caimao.bana.api.entity.HeepayRequestEntity;
import com.caimao.bana.api.entity.HeepaySubmitEntity;
import com.caimao.bana.api.entity.TpzChargeOrderEntity;

import java.util.List;

/**
 * 汇付宝充值相关接口
 * Created by WangXu on 2015/4/24.
 */
public interface IHeepayRecharge {

    public TpzChargeOrderEntity doCharge(HeepayRequestEntity heepayRequestEntity) throws Exception;

    public Boolean checkeHeepayNoticeSign(HeepayNoticeEntity heepayNoticeEntity) throws Exception;

    public boolean checkHeepayPayResult(Long orderNo) throws Exception;

}
