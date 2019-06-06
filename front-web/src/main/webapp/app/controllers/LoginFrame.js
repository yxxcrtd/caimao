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
	'dojo/domReady!'
], function(Helper, fx, registry, dom,
		on, mouse, keys, request, json, Ajax, query, domClass, domConstruct, domStyle, RSA, Global, focusUtil, cookie) {
	
	var config = {'login': false};
	var loginBtnEl = dom.byId('login_button'),
		usernameEl = dom.byId('username'),
		passwordEl = dom.byId('password'),
		captchaEl = dom.byId('captcha'),
		errorEl = dom.byId('login_error'),
		captchaImgEl = dom.byId('captchaimg'),
		captchaLink = dom.byId('captchaimg'),
		usernameError,
		passwordError,
		captchaError,
		loginError,
		errors = {},
		modulus = '',
		exponent = '',
		forgetPwdLink = dom.byId('forgetpwdlink'),
		
		initData = function() {
		},
		
		parseUrl = function() {
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
		},
		
		refreshCaptcha = function() {
			captchaImgEl.src = Global.baseUrl + '/code?_=' + new Date().valueOf();
		},
		
		// if field is focused, the related error should be removed
		removeError = function(el) {
			domClass.remove(el.parentNode, 'ui-form-item-error');
			delete errors[el.id];
			showError();
		},
		// init event
		addListeners = function() {
			on(loginBtnEl, 'click', onLogin);
			
			var formItems = query('.ui-form-item'),
				formItemInputs = query('.ui-form-item input');
			formItems.on(mouse.enter, function(event) {
				domClass.add(this, 'ui-form-item-enter');
			});
			formItems.on(mouse.leave, function(event) {
				domClass.remove(this, 'ui-form-item-enter');
			});
			formItemInputs.on('focus', function(event) {
				removeError(this);
				domClass.add(this.parentNode, 'ui-form-item-focus');
			});
			formItemInputs.on('blur', function(event) {
				domClass.remove(this.parentNode, 'ui-form-item-focus');
			});
			formItemInputs.on('keyup', function(event) {
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
			on(captchaLink, 'click', function(event) {
				captchaImgEl.src = Global.baseUrl + '/code?_=' + new Date().valueOf();
				captchaEl.focus();
			});
			
			on(captchaEl, 'keyup', function(event) {
				removeError(this);
				var value = this.value;
				if (value.length === 4) {
					Ajax.post(Global.baseUrl + '/captcha/check', {
						'captcha': value
						
					}).then(function(response) {
						if (response.success) {
						} else {
							errors.captcha = '验证码不正确';
							showError();
						}
					});
				}
			});
			
			on(usernameEl, 'blur', function() {
				value = usernameEl.value;
				if (!/^1[34578]\d{9}$/.test(value)) {
					errors.username = '账户是手机号码';
					showError();
				}
			});
		},
		
		initRSA = function() {
			Ajax.post(Global.baseUrl + '/sec/rsa/get', {
			}).then(function(response) {
				if (response.success) {
					modulus = response.data.modulus;
					exponent = response.data.exponent;
				} else {
					//TODO
				}
				
			});
		},
		
		// response field
		// return_code: 1 system error; 0 success; -1 business error
		// error_no: error number
		// error_info: error message
		// data_sets: request success data
		onLogin = function() {
			
			if (isValid()) {
				var key = RSA.getKeyPair(exponent, '', modulus);
				loginBtnEl.disabled = true;
				var loopId = setInterval(function() {
					if (loginBtnEl.value.length <= 7) {
						loginBtnEl.value += '.';
					} else {
						loginBtnEl.value = loginBtnEl.value.slice(0, 5);
					}
				}, 500);
				
				Ajax.post(Global.baseUrl + '/user/login', {
					'company': '1',
					'loginname': usernameEl.value,
					'loginPwd': RSA.encryptedString(key, dom.byId('password').value),
					'captcha': dom.byId('captcha').value
				}).then(function(response) {
					loginBtnEl.disabled = false;
					clearInterval(loopId);
					
					if (response.success) {
						// firefox save password prompt
						// ajax want to show the password prompt, for chrome, set the password field in the form element
						// for ff, submit the form
						var loginForm = dom.byId('loginform');
						loginForm.submit(); // this will pop the prompt for ff
						cookie('username', dom.byId('username').value, {path: '/'});
						cookie('loginname', dom.byId('username').value, {path: '/'});
						var returnUrl = parseUrl().url; // reload to refer url
						var loc = window.parent.location || window.location;
						if (returnUrl) {
							loc.href = returnUrl;
						} else {
							loc.href = Global.baseUrl + '/home/index.htm';
						}
						
					} else {
						loginBtnEl.value = '立即登录';
						errors.login = response.exceptions[0]['info'];
						showError();
						var type = response.exceptions[0]['no'];
						if (type == 83021201) {
							// reset captcha, password, focus password
							refreshCaptcha();
							passwordEl.value = '';
							captchaEl.value = '';
							focusUtil.focus(passwordEl);
						}
						else if (type == 830212) {
							// reset captcha, focus captcha
							captchaEl.value = '';
							focusUtil.focus(captchaEl);
						}
						
					}
				}, function() {
				});
			}
		},
		
		// validate before commit, not null validation
		isValid = function() {
			var username = dom.byId('username').value,
				password = dom.byId('password').value,
				captcha = dom.byId('captcha').value,
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
			else if (!captcha) {
				domClass.add(captchaEl.parentNode, 'ui-form-item-error');
				errors.captcha = '请输入验证码';
				error = true;
			}
			if (error) {
				showError();
			}
			return !error;
		},
		
		// some field not validated will show the error
		showError = function() {
			if (Global.isOwnEmptyObj(errors)) {
				errorEl.style.display = 'none';
				domConstruct.empty(errorEl);
			} else {
				domConstruct.empty(errorEl);
				if (errors.username) {
					usernameError = domConstruct.toDom('<div><i class="ui-icon icon-small-error" title="出错"></i><span class="am-ft-sm">'+errors.username+'</span></div>');
					domConstruct.place(usernameError, errorEl);
				}
				else if (errors.password) {
					passwordError = domConstruct.toDom('<div><i class="ui-icon icon-small-error" title="出错"></i><span class="am-ft-sm">'+errors.password+'</span></div>');
					domConstruct.place(passwordError, errorEl);
				}
				else if (errors.captcha) {
					captchaError = domConstruct.toDom('<div><i class="ui-icon icon-small-error" title="出错"></i><span class="am-ft-sm">'+errors.captcha+'</span></div>');
					domConstruct.place(captchaError, errorEl);
				}
				else if (errors.login) {
					loginError = domConstruct.toDom('<div><i class="ui-icon icon-small-error" title="出错"></i><span class="am-ft-sm">'+errors.login+'</span></div>');
					domConstruct.place(loginError, errorEl);
				}
				errorEl.style.display = 'block';
				fx.fadeIn({
					node: errorEl,
					duration: 300
				}).play();
			}
		};
	return {
		init: function() {
			initData();
			addListeners();
			initRSA();
		}
	}
});