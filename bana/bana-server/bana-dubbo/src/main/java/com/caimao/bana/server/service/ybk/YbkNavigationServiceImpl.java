package com.caimao.bana.server.service.ybk;

import com.caimao.bana.api.entity.res.ybk.FYbkNavigationRes;
import com.caimao.bana.api.entity.ybk.YbkNavigationEntity;
import com.caimao.bana.api.service.ybk.IYbkNavigationService;
import com.caimao.bana.server.dao.ybk.YbkNavigationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* YbkNavigationEntity 服务接口实现
*
* Created by wangxu@huobi.com on 2015-12-07 10:02:47 星期一
*/
@Service("ybkNavigationService")
public class YbkNavigationServiceImpl implements IYbkNavigationService {

    @Autowired
    private YbkNavigationDAO ybkNavigationDAO;

    /**
     * 查询所有的导航，字符串原格式
     * @return
     * @throws Exception
     */
    @Override
    public List<YbkNavigationEntity> selectAllStr() throws Exception {
        return this.ybkNavigationDAO.selectAll();
    }

    /**
     * 查询所有的导航
     * @return
     * @throws Exception
     */
    @Override
    public List<FYbkNavigationRes> selectAll() throws Exception {
        List<YbkNavigationEntity> navigationEntityList = this.ybkNavigationDAO.selectAll();
        List<FYbkNavigationRes> resList = new ArrayList<>();
        for (YbkNavigationEntity entity: navigationEntityList) {
            FYbkNavigationRes res = new FYbkNavigationRes();
            res.setId(entity.getId());
            res.setName(entity.getName());
            // 获取urls
            String[] urls = entity.getUrls().split(",");
            List<Map<String, String>> tmpList = new ArrayList<>();
            Map<String, String> tmpMap = new HashMap<>();
            for (String u : urls) {
                if (tmpMap.size() == 2) {
                    Map<String, String> newMap = new HashMap<>();
                    newMap.putAll(tmpMap);
                    tmpList.add(newMap);
                    tmpMap.clear();
                }
                if (tmpMap.size() == 0) {
                    tmpMap.put("name", u);
                } else if (tmpMap.size() == 1) {
                    tmpMap.put("url", u);
                }
            }
            if (tmpMap.size() == 2) {
                Map<String, String> newMap = new HashMap<>();
                newMap.putAll(tmpMap);
                tmpList.add(newMap);
                tmpMap.clear();
            }
            res.setUrls(tmpList);
            resList.add(res);
        }
        return resList;
    }

    /**
    * 查询指定YbkNavigation内容
    *
    * @param id
    * @return
    * @throws Exception
    */
    @Override
    public YbkNavigationEntity selectById(Integer id) throws Exception {
        return this.ybkNavigationDAO.selectById(id);
    }

   /**
    * 添加YbkNavigation的接口
    *
    * @param entity
    * @throws Exception
    */
    @Override
    public void insert(YbkNavigationEntity entity) throws Exception {
        this.ybkNavigationDAO.insert(entity);
    }

   /**
    * 删除YbkNavigation的接口
    *
    * @param id
    * @throws Exception
    */
    @Override
    public void deleteById(Integer id) throws Exception {
        this.ybkNavigationDAO.deleteById(id);
    }

   /**
    * 更新YbkNavigation的接口
    *
    * @param entity
    * @throws Exception
    */
    @Override
    public void update(YbkNavigationEntity entity) throws Exception {
        this.ybkNavigationDAO.update(entity);
    }

}
