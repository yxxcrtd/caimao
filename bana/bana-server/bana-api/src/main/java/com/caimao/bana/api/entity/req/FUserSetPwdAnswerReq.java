package com.caimao.bana.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 设置密保问题
 * Created by WangXu on 2015/5/27.
 */
public class FUserSetPwdAnswerReq implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotEmpty(message = "问题1不能为空")
    private String qestion1;
    @NotEmpty(message = "答案1不能为空")
    private String answer1;
    @NotEmpty(message = "问题2不能为空")
    private String qestion2;
    @NotEmpty(message = "答案2不能为空")
    private String answer2;
    @NotEmpty(message = "问题3不能为空")
    private String qestion3;
    @NotEmpty(message = "答案3不能为空")
    private String answer3;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getQestion1() {
        return qestion1;
    }

    public void setQestion1(String qestion1) {
        this.qestion1 = qestion1;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getQestion2() {
        return qestion2;
    }

    public void setQestion2(String qestion2) {
        this.qestion2 = qestion2;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getQestion3() {
        return qestion3;
    }

    public void setQestion3(String qestion3) {
        this.qestion3 = qestion3;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }
}
