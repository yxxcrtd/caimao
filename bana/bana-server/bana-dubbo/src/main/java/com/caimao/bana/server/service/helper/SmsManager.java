/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司.
 *All Rights Reserved
 */
package com.caimao.bana.server.service.helper;

/**
 * @author yanjg
 * 2015年4月28日
 */

import com.caimao.bana.api.entity.TpzSmsCheckcodeEntity;
import com.caimao.bana.api.entity.TpzSmsOutEntity;
import com.caimao.bana.api.enums.ESmsStatus;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.dao.sms.TpzSmsCheckcodeDao;
import com.caimao.bana.server.dao.sms.TpzSmsOutDao;
import com.caimao.bana.server.utils.Constants;
import com.hundsun.jresplus.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Service
public class SmsManager implements ISmsManager{
    @Autowired
    private TpzSmsOutDao smsOutDAO;

    @Autowired
    private TpzSmsCheckcodeDao smsCheckcodeDAO;
    private static final ExecutorService SMS_POOL = Executors.newFixedThreadPool(3);

    public void checkSmsCode(String mobile, String smsBizType, String checkCode) throws CustomerException {
        // 如果是测试环境，不进行发送短信验证码
        if (Constants.IS_DEBUG && checkCode.equals("888888")) {
            return;
        }
        if ((StringUtil.isEmpty(mobile)) || (StringUtil.isEmpty(smsBizType)) || (StringUtil.isEmpty(checkCode))) {
            throw new CustomerException("参数非法",83090,"BizServiceException");
        }
        TpzSmsCheckcodeEntity cond = new TpzSmsCheckcodeEntity();
        cond.setMobile(mobile);
        cond.setSmsBizType(smsBizType);
        TpzSmsCheckcodeEntity smsCheckcode = this.smsCheckcodeDAO.getByMobileAndBiz(cond);
        if (null == smsCheckcode) {
            throw new CustomerException("短信验证码错误",83091,"BizServiceException");
        }
        if (smsCheckcode.getCheckTimes() >= 6) {
            throw new CustomerException("短信验证码错误已超过6次，请重新获取",83092,"BizServiceException");
        }
        String code = smsCheckcode.getCheckCode();
        if (!code.equals(checkCode)) {
            updatteValid(smsCheckcode, false);
            throw new CustomerException( "短信验证码错误",83093,"NoRollbackException");
        }
        if (new Date().after(smsCheckcode.getInvalidDatetime())) {
            updatteValid(smsCheckcode, false);
            throw new CustomerException("短信验证码已失效，请重新获取",83094,"NoRollbackException");
        }
        updatteValid(smsCheckcode, true);
    }

    private void updatteValid(TpzSmsCheckcodeEntity smsCheckcode, boolean flag) {
        TpzSmsCheckcodeEntity update = new TpzSmsCheckcodeEntity();
        update.setId(smsCheckcode.getId());
        update.setLastCheckDatetime(new Date());
        if (!flag) {
            update.setCheckTimes(smsCheckcode.getCheckTimes() + 1);
        }
        this.smsCheckcodeDAO.update(update);
    }

//      public IDataset querySmsOutWithPage(F831905Req req)
//      {
//        List list = this.smsOutDAO.queryF831905ResWithPage(req);
//        IDataset ds = DatasetService.getDefaultInstance().getDataset(list, F831905Res.class);
//
//        ds.setTotalCount(req.getTotalCount().intValue());
//        ds.setDatasetName("result");
//        return ds;
//      }

//      public F831918Req querySmsOutList(F831918Req req)
//      {
//        List list = this.smsOutDAO.queryF831918ResWithPage(req);
//        req.setItems(list);
//        return req;
//      }

//      public void doRefreshSmsOutStatus(F831919Req req)
//      {
//        SmsOut sms = new SmsOut();
//        sms = (SmsOut)this.smsOutDAO.getById(req.getId());
//        if (sms != null)
//          try {
//            BeanUtils.copyProperties(sms, req);
//            this.smsOutDAO.update(sms);
//          } catch (IllegalAccessException e) {
//            e.printStackTrace();
//          } catch (InvocationTargetException e) {
//            e.printStackTrace();
//          }
//      }

    public void doSaveSmsOut(final String mobile, final String smsContent, final String smsType) {
        SMS_POOL.execute(new Runnable()
        {
            public void run() {
                TpzSmsOutEntity so = new TpzSmsOutEntity();
                so.setMobile(mobile);
                so.setSmsContent(smsContent);
                so.setSmsType(smsType);
                so.setSmsStatus(ESmsStatus.SEND.getCode());
                so.setCreateDatetime(new Date());
                SmsManager.this.smsOutDAO.save(so);
            }
        });
    }

}
