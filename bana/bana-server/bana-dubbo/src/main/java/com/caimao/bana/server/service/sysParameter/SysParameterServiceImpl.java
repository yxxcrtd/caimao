/*
*SysParameterServiceImpl.java
*Created on 2015/5/25 14:42
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.server.service.sysParameter;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.caimao.bana.api.entity.TsysParameterEntity;
import com.caimao.bana.api.service.SysParameterService;
import com.caimao.bana.server.dao.sysParameter.SysParameterDao;

/**
 * @author Administrator
 * @version 1.0.1
 */
@Service("sysParameterService")
public class SysParameterServiceImpl implements SysParameterService {

    @Resource
    private SysParameterDao sysParameterDao;

    @Override
    public TsysParameterEntity getSysparameterById(String paramCode) {
        return sysParameterDao.getById(paramCode);
    }

   

}
