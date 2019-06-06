package com.caimao.bana.api.entity.ybk;

import java.io.Serializable;
import java.util.Date;

/**
* YbkNavigationEntity 实例对象
*
* Created by wangxu@huobi.com on 2015-12-07 10:02:47 星期一
*/
public class YbkNavigationEntity implements Serializable {

    /**  */
    private Integer id;

    /** 导航名称 */
    private String name;

    /** 导航地址 */
    private String urls;

    /** 创建时间 */
    private Date created;


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

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}