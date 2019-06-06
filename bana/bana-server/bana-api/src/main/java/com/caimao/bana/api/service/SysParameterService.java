/*
*SysParameterService.java
*Created on 2015/5/25 14:41
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.TsysParameterEntity;

/**
 * @author Administrator
 * @version 1.0.1
 */
public interface SysParameterService {

    public TsysParameterEntity getSysparameterById(String paramCode);
    
}
