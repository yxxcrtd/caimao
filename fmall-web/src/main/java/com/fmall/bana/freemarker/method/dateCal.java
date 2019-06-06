package com.fmall.bana.freemarker.method;

import com.caimao.bana.api.utils.DateUtil;
import freemarker.template.SimpleDate;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.Date;
import java.util.List;

/**
 * 计算日期加减
 */
public class dateCal implements TemplateMethodModelEx {
    @Override
    public Date exec(List args) throws TemplateModelException {
        if (args.size() != 2) return null;
        //定义两个参数 date num
        SimpleDate sDate = (SimpleDate) args.get(0);
        Date date = sDate.getAsDate();
        Integer num = Integer.parseInt(args.get(1).toString());
        return DateUtil.addDays(date, num);
    }
}