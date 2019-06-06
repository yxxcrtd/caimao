package com.caimao.bana.server.service.content;

import com.caimao.bana.api.entity.zeus.ZeusProhibitStockEntity;
import com.caimao.bana.api.service.content.IContentService;
import com.caimao.bana.server.dao.content.ZeusProhibitStockDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 网站相关内容的东东（泛类）
 * Created by Administrator on 2015/8/17.
 */
@Service("contentService")
public class ContentServiceImpl implements IContentService {

    @Autowired
    private ZeusProhibitStockDao prohibitStockDao;

    // 保存禁买股票信息
    @Override
    public void saveProhibitStock(ZeusProhibitStockEntity entity) throws Exception {
        entity.setCreated(new Date());
        this.prohibitStockDao.save(entity);
    }

    // 获取所有禁买股票的信息
    @Override
    public List<ZeusProhibitStockEntity> listProhibitStock() throws Exception {
        return this.prohibitStockDao.list();
    }

    // 删除指定的禁买股票信息
    @Override
    public void delProhibitStock(Integer id) throws Exception {
        this.prohibitStockDao.del(id);
    }
}
