package com.caimao.bana.api.service.ybk;

import com.caimao.bana.api.entity.ybk.YBKTimeLineEntity;

/**
 * 邮币卡K线相关服务接口
 */
public interface IYBKLineService {
    public void updateKLine(YBKTimeLineEntity ybkTimeLineEntity) throws Exception;
}