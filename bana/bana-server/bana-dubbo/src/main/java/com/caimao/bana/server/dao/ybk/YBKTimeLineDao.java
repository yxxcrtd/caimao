package com.caimao.bana.server.dao.ybk;

import com.caimao.bana.api.entity.req.ybk.FYbkMarketReq;
import com.caimao.bana.api.entity.res.ybk.FYBKTimeLineRes;
import com.caimao.bana.api.entity.ybk.YBKTimeLineEntity;
import com.caimao.bana.server.utils.DateUtil;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮币卡分时数据表
 */
@Repository
public class YBKTimeLineDao extends SqlSessionDaoSupport {
    public Integer insert(YBKTimeLineEntity ybkTimeLineEntity) throws Exception{
        if (ybkTimeLineEntity.getCurPrice() == 0L) {
            return 0;
        }
        // 赋值今天天的总交易量
        ybkTimeLineEntity.setTodayAmount(ybkTimeLineEntity.getTotalAmount());
        ybkTimeLineEntity.setTodayMoney(ybkTimeLineEntity.getTotalMoney());

        // 获取上一次的分时图，成交量啥的需要减去上次的，才是这次的值
        YBKTimeLineEntity timeLineEntity = this.getLastLine(ybkTimeLineEntity.getExchangeName(), ybkTimeLineEntity.getCode());
        if (timeLineEntity != null) {
            if (DateUtil.convertDateToString(timeLineEntity.getDatetime()).equals(DateUtil.convertDateToString(ybkTimeLineEntity.getDatetime()))) {
                ybkTimeLineEntity.setTotalAmount(ybkTimeLineEntity.getTotalAmount() - timeLineEntity.getTodayAmount());
                ybkTimeLineEntity.setTotalMoney(ybkTimeLineEntity.getTotalMoney() - timeLineEntity.getTodayMoney());
            }
        }
        return this.getSqlSession().insert("YBKTimeLine.insert", ybkTimeLineEntity);
    }

    public YBKTimeLineEntity getLastLine(String exchange, String code) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("exchange", exchange);
        paramsMap.put("code", code);
        return this.getSqlSession().selectOne("YBKTimeLine.getLastLine", paramsMap);
    }

    public List<FYBKTimeLineRes> queryTimeLine(FYbkMarketReq req) throws Exception{
        return this.getSqlSession().selectList("YBKTimeLine.queryTimeLine", req);
    }
}