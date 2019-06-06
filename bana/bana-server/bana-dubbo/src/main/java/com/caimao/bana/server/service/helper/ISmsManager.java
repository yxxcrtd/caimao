/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.server.service.helper;

import com.caimao.bana.common.api.exception.CustomerException;


/**
 * @author yanjg 2015年4月28日
 */
public abstract interface ISmsManager {
    public abstract void checkSmsCode(String paramString1, String paramString2, String paramString3)throws CustomerException;

//    @Service(functionId = "831905")
//    public abstract IDataset querySmsOutWithPage(F831905Req paramF831905Req);
//
//    @Service(functionId = "831918")
//    public abstract F831918Req querySmsOutList(F831918Req paramF831918Req);
//
//    @Service(functionId = "831919")
//    public abstract void doRefreshSmsOutStatus(F831919Req paramF831919Req);

    public abstract void doSaveSmsOut(String paramString1, String paramString2, String paramString3);
}
