/*
*TestGene.java
*Created on 2015/4/24 15:22
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package deposit;

import com.caimao.bana.server.utils.ChannelUtil;
import com.caimao.bana.server.utils.MemoryDbidGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class TestGene extends BaseTest {

    @Resource
    private MemoryDbidGenerator dbidGenerator;

    @Autowired
    private ChannelUtil channelUtil;

    @Test
    public void test() throws Exception{
        System.out.println(this.channelUtil.MD5("加密下").toLowerCase());
    }

}
