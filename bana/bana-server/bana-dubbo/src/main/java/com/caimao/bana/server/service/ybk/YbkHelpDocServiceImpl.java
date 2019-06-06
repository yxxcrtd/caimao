package com.caimao.bana.server.service.ybk;


import com.caimao.bana.api.entity.req.ybk.FQueryYbkHelpDocReq;
import com.caimao.bana.api.entity.ybk.YbkHelpDocEntity;
import com.caimao.bana.api.enums.ybk.EYbkHelpDocType;
import com.caimao.bana.api.service.ybk.IYbkHelpDocService;
import com.caimao.bana.server.dao.ybk.YbkHelpDocDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* YbkHelpDocEntity 邮币卡帮助文档接口实现
*
* Created by wangxu@huobi.com on 2015-11-16 17:26:31
*/
@Service("ybkHelpDocService")
public class YbkHelpDocServiceImpl implements IYbkHelpDocService {

    @Autowired
    private YbkHelpDocDAO ybkHelpDocDAO;

   /**
    * 邮币卡帮助文档查询
    *
    * @param req
    * @return
    * @throws Exception
    */
    @Override
    public FQueryYbkHelpDocReq queryYbkHelpDocList(FQueryYbkHelpDocReq req) throws Exception {
//        if (req.getDateStart() != null) req.setDateStart(req.getDateStart() + " 00:00:00");
//        if (req.getDateEnd() != null) req.setDateEnd(req.getDateEnd() + " 23:59:59");
//        if (null != req.getCategoryId()) { req.setCategoryId(req.getCategoryId()); }
//        if (null != req.getIsHot()) { req.setIsHot(req.getIsHot()); }
        List<YbkHelpDocEntity> list = this.ybkHelpDocDAO.queryYbkHelpDocWithPage(req);
        if (list != null) {
            for (YbkHelpDocEntity entity: list) {
                entity.setCategoryStr(EYbkHelpDocType.findByCode(entity.getCategoryId()).getValue());
            }
        }
        req.setItems(list);
        return req;
    }

   /**
    * 根据ID获取帮助文档信息
    *
    * @param id
    * @return
    * @throws Exception
    */
    @Override
    public YbkHelpDocEntity selectById(Integer id) throws Exception {
        return this.ybkHelpDocDAO.selectById(id);
    }

   /**
    * 添加邮币卡帮助文档
    *
    * @param entity
    * @throws Exception
    */
    @Override
    public void insert(YbkHelpDocEntity entity) throws Exception {
        this.ybkHelpDocDAO.insert(entity);
    }

   /**
    * 根据ID删除帮助文档
    *
    * @param id
    * @throws Exception
    */
    @Override
    public void deleteById(Long id) throws Exception {
        this.ybkHelpDocDAO.deleteById(id);
    }

   /**
    * 更新邮币卡帮助文档
    *
    * @param entity
    * @throws Exception
    */
    @Override
    public void update(YbkHelpDocEntity entity) throws Exception {
        this.ybkHelpDocDAO.update(entity);
    }

}
