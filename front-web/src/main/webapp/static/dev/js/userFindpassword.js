/**
 * Created by Administrator on 2015/6/25.
 */

// @koala-prepend "./lib/jquery-1.11.1.min.js"
// @koala-prepend "./common/utils.js"
// @koala-prepend "./common/Data.js"
// @koala-prepend "./lib/RSA.js"
// @koala-prepend "./lib/formValidate.js"
// @koala-prepend "./v2/actionTip.js"

$(document).ready(function () {
    var findPwdMobile = $('#findPwdMobile'),
        smsCheckCode = $('#smsCheckCode'),
        password = $('#password'),
        passwordConfirm = $('#passwordConfirm'),
        sendSmsCheckCode = $('#sendSmsCheckCode'),
        periodSeconds = 0,
        mobileExist = false;

    formValidate.init($('#forget1'));
    formValidate.init($('#forget2'));

    $(findPwdMobile).bind('blur', function () {
        if (formValidate.validateInput(this) == false) {
            return false;
        }
        $.post('/user/mobile/check',
            {
                'mobile': $(findPwdMobile).val().trim()
            },
            function (response) {
                if (response.success) {
                    // 手机号码不存在
                    formValidate.showTips(findPwdMobile, '手机号码不存在');
                    mobileExist = false;
                } else {
                    mobileExist = true;
                }
            });
    });
    $(sendSmsCheckCode).bind('click', function () {
        if (formValidate.validateInput($(findPwdMobile)) == false) {
            return false;
        }
        if (mobileExist == false) return false;
        if (periodSeconds > 0) return false;
        $.post('/sms/loginpwdcode', {'mobile': $(findPwdMobile).val().trim()}, function (response) {
            if (response.success) {
                periodSeconds = 60;
                var disableInterval = window.setInterval(function () {
                    if (periodSeconds > 0) {
                        $(sendSmsCheckCode).html("(" + (--periodSeconds) + ")后重新发送");
                    } else {
                        $(sendSmsCheckCode).html("发送验证码");
                        window.clearInterval(disableInterval);
                    }
                }, 1000);
            } else {
                formValidate.showFormTips('findPwdS1Btn', response.msg);
            }
        });
    });

    $('#findPwdS1Btn').bind('click', function () {
        if (formValidate.validateForm($('#forget1')) == false) {
            return false;
        }
        if (mobileExist == false) return false;
        $.post('/user/checkFindPwdSmsCode',
            {
                'mobile': $(findPwdMobile).val().trim(),
                'check_code': $(smsCheckCode).val().trim()
            },
            function (response) {
                if (response.success) {
                    $('#findPwdStep1Div').hide();
                    $('#findPwdStep2Div').show();
                } else {
                    formValidate.showFormTips('findPwdS1Btn', response.msg);
                }
            });
    });

    $(password).bind('keyup', function () {
        var level = CMUTILS.pwdStrong($(this).val());
        switch (level) {
            case 0:
                $('#levelBar').removeClass().addClass('icons icon_low');
                break;
            case 1:
                $('#levelBar').removeClass().addClass('icons icon_low');
                break;
            case 2:
                $('#levelBar').removeClass().addClass('icons icon_medium');
                break;
            case 3:
                $('#levelBar').removeClass().addClass('icons icon_high');
                break;
            default:
                $('#levelBar').removeClass().addClass('icons icon_low');
        }
    });


    $('#findPwdS2Btn').bind('click', function () {
        if (formValidate.validateForm($('#forget2')) == false) {
            return false;
        }

        $.post('/user/loginpwd/find',
            {
                'mobile': $(findPwdMobile).val().trim(),
                'check_code': $(smsCheckCode).val().trim(),
                'user_pwd': RSAUtils.encryptedString($(password).val())
            },
            function (response) {
                if (response.success) {
                    $('#findPwdStep2Div').hide();
                    $('#findPwdStep3Div').show();
                } else {
                    formValidate.showFormTips('findPwdS2Btn', response.msg);
                }
            });
    });

});



