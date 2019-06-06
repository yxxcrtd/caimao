/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司.
 *All Rights Reserved
 */
package content;


import com.caimao.bana.api.entity.content.TpzPushMsgEntity;
import com.caimao.bana.api.entity.req.F830904Req;
import com.caimao.bana.api.entity.req.content.FMsgQueryListReq;
import com.caimao.bana.api.entity.req.message.FPushMsgAddReq;
import com.caimao.bana.api.enums.EPushModel;
import com.caimao.bana.api.enums.EPushType;
import com.caimao.bana.common.api.exception.CustomerException;
import com.caimao.bana.server.service.message.MessageServiceImpl;
import com.huobi.commons.utils.JsonUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parent.BaseTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjg
 * 2015年5月23日
 */
public class TestMessageServiceImpl extends BaseTest{
    @Autowired
    private MessageServiceImpl messageService;
    /**
     * Test method for {@link com.caimao.bana.server.service.message.MessageServiceImpl#queryPushMsg(com.caimao.bana.api.entity.req.F830904Req)}.
     */
    @Test
    public void testQueryPushMsg() {
        F830904Req f830904Req=new F830904Req();
        f830904Req.setPushUserId("799444006076419");
        f830904Req.setCreateDatetimeBegin("2015-04-20");
        f830904Req.setCreateDatetimeEnd("2015-04-23");
        f830904Req.setStart(Integer.valueOf("0"));
        f830904Req.setIsRead("0");
        f830904Req.setLimit(5);
        if(!"all".equalsIgnoreCase("4"))
            f830904Req.setPushType("4");
        messageService.queryPushMsg(f830904Req);
    }

    /**
     * Test method for {@link com.caimao.bana.server.service.message.MessageServiceImpl#getPushMsgContent(java.lang.Long)}.
     */
    @Test
    public void testGetPushMsgContent() {
        try {
            System.out.println(JsonUtil.toJson(messageService.getPushMsgContent(new Long("799483063435274").longValue())));
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CustomerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Test method for {@link com.caimao.bana.server.service.message.MessageServiceImpl#doSetReadFlag(java.lang.Long)}.
     */
    @Test
    public void testDoSetReadFlag() throws Exception {
        try {
            messageService.doSetReadFlag(new Long("799483063435274"));
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CustomerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetNotReadNum() throws Exception {
        Long userId = 802184463646721L;
        Integer num = this.messageService.getNotReadNum(userId);
        System.out.println(num);
    }

    @Test
    public void testQueryMsgList() throws Exception {

        List<String> pushTypes = new ArrayList<>();
        pushTypes.add("4");
        pushTypes.add("5");
        FMsgQueryListReq req = new FMsgQueryListReq();
        req.setPushUserId("802184463646721");
        req.setPushTypes(pushTypes);

        req = this.messageService.queryMsgList(req);

        System.out.println(ToStringBuilder.reflectionToString(req));
        if (req.getItems() != null) {
            for (TpzPushMsgEntity entity : req.getItems()) {
                System.out.println(ToStringBuilder.reflectionToString(entity));
            }
        }
    }
    
    @Test
    public void testAddMsg() throws Exception {
        FPushMsgAddReq req = new FPushMsgAddReq();
        req.setPushModel(EPushModel.HOME.getCode());
        req.setPushType(EPushType.PRICEALERT.getCode());
        req.setPushMsgKind("1");
        req.setPushMsgTitle("测试消息");
        req.setPushMsgContent("测试消息内容");
        req.setPushMsgDigest("测试消息描述");
        req.setPushExtend("");
        req.setPushUserId("802184463646721");
        req.setIsRead("0");
        
        this.messageService.addPushMsg(req);
    }
}
