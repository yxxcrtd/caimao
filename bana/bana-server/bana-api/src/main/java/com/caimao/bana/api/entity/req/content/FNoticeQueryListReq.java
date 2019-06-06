package com.caimao.bana.api.entity.req.content;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.content.FNoticeInfoRes;

import java.io.Serializable;

/**
 * 公告查询列表请求
 * Created by WangXu on 2015/6/18.
 */
public class FNoticeQueryListReq extends QueryBase<FNoticeInfoRes> implements Serializable {

    private Integer listShow;
    private Integer topShow;

    public Integer getListShow() {
        return listShow;
    }

    public void setListShow(Integer listShow) {
        this.listShow = listShow;
    }

    public Integer getTopShow() {
        return topShow;
    }

    public void setTopShow(Integer topShow) {
        this.topShow = topShow;
    }
}
