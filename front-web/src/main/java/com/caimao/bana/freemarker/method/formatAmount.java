package com.caimao.bana.freemarker.method;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

public class formatAmount implements TemplateMethodModelEx {
    @Override
    public Object exec(List args) throws TemplateModelException {
        if(args.size() != 4) return null;
        //定义四个参数 value, places, unit, down
        BigDecimal amount = new BigDecimal(0);
        if (args.get(0) != null) {
            amount = new BigDecimal(args.get(0).toString());
        }
        Integer places = Integer.parseInt(args.get(1).toString());
        String unit = args.get(2).toString();
        Integer down = Integer.parseInt(args.get(3).toString());
        //进位还是舍位
        Integer numMode = down == 1?BigDecimal.ROUND_DOWN:BigDecimal.ROUND_UP;
        //是否是万模式
        if(unit.equals("w")){
            amount = amount.divide(new BigDecimal("1000000"), places, numMode);
            NumberFormat nf = new DecimalFormat("#,###.####");
            return nf.format(amount) + "万";
        }else{
            amount = amount.divide(new BigDecimal("100"), places, numMode);
            NumberFormat nf = new DecimalFormat("#,###.##");
            return nf.format(amount);
        }
    }
}