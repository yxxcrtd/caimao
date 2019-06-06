package com.hsnet.pz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hsnet.pz.ao.information.InformationAO;
import com.hsnet.pz.biz.base.dto.req.F830917Req;

/**
 * 行业资讯、公告  Contro
 * @author zhanggl10620
 *
 */
@Controller
@RequestMapping(value = "/info")
public class InfomationController {
	
	@Autowired
	private InformationAO informationAO;
	
	
	/**
	 * 查询资讯标题列表
	 * @param infoChannel  ：目前包括1:公告、2:行业资讯
	 * @return
	 */
	@RequestMapping(value = "/titlePage", method = RequestMethod.GET)
	@ResponseBody
	public F830917Req queryInfoTitlePage( @RequestParam("start") int start, @RequestParam("limit") int limit,@RequestParam(value = "infoChannel",required=false)String infoChannel ){
		return informationAO.queryInfoTitlePage(start,limit,infoChannel);
	}
	
	/**
	 *  资讯内容列表
	 * @param id  :流水号
	 * @param infoChannel  ：目前包括1:公告、2:行业资讯
	 * @return
	 */
	@RequestMapping(value = "/contentPage", method = RequestMethod.GET)
	@ResponseBody
	public F830917Req queryInfoContentPage(@RequestParam("id") Long id,@RequestParam(value = "infoChannel",required = false)String infoChannel){
		return informationAO.queryInfoContentPage(id,infoChannel);
	}
}
