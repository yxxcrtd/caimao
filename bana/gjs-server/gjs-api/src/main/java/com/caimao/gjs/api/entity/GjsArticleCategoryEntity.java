package com.caimao.gjs.api.entity;

import java.io.Serializable;

/**
* GjsArticleCategoryEntity 实例对象
*
* Created by yangxinxin@huobi.com on 2016-01-07 16:02:03 星期四
*/
public class GjsArticleCategoryEntity implements Serializable {

    /**  */
    private Long id;

    /**  */
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}