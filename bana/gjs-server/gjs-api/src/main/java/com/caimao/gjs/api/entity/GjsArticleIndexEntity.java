package com.caimao.gjs.api.entity;

import java.io.Serializable;

/**
* GjsArticleIndexEntity 实例对象
*/
public class GjsArticleIndexEntity implements Serializable {

    /** 主键id */
    private Long id;

    /** 文章标签 */
    private String category;

    /** 文章标题 */
    private String title;

    /** 文章内容 */
    private String content;

    /** 是否显示：0显示、1隐藏 */
    private Integer isShow;

    /** 状态：-1草稿、0正常、1置顶1、2置顶2、3置顶3 ...... */
    private Integer status;

    /** 浏览次数 */
    private Integer view;

    /** 发布者 */
    private String user;

    /** 发布时间 */
    private String pub;

    /** 创建时间 */
    private String created;

    /** 今天，昨天，11月21日，11月20日 ... */
    private String label;

    /** 多长时间之前 */
    private String before;

    private String href;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}