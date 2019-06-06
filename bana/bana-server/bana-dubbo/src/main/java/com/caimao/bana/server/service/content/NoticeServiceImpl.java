package com.caimao.bana.server.service.content;

import com.caimao.bana.api.entity.content.BanaNoticeEntity;
import com.caimao.bana.api.entity.req.content.FNoticeQueryListReq;
import com.caimao.bana.api.entity.res.content.FNoticeInfoRes;
import com.caimao.bana.api.service.content.INoticeService;
import com.caimao.bana.server.dao.content.BanaNoticeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网站公告通知接口服务
 * Created by WangXu on 2015/6/18.
 */
@Service("noticeService")
public class NoticeServiceImpl implements INoticeService {

    @Autowired
    private BanaNoticeDao noticeDao;

    //查询公告列表
    @Override
    public FNoticeQueryListReq queryList(FNoticeQueryListReq req) throws Exception {
        List<FNoticeInfoRes> resList = this.noticeDao.queryListWithPage(req);
        req.setItems(resList);
        return req;
    }

    //更新公告信息
    @Override
    public void update(BanaNoticeEntity entity) throws Exception {
        this.noticeDao.update(entity);
    }

    //保存公告信息
    @Override
    public Long save(BanaNoticeEntity entity) throws Exception {
        this.noticeDao.save(entity);
        return entity.getId();
    }

    //根据ID获取公告信息
    @Override
    public BanaNoticeEntity queryById(Long id) throws Exception {
        return this.noticeDao.queryById(id);
    }

    //根据ID删除记录
    @Override
    public void delete(Long id) throws Exception {
        this.noticeDao.delete(id);
    }
}
