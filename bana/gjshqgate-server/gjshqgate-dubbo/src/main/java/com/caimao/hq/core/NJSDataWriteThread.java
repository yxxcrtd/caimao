package com.caimao.hq.core;

import com.caimao.hq.api.entity.CandleCycle;
import com.caimao.hq.api.entity.NJSSnapshot;
import com.caimao.hq.api.entity.Product;
import com.caimao.hq.api.entity.Snapshot;
import com.caimao.hq.api.service.IGjsErrorService;
import com.caimao.hq.api.service.IHQService;
import com.caimao.hq.api.service.INJSCandleService;
import com.caimao.hq.api.service.INJSSnapshotService;
import com.caimao.hq.utils.GJSProductUtils;
import com.caimao.hq.utils.JRedisUtil;
import com.caimao.hq.utils.MinTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("njsDataWriteThread")
public class NJSDataWriteThread implements Runnable{


	private Logger logger = Logger.getLogger(NJSDataWriteThread.class);

	@Autowired
	private IHQService hqService;
	private   volatile List<Snapshot> data=null;
	@Override
	public void run() {
		if(null!=data&&data.size()>0){

				for(Snapshot snapshot:data){
					try{
						hqService.insertRedisSnapshotHistory(snapshot);
					}catch (Exception ex){
						logger.error("南交所数据解析异常:"+ex.getMessage());
					}
				}
			}
	}

}
