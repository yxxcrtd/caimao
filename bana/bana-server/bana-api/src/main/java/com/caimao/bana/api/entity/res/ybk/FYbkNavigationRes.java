package com.caimao.bana.api.entity.res.ybk;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 返回的导航列表
 * Created by Administrator on 2015/12/7.
 */
public class FYbkNavigationRes implements Serializable {

    private Integer id;

    /**
     * 导航名称
     */
    private String name;

    /**
     * 导航地址列表
     */
    private List<Map<String, String>> urls;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, String>> getUrls() {
        return urls;
    }

    public void setUrls(List<Map<String, String>> urls) {
        this.urls = urls;
    }
}
