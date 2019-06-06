package com.caimao.gjs.server.base.framework;


import com.caimao.bana.common.api.entity.QueryBase;

/**
 * 分页参数：转换器
 * Created by WangXu on 2015/6/1.
 */
public class BizBasePageConvertor implements IPageConverter<QueryBase> {
    public IPageParameter toPage(QueryBase qryDto) {
        PageParameter page = new PageParameter();
        page.setRequireTotal(true);
        page.setStart(qryDto.getStart().intValue());
        page.setLimit(qryDto.getLimit().intValue());
        return page;
    }

    public void returnTotal(QueryBase qryDto, int total) {
        qryDto.setTotalCount(Integer.valueOf(total));
    }
}
