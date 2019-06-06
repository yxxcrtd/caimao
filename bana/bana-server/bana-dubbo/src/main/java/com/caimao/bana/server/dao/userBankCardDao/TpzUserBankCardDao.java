package com.caimao.bana.server.dao.userBankCardDao;

import com.caimao.bana.api.entity.TpzUserBankCardEntity;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户绑定银行卡信息相关操作
 * Created by WangXu on 2015/5/7.
 */
@Repository
public class TpzUserBankCardDao extends SqlSessionDaoSupport {

    @Autowired
    private MemoryDbidGenerator dbidGenerator;

    /**
     * 获取用户绑定的银行卡列表
     * @param userId 用户ID
     * @return  绑定的银行卡列表
     */
    public List<TpzUserBankCardEntity> queryUserBankCardList(Long userId, String bankCardStatus) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", userId);
        queryMap.put("bankCardStatus", bankCardStatus);
        return getSqlSession().selectList("TpzUserBankCard.queryUserBankCardList", queryMap);
    }

    /**
     * 获取指定绑定银行卡ID的信息  FOR UPDATE
     * @param id    绑定ID
     * @return  银行卡信息
     */
    public TpzUserBankCardEntity getBankCardById(Long id) {
        return getSqlSession().selectOne("TpzUserBankCard.getBankCardById", id);
    }

    /**
     * 根据银行卡号码查询绑定的银行卡信息
     * @param bankCardNo    银行卡号码
     * @return  银行卡信息
     */
    public List<TpzUserBankCardEntity> getBindBankByCardNo(String bankCardNo) {
        return getSqlSession().selectList("TpzUserBankCard.getBindBankByCardNo", bankCardNo);
    }

    /**
     * 保存用户绑定银行卡信息
     * @param tpzUserBankCardEntity 用户银行卡信息
     */
    public int save(TpzUserBankCardEntity tpzUserBankCardEntity) {
        tpzUserBankCardEntity.setId(this.dbidGenerator.getNextId());
        return getSqlSession().insert("TpzUserBankCard.save", tpzUserBankCardEntity);
    }

    /**
     * 更新用户已存的绑定银行卡信息
     * @param tpzUserBankCardEntity 用户银行卡信息
     */
    public void update(TpzUserBankCardEntity tpzUserBankCardEntity) {
        getSqlSession().update("TpzUserBankCard.update", tpzUserBankCardEntity);
    }

    /**
     * 将用户所有绑定的银行卡变为副卡
     * @param userId 用户ID
     */
    public void updateUserBank(Long userId) {
        getSqlSession().update("TpzUserBankCard.updateUserBank", userId);
    }

}
