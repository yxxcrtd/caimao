package user;

import com.caimao.bana.api.entity.*;
import com.caimao.bana.api.enums.AddScoreType;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.api.service.InviteInfoService;
import com.caimao.bana.api.service.SysParameterService;
import com.caimao.bana.api.service.TpzAccruedInterestBillService;
import com.caimao.bana.server.dao.userDao.TpzUserDao;
import com.caimao.bana.server.utils.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parent.BaseTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户数据访问接口测试
 * Created by WangXu on 2015/4/22.
 */
public class TestTpzUserDao extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(TestTpzUserDao.class);

    @Resource
    private TpzUserDao tpzUserDao;

    @Resource
    private IUserService userService;

    @Resource
    private TpzAccruedInterestBillService tpzAccruedInterestBillService;

    @Resource
    private SysParameterService sysParameterService;

    @Resource
    private InviteInfoService inviteInfoService;

    @Test
    public void testGetUserRefList() throws Exception {
        Long userId = 802184463646721L;
        Date date = DateUtil.convertStringToDate(DateUtil.DATA_TIME_PATTERN_1, "2015-07-31 17:00:00");

        List<TpzUserEntity> userEntityList = this.userService.getUserRefList(userId, date);

        System.out.println("查找到 " + userEntityList.size());
        if (userEntityList != null) {
            for (TpzUserEntity entity: userEntityList) {
                System.out.println(ToStringBuilder.reflectionToString(entity));
            }
        }
    }

    @Test
    /**
     * 测试获取用户信息的方法
     */
    public void testGetUserById() throws Exception {
        Long userId = 799436825427971L;
        TpzUserEntity tpzUserEntity = tpzUserDao.getUserById(userId);
        System.out.println(ToStringBuilder.reflectionToString(tpzUserEntity));
    }


    @Test
    public void testGetUseIdByPhone() throws Exception {
        String phong = "18612839215";
        Long userId = this.userService.getUserIdByPhone(phong);
        System.out.println(userId);
    }

    @Test
    public void testGetByCaimaoId() throws Exception {
        Long caimaoId = new Long("54");
        TpzUserEntity tpzUserEntity = this.userService.getByCaimaoId(caimaoId);
        System.out.println(ToStringBuilder.reflectionToString(tpzUserEntity));
    }

    @Test
    public void testGetById() throws Exception {
        Long caimaoId = new Long("799478835576835");
        TpzUserEntity tpzUserEntity = this.userService.getById(caimaoId);
        System.out.println(ToStringBuilder.reflectionToString(tpzUserEntity));
    }



    @Test
    public void testSub() {
        String name = "任培伟";
        String[] subfix = {"*", "**", "***", "****"};
        System.out.println(name.substring(0, 1).concat(subfix[name.length() - 2]));
    }

    @Test
    public void testAddScore() throws Exception {
        List<TpzAccruedInterestBillEntity> tpzAccruedInterestBillEntityList = tpzAccruedInterestBillService.queryBillListByWorkDate("20150422");
        TpzAccruedInterestBillEntity tpzAccruedInterestBillEntity = tpzAccruedInterestBillEntityList.get(0);

        TsysParameterEntity sysParameterEntity = sysParameterService.getSysparameterById("interesttoscore");

        TpzUserEntity userEntity = userService.getById(tpzAccruedInterestBillEntity.getUserId());
        if (userEntity.getRefUserId() != null && userEntity.getRefUserId() != 0) {
            InviteInfoEntity inviteInfoEntity = inviteInfoService.getInviteInfo(userEntity.getRefUserId());
            // 积分=扣息(00) / 100 * 返佣比率 / 利息兑换积分比例;
            BigDecimal score = new BigDecimal(tpzAccruedInterestBillEntity.getOrderAmount().longValue())
                    .divide(new BigDecimal("100"), 0, BigDecimal.ROUND_DOWN)
                    .multiply(inviteInfoEntity.getCommissionRate())
                    .divide(new BigDecimal(sysParameterEntity.getParamValue()), 0, BigDecimal.ROUND_DOWN);
            if (score.compareTo(BigDecimal.ZERO) > 0) {
                InviteReturnHistoryEntity inviteReturnHistoryEntity = new InviteReturnHistoryEntity();
                inviteReturnHistoryEntity.setUserId(userEntity.getRefUserId());
                inviteReturnHistoryEntity.setReturnType(AddScoreType.COMMISSION_RETURN.getValue());
                inviteReturnHistoryEntity.setOrderNo(tpzAccruedInterestBillEntity.getOrderNo());
                inviteReturnHistoryEntity.setOrderAmount(tpzAccruedInterestBillEntity.getOrderAmount());
                inviteReturnHistoryEntity.setReturnRate(inviteInfoEntity.getCommissionRate());
                inviteReturnHistoryEntity.setReturnAmount(score);

                userService.addScore(userEntity.getRefUserId(), score.intValue(), AddScoreType.COMMISSION_RETURN, inviteReturnHistoryEntity);
                logger.info("成功为用户{}加积分{}", userEntity.getRefUserId(), score);
            } else {
                logger.warn("积分不足一分");
            }
        } else {
            logger.info("结息记录:{}对应的融资用户无邀请用户~~");
        }
    }
    
    
}
