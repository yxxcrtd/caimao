package com.caimao.bana.api.service;

import com.caimao.bana.api.entity.TpzHomsAccountEntity;
import com.caimao.bana.api.entity.TpzHomsAccountLoanEntity;
import com.caimao.bana.api.entity.req.FZeusHomsAccountAssetsReq;
import com.caimao.bana.api.entity.req.FZeusHomsAccountHoldReq;
import com.caimao.bana.api.entity.res.other.FHomsNeedUpdateAssetsRes;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountAssetsEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsAccountHoldEntity;
import com.caimao.bana.api.entity.zeus.ZeusHomsRepaymentExcludeEntity;

import java.util.HashMap;
import java.util.List;

/**
 * HOMS账户相关服务
 * Created by WangXu on 2015/6/12.
 */
public interface IHomsAccountService {

    /**
     * 验证HOMS账户是不是指定用户的方法
     *
     * @param userId          用户ID
     * @param homsCombineId
     * @param homsFundAccount
     */
    public void valideUserHomsAccount(Long userId, String homsCombineId, String homsFundAccount) throws Exception;

    /**
     * 搜索使用的根据姓名或手机号模糊搜索
     *
     * @param req
     * @return
     */
    public FZeusHomsAccountAssetsReq searchZeusHomsAssetsList(FZeusHomsAccountAssetsReq req) throws Exception;

    /**
     * 保存homs操盘账户的资产信息
     *
     * @param entity
     * @return
     */
    public int saveZeusHomsAssets(ZeusHomsAccountAssetsEntity entity) throws Exception;

    /**
     * 根据条件查找记录
     *
     * @param entity
     * @return
     */
    public ZeusHomsAccountAssetsEntity getZeusHomsAssets(ZeusHomsAccountAssetsEntity entity) throws Exception;

    /**
     * 获取指定日期需要更新的资产的记录
     *
     * @return
     */
    public List<FHomsNeedUpdateAssetsRes> queryNeedUpdateAssetsList(String updateDate) throws Exception;

    /**
     * 获取HOMS主账户列表
     * @return
     * @throws Exception
     */
    public List<TpzHomsAccountEntity> queryHomsAccount() throws Exception;

    /**
     * 保存homs持仓
     * @param zeusHomsAccountHoldEntity
     * @return
     * @throws Exception
     */
    public Integer saveZeusHomsAccountHold(ZeusHomsAccountHoldEntity zeusHomsAccountHoldEntity) throws Exception;

    /**
     * 获取持仓信息列表
     * @param req
     * @return
     * @throws Exception
     */
    public FZeusHomsAccountHoldReq searchZeusHomsHoldList(FZeusHomsAccountHoldReq req) throws Exception;

    /**
     * 获取homs借贷关系
     * @param homsFundAccount
     * @param homsCombineId
     * @return
     * @throws Exception
     */
    public TpzHomsAccountLoanEntity queryTpzHomsAccountLoan(String homsFundAccount, String homsCombineId) throws Exception;

    /**
     * 获取已更新的列表
     * @param dateString
     * @return
     * @throws Exception
     */
    public List<String> queryUpdated(String dateString) throws Exception;

    /**
     * 获取指定的账户的排除列表（如果都为null，则查询所有的）
     * @param homsFundAccount
     * @param homsCombineId
     * @return
     * @throws Exception
     */
    public List<ZeusHomsRepaymentExcludeEntity> queryHomsRepaymentExcludeList(String homsFundAccount, String homsCombineId) throws Exception;

    /**
     * 保存有持仓能够还款的排除列表
     * @param homsCombineId
     * @return
     * @throws Exception
     */
    public Integer saveHomsRepaymentExclude(String homsCombineId) throws Exception;

    /**
     * 删除有持仓能够还款的排除列表
     * @param homsFundAccount
     * @param homsCombineId
     * @throws Exception
     */
    public void deleteHomsRepayMentExclude(String homsFundAccount, String homsCombineId) throws Exception;

    /**
     * 查询异常的还款记录
     * @throws Exception
     */
    public void checkAbnormalRepayAccountJour() throws Exception;
}
