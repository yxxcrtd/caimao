package com.caimao.bana.server.service.sms;

import com.caimao.bana.api.entity.TpzSmsCheckcodeEntity;
import com.caimao.bana.api.entity.TpzSmsOutEntity;
import com.caimao.bana.api.entity.req.FSmsListReq;
import com.caimao.bana.api.enums.ESmsBizType;
import com.caimao.bana.api.enums.ESmsStatus;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.api.service.SmsService;
import com.caimao.bana.server.annotation.SmsSendCount;
import com.caimao.bana.server.dao.sms.TpzSmsCheckcodeDao;
import com.caimao.bana.server.dao.sms.TpzSmsOutDao;
import com.caimao.bana.server.dao.sysParameter.SysParameterDao;
import com.caimao.bana.server.utils.Constants;
import com.caimao.bana.server.utils.SmsClientUtil;
import com.huobi.commons.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service("smsService")
public class SmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Autowired
    private TpzSmsOutDao smsOutDAO;
    @Autowired
    private TpzSmsCheckcodeDao smsCheckcodeDAO;
    @Autowired
    private SysParameterDao parameterDAO;
    private static int CHECK_CODE_LENGTH = 6;

    /*
     * 发送短信验证码
     *
     */
    @Override
    @SmsSendCount
    public void doSendSmsCheckCode(String mobile, String smsBizType) throws CustomerException {
        // 如果是测试环境，不进行发送短信验证码
        if (Constants.IS_DEBUG) {
            return;
        }
        if ((StringUtil.isEmpty(mobile)) || (StringUtil.isEmpty(smsBizType))) {
            throw new CustomerException("参数非法", 83090, "BizServiceException");
        }

        // 加黑手机号
        if (mobile.equals("18538721888") || mobile.equals("15513657818")) {
            throw new CustomerException("手机号已加黑，不允许发送验证码", 83090, "BizServiceException");
        }
        // 查询这个手机，一分钟内，发送超过一条，就不在发送了
        Integer smsSendCnt = this.smsOutDAO.getSmsCount(mobile, smsBizType, new Date(new Date().getTime() - (60 * 1000)));
        if (smsSendCnt >= 1) {
            throw new CustomerException("验证码发送过于频繁，请稍后再试", 83090, "BizServiceException");
        }

        // 查询这个手机号，在一个小时之内发送过几条验证码短信，如果超过3条，就不在发送了
        smsSendCnt = this.smsOutDAO.getSmsCount(mobile, smsBizType, new Date(new Date().getTime() - (60 * 60 * 1000)));
        if (smsSendCnt >= 3) {
            throw new CustomerException("验证码发送过于频繁，请一小时后再试", 83090, "BizServiceException");
        }

        // 查询这个手机号，在24小时内发送了操作 8 条，就不在发送了
        smsSendCnt = this.smsOutDAO.getSmsCount(mobile, smsBizType, new Date(new Date().getTime() - (24 * 60 * 60 * 1000)));
        if (smsSendCnt >= 8) {
            throw new CustomerException("验证码发送过于频繁，请24小时后再试", 83090, "BizServiceException");
        }

        String sms_content = this.parameterDAO.getById("smscheckcodecnt").getParamValue();

        String validMinute = this.parameterDAO.getById("smscheckcodetime").getParamValue();

        String checkCode = this.parameterDAO.getById("smscheckcode").getParamValue();
        if ((checkCode == null) || (checkCode.trim().equals(""))) {
            checkCode = createAuthCheckcode(CHECK_CODE_LENGTH);
        }
        sms_content = sms_content.replace("a", checkCode).replace("b", validMinute);

        TpzSmsOutEntity smsOut = new TpzSmsOutEntity();
        smsOut.setMobile(mobile);
        smsOut.setSmsType(smsBizType);
        smsOut.setCreateDatetime(new Date());
        smsOut.setSmsStatus(ESmsStatus.SEND.getCode());
        //smsOut.setSmsStatus(ESmsStatus.SEND_SUCCESS.getCode());
        smsOut.setSmsContent(sms_content);

        TpzSmsCheckcodeEntity cond = new TpzSmsCheckcodeEntity();
        cond.setMobile(mobile);
        cond.setSmsBizType(smsBizType);
        TpzSmsCheckcodeEntity smsCheckcode = this.smsCheckcodeDAO.getByMobileAndBiz(cond);
        boolean exist = true;
        if (smsCheckcode == null) {
            smsCheckcode = new TpzSmsCheckcodeEntity();
            exist = false;
        }
        smsCheckcode.setCheckCode(checkCode);
        Date now = new Date();
        smsCheckcode.setCheckcodeSetDatetime(now);
        smsCheckcode.setCheckTimes(Integer.valueOf(0));
        smsCheckcode.setMobile(mobile);
        smsCheckcode.setSmsBizType(smsBizType);
        smsCheckcode.setInvalidDatetime(new Date(now.getTime() + Integer.parseInt(validMinute) * 60000));

        if (exist)
            this.smsCheckcodeDAO.update(smsCheckcode);
        else {
            this.smsCheckcodeDAO.save(smsCheckcode);
        }
        this.smsOutDAO.save(smsOut);
        // 发送短信
//        try {
//            this.sendSms(mobile, sms_content);
//        } catch (Exception e) {
//
//        }
    }

    private String createAuthCheckcode(int num) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < num; i++)
            result.append(random.nextInt(10));
        return result.toString();
    }

    public void sendSms(String mobile, String content) throws Exception {
        SmsClientUtil.sendMsg(mobile, content);
    }

    /**
     * 查询已发短信但未注册的用户
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FSmsListReq queryUnRegisterMobileWithPage(FSmsListReq req) throws Exception {
        req.setItems(this.smsOutDAO.queryUnRegisterMobileWithPage(req));
        return req;
    }

    /**
     * 发送短信普通
     * @param mobile 手机号码
     * @param content 内容
     * @throws Exception
     */
    @Override
    public void doSendSmsCustom(String mobile, String content) throws Exception {
        TpzSmsOutEntity smsOut = new TpzSmsOutEntity();
        smsOut.setMobile(mobile);
        smsOut.setSmsType(ESmsBizType.CUSTOM.getCode());
        smsOut.setCreateDatetime(new Date());
        smsOut.setSmsStatus(ESmsStatus.SEND.getCode());
        smsOut.setSmsContent(content);
        this.smsOutDAO.save(smsOut);
    }
}
