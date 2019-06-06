package com.caimao.bana.server.service.ybk;

import com.caimao.bana.api.entity.ybk.YbkDaxinEntity;
import com.caimao.bana.api.entity.req.ybk.FQueryYbkDaxinReq;
import com.caimao.bana.api.service.ybk.IYbkDaxinService;
import com.caimao.bana.server.dao.ybk.YbkDaxinDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* YbkDaxinEntity 服务接口实现
*
* Created by yangxinxin@huobi.com on 2015-11-17 17:47:39 星期二
*/
@Service("ybkDaxinService")
public class YbkDaxinServiceImpl implements IYbkDaxinService {

    @Autowired
    private YbkDaxinDAO ybkDaxinDAO;

   /**
    * 查询YbkDaxin列表
    *
    * @param req
    * @return
    * @throws Exception
    */
    @Override
    public FQueryYbkDaxinReq queryYbkDaxinList(FQueryYbkDaxinReq req) throws Exception {
//        if (req.getDateStart() != null) req.setDateStart(req.getDateStart() + " 00:00:00");
//        if (req.getDateEnd() != null) req.setDateEnd(req.getDateEnd() + " 23:59:59");
//        if (null != req.getCategoryId()) { req.setCategoryId(req.getCategoryId()); }
//        if (null != req.getIsHot()) { req.setIsHot(req.getIsHot()); }
        List<YbkDaxinEntity> list = this.ybkDaxinDAO.queryYbkDaxinWithPage(req);
        req.setItems(list);
        return req;
    }

   /**
    * 查询指定YbkDaxin内容
    *
    * @param id
    * @return
    * @throws Exception
    */
    @Override
    public YbkDaxinEntity selectById(Long id) throws Exception {
        return this.ybkDaxinDAO.selectById(id);
    }

   /**
    * 添加YbkDaxin的接口
    *
    * @param entity
    * @throws Exception
    */
    @Override
    public void insert(YbkDaxinEntity entity) throws Exception {
        this.ybkDaxinDAO.insert(entity);
    }

   /**
    * 删除YbkDaxin的接口
    *
    * @param id
    * @throws Exception
    */
    @Override
    public void deleteById(Long id) throws Exception {
        this.ybkDaxinDAO.deleteById(id);
    }

   /**
    * 更新YbkDaxin的接口
    *
    * @param entity
    * @throws Exception
    */
    @Override
    public void update(YbkDaxinEntity entity) throws Exception {
        this.ybkDaxinDAO.update(entity);
    }

}
