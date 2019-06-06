package com.caimao.gjs.api.entity.req;

import com.caimao.gjs.api.entity.article.GjsArticleJin10Entity;
import com.caimao.bana.common.api.entity.QueryBase;

import java.io.Serializable;

/**
* GjsArticleJin10Entity 查询请求对象
*
* Created by yangxinxin@huobi.com on 2015-11-06 10:20:22 星期五
*/
public class FQueryGjsArticleJin10Req extends QueryBase<GjsArticleJin10Entity> implements Serializable {
    // TODO 这里的字段需要根据业务需求删除，当前提供的是全字段
    private Integer id;
    private String timeId;
    private Integer category;
    private String time;
    private String content;
    private Integer star;
    private String infact;
    private String before;
    private String expect;
    private Integer result;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getInfact() {
        return infact;
    }

    public void setInfact(String infact) {
        this.infact = infact;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

}