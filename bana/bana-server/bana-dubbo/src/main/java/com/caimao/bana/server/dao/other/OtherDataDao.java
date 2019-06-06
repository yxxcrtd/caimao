package com.caimao.bana.server.dao.other;

import com.caimao.bana.api.entity.res.FIndexPZRankingRes;
import com.caimao.bana.api.entity.res.FIndexRealtimePZRes;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 其他数据查询的东东
 * Created by WangXu on 2015/5/28.
 */
@Repository
public class OtherDataDao extends SqlSessionDaoSupport {


    /**
     * 查询首页内容中的融资排行列表
     * @param limit 显示条数
     * @return  排行数据
     */
    public List<FIndexPZRankingRes> queryPZRankingList(int limit) {
        return getSqlSession().selectList("OtherData.queryPZRankingList", limit);
    }

    /**
     * 查询首页实时融资的数据列表
     * @param limit 显示的条数
     * @return  实时数据
     */
    public List<FIndexRealtimePZRes> queryRealtimePZList (int limit) {
        return getSqlSession().selectList("OtherData.queryRealtimePZList", limit);
    }

    /**
     * 获取总的注册人数
     * @return
     */
    public Integer getTotalRegisterUserCount() {
        return getSqlSession().selectOne("OtherData.getTotalRegisterUserCount");
    }

    /**
     * 获取总的配资金额
     * @return
     */
    public Long getTotalPzSum() {
        return getSqlSession().selectOne("OtherData.getTotalPzSum");
    }

    /**
     * 获取总的投资金额
     * @return
     */
    public Long getTotalInviestSum() {
        return getSqlSession().selectOne("OtherData.getTotalInviestSum");
    }

    /**
     * 获取总的利息收入
     * @return
     */
    public Long getTotalInterestSum() {
        return getSqlSession().selectOne("OtherData.getTotalInterestSum");
    }

}
