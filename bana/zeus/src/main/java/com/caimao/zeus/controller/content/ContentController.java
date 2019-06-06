package com.caimao.zeus.controller.content;

import com.caimao.bana.api.entity.zeus.ZeusAlarmPeopleEntity;
import com.caimao.bana.api.entity.zeus.ZeusProhibitStockEntity;
import com.caimao.bana.api.service.IOperationService;
import com.caimao.bana.api.service.content.IContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 网站内容相关的东东
 * Created by Administrator on 2015/8/17.
 */
@Controller
@RequestMapping("/content")
public class ContentController {
    private Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Resource
    private IContentService contentService;

    @Resource
    private IOperationService operationService;


    /**
     * 显示所有的禁买股票信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/prohibit_stock_list", method = RequestMethod.GET)
    public ModelAndView prohibitStockList() throws Exception {
        ModelAndView mav = new ModelAndView("/content/content/prohibit_stock");
        mav.addObject("list", this.contentService.listProhibitStock());
        return mav;
    }

    /**
     * 保存禁买的股票信息
     * @param name  股票名称
     * @param code  股票代码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/prohibit_stock_save", method = RequestMethod.POST)
    public RedirectView prohibitStockSave(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "code") String code
    ) throws Exception {
        ZeusProhibitStockEntity entity = new ZeusProhibitStockEntity();
        entity.setName(name);
        entity.setCode(code);
        this.contentService.saveProhibitStock(entity);
        return new RedirectView("/content/prohibit_stock_list");
    }

    /**
     * 删除以保存的禁买股票信息
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/prohibit_stock_del", method = RequestMethod.GET)
    public RedirectView prohibitStockDel(@RequestParam(value = "id") Integer id) throws Exception {
        this.contentService.delProhibitStock(id);
        return new RedirectView("/content/prohibit_stock_list");
    }

    /**
     * 报警通知联系人列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/alarm_people_list", method = RequestMethod.GET)
    public ModelAndView alarmPeopleList() throws Exception {
        ModelAndView mav = new ModelAndView("/content/content/alarm_people");
        List<ZeusAlarmPeopleEntity> list = this.operationService.listAlarmPeople();
        mav.addObject("list", list);
        return mav;
    }

    /**
     * 保存报警联系人
     * @param key
     * @param name
     * @param emails
     * @param smss
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/alarm_people_save", method = RequestMethod.POST)
    public RedirectView alarmPeopleSave(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "emails") String emails,
            @RequestParam(value = "smss") String smss
    ) throws Exception {
        ZeusAlarmPeopleEntity entity = new ZeusAlarmPeopleEntity();
        entity.setKey(key);
        entity.setName(name);
        entity.setEmails(emails);
        entity.setSmss(smss);
        this.operationService.saveAlarmPeople(entity);
        return new RedirectView("/content/alarm_people_list");
    }

    /**
     * 删除报警联系人
     * @param key
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/alarm_people_del", method = RequestMethod.GET)
    public RedirectView alarmPeopleDel(@RequestParam(value = "key") String key) throws Exception {
        this.operationService.delAlarmPeople(key);
        return new RedirectView("/content/alarm_people_list");
    }

    /**
     * 更新报警联系人
     * @param key
     * @param name
     * @param emails
     * @param smss
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/alarm_people_update", method = RequestMethod.POST)
    public RedirectView alarmPeopleUpdate(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "emails") String emails,
            @RequestParam(value = "smss") String smss
    ) throws Exception {
        ZeusAlarmPeopleEntity entity = new ZeusAlarmPeopleEntity();
        entity.setKey(key);
        entity.setName(name);
        entity.setEmails(emails);
        entity.setSmss(smss);
        this.operationService.updateAlarmPeople(entity);
        return new RedirectView("/content/alarm_people_list");
    }
}
