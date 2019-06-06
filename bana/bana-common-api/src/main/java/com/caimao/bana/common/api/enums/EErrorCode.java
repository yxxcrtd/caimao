package com.caimao.bana.common.api.enums;

/**
 * <p>
 *     错误代码
 * </p>
 * Created by Administrator on 2015/10/10.
 */
public enum EErrorCode {
    ERROR_CODE_999999(999999, "服务器异常"),
    ERROR_CODE_100001(100001, "财猫账户未登录"),
    ERROR_CODE_100002(100002, "财猫账户不存在"),
    ERROR_CODE_100003(100003, "您的角色为理财人，不允许登录！"),
    ERROR_CODE_100004(100004, "手机号码格式错误"),
    ERROR_CODE_100005(100005, "短信类型不正确"),
    ERROR_CODE_100006(100006, "身份证号码格式不正确"),
    ERROR_CODE_100007(100007, "银行卡号码格式不正确"),
    ERROR_CODE_300001(300001, "交易所未登录"),
    ERROR_CODE_300002(300002, "交易所不存在"),
    ERROR_CODE_300003(300003, "交易所未开户"),
    ERROR_CODE_300004(300004, "交易所注销失败"),
    ERROR_CODE_300005(300005, "银行编号不存在"),
    ERROR_CODE_300006(300006, "开户失败"),
    ERROR_CODE_300007(300007, "获取交易员编号错误"),
    ERROR_CODE_300008(300008, "查询开户信息失败"),
    ERROR_CODE_300009(300009, "上金所上传图片失败"),
    ERROR_CODE_300010(300010, "上金所查询审核状态失败"),
    ERROR_CODE_300011(300011, "您已完成银商绑定，暂不支持更换银行卡"),
    ERROR_CODE_300012(300012, "上金所绑定账号失败"),
    ERROR_CODE_300013(300013, "上金所账号信息查询失败"),
    ERROR_CODE_300014(300014, "止盈价和止损价必须设置其一"),
    ERROR_CODE_300015(300015, "输入密码错误超限，请1小时后再次尝试"),
    ERROR_CODE_300016(300016, "目前当前非转账时间，转账时间周一到周五09：00--15:30"),
    ERROR_CODE_300017(300017, "目前当前非转账时间，转账时间：周一到周五08:50-16:00 晚20:00--02:30"),
    ERROR_CODE_300018(300018, "目前当前非转账时间，转账时间周一到周五09：00--22:00"),
    ERROR_CODE_300019(300019, "开户银行编号不存在"),;

    private Integer code;
    private String message;

    EErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static EErrorCode findByCode(Integer code) {
        for (EErrorCode sCode : EErrorCode.values()) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
