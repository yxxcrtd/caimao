package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.base.validated.Phone;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户找回资金密码的请求对象
 * Created by WangXu on 2015/5/27.
 */
public class FUserFindTradePwdReq implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "手机号码不能为空")
    @Phone(message = "手机号码格式错误")
    private String mobile;

    @NotEmpty(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码格式错误")
    private String checkCode;

    @NotEmpty(message = "问题不能为空")
    private String questionId;

    @NotEmpty(message = "答案不能为空")
    private String answerResult;

    @NotEmpty(message = "资金密码不能为空")
    private String userTradePwd;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswerResult() {
        return answerResult;
    }

    public void setAnswerResult(String answerResult) {
        this.answerResult = answerResult;
    }

    public String getUserTradePwd() {
        return userTradePwd;
    }

    public void setUserTradePwd(String userTradePwd) {
        this.userTradePwd = userTradePwd;
    }
}
