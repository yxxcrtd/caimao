package com.caimao.bana.api.service.content;

import com.caimao.bana.api.entity.zeus.ZeusProhibitStockEntity;

import java.util.List;

/**
 * 网站相关内容的东东（泛类）
 * Created by Administrator on 2015/8/17.
 */
public interface IContentService {

    /**
     * 保存禁买股票信息
     * @param entity
     * @throws Exception
     */
    public void saveProhibitStock(ZeusProhibitStockEntity entity) throws Exception;

    /**
     * 获取所有禁买股票的信息
     * @return
     * @throws Exception
     */
    public List<ZeusProhibitStockEntity> listProhibitStock() throws Exception;

    /**
     * 删除指定的禁买股票信息
     * @param id
     * @throws Exception
     */
    public void delProhibitStock(Integer id) throws Exception;
}
