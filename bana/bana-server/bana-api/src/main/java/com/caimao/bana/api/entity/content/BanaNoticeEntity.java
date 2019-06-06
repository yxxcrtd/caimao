package com.caimao.bana.api.entity.content;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统公告通知
 * Created by WangXu on 2015/6/18.
 */
public class BanaNoticeEntity implements Serializable {
    private Long id;
    @NotBlank(message = "公告标题不能为空")
    private String title;
    @NotBlank(message = "内容不能为空")
    private String content;
    private Integer pv;
    private String source;
    private String sourceHref;
    private Date created;
    private Integer listShow;
    private Integer topShow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceHref() {
        return sourceHref;
    }

    public void setSourceHref(String sourceHref) {
        this.sourceHref = sourceHref;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getListShow() {
        return listShow;
    }

    public void setListShow(Integer listShow) {
        this.listShow = listShow;
    }

    public Integer getTopShow() {
        return topShow;
    }

    public void setTopShow(Integer topShow) {
        this.topShow = topShow;
    }
}
