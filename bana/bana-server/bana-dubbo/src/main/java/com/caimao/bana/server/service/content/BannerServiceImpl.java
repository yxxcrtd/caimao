package com.caimao.bana.server.service.content;

import com.caimao.bana.api.entity.content.BanaBannerEntity;
import com.caimao.bana.api.entity.req.content.FQueryBannerListReq;
import com.caimao.bana.api.service.content.IBannerService;
import com.caimao.bana.server.dao.content.BanaBannerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * banner管理服务接口实现
 * Created by Administrator on 2015/10/14.
 */
@Service("bannerService")
public class BannerServiceImpl implements IBannerService {

    @Autowired
    private BanaBannerDao bannerDao;

    /**
     * 根据ID获取banner信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public BanaBannerEntity selectById(Integer id) throws Exception {
        return this.bannerDao.select(id);
    }

    /**
     * 删除banner 信息
     * @param id
     * @throws Exception
     */
    @Override
    public void delById(Integer id) throws Exception {
        this.bannerDao.delete(id);
    }

    /**
     * 添加banner信息
     * @param entity
     * @throws Exception
     */
    @Override
    public void addBanner(BanaBannerEntity entity) throws Exception {
        this.bannerDao.insert(entity);
    }

    /**
     * 更新banner信息
     * @param entity
     * @throws Exception
     */
    @Override
    public void updatgeBanner(BanaBannerEntity entity) throws Exception {
        this.bannerDao.update(entity);
    }

    /**
     * 根据条件查找banner信息
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public FQueryBannerListReq queryBannerList(FQueryBannerListReq req) throws Exception {
        req.setItems(this.bannerDao.queryListWithPage(req));
        return req;
    }
}
