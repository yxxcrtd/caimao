package com.caimao.bana.api.entity.guji;

import java.io.Serializable;

/**
* GujiConfigEntity 实例对象
*/
public class GujiConfigEntity implements Serializable {

    /** 主键id */
    private Long id;

    /** 配置类型（boolean，int，string，。。。）*/
    private String type;

    /** 配置项的key */
    private String key;

    /** 配置项的value */
    private String value;

    /** 注释 */
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}