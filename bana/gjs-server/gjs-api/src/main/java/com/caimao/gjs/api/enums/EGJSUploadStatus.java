package com.caimao.gjs.api.enums;

/**
 * <p>
 *     贵金属身份证上传状态
 * </p>
 * Created by Administrator on 2015/10/10.
 */
public enum EGJSUploadStatus {
    /**0 未上传*/
    UPLOAD_NO(0, "未上传"),
    /**1 已上传*/
    UPLOAD_YES(1, "已上传"),
    /**2 已提交*/
    SUBMIT_YES(2, "已提交"),
    /**3 未通过*/
    ACCESS_FAIL(3, "未通过"),
    /**4 已通过*/
    ACCESS_SUCCESS(4, "已通过");

    private Integer code;
    private String name;

    EGJSUploadStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static EGJSUploadStatus findByCode(Integer code) {
        for (EGJSUploadStatus sCode : EGJSUploadStatus.values()) {
            if (sCode.getCode().equals(code)) {
                return sCode;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
