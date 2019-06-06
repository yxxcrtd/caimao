package com.caimao.bana.api.service.content;

import com.caimao.bana.api.entity.content.BanaBannerEntity;
import com.caimao.bana.api.entity.req.content.FQueryBannerListReq;

/**
 * 首页banner管理
 * Created by Administrator on 2015/10/14.
 */
public interface IBannerService {

    /**
     * 根据ID获取banner信息
     * @param id
     * @return
     * @throws Exception
     */
    public BanaBannerEntity selectById(Integer id) throws Exception;

    /**
     * 删除banner 信息
     * @param id
     * @throws Exception
     */
    public void delById(Integer id) throws Exception;

    /**
     * 添加banner信息
     * @param entity
     * @throws Exception
     */
    public void addBanner(BanaBannerEntity entity) throws Exception;


    /**
     * 更新banner信息
     * @param entity
     * @throws Exception
     */
    public void updatgeBanner(BanaBannerEntity entity) throws Exception;

    /**
     * 根据条件查找banner信息
     * @param req
     * @return
     * @throws Exception
     */
    public FQueryBannerListReq queryBannerList(FQueryBannerListReq req) throws Exception;
    

}
