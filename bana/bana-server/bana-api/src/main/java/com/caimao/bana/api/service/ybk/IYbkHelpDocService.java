package com.caimao.bana.api.service.ybk;

import com.caimao.bana.api.entity.req.ybk.FQueryYbkHelpDocReq;
import com.caimao.bana.api.entity.ybk.YbkHelpDocEntity;

/**
* YbkHelpDocEntity
*
* Created by wangxu@huobi.com on 2015-11-16 17:26:31
*/
public interface IYbkHelpDocService {

   /**
    * 查询邮币卡帮助文档列表
    *
    * @param req
    * @return
    * @throws Exception
    */
    FQueryYbkHelpDocReq queryYbkHelpDocList(FQueryYbkHelpDocReq req) throws Exception;

   /**
    * 查询指定的帮助文档信息
    *
    * @param id
    * @return
    * @throws Exception
    */
    YbkHelpDocEntity selectById(Integer id) throws Exception;

   /**
    * 插入保存
    *
    * @param entity
    * @throws Exception
    */
    void insert(YbkHelpDocEntity entity) throws Exception;

   /**
    * 删除
    *
    * @param id
    * @throws Exception
    */
    void deleteById(Long id) throws Exception;

   /**
    * 更新
    *
    * @param entity
    * @throws Exception
    */
    void update(YbkHelpDocEntity entity) throws Exception;

}
