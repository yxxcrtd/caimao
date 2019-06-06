package com.fmall.bana.utils.captcha;

import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaStore;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;

/**
 * 图片验证码验证
 * Created by Administrator on 2015/12/10.
 */
public class CustomGenericManageableCaptchaService extends GenericManageableCaptchaService {
    public CustomGenericManageableCaptchaService(CaptchaEngine captchaEngine, int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize, int captchaStoreLoadBeforeGarbageCollection) {
        super(captchaEngine, minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize, captchaStoreLoadBeforeGarbageCollection);
    }

    public CustomGenericManageableCaptchaService(CaptchaStore captchaStore, CaptchaEngine captchaEngine, int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize, int captchaStoreLoadBeforeGarbageCollection) {
        super(captchaStore, captchaEngine, minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize, captchaStoreLoadBeforeGarbageCollection);
    }

    public Boolean validateResponseForID(String ID, Object response) throws CaptchaServiceException {
        if(!this.store.hasCaptcha(ID)) {
            throw new CaptchaServiceException("验证码已失效，请重新刷新验证码！");
        } else {
            Boolean valid = this.store.getCaptcha(ID).validateResponse(((String)response).toLowerCase());
            return valid;
        }
    }

    public void removeCaptcha(String sessionId) {
        if(sessionId != null && this.store.hasCaptcha(sessionId)) {
            this.store.removeCaptcha(sessionId);
        }

    }
}
