// @koala-prepend "./lib/jquery-1.11.1.min.js"
// @koala-prepend "./common/utils.js"
// @koala-prepend "./lib/RSA.js"
// @koala-prepend "./common/Data.js"
// @koala-prepend "./ux/protocol.js"
// @koala-prepend "./lib/formValidate.js"
// @koala-prepend "./v2/actionTip.js"


$(document).ready(function () {
    var mobileExist = true,
        regMobile = $('#registerMobile'),
        smsCheckCode = $('#smsCheckCode'),
        password = $('#password'),
        passwordConfirm = $('#passwordConfirm'),
        levelBar = $('#levelBar'),
        periodSeconds = 0,
        formObj1 = formValidate.init($('#regiester1')),
        formObj2 = formValidate.init($('#regiester2'));

    function parseUrl() {
        var paramIndex = location.href.indexOf('?'),
            paramsObj = {};
        if (paramIndex > 0) {
            var params = location.href.slice(paramIndex + 1).split('&');
            for (var i = 0, len = params.length; i < len; i++) {
                var dict = params[i].split('=');
                paramsObj[dict[0]] = dict[1];
            }
        }
        return paramsObj;
    }

    // 检查手机号是否存在
    function checkMobile(mobile) {
        // 验证手机号
        if (formValidate.validateInput(regMobile) == false) {
            return false;
        }
        $.post('/user/mobile/check',
            {'mobile': $('#registerMobile').val().trim()},
            function (response) {
                if (response.success) {
                    // 可以注册
                    mobileExist = false;
                } else {
                    mobileExist = true;
                    formValidate.showTips(regMobile, response.msg);
                }
            });
    }
    // 手机号码离开的事件
    $(regMobile).bind('blur', function () {
        // 验证手机号
        if (formValidate.validateInput(this) == false) {
            return false;
        }
        checkMobile($(this).val());
    });

    $('#sendSmsCheckCode').bind('click', function () {
        checkMobile($(regMobile).val());
        if (formValidate.validateInput($(regMobile)) == false) {
            return false;
        }
        if (mobileExist == true) return false;
        if (periodSeconds > 0) return false;
        $.post('/sms/registercode', {'mobile': $(regMobile).val().trim()}, function (response) {
            if (response.success) {
                periodSeconds = 60;
                var disableInterval = window.setInterval(function () {
                    if (periodSeconds > 0) {
                        $('#sendSmsCheckCode').html("(" + (--periodSeconds) + ")后重新发送");
                    } else {
                        window.clearInterval(disableInterval);
                        $('#sendSmsCheckCode').html("发送验证码");
                    }
                }, 1000);
            } else {
                formValidate.showTips(smsCheckCode, response.msg);
            }
        });
    });

    // 下一步的按钮
    $('#registerS1Btn').bind('click', function () {
        // 验证表单
        if (formObj1.validateForm($('#regiester1')) == false) {
            return false;
        }
        if (mobileExist == true) return false;
        $.post('/user/checkRegisterSmsCode',
            {
                'login_name': $(regMobile).val().trim(),
                'sms_code': $(smsCheckCode).val().trim()
            },
            function (response) {
                if (response.success) {
                    $('#registerStep1Div').hide();
                    $('#registerStep2Div').show();
                    $('#echoRegisterMobile').html("您的手机号码：<strong>" + response.data + "</strong>");
                } else {
                    formValidate.showFormTips('registerS1Btn', response.msg);
                }
            });
    });

    $(password).bind('keyup', function () {
        $('#levelBarParent').css('display', 'block');
        var level = CMUTILS.pwdStrong($('#password').val());
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

    // 注册协议
    $('#regiesterProtocol').click(function() {
        Protocol.showRegiesterProtocol();
    });

    $('#registerS2Btn').bind('click', function () {
        if (formObj2.validateForm($('#regiester2')) == false) {
            return false;
        }
        if (!$('#protocol').is(":checked")) {
            formValidate.showFormTips('registerS2Btn', '请阅读并同意协议');
            return;
        }
        var registData = {
            'login_name': $(regMobile).val().trim(),
            'login_pwd': RSAUtils.encryptedString($(password).val()),
            'sms_code': $(smsCheckCode).val().trim(),
            'ref_phone': $('#ref_phone').val()
        };
        $.post('/user/regist',
            registData,
            function (registResponse) {
                if (registResponse.success) {
                    var loginData = {
                        'login_name': $(regMobile).val().trim(),
                        'login_pwd': RSAUtils.encryptedString($(password).val())
                    };
                    $.post('/user/login',
                        loginData,
                        function (loginResponse) {
                            if (loginResponse.success) {
                                var returnUrl = parseUrl()["return"]; // reload to refer url
                                var app = parseUrl()["app"];
                                var jumpUrl = homeUrl;
                                if (typeof returnUrl != "undefined") returnUrl = decodeURIComponent(returnUrl);
                                if (app == "ybk") {
                                    jumpUrl = ybkUrl + (typeof returnUrl == "undefined" ? "" : returnUrl);
                                    window.location.href = jumpUrl;
                                } else if (app == "jin") {
                                    jumpUrl =  jinUrl + (typeof returnUrl == "undefined" ? "" : returnUrl);
                                    window.location.href = jumpUrl;
                                } else {
                                    location.href = jumpUrl;
                                }
                            } else {
                                formValidate.showFormTips('registerS2Btn', loginResponse.msg);
                            }
                        });
                } else {
                    formValidate.showFormTips('registerS2Btn', registResponse.msg);
                }
            });
    });
});





