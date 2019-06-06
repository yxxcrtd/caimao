package com.caimao.hq.service;


import com.caimao.hq.api.entity.GjsError;
import com.caimao.hq.api.service.IGjsErrorService;
import com.caimao.hq.dao.GjsErrorDao;
import com.caimao.hq.utils.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 废弃：记录错误的服务
 * Created by Administrator on 2015/9/30.
 */

@Service("gjsErrorService")
public class GjsErrorServiceImpl implements IGjsErrorService {

    private Logger logger = Logger.getLogger(GjsErrorServiceImpl.class);

    @Autowired
    private GjsErrorDao gjsErrorDao;

    @Override
    public int insert(GjsError gjsError) {
      return  gjsErrorDao.insert(gjsError);
    }

    @Override
    public int insert(String financeMic, String message, String content, String type) {

        GjsError gjsError=new GjsError();
        gjsError.setOptDate(DateUtils.getNoTime(""));
        gjsError.setStatus("0");
        gjsError.setType(type);//0表示插入数据
        gjsError.setExchange(financeMic);
        gjsError.setMessage(message);
        gjsError.setContent(content);
        return gjsErrorDao.insert(gjsError);

    }
}
