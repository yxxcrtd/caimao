package com.caimao.bana.api.service.ybk;

import com.caimao.bana.api.entity.ybk.YBKSummaryEntity;

import java.util.List;

/**
 * 文交所综合指数
 */
public interface IYBKSummaryService {

    public List<YBKSummaryEntity> queryAccountList()throws Exception ;

    public void update(YBKSummaryEntity entity)throws Exception ;

    public void insert(YBKSummaryEntity entity)throws Exception ;

    public YBKSummaryEntity queryById(Integer id)throws Exception ;
}