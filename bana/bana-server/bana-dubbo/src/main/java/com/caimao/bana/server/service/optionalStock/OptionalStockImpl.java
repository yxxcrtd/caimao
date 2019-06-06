package com.caimao.bana.server.service.optionalStock;

import com.caimao.bana.api.entity.OptionalStockEntity;
import com.caimao.bana.api.service.IOptionalStockService;
import com.caimao.bana.server.dao.optionalStockDao.OptionalStockDao;
import com.caimao.bana.server.utils.StringProcessUtils;
import com.hsnet.pz.core.exception.BizServiceException;
import com.huobi.commons.utils.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 自选股
 */
@Service("optionalStockService")
public class OptionalStockImpl implements IOptionalStockService {
    @Resource
    private OptionalStockDao optionalStockDao;

    @Override
    public Integer insertStock(Long userId, String stockCode, String stockName, Long sort) throws Exception {
        Integer userStockNum = optionalStockDao.queryUserStockNum(userId);
        if(userStockNum >= 20) throw new BizServiceException("102021", "自选股数量不能超过20个");
        try{
            OptionalStockEntity optionalStockEntity = new OptionalStockEntity();
            optionalStockEntity.setUserId(userId);
            optionalStockEntity.setStockCode(stockCode);
            optionalStockEntity.setStockName(stockName);
            optionalStockEntity.setSort(sort);
            optionalStockEntity.setCreated(new Date());
            optionalStockEntity.setMarketType(new Byte(StringProcessUtils.getMarketTypeByCode(stockCode)));
            return optionalStockDao.insertStock(optionalStockEntity);
        }catch(Exception e){
            throw new BizServiceException("102022", "该自选股已添加");
        }
    }

    @Override
    public Integer deleteStock(Long userId, Long id) throws Exception {
        return optionalStockDao.deleteStock(userId, id);
    }

    @Override
    public Integer sortStock(Long userId, Long id, Long sort) throws Exception {
        return optionalStockDao.sortStock(userId, id, sort);
    }

    @Override
    public List<OptionalStockEntity> queryStockByUserId(Long userId) throws Exception {
        return optionalStockDao.queryStockByUserId(userId);
    }

    @Override
    public String queryStockNameFromSina(String stockCode) throws Exception {
        try{
            if(stockCode.length() != 6) return null;
            String marketType = "";
            String stockPre = stockCode.substring(0, 1);
            switch (stockPre) {
                case "0":
                case "1":
                case "2":
                case "3":
                    marketType = "sz";
                    break;
                case "5":
                case "6":
                    marketType = "sh";
                    break;
            }
            if(marketType.equals("")) return null;
            String stockCodeUrl = marketType + stockCode;

            String responseStr = HttpUtil.get("http://hq.sinajs.cn/list=" + stockCodeUrl, new ResponseHandler<String>() {
                public String handleResponse(HttpResponse response) throws IOException {
                    if (response.getStatusLine().getStatusCode() == 200) {
                        return EntityUtils.toString(response.getEntity(), "GBK");
                    }
                    return null;
                }
            });
            if (!responseStr.contains("hq_str_" + stockCodeUrl)) return null;
            Integer PosStart = responseStr.indexOf("\"");
            Integer PosEnd = responseStr.indexOf(",");
            return responseStr.substring(PosStart + 1, PosEnd);
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public Integer deleteStockByCode(Long userId, String stockCode) throws Exception {
        return optionalStockDao.deleteStockByCode(userId, stockCode);
    }
}
