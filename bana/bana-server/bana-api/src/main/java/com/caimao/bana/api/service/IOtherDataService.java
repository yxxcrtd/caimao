package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.res.FIndexPZRankingRes;
import com.caimao.bana.api.entity.res.FIndexRealtimePZRes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 查询其他各类数据的服务
 * Created by WangXu on 2015/5/28.
 */
public interface IOtherDataService {

    /**
     * 首页融资排行数据
     * @param limit 显示几条
     * @return  排行数据
     * @throws Exception
     */
    public List<FIndexPZRankingRes> indexPzRankingList(int limit) throws Exception;

    /**
     * 首页配置动态
     * @param limit 显示条数
     * @return  排行数据
     * @throws Exception
     */
    public List<FIndexRealtimePZRes> indexRealtimePZList(int limit) throws Exception;

    /**
     * 首页首页显示的运营数据
     * @return
     * @throws Exception
     */
    public Map<String, Object> indexWebOperationInfo(Float ratio) throws Exception;

    /**
     * 临时为了解决月配的不收利息的问题
     * @throws Exception
     */
    public void prodLoanInterestFix() throws Exception;

    /**
     * 结息配资利息的报警通知
     * @throws Exception
     */
    public void settlePZInterestNotice() throws Exception;

}
