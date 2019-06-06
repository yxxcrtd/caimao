/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.huobi.commons.utils.HsDateUtil;

/**
 * @author yanjg
 * 2015年5月22日
 */
public class F830904Req extends QueryBase{
    private String pushUserId;
    private String createDatetimeBegin;
    private String createDatetimeEnd;
    private String isRead;
    private String pushType;
     public F830904Req()
     {
     }

     public String getPushType()
     {
         return pushType;
     }

     public void setPushType(String pushType)
     {
         this.pushType = pushType;
     }

     public String getPushUserId()
     {
         return pushUserId;
     }

     public void setPushUserId(String pushUserId)
     {
         this.pushUserId = pushUserId;
     }

     public String getCreateDatetimeBegin()
     {
         return createDatetimeBegin;
     }

     public void setCreateDatetimeBegin(String createDatetimeBegin)
     {
         this.createDatetimeBegin = HsDateUtil.convertDateToDateTime(createDatetimeBegin, "00:00:00");
     }

     public String getCreateDatetimeEnd()
     {
         return createDatetimeEnd;
     }

     public void setCreateDatetimeEnd(String createDatetimeEnd)
     {
         this.createDatetimeEnd = HsDateUtil.convertDateToDateTime(createDatetimeEnd, "23:59:59");
     }

     public String getIsRead()
     {
         return isRead;
     }

     public void setIsRead(String isRead)
     {
         this.isRead = isRead;
     }

     public String getDefaultOrderColumn()
     {
         return "t.push_msg_id";
     }
}
