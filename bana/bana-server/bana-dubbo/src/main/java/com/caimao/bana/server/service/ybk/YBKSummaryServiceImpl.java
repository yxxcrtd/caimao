package com.caimao.bana.server.service.ybk;

import com.caimao.bana.api.entity.ybk.YBKSummaryEntity;
import com.caimao.bana.api.service.ybk.IYBKSummaryService;
import com.caimao.bana.server.dao.ybk.YBKSummaryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("ybkSummaryService")
public class YBKSummaryServiceImpl implements IYBKSummaryService {

    @Resource
    private YBKSummaryDao ybkSummaryDao;

    @Override
    public List<YBKSummaryEntity> queryAccountList() throws Exception {
        return ybkSummaryDao.queryList();
    }

    @Override
    public void update(YBKSummaryEntity entity) throws Exception {
        ybkSummaryDao.update(entity);
    }

    @Override
    public void insert(YBKSummaryEntity entity) throws Exception {
        ybkSummaryDao.insert(entity);
    }

    @Override
    public YBKSummaryEntity queryById(Integer id) throws Exception {
        return ybkSummaryDao.queryById(id);
    }
}