package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.OptionalStockEntity;

import java.util.List;

/**
 * 自选股
 */
public interface IOptionalStockService {
    public Integer insertStock(Long userId, String stockCode, String stockName, Long sort) throws Exception;

    public Integer deleteStock(Long userId, Long id) throws Exception;

    public Integer sortStock(Long userId, Long id, Long sort) throws Exception;

    public List<OptionalStockEntity> queryStockByUserId(Long userId) throws Exception;

    public String queryStockNameFromSina(String stockCode) throws Exception;

    public Integer deleteStockByCode(Long userId, String stockCode) throws Exception;
}
