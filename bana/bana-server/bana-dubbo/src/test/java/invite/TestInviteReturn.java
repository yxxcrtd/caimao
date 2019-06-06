/*
*TestInviteReturn.java
*Created on 2015/5/25 16:45
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package invite;

import com.caimao.bana.server.dao.sysParameter.SysParameterDao;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class TestInviteReturn extends BaseTest {

    @Resource
    private SysParameterDao sysParameterDao;

    @Test
    public void testSysParameter() {
        System.out.println(ToStringBuilder.reflectionToString(sysParameterDao.getById("interesttoscore")));
    }

    @Test
    public void testDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Integer integer = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println(integer);


    }
}
