package com.caimao.bana.api.service.content;

import com.caimao.bana.api.entity.content.BanaNoticeEntity;
import com.caimao.bana.api.entity.req.content.FNoticeQueryListReq;

/**
 * 公告通知服务接口
 * Created by WangXu on 2015/6/18.
 */
public interface INoticeService {

    /**
     * 查询公告列表
     * @param req   请求对象
     * @return
     * @throws Exception
     */
    public FNoticeQueryListReq queryList(FNoticeQueryListReq req) throws Exception;

    /**
     * 更新公告信息
     * @param entity    公告实例
     * @throws Exception
     */
    public void update(BanaNoticeEntity entity) throws Exception;

    /**
     * 保存公告信息
     * @param entity    公告实例
     * @throws Exception
     */
    public Long save(BanaNoticeEntity entity) throws Exception;

    /**
     * 根据ID获取公告信息
     * @param id    ID
     * @throws Exception
     */
    public BanaNoticeEntity queryById(Long id) throws Exception;

    /**
     * 根据ID删除记录
     * @param id    ID
     * @throws Exception
     */
    public void delete(Long id) throws Exception;

}
