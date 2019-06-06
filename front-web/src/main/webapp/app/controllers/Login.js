/**
 * Controller for login, handler with all business event
 * */
define([
	'app/controllers/Helper',
	'dojo/_base/fx', 
	'dijit/registry', 
	'dojo/dom',
	'dojo/on',
	'dojo/mouse',
	'dojo/keys',
	'dojo/request',
	'dojo/json',
	'app/common/Ajax',
	'dojo/query',
	'dojo/dom-class',
	'dojo/dom-construct',
	'dojo/dom-style',
	'app/common/RSA',
	'app/common/Global',
	'dijit/focus',
	'dojo/cookie',
	'dojo/_base/lang',
    'app/ux/GenericTooltip',
	'dojo/domReady!'
], function(Helper, fx, registry, dom,
		on, mouse, keys, request, json, Ajax, query, domClass, 
		domConstruct, domStyle, RSA, Global, focusUtil, cookie, lang, Tooltip) {
    var loginBtnEl, usernameEl, passwordEl, captchaEl, errorEl, captchaImgEl, captchaLink, usernameError, passwordError, captchaError, loginError, errors, modulus, exponent, config, initData, parseUrl, refreshCaptcha, removeError, addListeners, initRSA, onLogin, isValid, showError,
    loginBtnEl = dom.byId('login_button'),
    usernameEl = dom.byId('username'),
    passwordEl = dom.byId('password'),
    captchaEl = dom.byId('captcha'),
    errorEl = dom.byId('login_error'),
    captchaImgEl = dom.byId('captchaimg'),
    errors = {},
    modulus = '',
    exponent = '';
    var u = Global.getUrlParam('u');
    var config = {
        login: false
    };
    var captchaCtnHTML = '<div class="ui-form-item ui-form-item-validate" style="margin: 0">'+
        '<label for="captcha" class="ui-label"><i class="fa fa-lg">&#xf029;</i></label>'+
        '<input type="text" id="captcha" class="ui-input ui-input-normal" maxlength="6" placeholder="验证码" autocomplete="off">'+
        '</div><div class="am-left-10 ui-form-item-validate">' +
        '<img id="captchaimg" style="cursor:pointer;" src="'+Global.baseUrl+'/captcha?_='+new Date().valueOf()+'"></div>';
    var errorCount = parseInt(Global.getUrlParam('c')) || 0;
    var formEl = dom.byId('loginform');
    var initData = function () {
        fx.fadeOut({ // IE bug opacity
            node: errorEl,
            duration: 0
        }).play();
        var username = cookie('loginname');
        if (username) {
            usernameEl.value = username;
        }
        
        if (u) {
        	cookie('u', u, {path: '/'});
        }        
    };
    var parseUrl = function () {
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
    };
    var refreshCaptcha = function () {
        captchaImgEl.src = Global.baseUrl + '/captcha?_=' + new Date().valueOf();
    };
    var removeError = function (el) {
        domClass.remove(el.parentNode, 'ui-form-item-error');
        delete errors[el.id];
        showError();
    };
    var addListeners = function () {
        on(loginBtnEl, 'click', onLogin);
        var formItems = query('.ui-form-item'),
            formItemInputs = query('.ui-form-item input');
        on(formEl, on.selector('.ui-form-item', mouse.enter), function() {
            domClass.add(this, 'ui-form-item-enter');
        });
        on(formEl, on.selector('.ui-form-item', mouse.leave), function() {
            domClass.remove(this, 'ui-form-item-enter');
        });
        on(formEl, '.ui-form-item input:focusin', function() {
            domClass.add(query('label', this.parentNode)[0], 'ui-form-item-label-focus');
            removeError(this);
            domClass.add(this.parentNode, 'ui-form-item-focus');
        });
        on(formEl, '.ui-form-item input:focusout', function() {
            domClass.remove(query('label', this.parentNode)[0], 'ui-form-item-label-focus');
            domClass.remove(this.parentNode, 'ui-form-item-focus');
        });
        formItemInputs.on('keyup', function (event) {
            var charOrCode = event.charCode || event.keyCode;
            if (charOrCode === keys.ENTER) {
                onLogin();
            } else {
                if (errors.login) {
                    delete errors.login;
                    showError();
                }
            }
        });
    };
    var initRSA = function () {
        Ajax.get(Global.baseUrl + '/sec/rsa', {
        }).then(function (response) {
            if (response.success) {
                modulus = response.data.modulus;
                exponent = response.data.exponent;
            } else {
                //TODO
            }

        });
    };
    var onLogin = function () {

        if (isValid()) {
            var key = RSA.getKeyPair(exponent, '', modulus);
            loginBtnEl.disabled = true;
            var loopId = setInterval(function () {
                if (loginBtnEl.value.length <= 5) {
                    loginBtnEl.value += '.';
                } else {
                    loginBtnEl.value = loginBtnEl.value.slice(0, 3);
                }
            }, 500);
            var captchaVal="";
            if(captchaEl!=null && captchaEl.value!=null){
            	captchaVal=captchaEl.value;
            }
            Ajax.post(Global.baseUrl + '/user/login', {
                'login_name': lang.trim(usernameEl.value),
                'captcha': lang.trim(captchaVal),
                'login_pwd': RSA.encryptedString(key, dom.byId('password').value)
            }).then(function (response) {
                loginBtnEl.disabled = false;
                clearInterval(loopId);
                loginBtnEl.value = loginBtnEl.value.slice(0, 3);
                if (response.success) {
                    // firefox save password prompt
                    // ajax want to show the password prompt, for chrome, set the password field in the form element
                    // for ff, submit the form
                    var loginForm = dom.byId('loginform');
                    loginForm.submit(); // this will pop the prompt for ff
                    cookie('username', dom.byId('username').value, {path: '/'});
                    cookie('loginname', dom.byId('username').value, {path: '/'});
                    var returnUrl = parseUrl().url; // reload to refer url
                    if (returnUrl) {
                        location.href = returnUrl;
                    } else {
                        location.href = Global.baseUrl + '/home/index.htm';
                    }

                } else {
                    errors.login = response.msg;
                    showError();
                    if (captchaEl) {
                        refreshCaptcha();
                        captchaEl.value = '';
                    }
                    passwordEl.value = '';
                    focusUtil.focus(passwordEl);
                    errorCount++;
                    showCaptcha();
                }
            }, function () {
            });
        }
    };

    function showCaptcha() {
        if (errorCount >= 3 && !captchaEl) {
            var captchaDom = domConstruct.toDom(captchaCtnHTML);
            domConstruct.place(captchaDom, dom.byId('captchactn'));
            captchaEl = dom.byId('captcha');
            captchaImgEl = dom.byId('captchaimg');
            on(dom.byId('captchaimg'), 'click', function (event) {
                captchaImgEl.src = Global.baseUrl + '/captcha?_=' + new Date().valueOf();
            });

            on(dom.byId('captcha'), 'keyup', function (event) {
                removeError(this);
                var value = this.value;
                if (value.length === 4) {
                    validateCaptcha(value);
                } else {
                    domConstruct.destroy(query('.captcha-check', dom.byId('captchactn'))[0]);
                }
            });

            on(captchaEl, 'blur', function() {
                validateCaptcha(this.value);
            });
        }
    }

    var isValid = function () {
        var username = lang.trim(dom.byId('username').value),
            password = dom.byId('password').value,
            captcha = captchaEl ? captchaEl.value : '',
            error = false;
        if (!username) {
            domClass.add(usernameEl.parentNode, 'ui-form-item-error');
            errors.username = '请输入手机号';
            error = true;
        }
        else if (!password) {
            domClass.add(passwordEl.parentNode, 'ui-form-item-error');
            errors.password = '请输入密码';
            error = true;
        }
        else if (!captcha && captchaEl) {
            domClass.add(captchaEl.parentNode, 'ui-form-item-error');
            errors.captcha = '请输入验证码';
            error = true;
        }else if(captcha && !validateCaptcha()){
        	domClass.add(captchaEl.parentNode, 'ui-form-item-error');
            errors.captcha = '验证码不正确';
            error = true;
        }
        else if (captchaEl && errors.captcha) {
            error = true;
        }
        if (error) {
            showError();
            errorCount++;
            showCaptcha();
        }
        return !error;
    };
    var showError = function () {
        if (Global.isOwnEmptyObj(errors)) {
            fx.fadeOut({
                node: errorEl,
                duration: 300
            }).play();
            domConstruct.empty(errorEl);
        } else {
            domConstruct.empty(errorEl);
            if (errors.username) {
                usernameError = domConstruct.toDom('<div><i class="ui-icon icon-small-error" title="出错"></i><span class="am-ft-sm">' + errors.username + '</span></div>');
                domConstruct.place(usernameError, errorEl);
            }
            else if (errors.password) {
                passwordError = domConstruct.toDom('<div><i class="ui-icon icon-small-error" title="出错"></i><span class="am-ft-sm">' + errors.password + '</span></div>');
                domConstruct.place(passwordError, errorEl);
            }
            else if (errors.captcha) {
                captchaError = domConstruct.toDom('<div><i class="ui-icon icon-small-error" title="出错"></i><span class="am-ft-sm">' + errors.captcha + '</span></div>');
                domConstruct.place(captchaError, errorEl);
            }
            else if (errors.login) {
                loginError = domConstruct.toDom('<div><i class="ui-icon icon-small-error" title="出错"></i><span class="am-ft-sm">' + errors.login + '</span></div>');
                domConstruct.place(loginError, errorEl);
            }
            fx.fadeIn({
                node: errorEl,
                duration: 300
            }).play();
        }
    };

    function aminImg() {
        setTimeout(function() {
            domClass.add(dom.byId('login-image'), 'login-img-narrow');
        }, 300);

    }

    function validateCaptcha(value) {
        value = value || captchaEl.value;
        var result = true;
        if (!value) {
            showError({'captcha': '请输入验证码'});
            result = false;
        }
        Ajax.post(Global.baseUrl + '/captcha/check', {
            'captcha': value
        }, true).then(function(response) {
            if (response.success) {
                errors.captcha = false;
                showError();
                result = true;
                var captchaCheckEl = query('.captcha-check', dom.byId('captchactn'))[0];
                if(!captchaCheckEl) {
                    domConstruct.place(domConstruct.toDom('<i class="fa captcha-check">&#xf00c;</i>'),dom.byId('captcha'), 'after');
                }
            } else {
                errors.captcha = '验证码不正确';
                showError();
                result = false;
                domConstruct.destroy(query('.captcha-check', dom.byId('captchactn'))[0]);
            }
        });
        return result;
    }

	return {
		init: function() {
			initData();
			addListeners();
			initRSA();
            aminImg();
            showCaptcha();
			Helper.init(config);
		}
	}
});