package com.caimao.bana.api.service.ybk;

import com.caimao.bana.api.entity.res.ybk.FYbkNavigationRes;
import com.caimao.bana.api.entity.ybk.YbkNavigationEntity;

import java.util.List;

/**
* YbkNavigationEntity 服务接口
*
* Created by wangxu@huobi.com on 2015-12-07 10:02:47 星期一
*/
public interface IYbkNavigationService {

    /**
     * 查询所有的导航，字符串原格式
     * @return
     * @throws Exception
     */
    List<YbkNavigationEntity> selectAllStr() throws Exception;

    /**
     * 查询所有的导航
     * @return
     * @throws Exception
     */
    List<FYbkNavigationRes> selectAll() throws Exception;

   /**
    * 查询指定内容
    *
    * @param id
    * @return
    * @throws Exception
    */
    YbkNavigationEntity selectById(Integer id) throws Exception;

   /**
    * 添加的接口
    *
    * @param entity
    * @throws Exception
    */
    void insert(YbkNavigationEntity entity) throws Exception;

   /**
    * 删除的接口
    *
    * @param id
    * @throws Exception
    */
    void deleteById(Integer id) throws Exception;

   /**
    * 更新的接口
    *
    * @param entity
    * @throws Exception
    */
    void update(YbkNavigationEntity entity) throws Exception;

}
