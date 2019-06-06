package activity;

import com.caimao.bana.api.entity.activity.BanaActivityRecordEntity;
import com.caimao.bana.api.enums.EActId;
import com.caimao.bana.server.service.activity.ActivityServiceImpl;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.Date;
import java.util.List;

/**
 * Created by WangXu on 2015/5/22.
 */
public class TestActivity extends BaseTest {

    @Autowired
    private ActivityServiceImpl activityService;

    @Test
    public void TestSaveWeixinPaoKu() {
        String phone = "18612839215";
        String pzValue = "1021";
        String userIP = "172.168.1.109";

        int res = this.activityService.saveWeixinPaoKu(phone, pzValue, userIP);

        System.out.println(res);
    }

    @Test
    public void TestGetShowRedPackage() throws Exception{
        Long userId = 802185872932865L;
        System.out.println(activityService.getShowRedPackage(userId));
    }

    @Test
    public void TestOpenRedPackage() throws Exception{
        Long userId = 802185872932865L;
        System.out.println(activityService.openRedPackage(userId));
    }

    @Test
    public void TestGetUserActivityRecord() throws Exception {
        Long userId = 802184463646721L;
        Integer actId = EActId.EWM100.getCode();

        List<BanaActivityRecordEntity> entityList = this.activityService.getUserActivityRecord(userId, actId);
        if (entityList != null) {
            for (BanaActivityRecordEntity entity : entityList) {
                System.out.println(ToStringBuilder.reflectionToString(entity));
            }
        }
    }

    @Test
    public void TestAddActivityRecord() throws Exception {
        BanaActivityRecordEntity entity = new BanaActivityRecordEntity();
        entity.setUserId(802184463646721L);
        entity.setActId(EActId.EWM100.getCode());
        entity.setAmount(10000L);
        entity.setDatetime(new Date());
        this.activityService.addActivityRecord(entity);
    }

    @Test
    public void TestJobsEWMActivity() throws Exception {
        this.activityService.jobsEWMActivity();
    }
}
