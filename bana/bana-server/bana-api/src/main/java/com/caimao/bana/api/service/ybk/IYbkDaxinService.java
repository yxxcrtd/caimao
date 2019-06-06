package com.caimao.bana.api.service.ybk;

import com.caimao.bana.api.entity.ybk.YbkDaxinEntity;
import com.caimao.bana.api.entity.req.ybk.FQueryYbkDaxinReq;

/**
* YbkDaxinEntity 服务接口
*
* Created by yangxinxin@huobi.com on 2015-11-17 17:47:39 星期二
*/
public interface IYbkDaxinService {

   /**
    * 查询列表
    *
    * @param req
    * @return
    * @throws Exception
    */
    FQueryYbkDaxinReq queryYbkDaxinList(FQueryYbkDaxinReq req) throws Exception;

   /**
    * 查询指定内容
    *
    * @param id
    * @return
    * @throws Exception
    */
    YbkDaxinEntity selectById(Long id) throws Exception;

   /**
    * 添加的接口
    *
    * @param entity
    * @throws Exception
    */
    void insert(YbkDaxinEntity entity) throws Exception;

   /**
    * 删除的接口
    *
    * @param id
    * @throws Exception
    */
    void deleteById(Long id) throws Exception;

   /**
    * 更新的接口
    *
    * @param entity
    * @throws Exception
    */
    void update(YbkDaxinEntity entity) throws Exception;

}
