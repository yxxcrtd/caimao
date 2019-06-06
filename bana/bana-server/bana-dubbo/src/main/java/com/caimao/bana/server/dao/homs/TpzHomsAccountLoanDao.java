package com.caimao.bana.server.dao.homs;

import com.caimao.bana.api.entity.TpzHomsAccountLoanEntity;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * homs 融资合约账户的数据库操作
 * Created by WangXu on 2015/6/12.
 */
@Repository
public class TpzHomsAccountLoanDao extends SqlSessionDaoSupport {

    public TpzHomsAccountLoanEntity getAccountByContractNo(Long contractNo) {
        return getSqlSession().selectOne("TpzHomsAccountLoan.getAccountByContractNo", contractNo);
    }

    /**
     * 获取符合条件的homs融资合约的账户信息
     * @param accountLoanEntity
     * @return
     */
    public List<TpzHomsAccountLoanEntity> getHomsAccount(TpzHomsAccountLoanEntity accountLoanEntity) {
        return getSqlSession().selectList("TpzHomsAccountLoan.getHomsAccount", accountLoanEntity);
    }

    public TpzHomsAccountLoanEntity queryTpzHomsAccountLoan(String homsFundAccount, String homsCombineId) throws Exception{
        HashMap<String, String> params = new HashMap<>();
        params.put("homsFundAccount", homsFundAccount);
        params.put("homsCombineId", homsCombineId);
        return getSqlSession().selectOne("TpzHomsAccountLoan.queryTpzHomsAccountLoan", params);
    }

    public TpzHomsAccountLoanEntity getByCombineId(String homsCombineId) {
        return getSqlSession().selectOne("TpzHomsAccountLoan.getByCombineId", homsCombineId);
    }
}
