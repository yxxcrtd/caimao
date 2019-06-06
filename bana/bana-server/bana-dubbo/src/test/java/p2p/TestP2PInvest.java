package p2p;

import com.caimao.bana.api.entity.req.FP2PAddinvestReq;
import com.caimao.bana.api.service.IP2PService;
import com.caimao.bana.server.dao.p2p.P2PInterestRecordDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * P2P单元测试
 * Created by WangXu on 2015/6/9.
 */

public class TestP2PInvest extends BaseTest {

    @Autowired
    private IP2PService p2pService;

    @Autowired
    private P2PInterestRecordDao p2PInterestRecordDao;

    @Test
    public void TestqueryInvestInterestExist() {
        Integer interestExist = this.p2PInterestRecordDao.queryInvestInterestExist(13L, 2);
        System.out.println(interestExist);
    }

    @Test
    public void getDoInvest() throws Exception{
        FP2PAddinvestReq req = new FP2PAddinvestReq();
        req.setUserId(801139310198790L);
        req.setInvestValue(27000L);
        req.setTradePwd("zxdzat");
        req.setTargetId(1L);
        p2pService.doAddInvest(req);
    }
    
    @Test
    public void getConfig() throws Exception{
        Map<String,Object> map = p2pService.getP2PGlobalConfig();
        Set<Entry<String, Object>> set = map.entrySet();
        Iterator<Entry<String, Object>> iterator= set.iterator();
        while(iterator.hasNext()){
            Entry<String, Object> entry = iterator.next();
            System.out.println(entry.getKey()+"============="+entry.getValue());
        }
    }
}
