package com.caimao.zeus.controller.ybk;

import com.caimao.bana.api.entity.req.ybk.FQueryYbkDaxinReq;
import com.caimao.bana.api.entity.req.ybk.FYbkExchangeQueryListReq;
import com.caimao.bana.api.entity.ybk.YbkDaxinEntity;
import com.caimao.bana.api.entity.ybk.YbkExchangeEntity;
import com.caimao.bana.api.service.ybk.IYBKService;
import com.caimao.bana.api.service.ybk.IYbkDaxinService;
import com.caimao.zeus.admin.controller.BaseController;
import com.caimao.zeus.util.PageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 邮币卡打新控制器
 */
@Controller
@RequestMapping("/ybk/daxin")
public class YBKDaxinController extends BaseController {
    @Resource
    private IYbkDaxinService ybkDaxinService;
    @Resource
    private IYBKService ybkService;

    /**
     * 邮币卡打新列表
     *
     * @param exchangeId
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "exchangeId", required = false, defaultValue = "0") Integer exchangeId,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) throws Exception {
        ModelAndView mav = new ModelAndView("ybk/daxin/list");
        FQueryYbkDaxinReq req = new FQueryYbkDaxinReq();
        Integer limit = 20;
        req.setStart((page - 1) * limit);
        req.setLimit(limit);
        if (exchangeId != 0) req.setExchangeId(exchangeId);
        req = this.ybkDaxinService.queryYbkDaxinList(req);
        if (req.getItems() != null) {
            PageUtils pageUtils = new PageUtils(page, limit, req.getTotalCount(), String.format("/ybk/daxin/list?exchangeId=%s&page=", exchangeId));
            String pageHtml = pageUtils.show();
            mav.addObject("pageHtml", pageHtml);
        }
        // 获取交易所列表
        FYbkExchangeQueryListReq exchangeReq = new FYbkExchangeQueryListReq();
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(exchangeReq);
        mav.addObject("exchangeList", exchangeEntityList);

        mav.addObject("list", req);
        mav.addObject("exchangeId", exchangeId);
        return mav;
    }

    /**
     * 打新活动保存
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ModelAndView save(@RequestParam(value = "id", required = false) Long id) throws Exception {
        ModelAndView mav = new ModelAndView("ybk/daxin/save");
        // 获取交易所列表
        FYbkExchangeQueryListReq exchangeReq = new FYbkExchangeQueryListReq();
        List<YbkExchangeEntity> exchangeEntityList = this.ybkService.queryExchangeList(exchangeReq);
        mav.addObject("exchangeList", exchangeEntityList);

        if (id != null && id != 0) {
            YbkDaxinEntity entity = this.ybkDaxinService.selectById(id);
            mav.addObject("articleDetail", entity);
        }
        return mav;
    }

    /**
     * 保存，修改
     *
     * @param id
     * @param exchangeId
     * @param daxinName
     * @param endDatetime
     * @param isShow
     * @param daxinUrl
     * @param sort
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam(value = "id", required = false) Long id,
                             @RequestParam(value = "exchangeId") Integer exchangeId,
                             @RequestParam(value = "daxinName") String daxinName,
                             @RequestParam(value = "endDatetime") String endDatetime,
                             @RequestParam(value = "isShow") String isShow,
                             @RequestParam(value = "daxinUrl") String daxinUrl,
                             @RequestParam(value = "sort") Integer sort,
                             HttpServletRequest request) throws Exception {
        // 获取交易所信息
        YbkExchangeEntity exchangeEntity = this.ybkService.getExchangeById(exchangeId);

        YbkDaxinEntity entity = new YbkDaxinEntity();
        entity.setExchangeId(exchangeId);
        entity.setExchangeName(exchangeEntity.getName());
        entity.setEndDatetime(new SimpleDateFormat("yyyy-MM-dd").parse(endDatetime));
        entity.setIsShow(isShow);
        entity.setDaxinUrl(daxinUrl);
        entity.setDaxinName(daxinName);
        entity.setSort(sort);
        if (id != null && id != 0) {
            entity.setId(id);
            this.ybkDaxinService.update(entity);
        } else {
            entity.setCreated(new Date());
            this.ybkDaxinService.insert(entity);
        }
        return jumpForSuccess(request, "保存、修改成功", "/ybk/daxin/list");
    }

    /**
     * 删除
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public ModelAndView del(@RequestParam(value = "id") Long id, HttpServletRequest request) throws Exception {
        this.ybkDaxinService.deleteById(id);
        return jumpForSuccess(request, "删除成功", "/ybk/daxin/list");
    }

}
