package com.caimao.bana.api.entity.ybk;

import java.io.Serializable;
import java.util.Date;

/**
* YbkHelpDocEntity 邮币卡帮助文档
*
* Created by wangxu@huobi.com on 2015-11-16 17:26:31
*/
public class YbkHelpDocEntity implements Serializable {

    private Integer id;

    private Integer categoryId;

    private String categoryStr;

    private String title;

    private String content;

    private Integer isShow;

    private Date created;

    public String getCategoryStr() {
        return categoryStr;
    }

    public void setCategoryStr(String categoryStr) {
        this.categoryStr = categoryStr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}