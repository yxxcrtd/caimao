// @koala-prepend "./lib/jquery-1.11.1.min.js"
// @koala-prepend "./common/utils.js"
// @koala-prepend "./lib/RSA.js"
// @koala-prepend "./common/Data.js"
// @koala-prepend "./lib/formValidate.js"
// @koala-prepend "./v2/actionTip.js"


var captchaElement = "<input id='captcha' type='text' validateType='empty:验证码不能为空 captcha' placeholder='请输入验证码' class='input_text input_verify'/>"
    + "<img id='captchaImg' style='cursor:pointer;' src='/captcha?_='" + new Date().valueOf() + "' width='100' height='40' />";

$(document).ready(function () {
    var usernameObj = $('#username'),
        passwordObj = $('#password'),
        formObj = formValidate.init($('#user_login'));

    function refreshCaptcha() {
        $('#captchaImg').attr('src', '/captcha?_=' + new Date().valueOf());
    }

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

    // 查询登录错误次数
    function updateErroNum() {
        $.get('/user/loginErrorCount?login_name='+$(usernameObj).val(), function (result) {
            if (result.data >= 3) {
                $('#captchaDiv').html(captchaElement).show().addClass('group');
                $('#captchaImg').bind('click', function () {
                    refreshCaptcha();
                });
            }
        });
    }

    $(usernameObj).blur(function() {
        updateErroNum();
    });


    // 密码的回车事件
    $(document).keyup(function(event) {
        var evt;
        evt = event ? event : (window.event ? window.event : null);//兼容IE和FF
        if (evt.keyCode==13){
            $('#loginBtn').trigger('click');
        }
    });

    $('#loginBtn').click(function () {
        var password = $(passwordObj).val();
        var username = $(usernameObj).val();

        if (formObj.validateForm() == false) {
            return false;
        }
        //console.info(password);
        var loginData = {
            'login_name': username,
            'login_pwd': RSAUtils.encryptedString(password)
        };
        if ($('#captchaDiv').html().length > 0) {
            loginData['captcha'] = $('#captcha').val();
        }
        $.post('/user/login',
            loginData,
            function (response) {
                if (response.success) {
                    // 设置cookie 中手机号的值
                    CMUTILS.setCookie('_loginName', username, 7);
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
                    formValidate.showFormTips('loginBtn', response.msg);
                    updateErroNum();
                }
            });
    });

    $(usernameObj).val(CMUTILS.getCookie('_loginName'));
    updateErroNum();
});

