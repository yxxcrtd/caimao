package com.caimao.bana.server.dao.content;

import com.caimao.bana.api.entity.content.BanaNoticeEntity;
import com.caimao.bana.api.entity.req.content.FNoticeQueryListReq;
import com.caimao.bana.api.entity.res.content.FNoticeInfoRes;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 公告消息数据访问对象
 * Created by WangXu on 2015/6/18.
 */
@Repository
public class BanaNoticeDao extends SqlSessionDaoSupport {

    /**
     * 获取公告列表
     * @param req   请求
     * @return
     */
    public List<FNoticeInfoRes> queryListWithPage(FNoticeQueryListReq req) {
        return getSqlSession().selectList("BanaNotice.queryListWithPage", req);
    }

    /**
     * 更新公告
     * @param entity 数据实例
     * @return
     */
    public int update(BanaNoticeEntity entity) {
        return getSqlSession().update("BanaNotice.update", entity);
    }

    /**
     * 保存公告
     * @param entity    数据实例
     * @return
     */
    public int save(BanaNoticeEntity entity) {
        return getSqlSession().insert("BanaNotice.save", entity);
    }

    /**
     * 查询指定的公告ID信息
     * @param id    ID
     * @return
     */
    public BanaNoticeEntity queryById(Long id) {
        return getSqlSession().selectOne("BanaNotice.queryById", id);
    }

    /**
     * 删除指定ID的公告
     * @param id
     */
    public void delete(Long id) {
        getSqlSession().delete("BanaNotice.delete", id);
    }

}
