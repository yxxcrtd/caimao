package com.caimao.gjs.api.entity.article;

import java.io.Serializable;

/**
* GjsArticleJin10Entity 实例对象
*
* Created by yangxinxin@huobi.com on 2015-11-05 15:46:30 星期四
*/
public class GjsArticleJin10Entity implements Serializable {

    private Integer id;

    /** 时间ID */
    private String timeId;

    /** 1：一般新闻或普通新闻；2：重要新闻；3：一般数据；4：重要数据； */
    private Integer category;

    /** 时间 */
    private String time;

    /** 内容 */
    private String content;

    /** 1,2,3,4,5 */
    private Integer star;

    /** 实际值 */
    private String infact;

    /** 前值 */
    private String beforeValue;

    /** 预期 */
    private String expect;

    /** 1：利空；2：利多；3：影响较小；TODO */
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

    public String getBeforeValue() {
        return beforeValue;
    }

    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
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