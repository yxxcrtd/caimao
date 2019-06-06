package com.caimao.hq.base.framework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangXu on 2015/5/29.
 */
public class PageConverterFactory
{
    private List<String> converters;
    private Map<String, Class<IPageConverter>> converterMap = new HashMap(3);

    public void setConverters(List<String> converters) {
        this.converters = converters;
        try {
            for (String converter : converters) {
                Class clazz = Class.forName(converter);
                Class tclz = (Class)((java.lang.reflect.ParameterizedType)clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0];
                this.converterMap.put(tclz.getName(), clazz);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IPageConverter createPageConverter(Object dto) throws Exception {
        for (Class superClass = dto.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
        {
            Class pcClz = (Class)this.converterMap.get(superClass.getName());

            if (pcClz != null) {
                IPageConverter pc = (IPageConverter)pcClz.newInstance();
                return pc;
            }
        }
        return null;
    }
}